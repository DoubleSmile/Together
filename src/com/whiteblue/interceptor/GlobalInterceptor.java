package com.whiteblue.interceptor;


import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.ehcache.CacheKit;
import com.whiteblue.config.Cfg;
import com.whiteblue.model.Message;
import com.whiteblue.model.News;
import com.whiteblue.model.User;
import com.whiteblue.tools.NewsUtil;

import java.util.List;


/**
 * Created by WhiteBlue on 15/1/15.
 */
public class GlobalInterceptor implements Interceptor {
    public static final String NEWS_CACHE="news";

    @Override
    public void intercept(ActionInvocation actionInvocation) {
        Controller controller = actionInvocation.getController();

        //检查登录状态加入cookie
        if (controller.getSessionAttr("user") == null && (controller.getCookie("user") != null)) {
            String cookies = controller.getCookie("user");
            if (cookies.contains("-")) {
                String split[] = cookies.split("-");
                User user = User.dao.getByCookies(split[0], split[1]);
                if (user != null) {
                    controller.getSession().setMaxInactiveInterval(1600);//设置失效时间(s)
                    controller.setSessionAttr("user", user);
                } else {
                    controller.removeCookie("user");
                }
            } else {
                controller.removeCookie("user");
            }
        }

        //新闻的添加
        List<News> list= CacheKit.get(NEWS_CACHE, 0);
        if(list==null){
            list= NewsUtil.getNews(Cfg.RSS_URL);
            CacheKit.put(NEWS_CACHE,0,list);
        }
        controller.setAttr("news_list", list);


        //检查新消息
        if (controller.getSessionAttr("user") != null) {
            User user = controller.getSessionAttr("user");
            controller.setAttr("messageNumber", Message.dao.getNumber(user.getInt("id")));
        }
        actionInvocation.invoke();
    }
}
