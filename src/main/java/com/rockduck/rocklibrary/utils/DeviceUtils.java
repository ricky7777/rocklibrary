package com.rockduck.rocklibrary.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 裝置相關的utils
 */
public class DeviceUtils {
    private WindowManager winMng;
    private static Resources res;

    public DeviceUtils(Context ctx) {
        this.res = ctx.getResources();
        this.winMng = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
    }


    public boolean isTablet() {
        // DisplayMetrics dm = new DisplayMetrics();
        // act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        // LogUtils.d(this, "widthPixels:%1$s,xdpi:%2$s,heightPixels:%3$s,ydpi:%4$s", dm.widthPixels, dm.xdpi, dm.heightPixels, dm.ydpi);
        // double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        // double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // LogUtils.d(this, "isTablet x:%1$s,y:%2$s", x, y);
        // double screenInches = Math.sqrt(x + y);
        // LogUtils.d(this, "Screen inches : " + screenInches);
        // return screenInches > 6.5;
        boolean xlarge = ((res.getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((res.getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public int getScreenWidthPx() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        winMng.getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    public int getScreenHeightPx() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        winMng.getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    /**
     * 小於4.1吋,被我歸為小畫面手機
     *
     * @return
     */
    public boolean isSmallScreen() {
        DisplayMetrics dm = new DisplayMetrics();
        winMng.getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        LogUtils.d(this, "Screen inches : " + screenInches);
        return screenInches < 4.1;
    }

    /**
     * 大於9吋以上,被我歸類為大平板
     *
     * @return
     */
    public boolean isXlargeScreen() {
        DisplayMetrics dm = new DisplayMetrics();
        winMng.getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        LogUtils.d(this, "Screen inches : " + screenInches);
        return screenInches > 9.0;
    }

    /**
     * 確認是否有SD card
     *
     * @return
     */
    public static boolean isSdPresent() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static String getExStoragePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public boolean isMdpi() {
        return getDensityDpi() == DisplayMetrics.DENSITY_MEDIUM;
    }

    public boolean isHdpi() {
        return getDensityDpi() == DisplayMetrics.DENSITY_HIGH;
    }

    public int getDensityDpi() {
        DisplayMetrics metrics = new DisplayMetrics();
        winMng.getDefaultDisplay().getMetrics(metrics);
        return metrics.densityDpi;
    }

    public boolean isLarge() {
        return (res.getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public boolean isXlarge() {
        return (res.getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * 4.4,4.4.2有bug<br/>
     * 運用此method判斷
     *
     * @return
     */
    public boolean isKitkat() {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt == Build.VERSION_CODES.KITKAT || sdkInt == Build.VERSION_CODES.KITKAT || sdkInt == Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLollipopAbove(){
        int sdkInt = Build.VERSION.SDK_INT;
        if(sdkInt >= Build.VERSION_CODES.LOLLIPOP){
            return true;
        }else{
            return false;
        }
    }
}
