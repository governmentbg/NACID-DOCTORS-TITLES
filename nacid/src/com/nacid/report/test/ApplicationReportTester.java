package com.nacid.report.test;

import java.io.FileOutputStream;
import java.util.Arrays;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.ApplicationDetailsForReport;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.report.JasperReportGenerator;
import com.nacid.report.export.ExportType;
import com.nacid.report.export.JasperReportNames;

public class ApplicationReportTester {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
		ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
		ApplicationDetailsForReport a = applicationsDataProvider.getApplicationDetailsForReport(88);
		System.out.println(a.getOwnerName());
		System.out.println(a.getOwnerDiplomaName());
		FileOutputStream os = new FileOutputStream("c:/test.rtf");
		JasperReportGenerator generator = new JasperReportGenerator(JasperReportNames.JASPER_REPORT_PISMO_ZAIAVITEL_INFO, ExportType.RTF, os);
		//generator.getParams().remove("IMAGES_DIR");
		//Map<String, String> commonVars = nacidDataProvider.getUtilsDataProvider().getCommonVariablesAsMap();
		//commonVars.put("expertName", "ЕКСПЕРТ ЕКСПЕРТОВ");
		//System.out.println(commonVars.get("nacidDirectorPosition"));
		//generator.setParams(commonVars);
		generator.export(Arrays.asList(a));
		os.close();
	}

}
