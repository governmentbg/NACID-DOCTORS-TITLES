<%@page import="com.nacid.bl.nomenclatures.NomenclaturesDataProvider"%>
<%@page import="com.nacid.bl.regprof.ProfessionalInstitutionDataProvider"%>
<%@page import="com.nacid.bl.nomenclatures.regprof.ProfessionExperienceDocumentType"%>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<h3 class="title"><span>${messages.applicationdata }</span></h3>
<h3 class="names">${application_header }</h3>
<script type="text/javascript">
var datesCount = new Hash();
/**
 * filling doctypesforcalc.set values...
 */
var doctypesforcalc = new Hash();
<c:forEach var="entry" items="${docTypeForExperienceCalculation}">
	doctypesforcalc.set(${entry.key}, ${entry.value});
</c:forEach>

var docTypeUP3 = parseInt(<%= ProfessionExperienceDocumentType.DOC_TYPE_UP_3 %>);
var secondarySpecialitiesCount = ${secondarySpecialitiesCount} + 0;
var higherSpecialitiesCount = ${higherSpecialitiesCount} + 0;
var sdkSpecialitiesCount = ${sdkSpecialitiesCount} + 0;
var higherProfessionalInstitutionType = <%= Integer.toString(ProfessionalInstitutionDataProvider.HIGHER_INSTITUTION_TYPE) %>;

function changeCertificateQualification() {
	if ($('submit_education').checked == true) {
		if ($('submit_experience').checked == true && $('certificate_qualification').value == $('professionName').value) {
			return;
		}
		if ($('div_sredno').visible()) {
			$('certificate_qualification').value = $('sec_qualification').value;
		} else if ($('div_visshe').visible()) {
			$('certificate_qualification').value = $('high_qualification').value;
		} else if ($('div_SDK').visible()) {
			$('certificate_qualification').value = $('sdk_qualification').value;
		}
	} else if ($('submit_experience').checked == true) {
		$('certificate_qualification').value = $('professionName').value;
	}
}

function additionalInputsValidation() {
	var ret = true;
	if ($('submit_education').checked == true) {
		ret = validateEducationDivs() && newNomenclaturesValidation() && ret;
		/*if ($('div_SDK').visible() && $('high_sdk_document_date').value && $('details.sdkDocumentDate').value) {
			ret = validateDateInterval($('high_sdk_document_date'), $('details.sdkDocumentDate'), 'd.m.yyyy', 'd.m.yyyy', 'Грешен период', true) && ret;
		}*/
	}
	if ($('submit_experience').checked == true) {
		ret = validateExperienceDocuments(true) && ret;
	}
	/*if ($('certificate_qualification').value != '' && $('details.certificateProfQualificationId').value.empty()) {
		setErrorOnInput($('certificate_qualification'), 'Трябва да изберете професионална квалификация от падащото меню или да маркирате професионалната квалификация за нов запис!');
		ret = false;
	}*/
	return ret;
}
function validateEducationDivs() {
	var ret = true;
	if ($('div_sredno').visible()) {
		ret = validateText($('sec_qualification'), true, 2, 200, null, '') && ret;
		ret = validateText($('sec_speciality'), ($('secondary_specialities_table').rows.length == 0 || !$('sec_speciality').value.empty()), 2, 150, null, '') && ret;
		ret = validateText($('sec_document_series'), !$('sec_document_series').value.empty(), -1, 32, null, '') && ret;
		ret = validateText($('sec_document_number'), !$('sec_document_number').value.empty(), -1, 32, null, '') && ret;
		ret = validateText($('sec_document_reg_number'), !$('sec_document_reg_number').value.empty(), -1, 32, null, '') && ret;
		ret = validateText($('sec_document_date'), !$('sec_document_date').value.empty(), -1, 50, null, '') && ret;
	} else if ($('div_visshe').visible()) {
		if (!($('high_org_name').value.empty())) {
			ret = validateText($('high_org_name'), true, 2, 255, null, '') && ret;
		}
		ret = validateText($('high_bg_name'), true, 2, 255, null, '') && ret;
		ret = validateText($('high_qualification'), true, 2, 200, null, '') && ret;
		ret = validateText($('high_speciality'), ($('higher_specialities_table').rows.length == 0 || !$('high_speciality').value.empty()), 2, 150, null, '') && ret;
		ret = validateText($('high_document_series'), !$('high_document_series').value.empty(), -1, 32, null, '') && ret;
		ret = validateText($('high_document_number'), !$('high_document_number').value.empty(), -1, 32, null, '') && ret;
		ret = validateText($('high_document_reg_number'), !$('high_document_reg_number').value.empty(), -1, 32, null, '') && ret;
		ret = validateText($('high_document_date'), !$('high_document_date').value.empty(), -1, 50, null, '') && ret;
	} else if ($('div_SDK').visible()) {
		if (!($('high_sdk_org_name').value.empty())) {
			ret = validateText($('high_sdk_org_name'), true, 2, 255, null, '') && ret;
		}
		ret = validateText($('high_sdk_bg_name'), true, 2, 255, null, '') && ret;
		ret = validateText($('high_sdk_qualification'), true, 2, 200, null, '') && ret;
		ret = validateText($('high_sdk_speciality'), ($('higher_sdk_specialities_table').rows.length == 0 || !$('high_sdk_speciality').value.empty()), 2, 150, null, '') && ret;
		ret = validateText($('high_sdk_document_series'), !$('high_sdk_document_series').value.empty(), -1, 32, null, '') && ret;
		ret = validateText($('high_sdk_document_number'), !$('high_sdk_document_number').value.empty(), -1, 32, null, '') && ret;
		ret = validateText($('high_sdk_document_reg_number'), !$('high_sdk_document_reg_number').value.empty(), -1, 32, null, '') && ret;
		ret = validateText($('high_sdk_document_date'), !$('high_sdk_document_date').value.empty(), -1, 50, null, '') && ret;
	}
	else return true;
	return ret;
}

function submitFunction() {
	if ($('submit_experience').checked == true) {
		calculatePeriod(false);
	}
}

function newNomenclaturesValidation() {
	var ret = true;
	if ($('div_sredno').visible()) {
		if ($('sec_qualification').value == "" && $('details.secProfQualificationId').value == "") {
			setErrorOnInput($('sec_qualification'), "Трябва да въведете име, за да създадете нов запис в номенклатура \"Професионална квалификация - обучение\"!");
	        ret = false;
		} else if ($('sec_qualification').value != "" && $('details.secProfQualificationId').value == "") {
			setErrorOnInput($('sec_qualification'), "Трябва да изберете професионална квалификация от падащото меню или да маркирате професионалната квалификация за нов запис!");
	        ret = false;
		}
		if ($('sec_speciality').value == "" && $('secSpecialityId').value == "" && $('secondary_specialities_table').rows.length == 0) {
			setErrorOnInput($('sec_speciality'), "Трябва да въведете име, за да създадете нов запис в номенклатура \"Специалност - обучение\"!");
	        ret = false;
		} else if ($('sec_speciality').value != "" && $('secSpecialityId').value == "") {
			setErrorOnInput($('sec_speciality'), "Трябва да изберете специалност от падащото меню или да маркирате специалността за нов запис!");
	        ret = false;
		}
	} else if ($('div_visshe').visible()) {
		if ($('high_qualification').value == "" && $('details.highProfQualificationId').value == "") {
			setErrorOnInput($('high_qualification'), "Трябва да въведете име, за да създадете нов запис в номенклатура \"Професионална квалификация - висше\"!");
	        ret = false;
		} else if ($('high_qualification').value != "" && $('details.highProfQualificationId').value == "") {
			setErrorOnInput($('high_qualification'), "Трябва да изберете професионална квалификация от падащото меню или да маркирате професионалната квалификация за нов запис!");
	        ret = false;
		}
		if ($('high_speciality').value == "" && $('highSpecialityId').value == "" && $('higher_specialities_table').rows.length == 0) {
			setErrorOnInput($('high_speciality'), "Трябва да въведете име, за да създадете нов запис в номенклатура \"Специалност - висше\"!");
	        ret = false;
		} else if ($('high_speciality').value != "" && $('highSpecialityId').value == "") {
			setErrorOnInput($('high_speciality'), "Трябва да изберете специалност от падащото меню или да маркирате специалността за нов запис!");
	        ret = false;
		}
	} else if ($('div_SDK').visible()) {
		if ($('high_sdk_qualification').value == "" && $('details.highProfQualificationId').value == "") {
			setErrorOnInput($('high_sdk_qualification'), "Трябва да въведете име, за да създадете нов запис в номенклатура \"Професионална квалификация - висше\"!");
	        ret = false;
		} else if ($('high_sdk_qualification').value != "" && $('details.highProfQualificationId').value == "") {
			setErrorOnInput($('high_sdk_qualification'), "Трябва да изберете професионална квалификация от падащото меню или да маркирате професионалната квалификация за нов запис!");
	        ret = false;
		}
		if ($('high_sdk_speciality').value == "" && $('highSpecialityId').value == "" && $('higher_sdk_specialities_table').rows.length == 0) {
			setErrorOnInput($('high_sdk_speciality'), "Трябва да въведете име, за да създадете нов запис в номенклатура \"Специалност - висше\"!");
	        ret = false;
		} else if (($('high_sdk_speciality').value != "" && $('highSpecialityId').value == "")) {
			setErrorOnInput($('high_sdk_speciality'), "Трябва да изберете специалност от падащото меню или да маркирате специалността за нов запис!");
	        ret = false;
		}
		if ($('sdk_qualification').value == "" && $('details.sdkProfQualificationId').value == "") {
			setErrorOnInput($('sdk_qualification'), "Трябва да въведете име, за да създадете нов запис в номенклатура \"Професионална квалификация - висше\"!");
	        ret = false;
		} else if ($('sdk_qualification').value != "" && $('details.sdkProfQualificationId').value == "") {
			setErrorOnInput($('sdk_qualification'), "Трябва да изберете професионална квалификация от падащото меню или да маркирате професионалната квалификация за нов запис!");
	        ret = false;
		}
		/*if ($('sdk_speciality').value == "" && $('sdkSpecialityId').value == "" && $('sdk_specialities_table').rows.length == 0) {
			setErrorOnInput($('sdk_speciality'), "Трябва да въведете име, за да създадете нов запис в номенклатура \"Специалност - висше\"!");
	        ret = false;
		} else if (($('sdk_speciality').value != "" && $('sdkSpecialityId').value == "")) {
			setErrorOnInput($('sdk_speciality'), "Трябва да изберете специалност от падащото меню или да маркирате специалността за нов запис!");
	        ret = false;
		}*/
	}
	return ret;
}

function updateSecSpecialityId(id) {
	$('secSpecialityId').value = $('record_id_' + id).innerHTML;
}

function updateHighSpecialityId(id) {
	$('highSpecialityId').value = $('record_id_' + id).innerHTML;
}

function updateSecProfQualification(id) {
	$('details.secProfQualificationId').value = $('record_id_' + id).innerHTML;
}

function updateHighProfQualification(id) {
	$('details.highProfQualificationId').value = $('record_id_' + id).innerHTML;
}

function updateCertificateProfQualification(id) {
	$('details.certificateProfQualificationId').value = $('record_id_' + id).innerHTML;
}

function updateSdkSpecialityId(id) {
	$('sdkSpecialityId').value = $('record_id_' + id).innerHTML;
}

function updateSdkProfQualification(id) {
	$('details.sdkProfQualificationId').value = $('record_id_' + id).innerHTML;
}

function resetSecondarySpecialities() {
	$('secondary_specialities_table').innerHTML = '';
	secondarySpecialitiesCount = 0;
	$('secondary_specialities_count').value = secondarySpecialitiesCount;
	$('sec_speciality').value = '';
	$('secSpecialityId').value = '';
}

function addSecondarySpeciality() {
	if (validateText($('sec_speciality'), true, 2, 150, null, '') && validateSecondarySpeciality()) {
		clearInputError('sec_speciality');
    	var table = $('secondary_specialities_table');
    	var rowCount = table.rows.length;
    	var row = table.insertRow(rowCount);
    	var specialityName = '';
    	
    	var cell1 = row.insertCell(0);
    	var specialityIdField = new Element('input', { 'type': 'hidden'});
    	specialityIdField.name = 'speciality_id' + rowCount;
    	cell1.style.display = 'none';

		var cell2 = row.insertCell(1);

		specialityIdField.value = $('secSpecialityId').value;
		//specialityName = $('secSpecialityId').options[$('secSpecialityId').selectedIndex].text;
		specialityName = $('sec_speciality').value;

		cell1.appendChild(specialityIdField);
		cell2.innerHTML = specialityName + ', ' + $('sec_qualification_degree').innerHTML;

		var cell3 = row.insertCell(2);
		var deleteButton = new Element('a', {'href': 'javascript:void(0);'});
		deleteButton.onclick = function() {deleteSecondarySpeciality(this.parentNode);};
		var img = new Element('img', {'src': '${pathPrefix}/img/icon_delete.png'});
		deleteButton.insert(img);
		cell3.appendChild(deleteButton);

		secondarySpecialitiesCount++;
		$('secondary_specialities_count').value = secondarySpecialitiesCount;
		$('sec_speciality').value = '';
		$('secSpecialityId').value = '';
	}
}

function addHigherSpeciality() {
	if (validateText($('high_speciality'), true, 2, 150, null, '') && validateHigherSpeciality()) {
		clearInputError('high_speciality');
    	var table = $('higher_specialities_table');
    	var rowCount = table.rows.length;
    	var row = table.insertRow(rowCount);
    	var specialityName = '';
    	
    	var cell1 = row.insertCell(0);
    	var specialityIdField = new Element('input', { 'type': 'hidden'});
    	specialityIdField.name = 'speciality_id' + rowCount;
    	cell1.style.display = 'none';
    	
		var cell2 = row.insertCell(1);
		
		specialityIdField.value = $('highSpecialityId').value;
		specialityName = $('high_speciality').value;
		
		cell1.appendChild(specialityIdField);
		cell2.innerHTML = specialityName;
		
		var cell3 = row.insertCell(2);
		var deleteButton = new Element('a', {'href': 'javascript:void(0);'});
		deleteButton.onclick = function() {deleteHigherSpeciality(this.parentNode);};
		var img = new Element('img', {'src': '${pathPrefix}/img/icon_delete.png'});
		deleteButton.insert(img);
		cell3.appendChild(deleteButton);
		
		higherSpecialitiesCount++;
		$('higher_specialities_count').value = higherSpecialitiesCount;
		$('high_speciality').value = '';
		$('highSpecialityId').value = '';
	}
}

function addHigherOrSdkSpeciality(sdk) {
	var id = sdk ? 'sdk_speciality' : 'high_sdk_speciality';
	var table = sdk ? $('sdk_specialities_table') : $('higher_sdk_specialities_table');
	var count = sdk ? 'sdk_specialities_count' : 'higher_specialities_count';
	if (validateText($(id), true, 2, 150, null, '') && validateHigherOrSdkSpeciality(sdk)) {
		clearInputError(id);
    	var rowCount = table.rows.length;
    	var row = table.insertRow(rowCount);
    	var specialityName = '';
    	
    	var cell1 = row.insertCell(0);
    	var specialityIdField = new Element('input', { 'type': 'hidden'});
    	var prefix = sdk ? 'sdk_' : '';
    	specialityIdField.name = prefix + 'speciality_id' + rowCount;
    	cell1.style.display = 'none';
    	
		var cell2 = row.insertCell(1);
		
		specialityIdField.value = sdk ? $('sdkSpecialityId').value : $('highSpecialityId').value;
		specialityName = $(id).value;
		
		cell1.appendChild(specialityIdField);
		cell2.innerHTML = specialityName;
		
		var cell3 = row.insertCell(2);
		var deleteButton = new Element('a', {'href': 'javascript:void(0);'});
		deleteButton.onclick = function() {deleteHigherOrSdkSpeciality(this.parentNode, sdk);};
		var img = new Element('img', {'src': '${pathPrefix}/img/icon_delete.png'});
		deleteButton.insert(img);
		cell3.appendChild(deleteButton);
		if (sdk) {
			sdkSpecialitiesCount++;
			$('sdkSpecialityId').value = ''; //RayaAdded-----------------------------------------------------
		} else {
			higherSpecialitiesCount++;
			$('highSpecialityId').value = ''; //RayaAdded-----------------------------------------------------
		}
		$(count).value = sdk ? sdkSpecialitiesCount : higherSpecialitiesCount;
		$(id).value = '';
	}
}

function deleteSecondarySpeciality(tableData) {
	if (secondarySpecialitiesCount > 0) {
		var table = $('secondary_specialities_table');
		var rowNumber = parseInt(tableData.parentNode.rowIndex);
		table.deleteRow(rowNumber);
		secondarySpecialitiesCount--;
		$('secondary_specialities_count').value = secondarySpecialitiesCount;
		iterateTable(table, false);
	}
}

function deleteHigherSpeciality(tableData) {
	if (higherSpecialitiesCount > 0) {
		var table = $('higher_specialities_table');
		var rowNumber = parseInt(tableData.parentNode.rowIndex);
		table.deleteRow(rowNumber);
		higherSpecialitiesCount--;
		$('higher_specialities_count').value = higherSpecialitiesCount;
		iterateTable(table, false);
	}
}

function deleteHigherOrSdkSpeciality(tableData, sdk) {
	var count = sdk ? sdkSpecialitiesCount : higherSpecialitiesCount;
	var id = sdk ? 'sdk_specialities_count' : 'higher_specialities_count';
	if (count > 0) {
		var table = sdk ? $('sdk_specialities_table') : $('higher_sdk_specialities_table');
		var rowNumber = parseInt(tableData.parentNode.rowIndex);
		table.deleteRow(rowNumber);
		if (sdk) {
			sdkSpecialitiesCount--;
		} else {
			higherSpecialitiesCount--;
		}
		$(id).value = sdk ? sdkSpecialitiesCount : higherSpecialitiesCount;
		iterateTable(table, sdk);
	}
}

function iterateTable(table, sdk) {
	var prefix = sdk ? 'sdk_' : '';
	var rowCount = table.rows.length;
	for (var i = 0; i < rowCount; i++) {
		var row = table.rows[i];
		row.cells[0].children[0].name = prefix + 'speciality_id' + i;
	}
}

function validateSecondarySpeciality() {
	if ($('secSpecialityId').value.empty()) {
		setErrorOnInput('sec_speciality', 'Моля изберете специалност от падащото меню или въведете нова в номенклатурата!')
		return false;
	}
    var table = $('secondary_specialities_table');
    var rowCount = table.rows.length;
    if (rowCount > 0 && $('sec_speciality').value != '') {
		for (var i = 0; i < rowCount; i++) {
			var row = table.rows[i];
			if (row.cells[0].children[0].value == $('secSpecialityId').value) {
				setErrorOnInput('sec_speciality', 'Специалността вече е добавена в списъка с избрани специалности!');
				return false;
			}
		}
    }
    clearInputError('sec_speciality');
	return true;
}

function validateHigherSpeciality() {
	if ($('highSpecialityId').value == '') {
		setErrorOnInput('high_speciality', 'Моля изберете специалност от падащото меню или въведете нова в номенклатурата!')
		return false;
	}
	var table = $('higher_specialities_table');
	var rowCount = table.rows.length;
    if (rowCount > 0 && $('high_speciality').value != '') {
		for (var i = 0; i < rowCount; i++) {
			var row = table.rows[i];
			if (row.cells[0].children[0].value == $('highSpecialityId').value) {
				setErrorOnInput('high_speciality', 'Специалността вече е добавена в списъка с избрани специалности!');
				return false;
			}
		}
    }
    clearInputError('high_speciality');
	return true;
}

function validateHigherOrSdkSpeciality(sdk) {
	var id = sdk ? 'sdkSpecialityId' : 'highSpecialityId';
	var speciality = sdk ? 'sdk_speciality' : 'high_sdk_speciality';
	if ($(id).value == '') {
		setErrorOnInput(speciality, 'Моля изберете специалност от падащото меню или въведете нова в номенклатурата!');
		return false;
	}
	var table = sdk ? $('sdk_specialities_table') : $('higher_sdk_specialities_table');
	var rowCount = table.rows.length;
	
	if (rowCount > 0 && $(speciality).value != '') {
		for (var i = 0; i < rowCount; i++) {
			var row = table.rows[i];
			if (row.cells[0].children[0].value == $(id).value) {
				setErrorOnInput(speciality, 'Специалността вече е добавена в списъка с избрани специалности!');
				return false;
			}
		}
	}
	clearInputError(speciality);
	return true;
}

function disableEducationDiv(educationDiv) {
	$$('#' + educationDiv + ' input').each(function(el) {
		el.disabled = true;
	});
	$$('#' + educationDiv + ' select').each(function(el) {
		el.disabled = true;
	});
}

function enableEducationDiv(educationDiv) {
	$$('#' + educationDiv + ' input').each(function(el) {
		if (el.parentNode.nodeName != 'TD' || el.name.indexOf('speciality_id') == 0) {
			el.disabled = false;
		}
	});
	$$('#' + educationDiv + ' select').each(function(el) {
		el.disabled = false;
	});
}

function showHideEduDivs() {
	var eduCombo = $('details.educationTypeId');
	if (eduCombo.value == 1) {
		$('div_visshe').show();
		$('div_sredno').hide();
		$('div_SDK').hide();
		disableEducationDiv('div_sredno');
		disableEducationDiv('div_SDK');
		enableEducationDiv('div_visshe');
		higherSpecialitiesCount = $('higher_specialities_table').rows.length;
	} else if (eduCombo.value == 2 || eduCombo.value == 4) {
		$('div_visshe').hide();
		$('div_sredno').show();
		$('div_SDK').hide();
		disableEducationDiv('div_visshe');
		disableEducationDiv('div_SDK');
		enableEducationDiv('div_sredno');
	} else if (eduCombo.value == 3) {
		$('div_visshe').hide();
		$('div_sredno').hide();
		$('div_SDK').show();
		disableEducationDiv('div_sredno');
		disableEducationDiv('div_visshe');
		enableEducationDiv('div_SDK');
		higherSpecialitiesCount = $('higher_sdk_specialities_table').rows.length;
		//Internet Explorer fieldset height fix
		$('right_fieldset').style.width = '390px';
		$('left_fieldset').style.width = '390px';
	} else {
		$('div_visshe').hide();
		$('div_sredno').hide();
		$('div_SDK').hide();
		disableEducationDiv('div_sredno');
		disableEducationDiv('div_visshe');
		disableEducationDiv('div_SDK');
	}
}

function adjustFieldsetHeight() {
	if (parseInt($('left_fieldset').clientHeight) < parseInt($('right_fieldset').clientHeight)) {
		var height = parseInt($('right_fieldset').clientHeight) - 10;
		$('left_fieldset').style.height = height + 'px';
	} else if (parseInt($('left_fieldset').clientHeight) > parseInt($('right_fieldset').clientHeight)) {
		var height = parseInt($('left_fieldset').clientHeight) - 10;
		$('right_fieldset').style.height = height + 'px';
	}
}
</script>

<v:form name="appform2" action="${pathPrefix }/control/regprofapplication/save?appId=${id}" method="post" 
	  backurl="${pathPrefix }/control/${back_screen}/list?getLastTableState=1&appNumber=${sessionScope.app_number_filter_value }" additionalvalidation="additionalInputsValidation()"
	  modelAttribute="com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseImpl"
	  onsubmit="submitFunction();">
	<nacid:systemmessage name="trainingCourseSystemMessage" />

	<v:hidden id="activeForm" value="2" name="activeForm"/>
	<v:hidden id="experienceRecord.id" path="experienceRecord.id" name="experienceRecord.id"/>
	<v:hidden id="experienceRecord.trainingCourseId" path="experienceRecord.trainingCourseId" name="experienceRecord.trainingCourseId"/>
	<v:hidden id="experienceRecord.years" path="experienceRecord.years" name="experienceRecord.years"/>
	<v:hidden id="experienceRecord.months" path="experienceRecord.months" name="experienceRecord.months"/>
	<v:hidden id="experienceRecord.days" path="experienceRecord.days" name="experienceRecord.days"/>
	<v:hidden id="details.id" path="details.id" name="details.id" />
	
	<div class="clr20"><!--  --></div> 
	 
	<fieldset><legend><v:checkboxinput id="submit_education" name="details.hasEducation" path="details.hasEducation" onclick="toggleSubmitDivs();"/>Обучение</legend>
		<div id="education">
		
		<v:hidden id="details.profInstitutionId" path="details.profInstitutionId" name="details.profInstitutionId"/>
		<v:hidden id="details.profInstitutionOrgNameId" path="details.profInstitutionOrgNameId" name="details.profInstitutionOrgNameId"/>
		
		<v:hidden id="details.highProfQualificationId" path="details.highProfQualificationId" name="details.highProfQualificationId"/>
        <v:hidden id="highSpecialityId" name="highSpecialityId"/>
        
        <div class="clr10"><!--  --></div>      
        <p><span class="fieldlabel2"><label for="">Вид обучение</label><br/></span>
			<nacid:combobox id="details.educationTypeId" name="details.educationTypeId" attributeName="educationTypeCombo" path="details.educationTypeId"
							style="brd w500" onchange="showHideEduDivs();"/>
			<v:comboBoxValidator input="details.educationTypeId" required="$('education').visible()"/>
        </p>
        <div class="clr5"><!--  --></div>
        <script type="text/javascript">
        	
        </script>
        <div id="div_sredno" style="display:none;">
        	        
        	<fieldset><legend>Обучаваща институция</legend>      	
			
			<p><span class="fieldlabel2"><label for="secBgName">Ново име</label><br/></span>
               	<v:textinput id="secBgName" name="secBgName" value="${secBgName}" class="brd w600" onkeydown="if(!isSpecialKeyPressed(event)) resetField('details.profInstitutionId');"/>
               	<v:textValidator input="secBgName" minLength="2" maxLength="255" required="$('div_sredno').visible()"/>
            </p>
			
       		<p><span class="fieldlabel2"><label for="secOrgName">Старо име</label><br/></span>
               	<v:textinput id="secOrgName" name="secOrgName" value="${secOrgName}" class="brd w600" onkeydown="if(!isSpecialKeyPressed(event)) resetField('details.profInstitutionOrgNameId');"/>
               	<v:textValidator input="secOrgName" minLength="2" maxLength="255" required="($('div_sredno').visible() && !($('secOrgName').value.empty()))"/>
            </p>
            <div class="clr"><!--  --></div>

            </fieldset>
            <div class="clr15"><!--  --></div>
               	<%-- <nacid:combobox id="details.secProfQualificationId" path="details.secProfQualificationId" name="details.secProfQualificationId"
               					onchange="getSecSpecialities(); resetSecondarySpecialities(); changeCertificateQualification();"
               					attributeName="secProfQualificationCombo" style="brd w600"/>
               	<v:comboBoxValidator input="details.secProfQualificationId" required="$('div_sredno').visible()"/>--%>
               	<%-- <v:textinput id="sec_qualification" name="sec_qualification" value="${qualificationAsProfession }" class="brd w500" 
               				 onkeydown="if(!isSpecialKeyPressed(event)){ resetField('details.highProfQualificationId'); resetSecondarySpecialities(); };"
               				 onblur="changeCertificateQualification();" />--%>
        		<v:hidden id="details.secProfQualificationId" path="details.secProfQualificationId" name="details.secProfQualificationId" />
        		<v:secQualificationInput onkeydown="if(!isSpecialKeyPressed(event)){ resetField('details.secProfQualificationId'); resetSecondarySpecialities(); };"
        			fieldName="Професионална квалификация по документи" onsuccess="updateSecProfQualification(id); changeCertificateQualification();" 
        			id="sec_qualification" class="brd w450 flt_lft" value="${qualificationAsProfession}" onblur="changeCertificateQualification();" />
            
			<div class="clr10"><!--  --></div>
			<p>
				<span class="fieldlabel"><label>Избрани специалности</label></span>
			</p>
			
			<table id="secondary_specialities_table" border="0">
				<c:forEach var="item" items="${secondarySpecialitiesList}" varStatus="varStatus">
					<tr>
						<td style="display:none;"><input type="hidden" name="speciality_id${varStatus.index}" value="${item.secondarySpecialityId}" /></td>
						<td>${item.secondarySpecialityName}, ${item.secondaryQualificationDegree}</td>
						<td><a href="javascript:void(0);" onclick="deleteSecondarySpeciality(this.parentNode);"><img src="${pathPrefix}/img/icon_delete.png"/></a></td>
					</tr>
				</c:forEach>
			</table>
			<input type="hidden" id="secondary_specialities_count" name="secondary_specialities_count" value="${secondarySpecialitiesCount}" />
			
			<div class="clr15"><!--  --></div>
            
            <div class="clr10"><!--  --></div>
               	<%-- <nacid:combobox id="secSpecialityId" onchange="changeDegree();"
               					name="secSpecialityId" attributeName="secSpecialityCombo" style="brd w250"/>
               	<v:comboBoxValidator input="secSpecialityId" required="$('div_sredno').visible() && $('secondary_specialities_table').rows.length == 0"/>--%>
               	<%-- <v:textinput id="sec_speciality" name="sec_speciality" class="brd w450" onkeydown="if(!isSpecialKeyPressed(event)) resetField('secSpecialityId');"/>--%>
               	<v:secSpecialityInput id="sec_speciality" fieldName="Специалност по документи" class="brd w450" onsuccess="updateSecSpecialityId(id); changeDegree();"
               						  onkeydown="if(!isSpecialKeyPressed(event)) resetField('secSpecialityId');" />
               	<v:hidden id="secSpecialityId" name="secSpecialityId" />
               	<span class="flt_rgt"><a href="javascript:addSecondarySpeciality();">Добави специалност</a></span>

            <div class="clr5"><!--  --></div>
            <p><span class="fieldlabel"><label for="sec_qualification_degree">Степен на професионална квалификация</label><br/></span>
               	<span id="sec_qualification_degree"></span>
            </p>
            <div class="clr5"><!--  --></div>
            <p><span class="fieldlabel"><label for="details.secCaliberId">Разред</label><br/></span>
            	<nacid:combobox id="details.secCaliberId" name="details.secCaliberId" attributeName="secondaryCaliberCombo" path="details.secCaliberId" style="brd w100"/>
            	<v:comboBoxValidator input="details.secCaliberId" required="false"/>
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel"><label for="details.documentType">Вид документ</label><br/></span>
               	<nacid:combobox id="doc_type_sec" name="details.documentType" attributeName="documentDocTypeCombo" path="details.documentType" style="brd w600"/>
               	<v:comboBoxValidator input="doc_type_sec" required="$('div_sredno').visible()"/>
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel"><label for="details.documentSeries">Серия на документ</label><br/></span>
               	<v:textinput id="sec_document_series" name="details.documentSeries" path="details.documentSeries" class="brd w200"/>
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel"><label for="details.documentNumber">Номер на документ</label><br/></span>
               	<v:textinput id="sec_document_number" name="details.documentNumber" path="details.documentNumber" class="brd w200"/>
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel"><label for="details.documentRegNumber">Рег. номер на документ</label><br/></span>
               	<v:textinput id="sec_document_reg_number" name="details.documentRegNumber" path="details.documentRegNumber" class="brd w200"/>
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel"><label for="details.documentDate">Дата на документ</label><br/></span>
               	<v:textinput id="sec_document_date" class="brd w200" name="details.documentDate" path="details.documentDate" />
            </p>
            <div class="clr"><!--  --></div>            
        </div>

        <div id="div_visshe" style="display:none;">
        	<fieldset><legend>Обучаваща институция</legend>      	

            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="highBgName">Ново име</label><br/></span>
               	<v:textinput id="high_bg_name" name="highBgName" value="${highBgName}" class="brd w600" onkeydown="if(!isSpecialKeyPressed(event)) resetField('details.profInstitutionId');"/>
            </p>
            <p><span class="fieldlabel2"><label for="highOrgName">Старо име</label><br/></span>
               	<v:textinput id="high_org_name" name="highOrgName" value="${highOrgName}" class="brd w600" 
               				 onkeydown="if(!isSpecialKeyPressed(event)) resetField('details.profInstitutionOrgNameId');" />
            </p>
            </fieldset>
            <div class="clr15"><!--  --></div>        	
        	<v:flatNomenclatureInput nomenclatureType="<%= Integer.toString(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION) %>"
        		onkeydown="if(!isSpecialKeyPressed(event)){ resetField('details.highProfQualificationId'); };"
        		fieldName="Професионална квалификация по документи" onblur="changeCertificateQualification();"
        		id="high_qualification" class="brd w450 flt_lft" onsuccess="updateHighProfQualification(id); changeCertificateQualification();" value="${high_qualification}" maxLength="200"/>
        		
        	<div class="clr10"><!--  --></div>
			<p>
				<span class="fieldlabel"><label>Избрани специалности</label></span>
			</p>
			
			<table id="higher_specialities_table" border="0">
				<c:forEach var="item" items="${higherSpecialitiesList}" varStatus="varStatus">
					<tr>
						<td style="display:none;"><input type="hidden" name="speciality_id${varStatus.index}" value="${item.higherSpecialityId}" /></td>
						<td>${item.higherSpecialityName}</td>
						<td><a href="javascript:void(0);" onclick="deleteHigherSpeciality(this.parentNode);"><img src="${pathPrefix}/img/icon_delete.png"/></a></td>
					</tr>	
				</c:forEach>
			</table>
			
			<div class="clr15"><!--  --></div>
        	
        	<div class="clr"><!--  --></div>
        	<v:flatNomenclatureInput nomenclatureType="<%= Integer.toString(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_SPECIALITY) %>"
        		onkeydown="if(!isSpecialKeyPressed(event)) resetField('highSpecialityId');"
        		fieldName="Специалност по документи" id="high_speciality" class="brd w450 flt_lft" onsuccess="updateHighSpecialityId(id);" value="${high_speciality}"/>
        	<span class="flt_rgt"><a href="javascript:addHigherSpeciality();">Добави специалност</a></span>
        	<div class="clr"><!--  --></div>
        	
        	 <div class="clr10"><!--  --></div>
        	<p><span class="fieldlabel"><label for="high_edu_level">ОКС</label><br/></span>
	        	<nacid:combobox id="high_edu_level" name="details.highEduLevelId" attributeName="highEduLevelCombo" path="details.highEduLevelId" style="brd w600"/>
				
	        </p>
	        <div class="clr"><!--  --></div>
            <p><span class="fieldlabel"><label for="details.documentType">Вид документ</label><br/></span>
               	<nacid:combobox id="doc_type_high" name="details.documentType" attributeName="documentDocTypeCombo" path="details.documentType" style="brd w600"/>
               	<v:comboBoxValidator input="doc_type_high" required="$('div_visshe').visible()"/>
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel"><label for="details.documentSeries">Серия на документ</label><br/></span>
               	<v:textinput id="high_document_series" name="details.documentSeries" path="details.documentSeries" class="brd w200"/>
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel"><label for="details.documentNumber">Номер на документ</label><br/></span>
               	<v:textinput id="high_document_number" name="details.documentNumber" path="details.documentNumber" class="brd w200"/>
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel"><label for="details.documentRegNumber">Рег. Номер на документ</label><br/></span>
               	<v:textinput id="high_document_reg_number" name="details.documentRegNumber" path="details.documentRegNumber" class="brd w200"/>
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel"><label for="details.documentDate">Дата на документ</label><br/></span>
               	<v:textinput id="high_document_date" class="brd w200" name="details.documentDate" path="details.documentDate" />
            </p>
            <div class="clr"><!--  --></div>  
        </div>
        
        <div id="div_SDK" style="display:none;">
        	<div class="flt_lft">
	        	<fieldset id="left_fieldset"><legend>Висше образование</legend>    
	        	<fieldset><legend>Обучаваща институция</legend>      	
				<p><span class="fieldlabel4"><label for="high_sdk_bg_name">Ново име</label><br/></span>
	               	<v:textinput id="high_sdk_bg_name" name="highBgName" value="${highBgName}" 
	               				onkeydown="if(!isSpecialKeyPressed(event)) resetField('details.profInstitutionId');" class="brd w290"/>
	            </p>
	            <div class="clr5"><!--  --></div>
	            <p><span class="fieldlabel4"><label for="highOrgName">Старо име</label><br/></span>
	               	<v:textinput id="high_sdk_org_name" name="highOrgName" value="${highOrgName}"
	               				onkeydown="if(!isSpecialKeyPressed(event)) resetField('details.profInstitutionOrgNameId');" class="brd w290"/>
	            </p>
	            </fieldset>
	            <div class="clr15"><!--  --></div>
	        	<v:flatNomenclatureInput nomenclatureType="<%= Integer.toString(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION) %>"
	        		onkeydown="if(!isSpecialKeyPressed(event)){ resetField('details.highProfQualificationId');};"
	        		labelclass="fieldlabel3" fieldName="Професионална квалификация по документи" id="high_sdk_qualification" class="brd w250" 
	        		onsuccess="updateHighProfQualification(id);" value="${high_qualification}" maxLength="200" />
       			
        		<div class="clr10"><!--  --></div>
				<p>
					<span class="fieldlabel3"><label>Избрани специалности</label></span>
				</p>
			
				<table id="higher_sdk_specialities_table" border="0">
					<c:forEach var="item" items="${higherSpecialitiesList}" varStatus="varStatus">
						<tr>
							<td style="display:none;"><input type="hidden" name="speciality_id${varStatus.index}" value="${item.higherSpecialityId}" /></td>
							<td>${item.higherSpecialityName}</td>
							<td><a href="javascript:void(0);" onclick="deleteHigherOrSdkSpeciality(this.parentNode, false);"><img src="${pathPrefix}/img/icon_delete.png"/></a></td>
						</tr>	
					</c:forEach>
				</table>
        		
        		<div class="clr15"><!--  --></div>
        		<v:flatNomenclatureInput nomenclatureType="<%= Integer.toString(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_SPECIALITY) %>"
        			onkeydown="if(!isSpecialKeyPressed(event)) resetField('highSpecialityId');" labelclass="fieldlabel3" 
        			fieldName="Специалност по документи" id="high_sdk_speciality" class="brd w250" onsuccess="updateHighSpecialityId(id); addHigherSpeciality();" value="${high_speciality}"/>
	        	<span class="flt_rgt"><a href="javascript:addHigherOrSdkSpeciality(false);">Добави специалност</a></span>
	        	
	        	<div class="clr10"><!--  --></div>
	        	
	            <p><span class="fieldlabel3"><label for="high_edu_level">ОКС</label><br/></span>
	               	<nacid:combobox id="high_sdk_edu_level" name="details.highEduLevelId" attributeName="highEduLevelCombo" path="details.highEduLevelId" style="brd w250"/>
	            </p>
	             <div class="clr"><!--  --></div>
	            <p><span class="fieldlabel3"><label for="details.documentType">Вид документ</label><br/></span>
	               	<nacid:combobox id="doc_type_high_sdk" name="details.documentType" attributeName="documentDocTypeCombo" path="details.documentType" style="brd w250"/>
	               	<v:comboBoxValidator input="doc_type_high_sdk" required="$('div_SDK').visible()"/>
	            </p>
	            <div class="clr5"><!--  --></div>
	            <p><span class="fieldlabel3"><label for="details.documentSeries">Серия на документ</label><br/></span>
	               	<v:textinput id="high_sdk_document_series" name="details.documentSeries" path="details.documentSeries" class="brd w200"/>
	            </p>
	            <div class="clr5"><!--  --></div>
	            <p><span class="fieldlabel3"><label for="details.documentNumber">Номер на документ</label><br/></span>
	               	<v:textinput id="high_sdk_document_number" name="details.documentNumber" path="details.documentNumber" class="brd w200"/>
	            </p>
	           	<div class="clr5"><!--  --></div>
	            <p><span class="fieldlabel3"><label for="details.documentRegNumber">Рег. Номер на документ</label><br/></span>
	               	<v:textinput id="high_sdk_document_reg_number" name="details.documentRegNumber" path="details.documentRegNumber" class="brd w200"/>
	            </p>
	            <div class="clr5"><!--  --></div>
	            <p><span class="fieldlabel3"><label for="details.documentDate">Дата на документ</label><br/></span>
	               	<v:textinput id="high_sdk_document_date" class="brd w200" name="details.documentDate" path="details.documentDate" />
	            </p>
	            <div class="clr"><!--  --></div> 
	            </fieldset>
            </div> 
            <div class="flt_lft">
	        	<fieldset id="right_fieldset"><legend>СДК</legend>    
	        	<v:hidden id="details.sdkProfQualificationId" path="details.sdkProfQualificationId" name="details.sdkProfQualificationId"/>
	        	<v:hidden id="sdkSpecialityId" name="sdkSpecialityId"/>
	        	<v:hidden id="details.sdkProfInstitutionId" path="details.sdkProfInstitutionId" name="details.sdkProfInstitutionId"/>
	        	<v:hidden id="details.sdkProfInstitutionOrgNameId" path="details.sdkProfInstitutionOrgNameId" name="details.sdkProfInstitutionOrgNameId"/>
	        	
	        	<fieldset><legend>Обучаваща институция</legend>
	        	<p><span class="fieldlabel4"><label for="sdkBgName">Ново име</label><br/></span>
	               	<v:textinput id="sdkBgName" name="sdkBgName" value="${sdkBgName}" class="brd w290" 
	               				onkeydown="if(!isSpecialKeyPressed(event)) resetField('details.sdkProfInstitutionId');"/>
	               	<v:textValidator input="sdkBgName" minLength="2" maxLength="255" required="$('div_SDK').visible()"/>
	            </p>
	            <div class="clr5"><!--  --></div>
	       		<p><span class="fieldlabel4"><label for="sdkOrgName">Старо име</label><br/></span>
	               	<v:textinput id="sdkOrgName" name="sdkOrgName" value="${sdkOrgName}" class="brd w290" 
	               				onkeydown="if(!isSpecialKeyPressed(event)) resetField('details.sdkProfInstitutionOrgNameId');"/>
	               	<v:textValidator input="sdkOrgName" minLength="2" maxLength="255" required="($('div_SDK').visible() && !($('sdkOrgName').value.empty()))"/>
	            </p>
	            <div class="clr"><!--  --></div>
	            </fieldset>
	            <div class="clr15"><!--  --></div>
				
				<v:flatNomenclatureInput nomenclatureType="<%= Integer.toString(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_PROF_QUALIFICATION) %>" labelclass="fieldlabel3"
					onkeydown="if(!isSpecialKeyPressed(event)) {resetField('details.sdkProfQualificationId'); };" 
        			fieldName="Професионална квалификация по документи" id="sdk_qualification" class="brd w250" onsuccess="updateSdkProfQualification(id);changeCertificateQualification();" value="${sdk_qualification}"
        			onblur="changeCertificateQualification();" maxLength="200"/>
        		
        		<div class="clr10"><!--  --></div>
				<p>
					<span class="fieldlabel3"><label>Избрани специалности</label></span>
				</p>
			
				<table id="sdk_specialities_table" border="0">
					<c:forEach var="item" items="${sdkSpecialitiesList}" varStatus="varStatus">
						<tr>
							<td style="display:none;"><input type="hidden" name="sdk_speciality_id${varStatus.index}" value="${item.sdkSpecialityId}" /></td>
							<td>${item.sdkSpecialityName}</td>
							<td><a href="javascript:void(0);" onclick="deleteHigherOrSdkSpeciality(this.parentNode, true);"><img src="${pathPrefix}/img/icon_delete.png"/></a></td>
						</tr>	
					</c:forEach>
				</table>
        		
        		<div class="clr15"><!--  --></div>
        		<v:flatNomenclatureInput nomenclatureType="<%= Integer.toString(NomenclaturesDataProvider.FLAT_NOMENCLATURE_HIGHER_SPECIALITY) %>" labelclass="fieldlabel3"
        			onkeydown="if(!isSpecialKeyPressed(event)) resetField('sdkSpecialityId');" value="${sdk_speciality}"
        			fieldName="Специалност по документи" id="sdk_speciality" class="brd w250" onsuccess="updateSdkSpecialityId(id);" />
				<span class="flt_rgt"><a href="javascript:addHigherOrSdkSpeciality(true);">Добави специалност</a></span>
				
				<div class="clr20"><!--  --></div>
				
				<%-- <p><span class="fieldlabel3"><label for="sdk_edu_level">ОКС</label><br/></span>
	               	<nacid:combobox id="sdk_edu_level" name="details.sdkEduLevelId" attributeName="sdkEduLevelCombo" path="details.sdkEduLevelId" style="brd"/>
	               	<v:comboBoxValidator input="sdk_edu_level" required="$('div_SDK').visible()"/>
	            </p>--%>
	            <div class="clr"><!--  --></div>
				<div class="clr"><!--  --></div>
				<div class="clr"><!--  --></div>
	            
				<div class="clr20"><!--  --></div>
	            <p><span class="fieldlabel3"><label for="details.sdkDocumentType">Вид документ</label><br/></span>
	               	<nacid:combobox id="doc_type_sdk_high" name="details.sdkDocumentType" attributeName="sdkDocTypeCombo" path="details.sdkDocumentType" style="brd w250"/>
	               	<v:comboBoxValidator input="doc_type_sdk_high" required="$('div_SDK').visible()"/>
	            </p>
	            <div class="clr5"><!--  --></div>
	            <p><span class="fieldlabel3"><label for="details.sdkDocumentSeries">Серия на документ</label><br/></span>
	               	<v:textinput id="details.sdkDocumentSeries" name="details.sdkDocumentSeries" path="details.sdkDocumentSeries" class="brd w200"/>
	               	<v:textValidator input="details.sdkDocumentSeries" maxLength="32" />
	            </p>
				<div class="clr5"><!--  --></div>
	            <p><span class="fieldlabel3"><label for="details.sdkDocumentNumber">Номер на документ</label><br/></span>
	               	<v:textinput id="details.sdkDocumentNumber" name="details.sdkDocumentNumber" path="details.sdkDocumentNumber" class="brd w200"/>
	               	<v:textValidator input="details.sdkDocumentNumber" maxLength="32" />
	            </p>
	            <div class="clr5"><!--  --></div>
	            <p><span class="fieldlabel3"><label for="details.sdkDocumentRegNumber">Рег. Номер на документ</label><br/></span>
	               	<v:textinput id="details.sdkDocumentRegNumber" name="details.sdkDocumentRegNumber" path="details.sdkDocumentRegNumber" class="brd w200"/>
	               	<v:textValidator input="details.sdkDocumentRegNumber" maxLength="32" />
	            </p>
	            <div class="clr5"><!--  --></div>
	            <p><span class="fieldlabel3"><label for="details.sdkDocumentDate">Дата на документ</label><br/></span>
	               	<v:textinput id="details.sdkDocumentDate" class="brd w200" name="details.sdkDocumentDate" path="details.sdkDocumentDate" />
	               	<v:textValidator input="details.sdkDocumentDate" maxLength="50"/>
	            </p>
	            <div class="clr"><!--  --></div> 
	            </fieldset>
            </div>
        </div> 
        <script type="text/javascript">
        
        	new Autocomplete('secBgName', { 
       	    	serviceUrl:'/nacid_regprof/control/professionalinstitutionsuggest',
       	    	dynamicParameters: function() {
    	      		return {education_type : $('details.educationTypeId').value};
    	      	},
       	   		onSelect: updateProfInstitutionData,
       	   		width:600
        	});
        	
	       	new Autocomplete('secOrgName', { 
	      	   	serviceUrl:'/nacid_regprof/control/professionalinstitutionsuggest?formername=1',
	      		dynamicParameters: function() {
	      			return {institution : $('details.profInstitutionId').value};
	      		},
	      	   	onSelect: updateProfInstitutionOrgNameId,
	      	  	width:600
	       	});
	       	       	
        	new Autocomplete('high_bg_name', { 
    	    	serviceUrl:'/nacid_regprof/control/professionalinstitutionsuggest',
       	    	dynamicParameters: function() {
    	      		return {education_type : $('details.educationTypeId').value};
    	      	},
    	    	onSelect: updateProfInstitutionData,
    	    	width:600
        	});
        	
        	new Autocomplete('high_org_name', { 
    	    	serviceUrl:'/nacid_regprof/control/professionalinstitutionsuggest?formername=1',
    	    	dynamicParameters: function() {
	      			return {institution : $('details.profInstitutionId').value};
	      		},
    	    	onSelect: updateProfInstitutionOrgNameId,
    	    	width:600
        	});
  
            new Autocomplete('high_sdk_bg_name', { 
        	    serviceUrl:'/nacid_regprof/control/professionalinstitutionsuggest?institution_type=' + higherProfessionalInstitutionType,
        	    onSelect: updateProfInstitutionData,
        	    width:500
        	});
        	
        	new Autocomplete('high_sdk_org_name', { 
        	    serviceUrl:'/nacid_regprof/control/professionalinstitutionsuggest?formername=1',
        	    dynamicParameters: function() {
	      			return {institution : $('details.profInstitutionId').value};
	      		},
        	    onSelect: updateProfInstitutionOrgNameId,
        	    width:500
        	});
        	
            new Autocomplete('sdkBgName', { 
       	    	dynamicParameters: function() {
    	      		return {education_type : $('details.educationTypeId').value};
    	      	},
                serviceUrl:'/nacid_regprof/control/professionalinstitutionsuggest',
                onSelect: updateSDKProfInstitutionData,
                width:500
            });
        	
        	new Autocomplete('sdkOrgName', { 
            	serviceUrl:'/nacid_regprof/control/professionalinstitutionsuggest?formername=1',
            	dynamicParameters: function() {
	      			return {institution : $('details.sdkProfInstitutionId').value};
	      		},
            	onSelect: updateSdkProfInstitutionOrgNameId,
            	width:500
            });
        	
        </script>
        
        <div class="clr20"><!--  --></div>
	</div>
	
	<div class="clr10"><!--  --></div>
	
	</fieldset>
	
	<div class="clr20"><!--  --></div>
	<fieldset><legend><v:checkboxinput id="regulated_education_training" path="details.regulatedEducationTraining" name="details.regulatedEducationTraining" onclick="$('regulatedEducationTrainingDiv').toggle();"/>Регулирано образование и обучение</legend>
		<div class="clr"><!--  --></div>
		<div id="regulatedEducationTrainingDiv" style="display: ${requestScope['com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseImpl'].details.regulatedEducationTraining == 1 ? 'block' : 'none'}">
			Образованието и обучението, изисквано за упражняване на професията, е регулирано в Република България.
		</div>
	</fieldset>
	<div class="clr20"><!--  --></div>
	<fieldset><legend><v:checkboxinput id="submit_experience" path="details.hasExperience" name="details.hasExperience" onclick="toggleSubmitDivs();"/>Стаж</legend>
		<div class="clr"><!--  --></div>
		
		<div id="experience">
		
      	<p><span class="fieldlabel2"><label for="professionName">Професия</label><br/></span>
       	<c:choose>
        	<c:when test="${professionName == null || professionName == ''}">
        		<v:textinput name="professionName" value="${qualificationAsProfession}" id="professionName" class="brd w500" onblur="changeCertificateQualification();"/>
        	</c:when>
        	<c:otherwise>
        		<v:textinput name="professionName" value="${professionName}" id="professionName" class="brd w500" onblur="changeCertificateQualification();"/>
        	</c:otherwise>
		</c:choose>
		</p>
								
		<v:textValidator input="professionName" maxLength="80" required="$('submit_experience').checked" />
		<script type="text/javascript">
 		   new Autocomplete('professionName', {
    	     serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?type=28',
    	     onSelect: updateProfession,
    	     width:500
    	   });
 		   
 		   function updateProfession(value, data) {
 			  $("professionName").value = data.evalJSON(true).name;
 			  changeCertificateQualification();
 		   }
		</script>
		
		<div class="clr5"><!--  --></div>
		<p class="flt_rgt"><a onclick="javascript:addExperienceDocumentRow();"href="javascript:void(0);">Добави нов документ</a></p>
		<div class="clr10"><!--  --></div>
		<c:forEach var="experienceDocument" items='${requestScope["com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseImpl"].experienceRecord.professionExperienceDocuments}'  varStatus="st">
		<fieldset id="experience_document_row${st.index }"><legend>Документи</legend>
			<v:hidden name="experienceRecord.professionExperienceDocuments[${st.index}].id" path="experienceRecord.professionExperienceDocuments[${st.index}].id" />
			<c:if test="${st.index != 0}">
				<p class="flt_rgt"><a onclick="this.parentNode.parentNode.remove();" style="color: red" href="javascript:void(0);">Премахни</a></p>
			</c:if>
			<p><span class="fieldlabel2"><label for="experienceRecord.professionExperienceDocuments[${st.index }].profExperienceDocTypeId">Тип документ за стаж</label><br/></span>
				<nacid:combobox id="experienceRecord.professionExperienceDocuments[${st.index}].profExperienceDocTypeId" name="experienceRecord.professionExperienceDocuments[${st.index}].profExperienceDocTypeId"
								path="experienceRecord.professionExperienceDocuments[${st.index}].profExperienceDocTypeId" attributeName="profExperienceDocTypeCombo" 
								onchange="updateForExperienceCalculation(this);" style="brd w200"/>
			</p>
			<div class="clr5"><!--  --></div>
			<p><span class="fieldlabel2"><label for="experienceRecord.professionExperienceDocuments[${st.index}].documentNumber">Номер документ</label><br/></span>
				
				<v:textinput id="experienceRecord.professionExperienceDocuments[${st.index}].documentNumber" name="experienceRecord.professionExperienceDocuments[${st.index}].documentNumber" path="experienceRecord.professionExperienceDocuments[${st.index}].documentNumber" class="brd w500"/>
			</p>
			<div class="clr5"><!--  --></div>
			<p><span class="fieldlabel2"><label for="experienceRecord.professionExperienceDocuments[${st.index}].documentDate">Дата документ</label><br/></span>
				<v:textinput id="experienceRecord.professionExperienceDocuments[${st.index}].documentDate" name="experienceRecord.professionExperienceDocuments[${st.index}].documentDate" path="experienceRecord.professionExperienceDocuments[${st.index}].documentDate" class="brd w200"/>
			</p>
			<div class="clr5"><!--  --></div>
			<p><span class="fieldlabel2"><label for="experienceRecord.professionExperienceDocuments[${st.index}].documentIssuer">Издаваща институция</label><br/></span>
				<v:textinput id="experienceRecord.professionExperienceDocuments[${st.index}].documentIssuer" name="experienceRecord.professionExperienceDocuments[${st.index}].documentIssuer" path="experienceRecord.professionExperienceDocuments[${st.index}].documentIssuer" class="brd w500"/>
			</p>
			<div class="clr5"><!--  --></div>
			<p><span class="fieldlabel2"><label for="experienceRecord.professionExperienceDocuments[${st.index}].documentIssuer">Влиза за изчисление</label></span>
				<v:checkboxinput name="experienceRecord.professionExperienceDocuments[${st.index}].forExperienceCalculation" id="experienceRecord.professionExperienceDocuments[${st.index}].forExperienceCalculation" path="experienceRecord.professionExperienceDocuments[${st.index}].forExperienceCalculation"/>
			</p>
			<div class="clr"><!--  --></div>
			
			<fieldset><legend>Работил за периода:</legend>
			<div class="clr10"><!--  --></div>
			<p class="flt_rgt"><a id="addDatesRow${st.index}" href="javascript:addDatesRow(${st.index});">Добави нов период</a></p>
			<div class="clr"><!--  --></div>
				
			<c:choose>
				<c:when test='${requestScope["com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseImpl"].experienceRecord.id > 0}'>
					<c:forEach var="datesPair" items='${experienceDocument.dates}' varStatus="pStatus">
						<div id="document${st.index}dates${pStatus.index}">
							<div class="clr5"></div>
							<v:hidden name="experienceRecord.professionExperienceDocuments[${st.index}].dates[${pStatus.index}].id" path="experienceRecord.professionExperienceDocuments[${st.index}].dates[${pStatus.index}].id" />
							
							<span class="datefield"><label for="experienceRecord.professionExperienceDocuments[${st.index}].dates[${pStatus.index}].dateFrom">от</label></span>
							<v:dateinput id="experienceRecord.professionExperienceDocuments[${st.index}].dates[${pStatus.index}].dateFrom" class="brd w100" name="experienceRecord.professionExperienceDocuments[${st.index}].dates[${pStatus.index}].dateFrom" path="experienceRecord.professionExperienceDocuments[${st.index}].dates[${pStatus.index}].dateFrom" />	

							<span class="datefield2"><label for="experienceRecord.professionExperienceDocuments[${st.index}].dates[${pStatus.index}].dateTo">до</label></span>
							<v:dateinput id="experienceRecord.professionExperienceDocuments[${st.index}].dates[${pStatus.index}].dateTo" class="brd w100" name="experienceRecord.professionExperienceDocuments[${st.index}].dates[${pStatus.index}].dateTo" path="experienceRecord.professionExperienceDocuments[${st.index}].dates[${pStatus.index}].dateTo" />

							<span class="datefield2"><label for="experienceRecord.professionExperienceDocuments[${st.index}].dates[${pStatus.index}].workdayDuration">Работен ден:</label></span>
							<nacid:combobox style="brd" 
								id="experienceRecord.professionExperienceDocuments[${st.index}].dates[${pStatus.index}].workdayDuration" 
								name="experienceRecord.professionExperienceDocuments[${st.index}].dates[${pStatus.index}].workdayDuration" attributeName="workdayDurations" 
											path="experienceRecord.professionExperienceDocuments[${st.index}].dates[${pStatus.index}].workdayDuration"/><label>часа</label>
                            <a id="document${st.index}remove${pStatus.index}" href="#" onclick="this.parentNode.remove();return false;" style="color: red;" class="pl20">Премахни</a>
                            <div class="clr5"></div>
						</div>
					</c:forEach>
					<script type="text/javascript">
						datesCount.set(${st.index}, ${experienceDocument.datesCount});
					</script>
				</c:when>
				<c:otherwise>
					<script type="text/javascript">
						datesCount.set(${st.index}, 0);
						addDatesRow(${st.index});
					</script>
				</c:otherwise>
			</c:choose>
			<div id="target${st.index}"><!--  --></div> 

			</fieldset>
			</fieldset>
		</c:forEach>
		<div class="clr15" id="after_last_document"><!--  --></div>
		<p class="flt_lft"><a href="javascript:calculatePeriod(true);">Изчисли професионален стаж</a></p>
		<div class="clr"><!--  --></div>
		<div id="totalDaysWorked" style="display:none;">Общо <b id="total"></b> дни</div>
		<div id="daysWorked2" style="display:none;">
				Общо<b id="years"></b><span id="yearCount"></span>,<b id="months"></b><span id="monthCount"></span>и<b id="days"></b><span id="dayCount"></span>професионален стаж
		</div>
		<div id="periodWorked" style="display:none;"></div>
		<div class="clr5"><!--  --></div>
		
		</div>
	</fieldset>
	
	<div class="clr20"><!--  --></div>
	<p class="checkbox"><v:checkboxinput path="details.notRestricted" name="details.notRestricted" id="details.notRestricted" />
		<label for="details.notRestricted">Не е отнето правото за упражняване на професията и 
			липса на наложени административни наказания във връзка с упражняване на професията</label></p>
			
	<div class="clr10"><!--  --></div>
	<div>
        <p><span class="fieldlabel"><label for="certificate_qualification" >Професионална квалификация, за която се иска издаването на удостоверение</label><br/></span>
			<v:textinput id="certificate_qualification" name="certificate_qualification" value="${certificate_qualification}" class="brd w600"/>
		</p>
        <v:textValidator input="certificate_qualification" required="true" minLength="2" maxLength="200"/>
	</div>
        <script type="text/javascript">
 		   new Autocomplete('certificate_qualification', {
    	     serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?type=43',
    	     onSelect: updateCertificateQualification,
    	     width:600
    	   });
 		   
 		   function updateCertificateQualification(value, data) {
 			  $("certificate_qualification").value = data.evalJSON(true).name;
 		   }
		</script>

	<div class="clr20"><!--  --></div>

	<fieldset id="experience_document_row" style="display:none;"><legend>Документи</legend>
		<p class="flt_rgt"><a onclick="this.parentNode.parentNode.remove();" style="color: red" href="javascript:void(0);">Премахни</a></p>
		<v:hidden name="docId" value="0" disabled="disabled"/>
		<p><span class="fieldlabel2"><label for="profExperienceDocTypeId">Тип документ за стаж</label><br/></span>
			<nacid:combobox id="profExperienceDocTypeId" name="profExperienceDocTypeId" attributeName="profExperienceDocTypeCombo" style="brd w200"  onchange="updateForExperienceCalculation(this)" disabled="disabled"/>
		</p>
		<div class="clr5"><!--  --></div>
		<p><span class="fieldlabel2"><label for="documentNumber" >Номер документ</label><br/></span>
			<v:textinput id="documentNumber" name="documentNumber" class="brd w500" value="" disabled="disabled"/>
		</p>
		<div class="clr5"><!--  --></div>
		<p><span class="fieldlabel2"><label for="documentDate" >Дата документ</label><br/></span>
			<v:textinput id="documentDate" name="documentDate" class="brd w200" disabled="disabled"/>
		</p>
		<div class="clr5"><!--  --></div>
		<p><span class="fieldlabel2"><label for="documentIssuer">Издаваща институция</label><br/></span>
			<v:textinput id="documentIssuer" name="documentIssuer" class="brd w500" disabled="disabled"/>
		</p>
		<div class="clr5"><!--  --></div>
		<p><span class="fieldlabel2"><label for="forExperienceCalculation">Влиза за изчисление</label></span>
			<v:checkboxinput name="forExperienceCalculation" id="forExperienceCalculation"  disabled="disabled"/>
		</p>
		<div class="clr"><!--  --></div>

		<fieldset><legend>Работил за периода:</legend>
			<div class="clr10"><!--  --></div>
			<p class="flt_rgt"><a id="addDatesRow" href="#">Добави нов период</a></p>
			<div class="clr"><!--  --></div>
			<div id="target"><!--  --></div> 
		</fieldset>
	</fieldset>

	<div id="dates_row" style="display:none;">
		<div class="clr5"><!--  --></div>
		<input type="hidden" id="id" name="id" value="0" disabled="disabled" />

		<span class="datefield"><label for="dateFrom">от</label></span>
		<v:dateinput id="dateFrom" style="brd w100" name="dateFrom" value="дд.мм.гггг" disabled="disabled" />

		<span class="datefield2"><label for="dateTo">до</label></span>
		<v:dateinput id="dateTo" style="brd w100" name="dateTo" value="дд.мм.гггг" disabled="disabled" />

		<span class="datefield2"><label for="workdayDuration">Работен ден:</label></span>
		<nacid:combobox name="workdayDuration" disabled="disabled" attributeName="workdayDurations" style="brd" />
		<label>часа</label>
		<a href="#" onclick="this.parentNode.remove();return false;" style="color: red; padding-left: 20px;">Премахни</a>
		<div class="clr10"><!--  --></div>	
	</div>
	<%-- 
	<c:if test="${is_responsible_user_logged == 1}">
		<div class="clr20"><!--  --></div>
		<fieldset><legend>Статус</legend>
			<div class="clr10"><!--  --></div>
			<p><span class="fieldlabel2"><label for="status">Статус</label><br/></span>
	         	<nacid:combobox name="statusInTrainingCourse" attributeName="statusComboInTrainingCourse" style="w308 brd"/>
	        </p>
	        <div class="clr10"><!--  --></div>
		</fieldset>
		<div class="clr20"><!--  --></div>
	</c:if>
	--%>
	
	<v:hidden id="higher_specialities_count" name="higher_specialities_count" value="${higherSpecialitiesCount}" />
	<v:hidden id="sdk_specialities_count" name="sdk_specialities_count" value="${sdkSpecialitiesCount}" />
</v:form>
<div class="clr20"><!--  --></div>

<script type="text/javascript">
/*if (window.location.pathname.lastIndexOf('view') != -1) { // view mode
	$$('#experience a').each(function(el) {
		el.hide();
	})
}*/

if ($('submit_education').checked == false && $('submit_experience').checked == false) {
	$('submit_education').checked = true;
	$('education').show();
}
toggleSubmitDivs();
showHideEduDivs();
</script>