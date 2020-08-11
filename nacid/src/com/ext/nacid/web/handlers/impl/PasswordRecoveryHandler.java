package com.ext.nacid.web.handlers.impl;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.external.ExtPersonDataProvider;
//import com.nacid.bl.external.users.ExtUser;
//import com.nacid.bl.external.users.ExtUsersDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.mail.MailBeanImpl;
import com.nacid.bl.mail.MailBean;
import com.nacid.bl.mail.MailDataProvider;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.UserOperationsUtils;
import com.nacid.web.model.common.SystemMessageWebModel;

public class PasswordRecoveryHandler extends NacidExtBaseRequestHandler {


    public PasswordRecoveryHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        String operationName = getOperationName(request);
        int operationId = UserOperationsUtils.getOperationId(operationName);

        if (operationId == UserOperationsUtils.OPERATION_LEVEL_NEW) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Добавяне на");
            handleNew(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_EDIT) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Промяна на");
            handleEdit(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_VIEW) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Преглед на");
            handleView(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_SAVE) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Промяна на");
            handleSave(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_LIST) {
            handleList(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_DELETE) {
            handleDelete(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_PRINT) {
            handlePrint(request, response);
        } else {
            handleDefault(request, response);
        }

    }
    
    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        
        request.setAttribute(WebKeys.NEXT_SCREEN, "recover_pass");
    }
    
    
    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        
        String error = "";
        if(username == null || email == null) {
            error = "Полетата са задължителни.";
        }
        else {

            UsersDataProvider euDP = getNacidDataProvider().getUsersDataProvider();
            User user = euDP.getUserByName(username);
            if (user != null) {
                ExtPersonDataProvider epdp = getNacidDataProvider().getExtPersonDataProvider();
                ExtPerson person = epdp.getExtPersonByUserId(user.getUserId());
                
                if (person.getEmail().equals(email)) {
                    
                    String pass = generateRandomPassword();
                    
                    euDP.changeUserPassword(user.getUserId(), pass);
                    
                    UtilsDataProvider udp = getNacidDataProvider().getUtilsDataProvider();
                    String subject = udp.getCommonVariableValue(UtilsDataProvider.PASS_RECOVERY_MAIL_SUBJECT);
                    String body = udp.getCommonVariableValue(UtilsDataProvider.PASS_RECOVERY_MAIL_BODY);
                    body = MessageFormat.format(body, pass);
                    String sender = getMailSender();//udp.getCommonVariableValue(UtilsDataProvider.MAIL_SENDER);
           
                    MailDataProvider mdp = getNacidDataProvider().getMailDataProvider();
            
                    Map<String, String> fromM = new HashMap<String, String>();
                    fromM.put(sender, sender);
                    Map<String, String> toM = new HashMap<String, String>();
                    toM.put(email, user.getFullName());
                    MailBean msg = new MailBeanImpl(fromM, sender, toM, 
                            ((Map<String, String>)null), ((Map<String, String>)null), subject,
                            fromM, body, new Date(), mdp.getSession());
                    mdp.sendMessage(msg);
                    
                    SystemMessageWebModel sm = new SystemMessageWebModel(
                            "Новата ви парола е изпратена на посочената електронна поща", 
                            SystemMessageWebModel.MESSAGE_TYPE_CORRECT);
                    addSystemMessageToSession(request, WebKeys.SYSTEM_MESSAGE, sm);
                    try {
                        response.sendRedirect(request.getContextPath() + "/control/login");
                        return;
                    }
                    catch(IOException e) {
                        throw Utils.logException(e);
                    }
                }
                else {
                    error = "Не е намерен такъв потребител.";
                }
            }
            else {
                error = "Не е намерен такъв потребител.";
            }
        }
            
        SystemMessageWebModel sm = new SystemMessageWebModel(
                error, 
                SystemMessageWebModel.MESSAGE_TYPE_ERROR);
        request.setAttribute(WebKeys.SYSTEM_MESSAGE, sm);
        request.setAttribute(WebKeys.NEXT_SCREEN, "recover_pass");
    }
    
    private static String generateRandomPassword() {
        String hash = Utils.getMD5HashString(System.currentTimeMillis()+"");
        String ret = "";
        Random r = new Random();
        for(int i = 0; i < 8; i ++) {
            ret += hash.charAt(r.nextInt(hash.length()));
        }
        return ret;
    }
    
    public String getMailSender(){
        UtilsDataProvider utilsDataProvider = getNacidDataProvider().getUtilsDataProvider();
        String sender = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.MAIL_SENDER);
        return sender;
    }
}
