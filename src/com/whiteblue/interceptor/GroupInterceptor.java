package com.whiteblue.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.whiteblue.model.User;

/**
 * Created by ynk on 15/1/31.
 */
public class GroupInterceptor implements Interceptor {
    @Override
    public void intercept(ActionInvocation actionInvocation) {
        Controller controller = actionInvocation.getController();
        if (controller.getSessionAttr("user") != null) {
            User user = controller.getSessionAttr("user");
            if ((user.get("groupID") == null) && (user.getInt("isTeacher") != 1)) {
                controller.redirect("/groups/selectGroup");
            } else {
                actionInvocation.invoke();
            }
        } else {
            actionInvocation.invoke();
        }
    }
}
