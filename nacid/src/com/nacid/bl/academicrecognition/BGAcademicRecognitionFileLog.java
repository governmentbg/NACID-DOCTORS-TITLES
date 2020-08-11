package com.nacid.bl.academicrecognition;

import java.util.Date;

public interface BGAcademicRecognitionFileLog {
	public Integer getId();
	public void setId(Integer id);
	public String getFileName();
	public void setFileName(String fileName);
	public Date getUploadDate();
	public void setUploadDate(Date uploadDate);
	public Integer getRecordCount();
	public void setRecordCount(Integer recordCount);
	public Integer getUploadUserId();
	public void setUploadUserId(Integer uploadUserId);
	
}
