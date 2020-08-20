package com.ext.nacid.regprof.web.handlers.impl.applications;

import com.ext.nacid.regprof.web.handlers.ExtRegprofUserAccessUtils;
import com.ext.nacid.regprof.web.model.applications.ExtRegprofApplicationWebModel;
import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.ext.nacid.web.handlers.impl.applications.ExtApplicationsHandler;
import com.ext.nacid.web.model.ExtPersonWebModel;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.applications.regprof.RegprofApplication;
import com.nacid.bl.applications.regprof.RegprofApplicationDataProvider;
import com.nacid.bl.exceptions.NotAuthorizedException;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.external.ExtPersonDataProvider;
import com.nacid.bl.external.applications.SignedXmlException;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.mail.MailBeanImpl;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationDetailsImpl;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationImpl;
import com.nacid.bl.mail.MailBean;
import com.nacid.bl.mail.MailDataProvider;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.nomenclatures.regprof.EducationType;
import com.nacid.bl.nomenclatures.regprof.ServiceType;
import com.nacid.bl.payments.Liability;
import com.nacid.bl.regprof.external.ExtRegprofApplicationAttachmentDataProvider;
import com.nacid.bl.regprof.external.ExtRegprofApplicationForList;
import com.nacid.bl.regprof.external.ExtRegprofApplicationsDataProvider;
import com.nacid.bl.regprof.external.ExtRegprofTrainingCourse;
import com.nacid.bl.signature.SignatureParams;
import com.nacid.bl.signature.SuccessSign;
import com.nacid.bl.table.*;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.data.external.applications.ExtPersonRecord;
import com.nacid.data.regprof.applications.RegprofCertificateNumberToAttachedDocRecord;
import com.nacid.data.regprof.external.*;
import com.nacid.data.regprof.external.applications.ExtRegprofDocumentRecipientRecord;
import com.nacid.utils.SignUtils;
import com.nacid.web.MessagesBundle;
import com.nacid.web.ValidationStrings;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.UserOperationsUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.hibernate.validator.constraints.impl.URLValidator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

import static com.nacid.bl.nomenclatures.DocumentType.*;
import static org.apache.cxf.version.Version.getName;

public class ExtRegprofApplicationsHandler extends NacidExtBaseRequestHandler {
    
    public static final int FORM_ID_APPLICATION_DATA = 1;
    public static final int FORM_ID_TRAINING_DATA = 2;
    public static final int FORM_ID_ATTACHMENTS_DATA = 3;
    public static final int FORM_ID_APPLYING = 4;
    public static final int FORM_ID_EXTERNAL_APPLICATION_REPORT = 5;
    public static final int FORM_ID_INTERNAL_APPLICATION_REPORT = 6;
    public static final int FORM_ID_EPAY_PAYMENT = 7;

    private final static String COLUMN_NAME_ID = "id";
    private final static String COLUMN_NAME_NAME = "Име";
    private final static String COLUMN_NAME_DATE = "Дата на въвеждане";
    private final static String COLUMN_NAME_DOCFLOW_NUMBER = "Деловоден номер";
    private final static String COLUMN_NAME_STATUS = "Статус";
    private final static String COLUMN_NAME_ESIGNED = "Ел. подп.";
    
    private static final String ATTRIBUTE_NAME = ExtRegprofApplicationImpl.class.getName();
    
    public ExtRegprofApplicationsHandler(ServletContext servletContext) {
        super(servletContext);
    }
    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ExtRegprofApplicationsDataProvider dp = nacidDataProvider.getExtRegprofApplicationsDataProvider();
        ExtPerson person = getExtPerson(request, response);
        fillData(request, response, nacidDataProvider, person, dp.createEmtpyRegprofApplication(person.getId()), FORM_ID_APPLICATION_DATA);
        
    }
    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        
        ExtPerson extPerson = getExtPerson(request, response);
        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        ExtRegprofApplicationsDataProvider dp = getNacidDataProvider().getExtRegprofApplicationsDataProvider();
        ExtRegprofApplicationImpl rec = dp.getExtRegprofApplication(id);
        
        try {
            if (UserOperationsUtils.getOperationId(getOperationName(request)) == UserOperationsUtils.OPERATION_LEVEL_EDIT) {
                ExtRegprofUserAccessUtils.checkApplicantActionAccess(ExtRegprofUserAccessUtils.USER_ACTION_CHANGE, getExtPerson(request, response), rec.getApplicationDetails());    
            } else {
                ExtRegprofUserAccessUtils.checkApplicantActionAccess(ExtRegprofUserAccessUtils.USER_ACTION_VIEW, getExtPerson(request, response), rec.getApplicationDetails());
            }    
        } catch (NotAuthorizedException e) {
            throw new RuntimeException(e.getMessage());
        }
        
        Integer activeFormId = DataConverter.parseInteger(request.getParameter(WebKeys.ACTIVE_FORM), FORM_ID_APPLICATION_DATA);
        fillData(request, response, getNacidDataProvider(), extPerson, rec, activeFormId);
        //RayaWritten---------------------------------------------------------------------------------------------------------
        //Generirane na taba s report-a (informaciqta) za vytre6noto zaqvlenie, kakto i taba sys zaqvlenieto ot vyn6noto prilojenie
        //tezi 2 taba se pokazvat samo kogato zaqvlenieto e prehvyrleno vyv vytre6nata baza!
        if (rec.getApplicationDetails().getRegprofApplicationId() != null) {
            RegprofApplicantReportHandler.generateExternalApplicantReport(getNacidDataProvider(), rec, request, "ext", extPerson);
            RegprofApplication app = getNacidDataProvider().getRegprofApplicationDataProvider().getRegprofApplication(rec.getApplicationDetails().getRegprofApplicationId());
            RegprofApplicantReportHandler.generateInternalApplicantReport(getNacidDataProvider(), app, request, "");
            request.setAttribute(WebKeys.ACTIVE_FORM, FORM_ID_EXTERNAL_APPLICATION_REPORT);
        }
        //--------------------------------------------------------------------------------------------------------------------------
    }

    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        Integer activeForm = DataConverter.parseInteger(request.getParameter(WebKeys.ACTIVE_FORM), FORM_ID_APPLICATION_DATA);
        if (activeForm == FORM_ID_APPLICATION_DATA) {
            saveApplication(request, response);
        } else if (activeForm == FORM_ID_TRAINING_DATA) {
            new ExtRegprofTrainingCourseHandler(getServletContext()).handleSave(request, response);
        } else if (activeForm == FORM_ID_APPLYING) {
            saveApply(request, response); 
        }
        request.getSession().removeAttribute(WebKeys.TABLE_EXTERNAL_REGPROF_APPLICATIONS);
    }

    private void saveApplication(HttpServletRequest request, HttpServletResponse response) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ExtRegprofApplicationsDataProvider dp = nacidDataProvider.getExtRegprofApplicationsDataProvider();
        ExtPersonDataProvider epdp = nacidDataProvider.getExtPersonDataProvider();
        ExtRegprofApplicationImpl record = (ExtRegprofApplicationImpl) request.getAttribute(ATTRIBUTE_NAME);
        if (record == null) {
            throw new UnknownRecordException("Missing ExtRegprofApplication object...");
        }



        ExtPerson representative = getExtPerson(request, response);
        if (record.getApplicationDetails().getId() != null) { //trying to edit...
            try {
                ExtRegprofUserAccessUtils.checkApplicantActionAccess(record.getApplicationDetails().getId(), getLoggedUser(request, response).getUserId(), ExtRegprofUserAccessUtils.USER_ACTION_CHANGE, getNacidDataProvider());
            } catch (NotAuthorizedException e) {
                throw new RuntimeException(e.getMessage());
            }    
        }


        //saving extPerson's civilId/civilIdType/ (if necessary)
        String representativePersonalId = DataConverter.parseString(request.getParameter("representative.civilId"), null);
        int representativePersonalIdType = DataConverter.parseInt(request.getParameter("representative.civilIdType"), 0);

        String representativeBirthCity = DataConverter.parseString(request.getParameter("representative_birth_city"), null);
        Date representativeBirthDate = DataConverter.parseDate(request.getParameter("representative_birth_date"));
        Integer representativeBirthCountryId = DataConverter.parseInteger(request.getParameter("representative_birth_country_id"), null);
        Integer representativeCitizenshipId = DataConverter.parseInteger(request.getParameter("representative_citizenship"), null);

        if (StringUtils.isEmpty(representative.getCivilId()) && representativePersonalId == null) {
            throw new RuntimeException("Няма въведен персонален идентификатор");
        }
        if (representative.getCivilIdTypeId() == null && representativePersonalIdType == 0) {
            throw new RuntimeException("Няма въведен тип персонален идентификатор");
        }

        if (representativePersonalIdType == CivilIdType.CIVIL_ID_TYPE_EGN && !DataConverter.validateEGN(representativePersonalId) && !DataConverter.parseBoolean(request.getParameter("representative.civilIdIgnoreEGNValidation"))) {
            throw new RuntimeException("Некоренто въведено ЕГН!");
        }
        if (representativePersonalIdType == CivilIdType.CIVIL_ID_TYPE_LNCH && !DataConverter.validateLNCH(representativePersonalId) && !DataConverter.parseBoolean(request.getParameter("representative.civilIdIgnoreLNCHValidation"))) {
            throw new RuntimeException("Некоренто въведено ЛНЧ!");
        }
        if (StringUtils.isEmpty(record.getApplicationDetails().getApplicantCity())) {
            throw new RuntimeException("Missing applicantCity");
        }
        if (StringUtils.isEmpty(record.getApplicationDetails().getApplicantPhone())) {
            throw new RuntimeException("Missing applicantPhone");
        }
        if (StringUtils.isEmpty(record.getApplicationDetails().getApplicantAddrDetails())) {
            throw new RuntimeException("Missing applicantAddress");
        }
        if (record.getApplicationDetails().getApplicantCountryId() == null) {
            throw new RuntimeException("Missing applicantCountry");
        }


        boolean differentApplicantRepresentative = record.getDifferentApplicantRepresentative() == 1;

        if (differentApplicantRepresentative) {
            ExtPersonRecord applicantRecord =  record.getApplicant();
            if (applicantRecord.getId() != 0) {
                ExtPerson dbPerson = epdp.getExtPerson(applicantRecord.getId());
                if (dbPerson == null) {
                    throw new RuntimeException("Unknown ext person with id = " + applicantRecord.getId());
                }
                if (!Objects.equals(applicantRecord.getCivilId(), dbPerson.getCivilId()) || !Objects.equals(applicantRecord.getCivilIdType(), dbPerson.getCivilIdTypeId())) {
                    throw new RuntimeException("Променени тип идентификатор/идентификатор/ на заявителя!");
                }
            }
            if (StringUtils.isEmpty(applicantRecord.getFname()) || !applicantRecord.getFname().matches(ValidationStrings.getValidationStringForJava(ValidationStrings.VALIDATION_STRING_NAME))) {
                throw new RuntimeException("Грешно въведено име на заявителя!");
            }
            if (!StringUtils.isEmpty(applicantRecord.getSname()) && !applicantRecord.getSname().matches(ValidationStrings.getValidationStringForJava(ValidationStrings.VALIDATION_STRING_NAME))) {
                throw new RuntimeException("Грешно въведено презиме на заявителя!");
            }
            if (StringUtils.isEmpty(applicantRecord.getLname()) || !applicantRecord.getLname().matches(ValidationStrings.getValidationStringForJava(ValidationStrings.VALIDATION_STRING_NAME))) {
                throw new RuntimeException("Грешно въведена фамилия на заявителя!");
            }

            if (record.getApplicationDetails().getNamesDontMatch() == 1) {
                ExtRegprofTrainingCourseRecord tc = record.getTrainingCourseDetails();
                if (tc.getDocumentCivilIdType() != null && tc.getDocumentCivilIdType() == CivilIdType.CIVIL_ID_TYPE_EGN && !DataConverter.validateEGN(tc.getDocumentCivilId()) && !DataConverter.parseBoolean(request.getParameter("trainingCourseDetails.documentCivilIdIgnoreEGNValidation"))) {
                    throw new RuntimeException("Грешно въведено ЕГН по документ за ПК!");
                }
                if (tc.getDocumentCivilIdType() != null && tc.getDocumentCivilIdType() == CivilIdType.CIVIL_ID_TYPE_LNCH && !DataConverter.validateLNCH(tc.getDocumentCivilId()) && !DataConverter.parseBoolean(request.getParameter("trainingCourseDetails.documentCivilIdIgnoreLNCHValidation"))) {
                    throw new RuntimeException("Грешно въведено ЛНЧ по документ за ПК!");
                }
                if (StringUtils.isEmpty(tc.getDocumentFname()) || !tc.getDocumentFname().matches(ValidationStrings.getValidationStringForJava(ValidationStrings.VALIDATION_STRING_NAME))) {
                    throw new RuntimeException("Грешно въведено име по документ за ПК!");
                }
                if (!StringUtils.isEmpty(tc.getDocumentSname()) && !tc.getDocumentSname().matches(ValidationStrings.getValidationStringForJava(ValidationStrings.VALIDATION_STRING_NAME))) {
                    throw new RuntimeException("Грешно въведено презиме по документ за ПК!");
                }
                if (StringUtils.isEmpty(tc.getDocumentLname()) || !tc.getDocumentLname().matches(ValidationStrings.getValidationStringForJava(ValidationStrings.VALIDATION_STRING_NAME))) {
                    throw new RuntimeException("Грешно въведена фамилия по документ за ПК!");
                }
            }

            if (StringUtils.isEmpty(applicantRecord.getCivilId())) {
                throw new RuntimeException("Няма въведен персонален идентификатор на заявителя");
            }

            if (applicantRecord.getCivilIdType() == null) {
                throw new RuntimeException("Няма въведен тип персонален идентификатор на заявителя");
            }
            if (applicantRecord.getEmail() == null) {
                throw new RuntimeException("Няма въведен email на заявителя!");
            }

            if (applicantRecord.getCivilIdType() != null && applicantRecord.getCivilIdType() == CivilIdType.CIVIL_ID_TYPE_EGN && !DataConverter.validateEGN(applicantRecord.getCivilId()) && !DataConverter.parseBoolean(request.getParameter("applicant.civilIdIgnoreEGNValidation"))) {
                throw new RuntimeException("Некоренто въведено ЕГН на заявител!");
            }
            if (applicantRecord.getCivilIdType() != null && applicantRecord.getCivilIdType() == CivilIdType.CIVIL_ID_TYPE_LNCH && !DataConverter.validateLNCH(applicantRecord.getCivilId()) && !DataConverter.parseBoolean(request.getParameter("applicant.civilIdIgnoreLNCHValidation"))) {
                throw new RuntimeException("Некоренто въведено ЛНЧ на заявител!");
            }
            if (StringUtils.isEmpty(applicantRecord.getBirthCity())) {
                throw new RuntimeException("Липсва място на раждане на заявителя");
            }
            if (applicantRecord.getBirthCountryId() == null) {
                throw new RuntimeException("Липсва държава на раждане на заявителя");
            }
            if (applicantRecord.getBirthDate() == null) {
                throw new RuntimeException("Липсва дата на раждане на заявителя");
            }
            if (applicantRecord.getCitizenshipId() == null) {
                throw new RuntimeException("Липсва гражданство на заявителя");
            }
            if (record.getApplicationDetails().getApplicantPersonalIdDocumentType() == null) {
                throw new RuntimeException("Липсва тип документ за самоличност");
            }
        } else {
            if (StringUtils.isEmpty(representativeBirthCity)) {
                throw new RuntimeException("Липсва място на раждане на представителя");
            }
            if (representativeBirthCountryId == null) {
                throw new RuntimeException("Липсва държава на раждане на представителя");
            }
            if (representativeBirthDate == null) {
                throw new RuntimeException("Липсва дата на раждане на представителя");
            }
            if (representativeCitizenshipId == null) {
                throw new RuntimeException("Липсва гражданство на представителя");
            }

        }
        boolean updateRepresentative = StringUtils.isEmpty(representative.getCivilId()) || representative.getCivilIdTypeId() == null || !differentApplicantRepresentative;

        if (updateRepresentative) {
            representativePersonalIdType = representative.getCivilIdTypeId() == null ? representativePersonalIdType : representative.getCivilIdTypeId();
            representativePersonalId = StringUtils.isEmpty(representative.getCivilId()) ? representativePersonalId : representative.getCivilId();

            representativeBirthCity = !differentApplicantRepresentative ? representativeBirthCity : representative.getBirthCity();
            representativeBirthCountryId = !differentApplicantRepresentative ? representativeBirthCountryId : representative.getBirthCountryId();
            representativeBirthDate = !differentApplicantRepresentative ? representativeBirthDate : representative.getBirthDate();
            representativeCitizenshipId = !differentApplicantRepresentative ? representativeCitizenshipId : representative.getCitizenshipId();

            epdp.saveExtPerson(representative.getId(), representative.getFName(), representative.getSName(), representative.getLName(), representativePersonalId, representativePersonalIdType, representativeBirthCountryId, representativeBirthCity, representativeBirthDate, representativeCitizenshipId, representative.getEmail(), representative.getHashCode(), representative.getUserId());
            request.getSession().setAttribute("extPerson", null);//resetting extPerson!
        }
        //end of saving person's civildId/civilIdType

        record.getApplicationDetails().setRepresentativeId(representative.getId());

        dp.saveExtRegprofApplication(record);

        if (record.getApplicationDetails().getId() != null) {
            record = dp.getExtRegprofApplication(record.getApplicationDetails().getId());
        }

        request.setAttribute("applicationStatusMessage", SystemMessageWebModel.createDataInsertedWebMessage());
        fillData(request, response, nacidDataProvider, getExtPerson(request, response), record, FORM_ID_APPLICATION_DATA);
    }

    private void saveApply(HttpServletRequest request, HttpServletResponse response) {

        int applicationId = DataConverter.parseInt(request.getParameter("applicationId"), 0);
        if (applicationId == 0) {
            throw new UnknownRecordException("unknown application id: " + applicationId);
        }
        ExtRegprofApplicationsDataProvider eaDP = getNacidDataProvider().getExtRegprofApplicationsDataProvider();
        ExtRegprofApplicationImpl app = eaDP.getExtRegprofApplication(applicationId);
        try {
            ExtRegprofUserAccessUtils.checkApplicantActionAccess(ExtRegprofUserAccessUtils.USER_ACTION_CHANGE, getExtPerson(request, response), app.getApplicationDetails());
        } catch (NotAuthorizedException e) {
            throw new RuntimeException(e.getMessage());
        }
        Integer serviceType = DataConverter.parseInteger(request.getParameter("serviceTypeId"), null);
        Integer paymentType = DataConverter.parseInteger(request.getParameter("paymentTypeId"), null);
        if (serviceType == null || paymentType == null) {
            throw new RuntimeException("ServiceType and PaymentType cannot be null!!!");
        }

        if (initApplyingTab(getNacidDataProvider(), request, response,app)) {
            throw new RuntimeException("Missing significant data...");
        }
        //Zapisvaneto na signed xml-a trqbva da stane zadyljitelno predi zapisvaneto na novite danni za application-a v bazata,
        //zashtoto sled prezapisvaneto na application-a, toi shte ima nov status i nov timeOfCreation!!!
        SuccessSign signedXml = (SuccessSign) request.getSession().getAttribute("signedXml-" + applicationId);
        if (signedXml != null && !StringUtils.isEmpty(signedXml.getXmlSigned())) {
            try {
                eaDP.saveSignedApplicationXml(getLoggedUser(request, response).getUserId(), applicationId, signedXml);
            } catch (SignedXmlException e) {
                addSystemMessageToSession(request, "applyingStatusMessage", new SystemMessageWebModel(e.getMessage(), SystemMessageWebModel.MESSAGE_TYPE_ERROR));
                try {
                    response.sendRedirect(request.getContextPath() + "/control/applications/"
                            + "edit"
                            + "?id=" + applicationId + "&activeForm=" + FORM_ID_APPLYING + "&showtab=1");
                } catch (IOException e1) {
                    throw Utils.logException(e1);
                }
                return;
            }   
        }

        Date dateSubmitted = new Date();
        eaDP.submitExtRegprofApplication(applicationId, dateSubmitted, ExtRegprofApplicationImpl.STATUS_NOT_EDITABLE, serviceType, paymentType);

        request.getSession().removeAttribute(WebKeys.TABLE_APPLICATIONS);
        /*UtilsDataProvider utilsDataProvider = getNacidDataProvider().getUtilsDataProvider();
        String msg = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.REGPROF_APPLICATION_APPLIED_MESSAGE);
        msg = MessageFormat.format(msg, DataConverter.formatDateTime(dateSubmitted, false));

        MailDataProvider mailDataProvider = getNacidDataProvider().getMailDataProvider();
        ExtPerson person = getExtPerson(request, response);
        String sender = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.MAIL_SENDER);
        mailDataProvider.sendMessage(sender, sender, person.getFullName(), person.getEmail(), utilsDataProvider.getCommonVariableValue(UtilsDataProvider.REGPROF_APPLICATION_APPLIED_SUBJECT), msg);

        addSystemMessageToSession(request, "applyingStatusMessage", new SystemMessageWebModel(msg, SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        */


        //sending messages to applicant and employees
        UtilsDataProvider utilsDataProvider = getNacidDataProvider().getUtilsDataProvider();       
        ExtPerson person = getExtPerson(request, response);
        String sender = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.REGPROF_MAIL_SENDER);
        String applicantSubject = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.REGPROF_APPLICATION_APPLIED_SUBJECT);
        Map <String, String> toM = new HashMap<String, String>();
        toM.put(person.getEmail(), person.getFName() + " " + person.getLName());
        String signedMsg = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.REGPROF_APPLICATION_APPLIED_MESSAGE);
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("dateSubmitted", DataConverter.formatDateTime(dateSubmitted, false));
        signedMsg = StrSubstitutor.replace(signedMsg, placeholders);
        String employeesMsg = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.EMPLOYEES_NEW_APPLICATION_MSG);
        String employeesSubject = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.EMPLOYEES_NEW_APPL_SUBJECT);
        Map <String, String> toEmployeesM = new HashMap<>();
        String employeesMailsString = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.EMPLOYEES_MAILS);
        String[] employeesMails = employeesMailsString.split(";");
        for(String receiver: employeesMails){
            toEmployeesM.put(receiver.trim(), receiver.trim());
        }
        Integer emailId = sendInformingMessage(sender, toM, signedMsg, applicantSubject);
        eaDP.saveApplicationComment(applicationId, "Subject:"  + applicantSubject + "\nBody:" + signedMsg, true, emailId, true, getLoggedUser(request, response).getUserId());
        sendInformingMessage(sender, toEmployeesM, employeesMsg, employeesSubject);
        //end of sending messages


        String statusMessage = MessagesBundle.getMessagesBundle().getValue("regprofApplicationAppliedMessage");
        statusMessage = StrSubstitutor.replace(statusMessage, placeholders);
        addSystemMessageToSession(request, "applyingStatusMessage", new SystemMessageWebModel(statusMessage, SystemMessageWebModel.MESSAGE_TYPE_CORRECT));

        try {
            response.sendRedirect(request.getContextPath() + "/control/applications/"
                    + "view"
                    + "?id=" + applicationId + "&activeForm=" + FORM_ID_APPLYING);
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }
    
    public Integer sendInformingMessage(String sender, Map<String, String> toM, String msg, String subject){
        MailDataProvider mailDataProvider = getNacidDataProvider().getMailDataProvider();
        Map <String, String> fromM = new HashMap<String, String>();
        fromM.put(sender, sender);
        MailBean mailBean = new MailBeanImpl(fromM, sender, toM, null, subject, msg, mailDataProvider.getSession());
        return mailDataProvider.sendMessage(mailBean);
    }

    static void fillData(HttpServletRequest request, HttpServletResponse response, NacidDataProvider nacidDataProvider, ExtPerson extPerson, ExtRegprofApplicationImpl rec, int activeForm) {
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        List<Country> countries = nacidDataProvider.getNomenclaturesDataProvider().getCountries(null, null);

        ComboBoxUtils.generateNomenclaturesComboBox(null, countries, true, request, "country", true);
        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_SERVICE_TYPE, nomenclaturesDataProvider, true, request, "serviceType", null, true);
        ComboBoxUtils.generateNomenclaturesRadioButton(rec == null || rec.getTrainingCourseDetails() == null || rec.getTrainingCourseDetails().getDocumentCivilIdType() == null ? 1 : rec.getTrainingCourseDetails().getDocumentCivilIdType(), nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, null, null), true, request, "documentCivilIdType");
        ComboBoxUtils.generateDocumentReceiveMethodRadioButton(rec == null || rec.getApplicationDetails() == null ? null : rec.getApplicationDetails().getDocumentReceiveMethodId(), nomenclaturesDataProvider.getDocumentReceiveMethods(null, null), true, request, "documentReceiveMethod");

        setNextScreen(request, "application_edit");
        
        ExtPersonWebModel epwm = new ExtPersonWebModel(extPerson);

        request.setAttribute("extPerson", epwm);
        request.setAttribute(ATTRIBUTE_NAME, rec);
        
        if (rec.getTrainingCourseDetails() != null && rec.getTrainingCourseDetails().getId() != null) {
            request.setAttribute("id", rec.getApplicationDetails().getId());
            new ExtRegprofTrainingCourseHandler(request.getSession().getServletContext()).handleEdit(request, response);    
        }
        
        Integer internalApplicationId = rec == null || rec.getApplicationDetails() == null ? null : rec.getApplicationDetails().getRegprofApplicationId();
        String docflowNumber = null;
        String certificateNumber = null;
        if (internalApplicationId != null) {
            RegprofApplicationDataProvider regprofApplicationDataProvider = nacidDataProvider.getRegprofApplicationDataProvider();
            RegprofApplication app = regprofApplicationDataProvider.getRegprofApplication(internalApplicationId);
            docflowNumber = Utils.generateDocFlowNumber(app.getApplicationDate(), app.getApplicationNumber());
            RegprofCertificateNumberToAttachedDocRecord certNum = regprofApplicationDataProvider.getCertificateNumber(internalApplicationId, 0);
            certificateNumber = certNum == null ? null : certNum.getCertificateNumber();
        }

        request.setAttribute("erpApplicationWebModel", new ExtRegprofApplicationWebModel(getAppStatusName(rec.getApplicationDetails() == null ? null : rec.getApplicationDetails().getStatus()), docflowNumber, certificateNumber));
        if (!rec.isNew()) {
            new ExtRegprofApplicationAttachmentHandler(request.getSession().getServletContext()).handleList(request, response);
            
            Liability liab = nacidDataProvider.getPaymentsDataProvider().getLiabilityByExternalApplicationId(rec.getApplicationDetails().getId());
            //Filling epaypayment data... tazi informaciq e nalichna samo ako plashtaneto e chrez epay!        
            if (rec.getApplicationDetails().getStatus() == ExtRegprofApplicationImpl.STATUS_NOT_EDITABLE && liab.getPaymentType() == Liability.PAYMENT_TYPE_EPAY) {
                EpayPaymentHandler.fillEpayPaymentData(extPerson, rec.getApplicationDetails(), request, nacidDataProvider, liab);
            }    
        }

        request.setAttribute(WebKeys.ACTIVE_FORM, activeForm);
        if (!rec.isNew() && rec.getApplicationDetails().getStatus() == ExtRegprofApplicationImpl.STATUS_EDITABLE) {
            initApplyingTab(nacidDataProvider, request, response, rec);    
        }

        ComboBoxUtils.generateNomenclaturesRadioButton(rec.getApplicant() == null ? null : rec.getApplicant().getCivilIdType(), nomenclaturesDataProvider.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, null, null), true, request, "applicantCivilIdType");

        ComboBoxUtils.generateNomenclaturesComboBox(rec.getApplicant() == null || rec.getDifferentApplicantRepresentative() != 1  ? null : rec.getApplicant().getBirthCountryId(), countries, true, request, "applicantBirthCountry", true);
        ComboBoxUtils.generateNomenclaturesComboBox(extPerson.getBirthCountryId(), countries, true, request, "representativeBirthCountry", true);

        ComboBoxUtils.generateNomenclaturesComboBox(extPerson.getCitizenshipId(), countries, true, request, "representativeCitizenship", true);
        ComboBoxUtils.generateNomenclaturesComboBox(rec.getApplicant() == null || rec.getDifferentApplicantRepresentative() != 1 ? null : rec.getApplicant().getCitizenshipId(), countries, true, request, "applicantCitizenship", true);

        ComboBoxUtils.generateNomenclaturesComboBox(rec.getApplicationDetails() == null ? null : rec.getApplicationDetails().getApplicantPersonalIdDocumentType(), NomenclaturesDataProvider.FLAT_NOMENCLATURE_PERSONAL_ID_DOCUMENT_TYPE, nomenclaturesDataProvider, true, request, "applicantPersonalIdDocumentType", null, true);

        request.setAttribute("regprofEservicesExpComment", nacidDataProvider.getUtilsDataProvider().getCommonVariableValue(UtilsDataProvider.REGPROF_ESERVICES_ÄPPLICATION_EXPERIENCE_COMMENT));
        request.setAttribute("regprofEservicesEduComment", nacidDataProvider.getUtilsDataProvider().getCommonVariableValue(UtilsDataProvider.REGPROF_ESERVICES_ÄPPLICATION_EDUCATION_COMMENT));
    }

    private static boolean addDocumentRecipientSystemMessage(NacidDataProvider nacidDataProvider, ExtRegprofApplicationImpl rec, List<SystemMessageWebModel> webModel, boolean hideApplyButton) {
        SystemMessageWebModel sm = new SystemMessageWebModel("Начин на получаване на уведомления", SystemMessageWebModel.MESSAGE_TYPE_CORRECT);
        boolean hasError = false;
        if (rec == null || rec.getApplicationDetails() == null || rec.getApplicationDetails().getDocumentReceiveMethodId() == null) {
            sm.setMessageType(SystemMessageWebModel.MESSAGE_TYPE_ERROR);
            hasError = true;
        } else if (rec != null && rec.getApplicationDetails() != null && rec.getApplicationDetails().getDocumentReceiveMethodId() != null && nacidDataProvider.getNomenclaturesDataProvider().getDocumentReceiveMethod(rec.getApplicationDetails().getDocumentReceiveMethodId() ).hasDocumentRecipient()) {
            ExtRegprofDocumentRecipientRecord dr = rec.getDocumentRecipient();
            if (dr == null) {
                hasError = true;
            } else  {
                if (StringUtils.isEmpty(dr.getAddress())) {
                    hasError = true;
                    sm.addAttribute("Адрес");
                }
                if (StringUtils.isEmpty(dr.getName())) {
                    hasError = true;
                    sm.addAttribute("Име");
                }
                if (StringUtils.isEmpty(dr.getDistrict())) {
                    hasError = true;
                    sm.addAttribute("Област");
                }
                if (StringUtils.isEmpty(dr.getPostCode())) {
                    hasError = true;
                    sm.addAttribute("Пощенски код");
                }
                if (StringUtils.isEmpty(dr.getCity())) {
                    hasError = true;
                    sm.addAttribute("Град");
                }
                if (StringUtils.isEmpty(dr.getMobilePhone())) {
                    hasError = true;
                    sm.addAttribute("Мобилен телефон");
                }
            }

        }
        if (hasError) {
            sm.setMessageType(SystemMessageWebModel.MESSAGE_TYPE_ERROR);
        }
        webModel.add(sm);
        return hasError || hideApplyButton;
    }
    public static boolean addSystemMessage(List<SystemMessageWebModel> webModel, String title, boolean isError, boolean hideApplyButton) {
        SystemMessageWebModel sm = new SystemMessageWebModel(title, isError ? SystemMessageWebModel.MESSAGE_TYPE_ERROR : SystemMessageWebModel.MESSAGE_TYPE_CORRECT);
        webModel.add(sm);
        return hideApplyButton || isError;
    }
    /**
     * 
     * @param nacidDataProvider
     * @param request
     * @param response
     * @param rec
     * @return dali ima problem ili ne pri inicializaciq na applying tab-a! - true - ima problem, false - nqma!
     */
    static boolean initApplyingTab(NacidDataProvider nacidDataProvider, HttpServletRequest request, HttpServletResponse response, ExtRegprofApplicationImpl rec) {
        ExtRegprofApplicationsDataProvider extRegprofApplicationsDataProvider = nacidDataProvider.getExtRegprofApplicationsDataProvider();
        
        boolean hideApplyButton = false;
        
        List<SystemMessageWebModel> listSM = new ArrayList<SystemMessageWebModel>();
        ExtRegprofApplicationDetailsImpl details = rec.getApplicationDetails();
        ExtRegprofTrainingCourseRecord tcDetails = rec.getTrainingCourseDetails();
        ExtRegprofTrainingCourse tc = extRegprofApplicationsDataProvider.getTrainingCourse(details.getId());
        ExtRegprofProfessionExperienceRecord experienceRecord = tc.getExperienceRecord();
        List<ExtRegprofTrainingCourseSpecialitiesRecord> specialities = tc.getSpecialities();



        List<Integer> attachmentTypesToCheck = new ArrayList<>();

        hideApplyButton = addDocumentRecipientSystemMessage(nacidDataProvider, rec, listSM, hideApplyButton);
        if (rec != null && rec.getApplicationDetails().getDocumentReceiveMethodId() != null) {
            DocumentReceiveMethod drm = nacidDataProvider.getNomenclaturesDataProvider().getDocumentReceiveMethod(rec.getApplicationDetails().getDocumentReceiveMethodId());
            if (drm.isEservicesRequirePaymentReceipt()) {
                attachmentTypesToCheck.add(DOC_TYPE_PAYMENT_ORDER_COURIER_NORQ);
            }
        }

        hideApplyButton = addSystemMessage(listSM, "Дата на раждане", rec == null || rec.getApplicant() == null || rec.getApplicant().getBirthDate() == null, hideApplyButton);
        hideApplyButton = addSystemMessage(listSM, "Държава на раждане", rec == null || rec.getApplicant() == null || rec.getApplicant().getBirthCountryId() == null, hideApplyButton);
        hideApplyButton = addSystemMessage(listSM, "Място на раждане", rec == null || rec.getApplicant() == null || StringUtils.isEmpty(rec.getApplicant().getBirthCity()), hideApplyButton);
        hideApplyButton = addSystemMessage(listSM, "Съгласен съм да се ползват лични данни за целите на проверката", rec.getApplicationDetails().getPersonalDataUsage() == 0, hideApplyButton);
        hideApplyButton = addSystemMessage(listSM, "Кандидатства за държава", details.getApplicationCountryId() == null, hideApplyButton);
        hideApplyButton = addSystemMessage(listSM, "Обучение или стаж", (tcDetails.getHasEducation() == 0 && tcDetails.getHasExperience() == 0) || (tcDetails.getHasExperience() == 1 && experienceRecord == null), hideApplyButton);

        if (tcDetails.getHasEducation() == 1) {
            hideApplyButton = addSystemMessage(listSM, "Вид обучение", tcDetails.getEducationTypeId() == null, hideApplyButton);
            
            boolean isEduNameError = true;
            boolean isQualificationError = true;
            boolean isSpecialityError = true;
            boolean isDocumentTypeError = true;
            if (tcDetails.getEducationTypeId() != null) {
                isEduNameError = tcDetails.getProfInstitutionId() == null && tcDetails.getProfInstitutionOrgNameId() == null && StringUtils.isEmpty(tcDetails.getProfInstitutionNameTxt()) && StringUtils.isEmpty(tcDetails.getProfInstitutionOrgNameTxt());
                isDocumentTypeError = tcDetails.getDocumentType() == null;
                //pri SDK se proverqbvat dali se popylneni i dvete institucii, kakto i dvata documentTypes!!!
                if (tcDetails.getEducationTypeId() == EducationType.EDU_TYPE_SDK) {
                   isEduNameError = isEduNameError || (tcDetails.getSdkProfInstitutionId() == null && tcDetails.getSdkProfInstitutionOrgNameId() == null && StringUtils.isEmpty(tcDetails.getSdkProfInstitutionNameTxt()) && StringUtils.isEmpty(tcDetails.getSdkProfInstitutionOrgNameTxt()));
                   isDocumentTypeError = isDocumentTypeError || tcDetails.getSdkDocumentType() == null;
                }

                //profQualification check
                if (tcDetails.getEducationTypeId() == EducationType.EDU_TYPE_SECONDARY || tcDetails.getEducationTypeId() == EducationType.EDU_TYPE_SECONDARY_PROFESSIONAL) {
                    isQualificationError = tcDetails.getSecProfQualificationId() == null;
                } else {//sdk ili vishshe
                    isQualificationError = tcDetails.getHighProfQualificationId() == null && StringUtils.isEmpty(tcDetails.getHighProfQualificationTxt());
                    if (tcDetails.getEducationTypeId() == EducationType.EDU_TYPE_SDK) {//pri sdk trqbva da sa popylneni kvalifikaciite i na visheto i na sdk
                        isQualificationError = isQualificationError || (tcDetails.getSdkProfQualificationId() == null && StringUtils.isEmpty(tcDetails.getSdkProfQualificationTxt()));    
                    }
                }
                //end of profQualificationCheck

                //specialities error check
                if (specialities != null && specialities.size() > 0) {
                    //isSpecialityError = false;
                    boolean isSpeciality = false;
                    boolean isSdkSpeciality = false;
                    for (ExtRegprofTrainingCourseSpecialitiesRecord r:specialities) {
                        if (tcDetails.getEducationTypeId() == EducationType.EDU_TYPE_SECONDARY || tcDetails.getEducationTypeId() == EducationType.EDU_TYPE_SECONDARY_PROFESSIONAL) {
                            isSpeciality = isSpeciality || r.getSecondarySpecialityId() != null || !StringUtils.isEmpty(r.getSecondarySpecialityTxt());
                        } else {
                            isSpeciality = isSpeciality || r.getHigherSpecialityId() != null || !StringUtils.isEmpty(r.getHigherSpecialityTxt());
                            if (tcDetails.getEducationTypeId() == EducationType.EDU_TYPE_SDK) {
                                isSdkSpeciality = isSdkSpeciality || r.getSdkSpecialityId() != null || !StringUtils.isEmpty(r.getSdkSpecialityTxt());
                            }
                        }
                    }
                    if (tcDetails.getEducationTypeId() != EducationType.EDU_TYPE_SDK) {
                        isSpecialityError = !isSpeciality;
                    } else {
                        isSpecialityError = !isSpeciality || !isSdkSpeciality;
                    }
                }
                //end of specialities error check
            }
            hideApplyButton = addSystemMessage(listSM, "Ново име или старо име на обучаваща институция", isEduNameError, hideApplyButton);
            hideApplyButton = addSystemMessage(listSM, "Професионална квалификация по документи", isQualificationError, hideApplyButton);
            hideApplyButton = addSystemMessage(listSM, "Специалност по документи", isSpecialityError, hideApplyButton);
            hideApplyButton = addSystemMessage(listSM, "Вид документ", isDocumentTypeError, hideApplyButton);
            
        }
        if (tcDetails.getHasExperience() == 1 && experienceRecord != null) {
            hideApplyButton = addSystemMessage(listSM, "Стаж - професия", experienceRecord.getNomenclatureProfessionExperienceId() == null && StringUtils.isEmpty(experienceRecord.getProfessionExperienceTxt()), hideApplyButton);
            List<? extends ExtRegprofProfessionExperienceDocumentRecord> expDocuments = experienceRecord.getProfessionExperienceDocuments();
            if (expDocuments == null || expDocuments.size() == 0) {
                hideApplyButton = addSystemMessage(listSM, "Документи за стаж", true, hideApplyButton);
            } else {
                boolean isDocumentTypeError = false;
                boolean isDocumentIssuerError = false;
                for (ExtRegprofProfessionExperienceDocumentRecord r:expDocuments) {
                    isDocumentTypeError = isDocumentTypeError || r.getProfExperienceDocTypeId() == null;
                    isDocumentIssuerError = isDocumentIssuerError || StringUtils.isEmpty(r.getDocumentIssuer());
                    List<? extends ExtRegprofProfessionExperienceDatesRecord> dates = r.getDates();
                    if (dates == null || dates.size() == 0) {
                        hideApplyButton = addSystemMessage(listSM, "Работил за периода - от дата, до дата, работен ден", true, hideApplyButton);        
                    } else {
                        for (ExtRegprofProfessionExperienceDatesRecord d:dates) {
                            hideApplyButton = addSystemMessage(listSM, "Работил за периода - от дата, до дата, работен ден", d.getDateFrom() == null || d.getDateTo() == null || d.getWorkdayDuration() == null, hideApplyButton);    
                        }
                    }
                    
                }
                hideApplyButton = addSystemMessage(listSM, "Тип документ за стаж", isDocumentTypeError, hideApplyButton);
                hideApplyButton = addSystemMessage(listSM, "Издаваща институция на документ за стаж", isDocumentIssuerError, hideApplyButton);
            }
        }
        
        hideApplyButton = addSystemMessage(listSM, "Професионална квалификация, за която се иска издаването на удостоверение", tcDetails.getCertificateProfQualificationId() == null && StringUtils.isEmpty(tcDetails.getCertificateProfQualificationTxt()), hideApplyButton);

        ExtRegprofApplicationAttachmentDataProvider attDP = nacidDataProvider.getExtRegprofApplicationAttachmentDataProvider();
        Arrays.asList(DOC_TYPE_REGPROF_APPLICANT_DECLARATION_PROFESSION_PRACTICE_RIGHTS, DOC_TYPE_REGPROF_CERTIFICATE_APPLY_PROFESSIONAL_QUALIFICATION, DOC_TYPE_REGPROF_DIPLOMA).forEach(attachmentTypesToCheck::add);
        for (Integer attType : attachmentTypesToCheck) {
            List<Attachment> declarations = attDP.getAttachmentsForApplicationByType(details.getId(), attType);
            hideApplyButton = addSystemMessage(listSM, ExtApplicationsHandler.generateAttachmentLabel(nacidDataProvider, attType, null), Utils.isEmpty(declarations), hideApplyButton);
        }

        request.setAttribute("smList", listSM);
        request.setAttribute("hideApplyButton", hideApplyButton);
                
        if (!hideApplyButton) {

            Integer applId = rec.getApplicationDetails().getId();
            if (request.getSession().getAttribute("signedXml-" + applId) == null) {
                String docXml = extRegprofApplicationsDataProvider.getExtRegprofApplicationXml(applId);
                String successUrl = SignUtils.generateContextUrl(request) + "/control/successSign?activeForm=" + FORM_ID_APPLYING + "&id=" + applId;
                String cancelUrl = SignUtils.generateContextUrl(request) + "control/application/edit?activeForm=" + FORM_ID_APPLYING + "&id=" + + applId;
                UtilsDataProvider utilsDataProvider = nacidDataProvider.getUtilsDataProvider();


                SignatureParams signatureParams = new SignatureParams(docXml, utilsDataProvider.getCommonVariableValue(UtilsDataProvider.REGPROF_APPLICATION_TYPE) + " : " + applId, successUrl, cancelUrl);//TODO

                String signRequestUrl = utilsDataProvider.getCommonVariableValue("signatureRequestUrl");
                String signSecretKey = utilsDataProvider.getCommonVariableValue("signatureSecretKey");
                String requestJson = SignUtils.createRequestJson(signatureParams, signSecretKey);
                request.setAttribute("requestJson", requestJson);
                request.setAttribute("signRequestUrl", signRequestUrl);
                request.setAttribute("hideSignButton", false);
            } else {
                request.setAttribute("hideSignButton", true);
            }
            request.setAttribute("host", request.getRequestURL().toString().split(request.getContextPath())[0]);

            //generating serviceType's combobox
            NomenclaturesDataProvider nDP= getNacidDataProvider(request.getSession()).getNomenclaturesDataProvider();
            List<ServiceType> serviceTypes = nDP.getServiceTypes(new Date(), null, null);
            ComboBoxWebModel wm = new ComboBoxWebModel(rec.getApplicationDetails().getServiceTypeId() == null ? null : rec.getApplicationDetails().getServiceTypeId().toString(), true);
            for (ServiceType st:serviceTypes) {
                wm.addItem(st.getId() + "", st.getName() + " / " + st.getServicePrice().toPlainString() + " лв");
            }
            request.setAttribute("serviceType", wm);
            //end of generating serviceType's combo...

            ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_PAYMENT_TYPE, nDP, true, request, "paymentType", null, true);
        }
        
        return hideApplyButton;
    }

    
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        Table table = (Table) session.getAttribute(WebKeys.TABLE_EXTERNAL_REGPROF_APPLICATIONS);
        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_DOCFLOW_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_ESIGNED, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);
            session.setAttribute(WebKeys.TABLE_EXTERNAL_REGPROF_APPLICATIONS, table);
            resetTableData(request, response);
        }

        // TableState settings
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_EXTERNAL_REGPROF_APPLICATIONS + WebKeys.TABLE_STATE);

        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            session.setAttribute(WebKeys.TABLE_EXTERNAL_REGPROF_APPLICATIONS + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък със заявления");
        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_EXTERNAL_REGPROF_APPLICATIONS + WebKeys.FILTER_WEB_MODEL);
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            session.setAttribute(WebKeys.TABLE_EXTERNAL_REGPROF_APPLICATIONS + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }

    private void resetTableData(HttpServletRequest request, HttpServletResponse response) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_EXTERNAL_REGPROF_APPLICATIONS);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ExtRegprofApplicationsDataProvider extRegprofApplicationsDataProvider = nacidDataProvider.getExtRegprofApplicationsDataProvider();
        List<ExtRegprofApplicationForList> apps = extRegprofApplicationsDataProvider.getExtRegprofApplicationByExtPerson(getExtPerson(request, response).getId());

        if (apps != null) {
            for (ExtRegprofApplicationForList app : apps) {
                try {

                    TableRow row = table.addRow(app.getId(),
                            app.getDate(), app.getDocflowNumber() == null ? "" : app.getDocflowNumber(),
                            app.getApplicantName(),
                            app.getFinalStatusName() == null ? getAppStatusName(app.getStatusId()) : app.getFinalStatusName(), app.isEsigned());
                    row.setEditable(app.getStatusId() == ExtRegprofApplicationImpl.STATUS_EDITABLE);
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
    public static String getAppStatusName(Integer externalStatus) {

        if (externalStatus == null) {
            return "Неподадено";
        }
        switch (externalStatus) {
            case ExtRegprofApplicationImpl.STATUS_EDITABLE: return "Неподадено";
            case ExtRegprofApplicationImpl.STATUS_NOT_EDITABLE: return "Проверка";
            case ExtRegprofApplicationImpl.STATUS_TRANSFERED: return "В процес на обработка";
            case ExtRegprofApplicationImpl.STATUS_FINISHED: return "Приключено";
            default: throw new RuntimeException("Unknown externalStatus!" + externalStatus);
        }
    }
}
