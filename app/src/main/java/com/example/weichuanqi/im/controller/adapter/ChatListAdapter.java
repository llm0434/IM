package com.example.weichuanqi.im.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.model.bean.Chat;

import java.util.List;

public class ChatListAdapter extends BaseAdapter {

    //左边的item
    public  static final int LEFT_FLAG = 0;
    //右边的item
    public static  final int RIGHT_FLAG =1;



    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private Chat chat;
    private List<Chat> mList;


    public ChatListAdapter(Context mContext, List<Chat> list) {
        this.mContext = mContext;
        this.mList = list;
        //获取系统服务
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolderLeft vl_left = null;
        ViewHolderRight vl_right = null;
        //获取当前的type，区分数据的加载
        int type= getItemViewType(i);
        if (view  ==null){
            switch (type){
                case LEFT_FLAG:
                    vl_left = new ViewHolderLeft();
                    view =mLayoutInflater.inflate(R.layout.left_item,null);
                    vl_left.tv_chat_left = (TextView) view.findViewById(R.id.tv_left_text);
                    vl_left.tv_send_msg_time_left = (TextView) view.findViewById(R.id.tv_send_msg_time_left);
                    view.setTag(vl_left);
                    break;
                case RIGHT_FLAG:
                    vl_right = new ViewHolderRight();
                    view = mLayoutInflater.inflate(R.layout.right_item,null);
                    vl_right.tv_chat_right  = (TextView) view.findViewById(R.id.tv_right_text);
                    vl_right.tv_send_msg_time_right = (TextView) view.findViewById(R.id.tv_send_msg_time_right);
                    view.setTag(vl_right);
                    break;
            }
        }else {

            switch (type){
                case LEFT_FLAG:
                    vl_left = (ViewHolderLeft) view.getTag();
                    break;
                case RIGHT_FLAG:
                    vl_right = (ViewHolderRight) view.getTag();
                    break;

            }

        }


        //赋值
        Chat data  = mList.get(i);
        switch (type){
            case LEFT_FLAG:
                vl_left.tv_chat_left.setText(data.getText());
                vl_left.tv_send_msg_time_left.setText(String.valueOf(data.getMsgTime()));
                break;
            case RIGHT_FLAG:
                vl_right.tv_chat_right.setText(data.getText());
                vl_right.tv_send_msg_time_right.setText(String.valueOf(data.getMsgTime()));
                break;

        }

        return view;
    }


    //根据数据源的postion现实item
    @Override
    public int getItemViewType(int position) {
       Chat chat = mList.get(position);
       int type = chat.getType();
       return  type;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }


    class ViewHolderLeft{
        public TextView tv_chat_left;
        public TextView tv_send_msg_time_left;
    }
    class ViewHolderRight{
        public TextView tv_chat_right;
        public TextView tv_send_msg_time_right;
    }




}
