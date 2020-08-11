package com.nacid.data.regprof;

import java.sql.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.springframework.format.annotation.DateTimeFormat;

import com.nacid.bl.impl.Utils;
import com.nacid.data.annotations.Table;
import com.nacid.data.external.applications.xml.Adapter2;

@Table(name="regprof.profession_experience_dates")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "id",
    "dateFrom",
    "dateTo",
    "workdayDuration",
    "profExperienceDocumentId"
})
@XmlRootElement(name = "profession_experience_dates_record")
public class RegprofProfessionExperienceDatesRecord {
    
    private Integer id;
    @DateTimeFormat(pattern="dd.MM.yyyy")
    @XmlJavaTypeAdapter(Adapter2.class)
    private Date dateFrom;
    @DateTimeFormat(pattern="dd.MM.yyyy")
    @XmlJavaTypeAdapter(Adapter2.class)
    private Date dateTo;
    private Integer workdayDuration;
    private Integer profExperienceDocumentId;
    public RegprofProfessionExperienceDatesRecord() {}
    
    

    public RegprofProfessionExperienceDatesRecord(Integer id, Date dateFrom,
            Date dateTo, Integer workdayDuration,
            Integer profExperienceDocumentId) {
        this.id = id;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.workdayDuration = workdayDuration;
        this.profExperienceDocumentId = profExperienceDocumentId;
    }



    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Date getDateFrom() {
        return dateFrom;
    }
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }
    public Date getDateTo() { 
        return dateTo;
    }
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
    public Integer getWorkdayDuration() {
        return workdayDuration;
    }
    public void setWorkdayDuration(Integer workdayDuration) {
        this.workdayDuration = workdayDuration;
    }
    
    public Integer getProfExperienceDocumentId() {
        return profExperienceDocumentId;
    }

    public void setProfExperienceDocumentId(Integer profExperienceDocumentId) {
        this.profExperienceDocumentId = profExperienceDocumentId;
    }

    //RayaWritten-----------------------------------------------
    @Override
    public boolean equals(Object o){
        boolean b = true;
        if(o.getClass() == this.getClass()){
           if(Utils.fieldChanged(((RegprofProfessionExperienceDatesRecord)o).getDateFrom(), this.getDateFrom())){
               b = false;
           }
           if(Utils.fieldChanged(((RegprofProfessionExperienceDatesRecord)o).getDateTo(), this.getDateTo())){
               b = false;
           }
           if(Utils.fieldChanged(((RegprofProfessionExperienceDatesRecord)o).getWorkdayDuration(), this.getWorkdayDuration())){
               b = false;
           }
        }
        return b;
    }
    //-------------------------------------------------------------
}