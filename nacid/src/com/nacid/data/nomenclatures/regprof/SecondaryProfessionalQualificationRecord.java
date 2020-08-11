package com.nacid.data.nomenclatures.regprof;

import com.nacid.data.annotations.Table;
import com.nacid.data.nomenclatures.FlatNomenclatureRecord;

import java.sql.Date;
//RayaWritten--------------------------------------------
@Table(name="nomenclatures.secondary_prof_qualification")
public class SecondaryProfessionalQualificationRecord extends
        FlatNomenclatureRecord {
    private Integer professionGroupId;
    private String code;
    
    public SecondaryProfessionalQualificationRecord(){}

    public SecondaryProfessionalQualificationRecord(int id, String name,
            Date dateFrom, Date dateTo, Integer professionGroupId,
            String professionGroupName, String code) {
        super(id, name, dateFrom, dateTo);
        this.professionGroupId = professionGroupId;
        this.code = code;
    }
    
    
    public Integer getProfessionGroupId() {
        return professionGroupId;
    }

    public void setProfessionGroupId(Integer professionGroupId) {
        this.professionGroupId = professionGroupId;
    }
    

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String toString() {
        final String tab = "\n\t";
        StringBuilder retValue = new StringBuilder();
        retValue.append(super.toString());
        retValue.append("SecondaryProfessionQualificationRecord ( ")
            .append(tab).append(" professionGroupId = ").append(this.professionGroupId)
            .append("\n )");
        return retValue.toString();
      }
        
}
//---------------------------------------------------------------------
