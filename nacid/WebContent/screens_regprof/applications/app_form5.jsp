<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/regprof_nacid_taglib.tld" prefix="nacid_regprof"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@page import="com.nacid.bl.nomenclatures.DocumentType"%>
<%@ page import="com.nacid.regprof.web.handlers.impl.applications.RegprofApplicationHandler" %>
<%@ page import="com.nacid.bl.applications.regprof.RegprofApplication" %>

<h3 class="title"><span>${suggestionOperation} ${fn:toLowerCase(suggestion)}</span></h3>
<h3 class="names">${application_header }</h3>
<div class="clr15"><!--  --></div>

<script type="text/javascript">

	var docFlowDocumentTypes = new Hash(); // po-skoro sa id-ta na dokumenti, koito ne mogat da se zavedat v delovodnata sistema
    docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_STAJ%>, true);
    docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_CPO%>, true);
    docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_HIGHER_AND_SECONDARY%>, true);
    docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_SDK%>, true);
    docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_1%>, true);
    docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_2%>, true);
    docFlowDocumentTypes.set(<%=DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_3%>, true);

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
			//$('docflow_num').hide();
		} else {
			$('doc_flow_enter').show();
			//$('docflow_num').show();
		}
	}
    function duplicate(duplicateType) {
        $('duplicate_type').value = duplicateType;
        $('statusForm').submit();
    }
</script>

<nacid:systemmessage name="finalizationMessage"/>

<c:if test="${not empty showDocuments }">
	<p class="cc">
		<input class="back" type="button"
			onclick="document.location.href='${pathPrefix }/control/${back_screen}/list?getLastTableState=0&appNumber=${sessionScope.app_number_filter_value }';"
			value="Назад" />
	</p>
</c:if>

<fieldset style="width: 870px; margin-left: 20px;"><legend class="noForm">Статус на заявлението</legend>
<v:form name="statusForm" skipsubmitbuttons="true" action="${pathPrefix }/control/regprofapplication/save?appId=${id}&activeForm=5" method="post" 
				additionalvalidation="validateArchiveNumber($('docflowStatusId'), $('archive_number_2'))">
            <input type="hidden" name="duplicate_type" id="duplicate_type" value=""/>
			<div class="clr5"><!--  --></div>
			<div>
				<span class="fieldlabel2"><label for="applicationStatusId">Статус</label></span> 
				<nacid:combobox id="applicationStatusId" name="applicationStatusId" attributeName="finalizationStatusCombo" style="brd w308" /><br />
                <span class="fieldlabel2"><label for="docflowStatusId">Деловоден статус</label></span>
                <nacid:combobox id="docflowStatusId" name="docflowStatusId" attributeName="finalizationDocflowStatusCombo" style="brd w308"
                                onchange="toggleArchiveNumber(this, $('archive_number_container_2'), $('archive_number_2'));" />
				<div class="clr"><!--  --></div>
     			<div id="archive_number_container_2">
      				<span class="fieldlabel2"><label for="archive_number_2">Номенклатурен номер :</label><br /></span>
					<v:textinput class="brd w200" maxlength="50" name="archive_number" id="archive_number_2"  value="${archiveNumber }" />
	  			</div>
			</div>
			<v:comboBoxValidator input="applicationStatusId" required="true"/>
			<div class="clr20"><!--  --></div>
			<v:submit value="Промени статус"/>
            <c:if test="${showDuplicateButtons}">
                <input type="button" value="Дубликат" name="sbmt" class="docs" style="width:130px" onclick="if (confirm('Сигурни ли сте, че желаете да създадете дубликат на заявлението?')) duplicate('duplicate');">
                <input type="button" value="Очевидна фактическа грешка" name="sbmt" class="docs" style="width:290px" onclick="if (confirm('Сигурни ли сте, че желаете да промените данните на заявлението поради очевидна фактическа грешка?')) duplicate('obvious_factual_error');">
            </c:if>
			<div class="clr20"><!--  --></div>
			</v:form>
			<v:form name="imiCorrespondenceForm" skipsubmitbuttons="true" action="${pathPrefix }/control/regprofapplication/save?appId=${id}&activeForm=5"
			 method="post">
			 	<p><span class="fieldlabel2"><label for="imiCorrespondence">Свързана преписка по IMI</label><br/></span>
        			<v:textinput id="imiCorrespondence" name="imiCorrespondence" value="${imiCorrespondence }" class="brd w300"/>
        		</p>
        		<v:textValidator input="imiCorrespondence" required="true" minLength="2" maxLength="100"/>
        		<div class="clr20"><!--  --></div>
				<v:submit value="Промени преписка"/>
				<div class="clr5"><!--  --></div>
			</v:form>
		</fieldset>

		<div class="clr5"><!--  --></div>

<c:if test="${empty showDocuments }">

	<input type="hidden" id="docflowNum" value="${docflowNum}" />
	<input type="hidden" id="docflowNum2" value="${docflowNum2}" />


<nacid_regprof:regprofApplicationAttachmentEdit>

	<v:form action="${pathPrefix }/control/regprofapplication/save?appId=${applicationId}&activeForm=5"
		method="post" name="appform5" skipSubmit="${not empty disableGeneration }"
		onsubmit="startProgressUpdater();" enctype="multipart/form-data"
		backurl="${pathPrefix }/control/${back_screen}/list?getLastTableState=0&appNumber=${sessionScope.app_number_filter_value }" 
		addGenerateButton="${(id == 0 || (id != 0 && id2 == 0)) && empty disableGeneration}" >

		<input id="id" type="hidden" name="id" value="${id }" />
		
		<fieldset id="suggestion_fieldset" ${disableGeneration}><legend>${suggestion}</legend>

		<c:choose>
			<c:when test="${id != 0}">
				<div class="clr"><!--  --></div>
				<c:if test="${docflowUrl != null && (docflowNum == null || docflowNum == '')}">
					<div id="doc_flow_enter">	
						<a class="flt_rgt" href="${pathPrefix }/${docflowUrl }" onclick="return docflowConfirmation()">Генерирай деловоден номер</a>
						<div class="clr"><!--  --></div>
					</div>
				</c:if>
				<a class="flt_rgt" href="${pathPrefix }/control/application_finalization/${fileName }?id=${id }&amp;original=1" target="_blank">Свали прикачения файл</a>
				<div class="clr"><!--  --></div>
				<c:if test="${scannedFileName != null }" >
					<a class="flt_rgt" href="${pathPrefix }/control/application_finalization/${scannedFileName }?id=${id }" 
						target="_blank">Свали сканираното изображение</a>
					<div class="clr"><!--  --></div>
				</c:if>
			</c:when>
		</c:choose>

		<v:textValidator input="docDescr" required="isNoDocTypeSelected()" />

		<input type="hidden" name="docflowUrl" value="${docflowUrl }" />
		<c:if test="${docflowNum != null && docflowNum != ''}">
			<p id="docflow_num"><span class="fieldlabel2"><label>Деловоден номер:</label><br /></span> 
				${docflowNum }
			</p>
			<div class="clr"><!--  --></div>
		</c:if>

		<p>
		  <span class="fieldlabel2"><label for="docTypeId">Тип документ</label></span> 
		  <nacid:combobox id="docTypeId" name="docTypeId" style="brd w450" disabled="${docflowNum}" attributeName="docTypeCombo" onchange="toggleDocFlow(this);"/>
		  <v:comboBoxValidator input="docTypeId" required="true"/>
		</p>
		<div class="clr"><!--  --></div>
        <%-- <c:if test="${not empty appStatusesToAllowCertNumberAssign}" >
            <p id="cert_number" style="display:none;">
                <span class="fieldlabel2"><label for="certNumber">Номер на удостоверение</label></span> 
                <v:textinput  class="brd w500" name="certNumber" id="certNumber" value="" maxlength="22"/>
            </p>
            <div class="clr"><!--  --></div>
            <v:textValidator input="certNumber" required="($('cert_number') != null && $('cert_number').visible())" maxLength="22" />
        </c:if>--%>	
		<p><span class="fieldlabel2"><label for="docDescr">Описание</label><br />
		</span> <textarea id="docDescr" class="brd w600" rows="3" cols="40"
			name="docDescr">${docDescr }</textarea></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="doc_content">Файл</label><br />
		</span> <input class="brd w240" name="doc_content" id="doc_content" type="file" value="" />&nbsp;&nbsp;${fileName }</p>
		<div class="clr"><!--  --></div>

		<c:if test="${id!=0}">
			<p><span class="fieldlabel2"><label for="scanned_content">Сканирано изображение</label><br /></span>
			<input class="brd w240" name="scanned_content" id="scanned_content" type="file" value="" />&nbsp;&nbsp;${scannedFileName }</p>
			<div class="clr"><!--  --></div>
		</c:if>

		</fieldset>

		<c:if test="${id != 0 && enableGeneration}">
			<div class="clr5"><!--  --></div>
			<fieldset ${disableGeneration}><legend>${documentTypeName}</legend>

				<c:if test="${id2 != 0}">
					<div class="clr"><!--  --></div>

					<c:if test="${docflowUrl2 != null && (docflowNum2 == null || docflowNum2 == '')}">
						<div id="doc_flow_enter2">	
							<a class="flt_rgt" href="${pathPrefix }/${docflowUrl2 }" onclick="return docflowConfirmation()">Генерирай деловоден номер</a>
							<div class="clr"><!--  --></div>
						</div>
					</c:if>
					<a class="flt_rgt" href="${pathPrefix }/control/application_finalization/${fileName2 }?id=${id2 }&amp;original=1" target="_blank">Свали прикачения файл</a>
					<div class="clr"><!--  --></div>
					<c:if test="${scannedFileName2 != null }" >				
						<a class="flt_rgt" href="${pathPrefix }/control/application_finalization/${scannedFileName2 }?id=${id2 }" target="_blank">Свали сканираното изображение</a>
						<div class="clr"><!--  --></div>
					</c:if>
					<div class="clr2"><!--  --></div>
				</c:if>

				<c:if test='${docflowNum2 != null && docflowNum2 != "" }'>
					<p><span class="fieldlabel2"><label>Деловоден номер:</label><br /></span> 
						${docflowNum2 }
					</p>
					<div class="clr"><!--  --></div>
				</c:if>
				<p><span class="fieldlabel2"><label for="docDescr">Описание</label><br /></span>
				<textarea id="docDescr2" class="brd w600" rows="3" cols="40" name="docDescr2" >${docDescr2}</textarea></p>
				<div class="clr"><!--  --></div>
				<p><span class="fieldlabel2"><label for="doc_content">Файл</label><br /></span>
				<input class="brd w240" id="doc_content2" name="doc_content2" type="file" value="" />&nbsp;&nbsp;${fileName2 }</p>
				<div class="clr"><!--  --></div>
				<c:if test="${id2 != 0}">
					<p><span class="fieldlabel2"><label for="scanned_content">Сканирано изображение</label><br /></span>
					<input class="brd w240" id="scanned_content2" name="scanned_content2" type="file" value="" />&nbsp;&nbsp;${scannedFileName2 }</p>
					<div class="clr"><!--  --></div>
				</c:if>
				<v:hidden id="id2" name="id2" value="${id2 }" />
				<v:hidden id="docTypeId2" name="docTypeId2" value="${docTypeId2}" />
				<v:hidden id="certNumber" name="certNumber" value="${certNumber}" />
			</fieldset>
		</c:if>
		


		<nacid:loadingBar />

	</v:form>
</nacid_regprof:regprofApplicationAttachmentEdit>

<div class="clr20"><!--  --></div>

<script type="text/javascript">
    toggleDocFlow($('docTypeId'));
    function toggleForm() {
    	if ($('toggle_checkbox').checked) {
    		$('id').disable();
    		$('docTypeId').disable();
    		$('docDescr').disable();
    		$('doc_content').disable();
    		$('scanned_content').disable();
    		$('id2').enable();
    		$('docTypeId2').enable()
    		$('docDescr2').enable();
    		if ($('docflowNum2').value.empty()) {
    			$('doc_content2').enable();
    		}
    		if ($('scanned_content2')) $('scanned_content2').enable();
    		
    	} else {
    		if ($('docflowNum').value.empty()) {
    			$('docTypeId').enable();
    		}
    		$('id').enable();
    		$('docDescr').enable();
    		$('doc_content').enable();
    		$('scanned_content').enable();
    		$('id2').disable();
    		$('docTypeId2').disable();
    		$('docDescr2').disable();
    		$('doc_content2').disable();
    		if ($('scanned_content2')) $('scanned_content2').disable();
    	}
    }

   	window.onload = function() {
   		if (window.location.pathname.lastIndexOf('view') != -1) { // view mode
			if ($('doc_flow_enter')) $('doc_flow_enter').hide();
			if ($('doc_flow_enter2')) $('doc_flow_enter2').hide();
			$$('.generate').each(function (element) {
				element.hide();
			});
   		}
   	}
</script>
</c:if>
<c:if test="${not empty showDocuments }">
	<div class="clr10"><!--  --></div>
	<p class="cc">
		<input class="back" type="button"
			onclick="document.location.href='${pathPrefix }/control/${back_screen}/list?getLastTableState=0&appNumber=${sessionScope.app_number_filter_value }';"
			value="Назад" />
	</p>
</c:if>
<script type="text/javascript">
	toggleArchiveNumber($('applicationStatusId'), $('archive_number_container_2'), $('archive_number_2'));
</script>
<c:if test="${showApostilleTransferButton || showApostilleTransferredMessage}">
	<div class="clr20"><!--  --></div>
	<form method="post" action="${pathPrefix }/control/regprof_apostille_service/save" name="apostilleServiceForm" id="apostilleServiceForm">
		<fieldset><legend>Прехвърляне в Регистър Апостили</legend>
			<c:if test="${showApostilleTransferredMessage}">
				Заявлението вече е прехвърлено в Регистър Апостили!
			</c:if>
			<c:if test="${showApostilleTransferButton}">
				<input type="hidden" name="application_id" value="${application.id}"/>
				<input type="button" value="Прехвърляне" name="sbmt" class="save" style="width:150px" onclick="if (confirm('Сигурни ли сте че желаете да прехвърлите заявлнието в регистър апостили ?')) {$('apostilleServiceForm').submit();}">
			</c:if>
		</fieldset>
	</form>
</c:if>