package com.nacid.data.nomenclatures.regprof;

import java.sql.Date;

import com.nacid.data.annotations.Table;
import com.nacid.data.nomenclatures.FlatNomenclatureRecord;

public class SecondarySpecialityRecord extends FlatNomenclatureRecord {
    
    private Integer profQualificationId;
    private Integer qualificationDegreeId;
    private String code;
        
    public SecondarySpecialityRecord()  { }

    public SecondarySpecialityRecord(int id, String name, Integer profQualificationId, Integer qualificationDegreeId, Date dateFrom, Date dateTo, String code) {
        super(id, name, dateFrom, dateTo);
        this.profQualificationId = profQualificationId;
        this.qualificationDegreeId = qualificationDegreeId;
        this.code = code;
    }

    public Integer getProfQualificationId() {
        return profQualificationId;
    }

    public Integer getQualificationDegreeId() {
        return qualificationDegreeId;
    }

    public void setProfQualificationId(Integer profQualificationId) {
        this.profQualificationId = profQualificationId;
    }

    public void setQualificationDegreeId(Integer qualificationDegreeId) {
        this.qualificationDegreeId = qualificationDegreeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    

}
