package com.nacid.bl.users;

import com.nacid.data.users.UserGroupMembershipRecord;

public interface UserGroupMembership {

    public static final int ADMINISTRATION_GROUP_ID = 1;
    public static final int NOMENCLATURES_GROUP_ID = 2;
    public static final int APPLICATIONS_GROUP_ID = 3;
    public static final int INQUIRES_GROUP_ID = 4;

    public static final int FULL_ACCESS_OPERATION_ID = UserGroupMembershipRecord.FULL_ACCESS_OPERATION_ID;
    public static final int FULL_ACCESS_GROUP_ID = UserGroupMembershipRecord.FULL_ACCESS_GROUP_ID;


	public long getUserId();
	public int getGroupId();
	public int getWebApplicationId();
	public boolean hasAccess(int operationId);
}
