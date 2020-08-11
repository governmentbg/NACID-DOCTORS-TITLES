package com.nacid.bl.applications;

import java.util.Date;
import java.util.List;

public interface TrainingInstitutionDataProvider {

    //public List<TrainingInstitution> selectTrainingInstitutionsByUniversity(int universityId);
    
	public List<TrainingInstitution> selectTrainingInstitutionsByCountry(int countryId);
    public List<TrainingInstitution> selectTrainingInstitutionsByUniversities(List<Integer> universityIds);
    
    public TrainingInstitution selectTrainingInstitution(int trainingInstId);
    
    public int saveTrainingInstitution(int id, String name, int countryId, String city, String pcode, String addrDetails, String phone, Date dateFrom,
            Date dateTo, int[] univIds);
    
    public void deactivateTrainingInstitution(int trainingInstId);

    public List<TrainingInstitution> selectTrainingInstitutions();

    /**
     * * tyrsi institution po partOfName
     * 	 * @param countryId - opredelq dyrjava - ako ne e zadadena - ne u4astva vyv filtriraneto
     * 	 * @param startsWith - dali name-a shte zapo4va s tazi duma ili shte se namira kydeto i da e v imeto na institution-a
     * @return
     */
    public List<Integer> getInstitutionIds(Integer countryId, boolean startsWith, String partOfName);
}
