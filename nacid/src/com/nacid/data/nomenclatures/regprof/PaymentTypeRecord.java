package com.nacid.data.nomenclatures.regprof;

import java.sql.Date;

import com.nacid.data.nomenclatures.FlatNomenclatureRecord;

public class PaymentTypeRecord extends FlatNomenclatureRecord {

    public PaymentTypeRecord() {
    }
    public PaymentTypeRecord(int id, String name, Date dateFrom, Date dateTo) {
        super(id, name, dateFrom, dateTo);
    }

}
