package com.nacid.bl.menu;

import java.util.List;

public interface MenuItem {

	public void click();
	public boolean isColapsed();
	public Integer getId();
	public List<? extends MenuItem> getChilds();
	public String getUrl();
	public String getName();
	public String getLongName();
	public boolean isSelected();
	public boolean isActive();
	public int getOrdNum();
}
