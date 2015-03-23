package com.whiteblue.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.whiteblue.model.User;

/**
 * Created by ynk on 15/1/30.
 */
public class TeacherInterceptor implements Interceptor {
    @Override
    public void intercept(ActionInvocation actionInvocation) {
        Controller controller = actionInvocation.getController();
        if (controller.getSessionAttr("user") != null) {
            User user = controller.getSessionAttr("user");
            if ((user.getInt("isTeacher") == 1) || (user.getInt("isTeacher") == 2)) {
                actionInvocation.invoke();
            } else {
                controller.setAttr("msg", "权限不足");
                controller.render("/index.html");
            }
        } else {
            controller.setAttr("msg", "请先登录");
            controller.render("/login.html");
        }
    }
}
