package com.nacid.bl.impl.menu;

import java.util.HashMap;

//import com.nacid.bl.external.menu.ExtMenu;
//import com.nacid.bl.external.menu.ExtMenuItem;
import com.nacid.bl.menu.Menu;
import com.nacid.bl.menu.MenuItem;

public class MenuImpl implements Menu/*, ExtMenu*/ {

	private HashMap<Integer, MenuItemImpl> itemsHolder = new HashMap<Integer, MenuItemImpl>();
	private MenuItemImpl mainMenu;
	private MenuItemImpl currentMenu;
	
	MenuImpl() {
		mainMenu = new MenuItemImpl(MAIN_MENU_ID, null, "ГЛАВНО МЕНЮ", null, null, true, 0);
		mainMenu.click();
		itemsHolder.put(mainMenu.getId(), mainMenu);
	}
	
	void addMenuItem(MenuItemImpl parent, MenuItemImpl item) {
		parent.addChild(item);
		itemsHolder.put(item.getId(), item);
	}
	
	MenuItemImpl getMainMenu() {
		return mainMenu;
	}
	
	@Override
	public MenuItemImpl getMenuItem(int id) {
		return itemsHolder.get(id);
	}
	
	//Tozi method e samo v Menu interface-a
	public void setCurrentMenu(MenuItem currentMenu) {
		setCurrentMenu((MenuItemImpl)currentMenu);
	}
	
	//Tozi method e samo v ExtMenu interface-a
	/*public void setCurrentMenu(ExtMenuItem currentMenu) {
		setCurrentMenu((MenuItemImpl)currentMenu);
		
	}*/
	
	private void setCurrentMenu(MenuItemImpl currentMenu) {
		if(this.currentMenu != null) {
			this.currentMenu.setSelected(false);
		}
		this.currentMenu = (MenuItemImpl)currentMenu;
		this.currentMenu.setSelected(true);
	}
	
	public MenuItemImpl getCurrentMenu() {
		return currentMenu;
	}
	
	@Override
	public MenuItemImpl getMenuItemByUrl(String url) {
		for(Integer id : itemsHolder.keySet()) {
			MenuItemImpl mi = itemsHolder.get(id);
			if(mi.getUrl() != null && mi.getUrl().indexOf(url) != -1) {
				return mi;
			}
		}
		return null;
	}

	
}
