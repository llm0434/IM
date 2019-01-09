package com.example.weichuanqi.im.controller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.controller.adapter.NewInviteLVAdapter;
import com.example.weichuanqi.im.model.Model;
import com.example.weichuanqi.im.model.bean.Invite;
import com.example.weichuanqi.im.model.db.DBHelper;
import com.example.weichuanqi.im.utils.Constant;
import com.example.weichuanqi.im.utils.ToastUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class NewFriendActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView lv_new_fri;
    private TextView tv_back_to_cont;
    private TextView tv_new_fri_add;

    private NewInviteLVAdapter lvAdapter;
    private NewInviteLVAdapter.OnInviteListener listener = new NewInviteLVAdapter.OnInviteListener() {
        @Override
        public void onAccept(final Invite info) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //1.服务器接受邀请
                        EMClient.getInstance().contactManager().acceptInvitation(info.getUser().getId());
                        //2.本地数据库更新
                        Model.getInstance().getDBHelper().getInviteDao().updateInvitionStatus(Invite.InvitationStatus.INVITE_ACCEPT,info.getUser().getId());
                        //3.提示用户成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(NewFriendActivity.this,"您接受了"+info.getUser().getName()+"的邀请");

                                refresh();
                            }
                        });


                    } catch (final HyphenateException e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(NewFriendActivity.this,"操作失败"+e.toString());
                            }
                        });



                    }
                }
            }).start();



        }

        @Override
        public void onRefuse(final Invite info) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        //1.服务器拒绝邀请
                        EMClient.getInstance().contactManager().declineInvitation(info.getUser().getId());
                        //2.本地数据库更新
                        Model.getInstance().getDBHelper().getInviteDao().deleteInvition(info.getUser().getId());
                        //3.提示信息
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(NewFriendActivity.this,"您拒绝了"+info.getUser().getName()+"的邀请");
                                //4.刷新数据
                                refresh();
                            }
                        });

                    } catch (final HyphenateException e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(NewFriendActivity.this,"操作失败"+e.toString());
                            }
                        });


                    }



                }
            }).start();


        }

        /**
         *  接受邀请
         * @param invationInfo
         */

        @Override
        public void onInviteAccept(final Invite invationInfo) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //1.反馈给服务器
                        EMClient.getInstance().groupManager().acceptInvitation(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getInviter());
                        //2.本地数据库更新
                        invationInfo.setStatus(Invite.InvitationStatus.GROUP_ACCEPT_INVITE);
                        Model.getInstance().getDBHelper().getInviteDao().addInvition(invationInfo);
                        //3.刷新页面
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(NewFriendActivity.this,"接受了邀请");

                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(NewFriendActivity.this,"接受邀请失败");
                            }
                        });
                    }
                }
            }).start();
        }

        /**
         *  拒绝邀请
         * @param invationInfo
         */
        @Override
        public void onInviteReject(final Invite invationInfo) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //1.反馈给服务器
                        EMClient.getInstance().groupManager().declineInvitation(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getInviter(),"拒绝邀请");
                        //2.本地数据库邀请
                        invationInfo.setStatus(Invite.InvitationStatus.GROUP_REJECT_INVITE);
                        Model.getInstance().getDBHelper().getInviteDao().addInvition(invationInfo);
                        //3.刷新页面
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(NewFriendActivity.this,"拒绝了邀请");

                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(NewFriendActivity.this,"拒绝邀请失败");

                                refresh();
                            }
                        });
                    }
                }
            }).start();
        }

        /**
         *  接受申请按钮                      此处参数可能误传
         * @param invationInfo
         */
        @Override
        public void onApplicationAccept(final Invite invationInfo) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //1.反馈给服务器
                        EMClient.getInstance().groupManager().acceptApplication(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getGroupName());
                        //2.本地数据库邀请
                        invationInfo.setStatus(Invite.InvitationStatus.GROUP_ACCEPT_APPLICATION);
                        Model.getInstance().getDBHelper().getInviteDao().addInvition(invationInfo);
                        //3.刷新页面
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(NewFriendActivity.this,"接受申请");

                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();

                    }
                }
            }).start();
        }

        /**
         *  拒绝申请按钮
         * @param invationInfo
         */
        @Override
        public void onApplicationReject(final Invite invationInfo) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //1.反馈给服务器
                        EMClient.getInstance().groupManager().declineApplication(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getGroupName(),"拒绝申请");
                        //2.本地数据库邀请
                        invationInfo.setStatus(Invite.InvitationStatus.GROUP_REJECT_APPLICATION);
                        Model.getInstance().getDBHelper().getInviteDao().addInvition(invationInfo);
                        //3.刷新页面
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(NewFriendActivity.this,"拒绝申请");

                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(NewFriendActivity.this,"拒绝申请失败");

                                refresh();
                            }
                        });

                    }
                }
            }).start();
        }
    };
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //接受到广播之后刷新页面
            refresh();

        }
    };
    private BroadcastReceiver groupListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        //初始化控件
        initView();
        //初始化数据
        initData();
        //初始化监听
        initListener();
    }
    //初始化监听
    void initListener() {
        tv_back_to_cont.setOnClickListener(this);
        tv_new_fri_add.setOnClickListener(this);
    }

    //初始化数据
    void initData() {
        lvAdapter  = new NewInviteLVAdapter(this,listener);

        lv_new_fri.setAdapter(lvAdapter);
        //刷新数据方法
        refresh();

        //注册邀请信息变化的广播
        broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(receiver,new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
        broadcastManager.registerReceiver(receiver,new IntentFilter(Constant.GROUP_INVITE_CHANGED));

    }
    //刷新数据方法
    private void refresh() {

        List<Invite> list = Model.getInstance().getDBHelper().getInviteDao().getAllInvition();
        if (list != null && list.size() >= 0){
            lvAdapter.refresh(list);
        }

    }

    //初始化控件
    void initView() {
        lv_new_fri = (ListView) findViewById(R.id.lv_new_friend);
        tv_back_to_cont = (TextView) findViewById(R.id.tv_back_to_cont);
        tv_new_fri_add = (TextView) findViewById(R.id.tv_new_fri_add);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back_to_cont:    //回到联系人界面
              finish();
              overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                break;
            case R.id.tv_new_fri_add:     // 跳转到添加好友界面
                Intent intent = new Intent(NewFriendActivity.this,AddFreGroActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在销毁活动时关闭广播
        broadcastManager.unregisterReceiver(receiver);
        broadcastManager.unregisterReceiver(groupListener);

    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }


}
