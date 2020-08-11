package com.ext.nacid.web.handlers.impl.expert;

import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.ext.nacid.web.handlers.UserAccessUtils;
import com.ext.nacid.web.handlers.impl.applications.ExtApplicationAttachmentHandler;
import com.ext.nacid.web.model.expert.ExpertStatementWebModel;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.ExpertStatementAttachment;
import com.nacid.bl.applications.ExpertStatementAttachmentDataProvider;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.comision.ComissionMemberDataProvider;
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
import com.nacid.web.handlers.impl.applications.FileUploadListener;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

import static com.nacid.web.handlers.impl.applications.BaseAttachmentHandler.writeEmptyImage;

public class ExpertStatementsHandler extends NacidExtBaseRequestHandler {

	private static final String COLUMN_NAME_ID = "id";
	private static final String COLUMN_NAME_EXPERT = "Експерт";
	private static final String COLUMN_NAME_DOCUMENT_TYPE = "Тип документ";
	private static final String COLUMN_NAME_FILE_NAME = "Име на файл";
	private static final String COLUMN_NAME_DESC = "Описание";
	private static final String COLUMN_NAME_PREVIEW = "Preview";
	
	private static final String TABLE_PREFIX = "";
	private final static String EDIT_SCREEN = "expert_statement_edit";
	
	public final static String APPL_ID_PARAM = "applId";

	private ServletContext servletContext;
    
	
	private class ImgObserver implements ImageObserver {
		
		boolean ready = false;
		@Override
		public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
			return ready = (infoflags & ALLBITS) != 0;
		}
	};
	
	public ExpertStatementsHandler(ServletContext servletContext) {
		super(servletContext);
		this.servletContext = servletContext;
	}
	
	@Override
	public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        int applicationId = Integer.parseInt(request.getParameter(APPL_ID_PARAM));
        
        
        request.setAttribute(WebKeys.EXPERT_STATEMENT_WEB_MODEL, 
                new ExpertStatementWebModel(0, "", "", applicationId));
        generateDocTypesComboBox(null, request);
        setNextScreen(request, EDIT_SCREEN);
    }

	@Override
	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
	    int applicationId = Integer.parseInt(request.getParameter(APPL_ID_PARAM));
        
        int attachmentId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (attachmentId <= 0) {
            throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
        }
        setNextScreen(request, EDIT_SCREEN);
        NacidDataProvider nacidDataProvider = getNacidDataProvider();

        ExpertStatementAttachmentDataProvider attDP = nacidDataProvider.getExpertStatementAttachmentDataProvider();
        
        ExpertStatementAttachment att = attDP.getExpertStatementAttachment(attachmentId, false);
        
        if (att == null) {
            throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
        }
        
        UserAccessUtils.checkExpertAccessToStatement(
                getLoggedUser(request, response), att, nacidDataProvider);
        
        generateDocTypesComboBox(att.getDocTypeId(), request);
        
        request.setAttribute(WebKeys.EXPERT_STATEMENT_WEB_MODEL, 
                new ExpertStatementWebModel(att.getId(),
                        att.getDocDescr(), att.getFileName(), 
                        applicationId));

    }
	
	@SuppressWarnings("rawtypes")
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        
        
        HttpSession session = request.getSession();

        // create file upload factory and upload servlet
        DiskFileItemFactory factory = new DiskFileItemFactory();
        
        FileCleaningTracker pTracker = FileCleanerCleanup
                .getFileCleaningTracker(servletContext);
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
            int docTypeId = 0;
            int diplExamId = 0;
            int id = 0;
            String fileName = null;
            String contentType = null;
            int fileSize = 0;
            int applicationId = 0;
            
            Iterator iter = uploadedItems.iterator();
            boolean generate = false;
            String errMsg = null;
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                if (item.isFormField()) {
                    if(item.getFieldName().equals("docDescr"))
                        docDescr = item.getString("UTF-8");
                    else if(item.getFieldName().equals("id"))
                        id = DataConverter.parseInt(item.getString("UTF-8"), 0);
                    else if(item.getFieldName().equals("docTypeId"))
                        docTypeId = DataConverter.parseInt(item.getString("UTF-8"), 0);
                    else if (item.getFieldName().equals(APPL_ID_PARAM)) {
                        applicationId = Integer.parseInt(item.getString("UTF-8"));
                    } else if (item.getFieldName().equals("generate")) {
                        generate = DataConverter.parseBoolean(item.getString("UTF-8"));
                    }
                } 
                else {
                    fileSize = (int)item.getSize();
                    if(fileSize > 0) {
                        is = item.getInputStream();
                        fileName = item.getName();
                        int index;
                        if((index = fileName.lastIndexOf('\\')) != -1) {
                            fileName = fileName.substring(index + 1, fileName.length());
                        }
                        if((index = fileName.lastIndexOf('/')) != -1) {
                            fileName = fileName.substring(index + 1, fileName.length());
                        }
                        if(fileName.equals("")) {
                            fileName = "file";
                        }
                        contentType = item.getContentType();
                    }
                }
            }
            
			if (generate && id == 0) {
				NomenclaturesDataProvider nomDP = getNacidDataProvider().getNomenclaturesDataProvider();
				DocumentType docType = nomDP.getDocumentType(docTypeId);

				contentType = null;
				fileName = null;
				is = null;
				fileSize = 0;
				//System.out.println("DocType:" + docType.getId() + " template:" + docType.getDocumentTemplate());
				if (docType != null && !StringUtils.isEmpty(docType.getDocumentTemplate())) {
					contentType = "application/msword";
					Application appl = getNacidDataProvider().getApplicationsDataProvider().getApplication(applicationId);
					fileName = appl.getApplicationNumber() + "_" + docType.getDocumentTemplate() + "_" + DataConverter.formatDate(new Date())
							+ ".doc";
					fileName = fileName.replace("/", "_");
					Map<String, Object> additionalParams = new HashMap<>();
					additionalParams.put("expertName", getLoggedUser(request, response).getFullName());
					is = TemplateGenerator.generateDocFromTemplate(getNacidDataProvider(), applicationId, docType, additionalParams);
					fileSize = is.available();

				} else {
					errMsg = MessagesBundle.getMessagesBundle().getValue("autogeneratedDocsConditionsExt");
				}
			}
            
            
            ExpertStatementAttachmentDataProvider attDP = getNacidDataProvider().getExpertStatementAttachmentDataProvider();
            ExpertStatementAttachment oldAtt = attDP.getExpertStatementAttachment(id, false); 
            if (id != 0 && oldAtt == null) {
                throw new UnknownRecordException("Unknown attachment ID:" + id);
            }
            
            if(oldAtt != null) {
                UserAccessUtils.checkExpertAccessToStatement(getLoggedUser(request, response), oldAtt, getNacidDataProvider());
            }
            else {
                UserAccessUtils.checkExpertAccessToApplication(
                        getLoggedUser(request, response), applicationId, getNacidDataProvider());
            }
            
            
            if (id == 0 && fileSize <= 0) {
            	if(errMsg == null) {
                    errMsg = "Не е посочен файл";
                }
            	SystemMessageWebModel webmodel = new SystemMessageWebModel(errMsg);
                request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
                request.setAttribute(WebKeys.EXPERT_STATEMENT_WEB_MODEL, 
                        new ExpertStatementWebModel(id, docDescr, fileName, applicationId));
                
            } else {
                ComissionMemberDataProvider cmdp = getNacidDataProvider().getComissionMemberDataProvider();
                int newId = attDP.saveExpertStatementAttacment(id, 
                        cmdp.getComissionMemberByUserId(getLoggedUser(request, response).getUserId()).getId(), 
                        docDescr, docTypeId, 
                        is, fileName, contentType, fileSize, applicationId); 
                ExpertStatementAttachment newAttachment = attDP.getExpertStatementAttachment(newId, false);
                request.setAttribute(WebKeys.SYSTEM_MESSAGE,
                        new SystemMessageWebModel("Данните бяха въведени в базата",
                                SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
                resetTableData(request, diplExamId);
                request.setAttribute(WebKeys.EXPERT_STATEMENT_WEB_MODEL, 
                        new ExpertStatementWebModel(
                                newId, docDescr, 
                                newAttachment.getFileName(), applicationId));
                
            }
            generateDocTypesComboBox(docTypeId, request);
        } catch (Exception e) {
            throw Utils.logException(this, e);
        }
        finally {
            session.removeAttribute(WebKeys.FILE_UPLOAD_LISTENER);
        }
        
        setNextScreen(request, EDIT_SCREEN);
        request.getSession().removeAttribute(WebKeys.TABLE_EXPERT_STATEMENT);
        
    }

	@Override
    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        int attId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (attId <= 0) {
          throw new UnknownRecordException("Unknown attachment Id:" + attId);
        }
        
        ExpertStatementAttachmentDataProvider attDP = getNacidDataProvider().getExpertStatementAttachmentDataProvider();
        
        ExpertStatementAttachment oldAtt = attDP.getExpertStatementAttachment(attId, false); 
        if (oldAtt == null) {
            throw new UnknownRecordException("Unknown attachment ID:" + attId);
        }
        
        UserAccessUtils.checkExpertAccessToStatement(getLoggedUser(request, response), oldAtt, getNacidDataProvider());
        
        
        attDP.deleteRecord(attId);
        
        request.getSession().removeAttribute(WebKeys.TABLE_EXPERT_STATEMENT);
        //handleList(request, response);
        try {
            response.sendRedirect(
                    request.getContextPath() + "/control/expert_application/edit?activeForm=2&id="+oldAtt.getApplicationId());
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }
    

	
	@Override
	public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
		int attachmentId = DataConverter.parseInt(request.getParameter("id"), -1);
		if (attachmentId <= 0) {
			throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
		}
		
		ExpertStatementAttachmentDataProvider axpAttDP = getNacidDataProvider().getExpertStatementAttachmentDataProvider();
		
		ExpertStatementAttachment att = axpAttDP.getExpertStatementAttachment(attachmentId, true);
		
		if (att == null) {
			throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
		}
		
		response.setContentType(att.getContentType());
		
		InputStream is = att.getContentStream();
		try {
			ServletOutputStream sos = response.getOutputStream();
			
			int read = 0;
			byte[] buf = new byte[1024];
			
			while((read = is.read(buf)) > 0) {
				sos.write(buf, 0, read);
			}
			
		}
		catch(Exception e) {
			throw Utils.logException(e);
		}
		finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void handleView(HttpServletRequest request, HttpServletResponse response){
		
		response.setContentType("image/jpeg");
		
		int attachmentId = DataConverter.parseInt(request.getParameter("id"), -1);
		if (attachmentId <= 0) {
			writeEmptyImage(response);
			return;
		}
		
		ExpertStatementAttachmentDataProvider attDP = getNacidDataProvider().getExpertStatementAttachmentDataProvider();
		
		ExpertStatementAttachment att = attDP.getExpertStatementAttachment(attachmentId, true);
		
		if (att == null) {
			writeEmptyImage(response);
			return;
		}
		
		InputStream is = att.getContentStream();
		try {
			ServletOutputStream sos = response.getOutputStream();
			
			
			if(!att.getContentType().startsWith("image") ) {
				writeEmptyImage(response);
				return;
			}
			
			BufferedImage src = ImageIO.read(is);
			if(src == null) {
				writeEmptyImage(response);
				return;
			}
			
				
			int srcW = src.getWidth();
			int srcH = src.getHeight();

			int w = DataConverter.parseInt(request.getParameter("width"), -1);
			BufferedImage out = src;
			if (w < src.getWidth()) {
				float multipl = (float) srcW / (float) w;
				int h = (int) ((float) srcH / multipl);

				out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				Graphics g = out.getGraphics();

				ImgObserver observer = new ImgObserver();
				boolean ready = g.drawImage(src, 0, 0, w, h, 0, 0, srcW, srcH, observer);
				while (!ready) {
					Thread.sleep(100);
					ready = observer.ready;
				}

			}
			ImageIO.write(out, "jpg", sos);
		}
		catch(Exception e) {
			throw Utils.logException(e);
		}
		finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	public void handleList(HttpServletRequest request, HttpServletResponse response) {
		
	    int applId = Integer.parseInt(request.getParameter("id"));
        
	    String operation = getOperationName(request);
        if(operation.equals("save")) {
            operation = "edit";
        }
        String group = getGroupName(request);
        String query = "activeForm=5&id=" + applId;
        request.setAttribute("backUrlExpStatement", 
                servletContext.getAttribute("pathPrefix") 
                + "/control" 
                + "/" + group 
                + "/" + operation
                + "?" + query);
	    
	    
		HttpSession session = request.getSession();
		Table table = (Table) session.getAttribute(WebKeys.TABLE_EXPERT_STATEMENT);
		
		//boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request, TABLE_PREFIX);
		boolean reloadTable = true;
		if (reloadTable || table == null) {
			TableFactory tableFactory = TableFactory.getInstance();
			table = tableFactory.createTable();
			
			table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
			table.addColumnHeader(COLUMN_NAME_EXPERT, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_DOCUMENT_TYPE, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_FILE_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_DESC, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_PREVIEW, CellValueDef.CELL_VALUE_TYPE_STRING);
			
			//table.addColumnHeader(COLUMN_NAME_WEB_SITE, CellValueDef.CELL_VALUE_TYPE_STRING);
			//table.addColumnHeader(COLUMN_NAME_URL, CellValueDef.CELL_VALUE_TYPE_STRING);
			
			session.setAttribute(WebKeys.TABLE_EXPERT_STATEMENT, table);
			resetTableData(request, applId);

		}

		// TableState settings
		TableState tableState = (TableState) session
				.getAttribute(WebKeys.TABLE_EXPERT_STATEMENT + WebKeys.TABLE_STATE);
		/**
		 * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
		 * vzeme posledniq tableState, togava se generira nov tableState!
		 */
		boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request, TABLE_PREFIX);
		if (tableState == null || !getLastTableState) {
			tableState = TableStateAndFiltersUtils.createBaseTableState(request, table, TABLE_PREFIX);
			session.setAttribute(WebKeys.TABLE_EXPERT_STATEMENT + WebKeys.TABLE_STATE, tableState);
		}

		// TableWebModel SETTINGS
		TableWebModel webmodel = new TableWebModel(null);
		webmodel.setViewOpenInNewWindow(true);
		// webmodel.setColumnFormatter("userDate",
		// CellFormatter.DATE_TIME_FORMATTER);
		webmodel.setGroupName("expert_statement");
		webmodel.insertTableData(table, tableState);
		webmodel.addRequestParam(TableWebModel.OPERATION_NAME_ALL, 
                APPL_ID_PARAM, applId+"");
		request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
		//request.setAttribute(WebKeys.NEXT_SCREEN, "diploma_type_attachment_list");

		// Generating filters for displaying to user
		FiltersWebModel filtersWebModel = (FiltersWebModel) session
				.getAttribute(WebKeys.TABLE_EXPERT_STATEMENT + WebKeys.FILTER_WEB_MODEL);
		/**
		 * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
		 * da se vzeme posledniq tableState(filterWebModel), togava se generira
		 * nov i se slaga v sesiqta!
		 */
		if (filtersWebModel == null || !getLastTableState) {
			filtersWebModel = new FiltersWebModel();
			
			session.setAttribute(WebKeys.TABLE_EXPERT_STATEMENT
					+ WebKeys.FILTER_WEB_MODEL, filtersWebModel);
		}
		request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
	}

	
	
	private void resetTableData(HttpServletRequest request, int applicationId) {
		Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_EXPERT_STATEMENT);
		if (table == null) {
			return;
		}
		table.emtyTableData();
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		ExpertStatementAttachmentDataProvider attDP = nacidDataProvider.getExpertStatementAttachmentDataProvider();
		
		List<ExpertStatementAttachment> attachments = attDP.getExpertStatementsForApplication(applicationId);
		
		if (attachments != null) {
			for (ExpertStatementAttachment att : attachments) {
				
				try {
				    ComissionMemberDataProvider comMemDP = getNacidDataProvider().getComissionMemberDataProvider();
				    ComissionMember mem = comMemDP.getComissionMember(att.getExpertId());
                    String expert = mem.getFullName();
                    
                    NomenclaturesDataProvider nomDP = getNacidDataProvider().getNomenclaturesDataProvider();
                    DocumentType documentType = nomDP.getDocumentType(att.getDocTypeId());
                    String docType = documentType == null ? "": documentType.getLongName();
                    
                    
					table.addRow(att.getId(), expert, docType, att.getFileName(),
							att.getDocDescr(), "");
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (CellCreationException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void generateDocTypesComboBox(Integer activeId, HttpServletRequest request) {
        List<DocumentType> flatNomeclatures = null;

        flatNomeclatures = getNacidDataProvider().getNomenclaturesDataProvider()
            .getDocumentTypes(null, null, DocCategory.EXPERT_OPINIONS);

        ComboBoxWebModel combobox = new ComboBoxWebModel(activeId == null ? "" : activeId + "", true);
        if (flatNomeclatures != null) {
            for (DocumentType s : flatNomeclatures) {
                if (!s.isIncoming()) {
                	combobox.addItem(s.getId() + "", s.getLongName());	
                }
            }
            request.setAttribute("docTypeCombo", combobox);
        }
    }
	
}
