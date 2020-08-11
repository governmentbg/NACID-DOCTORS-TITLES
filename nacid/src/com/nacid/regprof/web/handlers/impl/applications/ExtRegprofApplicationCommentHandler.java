package com.nacid.regprof.web.handlers.impl.applications;

import com.ext.nacid.regprof.web.model.applications.ExtRegprofApplicationCommentExtendedWebModel;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationImpl;
import com.nacid.bl.mail.MailDataProvider;
import com.nacid.bl.regprof.external.ExtRegprofApplicationCommentExtended;
import com.nacid.bl.regprof.external.ExtRegprofApplicationsDataProvider;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.Table;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.impl.applications.EApplyingHandler;
import com.nacid.web.handlers.impl.applications.ExtApplicationCommentHandler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * User: ggeorgiev
 * Date: 10.10.2019 г.
 * Time: 17:34
 */
public class ExtRegprofApplicationCommentHandler extends ExtApplicationCommentHandler {
    public ExtRegprofApplicationCommentHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public ExtRegprofApplicationCommentHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        String applicationId = request.getParameter(REQ_APPLICATION_ID);
        request.setAttribute(REQ_APPLICATION_ID, applicationId);
        request.setAttribute("groupName", getGroupName());
        request.setAttribute("backUrl", generateBackUrl(request, applicationId));
        request.setAttribute(WebKeys.NEXT_SCREEN, "ext_regprof_application_comment_new");
    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        String comment = request.getParameter("comment");
        boolean sendEmail = DataConverter.parseBoolean(request.getParameter("send_email"));
        int applicationId = Integer.parseInt(request.getParameter(REQ_APPLICATION_ID));
        Integer emailId = null;
        ExtRegprofApplicationsDataProvider extApplicationsDataProvider = getNacidDataProvider().getExtRegprofApplicationsDataProvider();
        if (sendEmail) {

            ExtRegprofApplicationImpl extApplication = extApplicationsDataProvider.getExtRegprofApplication(applicationId);
            Integer applicantId = extApplication.getApplicationDetails().getApplicantId();
            ExtPerson applicant = getNacidDataProvider().getExtPersonDataProvider().getExtPerson(applicantId);

            UtilsDataProvider udp = getNacidDataProvider().getUtilsDataProvider();
            String sender = udp.getCommonVariableValue(UtilsDataProvider.REGPROF_MAIL_SENDER);
            String subject = udp.getCommonVariableValue(UtilsDataProvider.ASK_APPLICANT_FOR_DOCUMENT_SUBJECT);

            MailDataProvider mdp = getNacidDataProvider().getMailDataProvider();

            emailId = mdp.sendMessage(sender, sender, applicant.getFullName(), applicant.getEmail(), subject, comment);
        }
        extApplicationsDataProvider.saveApplicationComment(applicationId, comment, sendEmail, emailId, false, getLoggedUser(request, response).getUserId());

        HttpSession session = request.getSession();
        session.removeAttribute(WebKeys.TABLE_EXT_APPLICATION_COMMENT);

        try {
            EApplyingHandler.removeCachedTables(request);
            response.sendRedirect(generateBackUrl(request, applicationId + ""));
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public void handleView(HttpServletRequest request, HttpServletResponse response) {
        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        if (id == 0) {
            throw new UnknownRecordException("No id");
        }
        String returnId = request.getParameter(REQ_APPLICATION_ID);

        ExtRegprofApplicationCommentExtended applicationComment = getNacidDataProvider().getExtRegprofApplicationsDataProvider().getApplicationComment(id);

        if (applicationComment == null) {
            throw new UnknownRecordException("No comment with id = " + id);
        }

        request.setAttribute(WebKeys.EXT_APPLICATION_COMMENT_WEB_MODEL, new ExtRegprofApplicationCommentExtendedWebModel(applicationComment, getNacidDataProvider().getUsersDataProvider()));
        request.setAttribute("backUrl", generateBackUrl(request, returnId));
        request.setAttribute(WebKeys.NEXT_SCREEN, "ext_regprof_application_comment_view");
    }
    private String generateBackUrl(HttpServletRequest request, String applicationId) {
        return request.getContextPath() + "/control/e_applying/edit?id=" + applicationId;

    }

    @Override
    protected void resetTableData(HttpServletRequest request, HttpServletResponse response) {
        int applicationId = (int) request.getAttribute("extApplId");
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_EXT_APPLICATION_COMMENT);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ExtRegprofApplicationsDataProvider extRegprofApplicationsDataProvider = nacidDataProvider.getExtRegprofApplicationsDataProvider();
        List<ExtRegprofApplicationCommentExtended> comments = extRegprofApplicationsDataProvider.getApplicationComments(applicationId);

        if (comments != null) {
            for (ExtRegprofApplicationCommentExtended m : comments) {
                try {
                    table.addRow(m.getId(),
                            m.getDateCreated(),
                            m.getComment(),
                            getSendMail(m),
                            m.getEmailRecipient(),
                            getMailStatus(m),
                            getMailType(m));
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }

    @Override
    public String getGroupName() {
        return "ext_regprof_application_comment";
    }
}
