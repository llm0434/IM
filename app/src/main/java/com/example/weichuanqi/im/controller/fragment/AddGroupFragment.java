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
import com.example.weichuanqi.im.utils.ToastUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class AddGroupFragment extends Fragment {
    private EditText et_add_gro;
    private ImageView iv_cla_gro;
    private TextView tv_ser_gro;
    private ImageView iv_res_gro;
    private TextView tv_res_gro_id;
    private TextView tv_add_gro;
    private LinearLayout lin_search_gro_result;
    private String search_gro;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_group,null,false);
        //实例化控件
        initView(v);
        return v;
    }
    //实例化控件
    private void initView(View v) {
        et_add_gro = (EditText)v.findViewById(R.id.et_add_gro);
        iv_cla_gro = (ImageView)v.findViewById(R.id.iv_cla_gro);
        tv_ser_gro = (TextView)v.findViewById(R.id.tv_ser_gro);
        iv_res_gro = (ImageView)v.findViewById(R.id.iv_res_gro);
        tv_res_gro_id = (TextView)v.findViewById(R.id.tv_res_gro_id);
        tv_add_gro = (TextView)v.findViewById(R.id.tv_add_gro);
        lin_search_gro_result = (LinearLayout)v.findViewById(R.id.lin_search_gro_result);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化布局
        initLayout();
        //初始化控件
        initListener();

    }
    //初始化控件
    void initListener() {
        iv_cla_gro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_add_gro.setText("");
                lin_search_gro_result.setVisibility(View.GONE);
            }
        });
        tv_ser_gro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_gro = et_add_gro.getText().toString().trim();
                if (!TextUtils.isEmpty(search_gro)){
                   // UserInfo userInfo = new UserInfo(search_gro);
                    lin_search_gro_result.setVisibility(View.VISIBLE);
                    tv_add_gro.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_add_shape));
                   // tv_res_gro_id.setText(userInfo.getIdentifier());
                    tv_res_gro_id.setText(search_gro);
                }else {
                    ToastUtil.toast(getActivity(),"输入框不能为空！");
                }



            }
        });
        tv_add_gro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_add_gro.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_add_select));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().groupManager().joinGroup(search_gro);
                            if (getActivity() == null){
                                return;
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.toast(getActivity(),"加入群组"+search_gro+"成功");
                                }
                            });

                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            if (getActivity() == null){
                                return;
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.toast(getActivity(),"加入群组"+search_gro+"失败");
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
        lin_search_gro_result.setVisibility(View.GONE);
    }
}
