package com.nacid.bl.external;

import com.nacid.bl.applications.base.PersonDocumentBase;

import java.util.Date;

public interface ExtPersonDocument extends PersonDocumentBase {
    public Integer getId();
    public Integer getPersonId();
    public Date getDateOfIssue();
    public String getIssuedBy();
    public String getNumber();
    public Integer getActive();
}
