package com.ext.nacid.regprof.web.handlers.impl.applications;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ext.nacid.regprof.web.handlers.ExtRegprofUserAccessUtils;
import com.ext.nacid.regprof.web.handlers.RegProfExtBaseRequestHandler;
import com.ext.nacid.regprof.web.model.applications.report.ExtRegprofApplicationForReportWebModel;
import com.ext.nacid.web.handlers.UserAccessUtils;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.applications.regprof.RegprofApplication;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachment;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachmentDataProvider;
import com.nacid.bl.exceptions.NotAuthorizedException;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationImpl;
import com.nacid.bl.regprof.external.ExtRegprofApplicationAttachmentDataProvider;
import com.nacid.bl.regprof.external.ExtRegprofApplicationsDataProvider;
import com.nacid.bl.regprof.external.ExtRegprofESignedInformation;
import com.nacid.data.DataConverter;
import com.nacid.regprof.web.model.applications.report.internal.RegprofApplicationInternalForReportWebModel;
import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.report.base.AttachmentForReportBaseWebModel;

//RayaWritten------------------------------------------------
public class RegprofApplicantReportHandler extends RegProfExtBaseRequestHandler {
    public RegprofApplicantReportHandler(ServletContext servletContext) {
        super(servletContext);
    }

    /**
     * generira applicantReport sprqmo internal application
     * @param nacidDataProvider
     * @param application
     * @param request
     * @param attrPrefix - shte se polzva za prefix na atributa, t.k. ima stranici (zasega edna edinstvena, v koqto trqbva da se vijdat 2 reporta ednovremenno i atributite trqbva da sa s razlichni imena)
     */
    public static void generateInternalApplicantReport(NacidDataProvider nacidDataProvider, RegprofApplication application, HttpServletRequest request, String attrPrefix) {
        RegprofApplicationAttachmentDataProvider applicationAttachmentDataProvider = nacidDataProvider.getRegprofApplicationAttachmentDataProvider();
        List<RegprofApplicationAttachment> attachments = applicationAttachmentDataProvider.getAttachmentsForParent(application.getId());
        if (attachments != null && attachments.size() > 0) {
            List<AttachmentForReportBaseWebModel> attachmentsWebModel = new ArrayList<AttachmentForReportBaseWebModel>();
            for (Attachment a:attachments) {
                attachmentsWebModel.add(new AttachmentForReportBaseWebModel(a));
            }
            request.setAttribute((attrPrefix == null ? "" : attrPrefix) + WebKeys.REGPROF_APPLICATION_ATTCH_FOR_REPORT_WEB_MODEL, attachmentsWebModel);
        }
        request.setAttribute((attrPrefix == null ? "" : attrPrefix) + "applicationId", application.getId());
        request.setAttribute((attrPrefix == null ? "" : attrPrefix) + WebKeys.REGPROF_APPLICATION_FOR_REPORT_WEB_MODEL, 
                new RegprofApplicationInternalForReportWebModel(application, nacidDataProvider));
    }
    /**
     * generira applicantReport sprqmo external application
     * @param nacidDataProvider
     * @param application
     * @param request
     * @param attrPrefix
     * @param extPerson
     */
    public static void generateExternalApplicantReport(NacidDataProvider nacidDataProvider, ExtRegprofApplicationImpl application, HttpServletRequest request, String attrPrefix, ExtPerson extPerson) {
        ExtRegprofApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getExtRegprofApplicationsDataProvider();
        ExtRegprofApplicationAttachmentDataProvider applicationAttachmentDataProvider = nacidDataProvider.getExtRegprofApplicationAttachmentDataProvider();
        List<Attachment> attachments = applicationAttachmentDataProvider.getAttachmentsForApplication(application.getApplicationDetails().getId());
        if (attachments != null && attachments.size() > 0) {
            List<AttachmentForReportBaseWebModel> attachmentsWebModel = new ArrayList<AttachmentForReportBaseWebModel>();
            for (Attachment a:attachments) {
                attachmentsWebModel.add(new AttachmentForReportBaseWebModel(a));
            }
            request.setAttribute((attrPrefix == null ? "" : attrPrefix) + WebKeys.REGPROF_APPLICATION_ATTCH_FOR_REPORT_WEB_MODEL, attachmentsWebModel);
        }
        request.setAttribute((attrPrefix == null ? "" : attrPrefix) + "applicationId", application.getApplicationDetails().getId());
        ExtRegprofESignedInformation esignedInfo = applicationsDataProvider.getEsignedInformation(application.getApplicationDetails().getId());
        request.setAttribute((attrPrefix == null ? "" : attrPrefix) + WebKeys.REGPROF_APPLICATION_FOR_REPORT_WEB_MODEL,  new ExtRegprofApplicationForReportWebModel(application, nacidDataProvider, extPerson, esignedInfo));
    }

}
//-------------------------------------------------------------
