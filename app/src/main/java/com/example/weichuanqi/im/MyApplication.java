package com.example.weichuanqi.im;

import android.app.Application;
import android.content.Context;

import com.example.weichuanqi.im.model.EventListener;
import com.example.weichuanqi.im.model.Model;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;



public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化model
        Model.getInstance().init(this);
        //初始化环信SDK
        initSDK();

        //全局上下文
        mContext = this;


    }

    //初始化环信SDK
    void initSDK() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

        options.setAutoAcceptGroupInvitation(false);

        options.setAutoLogin(false);

        //初始化
       EMClient.getInstance().init(getApplicationContext(),options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);

        //全局监听
        EventListener eventListener = new EventListener(getApplicationContext());



    }

    //获取全局上下文
    public static Context getGlobalApplication(){

        return mContext;
    }


}
