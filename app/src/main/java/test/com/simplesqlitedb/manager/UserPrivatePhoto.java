package test.com.simplesqlitedb.manager;
import android.util.Log;
import com.simple.dao.factory.DaoFactory;
import com.simple.util.FileUtils;
import java.io.File;
import test.com.simplesqlitedb.MyApp;
import test.com.simplesqlitedb.bean.User;
import test.com.simplesqlitedb.dao.UserDao;
/**
 * Created by zhouguizhijxhz on 2017/12/17.
 */
public enum  UserPrivatePhoto {
    /**
     * 存放本地数据库的路径
     */
    database("local/data/database/");
    private static final String TAG ="UserPrivatePhoto";
    /**
     * 文件存储的文件路径
     */
    private String value;
    UserPrivatePhoto(String value)
    {
        this.value = value;
    }
    public String getValue()
    {
        UserDao userDao= DaoFactory.getInstance().getBaseDao(UserDao.class,User.class);
        if(userDao!=null)
        {
            User currentUser=userDao.getCurrentUser();
            String path = FileUtils.getUserDataBasePath(MyApp.getInstance());
            File file = new File(path);
            if(currentUser!=null)
            {
                if(!file.exists())
                {
                    file.mkdirs();
                }
                File currentUserFile = new File(file,currentUser.getUser_id());
                if(!currentUserFile.exists())
                {
                    currentUserFile.mkdirs();
                }
                return currentUserFile+File.separator+"photo.db";
            }
        }
        return value;
    }
}
