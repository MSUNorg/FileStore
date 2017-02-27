/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.mob.spstore.entity;

import com.lamfire.jmongo.annotations.Entity;
import com.lamfire.jmongo.annotations.Id;

/**
 * @author zxc Jul 28, 2016 2:32:23 PM
 */
@Entity
public class FileMeta {

    @Id
    private String id;

    private String type;

    private byte[] data;

    public FileMeta() {

    }

    public FileMeta(String id, String type, byte[] data) {
        this.id = id;
        this.type = type;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
