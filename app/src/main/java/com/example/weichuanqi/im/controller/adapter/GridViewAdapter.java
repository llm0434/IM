package com.example.weichuanqi.im.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.model.bean.User;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {

    private Context mContext;
    private boolean mIsCanModify;
    private List<User> mUserList = new ArrayList<>();

    public boolean isDelete_model() {
        return delete_model;
    }
    //设置当前的删除模式
    public void setDelete_model(boolean delete_model) {
        this.delete_model = delete_model;
    }
    //获取当前的删除模式
    private boolean delete_model;
    private OnGroupContactListener mGroupContactListener;


    public GridViewAdapter(Context context,boolean isCanModify,OnGroupContactListener listener) {
        this.mContext = context;
        this.mIsCanModify = isCanModify;
        this.mGroupContactListener = listener;

    }



    /**
     *  刷新方法
     * @param list
     */
    public void refresh(List<User> list){
        if (list != null && list.size() >= 0){
            this.mUserList.clear();

            /**
             *    在群主的群详情页面，或有加号和减号，将加号和减号作为数据添加到集合中处理
             *    始终将数据添加到0的位置上，当新数据时就会依次递增下去，保证群成员始终在之前，添加和删除始终在之后
             *
             */
            //添加加号和减号
            User add = new User("add");
            User delete = new User("delete");

            mUserList.add(delete);
            mUserList.add(0,add);

            mUserList.addAll(0,list);


        }
        //刷新页面
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return mUserList == null ? 0:mUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserList.get(position);
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
            convertView = View.inflate(mContext, R.layout.group_contact,null);
            viewHolder.iv_group_contact_icon = (ImageView) convertView.findViewById(R.id.iv_group_contact_icon);
            viewHolder.iv_group_contact_delete = (ImageView) convertView.findViewById(R.id.iv_group_contact_delete);
            viewHolder.tv_group_contact_name = (TextView) convertView.findViewById(R.id.tv_group_contact_name);

            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //获取当前数据
        final User user = mUserList.get(position);

        //显示数据
        if (mIsCanModify){         // 是群主或者是获取的权限
            if (position == getCount() - 1){    //减号的处理
                if (delete_model){  //删除模式
                    convertView.setVisibility(View.INVISIBLE);
                }else {
                    convertView.setVisibility(View.VISIBLE);
                    viewHolder.iv_group_contact_icon.setImageResource(R.drawable.delete);
                    viewHolder.iv_group_contact_delete.setVisibility(View.GONE);
                    viewHolder.tv_group_contact_name.setVisibility(View.INVISIBLE);
                }


            }else if (position == getCount() - 2){  //加号的处理

                if (delete_model){//删除模式
                    convertView.setVisibility(View.INVISIBLE);
                }else {
                    convertView.setVisibility(View.VISIBLE);
                    viewHolder.iv_group_contact_icon.setImageResource(R.drawable.addition);
                    viewHolder.iv_group_contact_delete.setVisibility(View.GONE);
                    viewHolder.tv_group_contact_name.setVisibility(View.INVISIBLE);
                }
            }else {    //群成员的处理

                convertView.setVisibility(View.VISIBLE);
                viewHolder.tv_group_contact_name.setVisibility(View.VISIBLE);

                viewHolder.tv_group_contact_name.setText(user.getName());
                viewHolder.iv_group_contact_icon.setImageResource(R.drawable.image_male);

                if (delete_model){   //删除模式
                    viewHolder.iv_group_contact_delete.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.iv_group_contact_delete.setVisibility(View.GONE);
                }

            }

            //点击事件的处理
            if (position == getCount() - 1){    //减号的位置
                viewHolder.iv_group_contact_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!delete_model){
                            delete_model = true;
                            notifyDataSetChanged();
                        }
                    }
                });
            }else if (position == getCount() - 2){  //加号的位置
                viewHolder.iv_group_contact_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGroupContactListener.onAddGroupContact();
                    }
                });
            }else {
                viewHolder.iv_group_contact_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGroupContactListener.onDeleteGroupContact(user);
                    }
                });
            }

        }else {                     //群成员没有权限
            if (position == getCount() - 1 || position == getCount() - 2){      //加号和减号的位置
                convertView.setVisibility(View.GONE);
            }else {
                convertView.setVisibility(View.VISIBLE);

                viewHolder.tv_group_contact_name.setText(user.getName());
                //删除隐藏
                viewHolder.iv_group_contact_delete.setVisibility(View.GONE);
                viewHolder.iv_group_contact_icon.setImageResource(R.drawable.image_male);


            }
        }


        return convertView;
    }

    class ViewHolder{
        private ImageView iv_group_contact_icon;
        private ImageView iv_group_contact_delete;
        private TextView tv_group_contact_name;
    }

    public interface OnGroupContactListener{
        //添加群组成员
        void onAddGroupContact();

        //删除群组成员
        void onDeleteGroupContact(User user);
    }


}
