package com.nacid.web.model.applications.report.base;

import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.base.ApplicationBase;
import com.nacid.bl.nomenclatures.ApplicationType;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.data.DataConverter;
import com.nacid.web.ApplicantTypeHelper;
import com.nacid.web.model.applications.base.DocumentRecipientBaseWebModel;

/**
 * tozi klas sydyrja obshtite chasti mejdu vytre6niq i vyn6niq application
 * @author ggeorgiev
 *
 */
public class ApplicationForReportBaseWebModel {

	private boolean rudiApplication;
    private boolean doctorateApplication;
	private boolean sarApplication;
    private String applicationNumber;
    private String applicationDate;

    protected PersonForReportBaseWebModel applicant;
    protected PersonForReportBaseWebModel representative;
	protected String email;
	protected String id;
	protected int intId;
	protected String homeCity;
	protected String homePostCode;
	protected String homeAddressDetails;
	protected String bgCity;
	protected String bgPostCode;
	protected String bgAddressDetails;
	protected String bgPhone;
	protected String homePhone;
	protected String homeCountry;
	protected String recognitionPurposes;


	
	/**
	 * dali se dobavq nov zapis ili se editva star!!!
	 */
	protected boolean newRecord;
	/**
	 * dali da pokazva imenata po diploma - ako ima vyvedeni razli4ni imena po diploma i imena na zaqvitelq, togava trqbva da se vijda sekciqta
	 * "Имената  по документ за самоличност и диплома не съвпадат"
	 */
	protected boolean showDiplomaNames;
	protected TrainingCourseForReportBaseWebModel trainingCourseWebModel;
	protected Boolean personalDataUsage;
	protected Boolean dataAuthentic;
    protected CompanyForReportBaseWebModel applicantCompany;
	protected String applicantType;
    protected int applicantTypeId;
    protected String documentReceiveMethod;
    protected boolean showOwnerDetails;
    protected DocumentRecipientBaseWebModel documentRecipient;
	
	/**
	 * 
	 * @param a
	 */
	public ApplicationForReportBaseWebModel(ApplicationBase a) {
		newRecord = a == null ? true : false;
		id = a.getId() + "";
		this.intId = a.getId();
		homeCity = a.getHomeCity();
		homePostCode = a.getHomePostCode();
		homeAddressDetails = a.getHomeAddressDetails();
		bgCity = a.getBgCity();
		bgPostCode = a.getBgPostCode();
		bgAddressDetails = a.getBgAddressDetails();
		showDiplomaNames = a.differentApplicantAndDiplomaNames() ? true : false;
		this.bgPhone = a.getBgPhone();
		this.homePhone = a.getHomePhone();
		Country homeCountry = a.getHomeCountry();
		this.homeCountry = homeCountry == null ? "" : homeCountry.getName();
		personalDataUsage = a.isPersonalDataUsage();
		this.dataAuthentic = a.getDataAuthentic();
        this.applicantTypeId = a.getApplicantType();
        this.applicantType = ApplicantTypeHelper.getApplicantTypeName(a.getApplicantType());
        this.applicationNumber = a.getApplicationNumber();
        this.applicationDate = DataConverter.formatDate(a.getApplicationDate());
        rudiApplication = a.getApplicationType() == ApplicationType.RUDI_APPLICATION_TYPE;
        doctorateApplication = a.getApplicationType() == ApplicationType.DOCTORATE_APPLICATION_TYPE;
        this.documentReceiveMethod = a.getDocumentReceiveMethod() == null ? "" : a.getDocumentReceiveMethod().getName();
        this.showOwnerDetails = a.getApplicationType() == ApplicationType.STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE;
		sarApplication = a.getApplicationType() == ApplicationType.STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE;


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
    public boolean isNewRecord() {
        return newRecord;
    }
    public String getBgPhone() {
        return bgPhone;
    }
    public String getHomePhone() {
        return homePhone;
    }
	public PersonForReportBaseWebModel getApplicant() {
		return (PersonForReportBaseWebModel)applicant;
	}
	public TrainingCourseForReportBaseWebModel getTrainingCourseWebModel() {
		return (TrainingCourseForReportBaseWebModel) trainingCourseWebModel;
	}
	public String getHomeCountry() {
		return homeCountry;
	}
	public String getRecognitionPurposes() {
		return recognitionPurposes;
	}
	public String getEmail() {
		return email;
	}
	public Boolean isPersonalDataUsage() {
		return personalDataUsage;
	}
	public Boolean isDataAuthentic() {
		return dataAuthentic;
	}

    public CompanyForReportBaseWebModel getApplicantCompany() {
        return applicantCompany;
    }

    public String getApplicantType() {
        return applicantType;
    }

    public PersonForReportBaseWebModel getRepresentative() {
        return representative;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public int getApplicantTypeId() {
        return applicantTypeId;
    }

    public boolean isRudiApplication() {
        return rudiApplication;
    }

	public String getDocumentReceiveMethod() {
		return documentReceiveMethod;
	}

	public boolean isDoctorateApplication() {
		return doctorateApplication;
	}

	public boolean isShowOwnerDetails() {
		return showOwnerDetails;
	}

	public boolean isSarApplication() {
		return sarApplication;
	}

	public DocumentRecipientBaseWebModel getDocumentRecipient() {
		return documentRecipient;
	}

	public int getIntId() {
		return intId;
	}
}
