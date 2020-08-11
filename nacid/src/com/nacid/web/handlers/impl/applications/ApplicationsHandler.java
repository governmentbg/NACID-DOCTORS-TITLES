package com.nacid.web.handlers.impl.applications;

import com.ext.nacid.web.handlers.impl.applications.ApplicantReportHandler;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.*;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.comision.ComissionMemberDataProvider;
import com.nacid.bl.comision.ComissionMemberOrderCriteria;
import com.nacid.bl.comision.CommissionCalendar;
import com.nacid.bl.external.applications.ExtApplication;
import com.nacid.bl.external.applications.ExtApplicationsDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.TrainingCourseSpecialityImpl;
import com.nacid.bl.impl.applications.UniversityIdWithFacultyId;
import com.nacid.bl.impl.applications.UniversityWithFaculty;
import com.nacid.bl.mail.MailDataProvider;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.bl.table.*;
import com.nacid.bl.users.User;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.report.JasperReportGenerator;
import com.nacid.report.export.ExportType;
import com.nacid.report.export.JasperReportNames;
import com.nacid.web.ApplicantTypeHelper;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.handlers.impl.ajax.DiplomaTypeAjaxHandler;
import com.nacid.web.handlers.impl.ajax.OriginalEducationLevelAjaxHandler;
import com.nacid.web.handlers.impl.events.ApplicationEventsHandler;
import com.nacid.web.model.applications.*;
import com.nacid.web.model.comission.CommissionCalendarWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.nomenclatures.FlatNomenclatureWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.*;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.nacid.bl.applications.DiplomaType.APPLICATION_TYPE_DIPLOMA_TYPE;
import static com.nacid.bl.nomenclatures.ApplicationType.*;
import static com.nacid.bl.nomenclatures.NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_AREA;

public class ApplicationsHandler extends NacidBaseRequestHandler {

    private final static String COLUMN_NAME_ID = "id";
    private final static String COLUMN_NAME_APPLICATION_NUMBER = "Делов. номер";
    private final static String COLUMN_NAME_DATE = "Дата";
    private final static String COLUMN_NAME_UNIVERSITY = MessagesBundle.getMessagesBundle().getValue("University");
    private final static String COLUMN_NAME_COUNTRY = "Държава";
    private final static String COLUMN_NAME_SPECIALITY = "Специалност";
    private final static String COLUMN_NAME_EDU_LEVEL = "Придобита степен";
    private final static String COLUMN_NAME_EDU_LEVEL2 = "ОКС по диплома";
    private final static String COLUMN_NAME_NAME = "Име";
    //private final static String COLUMN_NAME_EMAIL = "Електронна поща";
    private final static String COLUMN_NAME_APP_STATUS = "Статус на заявлението";
    private final static String COLUMN_NAME_APP_DOCFLOW_STATUS = "Деловоден статус на заявлението";
    private final static String COLUMN_NAME_APP_STATUS_ID = "statusId";
    private final static String COLUMN_NAME_APP_DOCFLOW_STATUS_ID = "docflowStatusId";
    private final static String COLUMN_NAME_ESIGNED = "Ел. подп.";
    private final static String COLUMN_NAME_APPLICATION_TYPE = "Вид услуга";

    private final static String COLUMN_NAME_RECOGNIZED_QUALIFICATION = "Призната ПК";
    private final static String COLUMN_NAME_RECOGNIZED_PROF_GROUP = "Признато ПН";

    private static final String FILTER_NAME_APP_NUMBER = "appNumberFilter";
    private static final String FILTER_NAME_DOCFLOW_STATUS = "docflowStatusFilter";
    private static final String FILTER_NAME_STATUS = "statusFilter";
    private static final String FILTER_NAME_ONLY_ACTIVE = "onlyActive";

    public static final int FORM_ID_E_APPLYING = 0;
    public static final int FORM_ID_APPLICATION_DATA = 1;
    public static final int FORM_ID_TRAINING_DATA = 2;
    public static final int FORM_ID_ATTACHMENTS_DATA = 3;
    public static final int FORM_ID_STATUS_DATA = 4;
    public static final int FORM_ID_EXPERT_DATA = 5;

    private ServletContext servletContext;

    public ApplicationsHandler(ServletContext servletContext) {
        super(servletContext);
        this.servletContext = servletContext;
    }

    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nDP = nacidDataProvider.getNomenclaturesDataProvider();
        List<Country> countries = nDP.getCountries(null, null);
        ComboBoxUtils.generateNomenclaturesComboBox(NomenclaturesDataProvider.COUNTRY_ID_BULGARIA, countries, true, request, "homeCountry", false);
        ComboBoxUtils.generateNomenclaturesComboBox(null, countries, true, request, "applicantBirthCountry", true);
        ComboBoxUtils.generateNomenclaturesComboBox(NomenclaturesDataProvider.COUNTRY_ID_BULGARIA, countries, true, request, "reprCountryCombo", false);
        ComboBoxUtils.generateNomenclaturesComboBox(null, countries, true, request, "applicantCitizenship", true);

        ComboBoxUtils.generateNomenclaturesComboBox(null, countries, true, request, "ownerBirthCountry", true);
        ComboBoxUtils.generateNomenclaturesComboBox(null, countries, true, request, "ownerCitizenship", true);

        ComboBoxUtils.generateNomenclaturesComboBox(null, countries, true, request, "applicantCompanyCountry", true);

        ComboBoxUtils.generateUsersComboBox(null, nacidDataProvider.getUsersDataProvider().getUsers(0, 0, 1, NacidDataProvider.APP_NACID_ID), request, "responsibleUsers", true, false);

        generateCompaniesCombo(request, nacidDataProvider, null);
        addFlatNomenclaturesToWebModel(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, nDP, request,
                WebKeys.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true));

        ComboBoxUtils.generateDocumentReceiveMethodRadioButton(null, nDP.getDocumentReceiveMethods(null, null), true, request, "documentReceiveMethod");
        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_PERSONAL_ID_DOCUMENT_TYPE, nDP, true, request, "applicantPersonalIdDocumentType", null, true);
        request.setAttribute(WebKeys.BACK_SCREEN, getGroupName(request));
        request.setAttribute(WebKeys.NEXT_SCREEN, "application_edit");
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        int applicationId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (applicationId <= 0) {
            throw new UnknownRecordException("Unknown Application ID:request.getParameter(\"id\")=" + request.getParameter("id"));
        }
        Application application = getNacidDataProvider().getApplicationsDataProvider().getApplication(applicationId);
        if (application == null) {
            throw new UnknownRecordException("Unknown Application ID:" + applicationId);
        }
        addApplicationToWebModel(application, request, response, DataConverter.parseInt(request.getParameter("activeForm"), FORM_ID_APPLICATION_DATA));

        setOperationTypeInWebModel(request, "edit");
        request.setAttribute(WebKeys.BACK_SCREEN, getGroupName(request));
    }

    @Override
    public void handleView(HttpServletRequest request, HttpServletResponse response) {
        super.handleView(request, response);
        setOperationTypeInWebModel(request, "view");
    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
            new HomePageHandler(request.getSession().getServletContext()).processRequest(request, response);
            return;
        }
        int activeFormId = DataConverter.parseInt(request.getParameter("activeForm"), -1);
        if (activeFormId == -1) {
            throw new UnknownRecordException("Unknown active form id:" + activeFormId);
        }
        if (activeFormId == 1) {
            saveApplicationDataForm(request, response);
        } else if (activeFormId == 2) {
            saveTrainingCourse(request, response);
        }

        //resetTableData(request);
        // Regenerating saved application for edit!!!
        removeTableSessionAttributes(request);

        setOperationTypeInWebModel(request, "edit");
        
        request.setAttribute(WebKeys.BACK_SCREEN, getGroupName(request));

    }

    private void saveApplicationDataForm(HttpServletRequest request, HttpServletResponse response) {
        User user = getLoggedUser(request, response);
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
        TrainingCourseDataProvider trainingCourseDataProvider = nacidDataProvider.getTrainingCourseDataProvider();
        int recordId = DataConverter.parseInt(request.getParameter("id"), 0);
        Application application = null;
        if (recordId != 0) {
            application = applicationsDataProvider.getApplication(recordId);
            if (application == null) {
                throw new UnknownRecordException("Unknow application record with id=" + recordId);
            }
        }

        Integer applicantType = DataConverter.parseInteger(request.getParameter("applicant_type"), null);
        Integer applicantRecordId = null;
        Integer applicantCompanyRecordId = null;
        Integer ownerRecordId = DataConverter.parseInteger(request.getParameter("owner_record_id"), null);
        if (applicantType == ApplicantTypeHelper.APPLICANT_TYPE_PHYSICAL_PERSON) {
            applicantRecordId = DataConverter.parseInteger(request.getParameter("applicant_record_id"), null);

            if (applicantRecordId == null) {
                String applicantFirst = request.getParameter("applicant_first_name");
                String applicantMiddleName = request.getParameter("applicant_middle_name");
                String applicantLastName = request.getParameter("applicant_last_name");
                String applicantCivilId = request.getParameter("applicant_personal_id");
                int applicantCivilIdType = DataConverter.parseInt(request.getParameter("applicant_personal_id_type"), -1);
                if (applicantCivilIdType == -1) {
                    throw new UnknownRecordException("Unknown civilIdType ID:" + request.getParameter("applicant_personal_id_type"));
                }
                Integer applicantBirthCountryId = DataConverter.parseInt(request.getParameter("applicant_birth_place_country"), -1);
                if (applicantBirthCountryId == -1) {
                    throw new UnknownRecordException("Unknown applicantBirthCountryId ID:" + request.getParameter("applicant_birth_place_country"));
                }
                String applicantBirthCity = request.getParameter("applicant_birth_place_location");
                Date applicantBirthDate = DataConverter.parseDate(request.getParameter("applicant_birth_date"));
                Integer applicantCitizenshipId = DataConverter.parseInteger(request.getParameter("applicant_citizenship"), null);

                applicantRecordId = applicationsDataProvider.savePerson(0, applicantFirst, applicantMiddleName, applicantLastName, applicantCivilId,
                        applicantCivilIdType, applicantBirthCountryId, applicantBirthCity, applicantBirthDate, applicantCitizenshipId);
            }
        } else if (applicantType == ApplicantTypeHelper.APPLICANT_TYPE_LEGAL_PERSON || applicantType == ApplicantTypeHelper.APPLICANT_TYPE_UNIVERSITY) {
            applicantCompanyRecordId = DataConverter.parseInteger(request.getParameter("applicant_company_id"), null);

            if (applicantCompanyRecordId == null) {
                String companyEik = DataConverter.parseString(request.getParameter("applicant_company_eik"), null);
                String companyName = DataConverter.parseString(request.getParameter("applicant_company_name"), null);
                Integer companyCountryId = DataConverter.parseInteger(request.getParameter("applicant_company_country_id"), null);
                String companyCity = DataConverter.parseString(request.getParameter("applicant_company_city"), null);
                String postCode = DataConverter.parseString(request.getParameter("applicant_company_pcode"), null);
                String address = DataConverter.parseString(request.getParameter("applicant_company_address"), null);
                CompanyDataProvider companyDataProvider = nacidDataProvider.getCompanyDataProvider();
                applicantCompanyRecordId = companyDataProvider.saveCompany(0, companyName, companyCountryId, companyCity, postCode, address, null, new Date(), null, companyEik, null);
            }
        }

        if (ownerRecordId == null) {
            //TODO:tuk ima potencialen problem. Kakvo stava ako sa iskali da NE vyvedat owner???? Togava se slaga owner=applicant!!!
            String ownerFirst = DataConverter.parseString(request.getParameter("owner_first_name"), null);//ako nqma izbran owner ot autosuggestion, to se proverqva dali ima vyvedeno pyrvo ime na owner-a. Ako nqma, to za onwer se priema applicant-a!

            if (ownerFirst == null) {
                ownerRecordId = applicantRecordId;
            } else {
                String ownerMiddleName = DataConverter.parseString(request.getParameter("owner_middle_name"), null);
                String ownerLastName = DataConverter.parseString(request.getParameter("owner_last_name"), null);
                String ownerCivilId = DataConverter.parseString(request.getParameter("owner_personal_id"), null);
                int ownerCivilIdType = DataConverter.parseInt(request.getParameter("owner_personal_id_type"), -1);
                if (ownerCivilIdType == -1) {
                    throw new UnknownRecordException("Unknown civilIdType ID:" + request.getParameter("owner_personal_id_type"));
                }
                Integer ownerBirthCountryId = DataConverter.parseInt(request.getParameter("owner_birth_place_country"), -1);
                if (ownerBirthCountryId == -1) {
                    throw new UnknownRecordException("Unknown ownerBirthCountryId ID:" + request.getParameter("owner_birth_place_country"));
                }
                String ownerBirthCity = DataConverter.parseString(request.getParameter("owner_birth_place_location"), null);
                Date ownerBirthDate = DataConverter.parseDate(request.getParameter("owner_birth_date"));
                Integer ownerCitizenshipId = DataConverter.parseInteger(request.getParameter("owner_citizenship"), null);

                ownerRecordId = applicationsDataProvider.savePerson(0, ownerFirst, ownerMiddleName, ownerLastName, ownerCivilId,
                        ownerCivilIdType, ownerBirthCountryId, ownerBirthCity, ownerBirthDate, ownerCitizenshipId);


            }




        }

        boolean representativeDifferentPerson = DataConverter.parseBoolean(request.getParameter("representative_data"));
        boolean representativeIsCompany = DataConverter.parseBoolean(request.getParameter("represent_is_company"));
        Integer companyId = null;
        Integer reprCountryId = null;
        String reprCity = null;
        String reprPcode = null;
        String reprPhone = null;
        String reprAddressDetails = null;
        boolean dataAuthentic = DataConverter.parseBoolean(request.getParameter("data_authentic"));
        Boolean representativeAuthorized = null;
        if(representativeDifferentPerson) {
            if(representativeIsCompany) {
                companyId = DataConverter.parseInteger(request.getParameter("company"), null);
            }
            else {
                reprCountryId = DataConverter.parseInteger(request.getParameter("reprCountryId"), null);
                reprCity = request.getParameter("reprCity");
                reprPcode = request.getParameter("reprPcode");
                reprAddressDetails = DataConverter.escapeHtml(DataConverter.parseString(request.getParameter("reprAddressDetails"), ""));
            }
            reprPhone = DataConverter.escapeHtml(DataConverter.parseString(request.getParameter("reprPhone"), ""));
            representativeAuthorized = DataConverter.parseBoolean(request.getParameter("representative_authorized"));
        } 
        
        
        Integer representativeRecordId = representativeDifferentPerson ? DataConverter.parseInteger(request.getParameter("representative_record_id"), null) : null;
        
        
        // int trainingCourseRecordId = 0;
        String email = request.getParameter("applicant_email");
        int officialEmailCommunication = DataConverter.parseInt(request.getParameter("communicate_by_email"), 0);
        boolean personalDataUsage = DataConverter.parseBoolean(request.getParameter("personal_data_usage"));
        int homeCountryId = DataConverter.parseInt(request.getParameter("applicant_location_country"), 0);
        String homeCity = request.getParameter("applicant_location");
        String homePostCode = request.getParameter("applicant_location_post_code");
        String homeAddressDetails = DataConverter.escapeHtml(DataConverter.parseString(request.getParameter("applicant_address"), ""));
        String homePhone = DataConverter.escapeHtml(DataConverter.parseString(request.getParameter("homePhone"), ""));
        boolean homeIsBg;
        String bgCity;
        String bgPostCode;
        String bgAddressDetails;
        String bgPhone;
        if (homeCountryId == NomenclaturesDataProvider.COUNTRY_ID_BULGARIA) {
            homeIsBg = true;
            bgCity = null;
            bgPostCode = null;
            bgAddressDetails = null;
            bgPhone = null;
        } else {
            homeIsBg = false;
            bgCity = request.getParameter("bulgaria_contact_location");
            bgPostCode = request.getParameter("bulgaria_contact_location_post_code");
            bgAddressDetails = DataConverter.escapeHtml(DataConverter.parseString(request.getParameter("bulgaria_contact_address"), ""));
            bgPhone = DataConverter.escapeHtml(DataConverter.parseString(request.getParameter("bgPhone"), ""));
        }
        
        //Ako nqma predstavitel, togava tova neshto trqbva da ima stojnost na BG_ADDRESS_ONWER_APPLICANT
        int bgAddressOwner = representativeDifferentPerson ? DataConverter.parseInt(request.getParameter("bg_address_owner"), Application.BG_ADDRESS_OWNER_APPLICANT) : Application.BG_ADDRESS_OWNER_APPLICANT;

        // Tova trqbva da se settva samo pri nov zapis!!!!
        int createdByUserId = application == null ? user.getUserId() : application.getCreatedByUserId();
        

        // Tova trqbva da se settva samo pri nov zapis!!!
        Date timeOfCreation = application == null ? new Date() : application.getTimeOfCreation();
        int applicationStatusId = application == null ? NomenclaturesDataProvider.DEFAULT_APPLICATION_STATUS : application.getApplicationStatusId();
        int applicationDocflowStatusId = application == null ? ApplicationDocflowStatus.APPLICATION_VPROCEDURA_DOCFLOW_STATUS_CODE : application.getApplicationDocflowStatusId();
        int differentDiplomaNames = DataConverter.parseInt(request.getParameter("diploma_names"), 0);
        String appNumber = application == null ? null : application.getApplicationNumber();
        Date appDate = application == null ? null : application.getApplicationDate();

        /** Training course data manipulation */
        int trainingCourseRecordId = (application == null) ? 0 : application.getTrainingCourse().getId();
        String diplomaFirstName;
        String diplomaSurName;
        String diplomaLastName;
        if (differentDiplomaNames == 0) {
            diplomaFirstName = null;
            diplomaSurName = null;
            diplomaLastName = null;
        } else {
            diplomaFirstName = request.getParameter("diploma_firstName");
            diplomaSurName = request.getParameter("diploma_middleName");
            diplomaLastName = request.getParameter("diploma_lastName");
        }






        trainingCourseRecordId = trainingCourseDataProvider.saveTrainingCourse(trainingCourseRecordId, diplomaFirstName, diplomaSurName,
                diplomaLastName, ownerRecordId);
        /** End of training course Data manipulation */


        String representativeFirst = request.getParameter("representative_first_name");
        if (representativeDifferentPerson && representativeRecordId == null && !"".equals(representativeFirst)) {
            String representativeMiddleName = request.getParameter("representative_middle_name");
            String representativeLastName = request.getParameter("representative_last_name");
            String representativeCivilId = request.getParameter("representative_personal_id");
            int representativeCivilIdType = DataConverter.parseInt(request.getParameter("representative_personal_id_type"), -1);
            int representativeBirthCountry = 0;
            String representativeBirthCity = null;
        	representativeRecordId = applicationsDataProvider.savePerson(0, representativeFirst, representativeMiddleName, representativeLastName,
                    representativeCivilId, representativeCivilIdType, representativeBirthCountry, representativeBirthCity, null, null);
        	
        }
        Integer responsibleUser = DataConverter.parseInteger(request.getParameter("responsible_user"), null);
        String summary = null;
        String archiveNumber = null;
        String notes = request.getParameter("notes");
        String applicantInfo = null;
        String submittedDocs = null;
        if(application != null) {
            summary = application.getSummary();
            archiveNumber = application.getArchiveNumber();
            applicantInfo = application.getApplicantInfo();
            submittedDocs = application.getSubmittedDocs();
        }
        String representativeType = DataConverter.parseString(request.getParameter("representative_type"), null);
        Integer typePayment = DataConverter.parseInteger(request.getParameter("type_payment"), null);
        Integer deliveryType = DataConverter.parseInteger(request.getParameter("delivery_type"), null);
        Boolean declaration = DataConverter.parseIntegerToBoolean(DataConverter.parseInteger(request.getParameter("declaration"), null));
        String courierNameAddress = DataConverter.parseString(request.getParameter("courier_name_address"), null);
        String outgoingNumber = DataConverter.parseString(request.getParameter("outgoing_number"), null);
        String internalNumber = DataConverter.parseString(request.getParameter("internal_number"), null);
        Boolean isExpress = DataConverter.parseIntegerToBoolean(DataConverter.parseInteger(request.getParameter("is_express"), null));
        Integer applicationType = DataConverter.parseInteger(request.getParameter("application_type"), null);
        Integer documentReceiveMethodId = DataConverter.parseInteger(request.getParameter("document_receive_method_id"), null);
        Integer applicantPersonalIdDocumentType = DataConverter.parseInteger(request.getParameter("applicant_personal_id_document_type"), null);

        int newId = applicationsDataProvider.saveApplication(recordId, appNumber, appDate, applicantRecordId, applicantCompanyRecordId, representativeRecordId,
                trainingCourseRecordId, email, officialEmailCommunication, homeCountryId, homeCity, homePostCode, homeAddressDetails, homeIsBg,
                bgCity, bgPostCode, bgAddressDetails, bgAddressOwner, createdByUserId, timeOfCreation, applicationStatusId, companyId,
                differentDiplomaNames, homePhone, bgPhone, reprCountryId,
                reprCity, reprPcode, reprAddressDetails, reprPhone, summary, archiveNumber, personalDataUsage, dataAuthentic, representativeAuthorized, notes,
                submittedDocs, applicantInfo, user.getUserId(), responsibleUser, applicationDocflowStatusId,
                representativeType, typePayment == null ? null : typePayment.shortValue(), deliveryType, declaration,
                courierNameAddress, outgoingNumber, internalNumber, isExpress, applicantType, applicationType, documentReceiveMethodId, applicantPersonalIdDocumentType);
        application = applicationsDataProvider.getApplication(newId);

        //saving document recipient
        boolean hasDocumentRecipient = false;
        if (documentReceiveMethodId != null) {
            DocumentReceiveMethod documentReceiveMethod = nacidDataProvider.getNomenclaturesDataProvider().getDocumentReceiveMethod(documentReceiveMethodId);
            if (documentReceiveMethod.hasDocumentRecipient()) {
                hasDocumentRecipient = true;
                String name = DataConverter.parseString(request.getParameter("recipient_name"), null);
                int countryId = DataConverter.parseInt(request.getParameter("recipient_country"), 0);
                String city = DataConverter.parseString(request.getParameter("recipient_city"), null);
                String district = DataConverter.parseString(request.getParameter("recipient_district"), null);
                String postCode = DataConverter.parseString(request.getParameter("recipient_post_code"), null);
                String address = DataConverter.parseString(request.getParameter("recipient_address"), null);
                String mobilePhone = DataConverter.parseString(request.getParameter("recipient_mobile_phone"), null);
                applicationsDataProvider.saveDocumentRecipient(application.getId(), name, countryId, city, district, postCode, address, mobilePhone);
            }
        }
        if (!hasDocumentRecipient) {
            applicationsDataProvider.deleteDocumentRecipient(application.getId());
        }
        //end of saving document recipient

        if (recordId == 0) { //Ako se vyvejda novo zaqvlenie, i user-a e vyvel mail za komunikaciq, mu se izprashta takyv.
            sendMessageToUser(request, response, application);
        }

        request.setAttribute("applicationStatusMessage", new SystemMessageWebModel("Данните бяха въведени в базата",
                SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        addApplicationToWebModel(application, request, response, FORM_ID_APPLICATION_DATA);
        request.getSession().removeAttribute(WebKeys.TABLE_APPLICATIONS);
        
        request.setAttribute(WebKeys.APPLICATION_ID, newId);

    }

    private void saveTrainingCourse(HttpServletRequest request, HttpServletResponse response) {
        // User user = getLoggedUser(request, response);
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
        TrainingCourseDataProvider trainingCourseDataProvider = nacidDataProvider.getTrainingCourseDataProvider();
        int recordId = DataConverter.parseInt(request.getParameter("id"), 0);
        Application application = applicationsDataProvider.getApplication(recordId);
        if (application == null) {
            throw new UnknownRecordException("Unknown application record with id=" + recordId);
        }
        TrainingCourse trainingCourse = application.getTrainingCourse();
        String diplomaNumber = request.getParameter("diploma_number");
        String diplomaSeries = DataConverter.parseString(request.getParameter("diploma_series"), null);
        String diplomaRegistrationNumber = DataConverter.parseString(request.getParameter("diploma_registration_number"), null);
        Date diplomaDate = DataConverter.parseDate(request.getParameter("diploma_date"));
        Integer baseUniversityId = DataConverter.parseInteger(request.getParameter("base_university_id"), null);
        Integer baseFacultyId = DataConverter.parseInteger(request.getParameter("base_university_faculty_id"), null);
        UniversityIdWithFacultyId baseUniversityWithFaculty = baseUniversityId == null ? null : new UniversityIdWithFacultyId(baseUniversityId, baseFacultyId);

        boolean isJointDegree = false;
//        Set<Integer> jointUniversitiesIds = null;
        /*int jointUniversitiesCount = DataConverter.parseInt(request.getParameter("joint_universities_count"), 0);
        if (jointUniversitiesCount > 0) {
            jointUniversitiesIds = new LinkedHashSet<Integer>();
            for (int i = 0; i < jointUniversitiesCount; i++) {
                int currentUniversityId = DataConverter.parseInt(request.getParameter("joint_university_id" + i), -1);
                if (currentUniversityId != -1) {
                    jointUniversitiesIds.add(currentUniversityId);
                    isJointDegree = true;
                }
            }
            jointUniversitiesIds.remove(baseUniversityId);
        }*/
        //int trainingLocationCountryId = DataConverter.parseInt(request.getParameter("training_country_id"), 0);
        //String trainingLocationCity = request.getParameter("training_location_city");
        
        //int specialityId = DataConverter.parseInt(request.getParameter("trainingSpecialityId"), 0);




        List<TrainingCourseSpecialityImpl> trainingCourseSpecialities = new ArrayList<TrainingCourseSpecialityImpl>();
        int specialitiesCount = DataConverter.parseInt(request.getParameter("trainingSpeciality_specialities_count"), 0);
        for (int i = 1; i <= specialitiesCount; i++) {
            Integer specialityId = DataConverter.parseInteger(request.getParameter("specialitiesListItem_trainingSpeciality_" + i), null);
            if (specialityId != null) {
                Integer originalSpecialityId = DataConverter.parseInteger(request.getParameter("specialitiesListItem_original_trainingSpeciality_" + i), null);
                trainingCourseSpecialities.add(new TrainingCourseSpecialityImpl(trainingCourse.getId(), specialityId, originalSpecialityId));
            }
        }
        Integer singleSpecialityId = DataConverter.parseInteger(request.getParameter("trainingSpecialityId"), null);
        if(singleSpecialityId != null){
            Integer originalSpecialityId = DataConverter.parseInteger(request.getParameter("original_trainingSpecialityId"), null);
            trainingCourseSpecialities.add(new TrainingCourseSpecialityImpl(trainingCourse.getId(), singleSpecialityId, originalSpecialityId));
        }
        String thesisTopic = DataConverter.parseString(request.getParameter("thesis_topic"), null);
        String thesisTopicEn = DataConverter.parseString(request.getParameter("thesis_topic_en"), null);
        Date thesisDefenceDate = DataConverter.parseDate(request.getParameter("thesis_defence_date"));
        Integer thesisBibliography = DataConverter.parseInteger(request.getParameter("thesis_bibliography"), null);
        Integer thesisVolume = DataConverter.parseInteger(request.getParameter("thesis_volume"), null);
        Integer thesisLanguage = DataConverter.parseInteger(request.getParameter("thesis_language"), null);
        String thesisAnnotation = DataConverter.parseString(request.getParameter("thesis_annotation"), null);
        String thesisAnnotationEn = DataConverter.parseString(request.getParameter("thesis_annotation_en"), null);
        Integer profGroup = DataConverter.parseInteger(request.getParameter("prof_group"), null);



        Date trainingStart = DataConverter.parseYear(request.getParameter("training_start"));
        Date trainingEnd = DataConverter.parseYear(request.getParameter("training_end"));
        Double trainingDuration = DataConverter.parseDouble(request.getParameter("training_duration"), null);
        Integer trainingDurationUnitId = DataConverter.parseInteger(request.getParameter("training_duration_unit_id"), null);
        Integer trainingFormId = DataConverter.parseInteger(request.getParameter("training_form"), null);
        
		Integer schoolCountryId = DataConverter.parseInteger(request.getParameter("schoolCountryId"), null);
		String scoolCity = request.getParameter("schoolCity");
		String schoolName = request.getParameter("schoolName");
		Date schoolGraduationDate = DataConverter.parseYear(request.getParameter("schoolGraduationDate"));
		String schoolNotes = request.getParameter("schoolNotes");
		Integer prevDiplUniversityId = DataConverter.parseInteger(request.getParameter("prevDiplUniversityId"), null);
		Integer prevDiplEduLevelId = DataConverter.parseInteger(request.getParameter("prevDiplEduLevelId"), null);
		Date prevDiplGraduationDate = DataConverter.parseYear(request.getParameter("prevDiplGraduationDate"));
		String prevDiplNotes = request.getParameter("prevDiplNotes");
		Integer prevDiplomaSpecialityId = DataConverter.parseInteger(request.getParameter("prevDiplomaSpecialityId"), null);

        String trainingFormOther = null;
        if (trainingFormId == null) {
            trainingFormOther = request.getParameter("training_form_other");
        }
        String[] graduationWays = request.getParameterValues("graduation_way");
        Map<Integer, String> graduationWaysMap = null;
        if (graduationWays != null && graduationWays.length > 0) {
            List<Integer> graduationWayIds = new ArrayList<Integer>();
            for (String s : graduationWays) {
                graduationWayIds.add(DataConverter.parseInteger(s, null));
            }

            if (graduationWayIds.size() > 0) {
                graduationWaysMap = new HashMap<Integer, String>();
                for (Integer i : graduationWayIds) {
                    String value = null;
                    if (i == null) {
                        value = request.getParameter("graduation_way_other");
                    }
                    graduationWaysMap.put(i, value);
                }
            }
        }

        BigDecimal credits = DataConverter.parseBigDecimal(request.getParameter("credits"), null);
        //int educationLevelId = DataConverter.parseInt(request.getParameter("edu_level_id"), 0);
        Integer qualificationId = DataConverter.parseInteger(request.getParameter("trainingQualificationId"), null);
        Integer originalQualificationId = DataConverter.parseInteger(request.getParameter("trainingOriginalQualificationId"), null);
        Integer graduationDocumentTypeId = DataConverter.parseInteger(request.getParameter("graduationDocumentTypeId"), null);
        String[] recognitionPurposes = request.getParameterValues("recognition_purpose");
        Map<Integer, String> recognitionPurposesMap = null;
        if (recognitionPurposes != null && recognitionPurposes.length > 0) {
            List<Integer> recognitionPurposeIds = new ArrayList<Integer>();
            for (String s : recognitionPurposes) {
                recognitionPurposeIds.add(DataConverter.parseInteger(s, null));
            }
            if (recognitionPurposeIds.size() > 0) {
                recognitionPurposesMap = new HashMap<Integer, String>();
                for (Integer i : recognitionPurposeIds) {
                    String value = null;
                    if (i == null) {
                        value = request.getParameter("recognition_purpose_other");
                    }
                    recognitionPurposesMap.put(i, value);
                }
            }
        }
        int trainingLocationsCount = DataConverter.parseInt(request.getParameter("training_locations_count"), 0);
        class TrainingLocations {
        	Integer trainingLocationId;
        	int countryId;
        	String city;
        	
        	public TrainingLocations(Integer trainingLocationId, int countryId, String city) {
        		this.trainingLocationId = trainingLocationId;
        		this.countryId = countryId;
        		this.city = city;
			}
        }
        List<TrainingLocations> trainingLocations = null;
        Set<Integer> trainingLocationIds = new HashSet<Integer>();
        if (trainingLocationsCount > 0 ) {
        	trainingLocations = new ArrayList<TrainingLocations>();
        	for (int i = 0; i < trainingLocationsCount; i++) {
            	Integer trainingLocationId = DataConverter.parseInteger(request.getParameter("training_location_id" + i), null);
        		Integer countryId = DataConverter.parseInteger(request.getParameter("training_country" + i), null);
            	String city = request.getParameter("training_city" + i);
            	if (countryId != null) {
            		trainingLocations.add(new TrainingLocations(trainingLocationId, countryId, city));
            		if (trainingLocationId != null) {
            			trainingLocationIds.add(trainingLocationId);
            		}
            	}
            	
            }	
        }
        
        

        /**
         * writing recognition Purposes to database - it's important to delete
         * old data!!!!
         */
        applicationsDataProvider.deleteApplicationRecognitionPurpose(application.getId());
        if (recognitionPurposesMap != null) {
            for (Entry<Integer, String> entry : recognitionPurposesMap.entrySet()) {
                applicationsDataProvider.addApplicationRecognitionPurposeRecord(application.getId(), entry.getKey(), entry.getValue());
            }
        }
        /**
         * adding trainingForm and graduationWays to database - it's important
         * to delete old graduation ways at first!!!!
         */
        trainingCourseDataProvider.setTrainingCourseTrainingForm(trainingCourse.getId(), trainingFormId, trainingFormOther);
        trainingCourseDataProvider.deleteTrainingCourseGraduationWays(trainingCourse.getId());
        if (graduationWaysMap != null) {
            for (Entry<Integer, String> entry : graduationWaysMap.entrySet()) {
                trainingCourseDataProvider.addTrainingCourseGraduationWayRecord(trainingCourse.getId(), entry.getKey(), entry.getValue());
            }
        }
        Integer diplomaTypeId = DataConverter.parseInteger(request.getParameter("diploma_type"), null);
        DiplomaType diplomaType = diplomaTypeId == null ? null : getNacidDataProvider().getDiplomaTypeDataProvider().getDiplomaType(diplomaTypeId);



        Set<UniversityIdWithFacultyId> jointUniveristyWithFaculties = null;
        if (diplomaType != null) {
            jointUniveristyWithFaculties = diplomaType.getUniversityWithFacultyIds();
        	isJointDegree = diplomaType.isJointDegree();
        	if (jointUniveristyWithFaculties != null) {
                jointUniveristyWithFaculties.remove(baseUniversityWithFaculty);
        	}
        	
        	
        }
        /**
         * ako zaqvlenieto e migrirano, togava za educationLevel-a se vzema tozi koito e zapisan v bazata, a ne tozi ot diplomaType! 
         */
        Integer educationLevelId = diplomaType == null ? null : (diplomaTypeId == DiplomaType.MIGRATED_DIPLOMA_TYPE_ID ? trainingCourse.getEducationLevelId() : diplomaType.getEduLevelId());
        //TODO:Zasega slagam Arrays.asList(specialityId)
        Integer creditHours = DataConverter.parseInteger(request.getParameter("creditHours"), null);
        Integer ectsCredits = DataConverter.parseInteger(request.getParameter("ectsCredits"), null);
        trainingCourseDataProvider.saveTrainingCourse(trainingCourse.getId(), diplomaSeries, diplomaNumber, diplomaRegistrationNumber, diplomaDate, diplomaTypeId, trainingCourse
                .getFName(), trainingCourse.getSName(), trainingCourse.getLName(), isJointDegree, /*trainingLocationCountryId,
                trainingLocationCity,*/ trainingStart, trainingEnd, trainingDuration, trainingDurationUnitId, credits, educationLevelId,
                qualificationId, trainingCourse.isRecognized(), schoolCountryId, scoolCity,
                schoolName, schoolGraduationDate, schoolNotes, 
                prevDiplUniversityId, prevDiplEduLevelId,
                prevDiplGraduationDate, prevDiplNotes, prevDiplomaSpecialityId,
                trainingCourse.getRecognizedEduLevelId(), trainingCourse.getRecognizedQualificationId(), graduationDocumentTypeId, creditHours,
                ectsCredits, trainingCourse.getOwnerId(),
                thesisTopic, thesisTopicEn, profGroup, trainingCourse.getRecognizedProfGroupId(), thesisDefenceDate, thesisBibliography, thesisVolume, thesisLanguage, thesisAnnotation, thesisAnnotationEn, originalQualificationId);
        trainingCourseDataProvider.deleteTrainingCourseSpecialities(trainingCourse.getId());
        trainingCourseDataProvider.saveTrainingCourseSpecialities(trainingCourse.getId(), trainingCourseSpecialities);

        //Saving training locations
        //Ako v novovyvedenite trainingLocations nqma nqkoe, koeto go ima v bazata, to tova v bazata trqbva da se iztrie!!!
        List<? extends TrainingCourseTrainingLocation> dbTrainingLocations = trainingCourse.getTrainingCourseTrainingLocations();
        Map<Integer, TrainingCourseTrainingLocation> dbTrainingLocationsMap = new HashMap<Integer, TrainingCourseTrainingLocation>();
        if (dbTrainingLocations != null) {
        	for (TrainingCourseTrainingLocation tl:dbTrainingLocations) {
        		if (!trainingLocationIds.contains(tl.getId())) {
        			trainingCourseDataProvider.deleteTrainingCourseTrainingLocation(tl.getId());
        		}
        		dbTrainingLocationsMap.put(tl.getId(), tl);
        	}
        }
        if (trainingLocations != null) {
        	for (TrainingLocations tl:trainingLocations) {
        		TrainingCourseTrainingLocation dbtl = tl.trainingLocationId == null ? null : dbTrainingLocationsMap.get(tl.trainingLocationId);
        		//Ako zapisa v bazata i novovyvedeniq sa ekvivalentni togava ne se pravi nishto,
    			//inache se iztriva trainingInstitution-a ot bazata!!!!
        		if (dbtl != null &&
        				(dbtl.getTrainingLocationCountryId() != null && tl.countryId == dbtl.getTrainingLocationCountryId())
    					&&
    					(tl.city != null && (tl.city.equals(dbtl.getTrainingLocationCity())))	
        		) {
        		} else {
					trainingCourseDataProvider.saveTrainingCourseTrainingLocation(tl.trainingLocationId == null ? 0 : tl.trainingLocationId, trainingCourse.getId(), tl.countryId, tl.city, null);
				}
        	}
        }
        //End of saving training locations
        
        
        /*if(specialityId != 0) {
            trainingCourseDataProvider.saveRecognizedSpecialities(
                    trainingCourse.getId(), Collections.singletonList(specialityId));
        }*/

        if (baseUniversityWithFaculty != null) {
        	trainingCourseDataProvider.updateTrainingCourseUniversities(trainingCourse.getId(), baseUniversityWithFaculty, jointUniveristyWithFaculties == null || jointUniveristyWithFaculties.size() == 0 ? null
                    : new ArrayList<>(jointUniveristyWithFaculties));
        }
        
        
        
        
        
        //change status of application if education tab is correctly filled
        application = applicationsDataProvider.getApplication(recordId);
        trainingCourse = application.getTrainingCourse();
        UniversityWithFaculty university = trainingCourse.getBaseUniversityWithFaculty();
        diplomaType = trainingCourse.getDiplomaType();
        dbTrainingLocations = trainingCourse.getTrainingCourseTrainingLocations();
        boolean shouldNotHaveTrainingStartEnd = application.getApplicationType() == ApplicationType.DOCTORATE_APPLICATION_TYPE && educationLevelId != null && educationLevelId == EducationLevel.EDUCATION_LEVEL_DOCTOR_OF_SCIENCE;



        if (university != null && diplomaType != null 
             //   && !isEmpty(trainingCourse.getDiplomaNumber())
            //    && trainingCourse.getDiplomaDate() != null
                && (dbTrainingLocations != null && !isEmpty(dbTrainingLocations.get(0).getTrainingLocationCity()))
                && (shouldNotHaveTrainingStartEnd || (trainingCourse.getTrainingStart() != null && trainingCourse.getTrainingEnd() != null)) //za doctorate + doctorOfScience ne se iska trainingStart/trainingEnd!!!
              //  && trainingCourse.getTrainingCourseGraduationWays() != null
              // && !trainingCourse.getTrainingCourseGraduationWays().isEmpty()
              //  && trainingCourse.getSchoolCountryId() != null
             //   && !isEmpty(trainingCourse.getSchoolCity())
             //   && !isEmpty(trainingCourse.getSchoolName())
                && ApplicationStatus.RUDI_APPLCATION_STATUSES_FOR_CHANGE_TO_FOR_EXAMINATION.contains(application.getApplicationStatusId())) {
            applicationsDataProvider.updateApplicationStatus(application.getId(), 
                    ApplicationStatus.APPLICATION_FOR_EXAMINATION_STATUS_CODE, application.getApplicationDocflowStatusId(), null, null, null, getLoggedUser(request, response).getUserId());
            application = applicationsDataProvider.getApplication(recordId);   
        }
        
        request.setAttribute("trainingCourseStatusMessage", new SystemMessageWebModel("Данните бяха въведени в базата",
                SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        addApplicationToWebModel(application, request, response, FORM_ID_TRAINING_DATA);

    }
    
    private static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static void addApplicationToWebModel(Application application, HttpServletRequest request, HttpServletResponse response, Integer activeFormId) {
        request.setAttribute(WebKeys.NEXT_SCREEN, "application_edit");
        request.setAttribute(WebKeys.APPLICATION_ID, application.getId());
        NacidDataProvider nacidDataProvider = getNacidDataProvider(request.getSession());
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        TrainingCourse tc = application.getTrainingCourse();

        List<Country> countries = nomenclaturesDataProvider.getCountries(null, null);
        ComissionMemberDataProvider comissionMemberDataProvider = nacidDataProvider.getComissionMemberDataProvider();
        List<ComissionMember> comissionMembers = comissionMemberDataProvider.getComissionMembers(false, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_COMISSION_MEMBER, ComissionMemberOrderCriteria.ORDER_COLUMN_FULL_NAME, true));

        ApplicationWebModel applicationWebModel = new ApplicationWebModel(application, activeFormId, comissionMembers, nacidDataProvider);
        generateCompaniesCombo(request, nacidDataProvider, application);

        List<? extends FlatNomenclature> eduLevels = nomenclaturesDataProvider.getEducationLevels(application.getApplicationType(), null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true));


        ComboBoxUtils.generateNomenclaturesComboBox(null, eduLevels, false, request, "eduLevelsCombo", true);
        generateExpertPositionsCombo(request, nacidDataProvider);
        ComboBoxUtils.generateNomenclaturesComboBox(application.getHomeCountry().getId(), countries, true, request, "homeCountry", false);
        Person applicant = application.getApplicant();
        ComboBoxUtils.generateNomenclaturesComboBox(applicant == null ? null : applicant.getBirthCountryId(), countries, true, request, "applicantBirthCountry", true);
        OrderCriteria nameOrderCriteria = OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true);

        addFlatNomenclaturesToWebModel(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, nomenclaturesDataProvider, request,
                WebKeys.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true));



        ComboBoxUtils.generateNomenclaturesComboBox(applicant == null ? null : applicant.getCitizenshipId(), countries, true, request, "applicantCitizenship", true);

        Company applicantCompany = application.getApplicantCompany();
        ComboBoxUtils.generateNomenclaturesComboBox(applicantCompany == null ? null : applicantCompany.getCountryId() , countries, true, request, "applicantCompanyCountry", true);


        Person owner = application.getTrainingCourse().getOwner();
        ComboBoxUtils.generateNomenclaturesComboBox(owner == null ? null : owner.getBirthCountryId(), countries, true, request, "ownerBirthCountry", true);
        ComboBoxUtils.generateNomenclaturesComboBox(owner == null ? null : owner.getCitizenshipId(), countries, true, request, "ownerCitizenship", true);


        ComboBoxUtils.generateNomenclaturesComboBox(
                application.getReprCountryId() == null ? NomenclaturesDataProvider.COUNTRY_ID_BULGARIA : application.getReprCountryId(), 
                countries, true, request, "reprCountryCombo", false);
        
        Integer schoolCountryId = null;
        Integer prevDiplEduLevelId = null;
        if (application.getTrainingCourse() != null) {
            schoolCountryId = application.getTrainingCourse().getSchoolCountryId();
            prevDiplEduLevelId = application.getTrainingCourse().getPrevDiplomaEduLevelId();
        }
        ComboBoxUtils.generateNomenclaturesComboBox(
                schoolCountryId,
                countries, true, request, "schoolCountryCombo", true);
        

        List<? extends FlatNomenclature> prevdiplomaEduLevels = nomenclaturesDataProvider.getEducationLevels(RUDI_APPLICATION_TYPE, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true));
        ComboBoxUtils.generateNomenclaturesComboBox(
                prevDiplEduLevelId,
                prevdiplomaEduLevels, true, request, "prevDiplEduLevelCombo", true);
        OriginalEducationLevelAjaxHandler.generateOriginalEducationLevelsByCountryComboBox(application.getApplicationType(), null, 0, 0, nomenclaturesDataProvider, request);

        List<? extends User> users = nacidDataProvider.getUsersDataProvider().getUsers(0, 0, 1, NacidDataProvider.APP_NACID_ID);
        ComboBoxUtils.generateUsersComboBox(application.getResponsibleUserId(), users, request, "responsibleUsers", true, false);
        ComboBoxUtils.generateUsersComboBox(application.getCreatedByUserId(), users, request, "userCreated", true, false);

        List<ProfessionGroup> profGroups = nomenclaturesDataProvider.getProfessionGroups(0, null, null);
        List<FlatNomenclature> eduAreas = nomenclaturesDataProvider.getFlatNomenclatures(FLAT_NOMENCLATURE_EDUCATION_AREA, null, null);
        ComboBoxUtils.generateProfessionGroupComboBox(tc.getProfGroupId(), profGroups, true, request, "profGroupCombo", true);
        ComboBoxUtils.generateNomenclaturesComboBox(tc.getProfGroupId() == null ? null : tc.getProfGroup().getEducationAreaId(), eduAreas, false, request, "profGroupEduAreaCombo", true);
        ComboBoxUtils.generateProfessionGroupComboBox(tc.getRecognizedProfGroupId(), profGroups, true, request, "recognizedProfGroupCombo",  true);
        ComboBoxUtils.generateNomenclaturesComboBox(tc.getRecognizedProfGroupId() == null ? null : tc.getRecognizedProfGroup().getEducationAreaId(), eduAreas, false, request, "recognizedProfGroupEduAreaCombo", true);
        ComboBoxUtils.generateDocumentReceiveMethodRadioButton(application.getDocumentReceiveMethodId(), nomenclaturesDataProvider.getDocumentReceiveMethods(null, null), true, request, "documentReceiveMethod");
        ComboBoxUtils.generateNomenclaturesComboBox(application.getApplicantPersonalIdDocumentTypeId(), NomenclaturesDataProvider.FLAT_NOMENCLATURE_PERSONAL_ID_DOCUMENT_TYPE, nomenclaturesDataProvider, true, request, "applicantPersonalIdDocumentType", null, true);
        ComboBoxUtils.generateNomenclaturesComboBox(application.getDocumentRecipient() == null ? null : application.getDocumentRecipient().getCountryId(), countries, true, request, "recipientCountryCombo", true);
        request.setAttribute(WebKeys.APPLICATION_WEB_MODEL, applicationWebModel);


        // FORM 2
        TrainingCourse trainingCourse = application.getTrainingCourse();
        // ComboBoxUtils.generateNomenclaturesComboBox(trainingCourse.
        // getBaseUniversity() == null ? null :
        // trainingCourse.getBaseUniversity().getCountryId(), countries, true,
        // request, "jointUniversityCountry");
        
        UniversityWithFaculty baseUniversityWithFaculty = trainingCourse.getBaseUniversityWithFaculty();
        University baseUniversity = baseUniversityWithFaculty == null ? null : baseUniversityWithFaculty.getUniversity();
        DiplomaTypeAjaxHandler.generateDiplomaTypeCombo(application, request, nacidDataProvider, trainingCourse.getDiplomaTypeId(), baseUniversity == null ? new ArrayList<Integer>() : Arrays.asList(baseUniversity.getId()), APPLICATION_TYPE_DIPLOMA_TYPE.get(application.getApplicationType()));
        
        /**
         * za da ne generiram po edin comboboxWebModel za vsqka edna ot
         * dyrjavite za "chujdestranno vishe u4ilishte" v tab-a obu4enie, slagam
         * samo 1 webmodel i posle v tag-a za generirane na universiteta v
         * pyrviq red shte trqbva da sloja izvikvane na
         * setActiveItemId(UniversityWebModel.getCountryId());
         */
        
        ComboBoxUtils.generateNomenclaturesComboBox(null, countries, true, request, WebKeys.UNIVERSITY_COUNTRY, true);
        ComboBoxUtils.generateNomenclaturesComboBox(null, countries, true, request, "newUniversityCountry", true);
        ComboBoxUtils.generateNomenclaturesComboBox(null, countries, true, request, "newTrainingInstitutionCountry", true);

        ComboBoxUtils.generateNomenclaturesComboBox( null, countries, true, request, "trainingCountry", true);
        /*ComboBoxUtils.generateNomenclaturesComboBox(trainingCourse.getSpecialityId(), NomenclaturesDataProvider.NOMENCLATURE_SPECIALITY,
                nomenclaturesDataProvider, true, request, "trainingSpeciality", OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), true);*/

        ComboBoxUtils.generateNomenclaturesComboBox(trainingCourse.getDurationUnitId(), NomenclaturesDataProvider.FLAT_NOMENCLATURE_DURATION_UNIT,
                nomenclaturesDataProvider, true, request, "trainingDurationUnit", OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true), false);



        ComboBoxUtils.generateNomenclaturesComboBox(trainingCourse.getEducationLevelId(), eduLevels, true, request, "trainingEducationLevel", false);

        ComboBoxUtils.generateNomenclaturesComboBox(trainingCourse.getGraduationDocumentTypeId(), NomenclaturesDataProvider.FLAT_NOMENCLATURE_GRADUATION_DOCUMENT_TYPE, nomenclaturesDataProvider, false, request, "graduationDocumentTypeCombo", nameOrderCriteria, true);
        /*ComboBoxUtils.generateNomenclaturesComboBox(trainingCourse.getQualificationId(), NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION,
                nomenclaturesDataProvider, true, request, "trainingQualification", OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), true);*/
        //RayaWritten--------------------------------------------------------------------
        List<ProfessionGroup> professionGroups = nomenclaturesDataProvider.getProfessionGroups(0, null, null);
        ComboBoxUtils.generateNomenclaturesComboBox(null, professionGroups, true, request, "professionGroupCombo", true);
        //-------------------------------------------------------------------------------
        addStatusCombo(application.getApplicationStatusId(), request, getNacidDataProvider(request.getSession()));
        addDocflowStatusCombo(application.getApplicationDocflowStatusId(), request, getNacidDataProvider(request.getSession()));

        
        addFlatNomenclaturesToWebModel(nomenclaturesDataProvider.getGraduationWays(application.getApplicationType(), new Date(), OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true)),
                request, WebKeys.FLAT_NOMENCLATURE_GRADUATION_WAY);
        addFlatNomenclaturesToWebModel(NomenclaturesDataProvider.FLAT_NOMENCLATURE_TRAINING_FORM, nomenclaturesDataProvider, request,
                WebKeys.FLAT_NOMENCLATURE_TRAINING_FORM, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true));
        addFlatNomenclaturesToWebModel(NomenclaturesDataProvider.FLAT_NOMENCLATURE_RECOGNITION_PURPOSE, nomenclaturesDataProvider, request,
                WebKeys.FLAT_NOMENCLATURE_RECOGNITION_PURPOSE, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true));
        new ApplicationAttachmentHandler(request.getServletContext()).handleList(request, response);

        ComboBoxUtils.generateNomenclaturesComboBox(tc.getThesisLanguageId(), nomenclaturesDataProvider.getLanguages(null, null), true, request, "thesisLanguageCombo", true);
        
        
        //FORM4
		
        UniversityValidityHandler.generateUniversityValiditiesForApplication(application, nacidDataProvider, request, response);
		
        if (baseUniversity != null) {
			DiplomaExamination diplomaExamination = trainingCourse.getDiplomaExamination();
			generateInstitutionsCombo(request, nacidDataProvider, application, diplomaExamination == null ? null : diplomaExamination
					.getCompetentInstitutionId());
		}
		
		List<AppStatusHistoryWebModel> appStatusWMList = new ArrayList<AppStatusHistoryWebModel>();
		ApplicationsDataProvider appDP = nacidDataProvider.getApplicationsDataProvider();
		List<AppStatusHistory> appStatusHistoryList = appDP.getAppStatusHistory(application.getId());

		for(AppStatusHistory ash : appStatusHistoryList) {
		    appStatusWMList.add(new AppStatusHistoryWebModel(ash));
		}
		request.setAttribute(WebKeys.STATUS_HISTORY_LIST_WEB_MODEL, appStatusWMList);

        List<AppDocflowStatusHistory> appDocflowStatusHistoryList = appDP.getAppDocflowStatusHistory(application.getId());
        if (appDocflowStatusHistoryList != null) {
            List<AppDocflowStatusHistoryWebModel> wm = new ArrayList<AppDocflowStatusHistoryWebModel>();
            for (AppDocflowStatusHistory appDocflowStatusHistory : appDocflowStatusHistoryList) {
                wm.add(new AppDocflowStatusHistoryWebModel(appDocflowStatusHistory));
            }
            request.setAttribute(WebKeys.DOCFLOW_STATUS_HISTORY_LIST_WEB_MODEL, wm);
        }


		Integer legalReasonId = null;
		if (appStatusWMList != null && appStatusWMList.size() > 0) {
			legalReasonId =  appStatusWMList.get(0).getLegalReasonId();	
		}
		List<LegalReason> legalReasons = nomenclaturesDataProvider.getLegalReasons(application.getApplicationType(), null, null, application.getApplicationStatusId());
		if (legalReasons != null) {
			ComboBoxUtils.generateNomenclaturesComboBox(legalReasonId, legalReasons, true, request, "legalReason", true);
		}

		//opredelq poslednoto zasedanie, razglejdalo zaqvlenieto
		List<CommissionCalendar> commissionCalendars = application.getCommissionCalendars();
		if (commissionCalendars != null) {
			for (CommissionCalendar c:commissionCalendars) {
				if (c.getSessionStatusId() == SessionStatus.SESSION_STATUS_PROVEDENO) {
					request.setAttribute("lastCommissionCalendar", new CommissionCalendarWebModel(c));
					break;
				}
			}
		}
        ComboBoxUtils.generateNomenclaturesComboBox(tc.getRecognizedEduLevelId(), eduLevels, true, request, "recognizedEduLevel", true);
		
		request.setAttribute("universityExaminationByPlace", new UniversityExamByPlaceWebModel(applicationWebModel, trainingCourse, nacidDataProvider));
		request.setAttribute("uniInstitution", generateTrainingInstCombo(null, trainingCourse.getUniversityIds(), nacidDataProvider));
		
		/*generateTrainingInstCombo(null,
		        baseUniversity != null ? baseUniversity.getId() : 0,
		        request, getNacidDataProvider());*/
	
		new DiplExamAttachmentHandler(request.getServletContext()).handleList(request, response);
        //generateApplicationExpertsCombo(request, nacidDataProvider);
        
        new ApplicationEventsHandler(request.getServletContext()).handleList(request, response);
        
        
        
        //FORM 5
        ApplicationsExpertHandler.generateApplicationExpertsCombo(null, false, request, getNacidDataProvider(request.getSession()));
        new ExpertStatementAttachmentHandler(request.getServletContext()).handleList(request, response);
        request.setAttribute("application", application);
        new ApplicationFinalizationAttachmentHandler(request.getServletContext()).initFinalizationTab(request, response);
        new RasServiceHandler(request.getServletContext()).handleEdit(request, response);

        
        //FORM 0
        /**
         * TODO:tova ne e naj-inteligentnoto reshenie - zashtoto v momenta ApplicationsDataProvider.isElectronicallyApplied() vzema ot bazata ExtApplication obekt i 4rez nego proverqva
         * dali zaqvlenieto e podadeno elektronno i 2 reda po-nadolu za vtori pyt vzema sy6tiq extApplication 4rez ExtApplicationsDataProvider.getApplicationByInternalApplicationId....
         * no ne e qsno dali "elektronno podadeno" nqma da se formulira po drug na4in i zatova go ostavqm taka!
         */
        boolean electronicallyApplied = appDP.isElectronicallyApplied(application.getId()); 
        applicationWebModel.setElectronicallyApplied(electronicallyApplied);
        Integer extEduLevelId = null;
        if (electronicallyApplied) {
        	ExtApplicationsDataProvider extApplicationsDataProvider = nacidDataProvider.getExtApplicationsDataProvider();
        	ExtApplication extApplication = extApplicationsDataProvider.getApplicationByInternalApplicationId(application.getId());
        	ApplicantReportHandler.generateExternalApplicantReport(nacidDataProvider, extApplication, request, null);
            extEduLevelId = extApplication.getTrainingCourse().getEducationLevelId();
        }

        Integer diplomaYear = null;
        if (trainingCourse.getDiplomaDate() != null) {
            Calendar cal = new GregorianCalendar();
            cal.setTime(trainingCourse.getDiplomaDate());
            diplomaYear = cal.get(Calendar.YEAR);

        }
        List<Integer> universityCountryIds = new ArrayList<>();
        List<String> universityNames = new ArrayList<>();
        List<? extends UniversityWithFaculty> unis = trainingCourse.getUniversityWithFaculties();
        if (unis != null) {
            unis.forEach( u -> {
                        universityCountryIds.add(u.getUniversity().getCountryId());
                        universityNames.add(u.getUniversity().getBgName());
                    }
            );
        }


        List<String> specialityNames = trainingCourse.getSpecialities() == null ? null : trainingCourse.getSpecialities().stream().map(s -> nomenclaturesDataProvider.getSpeciality(s.getSpecialityId()).getName()).collect(Collectors.toList());
        Integer eduLevelId = trainingCourse.getEducationLevelId() == null ? extEduLevelId : trainingCourse.getEducationLevelId();//ako vytreshnoto prilojenie nqma eduLevel, to se vzema eduLevel-a na vyn6noto.
        initSimilarDiplomas(request, nacidDataProvider, owner.getCivilId(), owner.getFName(), owner.getSName(), owner.getLName(), universityCountryIds, null, eduLevelId, diplomaYear, specialityNames, universityNames, application.getId());
        
    }

    public void handlePrint(HttpServletRequest request, HttpServletResponse response) {
    	int applicationId = DataConverter.parseInt(request.getParameter("id"), 0);
    	if (applicationId == 0) {
    		throw new UnknownRecordException("unknown application record with id=" + request.getParameter("id"));
    	}
    	ApplicationsDataProvider applicationsDataProvider = getNacidDataProvider().getApplicationsDataProvider();
    	ApplicationDetailsForReport application = applicationsDataProvider.getApplicationDetailsForReport(applicationId);
    	List<ApplicationDetailsForReport> reports = new ArrayList<ApplicationDetailsForReport>();
    	reports.add(application);
    	
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
    	JasperReportGenerator generator = new JasperReportGenerator(JasperReportNames.JASPER_REPORT_REGISTRATION, ExportType.HTML, os);
    	try {
			generator.export(reports);
		} catch (Exception e) {
			throw Utils.logException(e);
		} 
    	try {
    		response.setContentType("text/html; charset=utf-8");
    		PrintWriter writer = response.getWriter();
			writer.write( os.toString("UTF-8"));
			writer.write("<script type=\"text/javascript\">window.print();</script>");
		} catch (IOException e) {
			throw Utils.logException(e);
		}
	}

	@Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {

	    //Date date = new Date();
		HttpSession session = request.getSession();
        //Ako e izbrana otmetkata samo aktivnite, parametrite se zapisvat v edin session atribut, ako ne e  izbrana, togava se zapisvat v drug...
        boolean onlyActive = isOnlyActive(request);

        Integer applicationType = DataConverter.parseInteger(request.getParameter("application_type"), null);
        Table table = (Table) session.getAttribute(WebKeys.TABLE_APPLICATIONS + applicationType + (onlyActive ? "1" : "2"));
        //System.out.println("table = " + table + " onlyActive?" + onlyActive);
        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            if (applicationType == RUDI_APPLICATION_TYPE) {
                generateRudiTableHeader(table);
            } else if (applicationType == DOCTORATE_APPLICATION_TYPE) {
                generateDoctorateTableHeader(table);
            } else if (applicationType == STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE) {
                generateStatuteAuthenticityRecommendationTableHeader(table);
            } else {
                throw new RuntimeException("Unknown application type");
            }

            session.setAttribute(WebKeys.TABLE_APPLICATIONS + applicationType + (onlyActive ? "1" : "2"), table);
            resetTableData(request);

        }

        // TableState settings
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_APPLICATIONS + request.getParameter("application_type") + WebKeys.TABLE_STATE);
        
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            TableStateAndFiltersUtils.addEqualsFilterToTableState(FILTER_NAME_APP_NUMBER, COLUMN_NAME_APPLICATION_NUMBER, request, table, tableState);
            TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_STATUS, COLUMN_NAME_APP_STATUS, request, table, tableState);           
            TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_DOCFLOW_STATUS, COLUMN_NAME_APP_DOCFLOW_STATUS, request, table, tableState);
            session.setAttribute(WebKeys.TABLE_APPLICATIONS + request.getParameter("application_type") + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък със заявления");

        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        String apnType = request.getParameter("application_type");
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
        webmodel.hideUnhideColumn(COLUMN_NAME_APP_STATUS_ID, true);
        webmodel.hideUnhideColumn(COLUMN_NAME_APP_DOCFLOW_STATUS_ID, true);
        webmodel.addRequestParam(TableWebModel.OPERATION_NAME_NEW, "application_type", apnType);
        webmodel.addFormAdditionalRequestParam("application_type", apnType);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");
        if (DataConverter.parseInt(request.getParameter("application_type"), 0) == ApplicationType.STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE) {
            webmodel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
        }
        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_APPLICATIONS + request.getParameter("application_type") + WebKeys.FILTER_WEB_MODEL);
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            filtersWebModel.addFiler(generateApplicationNumberFilterWebModel(request));
            filtersWebModel.addFiler(generateStatusFilterComboBox(FILTER_NAME_STATUS, getNacidDataProvider().getNomenclaturesDataProvider(), request));
            filtersWebModel.addFiler(generateDocflowStatusFilterComboBox(FILTER_NAME_DOCFLOW_STATUS, getNacidDataProvider().getNomenclaturesDataProvider(), request));
            filtersWebModel.addFiler(generateOnlyActiveFilterWebModel(request));
            session.setAttribute(WebKeys.TABLE_APPLICATIONS + request.getParameter("application_type") + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
        //System.out.println("Applications list Time cost:" + (new Date().getTime() - date.getTime()));
    }

    private void generateDoctorateTableHeader(Table table) {
        table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
        table.addColumnHeader(COLUMN_NAME_APPLICATION_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
        table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_UNIVERSITY, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);

        table.addColumnHeader(COLUMN_NAME_EDU_LEVEL, CellValueDef.CELL_VALUE_TYPE_STRING);


        //table.addColumnHeader(COLUMN_NAME_EMAIL, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_APP_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_APP_STATUS_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
        table.addColumnHeader(COLUMN_NAME_APP_DOCFLOW_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_APP_DOCFLOW_STATUS_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
        table.addColumnHeader(COLUMN_NAME_RECOGNIZED_PROF_GROUP, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_ESIGNED, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);

    }
    private void generateStatuteAuthenticityRecommendationTableHeader(Table table) {
        table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
        table.addColumnHeader(COLUMN_NAME_APPLICATION_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
        table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_UNIVERSITY, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_EDU_LEVEL2, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_SPECIALITY, CellValueDef.CELL_VALUE_TYPE_STRING);

        table.addColumnHeader(COLUMN_NAME_APP_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_APP_STATUS_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
        table.addColumnHeader(COLUMN_NAME_APP_DOCFLOW_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_APP_DOCFLOW_STATUS_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
        table.addColumnHeader(COLUMN_NAME_ESIGNED, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);

        table.addColumnHeader(COLUMN_NAME_APPLICATION_TYPE, CellValueDef.CELL_VALUE_TYPE_STRING);

    }
    private void generateRudiTableHeader(Table table) {
        table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
        table.addColumnHeader(COLUMN_NAME_APPLICATION_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
        table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_UNIVERSITY, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_EDU_LEVEL2, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_SPECIALITY, CellValueDef.CELL_VALUE_TYPE_STRING);

        table.addColumnHeader(COLUMN_NAME_APP_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_APP_STATUS_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
        table.addColumnHeader(COLUMN_NAME_APP_DOCFLOW_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_APP_DOCFLOW_STATUS_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
        table.addColumnHeader(COLUMN_NAME_RECOGNIZED_QUALIFICATION, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_ESIGNED, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);
    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_APPLICATIONS + request.getParameter("application_type") + (isOnlyActive(request) ? "1" : "2"));
        if (table == null) {
            return;
        }

        Integer applicationType = DataConverter.parseInteger(request.getParameter("application_type"), null);

        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        //NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
        //ExtApplicationsDataProvider extApplicationsDataProvider = nacidDataProvider.getExtApplicationsDataProvider();
        boolean onlyActive = isOnlyActive(request);
        //Ako se iskat samo aktivnite, togava se zarejdat vsi4ki bez izbroenite v ApplicationStatus.APPLICATIONS_ONLY_ACTIVE_STATUSES statusi, inache se vry6tat vsi4ki zapisi v bazata
        List<? extends ApplicationForList> apps = applicationsDataProvider.getApplicationsForList(applicationType, null, onlyActive ? true : false);
        //System.out.println("Resetting table data....onlyActive ? " + onlyActive);
        
        if (apps != null) {
            for (ApplicationForList app : apps) {
                try {

                    if (applicationType == ApplicationType.STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE) {
                        List<Integer> entryNumSeries = app.getEntryNumSeries();
                        String appTypes = generateApplicationTypesListColumn(request, ApplicationType.STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE, entryNumSeries);
                        table.addRow(app.getId(), app.getAppNum(), app.getAppDate(), app.getAptName(), app.getUniName(),
                                app.getUniCountryName(), app.getEduLevelName(), app.getSpecialityName(), /*app.getEmail(),*/
                                app.getApnStatusName(), app.getApnStatusId(), app.getDocflowStatusName(), app.getDocflowStatusId(), app.isExternalApplicationEsigned(), appTypes);
                    } else if (applicationType == RUDI_APPLICATION_TYPE){
                        table.addRow(app.getId(), app.getAppNum(), app.getAppDate(), app.getAptName(), app.getUniName(),
                                app.getUniCountryName(), app.getEduLevelName(), app.getSpecialityName(), /*app.getEmail(),*/
                                app.getApnStatusName(), app.getApnStatusId(), app.getDocflowStatusName(), app.getDocflowStatusId(), app.getRecognizedQualificationName(), app.isExternalApplicationEsigned());
                    } else if (applicationType == DOCTORATE_APPLICATION_TYPE) {
                        table.addRow(app.getId(), app.getAppNum(), app.getAppDate(), app.getAptName(), app.getUniName(),
                                app.getUniCountryName(), app.getEduLevelName(), /*app.getEmail(),*/
                                app.getApnStatusName(), app.getApnStatusId(), app.getDocflowStatusName(), app.getDocflowStatusId(), app.getRecognizedProfGroupName(), app.isExternalApplicationEsigned());
                    }

                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }

   public static String generateApplicationTypesListColumn(HttpServletRequest request, int applicationType, List<Integer> entryNumSeries) {
       Set<Integer> entryNumSeriesSet = new HashSet<>(entryNumSeries);
       NacidDataProvider nacidDataProvider = getNacidDataProvider(request.getSession());
       NumgeneratorDataProvider ndp = nacidDataProvider.getNumgeneratorDataProvider();
       String appTypes = "";
       for (int en : NumgeneratorDataProvider.APPLICATION_TYPE_TO_ENTRYNUM_SERIES.get(applicationType)) {
           appTypes += "<input type=\"checkbox\" " + (entryNumSeriesSet.contains(en) ? "checked=\"checked\"" : "" )+ "disabled=\"disabled\" title=\"" + ndp.getEntryNumSeriesNameById(en) + "\">";
       }
       return appTypes;
   }

    public static void addFlatNomenclaturesToWebModel(List<? extends FlatNomenclature> nomenclatures, HttpServletRequest request, String attributeName) {
        List<FlatNomenclatureWebModel> wm = new ArrayList<FlatNomenclatureWebModel>();
        if (nomenclatures != null) {
            for (FlatNomenclature fn : nomenclatures) {
                wm.add(new FlatNomenclatureWebModel(fn, null, null));
            }
            request.setAttribute(attributeName, wm);
        }
    }
    public static void addFlatNomenclaturesToWebModel(int nomenclatureType, NomenclaturesDataProvider nomenclaturesDataProvider,
                                                       HttpServletRequest request, String attributeName, OrderCriteria orderCriteria) {
        List<FlatNomenclature> flatNomenclatures = nomenclaturesDataProvider.getFlatNomenclatures(nomenclatureType, new Date(), orderCriteria);
        addFlatNomenclaturesToWebModel(flatNomenclatures, request, attributeName);

    }


    private void setOperationTypeInWebModel(HttpServletRequest request, String operation) {
        ApplicationWebModel wm = (ApplicationWebModel) request.getAttribute(WebKeys.APPLICATION_WEB_MODEL);
        if (wm != null) {
        	wm.setOperationType(operation);	
        }
        
    }
    private static void generateInstitutionsCombo(HttpServletRequest request, NacidDataProvider nacidDataProvider, Application application, Integer active) {
        ComboBoxWebModel combobox = new ComboBoxWebModel(active == null ? null : active + "", true);

        CompetentInstitutionDataProvider competentInstitutionDataProvider = nacidDataProvider.getCompetentInstitutionDataProvider();
        List<? extends UniversityWithFaculty> universities = application.getTrainingCourse().getUniversityWithFaculties();
        if (universities == null) {
            throw new RuntimeException("No university is set....");
        }
        Set<Integer> uniIds = new HashSet<Integer>();
        for (UniversityWithFaculty u : universities) {
        	uniIds.add(u.getUniversity().getCountryId());
        }
        List<CompetentInstitution> list = competentInstitutionDataProvider.getCompetentInstitutions(uniIds, true);
        if (list != null) {
            for (CompetentInstitution c : list) {
                Country country = c.getCountry();
            	combobox.addItem("" + c.getId(), c.getName() + (country == null ? "" : " / " + country.getName()));
            }
        }
        request.setAttribute(WebKeys.INSTITUTIONS_COMBO, combobox);
    }
    
    public static ComboBoxWebModel generateTrainingInstCombo(String selected, List<Integer> universityIds, NacidDataProvider nacidDataProvider) {
        ComboBoxWebModel combobox = new ComboBoxWebModel(selected, true);
        TrainingInstitutionDataProvider tiDP = nacidDataProvider.getTrainingInstitutionDataProvider();
        
        List<TrainingInstitution> tis = tiDP.selectTrainingInstitutionsByUniversities(universityIds);
        if(tis != null) {
            for(TrainingInstitution ti : tis) {
                String name = ti.getName();
                
                Country c = ti.getCountry();
                if (c != null) {
                	name += " / " + c.getName();
                }
                if(!ti.isActive()) {
                    name += " (inactive)";
                }
                
                combobox.addItem("" + ti.getId(), name);
            }
        }
        return combobox;
    }
    
    public static void generateCompaniesCombo(HttpServletRequest request, NacidDataProvider nacidDataProvider, Application application) {
        
        String selectedKey = "-";
        if(application != null &&
                application.getCompanyId() != null) {
            selectedKey = application.getCompanyId().toString();
        }
        ComboBoxWebModel combobox = new ComboBoxWebModel(selectedKey, true);

        
        List<Company> list = nacidDataProvider.getCompanyDataProvider().getCompanies(false);
        if (list != null) {
            for (Company c : list) {
                String inactive = "";
                if(c.getDateTo() != null && c.getDateTo().before(new Date())) {
                    inactive = " (inactive)";
                }
                combobox.addItem("" + c.getId(), c.getName() + inactive);
            }
        }
        request.setAttribute("companiesComboBox", combobox);
    }

    private static void generateExpertPositionsCombo(HttpServletRequest request, NacidDataProvider nacidDataProvider) {
        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_EXPERT_POSITION, nacidDataProvider.getNomenclaturesDataProvider(), false, request, "expertPositionsCombo", null, true);
    }



    
    public static ComboBoxFilterWebModel generateStatusFilterComboBox(String filterName,
            NomenclaturesDataProvider nomenclaturesDataProvider, HttpServletRequest request) {
        String activeStatusName = request.getParameter(filterName);
    	ComboBoxWebModel combobox = new ComboBoxWebModel(activeStatusName, true);

        List<ApplicationStatus> statuses = nomenclaturesDataProvider.getApplicationStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), false);
        if (statuses != null) {
            for (FlatNomenclature s : statuses) {
                combobox.addItem(s.getName() + "", s.getName() + (s.isActive() ? "" : " (inactive)"));
            }
        }
        ComboBoxFilterWebModel res = new ComboBoxFilterWebModel(combobox, filterName, COLUMN_NAME_APP_STATUS);
        res.setElementClass("brd w200");
        res.setLabelOnTop(true);
        return res;
    }

    public static ComboBoxFilterWebModel generateDocflowStatusFilterComboBox(String filterName,NomenclaturesDataProvider nomenclaturesDataProvider, HttpServletRequest request) {
        String activeStatusName = request.getParameter(filterName);
        ComboBoxWebModel combobox = new ComboBoxWebModel(activeStatusName, true);

        List<ApplicationDocflowStatus> statuses = nomenclaturesDataProvider.getApplicationDocflowStatuses(NumgeneratorDataProvider.NACID_SERIES_ID, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true));
        if (statuses != null) {
            for (FlatNomenclature s : statuses) {
                combobox.addItem(s.getName() + "", s.getName() + (s.isActive() ? "" : " (inactive)"));
            }
        }
        ComboBoxFilterWebModel res = new ComboBoxFilterWebModel(combobox, filterName, COLUMN_NAME_APP_DOCFLOW_STATUS);
        res.setElementClass("brd w250");
        res.setLabelOnTop(true);
        return res;
    }

    private static void addStatusCombo(Integer activeId, HttpServletRequest request, NacidDataProvider nacDP) {
        ComboBoxUtils.generateNomenclaturesComboBox(activeId, nacDP.getNomenclaturesDataProvider().getApplicationStatusesExcluding(NumgeneratorDataProvider.NACID_SERIES_ID, Arrays.asList(ApplicationStatus.APPLICATION_STATUS_MIGRATED, ApplicationStatus.APPLICATION_PODADENO_STATUS_CODE, ApplicationStatus.APPLICATION_IZCHAKVANE_STATUS_CODE), null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), false), true, request, "applicationStatus", false);
    }

    private static void addDocflowStatusCombo(Integer activeId, HttpServletRequest request, NacidDataProvider nacDP) {
        ComboBoxUtils.generateNomenclaturesComboBox(activeId, nacDP.getNomenclaturesDataProvider().getApplicationDocflowStatuses(NumgeneratorDataProvider.REGPROF_SERIES_ID, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true)), false, request, "applicationDocflowStatus", false);
    }
    private static boolean isOnlyActive(HttpServletRequest request) {
		boolean result = true;
		//Ako e sumbit-nata forma, vzema parametyra FILTER_NAME_ONLY_ACTIVE ot neq. Ako ne e submitnata, vzema predishniq status onlyActive, zapisan v sesiqta. Ako nqma nishto i tam, vry6ta true!
		//Ideqta e 4e pri natiskane na back butona pri razglejdane na zaqvlenie, doshlo ot tablicata s ne-arhiviranite zaqvleniq, i ako ne se slaga parametyr v sesiqta, onlyActive e true, no trqbva da e false!!!! 
		if (RequestParametersUtils.getParameterFormSubmitted(request)) {
			result = DataConverter.parseBoolean(request.getParameter(FILTER_NAME_ONLY_ACTIVE));
		} else if (request.getSession().getAttribute("onlyActive") != null) {
			result = (Boolean)request.getSession().getAttribute("onlyActive");
		}
		request.getSession().setAttribute("onlyActive", result);
		return result;
		/*//Po default (nqma submitnata forma (v slu4aq forma se submitva edinstveno pri izbirane na filtri)) se vry6tat samo aktivnite zapisi
    	
    	return !RequestParametersUtils.getParameterFormSubmitted(request) ? true : DataConverter.parseBoolean(request.getParameter(FILTER_NAME_ONLY_ACTIVE));*/
	}
    
    private static CheckBoxFilterWebModel generateOnlyActiveFilterWebModel(HttpServletRequest request) {
		CheckBoxFilterWebModel result =  new CheckBoxFilterWebModel(FILTER_NAME_ONLY_ACTIVE, 
				"Покажи само в процедура",
				isOnlyActive(request)
				);
		result.setLabelOnTop(true);
		result.setElementClass("brd flt_rgt");
    	return result;
	}
    private static FilterWebModel generateApplicationNumberFilterWebModel(HttpServletRequest request) {
    	 FilterWebModel filter = new TextFieldFilterWebModel(FILTER_NAME_APP_NUMBER, COLUMN_NAME_APPLICATION_NUMBER, request.getParameter(FILTER_NAME_APP_NUMBER));
         filter.setLabelOnTop(true);
         filter.setElementClass("brd w200");
         return filter;
    }
    protected static void removeTableSessionAttributes(HttpServletRequest request) {
    	request.getSession().removeAttribute(WebKeys.TABLE_APPLICATIONS + ApplicationType.STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE + "1");
    	request.getSession().removeAttribute(WebKeys.TABLE_APPLICATIONS + RUDI_APPLICATION_TYPE + "1");
    	request.getSession().removeAttribute(WebKeys.TABLE_APPLICATIONS + ApplicationType.DOCTORATE_APPLICATION_TYPE + "1");
        request.getSession().removeAttribute(WebKeys.TABLE_APPLICATIONS + ApplicationType.STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE + "2");
        request.getSession().removeAttribute(WebKeys.TABLE_APPLICATIONS + RUDI_APPLICATION_TYPE + "2");
        request.getSession().removeAttribute(WebKeys.TABLE_APPLICATIONS + ApplicationType.DOCTORATE_APPLICATION_TYPE + "2");
    }


    public static void initSimilarDiplomas(HttpServletRequest request, NacidDataProvider nacidDataProvider, String civilId, String firstName, String secondName, String lastName, List<Integer> universityCountryIds, List<String> universityCountryNames, Integer eduLevelId, Integer diplomaYear, List<String> specialityNames, List<String> universityNames, Integer skipApplicationId) {
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();

        List<SimilarDiploma> similarDiplomas = applicationsDataProvider.getSimilarDiplomas(civilId, firstName, universityCountryIds, universityCountryNames, eduLevelId, diplomaYear, skipApplicationId);
        List<SimilarDiplomaWebModel> similarDiplomasWebModel = similarDiplomas == null ? null : similarDiplomas.stream().map(a -> new SimilarDiplomaWebModel(a, nomenclaturesDataProvider, civilId, firstName, secondName, lastName, universityCountryIds, universityCountryNames, specialityNames, universityNames, eduLevelId, diplomaYear)).collect(Collectors.toList());
        request.setAttribute("similarDiplomas", similarDiplomasWebModel);
    }

    private void sendMessageToUser(HttpServletRequest request, HttpServletResponse response, Application application) {
		Person person = application.getApplicant();
		if (application.getOfficialEmailCommunication() == 0 || StringUtils.isEmpty(application.getEmail())) {
			return;
		}
		UtilsDataProvider utilsDataProvider = getNacidDataProvider().getUtilsDataProvider();
		String msgBody = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.APPLICATION_ENTERED_MESSAGE);
		msgBody = MessageFormat.format(msgBody, application.getDocFlowNumber(), person.getFullName());
		String msgSubject = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.APPLICATION_ENTERED_SUBJECT);
		MailDataProvider mailDataProvider = getNacidDataProvider().getMailDataProvider();
		String sender = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.MAIL_SENDER);
        mailDataProvider.sendMessage(sender, sender, person.getFullName(), application.getEmail(), msgSubject, msgBody);
	}
    
}
