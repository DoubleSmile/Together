package com.whiteblue.model;

import com.jfinal.plugin.ehcache.CacheKit;

import java.util.List;

/**
 * Created by WhiteBlue on 15/2/18.
 */
public class Choice extends Model<Choice> {
    public static final Choice dao = new Choice();

    public static final String CHOICE_CACHE = "choice";
    public static final String CHOICE_LIST_CACHE = "choice_list";

    public Choice() {
        super(CHOICE_CACHE);
    }

    public Choice getById(int Id) {
        return loadModel(Id);
    }

    public void newChoice(String title, int voteID) {
        this.set("title", title).set("voteID", voteID);
        this.save();
    }

    public List<Choice> getList(int voteID) {
        List<Choice> list = dao.findByCache(CHOICE_LIST_CACHE, voteID, "select id from choice where voteID = ? limit 5", voteID);
        return loadModelList(list);
    }

    public void addCount() {
        this.set("count", this.getInt("count") + 1);
        this.update();
        CacheKit.remove(CHOICE_CACHE, this.getInt("id"));
    }

    //检查是否选项过多
    public int numChoice(int voteID) {
        List<Choice> list = dao.find("select * from choice where voteID = ?", voteID);
        return list.size();
    }

    public void addChoice() {
        int count = this.getInt("count");
        this.set("count", this.getInt("count") + 1);
        this.update();
        CacheKit.remove(CHOICE_CACHE, this.getInt("id"));
    }


}
