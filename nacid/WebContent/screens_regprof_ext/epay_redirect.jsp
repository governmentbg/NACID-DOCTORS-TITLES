<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form id="myform" action="${epayRedirectWebModel.paymentUrl}" method="POST">
    <input type=hidden name="PAGE" value="paylogin" />
    <input type=hidden name="ENCODED" value="${epayRedirectWebModel.encoded}">
    <input type=hidden name="CHECKSUM" value="${epayRedirectWebModel.checksum}">
    <input type=hidden name="URL_OK" value="${epayRedirectWebModel.urlOk}">
    <input type=hidden name="URL_CANCEL" value="${epayRedirectWebModel.urlCancel}">
</form>

<script type="text/javascript">
    window.onload=function() {
        document.getElementById("myform").submit();
    }
</script>
