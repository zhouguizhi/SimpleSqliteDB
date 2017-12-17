package com.simple.dao.base;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import com.simple.annotations.DBField;
import com.simple.annotations.DBNotField;
import com.simple.annotations.DBNotNullAndUnique;
import com.simple.annotations.DBNotNullField;
import com.simple.annotations.DBPrivateKeyField;
import com.simple.annotations.DBTable;
import com.simple.config.DBType;
import com.simple.dao.interfaces.IBaseDao;
import com.simple.util.IOUtils;
import com.simple.util.StringUtil;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouguizhi on 2017/11/8.
 */
public abstract class BaseFieldDao<T> implements IBaseDao<T> {
    private static final String TAG = "BaseFieldDao";
    SQLiteDatabase sqLiteDatabase;
    private LinkedHashMap cacheFieldMap;
    private List<Field> cacheFieldList;
    private static final  String CREATE_TABLE_PRE ="create table if not exists ";
    private static final  String PRIMARY_KEY = "primary key autoincrement";
    private static final  String NOT_NULL = "not null";
    private static final  String NOT_NULL_AND_UNIQUE = "not null unique";
    //create table if not exists tb_person(age Integer ,company Text ,depmarket Text not null ,id Long primary key ,isVip Boolean ,name Text ,num Integer not null unique ,salary REAL )
    String tableName;
    private String tableSql;
    private Class<T> entity;
    public void init(Class<T> entityClazz, SQLiteDatabase sqLiteDatabase) {
        if (null != entityClazz && null != sqLiteDatabase) {
            this.entity = entityClazz;
            this.sqLiteDatabase = sqLiteDatabase;
            getTableName(entity);
            if (!sqLiteDatabase.isOpen()) {
                return;
            }
            if (!TextUtils.isEmpty(tableName)) {
                cacheFieldList = new ArrayList<>();
                //动态创建表
                tableSql =  dynamicsCreateTable(entityClazz);
                if(!TextUtils.isEmpty(tableSql)) sqLiteDatabase.execSQL(tableSql);
                initCacheMap();
            }
        }
    }
    /**
     * 获取表名
     * @param entityClazz
     */
    private void getTableName(Class<T> entityClazz) {
        if(null==entityClazz){
            return;
        }
        if (null == entityClazz.getAnnotation(DBTable.class) || TextUtils.isEmpty(entityClazz.getAnnotation(DBTable.class).value())) {
            tableName = entityClazz.getSimpleName().toLowerCase();
        } else {
            tableName = entityClazz.getAnnotation(DBTable.class).value();
        }
    }

    /**
     * 动态建表
     * @param entityClazz
     */
    private String dynamicsCreateTable(Class<T> entityClazz) {
        if(null==entityClazz){
            return "";
        }
        Field[] fields = entityClazz.getDeclaredFields();
        fields = createFields(fields);
        for(Field field:fields){
            if(field.getAnnotation(DBField.class)!=null){
                cacheFieldList.add(field);
            }else if(field.getAnnotation(DBNotNullField.class)!=null){
                cacheFieldList.add(field);
            }else if(field.getAnnotation(DBPrivateKeyField.class)!=null){
                cacheFieldList.add(field);
            }else if(field.getAnnotation(DBNotNullAndUnique.class)!=null){
                cacheFieldList.add(field);
            }else if(field.getAnnotation(DBNotField.class)!=null){

            }else{//表示没字段中没添加注解
                cacheFieldList.add(field);
            }
        }
        StringBuilder tableStr = new StringBuilder(CREATE_TABLE_PRE);
        if(null!=cacheFieldList&&!cacheFieldList.isEmpty()){
            tableStr.append(tableName).append("(");
            for(int i=0;i<cacheFieldList.size();i++){
                Field field = cacheFieldList.get(i);
                if(field.getAnnotation(DBField.class)!=null){
                    tableStr.append(field.getName()).append(" ").append(getTypeByField(field.getType())).append(" ,");
                }else if(field.getAnnotation(DBPrivateKeyField.class)!=null){
                    tableStr.append(field.getName()).append(" ").append(getTypeByField(field.getType())).append(" ").append(PRIMARY_KEY).append(" ,");
                }else if(field.getAnnotation(DBNotNullField.class)!=null){
                    tableStr.append(field.getName()).append(" ").append(getTypeByField(field.getType())).append(" ").append(NOT_NULL).append(" ,");
                }else if(field.getAnnotation(DBNotNullAndUnique.class)!=null){
                    tableStr.append(field.getName()).append(" ").append(getTypeByField(field.getType())).append(" ").append(NOT_NULL_AND_UNIQUE).append(" ,");
                }else{
                    tableStr.append(field.getName()).append(" ").append(getTypeByField(field.getType())).append(" ,");                }
                //删除最后一个 " ,"
            }
            tableStr = tableStr.deleteCharAt(tableStr.length()-1);
            tableStr.append(")");
            Log.e(TAG,"TABLE="+tableStr.toString());
        }
        return tableStr.toString();
    }
    private static String getTypeByField(Class fieldType){
        String type = "";
        if(null==fieldType){
            return type;
        }
        if(fieldType==int.class||fieldType==Integer.class){
            return DBType.INTEGER.getType();
        }else if(fieldType==double.class||fieldType==Double.class){
            return DBType.REAL.getType();
        }else if(fieldType==float.class||fieldType==Float.class){
            return DBType.REAL.getType();
        }else if(fieldType==short.class||fieldType==Short.class){
            return DBType.INTEGER.getType();
        }else if(fieldType==byte[].class){
            return DBType.BLOB.getType();
        }else if(fieldType==long.class||fieldType==Long.class){
            return DBType.LONG.getType();
        }else if(fieldType==boolean.class||fieldType==Boolean.class){
            return DBType.Boolean.getType();
        }else if(fieldType==String.class){
            return DBType.Text.getType();
        }
        return type;
    }
    private void initCacheMap() {
        if(TextUtils.isEmpty(tableName)||null==sqLiteDatabase||null==cacheFieldList||cacheFieldList.isEmpty()){
            return;
        }
        //查询空表 为了获取表的列名
        String sql = "select * from " + tableName + " limit 1,0";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        try {
            if (null != cursor) {
                //获取表的列名
                String[] columnNames = cursor.getColumnNames();
                if (null != entity) {
                    cacheFieldMap = new LinkedHashMap();
                    Field[] fields = cacheFieldList.toArray(new Field[cacheFieldList.size()]);
                    Map<Field, FieldMethod> fieldMethodMap = createMethod(entity, fields);
                    if (null != fields && fields.length > 0) {
                        for (String columnName : columnNames) {
                            String fieldName;
                            Field columnField = null;
                            for (Field field : fields) {
                                if (null!=field&&field.getAnnotation(DBField.class) != null && !TextUtils.isEmpty(field.getAnnotation(DBField.class).value())) {
                                    fieldName = field.getAnnotation(DBField.class).value();
                                }else if (null!=field&&field.getAnnotation(DBNotNullField.class) != null && !TextUtils.isEmpty(field.getAnnotation(DBNotNullField.class).value())) {
                                    fieldName = field.getAnnotation(DBNotNullField.class).value();
                                }else if(null!=field&&field.getAnnotation(DBNotNullAndUnique.class) != null && !TextUtils.isEmpty(field.getAnnotation(DBNotNullAndUnique.class).value())){
                                    fieldName = field.getAnnotation(DBNotNullAndUnique.class).value();
                                }else {
                                    fieldName = field.getName();
                                }
                                if (fieldName.equals(columnName)) {
                                    columnField = field;
                                    break;
                                }
                            }
                            if (null != columnField) {
                                FieldMethod fieldMethod = fieldMethodMap.get(columnField);
                                if(null!=cacheFieldMap){
                                    cacheFieldMap.put(columnName, fieldMethod);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } finally {
            IOUtils.close(cursor);
        }
    }

    private Map<Field, FieldMethod> createMethod(Class<T> entity, Field[] fields) {
        if (null == entity || null == fields || fields.length == 0) {
            return null;
        }
        Map<Field, FieldMethod> map = new HashMap<>();
        for (Field field : fields) {
            String fieldName = field.getName();
            String methodName;
            if(fieldName.startsWith("_")){
                methodName="get"+fieldName;
            }else{
                methodName="get"+((char)(fieldName.charAt(0)-32))+fieldName.substring(1);
            }
            Method method = null;
            try {
                method = entity.getMethod(methodName);
            } catch (NoSuchMethodException e) {
                if(field.getType()==Boolean.class||field.getType()==boolean.class){
                    if(fieldName.startsWith("is")){
                        String pro = fieldName.substring(2);
                            if(!TextUtils.isEmpty(pro)){
                                if(StringUtil.string(pro)){
                                    methodName="get"+pro;
                                }
                            }
                    }else{
                        if(fieldName.startsWith("_")){
                            methodName="is"+fieldName;
                        }else{
                            methodName="is"+((char)(fieldName.charAt(0)-32))+fieldName.substring(1);
                        }
                    }
                }
                try {
                    Log.e(TAG,"methodName="+methodName);
                    method = entity.getMethod(methodName);
                } catch (Exception e1) {
                }
            }
            FieldMethod fieldMethod = new FieldMethod();
            fieldMethod.field = field;
            fieldMethod.field.setAccessible(true);
            fieldMethod.method = method;
            map.put(field, fieldMethod);
        }
        return map;
    }

    private Field[] createFields(Field[] fields) {
        if (null == fields || fields.length == 0) {
            return null;
        }
        List<Field> fieldList = new ArrayList<>();
        for (Field field : fields) {
            if (!field.isSynthetic()) {
                fieldList.add(field);
            }
        }
        for (Field field : fieldList) {
            if ("serialVersionUID".equals(field.getName())) {
                fieldList.remove(field);
            }
        }
        return fieldList.toArray(new Field[fieldList.size()]);
    }

    Map<String, Object> objectToMap(T entity) {
        Map<String, Object> map = new HashMap<>();
        Log.e(TAG,"cacheFieldMap="+cacheFieldMap);
        if (null != cacheFieldMap && !cacheFieldMap.isEmpty()) {
            Iterator<FieldMethod> fieldIterator = cacheFieldMap.values().iterator();
            while(fieldIterator.hasNext()) {
                FieldMethod fieldMethod = fieldIterator.next();
                String key;
                Object value = null;
                Field field = fieldMethod.field;
                if (null != field) {
                    if (field.getAnnotation(DBField.class) == null) {
                        key = field.getName();
                    } else {
                        key = field.getAnnotation(DBField.class).value();
                    }
                    Method method = fieldMethod.method;
                    try {
                        if (null != method) {
                            value = method.invoke(entity);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    if(null!=value){
                        map.put(key, value);
                    }
                }
            }
        }
        return map;
    }

    ContentValues mapToContentValues(Map<String, Object> map) {
        if (null != map && !map.isEmpty()) {
            ContentValues contentValues = new ContentValues();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                Log.e(TAG,"value="+value);
                if (value!=null) {
                    if(value instanceof Boolean){
                        Boolean b = (Boolean) value;
                        Log.e(TAG,"b="+b);
                        contentValues.put(entry.getKey(), b?"0":"1");
                    }else{
                        contentValues.put(key,String.valueOf(value));
                    }
                }
            }
            return contentValues;
        }
        return null;
    }

    ArrayList getResult(Cursor cursor, T where) {
        if(null==cursor||null==where||null==cacheFieldMap||cacheFieldMap.isEmpty()) return null;
        ArrayList list = new ArrayList();
        Object obj;
        while (cursor.moveToNext()) {
            try {
                obj = where.getClass().newInstance();
                Iterator iterator = cacheFieldMap.entrySet().iterator();
                while(iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    String columnName = (String) entry.getKey();
                    Integer columnIndex = cursor.getColumnIndex(columnName);
                    FieldMethod FieldMethod = (FieldMethod) entry.getValue();
                    if(null!=FieldMethod){
                        Field field = FieldMethod.field;
                        if(null!=field){
                            //获取字段类型
                            Class type = field.getType();
                            if (columnIndex != -1) {
                                if (type == String.class) {
                                    field.set(obj, cursor.getString(columnIndex));
                                } else if (type == Double.class) {
                                    field.set(obj, cursor.getDouble(columnIndex));
                                } else if (type == Integer.class) {
                                    field.set(obj, cursor.getInt(columnIndex));
                                } else if (type == Long.class) {
                                    field.set(obj, cursor.getLong(columnIndex));
                                } else if (type == byte[].class) {
                                    field.set(obj, cursor.getBlob(columnIndex));
                                }else if(type==Short.class){
                                    field.set(obj, cursor.getShort(columnIndex));
                                }else if(type==Boolean.class){//在sqlite没有Boolean类型 要通过int转换
                                   Log.e(TAG,"cursor.getInt(columnIndex)==0="+cursor.getInt(columnIndex));
                                    field.set(obj, cursor.getInt(columnIndex)==0);
                                }
                                else {
                                    continue;
                                }
                        }
                      }
                    }
                }
                list.add(obj);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    private class FieldMethod {
        Field field;
        Method method;
    }
    public String getSql(){
        return tableSql;
    }
}
