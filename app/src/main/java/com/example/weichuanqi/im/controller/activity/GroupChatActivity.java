package com.example.weichuanqi.im.controller.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.controller.adapter.ChatListAdapter;
import com.example.weichuanqi.im.controller.adapter.GroupChatListAdapter;
import com.example.weichuanqi.im.model.bean.Chat;
import com.example.weichuanqi.im.utils.Constant;
import com.example.weichuanqi.im.utils.ToastUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GroupChatActivity extends AppCompatActivity implements View.OnClickListener, EMMessageListener {

    private ListView lv_group_chat;
    private TextView tv_group_back;
    private TextView tv_group_name;
    private ImageView iv_group_more_detail;
    private EditText et_group_chat;
    private TextView tv_group_send;
    private String chatType;
    private String groupId;
    private String groupName;
    private List<Chat> mList = new ArrayList<>();
    private GroupChatListAdapter chatListAdapter;

    private EMConversation conversation;
    private List<EMMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        //ToastUtil.toast(this, "这是群聊界面");
        //初始化控件
        initView();
        initConversation();
        initData();
        initListener();
    }

    private void initConversation() {
        mList.clear();
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        Set<String> keySet = conversations.keySet();
        //有了Set集合。就可以获取其迭代器。
        Iterator<String> it = keySet.iterator();
        while(it.hasNext()) {
            String key = it.next();
            Log.d("===好友==", key);
            conversation = EMClient.getInstance().chatManager().getConversation(key);
            if (conversation == null) {
                return;
            }
            //获取此会话的所有消息
            messages = conversation.getAllMessages();

            if (messages != null && messages.size() >= 0) {

                for (final EMMessage msg: messages) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Chat chat = new Chat();

                            if (msg.getChatType().toString().equals("GroupChat")){
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm:ss");// HH:mm:ss
                                //获取当前时间
                                Date date = new Date(msg.getMsgTime());
                                if (msg.getTo().equals(EMClient.getInstance().getCurrentUser().toString())){
                                    chat.setType(ChatListAdapter.RIGHT_FLAG);
                                    chat.setText(((EMTextMessageBody) msg.getBody()).getMessage());
                                    chat.setMsgTime(simpleDateFormat.format(date));
                                    chat.setMsgId(msg.getMsgId());
                                    chat.setReceive_id(groupId);
                                    chat.setSend_id(EMClient.getInstance().getCurrentUser());
                                    mList.add(chat);
                                }else {
                                    chat.setType(ChatListAdapter.LEFT_FLAG);
                                    chat.setText(((EMTextMessageBody) msg.getBody()).getMessage());
                                    chat.setMsgTime(simpleDateFormat.format(date));
                                    chat.setMsgId(msg.getMsgId());
                                    chat.setReceive_id(groupId);
                                    chat.setSend_id(EMClient.getInstance().getCurrentUser());
                                    mList.add(chat);
                                }



                            }
                        }
                    });

                }

            }



        }


        chatListAdapter = new GroupChatListAdapter(GroupChatActivity.this, mList);
        lv_group_chat.setAdapter(chatListAdapter);


    }

    private void initListener() {
        tv_group_back.setOnClickListener(this);
        tv_group_send.setOnClickListener(this);
        iv_group_more_detail.setOnClickListener(this);
    }

    private void initData() {
        chatType = getIntent().getStringExtra(Constant.CHAT_TYPE);
        groupId = getIntent().getStringExtra(Constant.TO_GROUP_ID);
        groupName = getIntent().getStringExtra(Constant.TO_GROUP_NAME);
        tv_group_name.setText(groupName);
        Log.d("======", "group_id=" + groupId);

        chatListAdapter.notifyDataSetChanged();
        //滚动到底部
        lv_group_chat.setSelection(lv_group_chat.getBottom());


    }

    private void initView() {
        lv_group_chat = (ListView) findViewById(R.id.lv_group_chat);
        tv_group_back = (TextView) findViewById(R.id.tv_group_back);
        iv_group_more_detail = (ImageView) findViewById(R.id.iv_group_more_detail);

        tv_group_name = (TextView) findViewById(R.id.tv_group_title_name);
        et_group_chat = (EditText) findViewById(R.id.et_group_chat);
        tv_group_send = (TextView) findViewById(R.id.tv_group_msg);
    }


    @Override
    protected void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_group_back:
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;

            case R.id.iv_group_more_detail:
                Intent in = new Intent(this,GroupMoreDetail.class);
                in.putExtra("group_more_id",groupId);
                startActivity(in);
                break;
            case R.id.tv_group_msg:
                sendMessage();
                break;
        }
    }

    void toGroupMore() {


    }

    private void sendMessage() {
        String input = et_group_chat.getText().toString().trim();
        if (!TextUtils.isEmpty(input)) {
            et_group_chat.setText("");

            //发送消息
            sendMsg(input);

        }


    }

    private void sendMsg(String input) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());

        Chat chat = new Chat();
        chat.setText(input);
        chat.setType(ChatListAdapter.RIGHT_FLAG);
        chat.setReceive_id(groupId);
        chat.setSend_id(EMClient.getInstance().getCurrentUser());
        chat.setMsgTime(simpleDateFormat.format(date));
        mList.add(chat);

        //通过服务器发送消息
        EMMessage message = EMMessage.createTxtSendMessage(input, groupId);
        message.setChatType(EMMessage.ChatType.GroupChat);
        EMClient.getInstance().chatManager().sendMessage(message);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int code, String error) {
                ToastUtil.toast(GroupChatActivity.this, "发送失败" + code + "," + error);
            }

            @Override
            public void onProgress(int progress, String status) {
                ToastUtil.toast(GroupChatActivity.this, "正在发送中" + progress + "," + status);
            }
        });

        chatListAdapter.notifyDataSetChanged();
        //滚动到底部
        lv_group_chat.setSelection(lv_group_chat.getBottom());


    }

    @Override
    public void onMessageReceived(final List<EMMessage> messages) {

        //收到消息
        for (final EMMessage msg : messages) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String reply = ((EMTextMessageBody) msg.getBody()).getMessage();
                    Chat chat = new Chat();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm:ss");// HH:mm:ss
                    //获取当前时间
                    Date date = new Date(msg.getMsgTime());
                    chat.setType(ChatListAdapter.LEFT_FLAG);
                    chat.setSend_id(msg.getFrom());
                    chat.setText(reply);
                    chat.setMsgTime(simpleDateFormat.format(date));
                    mList.add(chat);
                    chatListAdapter.notifyDataSetChanged();
                    //滚动到底部
                    lv_group_chat.setSelection(lv_group_chat.getBottom());
                }
            });

        }

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
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
}
