package com.android.loads.data;

import android.webkit.MimeTypeMap;

public class MimeTypeHelper {

    private static final String[] MimeTypes = {"image/png","audio/mpeg", "text/plain", "application/pdf"};

    public static boolean isSupportedMimeType(String mimeType) {
        for(String type : MimeTypes)
            if(mimeType.equals(type))
                return true;
        return false;
    }

    public static String getFileMimeType(String url) {
        String mimeType = null;
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(url);
        if(fileExtension !=null) {
            MimeTypeMap mimeMap = MimeTypeMap.getSingleton();
            mimeType = mimeMap.getMimeTypeFromExtension(fileExtension);
        }
        return mimeType;
    }
}