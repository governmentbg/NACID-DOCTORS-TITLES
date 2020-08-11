package com.nacid.bl.impl.applications.regprof;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.nacid.bl.RequestParameterInterface;
import com.nacid.bl.applications.regprof.DocumentExamination;
import com.nacid.data.DataConverter;
import com.nacid.data.annotations.Table;
//RayaWritten--------------------------------------------------------------
@Table(name="regprof.document_examination")
public class DocumentExaminationImpl implements DocumentExamination, RequestParameterInterface {
    private Integer id;
    private Integer trainingCourseId;
    @DateTimeFormat(pattern="dd.MM.yyyy")
    private Date documentExaminationDate;
    private Integer source;
    private Integer userCreated;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getTrainingCourseId() {
        return trainingCourseId;
    }
    public void setTrainingCourseId(Integer trainingCourseId) {
        this.trainingCourseId = trainingCourseId;
    }   
    
    public Integer getSource() {
        return source;
    }
    public void setSource(Integer source) {
        this.source = source;
    }
    
    public Date getDocumentExaminationDate() {
        return documentExaminationDate;
    }
    public void setDocumentExaminationDate(Date documentExaminationDate) {
        this.documentExaminationDate = documentExaminationDate;
    }
    public Integer getUserCreated() {
        return userCreated;
    }
    public void setUserCreated(Integer userCreated) {
        this.userCreated = userCreated;
    }    
    public String getDocumentExaminationDateFormatted() {
        return DataConverter.formatDate(documentExaminationDate);
    }
}
//----------------------------------------------------------------------------
