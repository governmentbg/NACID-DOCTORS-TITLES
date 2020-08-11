package com.nacid.data.applications;

import java.sql.Date;


public class UniversityRecord {

	private int id;
	private int countryId;
	private String bgName;
	private String orgName;
	private String city;
	private String addrDetails;
	private String phone;
	private String fax;
	private String email;
	private String webSite;
	private String urlDiplomaRegister;
	private Date dateFrom;
	private Date dateTo;
	private Integer universityGenericNameId;

	public UniversityRecord(int id, int countryId, String bgName, String orgName, String city, String addrDetails, String phone, String fax,
			String email, String webSite, String urlDiplomaRegister, Date dateFrom, Date dateTo, Integer universityGenericNameId) {

		this.id = id;
		this.countryId = countryId;
		this.bgName = bgName;
		this.orgName = orgName;
		this.city = city;
		this.addrDetails = addrDetails;
		this.phone = phone;
		this.fax = fax;
		this.email = email;
		this.webSite = webSite;
		this.urlDiplomaRegister = urlDiplomaRegister;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.universityGenericNameId = universityGenericNameId;
	}

	public UniversityRecord() {
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setCountryId( int countryId) {
		this.countryId = countryId;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setBgName( String bgName) {
		this.bgName = bgName;
	}

	public String getBgName() {
		return bgName;
	}

	public void setOrgName( String orgName) {
		this.orgName = orgName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setCity( String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setAddrDetails( String addrDetails) {
		this.addrDetails = addrDetails;
	}

	public String getAddrDetails() {
		return addrDetails;
	}

	public void setPhone( String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setFax( String fax) {
		this.fax = fax;
	}

	public String getFax() {
		return fax;
	}

	public void setEmail( String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setWebSite( String webSite) {
		this.webSite = webSite;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setUrlDiplomaRegister( String urlDiplomaRegister) {
		this.urlDiplomaRegister = urlDiplomaRegister;
	}

	public String getUrlDiplomaRegister() {
		return urlDiplomaRegister;
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

	public Integer getUniversityGenericNameId() {
		return universityGenericNameId;
	}

	public void setUniversityGenericNameId(Integer universityGenericNameId) {
		this.universityGenericNameId = universityGenericNameId;
	}
}
