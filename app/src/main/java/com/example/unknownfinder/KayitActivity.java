// This java file and activity_Kayit.xml written by Kıvanç Erbudak.

package com.example.unknownfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;

public class KayitActivity extends AppCompatActivity {

    EditText edt_name, edt_surname, edt_email, edt_phone, edt_sifre;

    Button btn_kayit;

    FirebaseAuth yetki;
    DatabaseReference yol;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);

        edt_name = findViewById(R.id.uname);
        edt_surname = findViewById(R.id.usurname);
        edt_email = findViewById(R.id.uemail);
        edt_phone = findViewById(R.id.uphone);
        edt_sifre = findViewById(R.id.upassword);
        btn_kayit = findViewById(R.id.registerbutton2);

        yetki = FirebaseAuth.getInstance();

        btn_kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Yeni kullanici kaydetme
                pd = new ProgressDialog(KayitActivity.this);
                pd.setMessage("Please wait...");
                pd.show();

                String str_name = edt_name.getText().toString();
                String str_surname = edt_surname.getText().toString();
                String str_email = edt_email.getText().toString();
                String str_phone = edt_phone.getText().toString();
                String str_sifre = edt_sifre.getText().toString();

                if (TextUtils.isEmpty(str_name) || TextUtils.isEmpty(str_surname) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_phone) || TextUtils.isEmpty(str_sifre)) {
                    Toast.makeText(KayitActivity.this, "Please fill the all boxes.", Toast.LENGTH_SHORT).show();
                }
                else if(str_sifre.length() < 6) {
                    Toast.makeText(KayitActivity.this, "Your password must be minimum 6 character.", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Yeni kullanici kaydediliyor
                    kaydet(str_name, str_surname, str_email, str_phone, str_sifre);
                }
            }
        });
    }
    private void kaydet(String username, String usersurname, String usermail, String userphone, String userpassword) {
        //Yeni kullanici kayit fonksiyonu
        yetki.createUserWithEmailAndPassword(usermail, userpassword)
        .addOnCompleteListener(KayitActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser fireaseKullanici = yetki.getCurrentUser();
                    String kullaniciId = fireaseKullanici.getUid();
                    yol = FirebaseDatabase.getInstance("https://unknownfinder1-77721-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Kullanicilar").child(kullaniciId);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("k_id", kullaniciId);
                    hashMap.put("k_name", username.toLowerCase());
                    hashMap.put("k_surname", usersurname.toLowerCase());
                    hashMap.put("k_phone", userphone);
                    hashMap.put("k_email", usermail);
                    yol.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                pd.dismiss();
                                Intent intent = new Intent(KayitActivity.this, AnaSayfaActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                }
                else {
                    pd.dismiss();
                    Toast.makeText(KayitActivity.this, "Registration was not succesful with this mail and password.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}