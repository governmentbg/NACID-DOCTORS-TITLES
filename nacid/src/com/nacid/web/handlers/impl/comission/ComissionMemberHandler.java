package com.nacid.web.handlers.impl.comission;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nacid.web.handlers.ComboBoxUtils;
import org.apache.commons.lang.StringUtils;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.comision.ComissionMemberDataProvider;
import com.nacid.bl.exceptions.UserAlreadyExistsException;
//import com.nacid.bl.external.users.ExtUser;
//import com.nacid.bl.external.users.ExtUserAlreadyExistsException;
//import com.nacid.bl.external.users.ExtUsersDataProvider;
import com.nacid.bl.external.users.ExtUsersGroupMembership;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.ProfessionGroup;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.table.TableState;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UserGroupMembership;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.comission.ComissionMemberWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;

public class ComissionMemberHandler extends NacidBaseRequestHandler {

	private static final String COLUMN_NAME_ID ="Id";
	private static final String COLUMN_NAME_NAME ="Име";  	
	private static final String COLUMN_NAME_FNAME ="Фамилия";
	//private static final String COLUMN_NAME_PHONE ="Телефон/и";
	//private static final String COLUMN_NAME_GSM ="GSM";
	private static final String COLUMN_NAME_PROF_GROUP ="Професионално направление";
	//private static final String COLUMN_NAME_EMAIL ="Електронна поща";
	private static final String COLUMN_NAME_COMISSION_POS ="Позиция в комисията";
	private static final String COLUMN_NAME_TO_DATE = "До дата";
	
	public ComissionMemberHandler(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void handleNew(HttpServletRequest request, HttpServletResponse response) {
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nDP = nacidDataProvider.getNomenclaturesDataProvider();
		generateProfGroupComboBox(0, nDP, request);
		generatePositionComboBox(0, nDP, request);
				
		request.setAttribute(WebKeys.NEXT_SCREEN, "comission_member_edit");
	}

	@Override
	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
		int memberId = DataConverter.parseInt(request.getParameter("id"), -1);
		if (memberId <= 0) {
			throw new UnknownRecordException("Unknown Member ID:" + memberId);
		}
		request.setAttribute(WebKeys.NEXT_SCREEN, "comission_member_edit");
		NacidDataProvider nacidDataProvider = getNacidDataProvider();

		ComissionMemberDataProvider cmDP = nacidDataProvider.getComissionMemberDataProvider();
		NomenclaturesDataProvider nDP = nacidDataProvider.getNomenclaturesDataProvider();
		ComissionMember cm = cmDP.getComissionMember(memberId);
		
		if (cm == null) {
			throw new UnknownRecordException("Unknown Member ID:" + memberId);
		}
		generateProfGroupComboBox((cm.getProfGroup() != null) ? cm.getProfGroup().getId() : 0,
				nDP, request);
		generatePositionComboBox(cm.getComissionPos().getId(),
				nDP, request);
		
		request.setAttribute(WebKeys.COMISSION_MEMBER_WEB_MODEL, new ComissionMemberWebModel(cm));
	}

	@Override
	public void handleList(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Table table = (Table) session.getAttribute(WebKeys.TABLE_COMISSION_MEMBER);

		boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
		if (reloadTable || table == null) {
			TableFactory tableFactory = TableFactory.getInstance();
			table = tableFactory.createTable();
			table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
			table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_FNAME, CellValueDef.CELL_VALUE_TYPE_STRING);
			//table.addColumnHeader(COLUMN_NAME_PHONE, CellValueDef.CELL_VALUE_TYPE_STRING);
			//table.addColumnHeader(COLUMN_NAME_GSM, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_PROF_GROUP, CellValueDef.CELL_VALUE_TYPE_STRING);
			//table.addColumnHeader(COLUMN_NAME_EMAIL, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_COMISSION_POS, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_TO_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
            
			
			session.setAttribute(WebKeys.TABLE_COMISSION_MEMBER, table);
			resetTableData(request);

		}

		// TableState settings
		TableState tableState = (TableState) session
				.getAttribute(WebKeys.TABLE_COMISSION_MEMBER + WebKeys.TABLE_STATE);
		/**
		 * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
		 * vzeme posledniq tableState, togava se generira nov tableState!
		 */
		boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
		if (tableState == null || !getLastTableState) {
			tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
			TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);
            session.setAttribute(WebKeys.TABLE_COMISSION_MEMBER + WebKeys.TABLE_STATE, tableState);
		}

		// TableWebModel SETTINGS
		TableWebModel webmodel = new TableWebModel("Списък на актуалния състав на комисията");
		// webmodel.setColumnFormatter("userDate",
		// CellFormatter.DATE_TIME_FORMATTER);
		webmodel.setGroupName(getGroupName(request));
		webmodel.insertTableData(table, tableState);
		request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
		request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");

		// Generating filters for displaying to user
		FiltersWebModel filtersWebModel = (FiltersWebModel) session
				.getAttribute(WebKeys.TABLE_COMISSION_MEMBER + WebKeys.FILTER_WEB_MODEL);
		/**
		 * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
		 * da se vzeme posledniq tableState(filterWebModel), togava se generira
		 * nov i se slaga v sesiqta!
		 */
		if (filtersWebModel == null || !getLastTableState) {
			filtersWebModel = new FiltersWebModel();
			filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));
            
			session.setAttribute(WebKeys.TABLE_COMISSION_MEMBER
					+ WebKeys.FILTER_WEB_MODEL, filtersWebModel);
		}
		request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);

	}

	@Override
	public void handleSave(HttpServletRequest request, HttpServletResponse response) {
		if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
			new HomePageHandler(request.getSession().getServletContext())
					.processRequest(request, response);
			return;
		}
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		ComissionMemberDataProvider cmDP = nacidDataProvider.getComissionMemberDataProvider();
		int id = DataConverter.parseInt(request.getParameter("id"), 0);
		String fname = request.getParameter("first_name");
		String sname = request.getParameter("second_name");
		String lname = request.getParameter("last_name");
		String degree = request.getParameter("degree");
		String institution = request.getParameter("institution");
		String division = request.getParameter("division");
		String title = request.getParameter("title");
		String egn = request.getParameter("personal_id");
		String homeCity = request.getParameter("location");
		String homePcode = request.getParameter("location_post_code");
		String homeAddress = request.getParameter("address");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		String gsm = request.getParameter("gsm");
		String iban = request.getParameter("iban");
		String bic = request.getParameter("bic");
		String userName = request.getParameter("userName");
		String userPass = request.getParameter("userPass");
		
		Integer profGroupId = null;
		try {
			profGroupId = new Integer(request.getParameter("profGroup"));
		}
		catch(NumberFormatException e) {}
		
		
		int position = DataConverter.parseInt(request.getParameter("member_consultant"), -1);
		
		Date dateFrom = null;
		if(request.getParameter("date_from") != null)
			dateFrom = DataConverter.parseDate(request.getParameter("date_from"));
		Date dateTo = null;
		if(request.getParameter("date_to") != null)
			dateTo = DataConverter.parseDate(request.getParameter("date_to"));
		
		if(id == 0 && dateFrom == null) {
		    dateFrom = new Date();
		}
		
		if (id != 0 && cmDP.getComissionMember(id) == null) {
			throw new UnknownRecordException("Unknown Comission member ID:" + id);
		}
		
		
		ComissionMemberWebModel comissionMemberWM = new ComissionMemberWebModel(id, fname, sname, lname, degree, institution, division, title, egn, homeCity, homeAddress, homePcode,
                phone, email, gsm, iban, bic, request.getParameter("date_from"), 
                request.getParameter("date_to"), userName);

		if(Utils.isEmptyString(userName)) userName = null;
		if(Utils.isEmptyString(userPass)) userPass = null;
		
		Integer userId = null;
        ComissionMember oldRec = cmDP.getComissionMember(id);
        User oldUser = null;
        int userStatus = User.USER_STATUS_ACTIVE;
        if(oldRec != null && oldRec.getUser() != null) {
            oldUser = oldRec.getUser();
            userId = oldUser.getUserId();
            userStatus = oldUser.getStatus();
        }
        
        SystemMessageWebModel smError = new SystemMessageWebModel();
		if (!StringUtils.isEmpty(egn) && id == 0 && cmDP.hasActiveMemberWithEGN(egn)) {
		    smError.addAttribute("Вече има активен член на комисията с такова ЕГН");
		} 
		if(userId == null && userName != null && userPass == null) {
		    smError.addAttribute("Паролата е задължителна при първо въвеждане на потребителско име.");
		}
		if(userId != null && userName == null) {
            smError.addAttribute("Потребителското име не може да се изтрива.");
        }
		
        if (!smError.getAttributes().isEmpty()) {
            finishHandleSave(request, nacidDataProvider, smError, comissionMemberWM, profGroupId, position);
            return;
        }
		
		Map<Integer, Set<Integer>> operationsAccess = new HashMap<Integer, Set<Integer>>();
        Set<Integer> set = new HashSet<Integer>();
        set.add(UserGroupMembership.FULL_ACCESS_OPERATION_ID);
        operationsAccess.put(ExtUsersGroupMembership.EXPERT_GROUP, set);
		    
        UsersDataProvider euDP = nacidDataProvider.getUsersDataProvider();
            
            
        
        try {
            
            if(userName != null) {
                Map<Integer, Map<Integer, Set<Integer>>> ops = new HashMap<Integer, Map<Integer,Set<Integer>>>();
                ops.put(NacidDataProvider.APP_NACID_EXT_ID, operationsAccess);
                userId = euDP.updateUser(userId == null ? 0 : userId, fname + " " + lname, null, 
                        userName, userPass, 
                        userStatus, null, null, ops);
            }
            

            int newId = cmDP.saveComissionMember(id, fname, sname, lname, degree, institution, division, title, egn, homeCity, homePcode,
                    homeAddress, phone, email, gsm, iban, bic, dateFrom, dateTo, position, profGroupId, userId);

            finishHandleSave(request, nacidDataProvider, 
                    new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT), 
                    new ComissionMemberWebModel(cmDP.getComissionMember(newId)), 
                    profGroupId, position);
            resetTableData(request);
            return;
        } catch (UserAlreadyExistsException e) {
            SystemMessageWebModel sm = new SystemMessageWebModel("Вече има потребител с това потребителско име.");
            finishHandleSave(request, nacidDataProvider, sm, comissionMemberWM, profGroupId, position);
            return;
        }
		
	}
	
	private void finishHandleSave(HttpServletRequest request, NacidDataProvider nacidDP,
	        SystemMessageWebModel sm, ComissionMemberWebModel cmWM, Integer profGroupId, int position) {
	    
	    request.setAttribute(WebKeys.SYSTEM_MESSAGE, sm);
        request.setAttribute(WebKeys.COMISSION_MEMBER_WEB_MODEL, cmWM);
	    
	    NomenclaturesDataProvider nDP = nacidDP.getNomenclaturesDataProvider();
        generateProfGroupComboBox(profGroupId, nDP, request);
        generatePositionComboBox(position, nDP, request);
        
        request.setAttribute(WebKeys.NEXT_SCREEN, "comission_member_edit");
        request.getSession().removeAttribute(WebKeys.TABLE_COMISSION_MEMBER);
	}

	@Override
	public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
		int cmId = DataConverter.parseInt(request.getParameter("id"), -1);
	    if (cmId <= 0) {
	      throw new UnknownRecordException("Unknown Comission Member ID:" + cmId);
	    }
	    ComissionMemberDataProvider cmDP = getNacidDataProvider().getComissionMemberDataProvider();
	    cmDP.deleteComissionMember(cmId);	    
	    request.getSession().removeAttribute(WebKeys.TABLE_COMISSION_MEMBER);
	    handleList(request, response);
	}
	
	private void resetTableData(HttpServletRequest request) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_COMISSION_MEMBER);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ComissionMemberDataProvider comissionMemberDP = nacidDataProvider.getComissionMemberDataProvider();
        List<ComissionMember> members = comissionMemberDP.getComissionMembers(false, null);

        if (members != null) {
            for (ComissionMember cm : members) {
                try {
                    String pgName = "";
                    if (cm.getProfGroup() != null && cm.getProfGroup().getName() != null) {
                        pgName = cm.getProfGroup().getName();
                    }
                    table.addRow(cm.getId(), cm.getFname(), cm.getLname(), 
                            /*cm.getPhone(), cm.getGsm(),*/ pgName, /*cm.getEmail(),*/ 
                            cm.getComissionPos()
                            .getName(), cm.getDateTo());
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
	
	private static void generateProfGroupComboBox(
			Integer profGroupId,
			NomenclaturesDataProvider nomenclaturesDataProvider,
			HttpServletRequest request) {
		ComboBoxUtils.generateProfessionGroupComboBox(profGroupId, nomenclaturesDataProvider.getProfessionGroups(0, null, null), false, request, "profGroupCombo", true);
	}
	
	private static void generatePositionComboBox(
			int activePosId,
			NomenclaturesDataProvider nomenclaturesDataProvider,
			HttpServletRequest request) {
		
		ComboBoxWebModel combobox = new ComboBoxWebModel(activePosId + "", true);
		List<FlatNomenclature> flats = nomenclaturesDataProvider.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_COMMISSION_POSITION, null, null);
		if (flats != null) {
			for (FlatNomenclature s : flats) {
				combobox.addItem(s.getId() + "", s.getName()
						+ (s.isActive() ? "" : " (inactive)"));
			}
			request.setAttribute("positionCombo", combobox);
		}
	}
}
