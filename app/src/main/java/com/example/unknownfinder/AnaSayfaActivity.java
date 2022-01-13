// This java file and activity_ana_sayfa.xml written by Kıvanç Erbudak.

package com.example.unknownfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.unknownfinder.Cerceve.AramaFragment;
import com.example.unknownfinder.Cerceve.HomeFragment;
import com.example.unknownfinder.Cerceve.ProfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AnaSayfaActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment seciliCerceve = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedLister);

        getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici, new HomeFragment()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedLister =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {

                    switch (menuitem.getItemId()){

                        case R.id.nav_home:

                            //ana cerceveyi cagir
                            seciliCerceve = new HomeFragment();
                            break;

                        case R.id.nav_arama:

                            //arama cerceveyi cagir
                            seciliCerceve = new AramaFragment();
                            break;

                        case R.id.nav_profil:

                            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                            editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();

                            //profil cerceveyi cagir
                            seciliCerceve = new ProfilFragment();
                            break;
                    }

                    if (seciliCerceve != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici, seciliCerceve).commit();

                    }

                    return true;
                }
            };


}