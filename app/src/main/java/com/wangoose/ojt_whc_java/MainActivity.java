package com.wangoose.ojt_whc_java;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    BookmarkMgmt bookmark;

    BottomNavigationView btNaView;

    Fragment fragment_home;
    Fragment fragment_bookmark;

    FragmentManager fragmentManager;

    SearchUsersResult bookmarkUserList;

    long pressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        bookmarkUserList = (SearchUsersResult) intent.getSerializableExtra("bookmarkUserList");

        bookmark = new BookmarkMgmt(getApplicationContext());

        btNaView = findViewById(R.id.btnav_view);

        fragmentManager = getSupportFragmentManager();

        btNaView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_fragment_home:
                        if (fragment_home == null) {
                            fragment_home = new FragmentHome();
                            fragmentManager.beginTransaction()
                                    .add(R.id.main_fragment_container, fragment_home, "HOME")
                                    .commit();
                            // commit() 작업을 즉시 실행시킴
                            // findFragmentByTag 적용을 위해 필요함. reference : https://eitu97.tistory.com/31
                            fragmentManager.executePendingTransactions();
                        }

                        if (fragment_home != null) {
                            // 해당 프래그먼트의 어댑터뷰 refresh
                            ((FragmentHome) fragmentManager.findFragmentByTag("HOME")).refreshHome();
                            fragmentManager.beginTransaction().show(fragment_home).commit();
                        }

                        if (fragment_bookmark != null)
                            fragmentManager.beginTransaction().hide(fragment_bookmark).commit();
                        break;
                    case R.id.item_fragment_bookmark:
                        if (fragment_bookmark == null) {
                            fragment_bookmark = new FragmentBookmark().newInstance(bookmarkUserList);
                            fragmentManager.beginTransaction()
                                    .add(R.id.main_fragment_container, fragment_bookmark, "BOOKMARK")
                                    .commit();
                            fragmentManager.executePendingTransactions();
                        }

                        if (fragment_home != null)
                            fragmentManager.beginTransaction().hide(fragment_home).commit();

                        if (fragment_bookmark != null) {
                            ((FragmentBookmark) fragmentManager.findFragmentByTag("BOOKMARK")).refreshBookmark(bookmarkUserList);
                            fragmentManager.beginTransaction().show(fragment_bookmark).commit();
                        }
                        break;
                }
                return true;
            }
        });

        View navBtnHome = btNaView.findViewById(R.id.item_fragment_home);
        navBtnHome.performClick();
    }

    @Override
    public void onBackPressed() { // reference : https://best421.tistory.com/71
        if (pressedTime == 0) {
            Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            pressedTime = System.currentTimeMillis();
        } else {
            int seconds = (int) (System.currentTimeMillis() - pressedTime);

            if (seconds > 2000) {
                Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
                pressedTime = 0;
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String task = data.getExtras().getString("task");
            UserItem userItem = (UserItem) data.getExtras().get("userItem");
            if (task.equals("add"))
                addBookmark(userItem);
            else
                deleteBookmark(userItem.getLogin());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (requestCode == 1)
                        ((FragmentBookmark) fragmentManager.findFragmentByTag("BOOKMARK")).refreshBookmark(bookmarkUserList);
                    else
                        ((FragmentHome) fragmentManager.findFragmentByTag("HOME")).refreshHome();
                }
            }, 300);
        }
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

    void goProfile(UserItem userItem, int requestCode) {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        intent.putExtra("userItem", userItem);
        startActivityForResult(intent, requestCode);
    }
}