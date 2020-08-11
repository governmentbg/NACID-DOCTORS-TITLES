<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<h3 class="title"><span>${operationStringForScreens } съобщения към заявление</span></h3>

<c:set var="back_onclick" value="document.location.href='${backUrl }';" />
<p class="cc">
	<input class="back" type="button" onclick="${back_onclick }" value="Назад" />
</p>

<fieldset><legend class="noForm">Данни за съобщението</legend>

	<p><span class="fieldlabel"><label>Дата:</label><br /></span>
		${extApplicationCommentWebModel.dateCreated }
	</p>
	<div class="clr"><!--  --></div>
	<p><span class="fieldlabel"><label>Коментар:</label><br /></span>
		${extApplicationCommentWebModel.comment }
	</p>
	<div class="clr"><!--  --></div>
	<p><span class="fieldlabel"><label>Потребител:</label><br /></span>
		${extApplicationCommentWebModel.userCreated }
	</p>
	<div class="clr"><!--  --></div>
	<c:if test="${extApplicationCommentWebModel.sendEmail}">
		<p><span class="fieldlabel"><label>Относно:</label><br /></span>
				${extApplicationCommentWebModel.subject }
		</p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel"><label>Статус:</label><br /></span>
				${extApplicationCommentWebModel.processed ? 'изпратено' : 'неизпратено'}
		</p>
		<div class="clr"><!--  --></div>

		<p><span class="fieldlabel"><label>Тип:</label><br /></span>
				${extApplicationCommentWebModel.incoming ? 'входящо' : 'изходящо' }
		</p>
		<div class="clr"><!--  --></div>

		<p><span class="fieldlabel"><label>Получател:</label><br /></span>
				${extApplicationCommentWebModel.recipient }
		</p>
		<div class="clr"><!--  --></div>
	</c:if>
</fieldset>

<div class="clr20"><!--  --></div>
<p class="cc">
	<input class="back" type="button" onclick="${back_onclick }" value="Назад" />
</p>