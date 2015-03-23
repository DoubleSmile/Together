package com.whiteblue.beetl;

import com.jfinal.kit.PathKit;
import com.jfinal.render.IMainRenderFactory;
import com.jfinal.render.Render;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.WebAppResourceLoader;

import java.io.IOException;

/**
 * Created by WhiteBlue on 15/1/21.
 */
public class BeetlRenderFactory implements IMainRenderFactory {

    public static String viewExtension = ".html";
    public static GroupTemplate groupTemplate = null;

    static {
        try {
            Configuration cfg = Configuration.defaultConfiguration();

            WebAppResourceLoader resourceLoader = new WebAppResourceLoader();

            resourceLoader.setRoot(PathKit.getWebRootPath() + "/WEB-INF/beetl/");

            groupTemplate = new GroupTemplate(resourceLoader, cfg);

            //设定格式化时间方法
            groupTemplate.registerFunction("printTime", new PrintTimeFunction());

        } catch (IOException e) {
            throw new RuntimeException("加载GroupTemplate失败", e);
        }
    }

    public Render getRender(String view) {
        return new BeetlRender(groupTemplate, view);
    }

    public String getViewExtension() {
        return viewExtension;
    }

}