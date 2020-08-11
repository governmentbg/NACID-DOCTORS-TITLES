package com.nacid.data.nomenclatures;

import java.sql.Date;

/**
 * Created by georgi.georgiev on 15.10.2015.
 */
public class AgeRangeRecord extends FlatNomenclatureRecord {
    public AgeRangeRecord() {
    }

    public AgeRangeRecord(int id, String name, Date dateFrom, Date dateTo) {
        super(id, name, dateFrom, dateTo);
    }
}
