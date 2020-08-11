package com.nacid.bl.impl.users;

import java.util.HashSet;
import java.util.Set;

import com.nacid.bl.users.UserGroupMembership;
import com.nacid.data.users.UserGroupMembershipRecord;


public class UserGroupMembershipImpl implements UserGroupMembership {
	private long userId;
	private int groupId;
	private int webApplicationId;
	protected Set<Integer> operationIds = new HashSet<Integer>();
	public UserGroupMembershipImpl(UserGroupMembershipRecord record) {
		this.userId = record.getUserId();
		this.groupId = record.getGroupId();
		this.webApplicationId = record.getWebApplication();
		String ops = record.getOperations();
		if (ops != null) {
			String[] res = ops.split(";");
			if (res != null && res.length > 0) {
				for (String s:res) {
					int current = Integer.parseInt(s);
					operationIds.add(current);
				}
			}
			
		}
		
	}
	public long getUserId() {
		return userId;
	}
	public int getGroupId() {
		return groupId;
	}
	public int getWebApplicationId() {
	    return webApplicationId;
	}
	//operaciite se zapisvat v bazata kato String ot vida 1;2;3;4;5
	//ako toq string sydyrja UserGroupMembershipRecord.FULL_ACCESS_OPERATION_ID, togava user-a ima prava za vsqka edna operationId
	public boolean hasAccess(int operationId) {
		return operationIds.contains(UserGroupMembershipRecord.FULL_ACCESS_OPERATION_ID) || operationIds.contains(operationId);
	}
}
