package com.nacid.bl.impl.comission;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.nacid.bl.OrderCriteria;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.comision.ComissionMemberDataProvider;
import com.nacid.bl.comision.ComissionMemberOrderCriteria;
//import com.nacid.bl.external.users.ExtUser;
//import com.nacid.bl.external.users.ExtUsersDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.ProfessionGroup;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.data.applications.ApplicationExpertRecord;
import com.nacid.data.comission.ComissionMemberRecord;
import com.nacid.db.comission.ComissionMemberDB;
import com.nacid.web.exceptions.UnknownRecordException;

public class ComissionMemberDataProviderImpl implements ComissionMemberDataProvider {

	private ComissionMemberDB db;
	private NomenclaturesDataProvider ndp;
	private UsersDataProvider udp;
	
	public ComissionMemberDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
		this.db = new ComissionMemberDB(nacidDataProvider.getDataSource());
		ndp = nacidDataProvider.getNomenclaturesDataProvider();
		udp = nacidDataProvider.getUsersDataProvider();
	}

	@Override
	public void deleteComissionMember(int id) {
		try {
			db.setEndDateToToday(id);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	@Override
	public ComissionMember getComissionMember(int id) {
		try {
			ComissionMemberRecord cmr = new ComissionMemberRecord();
			db.selectRecord(cmr , id);
			return createFromRecord(cmr);
		}
		catch(Exception e) {
			throw Utils.logException(e);
		}
	}
	
	public List<ComissionMember> getCommissionMembers(List<Integer> ids, OrderCriteria orderCriteria) {
		try {
			List<ComissionMemberRecord> commissionMemberRecords = db.getCommissionMembers(ids);
			if (commissionMemberRecords.size() == 0) {
				return null;
			}
			List<ComissionMember> result = new ArrayList<ComissionMember>();
			for (ComissionMemberRecord r:commissionMemberRecords) {
				result.add(createFromRecord(r));
			}
			Collections.sort(result, createComparator(orderCriteria));
			return result;
		}
		catch(Exception e) {
			throw Utils.logException(e);
		}
	}
	
	private ComissionMember createFromRecord(ComissionMemberRecord cmr) {
		FlatNomenclature cpr = ndp.getFlatNomenclature(
				NomenclaturesDataProvider.FLAT_NOMENCLATURE_COMMISSION_POSITION, 
				cmr.getComissionPos());
		//Speciality s = ndp.getSpeciality(cmr.getSpecialityId());
		ProfessionGroup pg = null;
		if(cmr.getProfGroupId() != null) {
			pg = ndp.getProfessionGroup(cmr.getProfGroupId());
		}
		//s.setProfessionGroupName(pg.getName());
		User usr = null;
		if(cmr.getUserId() != null) {
			usr = udp.getUser(cmr.getUserId()/*, false*/);
		}
		return new ComissionMemberImpl(cmr, cpr, pg, usr);
	}
	
	@Override 
	public boolean hasActiveMemberWithEGN(String egn) {
		try {
			String where = "egn=? and date_to is null";
			List<ComissionMemberRecord> cmrs = db.selectRecords(ComissionMemberRecord.class, where, egn); 
			return cmrs.size() != 0;
		}
		catch(Exception e) {
			throw Utils.logException(e);
		}
	}

	@Override
	public List<ComissionMember> getComissionMembers(boolean onlyActive, final OrderCriteria orderCriteria) {
		try {
			List<ComissionMemberRecord> cmrs = db.selectRecords(ComissionMemberRecord.class, "1=1"); 
			
			List<ComissionMember> ret = new ArrayList<ComissionMember>();
			for(ComissionMemberRecord cmr : cmrs) {
			    ComissionMember member = createFromRecord(cmr);
			    if (!onlyActive || (onlyActive && member.isActive())) {
			        ret.add(member);    
			    }
			}
			if (orderCriteria != null) {
			    Collections.sort(ret, createComparator(orderCriteria));
			}
			return ret;
		}
		catch(Exception e) {
			throw Utils.logException(e);
		}
	}
	private static Comparator<ComissionMember> createComparator(final OrderCriteria orderCriteria) {
		return new Comparator<ComissionMember>() {
            public int compare(ComissionMember o1, ComissionMember o2) {
                int res = 0;
                if (ComissionMemberOrderCriteria.ORDER_COLUMN_FULL_NAME.equals(orderCriteria.getOrderColumn())){
                    res = o1.getFullName().compareToIgnoreCase(o2.getFullName());
                } else if (ComissionMemberOrderCriteria.ORDER_COLUMN_ID.equals(orderCriteria.getOrderColumn())){
                    int x = o1.getId() - o2.getId();
                	res = x == 0 ? 0 : (x > 0 ? 1 : -1);
                }
                return orderCriteria.isAscending() ? res : -res;
            }
	        
	    };
	}
	
	@Override 
	public List<ComissionMember> getComissionMembersByEGNPart(String egnPart) {
		
		try {
			List<ComissionMemberRecord> cmrs = db.selectLastMembersWithEGNLike(egnPart); 
			
			List<ComissionMember> ret = new ArrayList<ComissionMember>();
			for(ComissionMemberRecord cmr : cmrs) {
				ret.add(createFromRecord(cmr));
			}
			return ret;
		}
		catch(Exception e) {
			throw Utils.logException(e);
		}
	}
	
	@Override
	public int saveComissionMember(
		int id,
		String fname,
		String sname,
		String lname,
		String degree,
		String institution,
		String division,
		String title,
		String egn,
		String homeCity,
		String homePcode,
		String homeAddress,
		String phone,
		String email,
		String gsm,
		String iban,
		String bic,
		Date dateFrom,
		Date dateTo,
		int comissionPos,
		Integer profGroupId,
		Integer userId) {
	    
	    if(fname != null) fname = fname.toUpperCase();
	    if(sname != null) sname = sname.toUpperCase();
	    if(lname != null) lname = lname.toUpperCase();
		
		ComissionMemberRecord cmr = new ComissionMemberRecord();
		cmr.setId(id);
		cmr.setFname(fname);
		cmr.setSname(sname);
		cmr.setLname(lname);
		cmr.setDegree(degree);
		cmr.setInstitution(institution);
		cmr.setDivision(division);
		cmr.setTitle(title);
		cmr.setEgn(egn);
		cmr.setHomeCity(homeCity);
		cmr.setHomePcode(homePcode);
		cmr.setHomeAddress(homeAddress);
		cmr.setPhone(phone);
		cmr.setEmail(email);
		cmr.setGsm(gsm);
		cmr.setIban(iban);
		cmr.setBic(bic);
		if(dateFrom != null)
			cmr.setDateFrom(new java.sql.Date(dateFrom.getTime()));
		if(dateTo != null)
			cmr.setDateTo(new java.sql.Date(dateTo.getTime()));
		cmr.setComissionPos(comissionPos);
		cmr.setProfGroupId(profGroupId);
		cmr.setUserId(userId);
		
		try {
			if(id == 0) {
				if(dateFrom == null) {
					dateFrom = new java.sql.Date(new Date().getTime());
				}
				cmr = db.insertRecord(cmr);
			}
			else {
			    ComissionMemberRecord oldCmr = new ComissionMemberRecord();
			    oldCmr = db.selectRecord(oldCmr, id);
			    if(oldCmr == null) {
			        throw new UnknownRecordException("Unknown commission member id="+id);
			    }
			    if(cmr.getComissionPos() != oldCmr.getComissionPos()) {
			        java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
			        oldCmr.setDateTo(now);
			        db.updateRecord(oldCmr);
			        
			        cmr.setDateFrom(now);
			        cmr = db.insertRecord(cmr);
			    }
			    else {
		             db.updateRecord(cmr);
			    }

			}
		}
		catch(Exception e) {
			throw Utils.logException(e);
		}
		return cmr.getId();
	}
	
	public List<Integer> getApplicationIdsByExpert(int expertId) {
		try {
            List<ApplicationExpertRecord> applicationsByExpert = db.getApplicationsByExpert(expertId);
            if (applicationsByExpert.size() == 0) {
            	return null;
            }
            List<Integer> result = new ArrayList<Integer>();
            for (ApplicationExpertRecord r:applicationsByExpert) {
            	result.add(r.getApplicationId());
            }
            return result;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
	}
	public ComissionMember getComissionMemberByUserId(int userId) {
		try {
            
			List<ComissionMemberRecord> comissionMembers = db.getCommissionMembersByUserId(userId);
			if (comissionMembers.size() == 0) {
				return null;
			}
			ComissionMemberRecord rec = comissionMembers.get(0);
			return new ComissionMemberImpl(rec, null, null, null);
			
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
	}
}
