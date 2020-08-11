package com.nacid.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.applications.DiplomaType;
import com.nacid.bl.applications.DiplomaTypeAttachmentDataProvider;
import com.nacid.bl.applications.DiplomaTypeDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.DocCategory;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.table.*;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.applications.DiplomaTypeAttachmentWebModel;
import com.nacid.web.model.applications.DiplomaTypeWebModel;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;

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
import java.util.Iterator;
import java.util.List;

import static com.nacid.web.handlers.impl.applications.BaseAttachmentHandler.writeEmptyImage;

public class DiplomaTypeAttachmentHandler extends NacidBaseRequestHandler {

	private static final String COLUMN_NAME_ID = "id";
	private static final String COLUMN_NAME_DIPLOMA = "Тип диплома";
	private static final String COLUMN_NAME_FILE_NAME = "Име";
	private static final String COLUMN_NAME_DESC = "Описание";
	private static final String COLUMN_NAME_PREVIEW = "Preview";
	
	private static final String DIPLOMA_TYPE_PARAM = "diplTypeID";
	private static final String EDIT_SCREEN = "diploma_type_attachment_edit";
	
	
	private ServletContext servletContext;
	
	private class ImgObserver implements ImageObserver {
		
		boolean ready = false;
		@Override
		public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
			return ready = (infoflags & ALLBITS) != 0;
		}
	};
	
	public DiplomaTypeAttachmentHandler(ServletContext servletContext) {
		super(servletContext);
		this.servletContext = servletContext;
	}

	public void handleNew(HttpServletRequest request, HttpServletResponse response) {
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		int diplomaTypeId = Integer.parseInt(request.getParameter(DIPLOMA_TYPE_PARAM));
		
		generateDocTypesComboBox(null,request);
		
		request.setAttribute(WebKeys.DIPLOMA_TYPE_ATTCH_WEB_MODEL, 
				new DiplomaTypeAttachmentWebModel(diplomaTypeId, 
						getTypeName(nacidDataProvider, diplomaTypeId)));
		request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
	}
	
	@Override
	public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
		int attachmentId = DataConverter.parseInt(request.getParameter("id"), -1);
		if (attachmentId <= 0) {
			throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
		}
		
		DiplomaTypeAttachmentDataProvider attDP = getNacidDataProvider().getDiplomaTypeAttachmentDataProvider();
		
		Attachment att = attDP.getDiplomaTypeAttacment(attachmentId, true);
		
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
		
		DiplomaTypeAttachmentDataProvider attDP = getNacidDataProvider().getDiplomaTypeAttachmentDataProvider();
		
		Attachment att = attDP.getDiplomaTypeAttacment(attachmentId, false);
		
		if (att == null || !att.getContentType().startsWith("image")) {
			writeEmptyImage(response);
			return;
		}
		
		att = attDP.getDiplomaTypeAttacment(attachmentId, true);
		
		InputStream is = att.getContentStream();
		try {
			ServletOutputStream sos = response.getOutputStream();
			
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

	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
		
		int attachmentId = DataConverter.parseInt(request.getParameter("id"), -1);
		if (attachmentId <= 0) {
			throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
		}
		request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
		NacidDataProvider nacidDataProvider = getNacidDataProvider();

		DiplomaTypeAttachmentDataProvider attDP = nacidDataProvider.getDiplomaTypeAttachmentDataProvider();
		
		Attachment att = attDP.getDiplomaTypeAttacment(attachmentId, false);
		
		if (att == null) {
			throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
		}
		
		generateDocTypesComboBox(att.getDocTypeId(),request);
		
		request.setAttribute(WebKeys.DIPLOMA_TYPE_ATTCH_WEB_MODEL, 
				new DiplomaTypeAttachmentWebModel(att.getId(), att.getParentId(),
						att.getDocDescr(), 
						getTypeName(nacidDataProvider, att.getParentId()), att.getFileName(), att.getDocTypeId()));

	}

	public void handleList(HttpServletRequest request, HttpServletResponse response) {
	    //int diplomaTypeId = DataConverter.parseInt(request.getParameter("id"), -1);
	    DiplomaTypeWebModel dtWM = (DiplomaTypeWebModel)request.getAttribute(WebKeys.DIPLOMA_TYPE_WEB_MODEL);
	    int diplomaTypeId = dtWM.getId();
	    
        String operation = getOperationName(request);
        if(operation.equals("save")) {
            operation = "edit";
        }
		request.getSession().setAttribute("backUrlDiplType", servletContext.getAttribute("pathPrefix")
				+ "/control" 
				+ "/" + getGroupName(request)
				+ "/" + operation
				+ "?id=" + diplomaTypeId);
		
		HttpSession session = request.getSession();
		Table table = (Table) session.getAttribute(WebKeys.TABLE_DIPLOMA_TYPE_ATTCH);
		
		//boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request);
		boolean reloadTable = true;
		if (reloadTable || table == null) {
			TableFactory tableFactory = TableFactory.getInstance();
			table = tableFactory.createTable();
		
						
			table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
			table.addColumnHeader(COLUMN_NAME_DIPLOMA, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_FILE_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_DESC, CellValueDef.CELL_VALUE_TYPE_STRING);
			table.addColumnHeader(COLUMN_NAME_PREVIEW, CellValueDef.CELL_VALUE_TYPE_STRING);
			
			//table.addColumnHeader(COLUMN_NAME_WEB_SITE, CellValueDef.CELL_VALUE_TYPE_STRING);
			//table.addColumnHeader(COLUMN_NAME_URL, CellValueDef.CELL_VALUE_TYPE_STRING);
			
			session.setAttribute(WebKeys.TABLE_DIPLOMA_TYPE_ATTCH, table);
			resetTableData(request, diplomaTypeId);
			
			

		}

		// TableState settings
		TableState tableState = (TableState) session
				.getAttribute(WebKeys.TABLE_DIPLOMA_TYPE_ATTCH + WebKeys.TABLE_STATE);
		/**
		 * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
		 * vzeme posledniq tableState, togava se generira nov tableState!
		 */
		boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
		if (tableState == null || !getLastTableState) {
			tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
			session.setAttribute(WebKeys.TABLE_DIPLOMA_TYPE_ATTCH + WebKeys.TABLE_STATE, tableState);
		}

		// TableWebModel SETTINGS
		TableWebModel webmodel = new TableWebModel("Списък на прикачените документи");
		webmodel.setViewOpenInNewWindow(true);
		webmodel.addRequestParam(TableWebModel.OPERATION_NAME_NEW, DIPLOMA_TYPE_PARAM, diplomaTypeId + "");

		webmodel.setGroupName("diploma_type_attachment");
		webmodel.insertTableData(table, tableState);
		request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
		
		//request.setAttribute(WebKeys.NEXT_SCREEN, "diploma_type_attachment_list");

		// Generating filters for displaying to user
		FiltersWebModel filtersWebModel = (FiltersWebModel) session
				.getAttribute(WebKeys.TABLE_DIPLOMA_TYPE_ATTCH + WebKeys.FILTER_WEB_MODEL);
		/**
		 * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
		 * da se vzeme posledniq tableState(filterWebModel), togava se generira
		 * nov i se slaga v sesiqta!
		 */
		if (filtersWebModel == null || !getLastTableState) {
			filtersWebModel = new FiltersWebModel();
			
			session.setAttribute(WebKeys.TABLE_DIPLOMA_TYPE_ATTCH
					+ WebKeys.FILTER_WEB_MODEL, filtersWebModel);
		}
		request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
	}

	@SuppressWarnings("unchecked")
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
			int diplomaTypeId = 0;
			int id = 0;
			String fileName = null;
			String contentType = null;
			int fileSize = 0;
			
			Iterator iter = uploadedItems.iterator();
			while (iter.hasNext()) {
			    FileItem item = (FileItem) iter.next();

			    if (item.isFormField()) {
			    	if(item.getFieldName().equals("docDescr"))
			        	docDescr = item.getString("UTF-8");
			        else if(item.getFieldName().equals("id"))
			        	id = DataConverter.parseInt(item.getString("UTF-8"), 0);
			        else if(item.getFieldName().equals("diplomaTypeId"))
			        	diplomaTypeId = DataConverter.parseInt(item.getString("UTF-8"), 0);
			        else if(item.getFieldName().equals("docTypeId"))
			        	docTypeId = DataConverter.parseInt(item.getString("UTF-8"), 0);
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
			
			DiplomaTypeAttachmentDataProvider attDP = getNacidDataProvider().getDiplomaTypeAttachmentDataProvider();
			
			if (id != 0 && attDP.getDiplomaTypeAttacment(id, false) == null) {
				throw new UnknownRecordException("Unknown attachment ID:" + id);
			}
			
			if (id == 0 && fileSize <= 0) {
				SystemMessageWebModel webmodel = new SystemMessageWebModel("Не е посочен файл");
				request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
				request.setAttribute(WebKeys.DIPLOMA_TYPE_ATTCH_WEB_MODEL, 
						new DiplomaTypeAttachmentWebModel(0, diplomaTypeId, docDescr, 
								getTypeName(getNacidDataProvider(), diplomaTypeId), "", docTypeId));
				
			} else {
			
				int newId = attDP.saveDiplomaTypeAttacment(id, 
						diplomaTypeId, docDescr, docTypeId, 
						is, fileName, contentType, fileSize, getLoggedUser(request, response).getUserId());
				Attachment newAttachment = attDP.getDiplomaTypeAttacment(newId, false);
				request.setAttribute(WebKeys.SYSTEM_MESSAGE,
						new SystemMessageWebModel("Данните бяха въведени в базата",
								SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
				resetTableData(request, diplomaTypeId);
				request.setAttribute(WebKeys.DIPLOMA_TYPE_ATTCH_WEB_MODEL, 
						new DiplomaTypeAttachmentWebModel(
								newId, diplomaTypeId, docDescr, 
								getTypeName(getNacidDataProvider(), diplomaTypeId), newAttachment.getFileName(), newAttachment.getDocTypeId()));
				
			}
			generateDocTypesComboBox(docTypeId,request);
		} catch (Exception e) {
			throw Utils.logException(this, e);
		}
		finally {
			session.removeAttribute(WebKeys.FILE_UPLOAD_LISTENER);
		}
		
		request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
		request.getSession().removeAttribute(WebKeys.TABLE_DIPLOMA_TYPE_ATTCH);
		
	}

	public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
		int attId = DataConverter.parseInt(request.getParameter("id"), -1);
	    if (attId <= 0) {
	      throw new UnknownRecordException("Unknown attachment Id:" + attId);
	    }
	    
	    DiplomaTypeAttachmentDataProvider attDP = getNacidDataProvider().getDiplomaTypeAttachmentDataProvider();
	    attDP.deleteRecord(attId);
	    
	    request.getSession().removeAttribute(WebKeys.TABLE_DIPLOMA_TYPE_ATTCH);
	    //handleList(request, response);
	    try {
			response.sendRedirect((String) request.getSession().getAttribute("backUrlDiplType"));
		} catch (IOException e) {
			throw Utils.logException(e);
		}
	}
	
	private void resetTableData(HttpServletRequest request, int diplomaTypeId) {
		Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_DIPLOMA_TYPE_ATTCH);
		if (table == null) {
			return;
		}
		table.emtyTableData();
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		DiplomaTypeAttachmentDataProvider attDP = nacidDataProvider.getDiplomaTypeAttachmentDataProvider();
		DiplomaTypeDataProvider dtDP = nacidDataProvider.getDiplomaTypeDataProvider();
		
		List<Attachment> attachments = attDP.getAttachmentsForDiplomaType(diplomaTypeId);
		
		if (attachments != null) {
			for (Attachment att : attachments) {
				
				
				DiplomaType dt = dtDP.getDiplomaType(diplomaTypeId);
				String diplTypeTitle = dt.getTitle();
				if(diplTypeTitle == null) {
					diplTypeTitle = "";
				}
				try {
					
					table.addRow(att.getId(), diplTypeTitle, att.getFileName(),
							att.getDocDescr(), "");
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (CellCreationException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private String getTypeName(NacidDataProvider ndp, int diplomaTypeId) {
		DiplomaTypeDataProvider dtDP = ndp.getDiplomaTypeDataProvider();
		DiplomaType dt = dtDP.getDiplomaType(diplomaTypeId);
		return dt.getTitle();
	}
	
	public void generateDocTypesComboBox(Integer activeId, HttpServletRequest request) {
        List<? extends FlatNomenclature> flatNomeclatures = null;

        flatNomeclatures = getNacidDataProvider().getNomenclaturesDataProvider()
            .getDocumentTypes(null, null, DocCategory.DIPLOMA_TYPE_ATTACMENTS);

        ComboBoxWebModel combobox = new ComboBoxWebModel(activeId == null ? "" : activeId + "", true);
        if (flatNomeclatures != null) {
            for (FlatNomenclature s : flatNomeclatures) {
                combobox.addItem(s.getId() + "", ((DocumentType) s).getLongName());
            }
        }
        request.setAttribute("docTypeCombo", combobox);
    }
}
