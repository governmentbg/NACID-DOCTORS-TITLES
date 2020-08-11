package com.nacid.web.model.inquires;

import com.nacid.web.model.applications.DiplomaTypeIssuerWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;

public class CommissionInquireUniversityWebModel extends DiplomaTypeIssuerWebModel {

	public CommissionInquireUniversityWebModel(Integer countryId, ComboBoxWebModel universityCombo) {
		super(countryId, universityCombo, null);
	}

}
