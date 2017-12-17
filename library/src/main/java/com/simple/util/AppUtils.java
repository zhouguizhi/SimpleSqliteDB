package com.simple.util;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
/**
 * Created by zhouguizhi on 2017/11/10.
 */
public class AppUtils {
    public static String getAppName(Context context){
        String appName = "";
        if(null==context){
            return appName;
        }
        PackageManager pm = context.getPackageManager();
        appName = context.getApplicationInfo().loadLabel(pm).toString();
        return appName;
    }
    public static String getVersionName(Context context){
        String versionName = "";
        if(null==context){
            return versionName;
        }
        try
        {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;

        } catch (PackageManager.NameNotFoundException e)
        {
            versionName = "";
        }
        return versionName;
    }
}
