package com.ext.nacid.web.model.applications;

import com.ext.nacid.web.model.ExtPersonWebModel;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.external.applications.ExtApplication;
import com.nacid.bl.external.applications.ExtTrainingCourse;
import com.nacid.bl.nomenclatures.Country;

import java.util.Objects;

public class ExtApplicationWebModel {
    private ExtPersonWebModel applicant;
    private ExtPersonWebModel representative;
    private String id = "0";
    private String homeCity = "";
    private String homePostCode = "";
    private String homeAddressDetails = "";
    private boolean homeIsBg = false;
    private String bgCity = "";
    private String bgPostCode = "";
    private String bgAddressDetails = "";
    private int homeCountryId = Country.COUNTRY_ID_BULGARIA;
    private String bgPhone = "";
    private String homePhone = "";
    private boolean isNew;
    private int status;
    private String diplFName;
    private String diplSName;
    private String diplLName;
    /**
     * dali da pokazva imenata po diploma - ako ima vyvedeni razli4ni imena po diploma i imena na zaqvitelq, togava trqbva da se vijda sekciqta
     * "Имената  по документ за самоличност и диплома не съвпадат"
     */
    private boolean showDiplomaNames;

    /**
     * shte pokazva koi tab e aktiven (Данни от заявление, обучение, приложения);
     */
    private int activeFormId;
    //private TrainingCourseWebModel trainingCourseWebModel;
    /**
     * applicationRecognitionPurposes
     * key - purposeOfRecognition
     * value - notes
     */
    //private Map<Integer, String> applicationRecognitionPurposes = new HashMap<Integer, String>();
    
    //shte pokazva dali dadenoto zaqvlenie e prehvyrleno vyv vytre6nata baza
    private boolean hasInternalApplication = false;
    //shte pokazva dali zaqvlenieto e podadeno (t.e. dali shte trqbva da se pokazva taba podavane)
    private boolean isApplied;
    private boolean personalDataUsage;
    private boolean dataAuthentic;
    private boolean differentApplicantRepresentative;
    private int applicationType;
    private ExtDocumentRecipientWebModel documentRecipientWebModel;
    public ExtApplicationWebModel(ExtApplication a, int activeFormId) {
        applicant = a.getApplicant() == null ? null : new ExtPersonWebModel(a.getApplicant());
        this.representative = a.getRepresentative() == null ? null : new ExtPersonWebModel(a.getRepresentative());
        id = a.getId() + "";
        isNew = false;
        homeCity = a.getHomeCity();
        homePostCode = a.getHomePostCode();
        homeAddressDetails = a.getHomeAddressDetails();
        homeIsBg = a.homeIsBg();
        bgCity = a.getBgCity();
        bgPostCode = a.getBgPostCode();
        bgAddressDetails = a.getBgAddressDetails();
        showDiplomaNames = a.differentApplicantAndDiplomaNames() ? true : false;
        this.activeFormId =  activeFormId;
        homeCountryId = a.getHomeCountryId();
        this.status = a.getApplicationStatus();
        ExtTrainingCourse tc = a.getTrainingCourse();
        this.diplFName = tc.getFName();
        this.diplSName = tc.getSName();
        this.diplLName = tc.getLName();
        /*List<ApplicationRecognitionPurpose> recognitionPurposes = a.getApplicationRecoginitionPurposes();
        if (recognitionPurposes != null) {
            for (ApplicationRecognitionPurpose rp:recognitionPurposes) {
                applicationRecognitionPurposes.put(rp.getRecognitionPurposeId(), rp.getNotes());
            }
        }*/
        
        this.bgPhone = a.getBgPhone();
        this.homePhone = a.getHomePhone();
        this.hasInternalApplication = a.getInternalApplicationId() != null;
        this.isApplied = a.getApplicationStatus() != ExtApplication.STATUS_EDITABLE;
        this.personalDataUsage = a.isPersonalDataUsage() == null ? false : a.isPersonalDataUsage();
        this.dataAuthentic = a.getDataAuthentic() == null ? false : a.getDataAuthentic();
        this.applicationType = a.getApplicationType();
        this.documentRecipientWebModel = a.getDocumentRecipient() == null ? null : new ExtDocumentRecipientWebModel(a.getDocumentRecipient());
        this.differentApplicantRepresentative = !Objects.equals(a.getApplicantId(), a.getRepresentativeId());
    }
    
	public ExtApplicationWebModel(ExtPerson representative, ExtPerson applicant, int id, String homeCity, String homePostCode, String homeAddressDetails,
			boolean homeIsBg, String bgCity, String bgPostCode, String bgAddressDetails, int homeCountryId, String bgPhone, String homePhone,
			boolean showDiplomaNames, int activeFormId, Boolean personalDataUsage, Boolean dataAuthentic, int applicationType) {
        this.representative = new ExtPersonWebModel(representative);
		this.applicant = new ExtPersonWebModel(applicant);
		this.id = id + "";
		this.isNew = id == 0;
		this.homeCity = homeCity;
		this.homePostCode = homePostCode;
		this.homeAddressDetails = homeAddressDetails;
		this.homeIsBg = homeIsBg;
		this.bgCity = bgCity;
		this.bgPostCode = bgPostCode;
		this.bgAddressDetails = bgAddressDetails;
		this.homeCountryId = homeCountryId;
		this.bgPhone = bgPhone;
		this.homePhone = homePhone;
		this.showDiplomaNames = showDiplomaNames;
		this.activeFormId = activeFormId;
		this.personalDataUsage = personalDataUsage;
		this.dataAuthentic = dataAuthentic;
		this.applicationType = applicationType;
        this.differentApplicantRepresentative = !Objects.equals(applicant.getId(), representative.getId());
	}
	/**
	 * tozi konstruktor se polzva pri syzdavane na nov application
	 * @param representative
	 * @param activeFormId
	 */
	public ExtApplicationWebModel(ExtPerson representative, int activeFormId, int applicationType) {
    	this.representative = new ExtPersonWebModel(representative);
    	this.activeFormId = activeFormId;
    	this.isNew = true;
    	this.personalDataUsage = false;
    	this.applicationType = applicationType;
    }
    public ExtPersonWebModel getApplicant() {
        return applicant;
    }
    public String getId() {
        return id;
    }
    public String getHomeCity() {
        return homeCity;
    }
    public String getHomePostCode() {
        return homePostCode;
    }
    public String getHomeAddressDetails() {
        return homeAddressDetails;
    }
    public boolean isHomeIsBg() {
        return homeIsBg;
    }
    public String getBgCity() {
        return bgCity;
    }
    public String getBgPostCode() {
        return bgPostCode;
    }
    public String getBgAddressDetails() {
        return bgAddressDetails;
    }
    public boolean showDiplomaNames() {
        return showDiplomaNames;
    }
    
    public int getActiveFormId() {
        return activeFormId;
    }
    public void setActiveFormId(int activeFormId) {
    	this.activeFormId = activeFormId;
    }
   public String getBgPhone() {
        return bgPhone;
    }
    public String getHomePhone() {
        return homePhone;
    }
	public int getHomeCountryId() {
		return homeCountryId;
	}
	public boolean isNew() {
		return isNew;
	}
	public int getStatus() {
		return status;
	}

	public String getDiplFName() {
		return diplFName;
	}

	public String getDiplSName() {
		return diplSName;
	}

	public String getDiplLName() {
		return diplLName;
	}
	public boolean hasInternalApplication() {
		return hasInternalApplication;
	}

	public boolean isApplied() {
		return isApplied;
	}

	public boolean isPersonalDataUsage() {
		return personalDataUsage;
	}
	public boolean isDataAuthentic() {
		return dataAuthentic;
	}

    public int getApplicationType() {
        return applicationType;
    }

    public ExtDocumentRecipientWebModel getDocumentRecipientWebModel() {
        return documentRecipientWebModel;
    }

    public boolean isDifferentApplicantRepresentative() {
        return differentApplicantRepresentative;
    }

    public ExtPersonWebModel getRepresentative() {
        return representative;
    }
}
