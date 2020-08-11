package com.nacid.web.model.nomenclatures;

import java.util.Date;

import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.data.DataConverter;

public class FlatNomenclatureWebModel {
	private Integer id;
	private String name = "";
	private String dateFrom = DataConverter.formatDate(new Date());
	private String dateTo = "дд.мм.гггг";
	/**
	 * groupName i nomenclatureName se polzvat samo i edinstveno za editvane na FlatNomenclatures, 
	 * tyi kato se polzva edna forma(edno jsp) - trqbva da se znae kakkyv e tipa editvana nomenclatura
	 * kakto i kakvo shte izpishe otgore na stranicata (пример: Данни за професионални направления)
	 */
	private String groupName = "";
	private String nomenclatureName = "";
	private int nomenclatureType;
	public FlatNomenclatureWebModel(Integer id, String name, String dateFrom, String dateTo, String groupName, String nomenclatureName, int nomenclatureType) {
		this.id = id;
		this.name = name;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.groupName = groupName;
		this.nomenclatureName = nomenclatureName;
		this.nomenclatureType = nomenclatureType;
	}
	public FlatNomenclatureWebModel(FlatNomenclature flatNomenclature, String groupName, String nomenclatureName) {
		this.id = flatNomenclature.getId();
		this.name = flatNomenclature.getName();
		this.dateFrom = flatNomenclature.getDateFrom() == null ? "дд.мм.гггг" : DataConverter.formatDate(flatNomenclature.getDateFrom());
		this.dateTo = flatNomenclature.getDateTo() == null ? "дд.мм.гггг" : DataConverter.formatDate(flatNomenclature.getDateTo());
		this.groupName = groupName;
		this.nomenclatureName = nomenclatureName;
		nomenclatureType = flatNomenclature.getNomenclatureType();
	}
	public FlatNomenclatureWebModel(String groupName, String nomenclatureName, int nomenclatureType) {
		this.groupName = groupName;
		this.nomenclatureName = nomenclatureName;
		this.nomenclatureType = nomenclatureType;
	}
	public String getId() {
		return id == null ? "" : id + "";
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
	public String getGroupName() {
		return groupName;
	}
	public String getNomenclatureName() {
		return nomenclatureName;
	}
	public Integer getIntegerId(){
		return id;
	}
	public int getNomenclatureType() {
		return nomenclatureType;
	}

}
