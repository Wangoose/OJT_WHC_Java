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

    List<UserResult> userList;
    Context context;

    public RviewAdapter(List<UserResult> userList, Context context) {
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
        UserResult uResult = userList.get(position);

        Glide.with(context)
            .load(uResult.getAvatarUrl())
            .circleCrop()
            .into(holder.ivAvatar);
        holder.tvName.setText(uResult.getLogin());
        holder.tvUrl.setText(uResult.getHtmlUrl());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}

class RviewHolder extends RecyclerView.ViewHolder {

    ImageView ivAvatar;
    TextView tvName, tvUrl;

    public RviewHolder(@NonNull View itemView) {
        super(itemView);
        ivAvatar = itemView.findViewById(R.id.ivAvatar);
        tvName = itemView.findViewById(R.id.tvName);
        tvUrl = itemView.findViewById(R.id.tvUrl);
    }
}