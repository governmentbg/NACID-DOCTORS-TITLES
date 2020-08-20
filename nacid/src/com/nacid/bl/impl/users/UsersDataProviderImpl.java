package com.nacid.bl.impl.users;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.exceptions.UserAlreadyExistsException;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.external.ExtPersonDataProvider;
import com.nacid.bl.external.users.ExtUserAlreadyExistsException;
import com.nacid.bl.external.users.ExtUsersGroupMembership;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.users.UserGroupMembership;
import com.nacid.bl.users.UserSysLogOperation;
import com.nacid.bl.users.UserSysLogOperationExtended;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.data.users.*;
import com.nacid.db.syslog.SyslogDb;
import com.nacid.db.users.UsersDB;
import com.nacid.db.utils.StandAloneDataSource;
import org.apache.commons.lang.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

//import com.nacid.bl.external.users.ExtUsersDataProvider;

public class UsersDataProviderImpl implements UsersDataProvider/*, ExtUsersDataProvider*/ {
	private NacidDataProviderImpl nacidDataProvider = null;
	private UsersDB db;
	private SyslogDb syslogDb;
	/**
	 * shte opredelq dali stava vypros za users ili external users...
	 */
	//private int webApplication;
	private Map<String, Integer> userSysLogRecordIds = new HashMap<String, Integer>();

	public UsersDataProviderImpl(NacidDataProviderImpl nacidDataProvider/*, int webApplication*/) {
		this.nacidDataProvider = nacidDataProvider;
		//this.webApplication = webApplication;
		this.db = new UsersDB(nacidDataProvider.getDataSource()/*, webApplication*/);
		syslogDb = new SyslogDb(nacidDataProvider.getDataSource());
	}

	public UserImpl getUser(int userId/*, boolean generateMenu*/) {
		return getUserByID(userId/*, generateMenu*/);
	}
	
	public void changeUserPassword(int userId, String newPassword) {
	    try {
            UserRecord record = db.selectRecord(UsersDB.getUserRecord(/*externalUsers*/), userId);
            if (record == null) {
                return;
            }
            record.setUserPass(Utils.getMD5HashString(newPassword));
            db.updateRecord(record);
        } catch (Exception e) {
            throw Utils.logException(this, e);
        }
	}

	private UserImpl getUserByID(int userID/*, boolean generateMenu*/) {
		try {
			UserRecord record = db.selectRecord(UsersDB.getUserRecord(/*externalUsers*/), userID);
			if (record == null) {
				return null;
			}
			return new UserImpl(nacidDataProvider, db, record/*, generateMenu, externalUsers*/);
		} catch (Exception e) {
			throw Utils.logException(this, e);
		}
	}

	public UserImpl loginUserByIp(String ip) {
		try {
			//Samo vytre6nite users mogat da se logvat po IP!
			/*if (externalUsers) {
				return null;
			}*/
			/**
			 * pyrvo se proverqva za individualno IP, sled tova ako nqma se
			 * vyrti cikyl mejdu vsi4ki grupovi syvpadeniq kato se po4va ot naj
			 * dylgoto kym naj kratkoto po dyljina
			 */
			UserAddressRecord addr = db.getUserAddressRecord(ip, UserAddressRecord.ADDRESS_RECORD_ACTIVE);
			if (addr != null) {
				return getUserByID(addr.getUserId()/*, true*/);
			}

			List<? extends UserAddressRecord> userAddresses = db.getUserAddressRecords(UserAddressRecord.ADDRESS_TYPE_GROUP,
					UserAddressRecord.ADDRESS_RECORD_ACTIVE);
			if (userAddresses == null || userAddresses.size() == 0) {
				return null;
			}
			Collections.sort(userAddresses, new Comparator<UserAddressRecord>() {
				public int compare(UserAddressRecord o1, UserAddressRecord o2) {
					return o2.getIpAddress().length() - o1.getIpAddress().length();
				}
			});
			for (UserAddressRecord r : userAddresses) {
				if (ip.indexOf(r.getIpAddress().trim()) == 0) {
					addr = r;
					return getUserByID(addr.getUserId()/*, true*/);
				}
			}
			return null;
		} catch (Exception e) {
			throw Utils.logException(this, e);
		}
	}

	public UserImpl loginUserByPass(String user, String pass, boolean md5, int webApplicationId) {
		try {
			UserRecord u = db.loginUser(user, md5 ? pass : makeMD5Token(pass));
			if (u == null) {
				return null;
			}

			if (!hasUserAccessToWebApp(u.getUserId(), webApplicationId, db)) {
			    return null;
			}
			
			return new UserImpl(nacidDataProvider, db, u/*, true/*, externalUsers*/);
		} catch (Exception e) {
			throw Utils.logException(this, e);
		}
	}
    public UserImpl loginPortalUser(String username, int webApplicationId, ExtPerson person, boolean backofficeAdministrator) {
        try {
            UserRecord u = db.getUserByName(username);
            if (u != null && u.getStatus() == 0) {
                return null;//Potrebitelq sy6testvuva, no ne e aktiven!!!
            }
            if (u == null) {
                u = registerDefaultUser(username, person);
            } else {
                u = new UserRecord(u.getUserId(), person.getFullName(), null, username, "", 1, person.getEmail(), null);
                db.updateRecord(u);
                ExtPerson old = nacidDataProvider.getExtPersonDataProvider().getExtPersonByUserId(u.getUserId());

                String fname = StringUtils.isEmpty(person.getFName()) && old != null ? old.getFName() : person.getFName();
                String sname = StringUtils.isEmpty(person.getSName()) && old != null ? old.getSName() : person.getSName();
                String lname = StringUtils.isEmpty(person.getLName()) && old != null ? old.getLName() : person.getLName();
                String civilId = StringUtils.isEmpty(person.getCivilId()) && old != null ? old.getCivilId() : person.getCivilId();
                Integer civilIdType = person.getCivilIdTypeId() == null && old != null ? old.getCivilIdTypeId() : person.getCivilIdTypeId();
                Integer birthCountryId = person.getBirthCountryId() == null && old != null ? old.getBirthCountryId() : person.getBirthCountryId();
                String birthCity = StringUtils.isEmpty(person.getBirthCity()) && old != null ? old.getBirthCity() : person.getBirthCity();
                Date birthDate = person.getBirthDate() == null && old != null ? old.getBirthDate() : person.getBirthDate();
                Integer citizenshipId = person.getCitizenshipId() == null && old != null ? old.getCitizenshipId() : person.getCitizenshipId();
                String email = StringUtils.isEmpty(person.getEmail()) && old != null ? old.getEmail() : person.getEmail();
                String hashCode = person.getHashCode();
                nacidDataProvider.getExtPersonDataProvider().saveExtPerson(old == null ? 0 : old.getId(), fname, sname, lname, civilId, civilIdType, birthCountryId, birthCity, birthDate, citizenshipId, email, hashCode, u.getUserId());

            }


            //ako user-a e backoffice user i nqma prava za backoffice prilojeniqta, to mu se dobavqt default-ni prava!
            if (backofficeAdministrator && NacidDataProvider.BACKOFFICE_APP_IDS.contains(webApplicationId) && !hasUserAccessToWebApp(u.getUserId(), webApplicationId, db)) {
                addDefaultAdministrativeAccessToUser(u.getUserId());
                u = db.getUserByName(u.getUserName());
            }

            if (!hasUserAccessToWebApp(u.getUserId(), webApplicationId, db)) {
                return null;
            }

            return new UserImpl(nacidDataProvider, db, u);
        } catch (SQLException e) {
            throw Utils.logException(this, e);
        } catch (UserAlreadyExistsException e) {
            throw Utils.logException(this, e);
        }

    }

    public void addDefaultAdministrativeAccessToUser(int userId) {

        Map<Integer, Set<Integer>> operationsAccess = new HashMap<Integer, Set<Integer>>();
        Set<Integer> fullAccessOperations = new HashSet<Integer>();
        Map<Integer, Map<Integer, Set<Integer>>> ops = new HashMap<Integer, Map<Integer, Set<Integer>>>();
        fullAccessOperations.add(UserGroupMembership.FULL_ACCESS_OPERATION_ID);
        operationsAccess = new HashMap<Integer, Set<Integer>>();
        operationsAccess.put(UserGroupMembership.APPLICATIONS_GROUP_ID, fullAccessOperations);
        operationsAccess.put(UserGroupMembership.INQUIRES_GROUP_ID, fullAccessOperations);
        operationsAccess.put(UserGroupMembership.NOMENCLATURES_GROUP_ID, fullAccessOperations);
        ops.put(NacidDataProvider.APP_NACID_ID, operationsAccess);
        ops.put(NacidDataProvider.APP_NACID_REGPROF_ID, operationsAccess);

        updateUserOperations(userId, ops);
    }

    private UserRecord registerDefaultUser(String username, ExtPerson person) throws UserAlreadyExistsException, SQLException {
        Map<Integer, Set<Integer>> operationsAccess = new HashMap<Integer, Set<Integer>>();
        Set<Integer> fullAccessOperations = new HashSet<Integer>();
        fullAccessOperations.add(UserGroupMembership.FULL_ACCESS_OPERATION_ID);
        operationsAccess.put(ExtUsersGroupMembership.APPLICANTS_GROUP, fullAccessOperations);
        Map<Integer, Map<Integer, Set<Integer>>> ops = new HashMap<Integer, Map<Integer, Set<Integer>>>();
        ops.put(NacidDataProvider.APP_NACID_EXT_ID, operationsAccess);
        ops.put(NacidDataProvider.APP_NACID_REGPROF_EXT_ID, operationsAccess);






        int res = updateUser(0, person.getFullName(), null, username, "", 1, person.getEmail(), null, ops);


        ExtPersonDataProvider epDP = nacidDataProvider.getExtPersonDataProvider();
        epDP.saveExtPerson(0, person.getFName(), person.getSName(), person.getLName(), person.getCivilId(), person.getCivilIdTypeId(), person.getBirthCountryId(),
                person.getBirthCity(), person.getBirthDate(), person.getCitizenshipId(), person.getEmail(), null, res);
        return db.selectRecord(new UserRecord(), res);
    }

	public UserImpl loginAnonymousUser() {
		return getUserByID(ANONYMOUS_USER_ID/*, true*/);
	}

	public int startUserSysLogging(int userId, String sessionId, String remoteAddress, String remoteHost, int webApplicationId) {
		// Ako userSysLogRecordIds.get(sessionId) != null, togava ve4e e  startirano
		// logvane za dadenata sessionId! i nqma nujda da se startira novo!
		if (userSysLogRecordIds.get(sessionId) != null) {
			return userSysLogRecordIds.get(sessionId);
		}
		try {
			UserSysLogRecord record = UsersDB.getUserSysLogRecord(/*webApplicationId/*externalUsers*/);
			record.setRecordId(0);
			record.setUserId(userId);
			record.setSessionId(sessionId);
			record.setRemoteAddress(remoteAddress);
			record.setRemoteHost(remoteHost);
			record.setTimeLogin(new Timestamp(new Date().getTime()));
			record.setTimeLogout(null);
			record.setWebApplicationId(webApplicationId);
			record = db.insertRecord(record);
			userSysLogRecordIds.put(sessionId, record.getRecordId());

			return record.getRecordId();
		} catch (Exception e) {
			throw Utils.logException(e);
		}
	}

	public UserSysLogOperation addUserSysLogOperation(int userId, String sessionId, String remoteAddress, String remoteHost, String groupName, String operationName,
			String queryString) {
		Integer userSysLogRecordId = getUserSysLogRecordId(userId, sessionId, remoteAddress, remoteHost);
		if (userSysLogRecordId == null) {
			throw new RuntimeException("user is not added to user sys log table so you cannot add an operation to him!!!!!!!");
		}
		try {

			UserSysLogOperationRecord record = UsersDB.getUserSysLogOperationRecord(/*webApplicationId*/);/*externalUsers*/
			record.setRecordId(0);
			record.setUserSysLogRecordId(userSysLogRecordId);
			record.setGroupName(groupName);
			record.setOperationName(operationName);
			record.setQueryString(queryString);
			record.setDateCreated(new Timestamp(new Date().getTime()));
			
			record = db.insertRecord(record);
			return record;
		} catch (Exception e) {
			throw Utils.logException(e);
		}
	}
	
	private Integer getUserSysLogRecordId(int userId, String sessionId, String remoteAddress, String remoteHost) {
		return userSysLogRecordIds.get(sessionId);
	}

	public void stopUserSysLogging(String sessionId/*, int webApplicationId*/) {
		// Ako ne e startirano logvane, togava nqma i kakvo da se spira!
		if (userSysLogRecordIds.get(sessionId) == null) {
			return;
		}
		try {
			UserSysLogRecord record = db.selectRecord(UsersDB.getUserSysLogRecord(/*webApplicationId/*externalUsers*/), userSysLogRecordIds.get(sessionId));
			record.setTimeLogout(new Timestamp(new Date().getTime()));
			db.updateRecord(record);
			userSysLogRecordIds.remove(sessionId);
		} catch (Exception e) {
			throw Utils.logException(e);
		}
	}

	public boolean isUserSysLoggingStarted(String sessiondId) {
		return userSysLogRecordIds.get(sessiondId) != null;
	}

	


	public UserImpl getUserForEdit(int userId) {
		try {
			UserRecord rec = db.selectRecord(UsersDB.getUserRecord(/*externalUsers*/), userId);
			if (rec != null) {
				return new UserImpl(nacidDataProvider, db, rec/*, false/*, externalUsers*/);
			}
			return null;
		} catch (Exception e) {
			throw Utils.logException(e);
		}
	}
	
	@Override
	public UserImpl getUserByName(String userName) {
	    try {
            UserRecord rec = db.getUserByName(userName);
            if(rec == null) {
                return null;
            }
            return new UserImpl(nacidDataProvider, db, rec/*, false/*, externalUsers*/);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
	}
	
	@Override
	public void changeUserStatus(int userId, int status) {
	    try {
	        UserRecord rec = db.selectRecord(UsersDB.getUserRecord(/*externalUsers*/), userId);
	        if(rec != null) {
	            rec.setStatus(status);
	            db.updateRecord(rec);
	        }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
	}

	public synchronized int updateUser(int id, String fullname, String shortname, String username, String userpass, int status, String email, String tel,
			Map<Integer, Map<Integer, Set<Integer>>> operationsAccess) throws UserAlreadyExistsException {
		try {
			UserRecord old = db.getUserByName(username);
			if (old != null && old.getUserId() != id) {
				throw new ExtUserAlreadyExistsException("User with username " + username + " is already registered!!!");
			}
			old = UsersDB.getUserRecord(/*externalUsers*/);
			old = db.selectRecord(old, id);
			
			UserRecord record = UsersDB.getUserRecord(/*externalUsers*/);
			record.setUserId(id);
			record.setFullName(fullname);
			record.setShortName(shortname);
			record.setUserName(username);
			record.setUserPass(userpass == null ? old.getUserPass() : makeMD5Token(userpass));
			record.setStatus(status);
			record.setEmail(email);
			record.setTelephone(tel);
			if (id == 0) {
				record = db.insertRecord(record);
			} else {
				db.updateRecord(record);
			}
            updateUserOperations(record.getUserId(), operationsAccess);

            return record.getUserId();
		} catch (SQLException e) {
			throw Utils.logException(e);
		} catch (NoSuchAlgorithmException e) {
			throw Utils.logException(e);
		}
	}

    public synchronized void updateUserOperations(int id, Map<Integer, Map<Integer, Set<Integer>>> operationsAccess) {
        try {

            if (operationsAccess == null) {
                db.deleteUsersGroupMembershipRecords(id, null);
            } else {
                for (Integer webAppId:operationsAccess.keySet()) {
                    db.deleteUsersGroupMembershipRecords(id, webAppId);
                    Map<Integer, Set<Integer>> operationsByWebApp = operationsAccess.get(webAppId);
                    if (operationsAccess != null) {
                        for (Integer groupId : operationsByWebApp.keySet()) {
                            String operations = StringUtils.join(operationsByWebApp.get(groupId), ";");
                            UserGroupMembershipRecord ugm = UsersDB.getUserGroupMembershipRecord(/*externalUsers*/);
                            ugm.setRecordId(0);
                            ugm.setUserId(id);
                            ugm.setGroupId(groupId);
                            ugm.setOperations(operations);
                            ugm.setWebApplication(webAppId);
                            db.insertRecord(ugm);
                        }
                    }
                }

            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

	public static String makeMD5Token(String value) throws NoSuchAlgorithmException {
		return Utils.getMD5HashString(value);
	}
	
	//Tozi method se polzva samo v UsersDataProvider, nqma go v ExtUsersDataProvider!!!
	public List<UserImpl> getUsers(int startRow, int rowsCount, int status, int webApplicationId) {
		try {
			List<? extends UserRecord> records = db.getUsers(startRow, rowsCount, status, Arrays.asList(webApplicationId));
			if (records == null || records.size() == 0) {
				return null;
			}
			List<UserImpl> result = new ArrayList<UserImpl>();
			for (UserRecord rec : records) {
                result.add(new UserImpl(nacidDataProvider, db, rec/*, false/*, externalUsers*/));
			}
			return result;
		} catch (Exception e) {
			throw Utils.logException(e);
		}
	}

    public List<UserImpl> getUsers(int startRow, int rowsCount, int status, int... webApplicationId) {
        try {
            List<Integer> apps = new ArrayList<Integer>();
            if (webApplicationId != null && webApplicationId.length > 0) {
                for (Integer app : webApplicationId) {
                    apps.add(app);
                }
            }
            List<? extends UserRecord> records = db.getUsers(startRow, rowsCount, status, apps);
            if (records == null || records.size() == 0) {
                return null;
            }
            List<UserImpl> result = new ArrayList<UserImpl>();
            for (UserRecord rec : records) {
                result.add(new UserImpl(nacidDataProvider, db, rec/*, false/*, externalUsers*/));
            }
            return result;
        } catch (Exception e) {
            throw Utils.logException(e);
        }
    }

	public int addApplicationChangeHistoryRecord(int applicationId, int userId, Date date, String operationName, String groupName) {
	    try {
	        ApplicationChangesHistoryRecord record = new ApplicationChangesHistoryRecord(null, applicationId, userId, new Timestamp(date.getTime()), operationName, groupName);
	        record = db.insertRecord(record);
            return record.getId();
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
	    
	}

    public static boolean hasUserAccessToWebApp(Integer userId, Integer webApplicationId, UsersDB db) throws SQLException {
	    if (webApplicationId == -1 || webApplicationId == 0) return true;
        List<? extends UserGroupMembershipRecord> records = db.getUserGroupMembershipRecords(userId, webApplicationId);
        Set<Integer> webApplicationsAccess = new HashSet<Integer>();
        for (UserGroupMembershipRecord rec : records) {
            webApplicationsAccess.add(rec.getWebApplication());
        }
        if (!webApplicationsAccess.contains(webApplicationId)) {
            return false;
        }
        else return true;
	}
	public int countUserSysLogOperations(Integer userId, Date dateFrom, Date dateTo, Integer webAppId, String groupName, String operationName, String queryString, String sessionId ) {
		try {
			return syslogDb.countUserSysLogOperationRecords(userId, Utils.getTimestamp(dateFrom), Utils.getTimestamp(dateTo), webAppId, groupName, operationName, queryString, sessionId);
		} catch (SQLException throwables) {
			throw Utils.logException(throwables);
		}
	}
	public List<UserSysLogOperationExtended> getUserSysLogOperations(Integer userId, Date dateFrom, Date dateTo, Integer webAppId, String groupName, String operationName, String queryString, String sessionId ) {
		try {
			return syslogDb.getUserSysLogOperationRecordsExtended(userId, Utils.getTimestamp(dateFrom), Utils.getTimestamp(dateTo), webAppId, groupName, operationName, queryString, sessionId).stream().collect(Collectors.toList());
		} catch (SQLException throwables) {
			throw Utils.logException(throwables);
		}
	}

	@Override
	public UserSysLogOperationExtended getUserSysLogOperationExtended(Integer id) {
		try {
			return syslogDb.getUserSysLogOperationRecordExtended(id);
		} catch (SQLException throwables) {
			throw Utils.logException(throwables);
		}
	}

	public static void main(String[] args) throws ExtUserAlreadyExistsException {
		NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
		UsersDataProvider usersDataProvider = nacidDataProvider.getUsersDataProvider();
		System.out.println(usersDataProvider.loginAnonymousUser());
		
		
		//ExtUsersDataProvider extUsersDataProvider = nacidDataProvider.getExtUsersDataProvider();
		//extUsersDataProvider.updateUser(0, "Тест тестов Тестов", "тест", "tester", "tester", 1, "", "", null);
		//System.out.println(extUsersDataProvider.loginUserByPass("test", "admin", false));
		
		
		
	}

}
