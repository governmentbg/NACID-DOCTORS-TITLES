package com.nacid.web.model.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.*;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.nomenclatures.*;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.model.applications.base.DocumentRecipientBaseWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationWebModel {
	protected String operationType;
	protected PersonWebModel applicant;
	protected PersonDocumentWebModel applicantDocument;
	protected PersonWebModel representative;
	protected String id;
	//protected String applicationNumber;
	//protected String applicationDate;
	protected String email;
	protected boolean officialEmailCommunication;
	protected String homeCity;
	protected String homePostCode;
	protected String homeAddressDetails;
	protected boolean homeIsBg;
	protected String bgCity;
	protected String bgPostCode;
	protected String bgAddressDetails;
	protected String bgAddressOwner;
	protected String applicationStatusId;
	protected boolean representativeIsCompany;
	protected String bgPhone;
	protected String homePhone;
	protected String reprPhone;
	protected String reprCity;
	protected String reprAddressDetails;
	protected String reprPcode;
	protected String docFlowNumber;
	protected boolean migrated;
	protected String notes;
	
	
	/**
	 * dali se dobavq nov zapis ili se editva star!!!
	 */
	protected boolean newRecord;
	/**
	 * dali da pokazva imenata na podatelq (ako ima vyveden podatel se pokazvat dannite, ako nqma - sa skriti)
	 */
	protected boolean showRepresentative;
	/**
	 * dali da pokazva imenata po diploma - ako ima vyvedeni razli4ni imena po diploma i imena na zaqvitelq, togava trqbva da se vijda sekciqta
	 * "Имената  по документ за самоличност и диплома не съвпадат"
	 */
	protected boolean showDiplomaNames;
	protected boolean bgAddressOwnerChecked;
	protected TrainingCourseWebModel trainingCourseWebModel;
	protected String archiveNumber;
	protected boolean showArchiveNumber;
	/**
	 * shte pokazva koi tab e aktiven (Данни от заявление, обучение, приложения, експерт);
	 */
	protected int activeFormId;
	/**
	 * applicationRecognitionPurposes
	 * key - purposeOfRecognition
	 * value - notes
	 */
	protected Map<Integer, String> applicationRecognitionPurposes = new HashMap<Integer, String>();
	protected List<ApplicationExpertWebModel> applicationExperts = null;
	
	protected boolean allowExpertAssignment;

	protected boolean hasMoreApplicationsWithSameApplicant;
	
    protected Boolean personalDataUsage;
	
    private boolean electronicallyApplied = false;
    private boolean dataAuthentic;
    private boolean representativeAuthorized;
    private String certificateNumber;
    private String summary;
    //private String applicantInfo;
    private String submittedDocs;
    //private boolean showSubmittedDocs;
    private int intAppStatusId;
    protected int applicantTypeId;
    protected int applicationType;
    private CompanyWebModel applicantCompany;
    private String applicationHeader;
    private boolean showRecognizedDetails;
    private DocumentRecipientWebModel documentRecipientWebModel;
    protected String outgoingNumber;
    protected String internalNumber;
    public ApplicationWebModel(Application a, int activeFormId, List<ComissionMember> commissionMembers, NacidDataProvider nacidDataProvider) {
    	
    	newRecord = a == null ? true : false;
		id = a.getId() + "";
		//applicationNumber = a.getApplicationNumber();
		//applicationDate = DataConverter.formatDate(a.getApplicationDate());
		docFlowNumber = a.getDocFlowNumber();
		email = a.getEmail();
		officialEmailCommunication = DataConverter.parseBoolean(a.getOfficialEmailCommunication() + "");
		homeCity = a.getHomeCity();
		homePostCode = a.getHomePostCode();
		homeAddressDetails = a.getHomeAddressDetails();
		homeIsBg = a.homeIsBg();
		bgCity = a.getBgCity();
		bgPostCode = a.getBgPostCode();
		bgAddressDetails = a.getBgAddressDetails();
		bgAddressOwner = a.getBgAddressOwner() + "";
		applicationStatusId = a.getApplicationStatusId() + "";
		showDiplomaNames = a.differentApplicantAndDiplomaNames() ? true : false;
		bgAddressOwnerChecked = a.getBgAddressOwner() == Application.BG_ADDRESS_OWNER_REPRESENTATIVE ? true : false;
		this.reprAddressDetails = a.getReprAddressDetails();
		this.bgPhone = a.getBgPhone();
		this.homePhone = a.getHomePhone();
		this.reprCity = a.getReprCity();
		this.reprPcode = a.getReprPcode();
		this.reprPhone = a.getReprPhone();
		this.archiveNumber = a.getArchiveNumber();
        this.showArchiveNumber = a.getApplicationDocflowStatusId() == ApplicationDocflowStatus.APPLICATION_ARCHIVED_DOCFLOW_STATUS_CODE ? true : false;
        this.personalDataUsage = a.isPersonalDataUsage();
    	this.dataAuthentic = a.getDataAuthentic() == null ? false : a.getDataAuthentic();
    	this.representativeAuthorized = a.getRepresentativeAuthorized() == null ? false : a.getRepresentativeAuthorized();
    	
    	List<ApplicationRecognitionPurpose> recognitionPurposes = a.getApplicationRecoginitionPurposes();
		if (recognitionPurposes != null) {
			for (ApplicationRecognitionPurpose rp:recognitionPurposes) {
				applicationRecognitionPurposes.put(rp.getRecognitionPurposeId(), rp.getNotes());
			}
		}
		List<ApplicationExpert> applicationExperts = a.getApplicationExperts();
		if (applicationExperts != null) {
			this.applicationExperts = new ArrayList<ApplicationExpertWebModel>();
			for (ApplicationExpert ae:applicationExperts) {
                ExpertPosition ep = ae.getExpertPosition();
                ComboBoxWebModel legalReasonsCombo = null;
                if (ep != null && ep.getRelatedAppStatusId() != null) {
                    List<LegalReason> legalReasons = nacidDataProvider.getNomenclaturesDataProvider().getLegalReasons(a.getApplicationType(), null, null, ep.getRelatedAppStatusId());
                    legalReasonsCombo = ComboBoxUtils.generateNomenclaturesComboBox(ae.getLegalReasonId(), legalReasons, false, null, null, true);
                }
                this.applicationExperts.add(new ApplicationExpertWebModel(ae, generateApplicationExpertsCombo(ae.getExpertId(), true, commissionMembers), legalReasonsCombo));
			} 
		}
		this.allowExpertAssignment = a.allowExpertAssignment();
    	this.activeFormId =  activeFormId;
    	this.hasMoreApplicationsWithSameApplicant = a.hasMoreApplicationsWithSameApplicant();
		
    	
    	
		this.applicant = a.getApplicant() == null ? null : new PersonWebModel(a.getApplicant());
		this.applicantDocument = a.getApplicant() == null || a.getApplicant().getPersonDocument() == null ? null : new PersonDocumentWebModel(a.getApplicant().getPersonDocument());

		this.representative = a.getRepresentative() == null ? null : new PersonWebModel(a.getRepresentative());
		this.showRepresentative = representative != null || a.getCompanyId() != null;
		this.representativeIsCompany = showRepresentative && a.getCompanyId() != null;
    	TrainingCourse tc = a.getTrainingCourse();
        this.trainingCourseWebModel = tc == null ? null : new TrainingCourseWebModel(nacidDataProvider, tc);
        this.migrated = a.isMigrated();
        this.notes = a.getNotes();
        this.certificateNumber = a.getCertificateNumber();
        this.summary = a.getSummary();
        //this.applicantInfo = a.getApplicantInfo();
        this.submittedDocs = a.getSubmittedDocs();
        //this.showSubmittedDocs = a.getApplicationStatusId() == ApplicationStatus.APPLICATION_POSTPONED_PRESENTED_DOCS ? true : false;
        this.intAppStatusId = a.getApplicationStatusId();
        this.showRecognizedDetails = (a.getApplicationType() == ApplicationType.DOCTORATE_APPLICATION_TYPE || a.getApplicationType() == ApplicationType.RUDI_APPLICATION_TYPE) && a.getApplicationStatusId() == ApplicationStatus.APPLICATION_PRIZNATO_STATUS_CODE;
        this.applicantTypeId = a.getApplicantType();
        this.applicationType = a.getApplicationType();
        this.applicantCompany = a.getApplicantCompanyId() == null ? null : new CompanyWebModel(a.getApplicantCompany());
        this.documentRecipientWebModel = a.getDocumentRecipient() == null ? null : new DocumentRecipientWebModel(a.getDocumentRecipient());

        generateApplicationHeader(a);
        this.outgoingNumber = a.getOutgoingNumber();
        this.internalNumber = a.getInternalNumber();
    }
    private static ComboBoxWebModel generateApplicationExpertsCombo(Integer selectedKey, boolean addEmpty, List<ComissionMember> comissionMembers) {
        ComboBoxWebModel combobox = new ComboBoxWebModel(selectedKey == null ? null : selectedKey.toString(), addEmpty);
        if (comissionMembers != null) {
            for (ComissionMember c : comissionMembers) {
                if (c.isActive() || (selectedKey != null && c.getId() == selectedKey))
                combobox.addItem("" + c.getId(), c.getFullName() + (c.getDegree() == null || "".equals(c.getDegree())? "" : ",  " + c.getDegree()) +  (c.isActive() ? "": "(inactive)"));
            }
        }
        return combobox;
    }
    
    
    public String getId() {
        return id;
    }
    public String getDocFlowNumber() {
		return docFlowNumber;
	}
	public String getEmail() {
        return email;
    }
    public boolean isOfficialEmailCommunication() {
        return officialEmailCommunication;
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
    public String getBgAddressOwner() {
        return bgAddressOwner;
    }
    public String getApplicationStatusId() {
        return applicationStatusId;
    }
    public boolean showRepresentative() {
        return showRepresentative;
    }
    public boolean showDiplomaNames() {
        return showDiplomaNames;
    }
    public boolean isNewRecord() {
        return newRecord;
    }
    public boolean isBgAddressOwnerChecked() {
        return bgAddressOwnerChecked;
    }
    
    public boolean isRepresentativeIsCompany() {
        return representativeIsCompany;
    }
    public String getBgPhone() {
        return bgPhone;
    }
    public String getHomePhone() {
        return homePhone;
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
    public String getArchiveNumber() {
		return archiveNumber;
	}
	public boolean showArchiveNumber() {
		return showArchiveNumber;
	}
    public PersonWebModel getApplicant() {
        return (PersonWebModel)applicant;
    }

    public PersonDocumentWebModel getApplicantDocument() {
        return applicantDocument;
    }

    public PersonWebModel getRepresentative() {
        return (PersonWebModel)representative;
    }
    public int getActiveFormId() {
        return activeFormId;
    }
    public TrainingCourseWebModel getTrainingCourseWebModel() {
        return (TrainingCourseWebModel)trainingCourseWebModel;
    }
    public Map<Integer, String> getApplicationRecognitionPurposes() {
        return applicationRecognitionPurposes;
    }
    public String getOperationType() {
        return operationType;
    }
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
    public List<ApplicationExpertWebModel> getApplicationExperts() {
        return applicationExperts;
    }
    public boolean allowExpertAssignment() {
        return allowExpertAssignment;
    }
    
	public boolean hasMoreApplicationsWithSameApplicant() {
		return hasMoreApplicationsWithSameApplicant;
	}
	public boolean isElectronicallyApplied() {
		return electronicallyApplied;
	}
	public void setElectronicallyApplied(boolean electronicallyApplied) {
		this.electronicallyApplied = electronicallyApplied;
	}
	public Boolean getPersonalDataUsage() {
		return personalDataUsage;
	}
	public boolean isShowRepresentative() {
		return showRepresentative;
	}
	public boolean isShowDiplomaNames() {
		return showDiplomaNames;
	}
	public boolean isShowArchiveNumber() {
		return showArchiveNumber;
	}
	public boolean isAllowExpertAssignment() {
		return allowExpertAssignment;
	}
	public boolean isHasMoreApplicationsWithSameApplicant() {
		return hasMoreApplicationsWithSameApplicant;
	}
	public boolean isDataAuthentic() {
		return dataAuthentic;
	}
	public boolean isRepresentativeAuthorized() {
		return representativeAuthorized;
	}
	public boolean isMigrated() {
		return migrated;
	}
	public String getNotes() {
		return notes;
	}
	public String getCertificateNumber() {
		return certificateNumber;
	}
	public void generateApplicationHeader(Application a) {
		if (a != null) {
            if (a.getApplicationType() == ApplicationType.RUDI_APPLICATION_TYPE || a.getApplicationType() == ApplicationType.DOCTORATE_APPLICATION_TYPE) {
                applicationHeader = a.getApplicantNames() + " - Деловоден № " + a.getDocFlowNumber();
                if (!StringUtils.isEmpty(a.getCertificateNumber())) {
                    applicationHeader += "<br />" + "Удостоверение № " + getCertificateNumber();
                }
            } else if (a.getApplicationType() == ApplicationType.STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE) {
                applicationHeader = "Деловоден № " + a.getDocFlowNumber();
                applicationHeader += "<br />Заявител: " + a.getApplicantNames()  ;
                applicationHeader += "<br />Собственик на дипломата: " + a.getTrainingCourse().getOwner().getFullName() ;

                List<String> statuses = new ArrayList<>();
                boolean foundNegative = false;
                int countPositive = 0;

                List<ApplicationKind> applicationKinds = a.getApplicationKinds();
                //всеки положителен статус се отбелязва в клас positive_class, първия отрицателен статус се отбелязва с negative_status, и всички след него са с objectless_status. Ако статуса не е нито положителен нито отрицателен, се отбелязва с default_status
                for (ApplicationKind applicationKind : applicationKinds) {
                    String spanClass;
                    if (foundNegative) {
                        spanClass = "objectless_status";
                    } else {
                        ApplicationStatus finalStatus = applicationKind.getFinalStatus();
                        if (finalStatus != null) {
                            boolean positive = ApplicationStatus.STATUTE_AUTHENTICITY_RECOMMENDATION_POSITIVE_NEGATIVE_STATUS_CODES.get(applicationKind.getEntryNumSeriesId()).get(finalStatus.getId());
                            if (positive) {
                                spanClass = "positive_status";
                                countPositive++;
                            } else {
                                spanClass = "negative_status";
                                foundNegative = true;
                            }
                        } else {
                            spanClass = "default_status";
                        }
                    }
                    statuses.add("<span class=\"" + spanClass + "\">" + applicationKind.getEntryNumSeriesName() + "</span>");

                }
                //ako docflow статуса е "в процедура" и има един negative status или всички статуси са positive, тогава заявлението е за приключване!
                applicationHeader += "<br />" + StringUtils.join(statuses, "  ");
                if (a.getApplicationDocflowStatusId() == ApplicationDocflowStatus.APPLICATION_VPROCEDURA_DOCFLOW_STATUS_CODE) {
                    if (countPositive == applicationKinds.size()) {
                        applicationHeader += "<span class=\"positive_status\"> - ЗА ПРИКЛЮЧВАНЕ</span>";
                    } else if (foundNegative) {
                        applicationHeader += "<span class=\"negative_status\"> - ЗА ПРИКЛЮЧВАНЕ</span>";
                    }
                }

            }

		}
	}
    public String getApplicationHeader() {
        return applicationHeader;
    }
	public String getSummary() {
		return summary;
	}
	public String getSubmittedDocs() {
		return submittedDocs;
	}
    public int getIntAppStatusId() {
        return intAppStatusId;
    }

    public int getApplicantTypeId() {
        return applicantTypeId;
    }

    public int getApplicationType() {
        return applicationType;
    }

    public CompanyWebModel getApplicantCompany() {
        return applicantCompany;
    }

    public boolean isShowRecognizedDetails() {
        return showRecognizedDetails;
    }

    public DocumentRecipientWebModel getDocumentRecipientWebModel() {
        return documentRecipientWebModel;
    }

    public String getOutgoingNumber() {
        return outgoingNumber;
    }

    public String getInternalNumber() {
        return internalNumber;
    }
}
