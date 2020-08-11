<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<nacid:diplomaTypeEdit>
<script type="text/javascript">
function getUniversities(el) {
    var universityId = el.id.replace("country","");
    var countryId = el.options[el.selectedIndex].value;
	new Ajax.Request('${pathPrefix}/control/diploma_type_university?university_number=' + universityId + '&countryId=' + countryId, {
        onComplete: function(oXHR) {
            $("university_span" + universityId).update(oXHR.responseText);
            // alert($("#university_span" +universityId));
            $$("#university_span" +universityId+" select")[0].setAttribute("onChange", "getUniversityFaculties(this, '-')");
        },
        method: 'GET'
  });
}

function updateOriginalEduLevels() {
    var el = $("country0");
    var countryId = el.options[el.selectedIndex].value;
    var eduLevelId = $('eduLevel').value;
    new Ajax.Request('${pathPrefix}/control/original_education_level/view?type=combo&countryId=' + countryId + "&eduLevelId=" + eduLevelId, {
        onComplete: function(oXHR) {
            $("orgEduLvl").update(oXHR.responseText);
        },
        method: 'GET'
    });
};
function updateNqf() {
    updateNationalQualificationsFrameworkCombo('nationalQualificationsFrameworkId', null);
    updateNationalQualificationsFrameworkCombo('nationalQualificationsFrameworkAccessId', null);
}
function updateNationalQualificationsFrameworkCombo(comboName, defaultValue) {
    var countryId = $("country0").value;
    new Ajax.Request('${pathPrefix}/control/national_qualifications_framework_ajax/view?countryId=' + countryId + '&name=' + comboName + '&defaultValue=' + defaultValue + '&style=brd w600', {
        asynchronous: false,
        onComplete: function(oXHR) {
            $(comboName + "_combo").update(oXHR.responseText);
        },
        method: 'GET'
    });
};
var universities = ${universities_count};
function addUniversity() {
    var el = $('university').clone(true);
    el.id = el.id + universities;
    $('eduLvl').insert({before:el});
    el.show();
    /* Preobarzuva ID-tata i name=ovete na inputite/selectovete/textbox-a, kato im slaga po edno jointUniversityId v kraq*/
    $$('#university' + universities + ' select', '#university' + universities + ' span').each(function(element) {
    	
        
        if (element.name != null && element.name != "") {
        	element.name = element.name + universities;
        }
        if (element.id != null && element.id != "") {
            element.id = element.id + universities ;
        }
    });
    /*Preobrazuva href-a na linka "premahni"*/
    $$('#university' + universities + ' a[href="javascript:void(0);"]')[0].setAttribute('href', "javascript:$('university" + universities + "').remove();enableDisableJointDegreeCheckBox();");
     
    universities++;
    $('universities_count').value = universities;
  }
function showHideJointDegree(show) {
    var el = $('add_universities');
	if (show) {
	    el.show();
	} else {
		el.hide();
	}
}
function enableDisableJointDegreeCheckBox() {
    if ($$('.university_class').length == 2)  {
        $('joint_degree').enable();
    } else {
    	$('joint_degree').disable();
    }
}
function getUniversityFaculties(el, facValue) {
    var universityNumber = el.id.replace("university_id","");
    var universityId = el.options[el.selectedIndex].value;
    new Ajax.Request('${pathPrefix}/control/diploma_type_university_faculty?university_number=' + universityNumber + '&university_id=' + universityId, {
        asynchronous: false,
        onComplete: function(oXHR) {
            $("university_faculty_span" + universityNumber).update(oXHR.responseText);
            $$("#university_faculty_span" + universityNumber + " select")[0].value = facValue;
            // $$("#diplomaTypeUniversityFaculty_span" +universityId+" select")[0].setAttribute("onChange", "showHideDiplomaTypeEditUniButton(this);");

        },
        method: 'GET'
    });
}
</script>
<h3 class="title"><span>${operationStringForScreens } тип диплома</span></h3>

	<v:form action="${pathPrefix }/control/diploma_type/save" method="post" name="form_diploma_type"
		backurl="${pathPrefix }/control/diploma_type/list?getLastTableState=1">

		<nacid:systemmessage name="diplomaTypeSystemMessage" />

		<v:dateIntervalValidator format="d.m.yyyy" dateTo="dateTo" dateFrom="dateFrom" />
		<v:dateValidator format="d.m.yyyy" input="dateTo" beforeToday="true"/>
		<v:dateValidator format="d.m.yyyy" input="dateFrom" beforeToday="true" />
		<v:comboBoxValidator input="university_id0" required="true" errormessage="${messages.must_choose_university}" />
		<v:comboBoxValidator input="eduLevel" required="true" errormessage="${messages.must_choose_edu_level}" />
		<v:textValidator input="title" required="true"/>
		<input id="id" type="hidden" name="id" value="${id }" />
		<input id="type" type="hidden" name="type" value="${type }" />
        <c:set var="onUniversityChange" value="getUniversityFaculties(this, '-')" />
		<fieldset><legend>Основни данни</legend>

		<p><span class="fieldlabel2"><label for="title">Заглавие</label><br />
		</span> <v:textinput class="brd w600" id="title" name="title"  value="${title }" /></p>
		<div class="clr"><!--  --></div>
		<p id="jointDegree" class="checkbox">
		  <input id="joint_degree" name="joint_degree" value="1" type="checkbox" ${joint_degree_checked } ${joint_degree_disabled } onclick="showHideJointDegree(this.checked);"/>
		  <label for="joint_degree">Съвместна степен</label>
		</p>
		
		<nacid:diplomaTypeIssuerEdit type="other">
		<div id="university${university_number }" class="university_class">
			<fieldset><legend>Висше училище</legend>
			  <c:if test="${university_number != 0}">
			     <p class="flt_rgt"><a href="javascript:void(0);" style="color: red" onclick="javascript:$('university${university_number}').remove();enableDisableJointDegreeCheckBox();">Премахни</a></p>
			  </c:if>
			  <div class="clr"><!--  --></div>
			  <p><span class="fieldlabel2"><label for="country${university_number }">${messages.Country }</label></span> 
			      <c:choose>
                      <c:when test="${university_number == 0}">
                          <nacid:combobox id="country${university_number }" name="country${university_number }" attributeName="countryCombo" style="w600 brd" onchange="getUniversities(this);updateOriginalEduLevels();updateNqf();" />
                      </c:when>
                      <c:otherwise>
                          <nacid:combobox id="country${university_number }" name="country${university_number }" attributeName="countryCombo" style="w600 brd" onchange="getUniversities(this);" />
                      </c:otherwise>
                  </c:choose>
			  </p>
			  <div class="clr"><!--  --></div>
			  <p><span class="fieldlabel2"><label for="university${university_number }">${messages.University }</label></span> 
			      <span id="university_span${university_number }">
                      <%@include file="diploma_type_university.jsp" %>
			      </span>
			  </p>
                <div class="clr"><!--  --></div>
                <p><span class="fieldlabel2"><label for="university_faculty${university_number }">${messages.UniversityFaculty }</label></span>
                    <span id="university_faculty_span${university_number }">
			         <%@include file="diploma_type_university_faculty.jsp" %>
			      </span>
                </p>
                <div class="clr10"><!--  --></div>
			</fieldset>
		</div>
		
		<c:if test="${university_number == 0}">
            <div class="clr10"><!--  --></div>
            <p class="flt_rgt" id="add_universities" ${add_universities_display }><a href="javascript:addUniversity();enableDisableJointDegreeCheckBox();">Добави висше училище</a></p>
            <div class="clr10"><!--  --></div>
        </c:if>
		</nacid:diplomaTypeIssuerEdit>
		
		<nacid:diplomaTypeIssuerEdit type="empty">
		<div id="university" style="display:none;" class="university_class">
            <fieldset><legend>Висше училище</legend>
              <p class="flt_rgt"><a href="javascript:void(0);" style="color: red">Премахни</a></p>
              <div class="clr"><!--  --></div>
              <p><span class="fieldlabel2"><label>${messages.Country }</label></span> 
                  <nacid:combobox id="country" name="country" attributeName="countryCombo" style="w600 brd" onchange="getUniversities(this);" />
              </p>
              <div class="clr"><!--  --></div>
              <p><span class="fieldlabel2"><label>${messages.University }</label></span> 
                  <span id="university_span">
                      <%@include file="diploma_type_university.jsp" %>
                  </span>
              </p>
                <div class="clr"><!--  --></div>
                <p><span class="fieldlabel2"><label for="university_faculty">${messages.UniversityFaculty }</label></span>
                    <span id="university_faculty_span">
			         <%@include file="diploma_type_university_faculty.jsp" %>
			      </span>
                </p>
              <div class="clr10"><!--  --></div>
            </fieldset>
        </div>
        </nacid:diplomaTypeIssuerEdit>
		<div id="eduLvl" class="clr20"><!--  --></div>
		<p><span class="fieldlabel2"><label for="eduLevel">Образователна степен</label><br />
		</span> <nacid:combobox id="eduLevel" name="eduLevel" attributeName="eduLevelCombo" style="w600 brd" onchange="updateOriginalEduLevels();"/></p>
		<div class="clr"><!--  --></div>
        <div class="clr20"><!--  --></div>
        <p>
            <span class="fieldlabel2"><label for="eduLevel">Оригинална образователна степен</label></span>
            <span id="orgEduLvl" >
                <nacid:combobox id="originalEduLevel" name="originalEduLevel" attributeName="originalEduLevelCombo" style="w600 brd" />
            </span>
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel2"><label for="bolognaCycleId">${messages.bolognaCycle }</label></span>
            <nacid:combobox id="bolognaCycleId" name="bolognaCycleId" attributeName="bolognaCycleCombo" style="w600 brd"/>
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel2"><label for="nationalQualificationsFrameworkId">${messages.nationalQualificationsFramework }</label></span>
            <span id="nationalQualificationsFrameworkId_combo"><nacid:combobox id="nationalQualificationsFrameworkId" name="nationalQualificationsFrameworkId" attributeName="nqfCombo" style="w600 brd"/></span>
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel2"><label for="europeanQualificationsFrameworkId">${messages.europeanQualificationsFramework }</label></span>
            <nacid:combobox id="europeanQualificationsFrameworkId" name="europeanQualificationsFrameworkId" attributeName="eqfCombo" style="w600 brd"/>
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel2"><label for="bolognaCycleAccessId">${messages.bolognaCycleAccess }</label></span>
            <nacid:combobox id="bolognaCycleAccessId" name="bolognaCycleAccessId" attributeName="bolognaCycleAccessCombo" style="w600 brd"/>
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel2"><label for="nationalQualificationsFrameworkAccessId">${messages.nationalQualificationsFrameworkAccess }</label></span>
            <span id="nationalQualificationsFrameworkAccessId_combo"><nacid:combobox id="nationalQualificationsFrameworkAccessId" name="nationalQualificationsFrameworkAccessId" attributeName="nqfAccessCombo" style="w600 brd"/></span>
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel2"><label for="europeanQualificationsFrameworkAccessId">${messages.europeanQualificationsFrameworkAccess }</label></span>
            <nacid:combobox id="europeanQualificationsFrameworkAccessId" name="europeanQualificationsFrameworkAccessId" attributeName="eqfAccessCombo" style="w600 brd"/>
        </p>
        <%--<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="visualElementsDescr">Описание на визуалните елементи</label><br />
		</span> <textarea id="visualElementsDescr" class="brd w600" rows="3" cols="40" name="visualElementsDescr">${visualElementsDescr }</textarea></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="protectionElementsDescr">Описание на защитните елементи</label><br />
		</span> <textarea id="protectionElementsDescr" class="brd w600" rows="3" cols="40" name="protectionElementsDescr">${protectionElementsDescr }</textarea></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="numberFormatDescr">Описание на формата на номера</label><br />
		</span> <textarea id="numberFormatDescr" class="brd w600" rows="3" cols="40" name="numberFormatDescr">${numberFormatDescr }</textarea></p>--%>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="notes">Бележки</label><br />
		</span> <textarea id="notes" class="brd w600" rows="3" cols="40" name="notes">${notes }</textarea></p>
		<div class="clr"><!--  --></div>

		<p><span class="fieldlabel2"><label for="dateFrom">От дата</label><br />
		</span> <v:dateinput name="dateFrom" id="dateFrom" value="${dateFrom }" style="brd w200" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="dateTo">До дата</label><br />
		</span> <v:dateinput name="dateTo" id="dateTo" value="${dateTo }" style="brd w200" /></p>
		<div class="clr"><!--  --></div>
		</fieldset>

        <input type="hidden" name="universities_count" id="universities_count" value="${universities_count }" />
	</v:form>
</nacid:diplomaTypeEdit>

<nacid:list />

<c:if test="${tableWebModel != null}">
	<script type="text/javascript">

		/*
 		* маха линковете за сортиране на таблицата с university_validity
 		*/
		$$('#main_table td[class="dark"] > a').each(function(anchor) {
  			anchor.up(0).innerHTML = anchor.innerHTML; 
		});
		$$('a[title="Преглед"]').each(function(link) {

			var cell = link.ancestors()[0];
			var row = cell.ancestors()[0];
			var fileName = (row.childElements())[3].innerHTML;

			
  			var old = link.href;
  			var ids = old.indexOf('id=') + 3;
  			var id = old.substring(ids, old.length);
			link.href = '${pathPrefix }/control/diploma_type_attachment/'+fileName+'?id='+id;

			var src = '${pathPrefix }/control/diploma_type_attachment/view?width=${messages.imgPreviewWidth }&id='+id;
			(row.childElements())[5].innerHTML = '<img src="'+src+'" alt=""/>';
	
		});

		document.tableForm1.action = '${backUrlDiplType }';
		<%--document.tableForm2.action = '${backUrlDiplType }';--%>



	</script>
</c:if>
<%@ include file="/screens/footer.jsp"%>
