package com.simple.util;
import android.text.TextUtils;
/**
 * Created by zhouguizhijxhz on 2017/12/17.
 */
public class StringUtil {
    public static boolean string(String str){
        char[] chars=new char[1];
        chars[0]=str.charAt(0);
        String temp=new String(chars);
        if(chars[0]>='A'  &&  chars[0]<='Z')
        {
            return true;
        }
        return false;
    }
}
