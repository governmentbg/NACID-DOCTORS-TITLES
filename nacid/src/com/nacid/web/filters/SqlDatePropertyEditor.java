package com.nacid.web.filters;

import com.nacid.data.DataConverter;

import java.beans.PropertyEditorSupport;
import java.sql.Date;

/**
 * Created by georgi.georgiev on 23.01.2015.
 */
public class SqlDatePropertyEditor extends PropertyEditorSupport {
    public void setAsText(String inc){
        java.util.Date res = DataConverter.parseDate(inc);
        java.sql.Date b =  res == null ? null : new java.sql.Date(res.getTime());
        setValue(b);
    }
    public String getAsText(){
        java.sql.Date val = (Date)getValue();
        return DataConverter.formatDate(val);
    }
}