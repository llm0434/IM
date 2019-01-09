package com.example.weichuanqi.im.controller.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.model.Model;
import com.example.weichuanqi.im.model.bean.User;
import com.hyphenate.chat.EMClient;



public class SplashActivity extends AppCompatActivity {


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //如果activity已经销毁掉 不做处理
            if (isFinishing()){
                return;
            }

            jump();
        }
    };

    private void jump() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (EMClient.getInstance().isLoggedInBefore()){
                    //已经登录过，获取用户信息
                    User user = Model.getInstance().getUserDao().getAccountById(EMClient.getInstance().getCurrentUser());
                    if (user == null){
                        Log.i("======","获取不到登录过的信息哦！必须登录！");
                        //跳转到登录界面
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                        finish();
                    }else {
                        Log.i("======","成功获取当前登录用户信息！");
                        Log.i("======","当前登录用户======"+user.getId());

                        //对模型层数据的处理
                        //Model.getInstance().loginSuccess(userInfo);

                        //跳转到主界面
                        startActivity(new Intent(SplashActivity.this,MainActivity.class));
                        finish();
                    }



                }else {
                    //没有登录过跳转到登录界面
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();


                }
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //发送延时消息
        handler.sendMessageDelayed(Message.obtain(),2000);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清除消息
        handler.removeCallbacksAndMessages(null);
    }
}
