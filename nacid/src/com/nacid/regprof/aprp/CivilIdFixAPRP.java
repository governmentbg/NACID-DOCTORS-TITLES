package com.nacid.regprof.aprp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Person;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.regprof.RegprofApplicationDetailsImpl;
import com.nacid.bl.impl.nomenclatures.regprof.APRPImpl;
import com.nacid.data.DataConverter;
import com.nacid.data.applications.PersonDocumentRecord;
import com.nacid.db.applications.regprof.RegprofApplicationDB;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.utils.csv.Csv2Record;

public class CivilIdFixAPRP {
    
    DataSource nacidDataSource;
    private static final String REPORT_FILENAME = "D:/reports/civil_id_fix.txt";
    private static final List<Integer> SKIP_LINES = Arrays.asList(1, 45, 72, 129, 162, 163, 164, 165, 166, 167);

    public CivilIdFixAPRP(DataSource nacidDataSource) {
        this.nacidDataSource = nacidDataSource;
    }
    
    private static void log(String fileName, String line) throws IOException {
        File report = new File(fileName);
        BufferedWriter reportWriter = new BufferedWriter(new FileWriter(report, true));
        System.out.println(line);
        reportWriter.write(line);
        reportWriter.newLine();
        reportWriter.flush();
        reportWriter.close();
    }
    
    private void fixCivilIds() throws IOException, ParseException, SQLException {
        Csv2Record<APRPImpl> csv2record = new Csv2Record<APRPImpl>(APRPImpl.class, "D:/projects/NACID/src/com/nacid/regprof/aprp/aprp.properties");
        csv2record.setSeparator(';');
        csv2record.setSkipLines(0);
        List<APRPImpl> csvLines = csv2record.generateRecords("D:/aprp2.csv");
        
        RegprofApplicationDB db = new RegprofApplicationDB(nacidDataSource);
        
        List<RegprofApplicationDetailsImpl> applications = db.selectRecords(RegprofApplicationDetailsImpl.class, null, null);
        Map<String, Integer> docflowToAppId = new HashMap<String, Integer>(); 
        for (RegprofApplicationDetailsImpl app : applications) {
            docflowToAppId.put(app.getAppNum() + "/" + DataConverter.formatDate(app.getAppDate()), app.getId());
        }
        
        NacidDataProvider nacidDataProvider = new NacidDataProviderImpl(nacidDataSource);
        
        int counter = 0;
        for (APRPImpl csvLine : csvLines) {
            counter++;
            if (SKIP_LINES.contains(counter)) {
                continue;
            }
            if (csvLine.getApplicantCivilId() == null || csvLine.getApplicantCivilId().trim().isEmpty()) {
                log(REPORT_FILENAME, "Line: " + counter);
                
                String docflowId = csvLine.getDocflowNum().trim() + "/" + DataConverter.formatDate(DataConverter.parseLongShortDate(csvLine.getEntryDate().trim()));
                log(REPORT_FILENAME, "app id = " + docflowToAppId.get(docflowId));
                String documentCivilId = csvLine.getDocumentNumber() != null ? csvLine.getDocumentNumber().trim() : "";
                Person applicant = nacidDataProvider.getApplicationsDataProvider().getPersonByCivilId(documentCivilId);
                if (applicant == null) {
                    log(REPORT_FILENAME, "applicant is null");
                } else {
                    log(REPORT_FILENAME, "applicant id = " + applicant.getId());
                    List<PersonDocumentRecord> list = nacidDataProvider.getRegprofApplicationDataProvider().getDocumentRecordsByPersonId(applicant.getId());
                    Integer applicationId = docflowToAppId.get(docflowId);
                    RegprofApplicationDetailsImpl application = db.selectRecord(new RegprofApplicationDetailsImpl(), applicationId);
                    application.setApplicantId(applicant.getId());
                    application.setApplicantDocumentsId(Utils.getListFirstElement(list).getId());
                    db.updateRecord(application, "applicant_id", "applicant_documents_id");
                }
            }
            
        }
    }
    /** select * from person where id in (6034, 6075, 6076, 6077, 6079, 6085, 6086, 6135); */
    public static void main(String[] args) throws IOException, ParseException, SQLException {
        CivilIdFixAPRP dataProvider = new CivilIdFixAPRP(new StandAloneDataSource("jdbc:postgresql://localhost:9000/NACID/", "postgres", "postgres"));
        dataProvider.fixCivilIds();
    }
}