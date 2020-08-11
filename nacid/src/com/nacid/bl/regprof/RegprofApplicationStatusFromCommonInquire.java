package com.nacid.bl.regprof;

import java.util.Date;

public class RegprofApplicationStatusFromCommonInquire  {
    private boolean onlyActualStatus;
    private Integer applicationStatusId;
    private Date applicationStatusDateFrom;
    private Date applicationStatusDateTo;

    private Integer finalApplicationStatusId;
    private Date finalApplicationStatusDateFrom;
    private Date finalApplicationStatusDateTo;

    private Integer docflowStatusId;
    private Date docflowStatusDateFrom;
    private Date docflowStatusDateTo;

    public RegprofApplicationStatusFromCommonInquire(boolean onlyActualStatus, Integer applicationStatus, Date applicationStatusDateFrom, Date applicationStatusDateTo, Integer finalApplicationStatus, Date finalApplicationStatusDateFrom, Date finalApplicationStatusDateTo, Integer docflowStatusId, Date docflowStatusDateFrom, Date docflowStatusDateTo) {
        this.onlyActualStatus = onlyActualStatus;
        this.applicationStatusId = applicationStatus;
        this.applicationStatusDateFrom = applicationStatusDateFrom;
        this.applicationStatusDateTo = applicationStatusDateTo;
        this.finalApplicationStatusId = finalApplicationStatus;
        this.finalApplicationStatusDateFrom = finalApplicationStatusDateFrom;
        this.finalApplicationStatusDateTo = finalApplicationStatusDateTo;
        this.docflowStatusId = docflowStatusId;
        this.docflowStatusDateFrom = docflowStatusDateFrom;
        this.docflowStatusDateTo = docflowStatusDateTo;
    }

    public boolean isOnlyActualStatus() {
        return onlyActualStatus;
    }

    public Integer getApplicationStatusId() {
        return applicationStatusId;
    }

    public Date getApplicationStatusDateFrom() {
        return applicationStatusDateFrom;
    }

    public Date getApplicationStatusDateTo() {
        return applicationStatusDateTo;
    }

    public Integer getFinalApplicationStatusId() {
        return finalApplicationStatusId;
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

    public Date getDocflowStatusDateFrom() {
        return docflowStatusDateFrom;
    }

    public Date getDocflowStatusDateTo() {
        return docflowStatusDateTo;
    }
}
