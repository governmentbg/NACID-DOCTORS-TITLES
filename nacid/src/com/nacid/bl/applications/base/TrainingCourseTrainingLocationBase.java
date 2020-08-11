package com.nacid.bl.applications.base;

import com.nacid.bl.nomenclatures.Country;

public interface TrainingCourseTrainingLocationBase {
	public int getId();
	public Integer getTrainingLocationCountryId();
    public Country getTrainingLocationCountry();
    public String getTrainingLocationCity();
}
