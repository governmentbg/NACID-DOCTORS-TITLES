<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/ext_nacid_taglib.tld" prefix="nacid_ext" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="com.nacid.bl.nomenclatures.NomenclaturesDataProvider"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ include file="/screens/header.jsp"%>

<nacid:systemmessage />

<p class="cc">
	<input class="back" type="button" 
		onclick="document.location.href='${pathPrefix }/control/e_applying/list?getLastTableState=1&entryNumSeries=${param.entryNumSeries}';" value="Назад" />
</p>

<div class="formClass">
<nacid_ext:report_applicant_external attachmentPrefix="${pathPrefix }/control/e_applying_attachments"/>

<c:if test="${!operationView }" >
	<div class="clr20"><!--  --></div>
	<fieldset><legend class="noForm">Коментари към заявлението</legend>
	<nacid:list />
	<script type="text/javascript">
		document.tableForm1.action = '${pathPrefix }/control/e_applying/edit?id=${extApplId}&entryNumSeries=${param.entryNumSeries}';
	</script>
	</fieldset>
	<div class="clr20"><!--  --></div>
</c:if>
</div>

<c:if test="${!operationView }" >
	<nacid:transferperson extAppId="${extApplId}" applicant="${applicationForReportWebModel.applicant}"
                          representative="${applicationForReportWebModel.differentApplicantRepresentative ? applicationForReportWebModel.representative : null}" documentData="${null }"
                          addDocumentData="false" applicantType="${applicationForReportWebModel.applicantType}" applicantTypeId="${applicationForReportWebModel.applicantTypeId}"
                          allowApplicantTypeChange="${applicationForReportWebModel.sarApplication}" applicantCompany="${applicationForReportWebModel.applicantCompany}" owner="${applicationForReportWebModel.trainingCourseWebModel.owner}"
						  showOwnerDetails="${applicationForReportWebModel.showOwnerDetails}"
                          transferUniversities="true"

            />
</c:if>

<%@ include file="../similar_diplomas.jsp"%>

<div class="clr20"><!--  --></div>
<p class="cc">
	<input class="back" type="button" 
		onclick="document.location.href='${pathPrefix }/control/e_applying/list?getLastTableState=1&entryNumSeries=${param.entryNumSeries}';" value="Назад" />
</p>

<%@ include file="/screens/footer.jsp"%>