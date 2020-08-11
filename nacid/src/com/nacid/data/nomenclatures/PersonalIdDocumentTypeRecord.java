package com.nacid.data.nomenclatures;

import java.sql.Date;

/**
 * Created by georgi.georgiev on 23.10.2018
 */
public class PersonalIdDocumentTypeRecord extends FlatNomenclatureRecord {
    public PersonalIdDocumentTypeRecord() {
    }

    public PersonalIdDocumentTypeRecord(int id, String name, Date dateFrom, Date dateTo) {
        super(id, name, dateFrom, dateTo);
    }
}
