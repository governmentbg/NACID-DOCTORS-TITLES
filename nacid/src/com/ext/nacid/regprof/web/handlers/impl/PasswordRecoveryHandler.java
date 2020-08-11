package com.ext.nacid.regprof.web.handlers.impl;

import javax.servlet.ServletContext;

import com.nacid.bl.utils.UtilsDataProvider;

public class PasswordRecoveryHandler extends com.ext.nacid.web.handlers.impl.PasswordRecoveryHandler {

    public PasswordRecoveryHandler(ServletContext servletContext) {
        super(servletContext);
    }
    @Override 
    public String getMailSender(){
        UtilsDataProvider utilsDataProvider = getNacidDataProvider().getUtilsDataProvider();
        String sender = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.REGPROF_MAIL_SENDER);
        return sender;
    }
}
