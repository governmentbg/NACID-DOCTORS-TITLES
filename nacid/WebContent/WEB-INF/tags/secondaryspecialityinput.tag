<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" dynamic-attributes="dynamicAttrs"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ attribute name="value" required="false" rtexprvalue="true"%>
<%@ attribute name="path" required="false" rtexprvalue="true"%>
<%@ attribute name="additionaltext" required="false" rtexprvalue="true"%>
<%@ attribute name="id" required="true" rtexprvalue="true"%>
<%@ attribute name="fieldName" required="true" rtexprvalue="true"%>
<%@ attribute name="onsuccess" required="false" rtexprvalue="true"%>
<%@ attribute name="labelclass" required="false" rtexprvalue="true"%>

<div id="flat_nomenclature_${id}">
	<div style="display:none;" id="correct_message_${id}" class="correct">Данните бяха успешно въведени в номенклатурата!</div>
	<div style="display:none;" id="error_message_${id}" class="error">Получи се грешка при опит за запис в номенклатурата!</div>
	<div style="display:none;" id="present_message_${id}" class="error">Номенклатура с това име вече е записана в базата данни!</div>
	
	<span id="record_id_${id}" style="display:none;"></span>
	<span class="<c:choose><c:when test="${empty labelclass}">fieldlabel</c:when><c:otherwise>${labelclass}</c:otherwise></c:choose>"><label for="${id}">${fieldName}</label></span>
	<v:tinput path="${path }" value="${value }">
	<input id="${id }" type="text" value="<c:out value='${value }' />"
		<c:forEach var="attrEntry" items="${dynamicAttrs}">
    		${attrEntry.key}="${attrEntry.value}"
    	</c:forEach>
		${additionaltext}
	/>
	</v:tinput>

	<span class="flt_rgt" id="${id}_new">
		<a href="javascript:void(0);" onclick="showSubwindow('${id}');" id="save_${id}">Нов</a>
	</span>
	
	<span id="new_flat_nomeclature_${id}" style="display:none; padding: 20px; width: 825px; height: auto;">
		<a href="javascript:void(0);" style="margin-right: 10px;" onclick="saveSecondarySpeciality('${id}', '${onsuccess}');">Запис</a>
   		<a href="javascript:void(0);" onclick="hideSubwindow('${id}'); $('correct_message_${id}').hide(); clearInputError($('${id}'));">Отказ</a>
	</span>
	<div style="clear: both;"><!--  --></div>
	<div class="clr10"><!--  --></div>
</div>

<script type="text/javascript">
if (window.location.pathname.lastIndexOf('view') != -1) { //ne raboti s window.onload = function() {...};
	$('${id}_new').hide();
}

new Autocomplete($('${id}'), { 
	serviceUrl:'/nacid_regprof/control//trainingcoursesuggestion?type=sec_speciality',
	dynamicParameters: function() {
  		return {prof_qual_id : $('details.secProfQualificationId').value};
  	},
	onSelect: updateSecSpeciality,
	width:500
});

function updateSecSpeciality(value, data) {
	var id = '${id}';
	$('record_id_${id}').innerHTML = data.evalJSON(true).id;
	if ('${onsuccess}' != '') {
		eval('${onsuccess}');
	}
	//changeDegree();
}

function clearMessages(id) {
	$('correct_message_' + id).hide();
	$('error_message_' + id).hide();
	$('present_message_' + id).hide();
}

function showSubwindow(id) {
	if ($$('.subwindow_open').length >= 1) {
		return;
	}
	clearMessages(id);
	$('flat_nomenclature_' + id).addClassName('subwindow');
	$('flat_nomenclature_' + id).addClassName('subwindow_open');
	$(id + '_new').hide();
	$('new_flat_nomeclature_' + id).show();
	if ($('Autocomplete_'+id) != null) {
		$('Autocomplete_'+id).parentNode.style.display = "none";
	}
}

function hideSubwindow(id) {
	$('flat_nomenclature_' + id).removeClassName('subwindow');
	$('flat_nomenclature_' + id).removeClassName('subwindow_open');
	$(id + '_new').show();
	$('new_flat_nomeclature_' + id).hide();
	//$('correct_message_' + id).hide();
	$('error_message_' + id).hide();
	if ($('Autocomplete_'+id) != null) {
		$('Autocomplete_'+id).parentNode.style.display = "block";
	}
}

function validateSecProfQualification() {
	if (!$('details.secProfQualificationId').value.empty() && !$('sec_qualification').value.empty()) {
		return true;
	} else {
		setErrorOnInput($('sec_qualification'), 'Трябва да посочите професионална квалификация');
		return false;
	}
}

function saveSecondarySpeciality(id, onSuccess) {
	if (validateText($(id), true, 2, 100, null, '') == true && validateSecProfQualification()) {
		new Ajax.Request('/nacid_regprof/control/sec_speciality_ajax/save', {
	  		method: 'post',
	  	  	parameters: {prof_qual_id: $('details.secProfQualificationId').value, name: $(id).value},
	  	  	onSuccess: function(transport) {
	      		var response = transport.responseText || "no response text";
				var json = response.evalJSON();
				if (parseInt(json.id) > 0) {
					clearInputError(id);
					$('record_id_' + id).innerHTML = json.id;
					$('correct_message_' + id).show();
					$('error_message_' + id).hide();
					$('present_message_' + id).hide();
					if (onSuccess != '') {
						eval(onSuccess);
					}
					hideSubwindow(id);
				}
				else if (parseInt(json.id) == 0) {
					$('correct_message_' + id).hide();
					$('error_message_' + id).hide();
					$('present_message_' + id).show();	
				}
				else {
					$('correct_message_' + id).hide();
					$('error_message_' + id).show();
					$('present_message_' + id).hide();
				}
	  	  },
	  	  onFailure: function() {
	  		$('correct_message_' + id).hide();
			$('error_message_' + id).show();
			$('present_message_' + id).hide();
	  	  }
		});
	}
}

</script>