package com.nacid.data.utils;

import com.nacid.bl.utils.UniversityDetail;
import com.nacid.data.annotations.Table;

/**
 * Created by georgi.georgiev on 23.10.2015.
 */
@Table(name="nomenclatures.university_details", sequence = "nomenclatures.university_details_id_seq")
public class UniversityDetailRecord implements UniversityDetail {
    private int id;
    private String universityName;
    private String letterRecipient;
    private String salutation;

    public UniversityDetailRecord() {
    }

    public UniversityDetailRecord(int id, String universityName, String letterRecipient, String salutation) {
        this.id = id;
        this.universityName = universityName;
        this.letterRecipient = letterRecipient;
        this.salutation = salutation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getLetterRecipient() {
        return letterRecipient;
    }

    public void setLetterRecipient(String letterRecipient) {
        this.letterRecipient = letterRecipient;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }
}
