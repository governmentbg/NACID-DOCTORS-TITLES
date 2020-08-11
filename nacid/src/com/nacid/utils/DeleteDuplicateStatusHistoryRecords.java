package com.nacid.utils;

import com.nacid.data.applications.AppStatusHistoryRecord;
import com.nacid.db.applications.ApplicationsDB;
import com.nacid.db.nomenclatures.NomenclaturesDB;
import com.nacid.db.utils.StandAloneDataSource;

import java.sql.SQLException;
import java.util.List;

/**
 * namerih dublirani zapisi v app_status_history - kogato se e update-val status ot komisiq-kalendar-obrabotka-motivi, sa se zapisvali po 2 zapisa za vsqka promqna !!!
 * @author ggeorgiev
 *
 */
public class DeleteDuplicateStatusHistoryRecords {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
        StandAloneDataSource ds = new StandAloneDataSource("jdbc:postgresql://fly.nacid.bg:5432/NACID/", "postgres", "postgres");
		// TODO Auto-generated method stub
		ApplicationsDB db = new ApplicationsDB(ds, new NomenclaturesDB(ds));
		List<AppStatusHistoryRecord> records = db.selectRecords(AppStatusHistoryRecord.class, " 1 = 1 order by id");
		int problemRecords = 0;
		for (int i = 0; i < records.size() - 1; i++) {
			AppStatusHistoryRecord r1 = records.get(i);
			AppStatusHistoryRecord r2 = records.get(i+1);
			if (
					r1.getApplicationId() == r2.getApplicationId() &&
					r1.getStatusId() == r2.getStatusId() &&
					r1.getDateAssigned().equals(r2.getDateAssigned()) &&
					(r1.getStatLegalReasonId() == null)
			) {
				System.out.println("Have to delete record " + r1.getId());
				db.deleteRecord(AppStatusHistoryRecord.class, r1.getId());
				problemRecords++;
			}
			
		}
		
		System.out.println("ProblemRecords:" + problemRecords);
	}

}
