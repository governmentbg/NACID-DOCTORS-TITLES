package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.OriginalEducationLevel;
import com.nacid.data.nomenclatures.OriginalEducationLevelRecord;

/**
 * Created by georgi.georgiev on 30.04.2015.
 */
public class OriginalEducationLevelImpl extends FlatNomenclatureImpl implements OriginalEducationLevel {

    private int educationLevelId;
    private int countryId;
    private String nameTranslated;

    public OriginalEducationLevelImpl(OriginalEducationLevelRecord record) {
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
        this.educationLevelId = record.getEduLevelId();
        this.countryId = record.getCountryId();
        this.nameTranslated = record.getNameTranslated();
    }

    @Override
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.NOMENCLATURE_ORIGINAL_EDUCATION_LEVEL;
    }

    public int getEducationLevelId() {
        return educationLevelId;
    }

    public int getCountryId() {
        return countryId;
    }

    public String getNameTranslated() {
        return nameTranslated;
    }
}
