package com.nacid.bl.impl;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.AppStatusHistory;
import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.bl.nomenclatures.LegalReason;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.data.applications.AppStatusHistoryRecord;

import java.util.Date;

public class AppStatusHistoryImpl implements AppStatusHistory {

    private int id;
    private int applicationId;
    private Date dateAssigned;
    private ApplicationStatus applicationStatus;
    private Integer statLegalReasonId;
    private NacidDataProvider nacidDataProvider;
    public AppStatusHistoryImpl(AppStatusHistoryRecord rec, NacidDataProvider nDP) {
        this.id = rec.getId();
        this.applicationId = rec.getApplicationId();
        this.dateAssigned = rec.getDateAssigned();
        this.statLegalReasonId = rec.getStatLegalReasonId();
        
        NomenclaturesDataProvider nomDP = nDP.getNomenclaturesDataProvider();
        applicationStatus = nomDP.getApplicationStatus(NumgeneratorDataProvider.NACID_SERIES_ID, rec.getStatusId());
        this.nacidDataProvider = nDP;
    }
    
    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getApplicationId() {
        return applicationId;
    }

    @Override
    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    @Override
    public int getApplicationStatusId() {
        return applicationStatus.getId();
    }

    @Override
    public Date getDateAssigned() {
        return dateAssigned;
    }

    @Override
    public Integer getStatLegalReasonId() {
        return statLegalReasonId;
    }
    public LegalReason getStatLegalReason() {
    	return getStatLegalReasonId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getLegalReason(getStatLegalReasonId());
    }

	public String getLongApplicationStatusName() {
		ApplicationStatus status = getApplicationStatus();
		LegalReason legalReason = getStatLegalReason();
		return status.getName() + (legalReason == null ? "" : " на основание " + legalReason.getName());
	}

}
