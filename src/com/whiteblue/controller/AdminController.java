package com.whiteblue.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.whiteblue.interceptor.AdminInterceptor;
import com.whiteblue.interceptor.LoginInterceptor;
import com.whiteblue.model.Groups;
import com.whiteblue.model.User;
import com.whiteblue.tools.SecurityUtil;
import com.whiteblue.validator.GroupsValidator;
import com.whiteblue.validator.RegisterValidator;


/**
 * Created by WhiteBlue on 15/1/23.
 */
public class AdminController extends WbController {

    @Before({LoginInterceptor.class, AdminInterceptor.class})
    public void index() {
        int pageNumber = getParaToInt(0, 1);
        Page<Groups> page = Groups.dao.listAll(pageNumber);
        setAttr("page", page);
        setAttr("actionUrl", "/admin/");
        render("/admin/index.html");
    }

    //列出老师
    @Before({LoginInterceptor.class, AdminInterceptor.class})
    public void listTeacher() {
        int pageNumber = getParaToInt(0, 1);
        Page<User> page = User.dao.listTeacher(pageNumber);
        setAttr("page", page);
        setAttr("actionUrl", "/admin/listTeacher/");
        render("/admin/list_teacher.html");
    }


    @Before({LoginInterceptor.class, AdminInterceptor.class})
    public void listStudent() {
        int pageNumber = getParaToInt(0, 1);
        Page<User> page = User.dao.listStudent(pageNumber);
        setAttr("page", page);
        setAttr("actionUrl", "/admin/listStudent/");
        render("/admin/list_student.html");
    }


    @Before({LoginInterceptor.class, AdminInterceptor.class})
    public void addT() {
        render("/admin/add_teacher.html");
    }

    @Before({LoginInterceptor.class, AdminInterceptor.class})
    public void addG() {
        render("/admin/add_groups.html");
    }

    @Before({LoginInterceptor.class, AdminInterceptor.class, RegisterValidator.class})
    public void addTeacher() {
        User user = getModel(User.class);
        user.newTeacher(getPara("account"), getPara("name"), SecurityUtil.bytesToMD5(getPara("pw1").getBytes()));
        redirect("/admin/listTeacher");
    }

    @Before({LoginInterceptor.class, AdminInterceptor.class, GroupsValidator.class})
    public void addGroups() {
        Groups groups=getModel(Groups.class);
        groups.newPublic(getPara("name"),getPara("content"));
        redirect("/admin/addG");
    }

    @Before({LoginInterceptor.class, AdminInterceptor.class})
    public void deleteGroup() {
        int id = getParaToInt(0);
        Groups groups = Groups.dao.getById(id);
        groups.deleteGroup();
        redirect("/admin");
    }

    @Before({LoginInterceptor.class, AdminInterceptor.class})
    public void deleteTeacher() {
        int id = getParaToInt(0);
        User user = User.dao.getById(id);
        user.deleteUser();
        redirect("/admin/listTeacher");
    }

    @Before({LoginInterceptor.class, AdminInterceptor.class})
    public void deleteStudent() {
        int id = getParaToInt(0);
        User user = User.dao.getById(id);
        user.deleteUser();
        redirect("/admin/listStudent");
    }

}
