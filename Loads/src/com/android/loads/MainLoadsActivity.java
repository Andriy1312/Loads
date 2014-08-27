package com.android.loads;

import com.android.loads.provider.IUiController;
import com.android.loads.provider.UIController;
import com.android.loads.ui.FileStoreSectionFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;

public class MainLoadsActivity extends FragmentActivity implements FileStoreSectionFragment.OnFileStoreListener {

    private IUiController uiController;
    private String currentFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_loads);
        uiController = new UIController(this);
        if(savedInstanceState != null) {
            currentFolder = savedInstanceState.getString("pathFolder");
            uiController.setCurrentFolderPath(currentFolder);
        }
        uiController.setActivityUI(getSupportFragmentManager(),this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.context_menu_delete) {
            uiController.deleteFile();
            return true;
        }
        else
            return super.onContextItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("pathFolder", uiController.getCurrentFolderPath());
    }

    @Override
    public void onListFileClick(int position) {
        if(uiController != null)
            uiController.clickToFile(position);
    }

    @Override
    public void onLongListFileClick(int position) {
        uiController.setFileForDeletion(position);
    }

    public void loadClick(View view) {
        uiController.startDownloading();
    }

    public void onCancel(View view) {
        uiController.stopDownloading();
    }
}