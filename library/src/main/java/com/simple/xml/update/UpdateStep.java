package com.simple.xml.update;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.List;
/**
 * 数据库升级脚本信息
 */
public class UpdateStep {
	//旧版本
	private String versionFrom;
	//新版本
	private String versionTo;
	//更新数据库脚本
	private List<UpdateDb> updateDbs;
	UpdateStep(Element ele)
	{
		versionFrom = ele.getAttribute("versionFrom");
		versionTo = ele.getAttribute("versionTo");
		updateDbs = new ArrayList<>();
		NodeList dbs = ele.getElementsByTagName("updateDb");
		for (int i = 0; i < dbs.getLength(); i++)
		{
			Element db = (Element) (dbs.item(i));
			UpdateDb updateDb = new UpdateDb(db);
			this.updateDbs.add(updateDb);
		}
	}
	public List<UpdateDb> getUpdateDbs()
	{
		return updateDbs;
	}
	public String getVersionFrom()
	{
		return versionFrom;
	}
	public String getVersionTo()
	{
		return versionTo;
	}
}
