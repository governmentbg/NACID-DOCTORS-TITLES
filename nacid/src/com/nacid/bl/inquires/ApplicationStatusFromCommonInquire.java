package com.nacid.bl.inquires;

import java.util.Date;

public class ApplicationStatusFromCommonInquire {
	private boolean onlyActualStatus;
	private ApplicationStatusAndLegalReasons applicationStatus;

	private Date applicationStatusDateFrom;
	private Date applicationStatusDateTo;
	private ApplicationStatusAndLegalReasons finalApplicationStatus;
	private Date finalApplicationStatusDateFrom;
	private Date finalApplicationStatusDateTo;
    private Integer docflowStatusId;
    private Date docflowStatusDateFrom;
    private Date docflowStatusDateTo;
	public ApplicationStatusFromCommonInquire(boolean onlyActualStatus, ApplicationStatusAndLegalReasons applicationStatus, Date applicationStatusDateFrom, Date applicationStatusDateTo,
			ApplicationStatusAndLegalReasons finalApplicationStatus, Date finalStatusDateFrom, Date finalStatusDateTo, Integer docflowStatusId, Date docflowStatusDateFrom, Date docflowStatusDateTo) {
		this.onlyActualStatus = onlyActualStatus;
		this.applicationStatus = applicationStatus;
		this.finalApplicationStatus = finalApplicationStatus;
		this.finalApplicationStatusDateFrom = finalStatusDateFrom;
		this.finalApplicationStatusDateTo = finalStatusDateTo;
        this.docflowStatusId = docflowStatusId;
        this.applicationStatusDateFrom = applicationStatusDateFrom;
        this.applicationStatusDateTo = applicationStatusDateTo;
        this.docflowStatusDateFrom = docflowStatusDateFrom;
        this.docflowStatusDateTo = docflowStatusDateTo;
	}
	public ApplicationStatusAndLegalReasons getApplicationStatus() {
		return applicationStatus;
	}
	public ApplicationStatusAndLegalReasons getFinalApplicationStatus() {
		return finalApplicationStatus;
	}
	public Date getFinalApplicationStatusDateFrom() {
		return finalApplicationStatusDateFrom;
	}
	public Date getFinalApplicationStatusDateTo() {
		return finalApplicationStatusDateTo;
	}

    public Integer getDocflowStatusId() {
        return docflowStatusId;
    }

	public Date getApplicationStatusDateFrom() {
		return applicationStatusDateFrom;
	}

	public Date getApplicationStatusDateTo() {
		return applicationStatusDateTo;
	}

	public Date getDocflowStatusDateFrom() {
		return docflowStatusDateFrom;
	}

	public Date getDocflowStatusDateTo() {
		return docflowStatusDateTo;
	}

	public boolean isOnlyActualStatus() {
		return onlyActualStatus;
	}
}
