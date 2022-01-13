// This java file and activity_Giris.xml written by Kıvanç Erbudak.

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GirisActivity extends AppCompatActivity {

    EditText edt_email_giris, edt_sifre_giris;

    Button btn_giris_yap;

    FirebaseAuth girisYetkisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        edt_email_giris = findViewById(R.id.uemail2);
        edt_sifre_giris = findViewById(R.id.upassword2);
        btn_giris_yap = findViewById(R.id.loginbutton2);

        girisYetkisi = FirebaseAuth.getInstance();

        btn_giris_yap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Giris yapma kodlari
                ProgressDialog pdGiris = new ProgressDialog(GirisActivity.this);
                pdGiris.setMessage("Logging in...");
                pdGiris.show();

                String str_emailGiris = edt_email_giris.getText().toString();
                String str_sifreGiris = edt_sifre_giris.getText().toString();
                
                if (TextUtils.isEmpty(str_emailGiris) || TextUtils.isEmpty(str_sifreGiris)) {
                    Toast.makeText(GirisActivity.this, "Please fill the all boxes.", Toast.LENGTH_SHORT).show();
                }
                else {
                    girisYetkisi.signInWithEmailAndPassword(str_emailGiris, str_sifreGiris)
                    .addOnCompleteListener(GirisActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {

                                DatabaseReference yolGiris = FirebaseDatabase.getInstance("https://unknownfinder1-77721-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Kullanicilar").child(girisYetkisi.getCurrentUser().getUid());
                                yolGiris.addValueEventListener(new ValueEventListener()

                                {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        pdGiris.dismiss();
                                        Intent intent = new Intent(GirisActivity.this, AnaSayfaActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        pdGiris.dismiss();
                                    }
                                });
                            }
                            else {
                                pdGiris.dismiss();
                                Toast.makeText(GirisActivity.this, "Logging failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}