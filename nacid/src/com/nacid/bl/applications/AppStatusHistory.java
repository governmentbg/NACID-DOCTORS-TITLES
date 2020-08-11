package com.nacid.bl.applications;

import java.util.Date;

import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.bl.nomenclatures.LegalReason;

public interface AppStatusHistory {

    public int getId();
    public int getApplicationId();
    public ApplicationStatus getApplicationStatus();
    public int getApplicationStatusId();
    public Date getDateAssigned();
    public Integer getStatLegalReasonId();
    public LegalReason getStatLegalReason();
    /**
     * vry6ta obedinenie mejdu applictionStatus i legalReason kato ime 
     * primerno отказано на основание чл.12....
     * @return
     */
    public String getLongApplicationStatusName();
    
}
