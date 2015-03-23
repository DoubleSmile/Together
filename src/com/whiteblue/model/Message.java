package com.whiteblue.model;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.whiteblue.config.Cfg;

import java.util.Date;
import java.util.List;

/**
 * Created by WhiteBlue on 15/1/24.
 */
//私信(只显示前80条)
public class Message extends Model<Message> {
    public static final Message dao = new Message();

    private static final String MESSAGE_CACHE = "message";
    private static final String MESSAGE_LIST_CACHE = "message_list";
    private static final String MESSAGE_NUMBER_CACHE = "message_number";

    public Message() {
        super(MESSAGE_CACHE);
    }

    public Message getById(int Id) {
        return loadModel(Id);
    }

    //得到消息列表
    public Page<Message> getMessageForMe(int pageNumber, int ID) {
        Page<Message> page = dao.paginateByCache(MESSAGE_LIST_CACHE, ID + "-" + pageNumber, pageNumber, Cfg.PAGE_SIZE, "select id", "from message where toID = ? or userID = ?", ID, ID);
        return loadModelPage(page);
    }


    //发送新私信
    public void sendNew(int userID, int toID, String content) {
        this.set("userID", userID);
        this.set("toID", toID).set("content", content);
        this.set("time", new Date());
        this.set("readA", 1);
        this.save();
        removeCache();
    }

    public User getUser() {
        return User.dao.getById(this.getInt("userID"));
    }

    public User getToUser(){
        return User.dao.getById(this.getInt("toID"));
    }


    public void setReadA() {
        set("readA", 1);
        update();
        removeCache();
    }

    public void setReadB() {
        set("readB", 1);
        update();
        removeCache();
    }

    //得到私信的数量
    public int getNumber(int userID) {
        Integer number = CacheKit.get(MESSAGE_NUMBER_CACHE, userID);
        if (number == null) {
            List<Message> list = dao.find("select id from message where toID = ? and readB = 0 or userID=? and readA=0 limit 80", userID, userID);
            if (list != null) {
                number = list.size();
                if (number >= 99)
                    number = 99;
            } else
                number = 0;
            CacheKit.put(MESSAGE_NUMBER_CACHE, userID, number);
        }
        return number;
    }

    public void removeCache() {
        CacheKit.removeAll(MESSAGE_LIST_CACHE);
        CacheKit.removeAll(MESSAGE_NUMBER_CACHE);
    }

}
