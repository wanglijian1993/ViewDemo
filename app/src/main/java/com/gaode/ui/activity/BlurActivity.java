package com.gaode.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import com.gaode.R;
import com.gaode.widget.RlurView;

/**
 * 当前类的注释: Android高斯模糊效果
 * 作者：WangLiJian on 2016/11/15.
 * 包名：com.gaode
 * 邮箱：wanglijian1214@163.com
 */

public class BlurActivity extends AppCompatActivity {

    private RlurView view;
    private String url = "http://192.168.1.63:8080/medicine/userfiles/1/images/photo/2016/10/57aa8adf0cf246d08b4ae9d0_1920x606!.jpg";
    private float height;   //总高
    private DisplayMetrics displayMetrics;
    private int level = 0; //0-25
    private int i;       //总高/25

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blur);
        view = (RlurView) findViewById(R.id.rlur_view);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        i = 1280 / 25;
        view.setImageView(url);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                int level = (int) (event.getRawY() / i);
                Log.i("move","==level:"+level);
                view.setBlurredLevel(level);
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(event);
    }
}
