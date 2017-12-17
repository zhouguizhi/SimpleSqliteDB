package com.simple.util;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by zhouguizhi 2017/11/8.
 */
public class FileUtils {
    private static final String fileName = "photo";
    private static final String dbVersion = "update.txt";
    private static boolean exitSDCard(){
        return Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
    }
    /**
     * @return 获取sd卡的根路径
     */
    private static String getSDCardRootPath(){
        String path = "";
        if(exitSDCard()){
            path = Environment.getExternalStorageDirectory().getAbsolutePath();//获取跟目录
        }
        return path;
    }
    private static String getAppName(Context context){
        String appName = "simple";//给一个默认值
        if(null==context){
            return appName;
        }
        PackageManager pm = context.getPackageManager();
        appName = context.getApplicationInfo().loadLabel(pm).toString();
        return appName;
    }
    public static String createUserDataBasePath(Context context){
        String path = createSqliteDataBasePath(context);
        if(TextUtils.isEmpty(fileName)){
            return path;
        }
        File file = new File(path,fileName);
        if(!file.exists()){
            file.mkdir();
        }
        return file.getAbsolutePath();
    }
    public static String createDBVserionPath(Context context){
        String path = createSqliteDataBasePath(context);
        if(TextUtils.isEmpty(dbVersion)){
            return path;
        }
        File file = new File(path,dbVersion);
        if(!file.exists()){
            file.mkdir();
        }
        return file.getAbsolutePath();
    }
    public static String getDBVersionPath(Context context){
        return createDBVserionPath(context);
    }
    public static String getUserDataBasePath(Context context){
        return createUserDataBasePath(context);
    }
    /**
     * 这是数据库的路径
     * @param context
     * @return
     */
    public static String createSqliteDataBasePath(Context context){
        File file;
        if(context==null){
            return "";
        }
        if(exitSDCard()){
            file = new File(getSDCardRootPath(),getAppName(context));
        }else{
            String defaultSqlitePath = context.getFilesDir().getAbsolutePath()+ File.separator+"databases";
            file = new File(defaultSqlitePath,getAppName(context));
        }
        //创建子目录
        if(!file.exists()){
            file.mkdir();
        }
        return file.getAbsolutePath();
    }
    /**
     * 复制单个文件(可更名复制)
     */
    public static void CopyFile(String oldPathFile, String newPathFile) {
        InputStream inStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPathFile);
            File newFile=new File(newPathFile);
            File parentFile=newFile.getParentFile();
            if(!parentFile.exists())
            {
                parentFile.mkdirs();
            }
            if (oldfile.exists()) { //文件存在时
                inStream = new FileInputStream(oldPathFile); //读入原文件
                fileOutputStream = new FileOutputStream(newPathFile);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    fileOutputStream.write(buffer, 0, byteread);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            IOUtils.close(inStream);
            IOUtils.close(fileOutputStream);
        }
    }
}
