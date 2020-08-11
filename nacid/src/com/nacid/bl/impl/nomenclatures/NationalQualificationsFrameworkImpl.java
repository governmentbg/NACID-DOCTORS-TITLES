package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.NationalQualificationsFramework;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.NationalQualificationsFrameworkRecord;

/**
 * Created by georgi.georgiev on 04.08.2015.
 */
public class NationalQualificationsFrameworkImpl extends FlatNomenclatureImpl implements NationalQualificationsFramework {
    private int countryId;
    public NationalQualificationsFrameworkImpl(NationalQualificationsFrameworkRecord rec) {
        super(rec.getId(), rec.getName(), rec.getDateFrom(), rec.getDateTo());
        this.countryId = rec.getCountryId();
    }

    @Override
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.NOMENCLATURE_NATIONAL_QUALIFICATIONS_FRAMEWORK;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
