package com.nacid.data.nomenclatures;

import java.sql.Date;

public class LegalReasonRecord extends FlatNomenclatureRecord {
	private Integer applicationStatusId;
	private String ordinanceArticle;
	private String regulationArticle;
	private String regulationText;
	public LegalReasonRecord() {
	}
	public LegalReasonRecord(int id, String name, Integer applicationStatusId, String ordinanceArticle, String regulationArticle, String regulationText, Date dateFrom, Date dateTo) {
		super(id, name, dateFrom, dateTo);
		this.applicationStatusId = applicationStatusId;
		this.ordinanceArticle = ordinanceArticle;
		this.regulationArticle = regulationArticle;
		this.regulationText = regulationText;
	}
	public Integer getApplicationStatusId() {
		return applicationStatusId;
	}
	public void setApplicationStatusId(Integer applicationStatusId) {
		this.applicationStatusId = applicationStatusId;
	}

	public String getOrdinanceArticle() {
		return ordinanceArticle;
	}

	public void setOrdinanceArticle(String ordinanceArticle) {
		this.ordinanceArticle = ordinanceArticle;
	}

	public String getRegulationArticle() {
		return regulationArticle;
	}

	public void setRegulationArticle(String regulationArticle) {
		this.regulationArticle = regulationArticle;
	}

	public String getRegulationText() {
		return regulationText;
	}

	public void setRegulationText(String regulationText) {
		this.regulationText = regulationText;
	}
}
