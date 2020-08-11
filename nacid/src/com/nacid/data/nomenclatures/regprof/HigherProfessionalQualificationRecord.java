package com.nacid.data.nomenclatures.regprof;

import com.nacid.data.annotations.Table;
import com.nacid.data.nomenclatures.FlatNomenclatureRecord;

import java.sql.Date;

@Table(name="nomenclatures.higher_prof_qualification")
public class HigherProfessionalQualificationRecord extends FlatNomenclatureRecord {
    
    public HigherProfessionalQualificationRecord() { }
    
    public HigherProfessionalQualificationRecord(int id, String name, Date dateFrom, Date dateTo) {
        super(id, name, dateFrom, dateTo);
    }
}
