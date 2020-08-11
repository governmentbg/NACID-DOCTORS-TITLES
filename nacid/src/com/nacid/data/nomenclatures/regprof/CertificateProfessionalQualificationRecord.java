package com.nacid.data.nomenclatures.regprof;

import java.sql.Date;

import com.nacid.data.nomenclatures.FlatNomenclatureRecord;

public class CertificateProfessionalQualificationRecord extends FlatNomenclatureRecord {
    
    public CertificateProfessionalQualificationRecord() { }
    
    public CertificateProfessionalQualificationRecord(int id, String name, Date dateFrom, Date dateTo) {
        super(id, name, dateFrom, dateTo);
    }
}
