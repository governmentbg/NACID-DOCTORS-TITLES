package com.nacid.regprof.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplication;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachment;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachmentDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.data.DataConverter;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.impl.applications.DocumentDocFlowHandler;
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

import static com.nacid.web.handlers.impl.applications.BaseAttachmentHandler.writeEmptyImage;

//RayaWritten-----------------------------------------------------------------

public abstract class RegprofBaseAttachmentHandler extends RegProfBaseRequestHandler {
    
    private class ImgObserver implements ImageObserver {

        boolean ready = false;

        @Override
        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            return ready = (infoflags & ALLBITS) != 0;
        }
    };
    
    public RegprofBaseAttachmentHandler(ServletContext servletContext) {
        super(servletContext);
    }
    

    protected abstract RegprofApplicationAttachmentDataProvider getRegprofApplicationAttachmentDataProvider();
    protected abstract int getDocTypeCategory();
    
    @Override
    public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
        int attachmentId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (attachmentId <= 0) {
            throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
        }
        
        RegprofApplicationAttachmentDataProvider attDP = getRegprofApplicationAttachmentDataProvider();
        RegprofApplicationAttachment att = attDP.getAttachment(attachmentId, false, false);
        //String fileName = getOperationName(request);
        boolean scanned;
        boolean isOriginal = DataConverter.parseBoolean(request.getParameter("original")); //COPY IN MY CODE
        if (isOriginal || StringUtils.isEmpty(att.getScannedContentType())) {
            scanned = false;
        } else {
            scanned = true;
        }
        /*
        if (!StringUtils.isEmpty(att.getScannedFileName())) {
            scanned = true;
        } else {
            scanned = false;
        }*/
        
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

        RegprofApplicationAttachmentDataProvider attDP = getRegprofApplicationAttachmentDataProvider();

        RegprofApplicationAttachment att = attDP.getAttachment(attachmentId, false, false);

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

    public static String prepareFileName(String fileName) {
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
        return fileName;
    }

    protected void generateDocTypesComboBox(Integer activeId, HttpServletRequest request) {
        List<? extends FlatNomenclature> flatNomeclatures = null;

        flatNomeclatures = getNacidDataProvider().getNomenclaturesDataProvider()
            .getDocumentTypes(null, null, getDocTypeCategory());

        ComboBoxWebModel combobox = new ComboBoxWebModel(activeId == null ? "" : activeId + "", true);
        if (flatNomeclatures != null) {
            for (FlatNomenclature s : flatNomeclatures) {
                combobox.addItem(s.getId() + "", ((DocumentType) s).getLongName());
            }
            request.setAttribute("docTypeCombo", combobox);
        }
    }
   

    protected static boolean areFilenamesAllowed(String fileName, String scannedFileName,
            int attachmentId, RegprofApplicationAttachmentDataProvider attDP) {
         
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
            RegprofApplicationAttachment att = attDP.getAttachment(attachmentId, false, false);
            if(fileName != null && att.getScannedFileName() != null) {
                return !fileName.equals(att.getScannedFileName());
            }
            else if(scannedFileName != null && att.getFileName() != null) {
                return !scannedFileName.equals(att.getFileName());
            }
            return true;
        }
    }

    protected String getDocFlowUrl(NacidDataProvider nDP, int appId, int attId, String editUrl) {
        RegprofApplication app = nDP.getRegprofApplicationDataProvider().getRegprofApplication(appId);
        return DocumentDocFlowHandler.prepareUrlForDocFlow(attId, getDocTypeCategory(), NacidDataProvider.APP_NACID_REGPROF_ID, app.getApplicationNumber(), editUrl);
    }

}
//-----------------------------------------------------------------------------------------------------------
