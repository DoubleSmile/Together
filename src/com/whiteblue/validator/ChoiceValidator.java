package com.whiteblue.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * Created by WhiteBlue on 15/2/22.
 */
public class ChoiceValidator extends Validator {
    @Override
    protected void validate(Controller controller) {
        validateRequired("title", "msg", "标题不能为空");
        validateString("title", true, 1, 30, "msg", "标题输入非法(30字以下)");
    }

    @Override
    protected void handleError(Controller controller) {
        controller.renderJson();
    }
}
