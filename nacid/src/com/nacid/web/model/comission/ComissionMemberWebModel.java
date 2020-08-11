package com.nacid.web.model.comission;

import com.nacid.bl.comision.ComissionMember;
import com.nacid.data.DataConverter;

public class ComissionMemberWebModel {

	private int id;
	private String fname;
	private String sname;
	private String lname;
	private String degree;
	private String institution;
	private String division;
	private String title;
	private String egn;
	private String homeCity;
	private String homePcode;
	private String homeAddress;
	private String phone;
	private String email;
	private String gsm;
	private String iban;
	private String bic;
	private String dateFrom;
	private String dateTo;
	private String profGroupName = "";
	private String commissionPositionName = "";
	private String userName;
	
	
	public ComissionMemberWebModel(ComissionMember cm) {
		id = cm.getId();
		fname = cm.getFname();
		sname = cm.getSname();
		lname = cm.getLname();
		degree = cm.getDegree();
		institution = cm.getInstitution();
		division = cm.getDivision();
		title = cm.getTitle();
		egn = cm.getEgn();
		homeCity = cm.getHomeCity();
		homeAddress = cm.getHomeAddress();
		homePcode = cm.getHomePcode();
		phone = cm.getPhone();
		email = cm.getEmail();
		gsm = cm.getGsm();
		iban = cm.getIban();
		bic = cm.getBic();
		dateFrom = cm.getDateFrom() == null ? "дд.мм.гггг" : DataConverter.formatDate(cm.getDateFrom());
		dateTo = cm.getDateTo() == null ? "дд.мм.гггг" : DataConverter.formatDate(cm.getDateTo());
		 
		if (cm.getProfGroup() != null && cm.getProfGroup().getName() != null) {
			profGroupName = cm.getProfGroup().getName();
        }
		commissionPositionName = cm.getComissionPos().getName();
        if (cm.getUser() != null) {
            userName = cm.getUser().getUserName();
        }
	}
	
	public ComissionMemberWebModel(int id, String fname, String sname,
			String lname, String degree, String institution, String division,
			String title, String egn, String homeCity, String homeAddress,
			String homePcode, String phone, String email, String gsm,
			String iban, String bic, String dateFrom, String dateTo, String userName) {
		this.id = id;
		this.fname = fname;
		this.sname = sname;
		this.lname = lname;
		this.degree = degree;
		this.institution = institution;
		this.division = division;
		this.title = title;
		this.egn = egn;
		this.homeCity = homeCity;
		this.homeAddress = homeAddress;
		this.homePcode = homePcode;
		this.phone = phone;
		this.email = email;
		this.gsm = gsm;
		this.iban = iban;
		this.bic = bic;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.userName = userName;
	}


	public String getUserName() {
        return userName;
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


	public String getDegree() {
		return degree;
	}


	public String getInstitution() {
		return institution;
	}


	public String getDivision() {
		return division;
	}


	public String getTitle() {
		return title;
	}


	public String getEgn() {
		return egn;
	}


	public String getHomeCity() {
		return homeCity;
	}


	public String getHomePcode() {
		return homePcode;
	}


	public String getHomeAddress() {
		return homeAddress;
	}


	public String getPhone() {
		return phone;
	}


	public String getEmail() {
		return email;
	}


	public String getGsm() {
		return gsm;
	}


	public String getIban() {
		return iban;
	}


	public String getBic() {
		return bic;
	}


	public String getDateFrom() {
		return dateFrom;
	}


	public String getDateTo() {
		return dateTo;
	}


	public int getId() {
		return id;
	}

	public String getProfGroupName() {
		return profGroupName;
	}

	public String getCommissionPositionName() {
		return commissionPositionName;
	}

	
}
