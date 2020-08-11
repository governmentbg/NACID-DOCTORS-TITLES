<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>

<h3 class="title"><span>Меню</span></h3>

<nacid:systemmessage />

<a href="${pathPrefix }/control/administration_menu/new">Създаване
на ново меню</a>
<div class="clr"><!--  --></div>
<nacid:menuEditList />

<%@ include file="/screens/footer.jsp"%>