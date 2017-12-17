package com.simple.dao.base;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import com.simple.util.Condition;
import com.simple.util.IOUtils;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouguizhi on 2017/11/8.
 */
public   class BaseDao<T> extends BaseFieldDao<T> {
    private static final String TAG = "BaseDao";
    @Override
    public long insert(T t) {
        long result = -1;
        Map<String,Object> map =  objectToMap(t);
        ContentValues contentValues  = mapToContentValues(map);
        if(null!=sqLiteDatabase&&!TextUtils.isEmpty(tableName)){
            result = sqLiteDatabase.insert(tableName,null,contentValues);
        }
        return result;
    }
    @Override
    public int delete(T t) {
        int result = -1;
        Map<String,Object> map =  objectToMap(t);
        Condition condition = new Condition(map);
        if(null!=sqLiteDatabase&&!TextUtils.isEmpty(tableName)){
            result = sqLiteDatabase.delete(tableName,condition.getWhereClause(),condition.getWhereArgs());
        }
        Log.e(TAG,"result="+result);
        return result;
    }
    /**
     * 删除所有数据
     */
    @Override
    public int delete() {
        int result = -1;
        if(null!=sqLiteDatabase&&!TextUtils.isEmpty(tableName)){
            result = sqLiteDatabase.delete(tableName,null,null);
        }
        return result;
    }
    @Override
    public int update(T t1, T t2) {
        int result = -1;
        Map<String,Object> map = objectToMap(t1);
        Map<String,Object> whereMap = objectToMap(t2);
        Condition condition = new Condition(whereMap);
        ContentValues contentValues = mapToContentValues(map);
        if(null!=sqLiteDatabase&&!TextUtils.isEmpty(tableName)){
            result = sqLiteDatabase.update(tableName,contentValues,condition.getWhereClause(),condition.getWhereArgs());
        }
        Log.e(TAG,"result="+result);
        return result;
    }
    @Override
    public List<T> query(T t) {
        return query(t,null,null,null);
    }
    @Override
    public List<T> query(T t, String orderBy, Integer startIndex, Integer limit) {
        Map map = objectToMap(t);
        String limitStr = null;
        if(startIndex!=null&&limit!=null&&startIndex>=0&&limit>0){
            limitStr = startIndex+","+limit;
        }
        Condition condition = new Condition(map);
        Cursor cursor = null;
        if(null!=sqLiteDatabase&&!TextUtils.isEmpty(tableName)){
            cursor = sqLiteDatabase.query(tableName,null,condition.getWhereClause(),condition.getWhereArgs(),null,null,orderBy,limitStr);
        }
        List<T> list = getResult(cursor,t);
        IOUtils.close(cursor);
        return list;
    }
    public String getTableSql(){
        return getSql();
    }
}
