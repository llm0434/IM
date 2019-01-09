package com.example.weichuanqi.im.controller.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.controller.adapter.AddContactAdapter;
import com.example.weichuanqi.im.model.Model;
import com.example.weichuanqi.im.model.bean.AddContactBean;
import com.example.weichuanqi.im.model.bean.User;
import com.example.weichuanqi.im.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.List;

public class AddContactActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_add_contact_back;
    private TextView tv_add_contact_save;
    private ListView lv_add_contact;
    private AddContactAdapter contactAdapter;
    private List<AddContactBean> contactBeanList;
    private List<String> exist_members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        getData();
        initView();
        initListener();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //1.从本地数据库中获取联系人信息
        List<User> allContacts = Model.getInstance().getDBHelper().getContactDao().getAllContacts();
        //2.校验数据合法性
        contactBeanList = new ArrayList<>();
        if (allContacts != null && allContacts.size() >= 0) {
            //3.数据转换
            for (User user : allContacts) {
                AddContactBean addContactBean = new AddContactBean(user, false);
                contactBeanList.add(addContactBean);
            }
        }
        //3.设置适配器
        contactAdapter = new AddContactAdapter(this, contactBeanList,exist_members);
        lv_add_contact.setAdapter(contactAdapter);


    }

    /**
     * 监听
     */
    private void initListener() {
        tv_add_contact_back.setOnClickListener(this);
        tv_add_contact_save.setOnClickListener(this);


        lv_add_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取view
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_add_contact);
                checkBox.setChecked(!checkBox.isChecked());
                //修改数据
                AddContactBean addContactBean = contactBeanList.get(position);
                addContactBean.setChecked(checkBox.isChecked());
                //刷新item
                contactAdapter.notifyDataSetChanged();

            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tv_add_contact_back = (TextView) findViewById(R.id.tv_add_contact_back);
        tv_add_contact_save = (TextView) findViewById(R.id.tv_add_contact_save);
        lv_add_contact = (ListView) findViewById(R.id.lv_lv_add_contact_);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_contact_back:
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.tv_add_contact_save:
                //将联系人添加到群组中
                save();
                break;
        }
    }

    /**
     * 将联系人添加到群组中
     */
    private void save() {
        //1.获取选中的联系人集合
        List<String> contact_list = contactAdapter.getCheckedContact();

        //2.返回数据到上一页
        Intent intent = new Intent();
        intent.putExtra("members",contact_list.toArray(new String[0]));
        setResult(RESULT_OK,intent);

        finish();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);


    }

    public void getData() {
        String stringExtra = getIntent().getStringExtra(Constant.TO_GROUP_ID);
        if (stringExtra != null){
            EMGroup group = EMClient.getInstance().groupManager().getGroup(stringExtra);
            exist_members = group.getMembers();
        }
        if (exist_members == null){
            exist_members = new ArrayList<>();
        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
}
