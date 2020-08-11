package com.nacid.web.model.applications;

import com.nacid.bl.applications.SimilarDiploma;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.DataConverter;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by georgi.georgiev on 09.10.2015.
 */
public class SimilarDiplomaWebModel {
    private String applicationId;
    private String diplomaYear;
    private String eduLevelName;
    private String docflowNumber;
    private String civilId;
    private String fullName;
    private String specialityNames;
    private String universityNames;
    private String universityCountryNames;



    public SimilarDiplomaWebModel(SimilarDiploma d, NomenclaturesDataProvider nomenclaturesDataProvider, String civilId, String firstName, String secondName, String lastName, List<Integer> universityCountryIds, List<String> universityCountryNames, List<String> specialityNames, final List<String> universityNames, Integer eduLevelId, Integer diplomaYear) {
        this.applicationId = d.getId() + "";

        Calendar diplomaDate = Calendar.getInstance();
        diplomaDate.setTime(d.getDiplomaDate());
        this.diplomaYear = setValue(DataConverter.formatYear(d.getDiplomaDate()), diplomaYear != null && diplomaDate.get(Calendar.YEAR) == diplomaYear);
        this.eduLevelName = setValue(d.getEduLevelName(), d.getEduLevelId() == eduLevelId);
        this.docflowNumber = d.getAppNum() + "/" + DataConverter.formatDate(d.getAppDate());
        this.civilId = setValue(d.getCivilId(), d.getCivilId() != null && d.getCivilId().equalsIgnoreCase(civilId));
        this.fullName = setValue(d.getFname(), d.getFname() != null && d.getFname().equalsIgnoreCase(firstName));
        this.fullName += " " + setValue(d.getSname(), d.getSname() != null && d.getSname().equalsIgnoreCase(secondName));
        this.fullName += " " + setValue(d.getLname(), d.getLname() != null && d.getLname().equalsIgnoreCase(lastName));

        this.universityCountryNames = d.getUniversityCountryIds().stream().map(countryId -> {
            String countryName = nomenclaturesDataProvider.getCountry(countryId).getName();
            return setValue(countryName, (universityCountryIds != null && universityCountryIds.contains(countryId)) || (universityCountryNames != null && universityCountryNames.contains(countryName)));
        }).collect(Collectors.joining(", "));


        this.universityNames = d.getUniversityNames().stream().map(un -> setValue(un, universityNames != null && universityNames.contains(un))).collect(Collectors.joining(", "));
        this.specialityNames = d.getSpecialityNames().stream().map(sp -> setValue(sp, specialityNames != null && specialityNames.contains(sp))).collect(Collectors.joining(", "));

    }

    private static String setValue(String foundValue, boolean setFieldColor) {
        String result =  foundValue == null ? "" : foundValue;
        if (setFieldColor) {
            result = "<span class=\"red\">" + result + "</span>";
        }

        return result;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getDiplomaYear() {
        return diplomaYear;
    }

    public String getEduLevelName() {
        return eduLevelName;
    }

    public String getDocflowNumber() {
        return docflowNumber;
    }

    public String getCivilId() {
        return civilId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSpecialityNames() {
        return specialityNames;
    }

    public String getUniversityNames() {
        return universityNames;
    }

    public String getUniversityCountryNames() {
        return universityCountryNames;
    }
}
