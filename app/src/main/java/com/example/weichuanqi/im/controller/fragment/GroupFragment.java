package com.example.weichuanqi.im.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.controller.activity.ChatActivity;
import com.example.weichuanqi.im.controller.activity.GroupChatActivity;
import com.example.weichuanqi.im.controller.adapter.GroupListAdapter;
import com.example.weichuanqi.im.model.Model;
import com.example.weichuanqi.im.model.bean.Chat;
import com.example.weichuanqi.im.model.bean.Group;
import com.example.weichuanqi.im.utils.Constant;
import com.example.weichuanqi.im.utils.ToastUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class GroupFragment extends Fragment {
    private ListView lv_group_list;
    private GroupListAdapter groupListAdapter ;
    private List<EMGroup> allGroups;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group, null, false);
        //实例化控件
        initView(v);
        return v;
    }

    /**
     * 实例化控件
     * @param v
     */
    private void initView(View v) {
        lv_group_list = (ListView) v.findViewById(R.id.lv_group_list);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //实例化群组
        initGroup();
        //实例化监听
        initListener();



    }
    //实例化监听
    private void initListener() {
        lv_group_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMGroup chat = allGroups.get(position);
                String userid = EMClient.getInstance().getCurrentUser();
                Intent intent = new Intent(getActivity(), GroupChatActivity.class);
                intent.putExtra(Constant.CHAT_TYPE,Constant.GROUP_CHAT);
                intent.putExtra(Constant.TO_GROUP_ID,chat.getGroupId());
                intent.putExtra(Constant.TO_GROUP_NAME,chat.getGroupName());
                startActivity(intent);

            }
        });


    }

    private void initGroup() {
        //从服务器去获取所有群组
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<EMGroup> grouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();//需异步处理
                    allGroups = EMClient.getInstance().groupManager().getAllGroups();
                    if (allGroups != null && allGroups.size() >= 0){

                        if (getActivity() != null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setAdapter(allGroups);
                                }
                            });
                        }


                    }else {
                        return;
                    }
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    /*if (getActivity() == null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.toast(getActivity(),"拉取群组信息失败"+e.toString());
                        }
                    });*/
                }
            }
        }).start();



    }

    private void setAdapter(List<EMGroup> allGroups) {
        groupListAdapter = new GroupListAdapter(getActivity(),allGroups);
        lv_group_list.setAdapter(groupListAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();

        initGroup();
    }
}
