package com.whiteblue.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * Created by WhiteBlue on 15/2/24.
 */
public class VoteValidator extends Validator {
    @Override
    protected void validate(Controller controller) {
        validateRequired("title", "msg", "标题不能为空");
        validateRequired("content", "msg", "内容不能为空");
        validateString("title", true, 1, 30, "msg", "标题有点长啊");
        validateString("content", true, 1, 200, "msg", "内容有点长啊");
    }

    @Override
    protected void handleError(Controller controller) {
        controller.renderJson();
    }
}
