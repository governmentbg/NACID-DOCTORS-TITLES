package com.nacid.data.menu;

public class MenuRecord {

	public final static int MAIN_PARENT_ID = -1;
	
	private Integer id;
	private Integer parentId;
	private Integer ordNum;
	private String url;
	private String name;
	private String longName;
	private int active;
	private Integer webApplication;
	public MenuRecord() {
	}
	public MenuRecord(Integer id, Integer parentId, Integer ordNum, String url,
			String name, String longName, boolean active, Integer webApplication) {
		this.id = id;
		this.parentId = parentId;
		if(parentId <= 0) {
			this.parentId = null;
		}
		this.ordNum = ordNum;
		this.url = url;
		this.name = name;
		this.longName = longName;
		this.active = active ? 1 : 0;
		this.webApplication = webApplication;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public Integer getOrdNum() {
		return ordNum;
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

	public int getActive() {
		return active;
	}
	
	public int getWebApplication() {
	    return webApplication;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public void setOrdNum(Integer ordNum) {
		this.ordNum = ordNum;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public void setWebApplication(int webApplication) {
	    this.webApplication = webApplication;
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof MenuRecord) {
			if(((MenuRecord) o).getId().equals(id))
				return true;
			else 
				return false;
		}
		else {
			return false;
		}
	}
	
}
