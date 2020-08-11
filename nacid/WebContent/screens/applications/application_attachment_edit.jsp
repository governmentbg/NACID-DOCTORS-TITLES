<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>


<%@page import="com.nacid.bl.nomenclatures.DocumentType"%><h3 class="title"><span>${operationStringForScreens } прикачен файл</span></h3>

<script type="text/javascript">
	var docFlowDocumentTypes = new Hash();
	docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_CERTIFICATE%>, true);
    docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_CERTIFICATE_DUPLICATE%>, true);
    docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_CERTIFICATE_FACTUAL_ERROR%>, true);

    var certId = <%=DocumentType.DOC_TYPE_CERTIFICATE%>;
    
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
			$('event_div').hide();
		} else {
			$('doc_flow_enter').show();
			$('event_div').show();
		}	
		
	}
	function toggleCertificateNumber(el) {
		if ($('cert_number') == null) {
            return;
        }

		if (appStatusesToAllowCertNumberAssign.get(el.options[el.selectedIndex].value)) {
            $('cert_number').show();
        } else {
            $('cert_number').hide();
        }
	}
</script>
<nacid:applicationAttachmentEdit>
    <script type="text/javascript">
    var appStatusesToAllowCertNumberAssign = new Hash();
	    <c:forEach items="${appStatusesToAllowCertNumberAssign}" var="statId">
	       appStatusesToAllowCertNumberAssign.set(${statId }, true);
	    </c:forEach>
    </script>	
	<v:form action="${pathPrefix }/control/application_attachment/save"
		method="post" name="form_diploma_type_attachment"
		onsubmit="startProgressUpdater();" enctype="multipart/form-data"
		backurl="${backUrlApplAtt }" addGenerateButton="${id==0}" >
		<input type="hidden" id="docflowNum" value="${docflowNum}" />

		<v:comboBoxValidator input="docTypeId" required="true"/>

		<c:if test="${id!=0}">
			<div class="clr"><!--  --></div>
			<c:if test="${docflowUrl != null && docflowNum == null && !operationView}">
			<div id="doc_flow_enter">	
				<a class="flt_rgt" href="${pathPrefix }/${docflowUrl }" >Генерирай деловоден номер</a>
				<div class="clr"><!--  --></div>
			</div>
			</c:if>
			<a class="flt_rgt" href="${pathPrefix }/control/application_attachment/${fileName }?id=${id }&amp;original=1"
				target="_blank">Свали прикачения файл</a> <!-- IS ORIGINAL _ HAS TO BE IN MY CODE TOO -->
			<div class="clr"><!--  --></div>
			<c:if test="${scannedFileName != null }" >
				<a class="flt_rgt" href="${pathPrefix }/control/application_attachment/${scannedFileName }?id=${id }" 
					target="_blank">Свали сканираното изображение</a>
				<div class="clr"><!--  --></div>
			</c:if>
		</c:if>

		<nacid:systemmessage name="attachmentStatusMessage" />
		
		<input id="id" type="hidden" name="id" value="${id }" />
		<input id="applicationId" type="hidden" name="applicationId"
			value="${applicationId }" />
		<fieldset><legend>Основни данни</legend> <v:textValidator
			input="docDescr" required="isNoDocTypeSelected()" />

		<input type="hidden" name="docflowUrl" value="${docflowUrl }" />
		<c:if test="${docflowNum != null }">
			<p><span class="fieldlabel2"><label>Деловоден номер:</label><br /></span> 
				${docflowNum }
			</p>
			<div class="clr"><!--  --></div>
		</c:if>

		<p>
		  <span class="fieldlabel2"><label for="docTypeId">Тип документ</label></span> 
		  <nacid:combobox id="docTypeId" name="docTypeId" disabled="${docflowNum}" attributeName="docTypeCombo" onchange="toggleDocFlow(this);toggleCertificateNumber(this);"/>
		</p>
		<div class="clr"><!--  --></div>
        <c:if test="${not empty appStatusesToAllowCertNumberAssign}" >
            <p id="cert_number" style="display:none;">
                <span class="fieldlabel2"><label for="certNumber">Номер на удостоверение</label></span> 
                <v:textinput  class="brd w500" name="certNumber" id="certNumber" value="" maxlength="22"/>
            </p>
            <div class="clr"><!--  --></div>
            <v:textValidator input="certNumber" required="($('cert_number') != null && $('cert_number').visible())" maxLength="22" />
        </c:if>
		<p><span class="fieldlabel2"><label for="docDescr">Описание</label><br />
		</span> <textarea id="docDescr" class="brd w600" rows="3" cols="40"
			name="docDescr">${docDescr }</textarea></p>
		<div class="clr"><!--  --></div>

		<p><span class="fieldlabel2"><label for="copyTypeId">Форма</label><br />
		</span> <nacid:combobox id="copyTypeId" name="copyTypeId"
			attributeName="copyTypeCombo" /></p>
		<div class="clr"><!--  --></div>


		<p><span class="fieldlabel"><label for="doc_content">Файл</label><br />
		</span> <input class="brd w240" name="doc_content" id="doc_content" type="file" value="" />&nbsp;&nbsp;${fileName }</p>
		<div class="clr"><!--  --></div>
		
		<c:if test="${id!=0}">
			<p><span class="fieldlabel"><label for="scanned_content">Сканирано изображение</label><br />
			</span> <input class="brd w240" name="scanned_content" id="scanned_content" type="file" value="" />&nbsp;&nbsp;${scannedFileName }</p>
			<div class="clr"><!--  --></div>
		</c:if>
		
		</fieldset>
		<div id="event_div">
		<nacid:eventEditTag />
		</div>
		<div class="clr20"><!--  --></div>

		<nacid:loadingBar />

	</v:form>
</nacid:applicationAttachmentEdit>
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
<%@ include file="/screens/footer.jsp"%>

