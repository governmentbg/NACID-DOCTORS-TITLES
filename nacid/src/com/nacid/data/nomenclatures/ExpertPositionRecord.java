package com.nacid.data.nomenclatures;

import java.sql.Date;

public class ExpertPositionRecord extends FlatNomenclatureRecord {

    private Integer relatedAppStatusId;

    public ExpertPositionRecord() {
    }

    public ExpertPositionRecord(int id, String name, Date dateFrom, Date dateTo, Integer relatedAppStatusId) {
        super(id, name, dateFrom, dateTo);
        this.relatedAppStatusId = relatedAppStatusId;
    }

    public Integer getRelatedAppStatusId() {
        return relatedAppStatusId;
    }

    public void setRelatedAppStatusId(Integer relatedAppStatusId) {
        this.relatedAppStatusId = relatedAppStatusId;
    }
}
