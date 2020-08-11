package com.nacid.web.model.comission;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class CommissionApplicationsWebModel {
	private String applicationIds = "";
	private String calendarId;
	private int applicationIdsCount = 0;
	public CommissionApplicationsWebModel(int calendarId, List<Integer> applicationIds) {
		if (applicationIds != null) {
			this.applicationIds = StringUtils.join(applicationIds, ";");
			//V javascripta razchitam 4e shte ima ";" sled poslednoto id (primerno "1;3;")
			this.applicationIds += ";";
			applicationIdsCount = applicationIds.size();
		}
		this.calendarId = calendarId + "";

		
	}
	public String getApplicationIds() {
		return applicationIds;
	}
	public String getCalendarId() {
		return calendarId;
	}
	public int getApplicationIdsCount() {
		return applicationIdsCount;
	}
	
}
