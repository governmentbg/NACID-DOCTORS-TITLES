package com.nacid.bl.regprof;

import com.nacid.bl.impl.applications.regprof.RegProfApplicationForInquireImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RegprofInquireDataProvider {
    public List<RegProfApplicationForInquireImpl> getRegprofApplicationsForCommonInqure(boolean esubmitted, Boolean esigned, Date applicationDateFrom, Date applicationDateTo, List<Integer> representativeCompanyIds,
            Map<Integer, List<Integer>> professionalInstitutions, List<Integer> candidateCountryIds, Date diplomaDateFrom, Date diplomaDateTo, Integer educationTypeId, List<Integer> secProfQualificationIds, List<Integer> highProfQualificationIds, 
            List<Integer> secSpecialityIds, List<Integer> higherSpecialityIds,List<Integer> sdkSpecialityIds, List<Integer> recognizedHigherEduLevelIds, List<Integer> recognizedSecondaryQualificationDegrees,
            List<Integer> recognizedProfessions, List<Integer> experienceProfessionIds, Map<Integer, List<Integer>> directiveArticleIds, List<RegprofApplicationStatusFromCommonInquire> actualStatuses,
            Integer serviceType, Date serviceTypeDateTo, List<String> imiCorrespondences, List<Integer> attachmentDocumentTypeIds, Boolean apostilleApplication,
            List<Integer> educationDocumentTypes, List<Integer> experienceDocumentTypes);
    /**
     * 
     * @param fname - firstName starts with.... pyrvoto ime zapo4va s....
     * @param sname - secondName starts with...
     * @param lname - lastName starts with...
     * @param personalId - chast ot personalId
     * @param applicationNum - celiq applicationNum
     * @param dateFrom - nachanla data na syzdavane na zaqvlenie
     * @param dateTo - krajna data na syzdavane na zaqvlenie
     * @return Applications za spravkata "spravki za zaqvitel"
     */
    public List<RegProfApplicationForInquireImpl> getRegprofApplicationsForApplicantInquire(String fname, String sname, String lname, String personalId, String diplFName, String diplSName, String diplLName, String reprFName, String reprSName, String reprLName, String reprPersonalId, Integer reprCompany, String applicationNum, Date dateFrom, Date dateTo);
}
