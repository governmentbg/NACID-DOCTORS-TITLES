package com.nacid.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.applications.AttachmentDataProvider;
import com.nacid.bl.events.Event;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.DocCategory;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.table.*;
import com.nacid.data.DataConverter;
import com.nacid.report.TemplateGenerator;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.applications.*;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.nacid.web.handlers.impl.applications.SaveAttachmentRequest.prepareFileName;

public class DiplExamAttachmentHandler extends BaseAttachmentHandler {

	private static final String COLUMN_NAME_ID = "id";
	private static final String COLUMN_NAME_FILE_NAME = "Име";
	private static final String COLUMN_NAME_DESC = "Описание";
	private static final String COLUMN_NAME_PREVIEW = "Preview";

    private static final String COLUMN_NAME_DOCFLOW_NUM = "Деловоден номер";
    private static final String COLUMN_NAME_DOCUMENT_TYPE = "Тип документ";
    
	private static final String DIPL_EXAM_PARAM = "diplExamId";
	private static final String EDIT_SCREEN = "dipl_exam_attachment_edit";
	
	private static final String TABLE_PREFIX = "diplExam";
	
	
	public DiplExamAttachmentHandler(ServletContext servletContext) {
		super(servletContext);
	}

	public void handleNew(HttpServletRequest request, HttpServletResponse response) {
		int diplExamId = Integer.parseInt(request.getParameter(DIPL_EXAM_PARAM));
		
		
		request.setAttribute(WebKeys.DIPL_EXAM_ATTCH_WEB_MODEL, 
				new DiplExamAttachmentWebModel(diplExamId,
				        request.getParameter(UniversityValidityHandler.APPLICATION_ID_PARAM)));
		generateDocTypesComboBox(null, null, request);
		request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
		

        generateEventTypeCombo(null, request);
	}
	
	@Override
	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
		
		int attachmentId = DataConverter.parseInt(request.getParameter("id"), -1);
		if (attachmentId <= 0) {
			throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
		}
		request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);

		AttachmentDataProvider attDP = getAttachmentDataProvider();
		
		Attachment att = attDP.getAttachment(attachmentId, false, false);
		
		if (att == null) {
			throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
		}
		
		generateDocTypesComboBox(null, att.getDocTypeId(), request);
		
		int applId = DataConverter.parseInt(request.getParameter(UniversityValidityHandler.APPLICATION_ID_PARAM), 0);
		
		String dfUrl = getDocFlowUrl(getNacidDataProvider(), applId, attachmentId, 
                getEditUrl(attachmentId, applId + ""));
        
		
        request.setAttribute(WebKeys.DIPL_EXAM_ATTCH_WEB_MODEL, 
				new DiplExamAttachmentWebModel(att.getId(), att.getParentId(),
						att.getDocDescr(), att.getFileName(), 
						applId + "",
						att.getScannedFileName(), att.getDocflowNum(), att.getDocTypeId(), dfUrl));

        Event ev = Utils.getListFirstElement(getNacidDataProvider().getEventDataProvider()
                .getEventsForDocument(attachmentId, att.getDocType().getId()));
        generateEventStatusCombo(ev != null ? ev.getEventStatus() : null, request);
        generateEventTypeCombo(ev != null ? ev.getEventTypeId() : null, request);
        addEventDatesToRequest(request, ev, getNacidDataProvider());
	}

	@Override
	public void handleList(HttpServletRequest request, HttpServletResponse response) {
		
	    ApplicationWebModel appWM = (ApplicationWebModel)request.getAttribute(WebKeys.APPLICATION_WEB_MODEL);
        TrainingCourseWebModel tcWM = appWM.getTrainingCourseWebModel();
        DiplomaExaminationWebModel deWM = tcWM.getDiplomaExaminationWebModel();
        
        int diplExamId = Integer.parseInt(deWM.getId());
	    int applicationId = Integer.parseInt(appWM.getId());
        
	    if(diplExamId <= 0) {
	        return;
	    }
	    
        String operation = getOperationName(request);
        if(operation.equals("save")) {
            operation = "edit";
        }
        String group = getGroupName(request);
        String query = "activeForm=4&id=" + applicationId;
        request.getSession().setAttribute("backUrlDiplExam", 
                request.getContextPath() 
                + "/control" 
                + "/" + group 
                + "/" + operation
                + "?" + query);
		HttpSession session = request.getSession();
		Table table = (Table) session.getAttribute(WebKeys.TABLE_DIPL_EXAM_ATTCH);
		
		//boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request, TABLE_PREFIX);
		boolean reloadTable = true;
		if (reloadTable || table == null) {
			TableFactory tableFactory = TableFactory.getInstance();
			table = tableFactory.createTable();
			
			table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
			table.addColumnHeader(COLUMN_NAME_DOCFLOW_NUM, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_DOCUMENT_TYPE, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_FILE_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_DESC, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_PREVIEW, CellValueDef.CELL_VALUE_TYPE_STRING);
			
			//table.addColumnHeader(COLUMN_NAME_WEB_SITE, CellValueDef.CELL_VALUE_TYPE_STRING);
			//table.addColumnHeader(COLUMN_NAME_URL, CellValueDef.CELL_VALUE_TYPE_STRING);
			
			session.setAttribute(WebKeys.TABLE_DIPL_EXAM_ATTCH, table);
			resetTableData(request, diplExamId);

		}

		// TableState settings
		TableState tableState = (TableState) session
				.getAttribute(WebKeys.TABLE_DIPL_EXAM_ATTCH + WebKeys.TABLE_STATE);
		/**
		 * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
		 * vzeme posledniq tableState, togava se generira nov tableState!
		 */
		boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request, TABLE_PREFIX);
		if (tableState == null || !getLastTableState) {
			tableState = TableStateAndFiltersUtils.createBaseTableState(request, table, TABLE_PREFIX);
			session.setAttribute(WebKeys.TABLE_DIPL_EXAM_ATTCH + WebKeys.TABLE_STATE, tableState);
		}

		// TableWebModel SETTINGS
		TableWebModel webmodel = new TableWebModel("Списък на прикачените документи");
		webmodel.setViewOpenInNewWindow(true);
		webmodel.addRequestParam(TableWebModel.OPERATION_NAME_NEW, DIPL_EXAM_PARAM, diplExamId + "");
		webmodel.addRequestParam(TableWebModel.OPERATION_NAME_ALL, 
                UniversityValidityHandler.APPLICATION_ID_PARAM, applicationId + "");
		// webmodel.setColumnFormatter("userDate",
		// CellFormatter.DATE_TIME_FORMATTER);
		webmodel.setGroupName("dipl_exam_attachment");
		webmodel.insertTableData(table, tableState);
		request.setAttribute("diplExamTableWebModel", webmodel);
		//request.setAttribute(WebKeys.NEXT_SCREEN, "diploma_type_attachment_list");

		// Generating filters for displaying to user
		FiltersWebModel filtersWebModel = (FiltersWebModel) session
				.getAttribute(WebKeys.TABLE_DIPL_EXAM_ATTCH + WebKeys.FILTER_WEB_MODEL);
		/**
		 * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
		 * da se vzeme posledniq tableState(filterWebModel), togava se generira
		 * nov i se slaga v sesiqta!
		 */
		if (filtersWebModel == null || !getLastTableState) {
			filtersWebModel = new FiltersWebModel();
			
			session.setAttribute(WebKeys.TABLE_DIPL_EXAM_ATTCH
					+ WebKeys.FILTER_WEB_MODEL, filtersWebModel);
		}
		request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
	}

	@SuppressWarnings("rawtypes")
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
		
		
		HttpSession session = request.getSession();

		// create file upload factory and upload servlet
		DiskFileItemFactory factory = new DiskFileItemFactory();
		
		FileCleaningTracker pTracker = FileCleanerCleanup
				.getFileCleaningTracker(getServletContext());
		factory.setFileCleaningTracker(pTracker);
		ServletFileUpload upload = new ServletFileUpload(factory);

		// set file upload progress listener
		FileUploadListener listener = new FileUploadListener();

		session.setAttribute(WebKeys.FILE_UPLOAD_LISTENER, listener);

		// upload servlet allows to set upload listener
		upload.setProgressListener(listener);

		List uploadedItems = null;
		
		try {
			// iterate over all uploaded files
			uploadedItems = upload.parseRequest(request);

			InputStream is = null;
			String docDescr = null;
			Integer docTypeId = null;
			int diplExamId = 0;
			int id = 0;
			String fileName = null;
			String contentType = null;
			int fileSize = 0;
            boolean generate = false;
            int applicationId = 0;
            
            InputStream scannedIs = null;
            String scannedContentType = null;
            String scannedFileName = null;
            int scannedFileSize = 0;
            
            Integer eventStatus = null;
            Integer eventType = null;
                        
            String docflowUrl = null;
            
			Iterator iter = uploadedItems.iterator();
			while (iter.hasNext()) {
			    FileItem item = (FileItem) iter.next();

			    if (item.isFormField()) {
			    	if(item.getFieldName().equals("docDescr"))
			        	docDescr = item.getString("UTF-8");
			        else if(item.getFieldName().equals("id"))
			        	id = DataConverter.parseInt(item.getString("UTF-8"), 0);
			        else if(item.getFieldName().equals("diplExamId"))
			            diplExamId = DataConverter.parseInt(item.getString("UTF-8"), 0);
			        else if(item.getFieldName().equals("docTypeId"))
			        	docTypeId = DataConverter.parseInteger(item.getString("UTF-8"), null);
			        else if (item.getFieldName().equals("generate")) {
                        generate = DataConverter.parseBoolean(item.getString("UTF-8"));
                    }
			        else if (item.getFieldName().equals(UniversityValidityHandler.APPLICATION_ID_PARAM)) {
                        applicationId = Integer.parseInt(item.getString("UTF-8"));
                    }
			        else if (item.getFieldName().equals("docflowUrl")) {
                        docflowUrl = item.getString("UTF-8");
                    }
			        else if (item.getFieldName().equals("eventStatus")) {
                        eventStatus = DataConverter.parseInteger(item.getString("UTF-8"), null);
                    }
                    else if (item.getFieldName().equals("eventType")) {
                        eventType = DataConverter.parseInteger(item.getString("UTF-8"), null);
                    }
			    } 
			    else {
			        if (item.getFieldName().equals("doc_content")) {
                        fileSize = (int) item.getSize();
                        if (fileSize > 0) {
                            is = item.getInputStream();
                            fileName = prepareFileName(item.getName());
                            contentType = item.getContentType();
                        }
                    }
                    else if (item.getFieldName().equals("scanned_content")) {
                        scannedFileSize = (int) item.getSize();
                        if (scannedFileSize > 0) {
                            scannedIs = item.getInputStream();
                            scannedFileName = prepareFileName(item.getName());    
                            scannedContentType = item.getContentType();
                        }
                    }
			    }
			}
			
			AttachmentDataProvider attDP = getAttachmentDataProvider();
			
			Attachment attachment = attDP.getAttachment(id, false, false);
            if (id != 0 && attachment == null) {
				throw new UnknownRecordException("Unknown attachment ID:" + id);
			}
            
			if(generate) {
                NomenclaturesDataProvider nomDP = getNacidDataProvider().getNomenclaturesDataProvider();
                DocumentType docType = nomDP.getDocumentType(docTypeId);
                if(docType != null && !docType.isIncoming() 
                        && docType.getDocumentTemplate() != null 
                        && docType.getDocumentTemplate().length() > 0) {
                    
                    contentType = "application/msword";
                    Application appl = getNacidDataProvider().getApplicationsDataProvider().getApplication(applicationId);
                    fileName = appl.getApplicationNumber() + "_" 
                        +docType.getDocumentTemplate()  + "_"
                        + DataConverter.formatDate(new Date()) + ".doc";
                    fileName = fileName.replace("/", "_");
                    is = TemplateGenerator.generateDocFromTemplate(
                            getNacidDataProvider(), applicationId, docType);
                    fileSize = is.available();
                }
                else {
                    contentType = null;
                    fileName = null;
                    is = null;
                    fileSize = 0;
                }
            }
			
			String docflowNum = attachment != null ? attachment.getDocflowNum() : null;
			if (id == 0 && fileSize <= 0) {
			    String errMsg = "Не е посочен файл";
                if(generate) {
                    errMsg = MessagesBundle.getMessagesBundle().getValue("autogeneratedDocsConditions");
                }
                SystemMessageWebModel webmodel = new SystemMessageWebModel(errMsg);
                request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
				request.setAttribute(WebKeys.DIPL_EXAM_ATTCH_WEB_MODEL, 
						new DiplExamAttachmentWebModel(0, diplExamId, docDescr, 
								"", applicationId+"", null, docflowNum, docTypeId, docflowUrl));
				
			}
			else if(!areFilenamesAllowed(fileName, scannedFileName, id, getAttachmentDataProvider())) {
	            SystemMessageWebModel webmodel = new SystemMessageWebModel("За този документ вече е прикачен файл с това име");
	            request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
	            request.setAttribute(WebKeys.APPLICATION_ATTCH_WEB_MODEL, 
	                    new ApplicationAttachmentWebModel(0, applicationId, docDescr, "", null, docflowNum, docTypeId, docflowUrl));
	        }
	        else {
	            
	            if(docTypeId == null && attachment != null) docTypeId = attachment.getDocTypeId();
                	            
			    int newId = attDP.saveAttacment(id, diplExamId, docDescr, docTypeId, 
			            null, 
			            attachment != null ? attachment.getDocflowId() : null, 
		                attachment != null ? attachment.getDocflowDate() : null, 
		                contentType, fileName, is, fileSize, 
			            scannedContentType, scannedFileName, scannedIs, scannedFileSize, getLoggedUser(request, response).getUserId());
				attachment = attDP.getAttachment(newId, false, false);
				request.setAttribute(WebKeys.SYSTEM_MESSAGE,
						new SystemMessageWebModel("Данните бяха въведени в базата",
								SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
				resetTableData(request, diplExamId);
				
				eventStatus = saveReminder(eventType, eventStatus, attachment, applicationId);
                				
				docflowUrl = getDocFlowUrl(getNacidDataProvider(), applicationId, newId, 
                        getEditUrl(newId, applicationId + ""));
                
				
				request.setAttribute(WebKeys.DIPL_EXAM_ATTCH_WEB_MODEL, 
						new DiplExamAttachmentWebModel(
								newId, diplExamId, docDescr, 
								attachment.getFileName(), applicationId+"",
								attachment.getScannedFileName(),
								attachment.getDocflowNum(),
								attachment.getDocTypeId(),
								docflowUrl));
				
			}
			generateDocTypesComboBox(null, docTypeId, request);
			
			Event ev = null;
            if(attachment != null) {
                ev = Utils.getListFirstElement(getNacidDataProvider().getEventDataProvider()
                        .getEventsForDocument(attachment.getId(), attachment.getDocType().getId()));
                request.setAttribute(WebKeys.APPLICATION_ID, applicationId);
            }
			generateEventStatusCombo(ev != null ? ev.getEventStatus() : null, request);
            generateEventTypeCombo(ev != null ? ev.getEventTypeId() : null, request);
            
            addEventDatesToRequest(request, ev, getNacidDataProvider());
			
		} catch (Exception e) {
			throw Utils.logException(this, e);
		}
		finally {
			session.removeAttribute(WebKeys.FILE_UPLOAD_LISTENER);
		}
		
		request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
		request.getSession().removeAttribute(WebKeys.TABLE_DIPL_EXAM_ATTCH);
		
	}

	public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
		int attId = DataConverter.parseInt(request.getParameter("id"), -1);
	    if (attId <= 0) {
	      throw new UnknownRecordException("Unknown attachment Id:" + attId);
	    }
	    
	    AttachmentDataProvider attDP = getAttachmentDataProvider();
	    Attachment att = attDP.getAttachment(attId, false, false);
	    NacidDataProvider nacidDataProvider = getNacidDataProvider();
	    ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
	    
	    int applicationId = applicationsDataProvider.getApplicationIdByDiplomaExaminationId(att.getParentId());
	    request.setAttribute(WebKeys.APPLICATION_ID, applicationId);
	    attDP.deleteAttachment(attId);
	    
	    request.getSession().removeAttribute(WebKeys.TABLE_DIPL_EXAM_ATTCH);
	    //handleList(request, response);
	    try {
			response.sendRedirect((String) request.getSession().getAttribute("backUrlDiplExam"));
		} catch (IOException e) {
			throw Utils.logException(e);
		}
	}
	
	private void resetTableData(HttpServletRequest request, int diplExamId) {
		Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_DIPL_EXAM_ATTCH);
		if (table == null) {
			return;
		}
		table.emtyTableData();
		AttachmentDataProvider attDP = getAttachmentDataProvider();
		
		List<Attachment> attachments = attDP.getAttachmentsForParent(diplExamId);
		
		if (attachments != null) {
			for (Attachment att : attachments) {
			    
			    String fileName = att.getScannedFileName() == null ? att.getFileName() : att.getScannedFileName();
                
				try {
				    NomenclaturesDataProvider nomDP = getNacidDataProvider().getNomenclaturesDataProvider();
				    DocumentType documentType = nomDP.getDocumentType(att.getDocTypeId());
                    String docType = documentType == null ? "": documentType.getLongName();
                    
					table.addRow(att.getId(), att.getDocflowNum(), docType, fileName,
							att.getDocDescr(), "")
							.setDeletable(StringUtils.isEmpty(att.getDocflowNum()));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (CellCreationException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	 private static String getEditUrl(int id, String applId) {
	     return "/control/dipl_exam_attachment/edit?id=" + id
	         + "&" + UniversityValidityHandler.APPLICATION_ID_PARAM + "=" + applId;
	 }
		
	@Override
	protected int getDocTypeCategory(Integer applicationId) {
	    return DocCategory.DIPLOMA_EXAMINATIONS;
	}
	
	@Override
	protected AttachmentDataProvider getAttachmentDataProvider() {
	    return getNacidDataProvider().getDiplExamAttachmentDataProvider();
	}
}
