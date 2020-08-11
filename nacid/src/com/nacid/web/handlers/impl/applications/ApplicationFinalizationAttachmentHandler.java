package com.nacid.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.applications.AttachmentDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.*;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.ApplicationAttachmentWebModel;
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
import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.nacid.bl.nomenclatures.ApplicationStatus.*;
import static com.nacid.bl.nomenclatures.DocumentType.*;


public class ApplicationFinalizationAttachmentHandler extends ApplicationAttachmentHandler {

    public ApplicationFinalizationAttachmentHandler(ServletContext servletContext) {
        super(servletContext);
    }

    //do handleEdit se stiga samo ako pyrvo se submitne formata za applicationFinalization (uploadne se nqkoj ot final files (udostoverenie i t.n.), sled  koeto se otide na taba prilojeniq, uploadne se nqkoj file i se netisne butona nazad!)
    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        int applicationId = DataConverter.parseInt(request.getParameter("id"), 0);
        int activeFormId = DataConverter.parseInt(request.getParameter("activeForm"), 0);
        try {
            response.sendRedirect(getServletContext().getAttribute("pathPrefix") + (activeFormId == 0 ? getEditUrl(applicationId) : getEditUrl(activeFormId, applicationId)));
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }

    public void initFinalizationTab(HttpServletRequest request, HttpServletResponse response) {
        Application application = (Application) request.getAttribute("application");

        boolean showDocuments = showDocuments(application);
        request.setAttribute("showDocuments", showDocuments);
        if (!showDocuments) {
            return;
        }
//        request.setAttribute(WebKeys.ACTIVE_FORM, ApplicationsHandler.FORM_ID_EXPERT_DATA);

        int appId = application.getId();
        NacidDataProvider nacidDataProvider = getNacidDataProvider(request.getSession());
        AttachmentDataProvider attDP = nacidDataProvider.getApplicationAttachmentDataProvider();
        disableFormIfNecessary(application, request);

        generateFirstFileLabel(application, request);
        Attachment firstAttachment = getFirstAttachment(application, attDP, request);
        if (firstAttachment == null) {
            createNewAttachmentWebModel(request, WebKeys.APPLICATION_ATTCH_WEB_MODEL, application.getId(), getFirstFileDocType(application));
            return;
        } else {
            createAttachmentWebModel(request, WebKeys.APPLICATION_ATTCH_WEB_MODEL, firstAttachment, appId);
        }

        boolean showSecondFile = showSecondFile(firstAttachment);
        request.setAttribute("showSecondFileFrame", showSecondFile);
        if (!showSecondFile) {
            return;
        }

        generateSecondFileLabel(application, firstAttachment, request);
        Attachment secondFileAttachment = getSecondFileAttachment(application, firstAttachment, attDP);
        if (firstAttachment.getDocTypeId() == DOC_TYPE_DOCTORATE_CERTIFICATE_SUGGESTION && secondFileAttachment != null) {
            request.setAttribute("showDuplicateButtons", true);
        }

        if (secondFileAttachment == null) {
            int secondFileDocType = getSecondFileDocType(firstAttachment);
            createNewAttachmentWebModel(request, WebKeys.APPLICATION_SECOND_ATTCH_WEB_MODEL, appId, secondFileDocType);
        } else {
            createAttachmentWebModel(request, WebKeys.APPLICATION_SECOND_ATTCH_WEB_MODEL, secondFileAttachment, appId);
        }
    }

    private static void disableFormIfNecessary(Application application, HttpServletRequest request) {
        int docflowStatus = application.getApplicationDocflowStatusId();
        if (ApplicationDocflowStatus.APPLICATION_ARCHIVED_DOCFLOW_STATUS_CODE == docflowStatus || ApplicationDocflowStatus.APPLICATION_FINISHED_DOCFLOW_STATUS_CODE == docflowStatus) {
            request.setAttribute("disableGeneration", "disabled=\"disabled\"");
        }
    }

    private static Attachment getFirstAttachment(Application application, AttachmentDataProvider attDP, HttpServletRequest request) {
        Integer docType = getFirstFileDocType(application);
        List<Attachment> list = attDP.getAttachmentsForParentByType(application.getId(), docType);
        if (Utils.isEmpty(list)) {
            return null;
        }
        Collections.sort(list, Comparator.comparing(Attachment::getId));
        return Utils.getListLastElement(list);
    }


    private static boolean showDocuments(Application application) {
        if (application.getApplicationType() != ApplicationType.DOCTORATE_APPLICATION_TYPE) {
            return false;
        }
        int applicationStatus = application.getFinalAppStatus() == null ? application.getApplicationStatusId() : application.getFinalAppStatus().getId();
        if (!Arrays.asList(APPLICATION_PRIZNATO_STATUS_CODE, APPLICATION_REFUSED_STATUS_CODE, APPLICATION_TERMINATED_STATUS_CODE, APPLICATION_OBEZSILENO_STATUS_CODE).contains(applicationStatus)) {
            return false;
        }
        return true;
    }

    private static void generateFirstFileLabel(Application application, HttpServletRequest request) {
        int applicationStatus = application.getFinalAppStatus() == null ? application.getApplicationStatusId() : application.getFinalAppStatus().getId();
        String firstFileLabel = null;
        if (APPLICATION_PRIZNATO_STATUS_CODE == applicationStatus) {
            firstFileLabel = "Предложение за удостоверение";
        } else if (ApplicationStatus.APPLICATION_REFUSED_STATUS_CODE == applicationStatus) {
            firstFileLabel = "Предложение за отказ";
        } else if (APPLICATION_TERMINATED_STATUS_CODE == applicationStatus) {
            firstFileLabel = "Писмо за прекратяване";
        } else if (APPLICATION_OBEZSILENO_STATUS_CODE == applicationStatus) {
            firstFileLabel = "Заповед за обезсилване";
        }
        request.setAttribute("firstFileLabel", firstFileLabel);
    }

    private static int getFirstFileDocType(Application application) {
        int applicationStatus = application.getFinalAppStatus() == null ? application.getApplicationStatusId() : application.getFinalAppStatus().getId();
        int docType;
        if (APPLICATION_PRIZNATO_STATUS_CODE == applicationStatus) {
            docType = DocumentType.DOC_TYPE_DOCTORATE_CERTIFICATE_SUGGESTION;
        } else if (ApplicationStatus.APPLICATION_REFUSED_STATUS_CODE == applicationStatus) {
            docType = DOC_TYPE_DOCTORATE_REFUSE_SUGGESTION;
        } else if (APPLICATION_TERMINATED_STATUS_CODE == applicationStatus) {
            docType = DOC_TYPE_DOCTORATE_PISMO_PREKRATIAVANE;
        } else if (APPLICATION_OBEZSILENO_STATUS_CODE == applicationStatus) {
            docType = DOC_TYPE_DOCTORATE_ZAPOVED_OBEZSILVANE;
        } else {
           throw new RuntimeException("Unknown application Status: " + applicationStatus);
        }
        return docType;
    }

    private static void generateSecondFileLabel(Application application, Attachment firstFileAttachment, HttpServletRequest request) {
        String secondFileLabel = null;
        if (firstFileAttachment.getDocTypeId() == DOC_TYPE_DOCTORATE_CERTIFICATE_SUGGESTION) {
            secondFileLabel = "Удостоврение";
        } else if (firstFileAttachment.getDocTypeId() == DOC_TYPE_DOCTORATE_REFUSE_SUGGESTION) {
            secondFileLabel = "Отказ";
        }
        request.setAttribute("secondFileLabel", secondFileLabel);
    }
    private static int getSecondFileDocType(Attachment firstDocAttachment) {
        if (firstDocAttachment.getDocTypeId() == DOC_TYPE_DOCTORATE_CERTIFICATE_SUGGESTION) {
            return DOC_TYPE_DOCTORATE_CERTIFICATE;
        } else if (firstDocAttachment.getDocTypeId() == DOC_TYPE_DOCTORATE_REFUSE_SUGGESTION) {
            return DOC_TYPE_DOCTORATE_REFUSE;
        } else {
            throw new RuntimeException("Unknown first file doc type: " + firstDocAttachment.getDocTypeId());
        }
    }


    private static boolean showSecondFile(Attachment firstFileAttachment) {
        return firstFileAttachment.getDocTypeId() == DOC_TYPE_DOCTORATE_CERTIFICATE_SUGGESTION || firstFileAttachment.getDocTypeId() == DOC_TYPE_DOCTORATE_REFUSE_SUGGESTION;
    }


    private static Attachment getSecondFileAttachment(Application application, Attachment att, AttachmentDataProvider attachmentDataProvider) {
        List<Attachment> secondFileAttachments;
        if (att.getDocTypeId() == DOC_TYPE_DOCTORATE_CERTIFICATE_SUGGESTION) {
            List<Integer> certificateDocumentTypes = new ArrayList<>();
            certificateDocumentTypes.add(DOC_TYPE_DOCTORATE_CERTIFICATE);
            certificateDocumentTypes.add(DOC_TYPE_DOCTORATE_CERTIFICATE_DUPLICATE);
            certificateDocumentTypes.add(DOC_TYPE_DOCTORATE_CERTIFICATE_FACTUAL_ERROR);

            secondFileAttachments = attachmentDataProvider.getAttachmentsForParentByTypes(application.getId(), certificateDocumentTypes);

        } else if (DOC_TYPE_DOCTORATE_REFUSE_SUGGESTION == att.getDocTypeId()) {
            secondFileAttachments = attachmentDataProvider.getAttachmentsForParentByType(application.getId(), DOC_TYPE_DOCTORATE_REFUSE);
        } else {
            throw new RuntimeException("Unknown attachment docTypeId:" + att.getDocTypeId());
        }
        if (Utils.isEmpty(secondFileAttachments)) {
            return null;
        }
        Collections.sort(secondFileAttachments, Comparator.comparing(Attachment::getId));
        return Utils.getListLastElement(secondFileAttachments);
    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {

        int applicationId = DataConverter.parseInt(request.getParameter("applicationId"), 0);
        HttpSession session = request.getSession();

        // create file upload factory and upload servlet
        DiskFileItemFactory factory = new DiskFileItemFactory();

        FileCleaningTracker pTracker = FileCleanerCleanup.getFileCleaningTracker(getServletContext());
        factory.setFileCleaningTracker(pTracker);
        ServletFileUpload upload = new ServletFileUpload(factory);

        // set file upload progress listener
        FileUploadListener listener = new FileUploadListener();

        session.setAttribute(WebKeys.FILE_UPLOAD_LISTENER, listener);

        // upload servlet allows to set upload listener
        upload.setProgressListener(listener);

        try {

            List items = upload.parseRequest(request);
            boolean generate = isGenerate(items);
            //saving the first attachment
            SaveAttachmentRequest rq1 = new SaveAttachmentRequest(items, request, null);
            rq1.setGenerate(generate && rq1.getId() == 0);

            if (rq1.getIs() != null || rq1.getScannedIs() != null || rq1.isGenerate() || !StringUtils.isEmpty(rq1.getDocDescr())) {
                Attachment attachment = saveFile(request, response, rq1);
                if (attachment == null) {
                    request.setAttribute(WebKeys.APPLICATION_ATTCH_WEB_MODEL, new ApplicationAttachmentWebModel(0, rq1.getApplicationId(), rq1.getDocDescr(), "", null, rq1.getDocflowNum(), rq1.getDocTypeId(), rq1.getDocflowUrl()));
                } else {
                    String docflowUrl = getDocFlowUrl(getNacidDataProvider(), applicationId, attachment.getId(), getEditUrl(attachment.getId()));
                    request.setAttribute(WebKeys.APPLICATION_ATTCH_WEB_MODEL, new ApplicationAttachmentWebModel(attachment.getId(), applicationId, attachment.getDocDescr(), attachment.getFileName(), attachment.getScannedFileName(), attachment.getDocflowNum(), attachment.getDocTypeId(), docflowUrl));
                }
            }


            //saving the second attachment!
            SaveAttachmentRequest rq2 = new SaveAttachmentRequest(items, request, "2");
            rq2.setGenerate(generate && rq1.getId() != 0 && rq2.getId() == 0);
            if (rq2.getIs() != null || rq2.getScannedIs() != null || rq2.isGenerate() || !StringUtils.isEmpty(rq2.getDocDescr())) {
                Attachment attachment = saveFile(request, response, rq2);
                if (attachment == null) {
                    request.setAttribute(WebKeys.APPLICATION_SECOND_ATTCH_WEB_MODEL, new ApplicationAttachmentWebModel(0, rq2.getApplicationId(), rq2.getDocDescr(), "", null, rq2.getDocflowNum(), rq2.getDocTypeId(), rq2.getDocflowUrl()));
                } else {
                    String docflowUrl = getDocFlowUrl(getNacidDataProvider(), applicationId, attachment.getId(), getEditUrl(attachment.getId()));
                    request.setAttribute(WebKeys.APPLICATION_SECOND_ATTCH_WEB_MODEL, new ApplicationAttachmentWebModel(attachment.getId(), applicationId, attachment.getDocDescr(), attachment.getFileName(), attachment.getScannedFileName(), attachment.getDocflowNum(), attachment.getDocTypeId(), docflowUrl));
                }
            }


        } catch (Exception e) {
            throw Utils.logException(this, e);
        } finally {
            session.removeAttribute(WebKeys.FILE_UPLOAD_LISTENER);
        }

        Application application = getNacidDataProvider().getApplicationsDataProvider().getApplication(applicationId);
        ApplicationsHandler.addApplicationToWebModel(application, request, response, ApplicationsHandler.FORM_ID_EXPERT_DATA);
    }

    private boolean isGenerate(List uploadedItems) throws UnsupportedEncodingException {
        Iterator iter = uploadedItems.iterator();
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
            if (item.getFieldName().equals("generate")) {
                return DataConverter.parseBoolean(item.getString("UTF-8"));
            }
        }
        return false;
    }
    private void createNewAttachmentWebModel(HttpServletRequest request, String keyName, int applicationId, int docTypeId) {
        request.setAttribute(keyName, new ApplicationAttachmentWebModel(0, applicationId, null, null, null, null, docTypeId, getDocFlowUrl(getNacidDataProvider(), applicationId, 0,  getEditUrl(applicationId))));
    }

    private void createAttachmentWebModel(HttpServletRequest request, String key, Attachment att, int applicationId) {
        String dfUrl = getDocFlowUrl(getNacidDataProvider(), applicationId, att.getId(), getEditUrl(applicationId));
        request.setAttribute(key, new ApplicationAttachmentWebModel(att.getId(), att.getParentId(), att.getDocDescr(), att.getFileName(), att.getScannedFileName(), att.getDocflowNum(), att.getDocTypeId(), dfUrl));
    }


    protected String getEditUrl(int activeFormId, int id) {
        return "/control/applications/edit?activeForm=" + activeFormId + "&id=" + id;
    }
    protected String getEditUrl(int id) {
        return getEditUrl(ApplicationsHandler.FORM_ID_EXPERT_DATA, id);
    }
}
