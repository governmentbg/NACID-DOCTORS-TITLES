package com.nacid.web.taglib.menu;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.menu.MenuWebModel;

public class MenuEditListTag extends SimpleTagSupport {

	private String pathPrefix;
	private boolean radioButtons;
	private int currentMenuId;
	
	public void doTag() throws JspException, IOException {
		MenuWebModel webmodel = (MenuWebModel) getJspContext().getAttribute(
				WebKeys.MENU_EDITLIST_WEB_MODEL, PageContext.REQUEST_SCOPE);

		if(radioButtons) {
			MenuEditEditTag editTag = (MenuEditEditTag)findAncestorWithClass(this, MenuEditEditTag.class);
			currentMenuId = editTag.getMenuId();
		}
		
		try {
			pathPrefix = (String) ((PageContext)getJspContext()).getSession()
					.getServletContext().getInitParameter("pathPrefix");
		}
		catch(Exception e) {
			e.printStackTrace();
			pathPrefix = "";
		}
		if (webmodel != null) {
			StringBuffer sb = new StringBuffer();
			paintChilds(sb, webmodel, true);
			getJspContext().getOut().println(sb.toString());
		}
	}
	
	private void paintChilds(StringBuffer sb, MenuWebModel parentMenu, boolean isMain) throws IOException {
		
		List<MenuWebModel> childs = parentMenu.getChilds();
		if(childs != null && childs.size() != 0) {
			sb.append("<ul>");
			if(isMain && radioButtons) {
				sb.append("<li class=\""+getMenuClass(parentMenu, true)+"\">");
				sb.append(getStringForItem(parentMenu));	
				sb.append("</li>");
			}
			for(MenuWebModel m : childs) {
				sb.append("<li class=\""+getMenuClass(m, isMain)+"\">");
				sb.append(getStringForItem(m));	
				paintChilds(sb, m, false);
				sb.append("</li>");
			}
			sb.append("</ul>");
		}
	}
	
	private String getMenuClass(MenuWebModel m, boolean isMain) {
		String pclass;
		if(m.getChilds() == null || m.getChilds().size() == 0) {
			pclass = "nochilds";
		}
		else {
			pclass = "childs";
		}
		return (isMain ? "main_" : "second_") + pclass;
	}
	
	private String getStringForItem(MenuWebModel m) {
		if (!radioButtons) {
			String url = pathPrefix + "/control/administration_menu/edit?id=" + m.getId();
			String classs = "";

			String title = "";
			if (m.getLongName() != null) {
				title = "title=\"" + m.getLongName() + "\"";
			}

			return MessageFormat.format("<a href=\"{0}\" {3} {1} >{2}{4}</a>", url, title, m.getName(), classs, m.isActive() ? "" : " (inactive)");
		}
		else {
			String selected = "";
			if(m.getChilds() != null) {
				for(MenuWebModel c : m.getChilds()) {
					if(c.getId() == currentMenuId) {
						selected = "checked=\"checked\"";
						break;
					}
				}
			}
			
			if(m.getId() == -1 && currentMenuId == 0) {
				//m is glavnoto menu
				//currentMenuId - nov zapis
				selected = "checked=\"checked\"";
			}
			//System.out.println(m.getId() + " " +currentMenuId + " " + selected);
			
			String ret = "";
			if(m.getId() == currentMenuId) {
				ret += m.getName() + (m.isActive() ? "" : " (inactive)");
			}
			else {
				ret += "<input id=\"radio_"+m.getId()+"\" type=\"radio\" value=\""+m.getId()+"\" name=\"parentId\" "+selected+"/>";
				ret += "<a href=\"#\" onclick=\"$('radio_"+m.getId()+"').checked = true; return false;\">";
				ret += m.getName() + (m.isActive() ? "" : " (inactive)");
				ret+="</a>";
			}
			
			return ret;
		}
	}

	public void setRadioButtons(boolean radioButtons) {
		this.radioButtons = radioButtons;
	}
	
}
