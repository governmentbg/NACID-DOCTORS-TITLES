package com.ext.nacid.web.model.applications.report;

import com.nacid.bl.external.ExtPerson;
import com.nacid.web.model.applications.report.base.PersonForReportBaseWebModel;

public class ExtPersonForReportWebModel extends PersonForReportBaseWebModel {
    private String email;
	public ExtPersonForReportWebModel(ExtPerson p) {
		super(p);
		this.email = p.getEmail();
	}
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
	

}
