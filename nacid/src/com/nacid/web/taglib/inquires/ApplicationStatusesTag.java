package com.nacid.web.taglib.inquires;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.inquires.ApplicationStatusesWebModel;

public class ApplicationStatusesTag extends SimpleTagSupport {
    
    @Override
    public void doTag() throws JspException, IOException {
        ApplicationStatusesWebModel webModel = (ApplicationStatusesWebModel)getJspContext().getAttribute(WebKeys.FINAL_APP_STATUSES, PageContext.REQUEST_SCOPE);
        int cnt = 1;
        for (String status:webModel.getApplicationStatuses().keySet()) {
            getJspContext().setAttribute("row_no", cnt++);
            getJspContext().setAttribute("status", status);
            getJspContext().setAttribute("apps_count", webModel.getApplicationStatuses().get(status));
            getJspBody().invoke(null);
        }
        
    }
}
