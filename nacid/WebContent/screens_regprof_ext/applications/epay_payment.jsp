<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:if test="${!epaymodel.paid}" >
<script type="text/javascript">
var count = 0;
var paid = false;
var attempts = 100;
function checkPaymentStatus() {

    console.log("insideCheckPaymentStatus...");

	if (count < attempts && !paid) {
		count++;
		new Ajax.Request('${pathPrefix}/control/getepaylastpaymentstatus', {
			  asynchronous : false, 
			  method : "POST", 
			  parameters: { applicationId: $F('applicationId') },
			  onSuccess: function(oXHR) {
				var resp = oXHR.responseText.evalJSON();
				var status = resp.status;
				if (status == 0) { //generated
                    $('paymentStatusMessage').innerHTML = "Очаква отговор от Epay...(" + count + ")";
				} else if (status == 1) {//paid
					paid = true;
					$('paymentStatusMessage').innerHTML = "Плащането в EPAY е извършено!";
					$('paymentStatusMessage').removeClassName("error");
					$('paymentStatusMessage').addClassName("correct");
					$('pmtSt').innerHTML = "Платено";
                    $('info').hide();
					return;
				} else if (status == 2) {//canceled
					$('paymentStatusMessage').innerHTML = "Плащането в EPAY е отказано!";
					$('paymentStatusMessage').removeClassName("correct");
					$('paymentStatusMessage').addClassName("error");
                    $$('.epay').each(function(el) {
                       el.show();
                    });
					return;
				} else {
					alert("Unknown liability status...");
					return;
				}
				
			  },
			  onFailure : function(oXHR) {
				alert("Възникна грешка при опит за проверка на статуса в EPAY..." + oXHR.statusText);
				count = attempts;//за да спре да проверява вече...
			  }
		});
		setTimeout("checkPaymentStatus();",6000);
	} else if (!paid) {
		$('paymentStatusMessage').innerHTML = "Не се получи отговор от epay в рамките на 10 минути. След получаване на отговора, ще бъдете уведомени по email!";
		$('paymentStatusMessage').removeClassName("correct");
		$('paymentStatusMessage').addClassName("error");
	}
	
}
function startCheckPaymentStatus() {
    return true;
}
</script>

</c:if>
<v:form name="appform7" action="${pathPrefix }/control/epayredirect" method="post">
	    <p class="cc">
			<input type="button" value="Назад" onclick="document.location.href='${pathPrefix}/control/applications/list?getLastTableState=1';" class="back" />
            <input type="submit" value="Плати" class="epay" onclick="return startCheckPaymentStatus();" style="display: none;"/>
		</p> 	
	  	<div id="paymentStatusMessage" class="error">&nbsp;</div>
	  	<v:hidden name="activeForm" value="7" />
	  	<v:hidden id="applicationId" name="applicationId" value="${epaymodel.id }" />
		<fieldset><legend>Плащане</legend>
 			<p>
 				<span class="fieldlabel"><label>Идентификационен номер на НАЦИД</label></span>
 				<span>${epaymodel.cin }</span>
 			</p>
 			<div class="clr"><!--  --></div>
 			<p>
 				<span class="fieldlabel"><label>Тип услуга</label></span>
 				<span>${epaymodel.serviceType }</span>
 			</p>
 			<div class="clr"><!--  --></div>
 			<p>
 				<span class="fieldlabel"><label>Сума за плащане</label></span>
 				<span>${epaymodel.amount }</span>
 			</p>
 			<div class="clr"><!--  --></div>
 			<p>
 				<span class="fieldlabel"><label>Статус на плащане</label></span>
 				<span id="pmtSt">${epaymodel.paidText }</span>
 			</p>
 			<c:if test="${!epaymodel.paid}">
 			<p id="info">
 				<span class="fieldlabel"><label>Информация</label></span>
 				<span class="red">При натискане на бутона плати, ще се отвори нов прозорец в който трябва да извършите плащането в epay, след това трябва да refresh-нете текущата страница!</span>
 			</p>
 			</c:if>
       </fieldset>
       <p class="cc">
			<input type="button" value="Назад" onclick="document.location.href='${pathPrefix}/control/applications/list?getLastTableState=1';" class="back" />
            <input type="submit" value="Плати" class="epay" onclick="return startCheckPaymentStatus();" style="display: none;"/>
		</p>
		
</v:form>
<c:if test="${epaymodel.waitingForEpay}">
<script type="text/javascript">
    checkPaymentStatus();
</script>
</c:if>
<c:if test="${!epaymodel.paid && !epaymodel.waitingForEpay}">
<script type="text/javascript">
    $$('.epay').each(function(el) {
        el.show();
    });
</script>
</c:if>