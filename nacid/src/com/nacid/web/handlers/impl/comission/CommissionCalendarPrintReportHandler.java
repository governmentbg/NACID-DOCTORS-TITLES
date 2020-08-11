package com.nacid.web.handlers.impl.comission;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.ApplicationDetailsForReport;
import com.nacid.bl.comision.CommissionCalendarDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.data.DataConverter;
import com.nacid.report.TemplateGenerator;
import com.nacid.web.handlers.NacidBaseRequestHandler;

public class CommissionCalendarPrintReportHandler extends NacidBaseRequestHandler {

    ServletContext servletContext;
    public CommissionCalendarPrintReportHandler(ServletContext servletContext) {
        super(servletContext);
        this.servletContext = servletContext;
    }

    @Override
    public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
        
        int calendarId = DataConverter.parseInt(request.getParameter("calendar_id"), -1);
        if(calendarId <= 0) {
            throw Utils.logException(new Exception("Unknown calendar id " + calendarId));
        }
        if(calendarId <= 0) {
            throw Utils.logException(new Exception("Unknown new app status id " + calendarId));
        }
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        CommissionCalendarDataProvider commissionCalendarDataProvider = nacidDataProvider.getCommissionCalendarDataProvider();
        List<ApplicationDetailsForReport> apps = commissionCalendarDataProvider.getApplicationDetailsForReport(calendarId);
        InputStream allCertsIs = TemplateGenerator.generateApplicationsXlsForCommissionCalendarReport( nacidDataProvider,  apps);
        response.setContentType("application/vnd.ms-excel");
        try {
            ServletOutputStream sos = response.getOutputStream();

            int read = 0;
            byte[] buf = new byte[1024];

            while ((read = allCertsIs.read(buf)) > 0) {
                sos.write(buf, 0, read);
            }

        } catch (Exception e) {
            throw Utils.logException(e);
        } finally {
            if (allCertsIs != null) {
                try {
                    allCertsIs.close();
                } catch (IOException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
}
