package com.rockduck.rocklibrary.utils;

import android.content.res.Resources;
import android.os.Environment;

import java.io.File;

/**
 * Created by ricky on 2016/06/06.<br/>
 * 共用function放在此
 */
public class CommonUtils {

    public final static String IMG_ENDFIX_PNG = ".png";
    private final static String ICON_PATH_DRAWABLE = "drawable://%1$s";
    private final static String PREFIX_DRAWABLE = "drawable";
    //圖檔的前後贅字
    private final static String IMG_ENDFIX_JPG = ".jpg";
    private static Resources res;
    private static String packageName;
    //icon uri,image loader use
    private static String uriIconImg;

    private static String uriImg;
    //icon sd path
    private static String pathSDFolderIcon;
    //大圖 sd path
    private static String pathSDFolderImg;
    private static String folderName;

    /**
     * 取得icon在sd card內的path
     *
     * @return
     */
    public static String getPathSDFolderIcon() {
        return pathSDFolderIcon;
    }

    /**
     * 取得img在sd card內的path
     *
     * @return
     */
    public static String getPathSDFolderImg() {
        return pathSDFolderImg;
    }

    public static String getUriIconImg(){return uriIconImg;}
    public static String getUriImg(){return uriImg;}

    /**
     * 用圖檔的名字,取得resId<br/>
     *
     * @param drawableName
     * @return
     */
    public static int name2ResId(String drawableName) {
        if(res == null){
            return 0;
        }

        try {
            int resID = res.getIdentifier(drawableName, PREFIX_DRAWABLE, packageName);
            return resID;
        }catch (Exception ex){
            return 0;
        }
    }

    /**
     * 用圖檔名稱取得path,imageLoader使用
     *
     * @param drawableName
     * @return
     */
    public static String name2Path(String folderPath, String drawableName,String imgUri) {
        String path = "";

        //先取sdcard內的圖檔,組出相關path
        String iconFullPathSD = String.format("%1$s%2$s%3$s", folderPath, drawableName, IMG_ENDFIX_JPG);

//        LogUtils.d("log", "iconFullPathSD:%1$s", iconFullPathSD);
        // String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
        File iconFile = new File(iconFullPathSD);
        if (iconFile.exists()) {
            //檔案存在,將uri傳回去
            path = String.format("%1$s%2$s%3$s", imgUri, drawableName, IMG_ENDFIX_JPG);
//            LogUtils.d("log", "path sd:%1$s", path);
        } else {
            //檔案不存在,取得resId,轉為uri
            int resId = name2ResId(drawableName);
            if (resId == 0) {
                resId = name2ResId("thumb_0000");
                path = String.format(ICON_PATH_DRAWABLE, resId);
            } else {
                path = String.format(ICON_PATH_DRAWABLE, resId);
            }
//                        LogUtils.d("log","path resId:%1$s",path);
        }


        return path;
    }

    /**
     * 判斷檔案是
     *
     * @param filePath
     * @return
     */
    public static boolean isFileInSDCard(String filePath) {
        boolean isFileExist = false;
        File f = new File(filePath);
        if (f.exists()) {
            isFileExist = true;
        } else {
            //該檔案不存在,查看上層是否存在,不存在順便create
            File parentFile = f.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
        }
        return isFileExist;
    }

//    /**
//     * 取得大圖的full path
//     *
//     * @return
//     */
//    public static String getBigImgFullPath(String cardDisplayName) {
//        String imgPrefix = IMG_PREFIX;
//        String imgEndfix = IMG_ENDFIX_PNG;
//        String cardFileName = String.format("%1$s%2$s.%3$s", imgPrefix, cardDisplayName, imgEndfix);
//        String cardFullPath = String.format("%1$s/%2$s/%3$s/%4$s",getExStoragePath(), folderName,
//                sdFolderImg, cardFileName);
//        return cardFullPath;
//    }

    public static String getExStoragePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public void init(Resources res, String packageName, String folderName, String folderNameIcon, String folderNameImg) {
        this.res = res;
        this.packageName = packageName;
        this.folderName = folderName;
        this.uriIconImg = String.format("file:///mnt/sdcard/%1$s/%2$s/", folderName, folderNameIcon);
        this.uriImg = String.format("file:///mnt/sdcard/%1$s/%2$s/", folderName, folderNameImg);
        this.pathSDFolderIcon = String.format("%1$s/%2$s/%3$s/", Environment.getExternalStorageDirectory(), folderName, folderNameIcon);
        this.pathSDFolderImg = String.format("%1$s/%2$s/%3$s/", Environment.getExternalStorageDirectory(), folderName, folderNameImg);
    }

}
