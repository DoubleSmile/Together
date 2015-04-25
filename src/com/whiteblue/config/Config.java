package com.whiteblue.config;

import com.jfinal.config.*;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.whiteblue.beetl.BeetlRenderFactory;
import com.whiteblue.controller.*;
import com.whiteblue.interceptor.GlobalInterceptor;
import com.whiteblue.model.*;

/**
 * Created by WhiteBlue on 15/1/14.
 */
public class Config extends JFinalConfig {

    /**
     * 配置常量
     */
    public void configConstant(Constants me) {
        me.setDevMode(false);
        me.setMainRenderFactory(new BeetlRenderFactory());
        me.setError404View("/error404.html");
        me.setError500View("/error500.html");
    }


    /**
     * 配置路由
     */
    public void configRoute(Routes me) {
        me.add("/", IndexController.class).add("/user", UserController.class);
        me.add("/groups", GroupController.class).add("/topic", TopicController.class);
        me.add("/msg", MessageController.class).add("/admin", AdminController.class);
        me.add("/post", PostController.class);
        me.add("/public", PublicController.class);
        me.add("/user-groups", UserGroupsController.class);
    }


    /**
     * 配置插件
     */
    public void configPlugin(Plugins me) {
        String jdbcUrl = PropKit.use("config.txt").get("SQL_URL");
        String user = PropKit.use("config.txt").get("SQL_USER");
        String password = PropKit.use("config.txt").get("SQL_PW");

        C3p0Plugin cp = new C3p0Plugin(jdbcUrl, user, password);
        me.add(cp);
        ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
        arp.setShowSql(false);
        me.add(new EhCachePlugin());
        me.add(arp);
        arp.addMapping("post", Post.class).addMapping("user", User.class)
                .addMapping("topic", Topic.class)
                .addMapping("groups", Groups.class)
                .addMapping("message", Message.class)
                .addMapping("returnMsg", ReturnMsg.class).addMapping("choice", Choice.class).addMapping("link", Link.class);
    }


    /**
     * 配置全局拦截器
     */
    public void configInterceptor(Interceptors me) {
        me.add(new GlobalInterceptor());
    }


    /**
     * 配置处理器
     */
    public void configHandler(Handlers me) {
        me.add(new ContextPathHandler("base"));
    }

    /**
     * 初始化常量
     */
    public void afterJFinalStart() {
    }

}