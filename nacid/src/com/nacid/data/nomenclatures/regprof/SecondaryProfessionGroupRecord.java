package com.nacid.data.nomenclatures.regprof;

import java.sql.Date;

import com.nacid.data.nomenclatures.FlatNomenclatureRecord;

//RayaWritten--------------------------------------------------
public class SecondaryProfessionGroupRecord extends FlatNomenclatureRecord{
    private String code;
    public SecondaryProfessionGroupRecord() {
        super();
    }

    public SecondaryProfessionGroupRecord(int id, String name, Date dateFrom,
            Date dateTo, String code) {
        super(id, name, dateFrom, dateTo);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    
}
//---------------------------------------------------