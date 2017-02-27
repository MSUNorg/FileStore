/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.mob.test;

import java.io.File;

import com.lamfire.code.Base64;
import com.lamfire.code.MD5;
import com.lamfire.json.JSON;
import com.lamfire.utils.FileUtils;
import com.lamfire.utils.HttpClient;

/**
 * @author zxc Jul 28, 2016 5:34:23 PM
 */
public class PStoreTest {

    public static void main(String[] args) throws Exception {
        byte[] bytes = FileUtils.readFileToByteArray(new File("D:\\data\\2013.jpg"));

        JSON json = new JSON();
        json.put("type", "jpg");
        json.put("length", bytes.length);
        json.put("hash", MD5.hash(bytes));
        json.put("data", Base64.encode(bytes));

        HttpClient client = new HttpClient();
        client.setMethod("POST");
        client.open("http://127.0.0.1:8080/store");
        client.post(json.toBytes("utf-8"));

        String response = client.readAsString();
        System.out.println(response);
    }
}
