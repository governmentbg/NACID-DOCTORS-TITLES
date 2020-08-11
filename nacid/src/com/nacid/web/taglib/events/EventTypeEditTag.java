package com.nacid.web.taglib.events;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.events.EventTypeWebModel;

public class EventTypeEditTag extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException, IOException {
        EventTypeWebModel webmodel = (EventTypeWebModel) getJspContext()
            .getAttribute( WebKeys.EVENT_TYPE_WEB_MODEL, PageContext.REQUEST_SCOPE);
    
        
        getJspContext().setAttribute("id", webmodel.getId());
        getJspContext().setAttribute("eventName", object2str(webmodel.getEventName()));
        getJspContext().setAttribute("reminderDays", object2str(webmodel.getReminderDays()));
        getJspContext().setAttribute("dueDays", object2str(webmodel.getDueDays()));
        getJspContext().setAttribute("reminderText", object2str(webmodel.getReminderText()));
        
        getJspBody().invoke(null);
    }
    
    private static String object2str(Object o) {
        if(o == null) {
            return "";
        }
        return o.toString();
    }
}
