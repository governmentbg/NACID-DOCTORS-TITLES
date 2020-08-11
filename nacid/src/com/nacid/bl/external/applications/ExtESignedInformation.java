package com.nacid.bl.external.applications;

import java.util.Date;

public interface ExtESignedInformation {
	public String getSignedXmlContent();
	public String getIssuer();
	public String getName();
	public String getEmail();
	public String getCivilId();
	public String getUnifiedIdCode();
	public Date getValidityFrom();
	public Date getValidityTo();
}
