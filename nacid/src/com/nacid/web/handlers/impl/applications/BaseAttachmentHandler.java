package com.nacid.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.applications.AttachmentDataProvider;
import com.nacid.bl.events.Event;
import com.nacid.bl.events.EventDataProvider;
import com.nacid.bl.events.EventType;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.*;
import com.nacid.data.DataConverter;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.model.common.ComboBoxWebModel;
import org.apache.commons.lang.StringUtils;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class BaseAttachmentHandler extends NacidBaseRequestHandler {


    private class ImgObserver implements ImageObserver {

        boolean ready = false;

        @Override
        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            return ready = (infoflags & ALLBITS) != 0;
        }
    };

    public BaseAttachmentHandler(ServletContext servletContext) {
        super(servletContext);
    }
    
    protected abstract AttachmentDataProvider getAttachmentDataProvider();
    protected abstract int getDocTypeCategory(Integer applicationId);

    @Override
    public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
        int attachmentId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (attachmentId <= 0) {
            throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
        }
        
        AttachmentDataProvider attDP = getAttachmentDataProvider();
        Attachment att = attDP.getAttachment(attachmentId, false, false);
        //String fileName = getOperationName(request);
        boolean scanned;
        boolean isOriginal = DataConverter.parseBoolean(request.getParameter("original")); //COPY IN MY CODE
        if (isOriginal || StringUtils.isEmpty(att.getScannedContentType())) {
            scanned = false;
        } else {
            scanned = true;
        }
        
        /*if(fileName.equals(att.getFileName())) {
            scanned = false;
        }
        else if(fileName.equals(att.getScannedFileName())) {
            scanned = true;
        }
        else {
            throw Utils.logException(new Exception("Unknown fileName"));
        }*/
        
        att = attDP.getAttachment(attachmentId, !scanned, scanned);

        if (att == null) {
            throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
        }
        InputStream is = null;
        
        if(scanned) {
            response.setContentType(att.getScannedContentType());
            is = att.getScannedContentStream();
        }
        else {
            response.setContentType(att.getContentType());
            is = att.getContentStream();
        }
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

    @Override
    public void handleView(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("image/jpeg");

        int attachmentId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (attachmentId <= 0) {
            writeEmptyImage(response);
            return;
        }

        AttachmentDataProvider attDP = getAttachmentDataProvider();

        Attachment att = attDP.getAttachment(attachmentId, false, false);

        if (att == null || att.getScannedContentType() == null 
                || !att.getScannedContentType().startsWith("image")) {
            writeEmptyImage(response);
            return;
        }
        
        att = attDP.getAttachment(attachmentId, false, true);

        InputStream is = att.getScannedContentStream();
        if(is == null) { 
            return;
        }
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
    

    protected void generateDocTypesComboBox(Integer applicationId, Integer activeId, HttpServletRequest request) {
        List<? extends FlatNomenclature> flatNomeclatures = null;

        flatNomeclatures = getNacidDataProvider().getNomenclaturesDataProvider()
            .getDocumentTypes(null, null, getDocTypeCategory(applicationId));

        ComboBoxWebModel combobox = new ComboBoxWebModel(activeId == null ? "" : activeId + "", true);
        if (flatNomeclatures != null) {
            for (FlatNomenclature s : flatNomeclatures) {
                combobox.addItem(s.getId() + "", ((DocumentType) s).getLongName());
            }
            request.setAttribute("docTypeCombo", combobox);
        }
    }
    
    protected void generateEventStatusCombo(Integer activeId, HttpServletRequest request) {
        List<? extends FlatNomenclature> flatNomeclatures = null;

        flatNomeclatures = getNacidDataProvider().getNomenclaturesDataProvider()
            .getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EVENT_STATUS,
                    null, null);

        ComboBoxWebModel combobox = new ComboBoxWebModel(activeId == null ? "" : activeId + "", false);
        if (flatNomeclatures != null) {
            for (FlatNomenclature s : flatNomeclatures) {
                combobox.addItem(s.getId() + "", s.getName() + (s.isActive() ? "" : " (inactive)"));
            }
            request.setAttribute("eventStatus", combobox);
        }
    }
    
    protected void generateEventTypeCombo(Integer activeId, HttpServletRequest request) {
        
        List<EventType> list = getNacidDataProvider().getEventTypeDataProvider().getEventTypes();

        ComboBoxWebModel combobox = new ComboBoxWebModel(activeId == null ? "" : activeId + "", true);
        if (list != null) {
            for (EventType s : list) {
                combobox.addItem(s.getId() + "", s.getEventName());
            }
            request.setAttribute("eventType", combobox);
        }
    }

    
    protected static boolean areFilenamesAllowed(String fileName, String scannedFileName,
            int attachmentId, AttachmentDataProvider attDP) {
         
        if(fileName == null && scannedFileName == null) {
            return true;
        }
        else if(fileName != null && scannedFileName != null) {
            return !fileName.equals(scannedFileName);
        }
        else if(attachmentId == 0) {
            return true;
        }
        else {
            Attachment att = attDP.getAttachment(attachmentId, false, false);
            if(fileName != null && att.getScannedFileName() != null) {
                return !fileName.equals(att.getScannedFileName());
            }
            else if(scannedFileName != null && att.getFileName() != null) {
                return !scannedFileName.equals(att.getFileName());
            }
            return true;
        }
    }
    
    protected String getDocFlowUrl(NacidDataProvider nDP, int appId, int attId, 
            String editUrl) {
        Application app = nDP.getApplicationsDataProvider().getApplication(appId);
        return DocumentDocFlowHandler.prepareUrlForDocFlow(attId, getDocTypeCategory(appId), NacidDataProvider.APP_NACID_ID, app.getApplicationNumber(), editUrl);
    }
    
    protected Integer saveReminder(Integer eventType, Integer eventStatus, Attachment att, int applId) {
        Integer docTypeId = att.getDocTypeId();
        EventDataProvider eDP = getNacidDataProvider().getEventDataProvider();
        Event oldEvent = Utils.getListFirstElement(eDP.getEventsForDocument(att.getId(), att.getDocTypeId()));
//        Integer category = att.getDocType().getDocCategoryIds()
        if(oldEvent == null) {
            if(eventType == null) {
                return null;
            }
            else {
                eDP.recalculateEvent(0, eventType, applId, att.getId(), getDocTypeCategory(applId), docTypeId);
                return EventStatus.STATUS_WAITING;
            }
        }
        else {
            if(eventType == null) {
                eDP.deleteEvent(oldEvent.getId());
                return null;
            }
            else if(!eventType.equals(oldEvent.getEventTypeId())) {
                eDP.recalculateEvent(oldEvent.getId(), eventType, applId, att.getId(), getDocTypeCategory(applId), docTypeId);
                return EventStatus.STATUS_WAITING;
            }
            else if(!eventStatus.equals(oldEvent.getEventStatus()) || getDocTypeCategory(applId) != oldEvent.getDocCategoryId()  || !docTypeId.equals(oldEvent.getDocumentTypeId()) ){
                eDP.setStatus(oldEvent.getId(), eventStatus, getDocTypeCategory(applId), docTypeId);
                return eventStatus;
            }
            else {
                return eventStatus;
            }
        }
    }
    
    public static void addEventDatesToRequest(HttpServletRequest request, Event ev, NacidDataProvider nDP) {
        request.setAttribute("hasEventDates", ev != null);
        if(ev != null) {
            request.setAttribute("eventDueDate", DataConverter.formatDate(ev.getDueDate()));
            request.setAttribute("eventReminderDate", DataConverter.formatDate(ev.getReminderDate()));
            
            ApplicationsDataProvider appDP = nDP.getApplicationsDataProvider();
            Application app = appDP.getApplication(ev.getApplicationId());
            String docflowNum = null;
            if(app != null) {
                docflowNum = app.getDocFlowNumber();
            }
            
            String docType = null;
            try {
                NomenclaturesDataProvider nomDP = nDP.getNomenclaturesDataProvider();
                AttachmentDataProvider attDP = null;
                if (ev.getDocCategoryId() == DocCategory.APPLICATION_ATTACHMENTS)
                    attDP = nDP.getApplicationAttachmentDataProvider();
                else if (ev.getDocCategoryId() == DocCategory.DIPLOMA_EXAMINATIONS)
                    attDP = nDP.getDiplExamAttachmentDataProvider();
                else if (ev.getDocCategoryId() == DocCategory.UNIVERSITY_EXAMINATIONS)
                    attDP = nDP.getUniExamAttachmentDataProvider();
                
                Attachment att = attDP.getAttachment(ev.getDocId(), false, false);
                
                DocumentType dt = nomDP.getDocumentType(att.getDocTypeId());
                docType = dt.getName();
            }
            catch(NullPointerException e) {
                
            }
            
            request.setAttribute("docflownum", docflowNum);
            request.setAttribute("doctype", docType);
            
        }
    }

    public static void writeEmptyImage(HttpServletResponse response) {
        try (InputStream is = BaseAttachmentHandler.class.getResourceAsStream("/1by1.jpg")){
            BufferedImage src = ImageIO.read(is);
            ServletOutputStream sos = response.getOutputStream();
            ImageIO.write(src, "jpg", sos);
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }
}
