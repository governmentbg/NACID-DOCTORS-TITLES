package com.nacid.bl.impl.comission;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.comision.CommissionMemberForReport;
import com.nacid.bl.nomenclatures.FlatNomenclature;

public class CommissionMemberForReportImpl implements CommissionMemberForReport{
	private String commissionPosition;
	private String degreeAndName;
	private String institutionAndPosition;
	private String institutionAndPositionForProtocol;
	public CommissionMemberForReportImpl(ComissionMember commissionMember) {
		FlatNomenclature commissionPosition = commissionMember.getComissionPos();
		this.commissionPosition = commissionPosition == null ? "" : commissionPosition.getName();
		this.degreeAndName = (StringUtils.isEmpty(commissionMember.getDegree()) ? "" : commissionMember.getDegree() + " ") + commissionMember.getFullName();
		
		
		List<String> institutionAndPositionElements = new ArrayList<String>();
		if (!StringUtils.isEmpty(commissionMember.getTitle())) {
			institutionAndPositionElements.add(commissionMember.getTitle());
		}
		if (!StringUtils.isEmpty(commissionMember.getInstitution())) {
			institutionAndPositionElements.add(commissionMember.getInstitution());
		}
		if (!StringUtils.isEmpty(commissionMember.getDivision())) {
			institutionAndPositionElements.add(commissionMember.getDivision());
		}
		
		institutionAndPosition = StringUtils.join(institutionAndPositionElements, ", ");
		
		List<String> institutionAndPositionElementsForProtocol = new ArrayList<String>();
		if (!StringUtils.isEmpty(commissionMember.getTitle())) {
            institutionAndPositionForProtocol = commissionMember.getTitle();
        }
        
		if (!StringUtils.isEmpty(commissionMember.getDivision())) {
            if (!StringUtils.isEmpty(institutionAndPositionForProtocol)) {
                institutionAndPositionForProtocol = commissionMember.getDivision() + " " + institutionAndPositionForProtocol;
            }
        }
		if (!StringUtils.isEmpty(institutionAndPositionForProtocol)) {
            institutionAndPositionElementsForProtocol.add(institutionAndPositionForProtocol);
        }
		
		if (!StringUtils.isEmpty(commissionMember.getInstitution())) {
		    institutionAndPositionElementsForProtocol.add(commissionMember.getInstitution());
        }
		institutionAndPositionForProtocol = StringUtils.join(institutionAndPositionElementsForProtocol, ", ");
	}
	
	public CommissionMemberForReportImpl(String commissionPosition, String degreeAndName, String institutionAndPosition) {
		this.commissionPosition = commissionPosition;
		this.degreeAndName = degreeAndName;
		this.institutionAndPosition = institutionAndPosition;
	}



	public String getCommissionPosition() {
		return commissionPosition;
	}

	public String getDegreeAndName() {
		return degreeAndName;
	}

	public String getInstitutionAndPosition() {
		return institutionAndPosition;
	}

    public String getInstitutionAndPositionForProtocol() {
        return institutionAndPositionForProtocol;
    }
	
}
