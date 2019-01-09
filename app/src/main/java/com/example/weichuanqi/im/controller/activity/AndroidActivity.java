package com.example.weichuanqi.im.controller.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.controller.adapter.AndroidListAdapter;
import com.example.weichuanqi.im.controller.adapter.ChatListAdapter;
import com.example.weichuanqi.im.model.bean.Chat;
import com.example.weichuanqi.im.utils.Constant;
import com.example.weichuanqi.im.utils.ShareUtils;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AndroidActivity extends AppCompatActivity{

    private ListView mChatListView;

    private List<Chat> mList = new ArrayList<>();
    private AndroidListAdapter adapter;
    private TextView tv_android_back;

    //输入框
    private EditText et_text;
    //发送按钮
    private Button btn_send;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.android_activity);

        initView();
        initListener();
        initData();

    }

    private void initData() {
        //设置适配器
        adapter = new AndroidListAdapter(AndroidActivity.this, mList);
        mChatListView.setAdapter(adapter);

        addLeftItem("我是您的智能聊天机器人");

    }

    private void initListener() {

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //1.获取输入框的内容
                String text = et_text.getText().toString();
                //2.判断是否为空
                if (!TextUtils.isEmpty(text)) {
                    //3.判断长度不能大于30
                    if (text.length() > 30) {
                        Toast.makeText(AndroidActivity.this, "输入文字超出限制", Toast.LENGTH_SHORT).show();
                    } else {
                        //4.清空当前的输入框
                        et_text.setText("");
                        //5.添加你输入的内容到right item
                        addRightItem(text);
                        //6.发送给机器人请求返回内容
                        String url = "http://op.juhe.cn/robot/index?info=" + text
                                + "&key=" + Constant.CHAT_LIST_KEY;
                        RxVolley.get(url, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                //Toast.makeText(getActivity(), "Json:" + t, Toast.LENGTH_SHORT).show();
                                //L.i("Json" + t);
                                parsingJson(t);
                            }
                        });
                    }
                } else {
                    Toast.makeText(AndroidActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_android_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });

    }

    private void initView() {
        mChatListView = (ListView)findViewById(R.id.mChatListView);
        et_text = (EditText)findViewById(R.id.et_android_text);
        btn_send = (Button)findViewById(R.id.btn_android_send);

        tv_android_back = (TextView) findViewById(R.id.tv_android_back);
    }


    //解析Json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObhect = new JSONObject(t);
            JSONObject jsonresult = jsonObhect.getJSONObject("result");
            //拿到返回值
            String text = jsonresult.getString("text");
            //7.拿到机器人的返回值之后添加在left item
            addLeftItem(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //添加左边文本
    private void addLeftItem(String text) {

      /*  boolean isSpeak = ShareUtils.getBoolean(AndroidActivity.this, "isSpeak", false);
        if (isSpeak) {
            startSpeak(text);
        }*/

        Chat date = new Chat();
        date.setType(AndroidListAdapter.VALUE_LEFT_TEXT);
        date.setText(text);
        mList.add(date);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());
    }

    //添加右边文本
    private void addRightItem(String text){

        Chat date = new Chat();
        date.setType(AndroidListAdapter.VALUE_RIGHT_TEXT);
        date.setText(text);
        mList.add(date);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
}
