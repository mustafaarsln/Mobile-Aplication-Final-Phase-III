//This adapter written by Abdülkadir Mustafa Arslan
package com.example.unknownfinder.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unknownfinder.Cerceve.ProfilFragment;
import com.example.unknownfinder.Kullanici;
import com.example.unknownfinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class KullaniciAdapter extends RecyclerView.Adapter<KullaniciAdapter.ViewHolder> {

    private Context mContext;
    private List<Kullanici> mKullanicilar;
    private FirebaseUser firebaseKullanici;

    public KullaniciAdapter(Context mContext, List<Kullanici> mKullanicilar) {
        this.mContext = mContext;
        this.mKullanicilar = mKullanicilar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.kullanici_ogesi, viewGroup, false);

        return new  KullaniciAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        firebaseKullanici = FirebaseAuth.getInstance().getCurrentUser();
        final Kullanici kullanici = mKullanicilar.get(i);

        viewHolder.btn_favorite.setVisibility(View.VISIBLE);

        viewHolder.kullaniciname.setText(kullanici.getK_name());
        viewHolder.kullanicisurname.setText(kullanici.getK_surname());
        //tanımlanan değerleri set etme aynı şekilde
        viewHolder.kullaniciphone.setText(kullanici.getK_phone());
        viewHolder.kullaniciemail.setText(kullanici.getK_email());
        takipEdiliyor(kullanici.getK_id(), viewHolder.btn_favorite);

        if (kullanici.getK_id().equals(firebaseKullanici.getUid())){

            viewHolder.btn_favorite.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", kullanici.getK_id());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().
                        replace(R.id.cerceve_kapsayici, new ProfilFragment()).commit();

            }
        });

        viewHolder.btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.btn_favorite.getText().toString().equals("Favorite")){
                    //Biz favoriye aldığımız zaman karşı tarafı bizim fav listesine alması için
                    FirebaseDatabase.getInstance("https://unknownfinder1-77721-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Takip").child(firebaseKullanici.getUid())
                            .child("TakipEdilenler").child(kullanici.getK_id()).setValue(true);

                    FirebaseDatabase.getInstance("https://unknownfinder1-77721-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Takip").child(kullanici.getK_id())
                            .child("takipciler").child(firebaseKullanici.getUid()).setValue(true);
                }

                else{

                    FirebaseDatabase.getInstance("https://unknownfinder1-77721-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Takip").child(firebaseKullanici.getUid())
                            .child("TakipEdilenler").child(kullanici.getK_id()).removeValue();

                    FirebaseDatabase.getInstance("https://unknownfinder1-77721-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Takip").child(kullanici.getK_id())
                            .child("takipciler").child(firebaseKullanici.getUid()).removeValue();

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mKullanicilar.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView kullaniciname;
        public TextView kullanicisurname;
        //sonradan email ve telefon için eklenen kodlar
        public TextView kullaniciphone;
        public TextView kullaniciemail;
        public Button btn_favorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            kullaniciname=itemView.findViewById(R.id.txt_name_Oge);
            kullanicisurname=itemView.findViewById(R.id.txt_surname_Oge);
            //public de olusturulan telefon ve maili tanımlama
            kullaniciphone=itemView.findViewById(R.id.txt_phone_Oge);
            kullaniciemail=itemView.findViewById(R.id.txt_email_Oge);
            btn_favorite=itemView.findViewById(R.id.btn_favorite_Oge);
        }
    }

    private void takipEdiliyor(String kullaniciId, Button button){

        DatabaseReference takipYolu = FirebaseDatabase.getInstance("https://unknownfinder1-77721-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Takip")
                .child(firebaseKullanici.getUid()).child("TakipEdilenler");

        takipYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(kullaniciId).exists()){
                    button.setText("Favorited");

                }
                else {
                    button.setText("Favorite");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
