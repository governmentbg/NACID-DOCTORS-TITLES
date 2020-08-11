package com.nacid.web.taglib.menu;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.menu.MenuWebModel;

public class MenuEditEditTag extends SimpleTagSupport {

	private MenuWebModel webmodel;
	
	@Override
	public void doTag() throws JspException, IOException {
		webmodel = (MenuWebModel) getJspContext().getAttribute(
				WebKeys.MENU_EDITEDIT_WEB_MODEL, PageContext.REQUEST_SCOPE);

		if(webmodel != null) {
			getJspContext().setAttribute("id", webmodel.getId());
			getJspContext().setAttribute("checked", webmodel.isActive() ? "checked=\"checked\"" : "");
			getJspContext().setAttribute("name", webmodel.getName());
			getJspContext().setAttribute("longName", webmodel.getLongName());
			getJspContext().setAttribute("ordNum", webmodel.getOrdNum());
			getJspContext().setAttribute("showDelete",	webmodel.isShowDelete());
		}
		else {
			getJspContext().setAttribute("id", "0");
			getJspContext().setAttribute("checked", "checked=\"checked\"");
			getJspContext().setAttribute("name", "");
			getJspContext().setAttribute("longName", "");
			getJspContext().setAttribute("ordNum", "");
			getJspContext().setAttribute("showDelete",	false);
		}
		getJspBody().invoke(null);
	}
	
	public int getMenuId() {
		if(webmodel == null) {
			return 0;
		}
		return webmodel.getId();
	}
}
