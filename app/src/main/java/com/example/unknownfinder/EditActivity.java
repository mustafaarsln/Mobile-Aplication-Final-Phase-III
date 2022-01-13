// This java file and activity_Edit.xml written by Abdülkadir Mustafa Arslan.

package com.example.unknownfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    Button btn_save;
    MaterialEditText mEdt_ad, mEdt_soyad, mEdt_phone;

    FirebaseUser mevcutKullanici;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        btn_save=findViewById(R.id.save_profilDuzenleActivity);

        //Material Edittextler
        mEdt_ad=findViewById(R.id.material_edt_text_Ad_profilDuzenleActivity);
        mEdt_soyad=findViewById(R.id.material_edt_text_Soyad_profilDuzenleActivity);
        mEdt_phone=findViewById(R.id.material_edt_text_Phone_profilDuzenleActivity);

        mevcutKullanici=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference kullaniciYolu= FirebaseDatabase.
                getInstance("https://unknownfinder1-77721-default-rtdb.europe-west1.firebasedatabase.app").
                getReference("Kullanicilar").child(mevcutKullanici.getUid());
        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanici kullanici= snapshot.getValue(Kullanici.class);

                mEdt_ad.setText(kullanici.getK_name());
                mEdt_soyad.setText(kullanici.getK_surname());
                mEdt_phone.setText(kullanici.getK_phone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //save butonu kodları
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profiliGuncelle(mEdt_ad.getText().toString(),mEdt_soyad.getText().toString(),mEdt_phone.getText().toString());
            }
        });


    }

    private void profiliGuncelle(String ad, String soyad, String phone) {
        DatabaseReference guncellemeYolu = FirebaseDatabase.getInstance("https://unknownfinder1-77721-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Kullanicilar").child(mevcutKullanici.getUid());

        HashMap<String,Object> kullaniciGuncelleHashMap = new HashMap<>();
        kullaniciGuncelleHashMap.put("k_name", ad);
        kullaniciGuncelleHashMap.put("k_surname", soyad);
        kullaniciGuncelleHashMap.put("k_phone", phone);

        guncellemeYolu.updateChildren(kullaniciGuncelleHashMap);

    }

}