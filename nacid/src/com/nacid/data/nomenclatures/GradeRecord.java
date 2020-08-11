package com.nacid.data.nomenclatures;

import java.sql.Date;

/**
 * Created by georgi.georgiev on 15.10.2015.
 */
public class GradeRecord extends FlatNomenclatureRecord {
    public GradeRecord() {
    }

    public GradeRecord(int id, String name, Date dateFrom, Date dateTo) {
        super(id, name, dateFrom, dateTo);
    }
}
