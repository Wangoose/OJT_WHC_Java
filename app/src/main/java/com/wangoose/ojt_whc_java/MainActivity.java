package com.wangoose.ojt_whc_java;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        btNaView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.item_fragment_home:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_fragment_container, fragment_home)
                                .commitAllowingStateLoss();
                        return true;
                    case R.id.item_fragment_bookmark:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_fragment_container, fragment_bookmark)
                                .commitAllowingStateLoss();
                        return true;
                }
                return true;
            }
        });

        View view = btNaView.findViewById(R.id.item_fragment_home);
        view.performClick();
    }
}