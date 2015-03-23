package com.whiteblue.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.whiteblue.model.User;

/**
 * Created by ynk on 15/1/30.
 * 只有学生能通过
 */
public class StudentInterceptor implements Interceptor {

    @Override
    public void intercept(ActionInvocation actionInvocation) {
        Controller controller = actionInvocation.getController();
        if (controller.getSessionAttr("user") != null) {
            User user = controller.getSessionAttr("user");
            if (user.getInt("isTeacher") == 0) {
                actionInvocation.invoke();
            } else {
                controller.setAttr("msg", "请使用教师页面");
                controller.render("/teacher/index.html");
            }
        } else {
            controller.setAttr("msg", "请先登录");
            controller.render("/login.html");
        }
    }
}
