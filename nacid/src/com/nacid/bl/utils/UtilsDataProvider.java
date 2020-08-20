package com.nacid.bl.utils;

import java.util.List;
import java.util.Map;

public interface UtilsDataProvider {
    
    public static final String REGISTRATION_LINK_MAIL_SUBJECT = "registrationLinkMailSubject";
    public static final String REGISTRATION_LINK_MAIL_BODY = "registrationLinkMailBody";
    public static final String MAIL_SENDER = "registrationLinkMailSender";
    public static final String PASS_RECOVERY_MAIL_SUBJECT = "passRecoveryMailSubject";
    public static final String PASS_RECOVERY_MAIL_BODY = "passRecoveryMailBody";
    public static final String EXPERT_ADDED_NOTIFICATION_SUBJECT = "expAddedNotifSubject";
    public static final String EXPERT_REMOVED_NOTIFICATION_SUBJECT = "expRemovedNotifSubject";
    public static final String EXPERT_ADDED_NOTIFICATION_BODY = "expAddedNotifBody";
    public static final String EXPERT_REMOVED_NOTIFICATION_BODY = "expRemovedNotifBody";
    public static final String COMMISSION_SESSION_CHANGED_BODY = "commissionSessionChangeBody";
    public static final String COMMISSION_SESSION_CHANGED_SUBJECT = "commissionSessionChangeSubject";
    public static final String ASK_APPLICANT_FOR_DOCUMENT_SUBJECT = "askApplForDocumentsSubject";
    public static final String APPLICATION_APPLIED_SUBJECT = "applicationAppliedSubject";
    public static final String APPLICATION_APPLIED_MESSAGE = "applicationAppliedMessage";
    public static final String APPLICATION_TRANSFERRED_SUBJECT = "applicationTransferredSubject";
    public static final String APPLICATION_TRANSFERRED_MESSAGE = "applicationTransferredMessage";
    public static final String STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TRANSFERRED_MESSAGE = "sarApplicationTransferredMsg";
    public static final String DOCTORATE_APPLICATION_TRANSFERRED_MESSAGE = "docApplicationTransferredMsg";
    public static final String APPLICATION_ENTERED_SUBJECT = "applicationEnteredSubject";
    public static final String APPLICATION_ENTERED_MESSAGE = "applicationEnteredMessage";
    public static final String REGPROF_APPLICATION_APPLIED_SUBJECT = "rpApplicationAppliedSubject";
    public static final String REGPROF_APPLICATION_APPLIED_MESSAGE = "rpApplicationAppliedMessage";
    public static final String REGPROF_MAIL_SENDER = "REGPROF_MAIL_SENDER";
    public static final String EMPLOYEES_NEW_APPLICATION_MSG = "EMPLOYEES_NEW_APPLICATION_MSG";
    public static final String EMPLOYEES_NEW_APPL_SUBJECT = "EMPLOYEES_NEW_APPL_SUBJECT";
    public static final String EMPLOYEES_MAILS = "EMPLOYEES_MAILS";
    public static final String ESOED_SEND_URL = "esoedSendUrl";
    //public static final String EPAY_URL = "EPAY_URL";
//    public static final String EPAY_GENERATE_IDN_URL = "EPAY_GENERATE_IDN_URL";
    public static final String EPAY_PAYMENT_URL = "EPAY_PAYMENT_URL";
    public static final String NACID_IBAN = "NACID_IBAN";
    public static final String NACID_BIC = "NACID_BIC";
    public static final String NACID_MERCHANT_NAME = "NACID_MERCHANT_NAME";
    public static final String EPAY_PAYMENT_TYPE_ID = "EPAY_PAYMENT_TYPE_ID";
    public static final String EPAY_URL_OK = "EPAY_URL_OK";
    public static final String EPAY_URL_CANCEL = "EPAY_URL_CANCEL";
    public static final String EPAY_CIN = "EPAY_CIN";
    public static final String EPAY_SECRET_KEY = "EPAY_SECRET_KEY";
    public static final String EPAY_PAYMENT_DESCRIPTION = "EPAY_PAYMENT_DESCRIPTION";
    public static final String EPAY_USER_MESSAGE_SUBJECT = "EPAY_USER_MESSAGE_SUBJECT";
    public static final String EPAY_USER_MESSAGE_BODY = "EPAY_USER_MESSAGE_BODY";
    public static final String EXT_APPLICATION_LOGIN_URL = "EXT_APPLICATION_LOGIN_URL";
    public static final String EXT_REGPROF_APPLICATION_LOGIN_URL = "EXT_REGPROF_APP_LOGIN_URL";

    
    public static final String SMTP_HOST = "smtpHost";
    public static final String SMTP_AUTH_USER = "smtpAuthUser";
    public static final String SMTP_AUTH_PASS = "smtpAuthPass";
    public static final String SMTP_PORT = "smtpPort";
    public static final String MAIL_ADDITIONAL_PROPERTIES = "mailAdditionalProperties";

    public static final String MAIL_NOTIFICATION_SUBJECT = "mailNotificationSubj";


    public static final String APPLICATION_TRANSFERRED_ADMININSTRATOR_MESSAGE = "appTransferredAdminMsg";
    public static final String APPLICATION_TRANSFERRED_ADMININSTRATOR_SUBJECT = "appTransferredAdminSubject";
    public static final String APPLICATION_TRANSFERRED_ADMININSTRATOR_MAIL = "appTransferredAdminMail";

    public static final String ATTACHMENTS_BASE_DIR = "ATTACHMENTS_BASE_DIR";
    public static final String RAS_SERVICE_URL = "rasServiceUrl";
    public static final String APOSTILLE_SERVICE_URL = "apostilleServiceUrl";
    public static final String REGPROF_ESERVICES_ÄPPLICATION_EXPERIENCE_COMMENT = "regprofEservicesExpComment";
    public static final String REGPROF_ESERVICES_ÄPPLICATION_EDUCATION_COMMENT = "regprofEservicesEduComment";
    public static final String NACID_ATTACHMENTS_URL = "nacidAttachmentsUrl";
    public static final String RUDI_QRCODE_URL = "rudiQrCodeUrl";

    public static final String REGPROF_QRCODE_URL = "norqQrCodeUrl";
    public static final String RUDI_APPLICATION_TYPE = "rudiApplicationType";
    public static final String REGPROF_APPLICATION_TYPE = "regprofApplicationType";
    
    
    
	public Map<String, String> getCommonVariablesAsMap();
	public String getCommonVariableValue(String commonVariableName);
	
	public CommonVariable getCommonVariable(int id);
	public int saveCommonVariable(int id, String variableName, String variableValue, String description );
	public List<CommonVariable> getCommonVariables();



    public List<UniversityDetail> getUniversityDetails();
    public UniversityDetail getUniversityDetail(int id);
    public int saveUniversityDetail(int id, String universityName, String letterHeader, String salutation);

}
