package com.nacid.data.applications;

import java.sql.Date;

public class DiplomaTypeRecord {

	private int id;
	private String visualElementsDescr;
	private String protectionElementsDescr;
	private String numberFormatDescr;
	private String notes;
	private Date dateFrom;
	private Date dateTo;
	private String title;
	private Integer eduLevelId;
    private Integer originalEduLevelId;
	private int isJointDegree;

    private Integer bolognaCycleId;
    private Integer nationalQualificationsFrameworkId;
    private Integer europeanQualificationsFrameworkId;

    private Integer bolognaCycleAccessId;
    private Integer nationalQualificationsFrameworkAccessId;
    private Integer europeanQualificationsFrameworkAccessId;
    private int type;

	public DiplomaTypeRecord(int id, String visualElementsDescr, String protectionElementsDescr, String numberFormatDescr,
			String notes, Date dateFrom, Date dateTo, String title, Integer eduLevelId, Integer originalEduLevelId, int isJointDegree,
            Integer bolognaCycleId, Integer nationalQualificationsFrameworkId, Integer europeanQualificationsFrameworkId,
            Integer bolognaCycleAccessId, Integer nationalQualificationsFrameworkAccessId, Integer europeanQualificationsFrameworkAccessId, int type) {

		this.id = id;
		this.visualElementsDescr = visualElementsDescr;
		this.protectionElementsDescr = protectionElementsDescr;
		this.numberFormatDescr = numberFormatDescr;
		this.notes = notes;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.title = title;
		this.eduLevelId = eduLevelId;
        this.originalEduLevelId = originalEduLevelId;
		this.isJointDegree = isJointDegree;

        this.bolognaCycleId = bolognaCycleId;
        this.nationalQualificationsFrameworkId = nationalQualificationsFrameworkId;
        this.europeanQualificationsFrameworkId = europeanQualificationsFrameworkId;

        this.bolognaCycleAccessId = bolognaCycleAccessId;
        this.nationalQualificationsFrameworkAccessId = nationalQualificationsFrameworkAccessId;
        this.europeanQualificationsFrameworkAccessId = europeanQualificationsFrameworkAccessId;

        this.type = type;

	}

	

	public DiplomaTypeRecord() {
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setVisualElementsDescr( String visualElementsDescr) {
		this.visualElementsDescr = visualElementsDescr;
	}

	public String getVisualElementsDescr() {
		return visualElementsDescr;
	}

	public void setProtectionElementsDescr( String protectionElementsDescr) {
		this.protectionElementsDescr = protectionElementsDescr;
	}

	public String getProtectionElementsDescr() {
		return protectionElementsDescr;
	}

	public void setNumberFormatDescr( String numberFormatDescr) {
		this.numberFormatDescr = numberFormatDescr;
	}

	public String getNumberFormatDescr() {
		return numberFormatDescr;
	}

	public void setNotes( String notes) {
		this.notes = notes;
	}

	public String getNotes() {
		return notes;
	}

	public void setDateFrom( Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateTo( Date dateTo) {
		this.dateTo = dateTo;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getEduLevelId() {
		return eduLevelId;
	}

	public void setEduLevelId(Integer eduLevelId) {
		this.eduLevelId = eduLevelId;
	}
	public int getIsJointDegree() {
		return isJointDegree;
	}

	public void setIsJointDegree(int isJointDegree) {
		this.isJointDegree = isJointDegree;
	}

    public Integer getOriginalEduLevelId() {
        return originalEduLevelId;
    }

    public void setOriginalEduLevelId(Integer originalEduLevelId) {
        this.originalEduLevelId = originalEduLevelId;
    }


    public Integer getBolognaCycleId() {
        return bolognaCycleId;
    }

    public void setBolognaCycleId(Integer bolognaCycleId) {
        this.bolognaCycleId = bolognaCycleId;
    }

    public Integer getNationalQualificationsFrameworkId() {
        return nationalQualificationsFrameworkId;
    }

    public void setNationalQualificationsFrameworkId(Integer nationalQualificationsFrameworkId) {
        this.nationalQualificationsFrameworkId = nationalQualificationsFrameworkId;
    }

    public Integer getEuropeanQualificationsFrameworkId() {
        return europeanQualificationsFrameworkId;
    }

    public void setEuropeanQualificationsFrameworkId(Integer europeanQualificationsFrameworkId) {
        this.europeanQualificationsFrameworkId = europeanQualificationsFrameworkId;
    }

    public Integer getBolognaCycleAccessId() {
        return bolognaCycleAccessId;
    }

    public void setBolognaCycleAccessId(Integer bolognaCycleAccessId) {
        this.bolognaCycleAccessId = bolognaCycleAccessId;
    }

    public Integer getNationalQualificationsFrameworkAccessId() {
        return nationalQualificationsFrameworkAccessId;
    }

    public void setNationalQualificationsFrameworkAccessId(Integer nationalQualificationsFrameworkAccessId) {
        this.nationalQualificationsFrameworkAccessId = nationalQualificationsFrameworkAccessId;
    }

    public Integer getEuropeanQualificationsFrameworkAccessId() {
        return europeanQualificationsFrameworkAccessId;
    }

    public void setEuropeanQualificationsFrameworkAccessId(Integer europeanQualificationsFrameworkAccessId) {
        this.europeanQualificationsFrameworkAccessId = europeanQualificationsFrameworkAccessId;
    }

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
