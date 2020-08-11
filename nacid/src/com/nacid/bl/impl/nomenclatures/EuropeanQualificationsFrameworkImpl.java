package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.EuropeanQualificationsFramework;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.EuropeanQualificationsFrameworkRecord;

/**
 * Created by georgi.georgiev on 04.08.2015.
 */
public class EuropeanQualificationsFrameworkImpl extends FlatNomenclatureImpl implements EuropeanQualificationsFramework {
    public EuropeanQualificationsFrameworkImpl(EuropeanQualificationsFrameworkRecord rec) {
        super(rec.getId(), rec.getName(), rec.getDateFrom(), rec.getDateTo());

    }
    @Override
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.FLAT_NOMENCLATURE_EUROPEAN_QUALIFICATIONS_FRAMEWORK;
    }
}
