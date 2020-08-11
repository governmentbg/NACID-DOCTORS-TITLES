package com.nacid.web.model.table.filters;

public class HiddenFilterWebModel extends TextFieldFilterWebModel {
	/**
	 * primer new TextFieldFilterWebModel(FILTER_NAME, COLUMN_NAME, request.getParameter(FILTER_NAME));
	 * @param name
	 * @param text
	 */
	public HiddenFilterWebModel(String name, String text) {
		super(name, null, text);
	}

	@Override
	public String getType() {
		return "hidden";
	}
	
}
