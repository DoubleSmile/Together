package com.whiteblue.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.whiteblue.model.Link;
import com.whiteblue.model.User;

/**
 * Created by WhiteBlue on 15/3/22.
 */
public class UserGroupsInterceptor implements Interceptor {
    @Override
    public void intercept(ActionInvocation actionInvocation) {
        Controller controller = actionInvocation.getController();
            User user = controller.getSessionAttr("user");
            if(Link.dao.getCount(user.getInt("id"))==0){
                controller.setAttr("msg","您尚未加入组,请选择组加入");
                controller.redirect("/user-groups/selectGroup");
            }else{
                actionInvocation.invoke();
            }
    }
}
