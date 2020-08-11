package com.nacid.web.handlers.impl.applications;

import com.ext.nacid.web.model.applications.ExtApplicationCommentExtendedWebModel;
import com.ext.nacid.web.model.applications.report.ExtApplicationForReportWebModel;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.external.applications.ExtApplication;
import com.nacid.bl.external.applications.ExtApplicationCommentExtended;
import com.nacid.bl.external.applications.ExtApplicationsDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.mail.MailDataProvider;
import com.nacid.bl.table.*;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * User: ggeorgiev
 * Date: 10.10.2019 г.
 * Time: 14:34
 */
public class ExtApplicationCommentHandler extends NacidBaseRequestHandler {

    protected static final String REQ_APPLICATION_ID = "applicationId";

    private final static String COLUMN_NAME_ID = "id";
    private final static String COLUMN_NAME_STATUS = "Статус";
    private final static String COLUMN_NAME_EMAIL = "Изпращане на email";
    private final static String COLUMN_NAME_INOUT = "Тип";
    private final static String COLUMN_NAME_DATE = "Дата";
    private final static String COLUMN_NAME_TO = "Получател";
    private final static String COLUMN_NAME_COMMENT = "Съобщение";


    public ExtApplicationCommentHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public ExtApplicationCommentHandler(ServletContext servletContext) {
        super(servletContext);
    }

    public static String getSendMail(ExtApplicationCommentExtended m) {
        return m.isSendEmail() ? "да" : "не";
    }

    public static String getMailStatus(ExtApplicationCommentExtended m) {
        return m.isSendEmail() ? (m.isEmailProcessed() == null ? "грешка при изпращането" : (m.isEmailProcessed() ? "изпратено" : "обработва се")) : "-";
    }

    public static String getMailType(ExtApplicationCommentExtended m) {
        return m.isEmailIncoming() == null ? "-" : (m.isEmailIncoming() ? "входящо" : "изходящо");
    }

    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        String applicationId = request.getParameter(REQ_APPLICATION_ID);
        request.setAttribute(REQ_APPLICATION_ID, applicationId);
        request.setAttribute("groupName", getGroupName());
        request.setAttribute(EApplyingHandler.ENTRY_NUM_SERIES_REQUEST_PARAMETER, request.getParameter(EApplyingHandler.ENTRY_NUM_SERIES_REQUEST_PARAMETER));
        request.setAttribute("backUrl", generateBackUrl(applicationId, request));
        request.setAttribute(WebKeys.NEXT_SCREEN, "ext_application_comment_new");
    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {

        String comment = request.getParameter("comment");
        boolean sendEmail = DataConverter.parseBoolean(request.getParameter("send_email"));
        int applicationId = Integer.parseInt(request.getParameter(REQ_APPLICATION_ID));
        Integer emailId = null;
        ExtApplicationsDataProvider extApplicationsDataProvider = getNacidDataProvider().getExtApplicationsDataProvider();
        if (sendEmail) {

            ExtApplication extApplication = extApplicationsDataProvider.getApplication(applicationId);

            UtilsDataProvider udp = getNacidDataProvider().getUtilsDataProvider();
            String sender = udp.getCommonVariableValue(UtilsDataProvider.MAIL_SENDER);
            String subject = udp.getCommonVariableValue(UtilsDataProvider.ASK_APPLICANT_FOR_DOCUMENT_SUBJECT);

            MailDataProvider mdp = getNacidDataProvider().getMailDataProvider();

            emailId = mdp.sendMessage(sender, sender, extApplication.getApplicant().getFullName(), extApplication.getApplicant().getEmail(), subject, comment);
        }
        extApplicationsDataProvider.saveApplicationComment(applicationId, comment, sendEmail, emailId, false, getLoggedUser(request, response).getUserId());

        HttpSession session = request.getSession();
        session.removeAttribute(WebKeys.TABLE_EXT_APPLICATION_COMMENT);

        try {
            EApplyingHandler.removeCachedTables(request);
            response.sendRedirect(generateBackUrl(applicationId + "", request));
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }

    public String getGroupName() {
        return "ext_application_comment";
    }
    @Override
    public void handleView(HttpServletRequest request, HttpServletResponse response) {
        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        if (id == 0) {
            throw new UnknownRecordException("No id");
        }
        String returnId = request.getParameter(REQ_APPLICATION_ID);

        ExtApplicationCommentExtended applicationComment = getNacidDataProvider().getExtApplicationsDataProvider().getApplicationComment(id);

        if (applicationComment == null) {
            throw new UnknownRecordException("No comment with id = " + id);
        }

        request.setAttribute(WebKeys.EXT_APPLICATION_COMMENT_WEB_MODEL, new ExtApplicationCommentExtendedWebModel(applicationComment, getNacidDataProvider().getUsersDataProvider()));
        request.setAttribute("backUrl", generateBackUrl(returnId, request));
        request.setAttribute(WebKeys.NEXT_SCREEN, "ext_application_comment_view");
    }

    private String generateBackUrl(String applicationId, HttpServletRequest request) {
       return request.getContextPath() + "/control/e_applying/edit?id=" + applicationId + "&entryNumSeries=" + request.getParameter(EApplyingHandler.ENTRY_NUM_SERIES_REQUEST_PARAMETER);
    }
    @Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        fillCommentsTable(request, response);
    }

    private void fillCommentsTable(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_EXT_APPLICATION_COMMENT);
        boolean reloadTable = true;//RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();


            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_COMMENT, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_EMAIL, CellValueDef.CELL_VALUE_TYPE_STRING);

            table.addColumnHeader(COLUMN_NAME_TO, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_INOUT, CellValueDef.CELL_VALUE_TYPE_STRING);


            session.setAttribute(WebKeys.TABLE_EXT_APPLICATION_COMMENT, table);
            resetTableData(request, response);

        }

        // TableState settings
        TableState tableState = (TableState) session
                .getAttribute(WebKeys.TABLE_EXT_APPLICATION_COMMENT + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
         * vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            session.setAttribute(WebKeys.TABLE_EXT_APPLICATION_COMMENT + WebKeys.TABLE_STATE, tableState);
        }


        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък на коментари към заявлението");
        // webmodel.setColumnFormatter("userDate",
        // CellFormatter.DATE_TIME_FORMATTER);
        webmodel.setGroupName(getGroupName());
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_EDIT);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
        webmodel.addRequestParam(TableWebModel.OPERATION_NAME_ALL, REQ_APPLICATION_ID, request.getParameter("id"));
        webmodel.addRequestParam(TableWebModel.OPERATION_NAME_ALL, EApplyingHandler.ENTRY_NUM_SERIES_REQUEST_PARAMETER, request.getParameter(EApplyingHandler.ENTRY_NUM_SERIES_REQUEST_PARAMETER));
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        //request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session
                .getAttribute(WebKeys.TABLE_EXT_APPLICATION_COMMENT + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            session.setAttribute(WebKeys.TABLE_EXT_APPLICATION_COMMENT
                    + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }

    protected void resetTableData(HttpServletRequest request, HttpServletResponse response) {
        ExtApplicationForReportWebModel eaWM = (ExtApplicationForReportWebModel) request.getAttribute(WebKeys.APPLICATION_FOR_REPORT_WEB_MODEL);
        int applicationId = eaWM.getIntId();
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_EXT_APPLICATION_COMMENT);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ExtApplicationsDataProvider extApplicationsDataProvider = nacidDataProvider.getExtApplicationsDataProvider();
        List<ExtApplicationCommentExtended> comments = extApplicationsDataProvider.getApplicationComments(applicationId);

        if (comments != null) {
            for (ExtApplicationCommentExtended m : comments) {
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


}
