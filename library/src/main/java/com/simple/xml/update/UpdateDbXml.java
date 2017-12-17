package com.simple.xml.update;
import com.simple.xml.version.CreateVersion;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.List;
/**
 * 升级更新数据库
 * 把xml文件中的节点封装成具体的对象
 */
public class UpdateDbXml
{
	//升级脚本列表
	private List<UpdateStep> updateSteps;
	//升级版本
	private List<CreateVersion> createVersions;

	public UpdateDbXml(Document document){
		{
			if(null!=document){
				NodeList updateSteps = document.getElementsByTagName("updateStep");
				this.updateSteps = new ArrayList<>();
				if(null!=updateSteps&&updateSteps.getLength()>0){
					for (int i = 0; i < updateSteps.getLength(); i++)
					{
						Element ele = (Element) (updateSteps.item(i));
						UpdateStep step = new UpdateStep(ele);
						this.updateSteps.add(step);
					}
				}
			}
		}
		{
			if(null!=document){
				NodeList nodeList = document.getElementsByTagName("createVersion");
				this.createVersions = new ArrayList<>();
				if(null!=nodeList&&nodeList.getLength()>0){
					for (int i = 0; i < nodeList.getLength(); i++)
					{
						Element ele = (Element) (nodeList.item(i));
						CreateVersion cv = new CreateVersion(ele);
						this.createVersions.add(cv);
					}
				}
			}
		}
	}
	public List<UpdateStep> getUpdateSteps()
	{
		return updateSteps;
	}
	public List<CreateVersion> getCreateVersions()
	{
		return createVersions;
	}
}
