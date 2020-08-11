package com.nacid.web;

import java.util.HashMap;
import java.util.Map;

public class ValidationStrings {
    private static Map<String, String> validationStrings = new HashMap<String, String>();
    public static final String VALIDATION_STRING_NAME = "name";
    public static final String VALIDATION_STRING_ORIGINAL_NAME = "original_name";
    public static final String VALIDATION_STRING_NOMENCLATURE_NAME = "nomenclature_name";
    public static final String VALIDATION_STRING_NOMENCLATURE_NAME_WITH_FULLSTOP_AND_DIGITS = "nomenclature_name_with_fullstop_digits";
    public static final String VALIDATION_STRING_NOMENCLATURE_ISO3166_CODE = "nomenclature_iso3166_code";
    public static final String VALIDATION_STRING_EMAIL = "email";
    public static final String VALIDATION_STRING_URL = "url";
    public static final String VALIDATION_STRING_DATE = "date";
    public static final String VALIDATION_STRING_CITY = "city";
    public static final String VALIDATION_STRING_POST_CODE = "post_code";
    public static final String VALIDATION_STRING_NUMBER = "number";
    public static final String VALIDATION_STRING_ALL_BUT_QUOTE = "allButQuote";
    static {
        validationStrings.put(VALIDATION_STRING_NAME, "/^[\u0400-\u04FF\\-'(). \"\\/]+$/");
        validationStrings.put(VALIDATION_STRING_ORIGINAL_NAME, "/^(([a-zA-Z\\-'() .\"\\/]+)|([\u0400-\u04FF\\-'() .\"\\/]+))$/");
        validationStrings.put(VALIDATION_STRING_NOMENCLATURE_NAME, "/^[\u0400-\u04FF\\-'() ,\"\\/]+$/");
        validationStrings.put(VALIDATION_STRING_NOMENCLATURE_NAME_WITH_FULLSTOP_AND_DIGITS, "/^[0-9\u0400-\u04FF\\-'() ,.\"\\/]+$/");
        validationStrings.put(VALIDATION_STRING_NOMENCLATURE_ISO3166_CODE, "/^[A-Za-z]+$/");
        //validationStrings.put("email", "/^([\u0400-\u04FFa-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\u0400-\u04FFa-zA-Z0-9\\-]+\\.)+))([\u0400-\u04FFa-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$/");
        validationStrings.put(VALIDATION_STRING_EMAIL, "/^[^@]+@[^@]+\\.[^@]+$/");
        validationStrings.put(VALIDATION_STRING_URL, "/^(?#Protocol)(?:(?:ht|f)tp(?:s?)\\:\\/\\/|~\\/|\\/)?(?#Username:Password)(?:\\w+:\\w+@)?(?#Subdomains)(?:(?:[-\\w]+\\.)+(?#TopLevel Domains)(?:com|org|net|gov|mil|biz|info|mobi|name|aero|jobs|museum|travel|[a-z]{2}))(?#Port)(?::[\\d]{1,5})?(?#Directories)(?:(?:(?:\\/(?:[-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?(?#Query)(?:(?:\\?(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?(?:[-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)(?:&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?(?:[-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*(?#Anchor)(?:#(?:[-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?$/");
        validationStrings.put(VALIDATION_STRING_DATE, "dd.m.yyyy");
        validationStrings.put(VALIDATION_STRING_CITY, "/^(([a-zA-Z0-9\\-' .\"\\/]+)|([\u0400-\u04FF0-9\\-' .\"\\/]+))$/");
        validationStrings.put(VALIDATION_STRING_POST_CODE, validationStrings.get(VALIDATION_STRING_CITY));
        validationStrings.put(VALIDATION_STRING_NUMBER, "/^[0-9]+$/");
        validationStrings.put(VALIDATION_STRING_ALL_BUT_QUOTE, "/^.*$/");
    }
    public static Map<String, String> getValidationStrings() {
        return validationStrings;
    }
    public static String getValidationStringForJava(String key) {
        String regex = validationStrings.get(key);
        regex = regex.substring(1, regex.length() - 1);
        return regex;
    }
}
