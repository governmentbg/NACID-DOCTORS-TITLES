package com.nacid.bl.users;

import com.nacid.bl.exceptions.UserAlreadyExistsException;
import com.nacid.bl.external.ExtPerson;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
//import com.nacid.bl.external.users.ExtUser;

public interface UsersDataProvider {
  public static final int ANONYMOUS_USER_ID = -1;
	public User loginUserByIp(String ip);
	/**
	 * 
	 * @param user
	 * @param pass
	 * @param md5 - pokazva dali parolata e md5 hashirana
	 * @return
	 */
	public User loginUserByPass(String user, String pass, boolean md5, int webApplicationId);

    public User loginPortalUser(String username, int webApplicationId, ExtPerson person, boolean backofficeAdministrator);
	public User loginAnonymousUser();
	
	/**
	 * @param startId - na4alno ID
	 * @param rowsCount - broi vyrnati redove
	 * @param status - 0 - ne u4astva vyv filtriraneto
	 * @return
	 */
	public List<? extends User> getUsers(int startId, int rowsCount, int status, int webApplicationId);

    public List<? extends User> getUsers(int startId, int rowsCount, int status, int... webApplicationId);

	/**
	 * User Stats Methods
	 */
	public int startUserSysLogging(int userId, String sessionId, String remoteAddress, String remoteHost, int webApplicationId);
  public UserSysLogOperation addUserSysLogOperation(int userId, String sessionId, String remoteAddress, String remoteHost, String groupName, String operationName, String queryString/*, int webApplicationId*/);
  //public UserSysLogOperation addExtUserSysLogOperation(int userId, String sessionId, String remoteAddress, String remoteHost, String groupName, String operationName, String queryString);
  public void stopUserSysLogging(String sessionId/*, int webApplicationId*/);
  public boolean isUserSysLoggingStarted(String sessionId);
  public User getUser(int userId/*, boolean generateMenu*/);
  public UserUpdater getUserForEdit(int userId);
  /**
   * dobavq/redaktira/ user
   * @param id
   * @param fullname
   * @param shortname
   * @param username
   * @param userpass
   * @param status
   * @param email
   * @param tel
   * @param operationsAccess - Map: key:applicationId, Value: (Map:Key:groupId, Value:operationId)
   * @return
   * @throws UserAlreadyExistsException - ako user-a koito se opita da se dobavi e registriran ve4e
   */
  public int updateUser(int id, String fullname, String shortname, String username, String userpass, int status, String email, String tel,
      Map<Integer, Map<Integer, Set<Integer>>> operationsAccess) throws UserAlreadyExistsException;
  public void updateUserOperations(int id, Map<Integer, Map<Integer, Set<Integer>>> operationsAccess);
  public int addApplicationChangeHistoryRecord(int applicationId, int userId, Date date, String operationName, String groupName);
  
  /* methods added from ExtUsersDataProvider */
  public User getUserByName(String userName);
  public void changeUserStatus(int userId, int status);
  public void changeUserPassword(int userId, String newPassword);

	public int countUserSysLogOperations(Integer userId, Date dateFrom, Date dateTo, Integer webAppId, String groupName, String operationName, String queryString, String sessionId );
	public List<UserSysLogOperationExtended> getUserSysLogOperations(Integer userId, Date dateFrom, Date dateTo, Integer webAppId, String groupName, String operationName, String queryString, String sessionId );
	public UserSysLogOperationExtended getUserSysLogOperationExtended(Integer id);
}
