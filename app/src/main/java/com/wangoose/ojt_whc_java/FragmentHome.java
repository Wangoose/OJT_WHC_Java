package com.wangoose.ojt_whc_java;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {

    View homeView;

    TextView tvMainInfo;

    SearchView searchView;

    RetrofitClient rfClient;
    RetrofitInterface rfInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView =  (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        homeView = rootView.findViewById(R.id.view_fragment_home);

        tvMainInfo = rootView.findViewById(R.id.tvMainInfo);

        searchView = requireActivity().findViewById(R.id.search_view);

        rfClient = RetrofitClient.getInstance();
        rfInterface = RetrofitClient.getRetrofitInterface();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                rfInterface.getSearchResult(query, "20", "1").enqueue(new Callback<SearchUsersResult>() {
                    @Override
                    public void onResponse(@NonNull Call<SearchUsersResult> call, @NonNull Response<SearchUsersResult> response) {
                        if (response.isSuccessful()) {
                            SearchUsersResult uResult = response.body();

                            RviewAdapter adapter = new RviewAdapter(uResult, getActivity(), homeView);
                            RecyclerView rView = rootView.findViewById(R.id.recycler1);
                            rView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            rView.setAdapter(adapter);

                            tvMainInfo.setText("");

                            Snackbar sb = Snackbar.make(homeView, "API Load Success", Snackbar.LENGTH_LONG);
                            sb.setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sb.dismiss();
                                }
                            });
                            sb.setAnchorView(R.id.btnav_view);
                            sb.show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SearchUsersResult> call, @NonNull Throwable t) {
                        Snackbar sb = Snackbar.make(homeView, "API Load Failed", Snackbar.LENGTH_LONG);
                        sb.setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sb.dismiss();
                            }
                        });
                        sb.setAnchorView(R.id.btnav_view);
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
        return rootView;
    }
}