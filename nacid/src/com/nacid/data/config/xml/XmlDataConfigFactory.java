package com.nacid.data.config.xml;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.nacid.bl.impl.Utils;
import com.nacid.data.config.AbstractDataConfig;
import com.nacid.data.config.DataConfigException;

public class XmlDataConfigFactory extends AbstractDataConfig {
    private static volatile XmlDataConfigFactory instance;
    private Map<String, DataObject> dataObjects = new HashMap<String, DataObject>();

    private XmlDataConfigFactory() {
        try {
            JAXBContext jc = JAXBContext.newInstance("com.nacid.data.config.xml");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            Config config = (Config) unmarshaller.unmarshal(XmlDataConfigFactory.class.getClassLoader().getResourceAsStream(
            "com/nacid/data/config/xml/config.xml"));
            if (config.getDataObject().size() > 0) {
                Map<String, DataObject> exactMatches = new HashMap<String, DataObject>();
                for (DataObject o : config.getDataObject()) {
                    exactMatches.put(o.getClassName(), o);
                }
                for (DataObject o : exactMatches.values()) {
                    dataObjects.put(o.getClassName(), modifySuperClassesDataObject(o.getClassName(), exactMatches));
                }
            }

        } catch (Exception e) {
            throw Utils.logException(e);
        }

    }

    public static XmlDataConfigFactory getInstance() {
        if (instance == null) {
            synchronized (XmlDataConfigFactory.class) {
                if (instance == null) {
                    instance = new XmlDataConfigFactory();
                }
            }
        }
        return instance;
    }

    protected DataObject getDataObject(String className) throws DataConfigException{
        DataObject o = dataObjects.get(className);
        if (o == null) {
            throw new DataConfigException("Class: " + className + " is not defined in:com/nacid/data/config/xml/config.xml");
        }
        return o;
    }
    /**
     * prenarejda DataObject obektite taka 4e ako ima slednata definiciq
     * <dataObject className="com.nacid.data.nomenclatures.FlatNomenclatureRecord" table="">
        <element variable="id" columnName="id" />
        <element variable="name" columnName="name" />
        <element variable="dateFrom" columnName="date_from" />
        <element variable="dateTo" columnName="date_to" />
      </dataObject>
      <dataObject className="com.nacid.data.nomenclatures.SpecialityRecord" table="n_speciality">
        <element variable="professionGroupId" columnName="prof_group_id" />
      </dataObject>
      i SpecialityRecord e naslednik na FlatNomenclatureRecord, DataObject-a za SpecialityRecord-a sydyrja i poletata ot NomenclatureBaseRecorda!!!!!
     * @param className
     * @param objects
     * @return
     */
    protected static DataObject modifySuperClassesDataObject(String className, Map<String, DataObject> objects) {
        try {
            if (objects.get(className) != null) {
                DataObject result = objects.get(className);
                Class<?> c = Class.forName(className).getSuperclass();
                DataObject currentDataObject;
                while ((currentDataObject = objects.get(c.getName())) != null) {
                    result.getElement().addAll(0, currentDataObject.getElement());
                    c = c.getSuperclass();
                }
                return result;
            }
            return null;
        } catch (ClassNotFoundException e) {
            // e.printStackTrace();
            return null;
        }

    }
    public static void main(String[] args) throws DataConfigException {
        XmlDataConfigFactory factory = XmlDataConfigFactory.getInstance();
        DataObject dataObject = factory.getDataObject("com.nacid.data.nomenclatures.SpecialityRecord");
        for (Element e : dataObject.getElement()) {
            System.out.println(e.getColumnName() + "  " + e.getVariable());
        }
    }
}
