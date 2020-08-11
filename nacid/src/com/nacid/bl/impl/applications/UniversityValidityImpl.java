package com.nacid.bl.impl.applications;

import java.sql.Date;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.University;
import com.nacid.bl.applications.UniversityValidity;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.applications.UniversityValidityRecord;

public class UniversityValidityImpl implements UniversityValidity {

	private int id;
	private Integer universityId;
	private Integer userId;
	private Date examinationDate;
	private boolean comunicated;
	private boolean recognized;
	private String notes;
	private Integer trainingLocationId;
	private boolean hasJoinedDegrees;
	private int[] sourcesOfInformation;
	private Object[] trainingForms;
	private NacidDataProvider nacidDataProvider;

	public UniversityValidityImpl(int id, Integer universityId, Integer userId, Date examinationDate, boolean isComunicated, boolean isRecognized,
			String notes, Integer trainingLocationId, boolean hasJoinedDegrees, NacidDataProvider nacidDataProvider) {

		this.id = id;
		this.universityId = universityId;
		this.userId = userId;
		this.examinationDate = examinationDate;
		this.comunicated = isComunicated;
		this.recognized = isRecognized;
		this.notes = notes;
		this.trainingLocationId = trainingLocationId;
		this.hasJoinedDegrees = hasJoinedDegrees;
		this.nacidDataProvider = nacidDataProvider;
		
	}
	
	public UniversityValidityImpl(UniversityValidityRecord rec, NacidDataProvider nacidDataProvider) {

		this.id = rec.getId();
		this.universityId = rec.getUniversityId();
		this.userId = rec.getUserId();
		this.examinationDate = rec.getExaminationDate();
		this.comunicated = rec.getIsComunicated() != 0;
		this.recognized = rec.getIsRecognized() != 0;
		this.notes = rec.getNotes();
		this.trainingLocationId = rec.getTrainingLocationId();
		this.hasJoinedDegrees = rec.getHasJoinedDegrees() != 0;
		this.nacidDataProvider = nacidDataProvider;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public Integer getUniversityId() {
		return universityId;
	}
	
	public University getUniversity() {
		return getUniversityId() == null ? null : nacidDataProvider.getUniversityDataProvider().getUniversity(getUniversityId());
	}
	
	@Override
	public Integer getUserId() {
		return userId;
	}

	@Override
	public Date getExaminationDate() {
		return examinationDate;
	}

	@Override
	public boolean isComunicated() {
		return comunicated;
	}

	@Override
	public boolean isRecognized() {
		return recognized;
	}

	@Override
	public String getNotes() {
		return notes;
	}

	@Override
	public Integer getTrainingLocationId() {
		return trainingLocationId;
	}
	public FlatNomenclature getTrainingLocation() {
		return getTrainingLocationId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_TRAINING_LOCATION, getTrainingLocationId());
	}

	@Override
	public boolean isHasJoinedDegrees() {
		return hasJoinedDegrees;
	}

	@Override
	public int[] getSourcesOfInformation() {
	    return sourcesOfInformation;
	}
	
	public void setSourcesOfInformation(int[] sourcesOfInformation) {
	    this.sourcesOfInformation = sourcesOfInformation;
	}
	
	@Override
	public Object[] getTrainingForms() {
	    return trainingForms;
	}
	
	public void setTrainingForms(Object[] trainingForms) {
	    this.trainingForms = trainingForms;
	}
}
