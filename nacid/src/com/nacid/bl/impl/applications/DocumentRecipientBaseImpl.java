package com.nacid.bl.impl.applications;

import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.applications.DocumentRecipientRecord;

public abstract class DocumentRecipientBaseImpl {
    private DocumentRecipientRecord record;
    private NomenclaturesDataProvider nomenclaturesDataProvider;

    public DocumentRecipientBaseImpl(DocumentRecipientRecord record, NomenclaturesDataProvider nomenclaturesDataProvider) {
        this.record = record;
        this.nomenclaturesDataProvider = nomenclaturesDataProvider;
    }

    public int getId() {
        return record.getId();
    }

    public int getApplicationId() {
        return record.getApplicationId();
    }

    public Country getCountry() {
        return nomenclaturesDataProvider.getCountry(record.getCountryId());
    }

    public int getCountryId() {
        return record.getCountryId();
    }

    public String getName() {
        return record.getName();
    }

    public String getCity() {
        return record.getCity();
    }

    public String getDistrict() {
        return record.getDistrict();
    }

    public String getPostCode() {
        return record.getPostCode();
    }

    public String getAddress() {
        return record.getAddress();
    }

    public String getMobilePhone() {
        return record.getMobilePhone();
    }
}
