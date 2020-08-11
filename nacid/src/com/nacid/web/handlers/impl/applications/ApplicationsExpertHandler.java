package com.nacid.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.ApplicationExpert;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.comision.ComissionMemberDataProvider;
import com.nacid.bl.comision.ComissionMemberOrderCriteria;
import com.nacid.bl.impl.mail.MailBeanImpl;
import com.nacid.bl.mail.MailBean;
import com.nacid.bl.mail.MailDataProvider;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.MessagesBundle;
import com.nacid.web.PropertiesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.model.common.ComboBoxWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.*;

public class ApplicationsExpertHandler extends NacidBaseRequestHandler {

    public ApplicationsExpertHandler(ServletContext servletContext) {
        super(servletContext);
    }
    /*private static class ApplicationExpertHelper {
        int expertId;
        String notes;
        int finished;
        public ApplicationExpertHelper(int expertId, String notes, int finished) {
            this.expertId = expertId;
            this.notes = notes;
            this.finished = finished;
        }
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + expertId;
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
            ApplicationExpertHelper other = (ApplicationExpertHelper) obj;
            if (expertId != other.expertId)
                return false;
            return true;
        }
        
    }*/

    public void handleView(HttpServletRequest request, HttpServletResponse response) {
        int applicationId = DataConverter.parseInt(request.getParameter("application_id"), 0);
        
        ApplicationsDataProvider applicationsDataProvider = getNacidDataProvider().getApplicationsDataProvider();
        Application application = applicationId == 0 ? null : applicationsDataProvider.getApplication(applicationId);
        if (application == null) {
            writeToResponse(response, "no application id is set");
            return;
        }
        if (!application.allowExpertAssignment()) {
            writeToResponse(response, MessagesBundle.getMessagesBundle().getValue("not_recognized"));
        } else {
            /*generateApplicationExpertsCombo(request, getNacidDataProvider());
            request.setAttribute(WebKeys.APPLICATION_WEB_MODEL, new ApplicationWebModel(application, 0));
            request.setAttribute(WebKeys.NEXT_SCREEN, "app_expert");*/
            writeToResponse(response, "ok");
        }
        
    }
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        int applicationId = DataConverter.parseInt(request.getParameter("application_id"), -1);
        Application application = applicationId == -1 ? null : getNacidDataProvider().getApplicationsDataProvider().getApplication(applicationId);
        if (application == null) {
            writeToResponse(response, "No applicaiton ID is set....");
            return;
        }
        request.setAttribute(WebKeys.APPLICATION_ID, applicationId);
        //Tozi try/catch se nalaga tyi kato moje da vyznikne nqkakyv vid greshki pri opit za zapis na dannite (primerno nqma dostyp do bazata!)
        //i ako go nqma maj na potrebitelq nqma da mu izpishe nishto....
        try {
            saveApplictionExpert(request, response, application);
        } catch (RuntimeException e) {
            e.printStackTrace();
            writeToResponse(response, MessagesBundle.getMessagesBundle().getValue("please_try_later"));
        }
        
        
    }
    
    
    public static ComboBoxWebModel generateApplicationExpertsCombo(Integer selectedKey, boolean addEmpty, HttpServletRequest request, NacidDataProvider nacidDataProvider) {
        
        ComboBoxWebModel combobox = new ComboBoxWebModel(selectedKey == null ? null : selectedKey.toString(), addEmpty);
        ComissionMemberDataProvider comissionMemberDataProvider = nacidDataProvider.getComissionMemberDataProvider();
        List<ComissionMember> comissionMembers = comissionMemberDataProvider.getComissionMembers(true, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_COMISSION_MEMBER, ComissionMemberOrderCriteria.ORDER_COLUMN_FULL_NAME, true));
        
        if (comissionMembers != null) {
            for (ComissionMember c : comissionMembers) {
                combobox.addItem("" + c.getId(), c.getFullName() + (c.getDegree() == null || "".equals(c.getDegree())? "" : ",  " + c.getDegree()));
            }
        }
        request.setAttribute(WebKeys.APPLICATION_EXPERTS_COMBO, combobox);
        return combobox;
    }
    
    private void saveApplictionExpert(HttpServletRequest request, HttpServletResponse response, Application application) {
        int applicationExpertsCount = DataConverter.parseInt(request.getParameter("application_experts_count"), 0);
        ApplicationsDataProvider applicationsDataProvider = getNacidDataProvider().getApplicationsDataProvider();
        
        List<ApplicationExpert> oldExperts = application.getApplicationExperts();
        Set<Integer> oldExpertIds = new HashSet<Integer>();//starite experti kym tova zaqvlenie
        Set<Integer> newExpertIds = new HashSet<Integer>();//pazi novodobavenite experti (koito gi nqma v oldExpertIds)
        if (oldExperts != null) {
            for (ApplicationExpert e : oldExperts) {
                oldExpertIds.add(e.getExpertId());
            }
        }
        applicationsDataProvider.deleteApplicationExperts(application.getId());
        if (applicationExpertsCount > 0) {

            Set<Integer> savedExpertIds = new HashSet<Integer>();
            for (int i = 0; i < applicationExpertsCount; i++) {
                String notes = request.getParameter("application_expert_notes" + i);
                int expertId = DataConverter.parseInt(request.getParameter("application_expert" + i), -1);
                if (expertId <= 0 || savedExpertIds.contains(expertId)) {
                    continue;//already imported or no expert...
                }
                savedExpertIds.add(expertId);

                if (!oldExpertIds.contains(expertId)) {
                    newExpertIds.add(expertId);
                }

                int finished = DataConverter.parseInt(request.getParameter("application_expert_processStat" + i), 0);
                String courseContent = DataConverter.parseString(request.getParameter("application_expert_course_content" + i), null);
                Integer expertPosition = DataConverter.parseInteger(request.getParameter("application_expert_position" + i), null);
                Integer eduLevelId = DataConverter.parseInteger(request.getParameter("application_expert_edu_level_id" + i), null);
//                Integer specialityId = DataConverter.parseInteger(request.getParameter("application_expert_speciality" + i + "Id"), null);
                Integer qualificationId = DataConverter.parseInteger(request.getParameter("application_expert_qualification" + i + "Id"), null);
                String previousBoardDecisions = DataConverter.parseString(request.getParameter("application_expert_previous_board_decisions" + i), null);
                String similarBulgarianPrograms = DataConverter.parseString(request.getParameter("application_expert_similar_bulgarian_programs" + i), null);
                Integer legalReasonId = DataConverter.parseInteger(request.getParameter("application_expert_legal_reason" + i), null);

                Set<Integer> specialityIds = new LinkedHashSet<Integer>();
                int specialitiesCount = DataConverter.parseInt(request.getParameter("applicationExpertSpeciality" + i + "_specialities_count"), 0);
                for (int j = 1; j <= specialitiesCount; j++) {
                    Integer specialityId = DataConverter.parseInteger(request.getParameter("specialitiesListItem_applicationExpertSpeciality" + i + "_" + j), null);
                    if (specialityId != null) {
                        specialityIds.add(specialityId);
                    }

                }
                Integer singleSpecialityId = DataConverter.parseInteger(request.getParameter("applicationExpertSpeciality" + i + "Id"), null);
                if (singleSpecialityId != null && !specialityIds.contains(singleSpecialityId)) {
                    specialityIds.add(singleSpecialityId);
                }
                applicationsDataProvider.addApplicationExpert(application.getId(),expertId, notes, finished, courseContent, expertPosition, legalReasonId, specialityIds == null ? null : new ArrayList<Integer>(specialityIds), qualificationId, eduLevelId, previousBoardDecisions, similarBulgarianPrograms);
            }

        }
        if (PropertiesBundle.isEmailsEnabled()) {
        	sendExpertMails(oldExpertIds, newExpertIds, getNacidDataProvider(), application.getApplicationNumber());
        }
        
        writeToResponse(response, MessagesBundle.getMessagesBundle().getValue("application_experts_changed"));
        
    }
    
    private static void sendExpertMails(Set<Integer> oldExpertIds, Set<Integer> newExpertIds, NacidDataProvider nDP, String applicationNumber) {
        Set<Integer> added = new HashSet<Integer>();
        added.addAll(newExpertIds);
        added.removeAll(oldExpertIds);
        Set<Integer> removed = new HashSet<Integer>();
        removed.addAll(oldExpertIds);
        removed.removeAll(newExpertIds);
        
        ComissionMemberDataProvider cmDP = nDP.getComissionMemberDataProvider();
        
        //members added to application
        Map<String, String> toMAdded = createMailMap(added, cmDP);
        
        //members removed from application
        Map<String, String> toMRemoved = createMailMap(removed, cmDP);
        
        
        if(!toMAdded.isEmpty()) {
            sendNotifications(toMAdded, nDP, true, applicationNumber);
        }
       
        if(!toMRemoved.isEmpty()) {
            sendNotifications(toMRemoved, nDP, false, applicationNumber);
        }
        
    }
    
    private static void sendNotifications(Map<String, String> experts, NacidDataProvider nDP, 
            boolean isForAdded, String applicationNumber) {
        MailDataProvider mdp = nDP.getMailDataProvider();
        
        UtilsDataProvider udp = nDP.getUtilsDataProvider();
        String subject = udp.getCommonVariableValue(
                isForAdded ? UtilsDataProvider.EXPERT_ADDED_NOTIFICATION_SUBJECT : UtilsDataProvider.EXPERT_REMOVED_NOTIFICATION_SUBJECT);
        String body = udp.getCommonVariableValue(
                isForAdded ? UtilsDataProvider.EXPERT_ADDED_NOTIFICATION_BODY : UtilsDataProvider.EXPERT_REMOVED_NOTIFICATION_BODY);
        String sender = udp.getCommonVariableValue(UtilsDataProvider.MAIL_SENDER);
        
        body = MessageFormat.format(body, applicationNumber);
        
        Map<String, String> fromM = Collections.singletonMap(sender, sender);
        
        MailBean msg = new MailBeanImpl(fromM, sender, experts, ((Map<String, String>)null), 
                ((Map<String, String>)null), subject,
                fromM, body, new Date(), mdp.getSession());
        mdp.sendMessage(msg);
    }
    
    private static Map<String, String> createMailMap(Set<Integer> memberIds, ComissionMemberDataProvider cmDP) {
        Map<String, String> ret = new HashMap<String, String>();
        for(Integer i : memberIds) {
            ComissionMember cm = cmDP.getComissionMember(i.intValue());
            if(cm.getEmail() != null && cm.getEmail().trim().length() > 0) {
                ret.put(cm.getEmail(), cm.getFname() + " " + cm.getLname());
            }
        }
        return ret;
    }
}
