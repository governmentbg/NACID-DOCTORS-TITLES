package com.nacid.db.menu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

//import com.nacid.data.external.menu.ExtMenuRecord;
import com.nacid.data.menu.MenuRecord;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.StandAloneDataSource;

public class MenuDB extends DatabaseService {

    private int webApplication;
	//private boolean externalUsers;
	public MenuDB(DataSource ds, int webApplication /*, boolean externalUsers*/) {
		super(ds);
		this.webApplication = webApplication;
		//this.externalUsers = externalUsers;
	}
	
	/**
	 * Gets the child menus.
	 * 
	 * @param parentId the parent id. If id < 0 returns all main menus
	 * @return the child menus
	 * @throws SQLException
	 */
	public List<? extends MenuRecord> getChildMenus(int parentId, boolean skipInactive) throws SQLException {
		String sql = "";
		List<Object> params = new ArrayList<Object>();
		if (parentId < 0) {
			sql += " parent_id is null ";
		} else {
			sql += " parent_id = ? ";
			params.add(parentId);
		}
		if (skipInactive) {
			sql += " and active != 0";
		}
		sql += " and web_app = ? ";
		params.add(webApplication);
		sql += " order by ord_num ";
		return selectRecords(getMenuRecord().getClass(), sql, params.size() == 0 ? null : params.toArray());
		
	}
	
	public int saveMenu(Integer id, Integer parentId, Integer ordNum, String name, String longName, 
			boolean active) throws SQLException {
		List<? extends MenuRecord> siblings2 = getChildMenus(parentId, false);
		List<MenuRecord> siblings = new ArrayList<MenuRecord>();
		for (MenuRecord s:siblings2) {
			siblings.add(s);
		}
		
		String url = null;
		MenuRecord oldRec = selectRecord(new MenuRecord(), id);
		if(oldRec != null) {
		    url = oldRec.getUrl();
		}
		
		MenuRecord current = new MenuRecord(id, parentId, ordNum, url, name, longName, active, webApplication);
		
		if(id == 0) {
			current = insertRecord(current);
		}
		siblings.remove(current);
		int newOrder = ordNum - 1;
		if(newOrder < 0)
			newOrder = 0;
		
		if(newOrder < siblings.size()) {
			siblings.add(newOrder, current);
		}
		else {
			siblings.add(current);
		}
		
		for(int i = 0; i < siblings.size(); i ++) {
		    MenuRecord r = siblings.get(i);
		    r.setOrdNum((i + 1) * 100);
			updateRecord(r);
		}
		return current.getId();
	}
	
	public void deleteMenu(int id) throws SQLException {
		deleteRecord(getMenuRecord().getClass(), id);
	}
	
	private static MenuRecord getMenuRecord() {
		return /*externalUsers ? new ExtMenuRecord() :*/ new MenuRecord();
	}
	
	public static void main(String[] args) throws SQLException {
		MenuDB db =new MenuDB(new StandAloneDataSource(), 1/*, false*/);
		db.getChildMenus(-1, true);
	}
}
