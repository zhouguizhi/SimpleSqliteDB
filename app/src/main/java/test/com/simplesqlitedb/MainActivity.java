package test.com.simplesqlitedb;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.simple.dao.factory.DaoFactory;
import com.simple.util.SortUtils;
import java.util.List;
import test.com.simplesqlitedb.bean.User;
import test.com.simplesqlitedb.dao.UserDao;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="MainActivity" ;
    private  UserDao userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDao =  DaoFactory.getInstance().getBaseDao(UserDao.class,User.class);
        String sql =  userDao.getSql();
        Log.e(TAG,"sql="+sql);
    }
    public void insert_user(View view){
        if(null!=userDao){
            for(int i=0;i<50;i++){
                User user = new User();
                user.setUser_id(String.valueOf(System.currentTimeMillis()));
                user.setPassword(String.valueOf(i));
                user.setName("zgz--"+i);
                user.setStatus(0);
                user.setVip(i % 2 == 0);
                user.setSarary(10000d);
                user.setDate(System.currentTimeMillis());
                userDao.insert(user);
            }
        }
    }
    public void delete(View view){
        if(null!=userDao){
            User user = new User();
            user.setVip(false);
            userDao.delete(user);
        }
    }
    public void deleteAll(View view){
        if(null!=userDao){
            userDao.delete();
        }
    }
    public void update(View view){
        if(null!=userDao){
            User where = new User();
            where.setName("zgz");

            User user = new User();
            user.setName("zhoudashen");
            userDao.update(user,where);
        }
    }
    public void query(View view){
        if(null!=userDao){
            User user = new User();
            List<User> list = userDao.query(user);
            Log.e(TAG,"查询到的个数size="+list.size());
            for(User u:list){
                Log.e(TAG,"user="+u);
            }
        }
    }
    public void no_condition_query(View view){
        if(null!=userDao){
            User user = new User();
            List<User> list = userDao.query(user);
            Log.e(TAG,"查询到的个数size="+list.size());
            for(User u:list){
                Log.e(TAG,"user="+u);
            }
        }
    }
    public void limit_query(View view){
        if(null!=userDao){
            User user = new User();
            List<User> list = userDao.query(user,null,0,10);
            for(User u:list){
                Log.e(TAG,"u="+u);
            }
        }
    }
    public void order_query(View view){
        if(null!=userDao){
            User user = new User();
            List<User> list = userDao.query(user,"date "+ SortUtils.DESC.getName(),0,10);
            for(User u:list){
                Log.e(TAG,"u="+u);
            }
        }
    }
    /**
     * 多用户登录
     */
    public void login(View view){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}
