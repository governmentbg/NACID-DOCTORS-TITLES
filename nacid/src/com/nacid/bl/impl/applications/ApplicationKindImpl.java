package com.nacid.bl.impl.applications;

import com.nacid.bl.applications.ApplicationKind;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.data.applications.ApplicationKindRecord;

import java.math.BigDecimal;

/**
 * Created by georgi.georgiev on 23.09.2015.
 */
public class ApplicationKindImpl implements ApplicationKind {
    private ApplicationKindRecord record;
    private NacidDataProviderImpl nacidDataProvider;
    private ApplicationStatus finalStatus;
    private boolean finalStatusInitialized;
    public ApplicationKindImpl(NacidDataProviderImpl nacidDataProvider, ApplicationKindRecord record) {
        this.record = record;
        this.nacidDataProvider = nacidDataProvider;
    }
    public Integer getId() {
        return record.getId();
    }
    public int getApplicationId() {
        return record.getApplicationId();
    }
    public int getEntryNumSeriesId() {
        return record.getEntryNumSeriesId();
    }
    public String getEntryNumSeriesName() {
        return nacidDataProvider.getNumgeneratorDataProvider().getEntryNumSeriesNameById(record.getEntryNumSeriesId());
    }
    public BigDecimal getPrice() {
        return record.getPrice();
    }
    public String getEntryNum() {
        return record.getEntryNum();
    }

    public com.nacid.bl.nomenclatures.ApplicationStatus getFinalStatus() {
        if (!finalStatusInitialized) {
            finalStatus = record.getFinalStatusHistoryId() == null ? null : nacidDataProvider.getApplicationsDataProvider().getApplicationStatusByAppStatusHistoryId(record.getFinalStatusHistoryId());
            finalStatusInitialized = true;
        }
        return finalStatus;
    }
}
