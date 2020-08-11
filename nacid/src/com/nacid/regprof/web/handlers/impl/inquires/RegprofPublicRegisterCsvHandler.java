package com.nacid.regprof.web.handlers.impl.inquires;

import au.com.bytecode.opencsv.CSVWriter;
import com.ext.nacid.regprof.web.handlers.impl.RegprofPublicRegisterHandler;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplicationDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplicationForPublicRegister;
import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ext.nacid.regprof.web.handlers.impl.RegprofPublicRegisterHandler.REGISTER_TYPE_IZDADENI;
import static com.ext.nacid.regprof.web.handlers.impl.RegprofPublicRegisterHandler.REGISTER_TYPE_OTKAZI;

/**
 * Created by georgi.georgiev on 9.10.2017 г..
 */
public class RegprofPublicRegisterCsvHandler extends RegProfBaseRequestHandler {
    public RegprofPublicRegisterCsvHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public RegprofPublicRegisterCsvHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handlePrint(HttpServletRequest request, HttpServletResponse response) {
        try {
            RegprofApplicationDataProvider dp = getNacidDataProvider().getRegprofApplicationDataProvider();
            NomenclaturesDataProvider ndp = getNacidDataProvider().getNomenclaturesDataProvider();

            response.setHeader("content-disposition", "attachment; filename=public_register_NoRQ.csv");
            response.setContentType("text/csv");
            CSVWriter w = new CSVWriter(response.getWriter(), ',', '"');
            addHeaders(w);
            addAppsByFinalStatus(dp, ndp, REGISTER_TYPE_IZDADENI, w);
            addAppsByFinalStatus(dp, ndp, REGISTER_TYPE_OTKAZI, w);
            addAppsByFinalStatus(dp, ndp, REGISTER_TYPE_OTKAZI, w);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addAppsByFinalStatus(RegprofApplicationDataProvider dp, NomenclaturesDataProvider ndp, int registerType, CSVWriter w) {
        List<? extends RegprofApplicationForPublicRegister> apps = dp.getRegprofApplicationsByFinalStatuses(RegprofPublicRegisterHandler.REGISTER_TYPE_TO_FINAL_STATUS.get(registerType));

        for (RegprofApplicationForPublicRegister app : apps) {
            w.writeNext(new String[]{
                    app.getAppNum(),
                    DataConverter.formatDate(app.getAppDate()),
                    ndp.getApplicationStatus(NumgeneratorDataProvider.REGPROF_SERIES_ID, app.getFinalStatusId()).getName(),
                    app.getApplicantName(),
                    app.getApplicationCountryName(),
                    registerType == REGISTER_TYPE_IZDADENI ?
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
        line.add("Кандидатства за държава");
        line.add("Номер на удостоверение");
        w.writeNext(line.toArray(new String[line.size()]));
    }
}
