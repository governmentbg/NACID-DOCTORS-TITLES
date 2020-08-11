package com.nacid.data.external.applications;

import java.io.InputStream;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = {
    "id",
    "docDescr",
    "contentStream",
    "contentType",
    "fileName"
})
@XmlRootElement(name = "AttachedDocument", namespace="http://ereg.egov.bg/segment/0009-000139")
public class ExtApplicationAttachmentRecord {
    
    @XmlElement(name = "AttachedDocumentUniqueIdentifier", namespace="http://ereg.egov.bg/value/0008-000134")
    private int id;
	private int applicationId;
	
	@XmlElement(name = "AttachedDocumentDescription", namespace="http://ereg.egov.bg/value/0008-000133")
	private String docDescr;
	private int docTypeId;
	@XmlElement(name = "FileType", namespace="http://ereg.egov.bg/value/0008-000041")
	private String contentType;
	@XmlElement(name = "AttachedDocumentFileName", namespace="http://ereg.egov.bg/value/0008-000135")
	private String fileName;
	@XmlElement(name = "AttachedDocumentFileContent", namespace="http://ereg.egov.bg/value/0008-000132")
	private InputStream contentStream;
	private int copyTypeId;
	public ExtApplicationAttachmentRecord(){}
	public ExtApplicationAttachmentRecord(int id, int applicationId, String docDescr, int docTypeId, String contentType, String fileName,
			InputStream contentStream, int copyTypeId) {

		this.id = id;
		this.applicationId = applicationId;
		this.docDescr = docDescr;
		this.docTypeId = docTypeId;
		this.contentType = contentType;
		this.fileName = fileName;
		this.contentStream = contentStream;
		this.copyTypeId = copyTypeId;
	}

	
    public int getCopyTypeId() {
		return copyTypeId;
	}

	public void setCopyTypeId(int copyTypeId) {
		this.copyTypeId = copyTypeId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	public int getApplicationId() {
		return applicationId;
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

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setContentStream(InputStream contentStream) {
		this.contentStream = contentStream;
	}

	public InputStream getContentStream() {
		return contentStream;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + applicationId;
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + copyTypeId;
		result = prime * result + ((docDescr == null) ? 0 : docDescr.hashCode());
		result = prime * result + docTypeId;
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExtApplicationAttachmentRecord other = (ExtApplicationAttachmentRecord) obj;
		if (applicationId != other.applicationId)
			return false;
		if (contentType == null) {
			if (other.contentType != null)
				return false;
		} else if (!contentType.equals(other.contentType))
			return false;
		if (copyTypeId != other.copyTypeId)
			return false;
		if (docDescr == null) {
			if (other.docDescr != null)
				return false;
		} else if (!docDescr.equals(other.docDescr))
			return false;
		if (docTypeId != other.docTypeId)
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
}
