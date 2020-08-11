package com.nacid.data.external.applications;

import com.nacid.bl.external.ExtPersonDocument;
import com.nacid.data.annotations.Table;
import org.springframework.format.annotation.DateTimeFormat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.sql.Date;
@Table(name="eservices.person_document")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "id",
    "personId",
    "dateOfIssue",
    "issuedBy",
    "number",
    "active"
})
@XmlRootElement(name = "ext_person_document_record")
public class ExtPersonDocumentRecord implements ExtPersonDocument {
    private Integer id;
    private Integer personId;
    @DateTimeFormat(pattern="dd.MM.yyyy")
    //@XmlJavaTypeAdapter(Adapter2.class)
    private Date dateOfIssue;
    private String issuedBy;
    private String number;
    private Integer active;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getPersonId() {
        return personId;
    }
    public void setPersonId(Integer personId) {
        this.personId = personId;
    }
    public Date getDateOfIssue() {
        return dateOfIssue;
    }
    public void setDateOfIssue(Date dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }
    public String getIssuedBy() {
        return issuedBy;
    }
    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public Integer getActive() {
        return active;
    }
    public void setActive(Integer active) {
        this.active = active;
    }
    
}
