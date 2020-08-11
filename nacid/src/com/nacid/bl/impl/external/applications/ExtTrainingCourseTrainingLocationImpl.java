package com.nacid.bl.impl.external.applications;

import com.nacid.bl.external.applications.ExtTrainingCourseTrainingLocation;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.data.external.applications.ExtTrainingCourseTrainingLocationRecord;

public class ExtTrainingCourseTrainingLocationImpl implements ExtTrainingCourseTrainingLocation {
	private NacidDataProviderImpl nacidDataProvider;
	private ExtTrainingCourseTrainingLocationRecord record;
	public ExtTrainingCourseTrainingLocationImpl(NacidDataProviderImpl nacidDataProvider, ExtTrainingCourseTrainingLocationRecord record) {
		this.nacidDataProvider = nacidDataProvider;
		this.record = record;
	}
	public int getId() {
		return record.getId();
	}
	public String getTrainingLocationCity() {
		return record.getTrainingCity();
	}

	
	public Country getTrainingLocationCountry() {
		return getTrainingLocationCountryId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getCountry(getTrainingLocationCountryId());
	}

	public Integer getTrainingLocationCountryId() {
		return record.getTrainingCountryId();
	}

}
