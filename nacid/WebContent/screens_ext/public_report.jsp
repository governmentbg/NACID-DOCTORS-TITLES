<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/ext_nacid_taglib.tld" prefix="ext_nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:set var="pageTitle" value="НАЦИД – Справка за академично признаване" scope="request"/>
<%@ include file="/screens_ext/header_short.jsp"%>
<div class="w1000 noMenuBase">
    <h3 class="title"><span>Справка за академично признаване</span></h3>

	<div class="w1000 single_column">
		<c:choose>
			<c:when test="${systemMessage != null }">
				<nacid:systemmessage/>
			</c:when>
			<c:otherwise>
				<p class="cc mt10">
                    <input class="back" type="button" onclick="document.location.href='${pathPrefix}/control/home';" value="НАЗАД"/>
                </p>
				<ext_nacid:report_expert attachmentPrefix="${pathPrefix }/control/public_report" hideAttachments="true" hideStatusHistory="true" hideExaminations="true" showCertificate="true" isApplicantViewer="true" />	
			</c:otherwise>
		</c:choose>
		<div class="clr"><!--  --></div>
    
        <p class="cc mt10">
            <input class="back" type="button" onclick="document.location.href='${pathPrefix}/control/home';" value="НАЗАД"/>
        </p>
	</div>

	
</div>
<!-- end of loginBase --> <!-- ******************************************* end of page content -->

<%@ include file="/screens_ext/footer_short.jsp"%>