package com.nacid.web.handlers;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class UserOperationsUtils {
	public static final int OPERATION_LEVEL_NEW = 1;
	public static final int OPERATION_LEVEL_EDIT = 2;
	public static final int OPERATION_LEVEL_SAVE = 3;
	public static final int OPERATION_LEVEL_LIST = 4;
	public static final int OPERATION_LEVEL_DELETE = 5;
	public static final int OPERATION_LEVEL_VIEW = 6;
	public static final int OPERATION_LEVEL_PRINT = 7;
	public static HashMap<String, Integer> partOfUrl2OperationLevel = new HashMap<String, Integer>();
	static {
		partOfUrl2OperationLevel.put("new", OPERATION_LEVEL_NEW);
		partOfUrl2OperationLevel.put("edit", OPERATION_LEVEL_EDIT);
		partOfUrl2OperationLevel.put("save", OPERATION_LEVEL_SAVE);
		partOfUrl2OperationLevel.put("list", OPERATION_LEVEL_LIST);
		partOfUrl2OperationLevel.put("delete", OPERATION_LEVEL_DELETE);
		partOfUrl2OperationLevel.put("view", OPERATION_LEVEL_VIEW);
		partOfUrl2OperationLevel.put("print", OPERATION_LEVEL_PRINT);
	}
	public static Map<Integer, String> operationToNameMap = new LinkedHashMap<Integer, String>();
	static {
	  operationToNameMap.put(OPERATION_LEVEL_NEW, "нов запис");
	  operationToNameMap.put(OPERATION_LEVEL_EDIT, "редактиране");
	  operationToNameMap.put(OPERATION_LEVEL_SAVE, "запис");
	  operationToNameMap.put(OPERATION_LEVEL_LIST, "таблица");
	  operationToNameMap.put(OPERATION_LEVEL_DELETE, "изтриване");
	  operationToNameMap.put(OPERATION_LEVEL_VIEW, "преглед");
	  operationToNameMap.put(OPERATION_LEVEL_PRINT, "принтиране");
	}

	
	public static int getOperationId(String operationName) {
		if (operationName == null) {
			return 0;
		}
		Integer operationId = partOfUrl2OperationLevel.get(operationName);
		return operationId == null ? 0 : operationId;
	}
	public static String getOperationName(int operationId) {
		for (Entry<String, Integer> e:partOfUrl2OperationLevel.entrySet()) {
			if (e.getValue() == operationId) {
				return e.getKey();
			}
		}
		return null;
	}
	public static boolean isOperationDefined(String operationName){
		return partOfUrl2OperationLevel.containsKey(operationName);
	}
}
