package com.example.weichuanqi.im.controller.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.example.weichuanqi.im.R;
import com.example.weichuanqi.im.controller.adapter.FragmentAdapter;
import com.example.weichuanqi.im.controller.fragment.ChatFragment;
import com.example.weichuanqi.im.controller.fragment.ContactFragment;
import com.example.weichuanqi.im.controller.fragment.SettingFragment;
import com.example.weichuanqi.im.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 主布局包含  1.viewpager  2.bottomNavigationView
 */
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bnv;
    private ViewPager vp;

    private List<Fragment> mFragments;
    private FragmentAdapter mAdapter;

    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //实例化控件
        initView();
        //实例化监听
        initListener();
    }

    //实例化监听
    private void initListener() {

        bnv.setOnNavigationItemSelectedListener(this);


    }

    //实例化控件
    private void initView() {
        bnv = (BottomNavigationView) findViewById(R.id.main_bnv);
        vp = (ViewPager) findViewById(R.id.my_vp);

        //将fragment 添加到集合中
        mFragments = new ArrayList<>();
        mFragments.add(new ChatFragment());
        mFragments.add(new ContactFragment());
        mFragments.add(new SettingFragment());

        //设置viewpager适配器
        mAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments);
        vp.setAdapter(mAdapter);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.item_chat:
                vp.setCurrentItem(0, false);//false 是禁用viewpager切换效果
                return true;
            case R.id.item_contact:
                vp.setCurrentItem(1, false);
                return true;
            case R.id.item_setting:
                vp.setCurrentItem(2, false);
                return true;
        }
        return false;
    }


   /* @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
    //此处代码正确
        if (menuItem != null){
            menuItem.setChecked(false);
        }else {
            bnv.getMenu().getItem(0).setChecked(true);
        }
        menuItem = bnv.getMenu().getItem(i);
        menuItem.setChecked(true);


    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }*/

    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                ToastUtil.toast(MainActivity.this,"再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


}
