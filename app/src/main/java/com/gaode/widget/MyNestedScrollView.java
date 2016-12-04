package com.gaode.widget;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.ImageView;

/**
 * 当前类的注释:
 * 作者：WangLiJian on 2016/11/4.
 * 包名：demo.com.appbardemo.weight
 * 邮箱：wanglijian1214@163.com
 */

public class MyNestedScrollView extends NestedScrollView {
    /*无效的点*/
    private static final int INVALID_POINTER = -1;
    /*滑动动画执行的时间*/
    private static final int MIN_SETTLE_DURATION = 200; // ms
    /*定义了一个时间插值器，根据ViewPage控件来定义的*/
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };
    private final float mMaxScale = 2.0f;
    private final float REFRESH_SCALE = 1.20F;
    /*当前活动的点Id,有效的点的Id*/
    protected int mActivePointerId = INVALID_POINTER;
    /*头部View 的容器*/
    private AppBarLayout mHeaderContainer;
    /*头部View 的图片*/
    private ImageView mHeaderImg;
    /*屏幕的高度*/
    private int mScreenHeight;
    /*屏幕的宽度*/
    private int mScreenWidth;
    private int mHeaderHeight;
    /*记录上一次手指触摸的点*/
    private float mLastMotionX;
    private float mLastMotionY;
    /*开始滑动的标志距离*/
    private int mTouchSlop;
    private float mScale;
    private float mLastScale;
    private boolean isNeedCancelParent;
    private AbsListView.OnScrollListener mScrollListener;
    private OnRefreshListener mRefreshListener;
    private VelocityTracker velocityTracker;

    public MyNestedScrollView(Context context) {
        super(context);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
//        mHeaderContainer = new FrameLayout(context);
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        mScreenHeight = metrics.heightPixels;
//        mScreenWidth = metrics.widthPixels;
//        mHeaderHeight = (int) ((9 * 1.0f / 16) * mScreenWidth);
//        AbsListView.LayoutParams absLayoutParams = new AbsListView.LayoutParams(mScreenWidth, mHeaderHeight);
//        mHeaderContainer.setLayoutParams(absLayoutParams);
//        mHeaderImg = new ImageView(context);
//        FrameLayout.LayoutParams imgLayoutParams = new FrameLayout.LayoutParams
//                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        mHeaderImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        mHeaderImg.setLayoutParams(imgLayoutParams);
        //初始化速度探测器
        mHeaderContainer.addView(mHeaderImg);
    }
 /*处理事件用*/

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                /*计算 x，y 的距离*/
                int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                if (mActivePointerId == INVALID_POINTER)
                    break;
                mLastMotionX = MotionEventCompat.getX(ev, index);
                mLastMotionY = MotionEventCompat.getY(ev, index);
                Log.i("down", "=====weight:" + mLastMotionX + "==height:" + mLastMotionY);
                // 结束动画
//                abortAnimation();
                mLastScale = (this.mHeaderContainer.getMeasuredHeight() / this.mHeaderHeight);
                isNeedCancelParent = true;
                break;
            case MotionEvent.ACTION_MOVE:
                int indexMove = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, indexMove);
                if(mLastMotionY==0.0){
                    mLastMotionY=MotionEventCompat.getPointerId(ev, indexMove);
                }
                Log.i("move", "========移动");
                if (mActivePointerId == INVALID_POINTER) {
                    /*这里相当于松手*/
                    finishPull();
                    isNeedCancelParent = true;
                } else {
                    Log.i("move", "===成功");
                    Log.i("move", "=====mHeaderContainer.getMeasuredHeight():" + mHeaderContainer.getMeasuredHeight() + "===mHeaderHeight:" + mHeaderHeight + "=====bottom:" + mHeaderContainer.getBottom());
                    if (mHeaderContainer.getBottom() >= mHeaderHeight) {
                        Log.i("move", "=====开始扩大");
                        ViewGroup.LayoutParams params = this.mHeaderContainer
                                .getLayoutParams();
                        final float y = MotionEventCompat.getY(ev, indexMove);
                        float dy = y - mLastMotionY;
                        Log.i("move", "====dy:" + dy);
                        float f = ((y - this.mLastMotionY + this.mHeaderContainer
                                .getMeasuredHeight()) / this.mHeaderHeight - this.mLastScale)
                                / 2.0F + this.mLastScale;
                        if ((this.mLastScale <= 1.0D) && (f <= this.mLastScale)) {
                            params.height = this.mHeaderHeight;
                            this.mHeaderContainer
                                    .setLayoutParams(params);
                            return super.onTouchEvent(ev);
                        }
                        /*这里设置紧凑度*/
                        dy = dy * 0.5f * (mHeaderHeight * 1.0f / params.height);
                        mLastScale = (dy + params.height) * 1.0f / mHeaderHeight;
                        mScale = clamp(mLastScale, 1.0f, mMaxScale);
                        Log.v("zgy", "=======mScale=====" + mLastScale + ",f = " + f);

                        params.height = (int) (mHeaderHeight * mScale);
                        mHeaderContainer.setLayoutParams(params);
                        mLastMotionY = y;
                        if (isNeedCancelParent) {
                            isNeedCancelParent = false;
                            MotionEvent motionEvent = MotionEvent.obtain(ev);
                            motionEvent.setAction(MotionEvent.ACTION_CANCEL);
                            super.onTouchEvent(motionEvent);
                        }
                        return true;
                    } else {
                        //缩小
//                        Log.i("small","====bottom:"+mHeaderContainer.getBottom()+"===upHeight:"+upHieght+"====alphaHeight:"+alphaHeight);
//                        alpha = (alphaHeight-(mHeaderContainer.getBottom() - upHieght)) / alphaHeight; //获取模糊值 0-1
//                        Log.i("small","====alpha:"+alpha);
//                        onRefreshAlpha.setalpha(alpha);
                    }
                    Log.i("move", "=====停止");
                    mLastMotionY = MotionEventCompat.getY(ev, indexMove);

                }

                break;
            case MotionEvent.ACTION_UP:
                finishPull();

                break;
            case MotionEvent.ACTION_POINTER_UP:
                try {
                    velocityTracker.recycle(); //if velocityTracker won't be used should be recycled
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int pointUpIndex = MotionEventCompat.getActionIndex(ev);
                int pointId = MotionEventCompat.getPointerId(ev, pointUpIndex);
                if (pointId == mActivePointerId) {
                    /*松手执行结束拖拽操作*/
                    finishPull();
                }

                break;

        }

        return super.onTouchEvent(ev);
    }

    public ImageView getHeaderImageView() {
        return this.mHeaderImg;
    }

    private void abortAnimation() {

    }

    public void setHeadView(AppBarLayout v, int height, int upHiehgt) {
        this.mHeaderContainer = v;
        mHeaderHeight = height;
    }

    public void setOnRefreshListener(OnRefreshListener l) {
        mRefreshListener = l;
    }

    private void finishPull() {
        mActivePointerId = INVALID_POINTER;
        if (mHeaderContainer.getBottom() > mHeaderHeight) {
            Log.v("zgy", "===super====onTouchEvent========");
            if (mScale > REFRESH_SCALE) {
                if (mRefreshListener != null) {
                    mRefreshListener.onRefresh();
                }
            }
            pullBackAnimation();
        }
    }

    private float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    private void pullBackAnimation() {
        ValueAnimator pullBack = ValueAnimator.ofFloat(mScale, 1.0f);
        pullBack.setInterpolator(sInterpolator);
        pullBack.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                ViewGroup.LayoutParams params = mHeaderContainer.getLayoutParams();
                params.height = (int) (mHeaderHeight * value);
                Log.i("up", "=====value:" + value + "====height:" + params.height);
                mHeaderContainer.setLayoutParams(params);
            }
        });
        pullBack.setDuration((long) (MIN_SETTLE_DURATION * mScale));
        pullBack.start();

    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    private class InternalScrollerListener implements OnScrollChangeListener {

        @Override
        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

        }
    }


}
