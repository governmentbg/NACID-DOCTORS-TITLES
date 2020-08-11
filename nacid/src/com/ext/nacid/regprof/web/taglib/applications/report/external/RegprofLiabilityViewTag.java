package com.ext.nacid.regprof.web.taglib.applications.report.external;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ext.nacid.regprof.web.model.applications.report.ExtRegprofApplicationForReportWebModel;
import com.nacid.web.payments.LiabilityWebModel;

public class RegprofLiabilityViewTag extends SimpleTagSupport{
    public void doTag() throws JspException, IOException {
        RegprofApplicationReportViewTag parent = (RegprofApplicationReportViewTag)findAncestorWithClass(this, RegprofApplicationReportViewTag.class);
        if (parent == null) {
            return;
        }
        ExtRegprofApplicationForReportWebModel webmodel = parent.getWebModel();
        LiabilityWebModel l = webmodel.getLiability();
        if (l != null) {
            getJspContext().setAttribute("liability_amount", l.getAmount());
            getJspContext().setAttribute("liability_payment_date", l.getPaymentDate());
            getJspContext().setAttribute("liability_payment_type", l.getPaymentType());
            getJspContext().setAttribute("liability_status", l.getStatus());
            getJspBody().invoke(null);
        }
    }
}
