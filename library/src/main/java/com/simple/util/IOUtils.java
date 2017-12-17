package com.simple.util;
import java.io.Closeable;
import java.io.IOException;
/**
 * Created by zhouguizhi on 2017/11/8.
 */
public class IOUtils {
    public static <T extends Closeable> void close(T t){
        if(null!=t){
            try {
                t.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
