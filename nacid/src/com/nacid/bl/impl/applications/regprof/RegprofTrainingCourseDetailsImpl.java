package com.nacid.bl.impl.applications.regprof;

import com.nacid.bl.RequestParameterInterface;
import com.nacid.data.annotations.Table;

@Table(name="regprof.training_course")
public class RegprofTrainingCourseDetailsImpl extends RegprofTrainingCourseDetailsBaseImpl implements RequestParameterInterface {
    
    public RegprofTrainingCourseDetailsImpl() { }
    
    public RegprofTrainingCourseDetailsImpl(Integer id, String documentFname,
            String documentLname, String documentSname, String documentCivilId,
            Integer documentCivilIdType, Integer profInstitutionId,
            String documentNumber, String documentDate,
            Integer secProfQualificationId,
            Integer highProfQualificationId, Integer highEduLevelId,
            Integer sdkProfInstitutionId, Integer sdkProfQualificationId,
            String sdkDocumentNumber, String sdkDocumentDate,
            Integer educationTypeId, int hasExperience,
            int hasEducation, Integer documentType, Integer sdkDocumentType,
            Integer profInstitutionOrgNameId,
            Integer sdkProfInstitutionOrgNameId, Integer secCaliberId,
            String documentSeries, String documentRegNumber,
            String sdkDocumentSeries, String sdkDocumentRegNumber,
            Integer certificateProfQualificationId, int notRestricted,
            int regulatedEducationTraining) {
        
        super(id, documentFname, documentLname, documentSname, documentCivilId, documentCivilIdType, profInstitutionId, documentNumber, documentDate, secProfQualificationId, 
                highProfQualificationId, highEduLevelId, sdkProfInstitutionId, sdkProfQualificationId, sdkDocumentNumber, sdkDocumentDate, educationTypeId, hasExperience,
                hasEducation, documentType, sdkDocumentType, profInstitutionOrgNameId, sdkProfInstitutionOrgNameId, secCaliberId, documentSeries, documentRegNumber, sdkDocumentSeries, 
                sdkDocumentRegNumber, certificateProfQualificationId, notRestricted, regulatedEducationTraining);
    }
    
}