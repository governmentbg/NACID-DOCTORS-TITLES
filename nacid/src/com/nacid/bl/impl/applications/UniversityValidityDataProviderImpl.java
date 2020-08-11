package com.nacid.bl.impl.applications;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.nacid.bl.applications.UniversityValidity;
import com.nacid.bl.applications.UniversityValidityDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.data.applications.AvailableTrainingFormsRecord;
import com.nacid.data.applications.SourceOfInformationRecord;
import com.nacid.data.applications.UniversityValidityRecord;
import com.nacid.db.applications.UniversityValidityDB;
import com.nacid.web.exceptions.HandlerException;

public class UniversityValidityDataProviderImpl implements UniversityValidityDataProvider {

	UniversityValidityDB db;
	NacidDataProviderImpl nacidDataProvider;
	
	public UniversityValidityDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
		this.db = new UniversityValidityDB(nacidDataProvider.getDataSource());
		this.nacidDataProvider = nacidDataProvider;
	}

	@Override
	public void deleteUniversityValidity(int id) {
	    throw new HandlerException("deleteUniversityValidity: Method not supported.");
		/*try {
			db.deleteRecord(UniversityValidityRecord.class, id);
		} catch (SQLException e) {
			throw Utils.logException(this, e);
		}*/
	}

	@Override
	public List<UniversityValidity> getUniversityValiditiesByUniversity(int universityId) {
		try {
			List<UniversityValidity> ret = new ArrayList<UniversityValidity>();
			List<UniversityValidityRecord> recs = db.selectRecords(UniversityValidityRecord.class, "university_id=?", universityId);
			for(UniversityValidityRecord r : recs) {
				ret.add(new UniversityValidityImpl(r, nacidDataProvider));
			}
			return ret;
		} catch (SQLException e) {
			throw Utils.logException(this, e);
		}
	}

	@Override
	public UniversityValidity getUniversityValidity(int id) {
		try {
			UniversityValidityRecord rec = new UniversityValidityRecord();
			rec = db.selectRecord(rec, id);
			if(rec == null) {
				return null;
			}
			UniversityValidityImpl ret = new UniversityValidityImpl(rec, nacidDataProvider);
			prefillSourcesOfInformation(ret);		
			prefillTrainingForms(ret);
			return ret;
		} catch (SQLException e) {
			throw Utils.logException(this, e);
		}
	}
	
	private void prefillTrainingForms(UniversityValidityImpl univVal) throws SQLException {
	    if(univVal == null) {
            return;
        }
	    
	    List<AvailableTrainingFormsRecord> forms = db.selectRecords(
	            AvailableTrainingFormsRecord.class, "university_validity_id=?", univVal.getId());
	    
	    Object[] elements = null;
	    if (forms != null && forms.size() > 0) {
	        elements = new Object[forms.size()];
            for (int i = 0; i < elements.length; i++) {
                if(forms.get(i).getTrainingFormId() != null) {
                    elements[i] = forms.get(i).getTrainingFormId();
                }
                else {
                    elements[i] = forms.get(i).getNotes();
                }
            }
        }
	    univVal.setTrainingForms(elements);
	}
	
	private void prefillSourcesOfInformation(UniversityValidityImpl ret) throws SQLException {
	    if(ret == null) {
            return;
        }
	    List<SourceOfInformationRecord> infoSrc = db.selectRecords(
	            SourceOfInformationRecord.class, "university_validity_id=?", ret.getId());
	    int[] elements = null;
	    if(infoSrc != null && infoSrc.size() > 0) {
	        elements = new int[infoSrc.size()];
	        for(int i = 0; i < elements.length; i++) {
	            elements[i] = infoSrc.get(i).getCompetentInstitutionId();
	        }
	    }
	    ret.setSourcesOfInformation(elements);
	}
	
	@Override
	public int saveUniversityValidity(int id, Integer universityId, Integer userId, Date examinationDate, boolean isComunicated,
			boolean isRecognized, String notes, Integer trainingLocationId, boolean hasJoinedDegrees, 
			List<Object> availableTrainingForms, List<Integer> sourcesOfInfo) {
		
		UniversityValidityRecord rec = new UniversityValidityRecord(id, universityId, userId, examinationDate, isComunicated ? 1 :0,
				isRecognized ? 1 : 0, notes, trainingLocationId, hasJoinedDegrees ? 1 : 0);
		try {
			if (id == 0) {
				rec = db.insertRecord(rec);
			}
			else {
				db.updateRecord(rec);
			}
			
			if(id != 0) {
                db.deleteRecords(AvailableTrainingFormsRecord.class, 
                        "university_validity_id=?", rec.getId());
                db.deleteRecords(SourceOfInformationRecord.class, 
                        "university_validity_id=?", rec.getId());
            }
			
			if (availableTrainingForms != null) {
                for (Object atf : availableTrainingForms) {
                    Integer trainingFormId = null;
                    String notesStr = null;
                    if (atf instanceof Integer) {
                        trainingFormId = (Integer) atf;
                    } else if (atf instanceof String) {
                        notesStr = (String) atf;
                    }
                    AvailableTrainingFormsRecord atfRec = new AvailableTrainingFormsRecord(0, rec.getId(), trainingFormId, notesStr);
                    db.insertRecord(atfRec);
                }
            }
			
			if (sourcesOfInfo != null) {
                for (int i : sourcesOfInfo) {
                    SourceOfInformationRecord siRec = new SourceOfInformationRecord(0, i, rec.getId());
                    db.insertRecord(siRec);
                }
            }
			
			return rec.getId();
		} catch (SQLException e) {
			throw Utils.logException(this, e);
		}
	}
}
