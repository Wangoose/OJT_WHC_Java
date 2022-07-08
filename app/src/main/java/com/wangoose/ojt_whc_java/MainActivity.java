package com.wangoose.ojt_whc_java;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Fragment fragment_home;
    Fragment fragment_bookmark;

    BottomNavigationView btNaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btNaView = findViewById(R.id.btnav_view);

        fragment_home = new FragmentHome();
        fragment_bookmark = new FragmentBookmark();

        SharedPreferences sPref = getSharedPreferences("PREF_WHC", MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.remove("PREF_USERNAME");
        editor.apply();

        startFragment(fragment_home);

        btNaView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_fragment_home:
                        startFragment(fragment_home);
                        return true;
                    case R.id.item_fragment_bookmark:
                        startFragment(fragment_bookmark);
                        return true;
                }
                return true;
            }
        });
    }

    void startFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragment)
                .commitAllowingStateLoss();
    }
}