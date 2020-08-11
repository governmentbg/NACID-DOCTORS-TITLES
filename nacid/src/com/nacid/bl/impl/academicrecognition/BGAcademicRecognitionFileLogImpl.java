package com.nacid.bl.impl.academicrecognition;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.nacid.bl.academicrecognition.BGAcademicRecognitionFileLog;
import com.nacid.data.annotations.Table;

@Table(name="bg_academic_recognition_file_log")
public class BGAcademicRecognitionFileLogImpl implements BGAcademicRecognitionFileLog {
	private Integer id;
	private String fileName;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
	private Date uploadDate;
	private Integer recordCount;
	private Integer  uploadUserId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public Integer getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}
	public Integer getUploadUserId() {
		return uploadUserId;
	}
	public void setUploadUserId(Integer uploadUserId) {
		this.uploadUserId = uploadUserId;
	}
	
	
}
