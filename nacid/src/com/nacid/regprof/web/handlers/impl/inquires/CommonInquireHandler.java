package com.nacid.regprof.web.handlers.impl.inquires;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.Company;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.regprof.RegProfApplicationForInquireImpl;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.bl.regprof.RegprofInquireDataProvider;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.regprof.web.model.inquires.CommonInquireWebModel;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.ComboBoxUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
//TODO:Trqbva da ima edin obsht InquireBaseHandler po podobie na nacid-skoto prilojenie! 
public class CommonInquireHandler extends InquireBaseHandler {
    
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_APPLICATION_NUMBER = "Делов. номер";
    private static final String COLUMN_NAME_DATE = "Дата";
    private static final String COLUMN_NAME_APPLICANT_NAMES = "Име";
    private static final String COLUMN_NAME_PERSONAL_IDENTIFIER = "Персонален идентификатор";
    private static final String COLUMN_NAME_CURRENT_STATUS = "Актуален статус";
    private static final String COLUMN_NAME_DOCFLOW_STATUS = "Деловоден статус";
    private static final String COLUMN_NAME_RECOGNIZED_PROFESSION = "Кандидатства за професионална квалификация";
    
    public CommonInquireHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public CommonInquireHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleView(HttpServletRequest request,HttpServletResponse response) {
        
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider ndp = nacidDataProvider.getNomenclaturesDataProvider();
        List<Company> companies = nacidDataProvider.getCompanyDataProvider().getCompanies(false);
        
        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_COUNTRY, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "countriesCombo", null, true);
        ComboBoxUtils.generateComboBox(null, companies, request, "companiesCombo", true, "getId", "getName");
        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_TYPE, nacidDataProvider.getNomenclaturesDataProvider(), true, request, "educationTypesCombo", null, true);
        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_DOCUMENT_TYPE, nacidDataProvider.getNomenclaturesDataProvider(), true, request, "educationDocumentTypesCombo", null, true);
        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, ndp, false, request, "eduLevels", null, true);
        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION_DEGREE, ndp, false, request, "qualificationDegrees", null, true);
        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_REGPROF_ARTICLE_DIRECTIVE, ndp, false, request, "directivesCombo", null, true);
        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE_DOCUMENT_TYPE, ndp, false, request, "experienceDocumentTypesCombo", null, true);
        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_SERVICE_TYPE, ndp, false, request, "serviceTypeCombo", null, true);
        
        List<ApplicationStatus> allStatuses = ndp.getApplicationStatuses(NumgeneratorDataProvider.REGPROF_SERIES_ID, null,
                OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), false);
        
        ComboBoxUtils.generateNomenclaturesComboBox(null, allStatuses, false, request, "allStatusesCombo", true);
        List<FlatNomenclature> finalStatuses = new ArrayList<FlatNomenclature>();
        for (ApplicationStatus fn:allStatuses) {
            if (fn.isLegal()) {
                finalStatuses.add(fn);
            }
        }
        OrderCriteria columnNameOrderCriteria = OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true);

        List<ApplicationStatus> allLegalStatuses = ndp.getApplicationStatuses(NumgeneratorDataProvider.REGPROF_SERIES_ID, null, columnNameOrderCriteria, true);
        ComboBoxUtils.generateNomenclaturesComboBox(null, allLegalStatuses, true, request, "allLegalStatusesCombo", true);


        List<ApplicationDocflowStatus> allDocflowStatuses = ndp.getApplicationDocflowStatuses(NumgeneratorDataProvider.REGPROF_SERIES_ID, null, columnNameOrderCriteria);
        ComboBoxUtils.generateNomenclaturesComboBox(null, allDocflowStatuses, true, request, "allDocflowStatusesCombo", true);


        List<DocumentType> attachmentDocumentTypesCombo = ndp.getDocumentTypes(null, columnNameOrderCriteria, DocCategory.REG_PROF_APPLICATION_ATTACHMENTS);
        ComboBoxUtils.generateNomenclaturesComboBox(null, attachmentDocumentTypesCombo, false, request, "attachmentDocumentTypesCombo", true);

        request.setAttribute(WebKeys.NEXT_SCREEN, "common_inquire");
    }

    @Override
    protected Table constructEmptyTable(HttpServletRequest request) {
        TableFactory tableFactory = TableFactory.getInstance();
        Table table = tableFactory.createTable();
        table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
        table.addColumnHeader(COLUMN_NAME_APPLICATION_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
        table.addColumnHeader(COLUMN_NAME_APPLICANT_NAMES, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_PERSONAL_IDENTIFIER, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_CURRENT_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_DOCFLOW_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_RECOGNIZED_PROFESSION, CellValueDef.CELL_VALUE_TYPE_STRING);
        
        return table;
    }

    @Override
    protected List<RegProfApplicationForInquireImpl> getApplications(HttpServletRequest request) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        RegprofInquireDataProvider inquireDataProvider = nacidDataProvider.getRegprofInquireDataProvider();
        CommonInquireWebModel webmodel = new CommonInquireWebModel(request);
        List<RegProfApplicationForInquireImpl> result = inquireDataProvider.getRegprofApplicationsForCommonInqure(
                webmodel.iseSubmited(),
                webmodel.geteSigned(),
                webmodel.getAppDateFrom(), 
                webmodel.getAppDateTo(), 
                webmodel.getRepresentativeCompanyIds(), 
                webmodel.getProfessionaInstitutions(), 
                webmodel.getApplicantCountryIds(), 
                webmodel.getDiplomaDateFrom(), 
                webmodel.getDiplomaDateTo(), 
                webmodel.getEducationType(), 
                webmodel.getSecQualificationIds(), 
                webmodel.getHigherQualificationIds(), 
                webmodel.getSecSpecialityIds(), 
                webmodel.getHigherSpecialityIds(), 
                webmodel.getSdkSpecialityIds(),
                webmodel.getRecognizedHigherEduLevelIds(), 
                webmodel.getRecognizedSecondaryQualificationDegrees(),
                webmodel.getRecogniedProfessions(), 
                webmodel.getExperienceProfessionIds(),
                webmodel.getDirectiveArticleIds(),
                webmodel.getApplicationStatuses(),
                webmodel.getServiceType(),
                webmodel.getServiceTypeDateTo(),
                webmodel.getImiCorrespondences(),
                webmodel.getAttachmentDocumentTypeIds(),
                webmodel.getApostilleApplication(),
                webmodel.getEducationDocumentTypeIds(),
                webmodel.getExperienceDocumentTypeIds()
        );
        
        return result;
    }

    @Override
    protected String getTableName() {
        return "tableCommonInquire";
    }

    @Override
    protected void resetTableData(HttpServletRequest request, List<RegProfApplicationForInquireImpl> apps) {
        Table table = (Table) request.getSession().getAttribute(getTableName());
        if (table == null) {
            return;
        }
        table.emtyTableData();
        if (apps != null) {
            for (RegProfApplicationForInquireImpl a:apps) {
                try {
                    table.addRow(a.getId(), a.getAppNumber(), a.getAppDate(), a.getApplicantDiplomaNames(), a.getCivilId(), a.getStatus(), a.getDocflowStatus(), a.getCertificateQualification());
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
        
    }
}
