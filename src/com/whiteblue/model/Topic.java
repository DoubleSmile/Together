package com.whiteblue.model;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.whiteblue.config.Cfg;

import java.util.Date;
import java.util.List;

/**
 * Created by WhiteBlue on 15/1/15.
 */
public class Topic extends Model<Topic> {
    public static final Topic dao = new Topic();

    private static final String TOPIC_CACHE = "topic";
    private static final String INDEX_TOPIC_CACHE = "index_topic";
    private static final String TOPIC_LIST_CACHE="topic_list";

    public Topic() {
        super(TOPIC_CACHE);
    }


    public Topic getById(int Id) {
        return loadModel(Id);
    }

    //得到首页热点信息
    public List<Topic> getForIndex() {
        List<Topic> list = dao.findByCache(INDEX_TOPIC_CACHE,0,"select id from topic where type = 'public' and mode != 'ban' order by topTime desc ,postCount desc limit 5");
        return loadModelList(list);
    }


    public Page<Topic> getForGroup(int groupID, int pageNumber) {
        Page<Topic> page = dao.paginateByCache(TOPIC_LIST_CACHE, groupID + "-" + pageNumber, pageNumber, Cfg.PAGE_SIZE, "select id", "from topic where groupID = ? and mode != 'ban' order by isTeacher desc, topTime desc ,postCount desc", groupID);
        return loadModelPage(page);
    }

    public Page<Topic> getTeacher(int groupID, int pageNumber) {
        Page<Topic> page = dao.paginateByCache(TOPIC_LIST_CACHE, groupID + "-" + pageNumber+"-teacher", pageNumber, Cfg.PAGE_SIZE, "select id", "from topic where groupID = ? and mode != 'ban' and isTeacher=1 order by topTime desc", groupID);
        return loadModelPage(page);
    }

    public Page<Topic> getHot(int groupID, int pageNumber) {
        Page<Topic> page = dao.paginateByCache(TOPIC_LIST_CACHE, groupID + "-" + pageNumber+"-hot", pageNumber, Cfg.PAGE_SIZE, "select id", "from topic where groupID = ? and mode != 'ban' order by postCount desc", groupID);
        return loadModelPage(page);
    }

    public void newTopic(String title, int userID, String content, int groupID, int isTeacher) {
        this.set("title", title).set("content", content);
        this.set("userID", userID);
        this.set("time", new Date());
        this.set("groupID", groupID);
        this.set("postCount", 0);
        this.set("topTime", new Date());
        this.set("isTeacher", isTeacher);
        this.set("mode", "topic");
        Groups group = Groups.dao.getById(groupID);
        if (group.get("mode").equals("user")) {
            this.set("type", "user");
        } else if (group.get("mode").equals("public")) {
            this.set("type", "public");
        } else {
            this.set("type", "private");
        }
        this.save();
        removeCache();
    }

    public void newVote(String title, int userID, String content, int groupID, int isTeacher) {
        this.set("title", title).set("content", content);
        this.set("userID", userID);
        this.set("time", new Date());
        this.set("groupID", groupID);
        this.set("postCount", 0);
        this.set("topTime", new Date());
        this.set("isTeacher", isTeacher);
        this.set("mode", "ban");
        Groups group = Groups.dao.getById(groupID);
        if (group.get("mode").equals("user")) {
            this.set("type", "user");
        } else if (group.get("mode").equals("public")) {
            this.set("type", "public");
        } else {
            this.set("type", "private");
        }
        this.save();
        removeCache();
    }

    public void setVote() {
        this.set("mode", "vote");
        this.update();
    }


    //增长回复计数
    public void addPostCount() {
        this.set("postCount", (Integer) this.get("postCount") + 1);
        this.update();
        removeCache();
    }


    //更新置顶参数
    public void updateTopTime() {
        this.set("topTime", new Date());
        this.update();
        removeCache();
    }


    public void deleteTopic() {
        this.delete();
        removeCache();
    }

    /* getter */
    public User getUser() {
        return User.dao.getById(this.getInt("userID"));
    }

    public Groups getGroups() {
        return Groups.dao.getById(getInt("groupID"));
    }

    public void removeCache() {
        CacheKit.removeAll(TOPIC_CACHE);
        CacheKit.removeAll(INDEX_TOPIC_CACHE);
        CacheKit.removeAll(TOPIC_LIST_CACHE);
    }


}
