package com.rockduck.rocklibrary.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.rockduck.rocklibrary.R;

/**
 * news 的 ui<br/>
 * ricky <br/>
 * 2016-07-01
 */
public class NewsUI extends RelativeLayout {

    public NewsUI(Context context){
        super(context);
        init(context);
    }

    public NewsUI(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NewsUI(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init(Context context){
        inflate(context, R.layout.common_news,this);
    }



}
