package com.ext.nacid.web.handlers.impl.applications;

import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.ext.nacid.web.handlers.UserAccessUtils;
import com.ext.nacid.web.model.applications.ExtApplicationAttachmentWebModel;
import com.ext.nacid.web.model.applications.ExtApplicationWebModel;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.external.ExtApplicationKind;
import com.nacid.bl.external.applications.ExtApplicationAttachmentDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.bl.table.*;
import com.nacid.bl.users.User;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
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

public class ExtApplicationAttachmentHandler extends NacidExtBaseRequestHandler {

    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_TYPE = "Тип документ";
    private static final String COLUMN_NAME_DESC = "Описание";
    private static final String COLUMN_NAME_COPY = "Форма";
    private static final String COLUMN_NAME_FILE_NAME = "Име";
    private static final String COLUMN_NAME_PREVIEW = "Preview";
    

    private static final String APPLICATION_PARAM = "applID";
    private static final String EDIT_SCREEN = "application_attachment_edit";

    private ServletContext servletContext;

    private class ImgObserver implements ImageObserver {

        boolean ready = false;

        @Override
        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            return ready = (infoflags & ALLBITS) != 0;
        }
    };

    public ExtApplicationAttachmentHandler(ServletContext servletContext) {
        super(servletContext);
        this.servletContext = servletContext;
    }

    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        int applicationId = Integer.parseInt(request.getParameter(APPLICATION_PARAM));

        request.setAttribute(WebKeys.EXT_APPLICATION_ATTCH_WEB_MODEL, new ExtApplicationAttachmentWebModel(applicationId));
        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);

        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_COPY_TYPE, getNacidDataProvider()
                .getNomenclaturesDataProvider(), true, request, "copyTypeCombo", null, true);

        generateDocTypesComboBox(applicationId, null, request);
    }

    @Override
    public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
        
        int attachmentId = DataConverter.parseInt(request.getParameter("id"), -1);
        
        Attachment att = getAttachment(attachmentId, true);
        
        if (att == null) {
            throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
        }
        
        checkApplicantActionAccess(UserAccessUtils.USER_ACTION_VIEW, getLoggedUser(request, response), att.getParentId());

        
        response.setContentType(att.getContentType());

        InputStream is = att.getContentStream();
        try {
            ServletOutputStream sos = response.getOutputStream();

            int read = 0;
            byte[] buf = new byte[1024];

            while ((read = is.read(buf)) > 0) {
                sos.write(buf, 0, read);
            }

        } catch (Exception e) {
            throw Utils.logException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
    public void handleView(HttpServletRequest request, HttpServletResponse response) {
        
        
        response.setContentType("image/jpeg");

        int attachmentId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (attachmentId <= 0) {
            writeEmptyImage(response);
            return;
        }
        
        Attachment att = getAttachment(attachmentId, false);

        if(att != null) {
            checkApplicantActionAccess(UserAccessUtils.USER_ACTION_VIEW, getLoggedUser(request, response), att.getParentId());
        }
        
        if (att == null || !att.getContentType().startsWith("image")) {
            writeEmptyImage(response);
            return;
        }
        
        att = getAttachment(attachmentId, true);

        InputStream is = att.getContentStream();
        try {
            ServletOutputStream sos = response.getOutputStream();

            BufferedImage src = ImageIO.read(is);
            if (src == null) {
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
        } catch (Exception e) {
            throw Utils.logException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }

    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {

        int attachmentId = DataConverter.parseInt(request.getParameter("id"), -1);

        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
        NacidDataProvider nacidDataProvider = getNacidDataProvider();

        Attachment att = getAttachment(attachmentId, false);

        if (att == null) {
            throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
        }
        
        checkApplicantActionAccess(UserAccessUtils.USER_ACTION_CHANGE, getLoggedUser(request, response), att.getParentId());

        request.setAttribute(WebKeys.EXT_APPLICATION_ATTCH_WEB_MODEL, new ExtApplicationAttachmentWebModel(att.getId(), att.getParentId(), att
                .getDocDescr(), att.getFileName()));

        ComboBoxUtils.generateNomenclaturesComboBox(att.getCopyTypeId(), NomenclaturesDataProvider.FLAT_NOMENCLATURE_COPY_TYPE, nacidDataProvider
                .getNomenclaturesDataProvider(), true, request, "copyTypeCombo", null, true);

        generateDocTypesComboBox(att.getParentId(), att.getDocTypeId(), request);
       
    }

    public void handleList(HttpServletRequest request, HttpServletResponse response) {

        
        //int applicationId = DataConverter.parseInt(request.getParameter("id"), -1);
        ExtApplicationWebModel appWM = (ExtApplicationWebModel)request.getAttribute(WebKeys.APPLICATION_WEB_MODEL);
        int applicationId;
        if (appWM == null) { //tova se sluchva pri edit na ext regprof application's attachment - togava nqma takym WebModel!
            applicationId = (Integer)request.getAttribute("id");
        } else { //tova se slucva pri edit na ext application's attachment
            applicationId = Integer.parseInt(appWM.getId());
        }
        
        
        String operation = getOperationName(request);
        if(operation.equals("save")) {
            operation = "edit";
        }
        String group = getGroupName(request);
        String query = "activeForm=3&id=" + applicationId;
        request.getSession().setAttribute("backUrlApplAtt", 
                servletContext.getAttribute("pathPrefix") 
                + "/control" 
                + "/" + group 
                + "/" + operation
                + "?" + query);

        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_APPLICATION_ATTCH);

        // boolean reloadTable =
        // RequestParametersUtils.getParameterReloadTable(request);
        boolean reloadTable = true;
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();

            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_TYPE, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DESC, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_COPY, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_FILE_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_PREVIEW, CellValueDef.CELL_VALUE_TYPE_STRING);

            
            session.setAttribute(WebKeys.TABLE_APPLICATION_ATTCH, table);
            resetTableData(request, applicationId);

        }

        // TableState settings
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_APPLICATION_ATTCH + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
         * vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            session.setAttribute(WebKeys.TABLE_APPLICATION_ATTCH + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel(null);
        webmodel.setGroupName("application_attachment");
        webmodel.insertTableData(table, tableState);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        webmodel.addRequestParam(TableWebModel.OPERATION_NAME_NEW, APPLICATION_PARAM, applicationId + "");
        webmodel.setViewOpenInNewWindow(true);
        
        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_APPLICATION_ATTCH + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();

            session.setAttribute(WebKeys.TABLE_APPLICATION_ATTCH + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }

    @SuppressWarnings("rawtypes")
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();

        // create file upload factory and upload servlet
        DiskFileItemFactory factory = new DiskFileItemFactory();

        FileCleaningTracker pTracker = FileCleanerCleanup.getFileCleaningTracker(servletContext);
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

            int id = 0;
            int applicationId = 0;
            String docDescr = null;
            int docTypeId = 0;
            String contentType = null;
            InputStream is = null;
            String fileName = null;
            int copyTypeId = 0;
            int fileSize = 0;

            Iterator iter = uploadedItems.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                if (item.isFormField()) {

                    if (item.getFieldName().equals("id"))
                        id = DataConverter.parseInt(item.getString("UTF-8"), 0);
                    else if (item.getFieldName().equals("applicationId"))
                        applicationId = DataConverter.parseInt(item.getString("UTF-8"), 0);
                    else if (item.getFieldName().equals("docDescr"))
                        docDescr = item.getString("UTF-8");
                    else if (item.getFieldName().equals("docTypeId"))
                        docTypeId = DataConverter.parseInt(item.getString("UTF-8"), 0);
                    else if (item.getFieldName().equals("copyTypeId"))
                        copyTypeId = DataConverter.parseInt(item.getString("UTF-8"), 0);
                    
                } else {
                    fileSize = (int) item.getSize();
                    if (fileSize > 0) {
                        is = item.getInputStream();
                        fileName = item.getName();
                        int index;
                        if ((index = fileName.lastIndexOf('\\')) != -1) {
                            fileName = fileName.substring(index + 1, fileName.length());
                        }
                        if ((index = fileName.lastIndexOf('/')) != -1) {
                            fileName = fileName.substring(index + 1, fileName.length());
                        }
                        if (fileName.equals("")) {
                            fileName = "file";
                        }
                        contentType = item.getContentType();
                    }
                }
            }

            checkApplicantActionAccess(UserAccessUtils.USER_ACTION_CHANGE, getLoggedUser(request, response), applicationId);
            
            

            if (id != 0 && getAttachment(id, false) == null) {
                throw new UnknownRecordException("Unknown attachment ID:" + id);
            }
            
            
            String errMsg = null;
            

            if (id == 0 && fileSize <= 0) {
                if(errMsg == null) {
                    errMsg = "Не е посочен файл";
                }
                SystemMessageWebModel webmodel = new SystemMessageWebModel(errMsg);
                request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
                request.setAttribute(WebKeys.EXT_APPLICATION_ATTCH_WEB_MODEL, new ExtApplicationAttachmentWebModel(0, applicationId, docDescr, ""));

            } else {
                
                int newId = saveAttachment(id, applicationId, docDescr, docTypeId, contentType, fileName, is, copyTypeId, fileSize, getLoggedUser(request, response).getUserId());

                request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата",
                        SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
                Attachment newAppAttach = getAttachment(newId, false);
                request.setAttribute(WebKeys.EXT_APPLICATION_ATTCH_WEB_MODEL,
                                new ExtApplicationAttachmentWebModel(newId, applicationId, docDescr, newAppAttach.getFileName()));
            }

            ComboBoxUtils.generateNomenclaturesComboBox(copyTypeId, NomenclaturesDataProvider.FLAT_NOMENCLATURE_COPY_TYPE, getNacidDataProvider()
                    .getNomenclaturesDataProvider(), true, request, "copyTypeCombo", null, true);
            generateDocTypesComboBox(applicationId, docTypeId, request);

        } catch (Exception e) {
            throw Utils.logException(this, e);
        } finally {
            session.removeAttribute(WebKeys.FILE_UPLOAD_LISTENER);
        }

        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
        request.getSession().removeAttribute(WebKeys.TABLE_APPLICATION_ATTCH);

    }

    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        int attId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (attId <= 0) {
            throw new UnknownRecordException("Unknown attachment Id:" + attId);
        }

        ExtApplicationAttachmentDataProvider attDP = getNacidDataProvider().getExtApplicationAttachmentDataProvider();
        
        Attachment att = attDP.getApplicationAttacment(attId, false);
        if (att != null) {
            checkApplicantActionAccess(UserAccessUtils.USER_ACTION_CHANGE, getLoggedUser(request, response), att.getParentId());
        }

        attDP.deleteAttachment(attId);

        request.getSession().removeAttribute(WebKeys.TABLE_APPLICATION_ATTCH);
        // handleList(request, response);
        try {
            response.sendRedirect((String) request.getSession().getAttribute("backUrlApplAtt"));
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }

    private void resetTableData(HttpServletRequest request, int applicationId) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_APPLICATION_ATTCH);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        
        NomenclaturesDataProvider nomDP = nacidDataProvider.getNomenclaturesDataProvider();
        List<Attachment> attachments = getAttachments(applicationId);

        if (attachments != null) {
            for (Attachment att : attachments) {

                String copyType = "";
                FlatNomenclature ct = nomDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_COPY_TYPE, att.getCopyTypeId());
                if (ct != null) {
                    copyType = ct.getName();
                }

                String docType = "";
                DocumentType dt = nomDP.getDocumentType(att.getDocTypeId());
                if (dt != null) {
                    docType = dt.getLongName();
                }

                try {

                    table.addRow(att.getId(), docType, att.getDocDescr(), copyType, att.getFileName(), "");
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }

    public void generateDocTypesComboBox(Integer applicationId, Integer activeId, HttpServletRequest request) {
        List<? extends FlatNomenclature> flatNomeclatures = null;

        flatNomeclatures = getNacidDataProvider().getNomenclaturesDataProvider()
            .getDocumentTypes(null, null, getDocumentCategoryId(applicationId));

        ComboBoxWebModel combobox = new ComboBoxWebModel(activeId == null ? "" : activeId + "", true);
        if (flatNomeclatures != null) {
            for (FlatNomenclature s : flatNomeclatures) {
                if(((DocumentType) s).isIncoming()) {
                    combobox.addItem(s.getId() + "", s.getName());
                }
            }
            request.setAttribute("docTypeCombo", combobox);
        }
    }
    
    
    protected void checkApplicantActionAccess(int operationId, User user, int applicationId) {
        UserAccessUtils.checkApplicantActionAccess(UserAccessUtils.USER_ACTION_CHANGE, user, applicationId, getNacidDataProvider());
    }
    protected Attachment getAttachment(int attachmentId, boolean loadContent) {
        if (attachmentId <= 0) {
            throw new UnknownRecordException("Unknown attachment Id:" + attachmentId);
        }

        ExtApplicationAttachmentDataProvider attDP = getNacidDataProvider().getExtApplicationAttachmentDataProvider();
        
        Attachment att = attDP.getApplicationAttacment(attachmentId, loadContent);
        return att;
    }
    protected List<Attachment> getAttachments(int applicationId) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ExtApplicationAttachmentDataProvider attDP = nacidDataProvider.getExtApplicationAttachmentDataProvider();
        return attDP.getAttachmentsForApplication(applicationId);
    }
    protected int saveAttachment(int id, int applicationId, String docDescr, int docTypeId, String contentType, String fileName,
            InputStream contentStream, int copyTypeId, int fileSize, int userCreated) {
        ExtApplicationAttachmentDataProvider dp = getNacidDataProvider().getExtApplicationAttachmentDataProvider();
        return dp.saveApplicationAttacment(id, applicationId, docDescr, docTypeId, contentType, fileName, contentStream, copyTypeId, fileSize, userCreated);
    }
    protected int getDocumentCategoryId(Integer applicationId) {
        List<ExtApplicationKind> applicationKinds = getNacidDataProvider().getExtApplicationsDataProvider().getApplicationKindsPerApplication(applicationId);
        Integer applicationType = NumgeneratorDataProvider.ENTRY_NUM_SERIES_TO_APPLICATION_TYPE.get(applicationKinds.get(0).getEntryNumSeriesId());
        return applicationType == ApplicationType.DOCTORATE_APPLICATION_TYPE ? DocCategory.APPLICATION_ATTACHMENTS_DOCTORATE : DocCategory.APPLICATION_ATTACHMENTS;
    }
}
