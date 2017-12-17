package com.simple.xml.db;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建数据库脚本
 */
public class CreateDb
{
	//数据库表名
	private String name;
	//创建表的sql语句集合
	private List<String> sqlCreates;

	public CreateDb(Element ele)
	{
		name = ele.getAttribute("name");
		{
			sqlCreates = new ArrayList<>();
			NodeList sql = ele.getElementsByTagName("sql_createTable");
			if(null!=sql&&sql.getLength()>0){
				for (int i = 0; i < sql.getLength(); i++)
				{
					String sqlCreate = sql.item(i).getTextContent();
					this.sqlCreates.add(sqlCreate);
				}
			}
		}
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public List<String> getSqlCreates()
	{
		return sqlCreates;
	}
}
