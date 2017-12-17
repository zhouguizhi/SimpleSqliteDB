package test.com.simplesqlitedb;
import android.app.Application;
import com.simple.config.DBConfig;
import com.simple.dao.factory.DaoFactory;
import com.simple.util.FileUtils;
/**
 * Created by zhouguizhijxhz on 2017/11/8.
 */
public class MyApp extends Application {
    private static MyApp myApp;
    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        DBConfig.Builder builder=new DBConfig.Builder();
        builder.setSqliteDataBaseName("kuaisouzgz.db").setSqliteDataBasePath(FileUtils.createSqliteDataBasePath(this));
        DBConfig dbConfig = builder.build();
        DaoFactory.getInstance().setDbConfig(dbConfig);
    }
    public static MyApp getInstance(){
        return myApp;
    }
}
