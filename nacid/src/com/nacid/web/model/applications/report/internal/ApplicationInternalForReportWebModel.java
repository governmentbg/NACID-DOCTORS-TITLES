package com.nacid.web.model.applications.report.internal;

import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.ApplicationRecognitionPurpose;
import com.nacid.bl.applications.Person;
import com.nacid.bl.applications.TrainingCourse;
import com.nacid.bl.nomenclatures.ApplicationDocflowStatus;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.ExternalApplicationStatus;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.web.model.applications.DocumentRecipientWebModel;
import com.nacid.web.model.applications.report.base.ApplicationForReportBaseWebModel;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
/**
 * tozi klas sydyrja razli4iqta, koito se polzvat samo vyv report-a na zaqvlenieto za vyn6tnoto prilojenie
 * @author ggeorgiev
 *
 */
public class ApplicationInternalForReportWebModel extends ApplicationForReportBaseWebModel {


	
	private boolean officialEmailCommunication;
	private boolean homeIsBg;
	protected boolean bgAddressOwnerChecked;
	private String applicationStatusId;
	private String applicationStatusForApplicant;
	private boolean representativeIsCompany;
	private boolean showRepresentative;
	
	private String reprPhone;
	private String reprCity;
	private String reprAddressDetails;
	private String reprPcode;
	

	private String applicationStatus;
	protected String archiveNumber;
	protected boolean showArchiveNumber;
	private boolean representativeAuthorized;
	private String certificateNumber;
	private String submittedDocs;
	private String applicantInfo;
	private boolean isRecognizedAndContainsProtocol;
	private String representativeCountry;

    private CompanyForReportWebModel company;
	/**
	 * 
	 * @param a
	 */
	public ApplicationInternalForReportWebModel(Application a) {
		super(a);
		Person applicant = a.getApplicant();
		this.applicant = applicant == null ? null : new PersonInternalForReportWebModel(a.getApplicant());
		Person representative = a.getRepresentative();
		this.representative = representative == null ? null : new PersonInternalForReportWebModel(representative);
		bgAddressOwnerChecked = a.getBgAddressOwner() == Application.BG_ADDRESS_OWNER_REPRESENTATIVE ? true : false;
		Country reprCountry = a.getReprCountry();
		this.officialEmailCommunication = a.getOfficialEmailCommunication() == 1 ? true : false;
		this.email = a.getEmail();
		representativeCountry = reprCountry == null ? "" : reprCountry.getName();
		
		showRepresentative = representative != null || a.getCompanyId() != null;
        representativeIsCompany = showRepresentative && a.getCompanyId() != null;
    	
		TrainingCourse tc = a.getTrainingCourse();
        this.trainingCourseWebModel = tc == null ? null : new TrainingCourseInternalForReportWebModel(tc);
        
        
        List<String> lst = new ArrayList<String>();
		List<ApplicationRecognitionPurpose> recognitionPurposesList = a.getApplicationRecoginitionPurposes();
		if (recognitionPurposesList == null) {
			this.recognitionPurposes = "";
		} else {
			for (ApplicationRecognitionPurpose gw:recognitionPurposesList) {
				lst.add(gw.getRecognitionPurposeNotes());
			}
			this.recognitionPurposes = StringUtils.join(lst, ", ");	
		}
		FlatNomenclature status = a.getApplicationStatus();
		this.applicationStatus = status == null ? null : status.getName();
		this.applicationStatusForApplicant = status == null ? null : ExternalApplicationStatus.getExternalStatusName(status);
		
		this.archiveNumber = a.getArchiveNumber();
		this.showArchiveNumber = a.getApplicationDocflowStatusId() == ApplicationDocflowStatus.APPLICATION_ARCHIVED_DOCFLOW_STATUS_CODE ? true : false;
		this.representativeAuthorized = a.getRepresentativeAuthorized() == null ? false : a.getRepresentativeAuthorized();
		this.certificateNumber = a.getCertificateNumber();
		if (!StringUtils.isEmpty(certificateNumber)) {
			certificateNumber = "№ " + certificateNumber;
		}
		this.applicantInfo = a.getApplicantInfo();
		this.submittedDocs = a.getSubmittedDocs();
		this.isRecognizedAndContainsProtocol = a.isRecognizedAndContainsCommissionProtocol();
		this.company = a.getCompany() == null ? null : new CompanyForReportWebModel(a.getCompany());
        this.applicantCompany = a.getApplicantCompanyId() == null ? null : new CompanyForReportWebModel(a.getApplicantCompany());
        this.documentRecipient = a.getDocumentRecipient() == null ? null : new DocumentRecipientWebModel(a.getDocumentRecipient());
		
	}
	public PersonInternalForReportWebModel getApplicant() {
		return (PersonInternalForReportWebModel)applicant;
	}

    @Override
    public CompanyForReportWebModel getApplicantCompany() {
        return (CompanyForReportWebModel) super.getApplicantCompany();
    }

    public PersonInternalForReportWebModel getRepresentative() {
		return (PersonInternalForReportWebModel)representative;
	}
	public TrainingCourseInternalForReportWebModel getTrainingCourseWebModel() {
		return (TrainingCourseInternalForReportWebModel) trainingCourseWebModel;
	}
	
	public String getApplicationStatus() {
		return applicationStatus;
	}

	public boolean isOfficialEmailCommunication() {
		return officialEmailCommunication;
	}
	public boolean isHomeIsBg() {
		return homeIsBg;
	}
	
	public String getApplicationStatusId() {
		return applicationStatusId;
	}
	public boolean isRepresentativeIsCompany() {
		return representativeIsCompany;
	}
	public boolean isShowRepresentative() {
		return showRepresentative;
	}
	public String getReprPhone() {
		return reprPhone;
	}
	public String getReprCity() {
		return reprCity;
	}
	public String getReprAddressDetails() {
		return reprAddressDetails;
	}
	public String getReprPcode() {
		return reprPcode;
	}
	public boolean isBgAddressOwnerChecked() {
        return bgAddressOwnerChecked;
    }
	public String getArchiveNumber() {
		return archiveNumber;
	}
	public boolean showArchiveNumber() {
		return showArchiveNumber;
	}
	public boolean isRepresentativeAuthorized() {
		return representativeAuthorized;
	}
	public String getCertificateNumber() {
		return certificateNumber;
	}
	public String getSubmittedDocs() {
		return submittedDocs;
	}
	public String getApplicantInfo() {
		return applicantInfo;
	}
	public boolean isRecognizedAndContainsProtocol() {
		return isRecognizedAndContainsProtocol;
	}
	public String getApplicationStatusForApplicant() {
		return applicationStatusForApplicant;
	}
	public boolean isShowArchiveNumber() {
		return showArchiveNumber;
	}
    public CompanyForReportWebModel getCompany() {
        return (CompanyForReportWebModel) company;
    }

    public String getRepresentativeCountry() {
        return representativeCountry;
    }

    @Override
	public DocumentRecipientWebModel getDocumentRecipient() {
		return (DocumentRecipientWebModel) documentRecipient;
	}
}
