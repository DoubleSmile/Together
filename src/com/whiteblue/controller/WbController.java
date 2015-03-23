package com.whiteblue.controller;

import com.jfinal.core.Controller;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Created by WhiteBlue on 15/1/29.
 */
public class WbController extends Controller {

    //复写Controller中的方法过滤参数
    @Override
    public String getPara(String name) {
        return StringEscapeUtils.escapeHtml4(super.getPara(name));
    }

    @Override
    public String getPara(String name, String defaultValue) {
        return StringEscapeUtils.escapeHtml4(super.getPara(name, defaultValue));
    }
}
