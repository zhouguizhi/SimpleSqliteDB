package com.simple.util;
import android.util.Log;
import java.util.ArrayList;
import java.util.Map;
/**
 * Created by zhouguizhi on 2017/11/9.
 */
public class Condition {
    private static final String TAG ="Condition" ;
    private String whereClause;
    private String[] whereArgs;

    public Condition(Map<String,Object> map) {
        if(null!=map){
            ArrayList list = new ArrayList();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" 1=1 ");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value != null) {
                    stringBuilder.append(" and " + key + " =?");
                    Log.e(TAG,"value="+value);
                    list.add(value);
                }
            }
            this.whereClause = stringBuilder.toString();
            if(!list.isEmpty()){
                whereArgs = new String[list.size()];
             for(int i=0;i<list.size();i++){
                 whereArgs[i] = String.valueOf(list.get(i));
             }
            }
        }
    }
    public String[] getWhereArgs() {
          return whereArgs;
    }

   public String getWhereClause() {
       return whereClause;
    }
}
