package com.nacid.data.nomenclatures;

import java.sql.Date;

public class DocumentTypeRecord extends FlatNomenclatureRecord{
	private int isIncoming;
	private int hasDocflowId;
	private String documentTemplate;
	public DocumentTypeRecord(){
	}
	public DocumentTypeRecord(int id, String name, Date dateFrom, Date dateTo, String documentTemplate, int isIncoming, int hasDocflowId) {
		super(id, name, dateFrom, dateTo);
		this.isIncoming = isIncoming;
		this.hasDocflowId = hasDocflowId;
		this.documentTemplate = documentTemplate;
	}
	public int getIsIncoming() {
		return isIncoming;
	}
	public void setIsIncoming(int isIncoming) {
		this.isIncoming = isIncoming;
	}
	public int getHasDocflowId() {
		return hasDocflowId;
	}
	public void setHasDocflowId(int hasDocflowId) {
		this.hasDocflowId = hasDocflowId;
	}
	public String getDocumentTemplate() {
		return documentTemplate;
	}
	public void setDocumentTemplate(String documentTemplate) {
		this.documentTemplate = documentTemplate;
	}
}
