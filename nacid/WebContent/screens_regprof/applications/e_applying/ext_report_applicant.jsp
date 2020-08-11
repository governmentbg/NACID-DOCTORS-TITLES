<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/ext_regprof_nacid_taglib.tld" prefix="regprof_ext" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="com.nacid.bl.nomenclatures.NomenclaturesDataProvider"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ include file="/screens_regprof/header.jsp"%>
<%@ page import="com.nacid.web.ApplicantTypeHelper" %>

<nacid:systemmessage />

<p class="cc">
	<input class="back" type="button" 
		onclick="document.location.href='${pathPrefix }/control/e_applying/list?getLastTableState=1';" value="Назад" />
</p>

<div class="formClass">
<regprof_ext:regprof_report_applicant_external attributePrefix="ext" attachmentPrefix="${pathPrefix }/control/e_applying_attachments"/>
<c:if test="${!operationView }" >
	<div class="clr20"><!--  --></div>
	<fieldset><legend class="noForm">Електронна поща</legend>
	<nacid:list />
	<script type="text/javascript">
		document.tableForm1.action = '${pathPrefix }/control/e_applying/edit?id=${extApplId}';
		<%--document.tableForm2.action = '${pathPrefix }/control/e_applying/edit?id=${extApplId}';--%>
	</script>
	</fieldset>
	<div class="clr20"><!--  --></div>
</c:if>
</div>
<c:if test="${!operationView }" >	
	<nacid:transferperson extAppId="${extAppId}" applicant="${applicant}" addDocumentData="true" documentData="${documentData }"
						  representative="${representative}"
                          applicantTypeId="0" applicantType="<%=ApplicantTypeHelper.getApplicantTypeName(0)%>"
                          allowApplicantTypeChange="false" showOwnerDetails="false"
                          transferUniversities="false"
            />
</c:if>

<div class="clr20"></div>
<p class="cc">
	<input class="back" type="button" 
		onclick="document.location.href='${pathPrefix }/control/e_applying/list?getLastTableState=1';" value="Назад" />
</p>

<%@ include file="/screens_regprof/footer.jsp"%>
