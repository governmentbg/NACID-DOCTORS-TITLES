package com.nacid.web.taglib.comission;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.comission.CommissionCalendarProtocolWebModel;

public class CommissionCalendarProtocolEditTag extends SimpleTagSupport {
    
    @Override
    public void doTag() throws JspException, IOException {
        CommissionCalendarProtocolWebModel webModel = (CommissionCalendarProtocolWebModel)getJspContext()
            .getAttribute(WebKeys.COMMISSION_CALENDAR_PROTOCOL_WEB_MODEL, PageContext.REQUEST_SCOPE);
        
        getJspContext().setAttribute("calendarId", webModel.getCalendarId());
        getJspContext().setAttribute("fileName", webModel.getFileName());
        getJspContext().setAttribute("operation", webModel.getOperation());
        getJspContext().setAttribute("hasDownload", webModel.getFileName() != null);
        
        getJspBody().invoke(null);
    }
}
