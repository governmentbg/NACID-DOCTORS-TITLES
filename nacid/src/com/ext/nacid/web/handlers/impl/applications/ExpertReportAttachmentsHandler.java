package com.ext.nacid.web.handlers.impl.applications;

import javax.servlet.ServletContext;

//import com.nacid.bl.external.users.ExtUser;
import com.nacid.bl.users.User;


public class ExpertReportAttachmentsHandler extends ApplicantReportInternalAttachmentsHandler {
	
	public ExpertReportAttachmentsHandler(ServletContext servletContext) {
		super(servletContext);
	}

	//check-va specifi4nata authorizaciq, koqto se otnasq do expert
	protected void checkSpecificAuthorization(User user, int applicationId) throws RuntimeException{
		//Specifi4nata za expert authorizaciq
		//16.12.2010 - ggeorgiev - maham specifi4nata authorizaciq za expert, tyi kato toj ve4e shte moje da gleda spravki za vsi4ki zaqvleniq i sledovatelno shte trqbva da moje da gleda prika4enite files na vsi4ki zaqvleniq
		/*NacidDataProvider nacidDataProvider = getNacidDataProvider();
		ComissionMemberDataProvider comissionMemberDataProvider = nacidDataProvider.getComissionMemberDataProvider();
		ComissionMember commissionMember = comissionMemberDataProvider.getComissionMemberByUserId(user.getUserId());
		if (commissionMember == null) {
			throw new RuntimeException("This user is not linked to any commission_member User Id = " + user.getUserId() + ")");
		}
		List<Integer> applicationsByExpert = comissionMemberDataProvider.getApplicationIdsByExpert(commissionMember.getId());
		if (applicationsByExpert == null || !applicationsByExpert.contains(applicationId)) {
			throw new RuntimeException("You are not authorized to view this application");
		}*/
	}
	

}
