<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ include file="commission_calendar_header.jsp"%>
<script type="text/javascript">
function doRefresh(form, table, updatechecked) {
	//Pyrvo promenq url-tata na formite i sled tova izpylnqva doRefresh2();
	//Tyi kato po default url-tata na formite za filtrite izglejdat vyv vida ${pathPrefix}/control/applications/list....
	//Trqbvashe da izbiram dali da promenqm url-tata na formite ili tqh da gi ostavq pravilni i da trqbva da promenqm urlatata za application view
	//ot ../control/commission_applications/view?id=? ....na .../control/applications/view?id=?
	changeCommissionMembersFormUrl();
	doRefresh2(form, table, updatechecked);
	resetUncheckedIds();
}
function changeCommissionMembersFormUrl() {
	$('tableForm1').action = '${pathPrefix}/control/commission_members_list/list';
	//$('tableForm2').action = '${pathPrefix}/control/commission_members_list/list';
}
var lastRow = ${commissionParticipationBaseData.commissionMemberIdsCount + 1};
function addRows() {
	var rowsCount = $('main_table').rows[0].cells.length;
	var checkboxesRowId = rowsCount - 2;
	$$("#main_table > tbody > tr").each( function(element) {
		var currentId =  parseInt(element.down(1).innerHTML);

		if (element.down(checkboxesRowId).down() != null && element.down(checkboxesRowId).down().checked && !isCommissionMemberChecked(currentId)) {

            element.down(0).update(lastRow++);
            element.id = "member_" + currentId;

            element.down().next(checkboxesRowId-1).update('<input type="checkbox" value="1" name="member_notified_' + currentId + '">');
            element.down().next(checkboxesRowId-1).removeAttribute("align");
            element.down().next(checkboxesRowId).update('<input type="checkbox" value="1" name="member_participated_' + currentId + '">');

			var td = new Element("td");
			setDeleteButton(td, currentId);
			element.insert( {
				bottom :td
			});
			
			//Syzdava tbody tag ako nqma takyv (ako ne syzdam tbody, ako nqma takova ima problemi sys stilovete pod FF)
			if ($('selectedCommissionMembersmain_table').childElements().length == 1) {
			    $('selectedCommissionMembersmain_table').insert( {
					bottom :new Element("tbody")
				});
			}
			//Noviq red go insertvam v <tbody>-to na tablicata
			$('selectedCommissionMembersmain_table').down(0).next().insert( {
				bottom :element
			});
			
			//Dobavq noviq element kym spisyka "commission_member_ids" na gornata tablica
			$('commission_member_ids').value = addElementIdToInput($('commission_member_ids').value, currentId);
			//Dobavq elementa kym skritite elementi ot dolnata tablica 
			document.tableForm1.uncheckedRows.value = addElementIdToInput(document.tableForm1.uncheckedRows.value, currentId);
		}
		clearCorrectMesageDiv();
		
	});
	doRefresh(document.tableForm1,$('main_table'));
}
function setDeleteButton(element, id) {
	element.update("<a href='javascript:void(0);'><img src='${pathPrefix}\/img\/icon_delete.png' onclick='removeElement(" + id + ")' \/><\/a>");
}
function isCommissionMemberChecked(id) {
	return inputValueToHash($('commission_member_ids').value).get(id);
}

function removeElement(appId) {
	$('commission_member_ids').value = removeElementIdFromInput($('commission_member_ids').value, appId);
	document.tableForm1.uncheckedRows.value = removeElementIdFromInput(document.tableForm1.uncheckedRows.value, appId);
	$("member_" + appId).remove();
	doRefresh(document.tableForm1,$('main_table'));
	resetUncheckedIds();
	clearCorrectMesageDiv();
}

//dobavq id-tata na gornata tablica vyv uncheckedRows input-a na formata ot tablicata
function resetUncheckedIds() {
	document.tableForm1.uncheckedRows.value = "";
	$$('#selectedCommissionMembersmain_table tbody > tr').each(function(row) {
		var id = parseInt(row.down(1).innerHTML);
		document.tableForm1.uncheckedRows.value = addElementIdToInput(document.tableForm1.uncheckedRows.value, id); 
	});
	
}
//Predefiniram clearFilters(f, t) ot table.js, zashtoto v tazi tablica clearFilters ne trqbva da zabyrsva input-a s uncheckedRows
function clearFilters(f,t) {
	f = switchForm(f);
	f.getElements('input').each(function (item){
		if (item.name != "uncheckedRows") {
			item.clear();
		}
	});
 	f.getElements('select').each(function (item){item.selectedIndex = 0;});
	doRefresh(f,t, false);
}

</script>
<h3 class="title"><span>Списък комисия</span></h3>
<h3 class="names">${commission_calendar_header }</h3>

<div class="formClass">
<fieldset ><legend class="noForm">Списък с избрани членове на комисията</legend>
	<nacid:systemmessage name="commissionMembersSaved"/>
	<v:form name="applications_form" method="post" action="${pathPrefix }/control/commission_members_list/save" backurl="${pathPrefix }/control/commission_calendar/list?getLastTableState=1">
		<input type="hidden" name="commission_member_ids" id="commission_member_ids" value="${commissionParticipationBaseData.commissionMemberIds }" />
		<input type="hidden" name="calendar_id" id="calendar_id" value="${commissionParticipationBaseData.calendarId }" />
		<a class="flt_rgt" href="${pathPrefix }/control/commission_members_inform?calendar_id=${commissionParticipationBaseData.calendarId }" onclick="if ($('applications_form').serialize() != form) {alert('${messages.informMembersMustSave}'); return false;} return true;">${messages.informMembers }</a>
		<div class="clr20"><!--  --></div>
		<table width="100%" cellspacing="0" cellpadding="1" border="1" class="sort-table sortable" id="selectedCommissionMembersmain_table">
			<thead>
				<tr>
					<td class="dark unsortable">No</td>
					<td class="dark" style="display:none" >Id</td>
					<td class="dark">Име</td>
					<td class="dark">Фамилия</td>
					<td class="dark">Професионално направление</td>
					<td class="dark">Позиция в комисията</td>
					<%-- <td class="dark">Телефон/и</td>--%>
					<td class="dark unsortable" title="Уведомен">Ув.<br /><input type="checkbox" onclick="toggleChecked($('selectedCommissionMembersmain_table'), this);"></td>
					<td class="dark unsortable" title="Присъствал">Пр.<br /><input type="checkbox" onclick="toggleChecked($('selectedCommissionMembersmain_table'), this);"></td>
					<td width="60" class="dark">&nbsp;</td>
				</tr>
			</thead>
			<nacid:commissionParticipationList>
				<tr id="member_${id }">
					<td>${row_id }</td>
					<td style="display:none">${id }</td>
					<td>${fname }</td>
					<td>${lname }</td>
					<td>${pgname }</td>
					<td>${posname }</td>
					<%-- <td>${phone }</td> --%>
					<td><input type="checkbox" name="member_notified_${id }" value="1" ${notified_checked }/></td>
					<td><input type="checkbox" name="member_participated_${id }" value="1" ${participated_checked }/></td>
					<td><a href='javascript:void(0);'><img src='${pathPrefix}/img/icon_delete.png' onclick='removeElement(${id})' /></a></td>
				</tr>
			</nacid:commissionParticipationList>
		</table>
		
		
		<nacid:list skipFilters="true" attribute="selectedCommissionMembersWebModel" tablePrefix="selectedCommissionMembers"/>
	</v:form>
</fieldset>
</div>
<div class="clr20"><!--  --></div>
<a class="flt_rgt" href="javascript:void(0);" onclick="javascript:addRows();">Добави маркираните записи</a>
<div class="clr20"><!--  --></div>
<div class="formClass">
<fieldset ><legend class="noForm">Списък с всички членове на комисията</legend>
<nacid:list />
</fieldset>
</div>
<script type="text/javascript">
//Reset-va id-tata na gornata tablica - da ne se poqvqvat v dolnata 
resetUncheckedIds();
//Smenq url-tata na formata za vsichki zaqvleniq
changeCommissionMembersFormUrl();
//var form = $('applications_form').serialize();
</script>

<%@ include file="commission_calendar_footer.jsp"%>