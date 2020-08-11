package com.nacid.web.handlers.impl.applications;

import com.ext.nacid.web.handlers.impl.applications.ApplicantReportHandler;
import com.ext.nacid.web.model.applications.ExtTrainingCourseSpecialityWebModel;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.*;
import com.nacid.bl.events.Event;
import com.nacid.bl.events.EventDataProvider;
import com.nacid.bl.external.ExtCompany;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.external.applications.*;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.mail.MailDataProvider;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.bl.table.*;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.data.external.applications.ExtTrainingCourseSpecialityRecord;
import com.nacid.web.ApplicantTypeHelper;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.applications.CompanyWebModel;
import com.nacid.web.model.applications.PersonWebModel;
import com.nacid.web.model.applications.TransferUniversityWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.CheckBoxFilterWebModel;
import com.nacid.web.model.table.filters.ComboBoxFilterWebModel;
import com.nacid.web.model.table.filters.FilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ext.nacid.web.handlers.impl.applications.ExtApplicationsHandler.getAppStatusName;
import static com.nacid.bl.external.applications.ExtApplication.*;

public class EApplyingHandler extends NacidBaseRequestHandler {

    private final static String COLUMN_NAME_ID = "id";
    private final static String COLUMN_NAME_NAME = "Име";
    private final static String COLUMN_NAME_DATE= "Дата";
    private final static String COLUMN_NAME_ESIGN= "Електронно подписан";
    private final static String COLUMN_NAME_APPLICATION_TYPE = "Тип на заявлението";
    private final static String COLUMN_NAME_STATUS = "Статус";
    private final static String COLUMN_NAME_STATUS_ID = "statusId";

    private final static String COLUMN_NAME_COMMUNICATED = "Комуникирано със заявителя";

    public final static String ENTRY_NUM_SERIES_REQUEST_PARAMETER = "entryNumSeries";


    private static final String FILTER_NAME_STATUS = "statusFilter";
    private static final String FILTER_NAME_COMMUNICATED = "communicatedFilter";

    public EApplyingHandler(ServletContext servletContext) {
        super(servletContext);
    }
	
	@Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        Integer extApplId = DataConverter.parseInteger(request.getParameter("id"), null);


        Integer applicantType = DataConverter.parseInteger(request.getParameter("applicant_type"), null);
        if (applicantType == null) {
            throw new RuntimeException("Unknown applicantType" + request.getParameter("applicant_type"));
        }

        Integer applicantId = null;
        Integer applicantCompanyId = null;
        Integer representativeId = null;
        Integer ownerId = null;

        if (applicantType == ApplicantTypeHelper.APPLICANT_TYPE_PHYSICAL_PERSON) {
            applicantId = savePersonIfNecessary(request, getNacidDataProvider(), "applicant");
        } else if (applicantType == ApplicantTypeHelper.APPLICANT_TYPE_LEGAL_PERSON || applicantType == ApplicantTypeHelper.APPLICANT_TYPE_UNIVERSITY) {
            applicantCompanyId = saveCompany(request, getNacidDataProvider());
        } else {
            throw new RuntimeException("Unknown applicantType " + applicantType);
        }


        representativeId = savePersonIfNecessary(request, getNacidDataProvider(), "representative");
        ownerId = savePersonIfNecessary(request, getNacidDataProvider(), "owner");
        ownerId = ownerId == null ? applicantId : ownerId;

        List<Integer> universities = saveUniversitiesIfNecessary(request, getNacidDataProvider());
        List<Integer> specialities = readSpecialities(request);


        ExtApplicationsDataProvider extAppDP = getNacidDataProvider().getExtApplicationsDataProvider();
        String diplomaSeries = RequestParametersUtils.parseString(request, "diploma_series", null);
        String diplomaNumber = RequestParametersUtils.parseString(request, "diploma_number", null);
        String diplomaRegistrationNumber = RequestParametersUtils.parseString(request, "diploma_registration_number", null);
        Date diplomaDate = RequestParametersUtils.parseDate(request, "diploma_date");
        int intApplicationId = extAppDP.transferApplicationToIntDB(extApplId,
                getLoggedUser(request, response).getUserId(), applicantId, applicantCompanyId, representativeId, ownerId, universities, specialities,diplomaSeries, diplomaNumber, diplomaRegistrationNumber, diplomaDate );

        EventDataProvider eDP = getNacidDataProvider().getEventDataProvider();



        ExtApplicationsDataProvider extApplicationsDataProvider = getNacidDataProvider().getExtApplicationsDataProvider();
        ExtApplication extApplication = extApplicationsDataProvider.getApplication(extApplId);

        ApplicationsDataProvider applicationsDataProvider = getNacidDataProvider().getApplicationsDataProvider();
        Application application = applicationsDataProvider.getApplication(intApplicationId);

        int attachmentId = sendMessageToUser(request, response, extApplication, application);
//        if (application.getApplicationType() == ApplicationType.RUDI_APPLICATION_TYPE) {
//            eDP.recalculateEvent(0, Event.EVENT_BRING_DOCUMENTS, intApplicationId, attachmentId, DocCategory.APPLICATION_ATTACHMENTS, DocumentType.DOC_TYPE_MESSAGE_BY_EMAIL_OUT);
//        }


        request.setAttribute(WebKeys.SYSTEM_MESSAGE,
                new SystemMessageWebModel("Заявлението е прехвърлено", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));

        sendMessageToAdministrators(request, response, extApplication, application);

        removeCachedTables(request);
        handleView(request, response);
    }

    public static void removeCachedTables(HttpServletRequest request) {
        request.getSession().removeAttribute(WebKeys.TABLE_EXT_APPLICATIONS + "0");
        request.getSession().removeAttribute(WebKeys.TABLE_EXT_APPLICATIONS + "1");
        request.getSession().removeAttribute(WebKeys.TABLE_EXT_APPLICATIONS + "2");
        request.getSession().removeAttribute(WebKeys.TABLE_EXT_APPLICATIONS + "3");
    }

    protected List<Integer> saveUniversitiesIfNecessary(HttpServletRequest request, NacidDataProvider nacidDataProvider) {
        Integer universitiesCount = RequestParametersUtils.parseInteger(request, "universities_count", null);
        if (universitiesCount == null) {
            throw new RuntimeException("Unknown universities count...");
        }
        List<Integer> result = new ArrayList<>();
        UniversityDataProvider universityDataProvider = nacidDataProvider.getUniversityDataProvider();
        for (int i = 1; i <= universitiesCount; i++) {
            int uniId = RequestParametersUtils.parseInteger(request, "university" + i + "_id", 0);
            if (uniId == 0) {
                String uniBgName = RequestParametersUtils.parseString(request, "university" + i + "_name_bg", null);
                String uniOrgName = RequestParametersUtils.parseString(request, "university" + i + "_original_name", null);
                int uniCountryId = RequestParametersUtils.parseInteger(request, "university" + i + "_country_id", 0);
                String city = RequestParametersUtils.parseString(request, "university" + i + "_city", null);
                String address = RequestParametersUtils.parseString(request, "university" + i + "_address", null);
                Integer universityGenericNameId = RequestParametersUtils.parseInteger(request, "university" + i + "_generic_name_id", null);
                List<University> unis = universityDataProvider.getUniversities(uniCountryId, UniversityDataProvider.NAME_TYPE_BG, true, uniBgName);
                if (unis != null && unis.get(0).getBgName().equals(uniBgName)) {
                    throw new RuntimeException("University with the same name exists....");
                }
                uniId = universityDataProvider.saveUniversity(0, uniCountryId, uniBgName, uniOrgName, city, address, null, null, null, null, null, new Date(), null, universityGenericNameId);
            }
            result.add(uniId);
        }
        return result;
    }

    protected List<Integer> readSpecialities(HttpServletRequest request) {
        Integer specialitiesCount = RequestParametersUtils.parseInteger(request, "specialities_count", null);
        if (specialitiesCount == null) {
            return null;
//            throw new RuntimeException("Unknown specialities count...");
        }
        List<Integer> result = new ArrayList<>();
        for (int i = 1; i <= specialitiesCount; i++) {
            int specialityId = RequestParametersUtils.parseInteger(request, "newSpeciality" + i + "Id", 0);
            if (specialityId == 0) {
                throw new RuntimeException("Unknow speciality id");
            }
            result.add(specialityId);
        }
        return result;
    }
    /**
     *
     * @param request
     * @param nacidDataProvider
     * @param personType - applicant/representative/owner
     * @return
     */
	protected Integer savePersonIfNecessary(HttpServletRequest request, NacidDataProvider nacidDataProvider, String personType) {
	    Integer personId = DataConverter.parseInteger(request.getParameter(personType + "_record_id"), null);
        if (personId != null) {
            return personId;
        }
        Integer persIdType = DataConverter.parseInteger(request.getParameter(personType + "_personal_id_type"), null);
        String persId = request.getParameter(personType + "_personal_id");
        if (StringUtils.isEmpty(persId)) {
            return null;
        }

        //ako e natisnat butona za kopirane na dannite na FL, to togava se tyrsi v bazata FL s tozi identifikator i ako ima takyv, to zaqvlenieto se vryzva s nego. Ideqta e che pri vyvejdane na zaqvlenie v koeto
        //applicant = owner i liceto ne sy6testvuva v backoffice-a, to toj shte se vyvede samo vednyj kato applicant i sled tova shte se prochete ot bazata kato owner!
        boolean copiedData = DataConverter.parseBoolean(request.getParameter(personType + "_copied_data"));
        if (copiedData) {
            List<Person> persons = nacidDataProvider.getApplicationsDataProvider().getPersons(persIdType, persId, false);
            if (persons != null && persons.size() > 0) {
                //vry6ta person-a s naj-golqmoto ID
                return persons.stream().sorted(Comparator.comparing(p -> -p.getId())).findFirst().get().getId();
            }

        }

        String fName = request.getParameter(personType + "_first_name");
        String sName = request.getParameter(personType + "_middle_name");
        String lName = request.getParameter(personType + "_last_name");
        Integer countryId = DataConverter.parseInteger(request.getParameter(personType + "_birth_place_country"), null);
        String city = request.getParameter(personType + "_birth_place_location");
        Date birthDate = DataConverter.parseDate(request.getParameter(personType + "_birth_date"));
        Integer citizenship = DataConverter.parseInteger(request.getParameter(personType + "_citizenship"), null);
        
        ApplicationsDataProvider appDP = nacidDataProvider.getApplicationsDataProvider();

        personId = appDP.savePerson(0, fName, sName, lName, persId, persIdType,
                countryId, city, birthDate, citizenship);

        return personId;
	}
    protected Integer saveCompany(HttpServletRequest request, NacidDataProvider nacidDataProvider) {
        Integer companyId = DataConverter.parseInteger(request.getParameter("applicant_company_id"), null);
        if (companyId != null) {
            return companyId;
        }
        String eik = DataConverter.parseString(request.getParameter("applicant_company_eik"), null);
        String name = DataConverter.parseString(request.getParameter("applicant_company_name"), null);
        Integer countryId = DataConverter.parseInteger(request.getParameter("applicant_company_country_id"), null);
        String city = DataConverter.parseString(request.getParameter("applicant_company_city"), null);
        String postCode = DataConverter.parseString(request.getParameter("applicant_company_pcode"), null);
        String address = DataConverter.parseString(request.getParameter("applicant_company_address"), null);
        CompanyDataProvider companyDataProvider = nacidDataProvider.getCompanyDataProvider();
        return companyDataProvider.saveCompany(0, name, countryId, city, postCode, address, null, null, null, eik, null);
    }
	/**
	 * 
	 * @param request
	 * @param nacidDataProvider
	 * @return - ako izobshto nqma documentData (v momenta v reguliranite profesii nqma documentdata), togava vry6ta null
	 */
	protected Integer saveDocumentData(HttpServletRequest request, NacidDataProvider nacidDataProvider, int personId) {
	    Integer documentData = DataConverter.parseInteger(request.getParameter("documentData"), null);
	    if (documentData == null) {
	        return null;
	    }
	    //ako ima selectnat documentId, togava vry6ta nomera mu!
	    Integer id = DataConverter.parseInteger(request.getParameter("documentData.id"), null);
	    if (id != null) {
	        return id;
	    }
	    String number = DataConverter.parseString(request.getParameter("documentData.number"), null);
	    String issuedBy = DataConverter.parseString(request.getParameter("documentData.issuedBy"), null);
	    Date issueDate = DataConverter.parseDate(request.getParameter("documentData.dateOfIssue"));
	    if (!StringUtils.isEmpty(number) && !StringUtils.isEmpty(issuedBy) && issueDate != null) {
            ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
            id = applicationsDataProvider.savePersonDocument(null, personId, issueDate, issuedBy, number);
        }
	    return id;
	}



    private int sendMessageToAdministrators(HttpServletRequest request, HttpServletResponse response, ExtApplication extApplication, Application application) {

        UtilsDataProvider utilsDataProvider = getNacidDataProvider().getUtilsDataProvider();

        String msgBody = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.APPLICATION_TRANSFERRED_ADMININSTRATOR_MESSAGE);

        msgBody = msgBody.replace("{DOCFLOW_NUMBER}", application.getDocFlowNumber());


        String msgSubject = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.APPLICATION_TRANSFERRED_ADMININSTRATOR_SUBJECT).replace("{DOCFLOW_NUMBER}", application.getDocFlowNumber());

        MailDataProvider mailDataProvider = getNacidDataProvider().getMailDataProvider();
        String sender = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.MAIL_SENDER);
        String receivers =  utilsDataProvider.getCommonVariableValue(UtilsDataProvider.APPLICATION_TRANSFERRED_ADMININSTRATOR_MAIL);

        mailDataProvider.sendMessage(sender, sender, receivers, receivers, msgSubject, msgBody);

        return saveMessageContentAsAttachment(sender, receivers, msgSubject, msgBody, application.getId(), getLoggedUser(request, response).getUserId());


    }
	/**
	 * izprashta message do user-a, vyvel external Application-a s informaciq 4e zaqvlenieto mu e prehvyrleno vyv vytre6nata baza i e polu4ilo dadeniq delovoden nomer.
	 * @param request
	 * @param response
     * @param extApplication
     * @param  application
	 * @return attachmentId-to na izprateniq mail ( mail-a se dobavq kato attached document kym dadenoto zaqvlenie)
	 */
	private int sendMessageToUser(HttpServletRequest request, HttpServletResponse response, ExtApplication extApplication, Application application) {

		UtilsDataProvider utilsDataProvider = getNacidDataProvider().getUtilsDataProvider();


		String bodyKey;
        if (application.getApplicationType() == ApplicationType.RUDI_APPLICATION_TYPE) {
            bodyKey = UtilsDataProvider.APPLICATION_TRANSFERRED_MESSAGE;

        } else if (application.getApplicationType() == ApplicationType.STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE) {
            bodyKey = UtilsDataProvider.STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TRANSFERRED_MESSAGE;
        } else if (application.getApplicationType() == ApplicationType.DOCTORATE_APPLICATION_TYPE) {
            bodyKey = UtilsDataProvider.DOCTORATE_APPLICATION_TRANSFERRED_MESSAGE;
        } else {
            throw new RuntimeException("Unknown application type...");
        }
        String msgBody = utilsDataProvider.getCommonVariableValue(bodyKey);

		msgBody = MessageFormat.format(msgBody, DataConverter.formatDateTime(extApplication.getTimeOfCreation(), false), application.getDocFlowNumber());
		String msgSubject = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.APPLICATION_TRANSFERRED_SUBJECT);
		
		MailDataProvider mailDataProvider = getNacidDataProvider().getMailDataProvider();
		String sender = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.MAIL_SENDER);
        Integer emailId = mailDataProvider.sendMessage(sender, sender, application.getApplicantNames(), application.getEmail(), msgSubject, msgBody);
        getNacidDataProvider().getExtApplicationsDataProvider().saveApplicationComment(extApplication.getId(), "Subject:"  + msgSubject + "\nBody:" + msgBody, true, emailId, true, getLoggedUser(request, response).getUserId());
		

        return saveMessageContentAsAttachment(sender, application.getEmail(), msgSubject, msgBody, application.getId(), getLoggedUser(request, response).getUserId());
	}

    int saveMessageContentAsAttachment(String sender, String receiver, String msgSubject, String msgBody, int applicationId, int loggedUserId) {
        AttachmentDataProvider attDP = getNacidDataProvider().getApplicationAttachmentDataProvider();
        StringBuilder sb = new StringBuilder();
        sb.append("From:" + sender + "\n\r");
        sb.append("To:" + receiver + "\n\r");
        sb.append("Subject:" + msgSubject + "\n\r");
        sb.append("Body:" + msgBody);
        byte[] data;
        try {
            data = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw Utils.logException(e);
        }
        InputStream is = new ByteArrayInputStream(data);
        return attDP.saveAttacment(0, applicationId, msgBody,
                DocumentType.DOC_TYPE_MESSAGE_BY_EMAIL_OUT,
                CopyType.ELECTRONIC_FORM,
                null, null,
                "text/plain", "mail_out_"+DataConverter.formatDateTime(new Date(), false) + ".txt",
                is, data.length,
                null, null, null, 0, loggedUserId);
    }

    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
    	int applicationId = DataConverter.parseInt(request.getParameter("id"), 0);
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		ExtApplicationsDataProvider extAppDP = nacidDataProvider.getExtApplicationsDataProvider();
		ExtApplication application = applicationId == 0 ? null : extAppDP.getApplication(applicationId);
		if (application == null) {
			throw new RuntimeException("No application is set....");
		}
		ApplicantReportHandler.generateExternalApplicantReport(nacidDataProvider, application, request, null);

        ExtTrainingCourse trainingCourse = application.getTrainingCourse();
        ExtPerson owner = trainingCourse.getOwner();
		initApplicantData(request, application.getApplicant(), application.getApplicantCompany(), application.getRepresentative(), owner);
        List<? extends ExtUniversityWithFaculty> uf = trainingCourse.getUniversityWithFaculties();

        initUniversitiesData(request, uf, getNacidDataProvider().getNomenclaturesDataProvider().getCountries(null, null));
        initSpecialitiesData(request, trainingCourse.getTrainingCourseSpecialities());
        initDiplomaData(request, trainingCourse);
        initSimilarDiplomas(request, nacidDataProvider, trainingCourse, owner);

//		new ApplicantMailHandler(getServletContext()).handleList(request, response);
        new ExtApplicationCommentHandler(getServletContext()).handleList(request, response);
		
		
    }
    protected void initSimilarDiplomas(HttpServletRequest request, NacidDataProvider nacidDataProvider, ExtTrainingCourse trainingCourse, ExtPerson owner) {
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
        List<ExtTrainingCourseSpecialityRecord> specialities = trainingCourse.getTrainingCourseSpecialities();
        List<String> specialityNames = specialities == null ? null : specialities.stream().map(speciality -> {
            return StringUtils.isEmpty(speciality.getSpecialityTxt()) ? nomenclaturesDataProvider.getSpeciality(speciality.getSpecialityId()).getName() : speciality.getSpecialityTxt();
        }).collect(Collectors.toList());

        Integer diplomaYear = null;
        if (trainingCourse.getDiplomaDate() != null) {
            Calendar cal = new GregorianCalendar();
            cal.setTime(trainingCourse.getDiplomaDate());
            diplomaYear = cal.get(Calendar.YEAR);
        }


        List<String> universityNames = new ArrayList<>();
        List<Integer> universityCountryIds = new ArrayList<>();
        List<String> universityCountryNames = new ArrayList<>();
        List<? extends ExtUniversityWithFaculty> unis = trainingCourse.getUniversityWithFaculties();
        if (unis != null) {
            unis.stream()
                    .map(ExtUniversityWithFaculty::getUniversity)
                    .forEach(u -> {
                if (u.isStandartUniversity()) {
                    universityNames.add(u.getBgName());
                    universityCountryIds.add(u.getCountryId());
                } else {
                    String[] parts = u.getUniversityTxt().split(",");
                    if (parts.length > 0) {
                        universityNames.add(parts[0].trim());
                    }
                    if (parts.length > 2) {
                        universityCountryNames.add(parts[2].trim());
                    }
                }
            });
        }



        ApplicationsHandler.initSimilarDiplomas(request, nacidDataProvider, owner.getCivilId(), owner.getFName(), owner.getSName(), owner.getLName(), universityCountryIds, universityCountryNames, trainingCourse.getEducationLevelId(), diplomaYear, specialityNames, universityNames, null);


    }
    protected void initApplicantData(HttpServletRequest request, ExtPerson applicantPerson, ExtCompany applicantCompany, ExtPerson representative, ExtPerson owner) {
        
        NomenclaturesDataProvider nDP = getNacidDataProvider().getNomenclaturesDataProvider(); 
        List<Country> countries = nDP.getCountries(null, null);



        int applicationId = DataConverter.parseInt(request.getParameter("id"), 0);
        request.setAttribute("extApplId", applicationId);

        ApplicationsDataProvider appDP = getNacidDataProvider().getApplicationsDataProvider();

        addPersonToWebModel(applicantPerson, appDP, request, "Applicant", countries);
        addPersonToWebModel(representative, appDP, request, "Representative", countries);
        addPersonToWebModel(owner, appDP, request, "Owner", countries);

        Integer applicantCompanyCountry = NomenclaturesDataProvider.COUNTRY_ID_BULGARIA;
        if (applicantCompany != null) {
            CompanyDataProvider cdp = getNacidDataProvider().getCompanyDataProvider();
            List<Company> companies = cdp.getCompaniesByEik(applicantCompany.getEik());
            request.setAttribute("backofficeCompanyCount", companies == null ? 0 : companies.size());
            if (companies != null && companies.size() == 1) {
                request.setAttribute("backofficeApplicantCompany", new CompanyWebModel(companies.get(0)));
                applicantCompanyCountry = companies.get(0).getCountryId();
            }
        }

        ComboBoxUtils.generateNomenclaturesComboBox(applicantCompanyCountry, countries, false, request, "applicantCompanyCountry", false);



        setNextScreen(request, "ext_report_applicant");
    }
    protected void initUniversitiesData(HttpServletRequest request, List<? extends ExtUniversityWithFaculty> universities, List<Country> countries) {
        if (universities != null) {
            List<TransferUniversityWebModel> universityWebModels = new ArrayList<>();
            ExtUniversityWithFaculty uf = universities.get(0);
            ExtUniversity u = uf.getUniversity();
            TransferUniversityWebModel wm = new TransferUniversityWebModel(u, getNacidDataProvider().getNomenclaturesDataProvider());

            if (!u.isStandartUniversity()) {
                //proverqva dali shte nameri standarten universitet s tova ime!
                String bgName = wm.getBgName();
                UniversityDataProvider universityDataProvider = getNacidDataProvider().getUniversityDataProvider();
                List<University> unis = universityDataProvider.getUniversities(wm.getIntCountryId(), UniversityDataProvider.NAME_TYPE_BG, true, bgName);
                if (unis != null) {
                    Optional<University> first = unis.stream().filter(uni -> bgName.equals(uni.getBgName())).findFirst();
                    if (first.isPresent()) {
                        wm = new TransferUniversityWebModel(first.get());
                    }
                }
            }

            //prehvyrlq se samo base universiteta!
            universityWebModels.add(wm);
            //List<TransferUniversityWebModel> universityWebModels = universities.stream().map(c -> new TransferUniversityWebModel(c, getNacidDataProvider().getNomenclaturesDataProvider())).collect(Collectors.toList());
            request.setAttribute("universities", universityWebModels);
            ComboBoxUtils.generateNomenclaturesComboBox(null, countries, false, request, "universityCountry", true);
        }
    }

    protected void initSpecialitiesData(HttpServletRequest request, List<? extends ExtTrainingCourseSpecialityRecord> specialityRecords) {
        if (specialityRecords != null) {
            NomenclaturesDataProvider nomenclaturesDataProvider = getNacidDataProvider().getNomenclaturesDataProvider();
            List<ExtTrainingCourseSpecialityWebModel> wm = specialityRecords.stream().map(s -> new ExtTrainingCourseSpecialityWebModel(s.getSpecialityId(), s.getSpecialityTxt(), s.getSpecialityId() == null ? null : nomenclaturesDataProvider.getSpeciality(s.getSpecialityId()).getName())).collect(Collectors.toList());
            request.setAttribute("specialities", wm);
            ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_PROFESSION_GROUP, nomenclaturesDataProvider, false, request, "professionGroupCombo", null, true);
        }
    }
    private void initDiplomaData(HttpServletRequest request, ExtTrainingCourse tc) {
        request.setAttribute("diploma_date", DataConverter.formatDate(tc.getDiplomaDate()));
        request.setAttribute("diploma_number", tc.getDiplomaNumber());
        request.setAttribute("diploma_series", tc.getDiplomaSeries());
        request.setAttribute("diploma_registration_number", tc.getDiplomaRegistrationNumber());

    }
    private void addPersonToWebModel(ExtPerson person, ApplicationsDataProvider appDP, HttpServletRequest request, String name, List<Country> countries) {
        Integer birthCountry = NomenclaturesDataProvider.COUNTRY_ID_BULGARIA;
        Integer citizenship = NomenclaturesDataProvider.COUNTRY_ID_BULGARIA;
        Integer civilIdtype = CivilIdType.CIVIL_ID_TYPE_EGN;
        if (person != null) {
            civilIdtype = person.getCivilIdTypeId();
            String civilId = person.getCivilId();
            List<Person> backofficePersons = civilIdtype == null || "0".equals(civilId) ? null : appDP.getPersons(civilIdtype, civilId, false);
            request.setAttribute("backoffice" + name + "Count", backofficePersons == null ? 0 : backofficePersons.size());
            if (backofficePersons != null && backofficePersons.size() == 1) {
                request.setAttribute("backoffice" + name, new PersonWebModel(backofficePersons.get(0)));
                birthCountry = backofficePersons.get(0).getBirthCountryId();
                citizenship = backofficePersons.get(0).getCitizenshipId();
            }
        }

        ComboBoxUtils.generateNomenclaturesComboBox(birthCountry, countries, false, request, "birthCountry" + name, false);
        ComboBoxUtils.generateNomenclaturesComboBox(citizenship, countries, false, request, "citizenship" + name, true);
        ComboBoxUtils.generateNomenclaturesRadioButton(civilIdtype, getNacidDataProvider().getNomenclaturesDataProvider().getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, null, null), false, request, "civilIdType" + name);
    }


    
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String tableName = getTableName(request);
        Table table = (Table) session.getAttribute(tableName );
        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_ESIGN, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);
            table.addColumnHeader(COLUMN_NAME_APPLICATION_TYPE, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_STATUS_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_COMMUNICATED, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);
            session.setAttribute(tableName , table);
            resetTableData(request, response);
        }

        // TableState settings
        TableState tableState = (TableState) session.getAttribute(tableName + WebKeys.TABLE_STATE);

        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_STATUS, COLUMN_NAME_STATUS_ID, request, table, tableState);
            TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_COMMUNICATED, COLUMN_NAME_COMMUNICATED, request, table, tableState);
            session.setAttribute(tableName + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък със заявления");

        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
        webmodel.hideUnhideColumn(COLUMN_NAME_STATUS_ID, true);
        webmodel.addRequestParam(TableWebModel.OPERATION_NAME_ALL, ENTRY_NUM_SERIES_REQUEST_PARAMETER, request.getParameter(ENTRY_NUM_SERIES_REQUEST_PARAMETER));
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        webmodel.addFormAdditionalRequestParam(ENTRY_NUM_SERIES_REQUEST_PARAMETER, request.getParameter(ENTRY_NUM_SERIES_REQUEST_PARAMETER));

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(tableName + WebKeys.FILTER_WEB_MODEL);
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();

            filtersWebModel.addFiler(generateStatusFilter(request));
            filtersWebModel.addFiler(generateCommunicatedFilterWebModel(request));
            session.setAttribute(tableName + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);

    }

    protected void resetTableData(HttpServletRequest request, HttpServletResponse response) {
        String tableName = getTableName(request);
        Table table = (Table) request.getSession().getAttribute(tableName);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ExtApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getExtApplicationsDataProvider();

        List<Integer> series = RequestParametersUtils.convertRequestParameterToIntegerList(request.getParameter("entryNumSeries"));
        

        List<EApplyApplication> apps = applicationsDataProvider.getEAppliedApplications(series);
        NumgeneratorDataProvider numgeneratorDataProvider = nacidDataProvider.getNumgeneratorDataProvider();
        if (apps != null) {
            for (EApplyApplication app : apps) {
                try {

                    List<Integer> appKinds = app.getApplicationKinds();
                    Set<Integer> appKindsSet = new HashSet<>(appKinds);
                    String applicationsKinds;
                    if (appKindsSet.contains(NumgeneratorDataProvider.STATUTE_SERIES_ID) ||
                            appKindsSet.contains(NumgeneratorDataProvider.AUTHENTICITY_SERIES_ID) ||
                            appKindsSet.contains(NumgeneratorDataProvider.RECOMMENDATION_SERIES_ID)) {
                            applicationsKinds = ApplicationsHandler.generateApplicationTypesListColumn(request, ApplicationType.STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE, appKinds);
                    } else {
                        List<String> applicationsKind = new ArrayList<String>();
                        for (Integer i : app.getApplicationKinds()) {
                            applicationsKind.add(numgeneratorDataProvider.getEntryNumSeriesNameById(i));
                        }
                        applicationsKinds = StringUtils.join(applicationsKind, ", ");
                    }

                    TableRow row = table.addRow(app.getId(), app.getTimeOfCreation(), app.getNames(), app.isEsigned(), applicationsKinds, getAppStatusName(app.getApplicationStatus(), null), app.getApplicationStatus(), app.isCommunicated());
                    row.setEditable(app.getApplicationStatus() == ExtApplication.STATUS_NOT_EDITABLE);
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }




    protected static String getTableName(HttpServletRequest request) {
        String param = request.getParameter(ENTRY_NUM_SERIES_REQUEST_PARAMETER);
        if (param == null || "null".equals(param)) {
            return WebKeys.TABLE_EXT_APPLICATIONS + "0";
        } else if (param.equals("4")) {
            return WebKeys.TABLE_EXT_APPLICATIONS + "1";
        } else if (param.equals("16;17;18")) {
            return WebKeys.TABLE_EXT_APPLICATIONS + "2";
        } else if (param.equals("21")) {
            return WebKeys.TABLE_EXT_APPLICATIONS + "3";
        } else {
            throw new RuntimeException("Unknown entryNumSeries value....");
        }


        //String tableName = WebKeys.TABLE_EXT_APPLICATIONS + ;
        //return tableName;
    }

    private FilterWebModel generateStatusFilter(HttpServletRequest request) {
        ComboBoxWebModel combobox = generateStatusComboBox(DataConverter.parseInteger(request.getParameter(FILTER_NAME_STATUS), null), getNacidDataProvider().getNomenclaturesDataProvider(), request);
        ComboBoxFilterWebModel result = new ComboBoxFilterWebModel(combobox, FILTER_NAME_STATUS, COLUMN_NAME_STATUS);
        result.setElementClass("brd w300");
        return result;
    }

    private static ComboBoxWebModel generateStatusComboBox(Integer statusId, NomenclaturesDataProvider nomDP, HttpServletRequest request) {

        ComboBoxWebModel wm = new ComboBoxWebModel(statusId == null ? null : statusId.toString(), true);
        wm.addItem(STATUS_EDITABLE + "", getAppStatusName(STATUS_EDITABLE, null));
        wm.addItem(STATUS_NOT_EDITABLE + "", getAppStatusName(STATUS_NOT_EDITABLE, null));
        wm.addItem(STATUS_FINISHED + "", getAppStatusName(STATUS_FINISHED, null));
        wm.addItem(STATUS_TRANSFERED + "", getAppStatusName(STATUS_TRANSFERED, null));
        request.setAttribute(COLUMN_NAME_STATUS_ID, wm);
        return wm;
    }
    private static FilterWebModel generateCommunicatedFilterWebModel(HttpServletRequest request) {
	    Integer communicated = DataConverter.parseInteger(request.getParameter(FILTER_NAME_COMMUNICATED), null);
        ComboBoxWebModel wm = new ComboBoxWebModel(communicated == null ? null : communicated.toString(), true);
        wm.addItem("1", "да");
        wm.addItem("0", "не");
        ComboBoxFilterWebModel result = new ComboBoxFilterWebModel(wm, FILTER_NAME_COMMUNICATED, COLUMN_NAME_COMMUNICATED);
        result.setElementClass("brd w100");
        return result;
    }
}
