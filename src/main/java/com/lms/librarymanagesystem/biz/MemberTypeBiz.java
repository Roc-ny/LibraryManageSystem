package com.lms.librarymanagesystem.biz;


import com.lms.librarymanagesystem.bean.MemberType;
import com.lms.librarymanagesystem.dao.MemberTypeDao;

import java.sql.SQLException;
import java.util.List;

public class MemberTypeBiz {
    MemberTypeDao dao =new MemberTypeDao();

    //返回所有会员类型
    public List<MemberType> getAll(){
        try {
            return dao.getAll();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    //根据会员类型id获取会员类型信息
    public MemberType getById(long id){
        MemberType memberType = null;

        try {
            memberType = dao.getById(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return memberType;

    }
}
