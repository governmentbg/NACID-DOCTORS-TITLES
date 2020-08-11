/**
 *
 */
package com.nacid.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//import com.aspose.words.jasperreports.License;
import com.nacid.report.export.ExportType;
import com.nacid.report.export.JRRtfExporterCustom;
import com.nacid.report.export.JasperReportNames;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JRDataSource;


/**
 * @author bily
 *
 */
public class JasperReportGenerator {

    private static final String REPORT_FILE_EXTENSION   = ".jasper";
    private static final String REPORTS_DIRECTORY       = "reportfiles";
    private static final String IMAGES_DIRECTORY        = "images";
    private static final String F_SEP                   = File.separator;
    private static final String PARAM_SUBREPORT_DIR     = "SUBREPORT_DIR";
    private static final String PARAM_IMAGE_DIR         = "IMAGE_DIR";
    private static final String IMAGES_URI              = "/nacid/images/";
    //private static final String LICENSE_FILE_NAME       = "Aspose.Words.JasperReports.lic";
    private static        String REPORTS_PATH            = null;
    private static        String SUBREPORTS_PATH         = null;
    private static        String CONTENT_PATH            = null;
    private static        String IMAGES_PATH             = null;
    private static        File   IMAGES_DIR              = null;


    private String          reportName      = null;
    private ExportType      exportFormat    = null;
    private JRDataSource    dataSource      = null;
    private OutputStream    exportResult    = null;
    private Map             params          = null;

    static {
        String path = JasperReportGenerator.class.getResource("JasperReportGenerator.class").getPath();
        try {
            REPORTS_PATH = ((new File(path)).getParent() + F_SEP + REPORTS_DIRECTORY + F_SEP).replace("%20", " ");
            CONTENT_PATH = (new File(path)).getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getPath().replace("%20", " ")+F_SEP;
            IMAGES_PATH  = CONTENT_PATH + IMAGES_DIRECTORY + F_SEP;
            IMAGES_DIR   = new File(CONTENT_PATH+IMAGES_DIRECTORY);
            //Change it when subreports directory is not reprtfiles. 
            SUBREPORTS_PATH = REPORTS_PATH;
            System.out.println("REPORTS_PATH   = " + REPORTS_PATH);
            System.out.println("CONTENT_PATH   = " + CONTENT_PATH);
            System.out.println("IMAGES_PATH   = " + IMAGES_PATH);
            System.out.println("SUBREPORTS_PATH   = " + SUBREPORTS_PATH);
            //TODO - to be removed later
            //registerAsosposeLicense();
        } catch(Exception ex) {
            System.err.println("Can not get compiled reports directory!" + ex.getMessage());
        }
    }

    /**
     * Construct report
     * @param reportName - one of the defined in {@link JasperReportNames} JASPER_REPORT_* constants
     * @param exportFormat - one of the defined in {@link ExportType} enum types
     * @param exportResult
     */
    public JasperReportGenerator( String            reportName,
                                  ExportType        exportFormat,
                                  OutputStream      exportResult) {
        this.reportName     = reportName;
        this.exportFormat   = exportFormat;
        this.exportResult   = exportResult;
        initDefaultParams();
    }


    /**
     * Export report with given collection of beans
     * @param data
     */
    public void export (Collection beans) throws JasperReportGeneratorException{
        dataSource = new JRBeanCollectionDataSource(beans);
        export();
    }

    /**
     * Set report parameters
     * @param params
     */
    public void setParams(Map params) {
        this.params = params;
    }

    /**
     * Get report parameters
     * @return
     */
    public Map getParams() {
        return params;
    }



    private void export() throws JasperReportGeneratorException{

        JasperReport jasperReport   = null;
        JasperPrint jasperPrint     = null;
        JRExporter exporter         = null;
        FileInputStream jasperXml   = null;
        String      reportFile      = null;
        //TODO
        try {
            reportFile = REPORTS_PATH+reportName+REPORT_FILE_EXTENSION;
            jasperXml = new FileInputStream(reportFile);
            ObjectInputStream ois = new ObjectInputStream(jasperXml);
            jasperReport = (JasperReport) ois.readObject();
        } catch(FileNotFoundException fEx) {
            throw new JasperReportGeneratorException("Can not get file "+reportFile, fEx);
        } catch(IOException ioEx) {
            throw new JasperReportGeneratorException("Unexpected IO exception occured. ", ioEx);
        } catch(ClassNotFoundException classEx) {
            throw new JasperReportGeneratorException("Unexpected ClassNotFoud exception occured. ", classEx);
        } finally {
            try {
                if(jasperXml !=null) jasperXml.close();
            } catch (IOException  ioCloseEx){
                throw new JasperReportGeneratorException("Unexpected IO exception occured when closing file " + reportFile, ioCloseEx);
            }
        }

        try {
        	
            jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
            //System.out.println("LENGTH = " + jasperReport.getPropertyNames().length);
    		for (String propName : jasperReport.getPropertyNames()) {
    			//System.out.println(" design propName = " + propName + "   propVal = " +jasperReport.getProperty(propName));
    			jasperPrint.setProperty(propName, jasperReport.getProperty(propName));
    		}
            

            //Export formats
            if(exportFormat == ExportType.RTF) {
                //exporter = new JRRtfExporter();
                exporter = new JRRtfExporterCustom();
            }else if(exportFormat == ExportType.PDF) {
                exporter = new JRPdfExporter();
            } else if (exportFormat == ExportType.DOCX) {
                exporter = new JRDocxExporter();
            } else if (exportFormat == ExportType.XLS) {
                exporter = new JRXlsExporter();
                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, true);
                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, true);
                exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN,true);
                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE , true);
                exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER,false);
            } else if (exportFormat == ExportType.HTML) {
                exporter = new JRHtmlExporter();
                exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR, IMAGES_DIR);
                exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, IMAGES_URI);
                exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, true);
            } else if (exportFormat == ExportType.CSV) {
                exporter = new JRCsvExporter();
            }

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, exportResult);
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
            
            exporter.exportReport();
            System.out.println("Report: \"" + reportName + "\" is generated and successfully exported as " + exportFormat);

        } catch(JRException jrEx) {
            throw new JasperReportGeneratorException("Can not fill report "+reportName, jrEx);
        }

    }
    
    private void initDefaultParams(){
    	params = new HashMap();
    	params.put(PARAM_SUBREPORT_DIR, SUBREPORTS_PATH);
    	params.put(PARAM_IMAGE_DIR, IMAGES_PATH);
    }

//    private static void registerAsosposeLicense() {
//    	try {
//    		FileInputStream fstream = new FileInputStream(REPORTS_PATH+F_SEP+LICENSE_FILE_NAME);
//            // Set the license through the stream object
//            License license = new License();
//            license.setLicense(fstream);
//    	} catch ( Exception e) {
//    		
//    	}
//    }

}
