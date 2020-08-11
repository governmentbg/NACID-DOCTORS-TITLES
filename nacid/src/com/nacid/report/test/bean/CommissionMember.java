package com.nacid.report.test.bean;

import com.nacid.bl.comision.CommissionMemberForReport;

public class CommissionMember implements CommissionMemberForReport{
	private String degreeAndName; //пример: Проф дтн. Иван Христов Иванов
	private String commissionPosition; //член на комисията
	private String institutionAndPosition; // (title+institution+division) пример: хоноруван асистент, СУ, славянска филология
	public CommissionMember(String degreeAndName, String commissionPosition, String institutionAndPosition) {
		this.degreeAndName = degreeAndName;
		this.commissionPosition = commissionPosition;
		this.institutionAndPosition = institutionAndPosition;
	}
	public String getDegreeAndName() {
		return degreeAndName;
	}
	public void setDegreeAndName(String degreeAndName) {
		this.degreeAndName = degreeAndName;
	}
	public String getCommissionPosition() {
		return commissionPosition;
	}
	public void setCommissionPosition(String commissionPosition) {
		this.commissionPosition = commissionPosition;
	}
	public String getInstitutionAndPosition() {
		return institutionAndPosition;
	}
	public void setInstitutionAndPosition(String institutionAndPosition) {
		this.institutionAndPosition = institutionAndPosition;
	}
    @Override
    public String getInstitutionAndPositionForProtocol() {
        // TODO Auto-generated method stub
        return null;
    }
	
	
	
}
