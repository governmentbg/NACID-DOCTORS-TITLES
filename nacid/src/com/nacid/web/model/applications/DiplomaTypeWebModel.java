package com.nacid.web.model.applications;

import java.util.ArrayList;
import java.util.List;

import com.nacid.bl.applications.DiplomaType;
import com.nacid.bl.impl.Utils;
import com.nacid.data.DataConverter;
import com.nacid.web.model.common.ComboBoxWebModel;

public class DiplomaTypeWebModel {
	protected int id;
	protected String visualElementsDescr = "";
	protected String protectionElementsDescr = "";
	protected String numberFormatDescr = "";
	protected String notes = "";
	protected String dateFrom = DataConverter.formatDate(Utils.getToday());
	protected String dateTo = "";
	protected String title;
	private boolean jointDegree;
	private List<DiplomaTypeIssuerWebModel> diplomaTypeIssuers = new ArrayList<DiplomaTypeIssuerWebModel>();
	protected String type;
	protected String educationLevel;
	protected String originalEducationLevel;
	protected String originalEducationLevelTranslated;


	public DiplomaTypeWebModel(int id, String visualElementsDescr, String protectionElementsDescr, String numberFormatDescr, String notes,
			String dateFrom, String dateTo, String title, boolean jointDegree, int type) {
		this.id = id;
		this.visualElementsDescr = visualElementsDescr;
		this.protectionElementsDescr = protectionElementsDescr;
		this.numberFormatDescr = numberFormatDescr;
		this.notes = notes;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.title = title;
		this.jointDegree = jointDegree;
		this.type = type + "";
	}

	public DiplomaTypeWebModel(DiplomaType diplomaType) {
		if (diplomaType == null) {
			this.type = DiplomaType.TYPE_NORMAL + "";
			return;
		}
		id = diplomaType.getId();
		title = diplomaType.getTitle();
		visualElementsDescr = diplomaType.getVisualElementsDescr();
		protectionElementsDescr = diplomaType.getProtectionElementsDescr();
		numberFormatDescr = diplomaType.getNumberFormatDescr();
		notes = diplomaType.getNotes();
		dateFrom = DataConverter.formatDate(diplomaType.getDateFrom(), "дд.мм.гггг");
		dateTo = DataConverter.formatDate(diplomaType.getDateTo(), "дд.мм.гггг");
		this.jointDegree = diplomaType.isJointDegree();
		this.type = diplomaType.getType() + "";
		this.educationLevel = diplomaType.getEducationLevel() == null ? "" : diplomaType.getEducationLevel().getName();
		this.originalEducationLevel = diplomaType.getOriginalEducationLevel() == null ? "" : diplomaType.getOriginalEducationLevel().getName();
		this.originalEducationLevelTranslated = diplomaType.getOriginalEducationLevel() == null ? "" : diplomaType.getOriginalEducationLevel().getNameTranslated();

		

	}
	public void addDiplomaTypeIssuer(Integer countryId, ComboBoxWebModel universityCombo, ComboBoxWebModel facultyCombo) {
		diplomaTypeIssuers.add(new DiplomaTypeIssuerWebModel(countryId, universityCombo, facultyCombo));
		
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

	public List<DiplomaTypeIssuerWebModel> getDiplomaTypeIssuers() {
		return diplomaTypeIssuers;
	}
	public boolean isJointDegree() {
		return jointDegree;
	}

	public String getType() {
		return type;
	}

	public String getEducationLevel() {
		return educationLevel;
	}

	public String getOriginalEducationLevel() {
		return originalEducationLevel;
	}

	public String getOriginalEducationLevelTranslated() {
		return originalEducationLevelTranslated;
	}
}
