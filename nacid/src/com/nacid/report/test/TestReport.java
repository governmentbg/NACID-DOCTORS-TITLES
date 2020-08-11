/**
 *
 */
package com.nacid.report.test;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

import com.nacid.report.JasperReportGenerator;
import com.nacid.report.export.ExportType;
import com.nacid.report.test.bean.CommissionProtocol;

/**
 * @author bily
 *
 */
public class TestReport {
	
	private static final String OUTPUT_DIR="/opt/virtualBox/sharedFolders/reports/";
	private static final String SUBREPORT_DIR="/home/bily/projects/nacid/trunk/build/classes/com/nacid/report/reportfiles/";
	private static final String IMAGE_DIR="/home/bily/projects/nacid/trunk/WebContent/images/";

    /**
     * @param args
     */
    /**
     * @param args
     */
    public static void main(String[] args) {

        OutputStream result = null;
        JasperReportGenerator report = null;
        Collection         coll = null;
        try {
        	
        	for (int i = 0; i < args.length; i++) {
        		if (!args[i].equals("protokol")) {
        			coll = SampleJRDataSourceFactory.createBeanCollection();
        		}else {
        			coll = new ArrayList();
        			coll.add(CommissionProtocol.getCommissionProtocl());
        		}
	        	//RTF
	            result = new FileOutputStream(OUTPUT_DIR+args[i]+".rtf");
	            report = new JasperReportGenerator(args[i], ExportType.RTF, result);
	            report.getParams().put("IMAGE_DIR", IMAGE_DIR);
	            report.getParams().put("SUBREPORT_DIR", SUBREPORT_DIR);
	            report.getParams().put("nacidDirectorName", "Ваня Грашкина");
	            report.getParams().put("nacidDirectorPosition", "Изпълнителен директор");
	            report.export(coll);
	            result.flush();
	            result.close();
	        	
	        	//PDF
	            result = new FileOutputStream(OUTPUT_DIR+args[i]+".pdf");
	            report = new JasperReportGenerator(args[i], ExportType.PDF, result);
	            report.getParams().put("IMAGE_DIR", IMAGE_DIR);
	            report.getParams().put("SUBREPORT_DIR", SUBREPORT_DIR);
	            report.getParams().put("nacidDirectorName", "Ваня Грашкина");
	            report.getParams().put("nacidDirectorPosition", "Изпълнителен директор");
	            report.export(coll);
	            result.flush();
	            result.close();

//	            //RTF_CUSTOM
//	            result = new FileOutputStream(OUTPUT_DIR+args[i]+"_custom.rtf");
//	            report = new JasperReportGenerator(args[i], ExportType.RTF_CUSTOM, result);
//	            report.getParams().put("IMAGE_DIR", IMAGE_DIR);
//	            report.getParams().put("SUBREPORT_DIR", SUBREPORT_DIR);
//	            report.getParams().put("nacidDirectorName", "Ваня Грашкина");
//	            report.getParams().put("nacidDirectorPosition", "Изпълнителен директор");
//	            report.export(coll);
//	            result.flush();
//	            result.close();
//	            
//	            //RTF_ASPOSE
//	            result = new FileOutputStream(OUTPUT_DIR+args[i]+"_aspose.rtf");
//	            report = new JasperReportGenerator(args[i], ExportType.RTF_ASPOSE, result);
//	            report.getParams().put("IMAGE_DIR", IMAGE_DIR);
//	            report.getParams().put("SUBREPORT_DIR", SUBREPORT_DIR);
//	            report.getParams().put("nacidDirectorName", "Ваня Грашкина");
//	            report.getParams().put("nacidDirectorPosition", "Изпълнителен директор");
//	            report.export(coll);
//	            result.flush();
//	            result.close();

        	}
            
        } catch(Exception ex) {
            System.err.println("Unexpetcted exception occured. " + ex.getMessage());
            for(Object ob:ex.getStackTrace())
            	System.err.println(ob);
        }

    }

}
