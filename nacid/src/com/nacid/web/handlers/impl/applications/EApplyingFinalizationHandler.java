package com.nacid.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.external.applications.ExtApplication;
import com.nacid.bl.external.applications.ExtApplicationsDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.NacidBaseRequestHandler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * User: ggeorgiev
 * Date: 15.10.2019 г.
 * Time: 18:39
 */
public class EApplyingFinalizationHandler extends NacidBaseRequestHandler {
    public EApplyingFinalizationHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public EApplyingFinalizationHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        int applicationId = DataConverter.parseInt(request.getParameter("id"), 0);
        ExtApplicationsDataProvider extApplicationsDataProvider = getNacidDataProvider().getExtApplicationsDataProvider();
        ExtApplication extApplication = extApplicationsDataProvider.getApplication(applicationId);
        if (extApplication == null) {
            throw new RuntimeException("Unknown ext application id " + applicationId);
        }
        if (extApplication.getApplicationStatus() != ExtApplication.STATUS_NOT_EDITABLE) {
            throw new RuntimeException("Unsupported application status " + extApplication.getApplicationStatus());
        }
        extApplicationsDataProvider.markApplicationFinished(applicationId);
        try {
            EApplyingHandler.removeCachedTables(request);
            String entryNumSeries = NumgeneratorDataProvider.APPLICATION_TYPE_TO_ENTRYNUM_SERIES.get(extApplication.getApplicationType()).stream().map(r -> r.toString()).collect(Collectors.joining(","));
            response.sendRedirect(request.getContextPath() + "/control/e_applying/list?entryNumSeries=" + entryNumSeries + "&getLastTableState=1");
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }

}
