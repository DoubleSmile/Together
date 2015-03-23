package com.whiteblue.model;

import com.jfinal.plugin.ehcache.CacheKit;

import java.util.List;

/**
 * Created by WhiteBlue on 15/3/20.
 */
public class Link extends Model<Link> {
    public static final Link dao = new Link();

    public static final String LINK_CACHE = "link";

    public Link() {
        super(LINK_CACHE);
    }

    public List<Link> getList(int userID) {
        return loadModelList(dao.find("select id from link where userID = ?", userID));
    }

    public boolean isHave(int userID,int groupID){
        if(dao.findFirst("select id from link where userID = ? and groupID = ?", userID, groupID)==null){
            return false;
        }else{
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
    }

    public Groups getGroups() {
        return Groups.dao.getById(this.getInt("groupID"));
    }

    public User getUser() {
        return User.dao.getById(this.getInt("userID"));
    }

    public void remove() {
        CacheKit.removeAll(LINK_CACHE);
        this.delete();
    }

}
