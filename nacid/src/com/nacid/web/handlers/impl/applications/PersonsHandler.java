package com.nacid.web.handlers.impl.applications;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.bl.applications.Person;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.table.TableState;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.applications.PersonWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.nomenclatures.FlatNomenclatureWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import com.nacid.web.model.table.filters.TextFieldFilterWebModel;

public class PersonsHandler extends NacidBaseRequestHandler {

    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_NAME = "Име";
    private static final String COLUMN_NAME_SNAME = "Презиме";
    private static final String COLUMN_NAME_LNAME = "Фамилия";
    private static final String COLUMN_NAME_PID = "Персонален идентификатор";
    private static final String COLUMN_NAME_PID_TYPE = "Тип персонален идентификатор";
    private static final String COLUMN_NAME_CITIZENSHIP = "Гражданство";
    //private static final String COLUMN_NAME_CITY = "Град";

    private static final String FILTER_NAME = "fname";
    private static final String FILTER_SNAME = "sname";
    private static final String FILTER_LNAME = "lname";
    private static final String FILTER_EGN = "egn";

    public PersonsHandler(ServletContext servletContext) {
        super(servletContext);
    }

    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nDP = nacidDataProvider.getNomenclaturesDataProvider();

        generateCivilIdTypes(nDP, request);
        generateCountryComboBox(NomenclaturesDataProvider.COUNTRY_ID_BULGARIA, nDP, request);
        generateCitizenshipComboBox(null, nDP, request);
        request.setAttribute(WebKeys.PERSON_WEB_MODEL, new PersonWebModel(null));
        request.setAttribute(WebKeys.NEXT_SCREEN, "person_edit");
    }

    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {

        int personId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (personId <= 0) {
            throw new UnknownRecordException("Unknown Member ID:" + personId);
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, "person_edit");
        NacidDataProvider nacidDataProvider = getNacidDataProvider();

        ApplicationsDataProvider appDP = nacidDataProvider.getApplicationsDataProvider();
        NomenclaturesDataProvider nomDP = nacidDataProvider.getNomenclaturesDataProvider();

        Person person = appDP.getPerson(personId);

        if (person == null) {
            throw new UnknownRecordException("Unknown Member ID:" + person);
        }
        generateCountryComboBox(person.getBirthCountryId(), nomDP, request);
        generateCitizenshipComboBox(person.getCitizenshipId(), nomDP, request);
        generateCivilIdTypes(nomDP, request);
        request.setAttribute(WebKeys.PERSON_WEB_MODEL, new PersonWebModel(person));

    }

    public void handleList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_PERSON);

        boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();
            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_SNAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_LNAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_PID_TYPE, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_PID, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_CITIZENSHIP, CellValueDef.CELL_VALUE_TYPE_STRING);
            //table.addColumnHeader(COLUMN_NAME_CITY, CellValueDef.CELL_VALUE_TYPE_STRING);

            session.setAttribute(WebKeys.TABLE_PERSON, table);
            resetTableData(request);

        }

        // TableState settings
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_PERSON + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
         * vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);

            TableStateAndFiltersUtils.addStartsWithFilterToTableState(FILTER_NAME, COLUMN_NAME_NAME, request, table, tableState);
            TableStateAndFiltersUtils.addStartsWithFilterToTableState(FILTER_SNAME, COLUMN_NAME_SNAME, request, table, tableState);
            TableStateAndFiltersUtils.addStartsWithFilterToTableState(FILTER_LNAME, COLUMN_NAME_LNAME, request, table, tableState);
            TableStateAndFiltersUtils.addStartsWithFilterToTableState(FILTER_EGN, COLUMN_NAME_PID, request, table, tableState);
            /*
             * TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_PROF_GROUP_NAME
             * , COLUMN_NAME_PROF_GROUP_NAME, request, table, tableState);
             * TableStateAndFiltersUtils.addToDateFilterToTableState(request,
             * table, tableState);
             */

            session.setAttribute(WebKeys.TABLE_PERSON + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък на заявители");
        // webmodel.setColumnFormatter("userDate",
        // CellFormatter.DATE_TIME_FORMATTER);
        webmodel.setGroupName(getGroupName(request));
        webmodel.insertTableData(table, tableState);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_PERSON + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();

            TextFieldFilterWebModel tfFWB = new TextFieldFilterWebModel(FILTER_NAME, COLUMN_NAME_NAME, request.getParameter(FILTER_NAME));
            tfFWB.setLabelOnTop(true);
            tfFWB.setElementClass("brd");
            filtersWebModel.addFiler(tfFWB);

            tfFWB = new TextFieldFilterWebModel(FILTER_SNAME, COLUMN_NAME_SNAME, request.getParameter(FILTER_SNAME));
            tfFWB.setLabelOnTop(true);
            tfFWB.setElementClass("brd");
            filtersWebModel.addFiler(tfFWB);

            tfFWB = new TextFieldFilterWebModel(FILTER_LNAME, COLUMN_NAME_LNAME, request.getParameter(FILTER_LNAME));
            tfFWB.setLabelOnTop(true);
            tfFWB.setElementClass("brd");
            filtersWebModel.addFiler(tfFWB);

            tfFWB = new TextFieldFilterWebModel(FILTER_EGN, COLUMN_NAME_PID, request.getParameter(FILTER_EGN));
            tfFWB.setLabelOnTop(true);
            tfFWB.setElementClass("brd");
            filtersWebModel.addFiler(tfFWB);

            session.setAttribute(WebKeys.TABLE_PERSON + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }

    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
            new HomePageHandler(request.getSession().getServletContext()).processRequest(request, response);
            return;
        }

        int id = DataConverter.parseInt(request.getParameter("applicant_record_id"), 0);
        String applicantFirst = request.getParameter("applicant_first_name");
        String applicantMiddleName = request.getParameter("applicant_middle_name");
        String applicantLastName = request.getParameter("applicant_last_name");
        String applicantCivilId = request.getParameter("applicant_personal_id");
        int applicantCivilIdType = DataConverter.parseInt(request.getParameter("applicant_personal_id_type"), -1);
        if (applicantCivilIdType == -1) {
            throw new UnknownRecordException("Unknown civilIdType ID:" + applicantCivilIdType);
        }
        Integer applicantBirthCountryId = DataConverter.parseInteger(request.getParameter("applicant_birth_place_country"), null);
        /*if (applicantBirthCountryId == -1) {
            throw new UnknownRecordException("Unknown applicant birth country ID:" + applicantBirthCountryId);
        }*/
        String applicantBirthCity = request.getParameter("applicant_birth_place_location");
        Integer citizenshipId = DataConverter.parseInteger(
                request.getParameter("applicantCitizenshipId"), null);
        Date birthDate = DataConverter.parseDate(request.getParameter("applicantBirthDate"));
        
        ApplicationsDataProvider appDP = getNacidDataProvider().getApplicationsDataProvider();

        if (id != 0 && appDP.getPerson(id) == null) {
            throw new UnknownRecordException("Unknown person ID:" + id);
        }

        if (false) {
            /*
             * SystemMessageWebModel webmodel = new SystemMessageWebModel(
             * "грешка"); request.setAttribute(WebKeys.SYSTEM_MESSAGE,
             * webmodel);
             * request.setAttribute(WebKeys.COMISSION_MEMBER_WEB_MODEL, new
             * ComissionMemberWebModel( id, fname, sname, lname, degree,
             * institution, division, title, egn, homeCity, homeAddress,
             * homePcode, phone, email, gsm, iban, bic,
             * request.getParameter("date_from"),
             * request.getParameter("date_to")));
             */
        } else {

            int newId = appDP.savePerson(id, applicantFirst, applicantMiddleName, applicantLastName, applicantCivilId, applicantCivilIdType,
                    applicantBirthCountryId, applicantBirthCity, birthDate, citizenshipId);

            request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата",
                    SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
            resetTableData(request);
            request.setAttribute(WebKeys.PERSON_WEB_MODEL, new PersonWebModel(appDP.getPerson(newId)));
        }
        NomenclaturesDataProvider nomDP = getNacidDataProvider().getNomenclaturesDataProvider();

        generateCountryComboBox(applicantBirthCountryId, nomDP, request);
        generateCitizenshipComboBox(citizenshipId, nomDP, request);
        generateCivilIdTypes(nomDP, request);

        request.setAttribute(WebKeys.NEXT_SCREEN, "person_edit");
        request.getSession().removeAttribute(WebKeys.TABLE_PERSON);

    }

    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        int personId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (personId <= 0) {
            throw new UnknownRecordException("Unknown Person ID:" + personId);
        }
        ApplicationsDataProvider appDP = getNacidDataProvider().getApplicationsDataProvider();
        boolean result = appDP.deletePerson(personId);
        if (result) {
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Записът е изтрит.", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        } else {
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Записът се използва и не може да бъде изтрит.",
                    SystemMessageWebModel.MESSAGE_TYPE_ERROR));
        }
        request.getSession().removeAttribute(WebKeys.TABLE_PERSON);
        handleList(request, response);
    }

    private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_PERSON);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ApplicationsDataProvider appDP = nacidDataProvider.getApplicationsDataProvider();
        

        List<Person> persons = appDP.getPersons();

        if (persons != null) {
            for (Person p : persons) {

                FlatNomenclature pidType = p.getCivilIdType();
                Country country = p.getCitizenship();

                try {
                    table.addRow(p.getId(), p.getFName(), p.getSName(), p.getLName(), pidType == null ? "-" : pidType.getName(), p.getCivilId(),
                            country == null ? "-" : country.getName()/*, p.getBirthCity()*/);
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }

    private static void generateCountryComboBox(Integer activeCountryId, NomenclaturesDataProvider nomDP, HttpServletRequest request) {
        ComboBoxWebModel combobox = new ComboBoxWebModel(activeCountryId == null ? null : activeCountryId + "", true);
        List<Country> countries = nomDP.getCountries(Utils.getToday(), OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE,
                NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true));
        if (countries != null) {
            for (Country c : countries) {
                combobox.addItem(c.getId() + "", c.getName());
            }
            request.setAttribute("applicantBirthCountry", combobox);
        }
    }
    
    private static void generateCitizenshipComboBox(Integer activeCountryId, NomenclaturesDataProvider nomDP, HttpServletRequest request) {
        ComboBoxWebModel combobox = new ComboBoxWebModel(activeCountryId != null ? activeCountryId.toString() : "-", true);
        List<Country> countries = nomDP.getCountries(Utils.getToday(), OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE,
                NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true));
        if (countries != null) {
            for (Country c : countries) {
                combobox.addItem(c.getId() + "", c.getName());
            }
            request.setAttribute("applicantCitizenshipId", combobox);
        }
    }

    private static void generateCivilIdTypes(NomenclaturesDataProvider nomenclaturesDataProvider, HttpServletRequest request) {
        List<FlatNomenclature> civilIdTypes = nomenclaturesDataProvider.getFlatNomenclatures(
                NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, null, OrderCriteria.createOrderCriteria(
                        OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, true));
        List<FlatNomenclatureWebModel> civilIdTypesWebModel = new ArrayList<FlatNomenclatureWebModel>();
        if (civilIdTypes != null) {
            for (FlatNomenclature fn : civilIdTypes) {
                civilIdTypesWebModel.add(new FlatNomenclatureWebModel(fn, null, null));
            }
            request.setAttribute(WebKeys.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, civilIdTypesWebModel);
        }
    }
}
