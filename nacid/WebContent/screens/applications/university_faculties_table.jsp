<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="nacid_ext" uri="/META-INF/nacid_taglib.tld"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<table id="university_faculties_table" class="sort-table" width="100%" cellspacing="0" cellpadding="1" border="1">
    <thead>
    <tr>
        <td class="dark">Наименование</td>
        <td class="dark">Оригинално наименование</td>
        <td class="dark">от дата</td>
        <td class="dark">до дата</td>
        <td class="dark" width="60">&nbsp;</td>
    </tr>
    </thead>
    <c:forEach var="faculty" items="${universityFacultiesWebModel}">
        <tr>
            <td class="white_bg">${faculty.name}</td>
            <td class="white_bg">${faculty.originalName}</td>
            <td class="white_bg">${faculty.dateFrom}</td>
            <td class="white_bg">${faculty.dateTo}</td>
            <td>
                <a href="javascript: void(0);" onclick="showHideNewFacultyDiv(${faculty.id})" title="Редактиране">
                    <img src="/nacid/img/icon_edit.png">
                </a>
            </td>
        </tr>
    </c:forEach>
</table>