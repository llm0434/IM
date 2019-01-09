package com.example.weichuanqi.im.controller.activity;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.controller.adapter.GridViewAdapter;
import com.example.weichuanqi.im.model.bean.User;
import com.example.weichuanqi.im.utils.Constant;
import com.example.weichuanqi.im.utils.ToastUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class GroupMoreDetail extends AppCompatActivity {

    private TextView tv_group_more_back;
    private GridView gv_group_more;
    private TextView tv_exit_group;
    private String group_id;
    private EMGroup emGroup;
    private GridViewAdapter.OnGroupContactListener groupContactListener = new GridViewAdapter.OnGroupContactListener() {
        @Override
        public void onAddGroupContact() {
            //跳转到添加联系人界面
           Intent intent = new Intent(GroupMoreDetail.this,AddContactActivity.class);
           intent.putExtra(Constant.TO_GROUP_ID,emGroup.getGroupId());
           startActivityForResult(intent,2);
        }

        @Override
        public void onDeleteGroupContact(final User user) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //1.从服务器删除联系人
                        EMClient.getInstance().groupManager().removeUserFromGroup(emGroup.getGroupId(),user.getId());
                        //2.刷新页面
                        getGroupContactFromServer();
                        //提示信息
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(GroupMoreDetail.this,"删除联系人"+user.getName()+"成功");
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(GroupMoreDetail.this,"删除联系人失败"+e);
                            }
                        });
                    }
                }
            }).start();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //1.获取返回的联系人数组
        if (resultCode == RESULT_OK){
            final String[] members = data.getStringArrayExtra("members");
            //2.添加联系人到群组中
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().addUsersToGroup(emGroup.getGroupId(),members);
                        //刷新页面

                        //提示成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(GroupMoreDetail.this,"添加成员成功");
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(GroupMoreDetail.this,"添加成员失败"+e);
                            }
                        });
                    }
                }
            }).start();
        }

    }

    private List<User> mUsers;
    private GridViewAdapter gridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_more_detail);

        initView();
        initIntent();
        initData();

        initListener();


    }

    private void initListener() {
        gv_group_more.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //判断当前是否为删除模式，是删除模式，变为非删除模式
                        if (gridViewAdapter.isDelete_model()){
                            //变为非删除模式
                            gridViewAdapter.setDelete_model(false);
                            //刷新页面
                            gridViewAdapter.notifyDataSetChanged();
                        }
                        break;
                }


                return false;
            }
        });

        tv_group_more_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });





    }

    private void initData() {
        //1.判断当前用户是否为群主
        if (EMClient.getInstance().getCurrentUser().equals(emGroup.getOwner())){ //为群主
            tv_exit_group.setText("解散群组"+emGroup.getGroupId());

            tv_exit_group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //2.解散群组
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().destroyGroup(emGroup.getGroupId());
                                //3.发送解散群广播
                                sendExitGroupBC();
                                //4.更新布局
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.toast(GroupMoreDetail.this,emGroup.getGroupName()+"已被解散");
                                        startActivity(new Intent(GroupMoreDetail.this,MainActivity.class));
                                        finish();
                                    }
                                });
                            } catch (final HyphenateException e) {
                                e.printStackTrace();
                                //5.解散群组失败
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.toast(GroupMoreDetail.this,"解散群组失败"+e);
                                    }
                                });

                            }
                        }
                    }).start();
                }
            });

        }else {                                                                  //为成员
            tv_exit_group.setText("退出群组"+emGroup.getGroupId());

            tv_exit_group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //1.退群
                                EMClient.getInstance().groupManager().leaveGroup(emGroup.getGroupId());
                                //2.发送退群广播
                                sendExitGroupBC();
                                //3.更新页面
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.toast(GroupMoreDetail.this,"退群成功");
                                        startActivity(new Intent(GroupMoreDetail.this,MainActivity.class));
                                        finish();
                                    }
                                });
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.toast(GroupMoreDetail.this,"退群失败");
                                    }
                                });
                            }
                        }
                    }).start();
                }
            });






        }

            //2
        //GridView的显示
        boolean isCanModify = EMClient.getInstance().getCurrentUser().equals(emGroup.getOwner()) || emGroup.isPublic();

        gridViewAdapter = new GridViewAdapter(this,isCanModify,groupContactListener);
        gv_group_more.setAdapter(gridViewAdapter);

        //3.从服务器获取所有的群成员
        getGroupContactFromServer();






    }

    private void sendExitGroupBC() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent(Constant.EXIT_GROUP);
        intent.putExtra(Constant.TO_GROUP_ID,emGroup.getGroupId());
        broadcastManager.sendBroadcast(intent);


    }

    /**
     *  接受到数据
     */
    private void initIntent() {
        group_id = getIntent().getStringExtra("group_more_id");
        if (group_id == null){
            return;
        }else {
            emGroup = EMClient.getInstance().groupManager().getGroup(group_id);
        }


    }



    /**
     *     实例化控件
     */
    private void initView() {
        tv_group_more_back = (TextView) findViewById(R.id.tv_group_more_back);
        gv_group_more = (GridView) findViewById(R.id.gv_group_more);
        tv_exit_group = (TextView) findViewById(R.id.tv_exit_group);

    }

    /**
     *     从服务器获取所有的群成员
     */
    public void getGroupContactFromServer() {
       new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   //从服务获取所有的群信息
                   EMGroup groupFromServer = EMClient.getInstance().groupManager().getGroupFromServer(emGroup.getGroupId());
                   //获取所有的群组成员信息
                   List<String> members = groupFromServer.getMembers();

                   mUsers = new ArrayList<>();

                   for (String u:members ) {
                       User user = new User(u);
                       mUsers.add(user);
                   }
                   User own = new User(emGroup.getOwner());
                   User current = new User(EMClient.getInstance().getCurrentUser());
                    if (emGroup.getOwner().equals(EMClient.getInstance().getCurrentUser())){
                        mUsers.add(current);
                    }else {
                        mUsers.add(current);
                        mUsers.add(own);
                    }


                   //更新数据
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           gridViewAdapter.refresh(mUsers);
                       }
                   });

               } catch (HyphenateException e) {
                   e.printStackTrace();
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           ToastUtil.toast(GroupMoreDetail.this,"获取群信息失败");
                       }
                   });
               }
           }
       }).start();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }



}
