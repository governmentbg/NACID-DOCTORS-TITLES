package com.nacid.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.applications.AttachmentDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.ApplicationDocflowStatus;
import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.bl.nomenclatures.ApplicationType;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.impl.nomenclatures.DocflowApplicationStatusHandler;
import org.springframework.util.CollectionUtils;

import javax.print.Doc;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.nacid.bl.nomenclatures.ApplicationStatus.APPLICATION_PRIZNATO_STATUS_CODE;

/**
 * User: ggeorgiev
 * Date: 25.10.2019 г.
 * Time: 14:20
 */
public class RasServiceHandler extends NacidBaseRequestHandler {
    public RasServiceHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public RasServiceHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        Application application = (Application) request.getAttribute("application");
        if (application.getApplicationType() == ApplicationType.DOCTORATE_APPLICATION_TYPE && application.getApplicationStatusId() == APPLICATION_PRIZNATO_STATUS_CODE) {
            if (getNacidDataProvider().getRasApplicationsDataProvider().isApplicationTransferredInRas(application.getId())) {
                request.setAttribute("showRasTransferredMessage", true);
            } else if (application.getApplicationDocflowStatusId() == ApplicationDocflowStatus.APPLICATION_IZDADENO_DOCFLOW_STATUS_CODE && application.getCertificateNumber() != null) {//certificate number-a trqbva za da moje da se generira nomera na zaqvlenieto v ras
                AttachmentDataProvider attachmentDataProvider = getNacidDataProvider().getApplicationAttachmentDataProvider();
                List<Attachment> certificate = attachmentDataProvider.getAttachmentsForParentByTypes(application.getId(), new ArrayList<>(DocumentType.RUDI_DOCTORATE_CERTFIFICATE_DOC_TYPES));
                if (!CollectionUtils.isEmpty(certificate) && certificate.get(0).getScannedContentType() != null) {//butona se pokazva samo ako ima scanirano udostoverenie!
                    request.setAttribute("showRasTransferButton", true);
                }

            }
        }
    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        Integer applicationId = DataConverter.parseInteger(request.getParameter("application_id"), null);
        getNacidDataProvider().getRasApplicationsDataProvider().registerRasDoctorateApplication(applicationId);
        try {
            response.sendRedirect(getServletContext().getAttribute("pathPrefix") + "/control/applications/edit?activeForm=5&id=" + applicationId);
        } catch (IOException e) {
            throw Utils.logException(e);
        }

    }
}
