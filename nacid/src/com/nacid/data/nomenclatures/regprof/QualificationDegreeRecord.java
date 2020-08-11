package com.nacid.data.nomenclatures.regprof;

import java.sql.Date;

import com.nacid.data.annotations.Table;
import com.nacid.data.nomenclatures.FlatNomenclatureRecord;

//RayaWritten------------------------------
public class QualificationDegreeRecord extends FlatNomenclatureRecord{
    
    public QualificationDegreeRecord(){}
    
    public QualificationDegreeRecord(int id, String name, Date dateFrom, Date dateTo) {
      super(id, name, dateFrom, dateTo);
    }
}
//-------------------------------------------
