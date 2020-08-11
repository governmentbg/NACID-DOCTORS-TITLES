package com.nacid.bl.external.applications;

import com.nacid.bl.external.ExtApplicationKind;
import com.nacid.bl.external.ExtDocumentRecipient;
import com.nacid.data.external.applications.ExtApplicationCommentRecord;

import java.util.Date;
import java.util.List;

public interface ExtApplicationsDataProvider {
  public ExtApplication getApplication(int applicationId);
  public List<ExtApplicationKind> getApplicationKindsPerApplication(int applicationId);
  public ExtApplication getApplicationByInternalApplicationId(int internalApplicationId);

    /**
     *
     * @param applicantId
     * @param applicationTypeId - edin ot tipovete, definirani v {@link com.nacid.bl.nomenclatures.ApplicationType}. Pri null - vry6ta vsichki applicationTypes
     * @return
     */
  public List<ExtApplication> getApplicationsByRepresentative(int applicantId, Integer applicationTypeId);
  public List<ExtApplication> getApplicationsByStatus(List<Integer> statuses);

  public List getEAppliedApplications(List<Integer> entryNumSeries);
  
  public int saveApplication(
		  int id, Integer applicantId, Integer applicantCompanyId,boolean differentDiplomaNames,
			int trainingCourseId, int homeCountryId, String homeCity, String homePostCode, String homeAddressDetails, String homePhone, boolean homeIsBg,
			String bgCity, String bgPostCode, String bgAddressDetails, String bgPhone, Date timeOfCreation, String summary, int applicationStatus, Integer internalApplicationId,
			Boolean personalDataUsage, Boolean dataAuthentic, int applicantType, Boolean deputy, Integer representativeId, String representativeType, Integer contactDetailsId, Short typePayment, Integer deliveryType, Boolean declaration, String courierNameAddress, String outgoingNumber, String internalNumber, Boolean isExpress,
            String entryNum, int applicationType, Integer documentReceiveMethodId, Integer applicantPersonalIdDocumentType);
  /**
   * 
   * @param extApplicationId
   * @param loggedUserId
   * @param applicantId
   * @return generiranoto applicationId vyv vytre6nata baza
   */
  public int transferApplicationToIntDB(int extApplicationId, int loggedUserId, Integer applicantId, Integer applicantCompanyId, Integer representativeId, int ownerId, List<Integer> universityIds, List<Integer> specialityIds, String diplomaSeries, String diplomaNumber, String diplomaRegistrationNumber, Date diplomaDate);

  public void markApplicationFinished(int extApplicationId);
  /**
   * @param applicationId
   * @return xml-a na application-a, koito shte trqbva da se podpisva elektronno
   */
  public String getApplicationXml(int applicationId);
  
  /**
   * zapisva xml-a za daden applicationId
   * @param applicationId
   * @throws SignedXmlException - ako ima razminavane mejdu podpisaniq xml i dannite v bazata. Ako se hvyrli exception v message-a mu e text-a kojto trqbva da se izvede kato greshka na potrebitelq
   */

  public void saveSignedApplicationXml(int userId, int applicationId,  String signedXmlContent) throws SignedXmlException;
  /**
   * 
   * @param applicationId
   * @return dali zaqvlenieto s applicationId e elektronno podpisano
   */
  public boolean isESigned(int applicationId);


    /***
     * vry6ta poluchatelq na udostoverenieto
     * @param applicationId
     * @return
     */
    public ExtDocumentRecipient getDocumentRecipient(int applicationId);

    void saveDocumentRecipient(int applicationId, String name, int countryId, String city, String district, String postCode, String address, String mobilePhone);

    void deleteDocumentRecipient(int applicationId);

    public ExtApplicationCommentExtended getApplicationComment(int id);

    List<ExtApplicationCommentExtended> getApplicationComments(int applicationId);

    void saveApplicationComment(int applicationId, String comment, boolean sendEmail, Integer emailId, boolean systemMessage, int userCreated);
  
}
