package com.nacid.bl.impl.users;

import java.util.Set;

import com.nacid.bl.external.users.ExtUserGroupMembershipForEdit;
import com.nacid.bl.users.UserGroupMembershipForEdit;
import com.nacid.data.users.UserGroupMembershipRecord;

public class UserGroupMembershipForEditImpl extends UserGroupMembershipImpl implements UserGroupMembershipForEdit, ExtUserGroupMembershipForEdit {
  public UserGroupMembershipForEditImpl(UserGroupMembershipRecord record) {
    super(record);
  }
  public Set<Integer> getOperationIds() {
    return operationIds;
  }
}
