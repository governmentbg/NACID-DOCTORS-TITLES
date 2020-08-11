package com.nacid.regprof.web.model.applications.report.base;

import com.nacid.bl.applications.base.PersonDocumentBase;
import com.nacid.data.DataConverter;

//RayaWritten-----------------------------------------------
public class PersonDocumentForReportBaseWebModel {
    private String id;
    private String number;
    private String dateOfIssue;
    private String issuedBy;
    private boolean active;
    
    public PersonDocumentForReportBaseWebModel(PersonDocumentBase personDoc){
        id = personDoc.getId() + "";
        number = personDoc.getNumber();
        dateOfIssue = DataConverter.formatDate(personDoc.getDateOfIssue());
        issuedBy = personDoc.getIssuedBy();
        active = personDoc.getActive() == 1 ? true : false;
    }

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public boolean isActive() {
        return active;
    }
}
//---------------------------------------------------------------------