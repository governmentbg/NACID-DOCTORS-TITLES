package com.nacid.web.handlers.impl.nomenclatures;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.Language;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.HomePageHandler;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.nomenclatures.LanguageWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LanguageHandler extends FlatNomenclatureHandler {
    public LanguageHandler(ServletContext servletContext) {
        super(servletContext, "language_edit");
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        Integer flatNomenclatureId = DataConverter.parseInteger(request.getParameter("id"), null);
        Language lang = getNacidDataProvider().getNomenclaturesDataProvider().getLanguage(flatNomenclatureId);
        request.setAttribute(WebKeys.LANGUAGE, new LanguageWebModel(lang));

        request.setAttribute(WebKeys.NEXT_SCREEN, "language_edit");

    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        if (!RequestParametersUtils.getParameterFormSubmitted(request)) {
            new HomePageHandler(request.getSession().getServletContext()).processRequest(request, response);
            return;
        }
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        int id = DataConverter.parseInt(request.getParameter("id"), 0);
        String name = request.getParameter("name").trim();
        String iso639Code = request.getParameter("iso639Code");
        Date dateFrom = DataConverter.parseDate(request.getParameter("dateFrom"));
        //If new record is added and dateFrom is not set, then dateFrom is set to today
        if (id == 0 && dateFrom == null) {
            dateFrom = Utils.getToday();
        }
        Date dateTo = DataConverter.parseDate(request.getParameter("dateTo"));
        if (id != 0 && nomenclaturesDataProvider.getLanguage(id) == null) {
            throw new UnknownRecordException("Unknown Language ID:" + id);
        }
        if (name == null || "".equals(name) || iso639Code == null || "".equals(iso639Code)) {
            SystemMessageWebModel webmodel = new SystemMessageWebModel("Грешно въведени данни!");
            if (name == null || "".equals(name)) {
                webmodel.addAttribute("- грешно въведено име!");
            }
            if (iso639Code == null || "".equals(iso639Code)) {
                webmodel.addAttribute("- грешно въведен iso639Code код!");
            }
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
            request.setAttribute(WebKeys.LANGUAGE, new LanguageWebModel(id, name, iso639Code, request.getParameter("dateFrom"), request.getParameter("dateTo")));
        } else {
            int newId = nomenclaturesDataProvider.saveLanguage(id, name, dateFrom, dateTo, iso639Code);
            FlatNomenclatureHandler.refreshCachedNomenclatures(request);
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
            request.setAttribute(WebKeys.LANGUAGE, new LanguageWebModel(nomenclaturesDataProvider.getLanguage(newId)));
        }
        request.getSession().removeAttribute(WebKeys.TABLE_FLAT_NOMENCLATURE + getGroupName(request));
        request.setAttribute(WebKeys.NEXT_SCREEN, "language_edit");
    }
}
