/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.mob.spstore.action;

import com.lamfire.jmongo.dao.DAO;
import com.lamfire.jmongo.dao.DAOFactory;
import com.lamfire.logger.Logger;
import com.lamfire.utils.StringUtils;
import com.lamfire.warden.Action;
import com.lamfire.warden.ActionContext;
import com.lamfire.warden.anno.ACTION;
import com.mob.spstore.entity.FileMeta;

/**
 * @author zxc Jul 28, 2016 2:34:23 PM
 */
@ACTION(path = "/file/view")
public class ViewAction implements Action {

    private static final Logger LOGGER = Logger.getLogger(ViewAction.class);

    @Override
    public void execute(ActionContext context) {
        String id = context.getHttpRequestParameters().getString("id");
        if (StringUtils.isEmpty(id)) return;
        if (id.length() != 24) return;

        if (LOGGER.isDebugEnabled()) LOGGER.debug("[id] : " + id);

        DAO<FileMeta, String> dao = DAOFactory.get("finance", "SPStore", FileMeta.class);
        FileMeta sp = dao.get(id);
        if (sp == null) return;
        // image/jpeg
        if (StringUtils.equalsIgnoreCase("jpg", sp.getType())) {
            context.setResponseContentType("image/jpeg");
        }
        // image/png
        else if (StringUtils.equalsIgnoreCase("png", sp.getType())) {
            context.setResponseContentType("image/png");
        }
        // other
        else {
            context.setResponseContentType("image/png");
        }
        context.writeResponse(sp.getData());
    }
}
