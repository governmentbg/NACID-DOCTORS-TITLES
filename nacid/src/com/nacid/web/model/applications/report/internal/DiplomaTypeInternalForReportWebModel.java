package com.nacid.web.model.applications.report.internal;

import com.nacid.bl.applications.DiplomaType;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.data.DataConverter;

public class DiplomaTypeInternalForReportWebModel {
	protected int id;
	protected String visualElementsDescr = "";
	protected String protectionElementsDescr = "";
	protected String numberFormatDescr = "";
	protected String notes = "";
	protected String dateFrom = "";
	protected String dateTo = "";
	protected String title;
	private String educationLevel;
	public DiplomaTypeInternalForReportWebModel(DiplomaType diplomaType) {
		if (diplomaType == null) {
			return;
		}
		id = diplomaType.getId();
		visualElementsDescr = diplomaType.getVisualElementsDescr();
		protectionElementsDescr = diplomaType.getProtectionElementsDescr();
		numberFormatDescr = diplomaType.getNumberFormatDescr();
		notes = diplomaType.getNotes();
		dateFrom = DataConverter.formatDate(diplomaType.getDateFrom(), "");
		dateTo = DataConverter.formatDate(diplomaType.getDateTo(), "");
		title = diplomaType.getTitle();
		FlatNomenclature educationLevel = diplomaType.getEducationLevel();
		this.educationLevel = educationLevel == null ? "" : educationLevel.getName();
	}
	public String getTitle() {
		return title;
	}
	
	public int getId() {
		return id;
	}

	public String getVisualElementsDescr() {
		return visualElementsDescr;
	}

	public String getProtectionElementsDescr() {
		return protectionElementsDescr;
	}

	public String getNumberFormatDescr() {
		return numberFormatDescr;
	}

	public String getNotes() {
		return notes;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}
	public String getEducationLevel() {
		return educationLevel;
	}

}
