package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.OrderCriteria;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.nomenclatures.regprof.*;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.nomenclatures.regprof.*;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.data.nomenclatures.*;
import com.nacid.data.nomenclatures.regprof.*;
import com.nacid.db.nomenclatures.NomenclaturesDB;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class NomenclaturesDataProviderImpl implements NomenclaturesDataProvider {
	private NacidDataProviderImpl nacidDataProvider = null;
	private NomenclaturesDB db;
	private  Map<Integer, Map<Integer, FlatNomenclature>> cachedFlatNomenclatures = new HashMap<Integer, Map<Integer,FlatNomenclature>>();
	private Map<Integer, Country> cachedCountries = new HashMap<Integer, Country>();
	private Map<Integer, Speciality> cachedSpecialities = new HashMap<Integer, Speciality>();
	private Map<Integer, ProfessionGroup> cachedProfessionGroups = new HashMap<Integer, ProfessionGroup>();
	private HashMap<Integer, SecondaryProfessionalQualification> cachedSecondaryProfessionalQualifications = new HashMap<Integer,SecondaryProfessionalQualification>();
	private Map<Integer, DocumentType> cachedDocumentTypes = new HashMap<Integer, DocumentType>();
	private Map<Integer, SecondarySpeciality> cachedSecondarySpecialities = new HashMap<Integer, SecondarySpeciality>();
	private Map<Integer, ProfessionExperienceDocumentType> professionExperienceDocumentTypes = new HashMap<Integer, ProfessionExperienceDocumentType>();
	private Map<Integer, ServiceType> cachedServiceTypes = new HashMap<Integer, ServiceType>();
	private Map<Integer, RegprofArticleItem> cachedRegprofArticleItems = new HashMap<Integer, RegprofArticleItem>();
	private Map<Integer, SecondaryProfessionGroup> cachedSecondaryProfessionGroups = new HashMap<Integer, SecondaryProfessionGroup>();
    private Map<Integer, Map<Integer, ApplicationStatus>> cachedApplicationStatuses = new HashMap<Integer, Map<Integer, ApplicationStatus>>();
    private Map<Integer, Map<Integer, ApplicationDocflowStatus>> cachedApplicationDocflowStatuses = new HashMap<Integer, Map<Integer, ApplicationDocflowStatus>>();
    private Map<Integer, OriginalEducationLevel> cachedOriginalEducationLevels = new HashMap<Integer, OriginalEducationLevel>();
	private static Map<Integer, Class<? extends FlatNomenclatureRecord>> nomenclatureRecords = new HashMap<Integer, Class<? extends FlatNomenclatureRecord>>();
    private Map<Integer, NationalQualificationsFramework> cachedNationaQualificationFrameworks = new HashMap<Integer, NationalQualificationsFramework>();
    private Map<Integer, City> cachedCities = new HashMap<Integer, City>();
    private Map<Integer, GraoCity> cachedGraoCities = new HashMap<Integer, GraoCity>();
    private Map<Integer, ExpertPosition> cachedExpertPositions = new HashMap<Integer, ExpertPosition>();
    private Map<Integer, DocumentReceiveMethod> cachedDocumentReceiveMethods = new HashMap<>();
    private Map<Integer, Language> cachedLanguages = new HashMap<>();

	static {
		nomenclatureRecords.put(FLAT_NOMENCLATURE_EDUCATION_AREA, EducationAreaRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_EDUCATION_LEVEL, EducationLevelRecord.class);
		nomenclatureRecords.put(NOMENCLATURE_SPECIALITY, SpecialityRecord.class);
		nomenclatureRecords.put(NOMENCLATURE_PROFESSION_GROUP, ProfessionGroupRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_COMMISSION_POSITION, CommissionPositionRecord.class);
		nomenclatureRecords.put(NOMENCLATURE_COUNTRY, CountryRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_QUALIFICATION, QualificationRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_RECOGNITION_PURPOSE, RecognitionPurposeRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_TRAINING_FORM, TrainingFormRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_GRADUATION_WAY, GraduationWayRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_CIVIL_ID_TYPE, CivilIdTypeRecord.class) ;
		nomenclatureRecords.put(FLAT_NOMENCLATURE_DURATION_UNIT, DurationUnitRecord.class) ;
		nomenclatureRecords.put(FLAT_NOMENCLATURE_COPY_TYPE, CopyTypeRecord.class);
		nomenclatureRecords.put(NOMENCLATURE_DOCUMENT_TYPE, DocumentTypeRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_APPLICATION_SESSION_STATUS, ApplicationSessionStatusRecord.class);
		nomenclatureRecords.put(NOMENCLATURE_APPLICATION_STATUS, ApplicationStatusRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_TRAINING_LOCATION, TrainingLocationRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_SESSION_STATUS, SessionStatusRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_DOC_CATEGORY, DocCategoryRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_EVENT_STATUS, EventStatusRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_PROFESSIONAL_INSTITUTION_TYPE, ProfessionalInstitutionTypeRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_QUALIFICATION_DEGREE, QualificationDegreeRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_SECONDARY_CALIBER, SecondaryCaliberRecord.class);
		nomenclatureRecords.put(NOMENCLATURE_SECONDARY_PROFESSION_GROUP, SecondaryProfessionGroupRecord.class);
		nomenclatureRecords.put(NOMENCLATURE_SECONDARY_PROFESSIONAL_QUALIFICATION, SecondaryProfessionalQualificationRecord.class);
		nomenclatureRecords.put(NOMENCLATURE_SECONDARY_SPECIALITY, SecondarySpecialityRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_PROFESSION, ProfessionRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_REGPROF_ARTICLE_DIRECTIVE, RegprofArticleDirectiveRecord.class);
		//nomenclatureRecords.put(FLAT_NOMENCLATURE_LEGAL_REASON, LegalReasonRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_EDUCATION_TYPE, EducationTypeRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE, ProfessionExperienceRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION, HigherProfessionalQualificationRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_HIGHER_SPECIALITY, HigherSpecialityRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_EDUCATION_DOCUMENT_TYPE, EducationDocumentTypeRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE_DOCUMENT_TYPE, ProfessionExperienceDocumentTypeRecord.class);
		nomenclatureRecords.put(NOMENCLATURE_REGPROF_ARTICLE_ITEM, RegprofArticleItemRecord.class);
		nomenclatureRecords.put(NOMENCLATURE_SERVICE_TYPE, ServiceTypeRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_CERTIFICATE_PROF_QUALIFICATION, CertificateProfessionalQualificationRecord.class);
		nomenclatureRecords.put(FLAT_NOMENCLATURE_PAYMENT_TYPE, PaymentTypeRecord.class);
        nomenclatureRecords.put(NOMENCLATURE_ORIGINAL_EDUCATION_LEVEL, OriginalEducationLevelRecord.class);
        nomenclatureRecords.put(FLAT_NOMENCLATURE_ORIGINAL_SPECIALITY, OriginalSpecialityRecord.class);
        nomenclatureRecords.put(NOMENCLATURE_NATIONAL_QUALIFICATIONS_FRAMEWORK, NationalQualificationsFrameworkRecord.class);
        nomenclatureRecords.put(FLAT_NOMENCLATURE_EUROPEAN_QUALIFICATIONS_FRAMEWORK, EuropeanQualificationsFrameworkRecord.class);
        nomenclatureRecords.put(FLAT_NOMENCLATURE_BOLOGNA_CYCLE, BolognaCycleRecord.class);
        nomenclatureRecords.put(FLAT_NOMENCLATURE_GRADUATION_DOCUMENT_TYPE, GraduationDocumentTypeRecord.class);
        nomenclatureRecords.put(NOMENCLATURE_EXPERT_POSITION, ExpertPositionRecord.class);
        nomenclatureRecords.put(FLAT_NOMENCLATURE_GRADE, GradeRecord.class);
        nomenclatureRecords.put(FLAT_NOMENCLATURE_AGE_RANGE, AgeRangeRecord.class);
        nomenclatureRecords.put(FLAT_NOMENCLATURE_SCHOOL_TYPE, SchoolTypeRecord.class);
        nomenclatureRecords.put(FLAT_NOMENCLATURE_BG_ACAD_REC_STATUS, BgAcademicRecognitionStatusRecord.class);
        nomenclatureRecords.put(FLAT_NOMENCLATURE_PERSONAL_ID_DOCUMENT_TYPE, PersonalIdDocumentTypeRecord.class);
        nomenclatureRecords.put(FLAT_NOMENCLATURE_UNIVERSITY_GENERIC_NAME, UniversityGenericNameRecord.class);
        nomenclatureRecords.put(NOMENCLATURE_LANGUAGE, LanguageRecord.class);
        nomenclatureRecords.put(FLAT_NOMENCLATURE_ORIGINAL_QUALIFICATION, OriginalQualificationRecord.class);
	}
    public NomenclaturesDB getNomenclaturesDB() {
        return db;
    }

	public int saveFlatNomenclature(int nomenclatureType, int id, String name, Date dateFrom, Date dateTo) {
		Class<?> cls = nomenclatureRecords.get(nomenclatureType);
		try {
			FlatNomenclatureRecord o = (FlatNomenclatureRecord) cls.getConstructor(int.class, String.class, java.sql.Date.class, java.sql.Date.class ).newInstance(id, name, dateFrom == null ? null : new java.sql.Date(dateFrom.getTime()), dateTo == null ? null : new java.sql.Date(dateTo.getTime()));
			int res = saveNomenclature(o);
			resetCachedFlatNomenclatures(nomenclatureType);
			return res;
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Problem calling (int, String, date, date) constructor of record" + cls.getName());
		} catch (SecurityException e) {
			throw new IllegalArgumentException("Problem calling (int, String, date, date) constructor of record" + cls.getName());
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Problem calling (int, String, date, date) constructor of record" + cls.getName());
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Problem calling (int, String, date, date) constructor of record" + cls.getName());
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException("Problem calling (int, String, date, date) constructor of record" + cls.getName());
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("Problem calling (int, String, date, date) constructor of record" + cls.getName());
		}
	}

	public NomenclaturesDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
		this.nacidDataProvider = nacidDataProvider;
		this.db = new NomenclaturesDB(nacidDataProvider.getDataSource());
		resetAllNomenclatures();
		//Date date = new Date();
		//System.out.println("Started caching nomenclatures...");

		//resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE_DOCUMENT_TYPE);
		//System.out.println("Finished caching nomenclatures...Time cost:" + (new Date().getTime() - date.getTime()) + " miliseconds...");
	}

	public void resetAllNomenclatures(){
	    resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_APPLICATION_SESSION_STATUS);
        //resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_APPLICATION_STATUS);
        resetCachedApplicationStatuses();
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_CIVIL_ID_TYPE);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_COMMISSION_POSITION);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_COPY_TYPE);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_DOC_CATEGORY);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_DURATION_UNIT);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_EDUCATION_AREA);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_EDUCATION_LEVEL);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_EVENT_STATUS);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_GRADUATION_WAY);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_QUALIFICATION);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_RECOGNITION_PURPOSE);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_SESSION_STATUS);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_TRAINING_FORM);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_TRAINING_LOCATION);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_PROFESSIONAL_INSTITUTION_TYPE);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_QUALIFICATION_DEGREE);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_SECONDARY_CALIBER);
        resetCachedSecondaryProfessionGroups();
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_PROFESSION);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_HIGHER_SPECIALITY);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_EDUCATION_DOCUMENT_TYPE);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_REGPROF_ARTICLE_DIRECTIVE);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE_DOCUMENT_TYPE);
        resetCachedSpecialities();
        resetCachedCountries();
        resetCachedLanguages();
        resetCachedDocumentTypes();
        resetCachedProfessionGroups();
        resetCachedRegprofArticleItems();
        resetCachedServiceTypes();
        resetCachedSecondaryProfessionalQualifications();
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_EDUCATION_TYPE);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE);
        resetCachedSecondarySpecialities();
        resetCachedProfessionExperienceDocumentTypes();
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_CERTIFICATE_PROF_QUALIFICATION);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_PAYMENT_TYPE);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_ORIGINAL_SPECIALITY);
        resetCachedApplicationDocflowStatuses();
        resetCachedOriginalEducationLevels();
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_BOLOGNA_CYCLE);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_EUROPEAN_QUALIFICATIONS_FRAMEWORK);
        resetCachedNationalQualificationsFrameworks();
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_GRADUATION_DOCUMENT_TYPE);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_GRADE);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_SCHOOL_TYPE);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_AGE_RANGE);
        resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_BG_ACAD_REC_STATUS);
        resetCities();
        resetGraoCities();
        resetExpertPositions();
		resetDocumentReceiveMethods();
		resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_PERSONAL_ID_DOCUMENT_TYPE);
		resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_UNIVERSITY_GENERIC_NAME);
		resetCachedFlatNomenclatures(FLAT_NOMENCLATURE_ORIGINAL_QUALIFICATION);
        nacidDataProvider.getNumgeneratorDataProvider().clearCache();
	}

    private void resetCachedFlatNomenclatures(int nomenclatureType) {
		List<FlatNomenclature> noms = getFlatNomenclatures(nomenclatureType, null, null);
		if (noms != null) {
			Map<Integer, FlatNomenclature> fnoms = new HashMap<Integer, FlatNomenclature>();
			for (FlatNomenclature f:noms) {
				fnoms.put(f.getId(), f);
			}
			cachedFlatNomenclatures.put(nomenclatureType, fnoms);
		}
	}

    private void resetCachedApplicationStatuses() {
        cachedApplicationStatuses = new HashMap<>();

        Stream<Integer> entryNumSeriesToCache = Stream.of(NumgeneratorDataProvider.AUTHENTICITY_SERIES_ID, NumgeneratorDataProvider.REGPROF_SERIES_ID, NumgeneratorDataProvider.NACID_SERIES_ID, NumgeneratorDataProvider.STATUTE_SERIES_ID, NumgeneratorDataProvider.RECOMMENDATION_SERIES_ID, NumgeneratorDataProvider.DOCTORATE_SERIES_ID);
        entryNumSeriesToCache.forEach(entryNumSeries -> {
            List<ApplicationStatus> applicationStatuses = getApplicationStatuses(entryNumSeries, null, null, false);
            if (applicationStatuses != null) {
                Map<Integer, ApplicationStatus> map =
                        applicationStatuses.stream().
                                collect(Collectors.toMap(rec -> rec.getId(), Function.identity()));
                cachedApplicationStatuses.put(entryNumSeries,map);

            }

        });


    }

    private void resetCachedApplicationDocflowStatuses() {
        cachedApplicationDocflowStatuses = new HashMap<Integer, Map<Integer, ApplicationDocflowStatus>>();
        List<ApplicationDocflowStatus> applicationStatuses = getApplicationDocflowStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, null, null);
        if (applicationStatuses != null) {
            HashMap<Integer, ApplicationDocflowStatus> map = new HashMap<Integer, ApplicationDocflowStatus>();
            cachedApplicationDocflowStatuses.put(NumgeneratorDataProvider.NACID_SERIES_ID, map);
            for (ApplicationDocflowStatus dt:applicationStatuses) {
                map.put(dt.getId(), dt);
            }
        }

        applicationStatuses = getApplicationDocflowStatuses(NumgeneratorDataProvider.REGPROF_SERIES_ID, null, null);
        if (applicationStatuses != null) {
            HashMap<Integer, ApplicationDocflowStatus> map = new HashMap<Integer, ApplicationDocflowStatus>();
            cachedApplicationDocflowStatuses.put(NumgeneratorDataProvider.REGPROF_SERIES_ID, map);
            for (ApplicationDocflowStatus dt:applicationStatuses) {
                map.put(dt.getId(), dt);
            }
        }

    }

    private void resetCachedProfessionExperienceDocumentTypes() {
        List<ProfessionExperienceDocumentType> documentTypes = getProfessionExperienceDocumentTypes(null, null);
        if (documentTypes != null) {
            for (ProfessionExperienceDocumentType dt:documentTypes) {
                professionExperienceDocumentTypes.put(dt.getId(), dt);
            }
        }
    }

	private void resetCachedCountries() {
		List<Country> countries = getCountries(null, null);
		if (countries != null) {
			for (Country c:countries) {
				cachedCountries.put(c.getId(), c);
			}
		}
	}

	private void resetCachedLanguages() {
		List<Language> noms = getLanguages(null, null);
		if (noms != null) {
			for (Language c:noms) {
				cachedLanguages.put(c.getId(), c);
			}
		}
	}

	private void resetCachedSpecialities() {
		List<Speciality> specialities = getSpecialities(0, null, null);
		if (specialities != null) {
			for (Speciality s:specialities) {
				cachedSpecialities.put(s.getId(), s);
			}
		}
	}
	private void resetCachedProfessionGroups() {
		List<ProfessionGroup> profGroups = getProfessionGroups(0, null, null);
		if (profGroups != null) {
			for (ProfessionGroup s:profGroups) {
				cachedProfessionGroups.put(s.getId(), s);
			}
		}
	}


	private void resetCachedSecondaryProfessionalQualifications() {
	    List<SecondaryProfessionalQualification> profQualifications = getSecondaryProfessionalQualifications(null, null, null);
	    if (profQualifications != null) {
            for (SecondaryProfessionalQualification spq : profQualifications) {
                cachedSecondaryProfessionalQualifications.put(spq.getId(), spq);
            }
        }
	}

	private void resetCachedDocumentTypes() {
		List<DocumentType> docTypes = getDocumentTypes(null, null, 0);
		if (docTypes != null) {
			for (DocumentType s:docTypes) {
				cachedDocumentTypes.put(s.getId(), s);
			}
		}
	}


	private void resetCachedSecondarySpecialities() {
	    List<SecondarySpeciality> secondarySpecialities = getSecondarySpecialities(null, null, null, null);
	    if (secondarySpecialities != null) {
	        for (SecondarySpeciality ss : secondarySpecialities) {
	            cachedSecondarySpecialities.put(ss.getId(), ss);
	        }
	    }
	}

	private void resetCachedServiceTypes() {
        List<ServiceType> serviceTypes = getServiceTypes(null, null, null);
        if (serviceTypes != null) {
            for (ServiceType ss : serviceTypes) {
                cachedServiceTypes.put(ss.getId(), ss);
            }
        }
    }
    private void resetCachedOriginalEducationLevels() {
        List<OriginalEducationLevel> originalEducationLevels = getOriginalEducationLevels(null, null);
        cachedOriginalEducationLevels = new HashMap<Integer, OriginalEducationLevel>();
        if (originalEducationLevels != null) {
            for (OriginalEducationLevel ss : originalEducationLevels) {
                cachedOriginalEducationLevels.put(ss.getId(), ss);
            }
        }
    }
    private void resetCities() {
        List<City> c = getCities();
        cachedCities = new HashMap<Integer, City>();
        for (City city : c) {
            cachedCities.put(city.getId(), city);
        }
    }
    private void resetExpertPositions() {
        List<ExpertPosition> positions = getExpertPositions();
        cachedExpertPositions = positions == null ? new HashMap<>() : positions.stream().collect(Collectors.toMap(rec -> rec.getId(), Function.identity()));
    }
    private void resetDocumentReceiveMethods() {
		List<DocumentReceiveMethod> recs = getDocumentReceiveMethods(null, null);
		cachedDocumentReceiveMethods = recs == null ? new HashMap<>() : recs.stream().collect(Collectors.toMap(DocumentReceiveMethod::getId, Function.identity()));
	}

    private void resetGraoCities() {
        List<GraoCity> c = getGraoCities();
        cachedGraoCities = new HashMap<Integer, GraoCity>();
        for (GraoCity city : c) {
            cachedGraoCities.put(city.getId(), city);
        }
    }
	private void resetCachedRegprofArticleItems() {
        List<RegprofArticleItem> artielcItems = getRegprofArticleItems(null, null, null);
        if (artielcItems != null) {
            for (RegprofArticleItem ai : artielcItems) {
                cachedRegprofArticleItems.put(ai.getId(), ai);
            }
        }
    }
	private void resetCachedSecondaryProfessionGroups() {
        List<SecondaryProfessionGroup> groups = getSecondaryProfessionGroups(null, null);
        if (groups != null) {
            for (SecondaryProfessionGroup g:groups) {
                cachedSecondaryProfessionGroups.put(g.getId(), g);
            }
        }
    }


    private void resetCachedNationalQualificationsFrameworks() {
        List<NationalQualificationsFramework> noms = getNationalQualificationsFrameworks(null, null, null);
        if (noms != null) {
            for (NationalQualificationsFramework nom:noms) {
                cachedNationaQualificationFrameworks.put(nom.getId(), nom);
            }
        }
    }
	//--------------------------------------------------------------------------------
	public List<Country> getCountries(Date toDate, OrderCriteria orderCriteria) {
		try {
			List<CountryRecord> records = db.getCountryRecords(toDate == null ? null : new java.sql.Date(toDate.getTime()), orderCriteria == null ? NomenclatureOrderCriteria.getDefaultOrderCriteria(CountryRecord.class) : orderCriteria);
			if (records.size() == 0) {
				return null;
			}
			List<Country> result = new ArrayList<Country>();
			for (CountryRecord r: records) {
				result.add(new CountryImpl(r));
			}
			return result;
		} catch (Exception e) {
			throw Utils.logException(this, e);
		}
	}

	public List<Language> getLanguages(Date toDate, OrderCriteria orderCriteria) {
		try {
			List<LanguageRecord> records = db.getFlatNomenclatureRecords(LanguageRecord.class, toDate == null ? null : new java.sql.Date(toDate.getTime()), orderCriteria == null ? NomenclatureOrderCriteria.getDefaultOrderCriteria(LanguageRecord.class) : orderCriteria);
			return records.stream().map(LanguageImpl::new).collect(Collectors.toList());
		} catch (Exception e) {
			throw Utils.logException(this, e);
		}
	}

	public List<ProfessionGroup> getProfessionGroups(int educationAreaId, Date toDate, OrderCriteria orderCriteria) {
		try {
			List<ProfessionGroupRecord> records = db.getProfessionGroupRecords(educationAreaId, toDate == null ? null : new java.sql.Date(toDate.getTime()), orderCriteria);
			if (records.size() == 0) {
				return null;
			}
			List<ProfessionGroup> result = new ArrayList<ProfessionGroup>();
			for (ProfessionGroupRecord r: records) {
				result.add(new ProfessionGroupImpl(r));
			}
			return result;
		} catch (Exception e) {
			throw Utils.logException(this, e);
		}
	}

	public List<Speciality> getSpecialities(Integer professionGroupId, Date toDate, OrderCriteria orderCriteria) {
		try {
			List<SpecialityRecord> records = db.getSpecialityRecords(professionGroupId, toDate == null ? null : new java.sql.Date(toDate.getTime()), orderCriteria);
			if (records.size() == 0) {
				return null;
			}
			List<Speciality> result = new ArrayList<Speciality>();
			for (SpecialityRecord r: records) {
				result.add(new SpecialityImpl(r));
			}
			return result;
		} catch (Exception e) {
			throw Utils.logException(this, e);
		}
	}

	public List<Speciality> getSpecialities(String partOfName,boolean  nameStartsWith, Integer professionGroupId, Date toDate, OrderCriteria orderCriteria) {
		try {
			if (partOfName != null && "".equals(partOfName)) {
				return null;
			}
			List<SpecialityRecord> records = db.getSpecialityRecords(partOfName, nameStartsWith, professionGroupId, toDate == null ? null : new java.sql.Date(toDate.getTime()), orderCriteria);
			if (records.size() == 0) {
				return null;
			}
			List<Speciality> result = new ArrayList<Speciality>();
			for (SpecialityRecord r: records) {
				result.add(new SpecialityImpl(r));
			}
			return result;
		} catch (Exception e) {
			throw Utils.logException(this, e);
		}
	}


	public List<SecondaryProfessionalQualification> getSecondaryProfessionalQualifications(Date toDate, OrderCriteria orderCriteria, Integer profGroup){
	    try{
            List<SecondaryProfessionalQualificationRecord> records = db.getSecondaryProfessionalQualificationRecords(toDate == null ? null : new java.sql.Date(toDate.getTime()), orderCriteria, profGroup);
            if (records.size() == 0) {
                return null;
            }
            List<SecondaryProfessionalQualification> result = new ArrayList<SecondaryProfessionalQualification>();
            for (SecondaryProfessionalQualificationRecord r: records) {
                result.add(new SecondaryProfessionalQualificationImpl(this, r));
            }
            return result;
        } catch (Exception e) {
            throw Utils.logException(this, e);
        }
	}

	public SecondaryProfessionalQualification getSecondaryProfessionalQualificationByName(String name) {
        try {

            SecondaryProfessionalQualificationRecord record = db.selectRecord(SecondaryProfessionalQualificationRecord.class, " UPPER(name) = UPPER(?) ", name);
            if (record != null) {
                return new SecondaryProfessionalQualificationImpl(this, record);
            }
        } catch (SQLException e) {
            throw Utils.logException(this, e);
        }
	    return null;
	}

	public List<DocumentType> getDocumentTypes(Date toDate, OrderCriteria orderCriteria, int docCategoryId) {
		try {
			List<DocumentTypeRecordExtended> records = db.getDocumentTypeRecords(toDate == null ? null : new java.sql.Date(toDate.getTime()), orderCriteria, null, docCategoryId == 0 ? null : Arrays.asList(docCategoryId));
			if (records.size() == 0) {
				return null;
			}
			List<DocumentType> result = new ArrayList<DocumentType>();
			for (DocumentTypeRecordExtended r: records) {
				result.add(new DocumentTypeImpl(nacidDataProvider, r));
			}
			return result;
		} catch (Exception e) {
			throw Utils.logException(this, e);
		}
	}

	public List<DocumentType> getDocumentTypes(Date toDate, OrderCriteria orderCriteria, Boolean hasDocumentTemplate, List<Integer> docCategoryIds) {
		try {
			List<DocumentTypeRecordExtended> records = db.getDocumentTypeRecords(toDate == null ? null : new java.sql.Date(toDate.getTime()), orderCriteria == null ? NomenclatureOrderCriteria.getDefaultOrderCriteria(DocumentTypeRecord.class) : orderCriteria, hasDocumentTemplate, docCategoryIds);
			if (records.size() == 0) {
				return null;
			}
			List<DocumentType> result = new ArrayList<DocumentType>();
			for (DocumentTypeRecordExtended r: records) {
				result.add(new DocumentTypeImpl(nacidDataProvider, r));
			}
			return result;
		} catch (Exception e) {
			throw Utils.logException(this, e);
		}
	}


	public ProfessionGroup getProfessionGroup(int professionGroupId) {
		return cachedProfessionGroups.get(professionGroupId);
		/*try {
			ProfessionGroupRecord r = db.getProfessionGroupRecord(professionGroupId);
			return r == null ? null : new ProfessionGroupImpl(r);
		} catch (Exception e) {
			throw Utils.logException(e);
		}*/
	}

	public int saveProfessionGroup(int id, String name, int educationAreaId, Date dateFrom, Date dateTo) {
		java.sql.Date df = dateFrom == null ? null : new java.sql.Date(dateFrom.getTime());
		java.sql.Date dt = dateTo == null ? null : new java.sql.Date(dateTo.getTime());
		ProfessionGroupRecord record = new ProfessionGroupRecord(id, name, educationAreaId, df, dt, null);
		int res = saveNomenclature(record);
		resetCachedProfessionGroups();
		return res;
	}

	public Speciality getSpeciality(int specialityId) {
		return cachedSpecialities.get(specialityId);
		/*try {
			SpecialityRecord r = db.getSpecialityRecord(specialityId);
			return r == null ? null : new SpecialityImpl(r);
		} catch (Exception e) {
			throw Utils.logException(e);
		}*/
	}

	public int saveSpeciality(int id, String name, Integer professionGroupId, Date dateFrom, Date dateTo) {
		java.sql.Date df = dateFrom == null ? null : new java.sql.Date(dateFrom.getTime());
		java.sql.Date dt = dateTo == null ? null : new java.sql.Date(dateTo.getTime());
		SpecialityRecord record = new SpecialityRecord(id, name, professionGroupId, df, dt, null);
		int res = saveNomenclature(record);
		resetCachedSpecialities();
		return res;
	}

	public Country getCountry(int countryId) {
		return cachedCountries.get(countryId);
		/*CountryRecord r = getNomenclatureRecord(new CountryRecord(), countryId);
		return r == null ? null : new CountryImpl(r);*/
	}

	public Language getLanguage(int id) {
		return cachedLanguages.get(id);
	}
    public Country getCountryByName(String countryName) {
        Optional<Country> result = cachedCountries.values().stream().filter(c -> c.getName().equalsIgnoreCase(countryName)).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    @Override
    public Country getCountry(String iso3166Code) {
        if (StringUtils.isEmpty(iso3166Code)) {
            return null;
        }
        for (Country country : cachedCountries.values()) {
            if (country.getIso3166Code().equals(iso3166Code)) {
                return country;
            }
        }
        return null;
    }

    public int saveCountry(int id, String name, String iso3166Code, String officialName, Date dateFrom, Date dateTo) {
		java.sql.Date df = dateFrom == null ? null : new java.sql.Date(dateFrom.getTime());
		java.sql.Date dt = dateTo == null ? null : new java.sql.Date(dateTo.getTime());
		CountryRecord record = new CountryRecord(id, name, iso3166Code, officialName, df, dt);
		int res = saveNomenclature(record);
		resetCachedCountries();
		return res;
	}

	public int saveLanguage(int id, String name, Date dateFrom, Date dateTo, String iso639Code) {
		LanguageRecord rec = new LanguageRecord(id, name, Utils.getSqlDate(dateFrom), Utils.getSqlDate(dateTo), iso639Code);
		int res = saveNomenclature(rec);
		resetCachedLanguages();
		return res;
	}

	public SecondaryProfessionalQualification getSecondaryProfessionalQualification(Integer profQualId){
	    return cachedSecondaryProfessionalQualifications.get(profQualId);
	}

	public int saveSecondaryProfessionalQualification(int id, String name,Integer profQualificationId, Date dateFrom, Date dateTo, String code){
	    java.sql.Date df = dateFrom == null ? null : new java.sql.Date(dateFrom.getTime());
        java.sql.Date dt = dateTo == null ? null : new java.sql.Date(dateTo.getTime());
        SecondaryProfessionalQualificationRecord record = new SecondaryProfessionalQualificationRecord(id, name, df, dt, profQualificationId, null, code);
        int res = saveNomenclature(record);
        resetCachedSecondaryProfessionalQualifications();
        return res;
	}

	public DocumentType getDocumentType(int documentTypeId) {
		return cachedDocumentTypes.get(documentTypeId);
		/*try {
			DocumentTypeRecord r = db.getDocumentTypeRecord(documentTypeId);
			return r == null ? null : new DocumentTypeImpl(this, r);
		} catch (Exception e) {
			throw Utils.logException(e);
		}*/

	}

	public int saveDocumentType(int id, String name, boolean isIncoming, boolean hasDocflowId, List<Integer> docCategoryIds, String documenTemplate, Date dateFrom, Date dateTo) {
		try {
			java.sql.Date df = dateFrom == null ? null : new java.sql.Date(dateFrom.getTime());
			java.sql.Date dt = dateTo == null ? null : new java.sql.Date(dateTo.getTime());
			DocumentTypeRecord record = new DocumentTypeRecord(id, name, df, dt, documenTemplate, isIncoming ? 1 : 0, hasDocflowId ? 1 : 0);
			int res = saveNomenclature(record);
			if (id != 0) {//update
				db.deleteRecords(DocumentTypeToDocumentCategoryRecord.class, "doc_type_id = ?", id);
			}
			if (docCategoryIds != null) {
				for (Integer i : docCategoryIds) {
					DocumentTypeToDocumentCategoryRecord r = new DocumentTypeToDocumentCategoryRecord(0, res, i);
					db.insertRecord(r);
				}
			}
			resetCachedDocumentTypes();
			return res;
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	public FlatNomenclature getFlatNomenclature(int nomenclatureId, int recordId) {
		return cachedFlatNomenclatures.get(nomenclatureId).get(recordId);
		/*Class<? extends FlatNomenclatureRecord> cls = nomenclatureRecords.get(nomenclatureId);
		FlatNomenclatureRecord o;
		try {
			o = cls.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Problem invocating empty constructor of " + cls.getName());
		}
		try {
			o = db.selectRecord(o, recordId);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
		return o == null ? null : generateFlatNomenclature(o);*/
	}


	public List<FlatNomenclature> getFlatNomenclatures(int nomenclatureId, Date toDate, OrderCriteria orderCriteria) {
		Class<? extends FlatNomenclatureRecord> cls = nomenclatureRecords.get(nomenclatureId);
		List<? extends FlatNomenclatureRecord> flatNomenclatures;
		try {
			flatNomenclatures = db.getFlatNomenclatureRecords(cls, toDate == null ? null : new java.sql.Date(toDate.getTime()), orderCriteria == null ? NomenclatureOrderCriteria.getDefaultOrderCriteria(cls) : orderCriteria);
			if (flatNomenclatures.size() == 0) {
				return null;
			}
			List<FlatNomenclature> result = new ArrayList<FlatNomenclature>();
			for (FlatNomenclatureRecord f:flatNomenclatures) {
				result.add(generateFlatNomenclature(f));
			}
			return result;
		} catch (SQLException e) {
			throw Utils.logException(e);
		}

	}

	public FlatNomenclature getFlatNomenclature(int nomenclatureId, String name){
	    if(name == null){
	        return null;
	    }
	    FlatNomenclature flatNom = null;
	    Map<Integer, ? extends FlatNomenclature> noms;
	    if (nomenclatureId == NomenclaturesDataProvider.NOMENCLATURE_SPECIALITY) {
	    	noms = cachedSpecialities;
		} else {
	    	noms = cachedFlatNomenclatures.get(nomenclatureId);
		}
	    if (noms != null) {
            for(Entry<Integer, ? extends FlatNomenclature> fn: noms.entrySet()){
                if(fn.getValue().getName().equalsIgnoreCase(name.trim())){
                    flatNom = fn.getValue();
                }
            }
        }

	    return flatNom;
	}

	public List<FlatNomenclature> getFlatNomenclatures(int nomenclatureId, String nameStartsWith, Date toDate, OrderCriteria orderCriteria) {
        return _getNoms(nomenclatureId, StringUtils.isEmpty(nameStartsWith) ? nameStartsWith : nameStartsWith + "%", toDate, orderCriteria);
	}

    public List<FlatNomenclature> getFlatNomenclaturesContainingNameLike(int nomenclatureId, String nameLike, Date toDate, OrderCriteria orderCriteria) {
		return _getNoms(nomenclatureId, StringUtils.isEmpty(nameLike) ? nameLike : "%" + nameLike + "%", toDate, orderCriteria);
    }
    private List<FlatNomenclature> _getNoms(int nomenclatureId, String nameLike, Date toDate, OrderCriteria orderCriteria) {
		if (nameLike != null && "".equals(nameLike)) {
			return null;
		}
		Class<? extends FlatNomenclatureRecord> cls = nomenclatureRecords.get(nomenclatureId);
		List<? extends FlatNomenclatureRecord> flatNomenclatures;
		try {
			flatNomenclatures = db.getFlatNomenclatureRecords(cls, nameLike, toDate == null ? null : new java.sql.Date(toDate.getTime()), orderCriteria == null ? NomenclatureOrderCriteria.getDefaultOrderCriteria(cls) : orderCriteria);
			if (flatNomenclatures.size() == 0) {
				return null;
			}
			List<FlatNomenclature> result = new ArrayList<FlatNomenclature>();
			for (FlatNomenclatureRecord f:flatNomenclatures) {
				result.add(generateFlatNomenclature(f));
			}
			return result;
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	private <T extends FlatNomenclatureRecord> int saveNomenclature(T t) {
		try {
			if (t.getId() == 0) {
				t = (T) db.insertNomenclatureRecord(t);
			} else {
				db.updateRecord(t);
			}
			return t.getId();
		} catch (Exception e) {
			throw Utils.logException(this, e);
		}
	}

	/*private <T extends FlatNomenclatureRecord> T getNomenclatureRecord(T t, int id) {
		try {
			return db.selectRecord(t, id);
		} catch (Exception e) {
			throw Utils.logException(this, e);
		}
	}*/

	private FlatNomenclature generateFlatNomenclature(FlatNomenclatureRecord record) {
		if (record instanceof EducationAreaRecord) {
			return new EducationAreaImpl((EducationAreaRecord) record);
		} if (record instanceof BgAcademicRecognitionStatusRecord) {
			return new BgAcademicRecognitionStatusImpl((BgAcademicRecognitionStatusRecord) record);
		} else if (record instanceof EducationLevelRecord) {
			return new EducationLevelImpl((EducationLevelRecord) record);
		} else if (record instanceof CivilIdTypeRecord) {
			return new CivilIdTypeImpl((CivilIdTypeRecord) record);
		} else if (record instanceof CommissionPositionRecord) {
			return new CommissionPositionImpl((CommissionPositionRecord) record);
		} else if (record instanceof DurationUnitRecord) {
			return new DurationUnitImpl((DurationUnitRecord) record);
		} else if (record instanceof GraduationWayRecord) {
			return new GraduationWayImpl((GraduationWayRecord) record);
		} else if (record instanceof QualificationRecord) {
			return new QualificationImpl((QualificationRecord) record);
		} else if (record instanceof OriginalQualificationRecord) {
			return new OriginalQualificationImpl((OriginalQualificationRecord) record);
		} else if (record instanceof RecognitionPurposeRecord) {
			return new RecognitionPurposeImpl((RecognitionPurposeRecord) record);
		} else if (record instanceof TrainingFormRecord) {
			return new TrainingFormImpl((TrainingFormRecord) record);
		} else if (record instanceof CopyTypeRecord) {
			return new CopyTypeImpl((CopyTypeRecord) record);
		} else if(record instanceof ProfessionalInstitutionTypeRecord) {
		    return new ProfessionalInstitutionTypeImpl ((ProfessionalInstitutionTypeRecord)record);
		} else if(record instanceof QualificationDegreeRecord) {
		    return new QualificationDegreeImpl((QualificationDegreeRecord)record);
		} else if(record instanceof SecondaryCaliberRecord){
		    return new SecondaryCaliberImpl((SecondaryCaliberRecord)record);
		} else if(record instanceof SecondaryProfessionGroupRecord){
		    return new SecondaryProfessionGroupImpl((SecondaryProfessionGroupRecord)record);
		} else if (record instanceof SecondarySpecialityRecord) {
		    return new SecondarySpecialityImpl(this, (SecondarySpecialityRecord) record);
		} else if(record instanceof ProfessionRecord){
		    return new ProfessionImpl((ProfessionRecord) record);
		}
		//--------------------------------------------------------------
		/*
		 * else if (record instanceof DocumentTypeRecord) {
		 * return new DocumentTypeImpl((DocumentTypeRecord)record);
		 * }
		 */
		else if (record instanceof ApplicationSessionStatusRecord) {
			return new ApplicationSessionStatusImpl((ApplicationSessionStatusRecord) record);
		} else if (record instanceof TrainingLocationRecord) {
			return new TrainingLocationImpl((TrainingLocationRecord) record);
		} else if (record instanceof SessionStatusRecord) {
			return new SessionStatusImpl((SessionStatusRecord) record);
		} else if (record instanceof DocCategoryRecord) {
			return new DocCategoryImpl((DocCategoryRecord) record);
		} else if (record instanceof EventStatusRecord) {
			return new EventStatusImpl((EventStatusRecord) record);
		} else if (record instanceof EducationTypeRecord) {
		    return new EducationTypeImpl((EducationTypeRecord) record);
		} else if (record instanceof ProfessionExperienceRecord) {
		    return new ProfessionExperienceImpl((ProfessionExperienceRecord) record);
		} else if (record instanceof HigherProfessionalQualificationRecord) {
		    return new HigherProfessionalQualificationImpl((HigherProfessionalQualificationRecord) record);
		} else if (record instanceof HigherSpecialityRecord) {
		    return new HigherSpecialityImpl((HigherSpecialityRecord) record);
		} else if (record instanceof EducationDocumentTypeRecord) {
		    return new EducationDocumentTypeImpl((EducationDocumentTypeRecord)record);
		} else if (record instanceof ProfessionExperienceDocumentTypeRecord) {
            return new ProfessionExperienceDocumentTypeImpl((ProfessionExperienceDocumentTypeRecord) record);
        } else if (record instanceof RegprofArticleDirectiveRecord) {
            return new RegprofArticleDirectiveImpl((RegprofArticleDirectiveRecord) record);
        } else if (record instanceof ServiceTypeRecord) {
            return new ServiceTypeImpl((ServiceTypeRecord) record);
        } else if (record instanceof CertificateProfessionalQualificationRecord) {
            return new CertificateProfessionalQualificationImpl((CertificateProfessionalQualificationRecord) record);
        } else if (record instanceof PaymentTypeRecord) {
            return new PaymentTypeImpl((PaymentTypeRecord) record);
        } else if(record instanceof SecondaryProfessionalQualificationRecord) {
            return new SecondaryProfessionalQualificationImpl(this, (SecondaryProfessionalQualificationRecord) record);
        } else if(record instanceof OriginalSpecialityRecord) {
            return new OriginalSpecialityImpl((OriginalSpecialityRecord) record);
        } else if(record instanceof BolognaCycleRecord) {
            return new BolognaCycleImpl((BolognaCycleRecord) record);
        } else if (record instanceof EuropeanQualificationsFrameworkRecord) {
            return new EuropeanQualificationsFrameworkImpl((EuropeanQualificationsFrameworkRecord)record);
        } else if (record instanceof GraduationDocumentTypeRecord) {
            return new GraduationDocumentTypeImpl((GraduationDocumentTypeRecord)record);
        } else if (record instanceof GradeRecord) {
            return new GradeImpl((GradeRecord)record);
        }else if (record instanceof SchoolTypeRecord) {
            return new SchoolTypeImpl((SchoolTypeRecord)record);
        }else if (record instanceof AgeRangeRecord) {
            return new AgeRangeImpl((AgeRangeRecord)record);
        }else if (record instanceof PersonalIdDocumentTypeRecord) {
			return new PersonalIdDocumentTypeImpl((PersonalIdDocumentTypeRecord)record);
		} /*else if (record instanceof DocumentReceiveMethodRecord) {
			return new DocumentReceiveMethodImpl((DocumentReceiveMethodRecord) record);
		}*/ else if (record instanceof UniversityGenericNameRecord) {
			return new UniversityGenericNameImpl((UniversityGenericNameRecord) record);
		}else if (record instanceof LanguageRecord) {
			return new LanguageImpl((LanguageRecord) record);
		} else if (record instanceof OriginalEducationLevelRecord) {
			return new OriginalEducationLevelImpl((OriginalEducationLevelRecord) record);
		}
		throw new RuntimeException("FlatNomenclature " + record.getClass().getName() + " not defined....");
	}

	public void deleteNomenclature(int nomenclatureId, int value) {
		Class<? extends FlatNomenclatureRecord> cls = nomenclatureRecords.get(nomenclatureId);
		if (cls == null) {
			throw new IllegalArgumentException("No nomenclature class defined for nomenclatureID:" + nomenclatureId);
		}
		try {
			db.deleteRecord(cls, value);
		} catch (Exception e) {
			throw Utils.logException(this, e);
		}
	}

	public List<ApplicationStatus> getApplicationStatusesExcluding(int entryNumSeriesId, List<Integer> excludeStatusIds, Date dateTo, OrderCriteria orderCriteria, boolean onlyLegal) {
		if (excludeStatusIds == null || excludeStatusIds.size() == 0) {
			return null;
		}
		List<ApplicationStatus> nomenclatures = getApplicationStatuses(entryNumSeriesId, dateTo, orderCriteria, onlyLegal);
		if (nomenclatures == null) {
			return null;
		}
		List<ApplicationStatus> result = new ArrayList<ApplicationStatus>();
		for (ApplicationStatus n:nomenclatures) {
			if (!excludeStatusIds.contains(n.getId())) {
				result.add(n);
			}
		}
		return result.size() == 0 ? null : result;
	}

	public List<ApplicationStatus> getApplicationStatuses(int entryNumSeriesId, List<Integer> statusIds, Date dateTo, OrderCriteria orderCriteria, boolean onlyLegal) {
		if (statusIds == null || statusIds.size() == 0) {
			return null;
		}
		List<ApplicationStatus> nomenclatures = getApplicationStatuses(entryNumSeriesId, dateTo, orderCriteria, onlyLegal);
		if (nomenclatures == null || statusIds == null || statusIds.size() == 0) {
			return nomenclatures;
		}
		List<ApplicationStatus> result = new ArrayList<ApplicationStatus>();
		for (ApplicationStatus n:nomenclatures) {
			if (statusIds.contains(n.getId())) {
				result.add(n);
			}
		}
		return result.size() == 0 ? null : result;
	}
    public List<ApplicationStatus> getApplicationStatuses(int numgeneratorEntryNumSeriesId, Date dateTo, OrderCriteria orderCriteria, boolean onlyLegal) {
        try {
            List<ApplicationStatusRecordExtended> recs = db.getApplicationStatusRecords(numgeneratorEntryNumSeriesId, Utils.getSqlDate(dateTo), orderCriteria, onlyLegal);
            if (recs.size() == 0) {
                return null;
            }
            List<ApplicationStatus> result = new ArrayList<ApplicationStatus>();
            for (ApplicationStatusRecordExtended rec : recs) {
                result.add(new ApplicationStatusImpl(rec));
            }
            return result;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public ApplicationStatus getApplicationStatus(int numgeneratorSeriesId, int statusId) {
        return cachedApplicationStatuses.get(numgeneratorSeriesId).get(statusId);
    }

    public int saveApplicationStatus(int entryNumSriesId, int id, String name, Date dateFrom, Date dateTo, boolean isLegal) {
        try {
            ApplicationStatusRecord rec = new ApplicationStatusRecord(id, name, Utils.getSqlDate(dateFrom), Utils.getSqlDate(dateTo));
            db.saveApplicationStatusRecord(rec, entryNumSriesId, isLegal);
            resetCachedApplicationStatuses();
            return rec.getId();
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public ApplicationDocflowStatus getApplicationDocflowStatus(int numgeneratorSeriesId, int statusId) {
        return cachedApplicationDocflowStatuses.get(numgeneratorSeriesId).get(statusId);
    }

    public int saveApplicationDocflowStatus(int numgeneratorSeriesId, int id, String name, Date dateFrom, Date dateTo) {
        try {
            ApplicationDocflowStatusRecord rec = new ApplicationDocflowStatusRecord(id, name, Utils.getSqlDate(dateFrom), Utils.getSqlDate(dateTo));

            db.saveApplicationDocflowStatusRecord(rec, numgeneratorSeriesId);
            resetCachedApplicationStatuses();
            return rec.getId();
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public List<ApplicationDocflowStatus> getApplicationDocflowStatuses(int numgeneratorSeriesId, Date dateTo, OrderCriteria orderCriteria) {
        try {

            List<ApplicationDocflowStatusRecord> recs = db.getApplicationDocflowStatusRecords(numgeneratorSeriesId, Utils.getSqlDate(dateTo), orderCriteria);
            if (recs.size() == 0) {
                return null;
            }
            List<ApplicationDocflowStatus> result = new ArrayList<ApplicationDocflowStatus>();
            for (ApplicationDocflowStatusRecord rec : recs) {
                result.add(new ApplicationDocflowStatusImpl(rec));
            }
            return result;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

	public LegalReason getLegalReason(int legalReasonId) {
		try {
			LegalReasonRecord rec = db.selectRecord(new LegalReasonRecord(), legalReasonId);
			return rec == null ? null : new LegalReasonImpl(rec, nacidDataProvider);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}

	}

	public List<LegalReason> getLegalReasons(int applicationType, Date dateTo, OrderCriteria orderCriteria, Integer applicationStatusId) {
		try {
			List<LegalReasonRecord> recs = db.getLegalReasonRecords(applicationType, Utils.getSqlDate(dateTo), orderCriteria, applicationStatusId);
			if (recs.size() == 0) {
				return null;
			}
			List<LegalReason> result = new ArrayList<LegalReason>();
			for (LegalReasonRecord r:recs) {
				result.add(new LegalReasonImpl(r, nacidDataProvider));
			}
			return result;
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	@Override
	public List<Integer> getApplicationTypesPerLegalReason(int legalReasonId) {
		try {
			return db.getApplicationTypesPerLegalReasonId(legalReasonId);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	public int saveLegalReason(int id, String name, Integer appStatusId, String ordinanceArticle, String regulationArticle, String regulationText, Date dateFrom, Date dateTo, List<Integer> applicationTypes) {
		try {
			return db.saveLegalReason(id, name, appStatusId, ordinanceArticle, regulationArticle, regulationText, dateFrom, dateTo, applicationTypes);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	public List<SecondarySpeciality> getSecondarySpecialities(String partOfName, Date toDate, OrderCriteria orderCriteria, Integer profQualificationId) {
        try {
            List<SecondarySpecialityRecord> records = db.getSecondarySpecialityRecords(partOfName, toDate == null ? null : new java.sql.Date(toDate.getTime()),
                    orderCriteria == null ? NomenclatureOrderCriteria.getDefaultOrderCriteria(SecondarySpecialityRecord.class) : orderCriteria, profQualificationId);
            if (records.size() == 0) {
                return null;
            }
            List<SecondarySpeciality> result = new ArrayList<SecondarySpeciality>();
            resetCachedSecondaryProfessionalQualifications();
            for (SecondarySpecialityRecord r: records) {
                result.add(new SecondarySpecialityImpl(this, r));
            }
            return result;
        } catch (Exception e) {
            throw Utils.logException(this, e);
        }
    }

    @Override
    public SecondarySpeciality getSecondarySpeciality(int secondarySpecialityId) {
        return cachedSecondarySpecialities.get(secondarySpecialityId);
    }

    public SecondarySpeciality getSecondarySpecialityByName(String name) {
        try {
            SecondarySpecialityRecord record = db.selectRecord(SecondarySpecialityRecord.class, " UPPER(name) = UPPER(?) ", name);
            if (record != null) {
                return new SecondarySpecialityImpl(this, record);
            }
            return null;
        } catch (SQLException e) {
            throw Utils.logException(this, e);
        }
    }

    @Override
    public int saveSecondarySpeciality(int id, String name, Integer professionalQualificationId, Integer qualificationDegreeId, Date dateFrom, Date dateTo, String code) {
        java.sql.Date df = dateFrom == null ? null : new java.sql.Date(dateFrom.getTime());
        java.sql.Date dt = dateTo == null ? null : new java.sql.Date(dateTo.getTime());
        SecondarySpecialityRecord record = new SecondarySpecialityRecord(id, name, professionalQualificationId, qualificationDegreeId, df, dt, code);
        int res = saveNomenclature(record);
        resetCachedSecondarySpecialities();
        return res;
    }

    //RayaWritten---------------------------------------------------------------------------
    public RegprofArticleDirective getRegprofArticleDirectiveWithItems(int id){
        FlatNomenclature articleRecord = getFlatNomenclature(FLAT_NOMENCLATURE_REGPROF_ARTICLE_DIRECTIVE, id);
        List<RegprofArticleItem> items = getRegprofArticleItems(null, null, id);
        RegprofArticleDirective article = (RegprofArticleDirective) articleRecord;
        article.setArticleItems(items);
        return article;
    }

    public RegprofArticleItem getRegprofArticleItem(int itemId) {
        return cachedRegprofArticleItems.get(itemId);

    }
    public List<RegprofArticleItem> getRegprofArticleItems(Date dateTo, OrderCriteria orderCriteria, Integer articleDirectiveId) {
        try {
            List<RegprofArticleItemRecord> recs = db.getRegprofArticleItemRecords(Utils.getSqlDate(dateTo), orderCriteria, articleDirectiveId);
            if (recs.size() == 0) {
                return null;
            }
            List<RegprofArticleItem> result = new ArrayList<RegprofArticleItem>();
            for (RegprofArticleItemRecord r:recs) {
                result.add(new RegprofArticleItemImpl(r));
            }
            return result;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public int saveRegprofArticleItem(int id, String name, Integer articleDirectiveId, String qualificationLevelLabel, Date dateFrom, Date dateTo) {
        try {
            int res = id;
            RegprofArticleItemRecord rec = new RegprofArticleItemRecord(id, name, articleDirectiveId, qualificationLevelLabel, Utils.getSqlDate(dateFrom), Utils.getSqlDate(dateTo));
            if (id == 0) {
                rec = db.insertRecord(rec);
                res = rec.getId();
            } else {
                db.updateRecord(rec);
            }
            resetCachedRegprofArticleItems();
            return res;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public List<SecondaryProfessionGroup> getSecondaryProfessionGroups(Date dateTo, OrderCriteria orderCriteria) {
        try {
            List<SecondaryProfessionGroupRecord> recs = db.getSecondaryProfessionGroupRecords(Utils.getSqlDate(dateTo), orderCriteria);
            if (recs.size() == 0) {
                return null;
            }
            List<SecondaryProfessionGroup> result = new ArrayList<SecondaryProfessionGroup>();
            for (SecondaryProfessionGroupRecord r:recs) {
                result.add(new SecondaryProfessionGroupImpl(r));
            }
            return result;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public SecondaryProfessionGroup getSecondaryProfessionGroup(Integer groupId) {
        return cachedSecondaryProfessionGroups.get(groupId);

    }
    public int saveSecondaryProfessionGroup(int id, String name, Date dateFrom, Date dateTo, String code) {
        try {
            int res = id;
            SecondaryProfessionGroupRecord rec = new SecondaryProfessionGroupRecord(id, name, Utils.getSqlDate(dateFrom), Utils.getSqlDate(dateTo), code);
            if (id == 0) {
                rec = db.insertRecord(rec);
                res = rec.getId();
            } else {
                db.updateRecord(rec);
            }
            resetCachedSecondaryProfessionGroups();
            return res;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public ServiceType getServiceType(int serviceTypeIdId) {
        return cachedServiceTypes.get(serviceTypeIdId);

    }
    public List<ServiceType> getServiceTypes(Date dateTo, OrderCriteria orderCriteria, Integer executionDays) {
        try {
            List<ServiceTypeRecord> recs = db.getServiceTypeRecords(Utils.getSqlDate(dateTo), orderCriteria, executionDays);
            if (recs.size() == 0) {
                return null;
            }
            List<ServiceType> result = new ArrayList<ServiceType>();
            for (ServiceTypeRecord r:recs) {
                result.add(new ServiceTypeImpl(r));
            }
            return result;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public int saveServiceType(int id, String name, Integer executionDays, Date dateFrom, Date dateTo, BigDecimal servicePrice) {
        try {
            int res = id;
            ServiceTypeRecord rec = new ServiceTypeRecord(id, name, executionDays, Utils.getSqlDate(dateFrom), Utils.getSqlDate(dateTo), servicePrice);
            if (id == 0) {
                rec = db.insertRecord(rec);
                res = rec.getId();
            } else {
                db.updateRecord(rec);
            }
            resetCachedServiceTypes();
            return res;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }


    @Override
    public List<ProfessionExperienceDocumentType> getProfessionExperienceDocumentTypes(
            Date dateTo, OrderCriteria orderCriteria) {
        try {
            List<ProfessionExperienceDocumentTypeRecord> records = db.getProfessionExperienceDocumentTypesRecords(Utils.getSqlDate(dateTo), orderCriteria);
            List<ProfessionExperienceDocumentType> result = new ArrayList<ProfessionExperienceDocumentType>();
            if (records.size() != 0) {
                for (ProfessionExperienceDocumentTypeRecord record:records) {
                    result.add(new ProfessionExperienceDocumentTypeImpl(record));
                }
            }
            return result;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public ProfessionExperienceDocumentType getProfessionExperienceDocumentType(int id) {
        return professionExperienceDocumentTypes.get(id);
    }

    @Override
    public int saveProfessionExperienceDocumentType(int id, String name,boolean isForExperienceCalculation, Date dateFrom, Date dateTo) {
        ProfessionExperienceDocumentTypeRecord record = new ProfessionExperienceDocumentTypeRecord(id, name, Utils.getSqlDate(dateFrom), Utils.getSqlDate(dateTo), isForExperienceCalculation ? 1 : 0);
        try {
            if (record.getId() == 0) {
                record = db.insertRecord(record);
            } else {
                db.updateRecord(record);
            }
           resetCachedProfessionExperienceDocumentTypes();
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        return record.getId();
    }




    public void updateCountryFormalName(String code, String formalName){
        try {
            CountryRecord c = db.selectRecord(CountryRecord.class, " iso3166_code like ?", code);
            if(c != null){
                c.setOfficialName(formalName);
                db.updateRecord(c, "official_name");
                System.out.println(code+" is updated");
            } else {
                System.out.println(code+" is not updated");
            }
        } catch (SQLException e) {
            Utils.logException(e);
        }
    }


    public NationalQualificationsFramework getNationalQualificationsFramework(int id) {
        return cachedNationaQualificationFrameworks.get(id);

    }
    public List<NationalQualificationsFramework> getNationalQualificationsFrameworks(Date dateTo, Integer countryId, OrderCriteria orderCriteria) {
        try {
            List<NationalQualificationsFrameworkRecord> recs = db.getNationalQualificationsFrameworks(Utils.getSqlDate(dateTo), countryId, orderCriteria);
            if (recs.size() == 0) {
                return null;
            }
            List<NationalQualificationsFramework> result = new ArrayList<NationalQualificationsFramework>();
            for (NationalQualificationsFrameworkRecord r:recs) {
                result.add(new NationalQualificationsFrameworkImpl(r));
            }
            return result;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public int saveNationalQualificationsFramework(int id, String name, int countryId, Date dateFrom, Date dateTo) {
        try {
            int res = id;
            NationalQualificationsFrameworkRecord rec = new NationalQualificationsFrameworkRecord(id, name, countryId, Utils.getSqlDate(dateFrom), Utils.getSqlDate(dateTo));
            if (id == 0) {
                rec = db.insertRecord(rec);
                res = rec.getId();
            } else {
                db.updateRecord(rec);
            }
            resetCachedNationalQualificationsFrameworks();
            return res;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }




    public OriginalEducationLevel getOriginalEducationLevel(int orinalEducationLevelId) {
        return cachedOriginalEducationLevels.get(orinalEducationLevelId);

    }
    public List<OriginalEducationLevel> getOriginalEducationLevels(Date dateTo, OrderCriteria orderCriteria) {
        try {
            List<OriginalEducationLevelRecord> recs = db.getOriginalEducationLevelRecords(Utils.getSqlDate(dateTo), orderCriteria);
			return recs.size() == 0 ? null : recs.stream().map(r -> new OriginalEducationLevelImpl(r)).collect(Collectors.toList());
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public List<OriginalEducationLevel> getOriginalEducationLevels(Integer countryId, List<Integer> eduLevelIds, Date dateTo, OrderCriteria orderCriteria) {
        try {
            List<OriginalEducationLevelRecord> recs = db.getOriginalEducationLevelRecordsByCountry(countryId, eduLevelIds, Utils.getSqlDate(dateTo), orderCriteria);
            return recs.size() == 0 ? null : recs.stream().map(r -> new OriginalEducationLevelImpl(r)).collect(Collectors.toList());
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public int saveOriginalEducationLevel(int id, String name, String nameTransalted, int eduLevelId, int countryId, Date dateFrom, Date dateTo) {
        try {
            int res = id;
            OriginalEducationLevelRecord rec = new OriginalEducationLevelRecord(id, name, nameTransalted, Utils.getSqlDate(dateFrom), Utils.getSqlDate(dateTo), countryId, eduLevelId);
            if (id == 0) {
                rec = db.insertRecord(rec);
                res = rec.getId();
            } else {
                db.updateRecord(rec);
            }
            resetCachedOriginalEducationLevels();
            return res;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public GraoCity getGraoCity(int id) {
        return cachedGraoCities.get(id);
    }

    public List<GraoCity> getGraoCities() {
        try {
            List<GraoCityRecord> cities = db.selectRecords(GraoCityRecord.class, null);
            List<GraoCity> result = new ArrayList<GraoCity>();
            for (GraoCityRecord city : cities) {
                result.add(city);
            }
            return result;

        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }


    public City getCity(int id) {
        return cachedCities.get(id);
    }

    public List<City> getCities() {
        try {
            List<CityRecord> cities = db.selectRecords(CityRecord.class, null);
            List<City> result = new ArrayList<City>();
            for (CityRecord city : cities) {
                result.add(new CityImpl(city));
            }
            return result;

        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public ExpertPosition getExpertPosition(int expertPositionId) {
        return cachedExpertPositions.get(expertPositionId);
    }

    public List<ExpertPosition> getExpertPositions() {
        try {
            List<ExpertPositionRecord> recs = db.selectRecords(ExpertPositionRecord.class, "1 = 1 order by name");
            return recs.size() == 0 ? null : recs.stream().map(r -> new ExpertPositionImpl(nacidDataProvider, r)).collect(Collectors.toList());
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public int saveExpertPosition(int id, String name, Integer appStatusId, Date dateFrom, Date dateTo) {
        try {
            ExpertPositionRecord rec = new ExpertPositionRecord(id, name, Utils.getSqlDate(dateFrom), Utils.getSqlDate(dateTo), appStatusId);
            if (rec.getId() == 0) {
                rec = db.insertRecord(rec);
            } else {
                db.updateRecord(rec);
            }
            resetExpertPositions();
            return rec.getId();
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }


	@Override
	public List<String> getBgAcademicRecognitionSuggestion(String type, String nameContains) {
		try {
			return db.getBgAcademicRecognitionCitizenshipSuggestion(type, nameContains);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	@Override
	public List<GraduationWay> getGraduationWays(int applicationType, Date dateTo, OrderCriteria orderCriteria) {
		try {
			List<GraduationWayRecord> res = db.getGraduationWayRecords(applicationType, Utils.getSqlDate(dateTo), orderCriteria);
			return res.size() == 0 ? null : res.stream().map(r -> (GraduationWay) generateFlatNomenclature(r)).collect(Collectors.toList());
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	@Override
	public List<Integer> getApplicationTypesPerGraduationWay(int graduationWay) {
		try {
			return db.getApplicationTypesPerGraduationWay(graduationWay);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	@Override
	public void updateGraduationWayApplicationTypes(int graduationWay, List<Integer> appTypes) {
		try {
			db.updateGraduationWayApplicationTypes(graduationWay, appTypes);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	@Override
	public List<EducationLevel> getEducationLevels(int applicationType, Date date, OrderCriteria orderCriteria) {
		try {
			List<EducationLevelRecord> res = db.getEducationLevelRecords(applicationType, Utils.getSqlDate(date), orderCriteria);
			return res.size() == 0 ? null : res.stream().map(r -> (EducationLevel) generateFlatNomenclature(r)).collect(Collectors.toList());
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	@Override
	public List<Integer> getApplicationTypesPerEducationLevel(int eduLevelId) {
		try {
			return db.getApplicationTypesPerEduLevel(eduLevelId);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	@Override
	public void updateEducationLevelApplicationTypes(int eduLevelId, List<Integer> appTypes) {
		try {
			db.updateEduLevelApplicationTypes(eduLevelId, appTypes);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	@Override
	public List<DocumentReceiveMethod> getDocumentReceiveMethods(Date dateTo, OrderCriteria orderCriteria) {
		try {
			List<DocumentReceiveMethod> res = db.getDocumentReceiveMethodRecords(Utils.getSqlDate(dateTo), orderCriteria).stream().map(r -> new DocumentReceiveMethodImpl(r)).collect(Collectors.toList());
			return res.size() == 0 ? null : res;
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	@Override
	public DocumentReceiveMethod getDocumentReceiveMethod(int id) {
		try {
			DocumentReceiveMethodRecord res = db.getDocumentReceiveMethod(id);
			return res == null ? null : new DocumentReceiveMethodImpl(res);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	@Override
	public int saveDocumentReceiveMethod(int id, String name, boolean hasDocumentRecipient,  boolean eservicesRequirePaymentReceipt, Date dateFrom, Date dateTo) {
		try {
			DocumentReceiveMethodRecord rec = new DocumentReceiveMethodRecord(id, name, hasDocumentRecipient ? 1 : 0, eservicesRequirePaymentReceipt ? 1 : 0, Utils.getSqlDate(dateFrom), Utils.getSqlDate(dateTo));
			if (rec.getId() == 0) {
				rec = db.insertRecord(rec);
				id = rec.getId();
			} else {
				db.updateRecord(rec);
			}
			resetDocumentReceiveMethods();
			return id;
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	//-----------------------------------------------------------------------------------------
    public static void main(String[] args) throws Exception {
        NomenclaturesDataProviderImpl nomenclaturesDataProviderImpl = new NomenclaturesDataProviderImpl(new NacidDataProviderImpl(new StandAloneDataSource()));
        //nomenclaturesDataProviderImpl.deleteSpeciality(90200055);
        //Calendar cal = new GregorianCalendar();
        //System.out.println(nomenclaturesDataProviderImpl.saveFlatNomenclature(FLAT_NOMENCLATURE_EDUCATION_LEVEL, 0, "proba", new java.sql.Date(cal.getTimeInMillis()), new java.sql.Date(cal.getTimeInMillis())));
        //nomenclaturesDataProviderImpl.getFlatNomenclatures(FLAT_NOMENCLATURE_NKPD_TYPE, null, null);
        //FlatNomenclature test = nomenclaturesDataProviderImpl.getFlatNomenclature(nomenclaturesDataProviderImpl.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION, 21);
        //System.out.println(test.getName());
		System.out.println(nomenclaturesDataProviderImpl.getFlatNomenclaturesContainingNameLike(43, "По", null, null));
//        nomenclaturesDataProviderImpl.cachedFlatNomenclatures.get(10).get(null);
//        Integer document = 4;
//        FlatNomenclature exprDocType = null;
//        if(document!= null){
//            exprDocType = nomenclaturesDataProviderImpl.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE_DOCUMENT_TYPE, document);
//        }
        //System.out.println("Saving.....");
        //nomenclaturesDataProviderImpl.saveProfessionExperienceDocumentType(4, "служебна книжка", true, null, null);
        
    }
}
