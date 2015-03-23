package com.whiteblue.beetl;

import com.jfinal.render.Render;
import com.jfinal.render.RenderException;
import org.beetl.core.GroupTemplate;
import org.beetl.core.exception.BeetlException;
import org.beetl.ext.web.WebRender;

/**
 * Created by WhiteBlue on 15/1/21.
 */
public class BeetlRender extends Render {
    private transient static final String encoding = getEncoding();
    private transient static final String contentType = "text/html; charset=" + encoding;
    GroupTemplate gt = null;

    public BeetlRender(GroupTemplate gt, String view) {
        this.gt = gt;
        this.view = view;
    }

    @Override
    public void render() {

        try

        {
            response.setContentType(contentType);
            WebRender webRender = new WebRender(gt);
            webRender.render(view, request, response);

        } catch (BeetlException e) {
            throw new RenderException(e);
        }

    }

}