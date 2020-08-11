package com.nacid.bl.impl.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.external.users.ExtUserGroupMembershipForEdit;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.menu.MenuImpl;
import com.nacid.bl.menu.Menu;
import com.nacid.bl.menu.MenuDataProvider;
import com.nacid.bl.users.UserGroupMembershipForEdit;
import com.nacid.bl.users.UserUpdater;
import com.nacid.bl.users.UsersGroupMembership;
import com.nacid.data.users.UserGroupMembershipRecord;
import com.nacid.data.users.UserRecord;
import com.nacid.db.users.UsersDB;


/***
 * 
 * @author ggeorgiev
 * v momenta e napraveno taka 4e vseki lognat potrebitel si ima otdelen user obekt!!!!!
 * t.e. dori i da ima 2ma lognati potrebiteli s edno i sy6to ime/parola/ v pametta(sesiqta) ima 2 User obekta - za vseki lognat potrebitel po edin!!!!
 * ako nqkoi den se promeni sistemata i na vsi4ki koito se identificira s edno ime/parola/ mu se podava edin i sy6ti obekt, shte ima problemi
 * sys pazeneto na statistikite (koga sa se lognali potrebitelite i kakvi operacii sa izpylnqvali!!!!) 
 */
public class UserImpl implements UserUpdater {
	private UserRecord userRecord;
	private UsersDB usersDB;
	private UsersGroupMembership userGroupsMembership;
	//private MenuImpl menu;
	private NacidDataProvider nacidDataProvider;
	private volatile ExtPerson person = null;
	private volatile ComissionMember expert = null;
	private Map<Integer, Menu> userMenus = new HashMap<Integer, Menu>();
	public UserImpl(NacidDataProvider ndataProv, UsersDB db, UserRecord userRecord/*, boolean addMenu, boolean externalUser*/) {
		this.usersDB = db;
		this.userRecord = userRecord;
		try {
			List<? extends UserGroupMembershipRecord> records = usersDB.getAllUserGroupMembershipRecords(getUserId()); //TODO: possible problem
			userGroupsMembership = new UserGroupsMembershipImpl(records);

		} catch (Exception e) {
			throw Utils.logException(e);
		}
		/*if (addMenu) {
			this.menu = (MenuImpl) (externalUser ? ndataProv.getMenuDataProvider(MenuDataProvider.APP_EXT_NACID).constructMenuForUser(this) : 
			    ndataProv.getMenuDataProvider(MenuDataProvider.APP_NACID).constructMenuForUser(this));  
		}*/
		nacidDataProvider = ndataProv;
	}


	public Menu getMenu(int webApplication) {
		if (userMenus.get(webApplication) == null) {
		    synchronized (this) {
                if (userMenus.get(webApplication) == null) {
                    Menu m = nacidDataProvider.getMenuDataProvider(webApplication).constructMenuForUser(this);
                    userMenus.put(webApplication, m);
                }
            }
		}
	    return userMenus.get(webApplication);
	}
	public String getEmail() {
		return userRecord.getEmail();
	}
	public String getFullName() {
		return userRecord.getFullName();
	}

	public String getUserName() {
		return userRecord.getUserName();
	}

	public String getUserPass() {
		return userRecord.getUserPass();
	}

	public String getShortName() {
		return userRecord.getShortName();
	}

	public int getStatus() {
		return userRecord.getStatus();
	}

	public String getTelephone() {
		return userRecord.getTelephone();
	}

	public int getUserId() {
		return userRecord.getUserId();
	}

	public boolean hasAccess(Integer groupId, Integer operationId, Integer webApplicationId) {
		if (groupId == null || operationId == null) {
			return true;
		}
		return userGroupsMembership.hasAccess(groupId, operationId, webApplicationId);
	}

	public String toString() {
		return userRecord.toString();
	}


	/**
	 * the methods bellow are used in UserUpdater interface!!!
	 */
	public List<ExtUserGroupMembershipForEdit> getUserGroupMemberships(int webApplicationId) {
		try {
			List<? extends UserGroupMembershipRecord> records = usersDB.getUserGroupMembershipRecords(getUserId(), webApplicationId); 
			if (records.size() == 0) {
				return null;
			}
			List<ExtUserGroupMembershipForEdit> result = new ArrayList<ExtUserGroupMembershipForEdit>();
			for (UserGroupMembershipRecord r:records) {
				result.add(new UserGroupMembershipForEditImpl(r));
			}
			return result;
		} catch (Exception e) {
			throw Utils.logException(e);
		}
	}
}
