<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/ext_nacid_taglib.tld" prefix="nacid_ext"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<h3 class="title"><span>${operationStringForScreens } прикачен файл</span></h3>

<script type="text/javascript">
	function isNoDocTypeSelected() {
		var dt = $('docTypeId');
		return dt.options[dt.selectedIndex].value == '-';
	}
</script>

<nacid_ext:extApplicationAttachmentEdit>
	<v:form action="${pathPrefix }/control/application_attachment/save"
		method="post" name="form_diploma_type_attachment"
		onsubmit="startProgressUpdater();" enctype="multipart/form-data"
		backurl="${backUrlApplAtt }" >

		<c:if test="${id!=0}">
			<div class="clr"><!--  --></div>
			<a class="flt_rgt" href="${pathPrefix }/control/application_attachment/${fileName }?id=${id }"
				target="_blank">Свали прикачения файл</a>
			<div class="clr"><!--  --></div>
		</c:if>

		<nacid:systemmessage />
		
		<input id="id" type="hidden" name="id" value="${id }" />
		<input id="applicationId" type="hidden" name="applicationId"
			value="${applicationId }" />
		<fieldset><legend>Основни данни</legend> <v:textValidator
			input="docDescr" required="isNoDocTypeSelected()" />

		<p><span class="fieldlabel2"><label for="docTypeId">Тип
		документ</label><br />
		</span> <nacid:combobox id="docTypeId" name="docTypeId"
			attributeName="docTypeCombo" style="brd"/></p>
		<div class="clr"><!--  --></div>

		<p><span class="fieldlabel2"><label for="docDescr">Описание</label><br />
		</span> <textarea id="docDescr" class="brd w600" rows="3" cols="40"
			name="docDescr">${docDescr }</textarea></p>
		<div class="clr"><!--  --></div>
		<%-- 
		<p><span class="fieldlabel2"><label for="copyTypeId">Форма</label><br />
		</span> <nacid:combobox id="copyTypeId" name="copyTypeId"
			attributeName="copyTypeCombo" /></p>
		<div class="clr"><!--  --></div>
		--%>

		<p><span class="fieldlabel2"><label for="doc_content">Файл</label><br />
		</span> <input class="brd w240" name="doc_content"
			id="doc_content" type="file" value="" />&nbsp;&nbsp;${fileName }</p>
		<div class="clr"><!--  --></div>

		</fieldset>
		<div class="clr20"><!--  --></div>

		<nacid:loadingBar />

	</v:form>
</nacid_ext:extApplicationAttachmentEdit>
<div class="clr20"><!--  --></div>
<div style="text-align: center;"><a target="_blank"
	href="${pathPrefix }/control/application_attachment/${fileName }?id=${id }">
<img
	src="${pathPrefix }/control/application_attachment/view?width=500&id=${id }" />
</a></div>