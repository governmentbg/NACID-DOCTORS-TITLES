package com.nacid.bl.impl.menu;

import java.util.ArrayList;
import java.util.List;

//import com.nacid.bl.external.menu.ExtMenuItem;
import com.nacid.bl.menu.MenuItem;

class MenuItemImpl implements MenuItem/*, ExtMenuItem*/ {
	
	private Integer id;
	private List<MenuItemImpl> childs;
	private String url;
	private String name;
	private String longName;
	private boolean colapsed;
	private boolean selected;
	private MenuItemImpl parent;
	private boolean active;
	private int ordNum;
	
	MenuItemImpl(Integer id, String url, 
			String name, String longName, MenuItemImpl parent, boolean active,
			int ordNum) {
		this.id = id;
		this.url = url;
		this.longName = longName;
		this.name = name;
		this.childs = new ArrayList<MenuItemImpl>();
		this.colapsed = true;
		this.selected = false;
		this.parent = parent;
		this.active = active;
		this.ordNum = ordNum;
	}
	
	@Override
	public void click() {
		colapsed = !colapsed;
	}
	
	@Override
	public boolean isColapsed() {
		return colapsed;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public List<MenuItemImpl> getChilds() {
		return childs;
	}
	
	void addChild(MenuItemImpl child) {
		childs.add(child);
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getLongName() {
		return longName;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}
	
	@Override
	public boolean isActive() {
		return active;
	}
	
	void setSelected(boolean selected) {
		this.selected = selected;
		MenuItemImpl par = parent;
		while(par != null) {
			par.colapsed = false;
			par = par.parent;
		}
	}

	@Override
	public int getOrdNum() {
		return ordNum;
	}
}
