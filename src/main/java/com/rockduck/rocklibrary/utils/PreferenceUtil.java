package com.rockduck.rocklibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.rockduck.rocklibrary.RockConsts;

/**
 * 偏好設定Utils
 * 
 * @author Ricky
 */
public class PreferenceUtil {
    private static SharedPreferences sharedPref;

    public static void init(Context context) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    // // 取得預設的偏好設定
    // private static SharedPreferences getSharedPreferences(Context context) {
    // return PreferenceManager.getDefaultSharedPreferences(context);
    // }

    public static int getSharedInt(String key) {
        if (sharedPref == null) {
            return 0;
        } else {
            return sharedPref.getInt(key, 0);
        }
    }

    public static String getString(String key) {
        if (sharedPref == null) {
            return "";
        } else {
            return sharedPref.getString(key, "");
        }
    }

    public static Boolean getBoolean(String key) {
        if (sharedPref == null) {
            return false;
        } else {
            return sharedPref.getBoolean(key, false);
        }
    }

    public static void saveInt(String key, int value) {
        if (sharedPref != null) {
            Editor editor = sharedPref.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }

    public static void saveString(String key, String value) {
        if (sharedPref != null) {
            Editor editor = sharedPref.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public static void saveLong(String key, long value) {
        if (sharedPref != null) {
            Editor editor = sharedPref.edit();
            editor.putLong(key, value);
            editor.commit();
        }
    }

    public static void saveBoolean(String key) {
        if (sharedPref != null) {
            Editor editor = sharedPref.edit();
            editor.putBoolean(key, true);
            editor.commit();
        }
    }

    /**
     * 取得是否給評價
     * 
     * @return
     */
    public static boolean getRatingState(String key) {
        if (sharedPref == null) {
            return false;
        } else {
            return sharedPref.getBoolean(key, false);
        }
    }

    public static long getLong(String key) {
        if (sharedPref == null) {
            return 0;
        } else {
            return sharedPref.getLong(key, 0);
        }
    }

    public static int getInt(String key) {
        if (sharedPref == null) {
            return 0;
        } else {
            return sharedPref.getInt(key, 0);
        }
    }

    /**
     * 記錄已經rating
     * 
     */
    public static void saveRatingState(String key) {
        if (sharedPref != null) {
            Editor editor = sharedPref.edit();
            editor.putBoolean(key, true);
            editor.commit();
        }
    }

    // 取得開app次數
    public static int getViewTime() {
        if (sharedPref == null) {
            return 0;
        } else {
            return sharedPref.getInt(RockConsts.PKEY_VIEW_TIME, 0);
        }
    }

    /**
     * 增加遊戲次數
     * 
     */
    public static void addViewTime() {
        int playtime = getViewTime() + 1;
        if (sharedPref != null) {
            Editor editor = sharedPref.edit();
            editor.putInt(RockConsts.PKEY_VIEW_TIME, playtime);
            editor.commit();
        }
    }

    public static void saveFBRatingState() {
        if (sharedPref != null) {
            Editor editor = sharedPref.edit();
            editor.putBoolean(RockConsts.PKEY_FB_RANK_STATE, true);
            editor.commit();
        }
    }

    /**
     * 取得是否有顯示過fb rate
     *
     * @return
     */
    public static boolean getFBRatingState() {
        if (sharedPref == null) {
            return false;
        } else {
            return sharedPref.getBoolean(RockConsts.PKEY_FB_RANK_STATE, false);
        }
    }


}
