package com.nacid.web.model.applications;

import com.nacid.bl.applications.AppStatusHistory;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.data.DataConverter;

public class AppStatusHistoryWebModel {

    private String statusName;
    private String dateAssigned;
    private Integer legalReasonId;
    private String legalReason;
    private String longApplicationStatusName;
    private boolean isLegalStatus;
    public AppStatusHistoryWebModel(AppStatusHistory appStatusHistory) {

        this.statusName = appStatusHistory.getApplicationStatus().getName();
        this.dateAssigned = DataConverter.formatDate(appStatusHistory.getDateAssigned());
        this.legalReasonId = appStatusHistory.getStatLegalReasonId();
        FlatNomenclature legalReason = appStatusHistory.getStatLegalReason();
        this.legalReason = legalReason == null ? "" : legalReason.getName();
        this.longApplicationStatusName = appStatusHistory.getLongApplicationStatusName();
        this.isLegalStatus = appStatusHistory.getApplicationStatus().isLegal();
    }

    public String getStatusName() {
        return statusName;
    }

    public String getDateAssigned() {
        return dateAssigned;
    }

    public Integer getLegalReasonId() {
        return legalReasonId;
    }

	public String getLegalReason() {
//		System.out.println("getting legal reason........>" + legalReason + "<");
		return legalReason;
	}

	public String getLongApplicationStatusName() {
		return longApplicationStatusName;
	}

    public boolean isLegalStatus() {
        return isLegalStatus;
    }
}
