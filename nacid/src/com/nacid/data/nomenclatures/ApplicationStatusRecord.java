package com.nacid.data.nomenclatures;

import java.sql.Date;

public class ApplicationStatusRecord extends FlatNomenclatureRecord {
    public ApplicationStatusRecord() {
    }

    public ApplicationStatusRecord(int id, String name, Date dateFrom, Date dateTo) {
        super(id, name, dateFrom, dateTo);
    }
}
