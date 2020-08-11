package com.nacid.web.handlers.impl.nomenclatures;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclatureOrderCriteria;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.bl.table.TableState;
import com.nacid.data.DataConverter;
import com.nacid.report.export.JasperReportNames;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.RadioButtonWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.nomenclatures.DocumentTypeWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.ComboBoxFilterWebModel;
import com.nacid.web.model.table.filters.FilterWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import com.nacid.web.model.table.filters.TextFieldFilterWebModel;
import org.springframework.util.CollectionUtils;


public class DocumentTypeHandler extends NacidBaseRequestHandler {
	private static final String COLUMN_NAME_ID = "id";
	private static final String COLUMN_NAME_NAME = "Наименование";
	private static final String COLUMN_NAME_IS_INCOMING = "Входящ";
	private static final String COLUMN_NAME_HAS_DOCFLOW_ID = "Завежда се в деловодството";

	private static final String COLUMN_NAME_DOCUMENT_CATEGORY = "Категория";
	private static final String COLUMN_NAME_DOCUMENT_CATEGORY_IDS = "categoryIds";
	private static final String COLUMN_NAME_DATE_FROM = "От дата";
	private static final String COLUMN_NAME_DATE_TO = "До дата";

	private static final String FILTER_NAME_NAME = "nameFilter";
	private static final String FILTER_NAME_DOCUMENT_CATEGORY = "categoryFilter";


	public DocumentTypeHandler(ServletContext servletContext) {
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
		boolean isIncoming = DataConverter.parseBoolean(request.getParameter("is_incoming"));
		boolean hasDocflowId = DataConverter.parseBoolean(request.getParameter("has_docflow_id"));
		String[] cats = request.getParameterValues("doc_category_id");
		List<Integer> documentCategoryIds = cats == null || cats.length == 0 ? new ArrayList<>() : RequestParametersUtils.convertRequestParameterToIntegerList(Arrays.stream(cats).collect(Collectors.joining(";")));
		Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
		String documentTemplate = DataConverter.parseString(request.getParameter("document_template"), null);
		//If new record is added and dateFrom is not set, then dateFrom is set to today
		if (id == 0 && dateFrom == null) {
			dateFrom = Utils.getToday();
		}
		Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
		if (id != 0 && nomenclaturesDataProvider.getDocumentType(id) == null) {
			throw new UnknownRecordException("Unknown Document Type ID:" + id);
		}
		//Teoretichno tezi proverki se pravqt client side i se razbrahme da nqma server side proverki, no tozi kod e star(predi da se razberem tova neshto)
		//i sym go ostavil, zashtoto taka ili inache si vyr6i rabota
		if (name == null || "".equals(name) || CollectionUtils.isEmpty(documentCategoryIds)) {
			SystemMessageWebModel webmodel = new SystemMessageWebModel("Грешно въведени данни!");
			if (name == null || "".equals(name)) {
				webmodel.addAttribute("- грешно въведено име!");  
			}
			if (CollectionUtils.isEmpty(documentCategoryIds)) {
				webmodel.addAttribute("- грешно въведенa категория на документа!");  
			}
			request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
			request.setAttribute(WebKeys.DOCUMENT_TYPE, new DocumentTypeWebModel(id + "", name, isIncoming, hasDocflowId, documentCategoryIds, request.getParameter("dateFrom"), request.getParameter("dateTo"), documentTemplate));

			generateDocumentCategoriesMap(nacidDataProvider.getNomenclaturesDataProvider(), request);
//			ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_DOC_CATEGORY, getNacidDataProvider().getNomenclaturesDataProvider(), true, request, WebKeys.COMBO, NomenclatureOrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), false);
			generateIsIncomingRadio(request, isIncoming);
			generateHasDocflowIdRadio(request, hasDocflowId);

		
		} else {
			int newId = nomenclaturesDataProvider.saveDocumentType(id, name, isIncoming, hasDocflowId, documentCategoryIds, documentTemplate, dateFrom, dateTo);
			FlatNomenclatureHandler.refreshCachedNomenclatures(request);
			request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
			generateDocumentTypeWebModel(request, nomenclaturesDataProvider, nomenclaturesDataProvider.getDocumentType(newId));
			request.getSession().removeAttribute(WebKeys.TABLE_DOCUMENT_TYPE);
			
		}
		
		request.setAttribute(WebKeys.NEXT_SCREEN, "document_type_edit");


	}

	@Override
	public void handleNew(HttpServletRequest request, HttpServletResponse response) {
		generateDocumentTypeWebModel(request, getNacidDataProvider().getNomenclaturesDataProvider(), null);
		request.setAttribute(WebKeys.NEXT_SCREEN, "document_type_edit");
	}

	@Override
	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
		int documentTypeId = DataConverter.parseInt(request.getParameter("id"), -1);
		if (documentTypeId <= 0) {
			throw new UnknownRecordException("Unknown Document Type ID:" + documentTypeId);
		}

		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		DocumentType documentType = nomenclaturesDataProvider.getDocumentType(documentTypeId);
		if (documentType == null) {
			throw new UnknownRecordException("Unknown DocumentType ID:" + documentTypeId);
		}
		generateDocumentTypeWebModel(request, nomenclaturesDataProvider, documentType);
		request.setAttribute(WebKeys.NEXT_SCREEN, "document_type_edit");
	}
	
	private static void generateDocumentTypeWebModel(HttpServletRequest request, NomenclaturesDataProvider nomenclaturesDataProvider, DocumentType documentType) {
//		ComboBoxUtils.generateNomenclaturesComboBox(documentType == null ? null : documentType.getDocCategoryId(), NomenclaturesDataProvider.FLAT_NOMENCLATURE_DOC_CATEGORY, nomenclaturesDataProvider, true, request, WebKeys.COMBO, NomenclatureOrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), false);
		generateDocumentCategoriesMap(nomenclaturesDataProvider, request);
		generateIsIncomingRadio(request, documentType == null ? true : documentType.isIncoming());
		generateHasDocflowIdRadio(request, documentType == null ? true : documentType.isHasDocflowId());
		if (documentType != null) {
			request.setAttribute(WebKeys.DOCUMENT_TYPE, new DocumentTypeWebModel(documentType));	
		}
		
	}

	public void handleList(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		Table table = (Table) session.getAttribute(WebKeys.TABLE_DOCUMENT_TYPE);
		boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
		/**
		 * ako e settnat paremeter-a reloadTable ili table == null, se generira nova tablica!
		 */
		if (reloadTable || table == null) {
			TableFactory tableFactory = TableFactory.getInstance();
			table = tableFactory.createTable();
			table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
			table.addColumnHeader(COLUMN_NAME_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_IS_INCOMING, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);
			table.addColumnHeader(COLUMN_NAME_HAS_DOCFLOW_ID, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);
			table.addColumnHeader(COLUMN_NAME_DOCUMENT_CATEGORY, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_DOCUMENT_CATEGORY_IDS, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_DATE_FROM, CellValueDef.CELL_VALUE_TYPE_DATE);
			table.addColumnHeader(COLUMN_NAME_DATE_TO, CellValueDef.CELL_VALUE_TYPE_DATE);
			session.setAttribute(WebKeys.TABLE_DOCUMENT_TYPE, table);
			resetTableData(request);

		}
		TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_DOCUMENT_TYPE + WebKeys.TABLE_STATE);
		/**
		 * ako tableState-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState, togava se generira nov tableState!
		 */
		boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
		if (tableState == null || !getLastTableState) {
			tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
			TableStateAndFiltersUtils.addContainsFilterToTableState(FILTER_NAME_NAME, COLUMN_NAME_NAME, request, table, tableState);
			TableStateAndFiltersUtils.addContainsFilterToTableState(FILTER_NAME_DOCUMENT_CATEGORY, COLUMN_NAME_DOCUMENT_CATEGORY_IDS, request, table, tableState);
			TableStateAndFiltersUtils.addToDateFilterToTableState(request, table, tableState);

			session.setAttribute(WebKeys.TABLE_DOCUMENT_TYPE + WebKeys.TABLE_STATE, tableState);
		}

		TableWebModel webmodel = new TableWebModel("Типове документи");
		webmodel.setGroupName(getGroupName(request));
		webmodel.insertTableData(table, tableState);
		webmodel.hideUnhideColumn(COLUMN_NAME_DOCUMENT_CATEGORY_IDS, true);
		request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
		if (DataConverter.parseBoolean(request.getParameter("inner"))) {
			request.setAttribute(WebKeys.NEXT_SCREEN, "list_inner_table");
		} else {
			request.setAttribute(WebKeys.NEXT_SCREEN, "table_list");	
		}


		//Generating filters for displaying to user
		FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_DOCUMENT_TYPE + WebKeys.FILTER_WEB_MODEL);
		/**
		 * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva da se vzeme posledniq tableState(filterWebModel), togava se generira nov i se slaga v sesiqta!
		 */
		if (filtersWebModel == null || !getLastTableState) {
			filtersWebModel = new FiltersWebModel();
			filtersWebModel.addFiler(generateNameFilter(request));
			filtersWebModel.addFiler(generateDocumentCategoryComboBox(request.getParameter(FILTER_NAME_DOCUMENT_CATEGORY), getNacidDataProvider().getNomenclaturesDataProvider(), request));
			filtersWebModel.addFiler(TableStateAndFiltersUtils.getToDateFilterWebModel(request));

			session.setAttribute(WebKeys.TABLE_DOCUMENT_TYPE + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
		}
		request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
	}
	public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
		int documentTypeId = DataConverter.parseInt(request.getParameter("id"), -1);
		if (documentTypeId <= 0) {
			throw new UnknownRecordException("Unknown Document Type ID:" + documentTypeId);
		}
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		DocumentType documentType = nomenclaturesDataProvider.getDocumentType(documentTypeId);
		if (documentType == null) {
			throw new UnknownRecordException("Unknown Document Type ID:" + documentTypeId);
		}
		nomenclaturesDataProvider.saveDocumentType(documentType.getId(), documentType.getName(), documentType.isIncoming(), documentType.isHasDocflowId(), documentType.getDocCategoryIds(), documentType.getDocumentTemplate(), documentType.getDateFrom(), new Date());
		FlatNomenclatureHandler.refreshCachedNomenclatures(request);
		request.getSession().removeAttribute(WebKeys.TABLE_DOCUMENT_TYPE);
		handleList(request, response);
	}

	private void resetTableData(HttpServletRequest request) {
		Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_DOCUMENT_TYPE);
		if (table == null) {
			return;
		}
		table.emtyTableData();
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		List<DocumentType> documentTypes = nomenclaturesDataProvider.getDocumentTypes(null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_ID, false), 0);
		if (documentTypes != null) {
			for (DocumentType c:documentTypes) {
				try {
					table.addRow(c.getId(), c.getName(), c.isIncoming(), c.isHasDocflowId(),
							c.getDocCategoryIds().stream().map(r -> nomenclaturesDataProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_DOC_CATEGORY, r).getName()).collect(Collectors.joining("<br />")),
							c.getDocCategoryIds().stream().map(r -> "-" + r + "-").collect(Collectors.joining()),
							c.getDateFrom(),  c.getDateTo());
				} catch (IllegalArgumentException e) {
					throw Utils.logException(e);
				} catch (CellCreationException e) {
					throw Utils.logException(e);
				}
			}
		}
	}
	private static ComboBoxFilterWebModel generateDocumentCategoryComboBox(String activeDocumentCategoryName, NomenclaturesDataProvider nomenclaturesDataProvider, HttpServletRequest request) {
		ComboBoxWebModel combobox = new ComboBoxWebModel(activeDocumentCategoryName, true);
		List<FlatNomenclature> educationAreas = nomenclaturesDataProvider.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_DOC_CATEGORY, null, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true));
		if (educationAreas != null) {
			for (FlatNomenclature ea:educationAreas) {
				combobox.addItem("-"+ ea.getId() + "-", ea.getName() + (ea.isActive() ? "" : " (inactive)"));
			}
		}
		ComboBoxFilterWebModel res = new ComboBoxFilterWebModel(combobox, FILTER_NAME_DOCUMENT_CATEGORY, "Категория:");
		res.setElementClass("brd");
		return res;
	}
	private static FilterWebModel generateNameFilter( HttpServletRequest request) {
		TextFieldFilterWebModel wm = new TextFieldFilterWebModel(FILTER_NAME_NAME, COLUMN_NAME_NAME, request.getParameter(FILTER_NAME_NAME));
		wm.setElementClass("brd w200");
		return wm;
	}
	private static void generateIsIncomingRadio(HttpServletRequest request, boolean isIncoming) {
		RadioButtonWebModel rbwm = new RadioButtonWebModel(isIncoming + "");
		rbwm.addItem("true", "входящ");
		rbwm.addItem("false", "изходящ");
		request.setAttribute("isIncomingRadio", rbwm);
	}
	private static void generateHasDocflowIdRadio(HttpServletRequest request, boolean hasDocflowId) {
		RadioButtonWebModel rbwm = new RadioButtonWebModel(hasDocflowId + "");
		rbwm.addItem("true", "да");
		rbwm.addItem("false", "не");
		request.setAttribute("hasDocflowIdRadio", rbwm);
	}

	private static void generateDocumentCategoriesMap(NomenclaturesDataProvider nomenclaturesDataProvider, HttpServletRequest request) {
		List<FlatNomenclature> categories = nomenclaturesDataProvider.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_DOC_CATEGORY, null, null);
		Map<Integer, String> map = categories.stream().collect(Collectors.toMap(FlatNomenclature::getId, r -> r.getName() + (r.isActive() ? "" : "(inactive)"), (r1, r2) -> r1, LinkedHashMap::new));
		request.setAttribute("documentCategoriesMap", map);
	}
}
