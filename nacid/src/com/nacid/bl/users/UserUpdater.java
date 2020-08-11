package com.nacid.bl.users;

import java.util.List;

/**
 * interface used for manipulating users
 * @author ggeorgiev
 *
 */
public interface UserUpdater extends User {
  public List<? extends UserGroupMembershipForEdit> getUserGroupMemberships(int webApplicationId);
}
