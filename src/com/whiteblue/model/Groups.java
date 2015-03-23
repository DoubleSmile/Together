package com.whiteblue.model;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.whiteblue.config.Cfg;

import java.util.List;

/**
 * Created by WhiteBlue on 15/1/20.
 */
public class Groups extends Model<Groups> {
    public static final Groups dao = new Groups();

    private static final String GROUP_CACHE = "group";
    private static final String GROUP_LIST_CACHE = "group_list";
    private static final String PUBLIC_GROUP_LIST_CACHE = "public_group_list";
    private static final String PUBLIC_LIST_CACHE = "public_list";


    public Groups() {
        super(GROUP_CACHE);
    }

    public boolean hasGroup(String name) {
        if (dao.findFirst("select id from groups where name= ?", name) != null)
            return true;
        else
            return false;
    }

    public void newGroup(int userID, String name, String pw,String content,String mode) {
        this.set("userID", userID);
        this.set("name", name);
        this.set("pw", pw);
        this.set("content",content);
        this.set("mode",mode);
        this.save();
        CacheKit.removeAll(GROUP_LIST_CACHE);
    }

    public void newPublic(String name,String content) {
        this.set("name", name);
        this.set("content", content);
        this.set("mode","public");
        this.save();
        CacheKit.removeAll(PUBLIC_LIST_CACHE);
    }

    public Page<Groups> listAll(int pageNumber) {
        Page<Groups> page = dao.paginateByCache(GROUP_LIST_CACHE, "all-"+pageNumber, pageNumber, Cfg.PAGE_SIZE, "select id", "from groups");
        return loadModelPage(page);
    }


    //得到分组列表(私有)
    public Page<Groups> list(int pageNumber) {
        Page<Groups> page = dao.paginateByCache(GROUP_LIST_CACHE, "list-"+pageNumber, pageNumber, Cfg.PAGE_SIZE, "select id", "from groups where mode = 'private'");
        return loadModelPage(page);
    }

    //得到学生组列表
    public Page<Groups> listUserGroups(int pageNumber){
        Page<Groups> page = dao.paginateByCache(PUBLIC_GROUP_LIST_CACHE, "list-public-"+pageNumber, pageNumber, Cfg.PAGE_SIZE, "select id", "from groups where mode = 'user'");
        return loadModelPage(page);
    }

    //得到公共分组
    public List<Groups> listPublic() {
        List<Groups> list = dao.findByCache(PUBLIC_LIST_CACHE, 0, "select id from groups where mode = 'public'");
        return loadModelList(list);
    }


    public List<Groups> listForTeacher(int teacherID) {
        List<Groups> list = dao.find("select id from groups where userID = ? and mode = 'private' limit 5", teacherID);
        return loadModelList(list);
    }


    public User getUser() {
        return User.dao.getById(this.getInt("teacherID"));
    }

    public Groups getById(int Id) {
        return loadModel(Id);
    }

    public Groups getByPw(int ID, String pw) {
        return findFirst("select id from groups where id = ? and pw = ?", ID, pw);
    }

    public void deleteGroup() {
        this.delete();
        CacheKit.removeAll(GROUP_LIST_CACHE);
        CacheKit.removeAll(PUBLIC_LIST_CACHE);
        CacheKit.removeAll(PUBLIC_GROUP_LIST_CACHE);
    }

}
