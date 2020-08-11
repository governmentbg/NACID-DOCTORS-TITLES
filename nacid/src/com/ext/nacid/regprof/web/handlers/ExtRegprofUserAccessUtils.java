package com.ext.nacid.regprof.web.handlers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.exceptions.NotAuthorizedException;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationDetailsImpl;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationImpl;
import com.nacid.bl.regprof.external.ExtRegprofApplicationsDataProvider;

public class ExtRegprofUserAccessUtils {
    public final static int USER_ACTION_VIEW = 0;
    public final static int USER_ACTION_CHANGE = 1;
    public static final Map<Integer, List<Integer>> ACTION_TO_STATUS = new HashMap<Integer, List<Integer>>();
    static {
        ACTION_TO_STATUS.put(USER_ACTION_VIEW, Arrays.asList(ExtRegprofApplicationImpl.STATUS_EDITABLE, ExtRegprofApplicationImpl.STATUS_NOT_EDITABLE, ExtRegprofApplicationImpl.STATUS_TRANSFERED));
        ACTION_TO_STATUS.put(USER_ACTION_CHANGE, Arrays.asList(ExtRegprofApplicationImpl.STATUS_EDITABLE));
    }
    
    public static void checkApplicantActionAccess(int applicationId, int userId, int operationType, NacidDataProvider dp) throws NotAuthorizedException {
        ExtRegprofApplicationsDataProvider eaDP = dp.getExtRegprofApplicationsDataProvider();
        eaDP.checkApplicationAccess(applicationId, userId, operationType);
    }
    /**
     * @throws RuntimeException<br />
     *  ako uset.getPerson().getId() != application.getApplicantId(), hvyrlq exception<br />
     *  ako actiona e edit i status-a na zaqvlenieto e "not_editable", hvyrlq exception 
     */
    public static void checkApplicantActionAccess(int action, ExtPerson person, ExtRegprofApplicationDetailsImpl appDetails) throws NotAuthorizedException {
        
        if (appDetails == null ) {
            throw new RuntimeException("Unknown application.");
        }
        
        if (person.getId() != appDetails.getRepresentativeId()) {
            throw new NotAuthorizedException("Can't access applications which are not bound to your user id!");
        }
        
        if (action == USER_ACTION_CHANGE
                && appDetails.getStatus() == ExtRegprofApplicationImpl.STATUS_NOT_EDITABLE) {
            throw new NotAuthorizedException("Record cannot be edited because it's already commited");
        }
    }
}
