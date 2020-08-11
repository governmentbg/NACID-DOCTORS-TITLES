package com.nacid.bl.external.users;

import java.util.Set;

import com.nacid.bl.users.UserGroupMembershipForEdit;

/**
 * toq interface shte se polzva samo pri editvane na users
 * @author ggeorgiev
 *
 */
public interface ExtUserGroupMembershipForEdit extends UserGroupMembershipForEdit{
	/**
	 * shte pokazva dali user-a ima dostyp do dadena operaciq - kato nqma da vry6ta true ako user-a ima full access za vsi4ki operacii ot grupata
	 * za razlika ot methoda definiran v {@link UserGroupMembership}
	 */
	//public boolean hasAccess(int operationId);
	/**
	 * shte pokazva dali potrebitelq ima full access za dadenata grupa
	 * @return
	 */
	//public boolean hasFullAccess();
  public int getGroupId();
  public Set<Integer> getOperationIds();
}
