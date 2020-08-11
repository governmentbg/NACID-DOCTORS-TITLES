package com.nacid.web.model.table.filters;

public abstract class FilterWebModel {
	
	/**
	 * name - imeto na inputa
	 * value - value na inputa (za vseki tag se obrabotva individualno)
	 * label - label na inputa
	 * 
	 * elementClass - class koito se slaga na inputa.
	 * labelOnTop - dali labela da e nad inputa
	 * 
	 * getType - unikalno ime na tipa filtar
	 */
	
	private String name;
	private Object value;
	private String label;
	private String elementClass;
	private boolean labelOnTop;
	/**
	 * name - imeto na inputa
	 * value - value na inputa (za vseki tag se obrabotva individualno)
	 * label - label na inputa
	 */
	public FilterWebModel(String name, String label, Object value) {
		this.name = name;
		this.label = label;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public abstract String getType();

	public String getElementClass() {
		return elementClass != null ? "class=\"" + elementClass + "\"" : "";
	}

	public void setElementClass(String elementClass) {
		this.elementClass = elementClass;
	}

	public boolean isLabelOnTop() {
		return labelOnTop;
	}

	public void setLabelOnTop(boolean labelOnTop) {
		this.labelOnTop = labelOnTop;
	}
	
	
}