<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" dynamic-attributes="dynamicAttrs"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ attribute name="value" required="false" rtexprvalue="true"%>
<%@ attribute name="originalSpecialityValue" required="false" rtexprvalue="true"%>
<%@ attribute name="path" required="false" rtexprvalue="true"%>
<%@ attribute name="additionaltext" required="false" rtexprvalue="true"%>
<%@ attribute name="id" required="true" rtexprvalue="true"%>
<%@ attribute name="comboAttribute" required="true" rtexprvalue="true"%>
<%@ attribute name="chooseMultiple" required="true" rtexprvalue="true"%>
<%@ attribute name="specialityIdInput" required="true" rtexprvalue="true"%>
<%@ attribute name="specialityOnkeydown" required="false" rtexprvalue="true"%>

<%@ attribute name="addOriginalSpeciality" required="false" rtexprvalue="true"%>
<%@ attribute name="originalSpecialityIdInput" required="false" rtexprvalue="true"%>
<%@ attribute name="originalSpecialityOnkeydown" required="false" rtexprvalue="true"%>
<%@ attribute name="specialityList" required="${chooseMultiple == true}" rtexprvalue="true" type="java.util.List"%>
<%@ attribute name="originalSpecialityList" required="${addOriginalSpeciality == true}" rtexprvalue="true" type="java.util.List"%>

<div id="speciality_outer_div_${id}">
	<div id="specialityList_${id}" style="display: none;">
	<p><span class="fieldlabel"><label>Избрани специалности</label></span>
	<table id="specialityListTable_${id}">
		<c:if test="${chooseMultiple == true && specialityList != null}">		
			<v:splist specialities="${specialityList }" originalSpecialities="${originalSpecialityList}">
				<tr id="specialityList_${id}row_${speciality.id }">
					<td>${specialityName}</td>
                    <td style="display:none;">
                        <v:hidden name="specialitiesListItem_${id}_${cnt}" value="${specialityId}"/>
                        <c:if test="${addOriginalSpeciality}">
                            <v:hidden name="specialitiesListItem_original_${id}_${cnt}" value="${originalSpecialityId}"/>
                        </c:if>

                    </td>
					<td><a href="javascript:void(0);" onclick="removeSpecialityFromList(this.parentNode.parentNode, '${id}');">
					<img src="/nacid/img/icon_delete.png"/></a></td>
				</tr>
			</v:splist>
		</c:if>
	</table>
	</p>
	</div>
	<div id="speciality_inner_div_${id}">
		<div style="display:none;" id="correct_message_${id}" class="correct">Данните за специалността бяха успешно въведени в номенклатурата!</div>
		<div style="display:none;" id="error_message_${id}" class="error">Получи се грешка при опит за запис в номенклатурата!</div>
		<div style="display:none;" id="present_message_${id}" class="error">Специалност с това име вече е записана в базата данни!</div>
		<div class="clr"><!--  --></div>
		<span class="fieldlabel"><label for="${id}">Специалност</label></span>
		<v:tinput path="${path }" value="${value }">
			<input id="${id }" type="text" value="<c:out value='${value }' />"
			<c:forEach var="attrEntry" items="${dynamicAttrs}">
           		${attrEntry.key}="${attrEntry.value}" </c:forEach>
				${additionaltext}
                <c:if test="${not empty specialityOnkeydown}">
                    onkeydown="${specialityOnkeydown}"
                </c:if>
            />
		</v:tinput>
		<p class="flt_rgt" id="${id}_new">
			<a href="javascript:void(0);" onclick="showHideSpecialityDivs('${id}');" id="specSave_${id}">Нов</a>
			<div class="clr"><!--  --></div>
		</p>
	</div>
    <div id="new_speciality_div_${id}" style="display:none; padding: 20px; width: 825px; height: auto;" class="subwindow">
        <div class="clr"><!--  --></div>
        <v:hidden id="specialityClass_${id }"/>
        <span class="fieldlabel"><label for="profession_group_id_${id}">Професионално направление</label></span>
        <nacid:combobox name="profGroupId" id="profession_group_id_${id }" attributeName="${comboAttribute}" style="w500 brd"/>
        <a href="javascript:void(0);" style="margin-right: 10px;" onclick="saveSpeciality('${id}', ${specialityIdInput}, ${chooseMultiple});">Запис</a>
        <a href="javascript:void(0);" onclick="showHideSpecialityDivs('${id}'); clearInputError($('${id}'));">Отказ</a>
    </div>

    <div id="after_speciality_div_${id}"></div>
    <c:if test="${addOriginalSpeciality}">
        <div id="speciality_inner_div_original_${id}">
            <div style="display:none;" id="correct_message_original_${id}" class="correct">Данните за оригинално наименование на специалността бяха успешно въведени в номенклатурата!</div>
            <div style="display:none;" id="error_message_original_${id}" class="error">Получи се грешка при опит за запис в номенклатурата!</div>
            <div style="display:none;" id="present_message_original_${id}" class="error">Оригинално наименование на специалност с това име вече е записана в базата данни!</div>
            <div class="clr"><!--  --></div>
            <span class="fieldlabel"><label for="original_${id}">Специалност - оригинално наименование</label></span>
            <input id="original_${id }" type="text" value="<c:out value='${originalSpecialityValue }' />"
            <c:forEach var="attrEntry" items="${dynamicAttrs}">
                ${attrEntry.key}="${attrEntry.value}"
            </c:forEach>
            <c:if test="${not empty originalSpecialityOnkeydown}">
                onkeydown="${originalSpecialityOnkeydown}"
            </c:if>
            />
            <p class="flt_rgt" id="original_${id}_new">
                <a href="javascript:void(0);" onclick="showHideOriginalSpecialityDivs('original_${id}');" id="specSave_original_${id}">Нов</a>
            <div class="clr"><!--  --></div>
            </p>
        </div>
        <div id="after_speciality_div_original_${id}"></div>
    </c:if>
    <div class="clr"><!--  --></div>
    <div>
        <c:if test="${chooseMultiple == true}">
            <c:choose>
                <c:when test="${addOriginalSpeciality}">
                    <p id="addSpeciality_original_${id}" class="flt_rgt"><a href="javascript:void(0);" onclick="addSpecialityToList('${id}', ${specialityIdInput}, 'original_${id}', ${originalSpecialityIdInput});">Добави специалност</a></p>
                </c:when>
                <c:otherwise>
                    <p id="addSpeciality_original_${id}" class="flt_rgt"><a href="javascript:void(0);" onclick="addSpecialityToList('${id}', ${specialityIdInput});">Добави специалност</a></p>
                </c:otherwise>
            </c:choose>

        </c:if>
    </div>
    <div id="new_speciality_div_original_${id}" style="display:none; padding: 20px; width: 825px; height: auto;" class="subwindow">
        <div class="clr"><!--  --></div>
        <v:hidden id="specialityClass_original_${id }"/>
        <a class="flt_rgt" href="javascript:void(0);" onclick="showHideOriginalSpecialityDivs('original_${id}'); clearInputError($('original_${id}'));">Отказ</a>
        <a class="flt_rgt" href="javascript:void(0);" style="margin-right: 10px;" onclick="saveOriginalSpeciality('original_${id}', ${originalSpecialityIdInput}, ${chooseMultiple});">Запис</a>

    </div>
</div>



<c:if test="${chooseMultiple == true && specialityList != null}">
	<script type="text/javascript">
		showHideSpecialityList('${id}');
	</script>
</c:if>
<script type="text/javascript">
	if(window.location.pathname.lastIndexOf('view') != -1){
		$("specSave_${id}").hide();
	}
	$('${id}').addClassName('flt_lft'); // IE7 fix
</script>