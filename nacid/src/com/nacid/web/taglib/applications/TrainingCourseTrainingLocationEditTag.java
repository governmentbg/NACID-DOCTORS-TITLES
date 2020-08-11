package com.nacid.web.taglib.applications;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.model.applications.TrainingCourseTrainingLocationWebModel;
import com.nacid.web.model.applications.TrainingCourseWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;

public class TrainingCourseTrainingLocationEditTag extends SimpleTagSupport {
	private String type;

	public void setType(String type) {
		this.type = type;
	}

	public void doTag() throws JspException, IOException {
		ComboBoxWebModel trainingCountry = (ComboBoxWebModel) getJspContext().getAttribute("trainingCountry", PageContext.REQUEST_SCOPE);
		if ("other".equals(type)) {
			TrainingCourseEditTag parent = (TrainingCourseEditTag) findAncestorWithClass(this, TrainingCourseEditTag.class);
			List<TrainingCourseTrainingLocationWebModel> trainingLocations;
			if (parent == null) {
				return;
			} else {
				TrainingCourseWebModel webModel = parent.getTrainingCourseWebModel();
				trainingLocations = webModel.getTrainingLocations();
			}
			if (trainingLocations == null) {
				trainingLocations = Arrays.asList(new TrainingCourseTrainingLocationWebModel("", null));
			}
			int i = 0;
			for (TrainingCourseTrainingLocationWebModel tl : trainingLocations) {
				getJspContext().setAttribute("training_location_id", tl.getId());
				getJspContext().setAttribute("training_location_city", tl.getTrainingLocationTrainingCity());
				getJspContext().setAttribute("row_id", i++);
				trainingCountry.setSelectedKey(tl.getTrainingLocationTrainingCountryId() == null ? null : tl.getTrainingLocationTrainingCountryId() + "");
				getJspBody().invoke(null);
			}

		} else if ("empty".equals(type)) {
			trainingCountry.setSelectedKey( null );
			getJspContext().setAttribute("training_location_city", "");
			getJspContext().setAttribute("row_id", "");
			getJspBody().invoke(null);
		}

	}
}
