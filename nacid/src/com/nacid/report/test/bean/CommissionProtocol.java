package com.nacid.report.test.bean;

import java.util.ArrayList;
import java.util.List;

public class CommissionProtocol {
	private String reportNumber; //номер/дата на заседанието
	private String commissionSessionDate; //дата на заседанието
	private String commissionSessionTime; //час на заседанието
	private CommissionMember commissionChairman;
	private CommissionMember commissionSecretary;
	private List<CommissionMember> commissionMembers;
	private List<CommissionMember> commissionConsultants;
	private List<CommissionMember> missingCommissionMembers;
	private List<Application> recognizedApplications;
	private List<Application> postponedApplications; //отложени  на основание член 9
	private List<Application> terminatedApplications; //прекратени на основание член 13
	private List<Application> refusedApplications; //отказано признаване на основание чл. 12, ал. 1, т. 1 във връзка с чл. 11, ал. 2  
	
	/**
	 * @return
	 */
	public static CommissionProtocol getCommissionProtocl() {
		CommissionProtocol result = new CommissionProtocol();
		result.setReportNumber("10/10.10.2010");
		result.setCommissionSessionDate("10.10.2010");
		result.setCommissionSessionTime("14:00");
		result.setCommissionChairman(new CommissionMember("проф д-р Ангел Иванов Ангелов", "член на комисията", "зам. ректор МСЕИ, УАСГ"));
		result.setCommissionSecretary(new CommissionMember("Доц.д-р инж. Емил Георгиев Михайлов", "консултант", "зам. ректор МСЕИ, УАСГ"));
		List<CommissionMember> members = new ArrayList<CommissionMember>();
		members.add(new CommissionMember("проф дтн. Пешо Пешев", "член на комисията", "зам. ректор ФКСУ, ТУ"));
		members.add(new CommissionMember("доц.д-р Христо Иванов Христов", "член на комисията", null));
		members.add(new CommissionMember("Мария Атанасова Фъртунова", "член на комисията", "директор дирекция ПВО, МОМН"));
		members.add(result.getCommissionChairman());
		result.setCommissionMembers(members);
		members = new ArrayList<CommissionMember>();
		members.add(new CommissionMember("доц.д-р Тодор Тодоров Тодоров", "консултант", null));
		members.add(new CommissionMember("проф дтн. Георги Георгиев Георгиев", "консултант", "зам. ректор ФКСУ, ТУ"));
		members.add(new CommissionMember("Иван Иванов Иванов", "консултант", "директор дирекция ПВО, МОМН"));
		members.add(result.getCommissionSecretary());
		result.setCommissionConsultants(members);
		members = new ArrayList<CommissionMember>();
		members.add(new CommissionMember("доц.д-р Христо Христов Христов", "консултант", null));
		result.setMissingCommissionMembers(members);
		List<Application> apps = new ArrayList<Application>();
		Application app = new Application();
		app.setApplicantName("Иван Иванов Иванов");
		app.setApplicationNumber("10/12.01.2010");
		
		app.setTrainingCourseLocation("Варна, България");
		app.setDiplomaSpecialityNames("програмист");
		app.setDiplomaEducationLevel("магистър");
		app.setAcknowledgedEducationLevel("бакалавър");
		app.setAcknowledgedSpecialityName("фризьор");
		apps.add(app);
		app = new Application();
		app.setApplicantName("Христо Христов Христов");
		app.setApplicationNumber("12/12.10.2000");
		app.setTrainingCourseLocation("София, България");
		app.setDiplomaSpecialityNames("финанси");
		app.setDiplomaEducationLevel("магистър");
		app.setAcknowledgedEducationLevel("доктор");
		app.setAcknowledgedSpecialityName("финанси");
		apps.add(app);
		result.setRecognizedApplications(apps);
		result.setPostponedApplications(apps);
		result.setPostponedApplications(apps);
		result.setTerminatedApplications(apps);
		
		return result;
	}
	
	
	
	public String getReportNumber() {
		return reportNumber;
	}
	public void setReportNumber(String reportNumber) {
		this.reportNumber = reportNumber;
	}
	public String getCommissionSessionDate() {
		return commissionSessionDate;
	}
	public void setCommissionSessionDate(String commissionSessionDate) {
		this.commissionSessionDate = commissionSessionDate;
	}
	public String getCommissionSessionTime() {
		return commissionSessionTime;
	}
	public void setCommissionSessionTime(String commissionSessionTime) {
		this.commissionSessionTime = commissionSessionTime;
	}
	
	public CommissionMember getCommissionChairman() {
		return commissionChairman;
	}
	public void setCommissionChairman(CommissionMember commissionChairman) {
		this.commissionChairman = commissionChairman;
	}
	public CommissionMember getCommissionSecretary() {
		return commissionSecretary;
	}
	public void setCommissionSecretary(CommissionMember commissionSecretary) {
		this.commissionSecretary = commissionSecretary;
	}
	public List<CommissionMember> getCommissionMembers() {
		return commissionMembers;
	}
	public void setCommissionMembers(List<CommissionMember> commissionMembers) {
		this.commissionMembers = commissionMembers;
	}
	public List<CommissionMember> getCommissionConsultants() {
		return commissionConsultants;
	}
	public void setCommissionConsultants(List<CommissionMember> commissionConsultants) {
		this.commissionConsultants = commissionConsultants;
	}
	public List<CommissionMember> getMissingCommissionMembers() {
		return missingCommissionMembers;
	}
	public void setMissingCommissionMembers(List<CommissionMember> missingCommissionMembers) {
		this.missingCommissionMembers = missingCommissionMembers;
	}
	public List<Application> getRecognizedApplications() {
		return recognizedApplications;
	}
	public void setRecognizedApplications(List<Application> recognizedApplications) {
		this.recognizedApplications = recognizedApplications;
	}
	public List<Application> getPostponedApplications() {
		return postponedApplications;
	}
	public void setPostponedApplications(List<Application> postponedApplications) {
		this.postponedApplications = postponedApplications;
	}
	
	public List<Application> getTerminatedApplications() {
		return terminatedApplications;
	}
	public void setTerminatedApplications(List<Application> terminatedApplications) {
		this.terminatedApplications = terminatedApplications;
	}
	public List<Application> getRefusedApplications() {
		return refusedApplications;
	}
	public void setRefusedApplications(List<Application> refusedApplications) {
		this.refusedApplications = refusedApplications;
	}
	
}
