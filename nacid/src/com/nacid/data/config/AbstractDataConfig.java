package com.nacid.data.config;

import java.util.ArrayList;
import java.util.List;

import com.nacid.data.config.xml.DataObject;
import com.nacid.data.config.xml.Element;

public abstract class AbstractDataConfig {
    protected abstract DataObject getDataObject(String clsName) throws DataConfigException;
    
    public List<String> getColumnNames(String clsName) throws DataConfigException{
        DataObject dataObject = getDataObject(clsName);
        List<String> result = new ArrayList<String>();
        for (Element e:dataObject.getElement()) {
            result.add(e.getColumnName());
        }
        return result;
    }
    public List<String> getVariableNames(String clsName) throws DataConfigException{
        DataObject dataObject = getDataObject(clsName);
        List<String> result = new ArrayList<String>();
        for (Element e:dataObject.getElement()) {
            result.add(e.getVariable());
        }
        return result;
    }
    /**
     * zasega unique e pyrvata kolona!!!!
     * @param className
     * @return
     * @throws IllegalArgumentException
     */
    public String getUniqueVariableName(String clsName) throws DataConfigException{
        DataObject dataObject = getDataObject(clsName);
        return dataObject.getElement().get(0).getVariable();
    }
    /**
     * zasega unique e pyrvata kolona!!!!
     * @param className
     * @return
     * @throws IllegalArgumentException
     */
    public String getUniqueColumnName(String clsName) throws DataConfigException{
        DataObject dataObject = getDataObject(clsName);
        return dataObject.getElement().get(0).getColumnName();
    }
    public String getTableName(String clsName) throws DataConfigException {
        return getDataObject(clsName).getTable();
    }
}
