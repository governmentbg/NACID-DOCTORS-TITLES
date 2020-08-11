package com.nacid.web.handlers.impl.nomenclatures;

import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.model.common.SystemMessageWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EducationLevelHandler extends FlatNomenclatureHandler {
    public EducationLevelHandler(ServletContext servletContext) {
        super(servletContext, "education_level_edit");
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        super.handleEdit(request, response);
        Integer flatNomenclatureId = DataConverter.parseInteger(request.getParameter("id"), null);
        List<Integer> applicationTypesPerGraduationWay = getNacidDataProvider().getNomenclaturesDataProvider().getApplicationTypesPerEducationLevel(flatNomenclatureId);
        request.setAttribute("activeApplicationTypes", applicationTypesPerGraduationWay.stream().collect(Collectors.toMap(Function.identity(), Function.identity())));
    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        super.handleSave(request, response);
        SystemMessageWebModel systemMessage = (SystemMessageWebModel) request.getAttribute(WebKeys.SYSTEM_MESSAGE);

        Integer flatNomenclatureId = RequestParametersUtils.parseInteger(request, "id", null);
        String[] appTypes = request.getParameterValues("application_type");
        List<Integer> appTypeIds = appTypes == null || appTypes.length == 0 ? new ArrayList<>() : RequestParametersUtils.convertRequestParameterToIntegerList(Arrays.stream(appTypes).collect(Collectors.joining(";")));

        //appTypes se zapisvat SAMO ako systemMessage-e e correct!!
        if (systemMessage == null || systemMessage.getMessageType() == SystemMessageWebModel.MESSAGE_TYPE_CORRECT) {
            getNacidDataProvider().getNomenclaturesDataProvider().updateEducationLevelApplicationTypes(flatNomenclatureId, appTypeIds);
        }

        refreshCachedNomenclatures(request);
        request.setAttribute("activeApplicationTypes", appTypeIds.stream().collect(Collectors.toMap(Function.identity(), Function.identity())));
    }
}
