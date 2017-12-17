package test.com.simplesqlitedb.manager;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import com.simple.config.DBConfig;
import com.simple.dao.factory.DaoFactory;
import com.simple.util.FileUtils;
import com.simple.util.IOUtils;
import com.simple.util.XmlUtil;
import com.simple.xml.db.CreateDb;
import com.simple.xml.update.UpdateDbXml;
import com.simple.xml.version.CreateVersion;
import java.io.File;
import java.util.List;
import test.com.simplesqlitedb.bean.User;
import test.com.simplesqlitedb.dao.UserDao;
/**
 * Created by zhouguizhijxhz on 2017/12/17.
 */
public class MultithUserLoginManager {
    private static final String TAG ="UpdateManager" ;
    private List<User> userList ;
    private File userChildFile ;
    private static MultithUserLoginManager INSTANCE ;
    private static Context mContext;
    public static MultithUserLoginManager getInstance(Context context){
        mContext = context;
        INSTANCE = new MultithUserLoginManager(context);
        return INSTANCE;
    }
    private MultithUserLoginManager(Context context)
    {
        if(null!=context){
            //这个path是创建数据库的路径
            userChildFile = new File(FileUtils.createUserDataBasePath(context.getApplicationContext()));
        }
        if(null!=userChildFile&&!userChildFile.exists())
        {
            userChildFile.mkdirs();
        }
    }
    /**
     * 检查表
     * @param dbVersionName 当前数据库版本
     */
    public void checkTable(String dbVersionName)  {
        UserDao userDao= DaoFactory.getInstance().getBaseDao(UserDao.class,User.class);
        userList=userDao.query(new User());
        UpdateDbXml xml = XmlUtil.readDbXml(mContext);
        CreateVersion currentCreateVersion = analyseCreateVersion(xml, dbVersionName);
        try
        {
            executeCreateVersion(currentCreateVersion,true);
        } catch (Exception e)
        {
        }
    }
    /**
     * 根据sql 检查sql是否正确
     * @param createVersion
     * @throws Exception
     */
    private void executeCreateVersion(CreateVersion createVersion,boolean isPhoto) throws Exception {
        if (createVersion == null || createVersion.getCreateDbs() == null||createVersion.getCreateDbs().isEmpty())
        {
            throw new Exception("no  sql statement");
        }
        List<CreateDb> list = createVersion.getCreateDbs();
        for (CreateDb cd : list)
        {
            if (cd == null || cd.getName() == null)
            {
                throw new Exception("db or dbName is null");
            }
            if (!"photo".equals(cd.getName()))
            {
                continue;
            }
            //获取创建表的sql语句
            List<String> tables = cd.getSqlCreates();
            SQLiteDatabase database = null;
            try
            {
                if (userList != null && !userList.isEmpty())
                {
                    for (int i = 0; i < userList.size(); i++)
                    {
                        database = getDatabase(cd, userList.get(i).getUser_id());
                        executeSql(database, tables);
                        database.close();
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            } finally
            {
                IOUtils.close(database);
            }
        }
    }
    /**
     * 执行sql语句
     */
    private void executeSql(SQLiteDatabase sqLiteDatabase, List<String> tables) throws Exception {
        if (null==sqLiteDatabase||null==tables||tables.isEmpty())
        {
            return;
        }
        // 开启事务
        sqLiteDatabase.beginTransaction();
        for (String sql : tables)
        {
            sql = sql.replaceAll("\r\n", " ");
            sql = sql.replaceAll("\n", " ");
            if (!TextUtils.isEmpty(sql.trim()))
            {
                try
                {
                    sqLiteDatabase.execSQL(sql);
                } catch (SQLException e)
                {
                }
            }
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }
    private SQLiteDatabase getDatabase(CreateDb db, String userId) {
        return getDatabase(null!=db?db.getName():"", userId);
    }

    /**
     * 创建数据库,在多用户登录情况下根据用户的额userid生成目录
     */
    private SQLiteDatabase getDatabase(String dbName, String userId) {
        String dbFilePath = null;
        SQLiteDatabase sqlLiteDatabase = null;
        File file=new File(userChildFile,userId);
        if(!file.exists())
        {
            file.mkdirs();
        }
        if (dbName.equalsIgnoreCase("photo"))
        {
            dbFilePath = file.getAbsolutePath()+ "/photo.db";// logic对应的数据库路径
        }else if (dbName.equalsIgnoreCase("user"))
        {
            DBConfig dbConfig = DaoFactory.getInstance().getDbConfig();
            if(null!=dbConfig){
                dbFilePath = dbConfig.getSqliteDataBasePath()+File.separator+dbConfig.getSqliteDataBaseName();
            }
        }
        if (dbFilePath != null)
        {
            File f = new File(dbFilePath);
            f.mkdirs();
            if (f.isDirectory())
            {
                f.delete();
            }
            Log.e(TAG,"dbFilePath="+dbFilePath);
            sqlLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFilePath, null);
        }
        return sqlLiteDatabase;
    }
    /**
     * 解析出对应版本的建表脚本
     * @return
     */
    private CreateVersion analyseCreateVersion(UpdateDbXml xml, String version) {
        CreateVersion cv = null;
        if (xml == null || version == null)
        {
            return cv;
        }
        List<CreateVersion> createVersions = xml.getCreateVersions();
        if (createVersions != null)
        {
            for (CreateVersion item : createVersions)
            {
                String[] createVersion = item.getVersion().trim().split(",");
                for (int i = 0; i < createVersion.length; i++)
                {
                    if (createVersion[i].trim().equalsIgnoreCase(version))
                    {
                        cv = item;
                        break;
                    }
                }
            }
        }
        return cv;
    }
}
