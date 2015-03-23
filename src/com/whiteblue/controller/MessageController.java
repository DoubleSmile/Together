package com.whiteblue.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.whiteblue.interceptor.GroupInterceptor;
import com.whiteblue.interceptor.LoginInterceptor;
import com.whiteblue.model.Message;
import com.whiteblue.model.ReturnMsg;
import com.whiteblue.model.User;

/**
 * Created by WhiteBlue on 15/1/25.
 */
public class MessageController extends WbController {

    //有缓存
    @Before(LoginInterceptor.class)
    public void index() {
        User user = getSessionAttr("user");
        setAttr("page", Message.dao.getMessageForMe(getParaToInt(0, 1), user.getInt("id")));
        setAttr("actionUrl", "/msg");
        render("/message_list.html");

    }

    //有缓存
    @Before(LoginInterceptor.class)
    public void info() {
        int messageID = getParaToInt(0, 1);
        User user = getSessionAttr("user");
        Page<ReturnMsg> page = ReturnMsg.dao.getMsgForUser(getParaToInt(1, 1), messageID);
        setAttr("page", page);
        Message msg = Message.dao.getById(messageID);
        setAttr("message", msg);
        //设定已读
        if (user.getInt("id") == msg.getInt("userID")) {
            msg.setReadA();
        } else {
            msg.setReadB();
        }
        setAttr("actionUrl", "/msg/info/" + getParaToInt(0) + "-");
        render("/message_info.html");
    }

    @Before(LoginInterceptor.class)
    public void sendMsg() {
        Message msg = getModel(Message.class);
        msg.sendNew(((User) getSessionAttr("user")).getInt("id"), getParaToInt("toID"), getPara("content"));
        setAttr("msg", 1);
        renderJson();
    }


    @Before(LoginInterceptor.class)
    public void newReturn() {
        ReturnMsg returnMsg = getModel(ReturnMsg.class);
        int messageID = getParaToInt("messageID");
        returnMsg.NewMsg(messageID, ((User) getSessionAttr("user")).getInt("id"), getPara("content"));
        Message message = Message.dao.getById(messageID);
        if (message.get("userID") == ((User) getSessionAttr("user")).getInt("id"))
            message.set("readB", 0);
        else
            message.set("readA", 0);
        message.update();
        redirect("/msg/info/" + messageID);
    }


}
