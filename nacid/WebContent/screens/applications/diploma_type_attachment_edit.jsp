<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<h3 class="title"><span>${operationStringForScreens } прикачен файл</span></h3>
<nacid:diplomaTypeAttachmentEdit>
	<v:form action="${pathPrefix }/control/diploma_type_attachment/save"
		method="post" name="form_diploma_type_attachment"
		onsubmit="startProgressUpdater();" enctype="multipart/form-data"
		backurl="${backUrlDiplType }">

		<c:if test="${id!=0 }">
			<div class="clr"><!--  --></div>
			<a class="flt_rgt" href="${pathPrefix }/control/diploma_type_attachment/${fileName }?id=${id }&amp;original=1" 
				target="_blank">Свали прикачения файл</a>
			<div class="clr"><!--  --></div>
		</c:if>

		<nacid:systemmessage />


		<input id="id" type="hidden" name="id" value="${id }" />
		<input id="diplomaTypeId" type="hidden" name="diplomaTypeId"
			value="${diplomaTypeId }" />
		<fieldset><legend>Основни данни за
		${diplomaTypeTitle }</legend>


		<p><span class="fieldlabel2"><label for="docDescr">Описание</label><br />
		</span> <textarea id="docDescr" class="brd w600" rows="3" cols="40"
			name="docDescr">${docDescr }</textarea></p>
		<div class="clr"><!--  --></div>
		
		<p>
			<span class="fieldlabel2"><label for="docTypeId">Тип документ</label></span> 
			<nacid:combobox id="docTypeId" name="docTypeId" attributeName="docTypeCombo" />
		</p>
		<div class="clr"><!--  --></div>


		<p><span class="fieldlabel"><label for="doc_content">Файл</label><br />
		</span> <input class="brd w240" name="doc_content" id="doc_content" type="file" value="" />&nbsp;&nbsp;${fileName }</p>
		<div class="clr"><!--  --></div>

		</fieldset>

		<nacid:loadingBar />

	</v:form>
</nacid:diplomaTypeAttachmentEdit>
<div class="clr20"><!--  --></div>
<div style="text-align: center;"><a target="_blank"
	href="${pathPrefix }/control/diploma_type_attachment/${fileName }?id=${id }">
<img
	src="${pathPrefix }/control/diploma_type_attachment/view?width=500&id=${id }" />
</a></div>
<%@ include file="/screens/footer.jsp"%>

