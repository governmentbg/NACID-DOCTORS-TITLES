package com.nacid.bl.impl.comission;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.AppStatusHistory;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.ApplicationDetailsForReport;
import com.nacid.bl.comision.*;
import com.nacid.bl.impl.applications.ApplicationDetailsForReportImpl;
import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.bl.nomenclatures.CommissionPosition;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;

import java.util.*;

public class CommissionProtocolImpl implements CommissionProtocol {
	private CommissionCalendar commissionCalendar;
	private CommissionMemberForReport chairman;
	private CommissionMemberForReport secretary;
	private List<CommissionMemberForReport> members;
	private List<CommissionMemberForReport> consultants;
	private List<CommissionMemberForReport> missing;
	private List<ApplicationDetailsForReport> recognized; //Priznati
	private Map<Integer, List<ApplicationDetailsForReport>> terminated; //Prekrateni
	private Map<Integer, List<ApplicationDetailsForReport>> postponed; //Otlojeni
	private Map<Integer, List<ApplicationDetailsForReport>> refused; //Otkazani
	private Map<Integer, Map<Integer, List<ApplicationDetailsForReport>>> applicationsMap;
	
	
	public CommissionProtocolImpl(NacidDataProvider nacidDataProvider, CommissionCalendar commissionCalendar) {
		this.commissionCalendar = commissionCalendar;
		
		UtilsDataProvider utilsDataProvider = nacidDataProvider.getUtilsDataProvider();
		CommissionCalendarDataProvider commissionCalendarDataProvider = nacidDataProvider.getCommissionCalendarDataProvider();

		
		//Generating secretary
		String degreeAndName = utilsDataProvider.getCommonVariableValue("secretaryDegreeAndName");
		String commissionPosition = utilsDataProvider.getCommonVariableValue("secretaryCommissionPosition");
		String institutionAndPosition = utilsDataProvider.getCommonVariableValue("secretaryInstAndPos");
		secretary = new CommissionMemberForReportImpl(commissionPosition, degreeAndName, institutionAndPosition);
		
		//Generating chairman
		degreeAndName = utilsDataProvider.getCommonVariableValue("chairmanDegreeAndName");
		commissionPosition = utilsDataProvider.getCommonVariableValue("chairmanCommissionPosition");
		institutionAndPosition = utilsDataProvider.getCommonVariableValue("chairmanInstAndPos");
		chairman = new CommissionMemberForReportImpl(commissionPosition, degreeAndName, institutionAndPosition);
		
		
		//Generating members, consultants, missing members
		List<CommissionParticipation> commissionParticipations = commissionCalendarDataProvider.getCommissionParticipations(commissionCalendar.getId());
		members = getCommissionMembersByCommissionPositionId(commissionParticipations, CommissionPosition.COMMISSION_POSITION_MEMBER, true);
		consultants = getCommissionMembersByCommissionPositionId(commissionParticipations, CommissionPosition.COMMISSION_POSITION_CONSULTANT, true);
		missing = getCommissionMembersByCommissionPositionId(commissionParticipations, 0, false);
		
		//Generating application details
		List<Application> applications = commissionCalendarDataProvider.getCommissionApplications(commissionCalendar.getId());
		applicationsMap = getApplicationsByStatus(applications, nacidDataProvider);
		
		postponed = applicationsMap.get(ApplicationStatus.APPLICATION_POSTPONED_STATUS_CODE);
		refused = applicationsMap.get(ApplicationStatus.APPLICATION_REFUSED_STATUS_CODE);
		terminated = applicationsMap.get(ApplicationStatus.APPLICATION_TERMINATED_STATUS_CODE);
		
		Map<Integer, List<ApplicationDetailsForReport>> recognized = applicationsMap.get(ApplicationStatus.APPLICATION_PRIZNATO_STATUS_CODE);
		this.recognized = recognized == null ? null : recognized.get(null);
		
	}
	public String getCommissionSessionDate() {
		return DataConverter.formatDate(commissionCalendar.getDateAndTime());
	}

	public String getCommissionSessionTime() {
		return DataConverter.formatTime(commissionCalendar.getDateAndTime(), false);
	}
	
	public String getReportNumber() {
		return commissionCalendar.getSessionNumber() + "/" + DataConverter.formatDate(commissionCalendar.getDateAndTime());
	}

	public CommissionMemberForReport getCommissionChairman() {
		return chairman;
	}
	public CommissionMemberForReport getCommissionSecretary() {
		return secretary;
	}

	public List<CommissionMemberForReport> getCommissionMembers() {
		return members;
	}
	
	public List<CommissionMemberForReport> getCommissionConsultants() {
		return consultants;
	}
	
	public List<CommissionMemberForReport> getMissingCommissionMembers() {
		return missing;
		
	}

	public List<ApplicationDetailsForReport> getRecognizedApplications() {
		return recognized;
	}
	
		
	public Map<Integer, List<ApplicationDetailsForReport>> getPostponedApplications() {
		return postponed;
	}

	
	public Map<Integer, List<ApplicationDetailsForReport>> getRefusedApplications() {
		return refused;
	}

	

	public Map<Integer, List<ApplicationDetailsForReport>> getTerminatedApplications() {
		return terminated;
	}
	
	
	public Map<Integer, Map<Integer, List<ApplicationDetailsForReport>>> getApplicationsMap() {
        return applicationsMap;
    }
    /**
	 * @param commissionParticipations - spisak s {@link CommissionParticipation} objects
	 * @param commissionPositionId - id na tipa poziciq (edin ot definiranite v {@link CommissionPosition} static members (ako e 0 - vry6ta vsi4ki)
	 * @param participated - true - uchastval, false - neu4astval
	 * @return samo notified member-ite, otgovarqshti na gornite kriterii
	 */
	private static List<CommissionMemberForReport> getCommissionMembersByCommissionPositionId(List<CommissionParticipation> commissionParticipations, int commissionPositionId, boolean participated) {
		List<CommissionMemberForReport> result = new ArrayList<CommissionMemberForReport>();
		if (commissionParticipations != null) {
			for (CommissionParticipation cp:commissionParticipations) {
				ComissionMember cm = cp.getCommissionMember();
			    //System.out.println(cp.getCommissionMember().getFullName() + "  " + cp.isNotified() + "  " + cp.isParticipated() + "  " + cm.getComissionPos().getId());
			    if (cp.isNotified() && (commissionPositionId == 0 || cm.getComissionPos().getId() == commissionPositionId) && cp.isParticipated() == participated) {
					result.add(new CommissionMemberForReportImpl(cm));
				}
			}	
		}
		
		return result.size() == 0 ? null : result;
	}
	
	/**
	 * @return
	 * Outer Map -
	 * key - statusId
	 * value - inner map
	 * 		-- key - legalReasonId
	 * 		-- value - List of ApplicationDetailsForReport 
	 * 		v zapis s key == null se slagat absoliutno vsi4ki ApplicationDetailsForReport nezavisimo ot legalReason-a
	 */
	private static Map<Integer, Map<Integer, List<ApplicationDetailsForReport>>> getApplicationsByStatus(List<Application> applications, NacidDataProvider nacidDataProvider) {
		Map<Integer, Map<Integer, List<ApplicationDetailsForReport>>> result = new HashMap<Integer, Map<Integer,List<ApplicationDetailsForReport>>>();
		if (applications != null) {
			for (Application a:applications) {
				ApplicationDetailsForReport applicationDetailsForReport = new ApplicationDetailsForReportImpl(nacidDataProvider, a);
				Map<Integer, List<ApplicationDetailsForReport>> appsByStatus = result.get(a.getApplicationStatusId());
				if (appsByStatus == null) {
					appsByStatus = new LinkedHashMap<Integer, List<ApplicationDetailsForReport>>();
					result.put(a.getApplicationStatusId(), appsByStatus);
				}
				List<AppStatusHistory> history = a.getAppStatusHistory();
				Integer currentLegalReasonId = history.get(0).getStatLegalReasonId();
				List<ApplicationDetailsForReport> appsByLegalReason = appsByStatus.get(currentLegalReasonId);
				if (appsByLegalReason == null) {
					appsByLegalReason = new ArrayList<ApplicationDetailsForReport>();
					appsByStatus.put(currentLegalReasonId, appsByLegalReason);
				}
				appsByLegalReason.add(applicationDetailsForReport);
				if (currentLegalReasonId != null) {
					appsByLegalReason = appsByStatus.get(null);
					if (appsByLegalReason == null) {
						appsByLegalReason = new ArrayList<ApplicationDetailsForReport>();
						appsByStatus.put(null, appsByLegalReason);
					}
					appsByLegalReason.add(applicationDetailsForReport);
				}
				
			}
		}
		return result;
	}
	
	
	public static List<ApplicationDetailsForReport> joinList(List<ApplicationDetailsForReport>... lst) {
		List<ApplicationDetailsForReport> result = new ArrayList<ApplicationDetailsForReport>();
		if (lst != null && lst.length > 0) {
			for (List<ApplicationDetailsForReport> list:lst) {
				if (list != null && list.size() > 0) {
					result.addAll(list);
				}
			}
		}
		return result.size() == 0 ? null : result;	
	}
}
