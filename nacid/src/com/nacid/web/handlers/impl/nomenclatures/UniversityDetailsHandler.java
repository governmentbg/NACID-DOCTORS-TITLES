package com.nacid.web.handlers.impl.nomenclatures;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.table.*;
import com.nacid.bl.utils.UniversityDetail;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.nomenclatures.UniversityDetailWebModel;
import com.nacid.web.model.table.TableWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class UniversityDetailsHandler extends NacidBaseRequestHandler {

    private final static String COLUMN_NAME_ID = "id";
    private final static String COLUMN_NAME_UNIVERSITY = "Университет";
    private final static String COLUMN_NAME_LETTER_RECIPIENT = "Получател на писмо";
    private final static String COLUMN_NAME_SALUTATION = "Обръщение";

    public UniversityDetailsHandler(ServletContext servletContext) {
        super(servletContext);
    }

    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WebKeys.NEXT_SCREEN, "university_details_edit");
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        int universityDetailId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (universityDetailId <= 0) {
            throw new UnknownRecordException("Unknown UniDetail ID:request.getParameter(\"id\")=" + request.getParameter("id"));
        }
        UtilsDataProvider utilsDataProvider = getNacidDataProvider().getUtilsDataProvider();
        UniversityDetail ud = utilsDataProvider.getUniversityDetail(universityDetailId);

        if (ud == null) {
            throw new UnknownRecordException("Unknown UniDetail ID:" + universityDetailId);
        }
        request.setAttribute(WebKeys.UNIVERSITY_DETAIL_WEB_MODEL, new UniversityDetailWebModel(ud));
        request.setAttribute(WebKeys.NEXT_SCREEN, "university_details_edit");
    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
            new HomePageHandler(request.getSession().getServletContext()).processRequest(request, response);
            return;
        }
        
        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        String universityName = request.getParameter("university_name");
        String letterRecipient = request.getParameter("letter_recipient");
        String salutation = request.getParameter("salutation");

        UtilsDataProvider utilsDataProvider = getNacidDataProvider().getUtilsDataProvider();
        id = utilsDataProvider.saveUniversityDetail(id, universityName, letterRecipient, salutation);
        request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата",
                SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        

        request.setAttribute(WebKeys.UNIVERSITY_DETAIL_WEB_MODEL, new UniversityDetailWebModel(id + "", universityName, letterRecipient, salutation));
        request.setAttribute(WebKeys.NEXT_SCREEN, "university_details_edit");
        request.getSession().removeAttribute(WebKeys.TABLE_UNIVERSITY_DETAILS);
    }
  

	@Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        
        Table table = (Table) session.getAttribute(WebKeys.TABLE_UNIVERSITY_DETAILS);

        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_UNIVERSITY, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_LETTER_RECIPIENT, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_SALUTATION, CellValueDef.CELL_VALUE_TYPE_STRING);

            session.setAttribute(WebKeys.TABLE_UNIVERSITY_DETAILS, table);
            resetTableData(request);

        }

        // TableState settings
        TableState tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък с детайли за университет");

        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);

        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");
    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_UNIVERSITY_DETAILS);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        List<UniversityDetail> vars = nacidDataProvider.getUtilsDataProvider().getUniversityDetails();
        
        if (vars != null) {
            for (UniversityDetail var : vars) {
                try {
                    table.addRow(var.getId(), var.getUniversityName(), var.getLetterRecipient(), var.getSalutation());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
}
