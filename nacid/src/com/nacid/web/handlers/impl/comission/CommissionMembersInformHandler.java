package com.nacid.web.handlers.impl.comission;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.comision.CommissionCalendarDataProvider;
import com.nacid.bl.comision.CommissionParticipation;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.model.comission.CommissionMembersInformWebModel;

public class CommissionMembersInformHandler extends NacidBaseRequestHandler {

	public CommissionMembersInformHandler(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
		int calendarId = DataConverter.parseInt(request.getParameter("calendar_id"), 0);
		if (calendarId == 0) {
			throw new UnknownRecordException("unknown calendar id");
		}
		/**
		 * Tuk principno ima leeek problem no pri leko natovarena sistema ne si struva usliqta da go fixvam - pyrvo notifyMembersForSessionChange
		 * pyrvo CommissionCalendarUtils.notifyMembersForSessionChange obhojda vsi4ki commissionParticipations za da im izprati mails, posle tuk v toq handler
		 * se obhojdat participations za da se syzdade webmodel-a s emailite na memberite, vmesto da stava samo s edna iteraciq.... no ne vqrvam nqkoga da se natovari tolkova sistemata
		 * 4e da trybva da se optimizira! 
		 */
		
		CommissionCalendarUtils.notifyMembersForSessionChange(calendarId, getNacidDataProvider(), false);
		
		CommissionCalendarDataProvider commissionCalendarDataProvider = getNacidDataProvider().getCommissionCalendarDataProvider();
		List<CommissionParticipation> commissionParticipations = commissionCalendarDataProvider.getCommissionParticipations(calendarId);
		CommissionMembersInformWebModel webmodel = new CommissionMembersInformWebModel(calendarId);
		for (CommissionParticipation cp:commissionParticipations) {
			ComissionMember member = cp.getCommissionMember();
			String email = member.getEmail();
			if (!StringUtils.isEmpty(email)) {
				//TODO:configure from, subject and mail content and send email
				//MailUtils.sendEmail("nacid@nacid.bg", email, null, null, null);
				webmodel.addCommissionMemberWebModel(member);
			}
		}
		request.setAttribute(WebKeys.COMMISSION_MEMBERS_INFORM_WEB_MODEL, webmodel);
		request.setAttribute(WebKeys.NEXT_SCREEN, "commission_members_inform");
	}

}
