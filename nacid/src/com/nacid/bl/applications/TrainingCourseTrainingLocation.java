package com.nacid.bl.applications;

import com.nacid.bl.applications.base.TrainingCourseTrainingLocationBase;

public interface TrainingCourseTrainingLocation extends TrainingCourseTrainingLocationBase{
	public Integer getTrainingInstitutionId();
	public TrainingInstitution getTrainingInstitution();
}
