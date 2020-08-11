package com.ext.nacid.web.model.applications;

import com.nacid.bl.external.applications.ExtESignedInformation;
import com.nacid.data.DataConverter;

public class ExtESignedInformationWebModel {
	//private String signedXmlContent;
	private String issuer;
	private String name;
	private String civilId;
	private String unifiedIdCode;
	private String email;
	private String validityFrom;
	private String validityTo;
	public ExtESignedInformationWebModel(ExtESignedInformation info) {
		//this.signedXmlContent = info.getSignedXmlContent();
		this.issuer = info.getIssuer();
		this.name = info.getName();
		this.civilId = info.getCivilId();
		this.unifiedIdCode = info.getUnifiedIdCode();
		this.email = info.getEmail();
		this.validityFrom = DataConverter.formatDateTime(info.getValidityFrom(), true);
		this.validityTo = DataConverter.formatDateTime(info.getValidityTo(), true);
	}
	public String getIssuer() {
		return issuer;
	}
	public String getName() {
		return name;
	}
	public String getCivilId() {
		return civilId;
	}
	public String getUnifiedIdCode() {
		return unifiedIdCode;
	}
	public String getEmail() {
		return email;
	}
	public String getValidityFrom() {
		return validityFrom;
	}
	public String getValidityTo() {
		return validityTo;
	}
	
}
