package com.example.weichuanqi.im.utils;

import android.content.Context;
import android.widget.Toast;

/**
 *     简易toast类
 */

public class ToastUtil {

    public static void toast(Context context,String msg){

        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();

    }



}
