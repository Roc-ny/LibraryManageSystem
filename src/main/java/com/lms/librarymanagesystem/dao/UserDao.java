package com.lms.librarymanagesystem.dao;

import com.lms.librarymanagesystem.bean.User;
import com.lms.librarymanagesystem.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Roc
 * 用户表的数据操作对象,通过DBHelper获取连接，查询用户，返回User对象给UserBiz的getUser方法
 */
public class UserDao {
    //创建QueryRunner对象(JDBC-->DBUtils)
    //apach开源框架,对JDBC进行简单封装
    QueryRunner runner = new QueryRunner();
    public User getUser(String name, String pwd) throws SQLException {

        // 1.调用DBHelper获取连接对象
        Connection  conn = DBHelper.getConnection();
        // 2.sql语句
        String sql="select * from user where name=? and pwd=? and state = 1";
        // 3.调用查询方法,将查询的数据封装成User对象
        //将查询到的用户信息封装成一个User对象
        //如果返回user为null，说明数据库里没有该用户信息，即用户名或密码错误
        //user不为null，则输入正确
        User user = runner.query(conn,sql,new BeanHandler<User>(User.class),name,pwd);

        // 4.关闭连接对象
        DBHelper.close(conn);

        // 5.返回user
        return user;
    }
    /**
     * 修改密码
     * @param id  需要修改密码的用户编号
     * @param pwd  新的密码
     * @return  修改的数据行
     */
    public int modifyPwd(long id,String pwd) throws SQLException {
        String sql="update  user set pwd = ? where id=?";
        Connection conn = DBHelper.getConnection();
        int count = runner.update(conn,sql,pwd,id);
        DBHelper.close(conn);
        return count;
    }


    //测试
    public static void main(String[] args) {
        User user = null;
        try {
            user = new UserDao().getUser("super","123");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(user);
    }

}
