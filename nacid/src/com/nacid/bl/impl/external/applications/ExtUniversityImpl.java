package com.nacid.bl.impl.external.applications;

import java.util.Date;

import com.nacid.bl.applications.University;
import com.nacid.bl.external.applications.ExtUniversity;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.data.external.applications.ExtDiplomaIssuerRecord;

public class ExtUniversityImpl implements ExtUniversity {

	//private NacidDataProvider nacidDataProvider;
	//private UniversityRecord record;
	private ExtDiplomaIssuerRecord extDiplomaIssuer;
	private University university;
	
	ExtUniversityImpl(University university, ExtDiplomaIssuerRecord extDiplomaIssuer) {
		this.extDiplomaIssuer = extDiplomaIssuer;
		this.university = university;
	}

	public String getUniversityTxt() {
		return isStandartUniversity() ? null : extDiplomaIssuer.getUniversityTxt();
	}

	public boolean isStandartUniversity() {
		return extDiplomaIssuer.getUniversityId() != null;
	}

	public String getAddrDetails() {
		return isStandartUniversity() ? university.getAddrDetails() : null;
	}

	public String getBgName() {
		return isStandartUniversity() ? university.getBgName() : null;
	}

	public String getCity() {
		return isStandartUniversity() ? university.getCity(): null;
	}

	public Country getCountry() {
		return isStandartUniversity() ? university.getCountry() : null;
	}

	public int getCountryId() {
		return isStandartUniversity() ? university.getCountryId() : 0;
	}

	public Date getDateFrom() {
		return isStandartUniversity() ? university.getDateFrom() : null;
	}

	
	public Date getDateTo() {
		return isStandartUniversity() ? university.getDateTo() : null;
	}
	
	public String getEmail() {
		return isStandartUniversity() ? university.getEmail() : null;
	}
	
	public String getFax() {
		return isStandartUniversity() ? university.getFax() : null;
	}
	
	public int getId() {
		return isStandartUniversity() ? university.getId() : 0;
	}

	
	public String getOrgName() {
		return isStandartUniversity() ? university.getOrgName() : null;
	}

	
	public String getPhone() {
		return isStandartUniversity() ? university.getPhone() : null;
	}

	
	public String getUrlDiplomaRegister() {
		return isStandartUniversity() ? university.getUrlDiplomaRegister() : null;
	}

	
	public String getWebSite() {
		return isStandartUniversity() ? university.getWebSite() : null;
	}

	
	public boolean isActive() {
		return isStandartUniversity() ? university.isActive() : false;
	}

	@Override
	public Integer getGenericNameId() {
		return isStandartUniversity() ? university.getGenericNameId() : null;
	}

	@Override
	public FlatNomenclature getGenericName() {
		return isStandartUniversity() ? university.getGenericName() : null;
	}
}
