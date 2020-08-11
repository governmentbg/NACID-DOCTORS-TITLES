package com.ext.nacid.web.taglib.applications;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ext.nacid.web.model.applications.ExtTrainingCourseTrainingLocationWebModel;
import com.ext.nacid.web.model.applications.ExtTrainingCourseWebModel;
import com.nacid.web.WebKeys;
import com.nacid.web.model.common.ComboBoxWebModel;


public class ExtTrainingCourseTrainingLocationEditTag extends SimpleTagSupport {
    private String type;
    public void setType(String type) {
    	this.type = type;
    }
    
    public void doTag() throws JspException, IOException {
        ComboBoxWebModel trainingCountry = (ComboBoxWebModel) getJspContext().getAttribute("trainingCountry", PageContext.REQUEST_SCOPE);
        if ("other".equals(type)) {
        	ExtTrainingCourseWebModel webModel = (ExtTrainingCourseWebModel) getJspContext().getAttribute(WebKeys.EXT_TRAINING_COURSE_WEB_MODEL, PageContext.REQUEST_SCOPE);
        	
        	List<ExtTrainingCourseTrainingLocationWebModel> trainingLocations = webModel == null ? null : webModel.getTrainingLocations();
            
        	if (trainingLocations == null) {
            	trainingLocations = Arrays.asList(new ExtTrainingCourseTrainingLocationWebModel("", null));
            }
        	int i = 0;
        	for (ExtTrainingCourseTrainingLocationWebModel tl : trainingLocations) {
				getJspContext().setAttribute("training_location_city", tl.getTrainingLocationTrainingCity());
				getJspContext().setAttribute("row_id", i++);
				trainingCountry.setSelectedKey(tl.getTrainingLocationTrainingCountryId() == null ? null : tl.getTrainingLocationTrainingCountryId() + "");
				getJspBody().invoke(null);
			}
            	
        } else if ("empty".equals(type)) {
        	trainingCountry.setSelectedKey( null );
        	getJspContext().setAttribute("training_location_city", "");
        	getJspBody().invoke(null);
        }
        
        
        
    }
}
