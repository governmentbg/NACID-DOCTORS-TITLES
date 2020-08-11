package com.nacid.bl.table.impl;

import java.util.ArrayList;
import java.util.List;

import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValue;



public class FilterCriteria {
	/**
	 * po-malko ot zadadenata stojnost
	 */
	public static final int CONDITION_LESS_THAN = 1;
	/**
	 * po-golqmo ot zadadenata stojnost
	 */
	public static final int CONDITION_GREATER_THAN = 2;
	/**
	 * ravno na zadadenata stojnost
	 * (za Stringove EQUALS ozna4ava equalsIgnoreCase!)
	 */
	public static final int CONDITION_EQUALS = 3;

	/**
	 * vaji samo za String criterias - filtrira vsi4ki redove, 4iito stojnost v zadadenata kolona zapo4vat s opredelen izraz!
	 * Filtera e Case insensitive!
	 */
	public static final int CONDITION_STARTS_WITH = 4;


	/**
	 * vaji samo String criterias - filtrira vsi4ki redove koito sydyrjat dadeniq izraz
	 */
	public static final int CONDITION_CONTAINS = 5;
	/**
	 * ako ima pove4e ot edno CellValue, to kombinaciqta mejdu tqh e ILI!!!
	 */
	private List<CellValue> cellValue;
	private int condition;
	private String columnName;
	private boolean isNot = false;

	/**
	 * 
	 * @param valueType - tip na stoinostta na kriteriq
	 * @param columnName - vyrhu koq kolona shte se prilaga kriteriq
	 * @param condition - systoqnie - edno ot definiranite v FilterCriteria
	 * @param value - stojnost
	 * @param isNot - dali uslovieto shte e protivopolojno na tova keto e opisano v condition (primerno usloviet e EQUALS, i e sett-nato isNot, uslovieto stava NOT EQUALS)
	 * @throws CellCreationException - ako ne moje da syzdade CellValue ot podadenite "value" i "valueType" 
	 */
	
	public FilterCriteria(int valueType, String columnName, int condition, Object value, boolean isNot) throws CellCreationException{
		this.columnName = columnName;
		this.cellValue = new ArrayList<CellValue>();
		this.cellValue.add(CellValue.createCellValue(value, valueType));
		this.condition = condition;
		this.isNot = isNot;
	}
	
	/**
     * 
     * @param valueType - tip na stoinostta na kriteriq
     * @param columnName - vyrhu koq kolona shte se prilaga kriteriq
     * @param condition - systoqnie - edno ot definiranite v FilterCriteria
     * @param value - stojnost - List ot stojnosti, kato togava se tyrsi po kriterii ILI (primerno x = 5 ili x = 6)...
     * @param isNot - dali uslovieto shte e protivopolojno na tova keto e opisano v condition (primerno usloviet e EQUALS, i e sett-nato isNot, uslovieto stava NOT EQUALS)
     * @throws CellCreationException - ako ne moje da syzdade CellValue ot podadenite "value" i "valueType" 
     */
    
    public FilterCriteria(int valueType, String columnName, int condition, List<Object> value, boolean isNot) throws CellCreationException{
        this.columnName = columnName;
        this.cellValue = new ArrayList<CellValue>();
        for (Object o:value) {
            this.cellValue.add(CellValue.createCellValue(o, valueType));    
        }
        this.condition = condition;
        this.isNot = isNot;
    }
	
	
	
	/*public FilterCriteria(int valueType, String columnName, int condition, Object value) throws CellCreationException{
		this(valueType, columnName, condition, value, false);
	}*/

	public String getColumnName() {
		return columnName;
	}

	public List<CellValue> getCellValue() {
		return cellValue;
	}

	/*public int getCellValueType() {
    	return cellValue.getCellValueType();
  	}*/

	public int getCondition() {
		return condition;
	}

	public void setCondition(int condition) {
		this.condition = condition;
	}
	/**
	 * @return dali uslovieto e protivopolojno na tova v condition (primerno ako condition = EQUALS i isNot=true, togava uslovieto stava NOT EQUALS)
	 */
	public boolean isNot() {
		return isNot;
	}
}
