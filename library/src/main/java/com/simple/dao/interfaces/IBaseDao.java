package com.simple.dao.interfaces;
import java.util.List;
/**
 * Created by zhouguizhi on 2017/11/8.
 */
public interface IBaseDao<T> {
    long insert(T t);
    int delete(T t);
    int delete();
    int update(T t1,T t2);
    List<T> query(T t);
    List<T> query(T t,String orderBy,Integer startIndex,Integer limit);
}
