package com.nacid.bl.applications;

import java.sql.Date;

import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;

public interface UniversityValidity {

	public int getId();

	public Integer getUniversityId();
	
	public University getUniversity();

	public Integer getUserId();

	public Date getExaminationDate();

	public boolean isComunicated();

	public boolean isRecognized();

	public String getNotes();

	public Integer getTrainingLocationId();
	
	public FlatNomenclature getTrainingLocation();
	
	public boolean isHasJoinedDegrees();

    public int[] getSourcesOfInformation();
    /**
     * G.Georgiev
     * ako konkretniq element e ot tip Integer, togava trainingForm-a e {@link FlatNomenclature} ot tip {@link NomenclaturesDataProvider}.FLAT_NOMENCLATURE_TRAINING_FORM
     * ako elementa e ot tip String, to togava trainingForm-a e ot tip "drug" s dadenoto ime...., -  tova ivo go e pisal az samo gadaq - da go pitam dali e taka
     * @return
     */
    public Object[] getTrainingForms();

}
