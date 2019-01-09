package com.example.weichuanqi.im.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.model.bean.Conversation;
import com.example.weichuanqi.im.model.bean.User;

import java.util.List;

public class ConversationAdapter extends BaseAdapter {

    private Context mContext;
    private List<Conversation> mList;
    private LayoutInflater layoutInflater;
    private Conversation mConver;


    public ConversationAdapter(Context mContext,List<Conversation> list) {
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

       ViewHolderConversation conversation = null;
       mConver = mList.get(position);
       if (convertView == null){
           conversation = new ViewHolderConversation();
           convertView = layoutInflater.inflate(R.layout.conversation_item,null);
           conversation.tv_conversation = (TextView) convertView.findViewById(R.id.tv_conversations);
           conversation.tv_msg_msg = (TextView) convertView.findViewById(R.id.tv_msg_msg);
           conversation.is_group = (ImageView) convertView.findViewById(R.id.iv_conversations);

           convertView.setTag(conversation);
       }else {
           conversation = (ViewHolderConversation) convertView.getTag();
       }

       conversation.tv_conversation.setText(mConver.getReceive_name());
       //conversation.tv_msg_msg.setText(mConver.getMessage());
        if (mConver.isIs_group()){
            conversation.is_group.setImageResource(R.drawable.group_icon);
        }else if (!mConver.isIs_group()){
            conversation.is_group.setImageResource(R.drawable.image_male);
        }

       return convertView;
    }

    class ViewHolderConversation{
        private TextView tv_conversation;
        private TextView tv_msg_msg;
        private ImageView is_group;


    }


}
