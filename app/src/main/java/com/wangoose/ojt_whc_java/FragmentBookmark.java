package com.wangoose.ojt_whc_java;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FragmentBookmark extends Fragment {

    BookmarkMgmt bookmark;

    RviewAdapter adapter;

    SearchUsersResult bookmarkUserList;

    SearchView searchView;

    TextView tvBookmarkInfo;

    View bookmarkView;

    public static FragmentBookmark newInstance(SearchUsersResult bookmarkUserList) {
        FragmentBookmark fb = new FragmentBookmark();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bookmarkUserList", bookmarkUserList);
        fb.setArguments(bundle);
        return fb;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_bookmark, container, false);

        if (getArguments() != null)
            bookmarkUserList = (SearchUsersResult) getArguments().getParcelable("bookmarkUserList");

        bookmark = new BookmarkMgmt(requireActivity());

        bookmarkView = rootView.findViewById(R.id.view_fragment_bookmark);

        searchView = rootView.findViewById(R.id.search_view_bookmark);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBookmark(bookmarkUserList.getItems(), query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.findViewById(androidx.appcompat.R.id.search_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", false);
                bookmarkUserList = ((MainActivity) requireActivity()).getBookmarkUserList();
                adapter.uItemList.clear();
                adapter.uItemList.addAll(bookmarkUserList.getItems());
                adapter.notifyDataSetChanged();
            }
        });

        tvBookmarkInfo = rootView.findViewById(R.id.tvBookmarkInfo);

        if (bookmarkUserList.getItems().size() != 0)
            tvBookmarkInfo.setText("");

        List<UserItem> newUserItems = new ArrayList<>(bookmarkUserList.getItems());
        bookmarkUserList = new SearchUsersResult(newUserItems);
        bookmarkUserList.setBookmarkList(true);

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

    void searchBookmark(List<UserItem> searchUserList, String target) {
        SearchUsersResult searchResult = ((MainActivity) requireActivity()).searchBookmark(searchUserList, target);
        adapter.uItemList.clear();
        adapter.uItemList.addAll(searchResult.getItems());
        adapter.notifyDataSetChanged();
    }

    void refreshBookmark(SearchUsersResult bookmarkUserList) {
        this.bookmarkUserList = bookmarkUserList;
        if (bookmarkUserList.getItems().size() != 0)
            tvBookmarkInfo.setText("");
        adapter.notifyDataSetChanged();
    }

    void goProfile(UserItem userItem, int requestCode) {
        ((MainActivity) requireActivity()).goProfile(userItem, requestCode);
    }
}