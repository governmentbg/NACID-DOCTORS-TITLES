package com.nacid.bl.impl.applications;

import com.nacid.bl.applications.*;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.nomenclatures.SpecialityImpl;
import com.nacid.bl.nomenclatures.Speciality;
import com.nacid.data.applications.*;
import com.nacid.data.nomenclatures.SpecialityRecord;
import com.nacid.db.applications.ApplicationsDB;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class TrainingCourseDataProviderImpl implements TrainingCourseDataProvider{
    private NacidDataProviderImpl nacidDataProvider;
    private ApplicationsDB db;

    public TrainingCourseDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
        this.db = new ApplicationsDB(nacidDataProvider.getDataSource(), nacidDataProvider.getNomenclaturesDataProvider().getNomenclaturesDB());
        this.nacidDataProvider = nacidDataProvider;
    }
    public TrainingCourse getTrainingCourse(int courseId) {
        try {
            TrainingCourseRecord record =  db.selectRecord(new TrainingCourseRecord(), courseId);
            if (record == null) {
                return null; 
            }
            return new TrainingCourseImpl(nacidDataProvider, this, record);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    List<TrainingCourseGraduationWay> getTrainingCourseGraduationWays(int trainingCourseId) {
        try {
            List<TrainingCourseGraduationWayRecord> records = db.getApplicationGraduationWayRecords(trainingCourseId);
            if (records.size() == 0) {
                return null;
            }
            List<TrainingCourseGraduationWay> result = new ArrayList<TrainingCourseGraduationWay>();
            for (TrainingCourseGraduationWayRecord r: records) {
                result.add(new TrainingCourseGraduationWayImpl(r, nacidDataProvider));
            }
            return result;
        } catch (SQLException e) {
            throw Utils.logException(e);

        }
    }
    TrainingCourseTrainingForm getTrainingCourseTrainingForm(int trainingCourseId) {
        try {
            /**
             * o4akva se 4e getApplicationTrainingFormRecords vry6ta 0 ili 1 broq zapisi, zashtoto moje da ima samo edin selectnat training_form!!!!
             */
            List<TrainingCourseTrainingFormRecord> records = db.getApplicationTrainingFormRecords(trainingCourseId);
            if (records.size() == 0) {
                return null;
            }
            return new TrainingCourseTrainingFormImpl(nacidDataProvider, records.get(0));
        } catch (SQLException e) {
            throw Utils.logException(e);

        }
    }
    
    @Override
    public List<Speciality> loadRecognizedSpecialities(int trainingCourseId) {
        try {
            List<SpecialityRecord> recs = db.loadRecognizedSpecialities(trainingCourseId);
            List<Speciality> ret = new ArrayList<Speciality>();
            if(recs != null) {
                for(SpecialityRecord rec : recs) {
                    ret.add(new SpecialityImpl(rec));
                }
            }
            return ret;
        } catch (Exception e) {
            throw Utils.logException(e);
        }
    }
    public List<TrainingCourseSpeciality> loadSpecialities(int trainingCourseId) {
        try {
            List<TrainingCourseSpecialityRecord> specialities = db.getSpecialitiesByTrainingCourse(trainingCourseId);
            List<TrainingCourseSpeciality> result = new ArrayList<TrainingCourseSpeciality>();
            for (TrainingCourseSpecialityRecord s:specialities) {
                result.add(new TrainingCourseSpecialityImpl(s.getTrainingCourseId(), s.getSpecialityId(), s.getOriginalSpecialityId()));
            }
            return result.size() == 0 ? null : result;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public void saveRecognizedSpecialities(int trainingCourseId, List<Integer> specialityIds) {
        try {
            db.deleteRecognizedSpecialities(trainingCourseId);
            db.saveRecognizedSpecialities(trainingCourseId, specialityIds);
        } catch (Exception e) {
            throw Utils.logException(e);
        }
    }
    
    public int saveTrainingCourse(int id, String diplomaSeries, String diplomaNumber, String diplomaRegistrationNumber, Date diplomaDate, Integer diplomaTypeId, String firstName, String surName, String lastName,
            boolean isJointDegree, /*Integer trainingLocationCountryId, String trainingLocationCity,*/ Date trainingStart, Date trainingEnd, Double trainingDuration,
            Integer durationUnitId, BigDecimal credits, Integer educationLevelId, Integer qualificationId, boolean recognized,
            Integer schoolCountryId, String scoolCity,
            String schoolName, Date schoolGraduationDate, String schoolNotes, 
            Integer prevDiplUniversityId, Integer prevDiplEduLevelId,
            Date prevDiplGraduationDate, String prevDiplNotes, Integer prevDiplomaSpecialityId, 
            Integer recognizedEduLevel,
            /*Integer trainingInstId,*/ Integer recognizedQualification, Integer graduationDocumentTypeId,
            Integer creditHours, Integer ectsCredits, int ownerId, String thesisTopic, String thesisTopicEn,
                                  Integer profGroupId, Integer recognizedProfGroupId, Date thesisDefenceDate,
                                  Integer thesisBibliography, Integer thesisVolume, Integer thesisLanguage, String thesisAnnotation, String thesisAnnotationEn, Integer originalQualificationId) {
        
        if(firstName != null) firstName = firstName.toUpperCase();
        if(surName != null) surName = surName.toUpperCase();
        if(lastName != null) lastName = lastName.toUpperCase();
        
        TrainingCourseRecord record = new TrainingCourseRecord(id, diplomaSeries, diplomaNumber, diplomaRegistrationNumber, Utils.getSqlDate(diplomaDate), diplomaTypeId, firstName, surName, lastName,
                isJointDegree ? 1 : 0, /*trainingLocationCountryId, trainingLocationCity,*/ Utils.getSqlDate(trainingStart), Utils.getSqlDate(trainingEnd), trainingDuration,
                durationUnitId, credits, /*specialityId,*/ educationLevelId, qualificationId, recognized ? 1 : 0,
                schoolCountryId, scoolCity, schoolName, Utils.getSqlDate(schoolGraduationDate), 
                schoolNotes, prevDiplUniversityId,
                prevDiplEduLevelId, Utils.getSqlDate(prevDiplGraduationDate), prevDiplNotes, prevDiplomaSpecialityId, 
                recognizedEduLevel, /*trainingInstId,*/ recognizedQualification, graduationDocumentTypeId, creditHours,
                ectsCredits, ownerId, thesisTopic, thesisTopicEn, profGroupId, recognizedProfGroupId, Utils.getSqlDate(thesisDefenceDate), thesisBibliography, thesisVolume, thesisLanguage, thesisAnnotation, thesisAnnotationEn, originalQualificationId);
        try {
            if (id == 0) {
                record = db.insertRecord(record);
            } else {
                db.updateRecord(record);
            }

            return record.getId();
        } catch (Exception e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public void saveTrainingCourseSpecialities(int trainingCourseId, List<TrainingCourseSpecialityImpl> specialities) {
        try {
            db.deleteSpecliaitiesByTrainingCourse(trainingCourseId);
            if (specialities != null) {
                for (TrainingCourseSpecialityImpl speciality : specialities) {
                    TrainingCourseSpecialityRecord r = new TrainingCourseSpecialityRecord(0, trainingCourseId, speciality.getSpecialityId(), speciality.getOriginalSpecialityId());
                    db.insertRecord(r);
                }
            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }


    }

    public int saveTrainingCourse(int id, String firstName, String surName, String lastName, int ownerId) {
        if(firstName != null) firstName = firstName.toUpperCase();
        if(surName != null) surName = surName.toUpperCase();
        if(lastName != null) lastName = lastName.toUpperCase();
        try {
            if (id == 0) {
                return db.insertTrainingForm(firstName, surName, lastName, ownerId);
            } else {
                db.updateTrainingForm(id, firstName, surName, lastName, ownerId);
                return id;
            }
        } catch (Exception e) {
            throw Utils.logException(e);
        }
    }
    public void deleteTrainingCourseGraduationWays(int trainingCourseId) {
        try {
            db.deleteTrainingCourseGraduationWayRecords(trainingCourseId);
        } catch (Exception e) {
            throw Utils.logException(e);
        }
    }
    public void deleteTrainingCourseSpecialities(int trainingCourseId) {
        try {
            db.deleteSpecliaitiesByTrainingCourse(trainingCourseId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public void addTrainingCourseGraduationWayRecord(int trainingCourseId, Integer graduationWayId, String notes) {
        try {
            TrainingCourseGraduationWayRecord record = new TrainingCourseGraduationWayRecord(0, trainingCourseId, graduationWayId, notes);
            db.insertRecord(record);
        } catch (Exception e) {
            throw Utils.logException(e);
        }
    }
    public void setTrainingCourseTrainingForm(int trainingCourseId, Integer trainingFormId, String notes) {
        try {
            db.deleteTrainingCourseTrainingFormRecords(trainingCourseId);
            TrainingCourseTrainingFormRecord record = new TrainingCourseTrainingFormRecord(0, trainingCourseId, trainingFormId, notes);
            db.insertRecord(record);
        } catch (Exception e) {
            throw Utils.logException(e);
        }
    }
    public void updateTrainingCourseUniversities(int trainingCourseId, UniversityIdWithFacultyId baseUniversity, List<UniversityIdWithFacultyId> jointUniversities) {
        try {
                     
            Set<Integer> newUniversityIds = new LinkedHashSet<Integer>();
            newUniversityIds.add(baseUniversity.getUniversityId());
            if (jointUniversities !=null && jointUniversities.size() > 0) {
            	newUniversityIds.addAll(jointUniversities.stream().map(r -> r.getUniversityId()).collect(Collectors.toList()));
            }
            TrainingCourse tc = getTrainingCourse(trainingCourseId);
            List<Integer> oldUniversities = tc.getUniversityIds();
            
            //Trqbva da se iztriqt ot bazata vsi4ki university examinations, za vyvedenite v bazata universiteti, koito gi nqma v noviq spisyk na universities
            //primerno - v bazata ima vyvedeni examinations za universiteti 1+2+3, a sega trqbva da se zapishat universiteti 1+3 => trqbva da se iztrie
            //examination-a za university 2!!!!
            
			if (oldUniversities != null) {
                oldUniversities.stream().filter(i -> !newUniversityIds.contains(i)).forEach(i -> deleteUniversityExaminations(trainingCourseId, i));
                /*for (int i : oldUniversities) {
					if (!newUniversityIds.contains(i)) {
						deleteUniversityExaminations(trainingCourseId, i);
					}
				}*/
			}
            db.deleteDiplomaIssuerRecords(trainingCourseId);
            int ord = 1;
            List<UniversityIdWithFacultyId> allUnis = new ArrayList<>();
            if (baseUniversity != null) {
                allUnis.add(baseUniversity);
            }
            if (jointUniversities != null) {
                allUnis.addAll(jointUniversities);
            }
            for (UniversityIdWithFacultyId u : allUnis) {
            	DiplomaIssuerRecord r = new DiplomaIssuerRecord(0, trainingCourseId, u.getUniversityId(), u.getFacultyId(), ord++);
            	db.insertRecord(r);
            }
        } catch (Exception e) {
            throw Utils.logException(e);
        }
    }
    
    public void deleteUniversityExaminations(int trainingCourseId, int universityId) {
        try {
            db.deleteUniversityExaminationRecords(trainingCourseId, universityId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }

    }
    public void saveUniversityExamination(int trainingCourseId, int universityValidityId, int userId, String notes) {
        try {
            UniversityValidityDataProvider universityValidityDataProvider = nacidDataProvider.getUniversityValidityDataProvider();
        	UniversityValidity uv = universityValidityDataProvider.getUniversityValidity(universityValidityId);
        	int universityId = uv.getUniversityId();
        	db.deleteUniversityExaminationRecords(trainingCourseId, universityId);
        	
        	UniversityExaminationRecord record = new UniversityExaminationRecord(0, trainingCourseId, universityValidityId, userId, Utils.getSqlDate(uv.getExaminationDate()), uv.isRecognized() ? 1 : 0, notes);
            db.insertRecord(record);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }

    }
    /**
     * tozi method se izpolzva v TrainingCourseImpl !
     * @param trainingCourseId
     * @return
     */
    public List<UniversityExamination> getUniversityExaminations(int trainingCourseId) {
        try {
            List<UniversityExaminationRecord> records = db.getUniversityExaminationRecords(trainingCourseId);
            if (records.size() == 0) {
                return null;
            }
            List<UniversityExamination> result = new ArrayList<UniversityExamination>();
            for (UniversityExaminationRecord r: records) {
            	result.add(new UniversityExaminationImpl(r, nacidDataProvider));
            }
            return result;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }

    }
    
    /*public Map<Integer, UniversityExamination> getUniversityExaminationsAsMap(int trainingCourseId) {
        Map<Integer, UniversityExamination> result = new HashMap<Integer, UniversityExamination>();
        List<UniversityExamination> universityExaminations = getUniversityExaminations(trainingCourseId);
        if (universityExaminations != null) {
            for (UniversityExamination e:universityExaminations) {
                result.put(e.getUniversityValidityId(), e);
            }
        }
        return result;
        
    }*/
    /**
     * tozi method se izpolzva v TrainingCourseImpl !
     * @param trainingCourseId
     * @return - o4akva se 4e za vseki trainingCourseId ima samo edin diplomaExamination!
     */
    public DiplomaExamination getDiplomaExamination(int trainingCourseId) {
        try {
            List<DiplomaExaminationRecord> records = db.getDiplomaExaminationRecords(trainingCourseId);
            if (records.size() == 0) {
                return null;
            }
            return new DiplomaExaminationImpl(records.get(0), nacidDataProvider);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public int saveDiplomaExamination(int id, int trainingCourseId, int userId, Date examinationDate, String notes, boolean isRecognized,
            Integer competentInstitutionId, boolean isInstitutionCommunicated, boolean isUniversityCommunicated, boolean foundInRegister) {
        try {
            DiplomaExaminationRecord record = new DiplomaExaminationRecord(id, trainingCourseId, userId, Utils.getSqlDate(examinationDate), notes,
                    isRecognized ? 1 : 0, competentInstitutionId, isInstitutionCommunicated ? 1 : 0, isUniversityCommunicated ? 1 : 0, foundInRegister ? 1 : 0);
            if (id == 0) {
                record = db.insertRecord(record);
            } else {
                db.updateRecord(record);
            }
            return record.getId();
        } catch (SQLException e) {
            throw Utils.logException(e);
        }

    }
    /**
     * 
     * @param diplomaTypeId
     * @return trainingCourses po daden diplomaTypeId - ideqta e pri editvane na DiplomaType - da se proverqva dali toq diploma type e vyrzan kym daden
     * training course - ako da - togava da ne moje da se promenq tozi diplomaType
     */
    public List<TrainingCourseRecord> getTrainingCourseRecords(int diplomaTypeId) {
    	try {
			return db.getTrainingCourseRecords(diplomaTypeId);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
    }
    /*public void deleteTrainingCourseTrainingLocations(int trainingCourseId) {
    	try {
			db.deleteTrainingCourseTrainingLocationRecords(trainingCourseId);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
    }*/
    public int saveTrainingCourseTrainingLocation(int trainingLocationId, int trainingCourseId, int trainigLocationTrainingCountryId, String trainingLocationTrainingCity, Integer trainingInstitutionId) {
    	TrainingCourseTrainingLocationRecord record = new TrainingCourseTrainingLocationRecord(trainingLocationId , trainingCourseId, trainigLocationTrainingCountryId, trainingLocationTrainingCity, trainingInstitutionId);
    	try {
			if (trainingLocationId == 0) {
				record = db.insertRecord(record);	
			} else {
				db.updateRecord(record);
			}
    		return record.getId();
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
    }
    
    public void deleteTrainingCourseTrainingLocation(int trainingCourseTrainingLocationId) {
    	try {
			db.deleteRecord(TrainingCourseTrainingLocationRecord.class, trainingCourseTrainingLocationId);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}
    public List<TrainingCourseTrainingLocation> getTrainingCourseTrainingLocations(int trainingCourseId) {
    	try {
			List<TrainingCourseTrainingLocationRecord> records = db.getTrainingCourseTrainingLocationRecords(trainingCourseId);
			if (records.size() == 0) {
				return null;
			}
			List<TrainingCourseTrainingLocation> result = new ArrayList<TrainingCourseTrainingLocation>();
			for (TrainingCourseTrainingLocationRecord r:records) {
				result.add(new TrainingCourseTrainingLocationImpl(nacidDataProvider, r));
			}
    		return result; 
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
    }

    @Override
    public void updateRecognizedDetails(int trainingCourseId, Integer recognizedEduLevelId, Integer recognizedQualificationId, Integer recognizedProfGroupId, List<Integer> recognizedSpecialities) {
        try {
            db.updateRecognizedDetails(trainingCourseId, recognizedEduLevelId, recognizedQualificationId, recognizedProfGroupId, recognizedSpecialities);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public static void main(String[] args) {
    	/*ApplicationsDataProvider applicationsDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource()).getApplicationsDataProvider();
    	Application application = applicationsDataProvider.getApplication(43);
    	TrainingCourse tc = application.getTrainingCourse();
    	System.out.println(tc.isRecognizedByHeadQuarter());
    	
    	TrainingCourseDataProviderImpl trainingCourseDataProvider = (TrainingCourseDataProviderImpl)NacidDataProvider.getNacidDataProvider(new StandAloneDataSource()).getTrainingCourseDataProvider();
    	//System.out.println(trainingCourseDataProvider.saver);
    	*/
        List<Integer> mylist =  new ArrayList<Integer>();
        mylist.addAll(Arrays.asList(1,2,null,5,null));
        mylist.removeAll(Arrays.asList((Integer)null));
        System.out.println(mylist);
    }
	
	
    
    
   
} 
