package com.whiteblue.model;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.whiteblue.config.Cfg;

import java.util.Date;

/**
 * Created by WhiteBlue on 15/1/18.
 */
public class Post extends Model<Post> {
    public static final Post dao = new Post();

    public static final String POST_CACHE = "post";

    public Post() {
        super(POST_CACHE);
    }


    public Page<Post> getPostPage(int pageNumber, int topicID) {
        Page<Post> page = dao.paginateByCache("topic-" + topicID, topicID + "-" + pageNumber, pageNumber, Cfg.PAGE_SIZE, "select id", "from post where topicID = ?", topicID);
        return loadModelPage(page);
    }

    public void newPost(int topicID, String content, int writer) {
        this.set("topicID", topicID);
        this.set("content", content);
        this.set("time", new Date());
        this.set("userID", writer);
        this.save();
        removeCache();
    }

    //新建投票纪录
    public void newChoice(int topicID, String content, int writer) {
        this.set("topicID", topicID).set("content", content);
        this.set("time", new Date());
        this.set("userID", writer);
        this.save();
        removeCache();
    }

    //用户是否已投票
    public boolean isVote(int topicID, int userID) {
        if (dao.findFirst("select id from post where topicID = ? and userID = ?", topicID, userID) == null) {
            return false;
        } else {
            return true;
        }
    }

    public void deletePost(int Id) {
        dao.deleteById(Id);
        removeCache();
    }

    public void removeCache() {
        CacheKit.removeAll("topic-" + this.getInt("topicID"));
    }

    public User getUser() {
        return User.dao.getById(this.getInt("userID"));
    }
}
