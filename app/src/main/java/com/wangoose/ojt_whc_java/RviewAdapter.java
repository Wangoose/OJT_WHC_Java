package com.wangoose.ojt_whc_java;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RviewAdapter extends RecyclerView.Adapter<RviewHolder> {

    SearchUsersResult userList;
    ProfileSearch pSearch;
    List<UserItem> uItemList;
    Context context;

    public RviewAdapter(SearchUsersResult userList, Context context) {
        this.userList = userList;
        this.context = context;
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

        pSearch = new ProfileSearch(context, holder, uItem.getLogin());
    }

    @Override
    public int getItemCount() {
        return userList.getItems().size();
    }
}

class RviewHolder extends RecyclerView.ViewHolder {

    ImageView ivAvatar;
    TextView tvName, tvUserId, tvBio;

    public RviewHolder(@NonNull View itemView) {
        super(itemView);
        ivAvatar = itemView.findViewById(R.id.ivAvatar);
        tvName = itemView.findViewById(R.id.tvName);
        tvUserId = itemView.findViewById(R.id.tvUserId);
        tvBio = itemView.findViewById(R.id.tvBio);
    }
}