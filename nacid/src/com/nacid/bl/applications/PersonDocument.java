package com.nacid.bl.applications;

import java.util.Date;

import com.nacid.bl.applications.base.PersonDocumentBase;
//RayaWritten---------------------------------
public interface PersonDocument extends PersonDocumentBase{
    public Integer getId();
    public void setId(Integer id);
    public String getNumber();
    public void setNumber(String number);
    public Date getDateOfIssue();
    public void setDateOfIssue(Date dateOfIssue);
    public String getIssuedBy();
    public void setIssuedBy(String issuedBy);
    public Integer getActive();
    public void setActive(Integer active);
    public Integer getPersonId();
    public void setPersonId(Integer personId);

}
//--------------------------------------------
