package com.nacid.data.config;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.nacid.data.config.annotations.AnnotationDataConfigFactory;
import com.nacid.data.config.xml.XmlDataConfigFactory;

public class DataConfigFactory {
    List<AbstractDataConfig> config = new ArrayList<AbstractDataConfig>();
    private static DataConfigFactory instance;
    private DataConfigFactory() {
        config.add(XmlDataConfigFactory.getInstance());
        config.add(AnnotationDataConfigFactory.getInstance());
    }
    public static DataConfigFactory getInstance() {
        if (instance == null) {
            synchronized (DataConfigFactory.class) {
                if (instance == null) {
                    instance = new DataConfigFactory();
                }
            }
        }
        return instance;
    }
    public List<String> getVariableNames(String clsName) {
        return (List<String>) execute(clsName, "getVariableNames");
    }
    public List<String> getColumnNames(String clsName) {
        return (List<String>) execute(clsName, "getColumnNames");
    }
    public String getUniqueColumnName(String clsName) {
        return (String) execute(clsName, "getUniqueColumnName");
    }
    public String getUniqueVariableName(String clsName) {
        return (String) execute(clsName, "getUniqueVariableName");
    }
    public String getTableName(String clsName) {
        return (String) execute(clsName, "getTableName");
    }
    private Object execute(String clsName, String methodName) {
        for (AbstractDataConfig dc : config) {
            try {
                return dc.getClass().getMethod(methodName, String.class).invoke(dc, clsName);
            } catch (Exception e) {
                if (e.getCause() == null || !(e.getCause() instanceof DataConfigException)) {
                    throw new RuntimeException(e.getCause() == null ? e : e.getCause());
                }
            }
        }
        throw new RuntimeException("No information for class is preset inside com/nacid/config/xml/config.xml nor the class with name " + clsName + " is annotated with com.nacid.data.config.annotations.Table annotation");
    }
    public static void main(String[] args) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        DataConfigFactory factory = DataConfigFactory.getInstance();
        //System.out.println(factory.execute(TestRecord.class.getName(), "getColumnNames", List.class));
        //System.out.println(factory.getTableName(com.nacid.data.external.users.ExtUserGroupMembershipRecord.class.getName()));
    }
}
