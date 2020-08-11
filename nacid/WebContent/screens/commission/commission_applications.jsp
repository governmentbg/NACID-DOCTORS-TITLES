<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ include file="commission_calendar_header.jsp"%>
<script type="text/javascript">
//Predefiniram funkciqta doRefresh da vika doRefresh2(), koqto refreshva tablicata s ajax
function doRefresh(form, table, updatechecked) {
	//Pyrvo promenq url-tata na formite i sled tova izpylnqva doRefresh2();
	//Tyi kato po default url-tata na formite za filtrite izglejdat vyv vida ${pathPrefix}/control/applications/list....
	//Trqbvashe da izbiram dali da promenqm url-tata na formite ili tqh da gi ostavq pravilni i da trqbva da promenqm urlatata za application view
	//ot ../control/commission_applications/view?id=? ....na .../control/applications/view?id=?
	changeApplicationsFormUrl();
	doRefresh2(form, table, updatechecked);
	return true;
}
//Tyi kato urlatata v tablicata idvat ot vida ...${pathPrefix}/control/applications/list, trqbvashe da izbiram da opravq url-to na formata, ili url-tata na vsi4ki opereacii (v sluchaq samo view)
function changeApplicationsFormUrl() {
	$('tableForm1').action = '${pathPrefix}/control/commission_applications/list?calendar_id=${commissionApplicationsWebModel.calendarId}';
	//$('tableForm2').action = '${pathPrefix}/control/commission_applications/list';
}
var lastRow = ${commissionApplicationsWebModel.applicationIdsCount + 1};
//Dobavq red ot dolnata tablica v gornata, sled koeto refreshva dolnata kato maha dobaveniq red.
var checkboxColId = 11;
function addRows() {
	$$("#main_table > tbody > tr").each( function(element) {
		var currentId =  parseInt(element.down(1).innerHTML);
		//element.down(8).down() se polu4ava null ako primerno sa pokazani pyrvite 2 reda i ima oshte redove za pokazvane, togava na toq red ima ...., t.e. nqma input, koito da e v 8mata kolona
		if (element.down(checkboxColId).down() != null && element.down(checkboxColId).down().checked && !isApplicationChecked(currentId)) {
			
			//element = element.clone(true);
			element.down(0).update(lastRow++);
			element.id = "application_" + currentId;
            
			element.down(checkboxColId).remove(); //Maha kolonata s checkbox-a ot osnovnata tablica
			//Regenerira kolonata s edit/view/delete butonite, kato ostavq samo delete - za iztrivane na dobaveniq red
			setDeleteButton(element.down(checkboxColId), currentId);
			//element.down(8).update("<a href='javascript:void(0);'><img src='${pathPrefix}\/img\/icon_delete.png' onclick='removeElement(" + currentId + ",this.up(2))' \/><\/a>");

			//Syzdava tbody tag ako nqma takyv (ako ne syzdam tbody, ako nqma takova ima problemi sys stilovete pod FF)
			if ($('selectedApplicationsmain_table').childElements().length == 1) {
			    $('selectedApplicationsmain_table').insert( {
					bottom :new Element("tbody")
				});
			}
			//Noviq red go insertvam v <tbody>-to na tablicata
			$('selectedApplicationsmain_table').down(0).next().insert( {
				bottom :element
			});
			//Dobavq noviq element kym spisyka "application_ids" na gornata tablica
			$('application_ids').value = addElementIdToInput($('application_ids').value, currentId);
			//Dobavq elementa kym skritite elementi ot dolnata tablica 
			document.tableForm1.uncheckedRows.value = addElementIdToInput(document.tableForm1.uncheckedRows.value, currentId);
		}
		clearCorrectMesageDiv();
		
	});
	

	//Nqma nujda ot doRefresh ve4e zashtoto az napravo premestvam zapisite ot dolu gore i dolnata tablica si ostava samo s redovete, koito trqbvat
	//no vse pak shte go sloja zashtoto broikata zapisi: x (obshto y) ne se refreshvat....
	doRefresh(document.tableForm1,$('main_table'));
}
function setDeleteButton(element, id) {
	element.update("<a href='javascript:void(0);'><img src='${pathPrefix}\/img\/icon_delete.png' onclick='removeElement(" + id + ")' \/><\/a>");
}
function isApplicationChecked(id) {
	return inputValueToHash($('application_ids').value).get(id);
}
//maha element ot gornata tablica pri natiskane na delete buton-a na reda, sled koeto refreshva dolnata tablica s ajax 
function removeElement(appId) {
	$('application_ids').value = removeElementIdFromInput($('application_ids').value, appId);
	document.tableForm1.uncheckedRows.value = removeElementIdFromInput(document.tableForm1.uncheckedRows.value, appId);
	$("application_" + appId).remove();
	if (doRefresh(document.tableForm1,$('main_table'))) {
		resetUncheckedIds();
	}
	clearCorrectMesageDiv();
}
//dobavq id-tata na gornata tablica vyv uncheckedRows input-a na formata ot tablicata
function resetUncheckedIds() {
	document.tableForm1.uncheckedRows.value = "";
	$$('#selectedApplicationsmain_table tbody > tr').each(function(row) {
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

<h3 class="title"><span>Заявления</span></h3>
<h3 class="names">${commission_calendar_header }</h3>
<div class="formClass">
<fieldset ><legend class="noForm">Списък с избрани заявления</legend>
	<nacid:systemmessage name="applicationsSaved"/>
	<v:form name="applications_form" method="post" action="${pathPrefix }/control/commission_applications/save" backurl="${pathPrefix }/control/commission_calendar/list?getLastTableState=1">
		<input type="hidden" name="application_ids" id="application_ids" value="${commissionApplicationsWebModel.applicationIds }" />
		<input type="hidden" name="calendar_id" id="calendar_id" value="${commissionApplicationsWebModel.calendarId }" />
		<input type="hidden" name="commission_apps_edit" value="1" />
		<nacid:list skipFilters="true" attribute="selectedApplicationsWebModel" tablePrefix="selectedApplications" elementClass="sortable"/>
	</v:form>
</fieldset>
</div>
<div class="clr20"><!--  --></div>
<a class="flt_rgt" href="javascript:void(0);" onclick="javascript:addRows();">Добави маркираните записи</a>
<!--  <input type="button" class="submit flt_rgt" onclick="javascript:addRows();" value="Добави маркираните записи" /> -->
<div class="clr20"><!--  --></div>
<div class="formClass">
<fieldset ><legend class="noForm">Списък с всички заявления</legend>
<nacid:list />
</fieldset>
</div>
<script type="text/javascript">
//Update-va delete butona na tablicata sys selektnatite applications
$$('#selectedApplicationsmain_table tbody > tr').each(function(row) {
	var id = parseInt(row.down(1).innerHTML);
	row.id = "application_" + id;
	setDeleteButton(row.down(checkboxColId), row.down(1).innerHTML);
  });
//Reset-va id-tata na gornata tablica - da ne se poqvqvat v dolnata 
resetUncheckedIds();
//Smenq url-tata na formata za vsichki zaqvleniq
changeApplicationsFormUrl();
$$('#selectedApplicationsmain_table thead > tr > td:nth-child(1)').each(function(element) {
	 element.addClassName("unsortable");
});	
//alert($$('#selectedApplicationsmain_table thead > tr > td:nth-child(0)')[0].innerHTML);
</script>

<%@ include file="commission_calendar_footer.jsp"%>