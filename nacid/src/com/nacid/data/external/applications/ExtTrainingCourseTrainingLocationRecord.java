//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-34 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.08.27 at 05:48:07 PM EEST 
//


package com.nacid.data.external.applications;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="trainingCourseId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="trainingCountryId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="trainingCity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "trainingCourseId",
    "trainingCountryId",
    "trainingCity"
})
@XmlRootElement(name = "ext_training_course_training_location_record")
public class ExtTrainingCourseTrainingLocationRecord {

    protected int id;
    protected int trainingCourseId;
    protected Integer trainingCountryId;
    protected String trainingCity;

    public ExtTrainingCourseTrainingLocationRecord() {
	}

	public ExtTrainingCourseTrainingLocationRecord(int id, int trainingCourseId, Integer trainingCountryId, String trainingCity) {
		this.id = id;
		this.trainingCourseId = trainingCourseId;
		this.trainingCountryId = trainingCountryId;
		this.trainingCity = trainingCity;
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
     * Gets the value of the trainingCountryId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTrainingCountryId() {
        return trainingCountryId;
    }

    /**
     * Sets the value of the trainingCountryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTrainingCountryId(Integer value) {
        this.trainingCountryId = value;
    }

    /**
     * Gets the value of the trainingCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrainingCity() {
        return trainingCity;
    }

    /**
     * Sets the value of the trainingCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrainingCity(String value) {
        this.trainingCity = value;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((trainingCity == null) ? 0 : trainingCity.hashCode());
		result = prime * result + ((trainingCountryId == null) ? 0 : trainingCountryId.hashCode());
		result = prime * result + trainingCourseId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExtTrainingCourseTrainingLocationRecord other = (ExtTrainingCourseTrainingLocationRecord) obj;
		if (id != other.id)
			return false;
		if (trainingCity == null) {
			if (other.trainingCity != null)
				return false;
		} else if (!trainingCity.equals(other.trainingCity))
			return false;
		if (trainingCountryId == null) {
			if (other.trainingCountryId != null)
				return false;
		} else if (!trainingCountryId.equals(other.trainingCountryId))
			return false;
		if (trainingCourseId != other.trainingCourseId)
			return false;
		return true;
	}

}