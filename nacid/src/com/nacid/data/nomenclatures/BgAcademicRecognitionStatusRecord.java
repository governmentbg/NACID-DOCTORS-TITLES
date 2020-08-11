package com.nacid.data.nomenclatures;

import java.sql.Date;

import com.nacid.data.annotations.Table;

@Table(name="nomenclatures.bg_academic_recognition_status", sequence = "nomenclatures.bg_academic_recognition_status_id_seq")
public class BgAcademicRecognitionStatusRecord extends FlatNomenclatureRecord{

	public BgAcademicRecognitionStatusRecord() {
		super();
	}

	public BgAcademicRecognitionStatusRecord(int id, String name,
			Date dateFrom, Date dateTo) {
		super(id, name, dateFrom, dateTo);
	}
	
}
