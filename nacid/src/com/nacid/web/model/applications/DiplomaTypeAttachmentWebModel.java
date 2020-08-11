package com.nacid.web.model.applications;



public class DiplomaTypeAttachmentWebModel extends AttachmentBaseWebModel{

	private int diplomaTypeId;
	private String diplomaTypeTitle = "";
	
	public DiplomaTypeAttachmentWebModel(int diplomaTypeId, String diplomaTypeTitle) {
		this.diplomaTypeId = diplomaTypeId;
		this.diplomaTypeTitle = diplomaTypeTitle;
	}
	
	public DiplomaTypeAttachmentWebModel(int id, int diplomaTypeId, 
			String docDescr, String diplomaTypeTitle, String fileName, int docType) {
		super(id, docDescr, fileName, null, null, docType, null);
		this.diplomaTypeId = diplomaTypeId;
		this.diplomaTypeTitle = diplomaTypeTitle;
	}
	public int getDiplomaTypeId() {
		return diplomaTypeId;
	}
	public String getDiplomaTypeTitle() {
		return diplomaTypeTitle;
	}
}
