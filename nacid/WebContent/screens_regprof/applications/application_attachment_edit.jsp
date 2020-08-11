<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens_regprof/header.jsp"%>
<%@ taglib uri="/META-INF/regprof_nacid_taglib.tld" prefix="nacid_regprof"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>


<%@page import="com.nacid.bl.nomenclatures.DocumentType"%><h3 class="title"><span>${operationStringForScreens } прикачен файл</span></h3>

<script type="text/javascript">
	var docFlowDocumentTypes = new Hash();
	docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_CPO%>, true);
	docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_HIGHER_AND_SECONDARY%>, true);
	docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_SDK%>, true);
	docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_STAJ%>, true);
	docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_CPO%>, true);
	docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_HIGHER_AND_SECONDARY%>, true);
	docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_SDK%>, true);
	docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_STAJ%>, true);
	docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_1%>, true);
	docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_2%>, true);
	docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_3%>, true);

    //var certId = <%=DocumentType.DOC_TYPE_CERTIFICATE%>;
    
    function isNoDocTypeSelected() {
		var dt = $('docTypeId');
		return dt.options[dt.selectedIndex].value == '-';
	}
    
	function toggleDocFlow(el) {
	    if (!$('doc_flow_enter')) {
		    return;
	    }
		var id = el.options[el.selectedIndex].value;
		if (docFlowDocumentTypes.get(id)) {
			$('doc_flow_enter').hide();
			//$('event_div').hide();
		} else {
			$('doc_flow_enter').show();
			//$('event_div').show();
		}
	}

</script>
<nacid_regprof:regprofApplicationAttachmentEdit>
	<v:form action="${pathPrefix }/control/application_attachment/save"
		method="post" name="form_diploma_type_attachment"
		onsubmit="startProgressUpdater();" enctype="multipart/form-data"
		backurl="${backUrlApplAtt }" addGenerateButton="${id==0}" >
		
		<v:hidden name="certNumber" value="${certNumber}" />

		<v:comboBoxValidator input="docTypeId" required="true"/>

		<c:if test="${id!=0}">
			<div class="clr"><!--  --></div>

			<c:if test="${docflowUrl != null && (docflowNum == null || docflowNum == '')}">
			<div id="doc_flow_enter">	
				<a class="flt_rgt" href="${pathPrefix }/${docflowUrl }" onclick="return docflowConfirmation()">Генерирай деловоден номер</a>
				<div class="clr"><!--  --></div>
			</div>
			</c:if>
			<a class="flt_rgt" href="${pathPrefix }/control/application_attachment/${fileName }?id=${id }&amp;original=1" target="_blank">Свали прикачения файл</a>
			<div class="clr"><!--  --></div>
			<c:if test="${scannedFileName != null }" >
				<a class="flt_rgt" href="${pathPrefix }/control/application_attachment/${scannedFileName }?id=${id }" 
					target="_blank">Свали сканираното изображение</a>
				<div class="clr"><!--  --></div>
			</c:if>
		</c:if>

		<nacid:systemmessage />
		
		<input id="id" type="hidden" name="id" value="${id }" />
		<input id="applicationId" type="hidden" name="applicationId" value="${applicationId }" />
		<fieldset><legend>Основни данни</legend> <v:textValidator
			input="docDescr" required="isNoDocTypeSelected()" />

		<input type="hidden" name="docflowUrl" value="${docflowUrl }" />
		<c:if test='${docflowNum != null && docflowNum != "" }'>
			<p><span class="fieldlabel2"><label>Деловоден номер:</label><br /></span> 
				${docflowNum }
			</p>
			<div class="clr"><!--  --></div>
		</c:if>

		<p>
		  <span class="fieldlabel2"><label for="docTypeId">Тип документ</label></span>
		  <nacid:combobox id="docTypeId" name="docTypeId" style="brd w600" disabled="${docflowNum}" attributeName="docTypeCombo" onchange="toggleDocFlow(this);"/>
		</p>
		<div class="clr"><!--  --></div>

		<p><span class="fieldlabel2"><label for="docDescr">Описание</label><br />
		</span> <textarea id="docDescr" class="brd w600" rows="3" cols="40"
			name="docDescr">${docDescr }</textarea></p>
		<div class="clr"><!--  --></div>

		<p><span class="fieldlabel2"><label for="doc_content">Файл</label><br />
		</span> <input class="brd w240" name="doc_content" id="doc_content" type="file" value="" />&nbsp;&nbsp;${fileName }</p>
		<div class="clr"><!--  --></div>
		
		<c:if test="${id!=0}">
			<p><span class="fieldlabel2"><label for="scanned_content">Сканирано изображение</label><br />
			</span> <input class="brd w240" name="scanned_content" id="scanned_content" type="file" value="" />&nbsp;&nbsp;${scannedFileName }</p>
			<div class="clr"><!--  --></div>
		</c:if>
		
		</fieldset>

		<div class="clr20"><!--  --></div>

		<nacid:loadingBar />

	</v:form>
</nacid_regprof:regprofApplicationAttachmentEdit>
<div class="clr20"><!--  --></div>
<c:if test="${scannedFileName != null }" >
<div style="text-align: center;"><a target="_blank"
	href="${pathPrefix }/control/application_attachment/${scannedFileName }?id=${id }">
<img
	src="${pathPrefix }/control/application_attachment/view?width=500&id=${id }" />
</a></div>
</c:if>
<script type="text/javascript">
    toggleDocFlow($('docTypeId'));
</script>
<%@ include file="/screens_regprof/footer.jsp"%>