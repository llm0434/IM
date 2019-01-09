package com.example.weichuanqi.im.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.controller.activity.AddFreGroActivity;
import com.example.weichuanqi.im.controller.activity.CreateGroupActivity;
import com.example.weichuanqi.im.controller.activity.NewFriendActivity;
import com.example.weichuanqi.im.controller.adapter.ContListVPAdapter;
import com.example.weichuanqi.im.model.Model;
import com.example.weichuanqi.im.model.bean.User;
import com.example.weichuanqi.im.utils.Constant;
import com.example.weichuanqi.im.utils.SPUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {
    private LinearLayout newFriend;
    private LinearLayout createGroup;
    private ImageView con_iv_add;
    private ImageView iv_red_point;
    private LocalBroadcastManager mlbm;

    private TabLayout tl_cont_list;
    private ViewPager vp_con_list;
    private List<Fragment> fragments;
    private ContListVPAdapter vpAdapter;


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //红点显示
            iv_red_point.setVisibility(View.VISIBLE);

            SPUtil.getInstance().save(SPUtil.IS_NEW_INVITE, true);
        }
    };
    private BroadcastReceiver groupListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //1.显示红点
            iv_red_point.setVisibility(View.VISIBLE);
            SPUtil.getInstance().save(SPUtil.IS_NEW_INVITE,true);

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact, null, false);

        //实例化控件
        initView(v);

        return v;
    }

    //实例化控件
    void initView(View v) {
        newFriend = (LinearLayout) v.findViewById(R.id.lin_new_friend);
        createGroup = (LinearLayout) v.findViewById(R.id.lin_create_group);
        con_iv_add = (ImageView) v.findViewById(R.id.cont_iv_add);
        iv_red_point = (ImageView) v.findViewById(R.id.iv_red_point);

        vp_con_list = (ViewPager) v.findViewById(R.id.vp_cont_list);
        tl_cont_list = (TabLayout) v.findViewById(R.id.tl_con_list);

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
        //初始化红点显示
        Boolean isVisable = SPUtil.getInstance().getBoolean(SPUtil.IS_NEW_INVITE, false);
        iv_red_point.setVisibility(isVisable ? View.VISIBLE : View.INVISIBLE);
        //注册广播
        mlbm = LocalBroadcastManager.getInstance(getActivity());

        mlbm.registerReceiver(receiver, new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
        mlbm.registerReceiver(groupListener,new IntentFilter(Constant.GROUP_INVITE_CHANGED));


        fragments = new ArrayList<>();
        fragments.add(new FriendFragment());
        fragments.add(new GroupFragment());
        vpAdapter = new ContListVPAdapter(getFragmentManager(),fragments);
        vp_con_list.setAdapter(vpAdapter);

        tl_cont_list.setupWithViewPager(vp_con_list);

    }
    //刷新页面的方法
    void refreshContacts() {


    }

    //实例化监听
    void initListener() {
        newFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏红点
                iv_red_point.setVisibility(View.INVISIBLE);
                SPUtil.getInstance().save(SPUtil.IS_NEW_INVITE, false);


                Intent intent = new Intent(getActivity(), NewFriendActivity.class);
                startActivity(intent);
            }
        });

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateGroupActivity.class);
                startActivity(intent);
            }
        });

        con_iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddFreGroActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭广播
        mlbm.unregisterReceiver(receiver);
        mlbm.unregisterReceiver(groupListener);

    }
}
