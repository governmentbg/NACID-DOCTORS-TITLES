package com.nacid.bl.impl.nomenclatures.regprof;

import com.nacid.bl.impl.nomenclatures.FlatNomenclatureImpl;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.regprof.ProfessionExperience;
import com.nacid.data.nomenclatures.regprof.ProfessionExperienceRecord;

public class ProfessionExperienceImpl extends FlatNomenclatureImpl implements ProfessionExperience {

    public ProfessionExperienceImpl(ProfessionExperienceRecord record) {
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    }
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE;
    }

}
