package com.whiteblue.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.whiteblue.beetl.PrintTimeFunction;
import com.whiteblue.interceptor.LoginInterceptor;
import com.whiteblue.interceptor.TeacherInterceptor;
import com.whiteblue.model.Choice;
import com.whiteblue.model.Post;
import com.whiteblue.model.Topic;
import com.whiteblue.model.User;
import com.whiteblue.validator.PostValidator;

/**
 * Created by WhiteBlue on 15/1/29.
 */
public class PostController extends WbController {

    //访问帖子
    public void index() {
        int topicID = getParaToInt(0, 1);
        Topic topic = Topic.dao.getById(topicID);
        setAttr("topic", topic);
        //投票判断
        if (topic.getStr("mode").equals("vote")) {
        	if(getSessionAttr("user")!=null){
	            if (PrintTimeFunction.isOverTime(topic.get("time"), 3)) {
	                setAttr("canAdd", 0);
	            } else {
		                if (Post.dao.isVote(topicID, ((User) getSessionAttr("user")).getInt("id"))) {
		                    setAttr("canAdd", 2);
		                } else {
		                    setAttr("canAdd", 1);
		                }
	            }
        	}
            setAttr("list", Choice.dao.getList(getParaToInt(0, 1)));
            int sum = topic.getInt("postCount");
            if (sum == 0) {
                setAttr("sum", 1);
            } else {
                setAttr("sum", sum);
            }
            render("/vote_info.html");
        } else {
            Page<Post> postPage = Post.dao.getPostPage(getParaToInt(1, 1), topicID);
            setAttr("page", postPage);
            setAttr("actionUrl", "/post/" + topicID + "-");
            render("/topic_info.html");
        }

    }


    //新回复
    @Before({LoginInterceptor.class, PostValidator.class})
    public void newPost() {
        int topicID = getParaToInt(0);
        Topic topic = Topic.dao.getById(topicID);
        topic.addPostCount();
        topic.updateTopTime();
        Post post = getModel(Post.class);
        post.newPost(topicID, getPara("content"), ((User) getSessionAttr("user")).getInt("id"));
        redirect("/post/" + topicID + "-" + getParaToInt(1, 1));
    }

    //新回复
    @Before(LoginInterceptor.class)
    public void vote() {
        int topicID = getParaToInt(0, 1);
        Topic topic = Topic.dao.getById(topicID);
        Choice choice = Choice.dao.getById(getParaToInt("choice"));
        topic.addPostCount();
        topic.updateTopTime();
        choice.addCount();
        Post post = getModel(Post.class);
        post.newPost(topicID, "0", ((User) getSessionAttr("user")).getInt("id"));
        redirect("/post/" + topicID);
    }


    //新投票
    public void addVote() {
        int topicID = getParaToInt(0, 1);
        User user = getSessionAttr("user");
        Topic topic = Topic.dao.getById(topicID);
        topic.updateTopTime();
        topic.addPostCount();
        if (Post.dao.isVote(topicID, user.getInt("id"))) {
            setAttr("msg", "0");
            redirect("/post/" + topicID);
        } else {
            Post post = getModel(Post.class);
            post.newChoice(topicID, getPara("content"), user.getInt("id"));
            setAttr("msg", "1");
            redirect("/post/" + topicID);
        }
    }


    @Before(TeacherInterceptor.class)
    public void deletePost() {
        int topicID = getParaToInt(0, 1);
        Post.dao.deletePost(getParaToInt(1));
        redirect("/post/" + topicID);
    }
}
