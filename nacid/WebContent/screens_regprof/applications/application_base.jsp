<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens_regprof/header.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:set var="activeForm" scope="page" value="${param.activeForm}" />
<script type="text/javascript">
var applicationId = ${requestScope["com.nacid.bl.impl.applications.regprof.RegprofApplicationImpl"].id};
var activeForm = <%= request.getParameter("activeForm") %> == null ? 1 : <%= request.getParameter("activeForm") %>;
var activeFormId = parseInt(activeForm);
if (applicationId == null) applicationId = 0;

function changeActiveForm(input) {
    var initFormValue = initformvalues[activeFormId];
    var isError = false;
    if (activeFormId != 4 && activeFormId != 5) {
        if (initFormValue != null && initFormValue != $("appform" + activeFormId).serialize()) {
            isError = true;
        }
    } else { //4tiq tab se obrabotva po razlichen na4in, zashtoto v nego ima mnogo pove4e ot 1 forma, koqto se submitva!!! Teoreti4no moje i drugite tabove da se obrabotvat kato nego, no ne sym siguren dali nqma da vyznikne nqkakyv nepredviden problem i zatova tqh gi ostavqm po stariq na4in!
        if (initFormValue != null && initFormValue.toQueryString() != generateFormSerializedValue(activeFormId).toQueryString()) {
            isError = true;
        }
    }
    if (isError) {
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
/*function text2html(text) {
	var div = document.createElement("div");
	div.setAttribute('id', 'form5_innerDiv');
	div.innerHTML = text;
	return div;
}*/
function changeFormToExpert() {
	new Ajax.Request('${pathPrefix}/ajax/applications_expert/view?application_id=' + applicationId, {
        onComplete: function(oXHR) {
		      if (oXHR.responseText == '${messages.not_recognized}') {
		    	   alert('${messages.application_not_recognized}');
			    } else if (oXHR.responseText == 'ok') {
			    	changeActiveForm($('form5'));
			    }
		      
        },
        onFailure : function(oXHR) {
          alert("Възникна грешка при опит за зареждане на  ${messages.expert} моля опитайте по-късно..." + oXHR.statusText);
        },
        method: 'GET'
  });
}
function resetForm(formName) {
    var formId = parseInt(formName.replace('appform',''));
    initformvalues[formId] = $(formName).serialize();
}
</script>

<ul id="formbuttons" class="tabs">
<nacid_regprof:regprofApplicationFormHeader formid="0">
	<li><a href="javascript:void(0);" id="form0" onclick="javascript:changeActiveForm(this);" class="m0 ${formHeaderClass }"><b class="p1"><!--  --></b><b class="p2">Подадени данни</b><b class="p3"><!--  --></b></a></li>
</nacid_regprof:regprofApplicationFormHeader>
<nacid_regprof:regprofApplicationFormHeader formid="1">
	<li><a href="javascript:void(0);" id="form1" onclick="javascript:changeActiveForm(this);" class="m1 ${formHeaderClass }"><b class="p1"><!--  --></b><b class="p2">Заявление</b><b class="p3"><!--  --></b></a></li>
</nacid_regprof:regprofApplicationFormHeader>
<nacid_regprof:regprofApplicationFormHeader formid="2">
	<li><a href="javascript:void(0);" id="form2" onclick="javascript:changeActiveForm(this);" class="m2 ${formHeaderClass }" style="${hideTrainingCourse}"><b class="p1"><!--  --></b><b class="p2">Обучение/Стаж</b><b class="p3"><!--  --></b></a></li>	
</nacid_regprof:regprofApplicationFormHeader>
<nacid_regprof:regprofApplicationFormHeader formid="3">
	<li><a href="javascript:void(0);" id="form3" onclick="javascript:changeActiveForm(this);" class="m3 ${formHeaderClass }"><b class="p1"><!--  --></b><b class="p2">Приложения</b><b class="p3"><!--  --></b></a></li>
</nacid_regprof:regprofApplicationFormHeader>
<nacid_regprof:regprofApplicationFormHeader formid="4">
	<li><a href="javascript:void(0);" id="form4" onclick="javascript:changeActiveForm(this);" class="m4 ${formHeaderClass }"><b class="p1"><!--  --></b><b class="p2">Проверка</b><b class="p3"><!--  --></b></a></li>
</nacid_regprof:regprofApplicationFormHeader>
<nacid_regprof:regprofApplicationFormHeader formid="5">
	<li><a href="javascript:void(0);" id="form5" onclick="javascript:changeActiveForm(this);" class="m5 ${formHeaderClass }" style="${hideFinalization}"><b class="p1"><!--  --></b><b class="p2">Приключване</b><b class="p3"><!--  --></b></a></li>
</nacid_regprof:regprofApplicationFormHeader>
</ul>

<nacid_regprof:regprofApplicationFormHeader formid="0">
	<div id="div_form0" style="${formDivStyle }"><%@ include file="/screens_regprof/applications/app_form0.jsp"%></div>
</nacid_regprof:regprofApplicationFormHeader>

<nacid_regprof:regprofApplicationFormHeader formid="1">
	<div id="div_form1" style="${formDivStyle }"><%@ include file="/screens_regprof/applications/app_form1.jsp"%></div>
</nacid_regprof:regprofApplicationFormHeader>

<nacid_regprof:regprofApplicationFormHeader formid="2">
	<div id="div_form2" style="${formDivStyle }"><%@ include file="/screens_regprof/applications/app_form2.jsp"%></div> 
</nacid_regprof:regprofApplicationFormHeader>

<nacid_regprof:regprofApplicationFormHeader formid="3">
	<div id="div_form3" style="${formDivStyle }"><%@ include file="/screens_regprof/applications/app_form3.jsp"%></div>
</nacid_regprof:regprofApplicationFormHeader>

<nacid_regprof:regprofApplicationFormHeader formid="4">
	<div id="div_form4" style="${formDivStyle }"><%@ include file="/screens_regprof/applications/app_form4.jsp"%></div>
</nacid_regprof:regprofApplicationFormHeader>

<nacid_regprof:regprofApplicationFormHeader formid="5">
	<div id="div_form5" style="${formDivStyle }"><%@ include file="/screens_regprof/applications/app_form5.jsp"%></div>
</nacid_regprof:regprofApplicationFormHeader>

<script type="text/javascript">

var initformvalues = new Array();
initformvalues[1] = $('appform1').serialize();
initformvalues[2] = '';
if($('appform2')) {
    initformvalues[2] = $('appform2').serialize();
}
initformvalues[4] = generateFormSerializedValue('4');
initformvalues[5] = generateFormSerializedValue('5');

function generateFormSerializedValue(id) {
	var el = new Hash();
    $$('#div_form' + id +' form').each(function(element) {
        el.set(element.id, element.serialize());
    });
    return el;
}

if ("${requestScope['com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseImpl'].details.hasEducation}" == 0 && 
		"${requestScope['com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseImpl'].details.hasExperience}" == 0 && $('form4') != null) {
	$('form4').hide();
	$('div_form4').hide();
}

<%--var status = parseInt("${requestScope['com.nacid.bl.impl.applications.regprof.RegprofApplicationImpl'].applicationDetails.status}");
if ( status < <%= RegprofApplicationStatus.APPLICATION_DENIAL_STATUS_CODE %> &&
		status != <%= RegprofApplicationStatus.APPLICATION_RECOGNIZED_EXPERIENCE_STATUS_CODE %> &&
		status != <%= RegprofApplicationStatus.APPLICATION_QUALIFICATION_STATUS_CODE %>) {
	$('form5').hide();
	$('div_form5').hide();
}--%>

</script>

<%@ include file="/screens_regprof/footer.jsp"%>