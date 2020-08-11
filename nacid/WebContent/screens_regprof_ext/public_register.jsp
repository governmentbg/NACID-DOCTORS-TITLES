<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/ext_nacid_taglib.tld" prefix="ext_nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:set var="pageTitle" value="НАЦИД – Публични регистри - професионални квалификации" scope="request"/>
<%@ include file="/screens_regprof_ext/header_short.jsp"%>
<div class="w1000 noMenuBase">
	<p class="cc">
	   НАЦИД организира процедурата по издаване на удостоверения за придобита професионална квалификация от 05 април 2011 г. <br/>
       Настоящият регистър отразява информацията след тази дата.
       <c:if test="${isCertificateType}">
       <br />Удостоверенията се издават на имената на лицето, посочени в предоставения за професионална квалификация документ.
       </c:if>
    </p>
    <div class="clr10"><!--  --></div>
    <hr class="none" />
    <h1 class="off_jws">${registerName }</h1>
    <div class="clr"><!--  --></div>
	<h3 class="title"><span>${registerName }</span></h3>
	<div class="clr"><!--  --></div>
	<p class="cc">
		<input class="back" type="button" onclick="document.location.href='${pathPrefix}/control/home';" value="НАЗАД" title="Назад"/>
	</p>
	<div class="w1000 single_column">
		<nacid:list />
	</div>

	<div class="clr"><!--  --></div>
	
	<p class="cc">
		<input class="back" type="button" onclick="document.location.href='${pathPrefix}/control/home';" value="НАЗАД" title="Назад"/>
	</p>
</div>
<%@ include file="/screens_regprof_ext/footer_short.jsp"%>
