package com.nacid.web.model.inquires;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.BaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by georgi.georgiev on 14.09.2016.
 */
public class BgAcademicRecognitionInquireWebModel {


    private List<String> ownerNames;
    private List<String> citizenshipNames;
    private List<String> universityNames;
    private List<String> universityCountryNames;
    private List<String> diplomaSpecialities;
    private List<String> diplomaEducationLevelNames;
    private String prototolNumber;
    private String denialPrototolNumber;
    private List<String> recognizedSpecialityNames;
    private List<Integer> universityIds;
    private String inputNumber;
    private String outputNumber;
    private List<Integer> recognitionStatuses;


    public BgAcademicRecognitionInquireWebModel(HttpServletRequest request) {
        NacidDataProvider nacidDataProvider = BaseRequestHandler.getNacidDataProvider(request.getSession());
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();

        ownerNames = generateStringElementList(request, "ownerIds", "ownerId", false);
        ownerNames = addMaskedElements(request, "ownerNamesIds", "owner", citizenshipNames, "owner", nomenclaturesDataProvider);

        //pyrvo slaga exact match elementite (doshli ot select na drop-down-a), sled tova slaga vsichki maski ot vida na бълг*
        citizenshipNames = generateStringElementList(request, "citizenshipIds", "citizenshipId", false);
        citizenshipNames = addMaskedElements(request, "citizenshipNamesIds", "citizenship", citizenshipNames, "citizenship", nomenclaturesDataProvider);
        //end of citizenship

        universityNames = generateStringElementList(request, "universityIds", "universityId", false);
        universityNames = addMaskedElements(request, "universityNamesIds", "university", universityNames, "university", nomenclaturesDataProvider);

        universityCountryNames = generateStringElementList(request, "universityCountryIds", "universityCountryId", false);
        universityCountryNames = addMaskedElements(request, "universityCountryNamesIds", "universityCountry", universityCountryNames, "university_country", nomenclaturesDataProvider);

        diplomaSpecialities = generateStringElementList(request, "diplomaSpecialityIds", "diplomaSpecialityId", false);
        diplomaSpecialities = addMaskedElements(request, "diplomaSpecialityNamesIds", "diplomaSpeciality", diplomaSpecialities, "diploma_speciality", nomenclaturesDataProvider);


        diplomaEducationLevelNames = generateStringElementList(request, "diplomaEducationLevelIds", "diplomaEducationLevelId", false);
        diplomaEducationLevelNames = addMaskedElements(request, "diplomaEducationLevelNamesIds", "diplomaEducationLevel", diplomaEducationLevelNames, "education_level", nomenclaturesDataProvider);

        prototolNumber = addWildcardSymbols(DataConverter.parseString(request.getParameter("protocol_number"), null));
        denialPrototolNumber = addWildcardSymbols(DataConverter.parseString(request.getParameter("denial_protocol_number"), null));

        recognizedSpecialityNames = generateStringElementList(request, "recognizedSpecialityIds", "recognizedSpecialityId", false);
        recognizedSpecialityNames = addMaskedElements(request, "recognizedSpecialityNamesIds", "recognizedSpeciality", recognizedSpecialityNames, "recognized_speciality", nomenclaturesDataProvider);

        List<Integer> universityCountryIds = new ArrayList<>();
        universityIds = new ArrayList<>();

        if (!InquiresUtils.generateUniversityCountryAndUniversityIds(request, universityIds, universityCountryIds, nacidDataProvider) && universityIds.size() == 0) {
            universityIds = null;
        }
        inputNumber = addWildcardSymbols(DataConverter.parseString(request.getParameter("input_number"), null));
        outputNumber = addWildcardSymbols(DataConverter.parseString(request.getParameter("output_number"), null));
        recognitionStatuses = InquiresUtils.generateElementList(request, "recognitionStatusIds", "recognitionStatus");
    }






    public static List<String> addMaskedElements(HttpServletRequest request, String idsAttributeName, String idAttributeName, List<String> currentIds, String type, NomenclaturesDataProvider nomenclaturesDataProvider) {
        Set<String> result = new HashSet<>();
        if (!Utils.isEmpty(currentIds)) {
            result.addAll(currentIds);
        }

        TreeSet<String> attributes = RequestParametersUtils.convertRequestParamToList(request.getParameter(idsAttributeName));
        if (attributes == null) {
            attributes = new TreeSet<>();
        }
        String attrId = idAttributeName == null ? null : DataConverter.parseString(request.getParameter(idAttributeName), null);
        if (attrId != null) {
            attributes.add(attrId);
        }
        for (String attribute : attributes) {
            attribute = addWildcardSymbols(attribute);
            List<String> records = nomenclaturesDataProvider.getBgAcademicRecognitionSuggestion(type, attribute);
            if (!Utils.isEmpty(records)) {
                result.addAll(records);
            }
        }
        return result.size() == 0 ? null : new ArrayList<>(result);

    }
    private static List<String> generateStringElementList(HttpServletRequest request, String idsAttributeName, String idAttributeName, boolean addWildCardSymbols) {
        //Tozi method dobavq i elementa, koito e v combobox-a, no ne e dobaven v gorniq spisyk!!!
        TreeSet<String> attributes = RequestParametersUtils.convertRequestParamToList(request.getParameter(idsAttributeName));
        String attrId = idAttributeName == null ? null : DataConverter.parseString(request.getParameter(idAttributeName), null);
        if (attrId != null) {
            if (attributes == null) {
                attributes = new TreeSet<>();
            }
            attributes.add(attrId);
        }


        List<String> result = null;
        if (attributes != null) {
            result = new ArrayList<>();
            for (String str : attributes) {
                result.add(addWildCardSymbols ? addWildcardSymbols(str) : str);
            }
        }

        return result;
    }

    private static final String addWildcardSymbols(String parameterValue) {
        if (StringUtils.isEmpty(parameterValue)) {
            return null;
        }
        if (parameterValue.charAt(parameterValue.length() -1 ) != '*') {
            parameterValue += "*";
        }
        parameterValue = parameterValue.replaceAll("\\*", "%");
        return parameterValue;
    }

    public List<String> getCitizenshipNames() {
        return citizenshipNames;
    }

    public List<String> getUniversityNames() {
        return universityNames;
    }

    public List<String> getUniversityCountryNames() {
        return universityCountryNames;
    }

    public List<String> getDiplomaSpecialities() {
        return diplomaSpecialities;
    }

    public List<String> getDiplomaEducationLevelNames() {
        return diplomaEducationLevelNames;
    }

    public String getPrototolNumber() {
        return prototolNumber;
    }

    public String getDenialPrototolNumber() {
        return denialPrototolNumber;
    }

    public List<String> getRecognizedSpecialityNames() {
        return recognizedSpecialityNames;
    }

    public List<Integer> getUniversityIds() {
        return universityIds;
    }

    public String getInputNumber() {
        return inputNumber;
    }

    public String getOutputNumber() {
        return outputNumber;
    }

    public List<Integer> getRecognitionStatuses() {
        return recognitionStatuses;
    }

    public List<String> getOwnerNames() {
        return ownerNames;
    }
}
