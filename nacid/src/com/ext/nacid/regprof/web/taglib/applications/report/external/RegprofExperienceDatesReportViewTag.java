package com.ext.nacid.regprof.web.taglib.applications.report.external;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ext.nacid.regprof.web.model.applications.report.ExtRegprofApplicationForReportWebModel;
import com.nacid.regprof.web.model.applications.report.base.RegprofProfExpeienceDatesForReportBaseWebModel;
import com.nacid.regprof.web.model.applications.report.base.RegprofProfExperienceDocumentForReportBaseWebModel;
import com.nacid.web.WebKeys;

//RayaWritten------------------------------------------------------
public class RegprofExperienceDatesReportViewTag extends SimpleTagSupport{
    protected String attributePrefix;
    protected String documentId;
    ExtRegprofApplicationForReportWebModel webmodel;
    
    public void setAttributePrefix(String attributePrefix) {
        this.attributePrefix = attributePrefix == null ? "" : attributePrefix;
    }   
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void doTag() throws JspException, IOException {
        webmodel = (ExtRegprofApplicationForReportWebModel) getJspContext().getAttribute(attributePrefix + WebKeys.REGPROF_APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
        if (webmodel == null) {
            return;
        }        
        List<RegprofProfExperienceDocumentForReportBaseWebModel> docs = webmodel.getProfessionExperience().getDocuments();
        for(RegprofProfExperienceDocumentForReportBaseWebModel d: docs){
            if(documentId.equals(d.getId())){
                for(RegprofProfExpeienceDatesForReportBaseWebModel dt: d.getDates()){
                    String date = "от "+dt.getDateFrom() + " до "+dt.getDateTo()+"/"+dt.getWorkdayDuration()+" часов работен ден";
                    getJspContext().setAttribute("single_date", date);
                    getJspBody().invoke(null);
                }
            }
        }      

    }
    public ExtRegprofApplicationForReportWebModel getWebmodel() {
        return webmodel;
    }
    
}
//----------------------------------------------------------------
