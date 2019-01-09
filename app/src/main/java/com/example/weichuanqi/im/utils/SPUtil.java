package com.example.weichuanqi.im.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.weichuanqi.im.MyApplication;

/**
 *  封装SP 工具类
 */
public class SPUtil {

    public static final String IS_NEW_INVITE = "is_new_invite";
    private static SPUtil mSpUtil = null;
    private static SharedPreferences sp;

    private SPUtil() {
    }

    public static SPUtil getInstance(){
        if (mSpUtil == null){
            mSpUtil = new SPUtil();
        }

        //context.getSharePerferences()
        if (sp == null) {
            sp = MyApplication.getGlobalApplication().getSharedPreferences("im", Context.MODE_PRIVATE);
        }

        return mSpUtil;
    }

    //保存方法
    public void save(String key,Object value){


        if (key == null){
            return;
        }

        if (value instanceof String){
            sp.edit().putString(key, (String) value).commit();
        }else if (value instanceof Boolean){
            sp.edit().putBoolean(key, (Boolean) value).commit();
        }else if (value instanceof Integer){
            sp.edit().putInt(key, (Integer) value).commit();
        }

    }

    //获取String数据的方法
    public String getString(String key,String defValue){

        return sp.getString(key,defValue);

    }

    //获取boolean数据的方法
    public Boolean getBoolean(String key, boolean defValue){

        return sp.getBoolean(key,defValue);
    }

    //获取int数据的方法
    public int getInt(String key,int defValue){
         return sp.getInt(key,defValue);
    }










}
