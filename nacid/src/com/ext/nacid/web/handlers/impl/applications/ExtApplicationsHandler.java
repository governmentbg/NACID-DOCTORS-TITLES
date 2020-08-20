package com.ext.nacid.web.handlers.impl.applications;

import com.ext.nacid.web.handlers.MenuShowHandler;
import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.ext.nacid.web.handlers.UserAccessUtils;
import com.ext.nacid.web.model.applications.ExtApplicationHeaderWebModel;
import com.ext.nacid.web.model.applications.ExtApplicationWebModel;
import com.ext.nacid.web.model.applications.ExtTrainingCourseWebModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.external.ExtDocumentRecipient;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.external.ExtPersonDataProvider;
import com.nacid.bl.external.applications.*;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.external.ExtPersonImpl;
import com.nacid.bl.impl.external.applications.ExtUniversityIdWithFacultyId;
import com.nacid.bl.mail.MailDataProvider;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.signature.SignatureParams;
import com.nacid.bl.signature.SuccessSign;
import com.nacid.bl.table.*;
import com.nacid.bl.users.User;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.data.external.applications.ExtTrainingCourseSpecialityRecord;
import com.nacid.utils.SignUtils;
import com.nacid.web.MessagesBundle;
import com.nacid.web.ValidationStrings;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.MenuUtils;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.UserOperationsUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.poi.hssf.record.formula.functions.Count;
import org.hibernate.validator.constraints.impl.URLValidator;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ext.nacid.regprof.web.handlers.impl.applications.ExtRegprofApplicationsHandler.addSystemMessage;
import static com.nacid.bl.nomenclatures.ApplicationType.DOCTORATE_APPLICATION_TYPE;
import static com.nacid.bl.nomenclatures.ApplicationType.RUDI_APPLICATION_TYPE;
import static com.nacid.bl.nomenclatures.DocumentType.*;
import static com.nacid.bl.nomenclatures.NomenclaturesDataProvider.*;
import static com.nacid.web.handlers.impl.applications.ApplicationsHandler.addFlatNomenclaturesToWebModel;

//import com.nacid.bl.external.users.ExtUser;

public class ExtApplicationsHandler extends NacidExtBaseRequestHandler {

    public static final int FORM_ID_APPLICATION_DATA = 1;
    public static final int FORM_ID_TRAINING_DATA = 2;
    public static final int FORM_ID_ATTACHMENTS_DATA = 3;
    public static final int FORM_ID_APPLYING = 4;
    public static final int FORM_ID_EXTERNAL_APPLICATION_REPORT = 5;
    public static final int FORM_ID_INTERNAL_APPLICATION_REPORT = 6;
    

    private final static String COLUMN_NAME_ID = "id";
    private final static String COLUMN_NAME_NAME = "Име";
    private final static String COLUMN_NAME_DATE = "Дата на въвеждане";
    private final static String COLUMN_NAME_DOCFLOW_NUMBER = "Деловоден номер";
    private final static String COLUMN_NAME_STATUS = "Статус";
    private final static String COLUMN_NAME_ESIGNED = "Ел. подп.";
    
    private final static int MAX_SPECIALITIES_COUNT = 10;

    public ExtApplicationsHandler(ServletContext servletContext) {
        super(servletContext);
    }

    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        User user = getLoggedUser(request, response);
        ExtPersonDataProvider epdp = nacidDataProvider.getExtPersonDataProvider();
        ExtPerson person = epdp.getExtPersonByUserId(user.getUserId());
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        List<Country> countries = nomenclaturesDataProvider.getCountries(null, null);
        ComboBoxUtils.generateNomenclaturesComboBox(Country.COUNTRY_ID_BULGARIA, countries, true, request, "homeCountry", false);
        Integer applicationType = DataConverter.parseInteger(request.getParameter("application_type"), null);
        request.setAttribute(WebKeys.APPLICATION_WEB_MODEL, new ExtApplicationWebModel(person, FORM_ID_APPLICATION_DATA, applicationType));

        setNextScreen(request, "application_edit");
        initApplyingTab(request, null);
        request.setAttribute(WebKeys.APPLICATION_HEADER, new ExtApplicationHeaderWebModel( getAppStatusName(ExtApplication.STATUS_EDITABLE, null), null));
        ComboBoxUtils.generateNomenclaturesRadioButton(CivilIdType.CIVIL_ID_TYPE_EGN, nomenclaturesDataProvider.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, null, null), true, request, "applicantCivilIdType");
        ComboBoxUtils.generateNomenclaturesRadioButton(CivilIdType.CIVIL_ID_TYPE_EGN, nomenclaturesDataProvider.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, null, null), true, request, "representativeCivilIdType");
        ComboBoxUtils.generateDocumentReceiveMethodRadioButton(null, nomenclaturesDataProvider.getDocumentReceiveMethods(null, null), true, request, "documentReceiveMethod");
        ComboBoxUtils.generateNomenclaturesComboBox(null, countries, true, request, "recipientCountryCombo", true);

        ComboBoxUtils.generateNomenclaturesComboBox(Country.COUNTRY_ID_BULGARIA, countries, true, request, "applicantBirthCountry", true);
        ComboBoxUtils.generateNomenclaturesComboBox(getExtPerson(request, response).getBirthCountryId(), countries, true, request, "representativeBirthCountry", true);

        ComboBoxUtils.generateNomenclaturesComboBox(person.getCitizenshipId(), countries, true, request, "representativeCitizenship", true);
        ComboBoxUtils.generateNomenclaturesComboBox(null, countries, true, request, "applicantCitizenship", true);

        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_PERSONAL_ID_DOCUMENT_TYPE, nomenclaturesDataProvider, true, request, "applicantPersonalIdDocumentType", null, true);
        request.setAttribute("dataUsageDeclarationUrl", nacidDataProvider.getNomenclaturesDataProvider().getDocumentType(DOC_TYPE_APPLICANT_DECLARATION_DATA_USAGE).getDocumentUrl());
//        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_PERSONAL_ID_DOCUMENT_TYPE, nDP, true, request, "applicantPersonalIdDocumentType", null, true);
    }
    private boolean hasDissertationGraduationWay(List<Integer> graduationWayIds) {
        return graduationWayIds != null && graduationWayIds.stream().filter(r -> Objects.equals(r, GraduationWay.GRADUATION_WAY_DISSERTATION)).findFirst().isPresent();
    }

    //tip na attachmenttite, koito shte se tyrsqt. Za nqkoi ot attachmentite shte se tyrsi ednovremenno i original i prevod (primerno za diplomata), zatova za tqh se podavat i copyTypes
    class AttachmentTypeToCheck {
        private int attTypeId;
        private Integer[] copyTypeId;

        public AttachmentTypeToCheck(int attTypeId, Integer... copyTypeId) {
            this.attTypeId = attTypeId;
            this.copyTypeId = copyTypeId;
        }
    }
    private void initApplyingTab(HttpServletRequest request, ExtApplication appl) {
        int applicationType = appl == null ? DataConverter.parseInt(request.getParameter("application_type"), 0) : appl.getApplicationType();
        boolean isDoctorateApplication = applicationType == DOCTORATE_APPLICATION_TYPE;
        boolean hideApplyButton = false;
        
        ExtTrainingCourse tc = appl != null ? appl.getTrainingCourse() : null;
        
        
        List<SystemMessageWebModel> listSM = new ArrayList<>();

        List<AttachmentTypeToCheck> attachmentTypesToCheck = new ArrayList<>();

        hideApplyButton = addSystemMessage(listSM, "Съгласен съм да се ползват лични данни за целите на проверката", appl == null || !appl.isPersonalDataUsage(), hideApplyButton);
        hideApplyButton = addSystemMessage(listSM, MessagesBundle.getMessagesBundle().getValue(isDoctorateApplication ? "dataAuthenticDeclarationDoctorate" : "dataAuthenticDeclaration"), appl == null || appl.getDataAuthentic() == null || !appl.getDataAuthentic(), hideApplyButton);

        hideApplyButton = addDocumentReceiveMethodSystemMessage(listSM, appl, hideApplyButton);
        if (appl != null && appl.getDocumentReceiveMethodId() != null) {
            DocumentReceiveMethod drm = getNacidDataProvider().getNomenclaturesDataProvider().getDocumentReceiveMethod(appl.getDocumentReceiveMethodId());
            if (drm.isEservicesRequirePaymentReceipt()) {
                attachmentTypesToCheck.add(new ExtApplicationsHandler.AttachmentTypeToCheck(DOC_TYPE_PAYMENT_ORDER_COURIER_RUDI));
            }
        }


        ExtUniversityWithFaculty uf = tc == null ? null : tc.getBaseUniversityWithFaculty();
        ExtUniversity baseUniversity = uf == null ? null : uf.getUniversity();

        hideApplyButton = addSystemMessage(listSM, isDoctorateApplication ? "Име на висшето училище или научната организация" : "Име на висшето училище", tc == null || (baseUniversity == null) || (!baseUniversity.isStandartUniversity() && StringUtils.isEmpty(baseUniversity.getUniversityTxt())), hideApplyButton);
        List<? extends ExtTrainingCourseTrainingLocation> tlocs = tc == null ? null : tc.getTrainingCourseTrainingLocations();
        hideApplyButton = addSystemMessage(listSM, "Място (държава и град), където се е провело обучението", tc == null || tlocs == null || tlocs.get(0).getTrainingLocationCountryId() == null || Utils.isEmptyString(tlocs.get(0).getTrainingLocationCity()), hideApplyButton);

        if (applicationType == RUDI_APPLICATION_TYPE) {
            hideApplyButton = addSystemMessage(listSM, "Специалност", tc == null || CollectionUtils.isEmpty(tc.getTrainingCourseSpecialities()), hideApplyButton);
        }
        if (appl != null && tc != null && !Objects.equals(tc.getEducationLevelId(), EducationLevel.EDUCATION_LEVEL_DOCTOR_OF_SCIENCE)) {//pri doctor of science poletata ne sa zadylvitelni
            hideApplyButton = addSystemMessage(listSM, "Начало на обучението", tc == null || tc.getTrainingStart() == null, hideApplyButton);
            hideApplyButton = addSystemMessage(listSM, "Край на обучението", tc == null || tc.getTrainingEnd() == null, hideApplyButton);
        }

        hideApplyButton = addSystemMessage(listSM, "Получена степен", tc == null || tc.getEducationLevelId() == null, hideApplyButton);
        if (appl != null && isDoctorateApplication) {
            List<ExtGraduationWay> gws = tc == null ? null : getNacidDataProvider().getExtTrainingCourseDataProvider().getExtGraduationWays(tc.getId());
            hideApplyButton = addSystemMessage(listSM, "Начин на придобиване на степента", CollectionUtils.isEmpty(gws), hideApplyButton);
            hideApplyButton = addSystemMessage(listSM, "Професионално направление по заявление", tc == null || tc.getProfGroupId() == null, hideApplyButton);

            boolean hasDissertationGraduationWay = gws != null && hasDissertationGraduationWay(gws.stream().map(r -> r.getGraduationWayId()).collect(Collectors.toList()));
            if (hasDissertationGraduationWay) {
                hideApplyButton = validateDissertationSection(listSM, tc, hideApplyButton);
                attachmentTypesToCheck.add(new AttachmentTypeToCheck(DOC_TYPE_DISSERTATION_WORK, null));
//                attachmentTypesToCheck.add(new AttachmentTypeToCheck(DOC_TYPE_ABSTRACT, null));
            } else {
                attachmentTypesToCheck.add(new AttachmentTypeToCheck(DOC_TYPE_OTHER_DOCTORATE_ATTACHMENTS));
            }
        }

        ExtApplicationAttachmentDataProvider attDP = getNacidDataProvider().getExtApplicationAttachmentDataProvider();

        Stream.of(new AttachmentTypeToCheck(DOC_TYPE_APPLICANT_DECLARATION_DATA_USAGE)).forEach(attachmentTypesToCheck::add);
        if (appl != null && appl.getApplicationType() == RUDI_APPLICATION_TYPE) {
            attachmentTypesToCheck.add(new AttachmentTypeToCheck(DOC_TYPE_DIPLOMA_UNIVERSITY, CopyType.COPY_TYPE_ORIGINAL_DOC, CopyType.COPY_TYPE_TRANSLATED_DOC));
            attachmentTypesToCheck.add(new AttachmentTypeToCheck(DOC_TYPE_DIPLOMA_ATTACHMENTS, CopyType.COPY_TYPE_ORIGINAL_DOC, CopyType.COPY_TYPE_TRANSLATED_DOC));
        } else if (appl != null && appl.getApplicationType() == DOCTORATE_APPLICATION_TYPE) {
            attachmentTypesToCheck.add(new AttachmentTypeToCheck(DOC_TYPE_DIPLOMA_DOCTORATE, CopyType.COPY_TYPE_ORIGINAL_DOC, CopyType.COPY_TYPE_TRANSLATED_DOC));
        }
        for (AttachmentTypeToCheck attType : attachmentTypesToCheck) {
            List<Attachment> declarations = appl == null ? null : attDP.getAttachmentsForApplicationByType(appl.getId(), attType.attTypeId);
            if (attType.copyTypeId != null && attType.copyTypeId.length > 0) {
                for (Integer copyType : attType.copyTypeId) {
                    hideApplyButton = addSystemMessage(listSM, generateAttachmentLabel(getNacidDataProvider(), attType.attTypeId, copyType), Utils.isEmpty(declarations) || declarations.stream().noneMatch(d -> Objects.equals(d.getCopyTypeId(), copyType)), hideApplyButton);
                }
            } else {
                hideApplyButton = addSystemMessage(listSM, generateAttachmentLabel(getNacidDataProvider(), attType.attTypeId, null), Utils.isEmpty(declarations), hideApplyButton);
            }
        }
        
        if (applicationType == ApplicationType.RUDI_APPLICATION_TYPE) {
            hideApplyButton = addSystemMessage(listSM, "Държава на средното училище" , tc == null || tc.getSchoolCountryId() == null, hideApplyButton);
        }

        
        request.setAttribute("smList", listSM);
        request.setAttribute("hideApplyButton", hideApplyButton);
        if (!hideApplyButton) {
            if (request.getSession().getAttribute("signedXml-" + appl.getId()) == null) {
                String docXml = getNacidDataProvider().getExtApplicationsDataProvider().getApplicationXml(appl.getId());
                String successUrl = SignUtils.generateContextUrl(request) + "/control/successSign?activeForm=" + FORM_ID_APPLYING + "&id=" + appl.getId();
                String cancelUrl = SignUtils.generateContextUrl(request) + "/control/application/edit?activeForm=" + FORM_ID_APPLYING + "&id=" + + appl.getId();
                UtilsDataProvider utilsDataProvider = getNacidDataProvider().getUtilsDataProvider();
                SignatureParams signatureParams = new SignatureParams(docXml, utilsDataProvider.getCommonVariableValue(UtilsDataProvider.RUDI_APPLICATION_TYPE) + " : " + appl.getId(), successUrl, cancelUrl);//TODO

                String signRequestUrl = utilsDataProvider.getCommonVariableValue("signatureRequestUrl");
                String signSecretKey = utilsDataProvider.getCommonVariableValue("signatureSecretKey");
                String requestJson = SignUtils.createRequestJson(signatureParams, signSecretKey);
                request.setAttribute("requestJson", requestJson);
                request.setAttribute("signRequestUrl", signRequestUrl);
                request.setAttribute("hideSignButton", false);
            } else {
                request.setAttribute("hideSignButton", true);
            }

        }
    }

    public static final String generateAttachmentLabel(NacidDataProvider nacidDataProvider, int attType, Integer copyType) {
        DocumentType docType = nacidDataProvider.getNomenclaturesDataProvider().getDocumentType(attType);
        String ct = copyType == null ? "" :  " - Форма:" + nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(FLAT_NOMENCLATURE_COPY_TYPE, copyType).getName();
        String res = "Прикачен документ: " + docType.getName() + ct;
        String url = docType.getDocumentUrl();
        if (!StringUtils.isEmpty(url)) {
            res += String.format("&nbsp;&nbsp;&nbsp;<a href=\"%s\" target=\"_blank\">Свали бланка</a>", url);
        }
        return res;
    }
    private boolean validateDissertationAttribute(SystemMessageWebModel sm, String title, boolean hasError, boolean errors) {
        if (hasError) {
            sm.addAttribute(title);
        }
        return errors || hasError;
    }
    private boolean validateDissertationSection(List<SystemMessageWebModel> webModel, ExtTrainingCourse tc, boolean hideApplyButton) {
        SystemMessageWebModel sm = new SystemMessageWebModel("Дисертация", SystemMessageWebModel.MESSAGE_TYPE_CORRECT);
        boolean hasError = false;
        hasError = validateDissertationAttribute(sm, "Тема", tc == null || StringUtils.isEmpty(tc.getThesisTopic()), hasError);
        hasError = validateDissertationAttribute(sm, "Тема на английски", tc == null || StringUtils.isEmpty(tc.getThesisTopicEn()), hasError);
        hasError = validateDissertationAttribute(sm, "Дата на защитата", tc == null || tc.getThesisDefenceDate() == null, hasError);
        hasError = validateDissertationAttribute(sm, "Език на основния текст", tc == null || tc.getThesisLanguageId() == null, hasError);
        hasError = validateDissertationAttribute(sm, "Библиография(бр. заглавия)", tc == null || tc.getThesisBibliography() == null, hasError);
        hasError = validateDissertationAttribute(sm, "Обем на дисертационния труд(бр. страници)", tc == null || tc.getThesisVolume() == null, hasError);
        hasError = validateDissertationAttribute(sm, "Анотация на български език", tc == null || StringUtils.isEmpty(tc.getThesisAnnotation()), hasError);
        hasError = validateDissertationAttribute(sm, "Анотация на английски език", tc == null || StringUtils.isEmpty(tc.getThesisAnnotationEn()), hasError);
        if (hasError) {
            sm.setMessageType(SystemMessageWebModel.MESSAGE_TYPE_ERROR);
        }
        webModel.add(sm);
        return hideApplyButton || hasError;
    }

    public static boolean addDocumentReceiveMethodSystemMessage(List<SystemMessageWebModel> webModel, ExtApplication appl, boolean hideApplyButton) {
        SystemMessageWebModel sm = new SystemMessageWebModel("Начин на получаване на уведомления", SystemMessageWebModel.MESSAGE_TYPE_CORRECT);
        boolean hasError = false;
        if (appl == null || appl.getDocumentReceiveMethod() == null) {
//            sm.setMessageType(SystemMessageWebModel.MESSAGE_TYPE_ERROR);
//            hideApplyButton = hideApplyButton || true;
            hasError = true;
        } else if (appl != null && appl.getDocumentReceiveMethod() != null && appl.getDocumentReceiveMethod().hasDocumentRecipient()) {
            ExtDocumentRecipient dr = appl.getDocumentRecipient();

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
        return hideApplyButton || hasError;
    }

    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        int activeFormId = DataConverter.parseInt(request.getParameter("activeForm"), FORM_ID_APPLICATION_DATA);

        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        ExtApplicationsDataProvider extApplicationsDataProvider = nacidDataProvider.getExtApplicationsDataProvider();
        ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
        User user = getLoggedUser(request, response);
        ExtPerson extPerson = getExtPerson(request, response);
        Integer applicationId = DataConverter.parseInteger(request.getParameter("id"), null);
        ExtApplication extApplication = applicationId == null ? null : extApplicationsDataProvider.getApplication(applicationId);
        if (extApplication == null) {
            throw new UnknownRecordException("Unknown application ID + " + request.getParameter("id"));
        }
        // Tyi kato tozi method se vika i pri edit i pri view, - pri edit se
        // proverqva dali user-a ima prava za edit, a pri view - prava za view
        if (UserOperationsUtils.getOperationId(getOperationName(request)) == UserOperationsUtils.OPERATION_LEVEL_EDIT) {
            UserAccessUtils.checkApplicantActionAccess(UserAccessUtils.USER_ACTION_CHANGE, user, extApplication, nacidDataProvider);
        } else {
            UserAccessUtils.checkApplicantActionAccess(UserAccessUtils.USER_ACTION_VIEW, user, extApplication, nacidDataProvider);
        }

        addApplicationToWebModel(extApplication, request, nacidDataProvider, activeFormId);
        new ExtApplicationAttachmentHandler(getServletContext()).handleList(request, response);
        Application intApplication = extApplication.getInternalApplicationId() == null ? null : applicationsDataProvider.getApplication(extApplication.getInternalApplicationId());
        request.setAttribute(WebKeys.APPLICATION_HEADER, new ExtApplicationHeaderWebModel(getAppStatusName(extApplication.getApplicationStatus(), intApplication), intApplication));
        
        //Generirane na taba s report-a (informaciqta) za vytre6noto zaqvlenie, kakto i taba sys zaqvlenieto ot vyn6noto prilojenie
        //tezi 2 taba se pokazvat samo kogato zaqvlenieto e prehvyrleno vyv vytre6nata baza!
        if (extApplication.getInternalApplicationId() != null) {
        	ApplicantReportHandler.generateExternalApplicantReport(nacidDataProvider, extApplication, request, "ext");
        	ExpertReportHandler.prepareExpertReport(request, response, nacidDataProvider, extApplication.getInternalApplicationId());
        	((ExtApplicationWebModel)request.getAttribute(WebKeys.APPLICATION_WEB_MODEL)).setActiveFormId(FORM_ID_EXTERNAL_APPLICATION_REPORT);
        }

        ComboBoxUtils.generateNomenclaturesRadioButton(extPerson.getCivilIdTypeId(), nomenclaturesDataProvider.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, null, null), true, request, "representativeCivilIdType");
        ComboBoxUtils.generateNomenclaturesRadioButton(extApplication.getApplicant().getCivilIdTypeId(), nomenclaturesDataProvider.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, null, null), true, request, "applicantCivilIdType");


        ComboBoxUtils.generateDocumentReceiveMethodRadioButton(extApplication.getDocumentReceiveMethodId(), nomenclaturesDataProvider.getDocumentReceiveMethods( null, null), true, request, "documentReceiveMethod");
        ComboBoxUtils.generateNomenclaturesComboBox(extApplication.getTrainingCourse().getThesisLanguageId(), nomenclaturesDataProvider.getLanguages(null, null), true, request, "thesisLanguageCombo", true);

        List<Country> countries = nomenclaturesDataProvider.getCountries(null, null);
        ComboBoxUtils.generateNomenclaturesComboBox(extPerson.getCitizenshipId(), countries, true, request, "representativeCitizenship", true);
        ComboBoxUtils.generateNomenclaturesComboBox(extApplication.getApplicant().getCitizenshipId(), countries, true, request, "applicantCitizenship", true);

        ComboBoxUtils.generateNomenclaturesComboBox(extApplication.getApplicantPersonalIdDocumentTypeId(), NomenclaturesDataProvider.FLAT_NOMENCLATURE_PERSONAL_ID_DOCUMENT_TYPE, nomenclaturesDataProvider, true, request, "applicantPersonalIdDocumentType", null, true);
        request.setAttribute("dataUsageDeclarationUrl", nomenclaturesDataProvider.getDocumentType(DOC_TYPE_APPLICANT_DECLARATION_DATA_USAGE).getDocumentUrl());
        setNextScreen(request, "application_edit");
    }

    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        int activeFormId = DataConverter.parseInt(request.getParameter("activeForm"), -1);

        switch (activeFormId) {
        case FORM_ID_APPLICATION_DATA:
            saveApplicationData(request, response);
            break;
        case FORM_ID_TRAINING_DATA:
            saveTrainingData(request, response);
            break;
        case FORM_ID_APPLYING:
            saveApply(request, response);
            break;
        default:
            throw Utils.logException(new Exception("Unknown active form"));
        }
    }
    
    private void saveApply(HttpServletRequest request, HttpServletResponse response) {
     
        int applicationId = DataConverter.parseInt(request.getParameter("applicationId"), 0);
        if (applicationId == 0) {
            throw new UnknownRecordException("unknown application id: " + applicationId);
        }

        UserAccessUtils.checkApplicantActionAccess(UserAccessUtils.USER_ACTION_CHANGE, getLoggedUser(request, response), applicationId,
                getNacidDataProvider());

        ExtApplicationsDataProvider eaDP = getNacidDataProvider().getExtApplicationsDataProvider();
        
        ExtApplication appl = eaDP.getApplication(applicationId);
        
        
        //Zapisvaneto na signed xml-a trqbva da stane zadyljitelno predi zapisvaneto na novite danni za application-a v bazata,
        //zashtoto sled prezapisvaneto na application-a, toi shte ima nov status i nov timeOfCreation!!!
        SuccessSign signedXml = (SuccessSign) request.getSession().getAttribute("signedXml-" + applicationId);
        //ExtESignedInformation esignedInformation = eaDP.getApplication(18).getESignedInformation();
        //String signedXml = esignedInformation.getSignedXmlContent();
        if (signedXml != null && !StringUtils.isEmpty(signedXml.getXmlSigned())) {
        	try {
				eaDP.saveSignedApplicationXml(getLoggedUser(request, response).getUserId(), applicationId, signedXml);
			} catch (SignedXmlException e) {
				addSystemMessageToSession(request, "applyingStatusMessage", new SystemMessageWebModel(e.getMessage(), SystemMessageWebModel.MESSAGE_TYPE_ERROR));
				try {
		            response.sendRedirect(request.getContextPath() + "/control/application/"
		                    + "edit"
		                    + "?id=" + applicationId + "&activeForm=" + FORM_ID_APPLYING + "&showtab=1");
		        } catch (IOException e1) {
		            throw Utils.logException(e1);
		        }
				return;
			}	
        }
        
        Date timeOfCreation = new Date();
        eaDP.saveApplication(appl.getId(), 
                appl.getApplicantId(),
                appl.getApplicantCompanyId(),
                appl.differentApplicantAndDiplomaNames(), 
                appl.getTrainingCourseId(), 
                appl.getHomeCountryId(), appl.getHomeCity(), 
                appl.getHomePostCode(), appl.getHomeAddressDetails(), 
                appl.getHomePhone(), appl.homeIsBg(), 
                appl.getBgCity(), appl.getBgPostCode(), appl.getBgAddressDetails(), 
                appl.getBgPhone(), timeOfCreation, /*appl.getTimeOfCreation(),*/ 
                appl.getSummary(), 
                ExtApplication.STATUS_NOT_EDITABLE, null, appl.isPersonalDataUsage(), appl.getDataAuthentic(), appl.getApplicantType(),
                appl.getDeputy(), appl.getRepresentativeId(), appl.getRepresentativeType(), appl.getContactDetailsId(),
                appl.getTypePayment(), appl.getDeliveryType(), appl.getDeclaration(), appl.getCourierNameAddress(), appl.getOutgoingNumber(),
                appl.getInternalNumber(), appl.getIsExpress(), appl.getDocFlowNumber(), appl.getApplicationType(),
                appl.getDocumentReceiveMethodId(),
                appl.getApplicantPersonalIdDocumentTypeId());
        
        
        
        
        request.getSession().removeAttribute(getTableAttribute(appl.getApplicationType()));

        String statusMessage = MessagesBundle.getMessagesBundle().getValue("applicationAppliedMessage");
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("dateSubmitted", DataConverter.formatDateTime(timeOfCreation, false));
        statusMessage = StrSubstitutor.replace(statusMessage, placeholders);
        addSystemMessageToSession(request, "applyingStatusMessage", new SystemMessageWebModel(statusMessage, SystemMessageWebModel.MESSAGE_TYPE_CORRECT));


        UtilsDataProvider utilsDataProvider = getNacidDataProvider().getUtilsDataProvider();
        String msg = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.APPLICATION_APPLIED_MESSAGE);
        msg = MessageFormat.format(msg, DataConverter.formatDateTime(timeOfCreation, false));

        MailDataProvider mailDataProvider = getNacidDataProvider().getMailDataProvider();
        User user = getLoggedUser(request, response);
        ExtPersonDataProvider epdp = getNacidDataProvider().getExtPersonDataProvider();
        ExtPerson person = epdp.getExtPersonByUserId(user.getUserId());
        String sender = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.MAIL_SENDER);
        String subject = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.APPLICATION_APPLIED_SUBJECT);
        Integer messageId = mailDataProvider.sendMessage(sender, sender, person.getFullName(), person.getEmail(), subject, msg);
        eaDP.saveApplicationComment(applicationId, "Subject:"  + subject + "\nBody:" + msg, true, messageId, true, user.getUserId());

        try {
            response.sendRedirect(request.getContextPath() + "/control/application/"
                    + "view"
                    + "?id=" + applicationId + "&activeForm=" + FORM_ID_APPLYING);
            
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }

    private void saveTrainingData(HttpServletRequest request, HttpServletResponse response) {
        int applicationId = DataConverter.parseInt(request.getParameter("applicationId"), 0);
        if (applicationId == 0) {
            throw new UnknownRecordException("unknown application id: " + applicationId);
        }
        ExtApplicationsDataProvider eaDP = getNacidDataProvider().getExtApplicationsDataProvider();
        ExtTrainingCourseDataProvider etcDP = getNacidDataProvider().getExtTrainingCourseDataProvider();
        ExtApplication appl = eaDP.getApplication(applicationId);
        if (appl == null) {
            throw Utils.logException(new UnknownRecordException("no application with id " + applicationId));
        }
        int applicationType = appl.getApplicationType();

        Integer eduLevel = DataConverter.parseInteger(request.getParameter("eduLevel"), null);

        boolean hasTrainingPeriodInformation = !Objects.equals(eduLevel, EducationLevel.EDUCATION_LEVEL_DOCTOR_OF_SCIENCE);

        UserAccessUtils.checkApplicantActionAccess(UserAccessUtils.USER_ACTION_CHANGE, getLoggedUser(request, response), applicationId,
                getNacidDataProvider());

        List<ExtUniversityIdWithFacultyId> universities = new ArrayList<>();
        int unisCount = DataConverter.parseInt(request.getParameter("universities_count"), 0);
        for (int i = 0; i < unisCount; i++) {
        	Integer universityId = DataConverter.parseInteger(request.getParameter("universityId" + i), null);
        	String universityName = null;
            Integer facultyId = null;
            String facultyName = null;
            if (universityId == null) {
                universityName = DataConverter.parseString(request.getParameter("universityName" + i), null);
                facultyName = DataConverter.parseString(request.getParameter("facultyName" + i), null);
            } else {
                facultyId = DataConverter.parseInteger(request.getParameter("facultyId" + i), null);
                if (facultyId == null) {
                    facultyName = DataConverter.parseString(request.getParameter("facultyName" + i), null);
                }
            }
            if (universityId != null || universityName != null) {
            	universities.add(new ExtUniversityIdWithFacultyId(universityId, universityName, facultyId, facultyName));
            }
        }
        boolean jointDegree = universities.size() > 1;
        String diplomaSeries = request.getParameter("diplomaSeries");
        String diplomaNumber = request.getParameter("diplomaNumber");
        String diplomaRegistrationNumber = request.getParameter("diplomaRegistrationNumber");
        Date diplomaDate = DataConverter.parseDate(request.getParameter("diplomaDate"));
        //Integer trainingCountryId = DataConverter.parseInteger(request.getParameter("trainingCountryId"), null);
        //String trainingLocationCity = request.getParameter("trainingLocationCity");

        /*Integer trainingSpecialityId = DataConverter.parseInteger(request.getParameter("trainingSpecialityId"), null);
        String trainingSpecialityTxt = null;
        if (trainingSpecialityId == null) {
            trainingSpecialityTxt = request.getParameter("trainingSpecialityTxt");
        }*/

        Date trainingStart = DataConverter.parseYear(request.getParameter("trainingStart"));
        Date trainingEnd = DataConverter.parseYear(request.getParameter("trainingEnd"));
        Double trainingDuration = !hasTrainingPeriodInformation ? null : DataConverter.parseDouble(request.getParameter("trainingDuration"), null);
        Integer trainingDurationUnitId = !hasTrainingPeriodInformation ? null : DataConverter.parseInteger(request.getParameter("trainingDurationUnitId"), null);

        Integer trainingForm = !hasTrainingPeriodInformation ? null : DataConverter.parseInteger(request.getParameter("training_form"), null);
        String trainingFormOther = null;
        if (trainingForm == null) {
            trainingFormOther = !hasTrainingPeriodInformation ? null : DataConverter.parseString(request.getParameter("training_form_other"), null);
        }

        String[] graduationWayIds = request.getParameterValues("graduation_way");
        List<Integer> graduationWays = null;
        String graduationWayOther = null;

        if (graduationWayIds != null) {
            graduationWays = new ArrayList<Integer>();
            for (String s : graduationWayIds) {
                Integer i = DataConverter.parseInteger(s, null);
                if (i != null) {
                    graduationWays.add(i);
                } else {
                    graduationWayOther = request.getParameter("graduation_way_other");
                }
            }
        }
        
        int trainingLocationsCount = DataConverter.parseInt(request.getParameter("training_locations_count"), 0);
        class TrainingLocations {
        	int countryId;
        	String city;
        	public TrainingLocations(int countryId, String city) {
        		this.countryId = countryId;
        		this.city = city;
			}
        }
        List<TrainingLocations> trainingLocations = null;
        if (trainingLocationsCount > 0 ) {
        	trainingLocations = new ArrayList<TrainingLocations>();
        	for (int i = 0; i < trainingLocationsCount; i++) {
            	Integer countryId = DataConverter.parseInteger(request.getParameter("training_country" + i), null);
            	String city = request.getParameter("training_city" + i);
            	if (countryId != null) {
            		trainingLocations.add(new TrainingLocations(countryId, city));	
            	}
            	
            }	
        }
        
        BigDecimal credits = !hasTrainingPeriodInformation ? null : DataConverter.parseBigDecimal(request.getParameter("credits"), null);


        Optional<NomenclatureIdAndText> qualification = generateNomenclatureIdAndText("qualificationId", "qualification", request, FLAT_NOMENCLATURE_QUALIFICATION, getNacidDataProvider().getNomenclaturesDataProvider());
        Integer qualificationId = qualification.isPresent() ? qualification.get().getId() : null;
        String qualificationText = qualification.isPresent() ? qualification.get().getText() : null;

        Integer schoolCountryId = DataConverter.parseInteger(request.getParameter("schoolCountryId"), null);
        String schoolCity = request.getParameter("schoolCity");
        String schoolName = request.getParameter("schoolName");
        Date schoolGraduationDate = DataConverter.parseYear(request.getParameter("schoolGraduationDate"));
        String schoolNotes = request.getParameter("schoolNotes");

        Integer prevDiplUniversityId = DataConverter.parseInteger(request.getParameter("prevDiplUniversityId"), null);
        String prevDiplName = null;
        if (prevDiplUniversityId == null) {
            prevDiplName = request.getParameter("prevDiplName");
        }

        Integer prevDiplEduLevelId = DataConverter.parseInteger(request.getParameter("prevDiplEduLevelId"), null);
        Date prevDiplGraduationDate = DataConverter.parseYear(request.getParameter("prevDiplGraduationDate"));
        String prevDiplNotes = request.getParameter("prevDiplNotes");
        Optional<NomenclatureIdAndText> prevDiplSpeciality = generateNomenclatureIdAndText("prevDiplomaSpecialityId", "prevDiplomaSpeciality", request, NOMENCLATURE_SPECIALITY, getNacidDataProvider().getNomenclaturesDataProvider());
        Integer prevDiplomaSpecialityId = prevDiplSpeciality.isPresent() ? prevDiplSpeciality.get().getId() : null;
        String prevDiplomaSpecialityTxt = prevDiplSpeciality.isPresent() ? prevDiplSpeciality.get().getText() : null;

        String[] purposeOfRecognitionIds = request.getParameterValues("purposeOfRecognition");
        List<Integer> purposesOfRecognition = null;
        String purposeOfRecognitionOther = null;

        if (purposeOfRecognitionIds != null) {
            purposesOfRecognition = new ArrayList<Integer>();
            for (String s : purposeOfRecognitionIds) {
                Integer i = DataConverter.parseInteger(s, null);
                if (i == null) {
                    purposeOfRecognitionOther = request.getParameter("purposeOfRecognitionOther");
                } else {
                    purposesOfRecognition.add(i);
                }
            }
        }
        



        int trainingCourseId = appl.getTrainingCourseId();
        ExtTrainingCourse oldTC = appl.getTrainingCourse();
        Integer graduationDocumentTypeId = DataConverter.parseInteger(request.getParameter("graduationDocumentTypeId"), null);//TODO: Da se sloji takova pole pri vyvejdane na trainingCourse vyv vyn6noto prilojenie!!!
        Integer creditHours = DataConverter.parseInteger(request.getParameter("creditHours"), null);//TODO:Da se sloji vyv formata za popylvane!
        Integer ectsCredits = DataConverter.parseInteger(request.getParameter("ectsCredits"), null);//TODO:Da se sloji vyv formata za popylvane!

        boolean hasDissertationGraduationWay = applicationType == DOCTORATE_APPLICATION_TYPE && hasDissertationGraduationWay(graduationWays);
        String thesisTopic = hasDissertationGraduationWay ? DataConverter.parseString(request.getParameter("thesis_topic"), null) : null;
        String thesisTopicEn = hasDissertationGraduationWay ? DataConverter.parseString(request.getParameter("thesis_topic_en"), null) : null;
        Date thesisDefenceDate = hasDissertationGraduationWay ? DataConverter.parseDate(request.getParameter("thesis_defence_date")) : null;
        Integer thesisBibliography = hasDissertationGraduationWay ? DataConverter.parseInteger(request.getParameter("thesis_bibliography"), null) : null;
        Integer thesisVolume = hasDissertationGraduationWay ? DataConverter.parseInteger(request.getParameter("thesis_volume"), null) : null;
        Integer thesisLanguage = hasDissertationGraduationWay ? DataConverter.parseInteger(request.getParameter("thesis_language"), null) : null;
        String thesisAnnotation = hasDissertationGraduationWay ? DataConverter.parseString(request.getParameter("thesis_annotation"), null) : null;
        String thesisAnnotationEn = hasDissertationGraduationWay ? DataConverter.parseString(request.getParameter("thesis_annotation_en"), null) : null;

        Integer profGroup = applicationType == DOCTORATE_APPLICATION_TYPE ? DataConverter.parseInteger(request.getParameter("prof_group"), null) : null;

//        ExtPerson userPerson = getExtPerson(request, response);
        etcDP.saveExtTrainingCourse(trainingCourseId, diplomaSeries, diplomaNumber, diplomaRegistrationNumber, diplomaDate, /*universityId, universityName,*/ oldTC.getFName(), oldTC.getSName(),
                oldTC.getLName(), /*trainingCountryId, trainingLocationCity,*/ jointDegree, trainingStart, trainingEnd, trainingDuration, trainingDurationUnitId,
                credits, /*trainingSpecialityId, trainingSpecialityTxt,*/ eduLevel, qualificationId, qualificationText, schoolCountryId, schoolCity,
                schoolName, schoolGraduationDate, schoolNotes, null, null, prevDiplUniversityId, prevDiplName, prevDiplEduLevelId,
                prevDiplGraduationDate, prevDiplNotes, prevDiplomaSpecialityId, prevDiplomaSpecialityTxt, null, null, graduationDocumentTypeId, creditHours, ectsCredits,
                appl.getApplicantId(), thesisTopic, thesisTopicEn, profGroup, thesisDefenceDate, thesisBibliography, thesisVolume, thesisLanguage, thesisAnnotation, thesisAnnotationEn);
        
        //Deleting old training course specialities
        etcDP.deleteSpecialities(trainingCourseId);
        
        //Saving training course specialities
        int specialitiesCount = DataConverter.parseInteger(request.getParameter("specialities_count"), 0);

        Set<Integer> specialityIds = new HashSet<Integer>(); //pomni veche vyvedenite specialityIds v bazata
        Set<String> specialityTexts = new HashSet<String>(); //pomni veche vyvedenite specialityTxts v bazata

        if (specialitiesCount > MAX_SPECIALITIES_COUNT) {
            specialitiesCount = MAX_SPECIALITIES_COUNT; // ogranichenie
        }

        //zapis na "izbrani specialnosti"
        for (int i = 0; i < specialitiesCount; i++) {
            saveSpecialityIfNotAlreadySaved(getNacidDataProvider().getNomenclaturesDataProvider(), etcDP, trainingCourseId, request, "speciality_id" + i, "speciality_text" + i, specialityIds, specialityTexts);
        }

        //zapis na specialnostta ot input-a (ako veche ne e zapisana)
        saveSpecialityIfNotAlreadySaved(getNacidDataProvider().getNomenclaturesDataProvider(), etcDP, trainingCourseId, request, "trainingSpecialityId", "trainingSpeciality", specialityIds, specialityTexts);

        etcDP.deleteTrainingCourseTrainingLocations(trainingCourseId);
      
        //Saving training locations
        etcDP.deleteTrainingCourseTrainingLocations(trainingCourseId);
        if (trainingLocations != null) {
        	for (TrainingLocations tl:trainingLocations) {
        		etcDP.addTrainingCourseTrainingLocation(trainingCourseId, tl.countryId, tl.city);
        	}
        }
        //End of saving training locations
        
        //Updating universities
        etcDP.updateTrainingCourseUniversities(trainingCourseId, universities);
        //end of updating universities
        
        List<Integer> tfs = null;
        if (trainingForm != null) {
            tfs = new ArrayList<Integer>();
            tfs.add(trainingForm);
        }
        etcDP.saveExtTrainingForms(tfs, trainingFormOther, trainingCourseId);

        etcDP.saveGraduationWays(graduationWays, graduationWayOther, trainingCourseId);
        etcDP.savePurposesOfrecognition(purposesOfRecognition, purposeOfRecognitionOther, applicationId);

        appl = eaDP.getApplication(applicationId);

        addSystemMessageToSession(request, "trainingCourseStatusMessage", new SystemMessageWebModel("Данните бяха записани",
                SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        try {
            response.sendRedirect(request.getContextPath() + "/control/application/"
                    + "edit"
                    + "?id=" + applicationId + "&activeForm=" + FORM_ID_TRAINING_DATA);
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }

    private void saveApplicationData(HttpServletRequest request, HttpServletResponse response) {
        User user = getLoggedUser(request, response);
        Integer applicationType = DataConverter.parseInteger(request.getParameter("application_type"), null);

        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ExtApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getExtApplicationsDataProvider();
        ExtPersonDataProvider epdp = nacidDataProvider.getExtPersonDataProvider();
        ExtPerson representative = epdp.getExtPersonByUserId(user.getUserId());
        String representativePersonalId = DataConverter.parseString(request.getParameter("representative_personal_id"), null);
        int representativePersonalIdType = DataConverter.parseInt(request.getParameter("representative_personal_id_type"), 0);

        String representativeBirthCity = DataConverter.parseString(request.getParameter("representative_birth_city"), null);
        Date representativeBirthDate = DataConverter.parseDate(request.getParameter("representative_birth_date"));
        Integer representativeBirthCountryId = DataConverter.parseInteger(request.getParameter("representative_birth_country_id"), null);
        Integer representativeCitizenship = DataConverter.parseInteger(request.getParameter("representative_citizenship"), null);

        int recordId = DataConverter.parseInt(request.getParameter("id"), 0);
        ExtApplication application = null;
        if (recordId != 0) {
            application = applicationsDataProvider.getApplication(recordId);

            UserAccessUtils.checkApplicantActionAccess(UserAccessUtils.USER_ACTION_CHANGE, getLoggedUser(request, response), application, nacidDataProvider);

        }
        SystemMessageWebModel error = new SystemMessageWebModel("Грешки", SystemMessageWebModel.MESSAGE_TYPE_ERROR);
        int homeCountryId = DataConverter.parseInt(request.getParameter("home_country_id"), 0);
        boolean personalDataUsage = DataConverter.parseBoolean(request.getParameter("personal_data_usage"));
        String homeCity = DataConverter.parseString(request.getParameter("home_city"), "");
        String homePostCode = DataConverter.parseString(request.getParameter("home_pcode"), "");
        String homeAddressDetails = DataConverter.escapeHtml(DataConverter.parseString(request.getParameter("home_addr_details"), ""));
        String homePhone = DataConverter.escapeHtml(DataConverter.parseString(request.getParameter("homePhone"), ""));
        Integer documentReceiveMethodId = DataConverter.parseInteger(request.getParameter("document_receive_method_id"), null);
        boolean homeIsBg;
        String bgCity;
        String bgPostCode;
        String bgAddressDetails;
        String bgPhone;
        boolean dataAuthentic = DataConverter.parseBoolean(request.getParameter("data_authentic"));
        if (homeCountryId == NomenclaturesDataProvider.COUNTRY_ID_BULGARIA) {
            homeIsBg = true;
            bgCity = null;
            bgPostCode = null;
            bgAddressDetails = null;
            bgPhone = null;
        } else {
            homeIsBg = false;
            bgCity = DataConverter.parseString(request.getParameter("bg_city"), "");
            bgPostCode = DataConverter.parseString(request.getParameter("bg_pcode"), "");
            bgAddressDetails = DataConverter.escapeHtml(DataConverter.parseString(request.getParameter("bg_addr_details"), ""));
            bgPhone = DataConverter.escapeHtml(DataConverter.parseString(request.getParameter("bgPhone"), ""));
        }

        Date timeOfCreation = application == null ? new Date() : application.getTimeOfCreation();
        boolean differentDiplomaNames = DataConverter.parseBoolean(request.getParameter("diploma_names"));

        /** Training course data manipulation */
        int trainingCourseRecordId = (application == null) ? 0 : application.getTrainingCourse().getId();
        ExtTrainingCourseDataProvider trainingCourseDataProvider = nacidDataProvider.getExtTrainingCourseDataProvider();
        String summary = null;
        String diplomaFirstName;
        String diplomaSurName;
        String diplomaLastName;
        if (!differentDiplomaNames) {
            diplomaFirstName = null;
            diplomaSurName = null;
            diplomaLastName = null;
        } else {
            diplomaFirstName = DataConverter.parseString(request.getParameter("diploma_firstName"), "");
            diplomaSurName = DataConverter.parseString(request.getParameter("diploma_middleName"), "");
            diplomaLastName = DataConverter.parseString(request.getParameter("diploma_lastName"), "");
        }

        /** End of training course Data manipulation */

        Integer internalApplicationId = application == null ? null : application.getInternalApplicationId();
        if (homeCountryId == 0) {
            error.addAttribute("Грешно избрана държава");
        }
        if (!StringUtils.isEmpty(homeCity)
                && !homeCity.matches(ValidationStrings.getValidationStringForJava(ValidationStrings.VALIDATION_STRING_CITY))) {
            error.addAttribute("Грешно въведен град");
        }
        if (!StringUtils.isEmpty(homePostCode)
                && !homePostCode.matches(ValidationStrings.getValidationStringForJava(ValidationStrings.VALIDATION_STRING_POST_CODE))) {
            error.addAttribute("Грешно въведен пощенски код");
        }
        if (!StringUtils.isEmpty(bgCity) && !bgCity.matches(ValidationStrings.getValidationStringForJava(ValidationStrings.VALIDATION_STRING_CITY))) {
            error.addAttribute("Грешно въведен град в България");
        }
        if (!StringUtils.isEmpty(bgPostCode)
                && !bgPostCode.matches(ValidationStrings.getValidationStringForJava(ValidationStrings.VALIDATION_STRING_POST_CODE))) {
            error.addAttribute("Грешно въведен пощенски код в България");
        }

        if (!StringUtils.isEmpty(diplomaFirstName)
                && !diplomaFirstName.matches(ValidationStrings.getValidationStringForJava(ValidationStrings.VALIDATION_STRING_NAME))) {
            error.addAttribute("Грешно въведено име по диплома");
        }
        if (!StringUtils.isEmpty(diplomaSurName)
                && !diplomaSurName.matches(ValidationStrings.getValidationStringForJava(ValidationStrings.VALIDATION_STRING_NAME))) {
            error.addAttribute("Грешно въведено презиме по диплома");
        }
        if (!StringUtils.isEmpty(diplomaLastName)
                && !diplomaLastName.matches(ValidationStrings.getValidationStringForJava(ValidationStrings.VALIDATION_STRING_NAME))) {
            error.addAttribute("Грешно въведена фамилия по диплома");
        }

        if (StringUtils.isEmpty(representative.getCivilId()) && representativePersonalId == null) {
            error.addAttribute("Няма въведен персонален идентификатор на представителя");
        }
        if (representative.getCivilIdTypeId() == null && representativePersonalIdType == 0) {
            error.addAttribute("Няма въведен тип персонален идентификатор на представителя");
        }

        if (representativePersonalIdType == CivilIdType.CIVIL_ID_TYPE_EGN && !DataConverter.validateEGN(representativePersonalId) && !DataConverter.parseBoolean(request.getParameter("representative_personal_idIgnoreEGNValidation"))) {
            error.addAttribute("Некоренто въведено ЕГН на представител!");
        }
        if (representativePersonalIdType == CivilIdType.CIVIL_ID_TYPE_LNCH && !DataConverter.validateLNCH(representativePersonalId) && !DataConverter.parseBoolean(request.getParameter("representative_personal_idIgnoreLNCHValidation"))) {
            error.addAttribute("Некоренто въведено ЛНЧ на представител!");
        }
        boolean differentApplicantRepresentative = DataConverter.parseBoolean(request.getParameter("different_applicant_representative"));
        Integer applicantPersonalIdDocumentTypeId = DataConverter.parseInteger(request.getParameter(differentApplicantRepresentative ? "applicant_personal_id_document_type" : "representative_personal_id_document_type"), null);
        if (applicantPersonalIdDocumentTypeId == null) {
            error.addAttribute("Некоренто въведен тип документ за самоличност!");
        }

        validateContactDetails(homeAddressDetails, homeCity, homePhone, homePostCode, error);
        if (!homeIsBg) {
            validateContactDetails(bgAddressDetails, bgCity, bgPhone, bgPostCode, error);
        }

        ExtPerson applicant;
        Integer applicantBirthCountryId = DataConverter.parseInteger(request.getParameter("applicant_birth_country_id"), null);
        if (differentApplicantRepresentative) {
            Integer applicantId = DataConverter.parseInteger(request.getParameter("applicant_id"), null);
            String applicantPersonalId = DataConverter.parseString(request.getParameter("applicant_personal_id"), null);
            Integer applicantPersonalIdType = DataConverter.parseInteger(request.getParameter("applicant_personal_id_type"), null);
            String firstName = DataConverter.parseString(request.getParameter("applicant_first_name"), null);
            String secondName = DataConverter.parseString(request.getParameter("applicant_second_name"), null);
            String lastName = DataConverter.parseString(request.getParameter("applicant_last_name"), null);
            String applicantEmail = DataConverter.parseString(request.getParameter("applicant_email"), null);
            String birthCity = DataConverter.parseString(request.getParameter("applicant_birth_city"), null);
            Date birthDate = DataConverter.parseDate(request.getParameter("applicant_birth_date"));
            Integer citizenshipId = DataConverter.parseInteger(request.getParameter("applicant_citizenship"), null);




            ExtPerson dbPerson = null;
            if (applicantId != null) {
                dbPerson = epdp.getExtPerson(applicantId);
                if (dbPerson == null) {
                    throw new RuntimeException("Unknown ext person with id = " + applicantId);
                }
                if (!Objects.equals(applicantPersonalId, dbPerson.getCivilId()) || !Objects.equals(applicantPersonalIdType, dbPerson.getCivilIdTypeId())) {
                    error.addAttribute("Променени тип идентификатор/идентификатор/ на заявителя!");
                }
            }
            if (StringUtils.isEmpty(firstName) || !firstName.matches(ValidationStrings.getValidationStringForJava(ValidationStrings.VALIDATION_STRING_NAME))) {
                error.addAttribute("Грешно въведно име на заявителя!");
            }
            if (!StringUtils.isEmpty(secondName) && !secondName.matches(ValidationStrings.getValidationStringForJava(ValidationStrings.VALIDATION_STRING_NAME))) {
                error.addAttribute("Грешно въведено презиме на заявителя!");
            }
            if (StringUtils.isEmpty(lastName) || !lastName.matches(ValidationStrings.getValidationStringForJava(ValidationStrings.VALIDATION_STRING_NAME))) {
                error.addAttribute("Грешно въведена фамилия на заявителя!");
            }

            if (StringUtils.isEmpty(applicantPersonalId)) {
                error.addAttribute("Няма въведен персонален идентификатор на заявителя");
            }

            if (applicantPersonalIdType == null) {
                error.addAttribute("Няма въведен тип персонален идентификатор на заявителя");
            }
            if (birthDate == null) {
                error.addAttribute("Некоренто въведена дата на раждане!");
            }
            if (applicantBirthCountryId == null) {
                error.addAttribute("Некоренто въведена държава на раждане!");
            }
            if (StringUtils.isEmpty(birthCity)) {
                error.addAttribute("Некоренто въведено мясти на раждане!");
            }

            if (applicantPersonalIdType == CivilIdType.CIVIL_ID_TYPE_EGN && !DataConverter.validateEGN(applicantPersonalId) && !DataConverter.parseBoolean(request.getParameter("applicant_personal_idIgnoreEGNValidation"))) {
                error.addAttribute("Некоренто въведено ЕГН на заявител!");
            }
            if (applicantPersonalIdType == CivilIdType.CIVIL_ID_TYPE_LNCH && !DataConverter.validateLNCH(applicantPersonalId) && !DataConverter.parseBoolean(request.getParameter("applicant_personal_idIgnoreLNCHValidation"))) {
                error.addAttribute("Некоренто въведено ЛНЧ на заявител!");
            }
            if (citizenshipId == null) {
                error.addAttribute("Некоренто въведено гражданство!");
            }
            if (applicantId == null && !StringUtils.isEmpty(applicantPersonalId) && applicantPersonalIdType != null) {
                dbPerson = epdp.getExtPerson(applicantPersonalIdType, applicantPersonalId);
            }


            applicant = new ExtPersonImpl(dbPerson == null ? 0 : dbPerson.getId(), firstName, secondName, lastName, applicantPersonalId, applicantPersonalIdType, applicantBirthCountryId, birthCity,
                    birthDate, citizenshipId, applicantEmail, null, dbPerson == null ? null : dbPerson.getUserId(), nacidDataProvider);
        } else {
            applicant = representative;
            if (representativeBirthCountryId == null) {
                error.addAttribute("Некоренто въведена държава на раждане!");
            }
            if (representativeBirthDate == null) {
                error.addAttribute("Некоренто въведена дата на раждане!");
            }
            if (StringUtils.isEmpty(representativeBirthCity)) {
                error.addAttribute("Некоренто въведено мясти на раждане!");
            }
            if (representativeCitizenship == null) {
                error.addAttribute("Некоренто въведено гражданство!");
            }
        }



        if (error.hasAttributes()) {
            NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
            List<Country> countries = nomenclaturesDataProvider.getCountries(null, null);

            ComboBoxUtils.generateNomenclaturesComboBox(homeCountryId, countries, true, request, "homeCountry", false);
            ComboBoxUtils.generateNomenclaturesComboBox(applicantBirthCountryId, countries, true, request, "applicantBirthCountry", true);
            ComboBoxUtils.generateNomenclaturesComboBox(representativeBirthCountryId, countries, true, request, "representativeBirthCountry", true);
            request.setAttribute(WebKeys.APPLICATION_WEB_MODEL, new ExtApplicationWebModel(representative, applicant, recordId, homeCity, homePostCode,
                    homeAddressDetails, homeIsBg, bgCity, bgPostCode, bgAddressDetails, homeCountryId, bgPhone, homePhone, differentDiplomaNames,
                    FORM_ID_APPLICATION_DATA, personalDataUsage, dataAuthentic, applicationType));

            ComboBoxUtils.generateDocumentReceiveMethodRadioButton(documentReceiveMethodId, nomenclaturesDataProvider.getDocumentReceiveMethods( null, null), true, request, "documentReceiveMethod");
            ComboBoxUtils.generateNomenclaturesComboBox(DataConverter.parseInteger(request.getParameter("applicant_citizenship"), null), countries, true, request, "representativeCitizenship", true);
            ComboBoxUtils.generateNomenclaturesComboBox(representativeCitizenship, countries, true, request, "applicantCitizenship", true);

            ComboBoxUtils.generateNomenclaturesComboBox(applicantPersonalIdDocumentTypeId, NomenclaturesDataProvider.FLAT_NOMENCLATURE_PERSONAL_ID_DOCUMENT_TYPE, nomenclaturesDataProvider, true, request, "applicantPersonalIdDocumentType", null, true);


            request.setAttribute("applicationStatusMessage", error);
            request.setAttribute("dataUsageDeclarationUrl", nacidDataProvider.getNomenclaturesDataProvider().getDocumentType(DOC_TYPE_APPLICANT_DECLARATION_DATA_USAGE).getDocumentUrl());
            setNextScreen(request, "application_edit");
            return;
        }
        trainingCourseRecordId = trainingCourseDataProvider.saveTrainingCourse(trainingCourseRecordId, diplomaFirstName, diplomaSurName,
                diplomaLastName, representative.getId());
        int applicantType = 0;

        if (differentApplicantRepresentative) {
            int applicantId = epdp.saveExtPerson(applicant.getId(), applicant.getFName(), applicant.getSName(), applicant.getLName(), applicant.getCivilId(), applicant.getCivilIdTypeId(), applicant.getBirthCountryId(), applicant.getBirthCity(),
                    applicant.getBirthDate(), applicant.getCitizenshipId(), applicant.getEmail(), applicant.getHashCode(), applicant.getUserId());
            applicant = epdp.getExtPerson(applicantId);
        }


        int newId = applicationsDataProvider.saveApplication(recordId, applicant.getId(), null, differentDiplomaNames, trainingCourseRecordId,
                homeCountryId, homeCity, homePostCode, homeAddressDetails, homePhone, homeIsBg, bgCity, bgPostCode, bgAddressDetails, bgPhone,
                timeOfCreation, summary, 0, internalApplicationId, personalDataUsage, dataAuthentic, applicantType,  null, representative.getId(), null, null, null, null, null, null, null, null, null, null, applicationType, documentReceiveMethodId, applicantPersonalIdDocumentTypeId);

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
                applicationsDataProvider.saveDocumentRecipient(newId, name, countryId, city, district, postCode, address, mobilePhone);
            }
        }
        if (!hasDocumentRecipient) {
            applicationsDataProvider.deleteDocumentRecipient(newId);
        }
        //end of saving document recipient


        boolean updateRepresentative = StringUtils.isEmpty(representative.getCivilId()) || representative.getCivilIdTypeId() == null || !differentApplicantRepresentative;
        if (updateRepresentative) {
            representativePersonalIdType = representative.getCivilIdTypeId() == null ? representativePersonalIdType : representative.getCivilIdTypeId();
            representativePersonalId = StringUtils.isEmpty(representative.getCivilId()) ? representativePersonalId : representative.getCivilId();

            representativeBirthCity = !differentApplicantRepresentative ? representativeBirthCity : representative.getBirthCity();
            representativeBirthCountryId = !differentApplicantRepresentative ? representativeBirthCountryId : representative.getBirthCountryId();
            representativeBirthDate = !differentApplicantRepresentative ? representativeBirthDate : representative.getBirthDate();
            representativeCitizenship = !differentApplicantRepresentative ? representativeCitizenship : representative.getCitizenshipId();

            epdp.saveExtPerson(representative.getId(), representative.getFName(), representative.getSName(), representative.getLName(), representativePersonalId, representativePersonalIdType, representativeBirthCountryId, representativeBirthCity, representativeBirthDate, representativeCitizenship, representative.getEmail(), representative.getHashCode(), representative.getUserId());
            request.getSession().setAttribute("extPerson", null);//resetting extPerson!
        }

        // Ako e promenen status-a na 1, togava ne trqbva da moje ve4e da se
        // editva, i zatova se prenaso4va kym sy6tata stranica no s
        // operation=view
        addSystemMessageToSession(request, "applicationStatusMessage", new SystemMessageWebModel("Данните бяха въведени в базата",
                SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        try {
            response.sendRedirect("/nacid_ext/control/application/edit"
                    + "?id=" + newId);
        } catch (IOException e) {
            throw Utils.logException(e);
        }
        request.getSession().removeAttribute(getTableAttribute(applicationType));
    }
    private void validateContactDetails(String addr, String city, String phone, String postCode, SystemMessageWebModel error) {
        if (StringUtils.isEmpty(addr)) {
            error.addAttribute("Няма въведен адрес за контакт!");
        }
        if (StringUtils.isEmpty(city)) {
            error.addAttribute("Няма въведен град за контакт!");
        }
        if (StringUtils.isEmpty(phone)) {
            error.addAttribute("Няма въведен телефон за контакт!");
        }
        if (StringUtils.isEmpty(postCode)) {
            error.addAttribute("Няма въведен пощенски код!");
        }
    }

    private void addApplicationToWebModel(ExtApplication application, HttpServletRequest request, NacidDataProvider nacidDataProvider,
            int activeFormId) {
        
        initApplyingTab(request, application);
        List<Country> countries = nacidDataProvider.getNomenclaturesDataProvider().getCountries(null, null);
        
        ComboBoxUtils.generateNomenclaturesComboBox(application.getHomeCountryId(), countries, true, request, "homeCountry", false);
        ComboBoxUtils.generateNomenclaturesComboBox(application.getDocumentRecipient() == null ? null : application.getDocumentRecipient().getCountryId(), countries, true, request, "recipientCountryCombo", true);
        request.setAttribute(WebKeys.APPLICATION_WEB_MODEL, new ExtApplicationWebModel(application, activeFormId));

        NomenclaturesDataProvider nomenclaturesDataProvider = getNacidDataProvider().getNomenclaturesDataProvider();
        addFlatNomenclaturesToWebModel(nomenclaturesDataProvider.getGraduationWays(application.getApplicationType(), new Date(), OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true)),
                request,
                WebKeys.FLAT_NOMENCLATURE_GRADUATION_WAY
                );
        addFlatNomenclaturesToWebModel(NomenclaturesDataProvider.FLAT_NOMENCLATURE_TRAINING_FORM, nomenclaturesDataProvider, request,
                WebKeys.FLAT_NOMENCLATURE_TRAINING_FORM,
                OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true));
        addFlatNomenclaturesToWebModel(NomenclaturesDataProvider.FLAT_NOMENCLATURE_RECOGNITION_PURPOSE, nomenclaturesDataProvider, request,
                WebKeys.FLAT_NOMENCLATURE_RECOGNITION_PURPOSE,
                OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true));

        ExtTrainingCourse extTC = application.getTrainingCourse();

        ComboBoxUtils.generateNomenclaturesComboBox(
                extTC.getSchoolCountryId(), countries, true,
                request, "schoolCountryCombo", true);

        ComboBoxUtils.generateNomenclaturesComboBox(null, countries, true, request, "trainingCountry", true);



        ComboBoxUtils.generateNomenclaturesComboBox(extTC.getDurationUnitId(), NomenclaturesDataProvider.FLAT_NOMENCLATURE_DURATION_UNIT,
                nomenclaturesDataProvider, true, request, "trainingDurationUnitId",
                OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true), false);

        List<ProfessionGroup> profGroups = nomenclaturesDataProvider.getProfessionGroups(0, new Date(), null);
        List<FlatNomenclature> eduAreas = nomenclaturesDataProvider.getFlatNomenclatures(FLAT_NOMENCLATURE_EDUCATION_AREA, null, null);
        ComboBoxUtils.generateProfessionGroupComboBox(extTC.getProfGroupId(), profGroups, true, request, "profGroupCombo", true);
        ComboBoxUtils.generateNomenclaturesComboBox(extTC.getProfGroupId() == null ? null : extTC.getProfGroup().getEducationAreaId(), eduAreas, false, request, "profGroupEduAreaCombo", true);

        List<? extends FlatNomenclature> prevDiplomaEduLevels = nomenclaturesDataProvider.getEducationLevels(ApplicationType.RUDI_APPLICATION_TYPE, new Date(), OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true));
        ComboBoxUtils.generateNomenclaturesComboBox(extTC.getPrevDiplomaEduLevelId(), prevDiplomaEduLevels, true, request, "prevDiplEduLevelCombo", true);

        List<? extends FlatNomenclature> eduLevels = nomenclaturesDataProvider.getEducationLevels(application.getApplicationType(), new Date(), OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true));
        ComboBoxUtils.generateNomenclaturesComboBox(extTC.getEducationLevelId(), eduLevels, true, request, "eduLevel", true);
        
        
/*        if(extTC.getSpecialityIds() != null) {
            specId = extTC.getSpecialityIds().get(0).toString();//TODO:Tova sega raboti samo s pyrvata specialnost ot extTrainingDataProvider!!!
        }
        else if (!Utils.isEmptyString(extTC.getSpecialityTxt())) {
            specId = "other";
        }
        */
        ExtTrainingCourseWebModel webmodel = new ExtTrainingCourseWebModel(application, nacidDataProvider);

        request.setAttribute(WebKeys.EXT_TRAINING_COURSE_WEB_MODEL, webmodel);

        ComboBoxUtils.generateNomenclaturesComboBox(application.getApplicant() == null || Objects.equals(application.getApplicantId(), application.getRepresentativeId())  ? null : application.getApplicant().getBirthCountryId(), countries, true, request, "applicantBirthCountry", true);
        ComboBoxUtils.generateNomenclaturesComboBox(application.getRepresentative().getBirthCountryId(), countries, true, request, "representativeBirthCountry", true);
    }


    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        MenuUtils.processMenuClick(request, getLoggedUser(request, response));
        new MenuShowHandler(getServletContext()).processRequest(request, response);//tyj kato ako se idva ot portala, nqma da e kliknato po menuto vseki pyt load-vam menuto pri /list
        HttpSession session = request.getSession();
        int applicationType = DataConverter.parseInt(getPathInfoElement(request.getPathInfo(), 3), 0);
        Table table = (Table) session.getAttribute(getTableAttribute(applicationType));
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
            session.setAttribute(getTableAttribute(applicationType), table);
            resetTableData(request, response, applicationType);
        }



        // TableState settings
        TableState tableState = (TableState) session.getAttribute(getTableAttribute(applicationType) + WebKeys.TABLE_STATE);

        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            session.setAttribute(getTableAttribute(applicationType)+ WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък със заявления");
        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
        webmodel.addRequestParam(TableWebModel.OPERATION_NAME_NEW, "application_type", applicationType + "");
        webmodel.addFormAdditionalRequestParam("application_type", applicationType + "");

        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(getTableAttribute(applicationType) + WebKeys.FILTER_WEB_MODEL);
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            session.setAttribute(getTableAttribute(applicationType) + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);

    }
    private String getTableAttribute(int applicationType) {
        return WebKeys.TABLE_APPLICATIONS + applicationType;
    }

    private void resetTableData(HttpServletRequest request, HttpServletResponse response, int applicationType) {
        Table table = (Table) request.getSession().getAttribute(getTableAttribute(applicationType));
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ExtApplicationsDataProvider extApplicationsDataProvider = nacidDataProvider.getExtApplicationsDataProvider();
        ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
        User user = getLoggedUser(request, response);
        ExtPersonDataProvider epdp = nacidDataProvider.getExtPersonDataProvider();
        ExtPerson person = epdp.getExtPersonByUserId(user.getUserId());
        List<ExtApplication> apps = extApplicationsDataProvider.getApplicationsByRepresentative(person.getId(), applicationType);

        if (apps != null) {
            for (ExtApplication app : apps) {
                Application intApp = app.getInternalApplicationId() == null ? null : applicationsDataProvider.getApplication(app.getInternalApplicationId());
            	try {
                    TableRow row = table.addRow(app.getId(), 
                            app.getTimeOfCreation(), intApp == null ? "" : intApp.getDocFlowNumber(),
                            app.getApplicant().getFullName(),
                            getAppStatusName(app.getApplicationStatus(), intApp), app.isESigned());
                    row.setEditable(app.getApplicationStatus() == ExtApplication.STATUS_EDITABLE);
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }


    public static void generateNomenclaturesComboBox(String activeId, int nomenclatureType, NomenclaturesDataProvider nomenclaturesDataProvider,
            HttpServletRequest request, String comboName, OrderCriteria orderCriteria) {

        List<? extends FlatNomenclature> flatNomeclatures = null;

        if (nomenclatureType == NomenclaturesDataProvider.NOMENCLATURE_SPECIALITY) {
            flatNomeclatures = nomenclaturesDataProvider.getSpecialities(0, null, orderCriteria);
        } else {
            flatNomeclatures = nomenclaturesDataProvider.getFlatNomenclatures(nomenclatureType, null, orderCriteria);
        }
        ComboBoxWebModel combobox = new ComboBoxWebModel(activeId, true);
        combobox.addItem("other", "Друго");
        if (flatNomeclatures != null) {
            for (FlatNomenclature s : flatNomeclatures) {
                if (!s.isActive()) {
                    continue;
                }
                combobox.addItem(s.getId() + "", s.getName());
            }
            if (comboName != null) {
                request.setAttribute(comboName, combobox);
            }
        }
    }
    
    public static String getAppStatusName(int s, Application intApplication) {
        if (intApplication != null) {
        	return ExternalApplicationStatus.getExternalStatusName(intApplication.getApplicationStatus());
        }
    	switch (s) {
            case ExtApplication.STATUS_EDITABLE: return "Неподадено";
            case ExtApplication.STATUS_NOT_EDITABLE: return "Проверка";
            case ExtApplication.STATUS_TRANSFERED: return "В процес на обработка";
            case ExtApplication.STATUS_FINISHED: return "Приключено";
            default: throw new RuntimeException("Unknown externalStatus!" + s ) ;
        }
    }
    @Getter
    @AllArgsConstructor
    static class NomenclatureIdAndText {
        private Integer id;
        private String text;
    }

    private static Optional<NomenclatureIdAndText> generateNomenclatureIdAndText(String requestParamId, String requestParamValue, HttpServletRequest request, int nomenclatureType, NomenclaturesDataProvider nomenclaturesDataProvider) {
        Integer id = DataConverter.parseInteger(request.getParameter(requestParamId), null);
        String txt = DataConverter.parseString(request.getParameter(requestParamValue), null);
        if (id == null && !StringUtils.isEmpty(txt)) {
            FlatNomenclature nom = nomenclaturesDataProvider.getFlatNomenclature(nomenclatureType, txt);
            id = nom == null ? id : (Integer) nom.getId();
            txt = nom != null ? null : txt;
        }
        if (id == null && StringUtils.isEmpty(txt)) {
            return Optional.empty();
        } else {
            return Optional.of(new NomenclatureIdAndText(id, txt == null ? null : txt.trim()));
        }
    }
    private static void saveSpecialityIfNotAlreadySaved(NomenclaturesDataProvider nomenclaturesDataProvider, ExtTrainingCourseDataProvider etcDP, int trainingCourseId,
                                                        HttpServletRequest request, String specialityIdParam, String specialityTxtParam,
                                                        Set<Integer> savedSpecialityIds, Set<String> savedSpecialityTexts) {
        Optional<NomenclatureIdAndText> spec = generateNomenclatureIdAndText(specialityIdParam, specialityTxtParam, request, NOMENCLATURE_SPECIALITY, nomenclaturesDataProvider);
        if (!spec.isPresent()) {
            return;
        }
        Integer specialityId = spec.get().getId();
        String specialityTxt = spec.get().getText();
        if (specialityId != null && savedSpecialityIds.contains(specialityId)) {
            return;
        }
        if (specialityTxt != null && savedSpecialityTexts.contains(specialityTxt)) {
            return;
        }
        ExtTrainingCourseSpecialityRecord record = new ExtTrainingCourseSpecialityRecord(0, trainingCourseId, specialityId, specialityTxt);
        etcDP.saveSpeciality(record);
        if (specialityId != null) {
            savedSpecialityIds.add(specialityId);
        } else {
            savedSpecialityTexts.add(specialityTxt);
        }
    }

    /*private static void saveSingleSpeciality(HttpServletRequest request, ExtTrainingCourseDataProvider etcDP, int trainingCourseId, List<Integer> specialityIds, List<String> specialityTexts) {
        String specialityIdAsString = DataConverter.parseString(request.getParameter("trainingSpecialityId"), "");
        ExtTrainingCourseSpecialityRecord record = null;
        if (specialityIdAsString.equals("other")) {
            String specialityTxt = DataConverter.parseString(request.getParameter("trainingSpecialityTxt"), "");
            specialityTxt = specialityTxt.trim();
            if (!specialityTxt.equals("") && !specialityTexts.contains(specialityTxt.toLowerCase())) {
                record = new ExtTrainingCourseSpecialityRecord(0, trainingCourseId, null, specialityTxt);
            }
        }
        else if (!specialityIdAsString.equals("")) {
            Integer specialityId = DataConverter.parseInteger(specialityIdAsString, null);
            if (specialityId != null && !specialityIds.contains(specialityId)) {
                record = new ExtTrainingCourseSpecialityRecord(0, trainingCourseId, specialityId, "");
            }
        }
        if (record != null) {
            etcDP.saveSpeciality(record);
        }
    }*/
}
