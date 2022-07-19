package com.wangoose.ojt_whc_java.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wangoose.ojt_whc_java.R;
import com.wangoose.ojt_whc_java.activity.MainActivity;
import com.wangoose.ojt_whc_java.adapter.RviewAdapter;
import com.wangoose.ojt_whc_java.databinding.FragmentBookmarkBinding;
import com.wangoose.ojt_whc_java.dto.SearchUsersResult;
import com.wangoose.ojt_whc_java.dto.UserItem;
import com.wangoose.ojt_whc_java.preference.BookmarkMgmt;

import java.util.ArrayList;
import java.util.List;

public class FragmentBookmark extends Fragment {

    private FragmentBookmarkBinding binding;

    BookmarkMgmt bookmark;

    RviewAdapter adapter;

    SearchUsersResult bookmarkUserList;

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
        binding = FragmentBookmarkBinding.inflate(inflater, container, false);

        if (getArguments() != null)
            bookmarkUserList = (SearchUsersResult) getArguments().getParcelable("bookmarkUserList");

        bookmark = new BookmarkMgmt(requireActivity());

        binding.searchViewBookmark.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        binding.searchViewBookmark.findViewById(androidx.appcompat.R.id.search_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.searchViewBookmark.setQuery("", false);
                bookmarkUserList = ((MainActivity) requireActivity()).getBookmarkUserList();
                if (adapter.uItemList != null)
                    adapter.uItemList.clear();
                if (adapter.uItemList != null && bookmarkUserList.getItems().size() != 0)
                    adapter.uItemList.addAll(bookmarkUserList.getItems());
                adapter.notifyDataSetChanged();
            }
        });

        if (bookmarkUserList.getItems().size() != 0)
            binding.tvBookmarkInfo.setText("");

        if (bookmarkUserList.getItems().size() == 0)
            bookmarkUserList.createEmptyItems();

        List<UserItem> newUserItems = new ArrayList<>(bookmarkUserList.getItems());
        bookmarkUserList = new SearchUsersResult(newUserItems);
        bookmarkUserList.setBookmarkList(true);

        adapter = new RviewAdapter(requireActivity(),
                FragmentBookmark.this,
                binding.viewFragmentBookmark,
                bookmarkUserList);
        binding.recycler2.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.recycler2.setAdapter(adapter);

        return binding.getRoot();
    }

    public void addBookmark(UserItem target) {
        ((MainActivity) requireActivity()).addBookmark(target);
    }

    public void deleteBookmark(String target) {
        ((MainActivity) requireActivity()).deleteBookmark(target);
    }

    void searchBookmark(List<UserItem> searchUserList, String target) {
        SearchUsersResult searchResult = ((MainActivity) requireActivity()).searchBookmark(searchUserList, target);
        if (searchResult.getItems().size() != 0) {
            adapter.uItemList.clear();
            adapter.uItemList.addAll(searchResult.getItems());
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(requireActivity(), R.string.toastNoSearchResult, Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshBookmark(SearchUsersResult bookmarkUserList) {
        this.bookmarkUserList = bookmarkUserList;
        binding.tvBookmarkInfo.setText(bookmarkUserList.getItems().size() != 0 ? "" : requireActivity().getString(R.string.bookmarkSearchViewMessage));
        if (adapter.uItemList == null)
            adapter.uItemList = new ArrayList<>();
        else
            adapter.uItemList.clear();
        if (adapter.uItemList != null && bookmarkUserList.getItems().size() != 0)
            adapter.uItemList.addAll(bookmarkUserList.getItems());
        adapter.notifyDataSetChanged();
    }

    public void goProfile(UserItem userItem, int requestCode) {
        ((MainActivity) requireActivity()).goProfile(userItem, requestCode);
    }
}