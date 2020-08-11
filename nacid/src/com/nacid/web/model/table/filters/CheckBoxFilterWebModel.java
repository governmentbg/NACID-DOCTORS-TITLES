package com.nacid.web.model.table.filters;

public class CheckBoxFilterWebModel extends FilterWebModel {
	
	
	public CheckBoxFilterWebModel(String name, String label, boolean active) {
		super(name, label, active ? "checked = \"checked\"" : "");
	}

	@Override
	public String getType() {
		return "checkBox";
	}

	
}
