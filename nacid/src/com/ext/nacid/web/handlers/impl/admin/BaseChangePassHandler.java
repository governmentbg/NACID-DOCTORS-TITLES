package com.ext.nacid.web.handlers.impl.admin;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
//import com.nacid.bl.external.users.ExtUser;
//import com.nacid.bl.external.users.ExtUsersDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.web.WebKeys;
import com.nacid.web.model.common.SystemMessageWebModel;

public abstract class BaseChangePassHandler extends NacidExtBaseRequestHandler {

    public BaseChangePassHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("handlerId", getGroupName(request));
        setNextScreen(request, "change_pass");
    }
    
    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        User usr = getLoggedUser(request, response);
        
        String oldPass = request.getParameter("oldPass");
        String newPass = request.getParameter("newPass");
        
        SystemMessageWebModel sm = null;
        if(Utils.isEmptyString(oldPass) || Utils.isEmptyString(newPass)) {
            sm = new SystemMessageWebModel("Задължителните полета не са попълнени", 
                    SystemMessageWebModel.MESSAGE_TYPE_ERROR);
        }
        else if(!Utils.getMD5HashString(oldPass).equals(usr.getUserPass())) {
            sm = new SystemMessageWebModel("Грешна парола", 
                    SystemMessageWebModel.MESSAGE_TYPE_ERROR);
            
        }
        else {
            UsersDataProvider euDP = getNacidDataProvider().getUsersDataProvider();
            euDP.changeUserPassword(usr.getUserId(), newPass);
            sm = new SystemMessageWebModel("Паролата е сменена", 
                    SystemMessageWebModel.MESSAGE_TYPE_CORRECT);
            request.getSession().setAttribute(WebKeys.LOGGED_USER, euDP.getUser(usr.getUserId()));
        }
        
        request.setAttribute(WebKeys.SYSTEM_MESSAGE, sm);
        handleEdit(request, response);
        return;
    }
}
