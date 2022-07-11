package com.wangoose.ojt_whc_java;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;

public class RviewAdapter extends RecyclerView.Adapter<RviewHolder> {

    SearchUsersResult userList;

    ProfileSearch pSearch;

    List<UserItem> uItemList;

    PreferenceMgmt pref_username, pref_bio, pref_bookmark;

    Context context;

    View homeView;

    public RviewAdapter(SearchUsersResult userList, Context context, View homeView) {
        this.context = context;
        this.userList = userList;
        this.homeView = homeView;
        pref_username = new PreferenceMgmt(context, "PREF_USERNAME");
        pref_bio = new PreferenceMgmt(context, "PREF_USERBIO");
        pref_bookmark = new PreferenceMgmt(context, "PREF_BOOKMARK");
    }

    @NonNull
    @Override
    public RviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_userlist, parent, false);
        return new RviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RviewHolder holder, int position) {
        uItemList = userList.getItems();
        UserItem uItem = uItemList.get(position);

        String userId = uItem.getLogin();
        String username = pref_username.containsPref(userId)? pref_username.getPref(userId) : "없음";
        String userAvatarUrl = uItem.getAvatarUrl();

        Log.d("testlog", "userId : " + userId + ", username : " + username);

        if (pref_username.containsPref(userId)) { // 로딩된 적이 있는 경우
            Glide.with(context)
                    .load(userAvatarUrl)
                    .circleCrop()
                    .into(holder.ivAvatar);

            holder.tvName.setText(username);
            holder.tvUserId.setText(userId);
            holder.tvBio.setText(pref_bio.getPref(userId));
        } else { // 로딩된 적이 없는 경우
            pSearch = new ProfileSearch(context, holder, userId);
        }

        // RecyclerView 스크롤시 CheckBox 상태 유지, 참고 : https://soulduse.tistory.com/53
        holder.chkbox.setOnCheckedChangeListener(null);

        holder.chkbox.setChecked(pref_bookmark.containsPref(userId));

        holder.chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String msg = "";
                if (isChecked) {
                    pref_bookmark.setPref(userId, userAvatarUrl);
                    holder.chkbox.setChecked(true);
                    msg += "Bookmark added";
                } else {
                    pref_bookmark.removePref(userId);
                    holder.chkbox.setChecked(false);
                    msg += "Bookmark deleted";
                }
                Snackbar sb = Snackbar.make(homeView, msg, Snackbar.LENGTH_SHORT);
                sb.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sb.dismiss();
                    }
                });
                sb.setAnchorView(R.id.btnav_view);
                sb.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.getItems().size();
    }
}

class RviewHolder extends RecyclerView.ViewHolder {

    CheckBox chkbox;
    ImageView ivAvatar;
    TextView tvName, tvUserId, tvBio;

    public RviewHolder(@NonNull View itemView) {
        super(itemView);
        chkbox = itemView.findViewById(R.id.chkbox_star);
        ivAvatar = itemView.findViewById(R.id.ivAvatar);
        tvName = itemView.findViewById(R.id.tvName);
        tvUserId = itemView.findViewById(R.id.tvUserId);
        tvBio = itemView.findViewById(R.id.tvBio);
    }
}