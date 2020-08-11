package com.nacid.bl.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.DiplomaType;
import com.nacid.bl.applications.DiplomaTypeDataProvider;
import com.nacid.bl.applications.DiplomaTypeEditException;
import com.nacid.bl.applications.DiplomaTypeIssuer;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.data.applications.DiplomaTypeIssuerRecord;
import com.nacid.data.applications.DiplomaTypeRecord;
import com.nacid.data.applications.DiplomaTypeRecordForList;
import com.nacid.data.applications.TrainingCourseRecord;
import com.nacid.db.applications.DiplomaTypeDB;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.web.MessagesBundle;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class DiplomaTypeDataProviderImpl implements DiplomaTypeDataProvider {

	private DiplomaTypeDB db;
	private NacidDataProviderImpl nacidDataProvider;
	public DiplomaTypeDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
		this.db = new DiplomaTypeDB(nacidDataProvider.getDataSource());
		this.nacidDataProvider = nacidDataProvider;
	}
	
	@Override
	public void disableDiplomaType(int id) {
		try {
			db.setEndDateToToday(id);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	@Override
	public List<DiplomaTypeRecordForList> getAllDiplomaTypes(Integer type) {
		
		try {

            return db.getAllDiplomaTypes(type);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}
	public List<DiplomaType> getDiplomaTypes(Collection<Integer> universityIds, Integer type) {
	    try {
            if (universityIds != null && universityIds.size() == 0) {
            	return null;
            }
	    	List<DiplomaType> ret = new ArrayList<DiplomaType>();
            List<DiplomaTypeIssuerRecord> issuerRecords = db.getDiplomaTypeIssuerRecords(universityIds);
            if (issuerRecords.size() == 0) {
                return null;
            }

            for(DiplomaTypeIssuerRecord dtir : issuerRecords) {
				DiplomaTypeRecord dt = db.selectRecord(new DiplomaTypeRecord(), dtir.getDiplomaTypeId());
				if (type == null || type == dt.getType()) {
					ret.add(new DiplomaTypeImpl(nacidDataProvider, dt));
				}
            }
            return ret;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
	}
	public List<DiplomaTypeIssuer> getDiplomaTypeIssuers(int diplomaTypeId) {
		try {
			List<DiplomaTypeIssuerRecord> records = db.getDiplomaTypeIssuerRecords(diplomaTypeId);
			if (records.size() == 0) {
				return null;
			}
			List<DiplomaTypeIssuer> result = new ArrayList<DiplomaTypeIssuer>();
			for (DiplomaTypeIssuerRecord r:records) {
				result.add(new DiplomaTypeIssuerImpl(nacidDataProvider, r));
			}
			return result;
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}
	@Override
	public DiplomaType getDiplomaType(int id) {
		try {
			
			DiplomaTypeRecord rec = new DiplomaTypeRecord();
			db.selectRecord(rec, id);

			DiplomaType ret = new DiplomaTypeImpl(nacidDataProvider, rec); 
			
			return ret;
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	@Override
	public int saveDiplomaType(int id, Set<UniversityIdWithFacultyId> universityWithFaculty, String visualElementsDescr, String protectionElementsDescr, String numberFormatDescr,
							   String notes, Date dateFrom, Date dateTo, String title, Integer eduLevelId, Integer originalEduLevelId, boolean jointDegree,
							   Integer bolognaCycleId, Integer nationalQualificationsFrameworkId, Integer europeanQualificationsFrameworkId,
							   Integer bolognaCycleAccessId, Integer nationalQualificationsFrameworkAccessId, Integer europeanQualificationsFrameworkAccessId,
							   int type
	) throws DiplomaTypeEditException {
		
		if (id != 0 && type == DiplomaType.TYPE_NORMAL) {
			DiplomaType dt = getDiplomaType(id);
			/**
			 * ako se editva star zapis i se opitva da se promeni eduLevelId, universityId ili title - togava se proverqva dali ima ve4e vyrzan trainingCourse
			 * s tova diplomaTypeId. Ako da, se hvyrlq exception s text udachen za printirane na potrebitelq  
			 */
			Set<UniversityIdWithFacultyId> oldDiplomaTypeUniversityIds = dt.getUniversityWithFacultyIds();
			boolean isEduLevelChanged = !Objects.equals(dt.getEduLevelId(), eduLevelId);
			//is university changed proverqva samo dali universitetite sa promenenie. Fakultetite mogat da se promenqt bez tova da se otrazqva na vida diploma!
			boolean isUniversityChanged = isUniversityChanged(oldDiplomaTypeUniversityIds, universityWithFaculty);
			if (isEduLevelChanged || (isUniversityChanged) || !dt.getTitle().equals(title)
                    /*|| (!DataConverter.equalIntegers(dt.getOriginalEducationLevelId(), originalEduLevelId))*/) {
				TrainingCourseDataProviderImpl trainingCourseDataProviderImpl = nacidDataProvider.getTrainingCourseDataProvider();
				List<TrainingCourseRecord> trainingCourseRecords = trainingCourseDataProviderImpl.getTrainingCourseRecords(id);
				if (trainingCourseRecords.size() != 0) {
					DiplomaTypeEditException exception = new DiplomaTypeEditException();
					if (isEduLevelChanged) {
						FlatNomenclature newEducationLevel = nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL, eduLevelId);
						exception.addMessage(MessageFormat.format(MessagesBundle.getMessagesBundle().getValue("existing_training_course_same_diploma_type_id_edu_level"), dt.getEducationLevel() == null ? "" : dt.getEducationLevel().getName(), newEducationLevel.getName()));
					} 
					if (isUniversityChanged){
						exception.addMessage(MessagesBundle.getMessagesBundle().getValue("existing_training_course_same_diploma_type_id_university"));
					} 
					if (!dt.getTitle().equals(title)){
						exception.addMessage(MessageFormat.format(MessagesBundle.getMessagesBundle().getValue("existing_training_course_same_diploma_type_id_title"), dt.getTitle(), title));
					}
                    if (!DataConverter.equalIntegers(dt.getOriginalEducationLevelId(), originalEduLevelId)) {
                        exception.addMessage(MessageFormat.format(MessagesBundle.getMessagesBundle().getValue("existing_training_course_same_diploma_type_id_original_edu_level"), dt.getOriginalEducationLevelId(), originalEduLevelId));
                    }

					throw exception;
				}	
			} 
			
		}
		
		java.sql.Date dateFromSql = null;
		if(dateFrom != null) {
			dateFromSql = new java.sql.Date(dateFrom.getTime());
		} else if(id == 0) {
			dateFromSql = new java.sql.Date(new Date().getTime());
		}
		
		java.sql.Date dateToSql = null;
		if(dateTo != null) {
			dateToSql = new java.sql.Date(dateTo.getTime());
		}
		
		
		DiplomaTypeRecord ur = new DiplomaTypeRecord(id, visualElementsDescr, protectionElementsDescr, numberFormatDescr,
				notes, dateFromSql, dateToSql, title, eduLevelId, originalEduLevelId, jointDegree ? 1 : 0,
                bolognaCycleId, nationalQualificationsFrameworkId, europeanQualificationsFrameworkId,
                bolognaCycleAccessId, nationalQualificationsFrameworkAccessId, europeanQualificationsFrameworkAccessId,
				type);
		
		try {
			if (id == 0) {
				id = db.insertRecord(ur).getId();
			} else {
				db.updateRecord(ur);
			}
			db.deleteDiplomaTypeIssuerRecords(id);
			if (universityWithFaculty != null && universityWithFaculty.size() > 0) {
				int i = 1;
				for (UniversityIdWithFacultyId e: universityWithFaculty) {
					db.insertRecord(new DiplomaTypeIssuerRecord(0, id, e.getUniversityId(), i++, e.getFacultyId()));
				}
			}
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
		
		return id;
	}


	private boolean isUniversityChanged(Set<UniversityIdWithFacultyId> original, Set<UniversityIdWithFacultyId> changed) {

        return !Objects.equals(original, changed);//otkazah se ot dolnata proverka - ako se update-ne vida diploma i se vyvedat novi faculties, v bazata danni ima oshte edna tablica diploma_issuer, koqto shte stane nekonsistentna!!!!
        /**
         * proverqva dali universitetite v diplomaType-a sa promeneni. Promqna ima ako ima razlika v universitetite ili ako v daden original universitet fakulteta ne e null, a v changed e razlichen ot stariq
         * @param original
         * @param changed
         * @return
         */
		/*if (original == null) {
			return changed != null;
		}
		//ako ima razlika v universitetite se vry6ta che ima promqna
		if (!Objects.equals(original.stream().map(r -> r.getUniversityId()).collect(Collectors.toList()), changed.stream().map(r -> r.getUniversityId()).collect(Collectors.toList()))) {
			return true;
		}
		//grupira promenenite universiteti po facultyId
		Map<Integer, Set<Integer>> changedGrouped = changed.stream().collect(Collectors.groupingBy(r -> r.getUniversityId(), Collectors.mapping(r -> r.getFacultyId(), Collectors.toSet())));

		return original.stream().anyMatch(ou -> ou.getFacultyId() != null && !changedGrouped.get(ou.getUniversityId()).contains(ou.getFacultyId()));*/
	}
	
	public List<DiplomaType> changeDiplomaTypeDateToByUniversity(int universityId, Date dateTo) {
		if (dateTo == null) {
			return null;
		}
		try {
			List<DiplomaType> result = new ArrayList<DiplomaType>();
			List<DiplomaTypeRecord> diplomaTypeRecords = db.getDiplomaTypeRecords(Arrays.asList(universityId));
			if (diplomaTypeRecords != null) {
				
				for (DiplomaTypeRecord r : diplomaTypeRecords) {
					if (r.getDateTo() == null || r.getDateTo().getTime() > dateTo.getTime()) {
						r.setDateTo(dateTo == null ? null : new java.sql.Date(dateTo.getTime()));
						db.updateRecord(r);
						result.add(new DiplomaTypeImpl(nacidDataProvider, r));
					}
				}
			}
			return result.size() == 0 ? null : result;
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}
	public static void main(String[] args) {
	    DiplomaTypeDataProvider diplomaTypeDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource()).getDiplomaTypeDataProvider();
	    //diplomaTypeDataProvider.getAllDiplomaTypes();
	    //System.out.println("end...");
        //System.out.println(new Integer(5).equals(null));
    }


	

}
