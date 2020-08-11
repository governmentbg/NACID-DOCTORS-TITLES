package com.nacid.regprof.web.model.nomenclatures;

import java.util.Date;

import com.nacid.bl.nomenclatures.regprof.ProfessionExperienceDocumentType;
import com.nacid.data.DataConverter;

public class ProfessionExperienceDocumentTypeWebModel {
	private String id = "";
	private String name = "";
	private String dateFrom = DataConverter.formatDate(new Date());
	private String dateTo = "дд.мм.гггг";
	private boolean isForExperienceCalculation;
	

	
	public ProfessionExperienceDocumentTypeWebModel(String id, String name, boolean isForExperienceCalculation, String dateFrom, String dateTo) {
		this.id = id;
		this.name = name;
		this.isForExperienceCalculation = isForExperienceCalculation;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
	}
	public ProfessionExperienceDocumentTypeWebModel(ProfessionExperienceDocumentType documentType) {
		this.id = documentType.getId() + "";
		this.name = documentType.getName();
		this.isForExperienceCalculation = documentType.isForExperienceCalculation();
		this.dateFrom = documentType.getDateFrom() == null ? "дд.мм.гггг" : DataConverter.formatDate(documentType.getDateFrom());
		this.dateTo = documentType.getDateTo() == null ? "дд.мм.гггг" : DataConverter.formatDate(documentType.getDateTo());
	}
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDateFrom() {
		return dateFrom;
	}
	public String getDateTo() {
		return dateTo;
	}
	public boolean isForExperienceCalculation() {
		return isForExperienceCalculation;
	}
	
	

}
