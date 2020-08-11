package com.nacid.bl.applications;

import com.nacid.bl.impl.applications.UniversityIdWithFacultyId;
import com.nacid.bl.nomenclatures.ApplicationType;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NationalQualificationsFramework;
import com.nacid.bl.nomenclatures.OriginalEducationLevel;

import java.util.*;

public interface DiplomaType {

	public static final int MIGRATED_DIPLOMA_TYPE_ID = 1; //id of the migrated diploma type

	public static final int TYPE_NORMAL = 1;
	public static final int TYPE_DOCTORATE = 2;

	public static Map<Integer, Integer> APPLICATION_TYPE_DIPLOMA_TYPE = new HashMap<Integer, Integer>(){{
		put(ApplicationType.RUDI_APPLICATION_TYPE, TYPE_NORMAL);
		put(ApplicationType.STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE, TYPE_NORMAL);
		put(ApplicationType.DOCTORATE_APPLICATION_TYPE, TYPE_DOCTORATE);
	}};


	//public int getUniversityId();
	//public University getUniversity();
	public List<DiplomaTypeIssuer> getDiplomaTypeIssuers();

	public Set<UniversityIdWithFacultyId> getUniversityWithFacultyIds();

//	public Set<Integer> getDiplomaTypeUniversityIds();
	public String getVisualElementsDescr();
	public String getProtectionElementsDescr();
	public String getNumberFormatDescr();
	public String getNotes();
	public Date getDateFrom();
	public Date getDateTo();
	public int getId();
	public boolean isActive();
	public String getTitle();
	public Integer getEduLevelId();
	public FlatNomenclature getEducationLevel();
	public boolean isJointDegree();
    public Integer getOriginalEducationLevelId();
    public OriginalEducationLevel getOriginalEducationLevel();


    public Integer getBolognaCycleId();
    public Integer getNationalQualificationsFrameworkId();
    public Integer getEuropeanQualificationsFrameworkId();

    public Integer getBolognaCycleAccessId();
    public Integer getNationalQualificationsFrameworkAccessId();
    public Integer getEuropeanQualificationsFrameworkAccessId();

    public FlatNomenclature getBolognaCycle();
    public FlatNomenclature getEuropeanQualificationsFramework();
    public NationalQualificationsFramework getNationalQualificationsFramework();

    public FlatNomenclature getBolognaCycleAccess();
    public FlatNomenclature getEuropeanQualificationsFrameworkAccess();
    public NationalQualificationsFramework getNationalQualificationsFrameworkAccess();

    public int getType();

}
