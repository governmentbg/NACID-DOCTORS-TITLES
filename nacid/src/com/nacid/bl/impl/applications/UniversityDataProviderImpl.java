package com.nacid.bl.impl.applications;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.nacid.bl.applications.University;
import com.nacid.bl.applications.UniversityDataProvider;
import com.nacid.bl.applications.UniversityFaculty;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.data.applications.UniversityFacultyRecord;
import com.nacid.data.applications.UniversityRecord;
import com.nacid.db.applications.UniversityDB;

public class UniversityDataProviderImpl implements UniversityDataProvider {

	private UniversityDB db;
	private NacidDataProviderImpl nacidDataProvider;
	public UniversityDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
		this.db = new UniversityDB(nacidDataProvider.getDataSource());
		this.nacidDataProvider = nacidDataProvider;
	}
	
	@Override
	public void disableUniversity(int id) {
		try {
			db.setEndDateToToday(id);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	@Override
	public List<University> getAllUniversities() {
		
		try {
			List<University> ret = new ArrayList<University>();
			
			List<UniversityRecord> recs = db.selectRecords(UniversityRecord.class, null);
		
			for(UniversityRecord ur : recs) {
				ret.add(new UniversityImpl(nacidDataProvider, ur));
			}
			
			return ret;
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}
	@Override
    public List<University> getUniversities(int countryId, boolean onlyActive) {
        
        try {
            List<UniversityRecord> recs = db.getUniversityRecords(countryId, onlyActive ? Utils.getSqlDate(new Date()) : null);
            if (recs.size() == 0) {
                return null;
            }
            List<University> ret = new ArrayList<University>();
            for(UniversityRecord ur : recs) {
                ret.add(new UniversityImpl(nacidDataProvider, ur));
            }
            return ret;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

	@Override
	public University getUniversity(int id) {
		try {
			
			UniversityRecord rec = new UniversityRecord();
			db.selectRecord(rec, id);

			University ret = new UniversityImpl(nacidDataProvider, rec); 
			
			return ret;
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	@Override
	public int saveUniversity(int id, int countryId, String bgName, String orgName, String city, String addrDetails, String phone, String fax,
			String email, String webSite, String urlDiplomaRegister, Date dateFrom, Date dateTo, Integer universityGenericNameId) {
		
		java.sql.Date dateFromSql = null;
		if(dateFrom != null) {
			dateFromSql = new java.sql.Date(dateFrom.getTime());
		}
		else if(id == 0) {
			dateFromSql = new java.sql.Date(new Date().getTime());
		}
		
		java.sql.Date dateToSql = null;
		if(dateTo != null) {
			dateToSql = new java.sql.Date(dateTo.getTime());
		}
		
		
		UniversityRecord ur = new UniversityRecord(id, countryId, bgName, orgName, city, addrDetails, phone, fax,
				email, webSite, urlDiplomaRegister, dateFromSql, dateToSql, universityGenericNameId);
		
		try {
			if (id == 0) {
				id = db.insertRecord(ur).getId();
			} else {
				db.updateRecord(ur);
			}
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
		
		return id;
	}
	
	@Override
	public List<UniversityWithFaculty> getUniversityWithFaculties(int trainingCourseId) {
	  try {
	    /**
	     * za da vyrna zapisite, podredeni po ord_num ili trqbva da napisha zaqvka koqto da obedinqva 2te tablici
	     * ili po drug na4in da gi podredq
	     * az reshih da vyrna vsi4ki zapisi diplomaIssuer, podredeni po ord_num, sled koeto vry6tam vsi4ki universityRecords
	     * za dadeniq diplomaId, 
	     */

	    List<UniversityRecord> unis = db.getUniversityRecords(trainingCourseId);
	    List<UniversityFacultyRecord> facs = db.getUniversityFaculties(trainingCourseId);
	    List<UniversityWithFaculty> result = new ArrayList<>();
	    for (int i = 0; i < unis.size(); i++) {
			UniversityRecord ur = unis.get(i);
			UniversityFacultyRecord fr = facs.get(i);
			result.add(new UniversityWithFaculty(new UniversityImpl(nacidDataProvider, ur), fr));
		}
	    return result;
	  } catch (SQLException e) {
	    throw Utils.logException(e);
	  }
  }
	public List<Integer> getUniversityIds(int trainingCourseId) {
		try {
		    List<UniversityRecord> recs = db.getUniversityRecords(trainingCourseId);
		    if (recs.size() == 0) {
		      return null;
		    }
		    List<Integer> result = new ArrayList<Integer>();
		    for(UniversityRecord ur : recs) {
		      result.add(ur.getId());
		    }
		    return result;
		  } catch (SQLException e) {
		    throw Utils.logException(e);
		  }
	}

  @Override
  public List<University> getUniversities(Integer countryId, int nameType, boolean startsWith, String partOfName) {
    if (partOfName == null || "".equals(partOfName)) {
      return null;
    }
    try {
      List<UniversityRecord> records = db.getUniversityRecords(countryId, nameType, startsWith, partOfName);
      if (records.size() == 0) {
        return null;
      }
      List<University> result = new ArrayList<University>();
      for (UniversityRecord ur:records) {
        result.add(new UniversityImpl(nacidDataProvider, ur));
      }
      return result;
    } catch (SQLException e) {
      throw Utils.logException(e);
    }
  }
  @Override
  public List<Integer> getUniversityIds(Integer countryId, int nameType, boolean startsWith, String partOfName) {
    if (partOfName == null || "".equals(partOfName)) {
      return null;
    }
    try {
      List<UniversityRecord> records = db.getUniversityRecords(countryId, nameType, startsWith, partOfName);
      if (records.size() == 0) {
        return null;
      }
      List<Integer> result = new ArrayList<Integer>();
      for (UniversityRecord ur:records) {
        result.add(ur.getId());
      }
      return result;
    } catch (SQLException e) {
      throw Utils.logException(e);
    }
  }

	@Override
	public List<UniversityFaculty> getUniversityFaculties(int universityId, String partOfName, boolean onlyActive) {
		try {
			return db.getUniversityFaculties(universityId, partOfName, onlyActive ? Utils.getSqlDate(new Date()) : null).stream().map(r -> (UniversityFaculty)r).collect(Collectors.toList());
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	@Override
	public UniversityFaculty getUniversityFaculty(int id) {
		try {
			return db.selectRecord(UniversityFacultyRecord.class, "id = ?", id);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	@Override
	public int saveUniveristyFaculty(int id, int universityId, String name, String originalName, Date dateFrom, Date dateTo) {
		try {
			UniversityFacultyRecord uf = new UniversityFacultyRecord(id, universityId, name, originalName, dateFrom, dateTo);
			if (uf.getId() == 0) {
				uf = db.insertRecord(uf);
			} else {
				db.updateRecord(uf);
			}
			return uf.getId();
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}
}
