<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/ext_regprof_nacid_taglib.tld" prefix="regprof_ext"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:set var="pageTitle" value="НАЦИД – Справка за професионални квалификации" scope="request"/>
<%@ include file="/screens_regprof_ext/header_short.jsp"%>
<div class="w1000 noMenuBase">
    <h3 class="title"><span>Справка за професионални квалификации</span></h3>

	<div class="w1000 single_column">
		<c:choose>
			<c:when test="${systemMessage != null }">
				<nacid:systemmessage/>
			</c:when>
			<c:otherwise>
				<p class="cc mt10">
                    <input class="back" type="button" onclick="document.location.href='${pathPrefix}/control/home';" value="НАЗАД"/>
                </p>
				<regprof_ext:regprof_report_applicant_internal attachmentPrefix="${pathPrefix }/control/public_report" hideAttachments="true"
					 showCertificate = "true"/>	
			</c:otherwise>
		</c:choose>
		<div class="clr"><!--  --></div>
    
        <p class="cc mt10">
            <input class="back" type="button" onclick="document.location.href='${pathPrefix}/control/home';" value="НАЗАД"/>
        </p>
	</div>

	
</div>
<!-- end of loginBase --> <!-- ******************************************* end of page content -->

<%@ include file="/screens_regprof_ext/footer_short.jsp"%>