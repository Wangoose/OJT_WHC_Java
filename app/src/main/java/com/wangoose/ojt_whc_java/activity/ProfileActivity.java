package com.wangoose.ojt_whc_java.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.wangoose.ojt_whc_java.preference.BookmarkMgmt;
import com.wangoose.ojt_whc_java.R;
import com.wangoose.ojt_whc_java.dto.UserItem;

public class ProfileActivity extends AppCompatActivity {

    BookmarkMgmt bookmark;

    Button btnWebProfile;

    CheckBox chkbox;

    ImageView ivAvatar;

    TextView tvName, tvUserId, tvBio, tvFollowers, tvFollowing;

    UserItem userItem;

    View profileView;

    private boolean bookmark_status = false;
    int requestCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(R.string.profileActivityTitle);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        bookmark = new BookmarkMgmt(getApplicationContext());

        Intent intent = getIntent();
        userItem = (UserItem) intent.getParcelableExtra("userItem");

        requestCode = intent.getIntExtra("requestCode", 0);

        btnWebProfile = findViewById(R.id.btnWebProfile);

        chkbox = findViewById(R.id.chkboxStar);

        ivAvatar = findViewById(R.id.ivAvatar);

        tvName = findViewById(R.id.tvName);
        tvUserId = findViewById(R.id.tvUserId);
        tvBio = findViewById(R.id.tvBio);
        tvFollowers = findViewById(R.id.tvFollowers);
        tvFollowing = findViewById(R.id.tvFollowing);

        profileView = findViewById(R.id.view_profile);

        chkbox.setChecked(bookmark.isBookmarked(userItem.getLogin()));

        bookmark_status = chkbox.isChecked(); // 최초 북마크 상태 기록

        Glide.with(this)
                .load(userItem.getAvatarUrl())
                .circleCrop()
                .into(ivAvatar);

        tvName.setText(userItem.getName());
        tvUserId.setText(userItem.getLogin());

        if (userItem.getBio() == null) {
            tvBio.setTextColor(Color.GRAY);
            tvBio.setText(getString(R.string.profileActivityUserBioIsNull));
        } else {
            tvBio.setText(userItem.getBio());
        }

        tvFollowers.setText(String.valueOf(userItem.getFollowers()));
        tvFollowing.setText(String.valueOf(userItem.getFollowing()));

        chkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "";
                if (chkbox.isChecked()) {
                    bookmark.addBookmark(userItem.getLogin());
                    chkbox.setChecked(true);
                    msg += getString(R.string.snackBarBookmarkAddedMessage);
                } else {
                    bookmark.removeBookmark(userItem.getLogin());
                    chkbox.setChecked(false);
                    msg += getString(R.string.snackBarBookmarkDeletedMessage);
                }
                Snackbar sb = Snackbar.make(profileView, msg, Snackbar.LENGTH_SHORT);
                sb.setAction(getString(R.string.snackBarConfirmMessage), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sb.dismiss();
                    }
                });
                sb.show();
            }
        });

        btnWebProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(userItem.getHtmlUrl()));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // reference : https://www.geeksforgeeks.org/how-to-add-and-customize-back-button-of-action-bar-in-android/
        if (item.getItemId() == android.R.id.home) // Appbar 상단 뒤로가기 버튼 눌렀을 시
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!(chkbox.isChecked() == bookmark_status)) { // 결과적으로 북마크 값이 바뀌었다면
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.putExtra("userItem", userItem);
            intent.putExtra("task", chkbox.isChecked() ? "add" : "delete");
            intent.putExtra("requestCode", requestCode);
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }
}