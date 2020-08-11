package com.nacid.data.nomenclatures;

import java.sql.Date;

/**
 * Created by georgi.georgiev on 14.04.2015.
 */
public class ApplicationDocflowStatusRecord extends FlatNomenclatureRecord {
    public ApplicationDocflowStatusRecord() {
    }

    public ApplicationDocflowStatusRecord(int id, String name, Date dateFrom, Date dateTo) {
        super(id, name, dateFrom, dateTo);
    }
}
