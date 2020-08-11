package com.nacid.bl.nomenclatures;

import com.nacid.bl.OrderCriteria;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.nomenclatures.ExpertPositionImpl;
import com.nacid.bl.nomenclatures.regprof.*;
import com.nacid.data.nomenclatures.ExpertPositionRecord;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @author ggeorgiev
 * Ploskite nomenkalutiri (tezi koito imat samo id, name, date_from, date_to) se manipulirat s method-ite
 * getFlatNomenclature(s), saveFlatNomenclature - tezi methods imat edin parametyr nomenclatureId, na koito se podavat
 * edna ot static final int promenlivite, definirani v {@link NomenclaturesDataProvider} i zapo4vashti s FLAT_ !
 * durigte promenlivi, koito ne zapo4vat s FLAT, ne sa ploski nomenclaturi i ne bi trqbvalo da se manipulirat s gornite 3 methods
 * te sa dobaveni za pylnota, i si imat sobstveni methods (pr.getCountry(ies), saveCountry
 */

public interface NomenclaturesDataProvider {
  public static final int FLAT_NOMENCLATURE_EDUCATION_AREA = 1;
  public static final int FLAT_NOMENCLATURE_EDUCATION_LEVEL = 2;
  public static final int NOMENCLATURE_SPECIALITY = 3;
  public static final int NOMENCLATURE_PROFESSION_GROUP = 4;
  public static final int FLAT_NOMENCLATURE_COMMISSION_POSITION = 5;
  public static final int NOMENCLATURE_COUNTRY = 6;
  public static final int FLAT_NOMENCLATURE_QUALIFICATION = 7;
  public static final int FLAT_NOMENCLATURE_RECOGNITION_PURPOSE = 8;
  public static final int FLAT_NOMENCLATURE_TRAINING_FORM = 9;
  public static final int FLAT_NOMENCLATURE_GRADUATION_WAY = 10;
  public static final int FLAT_NOMENCLATURE_CIVIL_ID_TYPE = 11;
  public static final int FLAT_NOMENCLATURE_DURATION_UNIT = 12;
  public static final int FLAT_NOMENCLATURE_COPY_TYPE = 13;
  public static final int NOMENCLATURE_DOCUMENT_TYPE = 14;

  public static final int NOMENCLATURE_APPLICATION_STATUS = 15;
  public static final int FLAT_NOMENCLATURE_APPLICATION_SESSION_STATUS = 16;
  public static final int FLAT_NOMENCLATURE_TRAINING_LOCATION = 17;
  public static final int FLAT_NOMENCLATURE_SESSION_STATUS = 18;
  public static final int FLAT_NOMENCLATURE_DOC_CATEGORY = 19;
  public static final int FLAT_NOMENCLATURE_EVENT_STATUS = 20;
  public static final int NOMENCLATURE_LEGAL_REASON = 21;

  //RayaWritten-----------------------------------------------------------------
  public static final int FLAT_NOMENCLATURE_PROFESSIONAL_INSTITUTION_TYPE = 25;
  //public static final int FLAT_NOMENCLATURE_REGPROF_APP_STATUS = 26;
  //---------------------------------------------------------------------------
  public static final int FLAT_NOMENCLATURE_EDUCATION_TYPE = 27;
  public static final int FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE = 28;
  //RayaWritten-----------------------------------------------------
  public static final int FLAT_NOMENCLATURE_QUALIFICATION_DEGREE = 29;
  //-----------------------------------------------------------------------
  public static final int FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION = 30;
  public static final int FLAT_NOMENCLATURE_HIGHER_SPECIALITY = 31;
  //RayaWritten------------------------------------------------------
  public static final int FLAT_NOMENCLATURE_SECONDARY_CALIBER = 32;
  public static final int NOMENCLATURE_SECONDARY_PROFESSIONAL_QUALIFICATION = 33;
  public static final int NOMENCLATURE_SECONDARY_PROFESSION_GROUP = 35;
  public static final int FLAT_NOMENCLATURE_PROFESSION = 36;
  //-----------------------------------------------------------------------
  public static final int NOMENCLATURE_SECONDARY_SPECIALITY = 37;
  public static final int FLAT_NOMENCLATURE_EDUCATION_DOCUMENT_TYPE = 38;  
  public static final int FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE_DOCUMENT_TYPE = 39;
  //RayaWritten------------------------------------------------------------------
  public static final int FLAT_NOMENCLATURE_REGPROF_ARTICLE_DIRECTIVE = 40;
  public static final int NOMENCLATURE_REGPROF_ARTICLE_ITEM = 41;
  public static final int NOMENCLATURE_SERVICE_TYPE = 42;
  public static final int FLAT_NOMENCLATURE_CERTIFICATE_PROF_QUALIFICATION = 43;
  public static final int FLAT_NOMENCLATURE_PAYMENT_TYPE = 44;

  public static final int NOMENCLATURE_APPLICATION_DOCFLOW_STATUS = 45;

  public static final int NOMENCLATURE_ORIGINAL_EDUCATION_LEVEL = 46;
  public static final int FLAT_NOMENCLATURE_ORIGINAL_SPECIALITY = 47;

    public static final int NOMENCLATURE_NATIONAL_QUALIFICATIONS_FRAMEWORK = 48;
    public static final int FLAT_NOMENCLATURE_EUROPEAN_QUALIFICATIONS_FRAMEWORK = 49;
    public static final int FLAT_NOMENCLATURE_BOLOGNA_CYCLE = 50;

    public static final int FLAT_NOMENCLATURE_GRADUATION_DOCUMENT_TYPE = 51;//tip na dokuemnta (certificate degree/certificate getuigschrift)
    public static final int NOMENCLATURE_EXPERT_POSITION = 52;
    public static final int FLAT_NOMENCLATURE_GRADE = 53;
    public static final int FLAT_NOMENCLATURE_SCHOOL_TYPE = 54;
    public static final int FLAT_NOMENCLATURE_AGE_RANGE = 55;
    public static final int FLAT_NOMENCLATURE_BG_ACAD_REC_STATUS = 56;


  public static final int FLAT_NOMENCLATURE_PERSONAL_ID_DOCUMENT_TYPE = 57;

  public static final int NOMENCLATURE_DOCUMENT_RECEIVE_METHOD = 58;
  public static final int FLAT_NOMENCLATURE_UNIVERSITY_GENERIC_NAME = 59;

  public static final int NOMENCLATURE_LANGUAGE = 60;

  public static final int FLAT_NOMENCLATURE_ORIGINAL_QUALIFICATION = 61;


  public static final int COUNTRY_ID_BULGARIA = 34;
  
  public static final int DEFAULT_APPLICATION_STATUS = 1;

  public void resetAllNomenclatures();
  public List<ProfessionGroup> getProfessionGroups(int educationAreaId, Date dateTo, OrderCriteria orderCriteria);
  public ProfessionGroup getProfessionGroup(int professionGroupId);
  public int saveProfessionGroup(int id, String name, int educationAreaId, Date dateFrom, Date dateTo);
  
  /**
   * @param professionGroupId
   * @param toDate
   * @param orderCriteria - orderCriteria - ako e null - dannite se filtrirat po default order criteria v zavisimost ot nomenclatureId
   * @return
   */
  public List<Speciality> getSpecialities(Integer professionGroupId, Date toDate, OrderCriteria orderCriteria);
  /**
   * @param partOfName - na4alo na imeto na specialinostta. ako e "", vry6ta null, ako e null, ne u4astva vyv filtriraneto
   * @param nameStartsWith - true - tyrsi se s v imena na specialnosti, zapo4vashti s partOfName, ako e false - tyrsi navsqkyde  
   * @param professionGroupId - ako e 0 ili null, ne u4astva vyv filtriraneto
   * @param dateTo - ako e 0, ne u4astva vyv filtriraneto
   * @param orderCriteria - ako e null se polzva default order criteria
   * @return
   */
  public List<Speciality> getSpecialities(String partOfName, boolean nameStartsWith, Integer professionGroupId, Date dateTo, OrderCriteria orderCriteria);
  public Speciality getSpeciality(int specialityId);
  public int saveSpeciality(int id, String name, Integer professionGroupId, Date dateFrom, Date dateTo);
  
  /**
   * 
   * @param dateTo
   * @param orderCriteria - orderCriteria - ako e null - dannite se filtrirat po default order criteria v zavisimost ot nomenclatureId
   * @return
   */
  public List<Country> getCountries(Date dateTo, OrderCriteria orderCriteria);
  public Country getCountry(int countryId);
  public Country getCountry(String iso3166Code);
  public Country getCountryByName(String countryName);
  public int saveCountry(int id, String name, String iso3166Code, String officialName, Date dateFrom, Date dateTo);
  ///RayaWritten-------------------------------------------------------------------------------------------------------
  public SecondaryProfessionalQualification getSecondaryProfessionalQualification(Integer profQualId);
  public int saveSecondaryProfessionalQualification(int id, String name,Integer profQualificationId, Date dateFrom, Date dateTo, String code);
  public List<SecondaryProfessionalQualification> getSecondaryProfessionalQualifications(Date toDate, OrderCriteria orderCriteria, Integer profGroup);
  
  public RegprofArticleDirective getRegprofArticleDirectiveWithItems(int id);
  public List<RegprofArticleItem> getRegprofArticleItems(Date dateTo, OrderCriteria orderCriteria, Integer articleDirectiveId);
  public RegprofArticleItem getRegprofArticleItem(int itemId);
  public int saveRegprofArticleItem(int id, String name, Integer articleDirectiveId, String qualificationLevelLabel, Date dateFrom, Date dateTo);
  public List<ServiceType> getServiceTypes(Date dateTo, OrderCriteria orderCriteria, Integer executionDays);
  public ServiceType getServiceType(int serviceTypeId);
  public int saveServiceType(int id, String name, Integer executionDays, Date dateFrom, Date dateTo, BigDecimal servicePrice);
  ///------------------------------------------------------------------------------------------
  public List<DocumentType> getDocumentTypes(Date dateTo, OrderCriteria orderCriteria, int docCategoryId);
  /**
   * @param dateTo
   * @param orderCriteria - po podrazbirane ordercriteria e po ID
   * @param hasDocumentTemplate - dali ima zadaden documentTemplate - null ne u4astva vyv filtriraneto, false - vry6ta samo tezi koito nqmat, true vry6ta samo tezi koito imat
   * @param docCategoryIds - list ot document categories 
   * @return
   */
  public List<DocumentType> getDocumentTypes(Date dateTo, OrderCriteria orderCriteria, Boolean hasDocumentTemplate, List<Integer> docCategoryIds);
  public DocumentType getDocumentType(int documentTypeId);
  public int saveDocumentType(int id, String name, boolean isIncoming, boolean hasDocflowId, List<Integer> docCategoryIds, String documentTemplate, Date dateFrom, Date dateTo);
  
  public List<ProfessionExperienceDocumentType> getProfessionExperienceDocumentTypes(Date dateTo, OrderCriteria orderCriteria);
  public ProfessionExperienceDocumentType getProfessionExperienceDocumentType(int id);
  public int saveProfessionExperienceDocumentType(int id, String name, boolean isForExperienceCalculation, Date dateFrom, Date dateTo);
  
  public List<LegalReason> getLegalReasons(int applicationType, Date dateTo, OrderCriteria orderCriteria, Integer applicationStatusId);
  public LegalReason getLegalReason(int legalReasonId);

  public int saveLegalReason(int id, String name, Integer appStatusId, String ordinanceArticle, String regulationArticle, String regulationText, Date dateFrom, Date dateTo, List<Integer> applicationTypes);

  public List<Integer> getApplicationTypesPerLegalReason(int legalReasonId);

  
  
  /**
  /**
   * fizi4eski iztriva dadena nomenclatura!
   * @param nomenclatureId - tip nomenclatura - edin ot opisanite v {@link NomenclaturesDataProvider}
   * @param value - id-to na nomenclaturata, koqto trqbva da se iztrie
   */
  public void deleteNomenclature(int nomenclatureId, int value);
  /**
   * @param nomenclatureId - edin ot tipovete definirani v {@link NomenclaturesDataProvider}, zapo4vashti s FLAT_
   * @param id
   * @param name
   * @param dateFrom
   * @param dateTo
   * @return
   */
  public int saveFlatNomenclature(int nomenclatureId, int id, String name, Date dateFrom, Date dateTo);
  public FlatNomenclature getFlatNomenclature(int nomenclatureId, int recordId);
  /**
   * ako za dateTo se podade dneshna data shte izkarva aktivnite kym dneshnata data
   * @param nomenclatureId
   * @param dateTo
   * @param orderCriteria - orderCriteria - ako e null - dannite se filtrirat po default order criteria v zavisimost ot nomenclatureId
   * @return
   */
  public List<FlatNomenclature> getFlatNomenclatures(int nomenclatureId, Date dateTo, OrderCriteria orderCriteria);
  
  /**
   * ako za dateTo se podade dneshna data shte izkarva aktivnite kym dneshnata data
   * @param nomenclatureId - edin ot definiranite v {@link NomenclaturesDataProvider} FLAT_NOMENCLATURE_* attributes
   * @param nameStartsWith - na4alo na imeto na nomenklaturata. ako e "", vry6ta null, ako e null, ne u4astva vyv filtriraneto
   * @param dateTo
   * @param orderCriteria - orderCriteria - ako e null - dannite se filtrirat po default order criteria v zavisimost ot nomenclatureId
   * @return
   */
  public List<FlatNomenclature> getFlatNomenclatures(int nomenclatureId, String nameStartsWith, Date dateTo, OrderCriteria orderCriteria);

    /**
     *
     * @param nomenclatureId
     * @param nameLike - chast ot ime. Moje da sydyrja %. ako se napishe учител, ще се търси само за name ilike 'учител', но ако се напише 'учител%', ще се търси за наме ilike 'учител%'
     * @param dateTo
     * @param orderCriteria
     * @return
     */
  public List<FlatNomenclature> getFlatNomenclaturesContainingNameLike(int nomenclatureId, String nameLike, Date dateTo, OrderCriteria orderCriteria);
  //RayaWritten-------------------------------------------------------------------
  public FlatNomenclature getFlatNomenclature(int nomenclatureId, String name);
  
  //-------------------------------------------------------------------------------
  /**
   * @param entryNumSeriesId - edin ot NumgeneratorDataProvider.*SERIES_ID
   * @param excludeStatusIds - ako excludeStatusIds == null ili excludeStatusIds.size() == 0, togava excludeStatusIds ne u4astva vyv filtriraneto
   * @param orderCriteria - ako e null - dannite se filtrirat po default order criteria
   * @return application statuses, izkliu4vajki tezi s id-ta = excludeStatusIds
   */
  public List<ApplicationStatus> getApplicationStatusesExcluding(int entryNumSeriesId, List<Integer> excludeStatusIds, Date dateTo, OrderCriteria orderCriteria, boolean onlyLegal);
  
  /**
   * @param entryNumSeriesId - edin ot NumgeneratorDataProvider.*SERIES_ID
   * @param statusIds
   * @param dateTo
   * @param orderCriteria - ako e null - dannite se filtrirat po default order criteria
   * @return samo ApplicationStatueses s id-ta sydyrjashti se v statusIds
   */
  public List<ApplicationStatus> getApplicationStatuses(int entryNumSeriesId, List<Integer> statusIds, Date dateTo, OrderCriteria orderCriteria, boolean onlyLegal);
  
  
  public List<SecondarySpeciality> getSecondarySpecialities(String partOfName, Date toDate, OrderCriteria orderCriteria, Integer profQualificationId);
  public SecondarySpeciality getSecondarySpeciality(int secondarySpecialityId);
  public int saveSecondarySpeciality(int id, String name, Integer professionalQualificationId, Integer qualificationDegreeId, Date dateFrom, Date dateTo, String code);
  //public List<FlatNomenclature> getRegprofApplicationStatusesExcluding(List<Integer> excludeStatusIds, Date dateTo, OrderCriteria orderCriteria);
  //public List<FlatNomenclature> getRegprofApplicationStatuses(List<Integer> statusIds, Date dateTo, OrderCriteria orderCriteria);
  
  public List<SecondaryProfessionGroup> getSecondaryProfessionGroups(Date dateTo, OrderCriteria orderCriteria);
  public int saveSecondaryProfessionGroup(int id, String name, Date dateFrom, Date dateTo, String code);
  public SecondaryProfessionGroup getSecondaryProfessionGroup(Integer groupId);
  public SecondaryProfessionalQualification getSecondaryProfessionalQualificationByName(String name);
  public SecondarySpeciality getSecondarySpecialityByName(String name);
  
  public void updateCountryFormalName(String code, String officialName);

    /**
     *
     * @param entryNumSeriesId - edin ot NumgeneratorDataProvider.*SERIES_ID
     * @param id
     * @param name
     * @param dateFrom
     * @param dateTo
     * @param isLegal
     * @return
     */
    public int saveApplicationStatus(int entryNumSriesId, int id, String name, Date dateFrom, Date dateTo, boolean isLegal);

    /**
     *
     * @param entryNumSeriesId - edin ot NumgeneratorDataProvider.*SERIES_ID
     * @param statusId
     * @return
     */
    public ApplicationStatus getApplicationStatus(int entryNumSeriesId, int statusId);

    /**
     *
     * @param numgeneratorEntryNumId - edin ot NumgeneratorDataProvider.* entryNumSeries
     * @param dateTo
     * @param orderCriteria
     * @return
     */
    public List<ApplicationStatus> getApplicationStatuses(int numgeneratorEntryNumId, Date dateTo, OrderCriteria orderCriteria, boolean onlyLegal);



    public ApplicationDocflowStatus getApplicationDocflowStatus(int entryNumSeriesId, int statusId);

    public int saveApplicationDocflowStatus(int entryNumSeriesId, int id, String name, Date dateFrom, Date dateTo);

    public List<ApplicationDocflowStatus> getApplicationDocflowStatuses(int entryNumSeriesId, Date dateTo, OrderCriteria orderCriteria);



    public OriginalEducationLevel getOriginalEducationLevel(int orinalEducationLevelId);
    public List<OriginalEducationLevel> getOriginalEducationLevels(Date dateTo, OrderCriteria orderCriteria);
    public List<OriginalEducationLevel> getOriginalEducationLevels(Integer countryId, List<Integer> eduLevelId, Date dateTo, OrderCriteria orderCriteria);
    public int saveOriginalEducationLevel(int id, String name, String nameTranslated, int eduLevelId, int countryId, Date dateFrom, Date dateTo);


    public NationalQualificationsFramework getNationalQualificationsFramework(int id);
    public List<NationalQualificationsFramework> getNationalQualificationsFrameworks(Date dateTo, Integer countryId, OrderCriteria orderCriteria);
    public int saveNationalQualificationsFramework(int id, String name, int countryId, Date dateFrom, Date dateTo);

    public City getCity(int id);
    public List<City> getCities();

    public GraoCity getGraoCity(int id);
    public List<GraoCity> getGraoCities();

    public ExpertPosition getExpertPosition(int expertPositionId);
    public List<ExpertPosition> getExpertPositions();
    public int saveExpertPosition(int id, String name, Integer appStatusId, Date dateFrom, Date dateTo);


  /**
   *
   * @param type - edin ot definiranite v {@link com.nacid.bl.academicrecognition.BGAcademicRecognitionInfo}
   * @param nameContains
   * @return
     */
  public List<String> getBgAcademicRecognitionSuggestion(String type, String nameContains);

  public List<GraduationWay> getGraduationWays(int applicationType, Date dateTo, OrderCriteria orderCriteria);


  public List<Integer> getApplicationTypesPerGraduationWay(int graduationWay);

  public void updateGraduationWayApplicationTypes(int graduationWay, List<Integer> appTypes);

  public List<EducationLevel> getEducationLevels(int applicationType, Date date, OrderCriteria orderCriteria);

  public List<Integer> getApplicationTypesPerEducationLevel(int eduLevelId);

  public void updateEducationLevelApplicationTypes(int eduLevelId, List<Integer> appTypes);

  public List<DocumentReceiveMethod> getDocumentReceiveMethods(Date dateTo, OrderCriteria orderCriteria);

  public DocumentReceiveMethod getDocumentReceiveMethod(int id);
  public int saveDocumentReceiveMethod(int id, String name, boolean hasDocumentRecipient, boolean eservicesRequirePaymentReceipt, Date dateFrom, Date dateTo);

  public Language getLanguage(int id);

  public int saveLanguage(int id, String name, Date dateFrom, Date dateTo, String iso639code);

  public List<Language> getLanguages(Date dateTo, OrderCriteria orderCriteria);

}
