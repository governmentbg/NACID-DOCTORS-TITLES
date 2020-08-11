package com.nacid.bl.applications;

import com.nacid.bl.applications.base.ApplicationBase;
import com.nacid.bl.comision.CommissionCalendar;
import com.nacid.bl.external.applications.ExtApplication;
import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.FlatNomenclature;

import java.util.List;

public interface Application extends ApplicationBase {

	public Person getApplicant();
    public Company getApplicantCompany();

	public Person getRepresentative();
	public Integer getRepresentativeId();
	public TrainingCourse getTrainingCourse();
	public String getEmail();
	public int getOfficialEmailCommunication();

	public FlatNomenclature getFinalAppStatus();

	public int getBgAddressOwner();
	public Integer getReprCountryId();
	public Country getReprCountry();
	public String getReprCity();
	public String getReprPcode();
	public String getReprAddressDetails();
	public String getReprPhone();

	/**
	 * @return - koi potrebitel e syzdal application-a
	 */
	public int getCreatedByUserId();

	public int getApplicationStatusId();
    public int getApplicationDocflowStatusId();
    public FlatNomenclature getApplicationDocflowStatus();

	public FlatNomenclature getApplicationStatus();

    public List<ApplicationRecognitionPurpose> getApplicationRecoginitionPurposes();

	public boolean allowExpertAssignment();
	public List<ApplicationExpert> getApplicationExperts();
	/**
	 * @return dali ima drugi podadeni zaqvleniq ot sy6tiq zaqvitel
	 */
	public boolean hasMoreApplicationsWithSameApplicant();

	/**
	 * @return arhivniq nomer
	 */
	public String getArchiveNumber();
	/**
	 * @return dali zaqvlenieto e prikliueno da se obrabotva ot vsi4ki experti
	 */
	public Boolean isFinishedByexperts();

	/**
	 * @return nomera na udostoverenieto (zapisan v tablicata cert_number_to_attached_doc
	 */
	public String getCertificateNumber();
	/**
	 * @return nevalidnite certificateNumbers
	 */
	public List<String> getInvalidatedCertificateNumbers();

	/**
	 * Декларирам, че заявлението за признаване на придобито висше образование в чуждестранно висше училище и документите към него са ми предоставени от заявителя
	 * @return
	 */
	public Boolean getRepresentativeAuthorized();
	
	/**
	 * belejki kym zaqvlenieto 
	 * @return
	 */
	public String getNotes();
	/**
	 * pokazva dali zaqvlenieto e migrirano ot starata baza danni - ako statusa na zaqvlenieto e "podadeno", to se vodi 4e ne e migrirano
	 * (za da moje da sa skriti dvata taba status i expert)
	 * @return
	 */
	public boolean isMigrated();
	/**
	 * @return istoriqta na promqnata na status-a na zaqvlenieto
	 */
	public List<AppStatusHistory> getAppStatusHistory();
	/**
	 * @return istoriqta na promenenite statusi, koito sa promeneni ot komisiqta, t.e. samo tezi, defininrani v {@link ApplicationStatus#RUDI_APPLICATION_STATUSES_FROM_COMMISSION}
	 */
	public List<AppStatusHistory> getCommissionAppStatusHistory();
	/**
	 * @return vry6ta commission calendar-ite na koito e razglejdano(ili shte se razglejda) tova zaqvlenie
	 */
	public List<CommissionCalendar> getCommissionCalendars();
	/**
	 * 
	 * @return ExtAppliction za tova zaqvlenie(ako ima takyv)
	 */
	public ExtApplication getExtApplication();
	
	public String getSubmittedDocs();
	
	public String getApplicantInfo();
	/**
	 * 
	 * @return dali zaqvlenieto e priznato i ima generiran protocol ot komisiqta - zasega se izpolzva v external prilojenieto - pri generirane na spravkata na zaqvitelq
	 * Pri tova uslovie se pokazvat priznati specialnosti/kvalifikacii/OKS
	 */
	public boolean isRecognizedAndContainsCommissionProtocol();
	/**
	 * 
	 * @return slujitelite rabotili po tova zaqvlenie
	 */
	public  List<String> getEmployeesWorkedOnThisApplication();
	/**
	 * 
	 * @return otgovornik
	 */
	public Integer getResponsibleUserId();



    public Integer getCompanyId();
    public Company getCompany();

    public List<ApplicationKind> getApplicationKinds();

    public DocumentRecipient getDocumentRecipient();

}
