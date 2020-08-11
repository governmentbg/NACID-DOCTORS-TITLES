package com.nacid.bl.comision;

import java.util.List;
import java.util.Map;

import com.nacid.bl.applications.ApplicationDetailsForReport;

/**
 * @author ggeorgiev
 *
 */
public interface CommissionProtocol {
	/**
	 * @return sessionNumber / sessionDate
	 */
	public String getReportNumber();
	/**
	 * @return sessionDate
	 */
	public String getCommissionSessionDate();
	/**
	 * @return sessionTime
	 */
	public String getCommissionSessionTime();
	/**
	 * @return predsedatelq na komisiqta
	 */
	public CommissionMemberForReport getCommissionChairman();
	/**
	 * @return sekretarq na komisiqta
	 */
	public CommissionMemberForReport getCommissionSecretary();
	/**
	 * @return samo CommissionMemberite koito imat CommissionPosition = MEMBER
	 */
	public List<CommissionMemberForReport> getCommissionMembers();
	/**
	 * @return commissionmemberite koito imat CommissionPosition = CONSULTANT
	 */
	public List<CommissionMemberForReport> getCommissionConsultants();
	/**
	 * @return lipsvashtite 4lenove na komisiqta
	 */
	public List<CommissionMemberForReport> getMissingCommissionMembers();
	public List<ApplicationDetailsForReport> getRecognizedApplications();
	/**
	 * @return
	 * key - legalReasonId
	 * value - list of {@link ApplicationDetailsForReport}
	 */
	public Map<Integer, List<ApplicationDetailsForReport>> getTerminatedApplications() ;
	/**
	 * @return
	 * key - legalReasonId
	 * value - list of {@link ApplicationDetailsForReport}
	 */
	public Map<Integer, List<ApplicationDetailsForReport>> getPostponedApplications();
	/**
	 * @return
	 * key - legalReasonId
	 * value - list of {@link ApplicationDetailsForReport}
	 */
	public Map<Integer, List<ApplicationDetailsForReport>> getRefusedApplications();
	
	/**
	 * Map - key - applicationId
	 *     - value - map
	 *         - key - legalReasonId
	 *         - value - applications with the given legalReasonId!
	 * @return
	 */
	public Map<Integer, Map<Integer, List<ApplicationDetailsForReport>>> getApplicationsMap();
}
