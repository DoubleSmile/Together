package com.whiteblue.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * Created by WhiteBlue on 15/2/22.
 */
public class LoginValidator extends Validator {
    @Override
    protected void validate(Controller controller) {
            validateString("account", true, 1, 20, "account_msg", "账户输入非法");
            validateString("pw", true, 4, 20, "pw_msg", "密码不正确(4到20位)");
    }

    @Override
    protected void handleError(Controller controller) {
        controller.render("/login.html");
    }

}
