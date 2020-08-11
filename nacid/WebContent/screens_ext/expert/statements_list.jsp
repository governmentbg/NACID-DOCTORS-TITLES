<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>

	<h3 class="title"><span>Списък със становища</span></h3>
	<div class="clr15"><!--  --></div>
	<p class="cc">
		<input class="back" type="button" onclick="document.location.href='${pathPrefix }/control/expert_application/list?getLastTableState=1';" value="Назад" />
	</p>
	<div class="clr15"><!--  --></div>
	<form name="finishedForm" method="post" action="${pathPrefix }/control/expert_application/save">
		<nacid:systemmessage name="expFinishedSM"/>
		<p class="checkbox">
		  <input type="hidden" name="id" value="${applicId}"/>
		  <input ${finishedSelected } id="finished" name="finished" value="1" type="checkbox" />
		  <label for="diploma_names">
		  		<b>Приключих с работата по това заявление</b>
		  		<input type="submit" class="save" value="Запиши" style="width: 100px;" />
		  </label>
		</p>
	</form>
	<div class="clr15"><!--  --></div>
	<nacid:list />

	<script type="text/javascript">
	/*
	 * маха линковете за сортиране на таблицата
	 */
	$$('#main_table td[class="dark"] > a').each(function(anchor) {
	  anchor.up(0).innerHTML = anchor.innerHTML; 
	});
		

		$$('#main_table a[title="Преглед"]').each(function(link) {
			var FILE_NAME_COL = 4;
			var PREVIEW_COL = 6;
			
			var cell = link.ancestors()[0];
			var row = cell.ancestors()[0];
			var fileName = (row.childElements())[FILE_NAME_COL].innerHTML;

			
  			var old = link.href;
  			var ids = old.indexOf('id=') + 3;
  			var id = old.substring(ids, old.length);
			link.href = '${pathPrefix }/control/expert_statement/'+fileName+'?id='+id;

			var src = '${pathPrefix }/control/expert_statement/view?width=${messages.imgPreviewWidth }&id='+id;
			(row.childElements())[PREVIEW_COL].innerHTML = '<img src="'+src+'" alt=""/>';
	
		});
		document.tableForm1.action = '${pathPrefix }/control/expert_application/edit?id=${applicationId }&activeForm=2';
		<%--document.tableForm2.action = '${pathPrefix }/control/expert_application/edit?id=${applicationId }&activeForm=2';--%>

	</script>

<div class="clr10"><!--  --></div>
<p class="cc">
	<input class="back" type="button" onclick="document.location.href='${pathPrefix }/control/expert_application/list?getLastTableState=1';" value="Назад" />
</p>