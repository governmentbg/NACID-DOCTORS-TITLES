package com.nacid.web.handlers.impl.nomenclatures;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.web.handlers.NacidBaseRequestHandler;
//RayaWritten----------------------------------------------------------------
public class ClearNomenclaturesHandler extends NacidBaseRequestHandler{

    public ClearNomenclaturesHandler(ServletContext servletContext) {
        super(servletContext);
    }
    
    public void processRequest(HttpServletRequest request, HttpServletResponse response){
        NomenclaturesDataProvider nomenclaturesDP = getNacidDataProvider().getNomenclaturesDataProvider();
        nomenclaturesDP.resetAllNomenclatures();
    }

}
//------------------------------------------------------------------------------
