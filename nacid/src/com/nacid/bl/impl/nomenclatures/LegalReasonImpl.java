package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.bl.nomenclatures.LegalReason;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.data.nomenclatures.LegalReasonRecord;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LegalReasonImpl extends FlatNomenclatureImpl implements LegalReason {
	private NacidDataProvider nacidDataProvider;
	private Integer applicationStatusId;
	private String ordinanceArticle;
	private String regulationArticle;
	private String regulationText;

	public LegalReasonImpl(LegalReasonRecord record, NacidDataProvider nacidDataProvider) {
		super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
		this.nacidDataProvider = nacidDataProvider;
		this.applicationStatusId = record.getApplicationStatusId();
		this.ordinanceArticle = record.getOrdinanceArticle();
		this.regulationArticle = record.getRegulationArticle();
		this.regulationText = record.getRegulationText();
	}

	public int getNomenclatureType() {
		return NomenclaturesDataProvider.NOMENCLATURE_LEGAL_REASON;
	}

	public ApplicationStatus getApplicationStatus() {
		return getApplicationStatusId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getApplicationStatus(NumgeneratorDataProvider.NACID_SERIES_ID, getApplicationStatusId());
	}

	public Integer getApplicationStatusId() {
		return applicationStatusId;
	}

	public String getOrdinanceArticle() {
		return ordinanceArticle;
	}

	public String getRegulationArticle() {
		return regulationArticle;
	}

	public String getRegulationText() {
		return regulationText;
	}

	@Override
	public String getRegulationTextPerEducationLevelId(int educationLevelId) {
		//regulationText-a izglejda ili "alabala" ili [x]ala[y]bala, kato x/y са educationLevelIds
		if (getRegulationText() == null) {
			return null;
		}
		Pattern pattern = Pattern.compile(String.format("\\[%d\\](.*?)\\s*\\[|\\[%d\\](.*?$)", educationLevelId, educationLevelId), Pattern.MULTILINE | Pattern.DOTALL);
		Matcher m = pattern.matcher(getRegulationText());
		if (m.find()) {
			return m.group(1) == null ? m.group(2) : m.group(1);
		} else {
			return getRegulationText();
		}
	}
}
