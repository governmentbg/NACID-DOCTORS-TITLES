package com.nacid.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.ApplicationType;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.model.common.SystemMessageWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by georgi.georgiev on 22.06.2015.
 */
public class DuplicateCertificateHandler extends NacidBaseRequestHandler {
    private static final Map<String, Integer> DUPLICATE_TYPE_TO_DOCUMENT_TYPE = new HashMap<String, Integer>() {{
        put("duplicate", DocumentType.DOC_TYPE_CERTIFICATE_DUPLICATE);
        put("obvious_factual_error", DocumentType.DOC_TYPE_CERTIFICATE_FACTUAL_ERROR);
    }};
    private static final Map<String, Integer> DOCTORATE_DUPLICATE_TYPE_TO_DOCUMENT_TYPE = new HashMap<String, Integer>() {{
        put("duplicate", DocumentType.DOC_TYPE_DOCTORATE_CERTIFICATE_DUPLICATE);
        put("obvious_factual_error", DocumentType.DOC_TYPE_DOCTORATE_CERTIFICATE_FACTUAL_ERROR);
    }};
    public DuplicateCertificateHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public DuplicateCertificateHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();

        int id = DataConverter.parseInt(request.getParameter("id"), 0);

        String duplicateType = request.getParameter("duplicate_type");
        Integer applicationType = DataConverter.parseInteger(request.getParameter("application_type"), null);
        if (applicationType == null) {
            throw new RuntimeException("Unknown applicationType" + request.getParameter("application_type"));
        }




        ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
        int documentType;

        Map<String, Integer> map = applicationType == ApplicationType.DOCTORATE_APPLICATION_TYPE ? DOCTORATE_DUPLICATE_TYPE_TO_DOCUMENT_TYPE : DUPLICATE_TYPE_TO_DOCUMENT_TYPE;
        String errMsg;
        documentType = map.get(duplicateType);
        errMsg = ApplicationAttachmentHandler.checkApplicationCertificateGenerationPossibility(id, documentType, getNacidDataProvider());
        if (errMsg != null) {
            addSystemMessageToSession(request, "applicationStatusMsg", new SystemMessageWebModel(errMsg, SystemMessageWebModel.MESSAGE_TYPE_ERROR));
        } else {
            if (duplicateType.equals("duplicate")) {
                applicationsDataProvider.duplicateApplicationCertificate(id, getLoggedUser(request, response).getUserId());
            } else if (duplicateType.equals("obvious_factual_error")) {
                applicationsDataProvider.duplucateApplicationCertificateBecauseOfFactualError(id, getLoggedUser(request, response).getUserId());
            }

            addSystemMessageToSession(request, "applicationStatusMsg", new SystemMessageWebModel("Генерирано е ново удостоверение, което се намира в прикачени документи. Ръчно трябва да се добави протокол за унищожаване!", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        }

        String redirectString =  getServletContext().getAttribute("pathPrefix") + "/control/applications/edit?activeForm=4&id=" + id;
        try {
            response.sendRedirect(redirectString);
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }
}
