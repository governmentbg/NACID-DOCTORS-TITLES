<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:if test="${not empty similarDiplomas}">
    <fieldset><legend class="noForm">Подобни дипломи</legend>
        <table width="100%" cellspacing="0" cellpadding="1" border="1" class="sort-table ">
            <tr>
                <td class="dark" align="center">Номер</td>
                <td class="dark" align="center">Собственик</td>
                <td class="dark" align="center">Държава Университет</td>
                <td class="dark" align="center">Университет</td>
                <td class="dark" align="center">ОКС</td>
                <td class="dark" align="center">Специалност</td>
                <td class="dark" align="center">Година на дипломата</td>
                <td class="dark" width="60" align="center">-</td>
            </tr>
        <c:forEach var="item" items="${similarDiplomas}">
            <tr>
                <td>${item.docflowNumber}</td>
                <td>${item.civilId} - ${item.fullName}</td>
                <td>${item.universityCountryNames}</td>
                <td>${item.universityNames}</td>
                <td>${item.eduLevelName}</td>
                <td>${item.specialityNames}</td>
                <td>${item.diplomaYear}</td>
                <td><a title="Преглед" href="${pathPrefix}/control/applications/view?id=${item.applicationId}" target="_blank">
                    <img src="/nacid/img/icon_view.png">
                </a></td>
            </tr>
            <%--
            <span class="fieldlabel">&nbsp;</span>
            <span><a target="_blank" href="${pathPrefix}/control/applications/edit?id=${item.applicationId}">${item.ownerNames} - ${item.universityNames}</a></span>
            <div class="clr"><!--  --></div>
            --%>
        </c:forEach>
        </table>
    </fieldset>
</c:if>