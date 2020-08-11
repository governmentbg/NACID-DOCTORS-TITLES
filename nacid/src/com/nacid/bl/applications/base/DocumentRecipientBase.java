package com.nacid.bl.applications.base;

import com.nacid.bl.nomenclatures.Country;

/**
 * User: ggeorgiev
 * Date: 4.10.2019 г.
 * Time: 14:26
 */
public interface DocumentRecipientBase {
    public int getId();

    public int getApplicationId();

    public Country getCountry();

    public int getCountryId();

    public String getName();

    public String getCity();

    public String getDistrict();

    public String getPostCode();

    public String getAddress();

    public String getMobilePhone();
}
