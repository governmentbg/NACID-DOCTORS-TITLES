package com.nacid.bl.nomenclatures;

import java.util.*;


public interface DocumentType extends FlatNomenclature {
    /**
     * dokument za samolichnost
     */
	public static final int DOC_TYPE_PERSONAL_ID_CARD = 1;
	public static final int DOC_TYPE_DIPLOMA_SCHOOL = 2;
    
    public static final int DOC_TYPE_SLUJ_BELEJKA_ZA_PRIZNATO = 5;
    public static final int DOC_TYPE_CERTIFICATE = 6;
    public static final int DOC_TYPE_PISMO_OTLAGANE = 9;
    public static final int DOC_TYPE_SLUJ_BELEJKA_PREDSTAVENI_DOCS = 10;
    public static final int DOC_TYPE_REFUSE = 14;
    public static final int DOC_TYPE_REFUSE_IN_BG = 15;
    public static final int DOC_TYPE_EXPERT_POSITION = 18;
    public static final int DOC_TYPE_CERTIFICATE_DUPLICATE = 20;
    public static final int DOC_TYPE_CERTIFICATE_FACTUAL_ERROR = 150;
    
    public static final int DOC_TYPE_AUTHENCITY_EN_EXEC_DIR = 24;

    public static final int DOC_TYPE_DIPLOMA_ATTACHMENTS = 27;//приложение към диплома

    public static final int DOC_TYPE_OBEZSILENO = 31;
    public static final int DOC_TYPE_MESSAGE_BY_EMAIL_OUT = 33;
    public static final int DOC_TYPE_MESSAGE_BY_EMAIL_IN = 34;
    
    public static final int DOC_TYPE_DIPLOMA_UNIVERSITY = 51;
    public static final int DOC_TYPE_DIPLOMA_DOCTORATE = 234;
    public static final int DOC_TYPE_MIGRATED = 52;
    
    public static final int DOC_TYPE_FACTUAL_ERROR_APPLY = 54;

    public static final int DOC_TYPE_UNISHTOJENO = 59;
    
    public static final int DOC_TYPE_DUPLICATE_APPLY = 60;
    
    public static final int DOC_TYPE_REGPROF_PERSONAL_DOCUMENT = 63;
    public static final int DOC_TYPE_REGPROF_DIPLOMA = 64;
    public static final int DOC_TYPE_REGPROF_OTHER_CERTIFICATES = 65;
    public static final int DOC_TYPE_REGPROF_PROFESSIONAL_ORGANIZATION_MEMBERSHIP = 66;
    public static final int DOC_TYPE_REGPROF_IDENTITY_DOCUMENT = 67;
    public static final int DOC_TYPE_REGPROF_CERTIFICATE_STAJ = 68;
    //public static final int DOC_TYPE_REGPROF_CERTIFICATE_DUPLICATE = 69;
    public static final int DOC_TYPE_REGPROF_CERTIFICATE_APPLY = 70;
    
    public static final int DOC_TYPE_REGPROF_APPLICANT_DECLARATION_PROFESSION_PRACTICE_RIGHTS = 73;
    public static final int DOC_TYPE_REGPROF_APPLICANT_DECLARATION_AUTHENTIC_DATA = 74;
    
    public static final int DOC_TYPE_ZAPOVED_OBEZSILVANE = 75;
    public static final int DOC_TYPE_REGPROF_TRUDOVA_KNIJKA = 76;
    public static final int DOC_TYPE_REGPROF_UP_3 = 77;
    
    public static final int DOC_TYPE_REGPROF_OSIGURITELNA_KNIJKA = 80;
    public static final int DOC_TYPE_REGPROF_PISMO_SPIRANE_SROK = 81;
    public static final int DOC_TYPE_REGPROF_ZAPITVANE_AUTH_EDUCATION_DOC_EXAM = 82;
    public static final int DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_3 = 83;    
    public static final int DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_STAJ = 84;
    
    public static final int DOC_TYPE_REGPROF_PISMO_PREKRATIAVANE = 85;
    public static final int DOC_TYPE_REGPROF_LETTER_TO_APPLICANT_FOR_INFORMATION = 86;
    
    public static final int DOC_TYPE_REGPROF_ZAPITVANE_AUTH_EXPERIENCE_DOC_EXAM = 87;
    
    public static final int DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_HIGHER_AND_SECONDARY = 92;
    public static final int DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_CPO = 93;
    public static final int DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_SDK = 94;
    public static final int DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_1 = 95;
    public static final int DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_2 = 97;
    
    public static final int DOC_TYPE_REGPROF_CERTIFICATE_HIGHER_AND_SECONDARY = 102;
    public static final int DOC_TYPE_REGPROF_CERTIFICATE_CPO = 103;
    public static final int DOC_TYPE_REGPROF_CERTIFICATE_SDK = 104;    
    public static final int DOC_TYPE_REGPROF_OTKAZ_1 = 105;
    public static final int DOC_TYPE_REGPROF_OTKAZ_2 = 107;
    public static final int DOC_TYPE_REGPROF_OTKAZ_3 = 108;
    
    public static final int DOC_TYPE_REGPROF_OTGOVOR_ZAPITVANE_DOC_EXAM = 109;
    public static final int DOC_TYPE_REGPROF_OTGOVOR_ZAPITVANE_EXPERIENCE_EXAM = 110;
    public static final int DOC_TYPE_REGPROF_PYLNOMOSHTNO = 111;
    public static final int DOC_TYPE_REGPROF_PAYMENT_DOCUMENT = 112;
    
    public static final int DOC_TYPE_REGPROF_CERTIFICATE_APPLY_PROFESSIONAL_QUALIFICATION = 115;
    
    public static final int DOC_TYPE_REGPROF_ISKANE_OBEZSILVANE = 117;
    
    public static final int DOC_TYPE_REGPROF_APPLICANT_DECLARATION_3 = 119;
    
    public static final int DOC_TYPE_REGPROF_DUPLICATE_APPLY = 122;
    public static final int DOC_TYPE_REGPROF_FACTUAL_ERROR_APPLY = 123;
    public static final int DOC_TYPE_REGPROF_ZAPOVED_OBEZSILVANE = 124;
    public static final int DOC_TYPE_REGPROF_OTHERS = 125;
    public static final int DOC_TYPE_REGPROF_COURT_DECISION = 126;
    public static final int DOC_TYPE_REGPROF_VOLUNTARILY_SUSPENSION_APPLY = 127;
    public static final int DOC_TYPE_REGPROF_MIGRATED = 129;
    public static final int DOC_TYPE_REGPROF_SCREENSHOT = 130;

    public static final int DOC_TYPE_REGPROF_DUPLICATE_CERTIFICATE = 69;
    public static final int DOC_TYPE_REGPROF_FACTUAL_ERROR_CERTIFICATE = 165;
    public static final int DOC_TYPE_REGPROF_UNISHTOJENO = 163;



    public static final int DOC_TYPE_DOCTORATE_CERTIFICATE = 217;
    public static final int DOC_TYPE_DOCTORATE_CERTIFICATE_SUGGESTION = 216;

    public static final int DOC_TYPE_DOCTORATE_REFUSE_SUGGESTION = 218;
    public static final int DOC_TYPE_DOCTORATE_REFUSE = 219;
    public static final int DOC_TYPE_DOCTORATE_PISMO_PREKRATIAVANE = 220;
    public static final int DOC_TYPE_DOCTORATE_ZAPOVED_OBEZSILVANE = 221;
    public static final int DOC_TYPE_DOCTORATE_CERTIFICATE_DUPLICATE = 222;
    public static final int DOC_TYPE_DOCTORATE_CERTIFICATE_FACTUAL_ERROR = 223;
    public static final int DOC_TYPE_DOCTORATE_OBEZSILENO = 224;
    public static final int DOC_TYPE_DOCTORATE_UNISHTOJENO = 225;

    public static final int DOC_TYPE_DOCTORATE_EXPERT_POSITION = 226;
    public static final int DOC_TYPE_APPLICANT_DECLARATION_DATA_USAGE = 229;
    public static final int DOC_TYPE_PAYMENT_ORDER_COURIER_RUDI = 230;
    public static final int DOC_TYPE_PAYMENT_ORDER_COURIER_NORQ = 231;
    public static final int DOC_TYPE_ABSTRACT = 232;
    public static final int DOC_TYPE_DISSERTATION_WORK = 233;
    public static final int DOC_TYPE_OTHER_DOCTORATE_ATTACHMENTS = 235;


    public static final List<Integer> REGPROF_SUGGESTIONS = Arrays.asList(DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_STAJ, DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_HIGHER_AND_SECONDARY,
            DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_CPO, DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_SDK, DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_3,
            DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_1, DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_2, DOC_TYPE_REGPROF_PISMO_PREKRATIAVANE);
    
    public static final List<Integer> REGPROF_CERTIFICATE_SUGGESTIONS = 
            Arrays.asList(DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_STAJ, DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_HIGHER_AND_SECONDARY,
                DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_CPO, DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_SDK);
    
    public static final List<Integer> REGPROF_CERTIFICATES = 
            Arrays.asList(DOC_TYPE_REGPROF_CERTIFICATE_STAJ, DOC_TYPE_REGPROF_CERTIFICATE_HIGHER_AND_SECONDARY, DOC_TYPE_REGPROF_CERTIFICATE_CPO, DOC_TYPE_REGPROF_CERTIFICATE_SDK, DOC_TYPE_REGPROF_FACTUAL_ERROR_CERTIFICATE, DOC_TYPE_REGPROF_DUPLICATE_CERTIFICATE);
    
    public static final List<Integer> REGPROF_DENIAL_SUGGESTIONS = Arrays.asList(DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_1, DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_2,
            DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_3);
    
    public static final List<Integer> REGPROF_DENIALS = Arrays.asList(DOC_TYPE_REGPROF_OTKAZ_1, DOC_TYPE_REGPROF_OTKAZ_2,
            DOC_TYPE_REGPROF_OTKAZ_3);
    
    public static final Map<Integer , Integer> REGPROF_SUGGESTIONS_TO_CERTIFICATES = new HashMap<Integer , Integer>() {{
        put(DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_CPO, DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_CPO);
        put(DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_HIGHER_AND_SECONDARY, DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_HIGHER_AND_SECONDARY);
        put(DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_SDK, DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_SDK);
        put(DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_STAJ, DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_STAJ);
    }};
    
    public static final Map<Integer, Integer> REGPROF_SUGGESTIONS_TO_DENIALS = new HashMap<Integer, Integer>() {{
        put(DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_1, DocumentType.DOC_TYPE_REGPROF_OTKAZ_1);
        put(DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_2, DocumentType.DOC_TYPE_REGPROF_OTKAZ_2);
        put(DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_3, DocumentType.DOC_TYPE_REGPROF_OTKAZ_3);
    }};

    public static final Set<Integer> RUDI_CERTIFICATE_DOC_TYPES = new HashSet<Integer>(){{
        add(DOC_TYPE_CERTIFICATE);
        add(DOC_TYPE_CERTIFICATE_DUPLICATE);
        add(DOC_TYPE_CERTIFICATE_FACTUAL_ERROR);
    }};
    Set<Integer> RUDI_DOCTORATE_CERTFIFICATE_DOC_TYPES = new HashSet<Integer>() {{
        add(DOC_TYPE_DOCTORATE_CERTIFICATE);
        add(DOC_TYPE_DOCTORATE_CERTIFICATE_DUPLICATE);
        add(DOC_TYPE_DOCTORATE_CERTIFICATE_FACTUAL_ERROR);
    }};
    Set<Integer> RUDI_AND_RUDI_DOCTORATE_CERTIFICATE_TYPES = new HashSet<Integer>() {{
        addAll(RUDI_CERTIFICATE_DOC_TYPES);
        addAll(RUDI_DOCTORATE_CERTFIFICATE_DOC_TYPES);
    }};

	/**
	 * 
	 * @return name + (isIncoming() ? "/вх." : "изх.") 
	 */
	public String getLongName();
	public boolean isIncoming();
	public boolean isHasDocflowId();
	public List<Integer> getDocCategoryIds();
	public String getDocumentTemplate();
    public String getDocumentUrl();
}
