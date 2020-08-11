package com.nacid.web.handlers.impl.nomenclatures;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.table.*;
import com.nacid.data.DataConverter;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.nomenclatures.FlatNomenclatureWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.nacid.configuration.security.rest.RestSecurityConfiguration.REST_PASS;
import static com.nacid.configuration.security.rest.RestSecurityConfiguration.REST_USER;
@Log4j
public class FlatNomenclatureHandler extends NacidBaseRequestHandler {

    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_NAME = "Наименование";
    private static final String COLUMN_NAME_DATE_FROM = "От дата";
    private static final String COLUMN_NAME_DATE_TO = "До дата";
    public static Map<String, Integer> groupNameToNomenclatureIdMap = new HashMap<String, Integer>();
    protected final String editNomenclatureScreen;
    static {
        groupNameToNomenclatureIdMap.put("nom_civilidtype", NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE);
        groupNameToNomenclatureIdMap.put("nom_commissionposition", NomenclaturesDataProvider.FLAT_NOMENCLATURE_COMMISSION_POSITION);
        groupNameToNomenclatureIdMap.put("nom_durationunit", NomenclaturesDataProvider.FLAT_NOMENCLATURE_DURATION_UNIT);
        groupNameToNomenclatureIdMap.put("nom_educationarea", NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_AREA);
        groupNameToNomenclatureIdMap.put("nom_educationlevel", NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL);
        groupNameToNomenclatureIdMap.put("nom_graduationway", NomenclaturesDataProvider.FLAT_NOMENCLATURE_GRADUATION_WAY);
        groupNameToNomenclatureIdMap.put("nom_qualification", NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION);
        groupNameToNomenclatureIdMap.put("nom_original_qualification", NomenclaturesDataProvider.FLAT_NOMENCLATURE_ORIGINAL_QUALIFICATION);
        groupNameToNomenclatureIdMap.put("nom_recognitionpurpose", NomenclaturesDataProvider.FLAT_NOMENCLATURE_RECOGNITION_PURPOSE);
        groupNameToNomenclatureIdMap.put("nom_trainingform", NomenclaturesDataProvider.FLAT_NOMENCLATURE_TRAINING_FORM);
        //groupNameToNomenclatureIdMap.put("nom_doctype", NomenclaturesDataProvider.NOMENCLATURE_DOCUMENT_TYPE);
        groupNameToNomenclatureIdMap.put("nom_copytype", NomenclaturesDataProvider.FLAT_NOMENCLATURE_COPY_TYPE);
        groupNameToNomenclatureIdMap.put("nom_appsessionstatus", NomenclaturesDataProvider.FLAT_NOMENCLATURE_APPLICATION_SESSION_STATUS);
        groupNameToNomenclatureIdMap.put("nom_traininglocation", NomenclaturesDataProvider.FLAT_NOMENCLATURE_TRAINING_LOCATION);
        groupNameToNomenclatureIdMap.put("nom_sessionstatus", NomenclaturesDataProvider.FLAT_NOMENCLATURE_SESSION_STATUS);
        groupNameToNomenclatureIdMap.put("nom_doc_category", NomenclaturesDataProvider.FLAT_NOMENCLATURE_DOC_CATEGORY);
        groupNameToNomenclatureIdMap.put("nom_eventstatus", NomenclaturesDataProvider.FLAT_NOMENCLATURE_EVENT_STATUS);
        //groupNameToNomenclatureIdMap.put("nom_legalreason", NomenclaturesDataProvider.FLAT_NOMENCLATURE_LEGAL_REASON);
        groupNameToNomenclatureIdMap.put("nom_professionalinstitutiontype", NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSIONAL_INSTITUTION_TYPE);
        groupNameToNomenclatureIdMap.put("nom_qualificationdegree", NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION_DEGREE);
        groupNameToNomenclatureIdMap.put("nom_secondarycaliber", NomenclaturesDataProvider.FLAT_NOMENCLATURE_SECONDARY_CALIBER);
        groupNameToNomenclatureIdMap.put("nom_profession", NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION);
        groupNameToNomenclatureIdMap.put("nom_education_type", NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_TYPE);
        groupNameToNomenclatureIdMap.put("nom_profession_experience", NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE);
        groupNameToNomenclatureIdMap.put("nom_higher_prof_qualification", NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION);
        groupNameToNomenclatureIdMap.put("nom_higher_speciality", NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_SPECIALITY);
        groupNameToNomenclatureIdMap.put("nom_education_document_type", NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_DOCUMENT_TYPE);
        groupNameToNomenclatureIdMap.put("nom_article_directive", NomenclaturesDataProvider.FLAT_NOMENCLATURE_REGPROF_ARTICLE_DIRECTIVE);
        groupNameToNomenclatureIdMap.put("nom_certificate_prof_qualification", NomenclaturesDataProvider.FLAT_NOMENCLATURE_CERTIFICATE_PROF_QUALIFICATION);
        //groupNameToNomenclatureIdMap.put("nom_secondaryprofqualification", NomenclaturesDataProvider.flat)
        //groupNameToNomenclatureIdMap.put("nom_profession_experience_document_type", NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION_EXPERIENCE_DOCUMENT_TYPE);
        groupNameToNomenclatureIdMap.put("nom_payment_type", NomenclaturesDataProvider.FLAT_NOMENCLATURE_PAYMENT_TYPE);
        groupNameToNomenclatureIdMap.put("nom_original_speciality", NomenclaturesDataProvider.FLAT_NOMENCLATURE_ORIGINAL_SPECIALITY);
        groupNameToNomenclatureIdMap.put("nom_docflow_status", NomenclaturesDataProvider.NOMENCLATURE_APPLICATION_DOCFLOW_STATUS);
        groupNameToNomenclatureIdMap.put("nom_regprof_docflow_status", NomenclaturesDataProvider.NOMENCLATURE_APPLICATION_DOCFLOW_STATUS);
        groupNameToNomenclatureIdMap.put("nom_bologna_cycle", NomenclaturesDataProvider.FLAT_NOMENCLATURE_BOLOGNA_CYCLE);
        groupNameToNomenclatureIdMap.put("nom_european_qualifications_framework", NomenclaturesDataProvider.FLAT_NOMENCLATURE_EUROPEAN_QUALIFICATIONS_FRAMEWORK);
        groupNameToNomenclatureIdMap.put("nom_graduation_document_type", NomenclaturesDataProvider.FLAT_NOMENCLATURE_GRADUATION_DOCUMENT_TYPE);
        groupNameToNomenclatureIdMap.put("nom_grade", NomenclaturesDataProvider.FLAT_NOMENCLATURE_GRADE);
        groupNameToNomenclatureIdMap.put("nom_school_type", NomenclaturesDataProvider.FLAT_NOMENCLATURE_SCHOOL_TYPE);
        groupNameToNomenclatureIdMap.put("nom_age_range", NomenclaturesDataProvider.FLAT_NOMENCLATURE_AGE_RANGE);
        groupNameToNomenclatureIdMap.put("nom_personaliddoctype", NomenclaturesDataProvider.FLAT_NOMENCLATURE_PERSONAL_ID_DOCUMENT_TYPE);
//        groupNameToNomenclatureIdMap.put("nom_document_receive_method", NomenclaturesDataProvider.FLAT_NOMENCLATURE_DOCUMENT_RECEIVE_METHOD);
        groupNameToNomenclatureIdMap.put("nom_university_generic_name", NomenclaturesDataProvider.FLAT_NOMENCLATURE_UNIVERSITY_GENERIC_NAME);
        groupNameToNomenclatureIdMap.put("nom_language", NomenclaturesDataProvider.NOMENCLATURE_LANGUAGE);
    }
    private static Map<String, String> groupNameToNomenclatureNameMap = new HashMap<String, String>();
    static {
        groupNameToNomenclatureNameMap.put("nom_civilidtype", "вид идентификатор|Видове идентификатори");
        groupNameToNomenclatureNameMap.put("nom_commissionposition", "длъжност в комисията|Длъжности в комисията");
        groupNameToNomenclatureNameMap.put("nom_durationunit", "Семестър/Триместър|Семестър/Триместър");
        groupNameToNomenclatureNameMap.put("nom_educationarea", "област на образованието|Области на образованието");
        groupNameToNomenclatureNameMap.put("nom_educationlevel", "степен на образованието|Степени на образованието");
        groupNameToNomenclatureNameMap.put("nom_graduationway", "начин на дипломиране|Начини на дипломиране");
        groupNameToNomenclatureNameMap.put("nom_qualification", "професионална квалификация|Професионални квалификации");
        groupNameToNomenclatureNameMap.put("nom_original_qualification", "оригинална професионална квалификация|Оригинални професионални квалификации");
        groupNameToNomenclatureNameMap.put("nom_recognitionpurpose", "цел на признаването|Цели на признаването");
        groupNameToNomenclatureNameMap.put("nom_trainingform", "форма на обучението|Форми на обучението");
        //groupNameToNomenclatureNameMap.put("nom_doctype", "тип документ|Типове документи");
        groupNameToNomenclatureNameMap.put("nom_copytype", "форма на документа|Форми на документа");
        groupNameToNomenclatureNameMap.put("nom_appsessionstatus", "статус на appsession|Статуси на appsession");
        groupNameToNomenclatureNameMap.put("nom_traininglocation", "място на обучението|Места на обучението");
        groupNameToNomenclatureNameMap.put("nom_sessionstatus", "статус на заседанието|Статуси на заседанието");
        groupNameToNomenclatureNameMap.put("nom_doc_category", "категория на документ|Категории документи");
        groupNameToNomenclatureNameMap.put("nom_eventstatus", "статус на напомняне|Статуси на напомняне");
        //groupNameToNomenclatureNameMap.put("nom_legalreason", "правно основание|Правни основания");
        groupNameToNomenclatureNameMap.put("nom_professionalinstitutiontype", "тип обучителна институция|Типове обучителни институции");
        groupNameToNomenclatureNameMap.put("nom_regprofappstatus", "статус на заявлението (рег. проф.)|Статуси на заявлението (рег. проф)");
        groupNameToNomenclatureNameMap.put("nom_qualificationdegree", "степен на проф. квалификация|Степени на проф. квалификация");
        groupNameToNomenclatureNameMap.put("nom_secondarycaliber", "разряд|Разреди");
        groupNameToNomenclatureNameMap.put("nom_profession", "удостоверена професия|Удостоверени професии");
        groupNameToNomenclatureNameMap.put("nom_article_directive", "Член Директива|Член Директива");
        groupNameToNomenclatureNameMap.put("nom_education_type", "вид обучение|Вид обучение");
        groupNameToNomenclatureNameMap.put("nom_profession_experience", "професия стаж|Професия стаж");
        groupNameToNomenclatureNameMap.put("nom_higher_prof_qualification", "професионални квалификации България висше|Професионални квалификации България висше");
        groupNameToNomenclatureNameMap.put("nom_higher_speciality", "специалност висше|Специалност висше");
        groupNameToNomenclatureNameMap.put("nom_education_document_type", "Вид документ за образование|Видове документи за образование");
        groupNameToNomenclatureNameMap.put("nom_certificate_prof_qualification", "професионална квалификация, за която се иска издаването на удостоверение|" +
        		"Професионална квалификация, за която се иска издаването на удостоверение");
        //groupNameToNomenclatureNameMap.put("nom_profession_experience_document_type", "Тип документ за стаж|Типове документи за стаж");
        groupNameToNomenclatureNameMap.put("nom_payment_type", "начин на плащане|Начини на плащане");
        groupNameToNomenclatureNameMap.put("nom_original_speciality", "Специалност - оригинално наименоване|Специалности - оригинално наименование");
        groupNameToNomenclatureNameMap.put("nom_docflow_status", "Деловоден статус|Деловодни статуси");
        groupNameToNomenclatureNameMap.put("nom_regprof_docflow_status", "Деловоден статус|Деловодни статуси");
        groupNameToNomenclatureNameMap.put("nom_bologna_cycle", "Цикъл по Болоня|Цикли по Болоня");
        groupNameToNomenclatureNameMap.put("nom_european_qualifications_framework", "Еропейска квалификационна рамка|Европейски квалификационни рамки");
        groupNameToNomenclatureNameMap.put("nom_graduation_document_type", "Тип на документа|Типове документи");
        groupNameToNomenclatureNameMap.put("nom_grade", "Клас|Класове");
        groupNameToNomenclatureNameMap.put("nom_school_type", "Вид училище|Видове училища");
        groupNameToNomenclatureNameMap.put("nom_age_range", "Възрастова група|Възрастови групи");
        groupNameToNomenclatureNameMap.put("nom_personaliddoctype", "Тип на документа за самоличност|Типове на документа за самоличност");
//        groupNameToNomenclatureNameMap.put("nom_document_receive_method", "Начин на получаване на уведомление|Начин на получаване на уведомление");
        groupNameToNomenclatureNameMap.put("nom_university_generic_name", "Генерично намименование|Генерични наименования");
        groupNameToNomenclatureNameMap.put("nom_language", "Език|Езици");
    }

    protected static String getGroupNameToNomenclatureName(String group, boolean forList) {
        return groupNameToNomenclatureNameMap.get(group).split("\\|")[forList ? 1 : 0];
    }

    public FlatNomenclatureHandler(ServletContext servletContext) {
        this(servletContext, "flat_nomenclature_edit");
    }
    protected FlatNomenclatureHandler(ServletContext servletContext, String editNomenclatureScreen) {
        super(servletContext);
        this.editNomenclatureScreen = editNomenclatureScreen;
    }
    public static void refreshCachedNomenclatures(HttpServletRequest request){
        URL url;
        List<String> apps = new ArrayList<String>();
        apps.add("nacid");
        apps.add("nacid_regprof");
        apps.add("nacid_ext");
        apps.add("nacid_regprof_ext");
        try {   
            for(String a: apps){
                if (("/" + a).equals(request.getContextPath())) {
                    continue;
                }
                url = new URL(request.getScheme() + "://" + request.getServerName() + ":"+request.getServerPort()+"/"+ a +"/rest/nomenclatures/clear");
                URLConnection connection = url.openConnection();
                String auth = REST_USER + ":" + REST_PASS;
                byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
                String authHeaderValue = "Basic " + new String(encodedAuth);
                connection.setRequestProperty("Authorization", authHeaderValue);
                String response = IOUtils.toString(connection.getInputStream());
                log.debug("Nomenclatures for " + url + " cleared! Response:" + response);
            }
        } catch (Exception e) {
            Utils.logException(e);
        }
    }
    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
            new HomePageHandler(request.getSession().getServletContext()).processRequest(request, response);
            return;
        }
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        int id = DataConverter.parseInt(request.getParameter("id"), 0);

        String name = request.getParameter("name").trim();
        Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));

        // If new record is added and dateFrom is not set, then dateFrom is set
        // to today
        if (id == 0 && dateFrom == null) {
            dateFrom = Utils.getToday();
        }
        Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
        String groupName = getGroupName(request);
        Integer nomenclatureId = groupNameToNomenclatureIdMap.get(groupName);
        if (nomenclatureId == null) {
            throw new UnknownRecordException("Unknown nomenclature name!" + id);
        }

        if (id != 0 && nomenclaturesDataProvider.getFlatNomenclature(nomenclatureId, id) == null) {
            throw new UnknownRecordException("Unknown Commission Position ID:" + id);
        }

        if (name == null || "".equals(name)) {
            SystemMessageWebModel webmodel = new SystemMessageWebModel("Грешно въведени данни!");
            webmodel.addAttribute("- грешно въведено име!");
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
            request.setAttribute(WebKeys.FLAT_NOMENCLATURE, new FlatNomenclatureWebModel(id, name, request.getParameter("dateFrom"), request
                    .getParameter("dateTo"), groupName, getGroupNameToNomenclatureName(groupName, false), nomenclatureId));
        } else {
            boolean saveNomenclature = true;
            if (id == 0) {
                FlatNomenclature flatNomenclature = nomenclaturesDataProvider.getFlatNomenclature(nomenclatureId, name);
                if (flatNomenclature != null) {
                    saveNomenclature = false;
                    request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Съществува номенклатура със същото име в базата данни", SystemMessageWebModel.MESSAGE_TYPE_ERROR));
                    request.setAttribute(WebKeys.FLAT_NOMENCLATURE, new FlatNomenclatureWebModel(groupName, getGroupNameToNomenclatureName(groupName, false), nomenclatureId));
                }
            }
            if (saveNomenclature) {
                int newId = nomenclaturesDataProvider.saveFlatNomenclature(nomenclatureId, id, name, dateFrom, dateTo);
                refreshCachedNomenclatures(request);
                request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
                //resetTableData(request, groupName, nomenclatureId);
                request.setAttribute(WebKeys.FLAT_NOMENCLATURE, new FlatNomenclatureWebModel(nomenclaturesDataProvider.getFlatNomenclature(
                        nomenclatureId, newId), groupName, getGroupNameToNomenclatureName(groupName, false)));
            }
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, editNomenclatureScreen);
        request.getSession().removeAttribute(WebKeys.TABLE_FLAT_NOMENCLATURE + groupName);        

    }

    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        String groupName = getGroupName(request);
        Integer nomenclatureId = groupNameToNomenclatureIdMap.get(groupName);
        if (nomenclatureId == null) {
            throw new UnknownRecordException("Unknown nomenclature name!" + groupName);
        }
        request.setAttribute(WebKeys.FLAT_NOMENCLATURE, new FlatNomenclatureWebModel(groupName, getGroupNameToNomenclatureName(groupName, false), nomenclatureId));
        request.setAttribute(WebKeys.NEXT_SCREEN, editNomenclatureScreen);
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        Integer flatNomenclatureId = DataConverter.parseInteger(request.getParameter("id"), null);
        if (flatNomenclatureId == null) {
            throw new UnknownRecordException("Unknown FlatNomenclature ID:" + flatNomenclatureId + " NomenclatureName:" + getGroupName(request));
        }
        String groupName = getGroupName(request);
        Integer nomenclatureId = groupNameToNomenclatureIdMap.get(groupName);
        if (nomenclatureId == null) {
            throw new UnknownRecordException("Unknown nomenclature name!" + groupName);
        }

        request.setAttribute(WebKeys.NEXT_SCREEN, editNomenclatureScreen);
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        FlatNomenclature flatNomenclature = nomenclaturesDataProvider.getFlatNomenclature(nomenclatureId, flatNomenclatureId);
        if (flatNomenclature == null) {
            throw new UnknownRecordException("Unknown FlatNomenclature ID:" + flatNomenclatureId + " NomenclatureName:" + getGroupName(request));
        }
        request.setAttribute(WebKeys.FLAT_NOMENCLATURE, new FlatNomenclatureWebModel(flatNomenclature, groupName, getGroupNameToNomenclatureName(groupName, false)));
    }

    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        String groupName = getGroupName(request);
        Integer nomenclatureId = groupNameToNomenclatureIdMap.get(groupName);
        if (nomenclatureId == null) {
            throw new UnknownRecordException("Unknown nomenclature name!" + groupName);
        }
        generateTable(request, groupName, nomenclatureId);
    }

    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        String groupName = getGroupName(request);
        Integer flatNomenclatureId = DataConverter.parseInteger(request.getParameter("id"), null);
        if (flatNomenclatureId == null) {
            throw new UnknownRecordException("Unknown Flat Nomenclature ID:" + flatNomenclatureId + " for nomenclature name:" + groupName);
        }

        Integer nomenclatureId = groupNameToNomenclatureIdMap.get(groupName);
        if (nomenclatureId == null) {
            throw new UnknownRecordException("Unknown nomenclature name!" + groupName);
        }
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        FlatNomenclature flatNomenclature = nomenclaturesDataProvider.getFlatNomenclature(nomenclatureId, flatNomenclatureId);
        if (flatNomenclature == null) {
            throw new UnknownRecordException("Unknown FlatNomenclature ID:" + flatNomenclatureId + " NomenclatureName:" + getGroupName(request));
        }
        nomenclaturesDataProvider.saveFlatNomenclature(nomenclatureId, flatNomenclature.getId(), flatNomenclature.getName(), flatNomenclature
                .getDateFrom(), new Date());
        request.getSession().removeAttribute(WebKeys.TABLE_FLAT_NOMENCLATURE + groupName);
        handleList(request, response);
    }

    private void generateTable(HttpServletRequest request, String groupName, int nomenclatureId) {
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_FLAT_NOMENCLATURE + groupName);
        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DATE_FROM, CellValueDef.CELL_VALUE_TYPE_DATE);
            table.addColumnHeader(COLUMN_NAME_DATE_TO, CellValueDef.CELL_VALUE_TYPE_DATE);

            session.setAttribute(WebKeys.TABLE_FLAT_NOMENCLATURE + groupName, table);
            resetTableData(request, groupName, nomenclatureId);

        }

        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_FLAT_NOMENCLATURE + groupName + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
         * vzeme posledniq tableState, togava se generira nov tableState!
         */
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);
            session.setAttribute(WebKeys.TABLE_FLAT_NOMENCLATURE + groupName + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel(getGroupNameToNomenclatureName(groupName, true));
        // webmodel.setColumnFormatter("userDate",
        // CellFormatter.DATE_TIME_FORMATTER);
        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_FLAT_NOMENCLATURE + groupName
                + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();
            filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));
            session.setAttribute(WebKeys.TABLE_FLAT_NOMENCLATURE + groupName + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);

    }

    private void resetTableData(HttpServletRequest request, String groupName, int nomenclatureId) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_FLAT_NOMENCLATURE + groupName);

        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        List<FlatNomenclature> flatNomenclatures = nomenclaturesDataProvider.getFlatNomenclatures(nomenclatureId, null, OrderCriteria
                .createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, false));

        if (flatNomenclatures != null) {
            for (FlatNomenclature cp : flatNomenclatures) {
                try {
                    table.addRow(cp.getId(), cp.getName(), cp.getDateFrom(), cp.getDateTo());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }

    public static void main(String[] args) {
        Table table = TableFactory.getInstance().createTable();
        table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
        table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_DATE_FROM, CellValueDef.CELL_VALUE_TYPE_DATE);
        table.addColumnHeader(COLUMN_NAME_DATE_TO, CellValueDef.CELL_VALUE_TYPE_DATE);

        TableState tableState = TableFactory.getInstance().createTableState();
        tableState.setOrderCriteria(COLUMN_NAME_DATE_TO, true);
        tableState.setOrdered(true);
        StandAloneDataSource standAloneDataSource = new StandAloneDataSource();
        NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(standAloneDataSource);
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        List<FlatNomenclature> flatNomenclatures = nomenclaturesDataProvider.getFlatNomenclatures(
                NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_AREA, null, null);

        if (flatNomenclatures != null) {
            for (FlatNomenclature cp : flatNomenclatures) {
                try {
                    table.addRow(cp.getId(), cp.getName(), cp.getDateFrom(), cp.getDateTo());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
        List<TableRow> tableRows = table.getRows(tableState);
        for (TableRow r : tableRows) {
            for (TableCell c : r.getCells()) {
                System.out.print(c.getCellValue().getValue() + "\t");
            }
            System.out.println();
        }
        // System.out.println(table.getRows(tableState));
    }

}
