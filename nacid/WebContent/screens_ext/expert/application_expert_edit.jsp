<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens_ext/header.jsp"%>
<script type="text/javascript">
var activeFormId = ${requestScope["expertApplicationFormId"] + 0} == 0 ? 1 : ${requestScope["expertApplicationFormId"] + 0 };
function changeActiveForm(input) {
	
	var newFormId = input.id.replace("form","");
	$("form" + activeFormId).removeClassName("selected");
  $("div_form" + activeFormId).hide();

  $("form" + newFormId).addClassName("selected");
  $("div_form" + newFormId).show();
  activeFormId = newFormId;
}
</script>

<ul id="formbuttons" class="tabs">
<nacid_ext:expertApplicationFormHeader formid="1">
	<li><a href="javascript:void(0);" id="form1" onclick="javascript:changeActiveForm(this);" class="m1 ${formHeaderClass }"><b class="p1"><!--  --></b><b class="p2">Заявление</b><b class="p3"><!--  --></b></a></li>
</nacid_ext:expertApplicationFormHeader> 
<nacid_ext:expertApplicationFormHeader formid="2">
  <li><a href="javascript:void(0);" id="form2" onclick="javascript:changeActiveForm(this);" class="m2 ${formHeaderClass }"><b class="p1"><!--  --></b><b class="p2">Становища</b><b class="p3"><!--  --></b></a></li>
</nacid_ext:expertApplicationFormHeader> 
</ul>

<nacid_ext:expertApplicationFormHeader formid="1">
	<div id="div_form1" style="${formDivStyle }">
	   <%@ include file="application_expert_report.jsp"%>
	</div>
</nacid_ext:expertApplicationFormHeader>
<nacid_ext:expertApplicationFormHeader formid="2">
	<div id="div_form2" style="${formDivStyle }"><%@ include file="statements_list.jsp"%></div>
</nacid_ext:expertApplicationFormHeader>


<%@ include file="/screens_ext/footer.jsp"%>