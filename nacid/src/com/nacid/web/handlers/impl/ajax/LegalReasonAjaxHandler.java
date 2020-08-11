package com.nacid.web.handlers.impl.ajax;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.nomenclatures.ExpertPosition;
import com.nacid.bl.nomenclatures.LegalReason;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.NacidBaseRequestHandler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class LegalReasonAjaxHandler extends NacidBaseRequestHandler {

	public LegalReasonAjaxHandler(ServletContext servletContext) {
		super(servletContext);
	}

	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		Integer applicationStatusId = DataConverter.parseInteger(request.getParameter("appStatusId"), null);
		boolean onlyActive = DataConverter.parseBoolean(request.getParameter("onlyActive"));
        Integer expertPositionId = DataConverter.parseInteger(request.getParameter("expertPositionId"), null);
        int applicationType = DataConverter.parseInt(request.getParameter("applicationType"), -1);
        NomenclaturesDataProvider nomenclaturesDataProvider = getNacidDataProvider().getNomenclaturesDataProvider();
        if (expertPositionId != null) {
            ExpertPosition ep = nomenclaturesDataProvider.getExpertPosition(expertPositionId);
            applicationStatusId = ep == null ? null : ep.getRelatedAppStatusId();
        }
		if (applicationStatusId != null) {
			List<LegalReason> legalReasons = nomenclaturesDataProvider.getLegalReasons(applicationType, onlyActive ? new Date() : null, null, applicationStatusId);
			if (legalReasons != null) {
				//ako size-a na legalReasons == 1, togava selectva napravo zapisa, ako zapisite sa poveche ot 1, togava selected = null!!
			    ComboBoxUtils.generateNomenclaturesComboBox(legalReasons.size() == 1 ? legalReasons.get(0).getId() : null, legalReasons, false, request, "legalReasonCombo", true);
				if ("commission_inquire".equals(request.getParameter("type"))) {
					request.setAttribute(WebKeys.NEXT_SCREEN, "commission_inquire_legal_reason_ajax");
				} else {
					request.setAttribute(WebKeys.NEXT_SCREEN, "legal_reason_ajax");	
				}
				return;
			}
		} 
		writeToResponse(response, "");
	}
	public static void main(String[] args) {
	    NomenclaturesDataProvider nomenclaturesDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource()).getNomenclaturesDataProvider();
	    List<LegalReason> legalReasons = nomenclaturesDataProvider.getLegalReasons(1, new Date(), null, 11);
	    System.out.println(legalReasons);
	}
	
}
