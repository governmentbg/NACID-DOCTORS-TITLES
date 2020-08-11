package com.nacid.data.nomenclatures;

import java.sql.Date;

/**
 * Created by georgi.georgiev on 04.08.2015.
 */
public class EuropeanQualificationsFrameworkRecord extends FlatNomenclatureRecord {
    public EuropeanQualificationsFrameworkRecord() {
    }
    public EuropeanQualificationsFrameworkRecord(int id, String name, Date dateFrom, Date dateTo) {
        super(id, name, dateFrom, dateTo);
    }
}
