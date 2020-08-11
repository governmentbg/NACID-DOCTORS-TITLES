package com.nacid.bl.impl.applications;

import com.nacid.bl.applications.DiplomaType;
import com.nacid.bl.applications.DiplomaTypeIssuer;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NationalQualificationsFramework;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.OriginalEducationLevel;
import com.nacid.data.applications.DiplomaTypeRecord;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DiplomaTypeImpl implements DiplomaType {

	private int id;
	private String visualElementsDescr;
	private String protectionElementsDescr;
	private String numberFormatDescr;
	private String notes;
	private Date dateFrom;
	private Date dateTo;
	private Integer eduLevelId;
	private String title;
	private boolean jointDegree;
    private Integer originalEducationLevelId;

    private Integer bolognaCycleId;
    private Integer nationalQualificationsFrameworkId;
    private Integer europeanQualificationsFrameworkId;

    private Integer bolognaCycleAccessId;
    private Integer nationalQualificationsFrameworkAccessId;
    private Integer europeanQualificationsFrameworkAccessId;


	private NacidDataProviderImpl nacidDataProvider;
	private NomenclaturesDataProvider nomenclaturesDataProvider;
	private int type;
	public DiplomaTypeImpl(NacidDataProviderImpl nacidDataProvider, DiplomaTypeRecord record) {
		this.id = record.getId();
		this.visualElementsDescr = record.getVisualElementsDescr();
		this.protectionElementsDescr = record.getProtectionElementsDescr();
		this.numberFormatDescr = record.getNumberFormatDescr();
		this.notes = record.getNotes();
		this.dateFrom = (Date) record.getDateFrom();
		this.dateTo = (Date) record.getDateTo();
		this.title = record.getTitle();
		this.eduLevelId = record.getEduLevelId();
		this.nacidDataProvider = nacidDataProvider;
        this.originalEducationLevelId = record.getOriginalEduLevelId();
		this.jointDegree = record.getIsJointDegree() == 1 ? true : false;

        this.bolognaCycleId = record.getBolognaCycleId();
        this.nationalQualificationsFrameworkId = record.getNationalQualificationsFrameworkId();
        this.europeanQualificationsFrameworkId = record.getEuropeanQualificationsFrameworkId();


        this.bolognaCycleAccessId = record.getBolognaCycleAccessId();
        this.nationalQualificationsFrameworkAccessId = record.getNationalQualificationsFrameworkAccessId();
        this.europeanQualificationsFrameworkAccessId = record.getEuropeanQualificationsFrameworkAccessId();

        nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        this.type = record.getType();
	}

	@Override
	public int getId() {
		return id;
	}

	public List<DiplomaTypeIssuer> getDiplomaTypeIssuers() {
		return nacidDataProvider.getDiplomaTypeDataProvider().getDiplomaTypeIssuers(getId());
	}


	public Set<UniversityIdWithFacultyId> getUniversityWithFacultyIds() {
		List<DiplomaTypeIssuer> issuers = getDiplomaTypeIssuers();
		if (issuers == null) {
			return null;
		}
		Set<UniversityIdWithFacultyId> result = new LinkedHashSet<>();
		for (DiplomaTypeIssuer i:issuers) {
			result.add(new UniversityIdWithFacultyId(i.getUniversityId(), i.getFacultyId()));
		}
		return result;
 	}
	@Override
	public String getVisualElementsDescr() {
		return visualElementsDescr;
	}

	@Override
	public String getProtectionElementsDescr() {
		return protectionElementsDescr;
	}

	@Override
	public String getNumberFormatDescr() {
		return numberFormatDescr;
	}

	@Override
	public String getNotes() {
		return notes;
	}

	@Override
	public Date getDateFrom() {
		return dateFrom;
	}

	@Override
	public Date getDateTo() {
		return dateTo;
	}
	
	@Override
	public String getTitle() {
		return title;
	}
	
	@Override
	public Integer getEduLevelId() {
		return eduLevelId;
	}
	public FlatNomenclature getEducationLevel() {
	    return getEduLevelId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, getEduLevelId());
	}
	
	@Override
	public boolean isActive() {
	    return Utils.isRecordActive(dateFrom, dateTo);
	}

	public boolean isJointDegree() {
		return jointDegree;
	}

    public Integer getOriginalEducationLevelId() {
        return originalEducationLevelId;
    }
    public OriginalEducationLevel getOriginalEducationLevel() {
        return getOriginalEducationLevelId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getOriginalEducationLevel(getOriginalEducationLevelId());
    }


    public Integer getBolognaCycleId() {
        return bolognaCycleId;
    }

    public Integer getNationalQualificationsFrameworkId() {
        return nationalQualificationsFrameworkId;
    }

    public Integer getEuropeanQualificationsFrameworkId() {
        return europeanQualificationsFrameworkId;
    }

    public Integer getBolognaCycleAccessId() {
        return bolognaCycleAccessId;
    }

    public Integer getNationalQualificationsFrameworkAccessId() {
        return nationalQualificationsFrameworkAccessId;
    }

    public Integer getEuropeanQualificationsFrameworkAccessId() {
        return europeanQualificationsFrameworkAccessId;
    }

    public FlatNomenclature getBolognaCycle() {
        return bolognaCycleId == null ? null : nomenclaturesDataProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_BOLOGNA_CYCLE, getBolognaCycleId());
    }


    public FlatNomenclature getEuropeanQualificationsFramework() {
        return europeanQualificationsFrameworkId == null ? null : nomenclaturesDataProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EUROPEAN_QUALIFICATIONS_FRAMEWORK, europeanQualificationsFrameworkId);
    }

    public NationalQualificationsFramework getNationalQualificationsFramework() {
        return nationalQualificationsFrameworkId == null ? null : nomenclaturesDataProvider.getNationalQualificationsFramework(nationalQualificationsFrameworkId);
    }



    public FlatNomenclature getBolognaCycleAccess() {
        return bolognaCycleAccessId == null ? null : nomenclaturesDataProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_BOLOGNA_CYCLE, bolognaCycleAccessId);
    }


    public FlatNomenclature getEuropeanQualificationsFrameworkAccess() {
        return europeanQualificationsFrameworkAccessId == null ? null : nomenclaturesDataProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EUROPEAN_QUALIFICATIONS_FRAMEWORK, europeanQualificationsFrameworkAccessId);
    }

    public NationalQualificationsFramework getNationalQualificationsFrameworkAccess() {
        return nationalQualificationsFrameworkAccessId == null ? null : nomenclaturesDataProvider.getNationalQualificationsFramework(nationalQualificationsFrameworkAccessId);
    }

	public int getType() {
		return type;
	}
}
