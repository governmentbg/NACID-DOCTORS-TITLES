package com.nacid.bl.impl.nomenclatures.regprof;

import com.nacid.bl.impl.nomenclatures.FlatNomenclatureImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.CertificateProfessionalQualification;
import com.nacid.data.nomenclatures.regprof.CertificateProfessionalQualificationRecord;

public class CertificateProfessionalQualificationImpl extends FlatNomenclatureImpl implements CertificateProfessionalQualification {

    public CertificateProfessionalQualificationImpl(CertificateProfessionalQualificationRecord record) {
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    }
    
    @Override
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.FLAT_NOMENCLATURE_CERTIFICATE_PROF_QUALIFICATION;
    }

}
