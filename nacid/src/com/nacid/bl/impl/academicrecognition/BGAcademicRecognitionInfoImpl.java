package com.nacid.bl.impl.academicrecognition;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.nacid.bl.RequestParameterInterface;
import com.nacid.bl.academicrecognition.BGAcademicRecognitionInfo;
import com.nacid.data.annotations.SuppressColumn;
import com.nacid.data.annotations.Table;

@Table(name="bg_academic_recognition_info")
public class BGAcademicRecognitionInfoImpl implements BGAcademicRecognitionInfo, RequestParameterInterface {

    private Integer id;
    private String applicant;
    private Integer recognizedUniversityId;
    private String citizenship;
    private String university;
    private String universityCountry;
    private String educationLevel;
    private String diplomaSpeciality;
    private String diplomaNumber;
    private String diplomaDate;
    private String protocolNumber;
    private String denialProtocolNumber;
    private String recognizedSpeciality;
    private String inputNumber;
    private String outputNumber;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    private Date createdDate;
    private String notes;
    private Integer recognitionStatusId;
    private Integer relatedRecognitionId;
    
    @SuppressColumn
    private List<BGAcademicRecognitionExtendedImpl> similarRecognitions;
    
    //private String recognizedUniversity;
    public String getApplicant() {
        return applicant;
    }
    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }
    public String getCitizenship() {
        return citizenship;
    }
    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }
    public String getUniversity() {
        return university;
    }
    public void setUniversity(String university) {
        this.university = university;
    }
    public String getUniversityCountry() {
        return universityCountry;
    }
    public void setUniversityCountry(String universityCountry) {
        this.universityCountry = universityCountry;
    }
    public String getEducationLevel() {
        return educationLevel;
    }
    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }
    public String getDiplomaSpeciality() {
        return diplomaSpeciality;
    }
    public void setDiplomaSpeciality(String diplomaSpeciality) {
        this.diplomaSpeciality = diplomaSpeciality;
    }
    public String getDiplomaNumber() {
        return diplomaNumber;
    }
    public void setDiplomaNumber(String diplomaNumber) {
        this.diplomaNumber = diplomaNumber;
    }
    public String getDiplomaDate() {
        return diplomaDate;
    }
    public void setDiplomaDate(String diplomaDate) {
        this.diplomaDate = diplomaDate;
    }
    public String getProtocolNumber() {
        return protocolNumber;
    }
    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }
    public String getDenialProtocolNumber() {
        return denialProtocolNumber;
    }
    public void setDenialProtocolNumber(String denialProtocolNumber) {
        this.denialProtocolNumber = denialProtocolNumber;
    }
    public String getRecognizedSpeciality() {
        return recognizedSpeciality;
    }
    public void setRecognizedSpeciality(String recognizedSpeciality) {
        this.recognizedSpeciality = recognizedSpeciality;
    }
    public Integer getRecognizedUniversityId() {
        return recognizedUniversityId;
    }
    public void setRecognizedUniversityId(Integer recognizedUniversityId) {
        this.recognizedUniversityId = recognizedUniversityId;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getInputNumber() {
		return inputNumber;
	}
	public void setInputNumber(String inputNumber) {
		this.inputNumber = inputNumber;
	}
	public String getOutputNumber() {
		return outputNumber;
	}
	public void setOutputNumber(String outputNumber) {
		this.outputNumber = outputNumber;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
	public Integer getRecognitionStatusId() {
		return recognitionStatusId;
	}
	public void setRecognitionStatusId(Integer recognitionStatusId) {
		this.recognitionStatusId = recognitionStatusId;
	}
	public Integer getRelatedRecognitionId() {
		return relatedRecognitionId;
	}
	public void setRelatedRecognitionId(Integer relatedRecognitionId) {
		this.relatedRecognitionId = relatedRecognitionId;
	}
	
	public List<BGAcademicRecognitionExtendedImpl> getSimilarRecognitions() {
		return similarRecognitions;
	}
	public void setSimilarRecognitions(
			List<BGAcademicRecognitionExtendedImpl> similarRecognitions) {
		this.similarRecognitions = similarRecognitions;
	}
	public boolean getHasSimilar(){
		return similarRecognitions != null && similarRecognitions.size() >0;
	}
	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BGAcademicRecognitionInfoImpl [applicant=").append(applicant).append(", citizenship=").append(citizenship).append(
                ", denialProtocolNumber=").append(denialProtocolNumber).append(", diplomaDate=").append(diplomaDate).append(", diplomaNumber=")
                .append(diplomaNumber).append(", diplomaSpeciality=").append(diplomaSpeciality).append(", educationLevel=").append(educationLevel)
                .append(", id=").append(id).append(", protocolNumber=").append(protocolNumber).append(", recognizedSpeciality=").append(
                        recognizedSpeciality).append(", recognizedUniversityId=").append(recognizedUniversityId).append(", university=").append(
                        university).append(", universityCountry=").append(universityCountry)
                        .append(", inputNumber=").append(inputNumber).append(", outputNumber=").append(outputNumber)
                        .append(", createdDate=").append(createdDate).append("]");
        return builder.toString();
    }
    
    
}