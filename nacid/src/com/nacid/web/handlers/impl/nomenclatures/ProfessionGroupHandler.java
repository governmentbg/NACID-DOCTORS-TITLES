package com.nacid.web.handlers.impl.nomenclatures;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.ProfessionGroup;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.table.TableState;
import com.nacid.data.DataConverter;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.nomenclatures.ProfessionGroupWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.ComboBoxFilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;


public class ProfessionGroupHandler extends NacidBaseRequestHandler {
  private static final String COLUMN_NAME_ID = "id";
  private static final String COLUMN_NAME_NAME = "Наименование";
  private static final String COLUMN_NAME_AREA_NAME = "Област на образование";
  private static final String COLUMN_NAME_AREA_ID = "Област на образование_id";
  private static final String COLUMN_NAME_DATE_FROM = "От дата";
  private static final String COLUMN_NAME_DATE_TO = "До дата";
  
  private static final String FILTER_NAME_AREA_NAME = "areaNameFilter";
  public ProfessionGroupHandler(ServletContext servletContext) {
    super(servletContext);
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
    String name = request.getParameter("name");
    int eduAreaId = DataConverter.parseInt(request.getParameter("edu_area_id"), -1);
    Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
    
    //If new record is added and dateFrom is not set, then dateFrom is set to today
    if (id == 0 && dateFrom == null) {
        dateFrom = Utils.getToday();
    }
    Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
    if (id != 0 && nomenclaturesDataProvider.getProfessionGroup(id) == null) {
      throw new UnknownRecordException("Unknown ProfessionGroup ID:" + id);
    }
    if (name == null || "".equals(name)) {
      SystemMessageWebModel webmodel = new SystemMessageWebModel("Грешно въведени данни!");
      webmodel.addAttribute("- грешно въведено име!");
      request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
      request.setAttribute(WebKeys.PROFESSION_GROUP, new ProfessionGroupWebModel(id, name, request.getParameter("dateFrom"), request.getParameter("dateTo")));
    } else {
      int newId = nomenclaturesDataProvider.saveProfessionGroup(id, name, eduAreaId, dateFrom, dateTo);
      FlatNomenclatureHandler.refreshCachedNomenclatures(request);
      request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
      request.setAttribute(WebKeys.PROFESSION_GROUP, new ProfessionGroupWebModel(nomenclaturesDataProvider.getProfessionGroup(newId)));
    }
    generateEducationAreaCheckBox(eduAreaId, nomenclaturesDataProvider, request);
    request.setAttribute(WebKeys.NEXT_SCREEN, "profession_group_edit");
    request.getSession().removeAttribute(WebKeys.TABLE_PROFESSION_GROUP);
    
    
  }

  @Override
	public void handleNew(HttpServletRequest request, HttpServletResponse response) {
    //request.setAttribute(WebKeys.EDUCATION_AREA, new EducationAreaWebModel("new"));
    generateEducationAreaCheckBox(0, getNacidDataProvider().getNomenclaturesDataProvider(), request);
    request.setAttribute(WebKeys.NEXT_SCREEN, "profession_group_edit");
	}
  
  @Override
	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
    int profGroupId = DataConverter.parseInt(request.getParameter("id"), -1);
    if (profGroupId <= 0) {
      throw new UnknownRecordException("Unknown ProfessionGroup ID:" + profGroupId);
    }
    request.setAttribute(WebKeys.NEXT_SCREEN, "profession_group_edit");
    NacidDataProvider nacidDataProvider = getNacidDataProvider();
    NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
    ProfessionGroup professionGroup = nomenclaturesDataProvider.getProfessionGroup(profGroupId);
    
    
    if (professionGroup == null) {
      throw new UnknownRecordException("Unknown ProfessionGroup ID:" + profGroupId);
    }
    generateEducationAreaCheckBox(professionGroup.getEducationAreaId() , nomenclaturesDataProvider, request);
    request.setAttribute(WebKeys.PROFESSION_GROUP, new ProfessionGroupWebModel(professionGroup));
	}
  
  public void handleList(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession();
    Table table = (Table) session.getAttribute(WebKeys.TABLE_PROFESSION_GROUP);
    boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
    /**
     * ako e settnat parametera reloadTable ili nqma tablica v sesiqta
     */
    if (reloadTable || table == null) {
      TableFactory tableFactory = TableFactory.getInstance();
      table = tableFactory.createTable();
      
      table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
      table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
      table.addColumnHeader(COLUMN_NAME_AREA_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
      table.addColumnHeader(COLUMN_NAME_AREA_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
      table.addColumnHeader(COLUMN_NAME_DATE_FROM, CellValueDef.CELL_VALUE_TYPE_DATE);
      table.addColumnHeader(COLUMN_NAME_DATE_TO, CellValueDef.CELL_VALUE_TYPE_DATE);
      
      session.setAttribute(WebKeys.TABLE_PROFESSION_GROUP, table);
      resetTableData(request);
        
    }
    
    TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_PROFESSION_GROUP + WebKeys.TABLE_STATE);
    /**
     * ako tableState-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState, togava se generira nov tableState!
     */
    boolean reloadTableState = RequestParametersUtils.getParameterGetLastTableState(request);
    if (tableState == null || !reloadTableState) {
      tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
      TableStateAndFiltersUtils.addComboFilterToTableState(FILTER_NAME_AREA_NAME, COLUMN_NAME_AREA_ID, request, table, tableState);
      TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);
      session.setAttribute(WebKeys.TABLE_PROFESSION_GROUP + WebKeys.TABLE_STATE, tableState);
    }
    
    //TABLE_USERS WEB MODEL SETTINGS
    TableWebModel webmodel = new TableWebModel("Професионални направления");
    //webmodel.setColumnFormatter("userDate", CellFormatter.DATE_TIME_FORMATTER);
    webmodel.setGroupName(getGroupName(request));
    webmodel.insertTableData(table, tableState);
    webmodel.hideUnhideColumn(COLUMN_NAME_AREA_ID, true);
    request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
    request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");
    
    //Generating filters for displaying to user
    FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_PROFESSION_GROUP + WebKeys.FILTER_WEB_MODEL);
    /**
     * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState(filterWebModel), togava se generira nov i se slaga v sesiqta!
     */
    if (filtersWebModel == null || !reloadTableState) {
      filtersWebModel = new FiltersWebModel();
      filtersWebModel.addFiler(generateAreaNameFilterComboBox(request.getParameter(FILTER_NAME_AREA_NAME), getNacidDataProvider().getNomenclaturesDataProvider(), request));
      filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));
      session.setAttribute(WebKeys.TABLE_PROFESSION_GROUP + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
    }
    request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);

  }
  public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
    int professionGroupId = DataConverter.parseInt(request.getParameter("id"), -1);
    if (professionGroupId <= 0) {
      throw new UnknownRecordException("Unknown ProfessionGroup ID:" + professionGroupId);
    }
    NacidDataProvider nacidDataProvider = getNacidDataProvider();
    NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
    ProfessionGroup professionGroup = nomenclaturesDataProvider.getProfessionGroup(professionGroupId);
    if (professionGroup == null) {
      throw new UnknownRecordException("Unknown ProfessionGroup ID:" + professionGroupId);
    }
    nomenclaturesDataProvider.saveProfessionGroup(professionGroup.getId(), professionGroup.getName(), professionGroup.getEducationAreaId(), professionGroup.getDateFrom(), Utils.getToday());
    FlatNomenclatureHandler.refreshCachedNomenclatures(request);
    request.getSession().removeAttribute(WebKeys.TABLE_PROFESSION_GROUP);
    handleList(request, response);
  }
  
  
  private void resetTableData(HttpServletRequest request) {
    Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_PROFESSION_GROUP);
    if (table == null) {
      return;
    }
    table.emtyTableData();
    NacidDataProvider nacidDataProvider = getNacidDataProvider();
    NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
    List<ProfessionGroup> professionGroups = nomenclaturesDataProvider.getProfessionGroups(0, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, false));

    if (professionGroups != null) {
      for (ProfessionGroup pg:professionGroups) {
        try {
          table.addRow(pg.getId(), pg.getName(), pg.getEducationAreaName(), pg.getEducationAreaId(), pg.getDateFrom(),  pg.getDateTo());
        } catch (IllegalArgumentException e) {
            throw Utils.logException(e);
        } catch (CellCreationException e) {
            throw Utils.logException(e);
        }
      }
    }
  }
  private void generateEducationAreaCheckBox(int eduAreaId, NomenclaturesDataProvider nomenclaturesDataProvider, HttpServletRequest request) {
    /**
     * popylvane na education area combo box-a
     */
    ComboBoxWebModel combobox = new ComboBoxWebModel(eduAreaId + "");
    List<FlatNomenclature> educationAreas = nomenclaturesDataProvider.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_AREA, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true));
    if (educationAreas != null) {
      for (FlatNomenclature ea:educationAreas) {
        combobox.addItem(ea.getId() + "", ea.getName() + (ea.isActive() ? "" : " (inactive)"));
      }
      request.setAttribute(WebKeys.COMBO, combobox);
    }
  }
  
  private static ComboBoxFilterWebModel generateAreaNameFilterComboBox(String activeProfessionGroupName, NomenclaturesDataProvider nomenclaturesDataProvider, HttpServletRequest request) {
    ComboBoxWebModel combobox = new ComboBoxWebModel(activeProfessionGroupName, true);
    
    List<FlatNomenclature> educationAreas = nomenclaturesDataProvider.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_AREA, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true));
    if (educationAreas != null) {
      for (FlatNomenclature ea:educationAreas) {
        combobox.addItem(ea.getId() + "", ea.getName() + (ea.isActive() ? "" : " (inactive)"));
      }
    }
    ComboBoxFilterWebModel res = new ComboBoxFilterWebModel(combobox, FILTER_NAME_AREA_NAME, "Област на образованието:");
    res.setElementClass("brd");
    return res;
  }
  public static void main(String[] args) {
      NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
      List<FlatNomenclature> fns = nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_AREA, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true));
      for (FlatNomenclature fn:fns) {
          System.out.println(fn.getName());
      }
      
      
  }
  
  
}
