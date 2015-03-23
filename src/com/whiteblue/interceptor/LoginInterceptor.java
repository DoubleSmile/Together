package com.whiteblue.interceptor;


import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/**
 * Created by WhiteBlue on 15/1/15.
 */
public class LoginInterceptor implements Interceptor {
    @Override
    public void intercept(ActionInvocation actionInvocation) {
        Controller controller = actionInvocation.getController();
        if (controller.getSessionAttr("user") != null) {
            actionInvocation.invoke();
        } else {
            controller.setAttr("msg", "请先登录");
            controller.render("/login.html");
        }
    }
}
