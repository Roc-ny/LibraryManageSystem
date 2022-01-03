package com.lms.librarymanagesystem.biz;

import com.lms.librarymanagesystem.bean.User;
import com.lms.librarymanagesystem.dao.UserDao;

import java.sql.SQLException;

/**
 * @author Roc
 * 调用getUser方法，传入用户登录输入的name和pwd，并返回UserDao中从数据库查询到的User对象
 */
public class UserBiz {
    //构建UserDao的对象
    UserDao userDao = new UserDao();
    public User getUser(String name, String pwd){
        User  user = null;
        try {
            user  = userDao.getUser(name,pwd);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }


    public int modifyPwd(long id,String pwd){
        int count = 0;
        try {
            count = userDao.modifyPwd(id,pwd);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;

    }
}
