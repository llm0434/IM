package com.example.weichuanqi.im.controller.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
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
import com.example.weichuanqi.im.model.bean.Chat;
import com.example.weichuanqi.im.model.bean.Conversation;
import com.example.weichuanqi.im.utils.Constant;
import com.example.weichuanqi.im.utils.LogUtil;
import com.example.weichuanqi.im.utils.ToastUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, EMMessageListener {

    private TextView tv_chat_back;
    private TextView tv_chat_username;
    private ImageView iv_chat_msg_refresh;
    private EditText et_chat;
    private TextView tv_send_msg;
    private ListView lv_chat;
    private ChatListAdapter chatListAdapter;
    private List<Chat> mList = new ArrayList<>();
    private String userName;       //当前聊天对象
    private String userId;
    private String chat_type;
    private List<Chat> getConverList;
    private EMConversation conversation;
    private List<EMMessage> messages;
    private String lastMessage;
    private int msgCount;

    private final int take_photo = 1;
    private ImageView iv_pic;
    private Uri imageUri;

    private ImageView iv_chat_dynamic;
    private ImageView iv_chat_camera;
    private ImageView iv_chat_emoji;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //ToastUtil.toast(this, "这是单聊界面");
        EMClient.getInstance().chatManager().loadAllConversations();
        EMClient.getInstance().groupManager().loadAllGroups();
        //初始化控件
        initView();
        initConversation();
        //初始化数据
        initData();

        //点击事件
        initListener();
    }

    /**
     * 获取聊天记录
     */
    private void initConversation() {
        mList.clear();
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        Set<String> keySet = conversations.keySet();
        //有了Set集合。就可以获取其迭代器。
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()) {
            String key = it.next();
            Log.d("===好友==", key);
            conversation = EMClient.getInstance().chatManager().getConversation(key);
            if (conversation == null) {
                return;
            }
            //获取此会话的所有消息
            messages = conversation.getAllMessages();
            lastMessage = conversation.getLastMessage().getMsgId();
            msgCount = conversation.getAllMsgCount();

            if (messages != null && messages.size() >= 0) {

                for (final EMMessage msg : messages) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Chat chat = new Chat();

                            if (msg.getChatType().toString().equals("Chat")) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm:ss");// HH:mm:ss
                                //获取当前时间
                                Date date = new Date(msg.getMsgTime());
                                if (msg.getTo().equals(EMClient.getInstance().getCurrentUser().toString())) {
                                    chat.setType(ChatListAdapter.LEFT_FLAG);
                                    chat.setText(((EMTextMessageBody) msg.getBody()).getMessage());
                                    chat.setMsgTime(simpleDateFormat.format(date));
                                    chat.setMsgId(msg.getMsgId());
                                    mList.add(chat);
                                } else {
                                    chat.setType(ChatListAdapter.RIGHT_FLAG);
                                    chat.setText(((EMTextMessageBody) msg.getBody()).getMessage());
                                    chat.setMsgTime(simpleDateFormat.format(date));
                                    chat.setMsgId(msg.getMsgId());
                                    mList.add(chat);
                                }


                            }
                        }
                    });

                }

            }


        }

        chatListAdapter = new ChatListAdapter(this, mList);
        lv_chat.setAdapter(chatListAdapter);


    }

    //点击事件
    private void initListener() {
        tv_chat_back.setOnClickListener(this);
        iv_chat_msg_refresh.setOnClickListener(this);
        tv_send_msg.setOnClickListener(this);

        iv_chat_dynamic.setOnClickListener(this);
        iv_chat_camera.setOnClickListener(this);
        iv_chat_emoji.setOnClickListener(this);


    }

    //初始化控件
    private void initView() {
        tv_chat_back = (TextView) findViewById(R.id.tv_chat_back);
        tv_chat_username = (TextView) findViewById(R.id.tv_chat_username);
        iv_chat_msg_refresh = (ImageView) findViewById(R.id.iv_char_refresh);
        et_chat = (EditText) findViewById(R.id.et_chat);
        tv_send_msg = (TextView) findViewById(R.id.tv_send_msg);
        lv_chat = (ListView) findViewById(R.id.lv_chat);

        iv_chat_dynamic = (ImageView) findViewById(R.id.iv_chat_dynamic);
        iv_chat_camera = (ImageView) findViewById(R.id.iv_chat_camera);
        iv_chat_emoji = (ImageView) findViewById(R.id.iv_chat_emoji);





    }

    //初始化数据
    private void initData() {
        userId = getIntent().getStringExtra(Constant.TO_USER_ID);
        userName = getIntent().getStringExtra(Constant.TO_USER_NAME);
        chat_type = getIntent().getStringExtra(Constant.CHAT_TYPE);
        LogUtil.log("======", userName);
        //设置当前聊天对象的用户名
        tv_chat_username.setText(userName);
        //设置聊天listview适配器
        /*chatListAdapter = new ChatListAdapter(this, mList);
        lv_chat.setAdapter(chatListAdapter);*/
        chatListAdapter.notifyDataSetChanged();
        //滚动到底部
        lv_chat.setSelection(lv_chat.getBottom());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send_msg:
                //发送消息
                sendMsg();
                break;

            case R.id.iv_char_refresh:

                break;
            case R.id.tv_chat_back:
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;

            case R.id.iv_chat_dynamic:

                break;
            case R.id.iv_chat_camera:
                toCamera();
                break;
            case R.id.iv_chat_emoji:

                break;

        }
    }

    /**
     * 发送图片消息
     */
    private void sendPic() {
        EMMessage pic_message = EMMessage.createImageSendMessage(imageUri.getPath(), false, userId);
        EMClient.getInstance().chatManager().sendMessage(pic_message);



    }

    /**
     * 拍照方法
     */
    private void toCamera() {
        File outImage = new File(getExternalCacheDir(), "out_image.jpg");
        try {
            if (outImage.exists()) {
                outImage.delete();
            }
            outImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(this,"com.example.weichuanqi.im.controller.activity.ChatActivity",outImage);

        }else {
            imageUri = Uri.fromFile(outImage);
        }
        //启用相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,take_photo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case take_photo:
                if (resultCode == RESULT_OK){
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        iv_pic.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }



    }

    //发送消息
    private void sendMsg() {
        final String input = et_chat.getText().toString().trim();

        if (!TextUtils.isEmpty(input)) {
            et_chat.setText("");

            //发送消息
            sendMessage(input);

        }
    }

    //  发送消息
    private void sendMessage(final String input) {
        Chat chat = new Chat();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());

        chat.setText(input);
        chat.setType(ChatListAdapter.RIGHT_FLAG);
        chat.setReceive_id(userName);
        chat.setMsgTime(simpleDateFormat.format(date));

        mList.add(chat);

        //通过服务器发送消息
        EMMessage message = EMMessage.createTxtSendMessage(input, userId);
        message.setChatType(EMMessage.ChatType.Chat);
        EMClient.getInstance().chatManager().sendMessage(message);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int code, String error) {
                ToastUtil.toast(ChatActivity.this, "发送失败" + code + "," + error);
            }

            @Override
            public void onProgress(int progress, String status) {
                ToastUtil.toast(ChatActivity.this, "正在发送中" + progress + "," + status);
            }
        });

        chatListAdapter.notifyDataSetChanged();
        //滚动到底部
        lv_chat.setSelection(lv_chat.getBottom());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在不需要的时候移除listener
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }

    @Override
    public void onMessageReceived(List<EMMessage> messages) {
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
                    chat.setText(reply);
                    chat.setMsgTime(simpleDateFormat.format(date));
                    chat.setMsgId(msg.getMsgId());
                    mList.add(chat);
                    chatListAdapter.notifyDataSetChanged();
                    //滚动到底部
                    lv_chat.setSelection(lv_chat.getBottom());
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
    protected void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

}
