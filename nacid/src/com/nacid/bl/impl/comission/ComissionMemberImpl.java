package com.nacid.bl.impl.comission;

import java.util.Date;

import com.nacid.bl.comision.ComissionMember;
//import com.nacid.bl.external.users.ExtUser;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.ProfessionGroup;
import com.nacid.bl.users.User;
import com.nacid.data.comission.ComissionMemberRecord;

public class ComissionMemberImpl implements ComissionMember {

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
	private Date dateFrom;
	private Date dateTo;
	private FlatNomenclature comissionPos;
	private ProfessionGroup profGroup;
	private User user;
	
	ComissionMemberImpl(ComissionMemberRecord cmr, 
			FlatNomenclature comissionPos, ProfessionGroup profGroup, User user) {
		id = cmr.getId();
		fname = cmr.getFname();
		sname = cmr.getSname();
		lname = cmr.getLname();
		degree = cmr.getDegree();
		institution = cmr.getInstitution();
		division = cmr.getDivision();
		title = cmr.getTitle();
		egn = cmr.getEgn();
		homeCity = cmr.getHomeCity();
		homePcode = cmr.getHomePcode();
		homeAddress = cmr.getHomeAddress();
		phone = cmr.getPhone();
		email = cmr.getEmail();
		gsm = cmr.getGsm();
		iban = cmr.getIban();
		bic = cmr.getBic();
		dateFrom = cmr.getDateFrom();
		dateTo = cmr.getDateTo();
		this.comissionPos = comissionPos;
		this.profGroup = profGroup;
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public int getId() {
		return id;
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
	public String getFullName() {
	    return fname + " " + (sname != null && !sname.equals("") ? sname + " " : "") + (lname != null ? lname : "");
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

	public Date getDateFrom() {
		return dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public FlatNomenclature getComissionPos() {
		return comissionPos;
	}

	public ProfessionGroup getProfGroup() {
		return profGroup;
	}
	public boolean isActive() {
	    return Utils.isRecordActive(dateFrom, dateTo);
	}
	
	
}
