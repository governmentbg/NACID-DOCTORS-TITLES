package com.nacid.utils;


/**
 * 
 * SQL: select missing applications
 * select application_id,count(*) from app_status_history where date_assigned <= '2010-11-12' and status_id in (10,11,12,13) 
and application_id not in (select comission_agenda.application_id from comission_agenda  
left join comission_calendar on (comission_calendar.id=comission_agenda.session_id)
where application_id in (select application_id from app_status_history where date_assigned <= '2010-11-12' and status_id in (10,11,12,13) group by application_id) 
and comission_calendar.id <= 84
group by comission_agenda.application_id order by comission_agenda.application_id)
group by application_id
order by application_id 
 */
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.sql.DataSource;

import com.nacid.data.applications.AppStatusHistoryRecord;
import com.nacid.data.comission.CommissionAgendaRecord;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.StandAloneDataSource;

/**
 * update-va starite(migrirani) app_status_history records i im settva session_id! za vsi4ki statusi ot komisiq 
 * @author ggeorgiev
 *
 */

public class UpdateOldApplicationStatusHistoryRecords {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	private static final int lastSessionId = 84;
	private static final Date migrationDate = new Date(new GregorianCalendar(2010, Calendar.NOVEMBER, 12).getTimeInMillis());
	public static void main(String[] args) throws SQLException {
		StandAloneDataSource ds = new StandAloneDataSource("jdbc:postgresql://fly.nacid.bg:5432/NACID/", "postgres", "postgres");
		MigratinDBService service = new MigratinDBService(ds);
		List<ApplicationId> migrationIds = service.getApplicationIds(lastSessionId, migrationDate);
		for (ApplicationId appId:migrationIds) {
			List<CommissionAgendaRecord> agenda = service.getCommissionAgendaRecords(appId.getApplicationId(), lastSessionId);
			List<AppStatusHistoryRecord> historyRecords = service.getAppStatusHistoryRecords(appId.getApplicationId(), migrationDate);
			//System.out.print(appId.getApplicationId() + "   " + agenda.size() + "  " + historyRecords.size());
			if (agenda.size() > 1 || historyRecords.size() > 1) {
				throw new RuntimeException("AgendaSize:" + agenda.size() + "   historyRecord.size():" + historyRecords.size() + " appId:" + appId);
			}
			AppStatusHistoryRecord rec = historyRecords.get(0);
			Integer oldSessionId = rec.getSessionId();
			Integer newSessionId = agenda.get(0).getSessionId();
			if (oldSessionId == null || !oldSessionId.equals(newSessionId) ) {
			    System.out.print("appId:" + rec.getApplicationId() + "  oldSessionId:" + oldSessionId + "  newSessionId:" + newSessionId);
			}
			rec.setSessionId(agenda.get(0).getSessionId());
			System.out.println("....Updating   ");
			service.updateRecord(historyRecords.get(0));
		}
		
		

	}

}
class MigratinDBService extends DatabaseService {

	protected MigratinDBService(DataSource ds) {
		super(ds);
		
	}
	public List<ApplicationId> getApplicationIds(int lastMigratedSessionId, Date migrationDate) throws SQLException {
    	return selectRecords("select comission_agenda.application_id from comission_agenda "+  
    			"left join comission_calendar on (comission_calendar.id=comission_agenda.session_id)" +
    			"where application_id in (select application_id from app_status_history where date_assigned <= ? and status_id in (10,11,12,13) group by application_id)" + 
    			"and comission_calendar.id <= ? " +
    			"group by comission_agenda.application_id order by comission_agenda.application_id", ApplicationId.class, migrationDate, lastMigratedSessionId);
    }
	public List<AppStatusHistoryRecord> getAppStatusHistoryRecords(int applicationId, Date migrationDate) throws SQLException {
		return selectRecords("SELECT * from app_status_history where application_id = ? and status_id in (10,11,12,13) and date_assigned <= ?", AppStatusHistoryRecord.class, applicationId, migrationDate);
	}
	public List<CommissionAgendaRecord> getCommissionAgendaRecords(int applicationId, int lastSessionId) throws SQLException {
		return selectRecords("SELECT * from comission_agenda join comission_calendar on (comission_agenda.session_id = comission_calendar.id) where comission_agenda.application_id = ? and comission_calendar.id <= ?", CommissionAgendaRecord.class, applicationId, lastSessionId);
	}
	
	
	
	
	
}
