package com.gaode.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.gaode.utils.BlurBitmapUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * 当前类的注释:
 * 作者：WangLiJian on 2016/11/15.
 * 包名：com.gaode.widget
 * 邮箱：wanglijian1214@163.com
 */

public class RlurView extends ImageView {

        /*========== 全局相关 ==========*/

    //上下文对象
    private Context mContext;
    //透明最大值
    private static final int ALPHA_MAX_VALUE = 255;
    //最大模糊度(在0.0到25.0之间)
    private static final float BLUR_RADIUS = 25f;
    private ImageView ivOriginal;      //原始图片
    //原图Bitmap
    private Bitmap mOriginBitmap;
    //模糊后的Bitmap
    private Bitmap mBlurredBitmap;
    private ImageView iv;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            return false;
        }
    });

    public RlurView(Context context) {
        super(context);
        mContext = context;
    }

    public RlurView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public RlurView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void setImageView(String url) {
        iv = this;
        ivOriginal = new ImageView(mContext);
        Picasso.with(mContext).load(url).into(ivOriginal, new Callback() {
            @Override
            public void onSuccess() {
                mOriginBitmap = ((BitmapDrawable) ivOriginal.getDrawable()).getBitmap();
                iv.setImageBitmap(mOriginBitmap);
                mBlurredBitmap = BlurBitmapUtil.blurBitmap(mContext, mOriginBitmap, 1);
                iv.setImageBitmap(mBlurredBitmap);
            }

            @Override
            public void onError() {

            }
        });

    }

    @SuppressWarnings("deprecation")
    public void setBlurredLevel(int level) {
        //超过模糊级别范围 直接抛异常
        if (level < 0 || level > 25) {
            throw new IllegalStateException("No validate level, the value must be 0~100");
        }
        mBlurredBitmap = BlurBitmapUtil.blurBitmap(mContext, mOriginBitmap, level);
        this.setImageBitmap(mBlurredBitmap);
    }
}
