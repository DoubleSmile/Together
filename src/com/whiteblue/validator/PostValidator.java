package com.whiteblue.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * Created by WhiteBlue on 15/2/22.
 */
public class PostValidator extends Validator {
    @Override
    protected void validate(Controller controller) {
        validateRequired("content", "msg", "内容不能为空");
        validateString("content", true, 1, 200, "msg", "内容输入非法");
    }

    @Override
    protected void handleError(Controller controller) {
        controller.renderJson();
    }
}
