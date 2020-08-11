package com.nacid.regprof.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.applications.AttachmentDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplication;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachment;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachmentDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplicationDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.ApplicationDocflowStatus;
import com.nacid.bl.nomenclatures.ApplicationType;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.nacid.bl.nomenclatures.ApplicationStatus.APPLICATION_PRIZNATO_STATUS_CODE;

public class RegprofApostilleServiceHandler extends NacidBaseRequestHandler {
    public RegprofApostilleServiceHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public RegprofApostilleServiceHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        RegprofApplication application = (RegprofApplication) request.getAttribute("application");
        RegprofApplicationDataProvider dp = getNacidDataProvider().getRegprofApplicationDataProvider();
        if (!StringUtils.isEmpty(application.getApplicationDetails().getExternalSystemId())) {
            if ( getNacidDataProvider().getApostilleApplicationsDataProvider().isApplicationTransferredInApostilleSystem(application.getId())) {
                request.setAttribute("showApostilleTransferredMessage", true);
            } else if (application.getApplicationDetails().getDocflowStatusId() == ApplicationDocflowStatus.APPLICATION_IZDADENO_DOCFLOW_STATUS_CODE) {
                RegprofApplicationAttachmentDataProvider attachmentDataProvider = getNacidDataProvider().getRegprofApplicationAttachmentDataProvider();
                List<RegprofApplicationAttachment> certificate = attachmentDataProvider.getAttachmentsForParentByTypes(application.getId(),DocumentType.REGPROF_CERTIFICATES);
                if (!CollectionUtils.isEmpty(certificate) && certificate.get(0).getScannedContentType() != null) {//butona se pokazva samo ako ima scanirano udostoverenie!
                    request.setAttribute("showApostilleTransferButton", true);
                }

            }
        }
    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        Integer applicationId = DataConverter.parseInteger(request.getParameter("application_id"), null);
        getNacidDataProvider().getApostilleApplicationsDataProvider().registerApplicationInApostilleSystem(applicationId);
        try {
            response.sendRedirect(getServletContext().getAttribute("pathPrefix") + "/control/regprofapplication/edit?activeForm=5&id=" + applicationId);
        } catch (IOException e) {
            throw Utils.logException(e);
        }

    }
}
