package com.nacid.bl.nomenclatures.regprof;

import com.nacid.bl.nomenclatures.FlatNomenclature;

//RayaWritten------------------------------------------------------
public interface SecondarySpeciality extends FlatNomenclature{
    public Integer getQualificationDegreeId();
    public String getQualificationDegreeName();
    public Integer getProfessionalQualificationId();
    public String getProfessionalQualificationName();
    public String getCode();
}
//----------------------------------------------------------
