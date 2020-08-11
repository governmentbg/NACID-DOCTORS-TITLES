package com.nacid.bl.menu;

import com.nacid.data.menu.MenuRecord;

public interface Menu {

	public static int MAIN_MENU_ID = MenuRecord.MAIN_PARENT_ID;
	
	public MenuItem getMenuItem(int id);

	public void setCurrentMenu(MenuItem currentMenu);

	public MenuItem getCurrentMenu();

	public MenuItem getMenuItemByUrl(String url);

}
