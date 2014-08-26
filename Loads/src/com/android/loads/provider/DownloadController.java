package com.android.loads.provider;

import java.io.File;
import com.android.loads.MainLoadsActivity;
import com.android.loads.R;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.widget.ProgressBar;

class DownloadController implements IDownloadController {

    private DownloadManager downloadManager;
    private long enqueueID;
    private Context mContext;
    volatile boolean isRun;
    BroadcastReceiver receiver;
    public DownloadController(Context context) {
        mContext = context;
    }

    @Override
    public void startDownloading(String url, String destinationPath) {
        if(mContext != null) {
            downloadManager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            File file = new File(url);
            Request request = new Request(Uri.parse(url));
            request.setDestinationInExternalPublicDir(destinationPath, file.getName());
            request.allowScanningByMediaScanner();
            enqueueID = downloadManager.enqueue(request);
            final ProgressBar progressBar = (ProgressBar)((MainLoadsActivity)mContext).findViewById(R.id.progress_bar);

            new Thread(new Runnable() {

                    @Override
                    public void run() {
                        boolean downloading = true;
                        isRun = true;
                        while (downloading && isRun) {
                            DownloadManager.Query q = new DownloadManager.Query();
                            q.setFilterById(enqueueID);
                            Cursor cursor = downloadManager.query(q);
                            cursor.moveToFirst();
                            int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                                downloading = false;
                            }
                            final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);
                            ((MainLoadsActivity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(dl_progress);
                               }
                            });
                            cursor.close();
                        }
                    }
                }).start();
        }
    }

    @Override
    public void stopDownloading() {
      isRun = false;
      
      if(downloadManager != null) {
          downloadManager.remove(enqueueID);
      }
    }

    @Override
    public int getProgress() {
        return 0;
    }

    @Override
    public void registerDownloadReceiver() {
       receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                  long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    Query query = new Query();
                    query.setFilterById(enqueueID);
                    Cursor cursor = downloadManager.query(query);
                    if(cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if(DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                            String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        }
                    }
                }
            }
        };      
        mContext.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}