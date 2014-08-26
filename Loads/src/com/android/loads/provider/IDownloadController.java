package com.android.loads.provider;

interface IDownloadController {

    void registerDownloadReceiver();

    void startDownloading(String url, String destinationPath);

    void stopDownloading();

    int getProgress();
}