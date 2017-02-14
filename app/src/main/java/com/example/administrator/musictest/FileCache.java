package com.example.administrator.musictest;

import android.content.Context;

import java.io.File;

/**
 * Created by Gagan on 2/11/2017.
 */

public class FileCache {
    private File cacheDir;

    public FileCache(Context context) {
        if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(),"LazyList");
        }
        else {
            cacheDir = context.getCacheDir();
        }
        if(!cacheDir.exists()){
            cacheDir.mkdirs();
        }
    }

    public File getFile(String url) {
        String fileName = String.valueOf(url.hashCode());
        File f = new File(cacheDir,fileName);
        return f;
    }

    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }

}
