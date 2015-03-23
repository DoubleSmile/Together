package com.whiteblue.config;

import com.jfinal.kit.PropKit;

/**
 * Created by WhiteBlue on 15/1/15.
 */
public class Cfg {
    public static int PAGE_SIZE = PropKit.use("config.txt").getInt("PAGE_SIZE");
    public static String RSS_URL = PropKit.use("config.txt").get("RSS");

}
