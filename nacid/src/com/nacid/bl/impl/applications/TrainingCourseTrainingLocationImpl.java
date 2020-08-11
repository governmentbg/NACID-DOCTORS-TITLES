package com.nacid.bl.impl.applications;

import com.nacid.bl.applications.TrainingCourseTrainingLocation;
import com.nacid.bl.applications.TrainingInstitution;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.data.applications.TrainingCourseTrainingLocationRecord;

public class TrainingCourseTrainingLocationImpl implements TrainingCourseTrainingLocation {
	private NacidDataProviderImpl nacidDataProvider;
	private TrainingCourseTrainingLocationRecord record;
	public TrainingCourseTrainingLocationImpl(NacidDataProviderImpl nacidDataProvider, TrainingCourseTrainingLocationRecord record) {
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
		return getTrainingLocationCountryId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getCountry(record.getTrainingCountryId());
	}

	public Integer getTrainingLocationCountryId() {
		return record.getTrainingCountryId();
	}
	public Integer getTrainingInstitutionId() {
		return record.getTrainingInstitutionId();
	}
	@Override
	public TrainingInstitution getTrainingInstitution() {
		return getTrainingInstitutionId() == null ? null : nacidDataProvider.getTrainingInstitutionDataProvider().selectTrainingInstitution(getTrainingInstitutionId());
	}

}
