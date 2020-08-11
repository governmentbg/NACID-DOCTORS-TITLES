package com.nacid.bl.impl.comission;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.ApplicationDetailsForReport;
import com.nacid.bl.applications.ApplicationForList;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.bl.comision.*;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.ApplicationDetailsForReportImpl;
import com.nacid.bl.impl.applications.ApplicationOrderCriteria;
import com.nacid.data.comission.CommissionAgendaRecord;
import com.nacid.data.comission.CommissionCalendarProtocolRecord;
import com.nacid.data.comission.CommissionCalendarRecord;
import com.nacid.data.comission.CommissionParticipationRecord;
import com.nacid.db.comission.CommissionCalendarDB;
import com.nacid.db.utils.StandAloneDataSource;

import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class CommissionCalendarDataProviderImpl implements CommissionCalendarDataProvider {

    private CommissionCalendarDB db;
    private NacidDataProvider nacidDataProvider;
    
    public CommissionCalendarDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
        db = new CommissionCalendarDB(nacidDataProvider.getDataSource());
        this.nacidDataProvider = nacidDataProvider;
    }

    public CommissionCalendarDB getDb() {
        return db;
    }

    @Override
    public CommissionCalendar getCommissionCalendar(int id) {
    	try {
    		CommissionCalendarRecord record = db.selectRecord(new CommissionCalendarRecord(), id);
    		return record == null ? null : new CommissionCalendarImpl(record);
    	} catch (SQLException e) {
    		throw Utils.logException(e);
    	}
    }

    @Override
    public List<CommissionCalendar> getCommissionCalendarsByStatus(int status) {
        
        List<CommissionCalendarRecord> records = null;
        
        try {
            if (status > 0) {
                records = db.selectRecords(CommissionCalendarRecord.class, "session_status_id=?", status);
            }
            else {
                records = db.selectRecords(CommissionCalendarRecord.class, null);
            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        return generateCommissionCalendars(records);
    }
    public List<CommissionCalendar> getCommissionCalendarsByApplication(int applicationId) {
    	try {
			List<CommissionCalendarRecord> records = db.getCommissionCalendarRecordsByApplication(applicationId);
			return generateCommissionCalendars(records);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
    }
    public List<CommissionCalendar> getCommissionCalendarsByDate(Date date) {
        
        try {
        	List<CommissionCalendarRecord> records = db.getCommissionCalendarRecordsByDate(date == null ? null : new Timestamp(date.getTime()));    
        	return generateCommissionCalendars(records);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    private static List<CommissionCalendar> generateCommissionCalendars(List<CommissionCalendarRecord> records) {
    	if (records.size() == 0 ) {
    		return null;
    	}
    	List<CommissionCalendar> ret = new ArrayList<CommissionCalendar>();
        for (CommissionCalendarRecord rec : records) {
			ret.add(new CommissionCalendarImpl(rec));
		}
        return ret;
    }

    /**
     * za momenta za sessionNumber se zapisva id-to na zapisa
     */
    public int saveCommissionCalendar(int id, Date dateAndTime, String notes, int sessionStatusId/*, int sessionNumber*/) {
    	try {
    		CommissionCalendarRecord record = new CommissionCalendarRecord(id, dateAndTime == null ? null : new Timestamp(dateAndTime.getTime()), notes, sessionStatusId, 0);
    		if (id == 0) {
    			record = db.insertRecord(record);
    			record.setSessionNumber(record.getId());
    			db.updateRecord(record);
    		} else {
    			record.setSessionNumber(id);
    			db.updateRecord(record);
    		}
    		return record.getId();
    	} catch (SQLException e) {
    		throw Utils.logException(e);
    	}
    }

	public List<ApplicationForList> getCommissionApplicationsForList(int calendarId) {
		List<Integer> applicationIds = getCommissionApplicationIds(calendarId);
		if (applicationIds == null) {
			return null;
		}
		ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
		return applicationsDataProvider.getApplicationsByCommission(calendarId);
		/*List<Application> result = new ArrayList<Application>();
		for (int i :applicationIds) {
			Application a = applicationsDataProvider.getApplication(i);
			if (a != null) {
				result.add(a);	
			}
			
		}
		return result;*/
	}
	public List<Integer> getCommissionApplicationIds(int calendarId) {
		try {
    		List<CommissionAgendaRecord> commissionAgendaRecords = db.getCommissionAgendas(calendarId);
    		if (commissionAgendaRecords.size() == 0) {
    			return null;
    		}
    		List<Integer> ids = new ArrayList<Integer>();
    		for (CommissionAgendaRecord record:commissionAgendaRecords) {
    			ids.add(record.getApplicationId());
    		}
    		Collections.sort(ids);
    		return ids;
    		
    	} catch (SQLException e) {
    		throw Utils.logException(e);
    	}
	}
	public void deleteCommissionApplications(int calendarId) {
		try {
			db.deleteCommissionAgendaRecords(calendarId);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}
	
	/**
	 * vry6ta komisiqta posledno razgledala dadeno zaqvlenie 
	 */
	public CommissionCalendar getLastCommissionExaminedApplication(int applicationId) {
		try {
			int commissionId = db.getLastCommissionIdExaminedApplication(applicationId);
			if (commissionId == 0) {
				return null;
			}
			return getCommissionCalendar(commissionId);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
		//CommissionAgendaRecord agendaRecord = db.getCommissionAgendas(calendarId)
	}
	
    public int addApplicationToCalendar(int calendarId, int applicationId) {
    	try {
    		CommissionAgendaRecord record = new CommissionAgendaRecord(0,applicationId, calendarId );
			record = db.insertRecord(record);
    		return record.getId();
    	} catch (SQLException e) {
    		throw Utils.logException(e);
    	}
    }
	public void setApplicationsToCalendar(int calendarId, List<Integer> applicationIds) {
    	try {
    		db.deleteCommissionAgendaRecords(calendarId);
    		if (applicationIds != null && applicationIds.size() > 0) {
    			for (int i:applicationIds) {
    				CommissionAgendaRecord record = new CommissionAgendaRecord(0, i, calendarId );
    				record = db.insertRecord(record);		
    			}
    		}
    		
    		
    	} catch (SQLException e) {
    		throw Utils.logException(e);
    	}
    }
	
	
	
	public List<CommissionParticipation> getCommissionParticipations(int commissionCalendarId) {
		try {
			List<CommissionParticipationRecord> records = db.getCommissionParticipationRecords(commissionCalendarId);
			if (records.size() == 0) {
    			return null;
    		}
    		List<Integer> ids = new ArrayList<Integer>();
    		for (CommissionParticipationRecord record:records) {
    			ids.add(record.getExpertId());
    		}
    		ComissionMemberDataProvider comissionMemberDataProvider = nacidDataProvider.getComissionMemberDataProvider();
    		List<ComissionMember> members = comissionMemberDataProvider.getCommissionMembers(ids, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_COMISSION_MEMBER, ComissionMemberOrderCriteria.ORDER_COLUMN_FULL_NAME, true));
    		Map<Integer, CommissionParticipationRecord> map = new HashMap<Integer, CommissionParticipationRecord>();
    		for (CommissionParticipationRecord r: records) {
    			map.put(r.getExpertId(), r);
    		}
    		List<CommissionParticipation> result = new ArrayList<CommissionParticipation>();
    		for (ComissionMember m:members) {
    			result.add(new CommissionParticipationImpl(m, map.get(m.getId())));
    		}
    		return result;
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
		/*List<ComissionMember> result = new ArrayList<ComissionMember>();
		for (int i:ids) {
			ComissionMember m = comissionMemberDataProvider.getComissionMember(i);
			if (m != null) {
				result.add(m);	
			}
			
		}
		return result;*/
		
		
	}
	/*public List<Integer> getCommissionParticipationMemberIds(int commissionCalendarId) {
		try {
    		List<CommissionParticipationRecord> commissionParticipationRecords = db.getCommissionParticipationRecords(commissionCalendarId);
    		if (commissionParticipationRecords.size() == 0) {
    			return null;
    		}
    		List<Integer> ids = new ArrayList<Integer>();
    		for (CommissionParticipationRecord record:commissionParticipationRecords) {
    			ids.add(record.getExpertId());
    		}
    		return ids;
    		
    	} catch (SQLException e) {
    		throw Utils.logException(e);
    	}
	}*/
	
	public void deleteCommissionMembersList(int calendarId) {
		try {
			db.deleteCommissionParticipationRecords(calendarId);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}
	
    public int addCommissionMemberToCalendar(int calendarId, int commissionMemberId, boolean notified, boolean participated, String notes) {
    	try {
    		CommissionParticipationRecord record = new CommissionParticipationRecord(0, commissionMemberId, calendarId, notified ? 1 : 0, participated ? 1 : 0, notes);
    		record = db.insertRecord(record);
    		return record.getId();
    	} catch (SQLException e) {
    		throw Utils.logException(e);
    	}
    }
	
    @Override
    public CommissionCalendarProtocol loadCalendarProtocol(int calendarId, boolean loadContent) {
        try {
            CommissionCalendarProtocolRecord rec = db.loadCalendarProtocol(calendarId, loadContent);
            if(rec != null) {
                return new CommissionCalendarProtocolImpl(rec);
            }
            return null;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public void addCalendarProtocol(int id, InputStream content, String contentType, String fileName, int fileSize) {
        try {
            CommissionCalendarProtocolRecord rec = new CommissionCalendarProtocolRecord(id, content, contentType, fileName);
            db.addCalendarProtocol(rec, fileSize);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    public CommissionProtocol getCommissionProtocolForReport(int calendarId) {
    	CommissionCalendar commissionCalendar = getCommissionCalendar(calendarId);
    	if (commissionCalendar == null) {
    		return null;
    	}
    	return new CommissionProtocolImpl(nacidDataProvider, commissionCalendar);
    }


    public List<Application> getCommissionApplications(int commissionCalendarId) {
        List<Integer> applicationIds = getCommissionApplicationIds(commissionCalendarId);
        if (applicationIds == null) {
            return null;
        }
        ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
        List<Application> applications = applicationsDataProvider.getApplications(applicationIds, OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_APPLICATION, ApplicationOrderCriteria.ORDER_COLUMN_ID, true));
        return applications;
    }

    public List<ApplicationDetailsForReport> getApplicationDetailsForReport(int commissionCalendarId) {
        List<Application> applications = getCommissionApplications(commissionCalendarId);
    	return applications == null ? null : applications.stream().map(a -> new ApplicationDetailsForReportImpl(nacidDataProvider, a)).collect(Collectors.toList());
    }
    
	public static void main(String[] args) throws Exception {
		/*NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
		CommissionCalendarDataProvider commissionCalendarDataProvider = nacidDataProvider.getCommissionCalendarDataProvider();
		CommissionProtocol commissionProtocol = commissionCalendarDataProvider.getCommissionProtocolForReport(33);
		if (commissionProtocol == null) {
			System.out.println("no commission protocol for given id....");
			return;
		}
		
		
		List<ApplicationDetailsForReport> recognized = commissionProtocol.getRecognizedApplications();
		System.out.println("Recognized applications:");
		if (recognized != null) {
			for (ApplicationDetailsForReport p:recognized) {
				System.out.println(p.getDocFlowNumber());
			}	
		}
		
		
		List<ApplicationDetailsForReport> postponed = commissionProtocol.getPostponedApplications();
		System.out.println("Postponed applications:");
		if (postponed != null) {
			for (ApplicationDetailsForReport p:postponed) {
				System.out.println(p.getDocFlowNumber());
			}	
		}
		
		
		List<ApplicationDetailsForReport> refused = commissionProtocol.getRefusedApplications();
		System.out.println("refused applications:");
		if (refused != null) {
			for (ApplicationDetailsForReport p:refused) {
				System.out.println(p.getDocFlowNumber());
			}	
		}
		
		List<ApplicationDetailsForReport> terminated = commissionProtocol.getTerminatedApplications();
		System.out.println("terminated applications:");
		if (terminated != null) {
			for (ApplicationDetailsForReport p:terminated) {
				System.out.println(p.getDocFlowNumber());
			}	
		}
		
		FileOutputStream os = new FileOutputStream("c:/test.rtf");
		JasperReportGenerator generator = new JasperReportGenerator(JasperReportNames.JASPER_REPORT_PROTOCOL, ExportType.RTF, os);
		generator.setParams(nacidDataProvider.getUtilsDataProvider().getCommonVariablesAsMap());
		generator.export(Arrays.asList(commissionProtocol));
		os.close();
		List<ApplicationDetailsForReport> applications = new ArrayList<ApplicationDetailsForReport>();
		NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
		ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
		applications.add(applicationsDataProvider.getApplicationDetailsForReport(59));
		applications.add(applicationsDataProvider.getApplicationDetailsForReport(60));
		FileOutputStream os = new FileOutputStream("c:/test.rtf");
		JasperReportGenerator generator = new JasperReportGenerator(JasperReportNames.JASPER_REPORT_UDOSTOVERENIE, ExportType.RTF, os);
		generator.setParams(nacidDataProvider.getUtilsDataProvider().getCommonVariablesAsMap());
		generator.export(applications);
		os.close();*/
		NacidDataProvider nacidDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource());
		CommissionCalendarDataProvider commissionCalendarDataProvider = nacidDataProvider.getCommissionCalendarDataProvider();
		CommissionProtocol commissionProtocol = commissionCalendarDataProvider.getCommissionProtocolForReport(80);
		if (commissionProtocol == null) {
			System.out.println("no commission protocol for given id....");
			return;
		}
		
		
	}
}
