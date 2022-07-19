package com.wangoose.ojt_whc_java.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.wangoose.ojt_whc_java.R;
import com.wangoose.ojt_whc_java.databinding.ItemUserlistBinding;
import com.wangoose.ojt_whc_java.dto.SearchUsersResult;
import com.wangoose.ojt_whc_java.dto.UserItem;
import com.wangoose.ojt_whc_java.fragment.FragmentBookmark;
import com.wangoose.ojt_whc_java.fragment.FragmentHome;
import com.wangoose.ojt_whc_java.preference.BookmarkMgmt;

import java.util.List;

public class RviewAdapter extends RecyclerView.Adapter<RviewHolder> {

    BookmarkMgmt bookmark;

    Context context;

    FragmentBookmark fragmentBookmark;

    FragmentHome fragmentHome;

    public List<UserItem> uItemList;

    ProfileSearch pSearch;

    SearchUsersResult userList;

    View fragView;

    public RviewAdapter(Context context, FragmentBookmark fragmentBookmark, View fragView, SearchUsersResult userList) {
        this.context = context;
        this.fragmentBookmark = fragmentBookmark;
        this.userList = new SearchUsersResult(userList.getItems());
        this.userList.setBookmarkList(true);
        this.uItemList = userList.getItems();
        this.fragView = fragView;
    }

    public RviewAdapter(Context context, FragmentHome fragmentHome, View fragView, SearchUsersResult userList) {
        this.context = context;
        this.fragmentHome = fragmentHome;
        this.userList = new SearchUsersResult(userList.getItems());
        this.userList.setBookmarkList(false);
        this.uItemList = userList.getItems();
        this.fragView = fragView;
    }

    @NonNull
    @Override
    public RviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        com.wangoose.ojt_whc_java.databinding.ItemUserlistBinding binding = ItemUserlistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RviewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RviewHolder holder, int position) {
        UserItem uItem = uItemList.get(position);

        // 받아온 정보들을 RecyclerView item[position]들에 각각 적용
        pSearch = new ProfileSearch(context, holder, uItem);

        holder.binding.recyclerItem.setOnClickListener(new View.OnClickListener() {
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
        holder.binding.chkboxStar.setOnCheckedChangeListener(null);

        holder.binding.chkboxStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmark = new BookmarkMgmt(context);
                String msg = "";
                if (holder.binding.chkboxStar.isChecked()) {
                    bookmark.addBookmark(uItem.getLogin());
                    holder.binding.chkboxStar.setChecked(true);
                    bookmarkManager("add", uItem, userList.isBookmarkList());
                    msg += context.getString(R.string.snackBarBookmarkAddedMessage);
                } else {
                    bookmark.removeBookmark(uItem.getLogin());
                    holder.binding.chkboxStar.setChecked(false);
                    bookmarkManager("delete", uItem, userList.isBookmarkList());
                    msg += context.getString(R.string.snackBarBookmarkDeletedMessage);
                }
                Snackbar sb = Snackbar.make(fragView, msg, Snackbar.LENGTH_SHORT);
                sb.setAction(context.getString(R.string.snackBarConfirmMessage), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sb.dismiss();
                    }
                });
                sb.setAnchorView(R.id.bottomNavigationView);
                sb.show();
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
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

    ItemUserlistBinding binding;

    public RviewHolder(ItemUserlistBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}