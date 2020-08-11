/*******************************************************************************
 * * $Id:: PermissionUtils.java 57312 2013-02-20 16:33:19Z karalch               $
 * *       . * .
 * *     * RRRR  *    Copyright © 2012 OHIM: Office for Harmonization
 * *   .   RR  R   .  in the Internal Market (trade marks and designs)
 * *   *   RRR     *
 * *    .  RR RR  .   ALL RIGHTS RESERVED
 * *     * . _ . *
 ******************************************************************************/
package bg.duosoft.security.nacid;

import org.springframework.security.core.Authentication;

public class PermissionUtils {
	
	/**
	 * Checks whether the given permission is accessible to the given authentication
	 * @param authentication the authentication object containing information about the registered user
	 * @param permission the permission that is required to access a specific resource or page
	 * @return true if it is authorised
	 */
	public static boolean checkRolesMapping(Authentication authentication, String permission) {
		if (authentication!=null 
				&& authentication.getPrincipal()!=null
				&& authentication.getPrincipal() instanceof UserDetails) {
			UserDetails portalUserDetails = (UserDetails) authentication.getPrincipal();
			return ((portalUserDetails.hasRole(permission)));
		} else {
			return false;
		}
	}
}
