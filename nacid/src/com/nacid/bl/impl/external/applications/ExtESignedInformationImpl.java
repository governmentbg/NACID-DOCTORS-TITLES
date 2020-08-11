package com.nacid.bl.impl.external.applications;

import java.util.Date;

import com.nacid.bl.external.applications.ExtESignedInformation;
import com.nacid.data.external.applications.ExtESignedInformationRecord;

public class ExtESignedInformationImpl implements ExtESignedInformation {
	ExtESignedInformationRecord record;
	public ExtESignedInformationImpl(ExtESignedInformationRecord record) {
		this.record = record;
	}
	public String getSignedXmlContent() {
		return record.getSignedXmlContent();
	}
	
	
	public String getIssuer() {
		return record.getIssuer();
	}
	public String getName() {
		return record.getName();
	}
	public String getEmail() {
		return record.getEmail();
	}
	public String getCivilId() {
		return record.getCivilId();
	}
	public String getUnifiedIdCode() {
		return record.getUnifiedIdCode();
	}
	public Date getValidityFrom() {
		return record.getValidityFrom();
	}
	public Date getValidityTo() {
		return record.getValidityTo();
	}
	
}
