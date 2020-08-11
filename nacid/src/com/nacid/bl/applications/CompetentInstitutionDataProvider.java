package com.nacid.bl.applications;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface CompetentInstitutionDataProvider {
    public int saveCompetentInstitution(int id, int countryId, String name, String originalName, String phone, String fax, String email, String addressDetails, String url, String notes,  Date dateFrom, Date dateTo);
    public CompetentInstitution getCompetentInstitution(int institutionId);
    /**
     * 
     * @param countryId - null ne u4astva vyv filtriraneto - inache samo instituciite na dadena dyrjava
     * @param onlyActive - true - samo aktivnite; false - vsi4ki
     * @return
     */
    public List<CompetentInstitution> getCompetentInstitutions(Integer countryId, boolean onlyActive);
    
    public List<CompetentInstitution> getCompetentInstitutions(Collection<Integer> countryIds, boolean onlyActive);
    /**
     * delete competent institution slaga dateTo na dneshna data!
     */
    public void deleteCompetentInstitution(int institutionId);
    
    public List<CompetentInstitution> getCompetentInstitutionsByUniversityValidityId(int validityId, boolean onlyActive);
}
