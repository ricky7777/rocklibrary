package com.rockduck.rocklibrary.utils;

import android.content.Context;

import java.util.regex.Pattern;

/**
 * 常用的計算tool
 * 
 * @author Ricky
 * 
 */
public class MathUtils {

    /**
     * 將dp轉為px的計算method
     * 
     * @param dp
     * @param context
     * @return
     */
    public static int dp2px(int dp, Context context) {
        final float dip = context.getResources().getDisplayMetrics().density;
        return (int) (dp * dip + 0.5f);
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}
