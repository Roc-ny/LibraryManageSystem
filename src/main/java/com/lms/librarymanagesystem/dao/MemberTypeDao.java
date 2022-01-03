package com.lms.librarymanagesystem.dao;


import com.lms.librarymanagesystem.bean.MemberType;
import com.lms.librarymanagesystem.util.DBHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MemberTypeDao {
    QueryRunner  runner = new QueryRunner();

    //查询所有的会员类型
    public List<MemberType> getAll() throws SQLException {
        Connection conn = DBHelper.getConnection();
        String sql="select * from membertype";
        List<MemberType> memberTypes = runner.query(conn,sql,new BeanListHandler<MemberType>(MemberType.class));
        DBHelper.close(conn);
        return memberTypes;
    }

    //根据会员类型编号查询对应的会员类型
    public MemberType getById(long id) throws SQLException {
        Connection conn = DBHelper.getConnection();
        String sql="select * from membertype where id=?";
        MemberType memberType= runner.query(conn,sql,new BeanHandler<MemberType>(MemberType.class),id);
        DBHelper.close(conn);
        return memberType;
    }
}
