package com.nacid.bl.applications;


public interface ApplicationDetailsForReport {
	public Application getApplication();
	public String getId();
	/**
	 * vry6ta DocFlowNumbera bez datata (pr:44-00-1234)
	 */
	public String getApplicationNumber();
	/**
	 * @return delovoden nomer na zaqvlenieto (vkl. datata) - pr 44-00-1234/12.12.2010
	 */
	public String getDocFlowNumber();
	/**
	 * @return data na podavane na zaqvlenieto
	 */
	public String getDate();
	/**
	 * @return trite imena na zaqvitelq - vzemat se ot person-a
	 */
	public String getOwnerName();
	/**
	 * @return В Протокола имената на заявителите да се показват по диплома, и ако има разлика с имената по лична карта, тези по лична карта да се показват в скоби. Например Иванка Иванова Петрова (Иванка Иванова Колева).
	 */
	public String getOwnerNameForProtocol();
	/**
	 * @return diplomaNames - ako ima vyvedeni differentDiplomaNames, vry6ta tqh, ina4e vry6ta person's names
	 */
	public String getDiplomaName();
	
	
	/**
	 * @return trite imena na zaqvitelq po diploma - ako sa ednakvi, vry6ta imenata na zaqvitelq
	 */
	public String getOwnerDiplomaName();
	/**
	 * @return identifikatora (egn/ln4/drugo) + nomera na civil ID-to 
	 */
	public String getOwnerCivilId();
	/**
     * При една специалност, връща името на специалността (пример:Химия)
     * при много специалности връща
     * първа специалност: химия, втора специалност: физика и т.н.
     */
	public String getDiplomaSpecialityNames();
	/**
	 * @return dali ima poveche ot edna specialnost po diploma
	 */
	public boolean isMultipleDiplomaSpecialities();
	/**
	 * @return pridobitata stepen po diploma
	 */
	public String getDiplomaEducationLevel();

	/**
	 * @return idto na pridobitata stepen po diploma
	 */
	public Integer getDiplomaEducationLevelId();
	
	/**
	 * @return имената на университетите за сертификата във вида "име" в "държава", разделени със ",". 
	 * Пример: Технически унивесристет в България, Московски Университет в Руска федерация
	 */
	public String getAllUniversitiesNamesAndLocationsForCertificate();
	
	/**
	 * @return dyrjavite v koito e provedeno obu4enieto razdeleni s "," ako sa pove4e ot edna Пр: Русия, Великобритания
	 */
	public String getTrainingCountries();
	
	
	/**
	 * @return имената на унивеситетите във вида "име", "държава", разделени с ";" - този метод засега се ползва на всички други места
	 * където трябва тази информация, с изключение на сертификата, защото там разделителите са други
	 * Пример: Технически унивесристет, България; Московски Университет, Руска федерация
	 */
	public String getAllUniversitiesNamesAndLocations();


    /**
     * връща bg имената на университетите, разделени със запетая
     * @return
     */
    public String getAllUniversitiesBgNames();

    /**
     * връща пълните имена на университетите разделени със запетая bgName <b>(orgName)</b>
     */
    public String getAllUniversitiesFullNames();
	
	
	/**
	 * @return "град", "държава"; "град", "държава" на местата на провеждане на обучението - мисля че ще се ползва на всички места с изключение на сертификата 
	 * Пример: Москва, Русия; Лондон, Великобритания
	 */
	public String getTrainingCountriesAndLocations();
	
	/**
	 * @return datata na sesiqta na komisiqta
	 */
	public String getCommissionSessionDate();
	
	/**
	 * @return adres-a za korespondenciq v bylgariq (na zaqvitelq ili pylnomoshtnika)
	 */
	public String getAddressDetailsBulgaria();
	/**
	 * @return grada za korespondenciq v bylgariq
	 */
	public String getCityDetailsBulgaria();
	/**
	 * @return - familiq na zaqvitelq
	 */
	public String getOwnerLastName();
	/**
	 * @return - priznata obrazovatelna stepen (koqto moje da se razlichava ot tazi po diploma)
	 */
	public String getRecognizedEducationLevel();
	/**
	 * @return priznato ime na specialnostta (koeto moje da se razlichava ot tova po diploma)
	 * ако специалността е само 1, се връща "специалността алабала", ако специалностите са повече от една се връща
	 * първа призната специалност: първата, втора призната специалност: втората, трета призната специалност:третата и т.н. 
	 */
	public String getRecognizedSpecialityName();
	/**
	 * @return priznato ime na specialnostta (koeto moje da se razlichava ot tova po diploma)
	 * ако специалността е само 1, се връща само името на специалността (за разлика от getRecognizedSpecialityName, което връща и думичката "специалността")
	 * Ако специалностите са повече от една се връща:
	 * първа призната специалност: първата, втора призната специалност: втората, трета призната специалност:третата и т.н.
	 */
	public String getRecognizedSpecialityNameForCertificate();
	/**
     * @return priznato ime na specialnostta (koeto moje da se razlichava ot tova po diploma)
     */
    public String getRecognizedQualification();
	/**
	 * @return mqsto na rajdane na zaqvitelq - city + country
	 */
	public String getOwnerBirthPlace();
	/**
	 * @return mqstoto na rajdane na zaqvitelq - country
	 */
	public String getOwnerBirthCountry();
	/**
	 * @return grajdanstvo na zaqvitelq (zasega izlgejda vyv vijda "гражданин на България" )
	 */
	public String getOwnerCitizenship();
	
	/**
	 * @return - mqsto na rajdane - <grad, dyrjava>
	 */
	public String getOwnerBirthDate();
	
	public String getOwnerAddressDetails(); //dyrjava, grad, adres
	
	public String getDiplomaNumber();
	
	public String getApplicationStatus();

	public String getDocflowStatus();
	/**
	 * @return statusite ot komisiite vyv vida date + statusName + "на основание" + legalReason
	 */
	public String getCommissionStatuses();
	
	public String getTrainingCourseQualification();
	/**
	 * @return prodyljitelnost na obrazovanieto - primerno 2 semstera
	 */
	public String getTrainingDuration();

    public String getTrainingStart();
    public String getTrainingEnd();
    public String getTrainingForm();
    public String getGraduationWay();
	/**
	 * @return godini na provejdane na obu4enieto, razdeleni s tire - pr: 2002-2004
	 */
	public String getTrainingYears();
	
	/**
	 *  [16:38:50] Irina Vassileva says: ако има инфо за завършено средно - училище, държава, град
		[16:38:53] Irina Vassileva says: +
		[16:39:13] Irina Vassileva says: ако има инфор за предходна образователна степен - униврситет, държава, град
		[16:39:23] Irina Vassileva says: и за двете, ако има година - и тя
		
		Променливата се ползва в генериране на двата екселски файла - този преди заседание и справката на комисията
	 */
	public String getPreviousEducation();
	/**
	 * @return motivite za priznavane/nepriznavane/
	 */
	public String getMotives();
	
	public String getDiplomaDate();
	
	public String getCertificateNumber();
	
	/**
	 * @return държава издала дипломата - ползва се в report.xls - ако има повече от 1 университет, връща "съвместна" 
	 */
	public String getCountryPublishedDiploma();
	
	public boolean isJointDegree();
	/**
	 * @return оригинално име<br />град<br />държава<br/><br/> за всеки университет - ползват се в Authencity_* reports
	 */
	public String getOriginalUniversitiesNameCityCountry();
	/**
	 * @return само оригиналните имена на университетите - ползват се в Authenticity_* reports
	 */
	public String getAllUniversitiesNames();
	/**
	 * @return samo dyrjavite na universitetite, razdeleni s ,
	 */
	public String getAllUniversitiesCountries();
	/**
	 * @return dneshnata data! vyv format dd.mm.yyyy
	 */
	public String getToday();
	/**
	 * vry6ta kalendarite na koito e razglejdano zasedanieto vyv vida "date" + "calendarId"
	 * @return
	 */
	public String getCommissionCalendars();
	/**
	 * @return imeto na pyrviq expert - bez titlata!, koito se polzva za sortirane na generiranata excelska tablica po ime na expert 
	 */
	public String getFirstExpertName();
	/**
	 * @return imenata na expertite, zaedno s titlite, assign-nati kym tova zaqvlenie, razdeleni s \n
	 */
	public String getExpertNames();
	
	/**
	 * @return номер предходно заседание, мотиви, представени документи;
	 */
	public String getLastCommissionCalendarInfo();
	
	
	/**
	 * @return - "Информация за заявителя"
	 */
	public String getApplicantInfo();
	/**
	 * @return belejki kym zaqvlenieto
	 */
	public String getNotes();
	
	public String getAllNotes();
	/**
	 * @return belejki kym predhodno obrazovanie
	 */
	public String getPreviousDiplomaNotes();

    public String getDiplomaOriginalEducationLevel();

    public String getDiplomaSpecialitiesForExpertPosition();


    public String getBolognaCycle();
    public String getEuropeanQualificationsFramework();
    public String getNationalQualificationsFramework();

    public String getBolognaCycleAccess();
    public String getEuropeanQualificationsFrameworkAccess();
    public String getNationalQualificationsFrameworkAccess();

    public boolean isUniversityAuthorized();
    public boolean isLegitimateProgram();

    /**
     * <b>bgName</b><br />orgName
     * @return
     */
    public String getUniversityNamesForExpertPosition();

    public String getGraduationDocumentType();
    public String getCreditHours();

    public String getCredits();

    /**
     * vry6ta universitetite, kakto se pokazvat v ExpertPosition:
     * <b>Име на Български</b><br/>English Name
     * @return
     */
    public String getUniversitiesForExpertPosition();

	/**
	 * @return fakultetite vyv vida
	 * <b>Име на Български</b><br/>English Name
	 */
	public String getFacultiesForExpertPosition();

    /**
     * vry6ta obuchavashta instituciq vyv vida
     * <b>trainingInstitutionName</b><br />trainingInstitutionCountry
     * Ako nqma takava, vry6ta UniversitiesForExpertPosition
     * @return
     */
    public String getTrainingInstitutionForExpertPosition();

    /**
     * @return vsichki dyrjavi na universiteti, razdeleni s nov red
     */
    public String getAllUniversityCountriesSeparatedByNewLine();


    public String getOutgoingNumber();
    public String getInternalNumber();

    /**
     * връща номер във вида "изх.№ outgoingNumber и вх № в НАЦИД docFlowNumber". Ако outgoingNumber е празн, връща само "вх № в НАЦИД docFlowNumber"
     * @return
     */
    public String getStatuteAuthenticityRecommendationLetterNumber();

    /**
     *
     * @return obuchavashta instituciq (ako ima takava)
     */
    public String getTrainingInstitution();


    /**
     * @return kompetentna instituciq na nacionalno nivo, ako ima takava
     */
    public String getCompetentInstitutionName();

	public String getResponsibleUser();

	//fields, related to doctorate applications
    public String getDiplomaProfessionGroup();

	public String getRecognizedProfessionGroup();

	public String getRecognizedEducationArea();

	public String getDiplomaOriginalEducationLevelTranslated();

	public String getThesisTopic();

	public String getThesisTopicEn();

	/**
	 * @return dryjavite na obuchavashtite institucii (ako ima takiva)
	 */
	public String getTrainingInstitutionLocation();

	/**
	 * @return spisyk s ime, grad, dyrjava na vsichki trainingInstitutions, razdeleni s ;
	 */
	public String getTrainingInstitutionNameCityCountry();

	public String getLegalReasonOrdinanceArticle();

	public String getLegalReasonRegulationArticle();

	public String getLegalReasonRegulationText();

	public default Integer getApplicationType() {
		return null;
	}
}
