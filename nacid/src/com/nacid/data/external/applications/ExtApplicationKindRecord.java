package com.nacid.data.external.applications;

import com.nacid.data.annotations.Table;

import java.math.BigDecimal;

/**
 * Created by georgi.georgiev on 27.08.2015.
 */
@Table(name = "eservices.rudi_application_kind")
public class ExtApplicationKindRecord {
    private Integer id;
    private int applicationId;
    private int entryNumSeriesId;
    private BigDecimal price;
    private String entryNum;

    public ExtApplicationKindRecord() {
    }

    public ExtApplicationKindRecord(Integer id, int applicationId, int entryNumSeriesId) {
        this.id = id;
        this.applicationId = applicationId;
        this.entryNumSeriesId = entryNumSeriesId;
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
}
