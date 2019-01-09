package com.example.weichuanqi.im.controller.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.model.bean.User;
import com.example.weichuanqi.im.utils.ToastUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class AddFriendFragment extends Fragment {

    private EditText et_add_fri;
    private ImageView iv_cla_fri;
    private TextView tv_ser_fri;
    private ImageView iv_res_fri;
    private TextView tv_res_fri_id;
    private TextView tv_add_fri;
    private LinearLayout lin_search_fri_result;
    private String search_fri;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_friend,null,false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        et_add_fri = (EditText)v.findViewById(R.id.et_add_fri);
        iv_cla_fri = (ImageView)v.findViewById(R.id.iv_cla_fri);
        tv_ser_fri = (TextView)v.findViewById(R.id.tv_ser_fri);
        iv_res_fri = (ImageView)v.findViewById(R.id.iv_res_fri);
        tv_res_fri_id = (TextView)v.findViewById(R.id.tv_res_fri_id);
        tv_add_fri = (TextView)v.findViewById(R.id.tv_add_fri);
        lin_search_fri_result = (LinearLayout)v.findViewById(R.id.lin_search_fri_result);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化布局
        initLayout();
        //初始化监听
        initListener();


    }
    //初始化监听
    void initListener() {
        iv_cla_fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_add_fri.setText("");
                lin_search_fri_result.setVisibility(View.GONE);
            }
        });
        tv_ser_fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_fri = et_add_fri.getText().toString().trim();
                if (!TextUtils.isEmpty(search_fri)){
                    User userInfo = new User(search_fri);
                    lin_search_fri_result.setVisibility(View.VISIBLE);
                    tv_add_fri.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_add_shape));
                    tv_res_fri_id.setText(userInfo.getId());
                }else {
                    ToastUtil.toast(getActivity(),"输入框不能为空！");
                }

            }
        });

        tv_add_fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_add_fri.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_add_select));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().contactManager().addContact(search_fri,"添加好友");

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.toast(getActivity(),"发送请求成功！");
                                }
                            });
                        } catch (final HyphenateException e) {
                            e.printStackTrace();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.toast(getActivity(),"发送请求失败！"+e.toString());
                                }
                            });
                        }

                    }
                }).start();
            }
        });



    }

    //初始化布局
   void initLayout() {
        lin_search_fri_result.setVisibility(View.GONE);


    }
}
