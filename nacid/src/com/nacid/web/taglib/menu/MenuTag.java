package com.nacid.web.taglib.menu;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.menu.MenuWebModel;

public class MenuTag extends SimpleTagSupport {

	private String pathPrefix;
	
	public void doTag() throws JspException, IOException {
		MenuWebModel webmodel = (MenuWebModel) getJspContext().getAttribute(
				WebKeys.MENU_WEB_MODEL, PageContext.REQUEST_SCOPE);

		
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
			List<MenuWebModel> menus = webmodel.getChilds();
			for(MenuWebModel m : menus) {
				paintMainMenu(sb, m);
			}
			getJspContext().getOut().println(sb.toString());
		}
	}
	
	private void paintMainMenu(StringBuffer sb, MenuWebModel m) throws IOException {
		
		sb.append(MessageFormat.format("<p id=\"menu{2}\" class=\"{1}\">{0}</p>",
				getStringForItem(m), getMenuClass(m), m.getId()));
		paintChilds(sb, m, true);	
		sb.append("<div class=\"clr10\"><!--  --></div>");
	}
	
	private void paintChilds(StringBuffer sb, MenuWebModel parentMenu, boolean isFirstLevel) throws IOException {
		
		List<MenuWebModel> childs = parentMenu.getChilds();
		if(childs != null && childs.size() != 0) {
			sb.append("<ul id=\"childs"+parentMenu.getId()+"\"");
			if(parentMenu.isColapsed()) {
				sb.append("style=\"display: none;\"");
			}
			if(!isFirstLevel) {
                sb.append("class=\"menu_second_level\"");
            }
            
			sb.append(">");
			
			for(MenuWebModel m : childs) {
				sb.append("<li id=\"menu"+m.getId()+"\" class=\""+getMenuClass(m)+"\">");
				sb.append(getStringForItem(m));	
				paintChilds(sb, m, false);
				sb.append("</li>");
			}
			sb.append("</ul>");
		}
	}
	
	private String getMenuClass(MenuWebModel m) {
		String pclass;
		if(m.getChilds() == null || m.getChilds().size() == 0) {
			pclass = "nochilds";
		}
		else {
			if(m.isColapsed()) {
				pclass = "colapsed";
			}
			else {
				pclass = "expanded";
			}
		}
		return pclass;
	}
	
	private String getStringForItem(MenuWebModel m) {
		String url = "";
		String onclick = "";
		String classs = "";
		if (m.getUrl() != null) {
			url = pathPrefix + m.getUrl();
			onclick="onclick=\"return clickMenu('"+pathPrefix+"','"+m.getId()+"', true);\"";
			if(m.isSelected() ) {
				classs = "class=\"selected\"";
			}
		}
		else {
			onclick="onclick=\"return clickMenu('"+pathPrefix+"','"+m.getId()+"', false);\"";
		}
		String title = "";
		if(m.getLongName() != null) {
			title = "title=\""+m.getLongName()+"\"";
		}
		
		return MessageFormat.format("<a href=\"{0}\" {4} {1} {3}>{2}</a>",
				url, title, m.getName(), onclick, classs);
	}
	
}
