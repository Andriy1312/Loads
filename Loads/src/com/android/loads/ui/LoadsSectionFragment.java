package com.android.loads.ui;

import com.android.loads.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoadsSectionFragment extends Fragment {

    public LoadsSectionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.loads_download_layout, container, false);
        return rootView;
    }
}