<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ include file="/screens/header.jsp"%>
<c:choose>
	<c:when test="${commissionMembersInformWebModel.commissionMembers != null}">
		<h3 class="title"><span>Бяха уведомени следните членове на комисията</span></h3>
		<div class="clr20"><!--  --></div>
		<table width="100%" cellspacing="0" cellpadding="1" border="1" class="sort-table" id="selectedCommissionMembersmain_table">
			<thead>
				<tr>
					<td class="dark">No</td>
					<td class="dark">Id</td>
					<td class="dark">Име</td>
					<td class="dark">Фамилия</td>
					<td class="dark">Професионално направление</td>
					<td class="dark">Позиция в комисията</td>
					<td class="dark">Телефон/и</td>
					<td class="dark">Ел. поща</td>
				</tr>
			</thead>
			<c:forEach var="cm" items="${commissionMembersInformWebModel.commissionMembers}" varStatus="status">
				<tr>
					<td>${status.count }</td>
					<td>${cm.id }</td>
					<td>${cm.fname }</td>
					<td>${cm.lname }</td>
					<td>${cm.profGroupName }</td>
					<td>${cm.commissionPositionName }</td>
					<td>${cm.phone }</td>
					<td>${cm.email }</td>
				</tr>
			</c:forEach>
		</table>
	</c:when>
	<c:otherwise>
		Няма експерти, които могат да бъдат уведомени
	</c:otherwise>
</c:choose>
<div class="clr20"><!--  --></div>
<a class="flt_rgt" href="${pathPrefix }/control/commission_members_list/edit/?calendar_id=${commissionMembersInformWebModel.calendarId}" >Назад</a>

<%@ include file="/screens/footer.jsp"%>