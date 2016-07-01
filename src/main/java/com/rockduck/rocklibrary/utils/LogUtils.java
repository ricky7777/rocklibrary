package com.rockduck.rocklibrary.utils;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/*
 * 印log的utils
 */
public class LogUtils {
    private static boolean DEBUG = true;

    public static void d(Object tag, String msg, Object... objects) {
        if (DEBUG) {
            String formatMsg = String.format(msg, objects);
            Log.d(tag.getClass().getSimpleName(), formatMsg);
        }
    }

    public static void d(Object tag, String msg, Exception ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        d(tag, msg, writer.toString());
    }

}