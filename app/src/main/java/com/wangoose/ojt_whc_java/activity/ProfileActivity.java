package com.wangoose.ojt_whc_java.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.wangoose.ojt_whc_java.R;
import com.wangoose.ojt_whc_java.databinding.ActivityProfileBinding;
import com.wangoose.ojt_whc_java.dto.UserItem;
import com.wangoose.ojt_whc_java.preference.BookmarkMgmt;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    BookmarkMgmt bookmark;

    UserItem userItem;

    private boolean bookmark_status = false;
    int requestCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(R.string.profileActivityTitle);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        bookmark = new BookmarkMgmt(getApplicationContext());

        Intent intent = getIntent();
        userItem = (UserItem) intent.getParcelableExtra("userItem");

        requestCode = intent.getIntExtra("requestCode", 0);

        binding.chkboxStar.setChecked(bookmark.isBookmarked(userItem.getLogin()));

        bookmark_status = binding.chkboxStar.isChecked(); // 최초 북마크 상태 기록

        Glide.with(this)
                .load(userItem.getAvatarUrl())
                .circleCrop()
                .into(binding.ivAvatar);

        binding.tvName.setText(userItem.getName());
        binding.tvUserId.setText(userItem.getLogin());

        if (userItem.getBio() == null) {
            binding.tvBio.setTextColor(Color.GRAY);
            binding.tvBio.setText(getString(R.string.profileActivityUserBioIsNull));
        } else {
            binding.tvBio.setText(userItem.getBio());
        }

        binding.tvFollowers.setText(String.valueOf(userItem.getFollowers()));
        binding.tvFollowing.setText(String.valueOf(userItem.getFollowing()));

        binding.chkboxStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "";
                if (binding.chkboxStar.isChecked()) {
                    bookmark.addBookmark(userItem.getLogin());
                    binding.chkboxStar.setChecked(true);
                    msg += getString(R.string.snackBarBookmarkAddedMessage);
                } else {
                    bookmark.removeBookmark(userItem.getLogin());
                    binding.chkboxStar.setChecked(false);
                    msg += getString(R.string.snackBarBookmarkDeletedMessage);
                }
                Snackbar sb = Snackbar.make(binding.viewProfile, msg, Snackbar.LENGTH_SHORT);
                sb.setAction(getString(R.string.snackBarConfirmMessage), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sb.dismiss();
                    }
                });
                sb.show();
            }
        });

        binding.btnWebProfile.setOnClickListener(new View.OnClickListener() {
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
        if (!(binding.chkboxStar.isChecked() == bookmark_status)) { // 결과적으로 북마크 값이 바뀌었다면
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.putExtra("userItem", userItem);
            intent.putExtra("task", binding.chkboxStar.isChecked() ? "add" : "delete");
            intent.putExtra("requestCode", requestCode);
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }
}