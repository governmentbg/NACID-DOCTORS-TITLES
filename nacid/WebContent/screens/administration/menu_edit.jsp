<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<script type="text/javascript">

	function deleteMenu(id) {
		if (!confirm('Сигурни ли сте да че желаете да изтриете менюто?')) {
			return;
		}
		else {
			document.location.href='${pathPrefix }/control/administration_menu/delete?id=' + id;
		}
	}
</script>

<h3 class="title"><span>${operationStringForScreens } меню</span></h3>
<nacid:menuEditEdit>
	<v:form action="${pathPrefix }/control/administration_menu/save"
		method="post" name="form_menu"
		backurl="${pathPrefix }/control/administration_menu/list">

		<nacid:systemmessage />

		<input id="id" type="hidden" name="id" value="${id }" />

		<c:if test="${showDelete }">
			<a href="#" onclick="deleteMenu(${id});">Изтриване на записа</a>
		</c:if>


		<fieldset><legend>Основни данни</legend> <v:textValidator
			input="name" required="true" maxLength="30" /> <v:textValidator
			input="ordNum" required="true" regex="/^\d+$/" /> <v:textValidator
			input="longName" maxLength="100" />

		<p><span class="fieldlabel2"><label for="name">Име</label><br />
		</span> <v:textinput class="brd w200" name="name"  value="${name }" />
		</p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="longName">Разширено
		име</label><br />
		</span> <v:textinput class="brd w500" name="longName" 
			value="${longName }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="ordNum">Позиция</label><br />
		</span> <v:textinput class="brd w50" name="ordNum"  value="${ordNum }" />
		</p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="longName">Активно</label><br />
		</span> <input id="active" name="active" type="checkbox" value="1" ${checked }/>
		</p>
		<div class="clr"><!--  --></div>
		</fieldset>
		<fieldset><legend>Избор на родителско меню</legend> <nacid:menuEditList
			radioButtons="true" /></fieldset>

	</v:form>
</nacid:menuEditEdit>

<%@ include file="/screens/footer.jsp"%>
