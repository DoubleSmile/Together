package com.whiteblue.controller;

import com.jfinal.aop.Before;
import com.whiteblue.interceptor.TeacherInterceptor;
import com.whiteblue.model.Groups;
import com.whiteblue.model.User;

/**
 * Created by ynk on 15/1/30.
 */
public class TeacherController extends WbController {


    @Before(TeacherInterceptor.class)
    public void newGroup() {
        Groups group = getModel(Groups.class);
        group.newGroup(((User) getSessionAttr("user")).getInt("id"), getPara("name"), getPara("pw"),getPara("content"),"private");
        redirect("/groups");
    }


}
