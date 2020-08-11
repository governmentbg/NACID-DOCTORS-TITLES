package com.nacid.bl.impl.regprof;

import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.regprof.RegProfApplicationForInquireImpl;
import com.nacid.bl.regprof.RegprofApplicationStatusFromCommonInquire;
import com.nacid.bl.regprof.RegprofInquireDataProvider;
import com.nacid.db.regprof.InquiresDB;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RegprofInquireDataProviderImpl implements RegprofInquireDataProvider {
    private NacidDataProviderImpl nacidDataProvider;
    private InquiresDB db;
    public RegprofInquireDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
        this.db = new InquiresDB(nacidDataProvider.getDataSource());
        this.nacidDataProvider = nacidDataProvider;
    }
    @Override
    public List<RegProfApplicationForInquireImpl> getRegprofApplicationsForCommonInqure(
            boolean esubmitted, Boolean eSigned,
            Date applicationDateFrom, Date applicationDateTo,
            List<Integer> representativeCompanyIds,
            Map<Integer, List<Integer>> professionalInstitutions,
            List<Integer> candidateCountryIds, Date diplomaDateFrom,
            Date diplomaDateTo, Integer educationTypeId,
            List<Integer> secProfQualificationIds,
            List<Integer> highProfQualificationIds,
            List<Integer> secSpecialityIds, List<Integer> higherSpecialityIds,
            List<Integer> sdkSpecialityIds, 
            List<Integer> recognizedHigherEduLevelIds, 
            List<Integer> recognizedSecondaryQualificationDegrees,
            List<Integer> recognizedProfessions,
            List<Integer> experienceProfessionIds,
            Map<Integer, List<Integer>> directiveArticleIds,
            List<RegprofApplicationStatusFromCommonInquire> actualStatuses,
            Integer serviceType, Date serviceTypeDateTo, List<String> imiCorrespondences, List<Integer> attachmentDocumentTypeIds, Boolean apostilleApplication, List<Integer> educationDocumentTypes,
            List<Integer> experienceDocumentTypes) {
        try {
            return db.getRegprofApplicationsForCommonInqure(esubmitted, eSigned, Utils.getSqlDate(applicationDateFrom), Utils.getSqlDate(applicationDateTo), representativeCompanyIds, professionalInstitutions, candidateCountryIds, Utils.getSqlDate(diplomaDateFrom), Utils.getSqlDate(diplomaDateTo), educationTypeId, secProfQualificationIds, highProfQualificationIds, secSpecialityIds, higherSpecialityIds, sdkSpecialityIds, recognizedHigherEduLevelIds, recognizedSecondaryQualificationDegrees, recognizedProfessions, experienceProfessionIds, directiveArticleIds, actualStatuses, serviceType, Utils.getSqlDate(serviceTypeDateTo), imiCorrespondences, attachmentDocumentTypeIds, apostilleApplication, educationDocumentTypes,
                    experienceDocumentTypes);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    @Override
    public List<RegProfApplicationForInquireImpl> getRegprofApplicationsForApplicantInquire(
            String fname, String sname, String lname, String personalId,
            String diplFName, String diplSName, String diplLName,
            String reprFName, String reprSName, String reprLName,
            String reprPersonalId, Integer reprCompany, String applicationNum,
            Date dateFrom, Date dateTo) {
        try {
            return db.getRegprofApplicationRecordsForApplicantInquire(fname, sname, lname, personalId, diplFName, diplSName, diplLName, reprFName, reprSName, reprLName, reprPersonalId, reprCompany, applicationNum, Utils.getSqlDate(dateFrom), Utils.getSqlDate(dateTo));
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
}
