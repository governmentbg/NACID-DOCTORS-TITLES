package com.nacid.bl.applications;

import com.nacid.bl.impl.applications.UniversityIdWithFacultyId;
import com.nacid.data.applications.DiplomaTypeRecordForList;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface DiplomaTypeDataProvider {

	public DiplomaType getDiplomaType(int id);
	public List<DiplomaTypeRecordForList> getAllDiplomaTypes(Integer type);
	//public List<DiplomaType> getDiplomaTypes(int universityId);
	/**
	 * ako universityIds == null, togava universityIds ne u4astvat vyv filtriraneto. 
	 * Ako universityIds.size() == 0, togava vry6ta null
	 * inache vry6ta DiplomaTypes za spisyka ot universityIds
	 */
	public List<DiplomaType> getDiplomaTypes(Collection<Integer> universityIds, Integer diplomaType);
	/**
	 * @throws DiplomaTypeEditException - tozi exception se hvyrlq pri opit za promqna na eduLevelId na sy6testvuva6t diplomaType, koito e vyrzan kym daden trainingCourse
	 * s DiplomaTypeEditException.getMessage() moje da se vzeme syob6tenieto s greshkata, koqto da se izvede na potrebitelq   
	 */
	public int saveDiplomaType(int id, Set<UniversityIdWithFacultyId> universityWithFaculty, String visualElementsDescr, String protectionElementsDescr, String numberFormatDescr,
							   String notes, Date dateFrom, Date dateTo, String title, Integer eduLevelId, Integer originalEduLevelId, boolean jointDegree,
							   Integer bolognaCycleId, Integer nationalQualificationsFrameworkId, Integer europeanQualificationsFrameworkId,
							   Integer bolognaCycleAccessId, Integer nationalQualificationsFrameworkAccessId, Integer europeanQualificationsFrameworkAccessId,
							   int type
	) throws DiplomaTypeEditException;
	public void disableDiplomaType(int id);



	/**
	 * promenq dateTo-to na vsi4ki diplomaTypes po zadaden universityId - t.e. kogato se promeni dateTo na daden universitet
	 * trqbva da se obhodqt vsi4ki diplomaTypes i da se napravqt neaktivni ako sa bili aktivni kym dadeniq dateTo
	 * Primerno - ako imame universitet koito e napraven neaktiven kym 12.12.2010, no imame diplomaType, prika4ena kym tozi universitet, 
	 * koqto e validna do 1.1.2011, togava i dateTo-to na diplomaType-a trqbva da stane 12.12.2010!, no ako diplomaType.dateTo e bil 10.12.2010, to toi ne se promenq
	 * 
	 * @param universityId
	 * @param dateTo
	 * @return vsi4ki DiplomaTypes na koito e promenena dateTo-to!
	 */
	public List<DiplomaType> changeDiplomaTypeDateToByUniversity(int universityId, Date dateTo);
	
}
