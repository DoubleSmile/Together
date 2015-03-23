package com.whiteblue.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * Created by WhiteBlue on 15/2/22.
 */
public class UserValidator extends Validator {
    @Override
    protected void validate(Controller controller) {
        validateRequired("name", "msg", "名称不能为空");
        validateString("name", true, 1, 20, "msg", "名称输入非法");
        validateString("feeling", false, 0, 100, "msg", "签名输入非法");
    }

    @Override
    protected void handleError(Controller controller) {
        controller.render("/index.html");
    }
}
