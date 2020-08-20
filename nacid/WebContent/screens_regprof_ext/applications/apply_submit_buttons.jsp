<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
	  	<p class="cc">
            <input type="button" value="Назад" onclick="document.location.href='${pathPrefix }/control/applications/list?getLastTableState=1';" class="back" />
            <c:if test="${!operationView && !hideApplyButton}">
				<input type="submit" style="width: 95px;" value="Подай" class="save" />
				<c:if test="${!hideSignButton}">
					<input type="button" style="width: 115px" value="Подпиши" class="save" onclick="$('request-form').submit();" />
				</c:if>
            </c:if>
        </p>