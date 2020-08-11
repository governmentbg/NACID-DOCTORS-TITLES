package com.ext.nacid.web.model.applications.report;

import com.nacid.bl.external.ExtCompany;
import com.nacid.web.model.applications.report.base.CompanyForReportBaseWebModel;

/**
 * Created by georgi.georgiev on 16.09.2015.
 */
public class ExtCompanyForReportWebModel extends CompanyForReportBaseWebModel{
    public ExtCompanyForReportWebModel(ExtCompany c) {
        super(c);
    }
}
