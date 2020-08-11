package com.nacid.data.regprof;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.springframework.util.AutoPopulatingList;

import com.nacid.data.annotations.SuppressColumn;
import com.nacid.data.annotations.Table;

@Table(name="regprof.profession_experience_documents")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "id",
    "profExperienceDocTypeId",
    "documentNumber",
    "documentDate",
    "documentIssuer",
    "profExperienceId",
    "forExperienceCalculation",
    "dates"
})
@XmlRootElement(name = "regprof_profession_experience_documents_record")
public class RegprofProfessionExperienceDocumentRecord {
    private Integer id;
    private Integer profExperienceDocTypeId;
    private String documentNumber;
    private String documentDate;
    private String documentIssuer;
    private Integer profExperienceId;
    private int forExperienceCalculation;
    @SuppressColumn
    protected List<? extends RegprofProfessionExperienceDatesRecord> dates = new AutoPopulatingList<RegprofProfessionExperienceDatesRecord>(RegprofProfessionExperienceDatesRecord.class);
    
    public RegprofProfessionExperienceDocumentRecord() {
    }
    public RegprofProfessionExperienceDocumentRecord(Integer id,
            Integer profExperienceDocTypeId, String documentNumber,
            String documentDate, String documentIssuer, Integer profExperienceId,
            int forExperienceCalculation,
            List<? extends RegprofProfessionExperienceDatesRecord> dates) {
        this.id = id;
        this.profExperienceDocTypeId = profExperienceDocTypeId;
        this.documentNumber = documentNumber;
        this.documentDate = documentDate;
        this.documentIssuer = documentIssuer;
        this.profExperienceId = profExperienceId;
        this.forExperienceCalculation = forExperienceCalculation;
        this.dates = dates;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getProfExperienceDocTypeId() {
        return profExperienceDocTypeId;
    }
    public void setProfExperienceDocTypeId(Integer profExperienceDocTypeId) {
        this.profExperienceDocTypeId = profExperienceDocTypeId;
    }
    public String getDocumentNumber() {
        return documentNumber;
    }
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
    public String getDocumentDate() {
        return documentDate;
    }
    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }
    public String getDocumentIssuer() {
        return documentIssuer;
    }
    public void setDocumentIssuer(String documentIssuer) {
        this.documentIssuer = documentIssuer;
    }
    public Integer getProfExperienceId() {
        return profExperienceId;
    }
    public void setProfExperienceId(Integer profExperienceId) {
        this.profExperienceId = profExperienceId;
    }
    public List<? extends RegprofProfessionExperienceDatesRecord> getDates() {
        return dates;
    }
    public void setDates(List<? extends RegprofProfessionExperienceDatesRecord> dates) {
        this.dates = dates;
    }
    public int getForExperienceCalculation() {
        return forExperienceCalculation;
    }
    public void setForExperienceCalculation(int forExperienceCalculation) {
        this.forExperienceCalculation = forExperienceCalculation;
    }
    public int getDatesCount() {
        return dates == null ? 0 : dates.size();
    }
}