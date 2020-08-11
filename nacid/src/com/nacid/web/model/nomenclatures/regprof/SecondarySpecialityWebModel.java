package com.nacid.web.model.nomenclatures.regprof;

import java.util.Date;

import com.nacid.bl.nomenclatures.regprof.SecondarySpeciality;
import com.nacid.data.DataConverter;

public class SecondarySpecialityWebModel {
    
    private String id = "";
    private String name = "";
    private String dateFrom = DataConverter.formatDate(new Date());
    private String dateTo = "дд.мм.гггг";
    private String professionalQualificationId = "";
    private String qualificationDegreeId = "";
    private String code = "";
    //private String ProfessionGroupName = "";
    
    public SecondarySpecialityWebModel() { }
    
    public SecondarySpecialityWebModel(String id, String name, String professionalQualificationId, String qualificationDegreeId, String dateFrom, String dateTo, String code) {
        this.id = id;
        this.name = name;
        this.professionalQualificationId = professionalQualificationId;
        this.qualificationDegreeId = qualificationDegreeId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.code = code;
    }
    
    public SecondarySpecialityWebModel(SecondarySpeciality speciality) {
        this.id = speciality.getId() + "";
        this.name = speciality.getName();
        this.professionalQualificationId = speciality.getProfessionalQualificationId() + "";
        this.qualificationDegreeId = speciality.getQualificationDegreeId() + "";
        //
        this.dateFrom = speciality.getDateFrom() == null ? "дд.мм.гггг" : DataConverter.formatDate(speciality.getDateFrom());
        this.dateTo = speciality.getDateTo() == null ? "дд.мм.гггг" : DataConverter.formatDate(speciality.getDateTo());
        this.code = speciality.getCode() == null ?  "" : speciality.getCode();
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getProfessionalQualificationId() {
        return professionalQualificationId;
    }
    
    public String getQualificationDegreeName() {
        return qualificationDegreeId;
    }
    
    public String getDateFrom() {
        return dateFrom;
    }
    
    public String getDateTo() {
        return dateTo;
    }

    public String getQualificationDegreeId() {
        return qualificationDegreeId;
    }

    public String getCode() {
        return code;
    }
    
}

