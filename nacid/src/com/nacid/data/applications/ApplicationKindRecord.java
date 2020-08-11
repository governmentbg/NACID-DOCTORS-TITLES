package com.nacid.data.applications;

import com.nacid.data.annotations.Table;

import java.math.BigDecimal;

/**
 * Created by georgi.georgiev on 27.08.2015.
 */
@Table(name = "application_kind")
public class ApplicationKindRecord {
    private Integer id;
    private int applicationId;
    private int entryNumSeriesId;
    private BigDecimal price;
    private String entryNum;
    private Integer finalStatusHistoryId;

    public ApplicationKindRecord() {
    }

    public ApplicationKindRecord(Integer id, int applicationId, int entryNumSeriesId, BigDecimal price, String entryNum, Integer finalStatusHistoryId) {
        this.id = id;
        this.applicationId = applicationId;
        this.entryNumSeriesId = entryNumSeriesId;
        this.price = price;
        this.entryNum = entryNum;
        this.finalStatusHistoryId = finalStatusHistoryId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getEntryNumSeriesId() {
        return entryNumSeriesId;
    }

    public void setEntryNumSeriesId(int entryNumSeriesId) {
        this.entryNumSeriesId = entryNumSeriesId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getEntryNum() {
        return entryNum;
    }

    public void setEntryNum(String entryNum) {
        this.entryNum = entryNum;
    }

    public Integer getFinalStatusHistoryId() {
        return finalStatusHistoryId;
    }

    public void setFinalStatusHistoryId(Integer finalStatusHistoryId) {
        this.finalStatusHistoryId = finalStatusHistoryId;
    }
}
