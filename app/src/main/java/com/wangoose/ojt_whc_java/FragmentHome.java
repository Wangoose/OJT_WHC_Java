package com.wangoose.ojt_whc_java;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {

    View homeView;

    RetrofitClient rfClient;
    RetrofitInterface rfInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView =  (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        homeView = rootView.findViewById(R.id.view_fragment_home);

        rfClient = RetrofitClient.getInstance();
        rfInterface = RetrofitClient.getRetrofitInterface();

        rfInterface.getUserResult().enqueue(new Callback<List<UserResult>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserResult>> call, @NonNull Response<List<UserResult>> response) {
                if (response.isSuccessful()) {
                    List<UserResult> uResult = response.body();

                    RviewAdapter adapter = new RviewAdapter(uResult, getActivity());
                    RecyclerView rView = rootView.findViewById(R.id.recycler1);
                    rView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rView.setAdapter(adapter);

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
            public void onFailure(@NonNull Call<List<UserResult>> call, @NonNull Throwable t) {
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
        return rootView;
    }
}
