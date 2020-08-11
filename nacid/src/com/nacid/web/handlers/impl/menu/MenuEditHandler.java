package com.nacid.web.handlers.impl.menu;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.menu.Menu;
import com.nacid.bl.menu.MenuDataProvider;
import com.nacid.bl.menu.MenuItem;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.MenuUtils;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.model.common.SystemMessageWebModel;

public class MenuEditHandler extends NacidBaseRequestHandler {

	public MenuEditHandler(ServletContext servletContext) {
		super(servletContext);
	}
	


	@Override
	public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
		int id = DataConverter.parseInt(request.getParameter("id"), 0);
		if (id <= 0) {
			throw new UnknownRecordException("Unknown menu ID:" + id);
		}
		MenuDataProvider mDP = getNacidDataProvider().getMenuDataProvider(NacidDataProvider.APP_NACID_ID);
		Menu menu = mDP.constructMenuForUser(null);
		
		MenuItem delMenu = menu.getMenuItem(id);
		
		if((delMenu.getUrl() != null && delMenu.getUrl().trim().length() != 0)
			|| (delMenu.getChilds() != null && delMenu.getChilds().size() != 0)) {
			request.setAttribute(WebKeys.SYSTEM_MESSAGE,
					new SystemMessageWebModel("Това меню не може да бъде изтрито",
							SystemMessageWebModel.MESSAGE_TYPE_ERROR));
		}
		else {
			mDP.deleteMenu(id);
			request.setAttribute(WebKeys.SYSTEM_MESSAGE,
					new SystemMessageWebModel("Менюто беше изтрито успешно",
							SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
		}
		
		handleList(request, response);
	}


	@Override
	public void handleList(HttpServletRequest request, HttpServletResponse response) {
		
		MenuDataProvider mDP = getNacidDataProvider().getMenuDataProvider(NacidDataProvider.APP_NACID_ID);
		Menu menu = mDP.constructMenuForUser(null);
		request.setAttribute(WebKeys.MENU_EDITLIST_WEB_MODEL, MenuUtils.getMenuWebModelFromMenu(menu));
		request.setAttribute(WebKeys.NEXT_SCREEN, "administration_menu_list");
	}
	
	@Override
	public void handleNew(HttpServletRequest request, HttpServletResponse response) {
		
		MenuDataProvider mDP = getNacidDataProvider().getMenuDataProvider(NacidDataProvider.APP_NACID_ID);
		Menu menu = mDP.constructMenuForUser(null);
		request.setAttribute(WebKeys.MENU_EDITLIST_WEB_MODEL, MenuUtils.getMenuWebModelFromMenu(menu));
		request.setAttribute(WebKeys.NEXT_SCREEN, "administration_menu_edit");
	}
	
	@Override
	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
		int id = DataConverter.parseInt(request.getParameter("id"), 0);
		if (id <= 0) {
			throw new UnknownRecordException("Unknown menu ID:" + id);
		}
		MenuDataProvider mDP = getNacidDataProvider().getMenuDataProvider(NacidDataProvider.APP_NACID_ID);
		Menu menu = mDP.constructMenuForUser(null);
		MenuItem current = menu.getMenuItem(id);
		request.setAttribute(WebKeys.MENU_EDITEDIT_WEB_MODEL, MenuUtils.menuItem2FlatMenuWebModel(current));
		request.setAttribute(WebKeys.MENU_EDITLIST_WEB_MODEL, MenuUtils.getMenuWebModelFromMenu(menu));
		request.setAttribute(WebKeys.NEXT_SCREEN, "administration_menu_edit");
	}
	
	@Override
	public void handleSave(HttpServletRequest request, HttpServletResponse response) {
		if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
			new HomePageHandler(request.getSession().getServletContext())
					.processRequest(request, response);
			return;
		}
		
		int id = DataConverter.parseInt(request.getParameter("id"), 0);
		int parentId = DataConverter.parseInt(request.getParameter("parentId"), 0);
		String name = request.getParameter("name");
		String longName = request.getParameter("longName");
		int ordNum = DataConverter.parseInt(request.getParameter("ordNum"), 0);
		boolean active = DataConverter.parseBoolean(request.getParameter("active"));
		
		MenuDataProvider mDP = getNacidDataProvider().getMenuDataProvider(NacidDataProvider.APP_NACID_ID);
		Menu menu = mDP.constructMenuForUser(null);
		
		if (id != 0 && menu.getMenuItem(id) == null) {
			throw new UnknownRecordException("Unknown menu ID:" + id);
		}
		
		if (false) {
			/*SystemMessageWebModel webmodel = new SystemMessageWebModel(
					"грешка");
			request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
			request.setAttribute(WebKeys.COMISSION_MEMBER_WEB_MODEL, new ComissionMemberWebModel(
					id, fname, sname,
					lname, degree, institution, division,
					title, egn, homeCity, homeAddress,
					homePcode, phone, email, gsm,
					iban, bic, request.getParameter("date_from"), request.getParameter("date_to")));
			*/
		} else {
		
			id = mDP.saveMenu(id, parentId, name, longName, 
					ordNum, active);
		
			request.setAttribute(WebKeys.SYSTEM_MESSAGE,
					new SystemMessageWebModel("Данните бяха въведени в базата",
							SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
			menu = mDP.constructMenuForUser(null);
		}
		request.setAttribute(WebKeys.MENU_EDITEDIT_WEB_MODEL, MenuUtils.menuItem2FlatMenuWebModel(menu.getMenuItem(id)));
		request.setAttribute(WebKeys.MENU_EDITLIST_WEB_MODEL, MenuUtils.getMenuWebModelFromMenu(menu));
		request.setAttribute(WebKeys.NEXT_SCREEN, "administration_menu_edit");
	}
}
