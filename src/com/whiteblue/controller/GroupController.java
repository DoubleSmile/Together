package com.whiteblue.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.whiteblue.interceptor.GroupInterceptor;
import com.whiteblue.interceptor.LoginInterceptor;
import com.whiteblue.interceptor.TeacherInterceptor;
import com.whiteblue.model.Groups;
import com.whiteblue.model.Topic;
import com.whiteblue.model.User;

import java.util.List;

/**
 * Created by WhiteBlue on 15/1/15.
 */
public class GroupController extends WbController {

    //用户导向教师组
    @Before({LoginInterceptor.class, GroupInterceptor.class})
    public void index() {
        keepPara("msg");
        User user = getSessionAttr("user");
        if (user.getInt("isTeacher") == 1) {
            this.redirect("/groups/teacherIndex");
        } else {
            this.redirect("/groups/studentIndex");
        }
    }

    //学生查看帖子列表
    @Before({LoginInterceptor.class, GroupInterceptor.class})
    public void studentIndex() {
        keepPara("msg");
        User user = getSessionAttr("user");
        int groupID = user.getInt("groupID");
        Groups group = Groups.dao.getById(groupID);
        int mode = getParaToInt(0, 0);
        Page<Topic> page = null;
        if (mode == 0) {
            page = Topic.dao.getForGroup(groupID, getParaToInt(1, 1));
        } else if (mode == 1) {
            page = Topic.dao.getTeacher(groupID, getParaToInt(1, 1));
        } else {
            page = Topic.dao.getHot(groupID, getParaToInt(1, 1));
        }
        setAttr("mode", mode);
        setAttr("page", page);
        setAttr("group", group);
        setAttr("actionUrl", "/groups/studentIndex/" + mode + "-");
        render("/topic_list.html");

    }

    //教师列出组
    @Before({LoginInterceptor.class, TeacherInterceptor.class})
    public void teacherIndex() {
        User user = getSessionAttr("user");
        List<Groups> list = Groups.dao.listForTeacher(user.getInt("id"));
        setAttr("list", list);
        render("/group_list.html");
    }

    //列出帖子列表(教师)
    @Before({LoginInterceptor.class, GroupInterceptor.class})
    public void listForTeacher() {
        keepPara("msg");
        int groupID = getParaToInt(0, 1);
        Groups group = Groups.dao.getById(groupID);
        int mode = getParaToInt(1, 0);
        Page<Topic> page = null;
        if (mode == 0) {
            page = Topic.dao.getForGroup(groupID, getParaToInt(2, 1));
        } else if (mode == 1) {
            page = Topic.dao.getTeacher(groupID, getParaToInt(2, 1));
        } else {
            page = Topic.dao.getHot(groupID, getParaToInt(2, 1));
        }
        setAttr("mode", mode);
        setAttr("page", page);
        setAttr("group", group);
        setAttr("actionUrl", "/groups/" + groupID + "-" + mode);
        render("/topic_list.html");
    }


    @Before(LoginInterceptor.class)
    public void listMember() {
        User user = getSessionAttr("user");
        int groupID = getParaToInt(0, 1);
        Groups group = Groups.dao.getById(groupID);
        Page<User> page = User.dao.list(getParaToInt(0, 1), getParaToInt(1, 1));
        setAttr("page", page);
        setAttr("group", group);
        setAttr("actionUrl", "/listMember/" + groupID + "-");
        render("/member_list.html");

    }

    //让没有组的用户选择组
    @Before(LoginInterceptor.class)
    public void selectGroup() {
        keepPara("msg");
        setAttr("page", Groups.dao.list(getParaToInt(0, 1)));
        render("/group_join.html");
    }

    @Before(LoginInterceptor.class)
    public void addMember() {
        int GroupID = getParaToInt("groupID");
        Groups group = Groups.dao.getById(GroupID);
        if (getPara("pw").equals(group.getStr("pw"))) {
            ((User) getSessionAttr("user")).set("groupID", GroupID).update();
            redirect("/");
        } else {
            setAttr("msg", "无效密码");
            redirect("/group/selectGroup");
        }
    }

    @Before(LoginInterceptor.class)
    public void newTopic() {
        Groups group = Groups.dao.getById(getParaToInt(0));
        setAttr("group", group);
        render("/topic_add.html");
    }

    @Before(LoginInterceptor.class)
    public void newVote() {
        Groups group = Groups.dao.getById(getParaToInt(0));
        setAttr("group", group);
        render("/vote_add.html");
    }


    //跳转到新组页面(仅教师)
    @Before({LoginInterceptor.class, TeacherInterceptor.class})
    public void newTeacherGroup() {
        render("/group_add.html");
    }


    //添加教师组
    @Before({LoginInterceptor.class, TeacherInterceptor.class})
    public void addTeacherGroup() {
        User user = getSessionAttr("user");
        Groups group = getModel(Groups.class);
        group.newGroup(user.getInt("id"), getPara("name"), getPara("pw"), getPara("content"), "private");
        redirect("/groups/index");
    }


}


