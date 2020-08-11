package com.ext.nacid.web.model;

import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.data.DataConverter;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExtPersonWebModel {

    private String id;
    private List<ExtPersonPersIdTypeWebModel> personalIdTypes;
    private String personalId;
    private String fname;
    private String sname;
    private String lname;
    private String birthCity;
    private String birthDate;
    private String email;
    private String username;
    private String personalIdType;

    private boolean editPersonlId;
    
    public ExtPersonWebModel(ExtPerson person) {
    	this.id = person.getId() + "";
        this.fname = person.getFName();
    	this.sname = person.getSName();
    	this.lname = person.getLName();
    	this.birthCity = person.getBirthCity();
    	this.birthDate = DataConverter.formatDate( person.getBirthDate(), "дд.мм.гггг");
    	this.email = person.getEmail();
    	FlatNomenclature pIdType = person.getCivilIdType();
    	personalIdType = (pIdType == null || StringUtils.isEmpty(person.getCivilId()))? "" : pIdType.getName();
    	personalId = person.getCivilId();
        editPersonlId = person.getCivilIdType() == null || StringUtils.isEmpty(person.getCivilId());
    }
    public List<ExtPersonPersIdTypeWebModel> getPersonalIdTypes() {
        return personalIdTypes;
    }

    public String getPersonalId() {
        return personalId;
    }

    public String getFname() {
        return fname;
    }

    public String getSname() {
        return sname;
    }

    public String getLname() {
        return lname;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
	public String getPersonalIdType() {
		return personalIdType;
	}

    public boolean isEditPersonlId() {
        return editPersonlId;
    }

    public String getFullName() {
        return Stream.of(fname, sname, lname).filter(StringUtils::isNotEmpty).collect(Collectors.joining(" "));
    }

    public String getId() {
        return id;
    }
}
