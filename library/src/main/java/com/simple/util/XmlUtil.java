package com.simple.util;
import android.content.Context;
import com.simple.xml.update.UpdateDbXml;
import org.w3c.dom.Document;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
/**
 * Created by zhouguizhi on 2017/12/17.
 * 读取assets下xml文件
 */
public class XmlUtil {
    public static UpdateDbXml readDbXml(Context context) {
        if(null==context){
            return null;
        }
        InputStream is = null;
        Document document = null;
        try
        {
            is = context.getAssets().open("updatesql.xml");
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.parse(is);
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            IOUtils.close(is);
        }
        if (document == null)
        {
            return null;
        }
        UpdateDbXml xml = new UpdateDbXml(document);
        return xml;
    }
}
