package com.ext.nacid.regprof.web.taglib.applications.report.internal;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import com.nacid.regprof.web.model.applications.report.base.RegprofProfExperienceDocumentForReportBaseWebModel;
import com.nacid.regprof.web.model.applications.report.internal.RegprofApplicationInternalForReportWebModel;
import com.nacid.web.WebKeys;


//RayaWritten--------------------------------------------------------------------------------------
public class RegprofExperienceDocumentReportViewTag extends  SimpleTagSupport{
    protected String attributePrefix;
    RegprofApplicationInternalForReportWebModel webmodel;
    public void setAttributePrefix(String attributePrefix) {
        this.attributePrefix = attributePrefix == null ? "" : attributePrefix;
    }
    
    public void doTag() throws JspException, IOException {
        webmodel = (RegprofApplicationInternalForReportWebModel) getJspContext().getAttribute(attributePrefix + WebKeys.REGPROF_APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
        if (webmodel == null) {
            return;
        }        
        List<RegprofProfExperienceDocumentForReportBaseWebModel> docs = webmodel.getProfessionExperience().getDocuments();
        for(RegprofProfExperienceDocumentForReportBaseWebModel d: docs){
            getJspContext().setAttribute("doc_id", d.getId());
            getJspContext().setAttribute("prof_expr_document_type", d.getProfExperienceDocType());
            getJspContext().setAttribute("document_number", d.getDocumentNumber());
            getJspContext().setAttribute("document_date", d.getDocumentDate());
            getJspContext().setAttribute("document_issuer", d.getDocumentIssuer());
            getJspContext().setAttribute("dates_count", d.getDatesCount());  
            getJspBody().invoke(null);
        }      
        
    }

    public RegprofApplicationInternalForReportWebModel getWebmodel() {
        return webmodel;
    }
    
}
//-----------------------------------------------------------------------------------------------------