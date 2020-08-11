package com.nacid.bl.impl.applications;

import com.nacid.bl.applications.DiplomaTypeIssuer;
import com.nacid.bl.applications.University;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.data.applications.DiplomaTypeIssuerRecord;

public class DiplomaTypeIssuerImpl implements DiplomaTypeIssuer {
	private DiplomaTypeIssuerRecord record;
	private NacidDataProviderImpl nacidDataProvider;
	public DiplomaTypeIssuerImpl(NacidDataProviderImpl nacidDataProvider, DiplomaTypeIssuerRecord record) {
		this.nacidDataProvider = nacidDataProvider;
		this.record = record;
	}
	@Override
	public int getId() {
		return record.getId();
	}
	
	@Override
	public int getDiplomaTypeId() {
		return record.getDiplomaTypeId();
	}
	
	@Override
	public int getUniversityId() {
		return record.getUniversityId();
	}

	@Override
	public Integer getFacultyId() {
		return record.getFacultyId();
	}

	public University getUniversity() {
		return nacidDataProvider.getUniversityDataProvider().getUniversity(getUniversityId());
	}

}
