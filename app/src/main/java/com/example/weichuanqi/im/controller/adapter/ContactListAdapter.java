package com.example.weichuanqi.im.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.model.bean.User;

import java.util.List;

public class ContactListAdapter extends BaseAdapter {


    private Context mContext;
    private List<User> mList;
    private LayoutInflater layoutInflater;
    private User user;

    public ContactListAdapter(Context mContext, List<User> list) {
        this.mContext = mContext;
        this.mList = list;
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
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

        ContactVH contactVH = null;
        user = mList.get(position);
        if (convertView == null) {
            contactVH = new ContactVH();
            convertView = layoutInflater.inflate(R.layout.contact_item, null);
            //contactVH.tv_tag = (TextView) convertView.findViewById(R.id.tv_cont_list_tag);
            contactVH.tv_name = (TextView) convertView.findViewById(R.id.tv_cont_list_name);
            //contactVH.linearLayout = (LinearLayout) convertView.findViewById(R.id.lin_cont_list_tag);

            convertView.setTag(contactVH);

        } else {
            contactVH = (ContactVH) convertView.getTag();
        }
        //判断

     /*   String[] tags = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        //1.获取name，判断是否为中文，是需要转换，否就不用转换
        //2.排序需要获取拼音，显示则显示中文
        for (int i = 0; i < mList.size(); i++) {

            String zimu = String.valueOf(mList.get(i).getId().toUpperCase().charAt(0));
            Log.i("====", "首字母===" + zimu);
            for (int j = 0; j < tags.length; j++) {

                if (zimu.equals(tags[j])) {
                    contactVH.linearLayout.setVisibility(View.GONE);

                } else {
                    contactVH.linearLayout.setVisibility(View.VISIBLE);
                    contactVH.tv_tag.setText(tags[j]);
                    contactVH.tv_name.setText(user.getName());
                }
            }

        }*/

            contactVH.tv_name.setText(user.getName());
            return convertView;
        }


        class ContactVH {
           // private TextView tv_tag;
            private TextView tv_name;
            //private LinearLayout linearLayout;


        }



}