<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens_ext/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<h3 class="title"><span>${operationStringForScreens } становище</span></h3>
<nacid_ext:expertStatementEdit>
	<v:form action="${pathPrefix }/control/expert_statement/save"
		method="post" name="form_dipl_exam_attachment"
		onsubmit="startProgressUpdater();" enctype="multipart/form-data"
		backurl="${pathPrefix }/control/expert_application/edit?id=${applicationId }&activeForm=2" addGenerateButton="${id == 0}">

		<c:if test="${id!=0 }">
			<div class="clr"><!--  --></div>
			<a class="flt_rgt" href="${pathPrefix }/control/expert_statement/${fileName }?id=${id }" 
				target="_blank">Свали прикачения файл</a>
			<div class="clr"><!--  --></div>
		</c:if>
		
		<nacid:systemmessage />

		<input id="applID" type="hidden" name="applId" value="${applicationId }" />
		<input id="id" type="hidden" name="id" value="${id }" />
		<fieldset><legend>Основни данни</legend>


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
		</span> <input class="brd w240" name="doc_content"
			id="doc_content" type="file" value="" />&nbsp;&nbsp;${fileName }</p>
		<div class="clr"><!--  --></div>

		</fieldset>

		<nacid:loadingBar />

	</v:form>
</nacid_ext:expertStatementEdit>
<div class="clr20"><!--  --></div>
<div style="text-align: center;">
	<a target="_blank" href="${pathPrefix }/control/expert_statement/${fileName }?id=${id }">
		<img src="${pathPrefix }/control/expert_statement/view?width=500&id=${id }" />
	</a>
</div>
<%@ include file="/screens_ext/footer.jsp"%>

