package com.nacid.data.nomenclatures;

import java.sql.Date;

/**
 * Created by georgi.georgiev on 15.10.2015.
 */
public class SchoolTypeRecord extends FlatNomenclatureRecord {
    public SchoolTypeRecord() {
    }

    public SchoolTypeRecord(int id, String name, Date dateFrom, Date dateTo) {
        super(id, name, dateFrom, dateTo);
    }
}
