package com.nacid.web.model.inquires;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.*;
import com.nacid.bl.comision.CommissionCalendar;
import com.nacid.bl.comision.CommissionCalendarDataProvider;
import com.nacid.bl.impl.applications.UniversityWithFaculty;
import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.data.DataConverter;
import com.nacid.data.inquire.ApplicationForInquireRecord;
import com.nacid.web.model.applications.AppStatusHistoryWebModel;
import com.nacid.web.model.comission.CommissionCalendarWebModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class ScreenFormatWebModel {
	public static final String DOCFLOW_NUMBER = "docFlowNumber";
	public static final String OWNER_NAMES = "ownerNames";
	public static final String APPLICANT_NAMES = "applicantNames";
	public static final String OWNER_CITIZENSHIP = "ownerCitizenship";
	public static final String UNIVERSITY_NAME = "universityName";
	public static final String UNIVERSITY_COUNTRY = "universityCountry";
	public static final String UNIVERSITY_EXAMINATION_RECOGNIZED = "universityExaminationRecognized";
	public static final String TRAINING_LOCAITON = "trainingLocation";
	public static final String DIPLOMA_EDU_LEVEL = "diplomaEduLevel";
	public static final String DIPLOMA_SPECIALTIY = "diplomaSpeciality";
	public static final String DIPLOMA_QUALIFICATION = "diplomaQualification";
	public static final String DIPLOMA_DATE = "diplomaDate";
	public static final String TRAINING_START = "trainingStart";
	public static final String TRAINING_END = "trainingEnd";
	public static final String TRAINING_FORM = "trainingForm";
	public static final String SCHOOL_DIPLOMA_INFO = "schoolDiplomaInfo";
	public static final String PREVIOUS_UNI_DIPLOMA_INFO = "previousUniDiplomaInfo";
	
	
	public static final String COMMISSION_STATUSES = "commissionStatuses";
	public static final String COMMISSION_CALENDARS = "commissionCalendars";
	public static final String MOTIVES = "motives";
	public static final String RECOGNIZED_EDU_LEVEL = "recognizedEduLevel";
	public static final String RECOGNIZED_SPECIALITIES = "recognizedSpecialities";
	public static final String RECOGNIZED_QUALIFICATION = "recognizedQualification";
	public static final String APPLICATION_STATUS = "applicationStatus";
	public static final String DOCFLOW_STATUS = "docflowStatus";

	public static final Map<String, Boolean> screen1Elements = new HashMap<String, Boolean>();
	static {
		screen1Elements.put(DOCFLOW_NUMBER, true);
		screen1Elements.put(UNIVERSITY_NAME, true);
		screen1Elements.put(UNIVERSITY_COUNTRY, true);
		screen1Elements.put(DIPLOMA_EDU_LEVEL, true);
		screen1Elements.put(DIPLOMA_SPECIALTIY, true);
		screen1Elements.put(COMMISSION_STATUSES, true);
	}
	public static final Map<String, Boolean> screen2Elements = new HashMap<String, Boolean>();
	static {
		screen2Elements.putAll(screen1Elements);
		screen2Elements.put(RECOGNIZED_EDU_LEVEL, true);
		screen2Elements.put(RECOGNIZED_SPECIALITIES, true);
		
	}
	public static final Map<String, Boolean> screen3Elements = new HashMap<String, Boolean>();
	static {
		screen3Elements.putAll(screen2Elements);
		screen3Elements.put(DIPLOMA_QUALIFICATION, true);
		screen3Elements.put(RECOGNIZED_QUALIFICATION, true);
		screen3Elements.put(OWNER_NAMES, true);
		screen3Elements.put(TRAINING_LOCAITON, true);
	}
	public static final Map<String, Boolean> screen4Elements = new HashMap<String, Boolean>();
	static {
		screen4Elements.putAll(screen3Elements);
		screen4Elements.put(OWNER_CITIZENSHIP, true);
		screen4Elements.put(UNIVERSITY_EXAMINATION_RECOGNIZED, true);
		screen4Elements.put(DIPLOMA_DATE, true);
		screen4Elements.put(TRAINING_START, true);
		screen4Elements.put(TRAINING_END, true);
		screen4Elements.put(TRAINING_FORM, true);
		screen4Elements.put(SCHOOL_DIPLOMA_INFO, true);
		screen4Elements.put(PREVIOUS_UNI_DIPLOMA_INFO, true);
		screen4Elements.put(COMMISSION_CALENDARS, true);
		screen4Elements.put(MOTIVES, true);
		screen4Elements.put(APPLICATION_STATUS, true);
		screen4Elements.put(DOCFLOW_STATUS, true);
	}
	
	
	
	
	
	private String docFlowNumber = "N/A";
	private String ownerNames = "N/A";
	private String applicantNames = "N/A";
	private String ownerCitizenship = "N/A";
	private String universityName = "N/A";
	private String universityCountry = "N/A";
	private String universityExaminationRecognized = "N/A";
	private String trainingLocation = "N/A";
	private String diplomaEduLevel = "N/A";
	private String diplomaSpeciality = "N/A";
	private String diplomaQualification = "N/A";
	private String diplomaDate = "N/A";
	private String trainingStart = "N/A";
	private String trainingEnd = "N/A";
	private String trainingForm = "N/A";
	private String schoolDiplomaInfo = "N/A";
	private String previousUniDiplomaInfo = "N/A";
	private List<AppStatusHistoryWebModel> commissionStatuses;
	private List<CommissionCalendarWebModel> commissionCalendars;
	private String motives = "N/A";
	private String recognizedEduLevel = "N/A";
	private String recognizedSpecialities = "N/A";
	private String recognizedQualification = "N/A";
	private String applicationStatus = "N/A";
	private String docflowStatus = "N/A";
	private boolean showUniversityCountry = true;
	private boolean showUniversityExamination = true;
	/**
	 * @param a
	 * @param map - trqbva da se opredeli dali se iskat UNIVERSITY_COUNTRY (i razni drugi poleta), za da se znae dali kogato se sglobqva
	 * universityName, da se slepq i tazi informaciq kym nego! 
	 */
	public ScreenFormatWebModel(ApplicationForInquireRecord a, Map<String, Boolean> map, NacidDataProvider nacidDataProvider) {

		
		ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
        CommissionCalendarDataProvider commissionCalendarDataProvider = nacidDataProvider.getCommissionCalendarDataProvider();
        TrainingCourseDataProvider trainingCourseDataProvider = nacidDataProvider.getTrainingCourseDataProvider();
		
		this.docFlowNumber = a.getDocflowNumber();
		this.ownerNames = a.getOwnerNames();
        this.applicantNames = a.getApplicantNames();
		this.ownerCitizenship = a.getOwnerCitizenshipName();

		if (!a.isJointDegree()) {
			universityName = CollectionUtils.isEmpty(a.getUniversityName()) || a.getUniversityName().get(0) == null ? "" : a.getUniversityName().get(0);
			universityCountry = a.getUniversityCountry() == null ? "" : a.getUniversityCountry();
			universityExaminationRecognized = a.getUniversityExaminationRecognized() == null ? "N/A" : (a.getUniversityExaminationRecognized() == 1 ? "легитимно" : "нелегитимно");
		} else {
			universityCountry = null;
			universityExaminationRecognized = null;
			showUniversityCountry = false;
			showUniversityExamination = false;

            //Tuk se pravi obryshtenie kym bazata da se prochete training course-a, no ne e golqm problem zashtoto
            List<String> res = new ArrayList<String>();
            TrainingCourse tc = trainingCourseDataProvider.getTrainingCourse(a.getTceId());
            List<? extends UniversityWithFaculty> universities = tc.getUniversityWithFaculties();
			if (universities != null) {
				for (UniversityWithFaculty uf:universities) {
					University u = uf.getUniversity();
					UniversityExamination examination = tc.getUniversityExaminationByUniversity(u.getId());
					List<String> l = new ArrayList<String>();
					if (map.containsKey(UNIVERSITY_NAME) && !StringUtils.isEmpty(u.getBgName())) {
						l.add(u.getBgName());	
					}
					if (map.containsKey(UNIVERSITY_COUNTRY) && u.getCountry() != null && !StringUtils.isEmpty(u.getCountry().getName())) {
						l.add(u.getCountry().getName());
					}
					if (map.containsKey(UNIVERSITY_EXAMINATION_RECOGNIZED)) {
						l.add(examination == null ? "N/A" : (examination.isRecognized() ? "легитимно" : "нелегитимно"));	
					}
					
					res.add(StringUtils.join(l, ", "));
					
				}
			}
			universityName = res.size() == 0 ? "N/A" : StringUtils.join(res, "; ");
		}
        List<String> locations = a.getTrainingLocation();
        trainingLocation = StringUtils.join(locations, "; ");

		this.diplomaEduLevel = a.getEduLevelName() == null ? "N/A" : a.getEduLevelName();
		this.diplomaQualification = a.getQualificationName() == null ? "N/A" : a.getQualificationName();
		this.diplomaSpeciality = a.getSpecialities() == null ? "N/A" : StringUtils.join(a.getSpecialities(), ";");
		this.diplomaDate = DataConverter.formatDate(a.getDiplomaDate(), "N/A");
		this.trainingStart = DataConverter.formatYear(a.getTrainingStart(), "N/A");
		this.trainingEnd = DataConverter.formatYear(a.getTrainingEnd(), "N/A");

		this.trainingForm  = a.getTrainingForm() ==  null ? "N/A" : a.getTrainingForm();

        List<String> lst = new ArrayList<String>();
		if (!StringUtils.isEmpty(a.getSchoolName())) {
			lst.add(a.getSchoolName());
		}
		if (a.getSchoolCountryName() != null) {
			lst.add(a.getSchoolCountryName());
		}
		if (!StringUtils.isEmpty(a.getSchoolCity())) {
			lst.add(a.getSchoolCity());
		}
		if (a.getSchoolGraduationDate() != null) {
			lst.add(DataConverter.formatYear(a.getSchoolGraduationDate()) + " г.");
		}
		if (!StringUtils.isEmpty(a.getSchoolNotes())) {
			lst.add(a.getSchoolNotes());
		}
		this.schoolDiplomaInfo = lst.size() == 0 ? "N/A" : StringUtils.join(lst, "; ");
		
		
		
		
		
		lst = new ArrayList<String>();

		if (!StringUtils.isEmpty(a.getPreviousDiplomaBgName())) {
			lst.add(a.getPreviousDiplomaBgName());
			if (a.getPreviousDiplomaCountry() != null) {
				lst.add(a.getPreviousDiplomaCountry());
			}
		}
		if (a.getPreviousDiplomaEduLevelName() != null) {
			lst.add(a.getPreviousDiplomaEduLevelName());
		}
		if (a.getPrevDiplomaGraduationDate() != null) {
			lst.add(DataConverter.formatYear(a.getPrevDiplomaGraduationDate()) + " г.");
		}
		if (!StringUtils.isEmpty(a.getPrevDiplomaNotes())) {
			lst.add(a.getPrevDiplomaNotes());
		}
		this.previousUniDiplomaInfo = lst.size() == 0 ? "N/A" : StringUtils.join(lst, "; ");
		if (!StringUtils.isEmpty(a.getSummary())) {
			this.motives = a.getSummary();	
		}
		if (a.getRecognizedEduLevelName() != null) {
			this.recognizedEduLevel = a.getRecognizedEduLevelName();
		}
		List<String> recognizedSpecialities = a.getRecognizedSpecialities();
		if (recognizedSpecialities != null) {
			this.recognizedSpecialities = StringUtils.join(recognizedSpecialities, ", ");
		}
		if (a.getRecognizedQualificationName() != null) {
			this.recognizedQualification = a.getRecognizedQualificationName();
		}
		this.applicationStatus = a.getApplicationStatusName();
		this.docflowStatus = a.getDocflowStatusName();


        List<AppStatusHistory> statusHistory = getCommissionAppStatusHistory(applicationsDataProvider.getAppStatusHistory(a.getId()));
		if (statusHistory != null) {
			commissionStatuses = new ArrayList<AppStatusHistoryWebModel>();
			for (AppStatusHistory s:statusHistory) {
				commissionStatuses.add(new AppStatusHistoryWebModel(s));
			}
		}

		List<CommissionCalendar> commissionCalendars = commissionCalendarDataProvider.getCommissionCalendarsByApplication(a.getId());
		if (commissionCalendars != null) {
			this.commissionCalendars = new ArrayList<CommissionCalendarWebModel>();
			for (CommissionCalendar c:commissionCalendars) {
				this.commissionCalendars.add(new CommissionCalendarWebModel(c));
			}
		}
	}

    public List<AppStatusHistory> getCommissionAppStatusHistory(List<AppStatusHistory> statusHistory) {
        List<AppStatusHistory> result = new ArrayList<AppStatusHistory>();
        if (statusHistory != null) {
            Set<Integer> availableStatuses = new HashSet<Integer>(ApplicationStatus.RUDI_APPLICATION_STATUSES_FROM_COMMISSION);
            for (AppStatusHistory s:statusHistory) {
                if (availableStatuses.contains(s.getApplicationStatusId())) {
                    result.add(s);
                }

            }
        }
        return result.size() == 0 ? null : result;
    }
	
	public String getDocFlowNumber() {
		return docFlowNumber;
	}

	public String getOwnerNames() {
		return ownerNames;
	}

	public String getOwnerCitizenship() {
		return ownerCitizenship;
	}

	public String getUniversityName() {
		return universityName;
	}

	public String getUniversityCountry() {
		return universityCountry;
	}

	public String getUniversityExaminationRecognized() {
		return universityExaminationRecognized;
	}

	public String getTrainingLocation() {
		return trainingLocation;
	}

	public String getDiplomaEduLevel() {
		return diplomaEduLevel;
	}

	public String getDiplomaSpeciality() {
		return diplomaSpeciality;
	}

	public String getDiplomaQualification() {
		return diplomaQualification;
	}

	public String getDiplomaDate() {
		return diplomaDate;
	}

	public String getTrainingStart() {
		return trainingStart;
	}

	public String getTrainingEnd() {
		return trainingEnd;
	}

	public String getTrainingForm() {
		return trainingForm;
	}

	public String getSchoolDiplomaInfo() {
		return schoolDiplomaInfo;
	}

	public String getPreviousUniDiplomaInfo() {
		return previousUniDiplomaInfo;
	}

	public List<AppStatusHistoryWebModel> getCommissionStatuses() {
		return commissionStatuses;
	}
	public String getCommissionStatusesAsString() {
		if (commissionStatuses == null || commissionStatuses.size() == 0) {
			return "";
		}
		List<String> result = new ArrayList<String>();
		for (AppStatusHistoryWebModel s:commissionStatuses) {
			result.add("    - Дата:" + s.getDateAssigned() + " Статус:" + s.getLongApplicationStatusName());
		}
		return StringUtils.join(result, "\n");
	}

	public List<CommissionCalendarWebModel> getCommissionCalendars() {
		return commissionCalendars;
	}
	public String getCommissionCalendarsAsString() {
		if (commissionCalendars == null || commissionCalendars.size() == 0) {
			return "";
		}
		List<String> result = new ArrayList<String>();
		for (CommissionCalendarWebModel cc:commissionCalendars) {
			result.add("    - Заседание № " + cc.getSessionNumber() + " от " + cc.getDateTime());
		}
		return StringUtils.join(result, "\n");
	}

	public String getMotives() {
		return motives;
	}

	public String getRecognizedEduLevel() {
		return recognizedEduLevel;
	}

	public String getRecognizedSpecialities() {
		return recognizedSpecialities;
	}

	public String getRecognizedQualification() {
		return recognizedQualification;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}

	public boolean isShowUniversityCountry() {
		return showUniversityCountry;
	}

	public boolean isShowUniversityExamination() {
		return showUniversityExamination;
	}

    public String getApplicantNames() {
        return applicantNames;
    }

	public String getDocflowStatus() {
		return docflowStatus;
	}
}
