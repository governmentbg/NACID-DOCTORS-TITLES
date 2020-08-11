package com.nacid.web.model.inquires;

import com.nacid.web.model.applications.DiplomaTypeIssuerWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;

public class InquireInstitutionWebModel extends DiplomaTypeIssuerWebModel {

	public InquireInstitutionWebModel(Integer countryId, ComboBoxWebModel institutionCombo) {
		super(countryId, institutionCombo, null);
	}

}
