package com.nacid.web.handlers.impl.users;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.table.*;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UserUpdater;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import com.nacid.web.model.users.UserWebModel;
import com.nacid.web.taglib.users.UserGroupMembershipHelper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


public class UsersHandler extends NacidBaseRequestHandler {
  private static final String COLUMN_NAME_ID = "id";
  private static final String COLUMN_NAME_SHORT_NAME = "кратко име";
  private static final String COLUMN_NAME_FULL_NAME = "пълно име";
  private static final String COLUMN_NAME_USER_NAME = "потребителско име";
  private static final String COLUMN_NAME_STATE = "активен";
  private static final String COLUMN_NAME_EMAIL = "email";
  private static final String COLUMN_NAME_PHONE = "тел.";
  
  private static final String FILTER_NAME_ONLY_ACTIVE = "onlyActive";
  public static final Set<Integer> SUPPORTED_APPS = new HashSet<Integer>(Arrays.asList(NacidDataProvider.APP_NACID_ID, NacidDataProvider.APP_NACID_REGPROF_ID));
  public UsersHandler(ServletContext servletContext) {
    super(servletContext);
  }

  
  @Override
  public void handleSave(HttpServletRequest request, HttpServletResponse response) {
    if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
      new HomePageHandler(request.getSession().getServletContext()).processRequest(request, response);
      return;
    }
    int userId = DataConverter.parseInteger(request.getParameter("id"), 0);
    UsersDataProvider usersDataProvider = getNacidDataProvider().getUsersDataProvider();
    UserUpdater user = userId == 0 ? null : usersDataProvider.getUserForEdit(userId);
    if (userId != 0 && user == null) {
      throw new UnknownRecordException("Unknown user ID:" + userId); 
    }
    Map<Integer, Map<Integer, Set<Integer>>> operations = new HashMap<Integer, Map<Integer, Set<Integer>>>();
  
    for (Integer app: SUPPORTED_APPS) {
        Map<Integer, Set<Integer>> m = new HashMap<Integer, Set<Integer>>();
        operations.put(app, m);
        for (Integer i:UserGroupMembershipHelper.operationToNameMap.keySet()) {
            String[] elements = request.getParameterValues("app" + app + "operation_" + i);
            if (elements != null && elements.length > 0) {
              Set<Integer> ops = new LinkedHashSet<Integer>();
              for (String e:elements) {
                Integer x = DataConverter.parseInteger(e, null);
                if (x != null) {
                  ops.add(x);
                }
              }
              m.put(i, ops);
            }
          }
    }

      usersDataProvider.updateUserOperations(userId, operations);
      request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
      resetTableData(request);
      user = usersDataProvider.getUserForEdit(userId);
      request.setAttribute(WebKeys.USER, new UserWebModel(user));
      request.setAttribute(WebKeys.NEXT_SCREEN, "user_edit");
  }

  @Override
	public void handleNew(HttpServletRequest request, HttpServletResponse response) {
      NacidDataProvider nacidDataProvider = getNacidDataProvider();
      UsersDataProvider usersDataProvider = nacidDataProvider.getUsersDataProvider();

      List<? extends User> externalUsers = usersDataProvider.getUsers(0, 0, 0, NacidDataProvider.APP_NACID_EXT_ID, NacidDataProvider.APP_NACID_REGPROF_EXT_ID);
      List<? extends User> internalUsers = usersDataProvider.getUsers(0, 0, 0, NacidDataProvider.APP_NACID_ID, NacidDataProvider.APP_NACID_REGPROF_ID);
      List<User> users = new ArrayList<User>();
      //proverqva dali dadeniq external user syshtestvuva v spisyka s internal users. Ako da, to ne go dobavq v komboto. V nego trqbva da vlqzat vshichki vyn6ni potrebiteli, koito ne sa i vytreshni!
      for (User user : externalUsers) {
          boolean exists = false;
          for (User internalUser : internalUsers) {
              if (user.getUserId() == internalUser.getUserId()) {
                  exists = true;
                  break;
              }
          }
          if (!exists) {
              users.add(user);
          }
      }


      generateUsersComboBox(null, users, request, "externalUsers", true, true);







      request.setAttribute(WebKeys.NEXT_SCREEN, "user_edit");
	}
  
  @Override
	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
    Integer userId = DataConverter.parseInteger(request.getParameter("id"), null);
    if (userId == null) {
      throw new UnknownRecordException("Unknown User ID:" + userId );
    }
    
    request.setAttribute(WebKeys.NEXT_SCREEN, "user_edit");
    NacidDataProvider nacidDataProvider = getNacidDataProvider();
    UsersDataProvider usersDataProvider = nacidDataProvider.getUsersDataProvider();
    UserUpdater user = usersDataProvider.getUserForEdit(userId);
    if (user == null) {
      throw new UnknownRecordException("Unknown User for User ID:" + userId);
    }
    request.setAttribute(WebKeys.USER, new UserWebModel(user));
	}
  
  public void handleList(HttpServletRequest request, HttpServletResponse response) {
    generateTable(request);
  }
  
  public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
    throw new UnsupportedOperationException("Operation Delete is not implemented yet");
    /*int flatNomenclatureId = DataConverter.parseInt(request.getParameter("id"), -1);
    String groupName = getGroupName(request);
    if (flatNomenclatureId <= 0) {
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
    nomenclaturesDataProvider.saveFlatNomenclature(nomenclatureId, flatNomenclature.getId(), flatNomenclature.getName(), flatNomenclature.getDateFrom(), new Date());
    request.getSession().removeAttribute(WebKeys.TABLE_FLAT_NOMENCLATURE + groupName);
    handleList(request, response);*/
  }
  
  private void generateTable(HttpServletRequest request) {
    HttpSession session = request.getSession();
    Table table = (Table) session.getAttribute(WebKeys.TABLE_USERS);
    boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
    if (reloadTable || table == null) {
      TableFactory tableFactory = TableFactory.getInstance();
      table = tableFactory.createTable();
      table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
      table.addColumnHeader(COLUMN_NAME_USER_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
      table.addColumnHeader(COLUMN_NAME_FULL_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
      table.addColumnHeader(COLUMN_NAME_EMAIL, CellValueDef.CELL_VALUE_TYPE_STRING);

      session.setAttribute(WebKeys.TABLE_USERS, table);
      resetTableData(request);
        
    }
    
    boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
    TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_USERS + WebKeys.TABLE_STATE);
    /**
     * ako tableState-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState, togava se generira nov tableState!
     */
    if (tableState == null || !getLastTableState) {
      tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
      TableStateAndFiltersUtils.addEqualsFilterToTableState(FILTER_NAME_ONLY_ACTIVE, 
              COLUMN_NAME_STATE, request, table, tableState);
      session.setAttribute(WebKeys.TABLE_USERS + WebKeys.TABLE_STATE, tableState);
    }
    
    //TableWebModel SETTINGS
    TableWebModel webmodel = new TableWebModel("Потребители");
    //webmodel.setColumnFormatter("userDate", CellFormatter.DATE_TIME_FORMATTER);
    webmodel.setGroupName(getGroupName(request));
    webmodel.insertTableData(table, tableState);
    webmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
    webmodel.hideOperation(TableWebModel.OPERATION_NAME_NEW);
    request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
    request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");
    
    //Generating filters for displaying to user
    FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_USERS + WebKeys.FILTER_WEB_MODEL);
    /**
     * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState(filterWebModel), togava se generira nov i se slaga v sesiqta!
     */
    if (filtersWebModel == null || !getLastTableState) {
      filtersWebModel = new FiltersWebModel();
    }
    request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    
  }
  
  private void resetTableData(HttpServletRequest request) {
    Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_USERS);
    
    if (table == null) {
      return;
    }
    table.emtyTableData();
    NacidDataProvider nacidDataProvider = getNacidDataProvider();
    UsersDataProvider usersDataProvider = nacidDataProvider.getUsersDataProvider();
    List<? extends User> users1 = usersDataProvider.getUsers(0, 0, 0, NacidDataProvider.APP_NACID_ID);
    List<? extends User> users2 = usersDataProvider.getUsers(0, 0, 0, NacidDataProvider.APP_NACID_REGPROF_ID);
    List<User> users = new ArrayList<User>();
    users.addAll(users1);
    if (users != null && users2 != null) {
        for (User u2:users2) {
            boolean exists = false;
            for (User u:users1) {
                if (u2.getUserId() == u.getUserId()) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                users.add(u2);
            }
        }
    }
    

    if (users != null) {
      for (User u:users) {
        try {
          table.addRow(u.getUserId(), u.getUserName(), u.getFullName(), u.getEmail());
        } catch (IllegalArgumentException e) {
            throw Utils.logException(e);
        } catch (CellCreationException e) {
            throw Utils.logException(e);
        }
      }
    }
  }
    private static ComboBoxWebModel generateUsersComboBox(Integer activeId, List<? extends User> users, HttpServletRequest request, String comboName, boolean addEmpty, boolean addInactive) {
        ComboBoxWebModel combobox = new ComboBoxWebModel(activeId == null ? "" : activeId + "", addEmpty);
        if (users != null) {
            for (User s : users) {
                if (addInactive || s.getStatus() == 1) {
                    combobox.addItem(s.getUserId() + "", s.getUserName() + " / " + s.getFullName());
                }
            }
            if (comboName != null && request != null) {
                request.setAttribute(comboName, combobox);
            }

        }
        return combobox;
    }
}
