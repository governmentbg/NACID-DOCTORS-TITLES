package com.nacid.web.handlers.impl.inquires;

import au.com.bytecode.opencsv.CSVWriter;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.ApplicationForPublicRegister;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgi.georgiev on 9.10.2017 г..
 */
public class PublicRegisterCsvHandler extends NacidBaseRequestHandler{
    public PublicRegisterCsvHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public PublicRegisterCsvHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handlePrint(HttpServletRequest request, HttpServletResponse response) {
        try {
            ApplicationsDataProvider dp = getNacidDataProvider().getApplicationsDataProvider();
            NomenclaturesDataProvider ndp = getNacidDataProvider().getNomenclaturesDataProvider();

            response.setHeader("content-disposition", "attachment; filename=public_register_RUDi.csv");
            response.setContentType("text/csv");
            CSVWriter w = new CSVWriter(response.getWriter(), ',', '"');
            addHeaders(w);
            addAppsByFinalStatus(dp, ndp, ApplicationStatus.APPLICATION_PRIZNATO_STATUS_CODE, w);
            addAppsByFinalStatus(dp, ndp, ApplicationStatus.APPLICATION_REFUSED_STATUS_CODE, w);
            addAppsByFinalStatus(dp, ndp, ApplicationStatus.APPLICATION_OBEZSILENO_STATUS_CODE, w);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addAppsByFinalStatus(ApplicationsDataProvider dp, NomenclaturesDataProvider ndp, int finalStatus, CSVWriter w) {
        List<? extends ApplicationForPublicRegister> apps = dp.getApplicationsByFinalStatus(finalStatus);

        for (ApplicationForPublicRegister app : apps) {
            w.writeNext(new String[]{
                    app.getAppNum(),
                    DataConverter.formatDate(app.getAppDate()),
                    ndp.getApplicationStatus(NumgeneratorDataProvider.NACID_SERIES_ID, app.getFinalStatusId()).getName(),
                    app.getApplicantName(),
                    app.getUniversityName(),
                    app.getUniversityCountry(),
                    app.getRecognizedSpecialityName(),
                    app.getFinalStatusId() == ApplicationStatus.APPLICATION_PRIZNATO_STATUS_CODE ?
                        app.getValidatedCertNumber():
                        StringUtils.join(app.getInvalidatedCertNumbers(), "; ")

            });
        }
    }

    private static void addHeaders(CSVWriter w) {
        List<String> line = new ArrayList<>();
        line.add("Деловоден номер");
        line.add("Дата");
        line.add("Краен статус");
        line.add("Име на заявител");
        line.add("Висше училище");
        line.add("Държава");
        line.add("Специалност");
        line.add("Номер на удостоверение");
        w.writeNext(line.toArray(new String[line.size()]));
    }
}
