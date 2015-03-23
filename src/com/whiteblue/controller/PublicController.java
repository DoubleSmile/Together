package com.whiteblue.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.whiteblue.interceptor.LoginInterceptor;
import com.whiteblue.model.Groups;
import com.whiteblue.model.Topic;
import com.whiteblue.model.User;
import com.whiteblue.validator.TopicValidator;

import java.util.List;

/**
 * Created by WhiteBlue on 15/2/20.
 */
public class PublicController extends Controller {

    public void index() {
        List<Groups> list = Groups.dao.listPublic();
        setAttr("list", list);
        render("/public_group_list.html");
    }


    public void listTopic() {
        int groupID = getParaToInt(0, 1);
        Groups group = Groups.dao.getById(groupID);
        Page<Topic> page = Topic.dao.getForGroup(groupID, getParaToInt(1, 1));
        setAttr("page", page);
        setAttr("group", group);
        setAttr("actionUrl", "/public/listTopic/" + groupID + "-");
        render("/public_topic_list.html");
    }


    @Before({LoginInterceptor.class, TopicValidator.class})
    public void newTopic() {
        int groupID = getParaToInt(0);
        Topic topic = getModel(Topic.class);
        User user = getSessionAttr("user");
        topic.newTopic(getPara("title"), user.getInt("id"), getPara("content"), groupID, user.getInt("isTeacher"));
        redirect("/public/" + groupID);
    }




    public void help() {
        render("/help.html");
    }
}
