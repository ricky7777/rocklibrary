package com.rockduck.rocklibrary.task;

import android.os.AsyncTask;
import android.os.Handler;

import com.rockduck.rocklibrary.utils.HttpUtils;

/**
 * Created by ricky on 2016/7/17.
 */
public class DownloadFileTask extends AsyncTask<String, Void, Void> {
    private final static int IDX_FILE_URL = 0;
    private final static int IDX_FILE_DST_PATH = 1;

    private Handler handler;
    private boolean isNeedNotify;
    private int what;

    public DownloadFileTask(Handler handler, boolean isNeedNotify,int what) {
        this.handler = handler;
        this.isNeedNotify = isNeedNotify;
        this.what = what;
    }

    @Override
    protected Void doInBackground(String... params) {
        String url = params[IDX_FILE_URL];
        String fileDstPath = params[IDX_FILE_DST_PATH];
        HttpUtils.getFile(url, fileDstPath);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(isNeedNotify){
            handler.sendEmptyMessage(what);
        }

    }
}
