<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="validation"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h3 class="title"><span>${operationStringForScreens } типове документи</span></h3>
<nacid:documentType>
	<validation:form action="${pathPrefix }/control/nom_doctype/save" method="post" name="docTypeForm" backurl="${pathPrefix }/control/nom_doctype/list?getLastTableState=1">
		<input type="hidden" name="id" value="${id }" />
		<fieldset><legend>Данни за тип документ</legend>
			<nacid:systemmessage />
			<p>
				<span class="fieldlabel"><label for="name">Наименование</label></span>
				<validation:textinput class="brd w600" maxlength="80" name="name"  value="${name }" />
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="flt_lft fieldlabel">Входящ/изходящ</span>
				<span class="flt_lft"><nacid:radiobutton name="is_incoming" attributeName="isIncomingRadio"/></span>
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="flt_lft fieldlabel">Завежда се в деловодството</span>
				<span class="flt_lft"><nacid:radiobutton name="has_docflow_id" attributeName="hasDocflowIdRadio"/></span>
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label for="document_template">Шаблон на документ</label><br /></span>
				<validation:textinput  id="docTemplate" name="document_template" value="${documentTemplate}" class="brd w600" disabled="disabled"/>
				<img src="/nacid/img/icon_edit.png" onclick="editDocTemplate();" />
				<validation:hidden id="docTemplateHidden"  name="document_template" value="${documentTemplate}" />
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label for="dateFrom">от дата</label></span>
				<validation:dateinput name="dateFrom" id="dateFrom" value="${dateFrom }" style="brd w200" />
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label for="dateTo">до дата</label></span>
				<validation:dateinput name="dateTo" id="dateTo" value="${dateTo }" style="brd w200" /></p>
			<div class="clr"><!--  --></div>

			<div class="clr"><!--  --></div>
			<div class="flt_lft">
				<div class="flt_lft fieldlabel"><b>Категория на документа</b></div>
				<div class="clr"><!--  --></div>
				<div class="flt_lft">
					<c:forEach var="cat" items="${documentCategoriesMap}">
						<input name="doc_category_id" value="${cat.key}" id="cat_${cat.key}" type="checkbox" <c:if test="${not empty documentType.docCategoryIdMap[cat.key]}">checked="checked"</c:if> /><label for="at${cat.key}">${cat.value}</label><br>
					</c:forEach>
				</div>
				<div class="clr"><!--  --></div>
			</div>
		</fieldset>
		<validation:dateValidator input="dateFrom" format="d.m.yyyy" required="false" beforeToday="true" />
		<validation:dateValidator input="dateTo" format="d.m.yyyy" required="false" beforeToday="true" />
		<validation:dateIntervalValidator dateFrom="dateFrom" dateTo="dateTo" format="d.m.yyyy" />
		<validation:textValidator input="name" minLength="1" maxLength="80" required="true" regex="${validations.nomenclature_name_with_fullstop_digits}" errormessage="${messages.err_nomenclature_name_with_fullstop_digits}" />
	</validation:form>
</nacid:documentType>
<script type="text/javascript">
	function editDocTemplate() {
		if ($('docTemplate').disabled) {
			$('docTemplate').enable();
			$('docTemplateHidden').disable();
		}
	}
</script>
<%@ include file="/screens/footer.jsp"%>
