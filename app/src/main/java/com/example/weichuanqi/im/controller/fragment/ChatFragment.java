package com.example.weichuanqi.im.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.controller.activity.ChatActivity;
import com.example.weichuanqi.im.controller.activity.GroupChatActivity;
import com.example.weichuanqi.im.controller.activity.SearchActivity;
import com.example.weichuanqi.im.controller.adapter.ConversationAdapter;
import com.example.weichuanqi.im.model.bean.Conversation;
import com.example.weichuanqi.im.model.bean.User;
import com.example.weichuanqi.im.utils.Constant;
import com.example.weichuanqi.im.utils.LogUtil;
import com.example.weichuanqi.im.utils.SPUtil;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChatFragment extends Fragment implements EMMessageListener {

    private ListView lv_conversation;
    private EMConversation conversation;

    private List<Conversation> mList = new ArrayList<>();
    private ConversationAdapter adapter;
    private Conversation mConver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, null, false);
        //实例化控件
        initView(v);
        return v;
    }

    //实例化控件
    void initView(View v) {

        lv_conversation = (ListView) v.findViewById(R.id.lv_conversation);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EMClient.getInstance().chatManager().loadAllConversations();
        EMClient.getInstance().groupManager().loadAllGroups();
        //初始化数据
        initData();
        //实例化监听
        initListener();
    }

    // //初始化数据
    private void initData() {
        mList.clear();


        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        //先获取map集合的所有键的Set集合,keySet();
        Set<String> keySet = conversations.keySet();
        //有了Set集合。就可以获取其迭代器。
        Iterator<String> it = keySet.iterator();
        while(it.hasNext()) {
            String key = it.next();
            Log.d("===好友==",key);
            mConver = new Conversation();
            /*String chat_type = null;
            String from_id = null;
            String to_id = null;
            String message = null;*/
            String  chat_type = null;
            String group_name = null;
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(key);
            if (conversation == null){
                return;
            }
            List<EMMessage> messages = conversation.getAllMessages();
            if (messages != null && messages.size() >= 0){


                for (EMMessage msg:messages) {
                    chat_type = msg.getChatType().toString();
                    if (chat_type.equals("Chat")){
                        group_name = key;
                        mConver.setIs_group(false);
                    }else if (chat_type.equals("GroupChat")){
                        group_name = EMClient.getInstance().groupManager().getGroup(key).toString();
                        mConver.setIs_group(true);
                    }

                /*from_id = msg.getFrom();-
                to_id =  msg.getTo();
                message = ((EMTextMessageBody) msg.getBody()).getMessage();
                Log.d("====","来自=="+from_id+"to"+to_id+"聊天类型=="+chat_type+"消息=="+message);*/
                }


            }



            /*//有了键可以通过map集合的get方法获取其对应的值。
            List<EMMessage> messages = conversations.get(key).getAllMessages();
            Log.d("====集合长度====",messages.size()+"");
            for (EMMessage msg:messages) {
                String chat_type = msg.getChatType().toString();
                String from = msg.getFrom();
                String message = ((EMTextMessageBody) msg.getBody()).getMessage();
                Log.d("====","来自=="+from+"\\n"+"聊天类型=="+chat_type+"\\n"+"消息=="+message);
            }*/


            mConver.setReceive_name(group_name);
            mConver.setReceive_id(key);

            mConver.setChat_type(chat_type);
            mList.add(mConver);

            adapter = new ConversationAdapter(getActivity(),mList);
            lv_conversation.setAdapter(adapter);

        }




        /*conversation = EMClient.getInstance().chatManager().getConversation("a1");
        //获取此会话的所有消息
        List<EMMessage> messages = conversation.getAllMessages();
        for (EMMessage msg : messages) {

            Log.d("====gettype====", msg.getType().toString());
            Log.d("====getFrom====", msg.getFrom());
            Log.d("====getTo====", msg.getTo());
            Log.d("====getMsgId====", msg.getMsgId());
            Log.d("====getUserName====", msg.getUserName());
            Log.d("====getFrom====", msg.getMsgTime() + "");
            Log.d("====getFrom====", ((EMTextMessageBody)msg.getBody()).getMessage());

        }*/

    }

    //实例化监听
    void initListener() {



        lv_conversation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Conversation mConversation = mList.get(position);

                if (mConversation.getChat_type().equals("Chat")){
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra(Constant.CHAT_TYPE,Constant.CHAT);
                    intent.putExtra(Constant.TO_USER_ID,mConversation.getReceive_id());
                    intent.putExtra(Constant.TO_USER_NAME,mConversation.getReceive_name());
                    startActivity(intent);
                }else if (mConversation.getChat_type().equals("GroupChat")){
                    Intent intent = new Intent(getActivity(), GroupChatActivity.class);
                    intent.putExtra(Constant.CHAT_TYPE,Constant.GROUP_CHAT);
                    intent.putExtra(Constant.TO_GROUP_ID,mConversation.getReceive_id());
                    intent.putExtra(Constant.TO_GROUP_NAME,mConversation.getReceive_name());
                    startActivity(intent);
                }



            }
        });

    }

    @Override
    public void onMessageReceived(final List<EMMessage> messages) {

  /*      mList.clear();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                for (int i = 0; i < messages.size(); i++) {
                    EMMessage msg = messages.get(i);
                    User user = mList.get(i);
                    String name = user.getName();
                    String from = msg.getFrom();
                    if (TextUtils.isEmpty(name) && TextUtils.isEmpty(from)){
                        return;
                    }
                    if (name.equals(from)){
                        adapter = new ConversationAdapter(getActivity(),mList,true);
                        lv_conversation.setAdapter(adapter);
                    }

                }
            }
        };*/






    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {

    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> messages) {

    }

    @Override
    public void onMessageChanged(EMMessage message, Object change) {

    }

    @Override
    public void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(this);
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().addMessageListener(this);
    }
}
