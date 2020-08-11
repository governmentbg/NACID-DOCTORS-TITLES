package com.nacid.bl.impl.external;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.external.ExtAddress;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.GraoCity;
import com.nacid.data.external.applications.ExtAddressRecord;

/**
 * Created by georgi.georgiev on 18.09.2015.
 */
public class ExtAddressImpl implements ExtAddress {
    private NacidDataProvider nacidDataProvider;
    private ExtAddressRecord record;

    public ExtAddressImpl(NacidDataProvider nacidDataProvider, ExtAddressRecord record) {
        this.nacidDataProvider = nacidDataProvider;
        this.record = record;
    }

    public int getId() {
        return record.getId();
    }

    public String getEmail() {
        return record.getEmail();
    }

    public String getAddress() {
        return record.getAddress();
    }

    public String getPostalCode() {
        return record.getPostalCode();
    }

    public String getPhone() {
        return record.getPhone();
    }

    public String getFax() {
        return record.getFax();
    }

    public Integer getCountryId() {
        return record.getCountryId();
    }

    public Country getCountry() {
        return record.getCountryId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getCountry(getCountryId());
    }

    public Integer getCityId() {
        return record.getCityId();
    }

    public GraoCity getCity() {
        return record.getCityId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getGraoCity(getCityId());
    }

    public String getForeignCity() {
        return record.getForeignCity();
    }
    public String getPostalBox() {
        return record.getPostalBox();
    }
}
