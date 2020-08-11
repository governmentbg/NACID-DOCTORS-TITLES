package com.ext.nacid.web.handlers;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nacid.web.WebKeys;
import com.nacid.web.handlers.BaseRequestHandler;
import com.nacid.web.handlers.UserOperationsUtils;
/**
 * handler, koito ne proverqva dali potrebitelq ima prava da izpylni dadena operaciq, a napravo si vika opredeleniq method v zavisimost ot operationName-a
 * @author ggeorgiev
 *
 */
public class NacidExtNoAuthorizationCheckBaseRequestHandler extends BaseRequestHandler{
    public NacidExtNoAuthorizationCheckBaseRequestHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void initHandler(HttpServletRequest request, HttpServletResponse response) {
        if(!(Boolean)request.getAttribute("ajaxServlet")) {
            MenuShowHandler mrh = new MenuShowHandler(request.getSession().getServletContext());
            mrh.processRequest(request, response);
        }
    }
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        String operationName = getOperationName(request);
        int operationId = UserOperationsUtils.getOperationId(operationName);

        if (operationId == UserOperationsUtils.OPERATION_LEVEL_NEW) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Добавяне на");
            handleNew(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_EDIT) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Промяна на");
            handleEdit(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_VIEW) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Преглед на");
            handleView(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_SAVE) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Промяна на");
            handleSave(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_LIST) {
            handleList(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_DELETE) {
            handleDelete(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_PRINT) {
            handlePrint(request, response);
        } else {
            handleDefault(request, response);
        }

    }
}
