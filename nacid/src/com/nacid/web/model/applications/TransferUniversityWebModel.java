package com.nacid.web.model.applications;

import com.nacid.bl.applications.University;
import com.nacid.bl.external.applications.ExtUniversity;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;

/**
 * Created by georgi.georgiev on 05.11.2015.
 */
public class TransferUniversityWebModel extends UniversityWebModel{
    private boolean standardUniversity;
    private String universityTxt;
    private Integer intCountryId;
    public TransferUniversityWebModel(University uni) {
        super(uni);
        this.standardUniversity = true;
        this.intCountryId = uni.getCountryId() == 0 ? null : uni.getCountryId();
    }

    public TransferUniversityWebModel(ExtUniversity university, NomenclaturesDataProvider nomenclaturesDataProvider) {
        super(university);
        this.standardUniversity = university.isStandartUniversity();
        if (!standardUniversity) {
            this.universityTxt = university.getUniversityTxt();
            String[] parts = universityTxt.split(",");

            //v tozi sluchai unuiversiteta e vyv vida "Наименивание, град, държава"
            if (parts.length > 0) {
                bgName = parts[0].trim();
            }
            if (parts.length > 1) {
                city = parts[1].trim();
            }
            if (parts.length > 2) {
                country = parts[2].trim();
                Country c = nomenclaturesDataProvider.getCountryByName(country);
                if (c != null) {
                    countryId = c.getId() + "";
                    intCountryId = c.getId();
                }
            }

            //probva da prochete dyrjavata ot vtoriq element...
            if ((countryId == null || countryId.equals("0")) && parts.length > 1) {
                Country c = nomenclaturesDataProvider.getCountryByName(parts[1].trim());
                if (c != null) {
                    country = c.getName();
                    countryId = c.getId() + "";
                    intCountryId = c.getId();
                    city = "";
                }
            }
        }
    }

    public boolean isStandardUniversity() {
        return standardUniversity;
    }

    public String getUniversityTxt() {
        return universityTxt;
    }

    public Integer getIntCountryId() {
        return intCountryId;
    }

    private static String getBgName(String fullName) {
        String[] parts = fullName.split(",");
        if (parts.length > 0) {
            return parts[0].trim();
        }
        return null;
    }

}
