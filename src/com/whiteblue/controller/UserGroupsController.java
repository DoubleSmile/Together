package com.whiteblue.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.whiteblue.interceptor.LoginInterceptor;
import com.whiteblue.interceptor.UserGroupsInterceptor;
import com.whiteblue.model.Groups;
import com.whiteblue.model.Link;
import com.whiteblue.model.Topic;
import com.whiteblue.model.User;
import com.whiteblue.validator.GroupsValidator;

import java.util.List;

/**
 * Created by WhiteBlue on 15/3/22.
 */
public class UserGroupsController extends Controller {

    @Before({LoginInterceptor.class, UserGroupsInterceptor.class})
    public void index() {
        User user = this.getSessionAttr("user");
        setAttr("list", Link.dao.getList(user.getInt("id")));
        this.render("/user_group_list.html");
    }

    @Before({LoginInterceptor.class, UserGroupsInterceptor.class})
    public void listTopic() {
        int groupID = getParaToInt(0);
        User user = this.getSessionAttr("user");
        List<Link> list = Link.dao.getList(user.getInt("id"));
        if (Link.dao.getByInfo(user.getInt("id"), groupID) != null) {
            Groups group = Groups.dao.getById(groupID);
            Page<Topic> page = Topic.dao.getForGroup(groupID, getParaToInt(1, 1));
            setAttr("page", page);
            setAttr("group", group);
            setAttr("actionUrl", "/user-groups/listTopic/" + groupID + "-");
            render("/user_topic_list.html");
        } else {
            redirect("/user-groups");
        }
    }


    @Before({LoginInterceptor.class, UserGroupsInterceptor.class})
    public void newTopic() {
        Groups group = Groups.dao.getById(getParaToInt(0));
        setAttr("group", group);
        render("/topic_add.html");
    }

    @Before({LoginInterceptor.class, UserGroupsInterceptor.class})
    public void newVote() {
        Groups group = Groups.dao.getById(getParaToInt(0));
        setAttr("group", group);
        render("/vote_add.html");
    }

    @Before(LoginInterceptor.class)
    public void selectGroup() {
        setAttr("page", Groups.dao.listUserGroups(getParaToInt(0, 1)));
        setAttr("actionUrl", "/user-groups/selectGroup/");
        render("/user_group_select.html");
    }


    //跳转到新组页面
    @Before(LoginInterceptor.class)
    public void newGroup() {
        render("/user_group_add.html");
    }

    //添加普通组
    @Before({LoginInterceptor.class, GroupsValidator.class})
    public void addGroup() {
        User user = getSessionAttr("user");
        if (Link.dao.getCount(user.getInt("id")) > 5) {
            redirect("/user-groups");
        } else {
            Groups group = getModel(Groups.class);
            group.newGroup(user.getInt("id"), getPara("name"), getPara("pw"), getPara("content"), "user");
            Link link = getModel(Link.class);
            link.add(user.getInt("id"), group.getInt("id"));
            redirect("/user-groups/listTopic/" + group.getInt("id"));
        }
    }

}
