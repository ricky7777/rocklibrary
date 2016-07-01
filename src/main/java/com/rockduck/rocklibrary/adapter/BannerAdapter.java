package com.rockduck.rocklibrary.adapter;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.rockduck.rocklibrary.R;
import com.rockduck.rocklibrary.RockConsts;
import com.rockduck.rocklibrary.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by ricky on 2016/1/24.<br/>
 * banner的adapter
 */
public class BannerAdapter extends BaseAdapter {
    //    private final static String ICON_PATH_DRAWABLE = "drawable://%1$s";
    private final LayoutInflater mInflater;
    private final DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private SparseArray<View> holder;
    private Handler handler;
    private JSONArray dataArray;

    public BannerAdapter(Activity activity, Handler myHandler) {
        this.handler = myHandler;
        this.mInflater = LayoutInflater.from(activity);

        ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        int maxKb = am.getMemoryClass() * 1024;
        int limitKb = maxKb / 8; // 1/8th of total ram
        // mLruCache = new LruCache<String, Bitmap>(limitKb);
        // LogUtils.d(this, "cache size1:%1$s,limitkb:%2$s", cacheSize, limitKb);

        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.loading_prompt) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.loading_prompt)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.loading_prompt) // 设置图片加载/解码过程中错误时候显示的图片
                // .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true)//
                .cacheOnDisk(true)//
                .imageScaleType(ImageScaleType.NONE)// 设置图片以如何的编码方式显示
                // .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                // .decodingOptions(opt)// 设置图片的解码配置
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                .build();// 构建完成
    }

    public void setData(JSONArray dataArray) {
        this.dataArray = dataArray;
    }


    @Override
    public int getCount() {
        if(dataArray == null){
            return 0;
        }
        int dataSize = dataArray.length();
        return dataSize;
    }

    @Override
    public Object getItem(int position) {
        Object obj = null;
        try {
            obj = dataArray.getJSONObject(position);
        } catch ( Exception ex ) {
            LogUtils.d(this, "get BannerItem exception:%1$s", ex);
        }
        return obj;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject item = (JSONObject) getItem(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.main_banner_item, null);
            holder = new SparseArray<View>();
            // holder.icon = (ImageView) convertView.findViewById(R.id.iv_guide_icon);
            // holder.text = (TextView) convertView.findViewById(R.id.tv_guide_number);
            convertView.setTag(holder);

        } else {
            holder = (SparseArray<View>) convertView.getTag();
        }

        try {
            //發http去要圖
            String bannerURL = item.getString(RockConsts.KEY_JSON_BANNER_LINK);
//            DownloadImgTask imgTask = new DownloadImgTask(activity, new BannerImgDownloadListener(convertView));
//            imgTask.execute(bannerURL);
            ImageView bannerView = (ImageView) ViewHolder.get(convertView, R.id.iv_banner);
            imageLoader.displayImage(bannerURL, bannerView, options);

            //設定click event
//            String title = item.getString(TosConsts.KEY_JSON_TITLE);
//            String link = item.getString(TosConsts.KEY_JSON_LINK);
//            String type = item.getString(TosConsts.KEY_JSON_NEWS_TYPE);
//            convertView.setOnClickListener(new BannerOnClickListener(link, type, title));
        } catch ( Exception ex ) {
            LogUtils.d(this, "banner adapter exception:%1$s", ex);
        }

        //set text
        try {
            String titleText = item.getString(RockConsts.KEY_JSON_TITLE);
            TextView title = ViewHolder.get(convertView, R.id.tv_banner_title);
            title.setText(titleText);
        } catch ( Exception ex ) {
            LogUtils.d(this, "set content error:%1$s", ex);
        }

        return convertView;
    }

    /**
     * ViewHolder,省略了keep view<br/>
     * 使用了get泛型
     */
    private static class ViewHolder {
        public static <T extends View> T get(View view, int id) {
            SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
            if (viewHolder == null) {
                viewHolder = new SparseArray<View>();
                view.setTag(viewHolder);
            }
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = view.findViewById(id);
                viewHolder.put(id, childView);
            }
            return (T) childView;
        }
    }

//    /**
//     * banner的事件listener
//     */
//    private class BannerOnClickListener implements View.OnClickListener {
//        private String link;
//        private String type;
//        private String title;
//
//        public BannerOnClickListener(String link, String type,String title) {
//            this.link = link;
//            this.type = type;
//            this.title = title;
//        }
//
//        @Override
//        public void onClick(View v) {
//            Message msg = handler.obtainMessage();
//            if (type.equals(RockConsts.KEY_JSON_NEWS_TYPE_SUB_BANNER)) {
//                msg.what = RockConsts.MSG_LOAD_WEBVIEW;
//            } else if (type.equals(RockConsts.KEY_JSON_NEWS_TYPE_YOUTUBE)) {
//                msg.what = RockConsts.MSG_SHOW_YOUTUBE;
//            }
//            Bundle data = new Bundle();
//            data.putString(RockConsts.KEY_BUNDLE_BANNER_TITLE, title);
//            data.putString(RockConsts.KEY_BUNDLE_BANNER_LINK, link);
//            msg.setData(data);
//            handler.sendMessage(msg);
//        }
//    }
}
