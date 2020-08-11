package com.nacid.web.handlers.impl.comission;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;

import com.nacid.bl.comision.CommissionCalendar;
import com.nacid.bl.comision.CommissionCalendarDataProvider;
import com.nacid.bl.comision.CommissionCalendarProtocol;
import com.nacid.bl.impl.Utils;
import com.nacid.data.DataConverter;
import com.nacid.report.TemplateGenerator;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.impl.applications.FileUploadListener;
import com.nacid.web.model.comission.CommissionCalendarProtocolWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;

public class CalendarSessionProtocolHandler extends NacidBaseRequestHandler {

    private ServletContext servletContext;
    public CalendarSessionProtocolHandler(ServletContext servletContext) {
        super(servletContext);
        this.servletContext = servletContext;
    }

    @Override
    public void handleView(HttpServletRequest request, HttpServletResponse response) {
        int calendarId = DataConverter.parseInt(request.getParameter("calendar_id"), -1);
        
        String fileName = null;
        CommissionCalendarDataProvider calDP = getNacidDataProvider().getCommissionCalendarDataProvider();

        CommissionCalendarProtocol protocol = calDP.loadCalendarProtocol(calendarId, false);
        
        if(protocol != null) {
            fileName = protocol.getFileName();
            if(fileName == null) {
                fileName = "protocol";
            }
        }
        String operation = getOperationName(request);
        request.setAttribute(WebKeys.COMMISSION_CALENDAR_PROTOCOL_WEB_MODEL, 
                new CommissionCalendarProtocolWebModel(calendarId, operation, fileName));
    }
    
    @Override
    public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
        int calendarId = DataConverter.parseInt(request.getParameter("calendarId"), -1);
        if (calendarId <= 0) {
            throw new UnknownRecordException("Unknown attachment ID:" + calendarId);
        }
        
        CommissionCalendarDataProvider calDP = getNacidDataProvider().getCommissionCalendarDataProvider();
        CommissionCalendarProtocol protocol = calDP.loadCalendarProtocol(calendarId, true);
        
        
        if (protocol == null) {
            throw new UnknownRecordException("Unknown attachment ID:" + calendarId);
        }
        
        response.setContentType(protocol.getContentType());
        
        InputStream is = protocol.getContent();
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
    
    @SuppressWarnings("unchecked")
    @Override
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
        
        int calendarId = 0;
        String operation = null;
        try {
            // iterate over all uploaded files
            uploadedItems = upload.parseRequest(request);

            InputStream is = null;
            boolean generate = false;
            int fileSize = 0;
            String fileName = null;
            String contentType = null;
            
            Iterator iter = uploadedItems.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                if (item.isFormField()) {
                    if(item.getFieldName().equals("calendarId"))
                        calendarId = DataConverter.parseInt(item.getString("UTF-8"), 0);
                    else if (item.getFieldName().equals("generate")) {
                        generate = DataConverter.parseBoolean(item.getString("UTF-8"));
                    }
                    else if (item.getFieldName().equals("operation")) {
                        operation = item.getString("UTF-8");
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
            
            CommissionCalendarDataProvider calDP = getNacidDataProvider().getCommissionCalendarDataProvider();
            CommissionCalendar comCal = calDP.getCommissionCalendar(calendarId);
            
            if (comCal == null) {
                throw new UnknownRecordException("Unknown calendar record ID:" + calendarId);
            }
            
            if(generate) {
                contentType = "application/msword";
                is = TemplateGenerator.generateCommissionProtocol(getNacidDataProvider(), calendarId);
                fileSize = is.available();
                fileName = comCal.getSessionNumber() 
                    + "_commission_session_protocol_"
                    + DataConverter.formatDate(comCal.getDateAndTime())
                    + ".doc";
                fileName = fileName.replace("/", "_");
            }
            
            if (fileSize <= 0) {
                String errMsg = "Не е посочен файл";
                if(generate) {
                    throw Utils.logException(new Exception("Generated doc is with size 0"));
                }
                SystemMessageWebModel webmodel = new SystemMessageWebModel(errMsg);
                addSystemMessageToSession(request, "protocolMsg", webmodel);
                
            } else {
            
                calDP.addCalendarProtocol(calendarId, is, contentType, fileName, fileSize); 
            
               
                addSystemMessageToSession(request, "protocolMsg", new SystemMessageWebModel("Данните бяха въведени в базата",
                        SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
            }
        } catch (Exception e) {
            throw Utils.logException(this, e);
        }
        finally {
            session.removeAttribute(WebKeys.FILE_UPLOAD_LISTENER);
        }
        
        String url = servletContext.getAttribute("pathPrefix") 
                + "/control" 
                + "/comission_calendar_process" 
                + "/" + operation
                + "?calendar_id=" + calendarId;
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            Utils.logException(e);
        }
    }
}
