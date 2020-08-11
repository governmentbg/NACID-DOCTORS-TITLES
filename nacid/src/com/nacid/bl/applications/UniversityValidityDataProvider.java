package com.nacid.bl.applications;

import java.sql.Date;
import java.util.List;

public interface UniversityValidityDataProvider {

	public List<UniversityValidity> getUniversityValiditiesByUniversity(int universityId);
	public UniversityValidity getUniversityValidity(int id);
	public void deleteUniversityValidity(int id);
	public int saveUniversityValidity(int id, Integer universityId, Integer userId, Date examinationDate, boolean isComunicated, boolean isRecognized,
            String notes, Integer trainingLocationId, boolean hasJoinedDegrees, List<Object> availableTrainingForms, List<Integer> selectedInstitutions);
}
