package com.nacid.bl.comision;

import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.ApplicationDetailsForReport;
import com.nacid.bl.applications.ApplicationForList;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

public interface CommissionCalendarDataProvider {

    public List<CommissionCalendar> getCommissionCalendarsByStatus(int status);
    public List<CommissionCalendar> getCommissionCalendarsByDate(Date date);
    public List<CommissionCalendar> getCommissionCalendarsByApplication(int applicationId);
    public CommissionCalendar getCommissionCalendar(int id);
    public int saveCommissionCalendar(int id, Date dateAndTime, String notes, int sessionStatusId/*, int sessionNumber*/);


    public List<Application> getCommissionApplications(int commissionCalendarId);
    /**
     * @param commissionCalendarId
     * @return vsi4ki Applications koito shte se razglejdat za dadeno zasedanie na komisiqta
     */
    public List<ApplicationForList> getCommissionApplicationsForList(int commissionCalendarId);
    public List<Integer> getCommissionApplicationIds(int commissionCalendarId);
    
    /**
     * iztriva vsi4ki applicationIds za dadeno zasedanie na komisiqta
     * @param calendarId
     */
    public void deleteCommissionApplications(int calendarId);
    public int addApplicationToCalendar(int calendarId, int applicationId);
    
    public void setApplicationsToCalendar(int calendarId, List<Integer> applicationIds);
    
    /**
     * @param commissionCalendarId
     * @return vsi4ki CommissionParticipation koito shte u4astvat v dadeno zasedanie
     */
    public List<CommissionParticipation> getCommissionParticipations(int commissionCalendarId);
    
    public void deleteCommissionMembersList(int calendarId);
    public int addCommissionMemberToCalendar(int calendarId, int commissionMemberId, boolean notified, boolean participated, String notes);
    public CommissionCalendarProtocol loadCalendarProtocol(int calendarId, boolean loadContent);
    public void addCalendarProtocol(int id, InputStream content, String contentType, String fileName, int fileSize);
    public CommissionProtocol getCommissionProtocolForReport(int calendarId);
    
    /**
     * @return List<{@link ApplicationDetailsForReport}>, po daden commissionCalendarId, za printirane v report.xls
     */
    public List<ApplicationDetailsForReport> getApplicationDetailsForReport(int commissionCalendarId);

    /**
     * vry6ta poslednata komisiq, razglejdala dadeno zaqvlenie - tuk se priema 4e ako trqbva da se izdade zaqvlenie za priznato obrazovanie, to poslednata komisiq razglejdala tova zaqvlenie 6te go e priznala
     * sy6toto vaji i za otkazite
     * @param applicationId
     * @return
     */
    public CommissionCalendar getLastCommissionExaminedApplication(int applicationId);
}
