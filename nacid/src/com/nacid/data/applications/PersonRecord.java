package com.nacid.data.applications;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.nacid.data.annotations.Table;
@Table(name="person")
public class PersonRecord {
    //RayaChanged from int to Integer------------
    private Integer id;
    //-------------------------------------------
    private String fName;
    private String sName;
    private String lName;
    private String civilId;
    private Integer civilIdType;
    private Integer birthCountryId;
    private String birthCity;
    @DateTimeFormat(pattern="dd.MM.yyyy")
    private Date birthDate;
    private Integer citizenshipId;
    public PersonRecord() {
    }
    
    public PersonRecord(int id, String fName, String sName, String lName, String civilId, Integer civilIdType, Integer birthCountryId, String birthCity,
			Date birthDate, Integer citizenshipId) {
		this.id = id;
		this.fName = fName;
		this.sName = sName;
		this.lName = lName;
		this.civilId = civilId;
		this.civilIdType = civilIdType;
		this.birthCountryId = birthCountryId;
		this.birthCity = birthCity;
		this.birthDate = birthDate;
		this.citizenshipId = citizenshipId;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFName() {
		return fName;
	}
	public void setFName(String fName) {
		this.fName = fName;
	}
	public String getSName() {
		return sName;
	}
	public void setSName(String sName) {
		this.sName = sName;
	}
	public String getLName() {
		return lName;
	}
	public void setLName(String lName) {
		this.lName = lName;
	}
	public String getCivilId() {
		return civilId;
	}
	public void setCivilId(String civilId) {
		this.civilId = civilId;
	}
	public Integer getCivilIdType() {
		return civilIdType;
	}
	public void setCivilIdType(Integer civilIdType) {
		this.civilIdType = civilIdType;
	}
	public Integer getBirthCountryId() {
		return birthCountryId;
	}
	public void setBirthCountryId(Integer birthCountryId) {
		this.birthCountryId = birthCountryId;
	}
	public String getBirthCity() {
		return birthCity;
	}
	public void setBirthCity(String birthCity) {
		this.birthCity = birthCity;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public Integer getCitizenshipId() {
		return citizenshipId;
	}
	public void setCitizenshipId(Integer citizenshipId) {
		this.citizenshipId = citizenshipId;
	}
	public String getFullName() {
        return (StringUtils.isEmpty(getFName()) ? "" : getFName() + " ") + (StringUtils.isEmpty(getSName()) ? "" : getSName() + " ") + (StringUtils.isEmpty(getLName()) ? "" : getLName());
    }

    public String getfName() {
        return getFName();
    }

    public void setfName(String fName) {
        setFName(fName);
    }

    public String getsName() {
        return getSName();
    }

    public void setsName(String sName) {
        setSName(sName);
    }

    public String getlName() {
        return getLName();
    }

    public void setlName(String lName) {
        setLName(lName);
    }

    @Override
    public String toString() {
        return "PersonRecord [birthCity=" + birthCity + ", birthCountryId="
                + birthCountryId + ", birthDate=" + birthDate
                + ", citizenshipId=" + citizenshipId + ", civilId=" + civilId
                + ", civilIdType=" + civilIdType + ", fName=" + fName + ", id="
                + id + ", lName=" + lName + ", sName=" + sName + "]";
    }

}
