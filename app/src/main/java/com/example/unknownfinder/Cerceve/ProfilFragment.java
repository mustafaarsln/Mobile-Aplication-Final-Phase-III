// All the  fragment.java files and fragment xml files written by Abdülkadir Mustafa ARSLAN.

package com.example.unknownfinder.Cerceve;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.unknownfinder.AnaSayfaActivity;
import com.example.unknownfinder.BaslangicActivity;
import com.example.unknownfinder.EditActivity;
import com.example.unknownfinder.GirisActivity;
import com.example.unknownfinder.Kullanici;
import com.example.unknownfinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
  */
public class ProfilFragment extends Fragment {

    TextView txt_kullaniciemail, txt_kullaniciname, txt_kullanicisurname, txt_kullaniciphone;
    Button btn_profili_Duzenle, btn_Logout;

    FirebaseUser mevcutKullanici;
    String profilId;
    FirebaseAuth firebaseAuth;




    public ProfilFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profilId = prefs.getString("profileid", "none");

        txt_kullaniciemail=view.findViewById(R.id.txt_kullaniciemail_profilCerceve);
        txt_kullaniciname=view.findViewById(R.id.txt_kullaniciname_profilCerceve);
        txt_kullanicisurname=view.findViewById(R.id.txt_kullanicisurname_profilCerceve);
        txt_kullaniciphone=view.findViewById(R.id.txt_kullaniciphonee_profilCerceve);

        btn_profili_Duzenle=view.findViewById(R.id.editbutton);
        btn_Logout=view.findViewById(R.id.logoutbutton);


        firebaseAuth = FirebaseAuth.getInstance();


        kullaniciBilgisi();

        btn_profili_Duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditActivity.class));
            }
        });

        btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(getContext(), BaslangicActivity.class));



            }
        });


        return view;
    }

    private void kullaniciBilgisi()
    {
        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance("https://unknownfinder1-77721-default-rtdb.europe-west1.firebasedatabase.app").getReference("Kullanicilar")
                .child(profilId);

        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getContext() == null)
                {
                    return;
                }
                Kullanici kullanici= dataSnapshot.getValue(Kullanici.class);
                txt_kullaniciemail.setText(kullanici.getK_email());
                txt_kullaniciname.setText(kullanici.getK_name());
                txt_kullanicisurname.setText(kullanici.getK_surname());
                txt_kullaniciphone.setText(kullanici.getK_phone());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}