package com.nacid.db.comission;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.nacid.data.applications.ApplicationExpertRecord;
import com.nacid.data.comission.ComissionMemberRecord;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.SQLUtils;

public class ComissionMemberDB extends DatabaseService {

	private static String UPDATE_END_DATE_SQL = "update comission_member set date_to=? where id=?";
	
	public ComissionMemberDB(DataSource ds) {
		super(ds);
	}

	public void setEndDateToToday(int id) throws SQLException {
		Connection con = getConnection();
		try {
			PreparedStatement p = con.prepareStatement(UPDATE_END_DATE_SQL);
			try {
				
				p.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
				p.setInt(2, id);
				
				p.executeUpdate();
			} finally {
				p.close();
			}
		} finally {
			release(con);
		}
	}
	
	public List<ComissionMemberRecord> selectLastMembersWithEGNLike(String egnPart) throws SQLException {
		if(egnPart == null || egnPart.equals("")) {
			return new ArrayList<ComissionMemberRecord>();
		}
		egnPart = egnPart + "%";
		return selectRecords(ComissionMemberRecord.class, "id in (SELECT max(id) from comission_member where egn like ? group by egn)", egnPart);
	}
	public List<ComissionMemberRecord> getCommissionMembers(List<Integer> ids) throws SQLException {
		List<Object> objects = new ArrayList<Object>();
		String str = " 1 = 1 ";
		if (ids != null && ids.size() > 0) {
			str += " AND  id in (" +  SQLUtils.columnsToParameterList(StringUtils.join(ids, ",")) + ") ";
			for (Integer i:ids) {
	    		objects.add(i);
	    	}
		}
		return selectRecords(ComissionMemberRecord.class, str, objects.size() == 0 ? null : objects.toArray());
	}
	
	public List<ApplicationExpertRecord> getApplicationsByExpert(int expertId) throws SQLException {
        return selectRecords(ApplicationExpertRecord.class, "expert_id = ?", expertId);
    }
	public List<ComissionMemberRecord> getCommissionMembersByUserId(int userId) throws SQLException {
        return selectRecords(ComissionMemberRecord.class, "user_id = ? and (date_to is null or date_to > ?)", userId, new Date(System.currentTimeMillis()));
    }
}
