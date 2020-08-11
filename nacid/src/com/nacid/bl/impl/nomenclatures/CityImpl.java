package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.City;
import com.nacid.data.nomenclatures.CityRecord;

/**
 * Created by georgi.georgiev on 16.09.2015.
 */
public class CityImpl implements City{
    private CityRecord record;
    public CityImpl(CityRecord record) {
        this.record = record;
    }
    @Override
    public int getId() {
        return record.getId();
    }

    @Override
    public String getCode() {
        return record.getCode();
    }

    @Override
    public String getTypeName() {
        return record.getTypeName();
    }

    @Override
    public String getName() {
        return record.getName();
    }

    @Override
    public String getDistrictCode() {
        return record.getDistrictCode();
    }

    @Override
    public String getMunicipalityCode() {
        return record.getMunicipalityCode();
    }

    @Override
    public String getMayoraltyCode() {
        return record.getMayoraltyCode();
    }

    @Override
    public String getTypeCode() {
        return record.getTypeCode();
    }

    @Override
    public String getCategory() {
        return record.getCategory();
    }

    @Override
    public String getAltitude() {
        return record.getAltitude();
    }
}
