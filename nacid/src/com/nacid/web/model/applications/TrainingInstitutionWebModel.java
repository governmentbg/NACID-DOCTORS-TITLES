package com.nacid.web.model.applications;

import java.util.ArrayList;
import java.util.List;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.TrainingInstitution;
import com.nacid.bl.applications.UniversityDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.data.DataConverter;

public class TrainingInstitutionWebModel {
    
    private final static String DATE_FORMAT = "дд.мм.гггг";
    
    private int id = 0;
    private String name = "";
    private String pcode = "";
    private String city = "";
    private String addrDetails = "";
    private String phone = "";
    private String dateFrom = DataConverter.formatDate(Utils.getToday());
    private String dateTo = DATE_FORMAT;
    private String universityIds = "";
    private List<UniversityWebModel> universities = new ArrayList<UniversityWebModel>();
    
    public TrainingInstitutionWebModel(TrainingInstitution trInst, NacidDataProvider nacidDP) {
        
        if(trInst == null) {
            return;
        }
        this.id = trInst.getId();
        this.name = trInst.getName();
        this.pcode = trInst.getPcode();
        this.city = trInst.getCity();
        this.addrDetails = trInst.getAddrDetails();
        this.phone = trInst.getPhone();
        this.dateFrom = DataConverter.formatDate(trInst.getDateFrom(), DATE_FORMAT);
        this.dateTo = DataConverter.formatDate(trInst.getDateTo(), DATE_FORMAT);
        
        UniversityDataProvider uDP = nacidDP.getUniversityDataProvider();
        if(trInst.getUnivIds() != null) {
            String coma = "";
            for(int i : trInst.getUnivIds()) {
                
                UniversityWebModel uwm = new UniversityWebModel(uDP.getUniversity(i));
                universities.add(uwm);
                universityIds += coma + i;
                coma = ";";
            }
        }
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

    public String getCity() {
        return city;
    }

    public String getAddrDetails() {
        return addrDetails;
    }

    public String getPhone() {
        return phone;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public String getUniversityIds() {
        return universityIds;
    }

    public List<UniversityWebModel> getUniversities() {
        return universities;
    }
    
    
}
