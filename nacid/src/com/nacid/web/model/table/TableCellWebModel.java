package com.nacid.web.model.table;

import com.nacid.bl.table.CellValue;

public class TableCellWebModel {
	private String value;
	private String title;
	TableCellWebModel(CellFormatter formatter, CellValue value) {
		this.value = formatter.getValue(value);
		this.title = formatter.getCellTitle(value);
	}

	public String getValue() {
		return value;
	}

	public String getTitle() {
		return title;
	}
}
