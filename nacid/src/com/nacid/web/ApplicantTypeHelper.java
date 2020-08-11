package com.nacid.web;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by georgi.georgiev on 16.09.2015.
 */
public class ApplicantTypeHelper {
    public static Integer APPLICANT_TYPE_PHYSICAL_PERSON = 0;
    public static Integer APPLICANT_TYPE_LEGAL_PERSON = 2;
    public static Integer APPLICANT_TYPE_UNIVERSITY = 5;
    public static Map<Integer, String> APPLICANT_TYPE_ID_TO_NAME = new LinkedHashMap<Integer, String>();
    static {
        APPLICANT_TYPE_ID_TO_NAME.put(0, "Физическо лице");
        APPLICANT_TYPE_ID_TO_NAME.put(2, "Юридическо лице");
        APPLICANT_TYPE_ID_TO_NAME.put(5, "Университет");
    }
    public static String getApplicantTypeName(int applicantTypeId) {
        return APPLICANT_TYPE_ID_TO_NAME.get(applicantTypeId);
    }
}
