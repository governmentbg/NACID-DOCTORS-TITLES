package com.nacid.web.model.nomenclatures;

import com.nacid.bl.utils.UniversityDetail;

public class UniversityDetailWebModel {
	private String id;
    private String universityName;
    private String letterRecipient;
    private String salutation;

	public UniversityDetailWebModel(String id, String universityName, String letterRecipient, String salutation) {
		this.id = id;
		this.universityName = universityName;
        this.letterRecipient = letterRecipient;
        this.salutation = salutation;
	}
	public UniversityDetailWebModel(UniversityDetail ud) {
		this.id = ud.getId() + "";
		this.universityName = ud.getUniversityName();
        this.letterRecipient = ud.getLetterRecipient();
        this.salutation = ud.getSalutation();
	}

    public String getId() {
        return id;
    }

    public String getUniversityName() {
        return universityName;
    }

    public String getLetterRecipient() {
        return letterRecipient;
    }

    public String getSalutation() {
        return salutation;
    }
}
