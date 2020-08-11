package com.nacid.data.nomenclatures;

import java.sql.Date;

/**
 * Created by georgi.georgiev on 14.04.2015.
 */
public class ApplicationStatusRecordExtended extends ApplicationStatusRecord {
    private int isLegal;
    public ApplicationStatusRecordExtended() {
    }

    public ApplicationStatusRecordExtended(int id, String name, Date dateFrom, Date dateTo, int isLegal) {
        super(id, name, dateFrom, dateTo);
        this.isLegal = isLegal;
    }

    public int getIsLegal() {
        return isLegal;
    }

    public void setIsLegal(int isLegal) {
        this.isLegal = isLegal;
    }
}
