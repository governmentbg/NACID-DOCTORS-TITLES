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
<%@ attribute name="nomenclatureValues" required="true" rtexprvalue="true"%>
<%@ attribute name="inputClass" required="false" rtexprvalue="true"%>
<c:set value="${empty inputClass ? 'brd w400' : inputClass}" var="inpCls" />

<div id="flat_nomenclature_${id}">
	<div style="display:none;" id="correct_message_${id}" class="correct">Данните бяха успешно въведени в номенклатурата!</div>
	<div style="display:none;" id="error_message_${id}" class="error">Получи се грешка при опит за запис в номенклатурата!</div>
	<div style="display:none;" id="present_message_${id}" class="error">Номенклатура с това име вече е записана в базата данни!</div>
	
    <span class="<c:choose><c:when test="${empty labelclass}">fieldlabel</c:when><c:otherwise>${labelclass}</c:otherwise></c:choose>"><label for="${id}">${fieldName}</label></span>
	<span id="flat_nomenclature_id_${id}_combo"><nacid:combobox name="${id}" id="${id}" attributeName="${nomenclatureValues}" path="${value }" style="${inpCls}"/></span>
    <span class="flt_rgt" id="${id}_new">
		<a href="javascript:void(0);" onclick="showSubwindowForSelectFlatNomenclature('${id}');" id="new_${id}">Нов</a>
	</span>
    <div class="clr"><!--  --></div>
	<span id="new_flat_nomeclature_${id}" style="display:none; margin-top:-30px; padding: 20px; width: 825px; height: auto;">
		<span class="<c:choose><c:when test="${empty labelclass}">fieldlabel</c:when><c:otherwise>${labelclass}</c:otherwise></c:choose>"><label for="input_${id}">${fieldName}</label></span>
        <input name="input_${id}" id="input_${id}" value="" class="brd w400"/>
        <a href="javascript:void(0);" style="margin-right: 10px;" onclick="saveSelectFlatNomenclature('${id}', '${nomenclatureType}', '${onsuccess}');">Запис</a>
   		<a href="javascript:void(0);" onclick="hideSubwindowForSelectFlatNomenclature('${id}'); $('correct_message_${id}').hide(); clearInputError($('input_${id}'));">Отказ</a>
	</span>
	<div style="clear: both;"><!--  --></div>
	<div class="clr10"><!--  --></div>
</div>

<script type="text/javascript">
windowLevel = typeof(windowLevel) == 'undefined' ? new Object() : windowLevel;
windowLevel['${id}'] = ${empty windowLevel ? 1 : windowLevel};
if (window.location.pathname.lastIndexOf('view') != -1) { //ne raboti s window.onload = function() {...};
	$('${id}_new').hide();
}
</script>