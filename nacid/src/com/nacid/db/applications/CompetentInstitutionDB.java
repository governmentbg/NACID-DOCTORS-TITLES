package com.nacid.db.applications;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.nacid.data.applications.CompetentInstitutionRecord;
import com.nacid.data.applications.SourceOfInformationRecord;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.SQLUtils;
import com.nacid.db.utils.StandAloneDataSource;


public class CompetentInstitutionDB extends DatabaseService {

	public CompetentInstitutionDB(DataSource ds) {
		super(ds);
	}
	public List<CompetentInstitutionRecord> getCompetentInstitutionRecords(Integer countryId, java.sql.Date toDate) throws SQLException{
        List<Object> objects = new ArrayList<Object>();
        String str = " 1 = 1 ";
        if (countryId != null) {
            str += " AND country_id = ?";
            objects.add(countryId);
        }
        if (toDate != null) {
            str += " AND (date_from <= ? OR date_from is null) ";
            str += " AND (date_to >= ? OR date_to is null) ";
            objects.add(toDate);
            objects.add(toDate);
        }
        if (objects.size() > 0) {
            return selectRecords(CompetentInstitutionRecord.class , str, objects.toArray());
        } else {
            return selectRecords(CompetentInstitutionRecord.class, null);
        }
    }
	
	public List<CompetentInstitutionRecord> getCompetentInstitutionRecords(Collection<Integer> countryIds, java.sql.Date toDate) throws SQLException{
        List<Object> objects = new ArrayList<Object>();
        String str = " 1 = 1 ";
        if (countryIds != null) {
            str += " AND country_id  in (" + SQLUtils.columnsToParameterList(StringUtils.join(countryIds, ",")) + ") ";
            objects.addAll(countryIds);
        }
        if (toDate != null) {
            str += " AND (date_from <= ? OR date_from is null) ";
            str += " AND (date_to >= ? OR date_to is null) ";
            objects.add(toDate);
            objects.add(toDate);
        }
        if (objects.size() > 0) {
            return selectRecords(CompetentInstitutionRecord.class , str, objects.toArray());
        } else {
            return selectRecords(CompetentInstitutionRecord.class, null);
        }
    }
	
	public List<CompetentInstitutionRecord> getCompetentInstitutionRecordsByUniversityValidityId(int validityId, Date toDate) throws SQLException{
        //Za da ne pisha nova zaqvka, koqto da join-va tablicite sources_of_information i competent_institution, vzemam zapisite 
		//ot sources_of_information po standartniq na4in, sled keto gi vkarvam v edin arraylist, sled koeto vzemam competent isntitutions, koito 
		//gi narejdat v hashmap s kliu4 = id, i zavyrtam edin cikyl po arraylist-a, i ot nego izkarvam zapisite v sy6tiq red, koito sa zapisani v taq tablica
		//za da moje rezultata da se naredi v sy6tiq red!!!
        List<SourceOfInformationRecord> sourcesOfInformation = selectRecords(SourceOfInformationRecord.class, " university_validity_id = ? ", validityId);
        if (sourcesOfInformation.size() == 0) {
        	return new ArrayList<CompetentInstitutionRecord>();
        }
        List<Integer> competentInstitutionIds = new ArrayList<Integer>();
        for (SourceOfInformationRecord r:sourcesOfInformation) {
        	competentInstitutionIds.add(r.getCompetentInstitutionId());
        }
        List<Object> objects = new ArrayList<Object>();
        String str = " id  in (" + SQLUtils.columnsToParameterList(StringUtils.join(competentInstitutionIds, ",")) + ") ";
        objects.addAll(competentInstitutionIds);
        if (toDate != null) {
        	str += " AND (date_from <= ? OR date_from is null) ";
            str += " AND (date_to >= ? OR date_to is null) ";
            objects.add(toDate);
            objects.add(toDate);
        }
        List<CompetentInstitutionRecord> result;
        if (objects.size() > 0) {
            result = selectRecords(CompetentInstitutionRecord.class , str, objects.toArray());
        } else {
        	result = selectRecords(CompetentInstitutionRecord.class, null);
        }
        Map<Integer, CompetentInstitutionRecord> map = new HashMap<Integer, CompetentInstitutionRecord>();
        for (CompetentInstitutionRecord r: result) {
        	map.put(r.getId(), r);
        }
        result = new ArrayList<CompetentInstitutionRecord>();
        for (int r:competentInstitutionIds) {
        	if (map.get(r) != null) {
        		result.add(map.get(r));	
        	}
        }
        return result;
    }
	public static void main(String[] args) throws SQLException {
		CompetentInstitutionDB db = new CompetentInstitutionDB(new StandAloneDataSource());
		System.out.println(db.getCompetentInstitutionRecordsByUniversityValidityId(26, null));
	}
}
