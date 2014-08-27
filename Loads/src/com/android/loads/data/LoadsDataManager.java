package com.android.loads.data;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import android.content.Intent;
import android.net.Uri;

public class LoadsDataManager {

    private static final String DEFAULT_DOWNLOAD_PATH = "/Download";
    private OnLoadsDataListener loadsListener;
    private File rootFile;
    private File currentRootFile;
    private String currentDownloadFolder;

    public LoadsDataManager(String root) {
        currentRootFile = rootFile = new File(root);
    }

    public LoadsDataManager(File externalStorageDirectory) {
        currentRootFile = rootFile = externalStorageDirectory;
    }

    public void startFile(File file) {
        if(file.isDirectory()) {
            currentRootFile = file;
            if(loadsListener != null)
                loadsListener.onFolderChange();
        }
        else {
            Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
            String mime = getMimeType(file.getPath());
            if(isSupporttedMimeType(mime)) {
                openFileIntent.setDataAndType(Uri.fromFile(file), mime);
                if(loadsListener != null)
                    loadsListener.onFileStarted(openFileIntent);
            }
        }
    }

    public void backToPreviouseFolder() {
        if(currentRootFile != rootFile) {
            currentRootFile = currentRootFile.getParentFile();
            if(loadsListener != null)
                loadsListener.onFolderChange();
        }
    }

    public ArrayList<File> getFileList() {
        FileFilter mFolderFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if(pathname.canRead() && pathname.isDirectory() && !pathname.isHidden())
                    return true;
                return false;
            }
        };
        FileFilter mFileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if(pathname.canRead() && pathname.isFile() && !pathname.isHidden())
                    return true;
                return false;
            }
        };
        File[] folders = currentRootFile.listFiles(mFolderFilter);
        File[] files = currentRootFile.listFiles(mFileFilter);
        ArrayList<File> resultList = new ArrayList<File>();
        for(File file : folders)
            resultList.add(file);
        for(File file : files)
            resultList.add(file);
        return resultList;
    }

    public void removeFile(File file) {
        boolean isDelete = file.delete();
        if(loadsListener != null)
            loadsListener.onFileRemoved(isDelete);
    }

    public void setOnLoadDataListener(OnLoadsDataListener listener) {
        loadsListener = listener;
    }

    public void setDownloadsFolderPath(String url) {
        currentDownloadFolder = url;
    }

    public boolean isSupporttedMimeType(String mimeType) {
        return MimeTypeHelper.isSupportedMimeType(mimeType);
    }

    public String getMimeType(String url) {
        return MimeTypeHelper.getFileMimeType(url);
    }

    public String getRootFilePath() {
        return rootFile.getAbsolutePath();
    }

    public String getCurrentRootPath() {
        return currentRootFile.getAbsolutePath();
    }

    public String getDownloadFolderPath() {
        if(currentDownloadFolder != null)
            return currentDownloadFolder;
        return DEFAULT_DOWNLOAD_PATH;
    }

    public void setCurrentRootFile(String url) {
        currentRootFile = new File(url);
    }
}