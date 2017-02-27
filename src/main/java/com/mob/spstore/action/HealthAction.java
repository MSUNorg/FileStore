/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.mob.spstore.action;

import com.lamfire.json.JSON;
import com.lamfire.utils.DateFormatUtils;
import com.lamfire.warden.Action;
import com.lamfire.warden.ActionContext;
import com.lamfire.warden.anno.ACTION;

/**
 * @author zxc Jul 28, 2016 5:34:23 PM
 */
@ACTION(path = "/health")
public class HealthAction implements Action {

    @Override
    public void execute(ActionContext context) {
        JSON json = new JSON();
        json.put("status", 200);
        json.put("time", DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss:SSS"));
        context.writeResponse(json.toJSONString());
    }
}
