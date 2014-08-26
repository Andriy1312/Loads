package com.android.loads.data;

import android.content.Intent;

public interface OnLoadsDataListener {

    public void onFileStarted(Intent intent);

    public void onFolderChange();

    public void onFileSaved();

    public void onFileRemoved(boolean isRemoved);

    public void onErrorMessage();
}