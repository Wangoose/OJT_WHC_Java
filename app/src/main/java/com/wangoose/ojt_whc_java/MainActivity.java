package com.wangoose.ojt_whc_java;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BookmarkMgmt bookmark;

    BottomNavigationView btNaView;

    Fragment fragmentHome;
    Fragment fragmentBookmark;

    FragmentManager fragmentManager;

    SearchUsersResult bookmarkUserList;

    int searchCondition = 0;

    long pressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        bookmarkUserList = (SearchUsersResult) intent.getParcelableExtra("bookmarkUserList");

        bookmark = new BookmarkMgmt(getApplicationContext());

        btNaView = findViewById(R.id.btnav_view);

        fragmentManager = getSupportFragmentManager();

        btNaView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_fragment_home:
                        if (fragmentHome != null) {
                            // 해당 프래그먼트의 어댑터뷰 refresh
                            ((FragmentHome) fragmentManager.findFragmentByTag("HOME")).refreshHome();
                            fragmentManager.beginTransaction().show(fragmentHome).commit();
                        } else {
                            fragmentHome = new FragmentHome();
                            fragmentManager.beginTransaction()
                                    .add(R.id.main_fragment_container, fragmentHome, "HOME")
                                    .commit();
                            // commit() 작업을 즉시 실행시킴
                            // findFragmentByTag 적용을 위해 필요함. reference : https://eitu97.tistory.com/31
                            fragmentManager.executePendingTransactions();
                        }
                        if (fragmentBookmark != null) {
                            fragmentManager.beginTransaction().hide(fragmentBookmark).commit();
                        }
                        break;
                    case R.id.item_fragment_bookmark:
                        if (fragmentBookmark != null) {
                            ((FragmentBookmark) fragmentManager.findFragmentByTag("BOOKMARK")).refreshBookmark(bookmarkUserList);
                            fragmentManager.beginTransaction().show(fragmentBookmark).commit();
                        } else {
                            fragmentBookmark = new FragmentBookmark().newInstance(bookmarkUserList);
                            fragmentManager.beginTransaction()
                                    .add(R.id.main_fragment_container, fragmentBookmark, "BOOKMARK")
                                    .commit();
                            fragmentManager.executePendingTransactions();
                        }
                        if (fragmentHome != null) {
                            fragmentManager.beginTransaction().hide(fragmentHome).commit();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.item_search_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSearchByUserId:
                item.setChecked(true);
                searchCondition = 0;
                break;
            case R.id.menuSearchByUsername:
                item.setChecked(true);
                searchCondition = 1;
                break;
        }
        return super.onOptionsItemSelected(item);
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
            }, 50);
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

    SearchUsersResult getBookmarkUserList() {
        return bookmarkUserList;
    }

    SearchUsersResult searchBookmark(List<UserItem> searchUserList, String target) {
        List<UserItem> resultItems = new ArrayList<>();
        for (UserItem uItem : searchUserList) {
            switch (searchCondition) {
                case 0:
                    if (uItem.getLogin().contains(target))
                        resultItems.add(uItem);
                    break;
                case 1:
                    if (uItem.getName() == null)
                        continue;
                    if (uItem.getName().contains(target))
                        resultItems.add(uItem);
                    break;
            }
        }
        SearchUsersResult bookmarkSearchResult = new SearchUsersResult(resultItems);
        bookmarkSearchResult.setBookmarkList(true);
        return bookmarkSearchResult;
    }

    void goProfile(UserItem userItem, int requestCode) {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        intent.putExtra("userItem", userItem);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
    // reference : https://blogdeveloperspot.blogspot.com/2022/03/android-edittext-android-simple.html
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}