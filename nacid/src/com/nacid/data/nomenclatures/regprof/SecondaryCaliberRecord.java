package com.nacid.data.nomenclatures.regprof;

import java.sql.Date;

import com.nacid.data.nomenclatures.FlatNomenclatureRecord;
//RayaWritten-----------------------------------------------------
public class SecondaryCaliberRecord extends FlatNomenclatureRecord {

    public SecondaryCaliberRecord() {
        super();
    }

    public SecondaryCaliberRecord(int id, String name, Date dateFrom,
            Date dateTo) {
        super(id, name, dateFrom, dateTo);
    }
    
}
//-----------------------------------------------------------------