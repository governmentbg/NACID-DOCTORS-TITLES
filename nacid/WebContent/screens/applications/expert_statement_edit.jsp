<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<h3 class="title"><span>${operationStringForScreens } становище</span></h3>

	<v:form action="${pathPrefix }/control/expert_statement_attachment/save"
		method="post" name="form_dipl_exam_attachment"
		onsubmit="startProgressUpdater();" enctype="multipart/form-data"
		backurl="${pathPrefix }/control/applications/edit?id=${expertStatementWebModel.applicationId }&activeForm=5"
        addGenerateButton="${expertStatementWebModel.id == 0}">
        <v:comboBoxValidator input="expertId" required="true" />
        <v:comboBoxValidator input="docTypeId" required="true" />
		<c:if test="${expertStatementWebModel.id!=0 }">
			<div class="clr"><!--  --></div>
			<a class="flt_rgt" href="${pathPrefix }/control/expert_statement_attachment/${expertStatementWebModel.fileName }?id=${expertStatementWebModel.id }" 
				target="_blank">Свали прикачения файл</a>
			<div class="clr"><!--  --></div>
		</c:if>
		
		<nacid:systemmessage />

		<input id="applID" type="hidden" name="applId" value="${expertStatementWebModel.applicationId }" />
		<input id="id" type="hidden" name="id" value="${expertStatementWebModel.id }" />
		<fieldset><legend>Основни данни</legend>


		<p><span class="fieldlabel2"><label for="docDescr">Описание</label><br />
		</span> <textarea id="docDescr" class="brd w600" rows="3" cols="40"
			name="docDescr">${expertStatementWebModel.docDescr }</textarea></p>
		<div class="clr"><!--  --></div>

		<p>
			<span class="fieldlabel2"><label for="docTypeId">Тип документ</label></span> 
			<nacid:combobox id="docTypeId" name="docTypeId" attributeName="docTypeCombo" />
		</p>
		<div class="clr"><!--  --></div>
        
        <p>
            <span class="fieldlabel2"><label for="expertId">Експерт</label></span> 
            <nacid:combobox id="expertId" name="expertId" attributeName="applicationExpertsCombo" />
        </p>
        <div class="clr"><!--  --></div>

        <p>
            <span class="fieldlabel2"><label for="universityDetailId">Университет</label></span>
            <nacid:combobox id="universityDetailId" name="universityDetailId" attributeName="universityDetailsCombo" />
        </p>
        <div class="clr"><!--  --></div>


		<p><span class="fieldlabel"><label for="doc_content">Файл</label><br />
		</span> <input class="brd w240" name="doc_content"
			id="doc_content" type="file" value="" />&nbsp;&nbsp;${expertStatementWebModel.fileName }</p>
		<div class="clr"><!--  --></div>

		</fieldset>

		<nacid:loadingBar />

	</v:form>

<div class="clr20"><!--  --></div>
<div style="text-align: center;">
	<a target="_blank" href="${pathPrefix }/control/expert_statement_attachment/${expertStatementWebModel.fileName }?id=${expertStatementWebModel.id }">
		<img src="${pathPrefix }/control/expert_statement_attachment/view?width=500&id=${expertStatementWebModel.id }" />
	</a>
</div>
<%@ include file="/screens/footer.jsp"%>

