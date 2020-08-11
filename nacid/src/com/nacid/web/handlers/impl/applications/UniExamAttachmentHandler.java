package com.nacid.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Application;
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
import com.nacid.web.model.applications.ApplicationAttachmentWebModel;
import com.nacid.web.model.applications.UniExamAttachmentWebModel;
import com.nacid.web.model.applications.UniversityValidityWebModel;
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

public class UniExamAttachmentHandler extends BaseAttachmentHandler {

	private static final String COLUMN_NAME_ID = "id";
	private static final String COLUMN_NAME_DOCFLOW_NUM = "Деловоден номер";
	private static final String COLUMN_NAME_DOCUMENT_TYPE = "Тип документ";
	
	private static final String COLUMN_NAME_FILE_NAME = "Име";
	private static final String COLUMN_NAME_DESC = "Описание";
	private static final String COLUMN_NAME_PREVIEW = "Preview";
	
	
	private static final String UNIVERSITY_VALIDITY_PARAM = "uniValId";
	private static final String EDIT_SCREEN = "uni_exam_attachment_edit";
	
	
	
	
	public UniExamAttachmentHandler(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void handleNew(HttpServletRequest request, HttpServletResponse response) {
		int universityValidityId = Integer.parseInt(request.getParameter(UNIVERSITY_VALIDITY_PARAM));
		
		
		request.setAttribute(WebKeys.UNI_EXAM_ATTCH_WEB_MODEL, 
				new UniExamAttachmentWebModel(universityValidityId,
				        request.getParameter(UniversityValidityHandler.APPLICATION_ID_PARAM), request.getParameter(UniversityValidityHandler.APPLICATION_GROUP_PARAM)));
		generateDocTypesComboBox(null, null, request);
		request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
		
		generateEventTypeCombo(null, request);
	}
	
	@Override
	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
		
	    int applicationId = DataConverter.parseInt(request.getParameter(UniversityValidityHandler.APPLICATION_ID_PARAM), 0);
	    String groupName = request.getParameter(UniversityValidityHandler.APPLICATION_GROUP_PARAM);
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
		generateDocTypesComboBox(applicationId, att.getDocTypeId(), request);
		
		
		String dfUrl = getDocFlowUrl(getNacidDataProvider(), applicationId, attachmentId, 
                getEditUrl(attachmentId, applicationId + "", groupName));
		
		request.setAttribute(WebKeys.UNI_EXAM_ATTCH_WEB_MODEL, 
				new UniExamAttachmentWebModel(att.getId(), att.getParentId(),
						att.getDocDescr(), att.getFileName(), applicationId + "", 
						groupName, att.getScannedFileName(), att.getDocflowNum(), att.getDocTypeId(), dfUrl));

		Event ev = Utils.getListFirstElement(getNacidDataProvider().getEventDataProvider()
                .getEventsForDocument(attachmentId, att.getDocType().getId()));
        generateEventStatusCombo(ev != null ? ev.getEventStatus() : null, request);
        generateEventTypeCombo(ev != null ? ev.getEventTypeId() : null, request);
        
        addEventDatesToRequest(request, ev, getNacidDataProvider());
    }

	private String getEditUrl(int attachmentId, String applicationId, String groupName) {
        return "/control/uni_exam_attachment/edit?id=" + attachmentId
            + "&" + UniversityValidityHandler.APPLICATION_ID_PARAM + "=" + applicationId
            + "&" + UniversityValidityHandler.APPLICATION_GROUP_PARAM + "=" + groupName;
    }

    public void handleList(HttpServletRequest request, HttpServletResponse response) {
		
	    //int universityValidityId = DataConverter.parseInt(request.getParameter("id"), -1);
	    UniversityValidityWebModel uvWM = (UniversityValidityWebModel)request.getAttribute(WebKeys.UNIVERSITY_VALIDITY_WEB_MODEL);
	    int universityValidityId = uvWM.getId();
	    int appId = uvWM.getAppId();
	    String appOper = uvWM.getAppOper();
	    //int appId = Integer.parseInt(request.getParameter(UniversityValidityHandler.APPLICATION_ID_PARAM));
	    //String appOper = request.getParameter(UniversityValidityHandler.APPLICATION_OPERATION_PARAM);
	    String operation = getOperationName(request);
	    if (operation.equals("save")) {
	        operation = "edit";
	    }
		request.getSession().setAttribute("backUrlUniExam", request.getContextPath()
				+ "/control" 
				+ "/" + getGroupName(request)
				+ "/" + operation
				+ "?id=" + universityValidityId 
				+ "&" + UniversityValidityHandler.APPLICATION_ID_PARAM + "=" + appId
				+ "&" + UniversityValidityHandler.APPLICATION_OPERATION_PARAM + "=" + appOper
				+ "&" + UniversityValidityHandler.APPLICATION_GROUP_PARAM + "=" + uvWM.getGroupName());
		
		HttpSession session = request.getSession();
		Table table = (Table) session.getAttribute(WebKeys.TABLE_UNI_EXAM_ATTCH);
		
		//boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
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
			
			session.setAttribute(WebKeys.TABLE_UNI_EXAM_ATTCH, table);
			resetTableData(request, universityValidityId);

		}

		// TableState settings
		TableState tableState = (TableState) session
				.getAttribute(WebKeys.TABLE_UNI_EXAM_ATTCH + WebKeys.TABLE_STATE);
		/**
		 * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
		 * vzeme posledniq tableState, togava se generira nov tableState!
		 */
		boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
		if (tableState == null || !getLastTableState) {
			tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
			session.setAttribute(WebKeys.TABLE_UNI_EXAM_ATTCH + WebKeys.TABLE_STATE, tableState);
		}

		// TableWebModel SETTINGS
		TableWebModel webmodel = new TableWebModel("Списък на прикачените документи");
		webmodel.setViewOpenInNewWindow(true);
		webmodel.addRequestParam(TableWebModel.OPERATION_NAME_NEW, UNIVERSITY_VALIDITY_PARAM, universityValidityId + "");
		webmodel.addRequestParam(TableWebModel.OPERATION_NAME_ALL, 
		        UniversityValidityHandler.APPLICATION_ID_PARAM, appId + "");
		webmodel.addRequestParam(TableWebModel.OPERATION_NAME_ALL, 
		        UniversityValidityHandler.APPLICATION_GROUP_PARAM, uvWM.getGroupName());
		// webmodel.setColumnFormatter("userDate",
		// CellFormatter.DATE_TIME_FORMATTER);
		webmodel.setGroupName("uni_exam_attachment");
		webmodel.insertTableData(table, tableState);
		request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
		//request.setAttribute(WebKeys.NEXT_SCREEN, "diploma_type_attachment_list");

		// Generating filters for displaying to user
		FiltersWebModel filtersWebModel = (FiltersWebModel) session
				.getAttribute(WebKeys.TABLE_UNI_EXAM_ATTCH + WebKeys.FILTER_WEB_MODEL);
		/**
		 * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
		 * da se vzeme posledniq tableState(filterWebModel), togava se generira
		 * nov i se slaga v sesiqta!
		 */
		if (filtersWebModel == null || !getLastTableState) {
			filtersWebModel = new FiltersWebModel();
			
			session.setAttribute(WebKeys.TABLE_UNI_EXAM_ATTCH
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
			int universityValidityId = 0;
			int id = 0;
			String fileName = null;
			String contentType = null;
			int fileSize = 0;
            boolean generate = false;
            int applicationId = 0;
            String groupName = "";
            
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
			        else if(item.getFieldName().equals("universityValidityId"))
			        	universityValidityId = DataConverter.parseInt(item.getString("UTF-8"), 0);
			        else if(item.getFieldName().equals("docTypeId"))
			        	docTypeId = DataConverter.parseInteger(item.getString("UTF-8"), 0);
			        else if (item.getFieldName().equals("generate")) {
	                    generate = DataConverter.parseBoolean(item.getString("UTF-8"));
	                }
			        else if (item.getFieldName().equals(UniversityValidityHandler.APPLICATION_ID_PARAM)) {
			            applicationId = Integer.parseInt(item.getString("UTF-8"));
			        }
			        else if (item.getFieldName().equals(UniversityValidityHandler.APPLICATION_GROUP_PARAM)) {
			            groupName = item.getString("UTF-8");
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
				request.setAttribute(WebKeys.UNI_EXAM_ATTCH_WEB_MODEL, 
						new UniExamAttachmentWebModel(0, universityValidityId, docDescr, 
								"", applicationId+"", groupName, null, docflowNum, docTypeId, docflowUrl));
				
			}
            else if(!areFilenamesAllowed(fileName, scannedFileName, id, getAttachmentDataProvider())) {
                SystemMessageWebModel webmodel = new SystemMessageWebModel("За този документ вече е прикачен файл с това име");
                request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
                request.setAttribute(WebKeys.APPLICATION_ATTCH_WEB_MODEL, 
                        new ApplicationAttachmentWebModel(0, applicationId, docDescr, "", null, docflowNum, docTypeId, docflowUrl));
            }
            else {
                if(docTypeId == null && attachment != null) docTypeId = attachment.getDocTypeId();
                                
				int newId = attDP.saveAttacment(id, universityValidityId, docDescr, 
				        docTypeId, null, 
				        attachment != null ? attachment.getDocflowId() : null, 
		                attachment != null ? attachment.getDocflowDate() : null, 
		                contentType, fileName, is, fileSize, 
				        scannedContentType, scannedFileName, scannedIs, scannedFileSize, getLoggedUser(request, response).getUserId());
				attachment = attDP.getAttachment(newId, false, false);
				request.setAttribute(WebKeys.SYSTEM_MESSAGE,
						new SystemMessageWebModel("Данните бяха въведени в базата",
								SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
				resetTableData(request, universityValidityId);
				
				eventStatus = saveReminder(eventType, eventStatus, attachment, applicationId);
                				
				docflowUrl = getDocFlowUrl(getNacidDataProvider(), applicationId, newId, 
                        getEditUrl(newId, applicationId+"", groupName));
                
				request.setAttribute(WebKeys.UNI_EXAM_ATTCH_WEB_MODEL, 
						new UniExamAttachmentWebModel(
								newId, universityValidityId, docDescr, 
								attachment.getFileName(), applicationId+"", groupName, 
								attachment.getScannedFileName(),
								attachment.getDocflowNum(), attachment.getDocTypeId(), docflowUrl));
				
				
			}
			
			generateDocTypesComboBox(applicationId, docTypeId, request);
	        
			Event ev = null;
            if(attachment != null) {
                ev = Utils.getListFirstElement(getNacidDataProvider().getEventDataProvider()
                        .getEventsForDocument(attachment.getId(), attachment.getDocType().getId()));
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
		request.getSession().removeAttribute(WebKeys.TABLE_UNI_EXAM_ATTCH);
		
	}

	public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
		int attId = DataConverter.parseInt(request.getParameter("id"), -1);
	    if (attId <= 0) {
	      throw new UnknownRecordException("Unknown attachment Id:" + attId);
	    }
	    
	    AttachmentDataProvider attDP = getAttachmentDataProvider();
	    attDP.deleteAttachment(attId);
	    
	    request.getSession().removeAttribute(WebKeys.TABLE_UNI_EXAM_ATTCH);
	    //handleList(request, response);
	    try {
			response.sendRedirect((String) request.getSession().getAttribute("backUrlUniExam"));
		} catch (IOException e) {
			throw Utils.logException(e);
		}
	}
	
	private void resetTableData(HttpServletRequest request, int universityValidityId) {
		Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_UNI_EXAM_ATTCH);
		if (table == null) {
			return;
		}
		table.emtyTableData();
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
		AttachmentDataProvider attDP = getAttachmentDataProvider();
		
		List<Attachment> attachments = attDP.getAttachmentsForParent(universityValidityId);
		
		if (attachments != null) {
			for (Attachment att : attachments) {
				
			    String fileName = att.getScannedFileName() == null ? att.getFileName() : att.getScannedFileName();
				try {
					DocumentType documentType = nomenclaturesDataProvider.getDocumentType(att.getDocTypeId());
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
	
	@Override
	protected int getDocTypeCategory(Integer applicationId) {
	    return DocCategory.UNIVERSITY_EXAMINATIONS;
	}
	
	@Override
	protected AttachmentDataProvider getAttachmentDataProvider() {
	    return getNacidDataProvider().getUniExamAttachmentDataProvider();
	}
	
}
