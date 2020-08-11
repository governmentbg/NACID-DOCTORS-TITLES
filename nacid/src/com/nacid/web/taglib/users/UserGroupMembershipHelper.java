package com.nacid.web.taglib.users;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nacid.data.users.UserGroupMembershipRecord;
import com.nacid.web.handlers.UserOperationsUtils;

public class UserGroupMembershipHelper {
  public static Map<Integer, String> operationToNameMap = new LinkedHashMap<Integer, String>();
  static {
    operationToNameMap.put(UserGroupMembershipRecord.FULL_ACCESS_OPERATION_ID, "Всички");
    operationToNameMap.putAll(UserOperationsUtils.operationToNameMap);
  }
}
