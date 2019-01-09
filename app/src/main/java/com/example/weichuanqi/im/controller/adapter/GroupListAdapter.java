package com.example.weichuanqi.im.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.model.bean.Group;
import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.List;

public class GroupListAdapter extends BaseAdapter {

    private Context mContext;
    private List<EMGroup> mList ;
    private LayoutInflater layoutInflater;


    public GroupListAdapter(Context context,List<EMGroup> list) {
        this.mContext = context;
        this.mList = list;
        this.layoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return mList == null ? 0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.group_list,null);
            viewHolder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        EMGroup emGroup = mList.get(position);
        viewHolder.tv_group_name.setText(emGroup.getGroupName());
        return convertView;
    }

    class ViewHolder{
        private TextView tv_group_name;
    }

}
