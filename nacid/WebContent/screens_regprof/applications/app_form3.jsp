<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/regprof_nacid_taglib.tld" prefix="nacid_regprof"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<nacid_regprof:regprofApplicationAttachmentsList>
	<h3 class="title"><span>Списък на прикачените документи</span></h3>

	<h3 class="names">${application_header }</h3>
	<div class="clr15"><!--  --></div>
	<p class="cc">
		<input class="back" type="button" onclick="document.location.href='${pathPrefix }/control/${back_screen}/list?getLastTableState=1&appNumber=${sessionScope.app_number_filter_value }';" value="Назад" />
	</p>

	<nacid:list />

	<script type="text/javascript">
	/*
	 * маха линковете за сортиране на таблицата
	 */

	$$('#main_table td[class="dark"] > a').each(function(anchor) {
	  anchor.up(0).innerHTML = anchor.innerHTML; 
	});
		

		$$('#main_table a[title="Преглед"]').each(function(link) {

			var cell = link.ancestors()[0];
			var row = cell.ancestors()[0];
			var fileName = (row.childElements())[6].innerHTML;

			
  			var old = link.href;
  			var ids = old.indexOf('id=') + 3;
  			var id = old.substring(ids, old.length);
			link.href = '${pathPrefix }/control/application_attachment/'+fileName+'?id='+id;

			var src = '${pathPrefix }/control/application_attachment/view?width=${messages.imgPreviewWidth }&amp;id='+id;
			(row.childElements())[6].innerHTML = '<img src="'+src+'" alt=""/>';

		});
		document.tableForm1.action = '${pathPrefix }/control/${back_screen}/${operation}?id=${record_id }&amp;activeForm=3';
		<%--document.tableForm2.action = '${pathPrefix }/control/${back_screen}/${operation}?id=${record_id }&amp;activeForm=3';--%>
	</script>
</nacid_regprof:regprofApplicationAttachmentsList>

<div class="clr20"><!--  --></div>
<p class="cc">
	<input class="back" type="button" onclick="document.location.href='${pathPrefix }/control/${back_screen}/list?getLastTableState=1&appNumber=${sessionScope.app_number_filter_value }';" value="Назад" />
</p>