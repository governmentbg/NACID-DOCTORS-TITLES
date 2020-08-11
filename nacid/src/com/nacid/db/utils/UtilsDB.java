package com.nacid.db.utils;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.nacid.data.utils.CommonVariableRecord;


public class UtilsDB extends DatabaseService {

    public UtilsDB(DataSource ds) {
        super(ds);
    }
    public List<CommonVariableRecord> getCommonVariableRecords(String commonVariableName) throws SQLException{
    	return selectRecords(CommonVariableRecord.class, " var_name = ?", commonVariableName);
    }
}
