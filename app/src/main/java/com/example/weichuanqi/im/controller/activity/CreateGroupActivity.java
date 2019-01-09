package com.example.weichuanqi.im.controller.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.utils.ToastUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_create_gro_back;
    private TextView tv_create_gro_add_cont;
    private EditText et_create_gro_name;
    private EditText et_create_gro_desc;
    private CheckBox cb_open;
    private CheckBox cb_check;
    private TextView tv_create_gro_commit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        initView();
        initListener();
    }

    private void initListener() {
        tv_create_gro_commit.setOnClickListener(this);
        tv_create_gro_back.setOnClickListener(this);
        tv_create_gro_add_cont.setOnClickListener(this);
    }

    private void initView() {
        et_create_gro_name = (EditText) findViewById(R.id.et_create_gro_name);
        et_create_gro_desc = (EditText) findViewById(R.id.et_create_gro_desc);
        cb_open = (CheckBox) findViewById(R.id.cb_open);
        cb_check = (CheckBox) findViewById(R.id.cb_check);
        tv_create_gro_commit = (TextView) findViewById(R.id.tv_create_gro_commit);
        tv_create_gro_back = (TextView) findViewById(R.id.tv_create_group_back);
        tv_create_gro_add_cont = (TextView) findViewById(R.id.tv_group_add_cont);


        tv_create_gro_add_cont.setClickable(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_create_gro_commit:
                createNewGroup();
                break;
            case R.id.tv_create_group_back:
                finish();
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                break;
            case R.id.tv_group_add_cont:
                addContactIntoNewGroup();
                break;
        }
    }

    private void createNewGroup() {
        final String group_name = et_create_gro_name.getText().toString().trim();
        final String group_desc = et_create_gro_desc.getText().toString().trim();

        if (!TextUtils.isEmpty(group_name) && !TextUtils.isEmpty(group_desc)){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    EMGroupOptions option = new EMGroupOptions();
                    option.maxUsers = 200;
                    EMGroupManager.EMGroupStyle group_style = null;
                    if (cb_open.isChecked()){ //公开群
                        // 1.加群需要验证
                        if (cb_check.isChecked()){
                            group_style = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                        }else { // 2.加群不需要验证
                            group_style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                        }

                    }else {   //私有群
                        //1.成员可以邀请加群
                        if (cb_check.isChecked()){
                            group_style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                        }else { // 2.管理员可以邀请加群
                            group_style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                        }

                    }

                    option.style = group_style;

                    try {
                        //1.创建群组
                        EMClient.getInstance().groupManager().createGroup(group_name, group_desc, new String[]{}, "创建群组", option);
                        //2.提示创建成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(CreateGroupActivity.this,"创建群组成功");

                                finish();
                                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        //3.创建群组失败
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(CreateGroupActivity.this,"创建群组失败"+e.toString());


                            }
                        });
                    }
                }
            }).start();




        }else {
            ToastUtil.toast(CreateGroupActivity.this,"群名称或者群组描述不能为空");
        }

    }




    /**
     *  添加联系人进入新群
     */
    private void addContactIntoNewGroup() {
        Intent intent = new Intent(CreateGroupActivity.this,AddContactActivity.class);

        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //成功获取数据
        if (resultCode == RESULT_OK){
            commit(data.getStringArrayExtra("members"));
        }

    }

    private void commit(final String[] members) {
        final String group_name = et_create_gro_name.getText().toString().trim();
        final String group_desc = et_create_gro_desc.getText().toString().trim();

        if (!TextUtils.isEmpty(group_name) && !TextUtils.isEmpty(group_desc)){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    EMGroupOptions option = new EMGroupOptions();
                    option.maxUsers = 200;
                    EMGroupManager.EMGroupStyle group_style = null;
                    if (cb_open.isChecked()){ //公开群
                        // 1.加群需要验证
                        if (cb_check.isChecked()){
                            group_style = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                        }else { // 2.加群不需要验证
                            group_style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                        }

                    }else {   //私有群
                        //1.成员可以邀请加群
                        if (cb_check.isChecked()){
                            group_style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                        }else { // 2.管理员可以邀请加群
                            group_style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                        }

                    }

                    option.style = group_style;

                    try {
                        //1.创建群组
                        EMClient.getInstance().groupManager().createGroup(group_name, group_desc, members, "创建群组", option);
                        //2.提示创建成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(CreateGroupActivity.this,"创建群组成功");

                                finish();
                                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        //3.创建群组失败
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.toast(CreateGroupActivity.this,"创建群组失败"+e.toString());


                            }
                        });
                    }
                }
            }).start();




        }else {
            ToastUtil.toast(CreateGroupActivity.this,"群名称或者群组描述不能为空");
        }

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

}
