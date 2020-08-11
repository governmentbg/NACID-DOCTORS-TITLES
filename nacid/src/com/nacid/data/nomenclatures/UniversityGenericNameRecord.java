package com.nacid.data.nomenclatures;

import java.sql.Date;

/**
 * Created by georgi.georgiev on 15.08.2019
 */
public class UniversityGenericNameRecord extends FlatNomenclatureRecord {
    public UniversityGenericNameRecord() {
    }

    public UniversityGenericNameRecord(int id, String name, Date dateFrom, Date dateTo) {
        super(id, name, dateFrom, dateTo);
    }
}
