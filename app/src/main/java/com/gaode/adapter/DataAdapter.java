package com.gaode.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * 当前类的注释:
 * 作者：WangLiJian on 2016/11/15.
 * 包名：com.gaode.adapter
 * 邮箱：wanglijian1214@163.com
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private LayoutInflater mLayout;
    private Context mContext;
    private List<String> strList;

    public DataAdapter(Context mContext, List<String> strList) {
        this.mContext = mContext;
        this.strList = strList;
        mLayout = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayout.inflate(android.R.layout.test_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv.setText(strList.get(position));
    }

    @Override
    public int getItemCount() {
        return strList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
