package com.rockduck.rocklibrary.ui;

import android.app.Activity;
import android.content.Intent;

import com.rockduck.rocklibrary.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Landing PageUI<br/>
 * 可以設定icon及要跳轉的activity,還有跳轉秒數
 * ricky <br/>
 * 2016-07-06
 */
public class LandingUI {

    private Activity oriActivity;
    private Class<?> dstClass;


    public LandingUI(Activity oriActivity, Class<?> dstClass){
        this.oriActivity = oriActivity;
        this.dstClass = dstClass;
    }

    //預設800毫秒後跳轉
    private final static long DEFAULT_DELAY = 800;

    /**
     * 開始跑landing畫面,預設是0.8秒後跳轉
     */
    public void startLanding() {
        startLanding(DEFAULT_DELAY);
    }


    /**
     * 開始跑landing畫面,可自定義秒數
     * @param delay
     */
    public void startLanding(long delay) {
        Timer mTimer = new Timer(true);
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(oriActivity, dstClass);
                oriActivity.startActivity(intent);
                oriActivity.overridePendingTransition(R.anim.zoom_enter, 0);
                oriActivity.finish();
            }
        };

        mTimer.schedule(mTimerTask, delay);
    }
}
