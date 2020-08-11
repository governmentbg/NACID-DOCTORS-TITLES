package com.nacid.web.handlers.impl.comission;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.comision.CommissionCalendar;
import com.nacid.bl.comision.CommissionCalendarDataProvider;
import com.nacid.bl.comision.CommissionParticipation;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.mail.MailBeanImpl;
import com.nacid.bl.mail.MailBean;
import com.nacid.bl.mail.MailDataProvider;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.PropertiesBundle;

public class CommissionCalendarUtils {
	/**
	 * 
	 * @param calendarId
	 * @param nDP
	 * @param onlyNotified - uvedomqva samo tezi members, koito imat vdignat flag notified!
	 */
    public static void notifyMembersForSessionChange(int calendarId, NacidDataProvider nDP, boolean onlyNotified) {
        if (!PropertiesBundle.isEmailsEnabled()) {
        	return;
        }
    	CommissionCalendarDataProvider comCalDP = nDP.getCommissionCalendarDataProvider();
        CommissionCalendar comCal = comCalDP.getCommissionCalendar(calendarId);
        
        List<CommissionParticipation> comPars = comCalDP.getCommissionParticipations(calendarId);
        
        Map<String, String> experts = new HashMap<String, String>();
        for(CommissionParticipation cp : comPars) {
            ComissionMember cm = cp.getCommissionMember();
            if((onlyNotified && cp.isNotified() || !onlyNotified) && !Utils.isEmptyString(cm.getEmail())) {
                experts.put(cm.getEmail(), cm.getFname() + " " + cm.getLname());
            }
        }
        
        
        MailDataProvider mdp = nDP.getMailDataProvider();
        
        UtilsDataProvider udp = nDP.getUtilsDataProvider();
        String subject = udp.getCommonVariableValue(
                UtilsDataProvider.COMMISSION_SESSION_CHANGED_SUBJECT);
        String body = udp.getCommonVariableValue(
                UtilsDataProvider.COMMISSION_SESSION_CHANGED_BODY);
        String sender = udp.getCommonVariableValue(UtilsDataProvider.MAIL_SENDER);
        
        body = MessageFormat.format(body, comCal.getSessionNumber(), 
                DataConverter.formatDateTime(comCal.getDateAndTime(), false),
                comCal.getNotes());
        
        Map<String, String> fromM = Collections.singletonMap(sender, sender);
        
        MailBean msg = new MailBeanImpl(fromM, sender, experts, ((Map<String, String>)null), 
                ((Map<String, String>)null), subject,
                fromM, body, new Date(), mdp.getSession());
        mdp.sendMessage(msg);
    }
}
