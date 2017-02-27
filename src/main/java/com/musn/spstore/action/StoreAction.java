/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.musn.spstore.action;

import java.io.File;
import java.util.Properties;

import com.lamfire.code.Base64;
import com.lamfire.code.MD5;
import com.lamfire.code.PUID;
import com.lamfire.jmongo.dao.DAO;
import com.lamfire.jmongo.dao.DAOFactory;
import com.lamfire.json.JSON;
import com.lamfire.logger.Logger;
import com.lamfire.utils.DateFormatUtils;
import com.lamfire.utils.FileUtils;
import com.lamfire.utils.PropertiesUtils;
import com.lamfire.utils.StringUtils;
import com.lamfire.warden.Action;
import com.lamfire.warden.ActionContext;
import com.lamfire.warden.anno.ACTION;
import com.musn.spstore.SPSBootstrap;
import com.musn.spstore.entity.FileMeta;

/**
 * @author zxc Jul 28, 2016 3:34:23 PM
 */
@ACTION(path = "/file/store")
public class StoreAction implements Action {

    private static final Logger LOGGER = Logger.getLogger(StoreAction.class);
    private static String       spstore_path;

    static {
        Properties pro = PropertiesUtils.load("spstore.properties", SPSBootstrap.class);
        spstore_path = pro.getProperty("spstore.path");
    }

    @Override
    public void execute(ActionContext context) {
        byte[] body = context.getRequestBody();
        if (body == null) {
            notfound(context, "");
            return;
        }
        JSON json = JSON.fromBytes(body);
        if (LOGGER.isDebugEnabled()) LOGGER.debug("[DATA] : " + json.toJSONString());

        String type = json.getString("type");
        String hash = json.getString("hash");
        int length = json.getInteger("length");
        String data = json.getString("data");

        // 检查参数
        if (StringUtils.isEmpty(data)) {
            error(context, "Bad request,data was empty");
            return;
        }
        if (!StringUtils.equalsIgnoreCase(type, "jpg") && !StringUtils.equalsIgnoreCase(type, "png")) {
            error(context, "Bad request,type['jpg','png'] not found");
            return;
        }
        byte[] bytes = Base64.decode(data);
        if (bytes == null) {
            error(context, "Bad request,Base64.decode error! bytes is null");
            return;
        }
        String bytesHash = MD5.hash(bytes);
        if (!StringUtils.equalsIgnoreCase(hash, bytesHash) || length != bytes.length) {
            error(context, "Bad request,data hash or length not matched");
            return;
        }

        String id = PUID.makeAsString();
        save(type, bytes, id);

        // 返回结果
        JSON result = new JSON();
        result.put("status", 200);
        result.put("id", id);
        result.put("timestamp", System.currentTimeMillis());
        context.writeResponse(result.toBytes("utf-8"));
    }

    private void save(String type, byte[] bytes, String id) {
        // 保存到磁盘 (备份)
        String path = spstore_path + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd") //
                      + "/" + id + "." + type;
        try {
            if (!FileUtils.existsParentDirs(path)) FileUtils.makeParentDirsIfNotExists(new File(path));
            FileUtils.writeByteArrayToFile(new File(path), bytes);
        } catch (Exception e) {
            LOGGER.error(path, e);
        }

        // 保存到数据库
        try {
            DAO<FileMeta, String> dao = DAOFactory.get("fileStore", "SPStore", FileMeta.class);
            dao.save(new FileMeta(id, type, bytes));
        } catch (Exception e) {
            LOGGER.error("SmallPicture save error!", e);
        }
    }

    private void notfound(ActionContext context, String msg) {
        JSON error = new JSON();
        error.put("status", 400);
        error.put("error", msg);
        context.setResponseStatus(400);
        context.writeResponse(error.toBytes("utf-8"));
    }

    private void error(ActionContext context, String msg) {
        JSON error = new JSON();
        error.put("status", 500);
        error.put("error", msg);
        context.writeResponse(error.toBytes("utf-8"));
    }
}
