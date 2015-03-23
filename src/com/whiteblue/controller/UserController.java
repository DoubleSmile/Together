package com.whiteblue.controller;

import com.jfinal.aop.Before;
import com.whiteblue.interceptor.AdminInterceptor;
import com.whiteblue.interceptor.LoginInterceptor;
import com.whiteblue.interceptor.TeacherInterceptor;
import com.whiteblue.model.Groups;
import com.whiteblue.model.Link;
import com.whiteblue.model.User;
import com.whiteblue.tools.SecurityUtil;
import com.whiteblue.validator.LoginValidator;
import com.whiteblue.validator.RegisterValidator;
import com.whiteblue.validator.UserValidator;


/**
 * Created by WhiteBlue on 15/1/15.
 */
public class UserController extends WbController {

    //信息编辑页面
    @Before(LoginInterceptor.class)
    public void editInfo() {
        render("/user_info_edit.html");
    }


    //查看他人或自己信息
    @Before(LoginInterceptor.class)
    public void getInfo() {
        User user = User.dao.getById(getParaToInt(0));
        setAttr("user0", user);
        render("/user_info.html");
    }


    //登陆方法
    @Before(LoginValidator.class)
    public void login() {
        String ID = getPara("account");
        String pw = getPara("pw");
        String md5 = SecurityUtil.bytesToMD5(pw.getBytes());
        User user = User.dao.getByPw(ID, md5);
        if (user != null) {
            setAttr("msg", 1);
            setCookie("user", user.getInt("id") + "-" + md5, 3600 * 24 * 30);
            setSessionAttr("user", user);
            redirect("/");
        } else {
            setAttr("msg", "用户名或密码错误");
            render("/login.html");
        }
    }

    //注销方法
    @Before(LoginInterceptor.class)
    public void logOut() {
        removeCookie("user");
        removeSessionAttr("user");
        redirect("/");
    }


    //注册方法
    @Before(RegisterValidator.class)
    public void register() {
        String pw = getPara("pw1");
        User user = getModel(User.class);
        user.saveNew(getPara("account"), getPara("name"), SecurityUtil.bytesToMD5(pw.getBytes()), "");
        setAttr("msg","注册成功,请登录");
        render("/login.html");
    }


    //修改自己信息
    @Before({LoginInterceptor.class, UserValidator.class})
    public void update() {
        User user = getSessionAttr("user");
        user.userUpdate(getPara("name"), getPara("feeling"));
        setSessionAttr("user", user);
        redirect("/user/getInfo/" + user.get("id"));
    }

    //加入教师组
    @Before(LoginInterceptor.class)
    public void joinTeacherGroup() {
        int groupID = getParaToInt("groupID");
        User user = getSessionAttr("user");
        if (Groups.dao.getByPw(groupID, getPara("pw")) != null) {
            user.setGroup(groupID);
            setAttr("msg", 1);
            renderJson();
        } else {
            setAttr("msg", 0);
            renderJson();
        }
    }


    //加入组
    @Before(LoginInterceptor.class)
    public void joinGroup() {
        int groupID = getParaToInt("groupID");
        User user = getSessionAttr("user");
        if (Link.dao.isHave(user.getInt("id"), groupID)) {
            setAttr("msg", 2);
            renderJson();
        } else {
            if (Groups.dao.getByPw(groupID, getPara("pw")) != null) {
                if (Link.dao.getCount(groupID) > 5) {
                    setAttr("msg", 3);
                    renderJson();
                } else {
                    Link link = getModel(Link.class);
                    link.add(user.getInt("id"), groupID);
                    setAttr("msg", 1);
                    setAttr("url", "/user-groups/listTopic/" + groupID);
                    renderJson();
                }
            } else {
                setAttr("msg", 0);
                renderJson();
            }
        }
    }


    //清除用户组信息
    @Before(TeacherInterceptor.class)
    public void clearUser() {
        User user = User.dao.getById(getParaToInt(0));
        user.clearGroup();
        redirect("/teacher/listStudent");
    }

    //删除用户
    @Before(AdminInterceptor.class)
    public void deleteUser() {
        User user = User.dao.getById(getParaToInt(0));
        user.deleteUser();
        redirect("/teacher/listStudent");
    }


}
