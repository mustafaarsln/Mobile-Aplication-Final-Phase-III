// All the  fragment.java files and fragment xml files written by Abdülkadir Mustafa ARSLAN.
package com.example.unknownfinder.Cerceve;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unknownfinder.Adapter.KullaniciAdapter;
import com.example.unknownfinder.Kullanici;
import com.example.unknownfinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private KullaniciAdapter kullaniciAdapter;
    private List<Kullanici> kayitlilisteleri;

    private List<String> favlistesi;





    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView=view.findViewById(R.id.recyler_view_FavaAlinanlar);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        kayitlilisteleri = new ArrayList<>();
        kullaniciAdapter = new KullaniciAdapter(getContext(), kayitlilisteleri);

        recyclerView.setAdapter(kullaniciAdapter);

        favKontrolu();


        return view;
    }

    private void favKontrolu()
    {
        favlistesi = new ArrayList<>();
        DatabaseReference favyolu=FirebaseDatabase.getInstance
                ("https://unknownfinder1-77721-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Takip").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("TakipEdilenler");

        favyolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favlistesi.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    favlistesi.add(snapshot.getKey());
                }

                kayitlilarioku();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void kayitlilarioku (){

        DatabaseReference kayitliyolu = FirebaseDatabase.getInstance
                ("https://unknownfinder1-77721-default-rtdb.europe-west1.firebasedatabase.app").getReference("Kullanicilar");

        kayitliyolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                kayitlilisteleri.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Kullanici kullanici = snapshot.getValue(Kullanici.class);
                    for (String id : favlistesi)
                    {
                        if (kullanici.getK_id().equals(id))
                        {
                            kayitlilisteleri.add(kullanici);
                        }
                    }
                }
                kullaniciAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}