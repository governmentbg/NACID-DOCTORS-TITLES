package com.nacid.bl.comision;

import java.util.Date;
import java.util.List;

import com.nacid.bl.OrderCriteria;

public interface ComissionMemberDataProvider {
    /**
     * 
     * @param onlyActive - true=samo aktivnite, false=vsi4ki
     * @param orderCriteria - pri null dannite ne sa sortirani, {@link ComissionMemberOrderCriteria} se syzdava kato se izvika static methoda {@link ComissionMemberOrderCriteria}.getComissionMemberOrderCriteria() 
     * @return
     */
	public List<ComissionMember> getComissionMembers(boolean onlyActive, OrderCriteria orderCriteria);
	public ComissionMember getComissionMember(int id);
	/**
	 * vry6ta spisyk ot {@link ComissionMember} 
	 * @param ids - ids, koito trqba da se vry6tat - null - vry6ta vsi4ki
	 * @param orderCriteria - syzdava se s {@link OrderCriteria}.createOrderCriteria(....) kato za type si izbira {@link OrderCriteria}
	 * @return
	 */
	public List<ComissionMember> getCommissionMembers(List<Integer> ids, OrderCriteria orderCriteria);
	public void deleteComissionMember(int id);
	public int saveComissionMember(int id, String fname, String sname, String lname,
			String degree, String institution, String division, String title,
			String egn, String homeCity, String homePcode, String homeAddress,
			String phone, String email, String gsm, String iban, String bic,
			Date dateFrom, Date dateTo, int comissionPos, Integer profGroupId,
			Integer userId);
	public boolean hasActiveMemberWithEGN(String egn);
	public List<ComissionMember> getComissionMembersByEGNPart(String egnPart);
	
	 /**
	   * vry6ta zaqvleniqta, koito sa prika4eni kym daden expert
	   * @param expertId
	   * @return
	   */
	  public List<Integer> getApplicationIdsByExpert(int expertId);
	  
	  public ComissionMember getComissionMemberByUserId(int userId);
	
}
