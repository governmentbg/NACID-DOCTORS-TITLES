package com.nacid.bl.table.impl;

import com.nacid.bl.table.CellValue;

public abstract class FilterMatcher {

	public static boolean match(FilterCriteria filterCriteria, CellValue value) {
		//Ne znam dali izobshto trqbva da se popada v tozi slu4ai - spored moite razmishleniq - ne.....
		if (value == null) {
			return false;
		}
		Boolean result = false;
		for (CellValue cv:filterCriteria.getCellValue()) {
		    switch (filterCriteria.getCondition()) {
	        case FilterCriteria.CONDITION_GREATER_THAN: {
	            result |= value.compareTo(cv) > 0;
	            break;
	        }
	        case FilterCriteria.CONDITION_LESS_THAN: {
	            result |= value.compareTo(cv) < 0;
	            break;
	        }
	        case FilterCriteria.CONDITION_EQUALS: {
	            /**
	             * ako i dvete stojnosti sa null, togava vry6ta true,
	             * no ako samo edna ot dvete stojnosti e null, a drugata ne e, togava vry6ta false!!!
	             */
	            if ((value.getValue() == null) && cv.getValue() == null) {
	                result |= true;
	                break;
	            } else if (value.getValue() == null || cv.getValue() == null) {
	                result |= false;
	                break;
	            }
	            result |= (value.compareTo(cv) == 0);
	            break;
	        }
	        case FilterCriteria.CONDITION_STARTS_WITH: {
	            //Ako stojnostta koqto shte se sravnqva e null, togava vry6ta false
	            if (value.getValue() == null) {
	                result |= false;
	                break;
	            }
	            if (cv.getValue() == null) {
	                throw new IllegalArgumentException("You cannot use null as \"starts with\" filter value");
	            }
	            if (!(value.getValue() instanceof String) || !(cv.getValue() instanceof String)) {
	                throw new IllegalArgumentException("FilterMatcher Criteria \"Condition Starts with\" is applicable only to String columns!");
	            }
	            result |= ((String)value.getValue()).toLowerCase().startsWith(((String)cv.getValue()).toLowerCase());
	            break;
	        }
	        case FilterCriteria.CONDITION_CONTAINS: {
	            //Ako stojnostta koqto shte se sravnqva e null, togava vry6ta false
	            if (value.getValue() == null) {
	                result |= false;
	                break;  
	            }
	            if (cv.getValue() == null) {
	                throw new IllegalArgumentException("You cannot use null as \"contains\" filter value");
	            }
	            if (!(value.getValue() instanceof String) || !(cv.getValue() instanceof String)) {
	                throw new IllegalArgumentException("FilterMatcher Criteria \"Condition Contains\" is applicable only to String columns!");
	            }
	            result |= ((String)value.getValue()).toLowerCase().contains(((String)cv.getValue()).toLowerCase());
	            break;
	        }
	        default: {
	                throw new IllegalArgumentException("Invalid filter condition: " + filterCriteria.getCondition());   
	        }
	        } 
		}
		
		return filterCriteria.isNot() ? !result : result;
	}
}
