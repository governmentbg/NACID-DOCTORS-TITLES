package com.nacid.bl.impl;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.academicrecognition.BGAcademicRecognitionLogDataProvider;
import com.nacid.bl.applications.*;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachmentDataProvider;
import com.nacid.bl.comision.ComissionMemberDataProvider;
import com.nacid.bl.events.EventDataProvider;
import com.nacid.bl.events.EventTypeDataProvider;
import com.nacid.bl.external.applications.ExtCompanyDataProvider;
import com.nacid.bl.impl.academicrecognition.AcademicRecognitionDataProviderImpl;
import com.nacid.bl.impl.academicrecognition.BGAcademicRecognitionLogDataProviderImpl;
import com.nacid.bl.impl.apostille.ApostilleApplicationsDataProviderImpl;
import com.nacid.bl.impl.applications.*;
import com.nacid.bl.impl.applications.regprof.RegprofApplicationAttachmentDataProviderImpl;
import com.nacid.bl.impl.applications.regprof.RegprofApplicationDataProviderImpl;
import com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseDataProviderImpl;
import com.nacid.bl.impl.comission.ComissionMemberDataProviderImpl;
import com.nacid.bl.impl.comission.CommissionCalendarDataProviderImpl;
import com.nacid.bl.impl.esoed.EsoedDataProviderImpl;
import com.nacid.bl.impl.events.EventDataProviderImpl;
import com.nacid.bl.impl.events.EventTypeDataProviderImpl;
import com.nacid.bl.impl.external.ExtPersonDataProviderImpl;
import com.nacid.bl.impl.external.applications.ExtApplicationAttachmentDataProviderImpl;
import com.nacid.bl.impl.external.applications.ExtApplicationsDataProviderImpl;
import com.nacid.bl.impl.external.applications.ExtCompanyDataProviderImpl;
import com.nacid.bl.impl.external.applications.ExtTrainingCourseDataProviderImpl;
import com.nacid.bl.impl.inquires.InquiresDataProviderImpl;
import com.nacid.bl.impl.mail.MailDataProviderImpl;
import com.nacid.bl.impl.menu.MenuDataProviderImpl;
import com.nacid.bl.impl.nomenclatures.NomenclaturesDataProviderImpl;
import com.nacid.bl.impl.numgenerator.NumgeneratorDataProviderImpl;
import com.nacid.bl.impl.payments.PaymentsDataProviderImpl;
import com.nacid.bl.impl.ras.RasApplicationsDataProviderImpl;
import com.nacid.bl.impl.regprof.ProfessionalInstitutionDataProviderImpl;
import com.nacid.bl.impl.regprof.RegprofInquireDataProviderImpl;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationAttachmentDataProviderImpl;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationsDataProviderImpl;
import com.nacid.bl.impl.users.UsersDataProviderImpl;
import com.nacid.bl.impl.utils.UtilsDataProviderImpl;
import com.nacid.bl.mail.MailDataProvider;
import com.nacid.bl.menu.MenuDataProvider;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.db.applications.AttachmentDB.ATTACHMENT_TYPE;
import com.nacid.db.regprof.RegprofApplicationAttachmentDB.REGPROF_ATTACHMENT_TYPE;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.Map;

public class NacidDataProviderImpl extends NacidDataProvider {
    private DataSource ds;
    
    private volatile UsersDataProvider usersDataProvider;
    private volatile NomenclaturesDataProviderImpl nomenclaturesDataProvider;
    // private UsersSysLogDataProvider usersSysLogDataProvider;
    private volatile Map<Integer, MenuDataProviderImpl> menuDataProvider = new HashMap<Integer, MenuDataProviderImpl>();
    //private volatile ExtMenuDataProvider extMenuDataProvider;
    private volatile ComissionMemberDataProvider comissionMemberDataProvider;
    private volatile ApplicationsDataProviderImpl applicationsDataProvider;
    private volatile UniversityDataProviderImpl universityDataProvider;
    private volatile DiplomaTypeDataProviderImpl diplomaTypeDataProvider;
    private volatile DiplomaTypeAttachmentDataProvider diplomaTypeAttachmentDataProvider;
    private volatile TrainingCourseDataProviderImpl trainingCourseDataProvider;
    private volatile AttachmentDataProviderImpl applicationAttachmentDataProvider;
    private volatile UniversityValidityDataProvider universityValidityDataProvider;
    private volatile AttachmentDataProvider uniExamAttachmentDataProvider;
    private volatile AttachmentDataProvider diplExamAttachmentDataProvider;
    private volatile CompetentInstitutionDataProvider competentInstitutionDataProvider;
    private volatile CommissionCalendarDataProviderImpl commissionCalendarDataProvider;
    private volatile CompanyDataProvider companyDataProvider;
    private volatile ExpertStatementAttachmentDataProvider expertStatementAttachmentDataProvider;
    private volatile UtilsDataProvider utilsDataProvider;
    private volatile TrainingInstitutionDataProvider trainingInstitutionDataProvider;
    private volatile MailDataProvider mailDataProvider;
    private volatile ExtPersonDataProviderImpl extPersonDataProvider;
    //private volatile ExtUsersDataProvider extUsersDataProvider;
    private volatile ExtApplicationsDataProviderImpl extApplicationsDataProvider;
    private volatile ExtApplicationAttachmentDataProviderImpl extApplicationAttachmentDataProvider;
    private volatile ExtTrainingCourseDataProviderImpl extTrainingCourseDataProvider;
    private volatile EventTypeDataProviderImpl eventTypeDataProvider;
    private volatile EventDataProviderImpl eventDataProvider;
    private volatile InquiresDataProviderImpl inquiresDataProvider;
    private volatile EsoedDataProviderImpl esoedDataProvider;
    private volatile AcademicRecognitionDataProviderImpl academicRecognitionDataProviderImpl;
    //RayaWritten--------------------------------------------------------------------------------------
    private volatile ProfessionalInstitutionDataProviderImpl professionalInstitutionDataProviderImpl;
    private volatile RegprofApplicationDataProviderImpl regprofApplicationDataProviderImpl;
    private volatile RegprofApplicationAttachmentDataProvider docExamAttachmentDataProvider;
    private volatile RegprofApplicationAttachmentDataProvider professionExperienceAttachmentDataProvider;
    private volatile EventDataProvider regprofEventDataProvider;
    private volatile EventTypeDataProvider regprofEventTypeDataProvider;
    private volatile BGAcademicRecognitionLogDataProvider bgAcademicRecognitionLogDataProvider;
    //-----------------------------------------------------------------------------------------------
    private volatile RegprofApplicationAttachmentDataProviderImpl regprofApplicationAttachmentDataProviderImpl;
    private volatile RegprofTrainingCourseDataProviderImpl regprofTrainingCourseDataProviderImpl;
    private volatile RegprofInquireDataProviderImpl regprofInquireDataProviderImpl;
    private volatile ExtRegprofApplicationsDataProviderImpl extRegprofApplicationsDataProviderImpl;
    private volatile ExtRegprofApplicationAttachmentDataProviderImpl extRegprofApplicationAttachmentDataProviderImpl;
    private volatile PaymentsDataProviderImpl paymentsDataProviderImpl;
    private volatile NumgeneratorDataProviderImpl numgeneratorDataProviderImpl;
    private volatile ExtCompanyDataProviderImpl extCompanyDataProvider;
    private volatile RasApplicationsDataProviderImpl rasApplicationsDataProvider;
    private volatile ApostilleApplicationsDataProviderImpl apostilleApplicationsDataProvider;

    public NacidDataProviderImpl(DataSource ds) {
        this.ds = ds;
    }

    public DataSource getDataSource() {
        return ds;
    }

    @Override
    public UsersDataProvider getUsersDataProvider() {
        if (usersDataProvider == null) {
            synchronized (this) {
                if (usersDataProvider == null) {
                    usersDataProvider = new UsersDataProviderImpl(this/*, false*/);
                }
            }
        }
        return usersDataProvider;
    }

    @Override
    public NomenclaturesDataProviderImpl getNomenclaturesDataProvider() {
        if (nomenclaturesDataProvider == null) {
            synchronized (this) {
                if (nomenclaturesDataProvider == null) {
                    nomenclaturesDataProvider = new NomenclaturesDataProviderImpl(this);
                }
            }
        }
        return nomenclaturesDataProvider;
    }

    @Override
    public MenuDataProvider getMenuDataProvider(int webApplication) {
        if (menuDataProvider.get(webApplication) == null) {
            synchronized (this) {
                if (menuDataProvider.get(webApplication) == null) {
                    MenuDataProviderImpl mdp = new MenuDataProviderImpl(this, webApplication);
                    menuDataProvider.put(webApplication, mdp);
                }
            }
        }
        return menuDataProvider.get(webApplication);
    }
    /*
    @Override
    public ExtMenuDataProvider getExtMenuDataProvider() {
        if (extMenuDataProvider == null) {
            synchronized (this) {
                if (extMenuDataProvider == null) {
                	extMenuDataProvider = new MenuDataProviderImpl(this, true);
                }
            }
        }
        return extMenuDataProvider;
    }*/

    @Override
    public ComissionMemberDataProvider getComissionMemberDataProvider() {
        if (comissionMemberDataProvider == null) {
            synchronized (this) {
                if (comissionMemberDataProvider == null) {
                    comissionMemberDataProvider = new ComissionMemberDataProviderImpl(this);
                }
            }
        }
        return comissionMemberDataProvider;
    }

    public ApplicationsDataProviderImpl getApplicationsDataProvider() {
        if (applicationsDataProvider == null) {
            synchronized (this) {
                if (applicationsDataProvider == null) {
                    applicationsDataProvider = new ApplicationsDataProviderImpl(this);
                }
            }
        }
        return applicationsDataProvider;
    }

    @Override
    public UniversityDataProviderImpl getUniversityDataProvider() {
        if (universityDataProvider == null) {
            synchronized (this) {
                if (universityDataProvider == null) {
                    universityDataProvider = new UniversityDataProviderImpl(this);
                }
            }
        }
        return universityDataProvider;
    }

    @Override
    public DiplomaTypeDataProviderImpl getDiplomaTypeDataProvider() {
        if (diplomaTypeDataProvider == null) {
            synchronized (this) {
                if (diplomaTypeDataProvider == null) {
                    diplomaTypeDataProvider = new DiplomaTypeDataProviderImpl(this);
                }
            }
        }
        return diplomaTypeDataProvider;
    }

    @Override
    public DiplomaTypeAttachmentDataProvider getDiplomaTypeAttachmentDataProvider() {
        if (diplomaTypeAttachmentDataProvider == null) {
            synchronized (this) {
                if (diplomaTypeAttachmentDataProvider == null) {
                    diplomaTypeAttachmentDataProvider = new DiplomaTypeAttachmentDataProviderImpl(this);
                }
            }
        }
        return diplomaTypeAttachmentDataProvider;
    }

    @Override
    public TrainingCourseDataProviderImpl getTrainingCourseDataProvider() {
        if (trainingCourseDataProvider == null) {
            synchronized (this) {
                if (trainingCourseDataProvider == null) {
                    trainingCourseDataProvider = new TrainingCourseDataProviderImpl(this);
                }
            }
        }
        return trainingCourseDataProvider;
    }

    @Override
    public AttachmentDataProviderImpl getApplicationAttachmentDataProvider() {
        if (applicationAttachmentDataProvider == null) {
            synchronized (this) {
                if (applicationAttachmentDataProvider == null) {
                    applicationAttachmentDataProvider = new AttachmentDataProviderImpl(this, ATTACHMENT_TYPE.APPLICATION);
                }
            }
        }
        return applicationAttachmentDataProvider;
    }

    @Override
    public UniversityValidityDataProvider getUniversityValidityDataProvider() {
        if (universityValidityDataProvider == null) {
            synchronized (this) {
                if (universityValidityDataProvider == null) {
                    universityValidityDataProvider = new UniversityValidityDataProviderImpl(this);
                }
            }
        }
        return universityValidityDataProvider;
    }
    
    @Override
    public AttachmentDataProvider getUniExamAttachmentDataProvider() {
        if (uniExamAttachmentDataProvider == null) {
            synchronized (this) {
                if(uniExamAttachmentDataProvider == null) {
                    uniExamAttachmentDataProvider = new AttachmentDataProviderImpl(this, ATTACHMENT_TYPE.UNIV_EXAMINATION);
                }
            }
        }
        return uniExamAttachmentDataProvider;
    }
    
    @Override
    public AttachmentDataProvider getDiplExamAttachmentDataProvider() {
        if (diplExamAttachmentDataProvider == null) {
            synchronized (this) {
                if(diplExamAttachmentDataProvider == null) {
                    diplExamAttachmentDataProvider = new AttachmentDataProviderImpl(this, ATTACHMENT_TYPE.DIPLOMA_EXAMINATION);
                }
            }
        }
        return diplExamAttachmentDataProvider;
    }
    
    //RayaWritten------------------------------------------------------
    public RegprofApplicationAttachmentDataProvider getDocExamAttachmentDataProvider() {
        if (docExamAttachmentDataProvider == null) {
            synchronized (this) {
                if(docExamAttachmentDataProvider == null) {
                    docExamAttachmentDataProvider = new RegprofApplicationAttachmentDataProviderImpl(this, REGPROF_ATTACHMENT_TYPE.DOCUMENT_EXAMINATION);
                }
            }
        }
        return docExamAttachmentDataProvider;
    }
    //---------------------------------------------------------------
    
    public RegprofApplicationAttachmentDataProvider getProfessionExperienceAttachmentDataProvider() {
        if (professionExperienceAttachmentDataProvider == null) {
            synchronized (this) {
                if(professionExperienceAttachmentDataProvider == null) {
                    professionExperienceAttachmentDataProvider = new RegprofApplicationAttachmentDataProviderImpl(this, REGPROF_ATTACHMENT_TYPE.REGPROF_PROFESSION_EXPERIENCE_DOCUMENTS);
                }
            }
        }
        return professionExperienceAttachmentDataProvider;
    }
    
    @Override
    public CompetentInstitutionDataProvider getCompetentInstitutionDataProvider() {
        if (competentInstitutionDataProvider == null) {
            synchronized (this) {
                if(competentInstitutionDataProvider == null) {
                    competentInstitutionDataProvider = new CompetentInstitutionDataProviderImpl(this);
                }
            }
        }
        return competentInstitutionDataProvider;
    }
    
    @Override
    public CommissionCalendarDataProviderImpl getCommissionCalendarDataProvider() {
        if (commissionCalendarDataProvider == null) {
            synchronized (this) {
                if(commissionCalendarDataProvider == null) {
                    commissionCalendarDataProvider = new CommissionCalendarDataProviderImpl(this);
                }
            }
        }
        return commissionCalendarDataProvider;
    }
    
    @Override
    public CompanyDataProvider getCompanyDataProvider() {
        if (companyDataProvider == null) {
            synchronized (this) {
                if(companyDataProvider == null) {
                    companyDataProvider = new CompanyDataProviderImpl(this);
                }
            }
        }
        return companyDataProvider;
    }

    @Override
    public ExtCompanyDataProvider getExtCompanyDataProvider() {
        if (extCompanyDataProvider == null) {
            synchronized (this) {
                if(extCompanyDataProvider == null) {
                    extCompanyDataProvider = new ExtCompanyDataProviderImpl(this);
                }
            }
        }
        return extCompanyDataProvider;
    }
    
    @Override
    public ExpertStatementAttachmentDataProvider getExpertStatementAttachmentDataProvider() {
        if (expertStatementAttachmentDataProvider == null) {
            synchronized (this) {
                if(expertStatementAttachmentDataProvider == null) {
                    expertStatementAttachmentDataProvider = new ExpertStatementAttachmentDataProviderImpl(this);
                }
            }
        }
        return expertStatementAttachmentDataProvider;
    }
    @Override
    public UtilsDataProvider getUtilsDataProvider() {
        if (utilsDataProvider == null) {
            synchronized (this) {
                if(utilsDataProvider == null) {
                	utilsDataProvider = new UtilsDataProviderImpl(this);
                }
            }
        }
        return utilsDataProvider;
    }
    @Override
    public TrainingInstitutionDataProvider getTrainingInstitutionDataProvider() {
        if (trainingInstitutionDataProvider == null) {
            synchronized (this) {
                if(trainingInstitutionDataProvider == null) {
                    trainingInstitutionDataProvider = new TrainingInstitutionDataProviderImpl(this);
                }
            }
        }
        return trainingInstitutionDataProvider;
    }

    @Override
    public MailDataProvider getMailDataProvider() {
        if (mailDataProvider == null) {
            synchronized (this) {
                if(mailDataProvider == null) {
                    mailDataProvider = new MailDataProviderImpl(this);
                }
            }
        }
        return mailDataProvider;
    }

    @Override
    public ExtPersonDataProviderImpl getExtPersonDataProvider() {
        if (extPersonDataProvider == null) {
            synchronized (this) {
                if(extPersonDataProvider == null) {
                    extPersonDataProvider = new ExtPersonDataProviderImpl(this);
                }
            }
        }
        return extPersonDataProvider;
    }
    /*public ExtUsersDataProvider getExtUsersDataProvider() {
    	if (extUsersDataProvider == null) {
            synchronized (this) {
                if(extUsersDataProvider == null) {
                	extUsersDataProvider = new UsersDataProviderImpl(this, true);
                }
            }
        }
        return extUsersDataProvider;
    }
    */
    public ExtApplicationsDataProviderImpl getExtApplicationsDataProvider() {
    	if (extApplicationsDataProvider == null) {
            synchronized (this) {
                if(extApplicationsDataProvider == null) {
                	extApplicationsDataProvider = new ExtApplicationsDataProviderImpl(this);
                }
            }
        }
        return extApplicationsDataProvider;
    }

    @Override
    public ExtApplicationAttachmentDataProviderImpl getExtApplicationAttachmentDataProvider() {
        if (extApplicationAttachmentDataProvider == null) {
            synchronized (this) {
                if(extApplicationAttachmentDataProvider == null) {
                    extApplicationAttachmentDataProvider = new ExtApplicationAttachmentDataProviderImpl(this);
                }
            }
        }
        return extApplicationAttachmentDataProvider;
    }

    @Override
    public ExtTrainingCourseDataProviderImpl getExtTrainingCourseDataProvider() {
        
        if (extTrainingCourseDataProvider == null) {
            synchronized (this) {
                if(extTrainingCourseDataProvider == null) {
                    extTrainingCourseDataProvider = new ExtTrainingCourseDataProviderImpl(this);
                }
            }
        }
        return extTrainingCourseDataProvider;
    }

    @Override
    public EventTypeDataProvider getEventTypeDataProvider() {
        if (eventTypeDataProvider == null) {
            synchronized (this) {
                if(eventTypeDataProvider == null) {
                    eventTypeDataProvider = new EventTypeDataProviderImpl(this);
                }
            }
        }
        return eventTypeDataProvider;
    }
    
    @Override
    public EventDataProvider getEventDataProvider() {
        if (eventDataProvider == null) {
            synchronized (this) {
                if(eventDataProvider == null) {
                    eventDataProvider = new EventDataProviderImpl(this);
                }
            }
        }
        return eventDataProvider;
    }
    
    
    public InquiresDataProviderImpl getInquiresDataProvider() {
        if (inquiresDataProvider == null) {
            synchronized (this) {
                if(inquiresDataProvider == null) {
                	inquiresDataProvider = new InquiresDataProviderImpl(this);
                }
            }
        }
        return inquiresDataProvider;
    }
    
    
    public EsoedDataProviderImpl getEsoedDataProvider() {
        if (esoedDataProvider == null) {
            synchronized (this) {
                if(esoedDataProvider == null) {
                	esoedDataProvider = new EsoedDataProviderImpl(this);
                }
            }
        }
        return esoedDataProvider;
    }
    
    public AcademicRecognitionDataProviderImpl getAcademicRecognitionDataProvider() {
        if (academicRecognitionDataProviderImpl == null) {
            synchronized (this) {
                if(academicRecognitionDataProviderImpl == null) {
                    academicRecognitionDataProviderImpl = new AcademicRecognitionDataProviderImpl(this);
                }
            }
        }
        return academicRecognitionDataProviderImpl;
    }

    //RayaWritten---------------------------------------------------------------------------------
    @Override
    public ProfessionalInstitutionDataProviderImpl getProfessionalInstitutionDataProvider() {
       if(professionalInstitutionDataProviderImpl==null) {
           synchronized (this) {
               if(professionalInstitutionDataProviderImpl==null) {
                   professionalInstitutionDataProviderImpl = new ProfessionalInstitutionDataProviderImpl(this);
               }        
           }
       }
       return professionalInstitutionDataProviderImpl;
       
    }
    
    @Override
    public BGAcademicRecognitionLogDataProvider getBGAcademicRecognitionLogDataProvider() {
       if(bgAcademicRecognitionLogDataProvider==null) {
           synchronized (this) {
               if(bgAcademicRecognitionLogDataProvider==null) {
            	   bgAcademicRecognitionLogDataProvider = new BGAcademicRecognitionLogDataProviderImpl(this);
               }        
           }
       }
       return bgAcademicRecognitionLogDataProvider;
       
    }
    
    @Override
    public RegprofApplicationDataProviderImpl getRegprofApplicationDataProvider(){
        if(regprofApplicationDataProviderImpl == null) {
            synchronized (this) {
                if(regprofApplicationDataProviderImpl == null) {
                    regprofApplicationDataProviderImpl = new RegprofApplicationDataProviderImpl(this);
                }
            }
        }
        return regprofApplicationDataProviderImpl;
    }
    
    @Override
    public RegprofApplicationAttachmentDataProviderImpl getRegprofApplicationAttachmentDataProvider(){
        if(regprofApplicationAttachmentDataProviderImpl == null) {
            synchronized (this) {
                if(regprofApplicationAttachmentDataProviderImpl == null) {
                    regprofApplicationAttachmentDataProviderImpl = new RegprofApplicationAttachmentDataProviderImpl(this);
                }
            }
        }
        return regprofApplicationAttachmentDataProviderImpl;
    }
    
    @Override
    public RegprofTrainingCourseDataProviderImpl getRegprofTrainingCourseDataProvider(){
        if(regprofTrainingCourseDataProviderImpl == null) {
            synchronized (this) {
                if(regprofTrainingCourseDataProviderImpl == null) {
                    regprofTrainingCourseDataProviderImpl = new RegprofTrainingCourseDataProviderImpl(this);
                }
            }
        }
        return regprofTrainingCourseDataProviderImpl;
    }
    
    @Override
    public RegprofInquireDataProviderImpl getRegprofInquireDataProvider(){
        if(regprofInquireDataProviderImpl == null) {
            synchronized (this) {
                if(regprofInquireDataProviderImpl == null) {
                    regprofInquireDataProviderImpl = new RegprofInquireDataProviderImpl(this);
                }
            }
        }
        return regprofInquireDataProviderImpl;
    }
    public ExtRegprofApplicationsDataProviderImpl getExtRegprofApplicationsDataProvider() {
        if (extRegprofApplicationsDataProviderImpl == null) {
            synchronized (this) {
                if (extRegprofApplicationsDataProviderImpl == null) {
                    extRegprofApplicationsDataProviderImpl = new ExtRegprofApplicationsDataProviderImpl(this);
                }
            }
        }
        return extRegprofApplicationsDataProviderImpl;
    }
    public ExtRegprofApplicationAttachmentDataProviderImpl getExtRegprofApplicationAttachmentDataProvider() {
       if (extRegprofApplicationAttachmentDataProviderImpl == null) {
           synchronized (this) {
               if (extRegprofApplicationAttachmentDataProviderImpl == null) {
                   extRegprofApplicationAttachmentDataProviderImpl = new ExtRegprofApplicationAttachmentDataProviderImpl(this);
               }
           }
       }
       return extRegprofApplicationAttachmentDataProviderImpl;
    }
    public PaymentsDataProviderImpl getPaymentsDataProvider() {
        if (paymentsDataProviderImpl == null) {
            synchronized (this) {
                if (paymentsDataProviderImpl == null) {
                    paymentsDataProviderImpl = new PaymentsDataProviderImpl(this);
                }
            }
        }
        return paymentsDataProviderImpl;
     }

    public NumgeneratorDataProviderImpl getNumgeneratorDataProvider() {
        if (numgeneratorDataProviderImpl == null) {
            synchronized (this) {
                if (numgeneratorDataProviderImpl == null) {
                    numgeneratorDataProviderImpl = new NumgeneratorDataProviderImpl(this);
                }
            }
        }
        return numgeneratorDataProviderImpl;
    }
    public RasApplicationsDataProviderImpl getRasApplicationsDataProvider() {
        if (rasApplicationsDataProvider == null) {
            synchronized (this) {
                if (rasApplicationsDataProvider == null) {
                    rasApplicationsDataProvider = new RasApplicationsDataProviderImpl(this);
                }
            }
        }
        return rasApplicationsDataProvider;
    }

    public ApostilleApplicationsDataProviderImpl getApostilleApplicationsDataProvider() {
        if (apostilleApplicationsDataProvider == null) {
            synchronized (this) {
                if (apostilleApplicationsDataProvider == null) {
                    apostilleApplicationsDataProvider = new ApostilleApplicationsDataProviderImpl(this);
                }
            }
        }
        return apostilleApplicationsDataProvider;
    }
}
