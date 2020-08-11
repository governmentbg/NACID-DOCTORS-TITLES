package com.nacid.data.nomenclatures.regprof;

import java.sql.Date;

import com.nacid.data.nomenclatures.FlatNomenclatureRecord;

//RayaWritten-----------------------------------------------------------------------------
public class ProfessionalInstitutionTypeRecord extends FlatNomenclatureRecord{
    
    public ProfessionalInstitutionTypeRecord() {
    }
    
    public ProfessionalInstitutionTypeRecord(int id, String name, Date dateFrom, Date dateTo) {
      super(id, name, dateFrom, dateTo);
    }
}
//-------------------------------------------------------------------------------------------