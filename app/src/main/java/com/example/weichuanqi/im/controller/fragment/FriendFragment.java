package com.example.weichuanqi.im.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.controller.activity.ChatActivity;
import com.example.weichuanqi.im.controller.adapter.ContactListAdapter;
import com.example.weichuanqi.im.controller.adapter.ConversationAdapter;
import com.example.weichuanqi.im.model.Model;
import com.example.weichuanqi.im.model.bean.Conversation;
import com.example.weichuanqi.im.model.bean.User;
import com.example.weichuanqi.im.utils.Constant;
import com.example.weichuanqi.im.utils.LogUtil;
import com.example.weichuanqi.im.utils.ToastUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class FriendFragment extends Fragment {

    private ListView lv_friend;
    private ContactListAdapter adapter;
    private LocalBroadcastManager mlbm;

    private List<Conversation> mList = new ArrayList<>();

    private Conversation mConver;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getUserContactByServer();
        }
    };
    private String delete_id;
    public List<User> userList = new ArrayList<>() ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friend, null, false);
        //实例化控件
        initView(v);



        return v;
    }

    private void initView(View v) {
        lv_friend = (ListView) v.findViewById(R.id.lv_friend_list);



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化数据
        initData();
        //将listview和context绑定在一起
        registerForContextMenu(lv_friend);
        //初始化监听
        initListener();

    }

    //初始化监听
    private void initListener() {
        lv_friend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = userList.get(position);
                LogUtil.log("======", "position=" + position + ",id=" + id + "当前用户名=" + user.getName() + ",当前用户id=" + user.getId());
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(Constant.CHAT_TYPE, Constant.CHAT);
                intent.putExtra(Constant.TO_USER_ID, user.getName());
                intent.putExtra(Constant.TO_USER_NAME, user.getName());
                startActivity(intent);
            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        //从接口的实现类AdapterView.AdapterContextMenuInfo中获取position
        //根据position来获取userID
        int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        User userinfo = (User) lv_friend.getItemAtPosition(position);
        delete_id = userinfo.getId();

        //添加删除布局
        getActivity().getMenuInflater().inflate(R.menu.contact_delete, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.contact_delete) {
            //删除联系人
            deleteContact();

            return true;
        }
        return super.onContextItemSelected(item);
    }

    //删除联系人
    private void deleteContact() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(delete_id);
                    //1.本地数据库gengxin

                    Model.getInstance().getDBHelper().getContactDao().deleteContact(delete_id);
                    //2.提示删除成功
                    if (getActivity() == null) {
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.toast(getActivity(), "删除联系人" + delete_id + "成功");

                            getUserContactByServer();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();

                    if (getActivity() == null) {
                        return;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.toast(getActivity(), "删除联系人" + delete_id + "失败" + e);
                        }
                    });
                }
            }
        }).start();


    }

    private void initData() {

        //注册广播
        mlbm = LocalBroadcastManager.getInstance(getActivity());
        mlbm.registerReceiver(receiver, new IntentFilter(Constant.CONTACT_CHANGED));
        //从服务器获取联系人列表
        getUserContactByServer();

    }

    //从服务器获取联系人列表
    private void getUserContactByServer() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //1. 从服务器获取到了所有的联系人的id集合
                    List<String> contacts = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //2. 校验数据
                    if (contacts != null && contacts.size() >= 0) {
                        //3. 保存联系人到本地数据库
                        //将id转换为user对象
                        final List<User> mList = new ArrayList<>();
                        for (String id : contacts) {
                            User user = new User(id);
                            mList.add(user);
                            userList.add(user);
                        }

                        //保存联系人到本地数据库
                        Model.getInstance().getDBHelper().getContactDao().saveMoreContact(mList, true);
                        if (getActivity() == null) {
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (userList != null && userList.size() >= 0) {
                                    adapter = new ContactListAdapter(getActivity(), mList);
                                    lv_friend.setAdapter(adapter);
                                }
                            }
                        });

                    }

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mlbm.unregisterReceiver(receiver);
    }






}
