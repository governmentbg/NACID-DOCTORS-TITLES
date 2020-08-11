package com.nacid.data.regprof.external.applications.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.nacid.bl.impl.applications.regprof.RegprofApplicationDetailsBaseImpl;
import com.nacid.data.external.applications.ExtApplicationAttachmentRecord;
import com.nacid.data.external.applications.ExtPersonDocumentRecord;
import com.nacid.data.external.applications.ExtPersonRecord;
import com.nacid.data.regprof.external.ExtRegprofProfessionExperienceRecord;
import com.nacid.data.regprof.external.ExtRegprofTrainingCourseRecord;
import com.nacid.data.regprof.external.ExtRegprofTrainingCourseSpecialitiesRecord;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "extRegprofApplicationRecord",
    "extRegprofTrainingCourseRecord",
    "extPersonDocumentRecord",
    "extRegprofProfessionExperienceRecord",
    "trainingCourseSpecialities",
    "person",
    "attachments"
})
@XmlRootElement(name = "ext_repgorf_applications_xml")
public class ExtRegprofApplicationsXml {
    @XmlElement(name = "ext_regprof_application_record", required = true)
    private RegprofApplicationDetailsBaseImpl extRegprofApplicationRecord;
    
    @XmlElement(name = "ext_regprof_training_course_record", required = true)
    private ExtRegprofTrainingCourseRecord extRegprofTrainingCourseRecord;
    
    @XmlElement(name = "ext_person_document_record", required = true)
    private ExtPersonDocumentRecord extPersonDocumentRecord;
    
    @XmlElement(name = "ext_regprof_profession_experience_record", required = true)
    private ExtRegprofProfessionExperienceRecord extRegprofProfessionExperienceRecord;
    
    @XmlElement(name = "ext_regprof_training_course_specialities_record", required = true)
    private List<ExtRegprofTrainingCourseSpecialitiesRecord> trainingCourseSpecialities;
    
    @XmlElement(name = "ext_person_record", required = true)
    private ExtPersonRecord person;
    
    @XmlElement(name = "AttachedDocument", namespace="http://ereg.egov.bg/segment/0009-000139")
    private List<ExtApplicationAttachmentRecord> attachments;
    public RegprofApplicationDetailsBaseImpl getExtRegprofApplicationRecord() {
        return extRegprofApplicationRecord;
    }
    public void setExtRegprofApplicationRecord(
            RegprofApplicationDetailsBaseImpl extRegprofApplicationRecord) {
        this.extRegprofApplicationRecord = extRegprofApplicationRecord;
    }
    public ExtRegprofTrainingCourseRecord getExtRegprofTrainingCourseRecord() {
        return extRegprofTrainingCourseRecord;
    }
    public void setExtRegprofTrainingCourseRecord(
            ExtRegprofTrainingCourseRecord extRegprofTrainingCourseRecord) {
        this.extRegprofTrainingCourseRecord = extRegprofTrainingCourseRecord;
    }
    public ExtPersonDocumentRecord getExtPersonDocumentRecord() {
        return extPersonDocumentRecord;
    }
    public void setExtPersonDocumentRecord(
            ExtPersonDocumentRecord extPersonDocumentRecord) {
        this.extPersonDocumentRecord = extPersonDocumentRecord;
    }
    public ExtRegprofProfessionExperienceRecord getExtRegprofProfessionExperienceRecord() {
        return extRegprofProfessionExperienceRecord;
    }
    public void setExtRegprofProfessionExperienceRecord(
            ExtRegprofProfessionExperienceRecord extRegprofProfessionExperienceRecord) {
        this.extRegprofProfessionExperienceRecord = extRegprofProfessionExperienceRecord;
    }
    public List<ExtRegprofTrainingCourseSpecialitiesRecord> getTrainingCourseSpecialities() {
        return trainingCourseSpecialities;
    }
    public void setTrainingCourseSpecialities(
            List<ExtRegprofTrainingCourseSpecialitiesRecord> trainingCourseSpecialities) {
        this.trainingCourseSpecialities = trainingCourseSpecialities;
    }
    public ExtPersonRecord getPerson() {
        return person;
    }
    public void setPerson(ExtPersonRecord person) {
        this.person = person;
    }
    public List<ExtApplicationAttachmentRecord> getAttachments() {
        return attachments;
    }
    public void setAttachments(List<ExtApplicationAttachmentRecord> attachments) {
        this.attachments = attachments;
    }
    
    
}
