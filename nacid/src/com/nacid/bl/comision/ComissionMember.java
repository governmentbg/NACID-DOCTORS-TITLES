package com.nacid.bl.comision;

import java.util.Date;

import com.nacid.bl.users.User;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.ProfessionGroup;

public interface ComissionMember {

	public int getId();
	public String getFname();
	public String getSname();
	public String getLname();
	public String getFullName();
	public String getDegree();
	public String getInstitution();
	public String getDivision();
	public String getTitle();
	public String getEgn();
	public String getHomeCity();
	public String getHomePcode();
	public String getHomeAddress();
	public String getPhone();
	public String getEmail();
	public String getGsm();
	public String getIban();
	public String getBic();
	public Date getDateFrom();
	public Date getDateTo();
	public FlatNomenclature getComissionPos();
	public ProfessionGroup getProfGroup();
	public User getUser();
	public boolean isActive();
}
