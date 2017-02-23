package com.rockduck.rocklibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 跟網路相關的Utils
 *
 * @author Ricky
 */
public class NetworkUtils {

    private static NetworkUtils instance;

    private NetworkUtils() {

    }

    public static NetworkUtils getInstance() {
        if (instance == null) {
            instance = new NetworkUtils();
        }

        return instance;
    }

    /**
     * 判斷是否有網路
     *
     * @return
     */
    private boolean isNetworkEnable(NetworkInfo networkInfo) {
        boolean isNetworkEnable = false;
        if (networkInfo == null) {
            LogUtils.d("log", "networkInfo is null,please init networkInfo");
            return isNetworkEnable;
        }

        if (networkInfo == null || !networkInfo.isAvailable()) { // 判斷是否有網路
            // 無網路
            isNetworkEnable = false;
        } else {
            // 有網路
            isNetworkEnable = true;
        }

        return isNetworkEnable;
    }

    /**
     * 確認網路情況
     *
     * @return
     */
    public boolean isNetworkEnable(Activity activity) {
        if (activity == null) {
            return false;
        }

        Object obj = activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (obj == null) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return NetworkUtils.getInstance().isNetworkEnable(info);
    }
}
