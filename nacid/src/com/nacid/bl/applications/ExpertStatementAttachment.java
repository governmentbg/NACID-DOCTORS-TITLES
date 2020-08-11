package com.nacid.bl.applications;

import java.io.InputStream;

public interface ExpertStatementAttachment {

	public int getId();

	public int getApplicationId();
	
	public int getExpertId();

	public int getDocTypeId();

	public String getDocDescr();

	public InputStream getContentStream();

	public String getFileName();

	public String getContentType();
	
}
