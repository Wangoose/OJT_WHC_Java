package com.wangoose.ojt_whc_java.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.wangoose.ojt_whc_java.R;
import com.wangoose.ojt_whc_java.activity.MainActivity;
import com.wangoose.ojt_whc_java.adapter.RviewAdapter;
import com.wangoose.ojt_whc_java.databinding.FragmentHomeBinding;
import com.wangoose.ojt_whc_java.dto.SearchUsersResult;
import com.wangoose.ojt_whc_java.dto.UserItem;
import com.wangoose.ojt_whc_java.preference.BookmarkMgmt;
import com.wangoose.ojt_whc_java.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {

    private FragmentHomeBinding binding;

    BookmarkMgmt bookmark;

    RetrofitClient rfClient = RetrofitClient.getInstance();

    RviewAdapter adapter;

    SearchUsersResult uResult;

    private static final int perPage = 20;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        bookmark = new BookmarkMgmt(requireActivity());

        binding.searchViewHome.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                RetrofitClient.getRetrofitInterface().getSearchResult(query, perPage).enqueue(new Callback<SearchUsersResult>() {
                    @Override
                    public void onResponse(@NonNull Call<SearchUsersResult> call, @NonNull Response<SearchUsersResult> response) {
                        if (response.isSuccessful()) {
                            uResult = response.body();

                            adapter = new RviewAdapter(requireActivity(),
                                    FragmentHome.this,
                                    binding.viewFragmentHome,
                                    uResult);
                            binding.recycler1.setLayoutManager(new LinearLayoutManager(requireActivity()));
                            binding.recycler1.setAdapter(adapter);

                            binding.tvMainInfo.setText("");

                            Snackbar sb = Snackbar.make(binding.viewFragmentHome,
                                    R.string.snackBarApiLoadSuccess,
                                    Snackbar.LENGTH_LONG);
                            sb.setAction(getString(R.string.snackBarConfirmMessage), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sb.dismiss();
                                }
                            });
                            sb.setAnchorView(R.id.bottomNavigationView);
                            sb.show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SearchUsersResult> call, @NonNull Throwable t) {
                        Snackbar sb = Snackbar.make(binding.viewFragmentHome,
                                R.string.snackBarApiLoadFail,
                                Snackbar.LENGTH_LONG);
                        sb.setAction(R.string.snackBarConfirmMessage, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sb.dismiss();
                            }
                        });
                        sb.setAnchorView(R.id.bottomNavigationView);
                        sb.show();
                        t.printStackTrace();
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

        binding.searchViewHome.findViewById(androidx.appcompat.R.id.search_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.searchViewHome.setQuery("", false);
            }
        });
        return binding.getRoot();
    }

    public void addBookmark(UserItem target) {
        ((MainActivity) requireActivity()).addBookmark(target);
    }

    public void deleteBookmark(String target) {
        ((MainActivity) requireActivity()).deleteBookmark(target);
    }

    public void refreshHome() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public void goProfile(UserItem userItem, int requestCode) {
        ((MainActivity) requireActivity()).goProfile(userItem, requestCode);
    }
}