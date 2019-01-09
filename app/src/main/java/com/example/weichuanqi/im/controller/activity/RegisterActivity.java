package com.example.weichuanqi.im.controller.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.weichuanqi.im.R;


import com.example.weichuanqi.im.utils.CheckInputIsNull;
import com.example.weichuanqi.im.utils.ToastUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;



public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_register_back;
    private EditText et_register_account;
    private EditText et_register_pwd;
    private Button btn_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //实例化控件
        initView();
        //实例化监听
        initListener();
    }
    //实例化监听
    private void initListener() {
        tv_register_back.setOnClickListener(this);
        btn_register.setOnClickListener(this);

    }

    //实例化控件
    private void initView() {
        tv_register_back = (TextView) findViewById(R.id.tv_register_back);
        et_register_account = (EditText) findViewById(R.id.et_register_account);
        et_register_pwd = (EditText) findViewById(R.id.et_register_pwd);
        btn_register = (Button) findViewById(R.id.btn_register_register);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_register_back:
                finish();
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                break;
            case R.id.btn_register_register:
                //注册账户
                register();
                break;
        }
    }

    //注册账户
    private void register() {

        final String account = et_register_account.getText().toString().trim();
        final String pwd = et_register_pwd.getText().toString().trim();

        boolean isNull = CheckInputIsNull.isNotNull(this,account,pwd);
           if (isNull){

               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       try {
                           EMClient.getInstance().createAccount(account,pwd);
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   ToastUtil.toast(RegisterActivity.this,"注册用户成功"+account);

                                   Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                   startActivity(intent);
                                   finish();
                               }
                           });



                       } catch (final HyphenateException e) {
                           e.printStackTrace();
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   ToastUtil.toast(RegisterActivity.this,e.getMessage());
                               }
                           });
                       }

                   }
               }).start();





           }


    }





}
