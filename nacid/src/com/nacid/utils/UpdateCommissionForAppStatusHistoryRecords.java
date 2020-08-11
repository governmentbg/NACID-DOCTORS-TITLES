package com.nacid.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.nacid.data.applications.AppStatusHistoryRecord;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.utils.csv.Csv2Record;

public class UpdateCommissionForAppStatusHistoryRecords {

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws SQLException, IOException, ParseException {
		StandAloneDataSource ds = new StandAloneDataSource("jdbc:postgresql://fly.nacid.bg:5432/NACID/", "postgres", "postgres");
		MigratinDBService service = new MigratinDBService(ds);
		List<AppStatusHistoryRecord> oldRecords = service.selectRecords(AppStatusHistoryRecord.class, null);
		//System.out.println(oldRecords);

		Csv2Record<AppStatusHistoryRecord> historyRecords = new Csv2Record<AppStatusHistoryRecord>(AppStatusHistoryRecord.class, "D:/ggeorgiev/gravis projects/NACID/src/com/nacid/utils/appstatushistory-import.properties");
		historyRecords.setSeparator(';');
		historyRecords.setSkipLines(1);
		List<AppStatusHistoryRecord> updateableRecords = historyRecords.generateRecords("C:/Users/ggeorgiev/Desktop/statuses_final.csv");
		Map<Integer, AppStatusHistoryRecord> newAppStatusHistoryRecords = new HashMap<Integer, AppStatusHistoryRecord>();
		for (AppStatusHistoryRecord r:updateableRecords) {
			newAppStatusHistoryRecords.put(r.getId(), r);
		}
		int updatedRecords = 0;
		for (AppStatusHistoryRecord r:oldRecords) {
			AppStatusHistoryRecord newRecord = newAppStatusHistoryRecords.get(r.getId());
			if (newRecord == null) {
				//System.out.println("Continue....");
				continue;
			}
			updatedRecords++;
			if (r.getApplicationId() != newRecord.getApplicationId() || r.getDateAssigned().getTime() != newRecord.getDateAssigned().getTime()) {
				throw new RuntimeException("ProblemRecord:" + r.getId() + "");
			}
			System.out.println("Id:" + r.getId() + "  oldSessionId:" + r.getSessionId() + "  newSessionId:" + newRecord.getSessionId());
			r.setSessionId(newRecord.getSessionId());
			//service.updateRecord(r);
		}
		System.out.println("Updated records:" + updatedRecords);
	}
	
}
