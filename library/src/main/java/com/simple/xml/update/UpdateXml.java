package com.simple.xml.update;

import com.simple.xml.version.CreateVersion;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShuWen on 2017/2/18.
 */

public class UpdateXml {

    private List<CreateVersion> createVersions;

    private List<UpdateStep> updateSteps;

    public UpdateXml(Document document){

        createVersions = new ArrayList<>();
        updateSteps = new ArrayList<>();
        NodeList createversionList = document.getElementsByTagName("createVersion");
        for (int i = 0; i < createversionList.getLength(); i++) {
            Element element = (Element) createversionList.item(i);
            CreateVersion createVersion = new CreateVersion(element);
            createVersions.add(createVersion);
        }

        NodeList updateSetList = document.getElementsByTagName("updateStep");
        for (int j = 0; j < updateSetList.getLength(); j++) {
            Element element = (Element) updateSetList.item(j);
            UpdateStep step = new UpdateStep(element);
            updateSteps.add(step);
        }

    }

    public List<CreateVersion> getCreateVersions() {
        return createVersions;
    }

    public List<UpdateStep> getUpdateSteps() {
        return updateSteps;
    }
}
