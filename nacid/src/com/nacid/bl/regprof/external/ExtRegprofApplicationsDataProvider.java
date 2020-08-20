package com.nacid.bl.regprof.external;

import java.util.Date;
import java.util.List;

import com.nacid.bl.applications.regprof.RegprofApplication;
import com.nacid.bl.docflow.DocFlowException;
import com.nacid.bl.exceptions.NotAuthorizedException;
import com.nacid.bl.external.applications.SignedXmlException;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationDetailsImpl;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationImpl;
import com.nacid.bl.signature.SuccessSign;

public interface ExtRegprofApplicationsDataProvider {
     
    public ExtRegprofApplicationImpl createEmtpyRegprofApplication(int extPersonId);
    
    public ExtRegprofApplicationImpl getExtRegprofApplication(int applicationId);
    
    public ExtRegprofApplicationImpl getExtRegprofApplicationByInternalApplicationId(int internalApplicationId);
    /**
     * vry6ta xml-a, na zaqvlenieto, koito shte se podpisva!
     * @param applicationId
     * @return
     */
    public String getExtRegprofApplicationXml(int applicationId);
    /**
     * vry6ta informaciq za podpisaniq xml!
     * @param applicationId
     * @return
     */
    public ExtRegprofESignedInformation getEsignedInformation(int applicationId);
    
    public ExtRegprofApplicationDetailsImpl getApplicationDetails(int applicationId);
    public List<ExtRegprofApplicationForList> getExtRegprofApplicationByExtPerson(int extPerson);
    public void saveExtRegprofApplication(ExtRegprofApplicationImpl application);
    
    public ExtRegprofTrainingCourse getTrainingCourse(int applicationId);
    public void saveExtRegprofTrainingCourse(ExtRegprofTrainingCourse tc);
    
    /**
     * proverqva dali dadeniq potrebitel ima pravo da editva zaqvlenie s ID=applicationId
     * @param applicationId
     * @param userId
     * @param operationType
     * @throws NotAuthorizedException
     */
    public void checkApplicationAccess(int applicationId, int userId, int operationType) throws NotAuthorizedException;
    
    public void saveSignedApplicationXml(int userId, int applicationId,  SuccessSign signedXmlContent) throws SignedXmlException;
    
    public void submitExtRegprofApplication(int applicationId, Date dateSubmitted, Integer status, Integer serviceType, Integer paymentType);
    
    public List<ExtRegprofApplicationForList> getExtRegprofApplicationsByStatuses(List<Integer> statuses);
    
    public RegprofApplication transferApplicationToIntDb(int extApplicationId, int userId, int applicantId, Integer representativeId, Integer personDocumentId) throws DocFlowException;

    public void markApplicationFinished(int extApplicationId);


    public ExtRegprofApplicationCommentExtended getApplicationComment(int id);

    List<ExtRegprofApplicationCommentExtended> getApplicationComments(int applicationId);

    void saveApplicationComment(int applicationId, String comment, boolean sendEmail, Integer emailId, boolean systemMessage, int userCreated);



}
