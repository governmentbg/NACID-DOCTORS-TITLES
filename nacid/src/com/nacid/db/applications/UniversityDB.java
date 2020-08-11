package com.nacid.db.applications;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;

import com.nacid.bl.applications.University;
import com.nacid.bl.applications.UniversityDataProvider;
import com.nacid.data.applications.UniversityFacultyRecord;
import com.nacid.data.applications.UniversityRecord;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.lang.StringUtils;


public class UniversityDB extends DatabaseService {

    private static String UPDATE_END_DATE_SQL = "update university set date_to=? where id=?";

    public UniversityDB(DataSource ds) {
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


    public List<UniversityRecord> getUniversityRecords(int diplomaId) throws SQLException{
        String sql = "SELECT * from university" + 
        " LEFT JOIN diploma_issuer on (university.id = university_id) "+ 
        " where diploma_id = ? " + 
        " order by ord_num ";
        return selectRecords(sql, UniversityRecord.class, diplomaId);
    }
    public List<UniversityFacultyRecord> getUniversityFaculties(int diplomaId) throws SQLException{
        List<UniversityFacultyRecord> ret = selectRecords("select uf.*\n" +
                "from diploma_issuer di \n" +
                "left join university_faculty uf on uf.id = di.faculty_id\n" +
                "where diploma_id = ?\n" +
                "order by ord_num", UniversityFacultyRecord.class, diplomaId);
        return ret.stream().map(r -> r.getId() == 0 ? null : r).collect(Collectors.toList());
    }

    public List<UniversityRecord> getUniversityRecords(Integer countryId, int nameType, boolean startsWith, String partOfName) throws SQLException {
        //String sql = nameType == UniversityDataProvider.NAME_TYPE_BG ? " bg_name ilike ?" : " org_name ilike ?";
    	List<Object> params = new ArrayList<Object>();
    	String sql;
    	
        switch (nameType) {
		case 0:
			sql = " ( bg_name ilike ? or org_name ilike ? ) ";
			params.add((startsWith ? "" : "%") + partOfName + "%");
			params.add((startsWith ? "" : "%") + partOfName + "%");
			break;
		case UniversityDataProvider.NAME_TYPE_BG :
			sql = " bg_name ilike ?";
			params.add((startsWith ? "" : "%") + partOfName + "%");
			break;
		case UniversityDataProvider.NAME_TYPE_ORIGINAL :
			sql = " org_name ilike ?";
			params.add((startsWith ? "" : "%") + partOfName + "%");
			break;
		default:
			throw new IllegalArgumentException("nameType must be one of the defined in UniversityDataProvider NAME_TYPE_BG or NAME_TYPE_ORIGINAL or 0!!!");
		}
        
        sql += " AND (date_from <= ? OR date_from is null) "
                + " AND (date_to >= ? OR date_to is null) " ;
        Date d = new Date(System.currentTimeMillis());
        params.add(d);
        params.add(d);
        
        if (countryId != null) {
    		sql += " AND country_id = ?";
    		params.add(countryId);
    	}
        //System.out.println(sql);
        //System.out.println(params);
        return selectRecords(UniversityRecord.class, sql, params.toArray());
    }
    public List<UniversityRecord> getUniversityRecords(int countryId, Date dateTo) throws SQLException {
        List<Object> objects = new ArrayList<Object>();
        String str = " 1 = 1 ";
        if (countryId > 0) {
            str += " AND country_id = ?";
            objects.add(countryId);
        }
        if (dateTo != null) {
            str += " AND (date_from <= ? OR date_from is null) ";
            str += " AND (date_to >= ? OR date_to is null) ";
            objects.add(dateTo);
            objects.add(dateTo);
        }
        str += " ORDER BY bg_name";
        if (objects.size() > 0) {
            return selectRecords(UniversityRecord.class , str, objects.toArray());
        } else {
            return selectRecords(UniversityRecord.class, null);
        }
    }

    public List<UniversityFacultyRecord> getUniversityFaculties(int universityId, String partOfName, Date dateTo) throws SQLException {
        List<Object> objects = new ArrayList<>();
        List<String> sql = new ArrayList<>();
        sql.add("1 = 1");
        sql.add("university_id = ?");
        objects.add(universityId);
        if (dateTo != null) {
            sql.add(" (date_from <= ? OR date_from is null) ");
            sql.add(" (date_to >= ? OR date_to is null) ");
            objects.add(dateTo);
            objects.add(dateTo);
        }
        if (!StringUtils.isEmpty(partOfName)) {
            sql.add(" name ilike ?");
            objects.add("%" + partOfName + "%");
        }
        return selectRecords(UniversityFacultyRecord.class, sql.stream().collect(Collectors.joining(" AND ")) + " ORDER BY name", objects.toArray());
    }


    /*public List<UniversityRecord> getUniversities(List<Integer> universityIds) throws SQLException  {
	  List<String> questions = new ArrayList<String>();
	  for (Integer i:universityIds) {
	    questions.add("?");
	  }
	  String whereClause = StringUtils.join(questions, ",");
	  return selectRecords(UniversityRecord.class, " id in (" + whereClause + ")", universityIds.toArray());
	}
*/
	public static void main(String[] args) throws SQLException{
	  UniversityDB db = new UniversityDB(new StandAloneDataSource());
        System.out.println(db.getUniversityRecords(5));
//	  System.out.println(db.getUniversityRecords(229, 0,true, "технически"));//getUniversities(Arrays.asList(1,2,3));
//	  System.out.println("End....");
	}
    
}
