package com.nacid.web.handlers.impl.inquires;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.inquires.InquiresDataProvider;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.table.*;
import com.nacid.data.inquire.BGAcademicRecognitionForReportRecord;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.inquires.BgAcademicRecognitionInquireWebModel;
import com.nacid.web.model.inquires.CommissionInquireUniversityWebModel;
import com.nacid.web.model.table.TableWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * Created by georgi.georgiev on 13.09.2016.
 */
public class BgAcademicRecognitionInquiryHandler extends NacidBaseRequestHandler {
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_APPLICANT = "Заявител";
    private static final String COLUMN_NAME_CITIZENSHIP = "Гражданство";

    private static final String COLUMN_NAME_UNIVERSITY = "Университет";
    private static final String COLUMN_NAME_UNIVERSITY_COUNTRY = "Държава на ВУ";
    private static final String COLUMN_NAME_EDUCATION_LEVEL = "Придобита ОКС";
    private static final String COLUMN_NAME_DIPLOMA_SPECIALITY = "Специалност по диплома";
    private static final String COLUMN_NAME_DIPLOMA_NUMBER = "Номер на диплома";
    private static final String COLUMN_NAME_DIPLOMA_DATE = "Дата на диплома";
    private static final String COLUMN_NAME_PROTOCOL_NUMBER = "Решение на АС на ВУ";
    private static final String COLUMN_NAME_DENIAL_PROTOCOL_NUMBER = "Решение на АС на ВУ (отказ/ професионален бакалавър / бакалавър / магистър)";
    private static final String COLUMN_NAME_RECOGNIZED_SPECIALITY = "Призната специалност";
    private static final String COLUMN_NAME_IN_NUMBER = "Входящ номер";
    private static final String COLUMN_NAME_OUT_NUMBER = "Изходящ номер";

    public BgAcademicRecognitionInquiryHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public BgAcademicRecognitionInquiryHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleView(HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("universities", Arrays.asList(new CommissionInquireUniversityWebModel(null, new ComboBoxWebModel(null, true))));
        request.setAttribute("universities_count", 1);
        request.setAttribute("emptyUniversityCombo", new ComboBoxWebModel(null, true));

        ComboBoxUtils.generateNomenclaturesComboBox(NomenclaturesDataProvider.COUNTRY_ID_BULGARIA, NomenclaturesDataProvider.NOMENCLATURE_COUNTRY, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "countriesCombo", null, true);
        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_BG_ACAD_REC_STATUS, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "recognitionStatusesCombo", null, true);
        setNextScreen(request, "bg_academic_recognition_inquire");

    }

    @Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        BgAcademicRecognitionInquireWebModel wm = new BgAcademicRecognitionInquireWebModel(request);
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        InquiresDataProvider inquiresDataProvider = nacidDataProvider.getInquiresDataProvider();
        List<BGAcademicRecognitionForReportRecord> list = inquiresDataProvider.getBgAcademicRecognitionForReportRecords(wm.getOwnerNames(), wm.getCitizenshipNames(), wm.getUniversityNames(), wm.getUniversityCountryNames(), wm.getDiplomaSpecialities(), wm.getDiplomaEducationLevelNames(), wm.getPrototolNumber(), wm.getDenialPrototolNumber(), wm.getRecognizedSpecialityNames(), wm.getUniversityIds(), wm.getOutputNumber(), wm.getInputNumber(), wm.getRecognitionStatuses());



        //System.out.println("Inside Inquire Base Handler's handle list");
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(getTableName());

        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            table = constructEmptyTable(request);
            session.setAttribute(getTableName(), table);
            resetTableData(request, list);
        }

        // TableState settings
        TableState tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);


        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel(null);

        webmodel.setGroupName("bgacademicrecognition");
        webmodel.setFormGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_EDIT);
        webmodel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
        //webmodel.addRequestParam(TableWebModel.OPERATION_NAME_VIEW, WebKeys.APPLICATION_BACK_URL, getGroupName(request) + "/view");
        webmodel.setViewOpenInNewWindow(true);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);


        request.setAttribute(WebKeys.NEXT_SCREEN, "inquire_list");


    }

    protected Table constructEmptyTable(HttpServletRequest request) {
        TableFactory tableFactory = TableFactory.getInstance();
        Table table = tableFactory.createTable();

        table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
        table.addColumnHeader(COLUMN_NAME_APPLICANT, CellValueDef.CELL_VALUE_TYPE_STRING);

        table.addColumnHeader(COLUMN_NAME_UNIVERSITY, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_UNIVERSITY_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);

        table.addColumnHeader(COLUMN_NAME_DIPLOMA_SPECIALITY, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_DIPLOMA_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_DIPLOMA_DATE, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_PROTOCOL_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_DENIAL_PROTOCOL_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_RECOGNIZED_SPECIALITY, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_IN_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_OUT_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);

        request.getSession().setAttribute(getTableName(), table);
        resetTableData(request, null);

        return table;

    }
    private void resetTableData(HttpServletRequest request, List<BGAcademicRecognitionForReportRecord> list) {
        Table table = (Table) request.getSession().getAttribute(getTableName());
        if (table == null) {
            return;
        }
        table.emtyTableData();


        if (list != null) {
            for (BGAcademicRecognitionForReportRecord acRec : list) {
                try {

                    table.addRow(acRec.getId(), acRec.getApplicant(), acRec.getUniversity(), acRec.getUniversityCountry(),
                            acRec.getDiplomaSpeciality(), acRec.getDiplomaNumber(), acRec.getDiplomaDate(),
                            acRec.getProtocolNumber(), acRec.getDenialProtocolNumber(),
                            acRec.getRecognizedSpeciality(), acRec.getInputNumber(), acRec.getOutputNumber());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
    private String getTableName() {
        return "tableBgAcademicInquire";
    }
}
