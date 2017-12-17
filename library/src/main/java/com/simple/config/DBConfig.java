package com.simple.config;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
/**
 * Created by zhouguizhi on 2017/11/8.
 */
public class DBConfig {
    //数据库路径
    private String sqliteDataBasePath;
    //数据库名称
    private String sqliteDataBaseName;
    private SQLiteDatabase sqLiteDatabase;
    private DBConfig(Builder builder){
        this.sqliteDataBaseName = builder.sqliteDataBaseName;
        this.sqliteDataBasePath = builder.sqliteDataBasePath;
        openSqliteDataBase();
    }

    private void openSqliteDataBase() {
        this.sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sqliteDataBasePath+ File.separator+sqliteDataBaseName,null);
    }

    public static class Builder {
        private  String sqliteDataBasePath;
        private  String sqliteDataBaseName;
        public Builder setSqliteDataBasePath(String sqliteDataBasePath) {
            this.sqliteDataBasePath = sqliteDataBasePath;
            Log.e("DBConfig","sqliteDataBasePath="+sqliteDataBasePath);
            return this;
        }
        public Builder setSqliteDataBaseName(String sqliteDataBaseName) {
            this.sqliteDataBaseName = sqliteDataBaseName;
            return this;
        }
        public DBConfig build(){
            return new DBConfig(this);
        }
    }
    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    public String getSqliteDataBasePath() {
        return sqliteDataBasePath;
    }

    public String getSqliteDataBaseName() {
        return sqliteDataBaseName;
    }
}
