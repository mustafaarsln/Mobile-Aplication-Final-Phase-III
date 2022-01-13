// All the  fragment.java files and fragment xml files written by Abdülkadir Mustafa ARSLAN.
package com.example.unknownfinder.Cerceve;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.unknownfinder.Adapter.KullaniciAdapter;
import com.example.unknownfinder.Kullanici;
import com.example.unknownfinder.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AramaFragment extends Fragment {

    private RecyclerView recyclerView;
    private KullaniciAdapter kullaniciAdapter;
    private List<Kullanici> mKullaniciler;

    EditText arama_bar;
    EditText arama2_bar;

    public AramaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_arama, container, false);

        recyclerView=view.findViewById(R.id.recyler_view_Arama);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        arama_bar=view.findViewById(R.id.edt_arama_bar);
        arama2_bar=view.findViewById(R.id.edt_arama_phone_bar);

        mKullaniciler=new ArrayList<>();
        kullaniciAdapter=new KullaniciAdapter(getContext(),mKullaniciler);

        recyclerView.setAdapter(kullaniciAdapter);

        //admin yazılmıyorsa hepsini oku
        if (arama_bar.getText().toString().equals("admin")){
            kullanicileriOku();
        }
        //birşeyler yazdıkça birşeyler önerecek

        arama2_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                kullaniciAra2(s.toString().toLowerCase());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });




        arama_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                kullaniciAra(s.toString().toLowerCase());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view;
    }
    //Burada verileri kullanıcı sınıfından alıp sıralamaı k_name göre yapıyor.
    private void kullaniciAra(String s){
        Query sorgu = FirebaseDatabase.getInstance("https://unknownfinder1-77721-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Kullanicilar").orderByChild("k_email")
                .startAt(s)
                .endAt(s+"\uf8ff");


        sorgu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                mKullaniciler.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren())
                {
                    Kullanici kullanici = snapshot.getValue(Kullanici.class);
                    mKullaniciler.add(kullanici);
                    if (arama_bar.getText().toString().equals("")){
                        mKullaniciler.clear();
                    }
                }
                //veriler her güncellendiğinde listenin de güncellenmesi için...
                kullaniciAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void kullaniciAra2(String s){
        Query sorgu2 = FirebaseDatabase.getInstance("https://unknownfinder1-77721-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Kullanicilar").orderByChild("k_phone")
                .startAt(s)
                .endAt(s+"\uf8ff");


        sorgu2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                mKullaniciler.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren())
                {
                    Kullanici kullanici = snapshot.getValue(Kullanici.class);
                    mKullaniciler.add(kullanici);
                    if (arama2_bar.getText().toString().equals("")){
                        mKullaniciler.clear();
                    }
                }
                //veriler her güncellendiğinde listenin de güncellenmesi için...
                kullaniciAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }







    private void kullanicileriOku()
    {
        DatabaseReference kullanicilerYolu = FirebaseDatabase.getInstance
                ("https://unknownfinder1-77721-default-rtdb.europe-west1.firebasedatabase.app").getReference("Kullanicilar");

        kullanicilerYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //araba boş ise tüm kullanıcıları göster
                if (arama_bar.getText().toString().equals("admin")){

                    mKullaniciler.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        Kullanici kullanici=snapshot.getValue(Kullanici.class);
                        mKullaniciler.add(kullanici);

                    }
                    kullaniciAdapter.notifyDataSetChanged();
                    //her türlü değişiklik hemen yansısın diye
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}