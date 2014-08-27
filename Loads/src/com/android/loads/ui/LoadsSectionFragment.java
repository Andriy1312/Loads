package com.android.loads.ui;

import com.android.loads.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class LoadsSectionFragment extends Fragment {

    private ProgressBar progressBar;
    private EditText editURLText;
    RelativeLayout loadLayout;
    RelativeLayout mainLayout;
    public LoadsSectionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.loads_download_layout, container, false);
        if(rootView != null) {
            progressBar = (ProgressBar)rootView.findViewById(R.id.progress_bar);
            editURLText = (EditText)rootView.findViewById(R.id.input_text_field);
            loadLayout =(RelativeLayout)rootView.findViewById(R.id.progress_layout);
            mainLayout =(RelativeLayout)rootView.findViewById(R.id.main_load_layout);
            if(savedInstanceState != null) {
                progressBar.setProgress(savedInstanceState.getInt("current_progress"));
                mainLayout.setVisibility(savedInstanceState.getInt("main_visibility"));
                loadLayout.setVisibility(savedInstanceState.getInt("load_visibility"));
            }
        }
        
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(progressBar != null) {
            outState.putInt("current_progress", progressBar.getProgress());
            outState.putInt("main_visibility", mainLayout.getVisibility());
            outState.putInt("load_visibility", loadLayout.getVisibility());
        }
    }
}