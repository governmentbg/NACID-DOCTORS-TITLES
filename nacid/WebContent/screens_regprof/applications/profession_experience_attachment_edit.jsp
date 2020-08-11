<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens_regprof/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/regprof_nacid_taglib.tld" prefix="regprof"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@page import="com.nacid.bl.nomenclatures.DocumentType"%>

<h3 class="title"><span>${operationStringForScreens } прикачен файл</span></h3>
<nacid_regprof:regprofApplicationAttachmentEdit>
	<v:form action="${pathPrefix }/control/profession_experience_attachment/save"
		method="post" name="profession_experience_exam_attachment"
		onsubmit="startProgressUpdater();" enctype="multipart/form-data"
		backurl="${backUrlDocExam }#profession_experience_examination_fieldset" addGenerateButton="${id == 0}"> <!-- temporary solution -->

		<v:comboBoxValidator input="docTypeId" required="true"/>

		<c:if test="${id!=0 }">
			<div class="clr"><!--  --></div>
			<c:if test="${docflowUrl != null && (docflowNum == null || docflowNum == '')}">
				<a class="flt_rgt" href="${pathPrefix }/${docflowUrl }" onclick="return docflowConfirmation()">Генерирай деловоден номер</a>
				<div class="clr"><!--  --></div>
			</c:if>
			<a class="flt_rgt" href="${pathPrefix }/control/profession_experience_attachment/${fileName }?id=${id }&amp;original=1" 
				target="_blank">Свали прикачения файл</a>
			<div class="clr"><!--  --></div>
			<c:if test="${scannedFileName != null }" >
				<a class="flt_rgt" href="${pathPrefix }/control/profession_experience_attachment/${scannedFileName }?id=${id }" 
					target="_blank">Свали сканираното изображение</a>
				<div class="clr"><!--  --></div>
			</c:if>
		</c:if>
		
		<nacid:systemmessage />

		<input id="appId" type="hidden" name="appId" value="${applicationId }" />
		<input id="id" type="hidden" name="id" value="${id }" />
		<input id="docExamId" type="hidden" name="docExamId"
			value="${docExamId }" />
		<fieldset><legend>Основни данни</legend>

		<input type="hidden" name="docflowUrl" value="${docflowUrl }" />
		<c:if test="${docflowNum != null && docflowNum != ''}">
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
			<span class="fieldlabel2"><label for="docTypeId">Към документ</label></span> 
			<nacid:combobox id="professionExperienceDocumentId" name="professionExperienceDocumentId" disabled="${docflowNum}"
					attributeName="experienceDocumentCombo" style="brd w600"/>
			<v:comboBoxValidator input="professionExperienceDocumentId" required="true"/>
		</p>
		<div class="clr"><!--  --></div>

		<p>
			<span class="fieldlabel2"><label for="docTypeId">Тип документ</label></span> 
			<nacid:combobox id="docTypeId" name="docTypeId" disabled="${docflowNum}" style="brd"
					attributeName="docTypeCombo" />
		</p>
		<div class="clr"><!--  --></div>

		<p><span class="fieldlabel2"><label for="doc_content">Файл</label></span> 
		  <input class="brd w240" name="doc_content" id="doc_content" type="file" value="" />&nbsp;&nbsp;${fileName }
		</p>
		<div class="clr"><!--  --></div>

		<c:if test="${id!=0}">
			<p><span class="fieldlabel2"><label for="scanned_content">Сканирано изображение</label><br />
			</span> <input class="brd w240" name="scanned_content" id="scanned_content" type="file" value="" />&nbsp;&nbsp;${scannedFileName }</p>
			<div class="clr"><!--  --></div>
		</c:if>

		</fieldset>
		
		<%--<nacid:eventEditTag />--%>
		
		<div class="clr20"><!--  --></div>

		<nacid:loadingBar />

	</v:form>
</nacid_regprof:regprofApplicationAttachmentEdit>
<div class="clr20"><!--  --></div>
<c:if test="${scannedFileName != null }" >
<div style="text-align: center;">
	<a target="_blank" href="${pathPrefix }/control/profession_experience_attachment/${scannedFileName }?id=${id }">
		<img src="${pathPrefix }/control/profession_experience_attachment/view?width=500&id=${id }" />
	</a>
</div>
</c:if>
<%@ include file="/screens_regprof/footer.jsp"%>