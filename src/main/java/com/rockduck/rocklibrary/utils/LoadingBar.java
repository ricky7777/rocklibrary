package com.rockduck.rocklibrary.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

/**
 * 載入的loading bar
 *
 * @author Ricky
 */
public class LoadingBar extends ProgressDialog {
    public static LoadingBar instance;
    private static Activity act;
    private ProgressDialog progressDialog;

    private LoadingBar(Activity context) {
        super(context);
    }

    public static void init(Activity activity) {
        if (instance == null) {
            act = activity;
            instance = new LoadingBar(activity);
        }
    }

    public void dialogShow(int loadingTextId) {
        String loadingText = act.getString(loadingTextId);
        dialogShow(loadingText);
    }

    public void dialogShow(String loadingText) {
        Context context = getContext();

        if (progressDialog == null) {
            if (!act.isFinishing()) {
                progressDialog = show(context, null, loadingText);
            }
        } else {
            if (!progressDialog.isShowing()) {
                // show完了,再new一個給他
                if (!act.isFinishing()) {
                    progressDialog = show(context, null, loadingText);
                }
            }
        }

        if (progressDialog != null) {
            progressDialog.setOnKeyListener(new MyKeyListener());
        }

    }

    public void dismissDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void setCustomMessage(String msg) {
        if (progressDialog != null) {
            progressDialog.setMessage(msg);
        }
    }

    public void setCustomMessage(int msgId){
        if(progressDialog!=null){
            String msg = act.getString(msgId);
            setCustomMessage(msg);
        }
    }

    private class MyKeyListener implements OnKeyListener {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            int action = event.getAction();
            if (keyCode == KeyEvent.KEYCODE_BACK && action == KeyEvent.ACTION_UP) {
                dialog.dismiss();
            }
            return false;
        }
    }

}
