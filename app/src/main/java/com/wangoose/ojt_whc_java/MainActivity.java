package com.wangoose.ojt_whc_java;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    View mainView;

    RetrofitClient rfClient;
    RetrofitInterface rfInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("User Lists");

        mainView = findViewById(R.id.constview_main);

        rfClient = RetrofitClient.getInstance();
        rfInterface = RetrofitClient.getRetrofitInterface();

        rfInterface.getUserResult().enqueue(new Callback<List<UserResult>>() {
            @Override
            public void onResponse(Call<List<UserResult>> call, Response<List<UserResult>> response) {
                if (response.isSuccessful()) {
                    List<UserResult> uResult = response.body();

                    RviewAdapter adapter = new RviewAdapter(uResult, MainActivity.this);
                    RecyclerView rView = findViewById(R.id.recycler1);
                    rView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    rView.setAdapter(adapter);
                    Snackbar sb = Snackbar.make(mainView, "API Load Success", Snackbar.LENGTH_LONG);
                    sb.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sb.dismiss();
                        }
                    });
                    sb.show();
                }
            }

            @Override
            public void onFailure(Call<List<UserResult>> call, Throwable t) {
                Snackbar sb = Snackbar.make(mainView, "API Load Failed", Snackbar.LENGTH_LONG);
                sb.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sb.dismiss();
                    }
                });
                sb.show();
                t.printStackTrace();
            }
        });
    }
}