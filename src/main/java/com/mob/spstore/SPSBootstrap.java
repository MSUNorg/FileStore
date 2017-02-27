/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.mob.spstore;

import java.util.Properties;

import com.lamfire.logger.Logger;
import com.lamfire.utils.PropertiesUtils;
import com.lamfire.warden.HttpServer;

/**
 * @author zxc Jul 28, 2016 2:31:23 PM
 */
public class SPSBootstrap {

    private static final Logger LOGGER = Logger.getLogger(SPSBootstrap.class);

    private static String       host;
    private static int          port;
    private static String       action_root;
    private static int          threads;

    static {
        Properties pro = PropertiesUtils.load("netty-server.properties", SPSBootstrap.class);
        action_root = pro.getProperty("server.action.root");
        host = pro.getProperty("server.hostname");
        port = Integer.parseInt(pro.getProperty("server.port"));
        threads = Integer.parseInt(pro.getProperty("server.worker.threads"));
    }

    public static void main(String[] args) throws Exception {
        HttpServer server = new HttpServer(host, port);
        server.registerAll(action_root);
        server.setWorkerThreads(threads);
        server.startup();
        LOGGER.info("SPSBootstrap started! on host_name:" + host + " server_port:" + port);
    }
}
