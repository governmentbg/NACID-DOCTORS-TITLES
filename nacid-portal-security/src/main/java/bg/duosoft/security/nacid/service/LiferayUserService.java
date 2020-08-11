package bg.duosoft.security.nacid.service;

import bg.duosoft.security.nacid.service.rest.LiferayRestTemplate;
import bg.duosoft.security.nacid.service.rest.objects.GetCompanyResult;
import bg.duosoft.security.nacid.service.rest.objects.GetUserResult;
import bg.duosoft.security.nacid.service.rest.objects.UserGroup;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


public class LiferayUserService implements UserDetailsService  {
    @Autowired
    PermissionsService permissionsService;
    @Value("${liferay.remote.user.name}")
    private String remoteUser;
    @Value("${liferay.remote.user.password}")
    private String remotePassword;
    @Value("${liferay.virtual.host}")
    private String virtualHost;
    @Value("${liferay.jsonws.service.url}/company/get-company-by-virtual-host/virtual-host/{virtual_host}")
    private String getCompanyByVirtualHostUrl;
    @Value("${liferay.jsonws.service.url}/user/get-user-by-screen-name/company-id/{company_id}/screen-name/{screen_name}")
    private String getUserByScreenNameUrl;
    @Value("${liferay.jsonws.service.url}/usergroup/get-user-user-groups/user-id/{user_id}")
    private String getUserUserGroupsUrl;
    @Value("${liferay.jsonws.service.url}/expandovalue/get-data/company-id/{company_id}/class-name/{class_name}/table-name/{table_name}/column-name/{column_name}/class-pk/{class_pk}")
    private String getExpandoUrl;

    private RestTemplate restTemplate;

    @PostConstruct
    private void init() {
        restTemplate = new LiferayRestTemplate(remoteUser, remotePassword);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetailsImpl result = new UserDetailsImpl();



        GetCompanyResult company = restTemplate.getForObject(getCompanyByVirtualHostUrl, GetCompanyResult.class, virtualHost);
        GetUserResult user = restTemplate.getForObject(getUserByScreenNameUrl, GetUserResult.class, company.getCompanyId(), username);

        result.setUsername(username);
        result.setCtmId(user.getUserId().longValue());
        result.setEnabled(true); //I checked that if the user is deleted, an exception is thrown on userSoap.getUserByScreenName
        result.setDtcreate(user.getCreateDate());
        result.setDtupdate(user.getModifiedDate());
        result.setEmail(user.getEmailAddress());
        result.setAccountNonLocked(true);
        result.setAccountNonExpired(true);
        result.setCredentialsNonExpired(true);


        List<String> roles = new ArrayList<String>();
        ResponseEntity<UserGroup[]> res = restTemplate.getForEntity(getUserUserGroupsUrl, UserGroup[].class, user.getUserId());
        for (UserGroup userGroup : res.getBody()) {
            roles.add(userGroup.getName());
            result.addOrignalRole(userGroup.getName());
        }

        result.setRoles(permissionsService.getPermissions(roles));

        List<String> lst = new ArrayList<String>();
        if (!StringUtils.isEmpty(user.getFirstName())) {
            lst.add(user.getFirstName());
            result.setFirstName(user.getFirstName());
        }
        if (!StringUtils.isEmpty(user.getMiddleName())) {
            lst.add(user.getMiddleName());
            result.setSecondName(user.getMiddleName());
        }
        if (!StringUtils.isEmpty(user.getLastName())) {
            lst.add(user.getLastName());
            result.setLastName(user.getLastName());
        }
        result.setFullName(StringUtils.join(lst, " "));
        result.setBirthCity(null);//TODO
        result.setBirthCountryCode(null);//TODO
        result.setBirthDate(null);//TODO
        result.setCitizenshipCountryCode(null);//TODO
        String personalNumber = restTemplate.getForObject(getExpandoUrl, String.class, company.getCompanyId(), "com.liferay.portal.model.User", "CUSTOM_FIELDS", "identifier", user.getUserId());

        personalNumber = personalNumber.length() > 1 && personalNumber.startsWith("\"") ? personalNumber.substring(1, personalNumber.length() - 1) : personalNumber;//okazva se che personalNumber-a ima kavichki v nachaloto i kraq (primerno "1010101010"), koitot rqbva da se izchistqt!!!! //2019-12-02 -> okazva se che ne vinagi ima - na produktivniq server nqma kavichki!!!!
        ResponseEntity<String[]> personaNumberType = restTemplate.getForEntity(getExpandoUrl, String[].class, company.getCompanyId(), "com.liferay.portal.model.User", "CUSTOM_FIELDS", "identifierType", user.getUserId());
        result.setPersonalNumber(personalNumber);
        result.setPersonalNumberType(personaNumberType.getBody() == null || personaNumberType.getBody().length == 0 ? null : personaNumberType.getBody()[0]);


        return result;
    }
}
