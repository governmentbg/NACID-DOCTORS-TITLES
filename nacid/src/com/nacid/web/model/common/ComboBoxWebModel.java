package com.nacid.web.model.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComboBoxWebModel {
	public static final String EMPTY_ROW_KEY = "-";
	
	private class Item {
		private String key;
		private String value;
		private Map<String, String> additionalAttributes;
		Item(String key, String value, Map<String, String> additionalAttributes) {
			this.key = key;
			this.value = value;
			this.additionalAttributes = additionalAttributes;
		}
	}
	private Map<String, List<String>> additionalAttributes;
	private List<Item> items = new ArrayList<Item>();
	private String selectedKey;
	public ComboBoxWebModel(String selectedKey) {
		this(selectedKey, false);
	}
	/**
	 * @param selectedKey
	 * @param addEmptyElement - dali da dobavi naj gore element koito da ima stojnost key = "-", value = "-"
	 */
	public ComboBoxWebModel(String selectedKey, boolean addEmptyElement) {
		this.selectedKey = selectedKey;
		if (addEmptyElement) {
			items.add(new Item(EMPTY_ROW_KEY, EMPTY_ROW_KEY, null));
		}
	}
	/**
	 * zadava izbraniq key
	 * @param selectedKey
	 */
	public void setSelectedKey(String selectedKey) {
		this.selectedKey = selectedKey;
	}
	public void addItem(String key, String value) {
		addItem(key, value, null);
	}
	public void addItem(String key, String value, Map<String, String> additionalAttributes) {
		items.add(new Item(key, value, additionalAttributes));
	}
		
	public int getItemsCount() {
		return items.size();
	}
	public String getItemKey(int itemId) {
		if (itemId < 0 || itemId >= items.size()) {
			return "";
		}
		return items.get(itemId).key;
	}
	public String getItemValue(int itemId) {
		if (itemId < 0 || itemId >= items.size()) {
			return "";
		}
		return items.get(itemId).value;
	}
	public Map<String, String> getAdditionalAttributes(int itemId) {
		if (itemId < 0 || itemId >= items.size()) {
			return null;
		}
		return items.get(itemId).additionalAttributes;
	}
	public boolean isItemSelected(int itemId) {
		if (selectedKey == null || getItemKey(itemId) == null) {
			return false;
		}
		return getItemKey(itemId).equals(selectedKey);
	}

	/*public boolean isKeySelected(String itemKey) {
    if (selectedKey == null || itemKey == null) {
      return false;
    }
    return itemKey.equals(selectedKey);
  }*/
	
	public String toString() {
		final String tab = "\n\t";
		StringBuilder retValue = new StringBuilder();
		retValue.append("ComboBoxWebModel ( ")
		.append(tab).append(" items = ").append(this.items)
		.append(tab).append(" selectedKey = ").append(this.selectedKey)
		.append(tab).append(" additionalAttributes = ").append(this.additionalAttributes)
		.append("\n )");
		return retValue.toString();
	}
    public String getSelectedKey() {
        return selectedKey;
    }

}
