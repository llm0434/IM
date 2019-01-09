package com.example.weichuanqi.im.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 *      判断输入账户和密码时的判空工具类
 *
 */

public class CheckInputIsNull {


    public static boolean isNotNull(Context context,String name,String password){

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)){
            return true;
        }else {
            Toast.makeText(context,"用户名或者密码不能为空！",Toast.LENGTH_SHORT).show();
        }
        return false;
    }







}
