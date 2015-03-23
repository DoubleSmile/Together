package com.whiteblue.model;

import com.jfinal.plugin.ehcache.CacheKit;
import com.whiteblue.tools.NewsUtil;

import java.util.List;

/**
 * Created by WhiteBlue on 15/2/28.
 */
public class News{
    private String title;
    private String href;
    private String content;

    public News(String title,String href,String content){
        this.title=title;
        this.href=href;
        if(content.length()>150){
            this.content=content.substring(0,150)+".....";
        }else{
            this.content=content;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getHref() {
        return href;
    }

    public String getContent() {
        return content;
    }



}
