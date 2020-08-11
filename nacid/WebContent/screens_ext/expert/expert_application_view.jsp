<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/ext_nacid_taglib.tld" prefix="nacid_ext"%>
<%@ include file="/screens_ext/header.jsp"%>
    <h3 class="title"><span>Заявление</span></h3>
	
	<div class="clr15"><!--  --></div>
	<p class="cc">
		<input class="back" type="button" onclick="document.location.href='${pathPrefix }/control/commission_inquire/view';" value="Назад" />
	</p>
	<div class="clr15"><!--  --></div>
	<nacid_ext:report_expert attachmentPrefix="${pathPrefix }/control/expertreportattachments"/>
	<div class="clr10"><!--  --></div>
	<p class="cc">
		<input class="back" type="button" onclick="document.location.href='${pathPrefix }/control/commission_inquire/view';" value="Назад" />
	</p>
<%@ include file="/screens_ext/footer.jsp"%>