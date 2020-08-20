package com.nacid.bl.impl.menu;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.nacid.bl.NacidDataProvider;
/*import com.nacid.bl.external.menu.ExtMenu;
import com.nacid.bl.external.menu.ExtMenuDataProvider;
import com.nacid.bl.external.users.ExtUser;*/
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.menu.Menu;
import com.nacid.bl.menu.MenuDataProvider;
import com.nacid.bl.menu.MenuItem;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.data.menu.MenuRecord;
import com.nacid.db.menu.MenuDB;
import com.nacid.db.utils.StandAloneDataSource;

public class MenuDataProviderImpl implements MenuDataProvider /*, ExtMenuDataProvider*/ {

	private MenuDB db;
	private int webApplication;
	private List<MenuItem> records;
	//private boolean externalUsers;
	/**
	 * 
	 * @param nacidDataProvider
	 * @param externalUsers - ako true - shte vry6ta {@link MenuDataProviderImpl}, koito se otnasq za vy6nite potrebiteli (tablica ext_menu), <br />
	 * ako false - shte vry6ta {@link MenuDataProviderImpl} kloito se otnasq za vytre6nite potrebiteli (tablica menu)
	 */
	public MenuDataProviderImpl(NacidDataProviderImpl nacidDataProvider, int webApplication) {
		this.db = new MenuDB(nacidDataProvider.getDataSource(), webApplication);
		this.webApplication = webApplication;
		initCache();
	}
	private void initCache() {
		try {
			records = db.getMenuRecords(webApplication).stream().map(r -> new MenuItemImpl(r.getId(),r.getUrl(), r.getName(), r.getLongName(), null, r.getActive() == 1, r.getOrdNum())).collect(Collectors.toList());
		} catch (SQLException throwables) {
			throw Utils.logException(throwables);
		}
	}

	//Polzva se v MenuDataProvider
	public Menu constructMenuForUser(User user) {
		return getMenu(user);
	}
	
	//Polzva se v ExtMenuDataProvider
	/*public ExtMenu constructMenuForUser(ExtUser user) {
		return getMenu(user);
	}*/
	private MenuImpl getMenu(User user) {
		try {
			MenuImpl ret = new MenuImpl();
			loadChilds(ret.getMainMenu(), ret, user);
			return ret;
		}
		catch(Exception e) {
			throw Utils.logException(this, e);
		}
	}
	private void loadChilds(MenuItemImpl parent, MenuImpl menu, User user) throws Exception {
		
		List<? extends MenuRecord> childs = db.getChildMenus(parent.getId(), user != null);
		int order = 1;
		for(MenuRecord mr : childs) {
			
			String url = mr.getUrl();
			if (url != null && url.trim().length() != 0 
					&& (url.startsWith("/control") || url.startsWith("control"))) {
				Integer groupId;
				Integer operId;

				String group = null;
				String oper = null;
				String[] parts = url.split("/");
				for (String p : parts) {
					if (p.length() > 0 && !p.equals("control")) {
						if (group == null) {
							group = p;
						} else if (oper == null) {
							oper = p;
						} else {
							break;
						}
					}
				}
				
				if (oper == null || group == null) {
					System.err.println("Wrong url found in DB: " + url);
					continue;
				}
				
				int qIndex = oper.indexOf('?');
				if(qIndex > 0) {
					oper = oper.substring(0, qIndex);
				}
				
				groupId = com.ext.nacid.web.handlers.UserAccessUtils.getGroupId(group);
                operId = com.ext.nacid.web.handlers.UserAccessUtils.getOperationId(oper);
				
				//System.out.println("group: >" + group + "< groupId:>" + groupId + "< operation:>" + oper + "< operId:>" + operId + "<");
				/*if (operId == null) {
					throw new Exception("Unknown operation: " + oper + " for url: " + url);
				}
				
				if (groupId == null) {
					throw new Exception("Unknown group: " + oper + " for url: " + url);
				}*/
				
				if (user != null && !user.hasAccess(groupId, operId, webApplication)) {
					continue;
				}
			}
			MenuItemImpl m = new MenuItemImpl(mr.getId(), mr.getUrl(), 
					mr.getName(), mr.getLongName(), parent, mr.getActive() != 0, order++);
			loadChilds(m, menu, user);
			if(user == null || m.getUrl() != null || m.getChilds().size() != 0) {
				menu.addMenuItem(parent, m);
			}
		}
	}

	@Override
	public int saveMenu(int id, int parentId, String name, String longName, int ordNum, boolean active) {
		try {
			int res = db.saveMenu(id, parentId, ordNum, name, longName, active);
			initCache();
			return res;
		} catch (SQLException e) {
			throw Utils.logException(this, e);
		}
	}

	@Override
	public void deleteMenu(int id) {
		try {
			db.deleteMenu(id);
			initCache();
		} catch (SQLException e) {
			throw Utils.logException(this, e);
		}
	}

	@Override
	public List<MenuItem> getMenuItemsByPartOfUrl(String partOfUrl) {
		return records.stream().filter(r -> r.getUrl() != null && r.getUrl().contains(partOfUrl)).collect(Collectors.toList());
	}

	public static void main(String[] args) {
		NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
		MenuDataProvider menuDataProvider = nacidDataProvider.getMenuDataProvider(NacidDataProvider.APP_NACID_REGPROF_ID);
	    UsersDataProvider usersDataProvider = nacidDataProvider.getUsersDataProvider();
	    User u = usersDataProvider.getUser(1/*, true*/);
	    Menu m = menuDataProvider.constructMenuForUser(u);
	    System.out.println(m.getCurrentMenu().getChilds().isEmpty());
		/*MenuDataProvider menuDataProvider = nacidDataProvider.getMenuDataProvider(MenuDataProvider.APP_NACID);
		UsersDataProvider usersDataProvider = nacidDataProvider.getUsersDataProvider();
		User u = usersDataProvider.getUser(18, false);
		System.out.println(com.nacid.web.handlers.UserAccessUtils.getOperationId("list"));
		System.out.println(u.hasAccess(com.nacid.web.handlers.UserAccessUtils.getGroupId("applications"), com.nacid.web.handlers.UserAccessUtils.getOperationId("view")));
		Menu m = menuDataProvider.constructMenuForUser(u);
		System.out.println(m.getCurrentMenu());
		*/
		//Menu menu = menuDataProvider.constructMenuForUser(usersDataProvider.getUser(1, true));
		//System.out.println(menu.getCurrentMenu().getChilds().size());
	}

	

}
