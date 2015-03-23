package com.whiteblue.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.whiteblue.model.User;

/**
 * Created by WhiteBlue on 15/2/22.
 */
public class RegisterValidator extends Validator {
    @Override
    protected void validate(Controller controller) {
        validateString("account", true, 1, 20, "account_msg", "账户输入非法");
        validateString("name", true, 1, 20, "name_msg", "名称输入非法");
        validateString("pw1", true, 4, 10, "pw_msg", "密码不正确(4到10位的字符)");
        validateString("pw2", true, 4, 10, "pw_msg", "两次密码不一致");
        if(!controller.getPara("pw1").equals(controller.getPara("pw2"))){
            addError("msg","两次密码不一致");
        }
        if (User.dao.hasUserId(controller.getPara("account"))) {
            addError("msg", "账户已存在");
        }
    }

    @Override
    protected void handleError(Controller controller) {
        controller.render("/register.html");
    }
}
