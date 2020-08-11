package com.nacid.regprof.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.external.applications.ExtApplication;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationImpl;
import com.nacid.bl.regprof.external.ExtRegprofApplicationsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.impl.applications.EApplyingHandler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: ggeorgiev
 * Date: 15.10.2019 г.
 * Time: 19:02
 */
public class RegprofEApplyingFinalizationHandler extends NacidBaseRequestHandler {
    public RegprofEApplyingFinalizationHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public RegprofEApplyingFinalizationHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        int applicationId = DataConverter.parseInt(request.getParameter("id"), 0);
        ExtRegprofApplicationsDataProvider extApplicationsDataProvider = getNacidDataProvider().getExtRegprofApplicationsDataProvider();
        ExtRegprofApplicationImpl extApplication = extApplicationsDataProvider.getExtRegprofApplication(applicationId);
        if (extApplication == null) {
            throw new RuntimeException("Unknown ext application id " + applicationId);
        }
        if (extApplication.getApplicationDetails().getStatus() != ExtApplication.STATUS_NOT_EDITABLE) {
            throw new RuntimeException("Unsupported application status " + extApplication.getApplicationDetails().getStatus());
        }
        extApplicationsDataProvider.markApplicationFinished(applicationId);
        try {
            EApplyingHandler.removeCachedTables(request);
            response.sendRedirect(request.getContextPath() + "/control/e_applying/list?getLastTableState=1");
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }
}
