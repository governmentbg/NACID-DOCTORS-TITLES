package com.nacid.data.applications;

import com.nacid.data.annotations.Column;
import com.nacid.data.annotations.Table;

@Table(name = "app_status_doc_type")
public class AppStatusDocTypeRecord {
	private int id;
	/**
	 * applicationStatusId == 0 ozna4ava vsi4ki applicationStatusIds
	 */
	@Column(name="app_status_id")
	private int applicationStatusId;
	@Column(name="doc_type_id")
	private int documentTypeId;
	
	public AppStatusDocTypeRecord() {
	}
	public AppStatusDocTypeRecord(int id, int applicationStatusId, int documentTypeId) {
		this.id = id;
		this.applicationStatusId = applicationStatusId;
		this.documentTypeId = documentTypeId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getApplicationStatusId() {
		return applicationStatusId;
	}
	public void setApplicationStatusId(int applicationStatusId) {
		this.applicationStatusId = applicationStatusId;
	}
	public int getDocumentTypeId() {
		return documentTypeId;
	}
	public void setDocumentTypeId(int documentTypeId) {
		this.documentTypeId = documentTypeId;
	}
	
}
