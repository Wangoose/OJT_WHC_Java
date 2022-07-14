package com.wangoose.ojt_whc_java;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

public class ProfileActivity extends AppCompatActivity {

    BookmarkMgmt bookmark;

    Button btnWebProfile;

    CheckBox chkbox;

    ImageView ivAvatar;

    TextView tvName, tvUserId, tvBio, tvFollowers, tvFollowing;

    UserItem userItem;

    View profileView;

    boolean bookmark_status = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bookmark = new BookmarkMgmt(getApplicationContext());

        Intent intent = getIntent();
        userItem = (UserItem) intent.getParcelableExtra("userItem");

        btnWebProfile = findViewById(R.id.btnWebProfile);

        chkbox = findViewById(R.id.chkbox_star);

        ivAvatar = findViewById(R.id.ivAvatar);

        tvName = findViewById(R.id.tvName);
        tvUserId = findViewById(R.id.tvUserId);
        tvBio = findViewById(R.id.tvBio);
        tvFollowers = findViewById(R.id.tvFollowers);
        tvFollowing = findViewById(R.id.tvFollowing);

        profileView = findViewById(R.id.view_profile);

        chkbox.setChecked(bookmark.isBookmarked(userItem.getLogin()));

        bookmark_status = chkbox.isChecked();

        Glide.with(this)
                .load(userItem.getAvatarUrl())
                .circleCrop()
                .into(ivAvatar);

        tvName.setText(userItem.getName());
        tvUserId.setText(userItem.getLogin());

        if (userItem.getBio() == null) {
            tvBio.setTextColor(Color.GRAY);
            tvBio.setText("(There is no comment)");
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
                    msg += "Bookmark added";
                } else {
                    bookmark.removeBookmark(userItem.getLogin());
                    chkbox.setChecked(false);
                    msg += "Bookmark deleted";
                }
                Snackbar sb = Snackbar.make(profileView, msg, Snackbar.LENGTH_SHORT);
                sb.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sb.dismiss();
                    }
                });
//                sb.setAnchorView(R.id.btnav_view);
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
    public void onBackPressed() {
        if (!(chkbox.isChecked() == bookmark_status)) { // 결과적으로 북마크 값이 바뀌었다면
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.putExtra("userItem", userItem);
            String task = chkbox.isChecked() ? "add" : "delete";
            intent.putExtra("task", task);
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }
}