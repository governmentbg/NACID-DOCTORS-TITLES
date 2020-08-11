function changeOrder(form, column, table) {
	var columnName = column.innerHTML;
	form.orderAscending.value = columnName == form.orderCol.value ? (form.orderAscending.value == "false" ? "true" : "false" ) : "true"; 
	form.orderCol.value = columnName;
	doRefresh(form, table);
}
/**
 * dobavq novite marikrani redove kym starite
 t.e. ako ima markirani predvaritelno 0;1;2, no v momenta se vizualizirat redove 20-30, tozi kod shte zapazi starite redove, tyi kato te 
 ne sa v tekushto pokazanite
 dolnata (makriranata versiq na taq funkciq) shteshe da zatrie 0;1;2 zashtoto tq priema za markirani samo tezi koito sa na ekrana (no moje da ima markirani redove, koito prosto da ne se vijdat!!!)....
 */
function setCheckedRows(form, table) {
	$$("#" + table.id +" input[type=checkbox]").each(function (element) {
		var elvalue = parseInt(element.name.replace("rowid_",""));
		if (element.checked) {
			form.checkedRows.value = addElementIdToInput(form.checkedRows.value, elvalue); 
		} else {
			form.checkedRows.value = removeElementIdFromInput(form.checkedRows.value, elvalue);
		}
	});
	return true;
}
/*function setCheckedRows(form) {
	 var result = "";
	 $$("#main_table input[type=checkbox]").each(function (element) {
	 if (element.checked) {
		 result += element.name.replace("rowid_","") + ";";
		 }	  
  });  
	 form.checkedRows.value = result;
	return true;
}*/
function goFirst(form, table) {
	form.rowBegin.value = 0;
	doRefresh(form, table);
}
function goPrevious(form, table) {
	var val = form.rowBegin.value - form.rowsCount.value;
	if (val < 0) {
		val = 0;
	}
	form.rowBegin.value = val;
	doRefresh(form, table);
}
function goNext(form, table) {
	var val = parseInt(form.rowBegin.value) + parseInt(form.rowsCount.value);
	if (val >= form.availableRowsCount.value) {
		val = form.rowBegin.value;
	}
	form.rowBegin.value = val;
	doRefresh(form, table);
}
function goLast(form, table) {
	var rowsCount = parseInt(form.rowsCount.value);
	var availableRowsCount = parseInt(form.availableRowsCount.value);
	var pagesCount = Math.floor((availableRowsCount) / rowsCount);
	var val = availableRowsCount <= (pagesCount) * rowsCount   ? (pagesCount - 1) * rowsCount : (pagesCount) * rowsCount;
	form.rowBegin.value = val;
	doRefresh(form, table);
}
function doRefresh(form, table, updatechecked) {
	form = switchForm(form);
	if (updatechecked == null || updatechecked == true) {
		setCheckedRows(form, table)
	}
	form.submit();
}
//Tazi funkciq razchita che imenata na formite sa vyv vida "....1", kato pyrviq index e 1 (kym momenta ima edna forma nad tablicata i edna pod neq - gornata se kazva tableForm1, a dolnata tableForm2)
//koda nqma da raboti ako ednata forma se prekrysti na "...11" a drugata na "...12"
//ideqta na if-a e ako e submitnata dolnata forma (id=2), togava vsi4ki input-i ot neq da se prehvyrlqt v gornata i da se submitne gornata
//tyi kato v gornata ima osven osnovnite poleta (nachalen red i broi redove) i dinami4nite filtri!
function switchForm(form) {
	if (form.name.indexOf("1") == -1) {
		var f1 = $(form.name.substring(0, form.name.length - 1) + "1");
		f1.onlyChecked.checked = form.onlyChecked.checked;
		f1.rowBegin.value = form.rowBegin.value;
		f1.rowsCount.value = form.rowsCount.value;
		form = f1;
	}
	return form;
}
//Tazi funckiq eventualno shte se polzva ako reshim navsqkyde da minem s ajax pri prezarejdaneto na tablicata - v takyv slu4ai trqbva da se preimenuva na doRefresh()
function doRefresh2(form, table, updatechecked) {
	form = switchForm(form);
	if (updatechecked == null || updatechecked == true) {
		setCheckedRows(form, table)
	}
	var myparams = $(form).serialize();
	myparams += "&inner=1";
	var element = $(table.id.replace("main_table", "table_id"));
	setLoading(element);
	//Izchakva 0.1 mseconds predi da izpylni ajax refresh-a, tyi kato na IE imashe problem i ajax request-a se izpylnqvashe predi setLoading...
	ajaxRefresh.delay(0.1, form, element, myparams);
	return true;
}
function ajaxRefresh(form, element, myparams) {
	new Ajax.Request(
			form.action,
			{
				onSuccess : function(oXHR) {
					$(element).update(oXHR.responseText);
					removeLoading(element);
					
					//Tova e funckiq, koqto se izvikva v commission_applications.jsp i commission_members.jsp
					if (typeof (window.resetUncheckedIds) == 'function') {
						resetUncheckedIds();
					}
					//funkciq, koqto shte se execute-va sled ajax refresh!
					if (typeof (window.postAjaxRefresh) == 'function') {
						postAjaxRefresh();
					}
				},
				onFailure : function(oXHR) {
					alert("Възникна грешка при опит за зареждане на таблицата моля опитайте по-късно..."
							+ oXHR.statusText);
				},
				asynchronous : false,
				method : 'POST',
				parameters : myparams
			});
}
function toggleChecked(table, el) {
	var idx = getCellIndex($(el).up(0)) + 1;
	$$("#" + $(table).id + " tr > td:nth-child(" + idx + ") input[type=checkbox]").each( function(element) {
		element.checked = el.checked;
	});
}
function clearFilters(f,t) {
	f = switchForm(f);
	f.getElements('input').each(function (item){item.clear();});
	f.getElements('select').each(function (item){item.selectedIndex = 0;});
	f.sub.value = 1;
	doRefresh(f,t, false);
}
//21.06.2010 GGeorgiev
//Tyi kato typiq IE7 ne broi hidden kolonite pri presmqtaneto na cell.cellIndex
//namerih nqkyde v net-a reshenie na problema...
function getCellIndex(aCell) {
	aRow = aCell.parentNode;
	for (i = 0; i != aRow.cells.length; i++) {
		if (aRow.cells[i] == aCell) return i;

	}
	return false;
}


function addComboFilterElement(itemName) {
    element = $(itemName + 'Ids').next();
    var txt = element.options[element.selectedIndex].innerHTML;
    var id = element.options[element.selectedIndex].value;
    if(id == '') {
	                    return;
	                }

	                if($(itemName + 'Row' + id) != null) {
	                    return;
	                }
	                
	                $(itemName + 'Ids').value = addElementIdToInput($(itemName + 'Ids').value, id);
	                showHideComboFilterDiv(itemName);
	                
	                var tr = new Element('tr', {'id': itemName + 'Row' + id, 'class' : 'tdfontsize100'});

	                var td1 = new Element('td');

	                td1.insert(txt);
	                
	                var td2 = new Element('td');
	                var a2 = new Element('a', {'href': '#',
	                                            'onclick': 'removeComboFilterItem("'+itemName+'", "'+id+'"); return false;'});
	                var img = new Element('img', {'src': '/nacid/img/icon_delete.png'});
	                a2.insert(img);
	                td2.insert(a2);
	                
	                tr.insert(td1);
	                tr.insert(td2);
	                
	                $(itemName + 'selectedTable').down().insert({bottom: tr});
}
function showHideComboFilterDiv(itemName) {
	if($(itemName + 'Ids').value == '') {
		$(itemName + 'selectedDiv').hide();
	}
	else {
		$(itemName + 'selectedDiv').show();
	}
}
function removeComboFilterItem(name, id) {
    $(name+'Row'+id).remove();
    $(name + 'Ids').value = removeElementIdFromInput($(name + 'Ids').value, id);
    
}



