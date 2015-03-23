package com.whiteblue.controller;

import com.jfinal.aop.Before;
import com.whiteblue.interceptor.GroupInterceptor;
import com.whiteblue.model.Topic;

/**
 * Created by WhiteBlue on 15/1/15.
 */
public class IndexController extends WbController {

    public void index() {
        setAttr("list", Topic.dao.getForIndex());
        render("/index.html");
    }

    public void register() {
        if (getSessionAttr("user") != null) {
            setAttr("msg", "你已登陆");
            render("/register.html");
        } else {
            render("/register.html");
        }
    }

    public void login() {
        if (getSessionAttr("user") != null) {
            setAttr("msg", "你已登陆");
            render("/login.html");
        } else {
            render("/login.html");
        }

    }

    public void help() {
        render("/help.html");
    }

}
