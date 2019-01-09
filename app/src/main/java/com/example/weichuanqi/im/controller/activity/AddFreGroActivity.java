package com.example.weichuanqi.im.controller.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.controller.adapter.AddFriGroFragAdapter;
import com.example.weichuanqi.im.controller.fragment.AddFriendFragment;
import com.example.weichuanqi.im.controller.fragment.AddGroupFragment;

import java.util.ArrayList;
import java.util.List;

public class AddFreGroActivity extends AppCompatActivity  implements View.OnClickListener{

    private TabLayout tl_add;
    private ViewPager vp_add;
    private List<Fragment> mList;
    private AddFriGroFragAdapter mAdapter;
    private ImageView iv_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fre_gro);
        //实例化控件
        initView();
        //实例化监听
        initListener();
        //实例化数据
        initData();
    }
        //实例化监听
      void initListener() {
        iv_back.setOnClickListener(this);

    }

    //实例化数据
    void initData() {

        mList = new ArrayList<>();
        mList.add(new AddFriendFragment());
        mList.add(new AddGroupFragment());

        mAdapter = new AddFriGroFragAdapter(getSupportFragmentManager(),mList);
        vp_add.setAdapter(mAdapter);

        tl_add.setupWithViewPager(vp_add);


    }

    //实例化控件
     void initView() {
        tl_add  = (TabLayout) findViewById(R.id.tl_add_f_g);
        vp_add  = (ViewPager) findViewById(R.id.vp_add_f_g);
        iv_back = (ImageView) findViewById(R.id.iv_add_f_g_back);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_add_f_g_back://返回键
                finish();
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                break;


        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

}
