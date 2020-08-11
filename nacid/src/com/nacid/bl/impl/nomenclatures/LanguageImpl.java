package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.Language;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.LanguageRecord;

public class LanguageImpl extends FlatNomenclatureImpl implements Language {
    private String iso639Code;

    public LanguageImpl(LanguageRecord record) {
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
        this.iso639Code = record.getIso639Code();
    }

    public int getNomenclatureType() {
        return NomenclaturesDataProvider.NOMENCLATURE_LANGUAGE;
    }

    @Override
    public String getIso639Code() {
        return iso639Code;
    }
}
