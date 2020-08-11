package com.nacid.data.external.applications;

import java.sql.Timestamp;

public class ExtESignedInformationRecord {
	private int id;
	private int userId;
	private int applicationId;
	private String signedXmlContent;
	private String issuer;
	private String name;
	private String email;
	private String civilId;
	private String unifiedIdCode;
	private Timestamp validityFrom;
	private Timestamp validityTo;
	
	public ExtESignedInformationRecord() {
	}
	
	public ExtESignedInformationRecord(int id, int userId, int applicationId, String signedXmlContent) {
		this.id = id;
		this.userId = userId;
		this.applicationId = applicationId;
		this.signedXmlContent = signedXmlContent;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}
	public String getSignedXmlContent() {
		return signedXmlContent;
	}
	public void setSignedXmlContent(String signedXmlContent) {
		this.signedXmlContent = signedXmlContent;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCivilId() {
		return civilId;
	}
	public void setCivilId(String civilId) {
		this.civilId = civilId;
	}
	public String getUnifiedIdCode() {
		return unifiedIdCode;
	}
	public void setUnifiedIdCode(String unifiedIdCode) {
		this.unifiedIdCode = unifiedIdCode;
	}
	public Timestamp getValidityFrom() {
		return validityFrom;
	}
	public void setValidityFrom(Timestamp validityFrom) {
		this.validityFrom = validityFrom;
	}
	public Timestamp getValidityTo() {
		return validityTo;
	}
	public void setValidityTo(Timestamp validityTo) {
		this.validityTo = validityTo;
	}
	
	
}
