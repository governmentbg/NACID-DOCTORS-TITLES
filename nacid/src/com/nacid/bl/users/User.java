package com.nacid.bl.users;

import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.menu.Menu;

public interface User {
	public static final int USER_STATUS_INACTIVE = 0;
	public static final int USER_STATUS_ACTIVE = 1;
	public static final int ANONYMOUS_USER_ID = UsersDataProvider.ANONYMOUS_USER_ID;
	public static final int ADMIN_USER_ID = 1;
	public int getUserId();
	public String getFullName();
	public String getShortName();
	public String getUserName();
	/**
	 * @return md5 hashiranata parola
	 */
	public String getUserPass();
	public int getStatus();
	public String getEmail();
	public String getTelephone();
	/**
	 * 
	 * @param groupId - kogato za groupId se podade null - hasAccess vry6ta true, tyi kato vsi4ki Handlers koito nqmat definirani groupId sa dostypni za vsi4ki!
	 * @param actionId - kogato za actionId se podade null, vry6ta true
	 * @return
	 */
	public boolean hasAccess(Integer groupId, Integer actionId, Integer webApplicationId);
	public Menu getMenu(int webApplication);
	/**
	 * dobavq v bazata koga daden user se e lognal
	 * @param sessionId
	 * @param remoteAddress
	 * @param remoteHosts
	 * @return
	 */
	//public int startUserSysLogging(String sessionId, String remoteAddress, String remoteHost);
	/**
	 * updateva koga daden user se e logoffnal
	 */
	//public void stopUserSysLogging();
	/**
	 * logva dadenata operaciq v sistemata
	 * @param sessionId - ID na sesiqta
	 * @param remoteAddress
	 * @param remoteHost
	 * @param groupName - ime na grupata (tova moje bi e dobre da se promeni na ime na operaciqta???)
	 * @param operationName - ime na operaciqta
	 * @param queryString
	 * @return
	 */
	//public int addUserSysLogOperation(String sessionId, String remoteAddress, String remoteHost, String groupName, String operationName, String queryString);
}
