package com.wangoose.ojt_whc_java;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentBookmark extends Fragment {

    View bookmarkView;

    TextView tvBookmarkInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView =  (ViewGroup) inflater.inflate(R.layout.fragment_bookmark, container, false);

        bookmarkView = rootView.findViewById(R.id.view_fragment_bookmark);

        tvBookmarkInfo = rootView.findViewById(R.id.tvBookmarkInfo);



        return rootView;
    }
}
