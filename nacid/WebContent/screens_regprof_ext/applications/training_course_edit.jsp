<%@page import="com.nacid.bl.nomenclatures.NomenclaturesDataProvider"%>
<%@page import="com.nacid.bl.regprof.ProfessionalInstitutionDataProvider"%>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>



<%@page import="com.nacid.bl.impl.regprof.external.applications.ExtRegprofTrainingCourseImpl"%><h3 class="title"><span>${messages.applicationdata }</span></h3>
<h3 class="names">${application_header }</h3>
<script type="text/javascript">
var datesCount = new Hash();
/**
 * filling doctypesforcalc.set values... Tozi set opredelq dali dadeniq documentType uchastva v smqtaneto na staja!
 */
var doctypesforcalc = new Hash();
<c:forEach var="entry" items="${docTypeForExperienceCalculation}">
	doctypesforcalc.set(${entry.key}, ${entry.value});
</c:forEach>

var specialitiesCount = ${model.specialitiesCount} + 0;
var higherProfessionalInstitutionType = <%= Integer.toString(ProfessionalInstitutionDataProvider.HIGHER_INSTITUTION_TYPE) %>;

function updateForExperienceCalculation(el) {
	var val = el[el.selectedIndex].value;
	if (val == '-') {
		return;
	}
	//var isSelected = doctypesforcalc.get(val);
	var isSelected = true;//vsichki stajove za vyn6noto prilojenie se okaza, che sa isSelected!!!!
    var rowCnt = el.id.replace('experienceRecord.professionExperienceDocuments[', '');
    rowCnt = rowCnt.replace('].profExperienceDocTypeId', '');
    $('experienceRecord.professionExperienceDocuments[' + rowCnt + '].forExperienceCalculation').checked = isSelected;
}

function additionalInputsValidation() {
	var ret = true;
	if ($('submit_education').checked == true) {
		ret = validateEducationDivs() && /*newNomenclaturesValidation() &&*/ ret;
		/*if ($('div_SDK').visible() && $('high_sdk_document_date').value && $('details.sdkDocumentDate').value) {
			ret = validateDateInterval($('high_sdk_document_date'), $('details.sdkDocumentDate'), 'd.m.yyyy', 'd.m.yyyy', 'Грешен период', true) && ret;
		}*/
	}
	if ($('submit_experience').checked == true) {
		ret = validateExperienceDocuments(false) && ret;
	}
	return ret;
}

function validateEducationDivs() {
	var ret = true;
	if ($('div_sredno').visible()) {
		ret = validateText($('secBgName'), !$('secBgName').value.empty(), 2, 255, null, '') && ret;
		ret = validateText($('secOrgName'), !$('secOrgName').value.empty(), 2, 255, null, '') && ret;
		ret = validateText($('sec_document_series'), !$('sec_document_series').value.empty(), -1, 32, null, '') && ret;
		ret = validateText($('sec_document_number'), !$('sec_document_number').value.empty(), -1, 32, null, '') && ret;
		ret = validateText($('sec_document_reg_number'), !$('sec_document_reg_number').value.empty(), -1, 32, null, '') && ret;
		ret = validateText($('sec_document_date'), !$('sec_document_date').value.empty(), -1, 50, null, '') && ret;
		if ($('secBgName').value.empty() && $('secOrgName').value.empty()) {
			setErrorOnInput('secBgName', 'Необходимо е да се посочи ново или старо име');
			setErrorOnInput('secOrgName', 'Необходимо е да се посочи ново или старо име');
			ret = false;
		}
	} else if ($('div_visshe').visible()) {
		ret = validateText($('high_bg_name'), !$('high_bg_name').value.empty(), 2, 255, null, '') && ret;
		ret = validateText($('high_org_name'), !$('high_org_name').value.empty(), 2, 100, null, '') && ret;
		ret = validateText($('high_qualification'), true, 2, 150, null, '') && ret;
		ret = validateText($('high_speciality'), ($('higher_specialities_table').rows.length == 0 || !$('high_speciality').value.empty()), 2, 150, null, '') && ret;
		ret = validateText($('high_document_series'), !$('high_document_series').value.empty(), -1, 32, null, '') && ret;
		ret = validateText($('high_document_number'), !$('high_document_number').value.empty(), -1, 32, null, '') && ret;
		ret = validateText($('high_document_reg_number'), !$('high_document_reg_number').value.empty(), -1, 32, null, '') && ret;
		ret = validateText($('high_document_date'), !$('high_document_date').value.empty(), -1, 50, null, '') && ret;
		if ($('high_bg_name').value.empty() && $('high_org_name').value.empty()) {
			setErrorOnInput('high_bg_name', 'Необходимо е да се посочи ново или старо име');
			setErrorOnInput('high_org_name', 'Необходимо е да се посочи ново или старо име');
			ret = false;
		}
	} else if ($('div_SDK').visible()) {
		ret = validateText($('high_sdk_bg_name'), !$('high_sdk_bg_name').value.empty(), 2, 255, null, '') && ret;
		ret = validateText($('high_sdk_org_name'), !$('high_sdk_org_name').value.empty(), 2, 100, null, '') && ret;
		ret = validateText($('high_sdk_qualification'), true, 2, 150, null, '') && ret;
		ret = validateText($('high_sdk_speciality'), ($('higher_sdk_specialities_table').rows.length == 0 || !$('high_sdk_speciality').value.empty()), 2, 150, null, '') && ret;
		ret = validateText($('high_sdk_document_series'), !$('high_sdk_document_series').value.empty(), -1, 32, null, '') && ret;
		ret = validateText($('high_sdk_document_number'), !$('high_sdk_document_number').value.empty(), -1, 32, null, '') && ret;
		ret = validateText($('high_sdk_document_reg_number'), !$('high_sdk_document_reg_number').value.empty(), -1, 32, null, '') && ret;
		ret = validateText($('high_sdk_document_date'), !$('high_sdk_document_date').value.empty(), -1, 50, null, '') && ret;
		if ($('high_sdk_bg_name').value.empty() && $('high_sdk_org_name').value.empty()) {
			setErrorOnInput('high_sdk_bg_name', 'Необходимо е да се посочи ново или старо име');
			setErrorOnInput('high_sdk_org_name', 'Необходимо е да се посочи ново или старо име');
			ret = false;
		}
		if ($('sdkBgName').value.empty() && $('sdkOrgName').value.empty()) {
			setErrorOnInput('sdkBgName', 'Необходимо е да се посочи ново или старо име');
			setErrorOnInput('sdkOrgName', 'Необходимо е да се посочи ново или старо име');
			ret = false;
		}
	}
	else return true;
	return ret;
}

function submitFunction() {
	if ($('submit_experience').checked == true) {
		calculatePeriod(false);
	}
	//ako ima vyvedeni specialnosti, koito ne sa dobaveni s "dobavi specialnost", to te se dobavqt izkustveno...
	if ($('div_sredno').visible()) {
		addSecondarySpeciality();
	} else  if ($('div_visshe').visible()) {
		addHigherSpeciality();
	} else if ($('div_SDK').visible()) {
		addHigherOrSdkSpeciality(false);
		addHigherOrSdkSpeciality(true);
	}
}
/*function updateProfInstitutionData(value, data) {
	var d = data.evalJSON(true);
	$('details.profInstitutionId').value = d.id;

}*/
function updateSecQualificationData(value, data) {
	updateQualificationData(data, "sec");
	updateCertificateProfQualTxt();
}
function updateHighQualificationData(value, data) {
	updateQualificationData(data, "high");
	updateCertificateProfQualTxt();
}
function updateSDKQualificationData(value, data) {
	updateQualificationData(data, "sdk");
	updateCertificateProfQualTxt();
	
}
function updateQualificationData(data, name) {
	var d = data.evalJSON(true);
	$('details.' + name + 'ProfQualificationId').value = d.id;
}
function updateSecSpecialityData(value, data){
	updateSpecialityData(data, "sec");
	changeDegree();
}
function updateHighSpecialityData(value, data) {
	updateSpecialityData(data, "high");
}
function updateSDKSpecialityData(value, data) {
	updateSpecialityData(data, "sdk");
}
function updateSpecialityData(data, name) {
	var d = data.evalJSON(true);
	$(name + 'SpecialityId').value = d.id;
}
function changeRegulatedInCountry() {
	var country = $('details.countryId').options[$('details.countryId').selectedIndex].text;
	if (country == '-') {
		country = "Няма въведена информация за държава";
	}
	$('regulatedInCountry').innerHTML = country;
}
 function updateCertificateProfQual(value, data) {
	  var obj = data.evalJSON(true);
	  $("details.certificateProfQualificationId").value = obj.id;
		  $("details.certificateProfQualificationTxt").value = obj.name;
   }
function updateCertificateProfQualTxt() {
    var isEdu = $('submit_education').checked;
    var qual;
    if (isEdu) {
        var eduTypeId = $('details.educationTypeId').value;

        if (eduTypeId == 2 || eduTypeId == 4) {//sredno
            qual = $('sec_qualification').value;
        } else if (eduTypeId == 1) { //wisshe
            qual = $('high_qualification').value;
        } else if (eduTypeId == 3) { //sdk
            qual = $('sdk_qualification').value;
        } else {
            qual = '';
        }
    } else {
        qual = $('professionName').value;
    }
    $('details.certificateProfQualificationId').value = "";
    $('details.certificateProfQualificationTxt').value = qual;
}
	 
</script>
<div class="clr20"><!--  --></div> 

<v:form name="appform2" action="${pathPrefix }/control/applications/save?appId=${id}" method="post" 
	  backurl="${pathPrefix }/control/applications/list?getLastTableState=1" additionalvalidation="additionalInputsValidation()"
	  modelAttribute="<%=ExtRegprofTrainingCourseImpl.class.getName() %>"
	  onsubmit="submitFunction();">
	<nacid:systemmessage name="trainingCourseSystemMessage" />

	<v:hidden id="activeForm" value="2" name="activeForm"/>
	<v:hidden id="details.id" path="details.id" name="details.id" />
	
	<div class="clr20"><!--  --></div> 
	<p>
		<span class="fieldlabel2"><label for="applicationCountryId">Кандидатства за държава</label></span>
		<nacid:combobox id="applicationCountryId" name="applicationCountryId" attributeName="countries" path="applicationCountryId" style="brd w500"/>
	</p>
	<v:comboBoxValidator input="applicationCountryId" required="true" />
	<div class="clr20"><!--  --></div>
    <p><span class="fieldlabel"><label for="apostilleApplication">Заявление за апостил</label><br/></span>
        <v:checkboxinput id="apostilleApplication" name="apostilleApplication" path="apostilleApplication" style="brd w500" />
    </p>
    <div class="clr20"><!--  --></div>

    <fieldset><legend><v:checkboxinput id="submit_education" name="details.hasEducation" path="details.hasEducation" onclick="toggleSubmitDivs(); updateCertificateProfQualTxt();"/>Обучение</legend>
		<div id="education">
			<div id="comment">
				<i>${regprofEservicesEduComment}</i>
			</div>
		<v:hidden id="details.profInstitutionId" path="details.profInstitutionId" name="details.profInstitutionId"/>
		<v:hidden id="details.profInstitutionOrgNameId" path="details.profInstitutionOrgNameId" name="details.profInstitutionOrgNameId"/>
		<v:hidden id="highSpecialityId" name="highSpecialityId" value="${model.higherSpecialityId}"/>
		<v:hidden id="details.highProfQualificationId" path="details.highProfQualificationId" name="details.highProfQualificationId"/>
        
        
        <div class="clr10"><!--  --></div>      
        <p><span class="fieldlabel2"><label for="details.educationTypeId">Вид обучение</label><br/></span>
			<nacid:combobox id="details.educationTypeId" name="details.educationTypeId" attributeName="educationTypeCombo" path="details.educationTypeId"
							style="brd w500" onchange="showHideEduDivs();updateCertificateProfQualTxt();"/>
			<v:comboBoxValidator input="details.educationTypeId" required="$('education').visible()"/>
        </p>
        <div class="clr5"><!--  --></div>
        <script type="text/javascript">
        	function disableEducationDiv(educationDiv) {
        		$$('#' + educationDiv + ' select', '#' + educationDiv + ' input').each(function(el) {
	        		el.disabled = true;
	        	});
        	}
        	
        	function enableEducationDiv(educationDiv) {
        		$$('#' + educationDiv + ' select', '#' + educationDiv + ' input').each(function(el) {
	        		el.disabled = false;
	        	});
        	}
        	
        	function showHideEduDivs() {
	        	var eduCombo = $('details.educationTypeId');
	        	if(eduCombo.value == 1) {
	        		$('div_visshe').show();
	        		$('div_sredno').hide();
	        		$('div_SDK').hide();
	        		disableEducationDiv('div_sredno');
	        		disableEducationDiv('div_SDK');
	        		enableEducationDiv('div_visshe');
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
        </script>
        <div id="div_sredno" style="display:none;">
        	        
        	<fieldset><legend>Обучаваща институция</legend>      	
			
			<p><span class="fieldlabel2"><label for="details.profInstitutionNameTxt">Ново име</label><br/></span>
               	<v:textinput id="secBgName" name="details.profInstitutionNameTxt" value="${model.secondary ? model.profInstitutionNameTxt : ''}" class="brd w600" onkeydown="if(!isSpecialKeyPressed(event)) resetField('details.profInstitutionId');"/>
            </p>
			
       		<p><span class="fieldlabel2"><label for="details.profInstitutionOrgNameTxt">Старо име</label><br/></span>
               	<v:textinput id="secOrgName" name="details.profInstitutionOrgNameTxt" value="${model.secondary ? model.profInstitutionOrgNameTxt : ''}" class="brd w600" onkeydown="if(!isSpecialKeyPressed(event)) resetField('details.profInstitutionOrgNameId');"/>
            </p>
            <div class="clr"><!--  --></div>

            </fieldset>
            <div class="clr15"><!--  --></div>
            <p><span class="fieldlabel"><label for="sec_qualification">Професионална квалификация по документи</label><br/></span>
               	<v:textinput id="sec_qualification" value="${model.secondary ? model.secProfQualificationTxt : ''}" onkeydown="if(!isSpecialKeyPressed(event)){ resetField('details.secProfQualificationId');}" 
               	onchange="resetSecondarySpecialities(); updateCertificateProfQualTxt();" name="details.secProfQualificationTxt" class="brd w600"/>
               	<v:textValidator input="sec_qualification" required="$('div_sredno').visible()"/>
               	<v:hidden id="details.secProfQualificationId" name="details.secProfQualificationId" path="details.secProfQualificationId"/>
               	<script type="text/javascript">
		        	new Autocomplete('sec_qualification', { 
		       	    	serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?type=33',
		       	    	onSelect: updateSecQualificationData,
		       	   		width:500
		        	});
	        	</script>
            </p>
            <script type="text/javascript">

            function resetSecondarySpecialities() {
            	$('secondary_specialities_table').innerHTML = '';
            	$('sec_speciality').value = "";
            	$('secSpecialityId').value = "";
            }

            function specialityExists(tableId, specialityId, specialityTxt) {
				var ret = false;
            	$$('#' + tableId + ' input').each(function(el) {
					if (specialityId != null && specialityId != '' && el.name.indexOf("SpecialityId") > 0) {
						if (el.value == specialityId) {
							ret = true;
						}
					}
					if (specialityTxt != null && specialityTxt != '' && el.name.indexOf("SpecialityTxt") > 0) {
						if (el.value == specialityTxt.trim()) {
							ret = true;
						}
					}
	        	});
	        	if (ret) {
		        	alert("Специалността вече съществува");
	        	}
				return ret;
            }

            function addSecondarySpeciality() {
            	if (validateText($('sec_speciality'), true, 2, 150, null, '') && !specialityExists('secondary_specialities_table', $('secSpecialityId').value, $('sec_speciality').value)) {
            		//$('secSpecialityId').className = 'brd';
            		$('sec_speciality').removeClassName('errorinput');
        	    	var table = $('secondary_specialities_table');
        	    	var rowCount = table.rows.length;
        	    	var row = table.insertRow(rowCount);
        	    	var specialityName = '';

        	    	var cell0 = row.insertCell(0);
					var idField = new Element('input', { 'type': 'hidden', 'value':'0' });
					idField.name = 'specialities[' + specialitiesCount + '].id';
					cell0.style.display = 'none';
					cell0.appendChild(idField);

        	    	var cell1 = row.insertCell(1);
        	    	var specialityField = new Element('input', { 'type': 'hidden'});
        	    	specialityField.name = 'specialities[' + specialitiesCount + '].secondarySpecialityId';
        	    	if ($('secSpecialityId').value != '') { //dobavq se specialnost po ID
        	    		specialityField.name = 'specialities[' + specialitiesCount + '].secondarySpecialityId';
        	    		specialityField.value = $('secSpecialityId').value;
        	    	} else { //dobavq se specialnost po text
        	    		specialityField.name = 'specialities[' + specialitiesCount + '].secondarySpecialityTxt';
            	    	specialityField.value = $('sec_speciality').value.trim();

        	    	}
        	    	cell1.style.display = 'none';

					var cell2 = row.insertCell(2);
        			specialityName = $('sec_speciality').value;

        			cell1.appendChild(specialityField);
        			if ($('secSpecialityId').value != ''){
        				cell2.innerHTML = specialityName + ($('sec_qualification_degree').innerHTML != "" ?
        						", " + $('sec_qualification_degree').innerHTML : "");
        			} else {
        				cell2.innerHTML = specialityName;
        			}
        			var cell3 = row.insertCell(3);
        			var deleteButton = new Element('a', {'href': 'javascript:void(0);'});
        			deleteButton.onclick = function() {deleteSpeciality(this.parentNode);};
        			var img = new Element('img', {'src': '${pathPrefix}/img/icon_delete.png'});
        			deleteButton.insert(img);
        			cell3.appendChild(deleteButton);

        			specialitiesCount++;
        			$('specialities_count').value = specialitiesCount;
        			resetField($('sec_speciality'));
        			resetField($('secSpecialityId'));
        			$('sec_qualification_degree').innerHTML = "";
            	}
            }

            function addHigherSpeciality() {
            	if (validateText($('high_speciality'), true, 2, 150, null, '') && !specialityExists('higher_specialities_table', $('highSpecialityId').value, $('high_speciality').value)) {
            		$('high_speciality').removeClassName('errorinput');
        	    	var table = $('higher_specialities_table');
        	    	var rowCount = table.rows.length;
        	    	var row = table.insertRow(rowCount);
        	    	var specialityName = '';

					var cell0 = row.insertCell(0);
					var idField = new Element('input', { 'type': 'hidden', 'value':'0' });
					idField.name = 'specialities[' + specialitiesCount + '].id';
					cell0.style.display = 'none';
					cell0.appendChild(idField);

        	    	var cell1 = row.insertCell(1);
        	    	var specialityField = new Element('input', { 'type': 'hidden'});
        	    	if ($('highSpecialityId').value != '') { //dobavq se specialnost po ID
        	    		specialityField.name = 'specialities[' + specialitiesCount + '].higherSpecialityId';
        	    		specialityField.value = $('highSpecialityId').value;
        	    	} else { //dobavq se specialnost po text
        	    		specialityField.name = 'specialities[' + specialitiesCount + '].higherSpecialityTxt';
            	    	specialityField.value = $('high_speciality').value.trim();

        	    	}
        	    	cell1.style.display = 'none';

					var cell2 = row.insertCell(2);
        			specialityName = $('high_speciality').value;

        			cell1.appendChild(specialityField);
        			cell2.innerHTML = specialityName;

        			var cell3 = row.insertCell(3);
        			var deleteButton = new Element('a', {'href': 'javascript:void(0);'});
        			deleteButton.onclick = function() {deleteSpeciality(this.parentNode);};
        			var img = new Element('img', {'src': '${pathPrefix}/img/icon_delete.png'});
        			deleteButton.insert(img);
        			cell3.appendChild(deleteButton);

        			specialitiesCount++;
        			$('specialities_count').value = specialitiesCount;
        			resetField($('high_speciality'));
        			resetField($('highSpecialityId'));
            	}
            }

            function addHigherOrSdkSpeciality(sdk) {
            	var id = sdk ? 'sdk_speciality' : 'high_sdk_speciality';
            	var elementId = sdk ? 'sdkSpecialityId' : 'highSpecialityId';
            	var table = sdk ? 'sdk_specialities_table' : 'higher_sdk_specialities_table';
            	var prefix = sdk ? 'sdk' : 'higher';
            	if (validateText($(id), true, 2, 150, null, '')  && !specialityExists(table, $(elementId).value, $(id).value)) {
            		$(id).removeClassName('errorinput');
        	    	var rowCount = $(table).rows.length;
        	    	var row = $(table).insertRow(rowCount);
        	    	var specialityName = '';

        	    	var cell0 = row.insertCell(0);
					var idField = new Element('input', { 'type': 'hidden', 'value':'0' });
					idField.name = 'specialities[' + specialitiesCount + '].id';
					cell0.style.display = 'none';
					cell0.appendChild(idField);

					var cell1 = row.insertCell(1);
        	    	var specialityField = new Element('input', { 'type': 'hidden'});

        	    	var elementTxtValue = $(id).value;
        	    	if ($(elementId).value != '') { //dobavq se specialnost po ID
        	    		specialityField.name = 'specialities[' + specialitiesCount + '].' + prefix + 'SpecialityId';
        	    		specialityField.value = $(elementId).value;
        	    	} else { //dobavq se specialnost po text
        	    		specialityField.name = 'specialities[' + specialitiesCount + '].' + prefix + 'SpecialityTxt';
            	    	specialityField.value = elementTxtValue.trim();

        	    	}
        	    	cell1.style.display = 'none';
        	    	cell1.appendChild(specialityField);



        			var cell2 = row.insertCell(2);
        			specialityName = $(id).value;
        			cell2.innerHTML = specialityName;

        			var cell3 = row.insertCell(3);
        			var deleteButton = new Element('a', {'href': 'javascript:void(0);'});
        			deleteButton.onclick = function() {deleteSpeciality(this.parentNode, sdk);};
        			var img = new Element('img', {'src': '${pathPrefix}/img/icon_delete.png'});
        			deleteButton.insert(img);
        			cell3.appendChild(deleteButton);
        			specialitiesCount++;
        			$('specialities_count').value = specialitiesCount;

        			resetField($(id));
        			resetField($(elementId));
            	}
            }
        	function deleteSpeciality(tableData) {
        		var table = tableData.parentNode.parentNode;
    			var rowNumber = parseInt(tableData.parentNode.rowIndex);
    			table.deleteRow(rowNumber);
    			//iterateTable(table, false);
        	}

        	</script>

			<div class="clr10"><!--  --></div>
			<p>
				<span class="fieldlabel"><label>Избрани специалности</label></span>
			</p>

			<table id="secondary_specialities_table" border="0">
				<c:if test="${model.secondary && model.secondarySpecialitiesCount > 1}">
					<c:forEach var="item" items="${model.secondarySpecialities}" varStatus="varStatus">
						<tr>
							<td style="display:none;"><input type="hidden" value="0" name="specialities[${varStatus.index}].id"></td>
							<c:if test="${not empty item.secondarySpecialityId}">
							<td style="display:none;"><input type="hidden" name="specialities[${varStatus.index}].secondarySpecialityId" value="${item.secondarySpecialityId }"></td>
							</c:if>
							<c:if test="${empty item.secondarySpecialityId}">
							<td style="display:none;"><input type="hidden" name="specialities[${varStatus.index}].secondarySpecialityTxt" value="${item.secondarySpecialityTxt }"></td>
							</c:if>
							<td>${item.secondarySpecialityTxtToDisplay}</td>
							<td><a href="javascript:void(0);" onclick="deleteSpeciality(this.parentNode);"><img src="${pathPrefix}/img/icon_delete.png"/></a></td>
						</tr>
					</c:forEach>
				</c:if>
			</table>
			<div class="clr15"><!--  --></div>

            <p><span class="fieldlabel"><label for="sec_speciality">Специалност по документи</label><br/></span>
               	<v:textinput id="sec_speciality" class="brd w250" onkeydown="if(!isSpecialKeyPressed(event)){ resetField('secSpecialityId'); }"
               	onkeyup="if(this.value ==''){ $('sec_qualification_degree').innerHTML='';}" value="${model.secondary ? model.secondarySpeciality : ''}"/>
               	<v:textValidator input="sec_speciality" required="$('div_sredno').visible() && $('secondary_specialities_table').rows.length == 0"/>
               	<v:hidden id="secSpecialityId" name="secSpecialityId" value="${model.secondary ? model.secondarySpecialityId : ''}"/>
               	<span style="padding-left: 15px;"><a href="javascript:addSecondarySpeciality();">Добави специалност</a></span>
            </p>
            <script type="text/javascript">
            	new Autocomplete('sec_speciality', {
	      	   	serviceUrl:'${pathPrefix}/control/trainingcoursesuggestion?type=sec_speciality',
	      		dynamicParameters: function() {
	      			return {prof_qual_id : $('details.secProfQualificationId').value};
	      		},
	      	   	onSelect: updateSecSpecialityData,
	      	  	width:500
	       	});
            </script>
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
               	<nacid:combobox id="doc_type_sec" name="details.documentType" attributeName="documentDocTypeCombo" path="details.documentType" style="brd w500"/>
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
            <p><span class="fieldlabel2"><label for="details.profInstitutionNameTxt">Ново име</label><br/></span>
               	<v:textinput id="high_bg_name" name="details.profInstitutionNameTxt" value="${model.high ? model.profInstitutionNameTxt : ''}" class="brd w600" onkeydown="if(!isSpecialKeyPressed(event)) resetField('details.profInstitutionId');"/>
            </p>
            <p><span class="fieldlabel2"><label for="details.profInstitutionOrgNameTxt">Старо име</label><br/></span>
               	<v:textinput id="high_org_name" name="details.profInstitutionOrgNameTxt" value="${model.high ? model.profInstitutionOrgNameTxt : ''}" class="brd w600" onkeydown="if(!isSpecialKeyPressed(event)) resetField('details.profInstitutionOrgNameId');" />
            </p>
            </fieldset>
            <div class="clr15"><!--  --></div>
            <p>
            	<span class="fieldlabel"><label for="high_qualification">Професионална квалификация по документи</label></span>
        		<v:textinput onkeydown="if(!isSpecialKeyPressed(event)){ resetField('details.highProfQualificationId');}" id="high_qualification" class="brd w450 flt_lft"  value="${model.high ? model.highProfQualificationTxt : ''}" name="details.highProfQualificationTxt" onchange="updateCertificateProfQualTxt();"/>
        		<script type="text/javascript">
		        	new Autocomplete('high_qualification', {
		       	    	serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?type=30',
		       	    	onSelect: updateHighQualificationData,
		       	   		width:500
		        	});
	        	</script>
	        </p>
        	<div class="clr10"><!--  --></div>
			<p>
				<span class="fieldlabel"><label>Избрани специалности</label></span>
			</p>

			<table id="higher_specialities_table" border="0">
				<c:if test="${model.high and model.higherSpecialitiesCount > 1}">
					<c:forEach var="item" items="${model.higherSpecialities}" varStatus="varStatus">
						<tr>
							<td style="display:none;"><input type="hidden" value="0" name="specialities[${varStatus.index}].id"></td>
							<c:if test="${not empty item.higherSpecialityId}">
							<td style="display:none;"><input type="hidden" name="specialities[${varStatus.index}].higherSpecialityId" value="${item.higherSpecialityId }"></td>
							</c:if>
							<c:if test="${empty item.higherSpecialityId}">
							<td style="display:none;"><input type="hidden" name="specialities[${varStatus.index}].hiherSpecialityTxt" value="${item.higherSpecialityTxt }"></td>
							</c:if>
							<td>${item.higherSpecialityTxtToDisplay}</td>
							<td><a href="javascript:void(0);" onclick="deleteSpeciality(this.parentNode);"><img src="${pathPrefix}/img/icon_delete.png"/></a></td>
						</tr>
					</c:forEach>
				</c:if>
			</table>

			<div class="clr15"><!--  --></div>

        	<p>

        		<span class="fieldlabel"><label for="high_speciality">Специалност по документи</label></span>
        		<span class="flt_rgt"><a href="javascript:addHigherSpeciality();">Добави специалност</a></span>
        		<v:textinput onkeydown="if(!isSpecialKeyPressed(event)) resetField('highSpecialityId');" id="high_speciality" class="brd w450 flt_lft" value="${model.high ? model.higherSpeciality : ''}" />

        		<script type="text/javascript">
		        	new Autocomplete('high_speciality', {
		       	    	serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?type=31',
		       	    	onSelect: updateHighSpecialityData,
		       	   		width:500
		        	});
	        	</script>
        	</p>
        	<div class="clr"><!--  --></div>

        	 <div class="clr10"><!--  --></div>
        	<p><span class="fieldlabel"><label for="high_edu_level">ОКС</label><br/></span>
	        	<nacid:combobox id="high_edu_level" name="details.highEduLevelId" attributeName="highEduLevelCombo" path="details.highEduLevelId" style="brd w600"/>
				<%--<v:comboBoxValidator input="high_edu_level" required="$('div_visshe').visible()"/>--%>
	        </p>
	        <div class="clr"><!--  --></div>
            <p><span class="fieldlabel"><label for="details.documentType">Вид документ</label><br/></span>
               	<nacid:combobox id="doc_type_high" name="details.documentType" attributeName="documentDocTypeCombo" path="details.documentType" style="brd w500"/>
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
	               	<v:textinput id="high_sdk_bg_name" name="details.profInstitutionNameTxt" value="${model.sdk ? model.profInstitutionNameTxt : ''}"
	               				onkeydown="if(!isSpecialKeyPressed(event)) resetField('details.profInstitutionId');" class="brd w290"/>
	            </p>
	            <div class="clr5"><!--  --></div>
	            <p><span class="fieldlabel4"><label for="details.profInstitutionOrgNameTxt">Старо име</label><br/></span>
	               	<v:textinput id="high_sdk_org_name" name="details.profInstitutionOrgNameTxt" value="${model.sdk ? model.profInstitutionOrgNameTxt : ''}"
	               				onkeydown="if(!isSpecialKeyPressed(event)) resetField('details.profInstitutionOrgNameId');" class="brd w290"/>
	            </p>
	            </fieldset>
	            <div class="clr15"><!--  --></div>
	        	<p>
	            	<span class="fieldlabel3"><label for="high_qualification">Професионална квалификация по документи</label></span>
	        		<v:textinput onkeydown="if(!isSpecialKeyPressed(event)){ resetField('details.highProfQualificationId');}" id="high_sdk_qualification" class="brd w250 flt_lft"  value="${model.sdk ? model.highProfQualificationTxt : ''}" name="details.highProfQualificationTxt"/>
	        		<script type="text/javascript">
			        	new Autocomplete('high_sdk_qualification', {
			       	    	serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?type=30',
			       	    	onSelect: updateHighQualificationData,
			       	   		width:500
			        	});
		        	</script>
		        </p>
	        	<div class="clr"><!--  --></div>
	        	<p>
					<span class="fieldlabel3"><label>Избрани специалности</label></span>
				</p>

				<table id="higher_sdk_specialities_table" border="0">
					<c:if test="${model.sdk and model.higherSpecialitiesCount > 1}">
						<c:forEach var="item" items="${model.higherSpecialities}" varStatus="varStatus">
							<tr>
								<td style="display:none;"><input type="hidden" value="0" name="specialities[${varStatus.index}].id"></td>
								<c:if test="${not empty item.higherSpecialityId}">
								<td style="display:none;"><input type="hidden" name="specialities[${varStatus.index}].higherSpecialityId" value="${item.higherSpecialityId }"></td>
								</c:if>
								<c:if test="${empty item.higherSpecialityId}">
								<td style="display:none;"><input type="hidden" name="specialities[${varStatus.index}].hiherSpecialityTxt" value="${item.higherSpecialityTxt }"></td>
								</c:if>
								<td>${item.higherSpecialityTxtToDisplay}</td>
								<td><a href="javascript:void(0);" onclick="deleteSpeciality(this.parentNode);"><img src="${pathPrefix}/img/icon_delete.png"/></a></td>
							</tr>
						</c:forEach>
					</c:if>
				</table>

        		<div class="clr15"><!--  --></div>
        		<p>
	        		<span class="fieldlabel3"><label for="high_sdk_speciality">Специалност по документи</label></span>
	        		<v:textinput onkeydown="if(!isSpecialKeyPressed(event)) resetField('highSpecialityId');" id="high_sdk_speciality" class="brd w250 flt_lft" value="${model.sdk ? model.higherSpeciality : ''}" />

	        		<script type="text/javascript">
			        	new Autocomplete('high_sdk_speciality', {
			       	    	serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?type=31',
			       	    	onSelect: updateHighSpecialityData,
			       	   		width:500
			        	});
		        	</script>
	        	</p>
        		<div class="clr"><!--  --></div>
        		<span class="flt_rgt"><a href="javascript:addHigherOrSdkSpeciality(false);">Добави специалност</a></span>

	        	<div class="clr10"><!--  --></div>

	            <p><span class="fieldlabel3"><label for="high_edu_level">ОКС</label><br/></span>
	               	<nacid:combobox id="high_sdk_edu_level" name="details.highEduLevelId" attributeName="highEduLevelCombo" path="details.highEduLevelId" style="brd w250"/>
	               	<%--<v:comboBoxValidator input="high_sdk_edu_level" required="$('div_SDK').visible()"/>--%>
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
	        	<v:hidden id="sdkSpecialityId" name="sdkSpecialityId" value="${model.sdkSpecialityId }"/>
	        	<v:hidden id="details.sdkProfInstitutionId" path="details.sdkProfInstitutionId" name="details.sdkProfInstitutionId"/>
	        	<v:hidden id="details.sdkProfInstitutionOrgNameId" path="details.sdkProfInstitutionOrgNameId" name="details.sdkProfInstitutionOrgNameId"/>

	        	<fieldset><legend>Обучаваща институция</legend>
	        	<p><span class="fieldlabel4"><label for="details.sdkProfInstitutionNameTxt">Ново име</label><br/></span>
	               	<v:textinput id="sdkBgName" name="details.sdkProfInstitutionNameTxt" value="${model.sdk ? model.sdkProfInstitutionNameTxt : ''}" class="brd w290"
	               				onkeydown="if(!isSpecialKeyPressed(event)) resetField('details.sdkProfInstitutionId');"/>
	               	<v:textValidator input="details.sdkProfInstitutionNameTxt" minLength="2" maxLength="255" required="!$('sdkBgName').value.empty()"/>
	            </p>
	            <div class="clr5"><!--  --></div>
	       		<p><span class="fieldlabel4"><label for="details.sdkProfInstitutionOrgNameTxt">Старо име</label><br/></span>
	               	<v:textinput id="sdkOrgName" name="details.sdkProfInstitutionOrgNameTxt" value="${model.sdk ? model.sdkProfInstitutionOrgNameTxt : ''}" class="brd w290"
	               				onkeydown="if(!isSpecialKeyPressed(event)) resetField('details.sdkProfInstitutionOrgNameId');"/>
	               	<v:textValidator input="details.sdkProfInstitutionOrgNameTxt" minLength="2" maxLength="255" required="!$('sdkOrgName').value.empty()"/>
	            </p>
	            <div class="clr"><!--  --></div>
	            </fieldset>
	            <div class="clr15"><!--  --></div>

				<p>
	            	<span class="fieldlabel3"><label for="details.sdkProfQualificationTxt">Професионална квалификация по документи</label></span>
	        		<v:textinput onkeydown="if(!isSpecialKeyPressed(event)){ resetField('details.sdkProfQualificationId');}" id="sdk_qualification" class="brd w250 flt_lft" name="details.sdkProfQualificationTxt" value="${model.sdk ? model.sdkProfQualificationTxt : ''}" onchange="updateCertificateProfQualTxt()"/>
	        		<script type="text/javascript">
			        	new Autocomplete('sdk_qualification', {
			       	    	serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?type=30',
			       	    	onSelect: updateSDKQualificationData,
			       	   		width:500
			        	});
		        	</script>
		        </p>

				<div class="clr10"><!--  --></div>
				<p>
					<span class="fieldlabel3"><label>Избрани специалности</label></span>
				</p>

				<table id="sdk_specialities_table" border="0">
					<c:if test="${model.sdk and model.sdkSpecialitiesCount > 1}">
						<c:forEach var="item" items="${model.sdkSpecialities}" varStatus="varStatus">
							<tr>
								<td style="display:none;"><input type="hidden" value="0" name="specialities[${varStatus.index}].id"></td>
								<c:if test="${not empty item.sdkSpecialityId}">
								<td style="display:none;"><input type="hidden" name="specialities[${varStatus.index}].higherSpecialityId" value="${item.sdkSpecialityId }"></td>
								</c:if>
								<c:if test="${empty item.sdkSpecialityId}">
								<td style="display:none;"><input type="hidden" name="specialities[${varStatus.index}].hiherSpecialityTxt" value="${item.sdkSpecialityTxt }"></td>
								</c:if>
								<td>${item.sdkSpecialityTxtToDisplay}</td>
								<td><a href="javascript:void(0);" onclick="deleteSpeciality(this.parentNode);"><img src="${pathPrefix}/img/icon_delete.png"/></a></td>
							</tr>
						</c:forEach>
					</c:if>
				</table>

        		<div class="clr15"><!--  --></div>
        		<p>
	        		<span class="fieldlabel3"><label for="sdk_speciality">Специалност по документи</label></span>
	        		<v:textinput onkeydown="if(!isSpecialKeyPressed(event)) resetField('sdkSpecialityId');" name="sdk_speciality" id="sdk_speciality" class="brd w250 flt_lft" value="${model.sdk ? model.sdkSpeciality : ''}" />
	        		<v:textValidator input="sdk_speciality" required="($('div_SDK').visible() && $('sdk_specialities_table').rows.length == 0)"/>
	        		<script type="text/javascript">
			        	new Autocomplete('sdk_speciality', {
			       	    	serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?type=31',
			       	    	onSelect: updateSDKSpecialityData,
			       	   		width:500
			        	});
		        	</script>
	        	</p>
        		<div class="clr"><!--  --></div>
        		<span class="flt_rgt"><a href="javascript:addHigherOrSdkSpeciality(true);">Добави специалност</a></span>

        		<div class="clr20"><!--  --></div>

				<%-- <p><span class="fieldlabel3"><label for="sdk_edu_level">ОКС</label><br/></span>
	               	<nacid:combobox id="sdk_edu_level" name="details.sdkEduLevelId" attributeName="sdkEduLevelCombo" path="details.sdkEduLevelId" style="brd"/>
	               	<v:comboBoxValidator input="sdk_edu_level" required="$('div_SDK').visible()"/>
	            </p>--%>
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
	               	<v:textValidator input="details.sdkDocumentDate" maxLength="50" />
	            </p>
	            <div class="clr"><!--  --></div>
	            </fieldset>
            </div>
        </div>
        <script type="text/javascript">

        	new Autocomplete('secBgName', {
       	    	serviceUrl:'${pathPrefix}/control/professionalinstitutionsuggest',
       	    	dynamicParameters: function() {
    	      		return {education_type : $('details.educationTypeId').value};
    	      	},
       	   		onSelect: updateProfInstitutionData,
       	   		width:600
        	});

	       	new Autocomplete('secOrgName', {
	      	   	serviceUrl:'${pathPrefix}/control/professionalinstitutionsuggest?formername=1',
	      		dynamicParameters: function() {
	      			return {institution : $('details.profInstitutionId').value};
	      		},
	      	   	onSelect: updateProfInstitutionOrgNameId,
	      	  	width:600
	       	});

        	new Autocomplete('high_bg_name', {
    	    	serviceUrl:'${pathPrefix}/control/professionalinstitutionsuggest',
       	    	dynamicParameters: function() {
    	      		return {education_type : $('details.educationTypeId').value};
    	      	},
    	    	onSelect: updateProfInstitutionData,
    	    	width:600
        	});

        	new Autocomplete('high_org_name', {
    	    	serviceUrl:'${pathPrefix}/control/professionalinstitutionsuggest?formername=1',
    	    	dynamicParameters: function() {
	      			return {institution : $('details.profInstitutionId').value};
	      		},
    	    	onSelect: updateProfInstitutionOrgNameId,
    	    	width:600
        	});

            new Autocomplete('high_sdk_bg_name', {
        	    serviceUrl:'${pathPrefix}/control/professionalinstitutionsuggest?institution_type=' + higherProfessionalInstitutionType,
        	    onSelect: updateProfInstitutionData,
        	    width:500
        	});

        	new Autocomplete('high_sdk_org_name', {
        	    serviceUrl:'${pathPrefix}/control/professionalinstitutionsuggest?formername=1',
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
                serviceUrl:'${pathPrefix}/control/professionalinstitutionsuggest',
                onSelect: updateSDKProfInstitutionData,
                width:500
            });

        	new Autocomplete('sdkOrgName', {
            	serviceUrl:'${pathPrefix}/control/professionalinstitutionsuggest?formername=1',
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

	<fieldset><legend><v:checkboxinput id="submit_experience" path="details.hasExperience" name="details.hasExperience" onclick="toggleSubmitDivs(); updateCertificateProfQualTxt()"/>Стаж</legend>
		<v:hidden id="experienceRecord.id" path="experienceRecord.id" name="experienceRecord.id"/>
		<v:hidden id="experienceRecord.trainingCourseId" path="experienceRecord.trainingCourseId" name="experienceRecord.trainingCourseId"/>
		<v:hidden id="experienceRecord.years" path="experienceRecord.years" name="experienceRecord.years"/>
		<v:hidden id="experienceRecord.months" path="experienceRecord.months" name="experienceRecord.months"/>
		<v:hidden id="experienceRecord.days" path="experienceRecord.days" name="experienceRecord.days"/>
		<v:hidden id="experienceRecord.nomenclatureProfessionExperienceId" path="experienceRecord.nomenclatureProfessionExperienceId" name="experienceRecord.nomenclatureProfessionExperienceId"/>

		<div class="clr"><!--  --></div>

		<div id="experience">
			<div id="experience_comment">
				<i>${regprofEservicesExpComment}</i>
			</div>
			<div class="clr10"><!--  --></div>
      	<p><span class="fieldlabel2"><label for="experienceRecord.professionExperienceTxt">Професия</label><br/></span>
       		<v:textinput name="experienceRecord.professionExperienceTxt" value="${empty model.professionExperienceTxt ? (empty model.sdkProfQualificationTxt ? model.highProfQualificationTxt : model.sdkProfQualificationTxt) : model.professionExperienceTxt}" id="professionName" class="brd w500" onkeydown="if(!isSpecialKeyPressed(event)) resetField('experienceRecord.nomenclatureProfessionExperienceId');" onchange="updateCertificateProfQualTxt()"/>
       	</p>

		<v:textValidator input="experienceRecord.professionExperienceTxt" maxLength="80" required="$('submit_experience').checked" />
		<script type="text/javascript">
 		   new Autocomplete('professionName', {
    	     serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?type=28',
    	     onSelect: updateProfession,
    	     width:500
    	   });

 		   function updateProfession(value, data) {
			  var obj = data.evalJSON(true);
			  $("experienceRecord.nomenclatureProfessionExperienceId").value = obj.id;
  			  $("professionName").value = obj.name;
  			  updateCertificateProfQualTxt();
 		   }
		</script>

		<div class="clr5"><!--  --></div>
		<p class="flt_rgt"><a onclick="javascript:addExperienceDocumentRow();"href="javascript:void(0);">Добави нов документ</a></p>
		<div class="clr10"><!--  --></div>
		<c:forEach var="experienceDocument" items='${requestScope["com.nacid.bl.impl.regprof.external.applications.ExtRegprofTrainingCourseImpl"].experienceRecord.professionExperienceDocuments}'  varStatus="st">
		<fieldset id="experience_document_row${st.index }"><legend>Документи</legend>
			<v:hidden name="experienceRecord.professionExperienceDocuments[${st.index}].id" path="experienceRecord.professionExperienceDocuments[${st.index}].id" />
			<c:if test="${st.index != 0}">
				<p class="flt_rgt"><a onclick="this.parentNode.parentNode.remove();" style="color: red" href="javascript:void(0);">Премахни</a></p>
			</c:if>
			<p><span class="fieldlabel2"><label for=" experienceRecord.professionExperienceDocuments[${st.index }].profExperienceDocTypeId">Тип документ за стаж</label><br/></span>
				<nacid:combobox id="experienceRecord.professionExperienceDocuments[${st.index}].profExperienceDocTypeId" name="experienceRecord.professionExperienceDocuments[${st.index}].profExperienceDocTypeId"
								path="experienceRecord.professionExperienceDocuments[${st.index}].profExperienceDocTypeId" attributeName="profExperienceDocTypeCombo"
								onchange="updateForExperienceCalculation(this);" style="brd"/>
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
			<div style="display:none;"><%--GGeirguev: Otnachalo q bqh slojil tazi otmetka, no posle Irina kaza che nqma da se vyvejda ot potrebitelq, taka che prosto q skrivam da ne se vijda! --%>
			<p><span class="fieldlabel2"><label for="experienceRecord.professionExperienceDocuments[${st.index}].documentIssuer">Влиза за изчисление</label></span>
				<v:checkboxinput name="experienceRecord.professionExperienceDocuments[${st.index}].forExperienceCalculation" id="experienceRecord.professionExperienceDocuments[${st.index}].forExperienceCalculation" path="experienceRecord.professionExperienceDocuments[${st.index}].forExperienceCalculation"/>
			</p>
			</div>
			<div class="clr"><!--  --></div>

			<fieldset><legend>Работил за периода:</legend>
			<div class="clr10"><!--  --></div>
			<p class="flt_rgt"><a id="addDatesRow${st.index}" href="javascript:addDatesRow(${st.index});">Добави нов период</a></p>
			<div class="clr"><!--  --></div>

			<c:choose>
				<c:when test='${requestScope["com.nacid.bl.impl.regprof.external.applications.ExtRegprofTrainingCourseImpl"].experienceRecord.id > 0}'>
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
							<c:if test='${pStatus.index > 0}'>
								<a id="document${st.index}remove${pStatus.index}" href="#" onclick="this.parentNode.remove();return false;" style="color: red;" class="pl20">Премахни</a>
							</c:if>
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

	<p><span class="fieldlabel"><label for="experienceRecord.professionExperienceTxt">Професионална квалификация, за която се иска издаването на удостоверение</label><br/></span>
    	<v:textinput name="details.certificateProfQualificationTxt" value="${model.certificateProfQualificationTxt}" id="details.certificateProfQualificationTxt" class="brd w450 flt_lft" onkeydown="if(!isSpecialKeyPressed(event)) resetField('details.certificateProfQualificationId');" />
    </p>
	<v:textValidator input="details.certificateProfQualificationTxt" minLength="2" maxLength="150" required="true" />
	<script type="text/javascript">
 		   new Autocomplete('details.certificateProfQualificationTxt', {
    	     serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?type=43',
    	     onSelect: updateCertificateProfQual,
    	     width:500
    	   });
 		   
	</script>
	<v:hidden id="details.certificateProfQualificationId" path="details.certificateProfQualificationId" name="details.certificateProfQualificationId"/>
	
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
		<div style="display:none">
		<p><span class="fieldlabel2"><label for="forExperienceCalculation">Влиза за изчисление</label></span>
			<v:checkboxinput name="forExperienceCalculation" id="forExperienceCalculation"  disabled="disabled"/>
		</p>
		</div>
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

	
	<v:hidden id="specialities_count" name="specialities_count" value="${specialitiesCount}" />
</v:form>
<div class="clr20"><!--  --></div>

<script type="text/javascript">
/*if (window.location.pathname.lastIndexOf('view') != -1) { // view mode
	$('addDatesRow').hide();
	for (var i = 1; i < datesCount; i++) {
		$('remove' + i).hide();
	}
}*/
/*if ($('submit_education').checked == false && $('submit_experience').checked == false) {
	$('submit_education').checked = true;
	$('education').show();
}*/

toggleSubmitDivs();
if($('secSpecialityId').value != ""){
	changeDegree();
}
showHideEduDivs();
//getSecSpecialities();
</script>