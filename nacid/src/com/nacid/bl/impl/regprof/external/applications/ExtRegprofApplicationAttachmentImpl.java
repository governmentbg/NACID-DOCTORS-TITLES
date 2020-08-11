package com.nacid.bl.impl.regprof.external.applications;

import java.io.InputStream;
import java.util.Date;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.data.external.applications.ExtApplicationAttachmentRecord;

public class ExtRegprofApplicationAttachmentImpl implements Attachment {

	private int id;
	private int applicationId;
	private String docDescr;
	private int docTypeId;
	private String contentType;
	private String fileName;
	private InputStream contentStream;
	private int copyTypeId;
	private NacidDataProvider nacidDataProvider;

	
	
	ExtRegprofApplicationAttachmentImpl(ExtApplicationAttachmentRecord rec, NacidDataProvider nacidDataProvider) {

		this.id = rec.getId();
		this.applicationId = rec.getApplicationId();
		this.docDescr = rec.getDocDescr();
		this.docTypeId = rec.getDocTypeId();
		this.contentType = rec.getContentType();
		this.fileName = rec.getFileName();
		this.contentStream = rec.getContentStream();
		this.copyTypeId = rec.getCopyTypeId();
		this.nacidDataProvider = nacidDataProvider;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public int getParentId() {
		return applicationId;
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
	public String getContentType() {
		return contentType;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public InputStream getContentStream() {
		return contentStream;
	}

	@Override
	public int getCopyTypeId() {
		return copyTypeId;
	}

	
	public DocumentType getDocType() {
		return nacidDataProvider.getNomenclaturesDataProvider().getDocumentType(getDocTypeId());
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
    public String getDocflowId() {
        return null;
    }

    @Override
    public String getDocflowNum() {
        return null;
    }
	
	
}
