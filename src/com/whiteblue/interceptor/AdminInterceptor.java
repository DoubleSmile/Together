package com.whiteblue.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.whiteblue.model.User;

/**
 * Created by WhiteBlue on 15/1/27.
 */
public class AdminInterceptor implements Interceptor {
    @Override
    public void intercept(ActionInvocation actionInvocation) {
        Controller controller = actionInvocation.getController();
        User user = controller.getSessionAttr("user");
        if(user.getInt("isTeacher")==2){
            actionInvocation.invoke();
        }else{
            controller.setAttr("msg","权限不足");
            controller.redirect("/");
        }
    }
}
