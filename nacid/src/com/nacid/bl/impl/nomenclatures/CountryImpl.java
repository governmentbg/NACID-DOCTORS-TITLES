package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.CountryRecord;

public class CountryImpl extends FlatNomenclatureImpl implements Country {
    private String iso3166Code;
    private String officialName;

    public CountryImpl(CountryRecord record) {
        super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
        this.iso3166Code = record.getIso3166Code();
        this.officialName = record.getOfficialName();
    }
    public String getIso3166Code() {
        return iso3166Code;
    }
    public int getNomenclatureType() {
        return NomenclaturesDataProvider.NOMENCLATURE_COUNTRY;
    }
    public String getOfficialName() {
        return officialName;
    }

}
