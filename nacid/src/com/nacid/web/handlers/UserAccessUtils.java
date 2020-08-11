package com.nacid.web.handlers;

import com.nacid.web.HandlerToGroupManager;
import com.nacid.web.RequestProcessor;

import java.util.Map;

public class UserAccessUtils {

	public static Integer getGroupId(String group) {
		if(!group.startsWith("/")) {
			group = "/" + group;
		}
		Map<String, String> u2hMap = RequestProcessor.getUrlActionMapping();
		String handler = u2hMap.get(group);
		if(handler==null){
			return 0;
		}
		Map<String, Integer> h2gMap = HandlerToGroupManager.getHandler2GroupMap();
		return h2gMap.get(handler);
	}
	
	public static Integer getOperationId(String operation) {
		if(operation.startsWith("/")) {
			operation = operation.substring(1);
		}
		return UserOperationsUtils.getOperationId(operation);
	}
}
