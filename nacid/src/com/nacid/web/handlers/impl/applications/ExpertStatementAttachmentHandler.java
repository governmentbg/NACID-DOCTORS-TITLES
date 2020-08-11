package com.nacid.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.*;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.comision.ComissionMemberDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.ApplicationType;
import com.nacid.bl.nomenclatures.DocCategory;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.table.*;
import com.nacid.bl.utils.UniversityDetail;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.report.TemplateGenerator;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.applications.ApplicationWebModel;
import com.nacid.web.model.applications.ExpertStatementAttachmentWebModel;
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

import static com.nacid.bl.nomenclatures.ApplicationType.*;
import static com.nacid.bl.nomenclatures.DocCategory.*;
import static com.nacid.web.handlers.impl.applications.BaseAttachmentHandler.writeEmptyImage;

public class ExpertStatementAttachmentHandler extends NacidBaseRequestHandler {

	private static final String COLUMN_NAME_ID = "id";
	private static final String COLUMN_NAME_EXPERT = "Експерт";
	private static final String COLUMN_NAME_DOCUMENT_TYPE = "Тип документ";
	private static final String COLUMN_NAME_FILE_NAME = "Име на файл";
	private static final String COLUMN_NAME_DESC = "Описание";
	private static final String COLUMN_NAME_PREVIEW = "Preview";
	
	private static final String TABLE_PREFIX = "expStatement";

	private ServletContext servletContext;
    
	
	private class ImgObserver implements ImageObserver {
		
		boolean ready = false;
		@Override
		public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
			return ready = (infoflags & ALLBITS) != 0;
		}
	};
	
	public ExpertStatementAttachmentHandler(ServletContext servletContext) {
		super(servletContext);
		this.servletContext = servletContext;
	}

	public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        int applicationId = DataConverter.parseInt(request.getParameter("applId"), 0);
        
        request.setAttribute(WebKeys.EXPERT_STATEMENT_WEB_MODEL, 
                new ExpertStatementAttachmentWebModel(0, "", "", applicationId));
        Application app = getNacidDataProvider().getApplicationsDataProvider().getApplication(applicationId);
        generateDocTypesComboBox(null, request, app.getApplicationType());
        request.setAttribute(WebKeys.NEXT_SCREEN, "expert_statement_edit");

        generateApplicationExpertsCombo(app, null, true, request, getNacidDataProvider());
        generateUniversityDetailsCombobox(request, getNacidDataProvider());
        
    }
	
	public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
	    int applicationId = Integer.parseInt(request.getParameter("applId"));
        int attachmentId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (attachmentId <= 0) {
            throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, "expert_statement_edit");
        NacidDataProvider nacidDataProvider = getNacidDataProvider();

        
        ExpertStatementAttachmentDataProvider attDP = nacidDataProvider.getExpertStatementAttachmentDataProvider();
        ExpertStatementAttachment att = attDP.getExpertStatementAttachment(attachmentId, false);
        
        if (att == null) {
            throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
        }

        Application app = getNacidDataProvider().getApplicationsDataProvider().getApplication(applicationId);
        generateDocTypesComboBox(att.getDocTypeId(), request, app.getApplicationType());
        
        request.setAttribute(WebKeys.EXPERT_STATEMENT_WEB_MODEL, 
                new ExpertStatementAttachmentWebModel(att.getId(),
                        att.getDocDescr(), att.getFileName(), 
                        applicationId));

        generateApplicationExpertsCombo(app, att.getExpertId(), true, request, getNacidDataProvider());
        generateUniversityDetailsCombobox(request, getNacidDataProvider());

    }
	
	
	
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
            int expertId = 0;
            String errMsg = null;
            Integer universityDetailId = null;
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                if (item.isFormField()) {
                    if(item.getFieldName().equals("docDescr"))
                        docDescr = item.getString("UTF-8");
                    else if(item.getFieldName().equals("id"))
                        id = DataConverter.parseInt(item.getString("UTF-8"), 0);
                    else if(item.getFieldName().equals("docTypeId"))
                        docTypeId = DataConverter.parseInt(item.getString("UTF-8"), 0);
                    else if (item.getFieldName().equals("applId")) {
                        applicationId = Integer.parseInt(item.getString("UTF-8"));
                    } else if (item.getFieldName().equals("generate")) {
                        generate = DataConverter.parseBoolean(item.getString("UTF-8"));
                    } else if (item.getFieldName().equals("expertId")) {
                        expertId = DataConverter.parseInt(item.getString("UTF-8"), 0);
                    } else if (item.getFieldName().equals("universityDetailId")) {
                        universityDetailId = DataConverter.parseInteger(item.getString("UTF-8"), null);
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
            boolean hasError = false;

            ExpertStatementAttachmentDataProvider attDP = getNacidDataProvider().getExpertStatementAttachmentDataProvider();
            ApplicationsDataProvider applicationsDataProvider = getNacidDataProvider().getApplicationsDataProvider();
            Application a = applicationsDataProvider.getApplication(applicationId);

            if (generate) {
                NomenclaturesDataProvider nomDP = getNacidDataProvider().getNomenclaturesDataProvider();
                DocumentType docType = nomDP.getDocumentType(docTypeId);
                contentType = null;
                fileName = null;
                is = null;
                fileSize = 0;

                if (docType != null && docType.getDocumentTemplate() != null && docType.getDocumentTemplate().length() > 0) {


                    contentType = "application/msword";
                    fileName = a.getApplicationNumber() + "_" + docType.getDocumentTemplate() + "_" + DataConverter.formatDate(new Date()) + ".doc";
                    fileName = fileName.replace("/", "_");
                    if (StringUtils.isEmpty(docType.getDocumentTemplate())) {
                        errMsg = "Документа няма въведен template!";
                        hasError = true;
                    } else /*if (docType.getId() == DocumentType.DOC_TYPE_EXPERT_POSITION)*/ {
                        List<ExpertStatementAttachment> expertAttachments = attDP.getExpertStatementsForApplicationAndExpert(applicationId, expertId);
                        if (expertAttachments != null && expertAttachments.size() > 0) {
                            for (ExpertStatementAttachment expertAttachment : expertAttachments) {
                                if (expertAttachment.getDocTypeId() == docType.getId()) {
                                    hasError = true;
                                    errMsg = "Вече има прикачен доукент " + docType.getName() + " за този експерт!";
                                    break;
                                }
                            }
                        }
                        if (!hasError) {
                            Map<String, String> additionalParams = null;
                            if (universityDetailId != null) {
                                UniversityDetail universityDetail = getNacidDataProvider().getUtilsDataProvider().getUniversityDetail(universityDetailId);
                                if (universityDetail != null) {
                                    additionalParams = new HashMap<>();
                                    additionalParams.put("LETTER_RECIPIENT", universityDetail.getLetterRecipient());
                                    additionalParams.put("SALUTATION", universityDetail.getSalutation());
                                }
                            }

                            is = TemplateGenerator.generateExpertDocument(getNacidDataProvider(), applicationId, expertId, docType.getId(), additionalParams);
                            fileSize = is.available();
                        }
                    }
                } else {
                    errMsg = MessagesBundle.getMessagesBundle().getValue("autogeneratedDocsConditions");
                    hasError = true;
                }
            }

            if (!hasError) {
                ExpertStatementAttachment oldAtt = attDP.getExpertStatementAttachment(id, false);
                if (id != 0 && oldAtt == null) {
                    throw new UnknownRecordException("Unknown attachment ID:" + id);
                }

                if (id == 0 && fileSize <= 0) {
                    hasError = true;
                    errMsg = "Не е посочен файл";
                }
            }
            if (!hasError) {
                int newId = attDP.saveExpertStatementAttacment(id,expertId,docDescr, docTypeId, is, fileName, contentType, fileSize, applicationId);
                ExpertStatementAttachment newAttachment = attDP.getExpertStatementAttachment(newId, false);
                request.setAttribute(WebKeys.SYSTEM_MESSAGE,new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
                resetTableData(request, diplExamId);
                request.setAttribute(WebKeys.EXPERT_STATEMENT_WEB_MODEL,new ExpertStatementAttachmentWebModel(newId, docDescr, newAttachment.getFileName(), applicationId));
            }



            if (hasError) {
                SystemMessageWebModel webmodel = new SystemMessageWebModel(errMsg);
                request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
                request.setAttribute(WebKeys.EXPERT_STATEMENT_WEB_MODEL,
                        new ExpertStatementAttachmentWebModel(id, docDescr, fileName, applicationId));
            }
            generateDocTypesComboBox(docTypeId, request, a.getApplicationType());
            generateApplicationExpertsCombo(getNacidDataProvider().getApplicationsDataProvider().getApplication(applicationId), expertId, true, request, getNacidDataProvider());
            generateUniversityDetailsCombobox(request, getNacidDataProvider());
            request.setAttribute(WebKeys.APPLICATION_ID, applicationId);

        } catch (Exception e) {
            throw Utils.logException(this, e);
        }
        finally {
            session.removeAttribute(WebKeys.FILE_UPLOAD_LISTENER);
        }
        
        request.setAttribute(WebKeys.NEXT_SCREEN, "expert_statement_edit");
        request.getSession().removeAttribute(WebKeys.TABLE_EXPERT_STATEMENT);
        
    }
	
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
        request.setAttribute(WebKeys.APPLICATION_ID, oldAtt.getApplicationId());
        
        attDP.deleteRecord(attId);
        
        request.getSession().removeAttribute(WebKeys.TABLE_EXPERT_STATEMENT);
        try {
            response.sendRedirect(
                    request.getContextPath() + "/control/applications/edit?activeForm=5&id="+oldAtt.getApplicationId());
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
		
	    ApplicationWebModel appWM = (ApplicationWebModel)request.getAttribute(WebKeys.APPLICATION_WEB_MODEL);
        int applId = Integer.parseInt(appWM.getId());
        
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
		boolean getLastTableState = false;//RequestParametersUtils.getParameterGetLastTableState(request, TABLE_PREFIX);
		if (tableState == null || !getLastTableState) {
			tableState = TableStateAndFiltersUtils.createBaseTableState(request, table, TABLE_PREFIX);
			session.setAttribute(WebKeys.TABLE_EXPERT_STATEMENT + WebKeys.TABLE_STATE, tableState);
		}

		// TableWebModel SETTINGS
		TableWebModel webmodel = new TableWebModel("Списък със становища на експерти");
		webmodel.setViewOpenInNewWindow(true);
		// webmodel.setColumnFormatter("userDate",
		// CellFormatter.DATE_TIME_FORMATTER);
		webmodel.setGroupName("expert_statement_attachment");
		webmodel.insertTableData(table, tableState);
		webmodel.addRequestParam(TableWebModel.OPERATION_NAME_ALL, "applId", applId + "");
		request.setAttribute("expertStatementTableWebModel", webmodel);
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
	public void generateDocTypesComboBox(Integer activeId, HttpServletRequest request, int applicationType) {
        int docCategory;
        switch (applicationType) {
            case RUDI_APPLICATION_TYPE:
                docCategory = EXPERT_OPINIONS;
                break;
            case STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE:
                docCategory = EXPERT_OPINIONS_STATUTE_AUTHENTICITY_RECOMMENDATION;
                break;
            case DOCTORATE_APPLICATION_TYPE:
                docCategory = EXPERT_OPINIONS_DOCTORATE;
                break;
            default:
                throw new RuntimeException("Unknown applicationType:" + applicationType);
        }


	    List<DocumentType> flatNomeclatures = getNacidDataProvider().getNomenclaturesDataProvider().getDocumentTypes(null, null, docCategory);

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
	
	public static ComboBoxWebModel generateApplicationExpertsCombo(Application app, Integer selectedExpertId, boolean addEmpty, HttpServletRequest request, NacidDataProvider nacidDataProvider) {
		List<ApplicationExpert> applicationExperts = app.getApplicationExperts();
		
        ComboBoxWebModel combobox = new ComboBoxWebModel(selectedExpertId == null ? null : selectedExpertId.toString(), addEmpty);
        if (applicationExperts != null) {
            for (ApplicationExpert ae : applicationExperts) {
                ComissionMember c = ae.getExpert();
            	combobox.addItem("" + c.getId(), c.getFullName() + (c.getDegree() == null || "".equals(c.getDegree())? "" : ",  " + c.getDegree()));
            }
        }
        request.setAttribute(WebKeys.APPLICATION_EXPERTS_COMBO, combobox);
        return combobox;
    }

    public static void generateUniversityDetailsCombobox(HttpServletRequest request, NacidDataProvider nacidDataProvider) {
        UtilsDataProvider utilsDataProvider = nacidDataProvider.getUtilsDataProvider();
        List<UniversityDetail> universityDetails = utilsDataProvider.getUniversityDetails();
        ComboBoxWebModel wm = new ComboBoxWebModel(null, true);
        if (universityDetails != null) {
            universityDetails.forEach(d -> wm.addItem(d.getId() + "", d.getUniversityName()));
        }
        request.setAttribute("universityDetailsCombo", wm);


    }

	
	
}
