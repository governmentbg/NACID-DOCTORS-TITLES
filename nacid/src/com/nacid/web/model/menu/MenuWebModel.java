package com.nacid.web.model.menu;

import java.util.List;

public class MenuWebModel {

	private int id;
	private String url;
	private String name;
	private String longName;
	private List<MenuWebModel> childs;
	private boolean colapsed;
	private boolean selected;
	private boolean active;
	private int ordNum;
	private boolean showDelete;
	
	public boolean isShowDelete() {
		return showDelete;
	}

	public MenuWebModel(int id, String url, String name, String longName,
			List<MenuWebModel> childs, boolean colapsed, boolean selected, boolean active, 
			int ordNum) {
		this.id = id;
		this.url = url;
		this.name = name;
		this.longName = longName;
		this.childs = childs;
		this.colapsed = colapsed;
		this.selected = selected;
		this.active = active;
		this.ordNum = ordNum;
	}

	public int getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public String getName() {
		return name;
	}

	public String getLongName() {
		return longName;
	}

	public List<MenuWebModel> getChilds() {
		return childs;
	}
	
	public boolean isColapsed() {
		return colapsed;
	}
	
	public boolean isActive() {
		return active;
	}

	public boolean isSelected() {
		return selected;
	}
	
	public int getOrdNum() {
		return ordNum;
	}

	public void setShowDelete(boolean showDelete) {
		this.showDelete = showDelete;
	}
}
