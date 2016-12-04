package com.gaode.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gaode.R;
import com.gaode.adapter.DataAdapter;
import com.gaode.ui.activity.IndexActivity;
import com.gaode.utils.AdvanceDecoration;
import com.gaode.widget.MyNestedScrollView;
import com.gaode.widget.RollHeaderView;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前类的注释:
 * 作者：WangLiJian on 2016/11/15.
 * 包名：com.gaode.ui.fragment
 * 邮箱：wanglijian1214@163.com
 */

public class IndexFragment extends Fragment implements RollHeaderView.HeaderViewClickListener,
        MyNestedScrollView.OnRefreshListener, View.OnClickListener {
    private View v;
    private ImageView ivShow;
    private AppBarLayout appBarLayout;
    private MyNestedScrollView scrollView;
    private ProgressBar progress;
    private Context mContext;
    private RollHeaderView rollView;
    private RecyclerView rvData;
    private ImageView ivSeach;
    private LinearLayoutManager linearLayoutManager;
    private List<String> picList;
    private List<String> dataList;
    private float imageHeight;       //图片的高度
    private float toolbarHeight;   //toolbar的高度
    private float canMoveHeight;    //可滑动的高度
    private float alpha;            //透明度

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(mContext, "刷新成功", Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.GONE);
            return false;
        }
    });

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_index, container, false);
        initView();
        initData();
        return v;

    }

    /**
     * init data
     */
    private void initData() {
        picList = new ArrayList<>();
        dataList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            picList.add("aaaa");
        }
        rollView.setImgUrlData(picList);
        for (int i = 0; i < 30; i++) {
            dataList.add("测试数据:" + i);
        }
        DataAdapter adpter = new DataAdapter(mContext, dataList);
        rvData.setAdapter(adpter);

    }

    /**
     * init view
     */
    private void initView() {
        mContext = getContext();
        progress = (ProgressBar) v.findViewById(R.id.progress);
        scrollView = (MyNestedScrollView) v.findViewById(R.id.scrollview);
        appBarLayout = (AppBarLayout) v.findViewById(R.id.barlayout);
        ivShow = (ImageView) v.findViewById(R.id.iv_me);
        rollView = (RollHeaderView) v.findViewById(R.id.banner);
        rvData = (RecyclerView) v.findViewById(R.id.rv_fingerpost);
        ivSeach = (ImageView) v.findViewById(R.id.iv_seach);
        rvData.setNestedScrollingEnabled(false);
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvData.setLayoutManager(linearLayoutManager);
        rvData.addItemDecoration(new AdvanceDecoration(mContext, OrientationHelper.VERTICAL));
        rollView.setOnHeaderViewClickListener(this);
        ivShow.setOnClickListener(this);
        int w = ViewGroup.MeasureSpec.makeMeasureSpec(0, ViewGroup.MeasureSpec.UNSPECIFIED);
        int h = ViewGroup.MeasureSpec.makeMeasureSpec(0, ViewGroup.MeasureSpec.UNSPECIFIED);
        appBarLayout.measure(w, h);
        scrollView.setHeadView(appBarLayout, (int) (getResources().getDisplayMetrics().density * 230 + 0.5f), (int) (getResources().getDisplayMetrics().density * 68 + 0.5f));
        imageHeight = getResources().getDisplayMetrics().density * 230;
        toolbarHeight = getResources().getDisplayMetrics().density * 68;
        canMoveHeight = imageHeight - toolbarHeight;
        scrollView.setOnRefreshListener(this);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {//展开
                    rollView.refreshImageViewAlpha(0);
                    return;
                }
                alpha = (1 - (canMoveHeight - (verticalOffset * -1)) / canMoveHeight);

                rollView.refreshImageViewAlpha(alpha);
            }
        });
    }


    @Override
    public void onRefresh() {
        progress.setVisibility(View.VISIBLE);
        handler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    public void HeaderViewClick(int position) {

    }

    @Override
    public void onClick(View v) {
        ((IndexActivity) getActivity()).openDrawer();

    }
}
