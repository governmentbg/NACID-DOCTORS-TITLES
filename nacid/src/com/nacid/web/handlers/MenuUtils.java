package com.nacid.web.handlers;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.nacid.bl.menu.Menu;
import com.nacid.bl.menu.MenuItem;
import com.nacid.bl.users.User;
import com.nacid.web.model.menu.MenuWebModel;

public class MenuUtils {

	public static MenuWebModel getMenuWebModel(HttpServletRequest request, User user) {
	    Menu menu = user.getMenu((Integer)request.getSession().getServletContext().getAttribute("webApplicationId"));
		MenuItem main = menu.getMenuItem(Menu.MAIN_MENU_ID);
		
		return menuItem2MenuWebModel(main);
	}
	
	public static MenuWebModel getMenuWebModelFromMenu(Menu menu) {
		MenuItem main = menu.getMenuItem(Menu.MAIN_MENU_ID);
		return menuItem2MenuWebModel(main);
	}
	
	private static MenuWebModel menuItem2MenuWebModel(MenuItem menuItem) {
		
		ArrayList<MenuWebModel> childs = null;
		if(menuItem.getChilds() != null) {
			childs = new ArrayList<MenuWebModel>();
			for(MenuItem mi : menuItem.getChilds()) {
				childs.add(menuItem2MenuWebModel(mi));
			}
		}
		return new MenuWebModel(menuItem.getId(), menuItem.getUrl(), 
				menuItem.getName(), menuItem.getLongName(), 
				childs, menuItem.isColapsed(), menuItem.isSelected(), menuItem.isActive(), menuItem.getOrdNum());
		
	}
	
	public static MenuWebModel menuItem2FlatMenuWebModel(MenuItem menuItem) {
		
		MenuWebModel ret = new MenuWebModel(menuItem.getId(), menuItem.getUrl(), 
				menuItem.getName(), menuItem.getLongName(), 
				null, 
				menuItem.isColapsed(), menuItem.isSelected(), menuItem.isActive(), menuItem.getOrdNum()); 
		ret.setShowDelete(!((menuItem.getChilds() != null && menuItem.getChilds().size() > 0)
						|| (menuItem.getUrl()!= null && menuItem.getUrl().trim().length() > 0)));
		return ret;
	}
	
	public static void processMenuClick(HttpServletRequest request, User user) {
		String menuId = (String) request.getParameter("menu");
		Menu menu = user.getMenu((Integer)request.getSession().getServletContext().getAttribute("webApplicationId"));
		if(menuId != null) {
			int id = Integer.parseInt(menuId);
			MenuItem mi = menu.getMenuItem(id);
			menu.setCurrentMenu(mi);
			if(mi != null) {
				mi.click();
			}
		}
		else {
			MenuItem mi = menu.getMenuItemByUrl(request.getPathInfo());
			if(mi != null) {
				menu.setCurrentMenu(mi);
			}
		}
	}
}
