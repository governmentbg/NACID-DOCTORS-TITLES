package com.nacid.bl.applications;

import com.nacid.bl.impl.applications.UniversityWithFaculty;

import java.util.Date;
import java.util.List;

public interface UniversityDataProvider {
  public static final int NAME_TYPE_BG = 1;
  public static final int NAME_TYPE_ORIGINAL = 2;
	public University getUniversity(int id);
	public List<University> getAllUniversities();
	
	public int saveUniversity(int id, int countryId, String bgName, String orgName, String city, String addrDetails, String phone, String fax,
			String email, String webSite, String urlDiplomaRegister, Date dateFrom, Date dateTo, Integer universityGenericNameId);
	public void disableUniversity(int id);
	/**
	 * vry6ta universitetite podredeni po Order num
	 * @param diplomaId - trainingCourseId
	 * @return
	 */
	public List<UniversityWithFaculty> getUniversityWithFaculties(int diplomaId);
	
	/**
	 * tyrsi universitet po partOfName
	 * @param countryId - opredelq dyrjava - ako ne e zadadena - ne u4astva vyv filtriraneto
	 * @param nameType - opredelq dali partOfName e BG name ili Original name - vyzmojnite stojnosti sa edin ot NAME_TYPE_BG, NAME_TYPE_ORIGINAL ili 0. Pri 0, ne u4astva vyv filtriraneto
	 * @param startsWith - dali name-a shte zapo4va s tazi duma ili shte se namira kydeto i da e v imeto na universiteta
	 * @param partOfName
	 * @return
	 */
	public List<University> getUniversities(Integer countryId, int nameType, boolean startsWith, String partOfName);
	/**
	 * @see UniversityDataProvider#getUniversityIds(Integer, int, boolean, String)
	 * Razlikata e samo 4e vry6ta samo id-tata
	 * @return
	 */
	public List<Integer> getUniversityIds(Integer countryId, int nameType, boolean startsWith, String partOfName);
	/**
	 * 
	 * @param countryId - ako countryId == 0, ne u4astva vyv filtriraneto
	 * @param onlyActive - ako e true, vry6ta samo aktivnite universiteti
	 * @return List<University> po zadadenite kriterii - countryId-onlyActive, sortirani po "bg name"
	 */
	public List<University> getUniversities(int countryId, boolean onlyActive);


	public UniversityFaculty getUniversityFaculty(int id);
	public List<UniversityFaculty> getUniversityFaculties(int universityId, String partOfName, boolean onlyActive);
	public int saveUniveristyFaculty(int id, int universityId, String name, String originalName, Date dateFrom, Date dateTo);
	
	
}
