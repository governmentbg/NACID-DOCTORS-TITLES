package com.nacid.bl.impl.applications.regprof;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.nacid.bl.applications.regprof.RegprofApplicationDetails;
import com.nacid.bl.applications.regprof.RegprofTrainingCourse;
import com.nacid.bl.applications.regprof.RegprofTrainingCourseDataProvider;
import com.nacid.bl.applications.regprof.RegprofTrainingCourseDetailsBase;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.data.regprof.RegprofProfessionExperienceDatesRecord;
import com.nacid.data.regprof.RegprofProfessionExperienceDocumentRecord;
import com.nacid.data.regprof.RegprofProfessionExperienceExaminationRecord;
import com.nacid.data.regprof.RegprofProfessionExperienceRecord;
import com.nacid.data.regprof.RegprofTrainingCourseSpecialityRecord;
import com.nacid.db.applications.regprof.RegprofApplicationDB;

public class RegprofTrainingCourseDataProviderImpl implements RegprofTrainingCourseDataProvider {
    
    private NacidDataProviderImpl nacidDataProvider;
    private RegprofApplicationDB db;
    private static final Integer ONE_DAY = 24 * 60 * 60 * 1000; //milliseconds
    
    public RegprofTrainingCourseDataProviderImpl(NacidDataProviderImpl nacidDataProvider){
        this.nacidDataProvider = nacidDataProvider;
        this.db = new RegprofApplicationDB(nacidDataProvider.getDataSource());
    }
    
    public int saveRegprofProfessionExperienceRecords(RegprofTrainingCourse trainingCourse) { // tuk trqbva da se dobavqt drugite insertRecord
        int id = -1;
        updateDocumentExperienceDates(trainingCourse.getExperienceRecord());
        if (trainingCourse.getExperienceRecord().getId() == null || trainingCourse.getExperienceRecord().getId() == 0) { // insert a new record
            try {
                RegprofProfessionExperienceRecord insertedRecord = db.insertRecord(trainingCourse.getExperienceRecord());
                id = insertedRecord.getId();
                if (id > 0) {
                    if (trainingCourse.getExperienceRecord().getProfessionExperienceDocuments() != null) {
                        for (RegprofProfessionExperienceDocumentRecord rec:trainingCourse.getExperienceRecord().getProfessionExperienceDocuments()) {
                            if (rec.getId() == null) {//empty row!
                                continue;
                            }
                            rec.setProfExperienceId(id);
                            rec = db.insertRecord(rec);
                            if (rec.getDates() != null) {
                                for (RegprofProfessionExperienceDatesRecord r:rec.getDates()) {
                                    if (r.getId() == null) { //empty row
                                        continue;
                                    }
                                    r.setProfExperienceDocumentId(rec.getId());
                                    db.insertRecord(r);
                                }
                            }
                        }
                    }
                    db.updateTrainingCourseDetails(trainingCourse.getDetails());
                    return id;
                }
            } catch (SQLException e) {
                throw Utils.logException(e);
            }
        }
        else { // update existing records
            try {
                RegprofProfessionExperienceRecord record = trainingCourse.getExperienceRecord();
                db.updateRecord(record);
                id = record.getId();
                
                List<Integer> documentRecordsNotToDelete = new ArrayList<Integer>();
                if (trainingCourse.getExperienceRecord().getProfessionExperienceDocuments() != null) {
                    for (RegprofProfessionExperienceDocumentRecord rec:trainingCourse.getExperienceRecord().getProfessionExperienceDocuments()) {
                        if (rec.getId() == null) { //empty row!
                            continue;
                        }
                        int docId;
                        rec.setProfExperienceId(record.getId());
                        if (rec.getId() == 0) {
                            RegprofProfessionExperienceDocumentRecord newRec = db.insertRecord(rec);
                            docId = newRec.getId();
                        } else {
                            db.updateRecord(rec);
                            docId = rec.getId();
                        }
                        documentRecordsNotToDelete.add(docId);
                        db.deleteProfessionExperienceDatesRecords(docId, null);
                        if (rec.getDates() != null) {
                            for (RegprofProfessionExperienceDatesRecord r:rec.getDates()) {
                                if (r.getId() == null) { //empty row!
                                    continue;
                                }
                                r.setProfExperienceDocumentId(docId);
                                db.insertRecord(r);
                            }
                        }
                    }
                    if (documentRecordsNotToDelete.size() > 0) {
                        db.deleteProfessionExperienceDocumentRecords(id, documentRecordsNotToDelete);
                    }
                } else {
                    db.deleteAllProfessionExperienceDocumentRecords(record.getId());
                }
                
                
                db.updateTrainingCourseDetails(trainingCourse.getDetails()); ////////////////
                return id;

            } catch (SQLException e) {
                throw Utils.logException(e);
            }
        }
        return id;
    }
    
    public RegprofTrainingCourse getRegprofTrainingCourse(Integer applicationId) { // tuk trqbva da se dobavqt oshte neshta
        try {
            RegprofTrainingCourse trainingCourse = new RegprofTrainingCourseImpl();
            RegprofProfessionExperienceRecord record = db.getRegprofProfessionExperience(applicationId);
            if (record != null) {
                trainingCourse.setExperienceRecord(record);
                Integer professionExperienceId = trainingCourse.getExperienceRecord().getId();
                record.setProfessionExperienceDocuments(db.getRegprofProfessionExperienceDocumentRecords(professionExperienceId));
            }
            trainingCourse.setDetails(getRegprofTrainingCourseDetails(db.getRegprofApplicationDetails(applicationId).getTrainingCourseId()));
            return trainingCourse;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    //RayaWritten----------------------------------------------------------------------
    public RegprofTrainingCourseDetailsImpl getRegprofTrainingCourseDetails(Integer trainingCourseId){
        try{
            RegprofTrainingCourseDetailsImpl details = db.selectRecord(new RegprofTrainingCourseDetailsImpl(), trainingCourseId);
            return details;
        } catch(SQLException e){
            throw Utils.logException(e);
        }
        
    }
    
    public void updateRegprofTrainingCourseDetails(RegprofTrainingCourseDetailsBase details, String...columns){
        try {
            db.updateRecord(details, columns);
        } catch(SQLException e){
            throw Utils.logException(e);
        }
    }
    
    public Integer insertEmptyTCDetailsRecord(){
        try{
            return db.insertRecord(new RegprofTrainingCourseDetailsImpl(), true).getId();
        } catch(SQLException e){
            throw Utils.logException(e);
        }
    }
    public void deleteProfessionExperienceExamination(Integer professionExperienceId) {
        try{
            db.deleteRecords(RegprofProfessionExperienceExaminationRecord.class, "regprof_profession_experience_id = ?", professionExperienceId);
        }catch(SQLException e){
            throw Utils.logException(e);
        }
    }
  //--------------------------------------------------------------------------------------
    public Integer saveProfessionExperienceExamination(RegprofProfessionExperienceExaminationRecord record) {
        int id = -1;
        try {
            if (record.getId() == null || record.getId() < 1) {
                RegprofProfessionExperienceExaminationRecord insertedRecord = db.insertRecord(record);
                id = insertedRecord.getId();
            }
            else { // update
                db.updateRecord(record);
                id = record.getId();
            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        return id;
    }
    
    
    public RegprofProfessionExperienceExaminationRecord getProfessionExperienceExamination(Integer experienceId) {
        RegprofProfessionExperienceExaminationRecord record = new RegprofProfessionExperienceExaminationRecord();
        try {
            record = db.getProfessionExperienceExamination(experienceId);
            return record;
        } catch (SQLException e) {
            throw Utils.logException(e);
        } 
        
    }
    
    public RegprofTrainingCourseDetailsImpl getTrainingCourseDetails(int applicationId) {
        try {
            RegprofApplicationDetails applicationDetails = db.getRegprofApplicationDetails(applicationId);
            if (applicationDetails != null) {
                int trainingCourseId = applicationDetails.getTrainingCourseId();
                return db.getTrainingCourseDetails(trainingCourseId);
            }
            else return null;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    public void deleteRegprofProfessionExperienceRecords(int trainingCourseId) {
        try {
            db.deleteProfessionExperienceRecords(trainingCourseId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public void updateTrainingCourseDetails(RegprofTrainingCourseDetailsBase details) {
        try {
            db.updateTrainingCourseDetails(details);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public List<RegprofTrainingCourseSpecialityRecord> getTrainingCourseSpecialities(int regprofTrainingCourseId) {
        try {
            return db.selectRecords(RegprofTrainingCourseSpecialityRecord.class, " training_course_id = ? ORDER BY id ASC ", regprofTrainingCourseId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public void saveTrainingCourseSpecialities(List<RegprofTrainingCourseSpecialityRecord> records) {
        try {
            if (records != null && records.size() > 0) {
                for (RegprofTrainingCourseSpecialityRecord record : records) {
                    db.insertRecord(record);
                }
            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    public void deleteTrainingCourseSpecialities(int regprofTrainingCourseId) {
        try {
            db.deleteRecords(RegprofTrainingCourseSpecialityRecord.class, " training_course_id = ? ", regprofTrainingCourseId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public static void updateDocumentExperienceDates(RegprofProfessionExperienceRecord rec) {
        int totalDays = 0;
        int totalMonths = 0;
        int totalYears = 0;
        
        for (RegprofProfessionExperienceDocumentRecord r:rec.getProfessionExperienceDocuments()) {
            if (r.getForExperienceCalculation() == 0) { //ne uchastva v izchislenie na staja!
                continue;
            }
            
            Calendar cal = Calendar.getInstance();

            for (RegprofProfessionExperienceDatesRecord d:r.getDates()) {
                /*long diff = (d.getDateTo().getTime() - d.getDateFrom().getTime());
                int days2 = (int)((double)diff/ONE_DAY) + 1;
                if (d.getWorkdayDuration() != 8) {
                    diff *= ((double)d.getWorkdayDuration()/8);
                }
                days += days2;*/
                
                cal.setTime(d.getDateFrom());
                int dayFrom = cal.get(Calendar.DATE);
                int monthFrom = cal.get(Calendar.MONTH) + 1;
                int yearFrom = cal.get(Calendar.YEAR);
                
                cal.setTime(d.getDateTo());
                int dayTo = cal.get(Calendar.DATE);
                int monthTo = cal.get(Calendar.MONTH) + 1;
                int yearTo = cal.get(Calendar.YEAR);
                
                int days = dayTo - dayFrom;
                int months = monthTo - monthFrom;
                int years = yearTo - yearFrom;
                
                if (days < 0) {
                    days += 30;
                    months -= 1;
                }
                if (months < 0) {
                    months += 12;
                    years -= 1;
                }
                if (d.getWorkdayDuration() < 8) {
                    int ratio = d.getWorkdayDuration() / 8 ;
                    days = (days + 30 * months + 360 * years) * ratio;
                    years = days / 360;
                    days %= 360;
                    months = days / 30;
                    days %= 30;
                }
                totalDays += days;
                totalMonths += months;
                totalYears += years;
            }
            totalMonths += totalDays / 30;
            totalDays %= 30;
            totalYears += totalMonths / 12;
            totalMonths %= 12;
        }
        
        totalMonths += totalDays / 30;
        totalDays %= 30;
        totalYears += totalMonths / 12;
        totalMonths %= 12;
        
        rec.setDays(totalDays);
        rec.setMonths(totalMonths);
        rec.setYears(totalYears);
        
    }
    
    public RegprofProfessionExperienceDocumentRecord getRegprofProfessionExperienceDocument(int professionExperienceDocumentId) {
        try {
            return db.selectRecord(new RegprofProfessionExperienceDocumentRecord(), professionExperienceDocumentId);
        } catch (SQLException e) {
            Utils.logException(e);
        }
        return null;
    }
    
    public int saveTrainingCourseDetails(RegprofTrainingCourseDetailsImpl details) {
        try {
            RegprofTrainingCourseDetailsImpl record = db.insertRecord(details);
            if (record != null) {
                return record.getId();
            }
        } catch (SQLException e) {
            Utils.logException(e);
        }
        return 0;
    }
    
    public int saveProfessionExperienceRecord(RegprofProfessionExperienceRecord record) {
        try {
            RegprofProfessionExperienceRecord insertedRecord = db.insertRecord(record);
            if (insertedRecord != null) {
                return insertedRecord.getId();
            }
        } catch (SQLException e) {
            Utils.logException(e);
        }
        return 0;
    }
    
    public int saveProfessionExperienceDocumentRecord(RegprofProfessionExperienceDocumentRecord record) {
        try {
            RegprofProfessionExperienceDocumentRecord insertedRecord = db.insertRecord(record);
            if (insertedRecord != null) {
                return insertedRecord.getId();
            }
        } catch (SQLException e) {
            Utils.logException(e);
        }
        return 0;
    }
    
    public static void main(String[] args) {
        long diff = 8;
        int x = 5;
        x = x/4;
        diff *= ((double)6/8);
        System.out.println(diff);
    }

}