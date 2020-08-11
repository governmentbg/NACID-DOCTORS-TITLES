package com.nacid.regprof.web.handlers.impl.ajax;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nacid.bl.NacidDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
//RayaWritten----------------------------------------------------------------
public class ServiceTypeAjaxHandler extends RegProfBaseRequestHandler {

    public ServiceTypeAjaxHandler(ServletContext servletContext) {
        super(servletContext);
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response){
        Integer servId = DataConverter.parseInteger(request.getParameter("serviceId"), 0);
        String appDate = request.getParameter("applicationDate");      
        writeToResponse(response, DataConverter.formatDate(getNacidDataProvider().getRegprofApplicationDataProvider().calculateApplicationEndDate(DataConverter.parseDate(appDate), servId)));
    }

    

    public static void main(String args[]){
        NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
        String dt = "10.01.2013";
        System.out.println(DataConverter.parseDate(dt));
        System.out.println(nacidDataProvider.getRegprofApplicationDataProvider().calculateApplicationEndDate(null, 2));
        
        

    }
}
//---------------------------------------------------------------------------
