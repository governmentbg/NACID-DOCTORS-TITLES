package com.ext.nacid.web.model.applications;

public class ExtApplicationAttachmentWebModel {

	private int id;
	private int applicationId;
	private String docDescr = "";
	private String fileName = "";

	public ExtApplicationAttachmentWebModel(int applicationId) {
		this.applicationId = applicationId;
	}
	
	public ExtApplicationAttachmentWebModel(int id, int applicationId, 
			String docDescr, String fileName) {
		this.id = id;
		this.applicationId = applicationId;
		this.docDescr = docDescr;
		this.fileName = fileName;
	}

	public int getId() {
		return id;
	}

	public String getDocDescr() {
		return docDescr;
	}
	
	public String getFileName() {
		return fileName;
	}

	public int getApplicationId() {
		return applicationId;
	}
	
	
}
