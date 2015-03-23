package com.whiteblue.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.whiteblue.model.Groups;

/**
 * Created by WhiteBlue on 15/3/7.
 */
public class GroupsValidator extends Validator {
    @Override
    protected void validate(Controller controller) {
        if (Groups.dao.hasGroup(controller.getPara("name"))) {
            addError("msg", "组已存在");
        } else {
            validateString("name", true, 1, 20, "msg", "名称输入非法");
            validateString("content", true, 0, 100, "msg", "内容非法");
        }
    }

    @Override
    protected void handleError(Controller controller) {
        controller.redirect("/");
    }
}
