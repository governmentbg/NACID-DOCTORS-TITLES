package com.nacid.bl.impl.nomenclatures.regprof;

import com.nacid.bl.impl.nomenclatures.FlatNomenclatureImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.SecondarySpeciality;
import com.nacid.data.nomenclatures.regprof.SecondarySpecialityRecord;

public class SecondarySpecialityImpl extends FlatNomenclatureImpl implements SecondarySpeciality {
    
    private Integer professionalQualificationId;
    private Integer qualificationDegreeId;
    private String professionalQualificationName;
    private String qualificationDegreeName;
    private String code;
    
    public SecondarySpecialityImpl(NomenclaturesDataProvider nomenclaturesDataProvider, SecondarySpecialityRecord record) {
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
        this.professionalQualificationId = record.getProfQualificationId();
        this.qualificationDegreeId = record.getQualificationDegreeId();
        this.professionalQualificationName = record.getProfQualificationId() == null ? "" :
            nomenclaturesDataProvider.getSecondaryProfessionalQualification(record.getProfQualificationId()).getName();
        this.qualificationDegreeName = qualificationDegreeId == null ? "" :
                nomenclaturesDataProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION_DEGREE, qualificationDegreeId).getName();
        this.code = record.getCode();
    }

    public Integer getProfessionalQualificationId() {
        return professionalQualificationId;
    }

    public Integer getQualificationDegreeId() {
        return qualificationDegreeId;
    }

    public String getProfessionalQualificationName() {
        return professionalQualificationName;
    }

    public String getQualificationDegreeName() {
        return qualificationDegreeName;
    }

    @Override
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.NOMENCLATURE_SECONDARY_SPECIALITY;
    }

    public String getCode() {
        return code;
    }
    

}
