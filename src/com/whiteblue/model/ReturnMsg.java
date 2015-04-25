package com.whiteblue.model;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.whiteblue.config.Cfg;

import java.util.Date;

/**
 * Created by WhiteBlue on 15/1/25.
 */
public class ReturnMsg extends Model<ReturnMsg> {
    public static final ReturnMsg dao = new ReturnMsg();

    private static final String RETURN_MSG_CACHE = "return_msg";
    private static final String RETURN_MSG_LIST_CACHE = "return_msg_list";

    public ReturnMsg() {
        super(RETURN_MSG_CACHE);
    }

    public Page<ReturnMsg> getMsgForUser(int pageNumber, int messageID) {
        Page<ReturnMsg> page = dao.paginateByCache(RETURN_MSG_LIST_CACHE, messageID + "-" + pageNumber, pageNumber, Cfg.PAGE_SIZE, "select id", "from returnMsg where messageID = ?", messageID);
        return loadModelPage(page);
    }

    public void NewMsg(int messageID, int userID, String content) {
        this.set("userID", userID);
        this.set("content", content);
        this.set("messageID", messageID);
        this.set("time", new Date());
        this.save();
        removeCache();
    }

    public User getUser() {
        return User.dao.getById(this.getInt("userID"));
    }

    public void removeCache(){
        CacheKit.removeAll(RETURN_MSG_CACHE);
        CacheKit.removeAll(RETURN_MSG_LIST_CACHE);
    }

}
