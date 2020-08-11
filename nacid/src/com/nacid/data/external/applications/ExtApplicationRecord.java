//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-34 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.20 at 02:39:17 PM EEST 
//


package com.nacid.data.external.applications;

import com.nacid.data.external.applications.xml.Adapter1;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;
import java.util.Objects;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="applicantId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="differentDiplomaNames" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="trainingCourseId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="homeCountryId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="homeCity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="homePostCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="homeAddressDetails" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="homeIsBg" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="bgCity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="bgPostCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="bgAddressDetails" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="timeOfCreation" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="homePhone" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="bgPhone" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="summary" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="applicationStatus" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="applicationId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="dataAuthentic" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "id",
    "applicantId",
    "differentDiplomaNames",
    "trainingCourseId",
    "homeCountryId",
    "homeCity",
    "homePostCode",
    "homeAddressDetails",
    "homeIsBg",
    "bgCity",
    "bgPostCode",
    "bgAddressDetails",
    "timeOfCreation",
    "homePhone",
    "bgPhone",
    "summary",
    "applicationStatus",
    "applicationId",
    "personalDataUsage",
    "dataAuthentic",
    "applicantType",
    "companyId",
    "deputy",
    "representativeId",
    "representativeType",
    "contactDetailsId",
    "typePayment",
    "deliveryType",
    "declaration",
    "courierNameAddress",
    "outgoingNumber",
    "internalNumber",
    "isExpress",
    "entryNum",
    "documentReceiveMethodId",
    "applicantPersonalIdDocumentType"
})
@XmlRootElement(name = "ext_application_record")
public class ExtApplicationRecord {

    protected int id;
    protected Integer applicantId;
    protected int differentDiplomaNames;
    protected int trainingCourseId;
    protected int homeCountryId;
    @XmlElement(required = true)
    protected String homeCity;
    @XmlElement(required = true)
    protected String homePostCode;
    @XmlElement(required = true)
    protected String homeAddressDetails;
    protected Integer homeIsBg;
    @XmlElement(required = true)
    protected String bgCity;
    @XmlElement(required = true)
    protected String bgPostCode;
    @XmlElement(required = true)
    protected String bgAddressDetails;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Timestamp timeOfCreation;
    @XmlElement(required = true)
    protected String homePhone;
    @XmlElement(required = true)
    protected String bgPhone;
    @XmlElement(required = true)
    protected String summary;
    protected int applicationStatus;
    protected Integer applicationId;
    protected Integer personalDataUsage;
    protected Integer dataAuthentic;
    protected int applicantType;
    protected Integer companyId;
    protected Boolean deputy;
    private Integer representativeId;
    private String representativeType;
    private Integer contactDetailsId;
    private Short typePayment;
    private Integer deliveryType;
    private Boolean declaration;
    private String courierNameAddress;
    private String outgoingNumber;
    private String internalNumber;
    private Boolean isExpress;
    private String entryNum;
    private Integer documentReceiveMethodId;
    private Integer applicantPersonalIdDocumentType;


    public ExtApplicationRecord() {
	}



    public ExtApplicationRecord(int id, Integer applicantId, Integer applicantCompanyId, int differentDiplomaNames, int trainingCourseId, int homeCountryId, String homeCity,
			String homePostCode, String homeAddressDetails, String homePhone, Integer homeIsBg, String bgCity, String bgPostCode, String bgAddressDetails,
			String bgPhone, Timestamp timeOfCreation, String summary, int applicationStatus, Integer applicationId, Integer personalDataUsage, Integer dataAuthentic,
            int applicantType,  Boolean deputy, Integer representativeId, String representativeType, Integer contactDetailsId, Short typePayment, Integer deliveryType, Boolean declaration, String courierNameAddress, String outgoingNumber, String internalNumber, Boolean isExpress,
            String entryNum, Integer documentReceiveMethodId, Integer applicantPersonalIdDocumentType) {
        this.id = id;
        this.applicantId = applicantId;
        this.differentDiplomaNames = differentDiplomaNames;
        this.trainingCourseId = trainingCourseId;
        this.homeCountryId = homeCountryId;
        this.homeCity = homeCity;
        this.homePostCode = homePostCode;
        this.homeAddressDetails = homeAddressDetails;
        this.homeIsBg = homeIsBg;
        this.bgCity = bgCity;
        this.bgPostCode = bgPostCode;
        this.bgAddressDetails = bgAddressDetails;
        this.timeOfCreation = timeOfCreation;
        this.homePhone = homePhone;
        this.bgPhone = bgPhone;
        this.summary = summary;
        this.applicationStatus = applicationStatus;
        this.applicationId = applicationId;
        this.personalDataUsage = personalDataUsage;
        this.dataAuthentic = dataAuthentic;
        this.applicantType = applicantType;
        this.companyId = applicantCompanyId;
        this.deputy = deputy;
        this.representativeId = representativeId;
        this.representativeType = representativeType;
        this.contactDetailsId = contactDetailsId;
        this.typePayment = typePayment;
        this.deliveryType = deliveryType;
        this.declaration = declaration;
        this.courierNameAddress = courierNameAddress;
        this.outgoingNumber = outgoingNumber;
        this.internalNumber = internalNumber;
        this.isExpress = isExpress;
        this.entryNum = entryNum;
        this.documentReceiveMethodId = documentReceiveMethodId;
        this.applicantPersonalIdDocumentType = applicantPersonalIdDocumentType;
	}

	/**
     * Gets the value of the id property.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Gets the value of the applicantId property.
     * 
     */
    public Integer getApplicantId() {
        return applicantId;
    }

    /**
     * Sets the value of the applicantId property.
     * 
     */
    public void setApplicantId(Integer value) {
        this.applicantId = value;
    }

    /**
     * Gets the value of the differentDiplomaNames property.
     * 
     */
    public int getDifferentDiplomaNames() {
        return differentDiplomaNames;
    }

    /**
     * Sets the value of the differentDiplomaNames property.
     * 
     */
    public void setDifferentDiplomaNames(int value) {
        this.differentDiplomaNames = value;
    }

    /**
     * Gets the value of the trainingCourseId property.
     * 
     */
    public int getTrainingCourseId() {
        return trainingCourseId;
    }

    /**
     * Sets the value of the trainingCourseId property.
     * 
     */
    public void setTrainingCourseId(int value) {
        this.trainingCourseId = value;
    }

    /**
     * Gets the value of the homeCountryId property.
     * 
     */
    public int getHomeCountryId() {
        return homeCountryId;
    }

    /**
     * Sets the value of the homeCountryId property.
     * 
     */
    public void setHomeCountryId(int value) {
        this.homeCountryId = value;
    }

    /**
     * Gets the value of the homeCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomeCity() {
        return homeCity;
    }

    /**
     * Sets the value of the homeCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomeCity(String value) {
        this.homeCity = value;
    }

    /**
     * Gets the value of the homePostCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomePostCode() {
        return homePostCode;
    }

    /**
     * Sets the value of the homePostCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomePostCode(String value) {
        this.homePostCode = value;
    }

    /**
     * Gets the value of the homeAddressDetails property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomeAddressDetails() {
        return homeAddressDetails;
    }

    /**
     * Sets the value of the homeAddressDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomeAddressDetails(String value) {
        this.homeAddressDetails = value;
    }

    /**
     * Gets the value of the homeIsBg property.
     * 
     */
    public Integer getHomeIsBg() {
        return homeIsBg;
    }

    /**
     * Sets the value of the homeIsBg property.
     * 
     */
    public void setHomeIsBg(Integer value) {
        this.homeIsBg = value;
    }

    /**
     * Gets the value of the bgCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBgCity() {
        return bgCity;
    }

    /**
     * Sets the value of the bgCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBgCity(String value) {
        this.bgCity = value;
    }

    /**
     * Gets the value of the bgPostCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBgPostCode() {
        return bgPostCode;
    }

    /**
     * Sets the value of the bgPostCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBgPostCode(String value) {
        this.bgPostCode = value;
    }

    /**
     * Gets the value of the bgAddressDetails property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBgAddressDetails() {
        return bgAddressDetails;
    }

    /**
     * Sets the value of the bgAddressDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBgAddressDetails(String value) {
        this.bgAddressDetails = value;
    }

    /**
     * Gets the value of the timeOfCreation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Timestamp getTimeOfCreation() {
        return timeOfCreation;
    }

    /**
     * Sets the value of the timeOfCreation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeOfCreation(Timestamp value) {
        this.timeOfCreation = value;
    }

    /**
     * Gets the value of the homePhone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomePhone() {
        return homePhone;
    }

    /**
     * Sets the value of the homePhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomePhone(String value) {
        this.homePhone = value;
    }

    /**
     * Gets the value of the bgPhone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBgPhone() {
        return bgPhone;
    }

    /**
     * Sets the value of the bgPhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBgPhone(String value) {
        this.bgPhone = value;
    }

    /**
     * Gets the value of the summary property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets the value of the summary property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSummary(String value) {
        this.summary = value;
    }

    /**
     * Gets the value of the applicationStatus property.
     * 
     */
    public int getApplicationStatus() {
        return applicationStatus;
    }

    /**
     * Sets the value of the applicationStatus property.
     * 
     */
    public void setApplicationStatus(int value) {
        this.applicationStatus = value;
    }

    /**
     * Gets the value of the applicationId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getApplicationId() {
        return applicationId;
    }

    /**
     * Sets the value of the applicationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setApplicationId(Integer value) {
        this.applicationId = value;
    }

	public Integer getPersonalDataUsage() {
		return personalDataUsage;
	}

	public void setPersonalDataUsage(Integer personalDataUsage) {
		this.personalDataUsage = personalDataUsage;
	}
	public Integer getDataAuthentic() {
		return dataAuthentic;
	}

	public void setDataAuthentic(Integer dataAuthentic) {
		this.dataAuthentic = dataAuthentic;
	}



    public int getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(int applicantType) {
        this.applicantType = applicantType;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Boolean getDeputy() {
        return deputy;
    }

    public void setDeputy(Boolean deputy) {
        this.deputy = deputy;
    }

    public Integer getRepresentativeId() {
        return representativeId;
    }

    public void setRepresentativeId(Integer representativeId) {
        this.representativeId = representativeId;
    }

    public String getRepresentativeType() {
        return representativeType;
    }

    public void setRepresentativeType(String representativeType) {
        this.representativeType = representativeType;
    }

    public Integer getContactDetailsId() {
        return contactDetailsId;
    }

    public void setContactDetailsId(Integer contactDetailsId) {
        this.contactDetailsId = contactDetailsId;
    }

    public Short getTypePayment() {
        return typePayment;
    }

    public void setTypePayment(Short typePayment) {
        this.typePayment = typePayment;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Boolean getDeclaration() {
        return declaration;
    }

    public void setDeclaration(Boolean declaration) {
        this.declaration = declaration;
    }

    public String getCourierNameAddress() {
        return courierNameAddress;
    }

    public void setCourierNameAddress(String courierNameAddress) {
        this.courierNameAddress = courierNameAddress;
    }

    public String getOutgoingNumber() {
        return outgoingNumber;
    }

    public void setOutgoingNumber(String outgoingNumber) {
        this.outgoingNumber = outgoingNumber;
    }

    public String getInternalNumber() {
        return internalNumber;
    }

    public void setInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
    }

    public Boolean getIsExpress() {
        return isExpress;
    }

    public void setIsExpress(Boolean isExpress) {
        this.isExpress = isExpress;
    }

    public String getEntryNum() {
        return entryNum;
    }

    public void setEntryNum(String entryNum) {
        this.entryNum = entryNum;
    }

    public Integer getDocumentReceiveMethodId() {
        return documentReceiveMethodId;
    }

    public void setDocumentReceiveMethodId(Integer documentReceiveMethodId) {
        this.documentReceiveMethodId = documentReceiveMethodId;
    }

    public Integer getApplicantPersonalIdDocumentType() {
        return applicantPersonalIdDocumentType;
    }

    public void setApplicantPersonalIdDocumentType(Integer applicantPersonalIdDocumentType) {
        this.applicantPersonalIdDocumentType = applicantPersonalIdDocumentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtApplicationRecord that = (ExtApplicationRecord) o;
        return id == that.id &&
                differentDiplomaNames == that.differentDiplomaNames &&
                trainingCourseId == that.trainingCourseId &&
                homeCountryId == that.homeCountryId &&
                applicationStatus == that.applicationStatus &&
                applicantType == that.applicantType &&
                Objects.equals(applicantId, that.applicantId) &&
                Objects.equals(homeCity, that.homeCity) &&
                Objects.equals(homePostCode, that.homePostCode) &&
                Objects.equals(homeAddressDetails, that.homeAddressDetails) &&
                Objects.equals(homeIsBg, that.homeIsBg) &&
                Objects.equals(bgCity, that.bgCity) &&
                Objects.equals(bgPostCode, that.bgPostCode) &&
                Objects.equals(bgAddressDetails, that.bgAddressDetails) &&
                Objects.equals(timeOfCreation, that.timeOfCreation) &&
                Objects.equals(homePhone, that.homePhone) &&
                Objects.equals(bgPhone, that.bgPhone) &&
                Objects.equals(summary, that.summary) &&
                Objects.equals(applicationId, that.applicationId) &&
                Objects.equals(personalDataUsage, that.personalDataUsage) &&
                Objects.equals(dataAuthentic, that.dataAuthentic) &&
                Objects.equals(companyId, that.companyId) &&
                Objects.equals(deputy, that.deputy) &&
                Objects.equals(representativeId, that.representativeId) &&
                Objects.equals(representativeType, that.representativeType) &&
                Objects.equals(contactDetailsId, that.contactDetailsId) &&
                Objects.equals(typePayment, that.typePayment) &&
                Objects.equals(deliveryType, that.deliveryType) &&
                Objects.equals(declaration, that.declaration) &&
                Objects.equals(courierNameAddress, that.courierNameAddress) &&
                Objects.equals(outgoingNumber, that.outgoingNumber) &&
                Objects.equals(internalNumber, that.internalNumber) &&
                Objects.equals(isExpress, that.isExpress) &&
                Objects.equals(entryNum, that.entryNum) &&
                Objects.equals(documentReceiveMethodId, that.documentReceiveMethodId) &&
                Objects.equals(applicantPersonalIdDocumentType, that.applicantPersonalIdDocumentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, applicantId, differentDiplomaNames, trainingCourseId, homeCountryId, homeCity, homePostCode, homeAddressDetails, homeIsBg, bgCity, bgPostCode, bgAddressDetails, timeOfCreation, homePhone, bgPhone, summary, applicationStatus, applicationId, personalDataUsage, dataAuthentic, applicantType, companyId, deputy, representativeId, representativeType, contactDetailsId, typePayment, deliveryType, declaration, courierNameAddress, outgoingNumber, internalNumber, isExpress, entryNum, documentReceiveMethodId, applicantPersonalIdDocumentType);
    }
}