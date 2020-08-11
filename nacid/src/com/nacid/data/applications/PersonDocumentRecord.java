package com.nacid.data.applications;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
//RayaWritten------------------------------------------------------------
import com.nacid.bl.RequestParameterInterface;
import com.nacid.bl.applications.PersonDocument;
import com.nacid.data.annotations.Table;
@Table(name="person_document")
public class PersonDocumentRecord implements PersonDocument{
    private Integer id;
    private String number;
    @DateTimeFormat(pattern="dd.MM.yyyy")
    private Date dateOfIssue;
    private String issuedBy;
    private Integer active;
    private Integer personId;

    public PersonDocumentRecord() {
    }
    public PersonDocumentRecord(Integer id, String number, Date dateOfIssue,
            String issuedBy, Integer active, Integer personId) {
        this.id = id;
        this.number = number;
        this.dateOfIssue = dateOfIssue;
        this.issuedBy = issuedBy;
        this.active = active;
        this.personId = personId;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
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
    public Integer getActive() {
        return active;
    }
    public void setActive(Integer active) {
        this.active = active;
    }
    public Integer getPersonId() {
        return personId;
    }
    public void setPersonId(Integer personId) {
        this.personId = personId;
    }
}

