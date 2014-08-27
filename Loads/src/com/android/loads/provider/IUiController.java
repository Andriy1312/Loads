package com.android.loads.provider;

import com.android.loads.ui.FileStoreSectionFragment;
import android.support.v4.app.FragmentManager;

public interface IUiController {

    void setActivityUI(FragmentManager fragmentManager, FileStoreSectionFragment.OnFileStoreListener listener);

    void clickToFile(int position);

    void setFileForDeletion(int fileId);

    void deleteFile();

    void startDownloading();

    void stopDownloading();

    void setCurrentFolderPath(String url);

    String getCurrentFolderPath();
}