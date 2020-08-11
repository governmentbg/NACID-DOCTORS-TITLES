package com.nacid.web.model.applications;

import com.nacid.bl.applications.Company;
import com.nacid.data.DataConverter;

import java.util.Date;

public class CompanyWebModel {

    private final static String DATE_STRING = "дд.мм.гггг";
    
    private int id = 0;
    private String name = "";
    private String city = "";
    private String pcode = "";
    private String addressDetails = "";
    private String phone = "";
    private String dateFrom = DataConverter.formatDate(new Date());
    private String dateTo = DATE_STRING;
    private String eik;
    
    public CompanyWebModel(Company company) {
        if(company == null) {
            return;
        }
        this.id = company.getId();
        this.name = company.getName();
        this.city = company.getCityName();
        this.pcode = company.getPcode();
        this.addressDetails = company.getAddressDetails();
        this.phone = company.getPhone();
        this.dateFrom = DataConverter.formatDate(company.getDateFrom(), DATE_STRING);
        this.dateTo = DataConverter.formatDate(company.getDateTo(), DATE_STRING);
        this.eik = company.getEik();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPcode() {
        return pcode;
    }

    public String getAddressDetails() {
        return addressDetails;
    }

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public String getEik() {
        return eik;
    }
}
