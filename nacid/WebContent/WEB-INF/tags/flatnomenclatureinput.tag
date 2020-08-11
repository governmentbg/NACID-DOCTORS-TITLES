<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" dynamic-attributes="dynamicAttrs"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ attribute name="value" required="false" rtexprvalue="true"%>
<%@ attribute name="path" required="false" rtexprvalue="true"%>
<%@ attribute name="additionaltext" required="false" rtexprvalue="true"%>
<%@ attribute name="id" required="true" rtexprvalue="true"%>
<%@ attribute name="nomenclatureType" required="true" rtexprvalue="true"%>
<%@ attribute name="fieldName" required="true" rtexprvalue="true"%>
<%@ attribute name="onsuccess" required="false" rtexprvalue="true"%>
<%@ attribute name="labelclass" required="false" rtexprvalue="true"%>
<%@ attribute name="windowLevel" required="false" rtexprvalue="true"%>
<%@ attribute name="maxLength" required="false" rtexprvalue="true"%>

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
		<a href="javascript:void(0);" onclick="showFlatNomInputSubwindow('${id}', ${empty windowLevel ? 1 : windowLevel});" id="save_${id}">Нов</a>
	</span>
	
	<span id="new_flat_nomeclature_${id}" style="display:none; padding: 20px; width: 825px; height: auto;">
		<a href="javascript:void(0);" style="margin-right: 10px;" onclick="saveFlatNomenclature('${id}', '${nomenclatureType}', '${onsuccess}', ${empty maxLength ? 100 : maxLength}, ${empty windowLevel ? 1 : windowLevel});">Запис</a>
   		<a href="javascript:void(0);" onclick="hideFlatNomInputSubwindow('${id}', ${empty windowLevel ? 1 : windowLevel}); $('correct_message_${id}').hide(); clearInputError($('${id}'));">Отказ</a>
	</span>
	<div style="clear: both;"><!--  --></div>
	<div class="clr10"><!--  --></div>
</div>

<script type="text/javascript">
if (window.location.pathname.lastIndexOf('view') != -1) { //ne raboti s window.onload = function() {...};
	$('${id}_new').hide();
}

new Autocomplete($('${id}'), {
     serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?type=${nomenclatureType}',
     onSelect: updateFlatNomenclature${id},
     width:500
});

function updateFlatNomenclature${id}(value, data) {
	var id = '${id}';
	//$(id).value = data.evalJSON(true).name;
	$('record_id_${id}').innerHTML = data.evalJSON(true).id;
	if ('${onsuccess}' != '') {
		eval('${onsuccess}');
	}
}
</script>