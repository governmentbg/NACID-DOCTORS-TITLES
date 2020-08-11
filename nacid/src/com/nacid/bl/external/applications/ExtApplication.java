package com.nacid.bl.external.applications;

import com.nacid.bl.applications.base.ApplicationBase;
import com.nacid.bl.applications.base.DocumentRecipientBase;
import com.nacid.bl.external.*;

import java.util.List;

public interface ExtApplication extends ApplicationBase {


    /**
	 * status pri koito potrebitelq oshte moje da si editva dannite
	 */
	public static final int STATUS_EDITABLE = 0;
	/**
	 * status pri koito dannite sa prateni za priznavane v internal bazata na nacid i user-a nqma pravo da editva
	 */
	public static final int STATUS_NOT_EDITABLE = 1;
	/**
     * status pri koito kopirani v istinskata baza
     */
    public static final int STATUS_TRANSFERED = 3;
	/**
	 * status, pri koito zaqvlenieto e prikliucheno, t.e. nqma da se prehvyrli nikoga v backoffice-a! Potrebitelq sy6to ne trqbva da gi vijda tezi zaqvleniq!!!!
	 */
	public static final int STATUS_FINISHED = 4;
	
	public ExtPerson getApplicant();
    public ExtCompany getApplicantCompany();

    public ExtPerson getRepresentative();
	public ExtTrainingCourse getTrainingCourse();
	
	/**
	 * shte vry6ta dali imenata na zaqvitelq i tezi po diploma syvpadat!
	 * true - pri razlichni imena
	 * false - pri ednakvi 
	 */
	public Integer getInternalApplicationId();
	public int getApplicationStatus();
	
	/**
	 * @return dali zaqvlenieto e podpisano elektronno
	 */
	public boolean isESigned();
	/**
	 * @return elektronno podpisanata informaciq
	 */
	public ExtESignedInformation getESignedInformation();






    public Boolean getDeputy();
    public Integer getContactDetailsId();
    public ExtAddress getContactDetails();
    public List<ExtApplicationKind> getApplicationKinds();

	@Override
	ExtDocumentRecipient getDocumentRecipient();

}
