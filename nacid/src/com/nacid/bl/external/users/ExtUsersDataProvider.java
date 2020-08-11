/*package com.nacid.bl.external.users;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nacid.bl.users.UserSysLogOperation;
import com.nacid.bl.users.UsersDataProvider;

public interface ExtUsersDataProvider extends UsersDataProvider {
	//public static final int ANONYMOUS_USER_ID = -1;
	public ExtUser loginUserByIp(String ip);
	/**
	 * 
	 * @param ExtUser
	 * @param pass
	 * @param md5 - pokazva dali parolata e md5 hashirana
	 * @return
	 */
/*
	public ExtUser loginUserByPass(String ExtUser, String pass, boolean md5);
	public ExtUser loginAnonymousUser();

	/**
	 * @param startId - na4alno ID
	 * @param rowsCount - broi vyrnati redove
	 * @param status - 0 - ne u4astva vyv filtriraneto
	 * @return
	 */
	//public List<? extends ExtUser> getUsers(int startId, int rowsCount, int status);

	/**
	 * ExtUser Stats Methods
	 */
/*
	public int startUserSysLogging(int userId, String sessionId, String remoteAddress, String remoteHost);
	public UserSysLogOperation addUserSysLogOperation(int userId, String sessionId, String remoteAddress, String remoteHost, String groupName, String operationName, String queryString);
	public void stopUserSysLogging(String sessionId);
	public boolean isUserSysLoggingStarted(String sessionId);
	public ExtUser getUser(int ExtUserId, boolean generateMenu);
	public ExtUserUpdater getUserForEdit(int userId);
	/**
	 * dobavq/redaktira/ user
	 * @throws ExtUserAlreadyExistsException - ako user-a koito se opita da se dobavi e registriran ve4e
	 */
/*
	public int updateUser(int id, String fullname, String shortname, String username, String userpass, int status, String email, String tel,
			Map<Integer, Set<Integer>> operationsAccess) throws ExtUserAlreadyExistsException;
    public ExtUser getUserByName(String userName);
    public void changeUserPassword(int userId, String newPassword);
    
    public List<? extends ExtUser> getUsers(int startId, int rowsCount, int status);
    public void changeUserStatus(int userId, int status);
}*/
