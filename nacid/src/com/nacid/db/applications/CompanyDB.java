package com.nacid.db.applications;

import com.nacid.db.utils.DatabaseService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CompanyDB extends DatabaseService {

    private static String UPDATE_END_DATE_SQL = "update backoffice.company set date_to=? where id=?";
    
    
    public CompanyDB(DataSource ds) {
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
}
