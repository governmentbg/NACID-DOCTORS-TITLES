package com.nacid.data.applications;

import java.io.InputStream;

public class DiplomaTypeAttachmentRecord {

	private int id;
	private int diplomaTypeId;
	private String docDescr;
	private int docTypeId;
	private InputStream contentStream;
	private String fileName;
	private String contentType;

	public DiplomaTypeAttachmentRecord(int id, int diplomaTypeId, String docDescr, int docTypeId, InputStream contentStream, String fileName, String contentType) {

		this.id = id;
		this.diplomaTypeId = diplomaTypeId;
		this.docDescr = docDescr;
		this.docTypeId = docTypeId;
		this.contentStream = contentStream;
		this.fileName = fileName;
		this.contentType = contentType;
	}

	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setDiplomaTypeId(int diplomaTypeId) {
		this.diplomaTypeId = diplomaTypeId;
	}

	public int getDiplomaTypeId() {
		return diplomaTypeId;
	}

	public void setDocDescr(String docDescr) {
		this.docDescr = docDescr;
	}

	public String getDocDescr() {
		return docDescr;
	}

	public void setDocTypeId(int docTypeId) {
		this.docTypeId = docTypeId;
	}

	public int getDocTypeId() {
		return docTypeId;
	}

	public void setContentStream(InputStream contentStream) {
		this.contentStream = contentStream;
	}

	public InputStream getContentStream() {
		return contentStream;
	}


	public String getContentType() {
		return contentType;
	}


	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	
}
