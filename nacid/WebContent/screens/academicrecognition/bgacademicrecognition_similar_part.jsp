<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"  %>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<h4>Намерени подобни признати дипломи</h4>
<table class="sort-table " width="100%" cellspacing="0" cellpadding="1"
	border="1">
	<thead>
		<tr>
			<th></th>
			<th>Решение на БВУ</th>
			<th>Заявител</th>
			<th>Университет</th>
			<th>Държава на ВУ</th>
			<th>Придобита ОКС</th>
			<th>Специалност по диплома</th>
			<th>Номер на диплома</th>
			<th>Дата на диплома</th>
			<th>Статус</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${similar }" var="s">
			<tr>
				<td><input type="checkbox" value="${s.id }"
					class="similar_checkbox" onchange="setChosen(${s.id })" /></td>
				<td>${s.recognizedUniversityName }</td>
				<td>${s.applicant }</td>
				<td>${s.university }</td>
				<td>${s.universityCountry }</td>
				<td>${s.educationLevel }</td>
				<td>${s.diplomaSpeciality}</td>
				<td>${s.diplomaNumber }</td>
				<td>${s.diplomaDate }</td>
				<td>${s.recognitionStatusName }</td>
			</tr>
		</c:forEach>
	</tbody>
</table>