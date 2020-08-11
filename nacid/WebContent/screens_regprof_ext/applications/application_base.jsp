<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens_regprof_ext/header.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<script type="text/javascript">
var applicationId = '${applicationWebModel.id}';
var activeFormId = ${requestScope["activeForm"] + 0} == 0 ? 1 : ${requestScope["activeForm"] + 0 };
<c:choose>
<c:when test="${not empty operationView}">
   var isView = true;
</c:when> 
<c:otherwise>
	var isView = false;
</c:otherwise>
</c:choose>
function changeActiveForm(input) {
	var initFormValue = initformvalues[activeFormId];
	if (!isView && initFormValue != null && initFormValue != $("appform" + activeFormId).serialize()) {
		  alert("Трябва да запишете данните!");
			return;
	}
	var newFormId = input.id.replace("form","");
	$("form" + activeFormId).removeClassName("selected");
  $("div_form" + activeFormId).hide();

  $("form" + newFormId).addClassName("selected");
  $("div_form" + newFormId).show();
  activeFormId = newFormId;
}
</script>

<ul id="formbuttons" class="tabs">

<nacid_regprof_ext:extRegprofApplicationFormHeader formid="1">
 	<li><a href="javascript:void(0);" id="form1" onclick="javascript:changeActiveForm(this);" class="m1 ${formHeaderClass }"><b class="p1"><!--  --></b><b class="p2">Заявител</b><b class="p3"><!--  --></b></a></li>
</nacid_regprof_ext:extRegprofApplicationFormHeader> 

<nacid_regprof_ext:extRegprofApplicationFormHeader formid="2">
  <li><a href="javascript:void(0);" id="form2" onclick="javascript:changeActiveForm(this);" class="m2 ${formHeaderClass }"><b class="p1"><!--  --></b><b class="p2">Обучение</b><b class="p3"><!--  --></b></a></li>
</nacid_regprof_ext:extRegprofApplicationFormHeader> 
<nacid_regprof_ext:extRegprofApplicationFormHeader formid="3">
  <li><a href="javascript:void(0);" id="form3" onclick="javascript:changeActiveForm(this);" class="m3 ${formHeaderClass }"><b class="p1"><!--  --></b><b class="p2">Приложения</b><b class="p3"><!--  --></b></a></li>	
</nacid_regprof_ext:extRegprofApplicationFormHeader> 
<nacid_regprof_ext:extRegprofApplicationFormHeader formid="4">
  <li><a href="javascript:void(0);" id="form4" onclick="javascript:changeActiveForm(this);" class="m4 ${formHeaderClass }"><b class="p1"><!--  --></b><b class="p2">Подаване</b><b class="p3"><!--  --></b></a></li>	
</nacid_regprof_ext:extRegprofApplicationFormHeader>

<nacid_regprof_ext:extRegprofApplicationFormHeader formid="5">
  <li><a href="javascript:void(0);" id="form5" onclick="javascript:changeActiveForm(this);" class="m5 ${formHeaderClass }"><b class="p1"><!--  --></b><b class="p2">Заявление</b><b class="p3"><!--  --></b></a></li>    
</nacid_regprof_ext:extRegprofApplicationFormHeader>
<nacid_regprof_ext:extRegprofApplicationFormHeader formid="6">
  <li><a href="javascript:void(0);" id="form6" onclick="javascript:changeActiveForm(this);" class="m6 ${formHeaderClass }"><b class="p1"><!--  --></b><b class="p2">Информация</b><b class="p3"><!--  --></b></a></li>    
</nacid_regprof_ext:extRegprofApplicationFormHeader>
<nacid_regprof_ext:extRegprofApplicationFormHeader formid="7">
  <li><a href="javascript:void(0);" id="form7" onclick="javascript:changeActiveForm(this);" class="m7 ${formHeaderClass }"><b class="p1"><!--  --></b><b class="p2">Плащане</b><b class="p3"><!--  --></b></a></li>    
</nacid_regprof_ext:extRegprofApplicationFormHeader>
</ul>

<h3 class="names">${operationStringForScreens } заявление ${erpApplicationWebModel.docflowNumber }</h3>
<h3 class="names">Статусът на заявлението е ${erpApplicationWebModel.statusName}${erpApplicationWebModel.certificateNumber }</h3>
<nacid_regprof_ext:extRegprofApplicationFormHeader formid="1">
	<div id="div_form1" style="${formDivStyle }"><%@ include file="application_edit.jsp"%></div>
</nacid_regprof_ext:extRegprofApplicationFormHeader>
<nacid_regprof_ext:extRegprofApplicationFormHeader formid="2">
	<div id="div_form2" style="${formDivStyle }"><%@ include file="training_course_edit.jsp"%></div>
</nacid_regprof_ext:extRegprofApplicationFormHeader>
<nacid_regprof_ext:extRegprofApplicationFormHeader formid="3">
	<div id="div_form3" style="${formDivStyle }"><%@ include file="attached_docs.jsp"%></div>
</nacid_regprof_ext:extRegprofApplicationFormHeader>
<nacid_regprof_ext:extRegprofApplicationFormHeader formid="4">
	<div id="div_form4" style="${formDivStyle }"><%@ include file="applying.jsp"%></div>
</nacid_regprof_ext:extRegprofApplicationFormHeader>

<nacid_regprof_ext:extRegprofApplicationFormHeader formid="5">
    <div id="div_form5" style="${formDivStyle }"><%@ include file="report_application_external.jsp"%></div>
</nacid_regprof_ext:extRegprofApplicationFormHeader>
<nacid_regprof_ext:extRegprofApplicationFormHeader formid="6">
    <div id="div_form6" style="${formDivStyle }"><%@ include file="report_application_internal.jsp"%></div>
</nacid_regprof_ext:extRegprofApplicationFormHeader>
<nacid_regprof_ext:extRegprofApplicationFormHeader formid="7">
    <div id="div_form7" style="${formDivStyle }"><%@ include file="epay_payment.jsp"%></div>
</nacid_regprof_ext:extRegprofApplicationFormHeader>
<script type="text/javascript">

var initformvalues = new Array();
if($('appform1')) {
	initformvalues[1] = $('appform1').serialize();
}
initformvalues[2] = '';

if($('appform2')) {
	initformvalues[2] = $('appform2').serialize();
}
if ($('appform4')) {
	initformvalues[4] = $('appform4').serialize();
}
</script>


<%@ include file="/screens_regprof_ext/footer.jsp"%>