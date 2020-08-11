package com.nacid.bl.impl.applications;

import java.sql.Date;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.TrainingInstitution;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.data.applications.TrainingInstitutionRecord;

public class TrainingInstitutionImpl implements TrainingInstitution {

    private int id;
    private String name;
    private int countryId;
    private String city;
    private String pcode;
    private String addrDetails;
    private String phone;
    private Date dateFrom;
    private Date dateTo;
    private int[] univIds;
    private NacidDataProvider nacidDataProvider;
    
    public TrainingInstitutionImpl(int id, String name, int countryId, String city, String pcode, String addrDetails, String phone, Date dateFrom,
            Date dateTo, int[] univIds, NacidDataProvider nacidDataProvider) {
        this.id = id;
        this.name = name;
        this.countryId = countryId;
        this.city = city;
        this.pcode = pcode;
        this.addrDetails = addrDetails;
        this.phone = phone;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.univIds = univIds;
        this.nacidDataProvider = nacidDataProvider;
    }
    
    public TrainingInstitutionImpl(TrainingInstitutionRecord rec, NacidDataProvider nacidDataProvider) {
        this.id = rec.getId();
        this.name = rec.getName();
        this.countryId = rec.getCountryId();
        this.city = rec.getCity();
        this.pcode = rec.getPcode();
        this.addrDetails = rec.getAddrDetails();
        this.phone = rec.getPhone();
        this.dateFrom = rec.getDateFrom();
        this.dateTo = rec.getDateTo();
        this.univIds = rec.getUnivIds();
        this.nacidDataProvider = nacidDataProvider;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getCountryId() {
        return countryId;
    }
    public Country getCountry() {
    	return nacidDataProvider.getNomenclaturesDataProvider().getCountry(getCountryId());
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public String getPcode() {
        return pcode;
    }

    @Override
    public String getAddrDetails() {
        return addrDetails;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public Date getDateFrom() {
        return dateFrom;
    }

    @Override
    public Date getDateTo() {
        return dateTo;
    }

    @Override
    public int[] getUnivIds() {
        return univIds;
    }
    public boolean isActive() {
    	return Utils.isRecordActive(dateFrom, dateTo);
    }
    
    
}
