package com.nacid.utils.regprof.datamigration.attached_docs;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.regprof.*;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.bl.nomenclatures.regprof.EducationType;
import com.nacid.data.DataConverter;
import com.nacid.db.utils.StandAloneDataSource;

import javax.activation.MimetypesFileTypeMap;
import javax.sql.DataSource;
import java.io.*;
import java.util.*;

public class DocumentMigrationDataProviderImpl {

    //private static final String REPORT_FILE_NAME = "d:/reports/report_production.txt";
    //private static final String NOT_IMPORTED_FILES = "d:/reports/not_imported_production.txt";
    private static final String REPORT_FILE_NAME = "d:/reports/suggestions_report.txt";
    private static final String NOT_IMPORTED_FILES = "d:/reports/suggestions_not_imported.txt";
    private static final String BASE_DIRECTORY = "d:/nacid_migration";
    private static final String SUGGESTIONS_DIRECTORY = "d:/nacid_files/07-00-00";

    private DataSource nacidDataSource;

    public DocumentMigrationDataProviderImpl(DataSource nacidDataSource) {
        this.nacidDataSource = nacidDataSource;
    }

    public void insertAttachedDocuments() throws IOException {
        File baseDirectory = new File(BASE_DIRECTORY);
        File[] subDirs = baseDirectory.listFiles();
        MimetypesFileTypeMap mimeFileTypesMap = new MimetypesFileTypeMap();

        NacidDataProvider nacidDataProvider = new NacidDataProviderImpl(nacidDataSource);
        RegprofApplicationDataProvider dp = nacidDataProvider.getRegprofApplicationDataProvider();
        RegprofApplicationAttachmentDataProvider attDP = nacidDataProvider.getRegprofApplicationAttachmentDataProvider();
        RegprofTrainingCourseDataProvider tcDP = nacidDataProvider.getRegprofTrainingCourseDataProvider();
        int counter = 0;
        int total = 0;

        if (subDirs != null) {
            int i = 0;
            for (File directory : subDirs) {
                if (!directory.isDirectory()) {
                    log(REPORT_FILE_NAME, (directory.getName() + " is not directory... continuing...\n"));
                    continue;
                }

                String year = directory.getName();
                Calendar calendar = Calendar.getInstance();
                Map<String, Integer> appIdToNum = new HashMap<String, Integer>();
                List<? extends RegprofApplicationDetailsForList> applications = dp.getRegprofApplicationDetailsForList(false);
                if (applications != null) {
                    for (RegprofApplicationDetailsForList app : applications) {
                        if (app.getAppDate() != null) {
                            calendar.setTime(app.getAppDate());
                            if (calendar.get(Calendar.YEAR) == DataConverter.parseInt(year, 0)) {
                                appIdToNum.put(app.getAppNum(), app.getId());
                            }
                        }
                    }
                    File[] applicationDirs = directory.listFiles();
                    if (applicationDirs != null) {
                        for (File appDir : applicationDirs) {
                            if (!appDir.isDirectory()) {
                                log(REPORT_FILE_NAME, (appDir.getName() + " is not directory... continuing...\n"));
                                continue;
                            }

                            String appNum = appDir.getName();
                            if (directory.getName().equals("2011")) {
                                appNum = appNum.replace("_2011", "");
                            } else if (directory.getName().equals("2012")) {
                                appNum = appNum.replace("_2012", "");
                                appNum = appNum.replace("-", "-00-");
                            } else {
                                continue;
                            }

                            File[] attachedFiles = appDir.listFiles();

                            Integer appId = appIdToNum.get(appNum);
                            List<String> existingFileNames = new ArrayList<String>();
                            if (appId != null) {
                                List<RegprofApplicationAttachment> attachments = attDP.getAttachmentsForParent(appId);
                                if (attachments != null && !attachments.isEmpty()) {
                                    for (RegprofApplicationAttachment att : attachments) {
                                        existingFileNames.add(att.getFileName().trim());
                                    }
                                }
                            }

                            /** handling files */
                            if (attachedFiles != null) {
                                for (File file : attachedFiles) {
                                    if (!file.isFile()) {
                                        log(REPORT_FILE_NAME, (file.getName() + " is not file... continuing...\n"));
                                        continue;
                                    }
                                    if ("Thumbs.db".equals(file.getName())) {
                                        continue;
                                    }
                                    if (file.length() > 20971520) {
                                        log(NOT_IMPORTED_FILES, file.getPath() + " File too big to import...." + file.length());
                                        total++;
                                        continue;
                                    }
                                    if (existingFileNames.contains(file.getName().trim())) {
                                        log(NOT_IMPORTED_FILES, file.getPath() + " already exists...");
                                        total++;
                                        continue;
                                    }

                                    if (appId != null) {
                                        RegprofTrainingCourse trainingCourse = tcDP.getRegprofTrainingCourse(appId);
                                        Integer educationTypeId = trainingCourse.getDetails().getEducationTypeId();
                                        int docTypeId = getDocumentType(file.getName(), educationTypeId);
                                        InputStream is = new FileInputStream(file);
                                        /*int newId = attDP.saveAttachment(0, appIdToNum.get(appNum), null, docTypeId, null, null, null, 
                                                mimeFileTypesMap.getContentType(file), file.getName(), is, is.available(), null, null, null, 0);
                                        if (newId > 0) {
                                            log(REPORT_FILE_NAME, "File " + file.getAbsolutePath() + " has been imported; parent_id = " + appId);
                                            counter++;
                                            if (docTypeId == DocumentType.DOC_TYPE_REGPROF_MIGRATED) {
                                                log(REPORT_FILE_NAME, "File " + file.getAbsolutePath() + " is of unknown document type!");
                                            }
                                        } else {
                                            log(NOT_IMPORTED_FILES, "File " + file.getAbsolutePath() + " has NOT been imported!"); 
                                        }*/
                                    } else {
                                        log(NOT_IMPORTED_FILES, "File " + file.getAbsolutePath() + " has NOT been imported due to missing application id!");
                                    }
                                    total++;
                                }
                            }
                            /** end of handling files */
                        }
                    }
                }
            }
        }

        log(REPORT_FILE_NAME, counter + " out of " + total + " files have been successfully imported");
    }

    public void insertCertificateSuggestions2013() throws IOException {
        File baseDirectory = new File(SUGGESTIONS_DIRECTORY);
        File[] applicationDirs = baseDirectory.listFiles();
        MimetypesFileTypeMap mimeFileTypesMap = new MimetypesFileTypeMap();

        NacidDataProvider nacidDataProvider = new NacidDataProviderImpl(nacidDataSource);
        RegprofApplicationDataProvider dp = nacidDataProvider.getRegprofApplicationDataProvider();
        RegprofApplicationAttachmentDataProvider attDP = nacidDataProvider.getRegprofApplicationAttachmentDataProvider();
        RegprofTrainingCourseDataProvider tcDP = nacidDataProvider.getRegprofTrainingCourseDataProvider();
        int counter = 0;
        int total = 0;

        if (applicationDirs != null) {

            Calendar calendar = Calendar.getInstance();
            Map<String, Integer> appNumToId = new HashMap<String, Integer>();
            List<? extends RegprofApplicationDetailsForList> applications = dp.getRegprofApplicationDetailsForList(false);
            if (applications != null) {
                for (RegprofApplicationDetailsForList app : applications) {
                    if (app.getAppDate() != null) {
                        calendar.setTime(app.getAppDate());
                        if (calendar.get(Calendar.YEAR) == 2013) {
                            appNumToId.put(app.getAppNum(), app.getId());
                        }
                    }
                }
            }

            for (File appDir : applicationDirs) {
                if (!appDir.isDirectory()) {
                    log(REPORT_FILE_NAME, (appDir.getName() + " is not directory... continuing...\n"));
                    continue;
                }

                String appNum = appDir.getName();

                Integer appId = appNumToId.get(appNum);
                if (appId == null) {
                    log(REPORT_FILE_NAME, "id to num " + appNum + " is null");
                    continue;
                }

                File[] attachedFiles = appDir.listFiles();

                Integer fileId = 0;
                for (File file : attachedFiles) {
                    if (!file.isFile()) {
                        log(REPORT_FILE_NAME, (file.getName() + " is not file... continuing...\n"));
                        continue;
                    }
                    if ("Thumbs.db".equals(file.getName())) {
                        continue;
                    }
                    total++;
                    if (file.length() > 20971520) {
                        log(NOT_IMPORTED_FILES, file.getPath() + " File too big to import...." + file.length());
                        total++;
                        continue;
                    }

                    String fileName = null;
                    String scannedName = null;

                    InputStream fileInputStream = null;
                    InputStream scannedInputStream = null;

                    String contentType = null; 
                    String scannedContentType = null;

                    if (fileId != null && fileId != 0) {
                        RegprofApplicationAttachment att = attDP.getAttachment(fileId, true, true);
                        if (att != null) {
                            if (att.getContentStream() != null) {
                                fileInputStream = att.getContentStream();
                                fileName = att.getFileName();
                                contentType = att.getContentType();
                            } else if (att.getScannedContentStream() != null) {
                                scannedInputStream = att.getScannedContentStream();
                                scannedName = att.getScannedFileName();
                                scannedContentType = att.getScannedContentType();
                            }
                        }
                    }
                    if (file.getName().contains("doc")) {
                        fileInputStream = new FileInputStream(file);
                        contentType = mimeFileTypesMap.getContentType(file);
                        fileName = file.getName();
                    } else if (file.getName().contains("pdf")) {
                        scannedInputStream = new FileInputStream(file);
                        scannedContentType = mimeFileTypesMap.getContentType(file);
                        scannedName = file.getName();
                    } else {
                        log(NOT_IMPORTED_FILES, file.getPath() + " File is neither *.doc nor *.pdf!");
                        continue;
                    }
                    
                    if (fileInputStream == null && scannedInputStream != null) {
                        fileInputStream = scannedInputStream;
                        scannedInputStream = null;
                        contentType = scannedContentType;
                        scannedContentType = null;
                        fileName = scannedName;
                        scannedName = null;
                    }

                    RegprofTrainingCourse trainingCourse = tcDP.getRegprofTrainingCourse(appId);
                    Integer educationTypeId = trainingCourse.getDetails().getEducationTypeId();
                    int docTypeId = getSuggestionByEducationType(educationTypeId);
                    log(REPORT_FILE_NAME, "Doc type id = " + docTypeId);
                    fileId = attDP.saveAttachment(fileId, appId, null, docTypeId, null, null, null, contentType, fileName, fileInputStream, fileInputStream != null ? fileInputStream.available() : 0, scannedContentType, scannedName, scannedInputStream, 
                                    scannedInputStream != null ? scannedInputStream.available() : 0, 1);
                    log(REPORT_FILE_NAME, "file id = " + fileId);
                    if (fileId != null && fileId > 0) {
                        counter++;
                    }
                    counter++;
                    log(REPORT_FILE_NAME, "application: " + appNum);
                }
            }
        }
        log(REPORT_FILE_NAME, "Total: " + counter + "/" + total);
    }

    private static int getDocumentType(String fileName, Integer educationTypeId) {
        fileName = fileName.toLowerCase();
        if (fileName.indexOf(".") > 0) {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }
        if (fileName.contains("drug") || fileName.contains("jalba") || fileName.contains("rajdane") || fileName.contains("ragdane") || fileName.contains("rajde") ||
                fileName.contains("brak") || fileName.contains("sl")) {
            return DocumentType.DOC_TYPE_REGPROF_OTHERS;
        } else if (fileName.contains("db") || fileName.contains("dp") || fileName.contains("-d-") || fileName.contains("ds") || fileName.contains("dk") || fileName.contains("sv") ||
                fileName.contains("ud") || fileName.contains("dvo") || fileName.contains("prof") || fileName.contains("sertifikat") || fileName.contains("kval")) {
            return DocumentType.DOC_TYPE_REGPROF_DIPLOMA;
        } else if (fileName.contains("d.s") || fileName.contains("lk") || fileName.contains("ik")) {
            return DocumentType.DOC_TYPE_REGPROF_PERSONAL_DOCUMENT;
        } else if (fileName.contains("dekl") || fileName.endsWith("d")) {
            return DocumentType.DOC_TYPE_REGPROF_APPLICANT_DECLARATION_3;
        } else if (fileName.contains("kvitancia") || fileName.contains("taksa") || fileName.contains("vn. belejka") || fileName.contains("vn.belejka")) {
            return DocumentType.DOC_TYPE_REGPROF_PAYMENT_DOCUMENT;
        } else if (fileName.contains("licenz") || fileName.contains("pr")) {
            return DocumentType.DOC_TYPE_REGPROF_OTHER_CERTIFICATES;
        } else if (fileName.contains("ime")) {
            return DocumentType.DOC_TYPE_REGPROF_IDENTITY_DOCUMENT;
        } else if (fileName.contains("tr") || fileName.contains("drud")) {
            return DocumentType.DOC_TYPE_REGPROF_TRUDOVA_KNIJKA;
        } else if (fileName.contains("up") || fileName.contains("u-3")) {
            return DocumentType.DOC_TYPE_REGPROF_UP_3;
        } else if (fileName.contains("osig")) {
            return DocumentType.DOC_TYPE_REGPROF_OSIGURITELNA_KNIJKA;
        } else if (fileName.contains("chl")) {
            return DocumentType.DOC_TYPE_REGPROF_PROFESSIONAL_ORGANIZATION_MEMBERSHIP;
        } else if (fileName.contains("sad")) {
            return DocumentType.DOC_TYPE_REGPROF_COURT_DECISION;
        } else if (fileName.contains("zap")) {
            return DocumentType.DOC_TYPE_REGPROF_ZAPITVANE_AUTH_EDUCATION_DOC_EXAM;
        } else if (fileName.contains("otg")) {
            return DocumentType.DOC_TYPE_REGPROF_OTGOVOR_ZAPITVANE_DOC_EXAM;
        } else if (fileName.contains("predl")) {
            return getSuggestionByEducationType(educationTypeId);
        } else if (fileName.contains("as")) {
            return DocumentType.DOC_TYPE_REGPROF_OTHER_CERTIFICATES;
        } else {

            fileName = fileName.replaceAll("\\d", "");
            fileName = fileName.replaceAll("\\W", "");
            fileName = fileName.replace("_", "");
            fileName = fileName.trim();
            if (fileName.isEmpty()) {
                return DocumentType.DOC_TYPE_REGPROF_DIPLOMA;
            } else if (fileName.contains("u")) {
                return getCertificateByEducationType(educationTypeId);
            } else if (fileName.contains("d") || fileName.contains("s")) {
                return DocumentType.DOC_TYPE_REGPROF_DIPLOMA;
            } else if (fileName.contains("p")) {
                return DocumentType.DOC_TYPE_REGPROF_PYLNOMOSHTNO;
            } else if (fileName.contains("t")) {
                return DocumentType.DOC_TYPE_REGPROF_PAYMENT_DOCUMENT;
            } else if (fileName.contains("z")) {
                return DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_APPLY_PROFESSIONAL_QUALIFICATION;
            }
        }
        return DocumentType.DOC_TYPE_REGPROF_MIGRATED;
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

    private static int getSuggestionByEducationType(Integer educationTypeId) {
        if (educationTypeId == null) {
            return DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_STAJ;
        } else if (educationTypeId == EducationType.EDU_TYPE_HIGH || educationTypeId == EducationType.EDU_TYPE_SECONDARY_PROFESSIONAL) {
            return DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_HIGHER_AND_SECONDARY;
        } else if (educationTypeId == EducationType.EDU_TYPE_SDK) {
            return DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_SDK;
        } else if (educationTypeId == EducationType.EDU_TYPE_SECONDARY) {
            return DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_CPO;
        } else {
            return DocumentType.DOC_TYPE_REGPROF_MIGRATED;
        }
    }

    private static int getCertificateByEducationType(Integer educationTypeId) {
        if (educationTypeId == null) {
            return DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_STAJ;
        } else if (educationTypeId == EducationType.EDU_TYPE_HIGH || educationTypeId == EducationType.EDU_TYPE_SECONDARY_PROFESSIONAL) {
            return DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_HIGHER_AND_SECONDARY;
        } else if (educationTypeId == EducationType.EDU_TYPE_SDK) {
            return DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_SDK;
        } else if (educationTypeId == EducationType.EDU_TYPE_SECONDARY) {
            return DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_CPO;
        } else {
            return DocumentType.DOC_TYPE_REGPROF_MIGRATED;
        }
    }

    public static void main (String[] args) throws Exception {
        DocumentMigrationDataProviderImpl dp = new DocumentMigrationDataProviderImpl(new StandAloneDataSource("jdbc:postgresql://localhost:9000/NACID/", "postgres", "postgres"));
        //dp.insertAttachedDocuments();
        dp.insertCertificateSuggestions2013();
    }
}
