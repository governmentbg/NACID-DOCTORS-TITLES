package com.nacid.bl;

import java.util.ArrayList;
import java.util.List;
import com.nacid.bl.applications.DiplomaTypeEditException;

/**
 * ideqta e tozi Exception da se hvyrlq pri opit za editvane na nqkakvi danni, koito ne mogat da se editnat po edna ili druga pri4ina
 * kym momenta tozi class se extende-va ot {@link DiplomaTypeEditException}, koito se hvyrlq pri opit za editvane na ime na unversitet, obrazovatelna stepen ili zaglavie na diplomaType, 
 * koito e vyrzan kym daden trainingCourse 
 * @author ggeorgiev
 *
 */
public class DataEditException extends Exception{
	/**
	 * ako pri opit za zapis na dannite vyzniknat pove4e ot edna greshka, to te mogat da se dobavqt s addMessage() i posle da se pro4etat s getMessages()
	 * v slu4aq s {@link DiplomaTypeEditException} moje ednovremeno nqkoj da se opitva da promeni universiteta i obrazovatelnata stepen. Ideqta e da mogat da se izvedat i dvete syob6teniq 
	 */
	List<String> messages = new ArrayList<String>();
	public DataEditException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataEditException(String message) {
		super(message);
	}

	public DataEditException() {
	}
	
	public void addMessage(String message) {
		messages.add(message);
	}
	public List<String> getMessages() {
		return messages;
	}
}
