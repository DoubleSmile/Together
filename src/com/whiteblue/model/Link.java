package com.whiteblue.model;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.whiteblue.config.Cfg;

import java.util.List;

/**
 * Created by WhiteBlue on 15/3/20.
 */
public class Link extends Model<Link> {
    public static final Link dao = new Link();

    public static final String LINK_CACHE = "link";
    public static final String LINK_LIST_CACHE = "link";

    public Link() {
        super(LINK_CACHE);
    }

    public List<Link> getList(int userID) {
        return loadModelList(dao.findByCache(LINK_LIST_CACHE,"list-"+userID,"select id from link where userID = ?", userID));
    }

    public Page<Link> getUserList(int groupID,int pageNumber){
        return loadModelPage(dao.paginateByCache(LINK_LIST_CACHE,"userList-"+groupID+"-"+pageNumber,pageNumber, Cfg.PAGE_SIZE,"select id ","from link where groupID = ?",groupID));
    }

    public boolean isHave(int userID, int groupID) {
        if (dao.findFirst("select id from link where userID = ? and groupID = ?", userID, groupID) == null) {
            return false;
        } else {
            return true;
        }
    }

    public int getCount(int userID) {
        List<Link> list = dao.find("select id from link where userID = ?", userID);
        return list.size();
    }

    //根据详细信息得到
    public Link getByInfo(int userID, int groupID) {
        return dao.findFirst("select id from link where userID = ? and groupID = ?", userID, groupID);
    }

    public void add(int userID, int groupID) {
        this.set("userID", userID);
        this.set("groupID", groupID);
        this.save();
        removeCache();
    }


    public Groups getGroups() {
        return Groups.dao.getById(this.getInt("groupID"));
    }

    public User getUser() {
        return User.dao.getById(this.getInt("userID"));
    }

    public void remove() {
        this.delete();
        removeCache();
    }

    public void removeCache() {
        CacheKit.removeAll(LINK_CACHE);
    }

}
