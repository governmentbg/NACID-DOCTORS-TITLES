<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="validation"%>
<h3 class="title"><span>${operationStringForScreens } начин на получаване на уведомление</span></h3>

<validation:form action="${pathPrefix }/control/nom_document_receive_method/save" method="post" name="documentReceiveMethodForm"
				 backurl="${pathPrefix }/control/nom_document_receive_method/list?getLastTableState=1">
	<input type="hidden" name="id" value="${flatNomenclature.id }" />
	<fieldset><legend>Данни за начин на получаване на уведомление</legend> <nacid:systemmessage />
		<p><span class="fieldlabel"><label for="name">Наименование</label><br />
	</span> <validation:textinput class="brd w600" name="name"  value="${flatNomenclature.name }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel"><label for="has_document_recipient">Има получател на административния акт</label><br />
	</span> <validation:checkboxinput class="brd w20"  name="has_document_recipient" id="has_document_recipient" value="1" checked="${flatNomenclature.hasDocumentRecipient }" /></p>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="has_document_recipient">Електронни услуги - трябва да се прикачи документ за платени такси за куриер</label></span>
			<validation:checkboxinput class="brd w20"  name="eservices_require_payment_receipt" id="eservices_require_payment_receipt" value="1" checked="${flatNomenclature.eservicesRequirePaymentReceipt }" />
		</p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel"><label for="dateFrom">от дата</label><br />
	</span> <validation:dateinput name="dateFrom" id="dateFrom" value="${flatNomenclature.dateFrom }" style="brd w200" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel"><label for="dateTo">до дата</label></span>
			<validation:dateinput name="dateTo" id="dateTo" value="${flatNomenclature.dateTo }" style="brd w200" /></p>
		<div class="clr"><!--  --></div>
	</fieldset>
	<validation:dateValidator input="dateFrom" format="d.m.yyyy" required="false" beforeToday="true" />
	<validation:dateValidator input="dateTo" format="d.m.yyyy" required="false" beforeToday="true" />
	<validation:dateIntervalValidator dateFrom="dateFrom" dateTo="dateTo" format="d.m.yyyy" />

</validation:form>

<%@ include file="/screens/footer.jsp"%>
