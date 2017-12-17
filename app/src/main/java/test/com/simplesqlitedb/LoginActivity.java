package test.com.simplesqlitedb;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.simple.dao.factory.DaoFactory;
import java.util.Date;
import test.com.simplesqlitedb.bean.Photo;
import test.com.simplesqlitedb.bean.User;
import test.com.simplesqlitedb.config.DBVersionConfig;
import test.com.simplesqlitedb.dao.PhotoDao;
import test.com.simplesqlitedb.dao.UserDao;
import test.com.simplesqlitedb.manager.MultithUserLoginManager;
import test.com.simplesqlitedb.manager.UserPrivatePhoto;
/**
 * Created by zhouguizhijxhz on 2017/12/17.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    /**
     * 这个登录时去请求后台,然后后台返回你的下面什么name  userid 在这直接写死了
     * @param view
     */
    public void login(View view){
        UserDao userDao =  DaoFactory.getInstance().getBaseDao(UserDao.class,User.class);
        User user=new User();
        user.setPassword("1111");
        user.setName("景甜");
        user.setUser_id("120");
        userDao.insert(user);
        MultithUserLoginManager.getInstance(this).checkTable(DBVersionConfig.DBVERSION);
    }
    public void insert(View view)
    {
        Photo photo=new Photo();
        photo.setPath("data/data/my.jpg");
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        photo.setTime(dateFormat.format(new Date()));
        PhotoDao photoDao=DaoFactory.getInstance().getUserDao(UserPrivatePhoto.database.getValue(),PhotoDao.class,Photo.class);
        photoDao.insert(photo);
        Log.e(TAG,"SQL="+photoDao.getSql());
    }
    public void app_update(View view){
    }
}
