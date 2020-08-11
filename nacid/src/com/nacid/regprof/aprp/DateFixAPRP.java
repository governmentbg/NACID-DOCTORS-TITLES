package com.nacid.regprof.aprp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.nacid.bl.impl.applications.regprof.RegprofApplicationDetailsImpl;
import com.nacid.bl.impl.nomenclatures.regprof.APRPImpl;
import com.nacid.data.DataConverter;
import com.nacid.db.applications.regprof.RegprofApplicationDB;
import com.nacid.utils.csv.Csv2Record;

public class DateFixAPRP {
    
    DataSource nacidDataSource;
    private static final String REPORT_FILENAME = "D:/reports/fix_production.txt";
    private static final List<Integer> SKIP_LINES = Arrays.asList(1, 45, 72, 129, 162, 163, 164, 165, 166, 167);

    public DateFixAPRP(DataSource nacidDataSource) {
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
    
    private void fixApplicationDates() throws IOException, ParseException, SQLException {
        Csv2Record<APRPImpl> csv2record = new Csv2Record<APRPImpl>(APRPImpl.class, "D:/projects/NACID/src/com/nacid/regprof/aprp/aprp.properties");
        csv2record.setSeparator(';');
        csv2record.setSkipLines(0);
        /*for (Integer lineNumber : SKIP_LINES) {
            csv2record.setSkipLines(lineNumber-1);
        }*/
        List<APRPImpl> csvLines = csv2record.generateRecords("D:/aprp2.csv");
        
        RegprofApplicationDB db = new RegprofApplicationDB(nacidDataSource);
        
        List<RegprofApplicationDetailsImpl> applications = db.selectRecords(RegprofApplicationDetailsImpl.class, null, null);
        Map<String, Integer> docflowToAppId = new HashMap<String, Integer>(); 
        for (RegprofApplicationDetailsImpl app : applications) {
            docflowToAppId.put(app.getAppNum() + "/" + DataConverter.formatDate(app.getAppDate()), app.getId());
        }
        
        int counter = 0;
        for (APRPImpl csvLine : csvLines) {
            counter++;
            if (SKIP_LINES.contains(counter)) {
                continue;
            }
            String entryDate = csvLine.getEntryDate() != null ? csvLine.getEntryDate().trim() : "";
            if (entryDate == null || entryDate.isEmpty()) {
                log(REPORT_FILENAME, "Entry date is not present on line: " + counter);
                continue;
            }
            Date applicationDate = DataConverter.parseLongShortDate(entryDate);
            
            String oldApplicationDateAsString = csvLine.getApplicationDate() != null ? csvLine.getApplicationDate().trim() : "";
            if (oldApplicationDateAsString == null || oldApplicationDateAsString.isEmpty()) {
                log(REPORT_FILENAME, "Line " + counter + " is ok");
                continue;
            }
            Date oldApplicationDate = DataConverter.parseLongShortDate(oldApplicationDateAsString);
            String docFlowId = csvLine.getDocflowNum() + "/" + DataConverter.formatDate(oldApplicationDate);
            Integer applicationId = docflowToAppId.get(docFlowId);
            if (applicationId == null) {
                log(REPORT_FILENAME, "Docflow id is not present: " + docFlowId);
                continue;
            }
            RegprofApplicationDetailsImpl application = db.selectRecord(new RegprofApplicationDetailsImpl(), applicationId);
            log(REPORT_FILENAME, "Line: " + counter + " id: " + application.getId() + " Old date: " + oldApplicationDate + " New date: " + applicationDate);
            application.setAppDate(applicationDate);
            db.updateRecord(application, "app_date");
        }
    }
    
    public static void increment(int i) {
        i++;
        System.out.println(i);
    }
    
    public static void main(String[] args) throws IOException, ParseException, SQLException {
        //DateFixAPRP dataProvider = new DateFixAPRP(new StandAloneDataSource("jdbc:postgresql://localhost:9000/NACID/", "postgres", "postgres"));
        //dataProvider.fixApplicationDates();
        //BigDecimal m = new BigDecimal("2.000009");
        //System.out.println(m.setScale(2, RoundingMode.UP));
       // System.out.println(m.setScale(2, RoundingMode.HALF_DOWN));
        int i = 1;
        increment(i);
        System.out.println(i);
    }

}
