package com.nacid.db.users;



import com.nacid.bl.users.User;
import com.nacid.data.users.*;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.SQLUtils;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.lang.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*import com.nacid.data.external.users.ExtUserAddressRecord;
import com.nacid.data.external.users.ExtUserGroupMembershipRecord;
import com.nacid.data.external.users.ExtUserRecord;*/
//import com.nacid.data.external.users.ExtUserSysLogRecord;
//import com.nacid.data.external.users.ExtUserSysLogOperationRecord;


public class UsersDB extends DatabaseService {
	//private boolean externalUsers;
	public UsersDB(DataSource ds/*, boolean externalUsers*/) {
		super(ds);
		//this.externalUsers = externalUsers;
	}
	public UserAddressRecord getUserAddressRecord(String address, Integer active) throws SQLException {
		String sql = " ip_address  = ? and addr_type = ? ";
		if (active != null) {
			sql += " AND ACTIVE = ?";
		}
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(address);
		parameters.add(UserAddressRecord.ADDRESS_TYPE_INDIVIDUAL);
		if (active != null) {
			parameters.add(active);
		}
		List<? extends UserAddressRecord> records = selectRecords(getUserAddressRecord().getClass(), sql, parameters.toArray());
		if (records.size() == 0) {
			return null;
		}
		return records.get(0);
	}

	public List<? extends UserAddressRecord> getUserAddressRecords(int groupType, Integer active) throws SQLException {

		String sql = " addr_type = ? ";
		if (active != null) {
			sql += " AND ACTIVE = ?";
		}

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(groupType);
		if (active != null) {
			parameters.add(active);
		}
		return selectRecords(getUserAddressRecord().getClass(), sql, parameters.toArray());
	}

	public UserRecord loginUser(String userName, String pass) throws Exception {
		String sql = " username = ? AND userpass = ? AND status = ? ";

		List<Object> parameters = new ArrayList<Object>();
		parameters.add(userName);
		parameters.add(pass);
		parameters.add(User.USER_STATUS_ACTIVE);
		List<? extends UserRecord> records = selectRecords(getUserRecord().getClass(), sql, parameters.toArray());
		if (records.size() == 0) {
			return null;
		}
		return records.get(0);
	}

	public List<? extends UserRecord> getUsers(int startRow, int rowsCount, int status, List<Integer> webApplicationIds) throws Exception {
		String sql = " 1 = 1 ";
		List<Object> parameters = new ArrayList<Object>();
		if (status != 0) {
			sql += " AND status = ?";
			parameters.add(status);
		}
        if (webApplicationIds != null && webApplicationIds.size() > 0) {
            sql += " AND id in (select user_id from users_group_membership where web_application in ("  + SQLUtils.columnsToParameterList(StringUtils.join(webApplicationIds, ", ")) + " ) )";
            parameters.addAll(webApplicationIds);
        }
		if (rowsCount > 0) {
			sql += " LIMIT ? ";
			parameters.add(rowsCount);
		}
		if (startRow != 0) {
			sql += " OFFSET ? ";
			parameters.add(startRow);
		}
        sql += " ORDER BY username";
		if (parameters.size() > 0) {
			return selectRecords(getUserRecord().getClass(), sql, parameters.toArray());
		}
		return selectRecords(getUserRecord().getClass(), null);
	}

	public UserRecord getUserByName(String username) throws SQLException {
		List<? extends UserRecord> result = selectRecords(getUserRecord().getClass(), " username = ? ", username);
		return result.size() == 0 ? null : result.get(0);
	}
	public void deleteUsersGroupMembershipRecords(int userId, Integer webApplicationId) throws SQLException {
	    List<Object> parameters = new ArrayList<Object>();
	    String sql = "user_id = ?";
	    parameters.add(userId);
	    if (webApplicationId != null) {
	        parameters.add(webApplicationId);
	        sql+= "AND web_application = ? ";
	    }
	    
		deleteRecords(getUserGroupMembershipRecord().getClass(), sql, parameters.toArray());
	}

	public List<? extends UserGroupMembershipRecord> getUserGroupMembershipRecords(int userId, int webApplicationId) throws SQLException {
	    List<Object> parameters = new ArrayList<Object>();
	    parameters.add(userId);
	    parameters.add(webApplicationId);
	    return selectRecords(getUserGroupMembershipRecord().getClass(), " USER_ID = ? AND web_application = ? ", parameters.toArray());
	}
	
	public List<? extends UserGroupMembershipRecord> getAllUserGroupMembershipRecords(int userId) throws SQLException {
	    return selectRecords(getUserGroupMembershipRecord().getClass(), " user_id = ? " , userId);
	}

	public static final UserRecord getUserRecord() {
		return new UserRecord();
	}
	
	public static final UserGroupMembershipRecord getUserGroupMembershipRecord() {
		return new UserGroupMembershipRecord();
	}
	
	public static final UserAddressRecord getUserAddressRecord() {
		return new UserAddressRecord();
	}

	public static final UserSysLogOperationRecord getUserSysLogOperationRecord(/*int webApplicationId*/) {
		return new UserSysLogOperationRecord();
	}
	
	/*public static final ExtUserSysLogOperationRecord getExtUserSysLogOperationRecord() {
	    return new ExtUserSysLogOperationRecord();
	}
	*/
	public static final UserSysLogRecord getUserSysLogRecord(/*int webApplicationId/*boolean externalUsers*/) {
		return /*externalUsers ? new ExtUserSysLogRecord() :*/ new UserSysLogRecord();
	}
	public static void main(String[] args) throws SQLException {
		UsersDB usersdb = new UsersDB(new StandAloneDataSource()/*, false*/);
		System.out.println(usersdb.getUserAddressRecord("127.0.0.1", 1));
	}

}
