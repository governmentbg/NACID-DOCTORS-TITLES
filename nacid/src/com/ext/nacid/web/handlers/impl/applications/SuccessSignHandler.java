package com.ext.nacid.web.handlers.impl.applications;

import com.ext.nacid.regprof.web.handlers.impl.applications.ExtRegprofApplicationsHandler;
import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.nacid.bl.signature.SuccessSign;
import com.nacid.data.DataConverter;
import com.nacid.utils.SignUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * User: Georgi
 * Date: 17.8.2020 г.
 * Time: 22:19
 */
public class SuccessSignHandler extends NacidExtBaseRequestHandler {
    public SuccessSignHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        processSuccessSign(request, response, "application");
    }
    public static void processSuccessSign(HttpServletRequest request, HttpServletResponse response, String urlPath) {
        String successSign = request.getParameter("successSign");
        if (successSign == null || "".equals(successSign.trim())) {
            throw new RuntimeException("No content...");
        }
        Integer applicationId = DataConverter.parseInteger(request.getParameter("id"), null);
        if (applicationId == null) {
            throw new RuntimeException("No application id...");
        }



        String signSecretKey = getNacidDataProvider(request.getSession()).getUtilsDataProvider().getCommonVariableValue("signatureSecretKey");

        SuccessSign sign = SignUtils.readSuccessSign(successSign, signSecretKey);

        String xmlSigned = sign.getXmlSigned();
        if (StringUtils.isEmpty(xmlSigned)) {
            throw new RuntimeException("Signed XML is empty....");
        }
        request.getSession().setAttribute("signedXml-" + request.getParameter("id"), sign);
        try {
            response.sendRedirect(request.getContextPath() + "/control/" + urlPath + "/edit"+ "?id=" + applicationId + "&activeForm=" + ExtRegprofApplicationsHandler.FORM_ID_APPLYING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
