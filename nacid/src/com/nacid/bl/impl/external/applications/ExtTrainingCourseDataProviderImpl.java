package com.nacid.bl.impl.external.applications;

import com.nacid.bl.external.applications.*;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.nomenclatures.SpecialityImpl;
import com.nacid.bl.nomenclatures.Speciality;
import com.nacid.data.external.applications.*;
import com.nacid.data.nomenclatures.SpecialityRecord;
import com.nacid.db.external.applications.ExtTrainingCourseDB;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExtTrainingCourseDataProviderImpl implements ExtTrainingCourseDataProvider {

    private ExtTrainingCourseDB db;
    private NacidDataProviderImpl nacidDataProvider;
    public ExtTrainingCourseDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
        db = new ExtTrainingCourseDB(nacidDataProvider.getDataSource());
        this.nacidDataProvider = nacidDataProvider;
    }
    
    @Override
    public ExtTrainingCourse getExtTrainingCourse(int id) {
        try {
            ExtTrainingCourseRecord rec = getExtTrainingCourseRecord(id);
            if(rec != null) {
                List<ExtDiplomaIssuerRecord> recs = db.getDiplomaIssuers(rec.getId());
                List<ExtTrainingCourseSpecialityRecord> trainingCourseSpecialities = db.getExtTrainingCourseSpecialities(rec.getId());
            	return new ExtTrainingCourseImpl(rec, recs.size() == 0 ? null : recs, trainingCourseSpecialities.isEmpty() ? null : trainingCourseSpecialities, nacidDataProvider);
            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        return null;
    }

    @Override
    public int saveExtTrainingCourse(int id, String diplomaSeries, String diplomaNum, String diplomaRegistrationNumber, Date diplomaDate, /*Integer universityId, String universityTxt,*/ String fname,
            String sname, String lname, /*Integer trainingLocationCountryId, String trainingLocationCity,*/ boolean jointDegree, Date trainingStart, Date trainingEnd,
            Double trainingDuration, Integer durationUnitId, BigDecimal credits, /*Integer specialityId, String specialityTxt,*/ Integer eduLevelId,
            Integer qualificationId, String qualificationTxt, Integer schoolCountry, String schoolCity, String schoolName, Date schoolGraduationDate,
            String schoolNotes, Integer prevDiplomaCountry, String prevDiplomaCity, Integer prevDiplomaUniversityId,
            String prevDiplomaUiniversityTxt, Integer prevDiplomaEduLevelId, Date prevDiplomaGraduationDate, String prevDiplomaNotes,
            Integer prevDiplomaSpecialityId, String prevDiplomaSpecialityTxt, Integer trainingInstId, String trainingInstTxt, Integer graduationDocumentTypeId, Integer creditHours, Integer ectsCredits,
            int ownerId, String thesisTopic, String thesisTopicEn, Integer profGroupId, Date thesisDefenceDate,
                                     Integer thesisBibliography, Integer thesisVolume, Integer thesisLanguage, String thesisAnnotation, String thesisAnnotationEn) {
        
        try {
            ExtTrainingCourseRecord rec = new ExtTrainingCourseRecord(id, diplomaSeries, diplomaNum, diplomaRegistrationNumber,
                    Utils.getSqlDate(diplomaDate), /*universityId, universityTxt,*/ fname, sname, lname,
                    /*trainingLocationCountryId, trainingLocationCity,*/ jointDegree ? 1 : 0, Utils.getSqlDate(trainingStart),
                    Utils.getSqlDate(trainingEnd), 
                    trainingDuration, durationUnitId, credits,
                    /*specialityId, specialityTxt,*/ eduLevelId, qualificationId, 
                    qualificationTxt, schoolCountry, schoolCity, schoolName,
                    Utils.getSqlDate(schoolGraduationDate), schoolNotes, prevDiplomaCountry, prevDiplomaCity, prevDiplomaUniversityId, prevDiplomaUiniversityTxt,
                    prevDiplomaEduLevelId, Utils.getSqlDate(prevDiplomaGraduationDate), 
                    prevDiplomaNotes, prevDiplomaSpecialityId, prevDiplomaSpecialityTxt, trainingInstId, trainingInstTxt, graduationDocumentTypeId, creditHours,
                    ectsCredits, ownerId, thesisTopic, thesisTopicEn, profGroupId, Utils.getSqlDate(thesisDefenceDate), thesisBibliography, thesisVolume, thesisLanguage, thesisAnnotation, thesisAnnotationEn);
            if(id == 0) {
                rec = db.insertRecord(rec);
            }
            else {
                db.updateRecord(rec);
            }
            return rec.getId();
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

	public int saveTrainingCourse(int courseId, String firstName, String middleName, String lastName, int ownerId) {
		try {
			ExtTrainingCourseRecord oldRec = courseId == 0 ? null : getExtTrainingCourseRecord(courseId);
			ExtTrainingCourseRecord rec = new ExtTrainingCourseRecord(courseId, 
                    oldRec == null ? null : oldRec.getDiplomaSeries(),
					oldRec == null ? null : oldRec.getDiplomaNum(),
                    oldRec == null ? null : oldRec.getDiplomaRegistrationNumber(),
                    oldRec == null ? null : oldRec.getDiplomaDate(),
					//oldRec == null ? null : oldRec.getUniversityId(), 
					//oldRec == null ? null : oldRec.getUniversityTxt(),
					firstName, middleName, lastName,
					oldRec == null ? 0 : oldRec.getIsJointDegree(), 
					//oldRec == null ? null : oldRec.getTrainingLocationCountryId(), 
					//oldRec == null ? null : oldRec.getTrainingLocationCity(), 
					oldRec == null ? null : oldRec.getTrainingStart(), 
					oldRec == null ? null : oldRec.getTrainingEnd(), 
					oldRec == null ? null : oldRec.getTrainingDuration(), 
					oldRec == null ? null : oldRec.getDurationUnitId(), 
					oldRec == null ? null : oldRec.getCredits(),
					//oldRec == null ? null : oldRec.getSpecialityId(), 
					//oldRec == null ? null : oldRec.getSpecialityTxt(),
					oldRec == null ? null : oldRec.getEduLevelId(), 
					oldRec == null ? null : oldRec.getQualificationId(), 
					oldRec == null ? null : oldRec.getQualificationTxt(), 
					oldRec == null ? null : oldRec.getSchoolCountry(),
					oldRec == null ? null : oldRec.getSchoolCity(), 
					oldRec == null ? null : oldRec.getSchoolName(),
					oldRec == null ? null : oldRec.getSchoolGraduationDate(), 
					oldRec == null ? null : oldRec.getSchoolNotes(), 
					oldRec == null ? null : oldRec.getPrevDiplomaCountry(),
					oldRec == null ? null : oldRec.getPrevDiplomaCity(), 
					oldRec == null ? null : oldRec.getPrevDiplomaUniversityId(), 
					oldRec == null ? null : oldRec.getPrevDiplomaUiniversityTxt(),
					oldRec == null ? null : oldRec.getPrevDiplomaEduLevelId(), 
					oldRec == null ? null : oldRec.getPrevDiplomaGraduationDate(), 
					oldRec == null ? null : oldRec.getPrevDiplomaNotes(), 
					oldRec == null ? null : oldRec.getPrevDiplomaSpecialityId(),
					oldRec == null ? null : oldRec.getPrevDiplomaSpecialityTxt(),
					oldRec == null ? null : oldRec.getTrainingInstId(), 
					oldRec == null ? null : oldRec.getTrainingInstTxt(),
                    oldRec == null ? null : oldRec.getGraduationDocumentTypeId(),
                    oldRec == null ? null : oldRec.getCreditHours(),
                    oldRec == null ? null : oldRec.getEctsCredits(),
                    ownerId,
                    oldRec == null ? null : oldRec.getThesisTopic(),
                    oldRec == null ? null : oldRec.getThesisTopicEn(),
                    oldRec == null ? null : oldRec.getProfGroupId(),
                    oldRec == null ? null : oldRec.getThesisDefenceDate(),
                    oldRec == null ? null : oldRec.getThesisBibliography(),
                    oldRec == null ? null : oldRec.getThesisVolume(),
                    oldRec == null ? null : oldRec.getThesisLanguageId(),
                    oldRec == null ? null : oldRec.getThesisAnnotation(),
                    oldRec == null ? null : oldRec.getThesisAnnotationEn()
            );
			if (courseId == 0) {
				rec = db.insertRecord(rec);
			} else {
				db.updateRecord(rec);
			}
			return rec.getId();
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

    @Override
    public List<ExtPurposeOfRecognition> getExtPurposesOfRecognition(int applicationId) {
        List<ExtPurposeOfRecognition> ret = new ArrayList<ExtPurposeOfRecognition>();
        try {
            List<ExtPurposeOfRecognitionRecord> recs = getExtPurposesOfRecognitionRecords(applicationId);
            if(recs != null) {
                for(ExtPurposeOfRecognitionRecord rec : recs) {
                    ret.add(new ExtPurposeOfRecognitionImpl(rec, nacidDataProvider));
                }
            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        return ret;
    }
    //Tozi method sy6testvuva samo v implementaciqta na ExtTrainingCourseDataProviderImpl (t.e. ne trqbva da se  vijda v interface-a) i se polzva za generirane na xml-a s informaciq za dadeno zaqvlenie
    public  List<ExtPurposeOfRecognitionRecord> getExtPurposesOfRecognitionRecords(int applicationId) throws SQLException {
    	return db.selectRecords(ExtPurposeOfRecognitionRecord.class, "application_id=?", applicationId);
    }

    @Override
    public void savePurposesOfrecognition(List<Integer> purposesIds, String note, int applicationId) {
        try {
            db.deleteRecords(ExtPurposeOfRecognitionRecord.class, "application_id=?", applicationId);
            if (purposesIds != null) {
                for (Integer i : purposesIds) {
                    ExtPurposeOfRecognitionRecord por = new ExtPurposeOfRecognitionRecord(0, applicationId, i, null);
                    db.insertRecord(por);
                }
            }
            if(note != null) {
                ExtPurposeOfRecognitionRecord por = new ExtPurposeOfRecognitionRecord(0, applicationId, null, note);
                db.insertRecord(por);
            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public List<ExtGraduationWay> getExtGraduationWays(int trainingCourseId) {
        List<ExtGraduationWay> ret = new ArrayList<ExtGraduationWay>();
        try {
            List<ExtGraduationWayRecord> recs = getExtGraduationWayRecords(trainingCourseId);
            if(recs != null) {
                for(ExtGraduationWayRecord rec : recs) {
                    ret.add(new ExtGraduationWayImpl(rec, nacidDataProvider));
                }
            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        return ret;
    }
    //Tozi method sy6testvuva samo v implementaciqta na ExtTrainingCourseDataProviderImpl (t.e. ne trqbva da se  vijda v interface-a) i se polzva za generirane na xml-a s informaciq za dadeno zaqvlenie
    public List<ExtGraduationWayRecord> getExtGraduationWayRecords(int trainingCourseId) throws SQLException {
    	return db.selectRecords(ExtGraduationWayRecord.class, "training_course_id=?", trainingCourseId);
    }

    @Override
    public void saveGraduationWays(List<Integer> graduationWaysIds, String note, int trainingCourseId) {
        try {
            db.deleteRecords(ExtGraduationWayRecord.class, "training_course_id=?", trainingCourseId);
            if (graduationWaysIds != null) {
                for (Integer i : graduationWaysIds) {
                    ExtGraduationWayRecord por = new ExtGraduationWayRecord(0, trainingCourseId, i, null);
                    db.insertRecord(por);
                }
            }
            if(note != null) {
                ExtGraduationWayRecord por = new ExtGraduationWayRecord(0, trainingCourseId, null, note);
                db.insertRecord(por);
            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public List<ExtTrainingForm> getExtTrainingForms(int trainingCourseId) {
        List<ExtTrainingForm> ret = new ArrayList<ExtTrainingForm>();
        try {
            List<ExtTrainingFormRecord> recs = getExtTrainingFormRecords(trainingCourseId);
            if(recs != null) {
                for(ExtTrainingFormRecord rec : recs) {
                    ret.add(new ExtTrainingFormImpl(rec, nacidDataProvider));
                }
            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        return ret;
    }
    //Tozi method sy6testvuva samo v implementaciqta na ExtTrainingCourseDataProviderImpl (t.e. ne trqbva da se  vijda v interface-a) i se polzva za generirane na xml-a s informaciq za dadeno zaqvlenie
    public List<ExtTrainingFormRecord> getExtTrainingFormRecords(int trainingCourseId) throws SQLException {
    	return db.selectRecords(ExtTrainingFormRecord.class, "training_course_id=?", trainingCourseId);
    }

    @Override
    public void saveExtTrainingForms(List<Integer> trainingFormIds, String note, int trainingCourseId) {
        try {
            db.deleteRecords(ExtTrainingFormRecord.class, "training_course_id=?", trainingCourseId);
            if (trainingFormIds != null) {
                for (Integer i : trainingFormIds) {
                    ExtTrainingFormRecord por = new ExtTrainingFormRecord(0, trainingCourseId, i, null);
                    db.insertRecord(por);
                }
            }
            if(note != null) {
                ExtTrainingFormRecord por = new ExtTrainingFormRecord(0, trainingCourseId, null, note);
                db.insertRecord(por);
            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    //Tozi method sy6testvuva samo v implementaciqta na ExtTrainingCourseDataProviderImpl (t.e. ne trqbva da se  vijda v interface-a) i se polzva za generirane na xml-a s informaciq za dadeno zaqvlenie
    public ExtTrainingCourseRecord getExtTrainingCourseRecord(int recordId) throws SQLException {
    	return db.selectRecord(new ExtTrainingCourseRecord(), recordId);
    }
    
    
    
    public void deleteTrainingCourseTrainingLocations(int trainingCourseId) {
    	try {
			db.deleteTrainingCourseTrainingLocationRecords(trainingCourseId);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
    }
    public int addTrainingCourseTrainingLocation(int trainingCourseId, int trainigLocationTrainingCountryId, String trainingLocationTrainingCity) {
    	ExtTrainingCourseTrainingLocationRecord record = new ExtTrainingCourseTrainingLocationRecord(0, trainingCourseId, trainigLocationTrainingCountryId, trainingLocationTrainingCity);
    	try {
			record = db.insertRecord(record);
			return record.getId();
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
    }
    public List<ExtTrainingCourseTrainingLocation> getTrainingCourseTrainingLocations(int trainingCourseId) {
    	try {
			List<ExtTrainingCourseTrainingLocationRecord> records = db.getTrainingCourseTrainingLocationRecords(trainingCourseId);
			if (records.size() == 0) {
				return null;
			}
			List<ExtTrainingCourseTrainingLocation> result = new ArrayList<ExtTrainingCourseTrainingLocation>();
			for (ExtTrainingCourseTrainingLocationRecord r:records) {
				result.add(new ExtTrainingCourseTrainingLocationImpl(nacidDataProvider, r));
			}
    		return result; 
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
    }
    //Tozi method sy6testvuva samo v implementaciqta na ExtTrainingCourseDataProviderImpl (t.e. ne trqbva da se  vijda v interface-a) i se polzva za generirane na xml-a s informaciq za dadeno zaqvlenie
    public List<ExtTrainingCourseTrainingLocationRecord> getTrainingCourseTrainingLocationRecords(int trainingCourseId) throws SQLException  {
    	return db.getTrainingCourseTrainingLocationRecords(trainingCourseId);
    }
    
    

    public void updateTrainingCourseUniversities(int trainingCourseId, List<ExtUniversityIdWithFacultyId> universityWithFaculties) {
    	try {
			db.deleteDiplomaIssuerRecords(trainingCourseId);
			if (CollectionUtils.isEmpty(universityWithFaculties)) {
	    		return;
	    	}


			
			for (int i = 0; i < universityWithFaculties.size(); i++) {
                ExtUniversityIdWithFacultyId uf = universityWithFaculties.get(i);
			    ExtDiplomaIssuerRecord rec = new ExtDiplomaIssuerRecord(0, trainingCourseId, uf.getUniversityId(), uf.getUniversityTxt(), i + 1, uf.getFacultyId(), uf.getFacultyTxt());
				db.insertRecord(rec);
			}
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
    	
	}
    
    //Tozi method sy6testvuva samo v implementaciqta na ExtTrainingCourseDataProviderImpl (t.e. ne trqbva da se  vijda v interface-a) i se polzva za generirane na xml-a s informaciq za dadeno zaqvlenie
    public List<ExtDiplomaIssuerRecord> getDiplomaIssuerRecords(int trainingCourseId) throws SQLException {
    	return db.getDiplomaIssuers(trainingCourseId);
    }


    //vryshta samo specialnostite s id ot eservivices.rudi_training_course_specialities
    public List<Speciality> loadSpecialities(int trainingCourseId) {
        List<ExtTrainingCourseSpecialityRecord> records;
        List<Speciality> specialities = null;
        try {
            records = db.selectRecords(ExtTrainingCourseSpecialityRecord.class, " ext_training_course_id = ? ", trainingCourseId);
            if (records != null) {
                specialities = new ArrayList<Speciality>();
                for (ExtTrainingCourseSpecialityRecord record : records) {
                    if (record.getSpecialityId() != null) {
                        SpecialityRecord specialityRecord = db.selectRecord(new SpecialityRecord(), record.getSpecialityId());
                        Speciality speciality = new SpecialityImpl(specialityRecord);
                        specialities.add(speciality);
                    }
                }
            }          
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specialities;
    }
    
    //vryshta samo specialnostite s txt ot eservivices.rudi_training_course_specialities
    public List<ExtTrainingCourseSpecialityRecord> loadTxtSpecialities(int trainingCourseId) {
        List<ExtTrainingCourseSpecialityRecord> records;
        List<ExtTrainingCourseSpecialityRecord> specialities = new ArrayList<ExtTrainingCourseSpecialityRecord>();
        try {
            records = db.selectRecords(ExtTrainingCourseSpecialityRecord.class, " ext_training_course_id = ? ", trainingCourseId);
            if (records != null) {
                for (ExtTrainingCourseSpecialityRecord record : records) {
                    if (record.getSpecialityId() == null) {
                        specialities.add(record);
                    }
                }
            }          
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specialities.isEmpty() ? null : specialities;
    }
    
    public void saveSpeciality(ExtTrainingCourseSpecialityRecord record) {
        try {
            db.insertRecord(record);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteSpecialities(int trainingCourseId) {
        try {
            db.deleteRecords(ExtTrainingCourseSpecialityRecord.class, " ext_training_course_id = ? ", trainingCourseId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
