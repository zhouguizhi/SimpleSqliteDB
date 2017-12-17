package com.simple.dao.factory;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.simple.config.DBConfig;
import com.simple.dao.base.BaseDao;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by zhouguizhi on 2017/11/8.
 */
public class DaoFactory {
    private DBConfig dbConfig;
    //一个user对应一个...Dao，内存优化每次都要去初始化bean实体的数据信息
    private Map<String,BaseDao> cacheDaoMap= Collections.synchronizedMap(new HashMap<String, BaseDao>());
    private DaoFactory() {

    }
    private static  class  LazyHolder{
        private static DaoFactory INSTANCE = new DaoFactory();
    }
    public static DaoFactory getInstance(){
        return LazyHolder.INSTANCE;
    }
    public void setDbConfig(DBConfig dbConfig) {
        this.dbConfig = dbConfig;
        if(this.dbConfig ==null){
            throw new UnsupportedOperationException("没有对数据库进行配置");
        }
    }
    public synchronized <T extends BaseDao<M>,M> T getBaseDao(Class<T> clazz, Class<M> entityClazz){
        if(null!=cacheDaoMap&&cacheDaoMap.get(clazz.getSimpleName())!=null)
        {
            return (T) cacheDaoMap.get(clazz.getSimpleName());
        }
        if(dbConfig ==null){
            throw new UnsupportedOperationException("没有对数据库进行配置");
        }
        if(clazz==null||entityClazz==null){
            return null;
        }
        BaseDao baseDao = null;
        try {
            baseDao = clazz.newInstance();
            baseDao.init(entityClazz, dbConfig.getSqLiteDatabase());
            cacheDaoMap.put(clazz.getSimpleName(),baseDao);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T)baseDao;
    }
    public   synchronized  <T extends  BaseDao<M>,M> T
    getUserDao(String path,Class<T> clazz,Class<M> entityClass)
    {
        Log.e("getUserDao","path="+path);
        SQLiteDatabase userDatabase= SQLiteDatabase.openOrCreateDatabase(path,null);
        BaseDao baseDao=null;
        try {
            baseDao=clazz.newInstance();
            baseDao.init(entityClass,userDatabase);
            cacheDaoMap.put(clazz.getSimpleName(),baseDao);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }
    public DBConfig getDbConfig() {
        return dbConfig;
    }
}
