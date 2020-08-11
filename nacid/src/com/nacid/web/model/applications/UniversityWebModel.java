package com.nacid.web.model.applications;

import com.nacid.bl.applications.University;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.data.DataConverter;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

public class UniversityWebModel {

	protected String id = "";
	protected int intId;
	protected String bgName = "";
	protected String orgName = "";
	protected String city = "";
	protected String addrDetails = "";
	protected String phone = "";
	protected String fax = "";
	protected String email = "";
	protected String webSite = "";
	protected String urlDiplomaRegister = "";
	protected String dateFrom = "";
	protected String dateTo = "";
	protected String countryId;
	protected String country;
	protected String inactive;
	protected String genericName;
	protected String genericNameId;
	protected boolean isNew = false;

	public UniversityWebModel(University university) {
		if(university == null) {
			dateFrom = DataConverter.formatDate(new Date());
			dateTo = "дд.мм.гггг";
			isNew = true;
			return;
		}
		
		id = university.getId() + "";
		intId = university.getId();
		bgName = university.getBgName();
		orgName = university.getOrgName();
		city = university.getCity();
		addrDetails = university.getAddrDetails();
		phone = university.getPhone();
		fax = university.getFax();
		email = university.getEmail();
		webSite = university.getWebSite();
		urlDiplomaRegister = university.getUrlDiplomaRegister();
		countryId = university.getCountryId() + "";
		dateFrom = DataConverter.formatDate(university.getDateFrom(), "дд.мм.гггг");
		dateTo = DataConverter.formatDate(university.getDateTo(), "дд.мм.гггг");
		Country c = university.getCountry();
		country = c == null ? "" : c.getName();
		this.genericName = university.getGenericName() == null ? "" : university.getGenericName().getName();
		this.genericNameId = university.getGenericNameId() == null ? "" : university.getGenericNameId() + "";
		inactive = university.isActive() ? "" : " (inactive)";
	}

	public String getId() {
		return id;
	}

	public String getBgName() {
		return bgName;
	}

	public String getOrgName() {
		return orgName;
	}

	public String getCity() {
		return city;
	}

	public String getAddrDetails() {
		return addrDetails;
	}

	public String getPhone() {
		return phone;
	}

	public String getFax() {
		return fax;
	}

	public String getEmail() {
		return email;
	}

	public String getUrlDiplomaRegister() {
		return urlDiplomaRegister;
	}

	public static String getUrlDiplomaRegisterLink(String url) {
		if (!StringUtils.isEmpty(url)) {
			return Arrays.stream(url.split("[\\r\\n]+")).map(r -> "<a href=\"" + r + "\" target=\"_blank\">Отвори страница</a>").collect(Collectors.joining("<br />"));
		} else {
			return null;
		}
	}
	public String getUrlDiplomaRegisterLink() {
		return getUrlDiplomaRegisterLink(getUrlDiplomaRegister());
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public String getWebSite() {
		return webSite;
	}

	public String getCountryId() {
		return countryId;
	}

	public String getCountry() {
		return country;
	}

	public String getInactive() {
		return inactive;
	}

	public boolean isNew() {
		return isNew;
	}

	public int getIntId() {
		return intId;
	}

	public String getGenericName() {
		return genericName;
	}

	public String getGenericNameId() {
		return genericNameId;
	}
}
