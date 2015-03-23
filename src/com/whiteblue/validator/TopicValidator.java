package com.whiteblue.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.whiteblue.model.Groups;

/**
 * Created by WhiteBlue on 15/2/22.
 */
public class TopicValidator extends Validator {
    @Override
    protected void validate(Controller controller) {
        validateString("title", true, 1, 30, "msg", "标题非法");
        validateString("content", true, 1, 200, "msg", "内容非法");
    }

    @Override
    protected void handleError(Controller controller) {
        int groupID = controller.getParaToInt("groupID");
        Groups group = Groups.dao.getById(groupID);
        if (group.get("mode").equals("user")) {
            controller.redirect("/user-groups/listTopic/" + groupID);
        } else if (group.get("mode").equals("public")) {
            controller.redirect("/public/listTopic/" + groupID);
        } else {
            controller.redirect("/groups");
        }
    }
}
