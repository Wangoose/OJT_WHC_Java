package com.wangoose.ojt_whc_java;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentBookmark extends Fragment {

    BookmarkMgmt bookmark;

    RetrofitClient rfClient;
    RetrofitInterface rfInterface;

    RviewAdapter adapter;

    SearchUsersResult bookmarkUserList;

    TextView tvBookmarkInfo;

    View bookmarkView;

    public FragmentBookmark(BookmarkMgmt bookmark, SearchUsersResult bookmarkUserList) {
        this.bookmark = bookmark;
        this.bookmarkUserList = bookmarkUserList;

        rfClient = RetrofitClient.getInstance();
        rfInterface = RetrofitClient.getRetrofitInterface();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_bookmark, container, false);

        bookmarkView = rootView.findViewById(R.id.view_fragment_bookmark);

        tvBookmarkInfo = rootView.findViewById(R.id.tvBookmarkInfo);

        if (bookmarkUserList.getItems().size() != 0) {
            tvBookmarkInfo.setText("");
        }

        adapter = new RviewAdapter(getActivity(), FragmentBookmark.this, bookmarkView, bookmarkUserList, bookmark);
        RecyclerView rView = rootView.findViewById(R.id.recycler2);
        rView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rView.setAdapter(adapter);

        return rootView;
    }

    void addBookmark(UserItem target) {
        ((MainActivity) requireActivity()).addBookmark(target);
    }

    void deleteBookmark(String target) {
        ((MainActivity) requireActivity()).deleteBookmark(target);
    }
}