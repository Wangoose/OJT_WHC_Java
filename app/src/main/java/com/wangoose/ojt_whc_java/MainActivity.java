package com.wangoose.ojt_whc_java;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    BookmarkMgmt bookmark;

    BottomNavigationView btNaView;

    Fragment fragment_home;
    Fragment fragment_bookmark;

    SearchUsersResult bookmarkUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        bookmarkUserList = (SearchUsersResult) intent.getSerializableExtra("bookmarkUserList");

        bookmark = new BookmarkMgmt(getApplicationContext());

        btNaView = findViewById(R.id.btnav_view);

        fragment_home = new FragmentHome(bookmark);
        fragment_bookmark = new FragmentBookmark(bookmark, bookmarkUserList);

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

        startFragment(fragment_home);
    }

    void startFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragment)
                .commitAllowingStateLoss();
    }

    void addBookmark(UserItem target) {
        bookmarkUserList.addItems(target);
    }

    void deleteBookmark(String target) {
        List<UserItem> uItemList = bookmarkUserList.getItems();
        boolean signal = true;
        for (int i = 0; i < uItemList.size() && signal; i++) { // Linear Search
            String userId = uItemList.get(i).getLogin();
            if (userId.equals(target)) {
                bookmarkUserList.removeItems(i); // Remove current index userItem
                signal = false; // escape
            }
        }
    }

}