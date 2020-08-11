package com.nacid.web.model.table.filters;

public class TextFieldFilterWebModel extends FilterWebModel {
	/**
	 * primer new TextFieldFilterWebModel(FILTER_NAME, COLUMN_NAME, request.getParameter(FILTER_NAME));
	 * @param name
	 * @param label
	 * @param text
	 */
	public TextFieldFilterWebModel(String name, String label, String text) {
		super(name, label, text);
	}

	@Override
	public String getType() {
		return "textField";
	}
	
}
