package com.nacid.bl.impl.regprof.external.applications;

import com.nacid.bl.impl.applications.regprof.RegprofApplicationDetailsBaseImpl;
import com.nacid.data.annotations.Table;

import java.sql.Timestamp;

@Table(name="eservices.regprof_application")
public class ExtRegprofApplicationDetailsImpl extends RegprofApplicationDetailsBaseImpl {
    private String appXml;
    private Integer regprofApplicationId;
    private Timestamp dateSubmitted;
    public String getAppXml() {
        return appXml;
    }
    public void setAppXml(String appXml) {
        this.appXml = appXml;
    }
    public Integer getRegprofApplicationId() {
        return regprofApplicationId;
    }
    public void setRegprofApplicationId(Integer regprofApplicationId) {
        this.regprofApplicationId = regprofApplicationId;
    }
    public Timestamp getDateSubmitted() {
        return dateSubmitted;
    }
    public void setDateSubmitted(Timestamp dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }
}