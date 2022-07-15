package com.wangoose.ojt_whc_java;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentBookmark extends Fragment {

    BookmarkMgmt bookmark;

    RviewAdapter adapter;

    SearchUsersResult bookmarkUserList;

    SearchView searchView;

    TextView tvBookmarkInfo;

    View bookmarkView;

    boolean isBackStack = false;

    public static FragmentBookmark newInstance(SearchUsersResult bookmarkUserList, boolean isBackStack) {
            FragmentBookmark fb = new FragmentBookmark();
            Bundle bundle = new Bundle();
            bundle.putParcelable("bookmarkUserList", bookmarkUserList);
            bundle.putBoolean("isBackStack", isBackStack);
            fb.setArguments(bundle);
            return fb;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_bookmark, container, false);

        if (getArguments() != null) {
            bookmarkUserList = (SearchUsersResult) getArguments().getParcelable("bookmarkUserList");
            isBackStack = getArguments().getBoolean("isBackStack");
        }

        bookmark = new BookmarkMgmt(requireActivity());

        bookmarkView = rootView.findViewById(R.id.view_fragment_bookmark);

        searchView = rootView.findViewById(R.id.search_view_bookmark);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBookmark(bookmarkUserList, query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (isBackStack)
                    Toast.makeText(requireActivity(), "응애 나 백스택", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        tvBookmarkInfo = rootView.findViewById(R.id.tvBookmarkInfo);

        if (bookmarkUserList.getItems().size() != 0)
            tvBookmarkInfo.setText("");

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

    void searchBookmark(SearchUsersResult bookmarkUserList, String target) {
        SearchUsersResult bookmarkSearchResult = ((MainActivity) requireActivity()).searchBookmark(bookmarkUserList, target);

        FragmentBookmark fragmentBookmark = new FragmentBookmark().newInstance(bookmarkSearchResult, true);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.view_fragment_bookmark, fragmentBookmark, "BOOKMARK")
                .addToBackStack(null)
                .commit();

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