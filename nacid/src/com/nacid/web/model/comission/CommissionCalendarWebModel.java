package com.nacid.web.model.comission;

import java.util.Date;

import com.nacid.bl.comision.CommissionCalendar;
import com.nacid.data.DataConverter;

public class CommissionCalendarWebModel {
	private String id;
	private String notes;
	private String dateTime;
	private String sessionNumber;
	public CommissionCalendarWebModel(int id, String notes, Date dateTime) {
		this.id = id + "";
		this.notes = notes;
		this.dateTime = DataConverter.formatDateTime(dateTime, false);
	}
	public CommissionCalendarWebModel(CommissionCalendar calendar) {
		this.id = calendar.getId() + "";
		this.notes = calendar.getNotes();
		this.dateTime = DataConverter.formatDateTime(calendar.getDateAndTime(), false);
		this.sessionNumber = calendar.getSessionNumber() + "";
	}
	public String getId() {
		return id;
	}
	public String getNotes() {
		return notes;
	}
	public String getDateTime() {
		return dateTime;
	}
	public String getSessionNumber() {
		return sessionNumber;
	}
}
