package com.nacid.bl;

import com.nacid.bl.academicrecognition.AcademicRecognitionDataProvider;
import com.nacid.bl.academicrecognition.BGAcademicRecognitionLogDataProvider;
import com.nacid.bl.apostille.ApostilleApplicationsDataProvider;
import com.nacid.bl.applications.*;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachmentDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplicationDataProvider;
import com.nacid.bl.comision.ComissionMemberDataProvider;
import com.nacid.bl.comision.CommissionCalendarDataProvider;
import com.nacid.bl.events.EventDataProvider;
import com.nacid.bl.events.EventTypeDataProvider;
import com.nacid.bl.external.ExtPersonDataProvider;
import com.nacid.bl.external.applications.ExtApplicationAttachmentDataProvider;
import com.nacid.bl.external.applications.ExtApplicationsDataProvider;
import com.nacid.bl.external.applications.ExtCompanyDataProvider;
import com.nacid.bl.external.applications.ExtTrainingCourseDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.academicrecognition.BGAcademicRecognitionLogDataProviderImpl;
import com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseDataProviderImpl;
import com.nacid.bl.impl.esoed.EsoedDataProviderImpl;
import com.nacid.bl.impl.regprof.RegprofInquireDataProviderImpl;
import com.nacid.bl.inquires.InquiresDataProvider;
import com.nacid.bl.mail.MailDataProvider;
import com.nacid.bl.menu.MenuDataProvider;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.bl.payments.PaymentsDataProvider;
import com.nacid.bl.ras.RasApplicationsDataProvider;
import com.nacid.bl.regprof.ProfessionalInstitutionDataProvider;
import com.nacid.bl.regprof.external.ExtRegprofApplicationAttachmentDataProvider;
import com.nacid.bl.regprof.external.ExtRegprofApplicationsDataProvider;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.bl.utils.UtilsDataProvider;

import javax.sql.DataSource;

import java.util.HashSet;
import java.util.Set;


public abstract class NacidDataProvider {
    
  /*************** WEB APPLICATION IDs ***************/
  public static final int APP_NACID_ID = 1; 
  public static final int APP_NACID_EXT_ID = 2;
  public static final int APP_NACID_REGPROF_ID = 3;
  public static final int APP_NACID_REGPROF_EXT_ID = 4;
    public static final Set<Integer> BACKOFFICE_APP_IDS = new HashSet<Integer>() {
        {
            add(APP_NACID_ID);
            add(APP_NACID_REGPROF_ID);
        }
    };
  /***************************************************/

  private static NacidDataProvider providerImpl;

  public static synchronized NacidDataProvider getNacidDataProvider(DataSource ds) {
    if (providerImpl == null) {
      providerImpl = new NacidDataProviderImpl(ds);
    }
    return providerImpl;
  }

  public abstract UsersDataProvider getUsersDataProvider();
  
  public abstract NomenclaturesDataProvider getNomenclaturesDataProvider();
  
  //public abstract UsersSysLogDataProvider getUsersSysLogDataProvider();

  public abstract MenuDataProvider getMenuDataProvider(int webApplication);
  
  //public abstract ExtMenuDataProvider getExtMenuDataProvider();

  public abstract ComissionMemberDataProvider getComissionMemberDataProvider();
  
  public abstract ApplicationsDataProvider getApplicationsDataProvider();
  
  public abstract UniversityDataProvider getUniversityDataProvider();
  
  public abstract DiplomaTypeDataProvider getDiplomaTypeDataProvider();
  
  public abstract DiplomaTypeAttachmentDataProvider getDiplomaTypeAttachmentDataProvider();
  
  public abstract TrainingCourseDataProvider getTrainingCourseDataProvider();

  public abstract AttachmentDataProvider getApplicationAttachmentDataProvider();
  
  public abstract UniversityValidityDataProvider getUniversityValidityDataProvider();
  
  public abstract AttachmentDataProvider getUniExamAttachmentDataProvider();
  
  public abstract AttachmentDataProvider getDiplExamAttachmentDataProvider();
  
  public abstract CompetentInstitutionDataProvider getCompetentInstitutionDataProvider();
  
  public abstract CommissionCalendarDataProvider getCommissionCalendarDataProvider();
  
  public abstract CompanyDataProvider getCompanyDataProvider();

    public abstract ExtCompanyDataProvider getExtCompanyDataProvider();

    public abstract ExpertStatementAttachmentDataProvider getExpertStatementAttachmentDataProvider();
  
  public abstract UtilsDataProvider getUtilsDataProvider();
  
  public abstract TrainingInstitutionDataProvider getTrainingInstitutionDataProvider();
  
  public abstract ExtPersonDataProvider getExtPersonDataProvider();
  
  //public abstract ExtUsersDataProvider getExtUsersDataProvider();

  public abstract MailDataProvider getMailDataProvider();
  
  public abstract ExtApplicationsDataProvider getExtApplicationsDataProvider();
  
  public abstract ExtApplicationAttachmentDataProvider getExtApplicationAttachmentDataProvider();
  
  public abstract ExtTrainingCourseDataProvider getExtTrainingCourseDataProvider();
  
  public abstract EventTypeDataProvider getEventTypeDataProvider();

  public abstract EventDataProvider getEventDataProvider();
  
  public abstract InquiresDataProvider getInquiresDataProvider();
  
  public abstract EsoedDataProviderImpl getEsoedDataProvider();
  
  public abstract AcademicRecognitionDataProvider getAcademicRecognitionDataProvider();
  
  //RayaWritten----------------------------------------------------------------------
  public abstract ProfessionalInstitutionDataProvider getProfessionalInstitutionDataProvider();
  
  public abstract RegprofApplicationDataProvider getRegprofApplicationDataProvider();
  
  public abstract RegprofApplicationAttachmentDataProvider getDocExamAttachmentDataProvider();
  public abstract BGAcademicRecognitionLogDataProvider getBGAcademicRecognitionLogDataProvider();
  //---------------------------------------------------------------------------------------
  public abstract RegprofApplicationAttachmentDataProvider getRegprofApplicationAttachmentDataProvider();

  public abstract RegprofTrainingCourseDataProviderImpl getRegprofTrainingCourseDataProvider();
  
  public abstract RegprofInquireDataProviderImpl getRegprofInquireDataProvider();
  public abstract RegprofApplicationAttachmentDataProvider getProfessionExperienceAttachmentDataProvider();
  
  public abstract ExtRegprofApplicationsDataProvider getExtRegprofApplicationsDataProvider();
  
  public abstract ExtRegprofApplicationAttachmentDataProvider getExtRegprofApplicationAttachmentDataProvider();
  
  public abstract PaymentsDataProvider getPaymentsDataProvider();

  public abstract NumgeneratorDataProvider getNumgeneratorDataProvider();

  public abstract RasApplicationsDataProvider getRasApplicationsDataProvider();

  public abstract ApostilleApplicationsDataProvider getApostilleApplicationsDataProvider();
}
