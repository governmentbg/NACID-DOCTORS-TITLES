package com.nacid.bl.users;

public interface UsersGroupMembership {
  public boolean hasAccess(int groupId, int operationId, int webApplicationId);
}
