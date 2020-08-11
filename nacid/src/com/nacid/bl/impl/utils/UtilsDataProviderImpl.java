package com.nacid.bl.impl.utils;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.utils.CommonVariable;
import com.nacid.bl.utils.UniversityDetail;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.utils.CommonVariableRecord;
import com.nacid.data.utils.UniversityDetailRecord;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.db.utils.UtilsDB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UtilsDataProviderImpl implements UtilsDataProvider {
	//private NacidDataProviderImpl nacidDataProvider = null;
	private UtilsDB db;
	private String rowsCount = null;
	public UtilsDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
	    //this.nacidDataProvider = nacidDataProvider;
	    this.db = new UtilsDB(nacidDataProvider.getDataSource());
	  }
	public Map<String, String> getCommonVariablesAsMap() {
		try {
			List<CommonVariableRecord> records = db.selectRecords(CommonVariableRecord.class, " 1 = 1 ");
			Map<String, String> result = new HashMap<String, String>();
			for (CommonVariableRecord r:records) {
				result.put(r.getVariableName(), r.getVariableValue());
			}
			return result;
		} catch (SQLException e) {
			throw Utils.logException(e);
			
		}
	}

    public List<UniversityDetail> getUniversityDetails() {
        try {
            List<UniversityDetailRecord> universityDetailRecords = db.selectRecords(UniversityDetailRecord.class, "1 = 1 order by university_name");
            return universityDetailRecords.size() == 0 ? null : universityDetailRecords.stream().map(Function.identity()).collect(Collectors.toList());
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public UniversityDetail getUniversityDetail(int id) {
        try {
            return db.selectRecord(UniversityDetailRecord.class, "id = ?", id);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public int saveUniversityDetail(int id, String universityName, String letterHeader, String salutation) {
        try {
            UniversityDetailRecord rec = new UniversityDetailRecord(id, universityName, letterHeader, salutation);
            if (rec.getId() == 0) {
                rec = db.insertRecord(rec);
            } else {
                db.updateRecord(rec);
            }
            return rec.getId();
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
	
	public String getCommonVariableValue(String commonVariableName) {
		try {
			//if commonVariableName == rowsCount, then reads cached rowsCount value. If it's not cached yet, it caches it after reading...
		    boolean isRowsCount = CommonVariable.COMMON_VARIABLE_ROWS_COUNT.equals(commonVariableName);
		    if (isRowsCount && rowsCount != null) {
			    return rowsCount;
			}
		    List<CommonVariableRecord> records = db.getCommonVariableRecords(commonVariableName);
			if (records.size() == 0) {
				return null;
			}
			if (isRowsCount) {
			    rowsCount = records.get(0).getVariableValue();
			}
			return records.get(0).getVariableValue();
		} catch (SQLException e) {
			throw Utils.logException(e);
			
		}
	}
	public CommonVariable getCommonVariable(int id) {
		try {
			CommonVariableRecord record = db.selectRecord(new CommonVariableRecord(), id);
			return record == null ? null : new CommonVariableImpl(record);
		} catch (SQLException e) {
			throw Utils.logException(e);
			
		}
	}
	public int saveCommonVariable(int id, String variableName, String variableValue, String description ) {
		try {
			if (CommonVariable.COMMON_VARIABLE_ROWS_COUNT.equals(variableName)) {
			    rowsCount = variableValue;//updates rowsCount cached value!;    
			}
		    CommonVariableRecord record = new CommonVariableRecord(id, variableName, variableValue, description);
			if (id == 0) {
				record = db.insertRecord(record);
			} else {
				db.updateRecord(record);
			}
			return record.getId();
			
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
		
		
	}
	public List<CommonVariable> getCommonVariables() {
		try {
			List<CommonVariableRecord> records = db.selectRecords(CommonVariableRecord.class, " 1 = 1 ");
			if (records.size() == 0) {
				return null;
			}
			List<CommonVariable> result = new ArrayList<CommonVariable>();
			for (CommonVariableRecord r:records) {
				result.add(new CommonVariableImpl(r));
			}
			return result;
		} catch (SQLException e) {
			throw Utils.logException(e);
			
		}
	}
	
	
	public static void main(String[] args) {
		NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
		UtilsDataProvider utilsDataProvider = nacidDataProvider.getUtilsDataProvider();
		System.out.println(utilsDataProvider.getCommonVariablesAsMap());
	}
	

}
