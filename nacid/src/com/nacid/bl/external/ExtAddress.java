package com.nacid.bl.external;

import com.nacid.bl.nomenclatures.Country;

/**
 * Created by georgi.georgiev on 18.09.2015.
 */
public interface ExtAddress {
    int getId();

    String getEmail();

    String getAddress();

    String getPostalCode();

    String getPhone();

    String getFax();

    Integer getCountryId();

    public Country getCountry();

    Integer getCityId();

    public com.nacid.bl.nomenclatures.GraoCity getCity();

    String getForeignCity();

    String getPostalBox();
}
