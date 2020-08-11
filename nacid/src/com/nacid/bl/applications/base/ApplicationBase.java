package com.nacid.bl.applications.base;

import com.nacid.bl.applications.DocumentRecipient;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.DocumentReceiveMethod;
import com.nacid.bl.nomenclatures.FlatNomenclature;

import java.util.Date;
import java.util.List;


public interface ApplicationBase {
	public static final int BG_ADDRESS_OWNER_APPLICANT = 0;
	public static final int BG_ADDRESS_OWNER_REPRESENTATIVE = 1;
	public int getId();


    /**
     * @return chastta ot docFlowNumber-a bez /applicationDate - primerno 44-00-1234
     */
    public String getApplicationNumber();
    public Date getApplicationDate();
    /**
     * @return applicationNumber + "/" + applicationDate
     */
    public String getDocFlowNumber();

    public Integer getApplicantId();
	public PersonBase getApplicant();

    public Integer getRepresentativeId();
    public PersonBase getRepresentative();

    public Integer getApplicantCompanyId();
    public CompanyBase getApplicantCompany();

    /**
     * imenata na zaqvitelq v zavisimost ot tova dali e FL ili UL
     * @return
     */
    public String getApplicantNames();

    public int getApplicantType();
	public int getTrainingCourseId();
	public TrainingCourseBase getTrainingCourse();

	public int getHomeCountryId();
	public Country getHomeCountry();
	public String getHomeCity();

	public String getHomePostCode();
	public String getHomeAddressDetails();
	public String getHomePhone();

	public boolean homeIsBg();
	public String getBgCity();
	public String getBgPostCode();
	public String getBgAddressDetails();
	public String getBgPhone();

	public Date getTimeOfCreation();
	/**
	 * shte vry6ta dali imenata na zaqvitelq i tezi po diploma syvpadat!
	 * true - pri razlichni imena
	 * false - pri ednakvi 
	 */
	public boolean differentApplicantAndDiplomaNames();
	/**
	 * vry6ta motivite!
	 * @return
	 */
	public String getSummary();

	/** zaqvitelq e syglasen lichnite mu danni da se polzvat za celite na proverkata*/
	public Boolean isPersonalDataUsage();

	/**
	 * Декларирам, че документите и данните, посочени в заявлението за признаване на придобито висше образование в чуждестранно висше училище, са истински и автентични
	 * @return
	 */
	public Boolean getDataAuthentic();


    public String getRepresentativeType();

    public Short getTypePayment();
    public FlatNomenclature getPaymentType();
    public Integer getDeliveryType();
    public Boolean getDeclaration();
    public String getCourierNameAddress();
    public String getOutgoingNumber();
    public String getInternalNumber();
    public Boolean getIsExpress();
    public List<? extends ApplicationKindBase> getApplicationKinds();

    public int getApplicationType();

	public Integer getDocumentReceiveMethodId();
	public DocumentReceiveMethod getDocumentReceiveMethod();

	public DocumentRecipientBase getDocumentRecipient();

	public Integer getApplicantPersonalIdDocumentTypeId();
	public FlatNomenclature getApplicantPersonalIdDocumentType();
}
