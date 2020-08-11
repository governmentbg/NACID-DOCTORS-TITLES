package com.nacid.web.handlers.impl.inquires;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.ApplicationDetailsForReport;
import com.nacid.bl.events.Event;
import com.nacid.bl.events.EventDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValueDef;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableFactory;
import com.nacid.data.inquire.ApplicationForInquireRecord;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.inquires.CommissionInquireUniversityWebModel;
import com.nacid.web.model.inquires.InquiryInquireWebModel;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.nacid.web.handlers.impl.applications.ApplicationsHandler.generateApplicationTypesListColumn;

public class InquiryInquireHandler extends InquireBaseHandler {
	private final static String COLUMN_NAME_ID = "id";
	private final static String COLUMN_NAME_APPLICATION_NUMBER = "Делов. номер";
	private final static String COLUMN_NAME_DATE = "Дата";
	private final static String COLUMN_NAME_OWNER_NAME = "Имена на собственика";
	private final static String COLUMN_NAME_DIPLOMA_EDU_LEVEL = "ОКС по диплома";
	private final static String COLUMN_NAME_DIPLOMA_SPECIALITY = "Специалност по диплома";
	private final static String COLUMN_NAME_EVENT_STATUS = "Статус на напомнянето";
	private final static String COLUMN_NAME_APP_STATUS = "Статус на заявлението";
	private final static String COLUMN_NAME_DOCFLOW_STATUS = "Деловоден статус";

    public InquiryInquireHandler(ServletContext servletContext) {
		super(servletContext);
	}

	
	public void handleView(HttpServletRequest request, HttpServletResponse response) {
		//super.handleView(request, response);
		List<DocumentType> documentTypes = getDocumentTypesForInquiryInquireCombobox(getNacidDataProvider());
		request.setAttribute("documentTypesCombo", generateDocumentTypeComboboxWebModel(documentTypes, getNacidDataProvider().getNomenclaturesDataProvider(), true));
		
		request.setAttribute("universities", Arrays.asList(new CommissionInquireUniversityWebModel(null, new ComboBoxWebModel(null, true))));
		request.setAttribute("universities_count", 1);
		request.setAttribute("emptyUniversityCombo", new ComboBoxWebModel(null, true));
		ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.NOMENCLATURE_COUNTRY, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "countriesCombo", null, true);
		ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_EVENT_STATUS, getNacidDataProvider().getNomenclaturesDataProvider(), false, request, "eventStatusesCombo", null, true);

		request.setAttribute(WebKeys.NEXT_SCREEN, "inquiry_inquire");
	}
	
	protected List<ApplicationDetailsForReport> getApplicationDetailsForReport(HttpServletRequest request) {
		InquiryInquireWebModel inquiryInquireWebModel = new InquiryInquireWebModel(request, getNacidDataProvider(), jointDegreeFlag);
		
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		List<ApplicationDetailsForReport> apps = nacidDataProvider.getInquiresDataProvider().getApplicationDetailsForReportForInquiryInquire(inquiryInquireWebModel);
		return apps;
	}


	protected List<ApplicationForInquireRecord> getApplications(HttpServletRequest request) {
		InquiryInquireWebModel inquiryInquireWebModel = new InquiryInquireWebModel(request, getNacidDataProvider(), jointDegreeFlag);
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		List<ApplicationForInquireRecord> apps = nacidDataProvider.getInquiresDataProvider().getApplicationsForInquiryInquire(inquiryInquireWebModel);
		return apps;
	}


	protected String getTableName() {
		return "tableInquiryInquire";
	}
	private static ComboBoxWebModel generateDocumentTypeComboboxWebModel(List<DocumentType> documentTypes, NomenclaturesDataProvider nomenclaturesDataProvider, boolean addEmpty) {
		ComboBoxWebModel webmodel = new ComboBoxWebModel(null, addEmpty);
		if (documentTypes != null) {
			for (DocumentType dt:documentTypes) {
				webmodel.addItem(dt.getId() + "", dt.getName() + ", " + dt.getDocCategoryIds().stream().map(r -> nomenclaturesDataProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_DOC_CATEGORY, r).getName()).collect(Collectors.joining("/")));
			}
		}
		return webmodel;
	}
	public static List<DocumentType> getDocumentTypesForInquiryInquireCombobox(NacidDataProvider nacidDataProvider) {
		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		return nomenclaturesDataProvider.getDocumentTypes(null, NomenclatureOrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), null, Arrays.asList(DocCategory.DIPLOMA_EXAMINATIONS, DocCategory.UNIVERSITY_EXAMINATIONS));
	}


	protected Table constructEmptyTable(HttpServletRequest request) {
		//boolean jointUniversities = DataConverter.parseBoolean(request.getParameter("joint_universities"));
		TableFactory tableFactory = TableFactory.getInstance();
		Table table = tableFactory.createTable();
		table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
		table.addColumnHeader(COLUMN_NAME_APPLICATION_NUMBER, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
		table.addColumnHeader(COLUMN_NAME_OWNER_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_UNIVERSITY_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_UNIVERSITY_COUNTRY, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_DIPLOMA_EDU_LEVEL, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_DIPLOMA_SPECIALITY, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_EVENT_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_APP_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_DOCFLOW_STATUS, CellValueDef.CELL_VALUE_TYPE_STRING);
		table.addColumnHeader(COLUMN_NAME_APPLICATION_ENTRY_NUMS, CellValueDef.CELL_VALUE_TYPE_STRING);
		return table;
	}


	protected void resetTableData(HttpServletRequest request, List<ApplicationForInquireRecord> apps) {
		Table table = (Table) request.getSession().getAttribute(getTableName());
		if (table == null) {
			return;
		}
		table.emtyTableData();
		EventDataProvider eventDataProvider = getNacidDataProvider().getEventDataProvider();
		InquiryInquireWebModel inquiryInquireWebModel = new InquiryInquireWebModel(request, getNacidDataProvider(), jointDegreeFlag);
		NomenclaturesDataProvider nomenclaturesDataProvider = getNacidDataProvider().getNomenclaturesDataProvider();
		List<Integer> eventStatusIds = inquiryInquireWebModel.getEventStatusIds();
		List<Integer> documentTypeIds = inquiryInquireWebModel.getDocumentTypeIds();

		//Ako se iskat samo aktivnite, togava se zarejdat vsi4ki bez izbroenite v ApplicationStatus.APPLICATIONS_ONLY_ACTIVE_STATUSES statusi, inache se vry6tat vsi4ki zapisi v bazata
		if (apps != null) {
			for (ApplicationForInquireRecord app : apps) {
				try {

					List<Event> events = eventDataProvider.getEventsForApplication(app.getId());
					String eventStatusStr = "-";
					if (events != null) {
						List<String> lst = new ArrayList<String>();
						for (Event e:events) {
							if (e.getDocumentTypeId() != null && e.getEventStatus() != null && 
									(eventStatusIds == null || eventStatusIds.contains(e.getEventStatus()) ) && 
									(documentTypeIds == null || documentTypeIds.contains(e.getDocumentTypeId()))
							) {
								DocumentType docType = nomenclaturesDataProvider.getDocumentType(e.getDocumentTypeId());
								FlatNomenclature eventStatus = nomenclaturesDataProvider.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EVENT_STATUS, e.getEventStatus());
								lst.add(docType.getName() + " - " + eventStatus.getName());
							}
						}
						eventStatusStr = lst.size() == 0 ? "-" : StringUtils.join(lst, ";<br />\n");
					}
					table.addRow(
							app.getId(), 
							app.getAppNumber(),
							app.getAppDate(),
                            app.getOwnerNames(),
							app.getUniversityNames(!addCountryColumn),
                            app.getUniversityCountry(),
                            app.getEduLevelName() == null ? "-" : app.getEduLevelName(),
							app.getSpecialityNamesSeparatedBySemiColon(),
							eventStatusStr, 
							app.getApplicationStatusName(),
							app.getDocflowStatusName(),
							generateApplicationTypesListColumn(request, app.getApplicationType(), app.getEntryNumSeries()));
				} catch (IllegalArgumentException e) {
					throw Utils.logException(e);
				} catch (CellCreationException e) {
					throw Utils.logException(e);
				}
			}
		}
	}
	
}
