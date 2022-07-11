package com.wangoose.ojt_whc_java;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView btNaView;

    Fragment fragment_home;
    Fragment fragment_bookmark;

    PreferenceMgmt pref_username, pref_bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btNaView = findViewById(R.id.btnav_view);

        fragment_home = new FragmentHome();
        fragment_bookmark = new FragmentBookmark();

        pref_username = new PreferenceMgmt(this, "PREF_USERNAME");
        pref_bio = new PreferenceMgmt(this, "PREF_BIO");
        pref_username.clearPref();
        pref_bio.clearPref();

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