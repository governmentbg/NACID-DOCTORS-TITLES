package com.nacid.report.export;

import com.nacid.bl.nomenclatures.DocumentType;

import java.util.LinkedHashMap;
import java.util.Map;

public class JasperReportNames {
	public static final String JASPER_REPORT_SLUJEBNA_BELEJKA_PODADENO = "slujebna_belejka_podadeno";
	public static final String JASPER_REPORT_SLUJEBNA_BELEJKA_PODADENI_DOCS = "slujebna_belejka_podadeni_docs";
	public static final String JASPER_REPORT_REGISTRATION = "registration";
	public static final String JASPER_REPORT_SLUJEBNA_BELEJKA_PRIZNATO = "slujebna_belejka_priznato";
	public static final String JASPER_REPORT_PISMO_ZAIAVITEL_INFO = "pismo_zaiavitel_info";
	public static final String JASPER_REPORT_UDOSTOVERENIE = "udostoverenie";
	public static final String JASPER_REPORT_PISMO_ZAIAVITEL = "pismo_zaiavitel";
	//public static final String JASPER_REPORT_RESHENIE_OTKAZ = "reshenie_otkaz";
	public static final String JASPER_REPORT_RESHENIE_OTKAZ_T1 = "reshenie_otkaz_t1";
	public static final String JASPER_REPORT_RESHENIE_OTKAZ_T2 = "reshenie_otkaz_t2";
	public static final String JASPER_REPORT_RESHENIE_OTKAZ_T1_S_OTLAGANE = "reshenie_otkaz_t1_s_otlagane";
	public static final String JASPER_REPORT_RESHENIE_OTKAZ_OBUCHENIE_BG = "reshenie_otkaz_BG";
	public static final String JASPER_REPORT_PROTOCOL = "protokol";
	public static final String JASPER_REPORT_REPORT_XLS= "report"; //xls reporta ot vsi4ki zaqvleniq za spravkite
	public static final String JASPER_REPORT_REGPROF_REPORT_XLS= "report_regprof"; //xls reporta ot vsi4ki zaqvleniq za spravkite
	public static final String JASPER_REPORT_COMMISSION_CALENDAR_REPORT_XLS= "report_commission"; //xls reporta ot vsi4ki zaqvleniq na komisiqta
	public static final String JASPER_REPORT_LETTER_AUTHENCITY_EN_CHEF_DIRECTORATE = "Authenticity_EN_chief_directorate"; //pismo ot nachalnik otdel do universitet za prou4vane na diploma (na angliiski)
	public static final String JASPER_REPORT_LETTER_AUTHENCITY_RU_CHEF_DIRECTORATE = "Authenticity_RU_chief_directorate"; //pismo ot nachalnik otdel do universitet za prou4vane na diploma (na ruski)
	public static final String JASPER_REPORT_LETTER_AUTHENTIC_EN_EXEC_DIRECTOR = "Authenticity_EN_exec_dir"; //pismo ot izpylnitelniq direktor do universitet za prou4vane na diploma (angliiski)
	public static final String JASPER_REPORT_PISMO_NEDOPUSKANE_T1 = "pismo_nedopuskane_t1";
	public static final String JASPER_REPORT_PISMO_NEDOPUSKANE_T2 = "pismo_nedopuskane_t2";
	public static final String JASPER_REPORT_PISMO_PROKURATURA = "pismo_prokuror";
	public static final String JASPER_REPORT_PISMO_PREKRATIAVANE = "pismo_prekratiavane";
	public static final String JASPER_REPORT_STANOVISHTE = "stanovishte";
	public static final String JASPER_REPORT_ZAPOVED_PREKRATIAVANE = "zapoved_prekratiavane";
	public static final String JASPER_REPORT_ZAPOVED_OBEZSILVANE = "zapoved_obezsilvane";
	
	public static final String JASPER_REPORT_PISMO_SPIRANE_SROK = "pismo_spirane_srok";
	//RayaWritten--------------------------------------------------------------------------
	public static final String JASPER_REPORT_ZAPITVANE_AUTH = "zapitvane_auth";
	//-----------------------------------------------------------------------------------
	public static final String JASPER_REPORT_AUTHENTICITY_ENIC_NARIC_ENG = "Authenticity_ENIC-NARIC_ENG";
	public static final String JASPER_REPORT_STATUSQUO_ENIC_NARIC_ENG = "Statusquo_ENIC-NARIC_ENG";
	//public static final String JASPER_REPORT_Authenticity_RU_chief_directorate = "Authenticity_RU_chief_directorate";
	public static final String JASPER_REPORT_Authenticity_University_RU = "Authenticity_University_RU";
	//public static final String JASPER_REPORT_Authenticity_EN_chief_directorate = "Authenticity_EN_chief_directorate";
	//public static final String JASPER_REPORT_Authenticity_EN_exec_dir = "Authenticity_EN_exec_dir";
	public static final String JASPER_REPORT_Authenticity_University_ENG = "Authenticity_University_ENG";
	

	
	/**
	 * report-a, koito se generira pri spravkite!
	 */
	public static final String JASPER_REPORT_INQUIRES_SCREENS = "inquires_screens";


    public static final String JASPER_REPORT_REGPROF_INQUIRES_SCREENS = "inquires_screens_regprof";
	/**
	 * report, koito printira text-a nqma danni s izbranite kriterii
	 */
	public static final String JASPER_REPORT_EMPTY = "empty_report";
	/**
	 * ukazva vryzkata mejdu ime na report template i poleto v drop down menu-to, koeto se pokazva na potrebitelq
	 * pri editvane na nomenklaturata {@link DocumentType} 
	 */
	public static Map<String, String> reportNamesToLablesMap = new LinkedHashMap<String, String>();
	static {
		reportNamesToLablesMap.put(JASPER_REPORT_PISMO_ZAIAVITEL, "Писмо до заявител");
		reportNamesToLablesMap.put(JASPER_REPORT_PISMO_ZAIAVITEL_INFO, "Писмо до заявител за изискване на допълнителна информация");
		reportNamesToLablesMap.put(JASPER_REPORT_PISMO_PREKRATIAVANE, "Писмо до заявител за прекратяване");
		reportNamesToLablesMap.put(JASPER_REPORT_PISMO_PROKURATURA, "Писмо до прокуратурата за фалшива диплома ");
		reportNamesToLablesMap.put(JASPER_REPORT_PISMO_NEDOPUSKANE_T1, "Писмо за недопускане до разглеждане по т.1");
		reportNamesToLablesMap.put(JASPER_REPORT_PISMO_NEDOPUSKANE_T2, "Писмо за недопускане до разглеждане по т.2");
		reportNamesToLablesMap.put(JASPER_REPORT_LETTER_AUTHENCITY_EN_CHEF_DIRECTORATE, "Писмо на английски от началник отдел до висше училище");
		reportNamesToLablesMap.put(JASPER_REPORT_LETTER_AUTHENTIC_EN_EXEC_DIRECTOR, "Писмо на английски от изпълнителен директор до висше училище");
		reportNamesToLablesMap.put(JASPER_REPORT_LETTER_AUTHENCITY_RU_CHEF_DIRECTORATE, "Писмо на руски от началник отдел до висше училище");
		reportNamesToLablesMap.put(JASPER_REPORT_RESHENIE_OTKAZ_T1, "Решение за отказ по т.1.");
		reportNamesToLablesMap.put(JASPER_REPORT_RESHENIE_OTKAZ_T1_S_OTLAGANE, "Решение за отказ по т.1. с отлагане");
		reportNamesToLablesMap.put(JASPER_REPORT_RESHENIE_OTKAZ_T2, "Решение за отказ по т.2.");
		reportNamesToLablesMap.put(JASPER_REPORT_RESHENIE_OTKAZ_OBUCHENIE_BG, "Решение за отказ - обучение в България");
		reportNamesToLablesMap.put(JASPER_REPORT_SLUJEBNA_BELEJKA_PRIZNATO, "Служебна бележка - признато образование");
		reportNamesToLablesMap.put(JASPER_REPORT_SLUJEBNA_BELEJKA_PODADENO, "Служебна бележка - подадено заявление");
		reportNamesToLablesMap.put(JASPER_REPORT_SLUJEBNA_BELEJKA_PODADENI_DOCS, "Служебна бележка - представени документи");
		reportNamesToLablesMap.put(JASPER_REPORT_STANOVISHTE, "Становище");
		reportNamesToLablesMap.put(JASPER_REPORT_UDOSTOVERENIE, "Удостоверение");
		reportNamesToLablesMap.put(JASPER_REPORT_ZAPOVED_PREKRATIAVANE, "Заповед за зам.министър за прекратяване");
		reportNamesToLablesMap.put(JASPER_REPORT_ZAPOVED_OBEZSILVANE, "Заповед за обезсилване");
		
		
		reportNamesToLablesMap.put(JASPER_REPORT_AUTHENTICITY_ENIC_NARIC_ENG, "Писмо английски Автентичност - ЕНИК-НАРИК");
		reportNamesToLablesMap.put(JASPER_REPORT_STATUSQUO_ENIC_NARIC_ENG, "Писмо английски Статут - ЕНИК-НАРИК");
		reportNamesToLablesMap.put(JASPER_REPORT_Authenticity_University_RU, "Писмо руски Автентичност - Университет");
		reportNamesToLablesMap.put(JASPER_REPORT_Authenticity_University_ENG, "Писмо английски Автентичност - Университет");
		
		/** regprof report names */
		reportNamesToLablesMap.put(JASPER_REPORT_PISMO_SPIRANE_SROK, "Уведомление за спиране на срока");
	}
	
}
