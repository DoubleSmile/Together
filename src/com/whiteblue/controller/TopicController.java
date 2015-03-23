package com.whiteblue.controller;

import com.jfinal.aop.Before;
import com.whiteblue.interceptor.LoginInterceptor;
import com.whiteblue.interceptor.TeacherInterceptor;
import com.whiteblue.model.Choice;
import com.whiteblue.model.Groups;
import com.whiteblue.model.Topic;
import com.whiteblue.model.User;
import com.whiteblue.validator.ChoiceValidator;
import com.whiteblue.validator.TopicValidator;

/**
 * Created by WhiteBlue on 15/1/16.
 */
public class TopicController extends WbController {

    //新建帖子
    @Before({LoginInterceptor.class, TopicValidator.class})
    public void newTopic() {
            Topic topic = getModel(Topic.class);
            User user = getSessionAttr("user");
            topic.newTopic(getPara("title"), user.getInt("id"), getPara("content"), getParaToInt("groupID"), user.getInt("isTeacher"));
            redirect("/post/"+topic.get("id"));
    }


    //新建投票
    @Before({LoginInterceptor.class,TopicValidator.class})
    public void newVote() {
            Topic topic = getModel(Topic.class);
            User user = getSessionAttr("user");
            topic.newVote(getPara("title"), user.getInt("id"), getPara("content"),getParaToInt("groupID"), user.getInt("isTeacher"));
            setAttr("voteID", topic.getInt("id"));
            setAttr("msg", 1);
            renderJson();
    }


    //新建选项
    @Before({LoginInterceptor.class, ChoiceValidator.class})
    public void newChoice() {
        Topic topic = Topic.dao.getById(getParaToInt("voteID"));
        int num=Choice.dao.numChoice(topic.getInt("id"));
        setAttr("num",num);
        if (num>5) {
            setAttr("msg", 0);
            renderJson();
        } else {
            Choice choice = getModel(Choice.class);
            choice.newChoice(getPara("title"), topic.getInt("id"));
            topic.setVote();
            setAttr("msg", 1);
            renderJson();
        }
    }


    @Before(TeacherInterceptor.class)
    public void deleteTopic() {
        int topicID = getParaToInt(0, 1);
        Topic topic = Topic.dao.getById(topicID);
        topic.deleteTopic();
        redirect("/groups");
    }
}
