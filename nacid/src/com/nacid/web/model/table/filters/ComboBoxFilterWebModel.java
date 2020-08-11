package com.nacid.web.model.table.filters;

import com.nacid.web.model.common.ComboBoxWebModel;

public class ComboBoxFilterWebModel extends FilterWebModel {
	/**
	 * comboName - imeto na combobox-a (tova koeto se slaga v &lt;select name="comboName"&gt; label - etiketa na comboBox-a
	 */
	public ComboBoxFilterWebModel(ComboBoxWebModel comboBox, String comboName, 
			String label) {
		super(comboName, label, generateOptionsString(comboBox));
	}

	public static String generateOptionsString(ComboBoxWebModel comboBox) {
		StringBuilder result = new StringBuilder("");
		for (int i = 0; i < comboBox.getItemsCount(); i++) {
			String selected = comboBox.isItemSelected(i) ? "selected=\"selected\"" : "";
			result.append("\t<option value=\"" + comboBox.getItemKey(i) + "\" " + selected + " >" + comboBox.getItemValue(i) + "</option>\r\n");
		}
		return result.toString();
	}

	@Override
	public String getType() {
		return "comboBox";
	}
}
