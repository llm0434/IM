package com.example.weichuanqi.im.model;

import android.content.Context;

import com.example.weichuanqi.im.model.bean.User;
import com.example.weichuanqi.im.model.dao.UserDao;
import com.example.weichuanqi.im.model.db.DBHelper;

/**
 *  全局模型层操作类
 *
 */

public class Model {


    private static Model model = null;
    private Model(){

    }
    //获取单例对象
    public static Model getInstance(){
        if (model == null){
            model = new Model();
        }
        return model;
    }



    private Context mContext;
    private UserDao userDao = null;
    private DBHelper dbHelper = null;



    public void init(Context context){
        this.mContext = context;
        //执行创建数据库语句

        userDao  = new UserDao(mContext);


    }

    //返回用户表操作类对象
    public UserDao getUserDao(){

        return userDao;
    }

    public DBHelper getDBHelper(){

        return dbHelper;

    }

    // 用户登录成功后的处理方法
    public void loginSuccess(User account) {

        // 校验
        if(account == null) {
            return;
        }

        if(dbHelper != null) {
            dbHelper.close();
        }
        dbHelper = new DBHelper(mContext,account.getName());

    }


}
