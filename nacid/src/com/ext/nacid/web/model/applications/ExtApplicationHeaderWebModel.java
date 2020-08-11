package com.ext.nacid.web.model.applications;

import org.apache.commons.lang.StringUtils;

import com.nacid.bl.applications.Application;

public class ExtApplicationHeaderWebModel {
	private String statusName;
	private String docFlowNumber;
	private String certificateNumber;
	public ExtApplicationHeaderWebModel(String statusName, Application intApplication) {
		this.statusName = statusName;
		this.docFlowNumber = intApplication == null ? "" : "Деловоден № " + intApplication.getDocFlowNumber();
		this.certificateNumber = intApplication == null || StringUtils.isEmpty(intApplication.getCertificateNumber()) ? "" : ". Сертификат №" + intApplication.getCertificateNumber();
	}
	public String getStatusName() {
		return statusName;
	}
	public String getDocFlowNumber() {
		return docFlowNumber;
	}
	public String getCertificateNumber() {
		return certificateNumber;
	}
	
}
