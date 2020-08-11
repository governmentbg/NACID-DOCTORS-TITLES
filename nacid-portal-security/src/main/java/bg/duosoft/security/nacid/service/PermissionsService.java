package bg.duosoft.security.nacid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

import java.util.*;

public  class PermissionsService {

    @Autowired(required=false)
    @Qualifier("permissionsProperties")
    private Properties permissionsProperties;


    /*public List<GrantedAuthority> getAuthoritiesByRoles(List<String> roles) {
        Set<String> permissions = getPermissions(roles);

        //updating authorities
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission));
        }
        return authorities.size() == 0 ? null : authorities;
    }*/

    /**
	 * Returns permission list for a given role.
	 * 
	 * @return returns a list of permissions for a given role
	 * @throws org.springframework.security.authentication.BadCredentialsException in case there is no such user in local repository
	 */
	public Set<String> getPermissions(Collection<String> authorities) {
        Set<String> permissions = new HashSet<String>();
		for (String role : authorities) {
			List<String> permissionsByRole = getPermissionsByRole(role);
            if (permissionsByRole != null && permissionsByRole.size() > 0) {
                permissions.addAll(permissionsByRole);
            }
		}
		return permissions;
	}

	/**
	 * Gets a role and returns a list of GrantedAuthorities with permissions
	 * 
	 * @param role
	 * @return @see List of @see GrantedAuthority containing permissions
	 */
	private List<String> getPermissionsByRole(String role) {
        List<String> out = new ArrayList<String>();
        if (permissionsProperties == null) {
            out.add(role);
            return out;
        }
        String permissions = permissionsProperties.getProperty(role);
	
		if (StringUtils.hasText(permissions)){
			out= Arrays.asList(permissions.split(","));
		}

		return out;

	}

}
