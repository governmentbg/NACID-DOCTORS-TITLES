package com.nacid.data.nomenclatures.regprof;

import com.nacid.data.annotations.Table;
import com.nacid.data.nomenclatures.FlatNomenclatureRecord;

import java.sql.Date;


@Table(name="nomenclatures.higher_speciality")
public class HigherSpecialityRecord extends FlatNomenclatureRecord {

    public HigherSpecialityRecord() { }
    
    public HigherSpecialityRecord(int id, String name, Date dateFrom, Date dateTo) {
        super(id, name, dateFrom, dateTo);
  }

}
