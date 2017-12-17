package com.simple.config;
/**
 * Created by zhouguizhi on 2017/11/21.
 */
public enum  DBType {
    LONG("Long"),INTEGER("Integer"),REAL("REAL"),BLOB("BLOB"),Boolean("Boolean"),Text("Text");
    private String type;
    DBType(String type){
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
