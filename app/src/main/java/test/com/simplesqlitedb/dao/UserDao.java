package test.com.simplesqlitedb.dao;
import android.util.Log;
import com.simple.dao.base.BaseDao;
import java.util.List;
import test.com.simplesqlitedb.bean.User;
/**
 * Created by zhouguizhi on 2017/11/8.
 */
public class UserDao extends BaseDao<User> {
    @Override
    public String getSql() {
        return super.getSql();
    }
    @Override
    public long insert(User entity) {
        List<User> list=query(new User());
        User where;
        for (User user:list)
        {
            where =new User();
            where.setUser_id(user.getUser_id());
            user.setStatus(1);
            Log.i("UserDao","用户"+user.getName()+"更改为未登录状态");
            update(user,where);
        }
        Log.i("UserDao","用户"+entity.getName()+"登录");
        entity.setStatus(0);//0表示登录
        return super.insert(entity);
    }
    /**
     * 得到当前登录的用户
     */
    public User getCurrentUser() {
        User user=new User();
        user.setStatus(0);
        List<User> list=query(user);
        if(list.size()>0)
        {
            return list.get(0);
        }
        return null;
    }
}
