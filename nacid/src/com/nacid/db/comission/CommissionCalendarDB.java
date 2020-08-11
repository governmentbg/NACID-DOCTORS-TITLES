package com.nacid.db.comission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.nacid.bl.nomenclatures.SessionStatus;
import com.nacid.data.comission.CommissionAgendaRecord;
import com.nacid.data.comission.CommissionCalendarProtocolRecord;
import com.nacid.data.comission.CommissionCalendarRecord;
import com.nacid.data.comission.CommissionParticipationRecord;
import com.nacid.db.utils.DatabaseService;

public class CommissionCalendarDB extends DatabaseService {

    public CommissionCalendarDB(DataSource ds) {
        super(ds);
    }
    public List<CommissionAgendaRecord> getCommissionAgendas(int calendarId) throws SQLException {
    	return selectRecords(CommissionAgendaRecord.class, " session_id = ? ", calendarId);
    }
    public void deleteCommissionAgendaRecords(int calendarId) throws SQLException {
    	deleteRecords(CommissionAgendaRecord.class, " session_id=? ", calendarId );
    }
    public int getLastCommissionIdExaminedApplication(int applicationId) throws SQLException{
    	Connection connection = getConnection();
    	try {
    		PreparedStatement ps = connection.prepareStatement("select max(session_id)" + 
    				" from comission_agenda " +  
    				" left join comission_calendar on (comission_agenda.session_id = comission_calendar.id) " +
    				" where application_id = ? and comission_calendar.session_status_id = ? group by application_id  ");
    		try {
        		ps.setInt(1, applicationId);
        		ps.setInt(2, SessionStatus.SESSION_STATUS_PROVEDENO);
            	ResultSet rs = ps.executeQuery();
            	try {
            		if (rs.next()) {
                		return rs.getInt(1);
                	}
            	} finally {
            		rs.close();
            	}
            		
        	} finally {
        		ps.close();
        	}
    		
    	} finally {
    		connection.close();
    	}
    	
    	return 0;
    	
    	
    	
    }
    
    public List<CommissionCalendarRecord> getCommissionCalendarRecordsByApplication(int applicationId) throws SQLException {
    	String sql = "select comission_calendar.* from comission_calendar join comission_agenda on (comission_agenda.session_id = comission_calendar.id) where application_id = ? order by comission_calendar.id desc";
    	return selectRecords(sql, CommissionCalendarRecord.class, applicationId);
    }
    
    public List<CommissionParticipationRecord> getCommissionParticipationRecords(int calendarId) throws SQLException {
    	return selectRecords(CommissionParticipationRecord.class, " session_id = ?", calendarId);
    }
    public void deleteCommissionParticipationRecords(int calendarId) throws SQLException {
    	deleteRecords(CommissionParticipationRecord.class, " session_id=? ", calendarId );
    }

       
    public CommissionCalendarProtocolRecord loadCalendarProtocol(int calendarId, boolean loadContent) throws SQLException {
        Connection con = getConnection();
        try {
            
            String SQL = loadContent ? 
                    "SELECT  id,protokol_content,protokol_content_type,protokol_file_name FROM comission_calendar where id=? and protokol_content is not null"
                    :
                    "SELECT  id,protokol_content_type,protokol_file_name FROM comission_calendar where id=? and protokol_content is not null";
            
            PreparedStatement p = con.prepareStatement(SQL);
            try {
                
                p.setInt(1, calendarId);
                
                ResultSet rs = p.executeQuery();
                try {
                    if(rs.next()) {
                        return new CommissionCalendarProtocolRecord(
                                rs.getInt("id"),
                                loadContent ? rs.getBinaryStream("protokol_content") : null,
                                rs.getString("protokol_content_type"),
                                rs.getString("protokol_file_name")
                            );
                    }
                    return null;
                } finally {
                    rs.close();
                }
                
            } finally {
                p.close();
            }
        } finally {
            release(con);
        }
    }
    
    public void addCalendarProtocol(CommissionCalendarProtocolRecord rec, int fileSize) throws SQLException {
        Connection con = getConnection();
        try {
            PreparedStatement p = con.prepareStatement("update comission_calendar set protokol_content=?,protokol_content_type=?,protokol_file_name=? where id=?");
            try {
                
                p.setBinaryStream(1, rec.getContent(), fileSize);
                p.setString(2, rec.getContentType());
                p.setString(3, rec.getFileName());
                p.setInt(4, rec.getId());
                
                p.executeUpdate();
                
            } finally {
                p.close();
            }
        } finally {
            release(con);
        }
    }
    public List<CommissionCalendarRecord> getCommissionCalendarRecordsByDate(java.sql.Timestamp date) throws SQLException {
    	if (date == null) {
    		return selectRecords(CommissionCalendarRecord.class, null);
    	}
    	return selectRecords(CommissionCalendarRecord.class, " session_time = ? ", date);
    }
    
}
