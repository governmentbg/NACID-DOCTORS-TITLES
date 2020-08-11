<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/ext_nacid_taglib.tld" prefix="ext_nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:set var="pageTitle" value="НАЦИД – Публични регистри за академично признаване" scope="request"/>
<%@ include file="/screens_ext/header_short.jsp"%>
<div class="w1000 noMenuBase">
	<p class="cc">
	   НАЦИД организира процедурата по академично признаване от 03 април 2009 г. <br/>
       Настоящият регистър отразява информацията след тази дата.
       <c:if test="${requestScope.isCertificateType}">
       <br />Удостоверенията се издават на имената на лицето, посочени в дипломата.
       </c:if>
    </p>
    <div class="clr10"><!--  --></div>
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
<%@ include file="/screens_ext/footer_short.jsp"%>
