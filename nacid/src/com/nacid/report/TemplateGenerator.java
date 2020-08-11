package com.nacid.report;

import com.nacid.bl.ApplicationDetailsForExpertPosition;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.QrUtils;
import com.nacid.bl.applications.ApplicationDetailsForReport;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplicationDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplicationDetailsForReport;
import com.nacid.bl.comision.CommissionCalendarDataProvider;
import com.nacid.bl.comision.CommissionProtocol;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.ApplicationDetailsForReportImpl;
import com.nacid.bl.impl.applications.regprof.RegprofApplicationDetailsForReportImpl;
import com.nacid.bl.nomenclatures.DocCategory;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.utils.UniversityDetail;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.report.export.ExportType;
import com.nacid.report.export.JasperReportNames;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.model.inquires.ScreenFormatWebModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import sun.misc.MessageUtils;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

public class TemplateGenerator {
    private static Logger logger = Logger.getLogger(TemplateGenerator.class);
    @AllArgsConstructor
    @Getter
    public static class GenerateCertificateRequest {
        private ApplicationDetailsForReport applicationDetailsForReport;
        private String cerificateNumber;
        private UUID uuid;
    }

	public static InputStream generateDocFromTemplate(NacidDataProvider nacidDataProvider, int applicationId, DocumentType docType) {
    	return generateDocsFromTemplate(nacidDataProvider, Arrays.asList(applicationId), docType);
    }
    public static InputStream generateRegprofCertificate(NacidDataProvider nacidDataProvider, int applicationId, DocumentType docType, String certificateNumber, UUID certificateUuid) {
	    Map<String, Object> additionalParams = new HashMap<>();
	    if (certificateNumber == null || certificateUuid == null) {
	        throw new RuntimeException("Certificate number and uuid should not be null!");
        }
        String qrUrl = nacidDataProvider.getUtilsDataProvider().getCommonVariableValue(UtilsDataProvider.REGPROF_QRCODE_URL);
        qrUrl = MessageFormat.format(qrUrl, certificateUuid.toString());
        additionalParams.put("P_QRCODE", QrUtils.generateQRCodeImage(qrUrl));
        RegprofApplicationDataProvider applicationsDataProvider = nacidDataProvider.getRegprofApplicationDataProvider();
        RegprofApplicationDetailsForReport app = applicationsDataProvider.getRegprofApplicationDetailsForReport(applicationId);
        if (app == null) {
            throw Utils.logException(new RuntimeException("No application data to generate..."));
        }
        ((RegprofApplicationDetailsForReportImpl)app).setCertificateNumber(certificateNumber);
        return generateInputStream(nacidDataProvider, new RegprofApplicationDetailsForReport[]{app}, docType.getDocumentTemplate(), ExportType.RTF, additionalParams);
    }

    public static InputStream generateRegprofDocFromTemplate(NacidDataProvider nacidDataProvider, int applicationId, DocumentType docType) {
        return generateRegprofDocsFromTemplate(nacidDataProvider, Arrays.asList(applicationId), docType);
    }
    public static InputStream generateRegprofDocFromTemplateWithAdditionalParams(NacidDataProvider nacidDataProvider, int applicationId, DocumentType docType, 
            Map<String, Object> additionalParams) {
        return generateRegprofDocsFromTemplate(nacidDataProvider, Arrays.asList(applicationId), docType, additionalParams);
    }
    /**
	 * sy6tiq kato {@link TemplateGenerator#generateDocFromTemplate(NacidDataProvider, int, DocumentType)} no vmesto applicationId, se podava napravo application
	 */
	public static InputStream generateDocFromTemplate(NacidDataProvider nacidDataProvider, ApplicationDetailsForReport application, DocumentType docType) {
        return generateDocsFromTemplate(nacidDataProvider, new ApplicationDetailsForReport[]{application}, docType);
	}
	public static InputStream generateDocFromTemplate(NacidDataProvider nacidDataProvider, RegprofApplicationDetailsForReport application, DocumentType docType) {
        return generateDocsFromTemplate(nacidDataProvider, new RegprofApplicationDetailsForReport[]{application}, docType);
	}
	/**
	 * @param nacidDataProvider
	 * @param applicationId
	 * @param docType
	 * @param additionalParams - dopylnitelni parametri, koito se podavat na jasper generatora (osven tezi ot common_vars, koito se predavat po default)
	 * @return .doc file ot tip docType za zadadeniq application s applicationId 
	 */
	public static InputStream generateDocFromTemplate(NacidDataProvider nacidDataProvider, int applicationId, DocumentType docType, Map<String, Object> additionalParams) {
        return generateDocsFromTemplate(nacidDataProvider, Arrays.asList(applicationId), docType, additionalParams);
	}

    /**
     * shte se polzva samo za generirane na udostoverenie, tyi kato pri nego v nqkoi slu4ai certificateNumber-a + uuid trqbva da se podadat na ryka, a ne da se 4etat ot bazata
     * @param nacidDataProvider
     * @return
     */
    public static InputStream generateCertificate(NacidDataProvider nacidDataProvider, int docTypeId, GenerateCertificateRequest request) {
        return generateCertificates(nacidDataProvider, docTypeId, Arrays.asList(request));
    }
    public static InputStream generateCertificates(NacidDataProvider nacidDataProvider, int docTypeId, List<GenerateCertificateRequest> requests) {
        DocumentType docType = nacidDataProvider.getNomenclaturesDataProvider().getDocumentType(docTypeId);
        Map<String, Object> additionalParams = new HashMap<>();
        additionalParams.put("P_DOC_TYPE_ID", docTypeId);

        for (GenerateCertificateRequest request : requests) {
            if (request.getCerificateNumber() == null || request.getUuid() == null) {
                throw new RuntimeException("Cannot generate certificate. CertificateNumber and uuid should not be empty!!!");
            }

            //2011.01.18 - Tova reshenie ne e nai-inteligentnoto - da se raz4ita 4e applicationDetailsForReport e ot type ApplicationDetailsForReportImpl, no ne mi se misli po dobro reshenie, a i maj nqma smisyl....
            ApplicationDetailsForReportImpl applicationDetailsForReport = (ApplicationDetailsForReportImpl) request.getApplicationDetailsForReport();
            applicationDetailsForReport.setCertificateNumber(request.getCerificateNumber());

            String qrUrl = nacidDataProvider.getUtilsDataProvider().getCommonVariableValue(UtilsDataProvider.RUDI_QRCODE_URL);
            qrUrl = MessageFormat.format(qrUrl, request.getUuid().toString());
            applicationDetailsForReport.setQrCode(QrUtils.generateQRCodeImage(qrUrl));;
        }

        return generateInputStream(nacidDataProvider, requests.stream().map(r -> r.getApplicationDetailsForReport()).toArray(), docType.getDocumentTemplate(), ExportType.RTF, additionalParams);
    }

    public static InputStream generateExpertDocument(NacidDataProvider nacidDataProvider, int applicationId, int expertId, int documentType, Map<String, String> additionalParameters) {
        ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
        ApplicationDetailsForExpertPosition applicationDetailsForExpertPosition = applicationsDataProvider.getApplicationDetailsForExpertPosition(applicationId, expertId);
        DocumentType docType = nacidDataProvider.getNomenclaturesDataProvider().getDocumentType(documentType);
        return generateInputStream(nacidDataProvider, new ApplicationDetailsForReport[] {applicationDetailsForExpertPosition}, docType.getDocumentTemplate(), ExportType.RTF, additionalParameters);
    }
	
	public static InputStream generateAnnulment(NacidDataProvider nacidDataProvider, int applicationId) {
	    ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
	    ApplicationDetailsForReport r = applicationsDataProvider.getApplicationDetailsForReport(applicationId);
	    DocumentType docType = nacidDataProvider.getNomenclaturesDataProvider().getDocumentType(DocumentType.DOC_TYPE_ZAPOVED_OBEZSILVANE);
	    return generateInputStream(nacidDataProvider, new ApplicationDetailsForReport[] {r}, docType.getDocumentTemplate(), ExportType.RTF, null);
	}
	
	public static InputStream generateTest(NacidDataProvider nacidDataProvider, int applicationId, String templateName) {
	    ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
	    ApplicationDetailsForReport r = applicationsDataProvider.getApplicationDetailsForReport(applicationId);
	    //DocumentType docType = nacidDataProvider.getNomenclaturesDataProvider().getDocumentType(docId);
	    return generateInputStream(nacidDataProvider, new ApplicationDetailsForReport[] {r}, templateName, ExportType.RTF, null);
	}
	
	public static InputStream generateRegprofTest(NacidDataProvider nacidDataProvider, int applicationId, String templateName) {
	        RegprofApplicationDataProvider applicationsDataProvider = nacidDataProvider.getRegprofApplicationDataProvider();
	        RegprofApplicationDetailsForReport r = applicationsDataProvider.getRegprofApplicationDetailsForReport(applicationId);
	        //DocumentType docType = nacidDataProvider.getNomenclaturesDataProvider().getDocumentType(docId);
	        return generateInputStream(nacidDataProvider, new RegprofApplicationDetailsForReport[] {r}, templateName, ExportType.RTF, null);
	}
	
	public static InputStream generateCommissionProtocol(NacidDataProvider nacidDataProvider, int calendarId) {
    	CommissionCalendarDataProvider commissionCalendarDataProvider = nacidDataProvider.getCommissionCalendarDataProvider();
    	CommissionProtocol protocol = commissionCalendarDataProvider.getCommissionProtocolForReport(calendarId);
    	if (protocol == null) {
        	throw Utils.logException(new UnknownRecordException("Unknown CommissionCalendar with ID = " + calendarId));
        }
    	return generateInputStream(nacidDataProvider, new Object[]{protocol}, JasperReportNames.JASPER_REPORT_PROTOCOL, ExportType.RTF, null);
    }
    
    public static InputStream generateDocsFromTemplate(NacidDataProvider nacidDataProvider, List<Integer> applicationIds, DocumentType docType) {
        return generateDocsFromTemplate(nacidDataProvider, applicationIds, docType, null);
    }
    
    private static InputStream generateRegprofDocsFromTemplate(NacidDataProvider nacidDataProvider, List<Integer> applicationIds, DocumentType docType) {
        return generateRegprofDocsFromTemplate(nacidDataProvider, applicationIds, docType, null);
    }
    /**
     * sy6tiq kato {@link TemplateGenerator#generateDocsFromTemplate(NacidDataProvider, List, DocumentType)}, no vmesto id-ta, mu se podavat napravo ApplicationDetailsForReport, tyi kato ima slu4ai, v koito ve4e sym gi vzel v pametta tezi raboti
     * i nqma nujda da se pravi otnovo obrashtenie kym bazata za prepro4itaneto im...
     */
    public static InputStream generateDocsFromTemplate(NacidDataProvider nacidDataProvider, ApplicationDetailsForReport[] applicationDetailsForReport, DocumentType docType) {
        checkDocumentType(docType.getId());
    	return generateInputStream(nacidDataProvider, applicationDetailsForReport, docType.getDocumentTemplate(), ExportType.RTF, null); 
    }
    public static InputStream generateDocsFromTemplate(NacidDataProvider nacidDataProvider, RegprofApplicationDetailsForReport[] applicationDetailsForReport, DocumentType docType) {
        checkDocumentType(docType.getId());
        return generateInputStream(nacidDataProvider, applicationDetailsForReport, docType.getDocumentTemplate(), ExportType.RTF, null); 
    }
    /**
     * 
     * @param nacidDataProvider
     * @param applicationIds
     * @param docType
     * @param additionalParams - dopylnitelni parametri, koito se podavat na jasper-a (osven tezi, koito gi ima v common_vars)
     * @return doc file ot tip docType za zadadenite applicationIds, t.e. generira edin file, no za mnogo applications
     */
    public static InputStream generateDocsFromTemplate(NacidDataProvider nacidDataProvider, List<Integer> applicationIds, DocumentType docType, Map<String, Object> additionalParams) {
        checkDocumentType(docType.getId());
        ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
        List<ApplicationDetailsForReport> applications = new ArrayList<ApplicationDetailsForReport>();
        for (int i:applicationIds) {
        	ApplicationDetailsForReport r = applicationsDataProvider.getApplicationDetailsForReport(i);
        	if (r != null) {
        		applications.add(r);
        	}
        }
    	if (applications == null || applications.size() == 0) {
    		throw Utils.logException(new RuntimeException("No application data to generate..."));
    	}
        return generateInputStream(nacidDataProvider, applications.toArray(new ApplicationDetailsForReport[applications.size()]), docType.getDocumentTemplate(), ExportType.RTF, additionalParams);
    }
    private static void checkDocumentType(int docTypeId) {
        if (DocumentType.RUDI_CERTIFICATE_DOC_TYPES.contains(docTypeId) || DocumentType.RUDI_DOCTORATE_CERTFIFICATE_DOC_TYPES.contains(docTypeId)) {
            throw new RuntimeException("Certificates should be generated only from the generateCertificate method!!!!");
        }
    }
    
    public static InputStream generateRegprofDocsFromTemplate(NacidDataProvider nacidDataProvider, List<Integer> applicationIds, DocumentType docType, Map<String, Object> additionalParams) {
        RegprofApplicationDataProvider applicationsDataProvider = nacidDataProvider.getRegprofApplicationDataProvider();
        List<RegprofApplicationDetailsForReport> applications = new ArrayList<RegprofApplicationDetailsForReport>();
        for (int i:applicationIds) {
            RegprofApplicationDetailsForReport r = applicationsDataProvider.getRegprofApplicationDetailsForReport(i);
            if (r != null) {
                applications.add(r);
            }
        }
        if (applications == null || applications.size() == 0) {
            throw Utils.logException(new RuntimeException("No application data to generate..."));
        }
        return generateInputStream(nacidDataProvider, applications.toArray(new RegprofApplicationDetailsForReport[applications.size()]), docType.getDocumentTemplate(), ExportType.RTF, additionalParams);
    }
    
    
    public static InputStream generateApplicationsXlsReport(NacidDataProvider nacidDataProvider, List<ApplicationDetailsForReport> applications) {
        if (applications == null || applications.size() == 0) {
    		throw Utils.logException(new RuntimeException("No application data to generate..."));
    	}
        return generateInputStream(nacidDataProvider, applications.toArray(new ApplicationDetailsForReport[applications.size()]), JasperReportNames.JASPER_REPORT_REPORT_XLS, ExportType.XLS, null);
    }


    public static InputStream generateRegprofApplicationsXlsReport(NacidDataProvider nacidDataProvider, List<com.nacid.regprof.web.model.inquires.ScreenFormatWebModel> applications) {
        if (applications == null || applications.size() == 0) {
            throw Utils.logException(new RuntimeException("No application data to generate..."));
        }
        return generateInputStream(nacidDataProvider, applications.toArray(new com.nacid.regprof.web.model.inquires.ScreenFormatWebModel[applications.size()]), JasperReportNames.JASPER_REPORT_REGPROF_REPORT_XLS, ExportType.XLS, null);
    }
    
    
    
    public static InputStream generateApplicationsXlsForCommissionCalendarReport(NacidDataProvider nacidDataProvider, List<ApplicationDetailsForReport> applications) {
        if (applications == null || applications.size() == 0) {
    		throw Utils.logException(new RuntimeException("No application data to generate..."));
    	}
        return generateInputStream(nacidDataProvider, applications.toArray(new ApplicationDetailsForReport[applications.size()]), JasperReportNames.JASPER_REPORT_COMMISSION_CALENDAR_REPORT_XLS, ExportType.XLS, null);
    }
    
    public static InputStream generateInquiresScreensReport(NacidDataProvider nacidDataProvider, Map<String, Boolean> elements, List<ScreenFormatWebModel> webmodel) {
    	if (webmodel == null || webmodel.size() == 0) {
    		return generateInputStream(nacidDataProvider, new Object[0], JasperReportNames.JASPER_REPORT_EMPTY, ExportType.RTF, null);
    	}
    	Map params = new HashMap();
    	params.put("elements", elements);
    	return generateInputStream(nacidDataProvider, webmodel.toArray(), JasperReportNames.JASPER_REPORT_INQUIRES_SCREENS, ExportType.RTF, params);
    }

    public static InputStream generateRegprofInquiresScreensReport(NacidDataProvider nacidDataProvider, Map<String, Boolean> elements, List<com.nacid.regprof.web.model.inquires.ScreenFormatWebModel> webmodel) {
        if (webmodel == null || webmodel.size() == 0) {
            return generateInputStream(nacidDataProvider, new Object[0], JasperReportNames.JASPER_REPORT_EMPTY, ExportType.RTF, null);
        }
        Map params = new HashMap();
        params.put("elements", elements);
        return generateInputStream(nacidDataProvider, webmodel.toArray(), JasperReportNames.JASPER_REPORT_REGPROF_INQUIRES_SCREENS, ExportType.RTF, params);
    }
    
    private static InputStream generateInputStream(NacidDataProvider nacidDataProvider, Object[] object, String reportName, ExportType exportType, Map additionalParams) {
    	UtilsDataProvider utilsDataProvider = nacidDataProvider.getUtilsDataProvider();
    	
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
        JasperReportGenerator generator = new JasperReportGenerator(reportName,exportType,  os);
        generator.getParams().putAll(utilsDataProvider.getCommonVariablesAsMap());
        if (additionalParams != null) {
        	generator.getParams().putAll(additionalParams);
        }
        try {
			generator.export(Arrays.asList(object));
		} catch (JasperReportGeneratorException e) {
			throw Utils.logException(e);
		}
		return new ByteArrayInputStream(os.toByteArray());
    }
    


    private static void generateDocFromTemplate(int applicationId, int documentTypeId) throws IOException {
        NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
        DocumentType documentType = nacidDataProvider.getNomenclaturesDataProvider().getDocumentType(documentTypeId);
        InputStream is = generateDocFromTemplate(nacidDataProvider, applicationId, documentType);
        writeInputStreamToFile(is, "d:/test.rtf");
    }
    private static void writeInputStreamToFile(InputStream is, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        int c;
        while ( (c = is.read()) != -1) {
            fos.write(c);
        }
        fos.flush();
        fos.close();
        is.close();
    }
    static void generateAnnulment() {
        try {
            NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
            InputStream is = generateAnnulment(nacidDataProvider, 3632);
            FileOutputStream fos = new FileOutputStream("d:/test_obezsilvane1.rtf");
            int c;
            while ( (c = is.read()) != -1) {
                fos.write(c);
            }
            fos.flush();
            fos.close();
            is.close();
        } catch (FileNotFoundException e) {
            throw Utils.logException(e);
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }
    
    static void generateTest(int appId, String fileName, String templateName) {
        try {
            NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
            InputStream is = generateTest(nacidDataProvider, appId, templateName /*10*/);
            FileOutputStream fos = new FileOutputStream("d:/jasper-test/"+fileName+".rtf");
            int c;
            while ( (c = is.read()) != -1) {
                fos.write(c);
            }
            fos.flush();
            fos.close();
            is.close();
        } catch (FileNotFoundException e) {
            throw Utils.logException(e);
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }
    
    static void generateRegprofTest(int appId, String fileName, String templateName) {
        try {
            NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
            InputStream is = generateRegprofTest(nacidDataProvider, appId, templateName /*10*/);
            FileOutputStream fos = new FileOutputStream("d:/jasper-test/"+fileName+".rtf");
            int c;
            while ( (c = is.read()) != -1) {
                fos.write(c);
            }
            fos.flush();
            fos.close();
            is.close();
        } catch (FileNotFoundException e) {
            throw Utils.logException(e);
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }
    
    static void generateCommissionProtocol() {
        try {
            NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource(/*"jdbc:postgresql://172.16.0.7/NACID/", "postgres", "postgres"*/));
            //NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
            //DocumentType documentType = nomenclaturesDataProvider.getDocumentType(6);
            InputStream is = generateCommissionProtocol(nacidDataProvider, 35);
            FileOutputStream fos = new FileOutputStream("d:/test1.rtf");
            int c;
            while ( (c = is.read()) != -1) {
                fos.write(c);
            }
            fos.flush();
            fos.close();
            is.close();
        } catch (FileNotFoundException e) {
            throw Utils.logException(e);
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }
    private static void generateUdostoverenie() {
        try {
            NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource(/*"jdbc:postgresql://172.16.0.7/NACID/", "postgres", "postgres"*/));
            //NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
            //DocumentType documentType = nomenclaturesDataProvider.getDocumentType(6);
            InputStream is = generateCertificate(nacidDataProvider, DocumentType.DOC_TYPE_CERTIFICATE, new GenerateCertificateRequest(nacidDataProvider.getApplicationsDataProvider().getApplicationDetailsForReport(3964), "00-00-0000", UUID.randomUUID()));
            FileOutputStream fos = new FileOutputStream("d:/test.rtf");
            int c;
            while ( (c = is.read()) != -1) {
                fos.write(c);
            }
            fos.flush();
            fos.close();
            is.close();
        } catch (FileNotFoundException e) {
            throw Utils.logException(e);
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }
    private static void generateExpertPosition(int applicationId, int expertId, int documentTypeId) throws IOException {
        NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource(/*"jdbc:postgresql://172.16.0.20/nacid", "postgres", "postgres"*/));
        UtilsDataProvider utilsDataProvider = nacidDataProvider.getUtilsDataProvider();
        UniversityDetail d = utilsDataProvider.getUniversityDetail(2);
        Map<String, String> map = new HashMap<>();
        map.put("LETTER_RECIPIENT", d.getLetterRecipient());
        map.put("SALUTATION", d.getSalutation());


        //map.put("LETTER_RECIPIENT", "ДО\nПРОФ. Д-Р ХРИСТО БОНДЖОЛОВ\nРЕКТОР\nВЕЛИКОТЪРНОВСКИ УНИВЕРСИТЕТ\n\"СВ. СВ. КЛИРИЛ И МЕТОДИЙ\nВЕЛИКО ТЪРНОВО\"");
        //map.put("SALUTATION", "УВАЖАЕМИ ПРОФЕСОР БОНДЖОЛОВ,");
        System.out.println(d.getLetterRecipient() + "\n" + map.get("LETTER_RECIPIENT"));
        InputStream is = generateExpertDocument(nacidDataProvider, applicationId, expertId, documentTypeId, map);
        FileOutputStream fos = new FileOutputStream("d:/test1.rtf");
        int c;
        while ( (c = is.read()) != -1) {
            fos.write(c);
        }
        fos.flush();
        fos.close();
        is.close();
    }

    public static void main(String[] args) throws IOException {
//        generateRegprofTest(5315, "proect_CPO", "proect_udost_CPO");
//        generateRegprofTest(5317, "proect_sr_vishhe", "proect_udost_sredno&visshe");
//        generateRegprofTest(5316, "proect_SDK", "proect_udost_SDK");
//        generateRegprofTest(5316, "proect_staj", "proect_udost_staj");
//        generateExpertDocument(6433, 120);
//        generateExpertPosition(6316, 89, 180);
        generateAllExpertPositions(6316, 89);
    }

    private static void generateAllExpertPositions(int applicationId, int expertId) throws IOException {
        NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource(/*"jdbc:postgresql://172.16.0.20/nacid", "postgres", "postgres"*/));
        UtilsDataProvider utilsDataProvider = nacidDataProvider.getUtilsDataProvider();
        UniversityDetail d = utilsDataProvider.getUniversityDetail(2);
        Map<String, String> map = new HashMap<>();
        map.put("LETTER_RECIPIENT", d.getLetterRecipient());
        map.put("SALUTATION", d.getSalutation());

        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        List<DocumentType> docTypes = nomenclaturesDataProvider.getDocumentTypes(null, null, DocCategory.EXPERT_OPINIONS_STATUTE_AUTHENTICITY_RECOMMENDATION);
        for (DocumentType docType : docTypes) {
            System.out.println(d.getLetterRecipient() + "\n" + map.get("LETTER_RECIPIENT"));
            InputStream is = generateExpertDocument(nacidDataProvider, applicationId, expertId, docType.getId(), map);
            FileOutputStream fos = new FileOutputStream("d:/temp/nacid/" + docType.getDocumentTemplate() + ".rtf");
            int c;
            while ( (c = is.read()) != -1) {
                fos.write(c);
            }
            fos.flush();
            fos.close();
            is.close();
        }


    }

}