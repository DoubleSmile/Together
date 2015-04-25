package com.whiteblue.model;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.whiteblue.config.Cfg;

import java.util.Date;


/**
 * Created by WhiteBlue on 15/1/15.
 */
public class User extends Model<User> {

    public static final User dao = new User();

    private static final String USER_CACHE = "user";
    private static final String USER_LIST_CACHE = "user_list";


    public User() {
        super(USER_CACHE);
    }


    public User getById(int Id) {
        return loadModel(Id);
    }


    //判断Account是否存在
    public boolean hasUserId(String account) {
        if (dao.findFirst("select id from user where account = ?", account) != null)
            return true;
        else
            return false;
    }

    //保存新用户
    public void saveNew(String account, String name, String pw, String feeling) {
        this.set("account", account).set("name", name).set("feeling", feeling);
        this.set("password", pw).set("time", new Date()).set("isTeacher", 0);
        this.save();
    }

    //保存新教师
    public void newTeacher(String account, String name, String pw) {
        this.set("account", account).set("name", name);
        this.set("password", pw).set("time", new Date());
        this.set("isTeacher", 1);
        this.save();
    }

    //用户信息更新（编辑）
    public void userUpdate(String name, String feeling) {
        this.set("name", name).set("feeling", feeling);
        this.update();
        removeCache();
    }

    //新用户设置组
    public void setGroup(int groupID) {
        this.set("groupID", groupID);
        this.update();
        removeCache();
    }


    //用于一般登陆
    public User getByPw(String account, String pw_md5) {
        return (User) dao.findFirst("select * from user where account=? and password=?", account, pw_md5);
    }

    //凭借cookies登陆
    public User getByCookies(String ID, String pw_md5) {
        return (User) dao.findFirst("select * from user where id=? and password=?", ID, pw_md5);
    }


    //得到人员列表
    public Page<User> list(int groupID, int pageNumber) {
        Page<User> page = dao.paginateByCache(USER_LIST_CACHE, groupID + '-' + pageNumber, pageNumber, Cfg.PAGE_SIZE, "select id", "from user where groupID = ?", groupID);
        return loadModelPage(page);
    }

    //得到教师列表
    public Page<User> listTeacher(int pageNumber) {
        return dao.paginate(pageNumber, Cfg.PAGE_SIZE, "select *", "from user where isTeacher = 1");
    }

    //得到学生列表
    public Page<User> listStudent(int pageNumber) {
        return dao.paginate(pageNumber, Cfg.PAGE_SIZE, "select *", "from user where isTeacher = 0");
    }


    //根据用户得到组
    public Groups getGroups() {
        return Groups.dao.getById(this.getInt("groupID"));
    }


    //删除用户
    public void deleteUser() {
        this.delete();
        removeCache();
    }

    //清除组信息
    public void clearGroup() {
        this.set("groupID", null);
        this.update();
        removeCache();
    }

    public void removeCache(){
        CacheKit.removeAll(USER_CACHE);
        CacheKit.removeAll(USER_LIST_CACHE);
    }


}
