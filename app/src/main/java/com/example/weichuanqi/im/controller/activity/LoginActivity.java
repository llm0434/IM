package com.example.weichuanqi.im.controller.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.controller.ui.CustomDialog;
import com.example.weichuanqi.im.model.Model;
import com.example.weichuanqi.im.model.bean.User;
import com.example.weichuanqi.im.utils.CheckInputIsNull;
import com.example.weichuanqi.im.utils.LogUtil;
import com.example.weichuanqi.im.utils.ToastUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;



public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_login_account;
    private EditText et_login_pwd;
    private TextView btn_login;
    private TextView tv_register;
    private TextView tv_forgetPwd;
    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EMClient.getInstance().chatManager().loadAllConversations();
        EMClient.getInstance().groupManager().loadAllGroups();

        //初始化控件
        initView();
        //实例化监听
        initListener();
    }

        //实例化监听
        void initListener() {
        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_forgetPwd.setOnClickListener(this);
    }

        //实例化控件
        void initView() {
        et_login_account  = (EditText) findViewById(R.id.et_login_account);
        et_login_pwd = (EditText) findViewById(R.id.et_login_pwd);
        btn_login = (TextView) findViewById(R.id.btn_login_login);
        tv_register = (TextView) findViewById(R.id.tv_login_register);
        tv_forgetPwd = (TextView) findViewById(R.id.tv_login_forgetPwd);

            dialog = new CustomDialog(this, 100, 100, R.layout.dialog_loding, R.style.Theme_dialog, Gravity.CENTER,R.style.pop_anim_style);
            dialog.setCancelable(false);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login_login:
                //登录
                login();
                break;
            case R.id.tv_login_register:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

                break;

            case R.id.tv_login_forgetPwd:
                Intent intent1 = new Intent(LoginActivity.this,ForgerPwdActivity.class);
                startActivity(intent1);
                break;
        }
    }

    //登录
    void login() {
        final String loginAccount = et_login_account.getText().toString().trim();
        final String loginPwd = et_login_pwd.getText().toString().trim();

        boolean isNotNull = CheckInputIsNull.isNotNull(this,loginAccount,loginAccount);
        if (isNotNull){
            dialog.show();
            EMClient.getInstance().login(loginAccount, loginPwd, new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            LogUtil.log("登录成功","成功");
                            dialog.dismiss();
                            //对模型层数据的处理
                            Model.getInstance().loginSuccess(new User(loginAccount));

                            //将用户信息保存到本地数据库
                            User user = new User(loginAccount);
                            Model.getInstance().getUserDao().addAccount(user);
                            //跳转到主页面
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(int code, final String error) {
                            dialog.dismiss();
                            LogUtil.log("登录失败",error);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.toast(LoginActivity.this,"用户名或者密码错误");
                                    ToastUtil.toast(LoginActivity.this,error);
                                }
                            });
                        }

                        @Override
                        public void onProgress(int progress, final String status) {
                            LogUtil.log("登录中！",status);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.toast(LoginActivity.this,status);
                                }
                            });
                        }
                    });




        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

}
