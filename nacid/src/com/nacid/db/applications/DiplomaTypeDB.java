package com.nacid.db.applications;

import com.nacid.data.applications.DiplomaIssuerRecord;
import com.nacid.data.applications.DiplomaTypeIssuerRecord;
import com.nacid.data.applications.DiplomaTypeRecord;
import com.nacid.data.applications.DiplomaTypeRecordForList;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.SQLUtils;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.lang.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DiplomaTypeDB extends DatabaseService {

	private static String UPDATE_END_DATE_SQL = "update diploma_type set date_to=? where id=?";
    private static final String SELECT_DIPLOMA_TYPES_FOR_LIST = "select dt.id, dt.title, el.name as edu_level_name, dt.edu_level_id, dt.date_from, dt.date_to,\n" +
            "array(select uni.bg_name ||' - '||uc.name \n" +
            "from university uni \n" +
            "join nomenclatures.country uc on uc.id = uni.country_id\n" +
            "where uni.id in (select university_id from diploma_type_issuer where diploma_type_id = dt.id)\n" +
            "order by uni.id\n" +
            ") as uni_names, \n" +
            "array(select uc.name \n" +
            "from university uni \n" +
            "join nomenclatures.country uc on uc.id = uni.country_id\n" +
            "where uni.id in (select university_id from diploma_type_issuer where diploma_type_id = dt.id)\n" +
            "order by uni.id\n" +
            ") as uni_countries\n" +
            "FROM diploma_type dt\n" +
            "left join nomenclatures.edu_level el on el.id = dt.edu_level_id\n" +
            "WHERE 1 = 1 \n";
    public List<DiplomaTypeRecordForList> getAllDiplomaTypes(Integer type) throws SQLException {
		String sql = SELECT_DIPLOMA_TYPES_FOR_LIST;
		List<Object> objects = new ArrayList<>();
		if (type != null) {
			sql += " AND dt.type = ?\n";
			objects.add(type);
		}
		sql += " order by dt.id desc";
    	return selectRecords(sql, DiplomaTypeRecordForList.class, objects.size() == 0 ? null : objects.toArray());
    }
	public DiplomaTypeDB(DataSource ds) {
		super(ds);
	}
	public List<DiplomaTypeIssuerRecord> getDiplomaTypeIssuerRecords(Collection<Integer> universityIds) throws SQLException {
		String str = null;
		List<Object> objects = new ArrayList<Object>();
		if (universityIds != null && universityIds.size() > 0) {
			str = " university_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(universityIds, ",")) + ") ";
			for (Integer i:universityIds) {
	    		objects.add(i);
	    	}
			
		} else {
			str = " 1 = 1 ";
		}
		return selectRecords(DiplomaTypeIssuerRecord.class, str , objects.size() == 0 ? null : objects.toArray());
	}
	public List<DiplomaTypeRecord> getDiplomaTypeRecords(Collection<Integer> universityIds) throws SQLException {
		String str = "select diploma_type.* from diploma_type " +
				" left join diploma_type_issuer on (diploma_type_issuer.diploma_type_id = diploma_type.id) " +
				" WHERE " ;
		
		List<Object> objects = new ArrayList<Object>();
		if (universityIds != null && universityIds.size() > 0) {
			str += "university_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(universityIds, ",")) + ") ";
			for (Integer i:universityIds) {
	    		objects.add(i);
	    	}
			
		} else {
			str += " 1 = 1 ";
		}
		return selectRecords(str, DiplomaTypeRecord.class, objects.size() == 0 ? null : objects.toArray());
		
	}
	public List<DiplomaTypeIssuerRecord> getDiplomaTypeIssuerRecords(int diplomaTypeId) throws SQLException {
		return selectRecords(DiplomaTypeIssuerRecord.class, " diploma_type_id = ? order by ord_num", diplomaTypeId);
	}
	public List<DiplomaIssuerRecord> getAllDiplomaIssuersByDiplomaTypeId(int diplomaTypeId) throws SQLException {
    	return selectRecords("select * from diploma_issuer where diploma_id in (select id from training_course where diploma_type_id = ?)", DiplomaIssuerRecord.class, diplomaTypeId);
	}




	public void deleteDiplomaTypeIssuerRecords(int diplomaTypeId) throws SQLException {
		deleteRecords(DiplomaTypeIssuerRecord.class, " diploma_type_id = ? ", diplomaTypeId);
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
	public static void main(String[] args) throws SQLException {
		DiplomaTypeDB db = new DiplomaTypeDB(new StandAloneDataSource());
		System.out.println(db.getDiplomaTypeRecords(Arrays.asList(18)).size());
	}
}
