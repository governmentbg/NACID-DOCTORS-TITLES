package bg.duosoft.security.nacid;

import java.util.Date;

public interface UserDetails extends org.springframework.security.core.userdetails.UserDetails {

	boolean hasRole(String role);

    public String getEmail();

    public String getFullName();

    public String getFirstName();

    public String getSecondName();

    public String getLastName();

    public String getPersonalNumber();

    public String getPersonalNumberType();

    public Date getBirthDate();

    public String getBirthCountryCode();

    public String getBirthCity();

    public String getCitizenshipCountryCode();





}
