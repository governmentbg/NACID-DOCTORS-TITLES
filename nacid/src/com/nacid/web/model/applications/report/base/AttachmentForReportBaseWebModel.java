package com.nacid.web.model.applications.report.base;

import com.nacid.bl.applications.Attachment;
import com.nacid.bl.nomenclatures.DocumentType;
/**
 * klas, koito shte se polzva za izbroqvane na prika4enite files svyrzani s reportite - i za vytre6noto i za vyn6noto prilojenie
 * shte se polzva za pokazvane na vsi4ki prika4eni dokumenti nezavisimo ot tipa im
 * @author ggeorgiev
 *
 */
public class AttachmentForReportBaseWebModel {
	private String documentType;
	private int id;
	private String docDescr = "";
	private String fileName = "";
	public AttachmentForReportBaseWebModel(Attachment attachment) {
		this.id = attachment.getId();
		this.docDescr = attachment.getDocDescr();
		this.fileName = attachment.getScannedFileName() == null ? attachment.getFileName() : attachment.getScannedFileName();
		DocumentType documentType = attachment.getDocType();
		this.documentType = documentType == null ? "" : documentType.getName();
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
	public String getDocumentType() {
		return documentType;
	}
	
}
