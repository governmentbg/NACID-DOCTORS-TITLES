package com.nacid.data.nomenclatures.regprof;

import com.nacid.data.annotations.Table;
import com.nacid.data.nomenclatures.FlatNomenclatureRecord;

import java.sql.Date;

@Table(name="nomenclatures.profession_experience_document_type")
public class ProfessionExperienceDocumentTypeRecord extends FlatNomenclatureRecord {
    private int forExperienceCalculation;
    public ProfessionExperienceDocumentTypeRecord() {
    }

    public ProfessionExperienceDocumentTypeRecord(int id, String name, Date dateFrom, Date dateTo, int forExperienceCalculation) {
        super(id, name, dateFrom, dateTo);
        this.forExperienceCalculation = forExperienceCalculation;
    }

    public int getForExperienceCalculation() {
        return forExperienceCalculation;
    }

    public void setForExperienceCalculation(int forExperienceCalculation) {
        this.forExperienceCalculation = forExperienceCalculation;
    }
    
}
