package com.nacid.web;

import com.nacid.bl.nomenclatures.ApplicationType;

import java.util.LinkedHashMap;
import java.util.Map;

public class ApplicationTypeHelper {
    public static Map<Integer, String> APPLICATION_TYPE_ID_TO_NAME = new LinkedHashMap<>();
    static {
        APPLICATION_TYPE_ID_TO_NAME.put(ApplicationType.RUDI_APPLICATION_TYPE, "RUDi");
        APPLICATION_TYPE_ID_TO_NAME.put(ApplicationType.STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE, "Статус/Автентичност/Препоръка");
        APPLICATION_TYPE_ID_TO_NAME.put(ApplicationType.DOCTORATE_APPLICATION_TYPE, "Признаване на докторски степени");
    }

}
