package bg.duosoft.security.nacid.service;

import bg.duosoft.security.nacid.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;



public class UserDetailsImpl implements UserDetails {
	private String password;
	private String username;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;
    private String firstName;
    private String secondName;
    private String lastName;
	private String fullName;
	private Long ctmId;
	private Date dtcreate;
	private Date dtupdate;
	private String email;
    private String birthCity;
    private String birthCountryCode;
    private Date birthDate;
    private String citizenshipCountryCode;
    private String personalNumber;
    private String personalNumberType;


    private boolean fullyInitialized;
    private Collection<GrantedAuthority> authorities;
	/**
	 * 
	 */
	private static final long serialVersionUID = 2769723626409535122L;
	
	private Set<String> roles;
    private Set<String> originalRoles;
	//private Set<String> rights;
	
	public UserDetailsImpl() {

	}
	

	public boolean hasRole(String role) {
		return (roles != null ? roles.contains(role) : false);
	}

	public void addRole(String role) {
		if (roles == null) {
			roles = new HashSet<String>();
		}
		roles.add(role);
	}


	public Collection<GrantedAuthority> getAuthorities() {
		if (authorities == null) {
            authorities = new ArrayList<GrantedAuthority>();
            if (roles != null) {
                for (String role : roles) {
                    authorities.add(new SimpleGrantedAuthority(role));
                }
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
        }
        return authorities;
	}

    public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}


	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}


	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}


	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}


	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}


	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}


	public boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	public Set<String> getRoles() {
		return roles;
	}


	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}


	/*public Set<String> getRights() {
		return rights;
	}


	public void setRights(Set<String> rights) {
		this.rights = rights;
	}*/


	public Long getCtmId() {
		return ctmId;
	}


	public void setCtmId(Long ctmId) {
		this.ctmId = ctmId;
	}


	public Date getDtcreate() {
		return dtcreate;
	}


	public void setDtcreate(Date dtcreate) {
		this.dtcreate = dtcreate;
	}


	public Date getDtupdate() {
		return dtupdate;
	}


	public void setDtupdate(Date dtupdate) {
		this.dtupdate = dtupdate;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

    public void addOrignalRole(String role) {
        if (originalRoles == null) {
            originalRoles = new HashSet<String>();
        }
        originalRoles.add(role);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }

    public String getBirthCountryCode() {
        return birthCountryCode;
    }

    public void setBirthCountryCode(String birthCountryCode) {
        this.birthCountryCode = birthCountryCode;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getCitizenshipCountryCode() {
        return citizenshipCountryCode;
    }

    public void setCitizenshipCountryCode(String citizenshipCountryCode) {
        this.citizenshipCountryCode = citizenshipCountryCode;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getPersonalNumberType() {
        return personalNumberType;
    }

    public void setPersonalNumberType(String personalNumberType) {
        this.personalNumberType = personalNumberType;
    }
}
