package com.nacid.web.model.applications;

import com.nacid.bl.applications.PersonDocument;
import com.nacid.data.DataConverter;

public class PersonDocumentWebModel {
    private String id;
    private String personId;
    private String dateOfIssue;
    private String issuedBy;
    private String number;
    public PersonDocumentWebModel(PersonDocument pd) {
        this.id = pd.getId() + "";
        this.dateOfIssue = DataConverter.formatDate(pd.getDateOfIssue());
        this.personId = pd.getPersonId() + "";
        this.issuedBy = pd.getIssuedBy();
        this.number = pd.getNumber();
    }
    public String getId() {
        return id;
    }

    public String getPersonId() {
        return personId;
    }

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public String getNumber() {
        return number;
    }
}
