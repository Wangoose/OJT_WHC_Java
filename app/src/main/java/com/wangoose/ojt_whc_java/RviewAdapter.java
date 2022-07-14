package com.wangoose.ojt_whc_java;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RviewAdapter extends RecyclerView.Adapter<RviewHolder> {

    BookmarkMgmt bookmark;

    Context context;

    FragmentBookmark fragmentBookmark;

    FragmentHome fragmentHome;

    List<UserItem> uItemList;

    ProfileSearch pSearch;

    SearchUsersResult userList;

    View fragView;

    public RviewAdapter(Context context, FragmentBookmark fragmentBookmark, View fragView, SearchUsersResult userList, BookmarkMgmt bookmark) {
        this.context = context;
        this.fragmentBookmark = fragmentBookmark;
        this.userList = userList;
        this.fragView = fragView;
        this.bookmark = bookmark;
    }

    public RviewAdapter(Context context, FragmentHome fragmentHome, View fragView, SearchUsersResult userList, BookmarkMgmt bookmark) {
        this.context = context;
        this.fragmentHome = fragmentHome;
        this.userList = userList;
        this.fragView = fragView;
        this.bookmark = bookmark;
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

        // 받아온 정보들을 RecyclerView item[position]들에 각각 적용
        pSearch = new ProfileSearch(context, holder, uItem);

        holder.recycler_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int requestCode = userList.isBookmarkList() ? 1 : 0;
                if (requestCode == 1)
                    fragmentBookmark.goProfile(uItem, requestCode);
                else
                    fragmentHome.goProfile(uItem, requestCode);
            }
        });

        // RecyclerView 스크롤시 CheckBox 상태 유지, reference : https://soulduse.tistory.com/53
        holder.chkbox.setOnCheckedChangeListener(null);

        holder.chkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "";
                if (holder.chkbox.isChecked()) {
                    bookmark.addBookmark(uItem.getLogin());
                    holder.chkbox.setChecked(true);
                    bookmarkManager("add", uItem, userList.isBookmarkList());
                    msg += "Bookmark added";
                } else {
                    bookmark.removeBookmark(uItem.getLogin());
                    holder.chkbox.setChecked(false);
                    bookmarkManager("delete", uItem, userList.isBookmarkList());
                    msg += "Bookmark deleted";
                }
                Snackbar sb = Snackbar.make(fragView, msg, Snackbar.LENGTH_SHORT);
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

    void bookmarkManager(String task, UserItem target, boolean isBookmarkFragment) {
        switch (task) {
            case "add":
                if (isBookmarkFragment)
                    fragmentBookmark.addBookmark(target);
                else
                    fragmentHome.addBookmark(target);
                break;
            case "delete":
                if (isBookmarkFragment)
                    fragmentBookmark.deleteBookmark(target.getLogin());
                else
                    fragmentHome.deleteBookmark(target.getLogin());
                break;
        }
    }
}

class RviewHolder extends RecyclerView.ViewHolder {

    CheckBox chkbox;
    ConstraintLayout recycler_item;
    ImageView ivAvatar;
    TextView tvName, tvUserId, tvBio;

    public RviewHolder(@NonNull View itemView) {
        super(itemView);
        chkbox = itemView.findViewById(R.id.chkbox_star);
        recycler_item = itemView.findViewById(R.id.recycler_item);
        ivAvatar = itemView.findViewById(R.id.ivAvatar);
        tvName = itemView.findViewById(R.id.tvName);
        tvUserId = itemView.findViewById(R.id.tvUserId);
        tvBio = itemView.findViewById(R.id.tvBio);
    }
}