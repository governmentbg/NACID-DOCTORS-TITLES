package com.nacid.data.config.annotations;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.academicrecognition.BGAcademicRecognitionExtendedImpl;
import com.nacid.data.annotations.Column;
import com.nacid.data.annotations.SuppressColumn;
import com.nacid.data.annotations.Table;
import com.nacid.data.config.AbstractDataConfig;
import com.nacid.data.config.DataConfigException;
import com.nacid.data.config.xml.DataObject;
import com.nacid.data.config.xml.Element;

public class AnnotationDataConfigFactory extends AbstractDataConfig {
    private static AnnotationDataConfigFactory instance;
    private Map<String, DataObject> dataObjectsByClass = new HashMap<String, DataObject>();
    private AnnotationDataConfigFactory() {}
    public static AnnotationDataConfigFactory getInstance() {
        if (instance == null) {
            synchronized (AnnotationDataConfigFactory.class) {
                if (instance == null) {
                    instance = new AnnotationDataConfigFactory();
                }
            }
        }
        return instance;
    }
    protected DataObject getDataObject(String clsName) {
        DataObject result = dataObjectsByClass.get(clsName);
        if (result == null) {
            result = loadClassData(clsName);
        }
        return result;
    }
    
    
    private synchronized DataObject loadClassData(String clsName) {
        try {
            DataObject dataObject = new DataObject();
            dataObject.setClassName(clsName);
            Class<?> cls = Class.forName(clsName);
            //dataObject.setTable(getTableName(cls));
            for (Field f:getUsableFields(cls)) {
                Element element = new Element();
                element.setVariable(f.getName());
                element.setColumnName(getColumnName(f));
                dataObject.getElement().add(element);
            }
            dataObjectsByClass.put(clsName, dataObject);
            return dataObject;
        } catch (ClassNotFoundException e) {
            throw Utils.logException(e);
        }

    }
    protected Field[] getUsableFields(Class<?> c) {
        List<Field> allFields = new ArrayList<Field>();
        
        
        Class<?> superclass = c.getSuperclass();
        while (superclass != null) {
            Field[] fields = superclass.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                allFields.addAll(Arrays.asList(fields));
            }
            superclass = superclass.getSuperclass();
        }
        Field[] fields = c.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            allFields.addAll(Arrays.asList(fields));    
        }
        List<Field> result = new ArrayList<Field>();
        for (Field f:allFields) {
            if ((f.getModifiers() & Modifier.STATIC) == Modifier.STATIC || f.isAnnotationPresent(SuppressColumn.class)) {
                continue;
            }
            result.add(f);
        }
        return result.toArray(new Field[0]);
    }
    
    public String getColumnName(Field f) {
        Column annotation = ((Column)f.getAnnotation(Column.class));
        if (annotation != null) {
            if (StringUtils.isEmpty(annotation.name())) {
                throw new RuntimeException("No ColumnName defined for field = " + f.getName());
            }
            return annotation.name();
        }
        String fieldName = f.getName();
        StringBuilder buff = new StringBuilder("");
        for (int i = 0; i < fieldName.length(); i++) {
            if (Character.isUpperCase(fieldName.charAt(i))) {
                buff.append('_');
            } 
            buff.append(Character.toLowerCase(fieldName.charAt(i)));
        }
        return buff.toString();
    }
    
    
    /**
     * predefiniram go, zashtoto da moje anotaciite da se polzvat za SELECT-vane na records, koito ne sa anotirani s @Table
     * tyj kato  getTableName(Class<?> cls) hvyrlq exception, ako lipsva tazi anotaciq.... Ideqtae e da se hvyrlq exception pri obry6tenie kym getTableName(String clsName)
     * a ne osthte pri generiraneto na DataObject-a 
     */
    @Override
    public String getTableName(String clsName) throws DataConfigException {
        try {
            return getTableName(Class.forName(clsName));
        } catch (ClassNotFoundException e) {
            throw Utils.logException(e);
        }
    } 
    protected String getTableName(Class<?> cls) {
        Table annotation = ((Table)cls.getAnnotation(Table.class));
        if (annotation == null) {
            throw new IllegalArgumentException("No annotation " + Table.class.getName() + " defined for class:" + cls.getName());
        }
        String tableName = annotation.name();
        if (StringUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("No name attribute defined inside the annotation: " + Table.class.getName() + " for class:" + cls.getName());
        }
        return tableName;
    }
    public static void main(String[] args) throws DataConfigException {
        AnnotationDataConfigFactory factory = new AnnotationDataConfigFactory();
        System.out.println(factory.getColumnNames(BGAcademicRecognitionExtendedImpl.class.getName()));
        
    }
}
