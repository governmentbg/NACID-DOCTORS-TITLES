package com.nacid.bl.impl.applications;

import java.io.InputStream;
import java.util.Date;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.data.applications.DiplomaTypeAttachmentRecord;

public class DiplomaTypeAttachmentImpl implements Attachment {

	private int id;
	private int diplomaTypeId;
	private String docDescr;
	private int docTypeId;
	private InputStream contentStream;
	private String fileName;
	private String contentType;
	private NacidDataProvider nacidDataProvider;
	DiplomaTypeAttachmentImpl(int id, int diplomaTypeId, String docDescr, int docTypeId, InputStream contentStream, String fileName, String contentType, NacidDataProvider nacidDataProvider) {

		this.id = id;
		this.diplomaTypeId = diplomaTypeId;
		this.docDescr = docDescr;
		this.docTypeId = docTypeId;
		this.contentStream = contentStream;
		this.fileName = fileName;
		this.contentType = contentType;
		this.nacidDataProvider = nacidDataProvider;
	}
	
	DiplomaTypeAttachmentImpl(DiplomaTypeAttachmentRecord rec, NacidDataProvider nacidDataProvider) {

		this.id = rec.getId();
		this.diplomaTypeId = rec.getDiplomaTypeId();
		this.docDescr = rec.getDocDescr();
		this.docTypeId = rec.getDocTypeId();
		this.contentStream = rec.getContentStream();
		this.fileName = rec.getFileName();
		this.contentType = rec.getContentType();
		this.nacidDataProvider = nacidDataProvider;
	}

	@Override
	public String getFileName() {
		return fileName;
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public int getParentId() {
		return diplomaTypeId;
	}
	
	public DocumentType getDocType() {
		return nacidDataProvider.getNomenclaturesDataProvider().getDocumentType(getDocTypeId());
	}
	
	@Override
	public String getDocDescr() {
		return docDescr;
	}

	@Override
	public int getDocTypeId() {
		return docTypeId;
	}

	@Override
	public InputStream getContentStream() {
		return contentStream;
	}
	
	@Override
	public String getContentType() {
		return contentType;
	}

    @Override
    public String getScannedContentType() {
        return null;
    }

    @Override
    public String getScannedFileName() {
        return null;
    }

    @Override
    public InputStream getScannedContentStream() {
        return null;
    }

    @Override
    public Date getDocflowDate() {
        return null;
    }

    @Override
    public int getCopyTypeId() {
        return 0;
    }

    @Override
    public String getDocflowId() {
        return null;
    }

    @Override
    public String getDocflowNum() {
        return null;
    }
}
