package com.example.weichuanqi.im.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.controller.activity.AboutusActivity;
import com.example.weichuanqi.im.controller.activity.AndroidActivity;
import com.example.weichuanqi.im.controller.activity.LoginActivity;
import com.example.weichuanqi.im.controller.activity.WeChatActivity;
import com.example.weichuanqi.im.controller.activity.WebViewActivity;
import com.example.weichuanqi.im.utils.LogUtil;
import com.example.weichuanqi.im.utils.ToastUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import static android.app.Activity.RESULT_OK;


public class SettingFragment extends Fragment {

    private TextView btn_logout;
    private TextView tv_setting_username;
    private TextView tv_setting_abouus;


    private TextView tv_setting_wechat;
    private TextView tv_setting_android;
    private TextView tv_setting_saoyisao;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, null, false);

        //实例化控件
        initView(v);
        return v;
    }

    //实例化控件
    void initView(View v) {
        btn_logout = (TextView) v.findViewById(R.id.btn_set_logout);
        tv_setting_username = (TextView) v.findViewById(R.id.tv_setting_username);
        tv_setting_abouus = (TextView) v.findViewById(R.id.tv_setting_aboutus);



        tv_setting_wechat = (TextView) v.findViewById(R.id.tv_setting_wechat);
        tv_setting_android = (TextView) v.findViewById(R.id.tv_setting_android);
        tv_setting_saoyisao = (TextView) v.findViewById(R.id.tv_setting_saoyisao);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化数据
        initData();
        //实例化监听
        initListener();
    }

    //初始化数据
    void initData() {
        String username = EMClient.getInstance().getCurrentUser();
        tv_setting_username.setText(username);
    }

    //实例化监听
    void initListener() {
        //退出登录
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().logout(false, new EMCallBack() {
                            @Override
                            public void onSuccess() {

                                //关闭数据库
                                //    Model.getInstance().getdBhelper().close();

                                LogUtil.log("退出成功", "成功");
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();

                            }

                            @Override
                            public void onError(int code, final String error) {
                                //退出账户失败
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        LogUtil.log("退出失败", error);
                                        ToastUtil.toast(getActivity(), error);
                                    }
                                });
                            }

                            @Override
                            public void onProgress(int progress, final String status) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        LogUtil.log("正在退出中", status);
                                        ToastUtil.toast(getActivity(), "正在退出中" + status);
                                    }
                                });
                            }
                        });
                    }
                }).start();

            }
        });

        tv_setting_abouus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutusActivity.class);
                startActivity(intent);
            }
        });




        tv_setting_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),WeChatActivity.class);
                startActivity(intent);
            }
        });

        tv_setting_android.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AndroidActivity.class);
                startActivity(intent);
            }
        });


        tv_setting_saoyisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openCameraIntent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            if (scanResult != null){
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "");
                intent.putExtra("url", scanResult);
                startActivity(intent);
            }
        }



    }
}
