package com.nacid.bl.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.*;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.comision.CommissionCalendar;
import com.nacid.bl.comision.CommissionCalendarDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class ApplicationDetailsForReportImpl implements ApplicationDetailsForReport {


	private String date; // Data na zaqvlenieto
	//private String applicationNumber; // Delovoden nomer na zaqvlenieto
	private String ownerName; // 3 imena na sobstvenika na diplomata
	private String ownerAddressDetails; // държава, град, адрес na sobstvenika na diplomata
	private String ownerCivilId; // EGN/PIK/drugo + nomer (primerno ЕГН 1010101010)
	private List<String> diplomaSpecialityNames; // ime na specialnostta
    private List<String> diplomaSpecialitiesForExpertPosition;
	private boolean multipleDiplomaSpecialities;
	
	//private String commissionSessionDate; // data na komisiqta
											// priznala(othvyrlila) zaqvlenieto
	private String diplomaEducationLevel; // obrazovatelna stepen
    private String diplomaOriginalEducationLevel; //originalno naimenovanie na OKS po dipoma
    private String diplomaOriginalEducationLevelTranslated; //originalno naimenovanie na OKS po dipoma - prevod
	private Integer diplomaEducationLevelId; //obrazovatelna stepen - id;

	private String addressDetailsBulgaria; // adres za korespondenciq v bylgariq
											// (na zaqvitelq ili pylnomoshtnika)
	private String cityDetailsBulgaria; // grad za korespondenciq v bylgariq
	private String ownerLastName; // familiq na sobstvenika na diplomata

	
	private String recognizedEducationLevel; // priznata obrazovatelna stepen (koqto moje da se razlichava ot tazi po diploma)

	// private String recognizedSpecialityName; //priznato ime na spacialnost
	// (koeto moje da se razlichava ot tova po diploma)
	// private String recognizedSpecialityNameForCertificate;
	private String recognizedQualification;// imeto na priznata kvalifikaciq

	
	private String ownerBirthPlace; // ownerBirthPlace - city + country;
	private String ownerBirthCountry;
	private String ownerBirthDate;
	private String ownerCitizenship;
	private String applicationStatus;
	private String docflowStatus;
	private String diplomaNumber;
	private String trainingCourseQualification;
	private String trainingDuration;
	private String motives;

	private String diplomaDate;

	private Application application;
	private TrainingCourse trainingCourse;
	private NacidDataProvider nacidDataProvider;
	private List<? extends UniversityWithFaculty> universities;
	private String members;
	private String firstExpertName;
	private String certificateNumber;
    private DiplomaType diplomaType;
    private boolean universityAuthorized;
    private boolean legitimateProgram;
    private String trainingInstitutionForExpertPosition;
    private String trainingInstitution = "";
    private String competentInstitutionName = "";
    private String recognizedProfessionGroup;
    private String recognizedEducationArea;
    private String thesisTopic;
    private String thesisTopicEn;
    private String responsibleUser;
	private final String diplomaProfessionGroup;
	private String trainingInstitutionLocation;
	private String trainingInstitutionNameCityCountry;
	private LegalReason legalReason;
	private int applicationType;
	public InputStream qrCode;


    public ApplicationDetailsForReportImpl(NacidDataProvider nacidDataProvider, Application application) {
		this.application = application;
		this.trainingCourse = application.getTrainingCourse();
		this.nacidDataProvider = nacidDataProvider;
		//System.out.println("Start of constructing AppliationDetailsForReport....");
		Person owner = trainingCourse.getOwner();

		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		
		Country citizenshipCountry = owner.getCitizenshipId() == null ? null : nomenclaturesDataProvider.getCountry(owner.getCitizenshipId());
		this.ownerCitizenship = citizenshipCountry == null ? "" : "гражданин на " + citizenshipCountry.getName();
		List<String> lst = new ArrayList<String>();
		if (!StringUtils.isEmpty(owner.getBirthCity())) {
			lst.add(owner.getBirthCity());
		}
		Country birthCountry = owner.getBirthCountry();
		if (birthCountry != null) {
			lst.add(birthCountry.getName());
			this.ownerBirthCountry = birthCountry.getName();
		}
		
		this.ownerBirthPlace = StringUtils.join(lst, ", ");
        diplomaType = trainingCourse.getDiplomaType();
        FlatNomenclature educationLevel = diplomaType == null ? null : diplomaType.getEducationLevel();
        OriginalEducationLevel diplomaOriginalEducationLevel = diplomaType == null ? null : diplomaType.getOriginalEducationLevel();



		this.date = DataConverter.formatDate(application.getApplicationDate(), "");
		//this.applicationNumber = application.getDocFlowNumber();
		this.ownerName = owner.getFullName();
		
		FlatNomenclature civilIdType = owner.getCivilIdType();
		this.ownerCivilId = StringUtils.isEmpty(owner.getCivilId()) ? "" : (civilIdType == null ? "" : civilIdType.getName())
				+ " " + owner.getCivilId();

		
		
		// Generating ownerAddressDetails
		lst = new ArrayList<String>();
		lst.add(application.getHomeCountry().getName());
		if (!StringUtils.isEmpty(application.getHomeCity())) {
			lst.add(application.getHomeCity());
		}
		if (!StringUtils.isEmpty(application.getHomeAddressDetails())) {
			lst.add(application.getHomeAddressDetails());
		}
		this.ownerAddressDetails = StringUtils.join(lst, ", ");
		// End of generating ownerAddressDetails

		this.applicationStatus = application.getApplicationStatus().getName();
		this.docflowStatus = application.getApplicationDocflowStatus().getName();

		this.diplomaEducationLevel = educationLevel == null ? "" : educationLevel.getName();
		this.diplomaEducationLevelId = diplomaType == null ? null : diplomaType.getEduLevelId();
        this.diplomaOriginalEducationLevel = diplomaOriginalEducationLevel == null ? "" : diplomaOriginalEducationLevel.getName();
        this.diplomaOriginalEducationLevelTranslated = diplomaOriginalEducationLevel == null ? "" : diplomaOriginalEducationLevel.getNameTranslated();

		List<TrainingCourseSpeciality> diplomaSpecialities = trainingCourse.getSpecialities();
		if (diplomaSpecialities != null) {
		    this.diplomaSpecialityNames = new ArrayList<String>();
            diplomaSpecialitiesForExpertPosition = new ArrayList<String>();
		    for (TrainingCourseSpeciality sp:diplomaSpecialities) {
                Speciality s = nomenclaturesDataProvider.getSpeciality(sp.getSpecialityId());
                FlatNomenclature os = sp.getOriginalSpecialityId() == null ? null : nomenclaturesDataProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_ORIGINAL_SPECIALITY, sp.getOriginalSpecialityId());
                diplomaSpecialityNames.add(s.getName());
                diplomaSpecialitiesForExpertPosition.add("<b>" + s.getName() + "</b>" + (os == null ? "" : "<br />" + os.getName()));
		    }
		    
		}
		multipleDiplomaSpecialities = diplomaSpecialities != null && diplomaSpecialities.size() > 1;
		 

		FlatNomenclature recEduLevel = trainingCourse.getRecognizedEducationLevel();
		this.recognizedEducationLevel = recEduLevel == null ? "" : recEduLevel.getName();

		Qualification recQualification = trainingCourse.getRecognizedQualification();
		this.recognizedQualification = recQualification == null ? "" : recQualification.getName();

		ProfessionGroup diplomaProfGroup = trainingCourse.getProfGroup();
		this.diplomaProfessionGroup = diplomaProfGroup == null ? "" : diplomaProfGroup.getName();
		ProfessionGroup profGroup = trainingCourse.getRecognizedProfGroup();
		this.recognizedProfessionGroup = profGroup == null ? "" : profGroup.getName();
		this.recognizedEducationArea = profGroup == null ? "" : profGroup.getEducationAreaName();

		

		/**
		 * generirane na adresa za kontakt v BG Ako zaqvitelq ne e podal adres
		 * za kontakt v BG, se gleda adresa na pylnomoshtnika, no ako
		 * pylnomoshtnika e firma, se gleda adresa na firmata No ako zaqvitelq i
		 * pylnomoshtnika imat adresi v 4ujbina, pismoto se adresira do
		 * zaqvitelq!!!!!
		 */
		Company representativeCompany = application.getCompany();
		if (application.homeIsBg() && !StringUtils.isEmpty(application.getHomeAddressDetails()) && !StringUtils.isEmpty(application.getHomeCity())) {
			// Ako osnovniq adres na zaqvitelq e v BG, dannite se vzemat ot tam
			this.addressDetailsBulgaria = application.getHomeAddressDetails();
			this.cityDetailsBulgaria = application.getHomeCity();
		} else if (!StringUtils.isEmpty(application.getBgAddressDetails()) && !StringUtils.isEmpty(application.getBgCity())) {
			// ako ima vyvedeni danni za adres v BG se vzemat tqh
			this.addressDetailsBulgaria = application.getBgAddressDetails();
			this.cityDetailsBulgaria = application.getBgCity();
		} else if (representativeCompany != null && representativeCompany.getCountryId() == NomenclaturesDataProvider.COUNTRY_ID_BULGARIA
				&& !StringUtils.isEmpty(representativeCompany.getAddressDetails()) && !StringUtils.isEmpty(representativeCompany.getCityName())) {
			// ako predstavitelq e firma, i tq e dala adres za korespondenciq v
			// BG, togava se vzemat dannite na firmata
			this.addressDetailsBulgaria = representativeCompany.getAddressDetails();
			this.cityDetailsBulgaria = representativeCompany.getCityName();
			;
		} else if (application.getReprCountryId() != null && application.getReprCountryId() == NomenclaturesDataProvider.COUNTRY_ID_BULGARIA
				&& !StringUtils.isEmpty(application.getReprAddressDetails()) && !StringUtils.isEmpty(application.getReprCity())) {
			// ako predstavitelq e dal adres v BG
			this.addressDetailsBulgaria = application.getReprAddressDetails();
			this.cityDetailsBulgaria = application.getReprCity();
		} else if (!StringUtils.isEmpty(application.getHomeAddressDetails()) && !StringUtils.isEmpty(application.getHomeCity())) {
			// ako ima vyveden adres za korespondenciq sys zaqvitelq bez
			// zna4enie ot dyrjavata se vzema nego!
			this.addressDetailsBulgaria = application.getHomeAddressDetails();
			this.cityDetailsBulgaria = application.getHomeCity() + ", " + application.getHomeCountry().getName();
		} else if (representativeCompany != null && !StringUtils.isEmpty(representativeCompany.getAddressDetails())
				&& !StringUtils.isEmpty(representativeCompany.getCityName())) {
			// ako predstavitelq e firma, koqto ve4e ima adres v 4ujbina, togava
			// se vzemat dannite na firmata
			this.addressDetailsBulgaria = representativeCompany.getAddressDetails();
			this.cityDetailsBulgaria = representativeCompany.getCityName() + ", " + representativeCompany.getCountry().getName();
		} else {
			// edinstvenata ni nadejda e da ima adres za korespondenciq s
			// predstavitelq, zashtoto ne ostanaha drugi vyzmojnosti :)....
			this.addressDetailsBulgaria = application.getReprAddressDetails() == null ? "" : application.getReprAddressDetails();
			this.cityDetailsBulgaria = (application.getReprCity() == null ? "" : application.getReprCity() + ", ")
					+ (application.getReprCountry() == null ? "" : application.getReprCountry().getName());
		}
		// Krai na generirane na adresite za kontakt v BG...

		this.ownerLastName = owner.getLName();
		this.ownerBirthDate = DataConverter.formatDate(owner.getBirthDate());
		this.diplomaNumber = trainingCourse.getDiplomaNumber();
		FlatNomenclature qualification = trainingCourse.getQualification();
		this.trainingCourseQualification = qualification == null ? "" : qualification.getName();
		this.trainingDuration = (trainingCourse.getTrainingDuration() == null || trainingCourse.getDurationUnit() == null) ? "" : "брой " + trainingCourse.getDurationUnit().getName() + " "
				+ DataConverter.formatFloatingNumber(trainingCourse.getTrainingDuration(), 2);

		
		this.motives = DataConverter.parseString(application.getSummary(), "");

		this.diplomaDate = DataConverter.formatDate(trainingCourse.getDiplomaDate());
		this.universities = trainingCourse.getUniversityWithFaculties();
		//System.out.println("end of constructing application details for report....");

        List<AppStatusHistory> statusHistory = application.getAppStatusHistory();
        universityAuthorized = statusHistory.stream().anyMatch(appStatusHistory -> appStatusHistory.getApplicationStatusId() == ApplicationStatus.APPLICATION_LEGITIMNO_PO_SEDALISHTE_STATUS_CODE);
        legitimateProgram = statusHistory.stream().anyMatch(appStatusHistory -> appStatusHistory.getApplicationStatusId() == ApplicationStatus.APPLICATION_LEGITIMNA_PROGRAMA_STATUS_CODE);

        List<String> trainingInstitutionForExpertPosition = new ArrayList<String>();
        List<String> trainingInstitution = new ArrayList<>();
        List<String> trainingInstitutionLocations = new ArrayList<>();



        List<? extends TrainingCourseTrainingLocation> tls = trainingCourse.getTrainingCourseTrainingLocations();
        if (tls != null) {
			trainingInstitutionNameCityCountry = tls.stream().map(r -> r.getTrainingInstitution()).filter(Objects::nonNull).map(r -> Arrays.asList(r.getName(), r.getCity(), r.getCountry() == null ? null : r.getCountry().getName()).stream().filter(Objects::nonNull).collect(Collectors.joining(", "))).collect(Collectors.joining("; "));
            for (TrainingCourseTrainingLocation tl : tls) {
                if (tl.getTrainingInstitutionId() != null) {
                    TrainingInstitution ti = tl.getTrainingInstitution();
                    trainingInstitutionForExpertPosition.add("<b>" + ti.getName() + "</b><br/>" + ti.getCountry().getName());
                    trainingInstitution.add(ti.getName());
					trainingInstitutionLocations.add(ti.getCountry().getName());
                }
            }
        }

        if (trainingInstitutionForExpertPosition.size() > 0) {
            this.trainingInstitutionForExpertPosition = StringUtils.join(trainingInstitutionForExpertPosition, "<br />");
        }
        if (trainingInstitution.size() > 0) {
            this.trainingInstitution = StringUtils.join(trainingInstitution, ", ");
        }
        if (trainingInstitutionLocations.size() > 0) {
        	this.trainingInstitutionLocation = StringUtils.join(trainingInstitutionLocations, ", ");
		}

        DiplomaExamination de = trainingCourse.getDiplomaExamination();
        competentInstitutionName = de == null || de.getCompetentInstitutionId() == null ? "" : de.getCompetentInstitution().getName();

        this.thesisTopic = trainingCourse.getThesisTopic();
        this.thesisTopicEn = trainingCourse.getThesisTopicEn();
        this.responsibleUser = application.getResponsibleUserId() == null ? "" : nacidDataProvider.getUsersDataProvider().getUser(application.getResponsibleUserId()).getFullName();

        List<AppStatusHistory> ash = application.getAppStatusHistory();
		this.legalReason = ash == null ? null : ash.get(0).getStatLegalReason();
		this.applicationType = application.getApplicationType();

    }
	public String getId() {
		return application.getId() + "";
	}
	public String getDate() {
		return date;
	}

	public String getApplicationNumber() {
		return application.getApplicationNumber();
	}

	public String getOwnerName() {
		return ownerName;
	}

	public String getOwnerCivilId() {
		return ownerCivilId;
	}

	/**
	 * При една специалност, връща името на специалността (пример:Химия)
	 * при много специалности връща
	 * първа специалност: химия, втора специалност: физика и т.н.
	 */
	public String getDiplomaSpecialityNames() {
		if (diplomaSpecialityNames != null && diplomaSpecialityNames.size() > 0) {
            if (diplomaSpecialityNames.size() == 1) {
                return diplomaSpecialityNames.get(0);
            } else {
                UtilsDataProvider utilsDataProvider = nacidDataProvider.getUtilsDataProvider();
                List<String> lst = new ArrayList<String>();
                for (int i = 0; i < diplomaSpecialityNames.size(); i++) {
                    String recognizedSpecialityId = utilsDataProvider.getCommonVariableValue((i + 1) + "diplomaSpeciality");
                    lst.add(recognizedSpecialityId + ": " + diplomaSpecialityNames.get(i));
                }
                return StringUtils.join(lst, ", ");
            }
        }
        return "";
	}
	
	public boolean isMultipleDiplomaSpecialities() {
        return multipleDiplomaSpecialities;
    }
    public String getCommissionSessionDate() {
		CommissionCalendarDataProvider commissionCalendarDataProvider = nacidDataProvider.getCommissionCalendarDataProvider();
		CommissionCalendar commissionCalendar = commissionCalendarDataProvider.getLastCommissionExaminedApplication(application.getId());
		String commissionSessionDate = "";
		if (commissionCalendar != null) {
			commissionSessionDate = DataConverter.formatDate(commissionCalendar.getDateAndTime());
		}
		//this.commissionSessionDate = "00.00.0000";
		return commissionSessionDate;
	}

	public String getDiplomaEducationLevel() {
		return diplomaEducationLevel;
	}

	public String getAddressDetailsBulgaria() {
		return addressDetailsBulgaria;
	}

	public String getCityDetailsBulgaria() {
		return cityDetailsBulgaria;
	}

	public String getOwnerLastName() {
		return ownerLastName;
	}

	public String getRecognizedEducationLevel() {
		return recognizedEducationLevel;
	}

	public String getRecognizedSpecialityName() {
		return getRecognizedSpeciality(false);
	}

	public String getRecognizedSpecialityNameForCertificate() {
		return getRecognizedSpeciality(true);
	}

	private String getRecognizedSpeciality(boolean forCertificate) {
		List<Speciality> recognizedSpecialities = trainingCourse.getRecognizedSpecialities();
		if (recognizedSpecialities != null && recognizedSpecialities.size() > 0) {
			if (recognizedSpecialities.size() == 1) {
				String specialityName = recognizedSpecialities.get(0).getName();
				return forCertificate ? specialityName : "специалността " + specialityName;
			} else {
				UtilsDataProvider utilsDataProvider = nacidDataProvider.getUtilsDataProvider();
				List<String> lst = new ArrayList<String>();
				for (int i = 0; i < recognizedSpecialities.size(); i++) {
					String recognizedSpecialityId = utilsDataProvider.getCommonVariableValue((i + 1) + "recognizedSpeciality");
					lst.add(recognizedSpecialityId + ": " + recognizedSpecialities.get(i).getName());
				}
				return StringUtils.join(lst, ", ");
			}
		}
		return "";
	}
	

	public String getOwnerBirthPlace() {
		return ownerBirthPlace;
	}

	public String getOwnerCitizenship() {
		return ownerCitizenship;
	}

	public String getOwnerAddressDetails() {
		return ownerAddressDetails;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}

	public String getDocflowStatus() {
		return docflowStatus;
	}

	public String getOwnerBirthDate() {
		return ownerBirthDate;
	}

	public String getDiplomaNumber() {
		return diplomaNumber;
	}

	public String getTrainingCourseQualification() {
		return trainingCourseQualification;
	}

	public String getTrainingDuration() {
		return trainingDuration;
	}

    @Override
    public String getTrainingStart() {
        return DataConverter.formatYear(trainingCourse.getTrainingStart());
    }

    @Override
    public String getTrainingEnd() {
        return DataConverter.formatYear(trainingCourse.getTrainingEnd());
    }

    @Override
    public String getTrainingForm() {
        TrainingCourseTrainingForm tf = trainingCourse.getTrainingCourseTrainingForm();
        return tf == null ? "" : tf.getTrainingFormName();
    }

    @Override
    public String getGraduationWay() {
        List<TrainingCourseGraduationWay> gws = trainingCourse.getTrainingCourseGraduationWays();
        if (gws != null && gws.size() > 0) {
            List<String> result = new ArrayList<String>();
            for (TrainingCourseGraduationWay gw : gws) {
                result.add(gw.getGraduationWayName());
            }
            return StringUtils.join(result, "<br />");
        }
        return null;
    }

    public String getTrainingYears() {
		return trainingCourse.getTrainingStart() != null || trainingCourse.getTrainingEnd() != null ? DataConverter.formatYear(trainingCourse.getTrainingStart()) + " - " + DataConverter.formatYear(trainingCourse.getTrainingEnd()) : "";
	}
	
	public String getCommissionStatuses() {
		List<AppStatusHistory> history = application.getCommissionAppStatusHistory();
		List<String> result = new ArrayList<String>();
		if (history != null) {
			for (AppStatusHistory h:history) {
				result.add("Дата:" + DataConverter.formatDate(h.getDateAssigned()) + " Статус:" + h.getLongApplicationStatusName());
			}
		}
		return StringUtils.join(result, ";\n");
	}
	public String getCommissionCalendars() {
		List<CommissionCalendar> commissionCalendars = application.getCommissionCalendars();
		List<String> result = new ArrayList<String>();
		if (commissionCalendars != null) {
			for (CommissionCalendar c:commissionCalendars) {
				result.add(c.getSessionNumber() + " от " + DataConverter.formatDateTime(c.getDateAndTime(), false));
			}
		}
		return StringUtils.join(result, ";\n");
	}
	/**
	 * предходно образование: държава, висше училище, година, специалност, ОКС;
	 */
	public String getPreviousEducation() {
		List<String> lst = new ArrayList<String>();
		if (trainingCourse.getPrevDiplomaUniversityId() != null) {
			University prevUniversity = trainingCourse.getPrevDiplomaUniversity();
			if (prevUniversity.getCountry() != null) {
				lst.add(prevUniversity.getCountry().getName());	
			} else {
				lst.add("");
			}
			lst.add(prevUniversity.getBgName());
			lst.add(DataConverter.formatYear(trainingCourse.getPrevDiplomaGraduationDate(), ""));
			lst.add(trainingCourse.getPrevDiplomaSpecialityId() == null ? "" : trainingCourse.getPrevDiplomaSpeciality().getName());
			lst.add(trainingCourse.getPrevDiplomaEduLevelId() == null ? "" : trainingCourse.getPrevDiplomaEduLevel().getName());
		}
		return StringUtils.join(lst, ",");
	}

	public String getMotives() {
		return motives;
	}

	public String getDiplomaDate() {
		return diplomaDate;
	}

	/**
	 * pri generirane na mnogo udostovereniq nakup, certificateNumber i qrCode trqbva da sa fields, a ne parameters!!!! Za celta v TemplateGenerator-a, te se setvat!
	 * @param certificateNumber
	 */
	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}
	public String getCertificateNumber() {
		if (certificateNumber == null) {
			certificateNumber = application.getCertificateNumber();
		}
		return certificateNumber;
	}
	public void setQrCode(InputStream qrCode) {
		this.qrCode = qrCode;
	}
	public InputStream getQrCode() {
		return qrCode;
	}

	public String getAllUniversitiesNamesAndLocationsForCertificate() {
		return getUniversitiesNamesAndLocations(universities, " в ", ", ");
	}
	public String getAllUniversitiesNamesAndLocationsForDoctorateCertificate() {
		return getUniversitiesNamesAndLocations(universities, ", ", "\n");
	}

	public String getAllUniversitiesNamesAndLocations() {
		return getUniversitiesNamesAndLocations(universities, ", ", "; ");
	}

    @Override
    public String getAllUniversitiesBgNames() {
        return universities == null ? "" : universities.stream().map(UniversityWithFaculty::getUniversity). map(University::getBgName).collect(Collectors.joining(", "));
    }

    @Override
    public String getAllUniversitiesFullNames() {
        return universities == null ? "" : universities.stream().map(UniversityWithFaculty::getUniversity). map(u -> u.getBgName() + (StringUtils.isEmpty(u.getOrgName()) ? "" : " <b>(" + u.getOrgName() + ")</b> ")).collect(Collectors.joining(", "));
    }

    private static String getUniversitiesNamesAndLocations(List<? extends UniversityWithFaculty> universities, String separatorBetweenUniversityNameAndCountry,
			String universitySeparator) {
		if (universities != null) {
			List<String> lst = new ArrayList<String>();
			for (UniversityWithFaculty uf : universities) {
				University u = uf.getUniversity();
				Country c = u.getCountry();
				lst.add(u.getBgName() + (c == null ? "" : separatorBetweenUniversityNameAndCountry + c.getName()));
			}
			return StringUtils.join(lst, universitySeparator);
		}
		return "";
	}

	public String getTrainingCountries() {
		List<? extends TrainingCourseTrainingLocation> trainingLocations = trainingCourse.getTrainingCourseTrainingLocations();
		return generateTrainingLocations(trainingLocations, false, null, ", ");
	}
	public String getTrainingCountriesAndLocations() {
		return generateTrainingLocations(trainingCourse.getTrainingCourseTrainingLocations(), true, ", ", "; ");
	}
	/**
	 * @param locations
	 * @param addCity - true - dobavq grada, false - vry6ta samo dyrjavata
	 * @param separatorBetweenCityAndCountry - ako addCity == false, tozi parametyr ne igrae, inache razdelq grada i dyrjavata
	 * @param separatorBetweenLocations - razdelitel mejdu 2 mestopolujeniq
	 * @return
	 */
	private static String generateTrainingLocations(List<? extends TrainingCourseTrainingLocation> locations, boolean addCity, String separatorBetweenCityAndCountry, String separatorBetweenLocations) {
		Set<String> result = new LinkedHashSet<String>();
		if (locations != null) {
			for (TrainingCourseTrainingLocation tl : locations) {
				String city = tl.getTrainingLocationCity();
				Country c = tl.getTrainingLocationCountry();
				if (addCity) {
					if (!StringUtils.isEmpty(city) && c != null) {
						result.add(city + separatorBetweenCityAndCountry + c.getName());
					} else if (c != null) {
						result.add(c.getName());
					} else if (!StringUtils.isEmpty(city)) {
						result.add(city);
					}
				} else if (c != null) {
					result.add(c.getName());
				}

			}
		}
		return StringUtils.join(result, separatorBetweenLocations);
	}

	@Override
	public String getRecognizedQualification() {
		return recognizedQualification;
	}

	@Override
	public String getAllUniversitiesNames() {
		if (universities != null) {
			List<String> lst = new ArrayList<String>();
			for (UniversityWithFaculty uf : universities) {
				lst.add(uf.getUniversity().getBgName() + "; " + uf.getUniversity().getOrgName());
			}
			return StringUtils.join(lst, ", ");
		}
		return "";
	}

    public String getUniversityNamesForExpertPosition() {
        if (universities != null) {
            List<String> lst = new ArrayList<String>();
            for (UniversityWithFaculty uf : universities) {
            	University u = uf.getUniversity();
                lst.add("<b>" + u.getBgName() + "</b>" + (StringUtils.isEmpty(u.getOrgName())? "" : "<br />" + u.getOrgName()));
            }
            return StringUtils.join(lst, "<br />");
        }
        return "";
    }

    public String getCountryPublishedDiploma() {
		if (trainingCourse.isJointDegree()) {
			return "съвместна";
		}
		UniversityWithFaculty buf = trainingCourse.getBaseUniversityWithFaculty();
		University u = buf == null ? null : buf.getUniversity();
		Country c = u == null ? null :  u.getCountry();
		return c == null ? "" : c.getName();
	}

	public String getOriginalUniversitiesNameCityCountry() {
		if (universities != null) {
			List<String> lst = new ArrayList<String>();
			for (UniversityWithFaculty uf : universities) {
				University u = uf.getUniversity();
				String city = u.getCity();
				Country c = u.getCountry();
				lst.add(u.getOrgName() + ";" + u.getBgName() + "<br />" + (!StringUtils.isEmpty(city) ? city : "") + "<br />" + (c != null ? c.getName() : ""));
			}
			return StringUtils.join(lst, "<br /><br />");
		}
		return "";
	}

    public String getAllUniversityCountriesSeparatedByNewLine() {
        if (universities != null) {
            List<String> lst = new ArrayList<String>();
            for (UniversityWithFaculty u : universities) {
                lst.add(u.getUniversity().getCountry().getName());
            }
            return StringUtils.join(lst, "<br />");
        }
        return "";
    }
	public String getOwnerBirthCountry() {
		return ownerBirthCountry;
	}

	public boolean isJointDegree() {
		return trainingCourse.isJointDegree();
	}

	public String getDocFlowNumber() {
		return application.getDocFlowNumber();
	}

	public String getToday() {
		return DataConverter.formatDate(Utils.getToday());
	}
	
	

	
	public String getOwnerDiplomaName() {
		if (!application.differentApplicantAndDiplomaNames()) {
			return getOwnerName();
		}
		return trainingCourse.getFullName();
	}

	@Override
	public String getAllUniversitiesCountries() {
		if (universities == null) {
			return "";
		}
		List<String> result = new ArrayList<String>();
		for (UniversityWithFaculty u:universities) {
			Country c = u.getUniversity().getCountry();
			if (c != null) {
				result.add(c.getName());	
			}
			
		}
		return StringUtils.join(result, ", ");
	}
	public String getFirstExpertName() {
		if (firstExpertName == null) {
			getExpertNames();
		}
		return firstExpertName;
	}
	@Override
	public String getExpertNames() {
		if (members == null || firstExpertName == null) {
			List<String> result = new ArrayList<String>();
			List<ApplicationExpert> experts = application.getApplicationExperts();
			firstExpertName = "";
			if (experts != null && experts.size() > 0) {
				List<ComissionMember> members = new ArrayList<ComissionMember>();
				for (ApplicationExpert e:experts) {
					members.add(e.getExpert());
				}
				firstExpertName = members.get(0).getFullName();
				for (ComissionMember m:members) {
					result.add((StringUtils.isEmpty(m.getDegree()) ? "" : m.getDegree() + " ") + m.getFullName());
				}
			}
			this.members = StringUtils.join(result, "\n");
		}
		return members;
	}
	
	public String getLastCommissionCalendarInfo() {
		List<CommissionCalendar> commissionCalendars = application.getCommissionCalendars();
		String result = "";
		if (commissionCalendars != null) {
			for (CommissionCalendar c:commissionCalendars) {
				if (c.getSessionStatusId() == SessionStatus.SESSION_STATUS_PROVEDENO) {
					result += c.getSessionNumber() + ",";
					break;
				}
			}
		}
		if ("".equals(result)) {
			result = ",";
		}
		result += DataConverter.parseString(application.getSummary(), "") + ",";
		result += DataConverter.parseString(application.getSubmittedDocs(), "");
		
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
		ApplicationDetailsForReport applicationDetailsForReport = nacidDataProvider.getApplicationsDataProvider().getApplicationDetailsForReport(3963);
		System.out.println(applicationDetailsForReport.getOwnerNameForProtocol());
		/*FileOutputStream os = new FileOutputStream("c:/test.rtf");
		JasperReportGenerator generator = new JasperReportGenerator(JasperReportNames.JASPER_REPORT_SLUJEBNA_BELEJKA_PODADENO, ExportType.RTF, os);
		generator.setParams(nacidDataProvider.getUtilsDataProvider().getCommonVariablesAsMap());
		generator.export(Arrays.asList(applicationDetailsForReport));
		os.close();*/
	}

	public String getDiplomaName() {
		if (application.differentApplicantAndDiplomaNames()) {
			return trainingCourse.getFullName();
		}
		return getOwnerName();
	}
	public String getApplicantInfo() {
		return application.getApplicantInfo() == null ? "" : application.getApplicantInfo();
	}
	public String getNotes() {
		return application.getNotes() == null ? "" : application.getNotes();
	}
	public String getPreviousDiplomaNotes() {
		return trainingCourse.getPrevDiplomaNotes() == null ? "" : trainingCourse.getPrevDiplomaNotes();
	}
	public String getAllNotes() {
		List<String> result = new ArrayList<String>();
		if (!StringUtils.isEmpty(application.getNotes())) {
			result.add("Бележки към заявление:" + application.getNotes());
		}
		if (!StringUtils.isEmpty(trainingCourse.getPrevDiplomaNotes())) {
			result.add("Бележки към предходно образование:" + trainingCourse.getPrevDiplomaNotes());
		}
		if (!StringUtils.isEmpty(trainingCourse.getSchoolNotes())) {
			result.add("Бележки към средно образование:" + trainingCourse.getSchoolNotes());
		}
		Collection<UniversityExamination> uniExamination = trainingCourse.getUniversityExaminations();
		if (uniExamination != null) {
			Iterator<UniversityExamination> iterator = uniExamination.iterator();
			List<String> lst = new ArrayList<String>();
			while (iterator.hasNext()) {
				UniversityExamination ue = iterator.next();
				UniversityValidity uv = ue.getUniversityValidity();
				if (uv != null && (!StringUtils.isEmpty(uv.getNotes()) || !StringUtils.isEmpty(ue.getNotes()))) { //Ne sym siguren tova dali izobshto moje da e null??? - maj moje - ako nqkoj vyvede notes, bez da selektne UniversityValidity??? - probvah i togava notes ne se zapisvat - t.e. ne moje da ima universityExamination bez UniversityValidity 
					lst.add(uv.getUniversity().getBgName()  + ": " + (StringUtils.isEmpty(uv.getNotes()) ? "" : uv.getNotes()) + "  " + (StringUtils.isEmpty(ue.getNotes()) ? "" : ue.getNotes()) + ", ");		
				}
				
			}
			if (lst.size() > 0) {
				result.add("Бележки към проверка на висше училище:" + StringUtils.join(lst, "; "));	
			}
		}
		DiplomaExamination diplomaExamination = trainingCourse.getDiplomaExamination();
 		if (diplomaExamination != null && !StringUtils.isEmpty(diplomaExamination.getNotes())) {
 			result.add("Бележки към проверка на дипломата:" + diplomaExamination.getNotes());
 		}

 		List<ApplicationExpert> experts = application.getApplicationExperts();
 		if (experts != null) {
 			List<String> lst = new ArrayList<String>();
 			for (ApplicationExpert e:experts) {
 	 			if (!StringUtils.isEmpty(e.getNotes())) {
 	 				lst.add(e.getExpert().getFullName() + "  " + e.getNotes());	
 	 			}
 	 		}
 			if (lst.size() > 0) {
 				result.add("Бележки към разпределение на експерти:" + StringUtils.join(lst, ", "));	
 			}
 			
 		}



 		return StringUtils.join(result, ";\n");
	}
	
	public Application getApplication() {
		return application;
	}
	
	public String getOwnerNameForProtocol() {
		//pyrvo za result slaga imenata po diploma ili po pasport, ako syvpadat. Sled tova ako ima razli4ie, dobavq i tezi po pasport
		String result = getDiplomaName();
		if (application.differentApplicantAndDiplomaNames()) {
			result += "("  + getOwnerName() + ")";
		}
		return result;
	}

    public String getDiplomaOriginalEducationLevel() {
        return diplomaOriginalEducationLevel;
    }

    public String getDiplomaSpecialitiesForExpertPosition() {
        return diplomaSpecialitiesForExpertPosition == null ? "" : StringUtils.join(diplomaSpecialitiesForExpertPosition, "<br />");
    }

    @Override
    public String getBolognaCycle() {
        return diplomaType == null || diplomaType.getBolognaCycleId() == null ? "" : diplomaType.getBolognaCycle().getName();
    }

    @Override
    public String getEuropeanQualificationsFramework() {
        return diplomaType == null || diplomaType.getEuropeanQualificationsFrameworkId() == null ? "" : diplomaType.getEuropeanQualificationsFramework().getName();
    }

    @Override
    public String getNationalQualificationsFramework() {
        return diplomaType == null || diplomaType.getNationalQualificationsFrameworkId() == null ? "" : diplomaType.getNationalQualificationsFramework().getName();
    }

    @Override
    public String getBolognaCycleAccess() {
        return diplomaType == null || diplomaType.getBolognaCycleAccessId() == null ? "" : diplomaType.getBolognaCycleAccess().getName();
    }

    @Override
    public String getEuropeanQualificationsFrameworkAccess() {
        return diplomaType == null || diplomaType.getEuropeanQualificationsFrameworkAccessId() == null ? "" : diplomaType.getEuropeanQualificationsFrameworkAccess().getName();
    }

    @Override
    public String getNationalQualificationsFrameworkAccess() {
        return diplomaType == null || diplomaType.getNationalQualificationsFrameworkAccessId() == null ? "" : diplomaType.getNationalQualificationsFrameworkAccess().getName();
    }

    public boolean isUniversityAuthorized() {
        return universityAuthorized;
    }

    public boolean isLegitimateProgram() {
        return legitimateProgram;
    }

    public String getGraduationDocumentType() {
        return trainingCourse.getGraduationDocumentTypeId() == null ? "" : trainingCourse.getGraduationDocumentType().getName();
    }


    @Override
    public String getCreditHours() {
        return trainingCourse.getCreditHours() == null ? "" : trainingCourse.getCreditHours().toString();
    }


    public String getCredits() {
        List<String> result = new ArrayList<String>();
        if (trainingCourse.getCredits() != null) {
            result.add(trainingCourse.getCredits().intValue() + " National Credits");
        }
        if (trainingCourse.getEctsCredits() != null) {
            result.add(trainingCourse.getEctsCredits() + " ECTS");
        }
        return StringUtils.join(result, " / ");
    }

    public String getUniversitiesForExpertPosition() {
        if (universities != null) {
            List<String> result = new ArrayList<String>();
            for (UniversityWithFaculty uf : universities) {
				University university = uf.getUniversity();
                result.add("<b>" + university.getBgName() + "</b>" + (!StringUtils.isEmpty(university.getOrgName()) ? "<br />" + university.getOrgName() : ""));
            }
            return StringUtils.join(result, "<br />");
        }
        return "";
    }
    public String getFacultiesForExpertPosition() {
		if (universities != null) {
			return universities.stream().map(UniversityWithFaculty::getFaculty).filter(Objects::nonNull).map(r -> "<b>" + r.getName() + "</b>" + (!StringUtils.isEmpty(r.getOriginalName()) ? "<br />" + r.getOriginalName() :"")).collect(Collectors.joining("<br />"));
		} else {
			return "";
		}
	}

    public String getTrainingInstitutionForExpertPosition() {
        return trainingInstitutionForExpertPosition;
    }

    public String getOutgoingNumber() {
        return application.getOutgoingNumber();
    }
    public String getInternalNumber() {
        return application.getInternalNumber();
    }


    public String getStatuteAuthenticityRecommendationLetterNumber() {
        return (!StringUtils.isEmpty(getOutgoingNumber()) ? "изх. № " + getOutgoingNumber() + " и " : "") + "вх. № в НАЦИД " + getDocFlowNumber() ;

    }

    public String getTrainingInstitution() {
        return trainingInstitution;
    }

    public String getCompetentInstitutionName() {
        return competentInstitutionName;
    }

	public String getDiplomaProfessionGroup() {
		return diplomaProfessionGroup;
	}

	public String getRecognizedProfessionGroup() {
		return recognizedProfessionGroup;
	}

	public String getDiplomaOriginalEducationLevelTranslated() {
		return diplomaOriginalEducationLevelTranslated;
	}

	public String getThesisTopic() {
		return thesisTopic;
	}

	public String getThesisTopicEn() {
		return thesisTopicEn;
	}

	public String getResponsibleUser() {
		return responsibleUser;
	}

	public Integer getDiplomaEducationLevelId() {
		return diplomaEducationLevelId;
	}

	public String getRecognizedEducationArea() {
		return recognizedEducationArea;
	}

	public String getTrainingInstitutionLocation() {
		return trainingInstitutionLocation;
	}

	public TrainingCourse getTrainingCourse() {
		return trainingCourse;
	}

	public String getTrainingInstitutionNameCityCountry() {
		return trainingInstitutionNameCityCountry;
	}

	@Override
	public String getLegalReasonOrdinanceArticle() {
		return legalReason == null ? null : legalReason.getOrdinanceArticle();
	}

	@Override
	public String getLegalReasonRegulationArticle() {
		return legalReason == null ? null : legalReason.getRegulationArticle();
	}

	@Override
	public String getLegalReasonRegulationText() {
		return legalReason == null || trainingCourse.getEducationLevelId() == null ? null : legalReason.getRegulationTextPerEducationLevelId(trainingCourse.getEducationLevelId());
	}

	public Integer getApplicationType() {
		return applicationType;
	}
}
