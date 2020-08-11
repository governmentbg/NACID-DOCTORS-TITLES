package com.ext.nacid.web.handlers;

import java.util.List;
import java.util.Map;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.ExpertStatementAttachment;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.comision.ComissionMemberDataProvider;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.external.ExtPersonDataProvider;
import com.nacid.bl.external.applications.ExtApplication;
import com.nacid.bl.external.applications.ExtApplicationsDataProvider;
//import com.nacid.bl.external.users.ExtUser;
import com.nacid.bl.users.User;
import com.nacid.web.HandlerToGroupManager;
import com.nacid.web.RequestProcessor;
import com.nacid.web.handlers.UserOperationsUtils;

public class UserAccessUtils {

    public final static int USER_ACTION_VIEW = 0;
    public final static int USER_ACTION_CHANGE = 1;
    
	public static Integer getGroupId(String group) {
		if(!group.startsWith("/")) {
			group = "/" + group;
		}
		Map<String, String> u2hMap = RequestProcessor.getUrlActionMapping();
		String handler = u2hMap.get(group);
		if(handler==null){
			return 0;
		}
		Map<String, Integer> h2gMap = HandlerToGroupManager.getHandler2GroupMap();
		return h2gMap.get(handler);
	}
	
	public static Integer getOperationId(String operation) {
		if(operation.startsWith("/")) {
			operation = operation.substring(1);
		}
		return UserOperationsUtils.getOperationId(operation);
	}
	
	public static void checkApplicantActionAccess(int action, User user, int applicationId, NacidDataProvider nDP) {
        ExtApplicationsDataProvider eaDP = nDP.getExtApplicationsDataProvider();
        ExtApplication app = eaDP.getApplication(applicationId);
        
        checkApplicantActionAccess(action, user, app, nDP);
    }
	/**
	 * @throws RuntimeException <br />
	 * 	ako uset.getPerson().getId() != application.getApplicantId(), hvyrlq exception<br />
	 *  ako actiona e edit i status-a na zaqvlenieto e "not_editable", hvyrlq exception 
	 */
	public static void checkApplicantActionAccess(int action, User user, ExtApplication app, NacidDataProvider nacidDataProvider) {
        
        if (app == null ) {
            throw new RuntimeException("Unknown application.");
        }
        ExtPersonDataProvider epdp = nacidDataProvider.getExtPersonDataProvider();
        ExtPerson person = epdp.getExtPersonByUserId(user.getUserId());
        
        if (person.getId() != app.getRepresentativeId()) {
            throw new RuntimeException("Can't access applications which are not bound to your user id!");
        }
        
        if (action == USER_ACTION_CHANGE
                && app.getApplicationStatus() == ExtApplication.STATUS_NOT_EDITABLE) {
            throw new RuntimeException("Record cannot be edited because it's already commited");
        }
    }
	/**
	 * shte proverqva dali dadeniq potrebitel ima pravo da razglejda vytre6no zaqvlenie s applicationID
	 * @param internalApplicationId
	 * @param user
	 * @param nacidDataProvider
	 * @return
	 */
	public static boolean hasAccessToViewInternalApplicationWithId(int internalApplicationId, User user, NacidDataProvider nacidDataProvider) {
		ExtApplicationsDataProvider extApplicationsDataProvider = nacidDataProvider.getExtApplicationsDataProvider();
		ExtPersonDataProvider epdp = nacidDataProvider.getExtPersonDataProvider();
        ExtPerson person = epdp.getExtPersonByUserId(user.getUserId());
		List<ExtApplication> extApplications = extApplicationsDataProvider.getApplicationsByRepresentative(person.getId(), null);
		if (extApplications == null) {
			return false;
		}
		for (ExtApplication a:extApplications) {
			if (a.getInternalApplicationId() != null && a.getInternalApplicationId() == internalApplicationId) {
				return true;
			}
		}
		return false;
	}
	
	
	public static void checkExpertAccessToApplication(User user, int applicationId, NacidDataProvider nDP) {
	    ComissionMemberDataProvider cmdp = nDP.getComissionMemberDataProvider();
	    ComissionMember cm = cmdp.getComissionMemberByUserId(user.getUserId());
	    if(cm == null || 
	            (cm != null && !nDP.getApplicationsDataProvider().hasExpertAccessToApplication(applicationId, cm.getId()))) {
	        throw new RuntimeException("Can't access applications which are not bound to your user id!");
	    }
	}
	
	public static void checkExpertAccessToStatement(User user, ExpertStatementAttachment statement, NacidDataProvider ndp) {
        ComissionMember dm = ndp.getComissionMemberDataProvider().getComissionMemberByUserId(user.getUserId());
	    if(dm == null || 
                (dm != null && dm.getId() != statement.getExpertId())) {
            throw new RuntimeException("Can't access statements which are not bound to your user id!");
        }
        
    }
}
