<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<h3 class="title"><span>${operationStringForScreens } прикачен файл</span></h3>
<nacid:diplExamAttachmentEdit>
	<v:form action="${pathPrefix }/control/dipl_exam_attachment/save"
		method="post" name="form_dipl_exam_attachment"
		onsubmit="startProgressUpdater();" enctype="multipart/form-data"
		backurl="${backUrlDiplExam }" addGenerateButton="${id==0}">

		<v:comboBoxValidator input="docTypeId" required="true"/>

		<c:if test="${id!=0 }">
			<div class="clr"><!--  --></div>
			<c:if test="${docflowUrl != null && docflowNum == null && !operationView}">
				<a class="flt_rgt" href="${pathPrefix }/${docflowUrl }" >Генерирай деловоден номер</a>
				<div class="clr"><!--  --></div>
			</c:if>
			<a class="flt_rgt" href="${pathPrefix }/control/dipl_exam_attachment/${fileName }?id=${id }&amp;original=1" 
				target="_blank">Свали прикачения файл</a>
			<div class="clr"><!--  --></div>
			<c:if test="${scannedFileName != null }" >
				<a class="flt_rgt" href="${pathPrefix }/control/dipl_exam_attachment/${scannedFileName }?id=${id }" 
					target="_blank">Свали сканираното изображение</a>
				<div class="clr"><!--  --></div>
			</c:if>
		</c:if>
		
		<nacid:systemmessage />

		<input id="applID" type="hidden" name="applID" value="${applicationId }" />
		<input id="id" type="hidden" name="id" value="${id }" />
		<input id="diplExamId" type="hidden" name="diplExamId"
			value="${diplExamId }" />
		<fieldset><legend>Основни данни</legend>

		<input type="hidden" name="docflowUrl" value="${docflowUrl }" />
		<c:if test="${docflowNum != null }">
			<p><span class="fieldlabel2"><label>Деловоден номер:</label><br /></span> 
				${docflowNum }
			</p>
			<div class="clr"><!--  --></div>
		</c:if>

		<p><span class="fieldlabel2"><label for="docDescr">Описание</label><br />
		</span> <textarea id="docDescr" class="brd w600" rows="3" cols="40"
			name="docDescr">${docDescr }</textarea></p>
		<div class="clr"><!--  --></div>

		<p>
			<span class="fieldlabel2"><label for="docTypeId">Тип документ</label></span> 
			<nacid:combobox id="docTypeId" name="docTypeId" disabled="${docflowNum}"
					attributeName="docTypeCombo" />
		</p>
		<div class="clr"><!--  --></div>


		<p><span class="fieldlabel"><label for="doc_content">Файл</label></span> 
		  <input class="brd w240" name="doc_content" id="doc_content" type="file" value="" />&nbsp;&nbsp;${fileName }
		</p>
		<div class="clr"><!--  --></div>

		<c:if test="${id!=0}">
			<p><span class="fieldlabel"><label for="scanned_content">Сканирано изображение</label><br />
			</span> <input class="brd w240" name="scanned_content" id="scanned_content" type="file" value="" />&nbsp;&nbsp;${scannedFileName }</p>
			<div class="clr"><!--  --></div>
		</c:if>

		</fieldset>
		
		<nacid:eventEditTag />
		
		<div class="clr20"><!--  --></div>

		<nacid:loadingBar />

	</v:form>
</nacid:diplExamAttachmentEdit>
<div class="clr20"><!--  --></div>
<c:if test="${scannedFileName != null }" >
<div style="text-align: center;">
	<a target="_blank" href="${pathPrefix }/control/dipl_exam_attachment/${scannedFileName }?id=${id }">
		<img src="${pathPrefix }/control/dipl_exam_attachment/view?width=500&id=${id }" />
	</a>
</div>
</c:if>
<%@ include file="/screens/footer.jsp"%>

