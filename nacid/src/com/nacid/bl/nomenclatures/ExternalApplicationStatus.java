package com.nacid.bl.nomenclatures;

import java.util.HashMap;
import java.util.Map;

public class ExternalApplicationStatus {
	
	/**
	 * imena na statusite, koito shte vijdat vynshnite potrebiteli!
	 */
	private static final Map<Integer, String> internalStatusToExternalStatusName = new HashMap<Integer, String>();
	static {
		internalStatusToExternalStatusName.put(ApplicationStatus.APPLICATION_STATUS_MIGRATED, "Мигрирано");
		internalStatusToExternalStatusName.put(ApplicationStatus.APPLICATION_PODADENO_STATUS_CODE, "Подадено");
		internalStatusToExternalStatusName.put(ApplicationStatus.APPLICATION_IZCHAKVANE_STATUS_CODE, "Изчакване");
		internalStatusToExternalStatusName.put(ApplicationStatus.APPLICATION_FOR_EXAMINATION_STATUS_CODE, "В процес на обработка");
		internalStatusToExternalStatusName.put(ApplicationStatus.APPLICATION_OTTEGLENO_STATUS_CODE, "Оттеглено");
		internalStatusToExternalStatusName.put(ApplicationStatus.APPLICATION_LEGITIMNO_PO_SEDALISHTE_STATUS_CODE, "В процес на обработка");
		internalStatusToExternalStatusName.put(ApplicationStatus.APPLICATION_LEGITIMNO_PO_MIASTO_STATUS_CODE, "В процес на обработка");
		internalStatusToExternalStatusName.put(ApplicationStatus.APPLICATION_LEGITIMNA_PROGRAMA_STATUS_CODE, "В процес на обработка");
		internalStatusToExternalStatusName.put(ApplicationStatus.APPLICATION_NELEGITIMNO_STATUS_CODE, "В процес на обработка");
		internalStatusToExternalStatusName.put(ApplicationStatus.APPLICATION_AUTHENTIC_STATUS_CODE, "В процес на обработка");
		internalStatusToExternalStatusName.put(ApplicationStatus.APPLICATION_FAKE_DIPLOMA_STATUS_CODE, "В процес на обработка");
		internalStatusToExternalStatusName.put(ApplicationStatus.APPLICATION_PRIZNATO_STATUS_CODE, "Признато");
		internalStatusToExternalStatusName.put(ApplicationStatus.APPLICATION_REFUSED_STATUS_CODE, "Отказ");
		internalStatusToExternalStatusName.put(ApplicationStatus.APPLICATION_POSTPONED_STATUS_CODE, "Отложено");
		internalStatusToExternalStatusName.put(ApplicationStatus.APPLICATION_TERMINATED_STATUS_CODE, "За прекратяване");
		internalStatusToExternalStatusName.put(ApplicationStatus.APPLICATION_OBEZSILENO_STATUS_CODE, "Обезсилено");
		internalStatusToExternalStatusName.put(ApplicationStatus.APPLICATION_POSTPONED_SUBMITTED_DOCS, "Отложено и представени документи");
		internalStatusToExternalStatusName.put(ApplicationStatus.APPLICATION_NEDOPUSKANE_DO_RAZGLEJDANE_STATUS_CODE, "Недопускане до разглеждане");
	}
	public static String getExternalStatusName(int internalStatusId) {
		return internalStatusToExternalStatusName.get(internalStatusId);
	}
	
	public static String getExternalStatusName(FlatNomenclature applicationStatus) {
		return internalStatusToExternalStatusName.get(applicationStatus.getId());
	}
}
