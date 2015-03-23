package com.whiteblue.model;

import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by WhiteBlue on 15/2/11.
 */
public class Model<M extends com.jfinal.plugin.activerecord.Model> extends com.jfinal.plugin.activerecord.Model<M> {
    private String cacheNameForModel;

    public Model(String cacheNameForModel) {
        this.cacheNameForModel = cacheNameForModel;
    }

    public Page<M> loadModelPage(Page<M> page) {
        List<M> modelList = page.getList();
        for (int i = 0; i < modelList.size(); i++) {
            com.jfinal.plugin.activerecord.Model model = modelList.get(i);
            M topic = loadModel(model.getInt("id"));
            modelList.set(i, topic);
        }
        return page;
    }

    public List<M> loadModelList(List<M> list) {
        for (int i = 0; i < list.size(); i++) {
            com.jfinal.plugin.activerecord.Model model = list.get(i);
            M topic = loadModel(model.getInt("id"));
            list.set(i, topic);
        }
        return list;
    }


    public M loadModel(int id) {
        final int ID = id;
        return (M) CacheKit.get(cacheNameForModel, ID, new IDataLoader() {
            @Override
            public Object load() {
                return findById(ID);
            }
        });
    }


}
