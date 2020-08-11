package com.nacid.bl.impl.external;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.external.ExtPersonDocument;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.external.applications.ExtPersonRecord;

import java.util.Date;

public class ExtPersonImpl implements ExtPerson {

    private int id;
    private String fname;
    private String sname;
    private String lname;
    private String civilId;
    private Integer civilIdType;
    private Integer birthCountryId;
    private String birthCity;
    private Date birthDate;
    private Integer citizenshipId;
    private String email;
    private String hashCode;
    private Integer userId;
    private NacidDataProvider nacidDataProvider;
    public ExtPersonImpl(ExtPersonRecord rec, NacidDataProvider nacidDataProvider) {
        this.id = rec.getId();
        this.fname = rec.getFname();
        this.sname = rec.getSname();
        this.lname = rec.getLname();
        this.civilId = rec.getCivilId();
        this.civilIdType = rec.getCivilIdType();
        this.birthCountryId = rec.getBirthCountryId();
        this.birthCity = rec.getBirthCity();
        this.birthDate = rec.getBirthDate();
        this.citizenshipId = rec.getCitizenshipId();
        this.email = rec.getEmail();
        this.hashCode = rec.getHashCode();
        this.userId = rec.getUserId();
        this.nacidDataProvider = nacidDataProvider;
    }
    
    public ExtPersonImpl(int id, String fname, String sname, String lname, String civilId, Integer civilIdType, Integer birthCountryId,
            String birthCity, Date birthDate, Integer citizenshipId, 
            String email, String hashCode, Integer userId, NacidDataProvider nacidDataProvider) {
        this.id = id;
        this.fname = fname;
        this.sname = sname;
        this.lname = lname;
        this.civilId = civilId;
        this.civilIdType = civilIdType;
        this.birthCountryId = birthCountryId;
        this.birthCity = birthCity;
        this.birthDate = birthDate;
        this.citizenshipId = citizenshipId;
        this.email = email;
        this.hashCode = hashCode;
        this.userId = userId;
        this.nacidDataProvider = nacidDataProvider;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getFName() {
        return fname;
    }

    @Override
    public String getSName() {
        return sname;
    }

    @Override
    public String getLName() {
        return lname;
    }
    public String getFullName() {
    	return fname + " " + (sname != null && !sname.equals("") ? sname + " " : "") + (lname != null ? lname : "");
    }

    @Override
    public String getCivilId() {
        return civilId;
    }

    @Override
    public Integer getCivilIdTypeId() {
        return civilIdType;
    }
    
    public FlatNomenclature getCivilIdType() {
		return getCivilIdTypeId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, getCivilIdTypeId());
	}

    @Override
    public Integer getBirthCountryId() {
        return birthCountryId;
    }

    @Override
    public String getBirthCity() {
        return birthCity;
    }

    @Override
    public Date getBirthDate() {
        return birthDate;
    }

    @Override
    public Integer getCitizenshipId() {
        return citizenshipId;
    }

    @Override
    public Integer getUserId() {
        return userId;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getHashCode() {
        return hashCode;
    }

	public Country getBirthCountry() {
		return getBirthCountryId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getCountry(getBirthCountryId());
	}

	public Country getCitizenship() {
		return getCitizenshipId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getCountry(getCitizenshipId());
	}
    public ExtPersonDocument getActiveExtPersonDocument() {
        return nacidDataProvider.getExtPersonDataProvider().getExtPersonActiveDocument(getId());
    }

    public void setId(int id) {
        this.id = id;
    }
}
