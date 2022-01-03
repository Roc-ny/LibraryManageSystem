package com.lms.librarymanagesystem.action;

import com.alibaba.fastjson.JSON;
import com.lms.librarymanagesystem.bean.Member;
import com.lms.librarymanagesystem.bean.MemberType;
import com.lms.librarymanagesystem.bean.Record;
import com.lms.librarymanagesystem.biz.MemberBiz;
import com.lms.librarymanagesystem.biz.MemberTypeBiz;
import com.lms.librarymanagesystem.biz.RecordBiz;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/member.let")
public class MemberServlet extends HttpServlet {
    MemberTypeBiz memberTypeBiz = new MemberTypeBiz();
    MemberBiz  memberBiz = new MemberBiz();
    RecordBiz recordBiz = new RecordBiz();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    /**
     * /member.let?type=addpre 添加准备(MemberTypes)
     * /member.let?type=add
     * /member.let?type=modifypre&id=xx 修改准备(MemberTypes ,Member)
     * /member.let?type=modify 修改
     * /member.let?type=remove&id=xx 删除
     * /member.let?type=query
     * /member.let?type=modifyrecharge 充值
     * /member.let?type=doajax&idn=xx 通过ajax请求会员信息
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //字符编码
         req.setCharacterEncoding("utf-8");
         resp.setContentType("text/html;charset=utf-8");
        //out
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession();
        if(session.getAttribute("user")==null){
            out.println("<script>alert('请登录');parent.window.location.href='login.html';</script>");
            return;
        }

        //请求类型
        String type = req.getParameter("type");
        //判断类型
        switch (type){
            //添加准备，向数据库拿取所有会员类型
            case "addpre":
                //获取所有的会员类型
                List<MemberType> memberTypes = memberTypeBiz.getAll();
                //存request
                req.setAttribute("memberTypes",memberTypes);
                //转发（获取转发器）
                req.getRequestDispatcher("mem_add.jsp").forward(req,resp);
                break;

            //添加会员信息
            case "add":
                String name =  req.getParameter("name");
                String pwd =  req.getParameter("pwd");
                long memberTypeId =  Long.parseLong(req.getParameter("memberType"));
                double  balance = Double.parseDouble(req.getParameter("balance"));
                String tel =  req.getParameter("tel");
                String idNumber =  req.getParameter("idNumber");
                int count = memberBiz.add(name,pwd,memberTypeId,balance,tel,idNumber);
                if(count>0){
                    out.println("<script>alert('会员开卡成功'); location.href='member.let?type=query';</script>");
                }else{
                    out.println("<script>alert('会员开卡失败'); location.href='member.let?type=query';</script>");
                }
                break;

            //修改前准备，向数据库拿取所有会员信息
            case "modifypre":
                //类型&会员的信息
                long id = Long.parseLong(req.getParameter("id"));
                Member member = memberBiz.getById(id);

                //获取所有的会员类型
                List<MemberType> memberTypes2 = memberTypeBiz.getAll();

                req.setAttribute("member",member);
                req.setAttribute("memberTypes",memberTypes2);

                req.getRequestDispatcher("mem_modify.jsp").forward(req,resp);
                break;

            //修改
            case "modify":
                long memberId = Long.parseLong( req.getParameter("id"));
                String name2 =  req.getParameter("name");
                String pwd2 =  req.getParameter("pwd");
                long memberTypeId2 =  Long.parseLong(req.getParameter("memberType"));
                double  balance2 = Double.parseDouble(req.getParameter("balance"));
                String tel2 =  req.getParameter("tel");
                String idNumber2 =  req.getParameter("idNumber");
                int count3 = memberBiz.modify(memberId,name2,pwd2,memberTypeId2,balance2,tel2,idNumber2);
                 if(count3>0){
                     out.println("<script>alert('会员修改成功'); location.href='member.let?type=query';</script>");
                 }else{
                     out.println("<script>alert('会员修改失败'); location.href='member.let?type=query';</script>");
                 }
                break;


            case "remove":
                 long memId = Long.parseLong(req.getParameter("id"));
                try {
                    int count2 = memberBiz.remove(memId);
                    if(count2>0){
                        out.println("<script>alert('会员删除成功'); location.href='member.let?type=query';</script>");
                    }else{
                        out.println("<script>alert('会员删除失败'); location.href='member.let?type=query';</script>");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("<script>alert('"+e.getMessage()+"'); location.href='member.let?type=query';</script>");
                }
                break;

            //查询所有会员信息
            case "query":
                List<Member> memberList = memberBiz.getAll();
                req.setAttribute("memberList",memberList);
                //转发到jsp页面
                req.getRequestDispatcher("mem_list.jsp").forward(req,resp);
                break;


            case "modifyrecharge":
                //获取身份证号和金额
                String idNumber3 = req.getParameter("idNumber");
                double amount = Double.parseDouble(req.getParameter("amount"));
                int count4 = memberBiz.modifyBalance(idNumber3,amount);
                if(count4>0){
                    out.println("<script>alert('会员充值成功'); location.href='member.let?type=query';</script>");
                }else{
                    out.println("<script>alert('会员充值失败'); location.href='member.let?type=query';</script>");
                }
                break;


            case "doajax":
                //1.获取身份证号
                String idNum = req.getParameter("idn");
                //2.获取 member对象
                Member member1 = memberBiz.getByIdNumber(idNum);
                //2.2 修改member借书数量
                List<Record> records = recordBiz.getRecordsByMemberId(member1.getId());
                if(records.size()>0){
                    long size = member1.getType().getAmount()-records.size();
                    member1.getType().setAmount(size);
                }
                //3. member1 --> json字符串
                String memberJson = JSON.toJSONString(member1);
                //4.响应客户端 注意：out.打印不能换行*****
                out.print(memberJson);
                break;
            default:
                resp.sendError(404,"请求的地址不存在");
        }
    }
}
