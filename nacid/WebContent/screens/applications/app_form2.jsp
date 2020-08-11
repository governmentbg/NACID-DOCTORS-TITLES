<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<%@page import="com.nacid.bl.nomenclatures.NomenclaturesDataProvider"%>

<nacid:trainingCourseEdit>
	<h3 class="title"><span>Информация за курса на обучение</span></h3>
	<script type="text/javascript">
var trainingLocations = ${training_locations_size + 0};
var isDoctorateApplicationType = ${isDoctorateApplicationType};
var isRudiApplicationType = ${isRudiApplicationType};
function updateUniversity(value, data, el) {
	var id = el.id;
	var d = data.evalJSON(true);
	updateBaseUniversityFields(d);
	updateDiplomaType(d.id);
    showHideEditUniversityButton();
}
function updateUniversityFaculty(value, data, el) {
    var id = el.id;
    var d = data.evalJSON(true);
    $('base_university_faculty').value = d.name;
    $('base_university_faculty_id').value = d.id;
}
//Update-va combobox-a s diplomaTypes, no samo ako ne e doctorate!
function updateDiplomaType(universityId) {
    if (isDoctorateApplicationType) {
        return;
	}
    new Ajax.Request('${pathPrefix}/control/diploma_type_ajax?type=1&universityId=' + universityId + '&application_id=' + document.appform2.id.value, {
        asynchronous : false,
        onSuccess: function(oXHR) {
            $("diploma_type_span").update(oXHR.responseText);
            $("edu_level_name").update("");
            $("original_edu_level_name").update("");
            $("original_edu_level_name_translated").update("");
            $("joint_universities").update("");
            $("editDiplomaType_diploma_type").hide();
        },
        onFailure : function(oXHR) {
			alert("Възникна грешка при опит за прочитане на видовете диплома за този университет. Моля опитайте по-късно..." + oXHR.statusText);
			$("diploma_type").update("");
            $("edu_level_name").update("");
            $("original_edu_level_name").update("");
			$("original_edu_level_name_translated").update("");
            $("joint_universities").update("");
		},
        method: 'GET'
      });
}
function updateBaseUniversityDiplomaType(editDiplomaTypeResponse) {
    var uni = editDiplomaTypeResponse.universities[0];
	updateBaseUniversityFields(uni);
}
function updateBaseUniversityFields(json) {
	$("base_university_id").value = json.id;
	$("base_university_original_name").value = json.orgName;
	$("base_university_name_bg").value = json.bgName;
	$("base_university_country_id").value = json.countryId;
	$("base_university_city").value = json.city;
	$("base_university_address").value = json.address;
	if (json.urlDiplomaRegisterLink == null || json.urlDiplomaRegisterLink == '') {
		$("base_university_url_diploma_register_container").hide();
		$("base_university_url_diploma_register").innerHTML = "";
	} else {
		$("base_university_url_diploma_register_container").show();
		$("base_university_url_diploma_register").innerHTML = json.urlDiplomaRegisterLink;
	}

	$("base_university_generic_name").value = json.genericName;
	$("base_university_generic_name_id").value = json.genericNameId;
}
function updateBaseUniversityFacultyDiplomaType(editDiplomaTypeResponse) {
	$("base_university_faculty").value = editDiplomaTypeResponse.universities[0].facultyName;
	$("base_university_faculty_id").value = editDiplomaTypeResponse.universities[0].facultyId;
}
function updateJointUniversities(select) {
	var diplomaTypeId = select.options[select.selectedIndex].value;
	if (diplomaTypeId == '-') {
		$("joint_universities").update("");
	} else {
		//Podava se kakto diplomaTypeId-to, taka i baseUniversityId-to, za da moje da se izkliu4i vyv vyrnatite rezultati!
		new Ajax.Request('${pathPrefix}/control/training_course_diploma_type_universities_ajax?diplomaTypeId=' + diplomaTypeId + '&baseUniversityId=' + $('base_university_id').value, {
			onCreate : function(oXHR) {
				setLoading($("div_form2"));
			},
			onSuccess: function(oXHR) {
				$("joint_universities").update(oXHR.responseText);
				removeLoading($("div_form2"));
			},
			onFailure : function(oXHR) {
				alert("Възникна грешка при опит за прочитане на съвместните университети свързани с този вид диплома. Моля опитайте по-късно..." + oXHR.statusText);
				$("joint_universities").update("");
				removeLoading($("div_form2"));
			},
			method: 'GET'
		});
	}
}
function updateEduLevelName(select) {
	var diplomaTypeId = select.options[select.selectedIndex].value;
	if (diplomaTypeId == '-') {
		$("edu_level_name").update("");
		$("original_edu_level_name").update("");
		$("original_edu_level_name_translated").update("");
	} else {
		new Ajax.Request('/nacid/control/diploma_type_ajax/view', {
			method: 'post',
			asynchronous: false,
			parameters: {
				id: diplomaTypeId
			},
			onSuccess: function(transport) {
				var response = transport.responseText || "no response text";
				var json = response.evalJSON(true);

				$("edu_level_name").update(json.educationLevelName);
				$("original_edu_level_name").update(json.originalEducationLevelName);
				$("original_edu_level_name_translated").update(json.originalEducationLevelTranslatedName);

			},
			onFailure: function() {
				alert('Error');
			}
		});
	}
}
function universityFacultyChanged() {
    $('base_university_faculty_id').value = "";
}
function toggleEducationPeriodInformation(hasEducationPeriodInformation) {
    if (!hasEducationPeriodInformation) {
        $("training_duration").disable();
        $("training_duration_unit_id").disable();
        var tf = document.getElementsByName("training_form");
        for (var i = 0; i < tf.length; i++) {
            $(tf[i]).disable();
        }
        $("credits").disable();
        $("ectsCredits").disable();
        $("creditHours").disable();
        $("training_form_other").disable();
    } else {
        $("training_duration").enable();
        $("training_duration_unit_id").enable();
        var tf = document.getElementsByName("training_form");
        for (var i = 0; i < tf.length; i++) {
            $(tf[i]).enable();
        }
        $("credits").enable();
        $("ectsCredits").enable();
        $("creditHours").enable();
        $("training_form_other").enable();
    }
}

//Vika se pri promqna na nqkoi ot inputite na universitet
function universityChanged(type) {
	var university_id = $('base_university_id').value;; 
	if (university_id != "") {
		$('base_university_id').value = "";
	  $('base_university_country_id').value = "-";
	  $('base_university_city').value = "";
	  $('base_university_address').value = "";
	  $('base_university_faculty_id').value = "";
      $('base_university_faculty').value = "";
      $('base_university_generic_name').value = "";
      $('base_university_generic_name_id').value = "";
	  if (type == 'bg') {
		    $('base_university_original_name').value = "";
	  } else {
		    $('base_university_name_bg').value = "";
      }
	  updateDiplomaType(null);
	}
    showHideEditUniversityButton();
}
  function addTrainingLocation() {
	  var el = $('training_location').clone(true);
	    el.id = el.id + trainingLocations;
	    $('specialities_div').insert({before:el});
	    el.show();
	    /* Preobarzuva ID-tata i name=ovete na inputite/selectovete/textbox-a, kato im slaga po edno jointUniversityId v kraq*/
	    $$('#training_location' + trainingLocations + ' input', '#training_location' + trainingLocations + ' select', '#training_location' + trainingLocations + ' textarea').each(function(element) {
	        element.name = element.name + trainingLocations;
	        if (element.id != null) {
	          element.id = element.id + trainingLocations ;
	        }
	    });
	    /*Preobrazuva href-a na linka "premahni"*/
	    $$('#training_location' + trainingLocations + ' a[href="javascript:void(0);"]')[0].setAttribute('href', "javascript:$('training_location" + trainingLocations + "').remove();void(0);");
	    trainingLocations++;
	    $('training_locations_count').value = trainingLocations;
  }
  function additionalInputsValidation() {
	  var ret = true;
	  if ($('base_university_id').value == "") {
		   setErrorOnInput($('base_university_name_bg'), "Трябва да въведете чуждестранно висше училище!");
		   ret = false;
	  }
	  //Ako vse pak nqkoi e vyvel text v trainingSpeciality, no ne e izbral opciq ot padashtoto menu
	  //RayaChanged--------------------------------------------------------------------------
	  
	  if ($('trainingQualification').value != "" && $('trainingQualificationId').value == "") {
          setErrorOnInput($('trainingQualification'), "Трябва да изберете квалификация от падащото меню!");
          ret = false;
      }	  
	  //RayaWritten----------------------------------------------------------
	  if(!specialityAdditionalInputValidation('trainingSpeciality')){
		  ret = false;
	  }
	  if(!specialityAdditionalInputValidation('prevDiplomaSpeciality')){
		  ret=false;
	  }
	  //------------------------------------------------------------------------------
		return ret;
  }
  var hasEducationPeriodInformation = ${has_education_period_information};

  
</script>

	<h3 class="names">${application_header }</h3>
	<nacid:systemmessage name="trainingCourseStatusMessage"/>
	<v:form name="appform2" action="${pathPrefix }/control/applications/save" method="post" additionalvalidation="additionalInputsValidation()"
	  backurl="${pathPrefix }/control/${back_screen}/list?application_type=${application_type}&getLastTableState=1">
		<v:dateValidator input="diploma_date" format="d.m.yyyy" required="false" beforeToday="true" />
		<v:textValidator input="diploma_number" maxLength="15" required="false"/>
		<v:dateValidator input="training_start" format="yyyy" emptyString="гггг" required="false" beforeToday="true"  />
		<v:dateValidator input="training_end" format="yyyy" emptyString="гггг" required="false" beforeToday="true"  />
		<v:dateValidator input="thesis_defence_date" format="d.m.yyyy" emptyString="дд.мм.гггг" required="false" beforeToday="true"  />
		<v:textValidator input="thesis_bibliography" regex="/^\d+$/" required="false" />
		<v:textValidator input="thesis_volume" regex="/^\d+$/" required="false" />

		<v:dateIntervalValidator format="yyyy" dateTo="training_end" dateFrom="training_start" />
		<v:dateIntervalValidator format="yyyy" dateTo="diploma_date" dateFrom="training_end" endFormat="d.m.yyyy" errormessage="${messages.trainig_date_end_before_diploma_date}" dontAllowEquals="true"/>
		<v:dateIntervalValidator format="yyyy" endFormat="d.m.yyyy" dateTo="diploma_date" dateFrom="training_start" errormessage="${messages.trainig_date_start_before_diploma_date}" />
		
		<v:dateIntervalValidator format="yyyy" dateTo="diploma_date" dateFrom="prevDiplGraduationDate" endFormat="d.m.yyyy"  errormessage="${messages.prevDiplDateBeforeDiplDate}" dontAllowEquals="false"/>
		<v:dateIntervalValidator format="yyyy" dateTo="diploma_date" dateFrom="schoolGraduationDate" endFormat="d.m.yyyy"  errormessage="${messages.schoolDateBeforeDiplDate}" dontAllowEquals="false"/>
		
		
		<v:textValidator input="training_duration" regex="/^(\d+\.?\d*|\.\d+)$/" maxLength="10" required="false" />
		<v:textValidator input="credits" regex="/^(\d+|\d+\.\d{0,2}|\.\d{1,2})$/" maxLength="10" required="false" />
		<v:textValidator input="creditHours" regex="/^\d+$/" maxLength="10" required="false" />
		<v:textValidator input="graduation_way_other" maxLength="255" required="$('gw_other').checked" />
		<v:textValidator input="training_form_other" maxLength="255" required="!$('tf_other').disabled && $('tf_other').checked" />
		<v:textValidator input="recognition_purpose_other" maxLength="255" required="$('rp_other').checked" />
    	<v:checkBoxValidator input="graduation_way" required="false" />
   		<v:checkBoxValidator input="recognition_purpose" required="false" />
    	<v:textValidator input="training_city0" maxLength="30" required="false"/>
    	<v:textValidator input="schoolName" maxLength="255" required="false" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}"/>
    	<v:comboBoxValidator input="training_country0" required="false" errormessage="Трябва да въведете държава!"/>
    	<v:comboBoxValidator input="diploma_type" required="false" />
		<input type="hidden" name="id" value="${application_id }" />

		
		<div class="clr"><!--  --></div>
		<nacid:trainingCourseUniversity type="base">
		<fieldset><legend>Данни за чуждестранното ${fn:toLowerCase(universityLabel)} </legend>
            <p id="university_new" class="flt_rgt">
                <a id="university_edit_lnk" onclick="showHideNewUniversityDiv(true, this.up(0).next(0), 1, $('base_university_id').value, 'baseUniversitySaved(json)');" href="javascript:void(0);" style="">Редакция</a>&nbsp;
                <a id="university_new_lnk" onclick="showHideNewUniversityDiv(false, this.up(0).next(0), 1, 0, 'baseUniversitySaved(json)');" href="javascript:void(0);" style="">Нов</a>
            </p>
            <div class="clr"><!--  --></div>
            <v:universityinput countryComboAttribute="newUniversityCountry" universityLabel="${universityLabel}"/>
			<v:universityfacultyinput />
            <p>
                <span class="fieldlabel"><label for="base_university_original_name">Оригинално наименование</label></span>
				<v:textinput class="brd w600" name="base_university_original_name" id="base_university_original_name"  value="${university_original_name }" onkeydown="if (!isSpecialKeyPressed(event)) universityChanged('en');" additionaltext="${isDoctorateApplicationType ? 'disabled=\"disabled\"' : ''}" />
            </p>
            <div class="clr"><!--  --></div>
            <p>
				<span class="fieldlabel"><label for="base_university_name_bg">Наименование на български</label></span> 
				<input type="hidden" name="base_university_id" id="base_university_id" value="${university_id }" />
				<v:textinput class="brd w600" name="base_university_name_bg" id="base_university_name_bg"  value="${university_name_bg }" onkeydown="if (!isSpecialKeyPressed(event)) universityChanged('bg');" additionaltext="${isDoctorateApplicationType ? 'disabled=\"disabled\"' : ''}" />
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label for="base_university_generic_name">Генерично наименование</label></span>
				<input type="hidden" name="base_university_generic_name_id" id="base_university_generic_name_id"  value="${university_generic_name_id }"/>
				<v:textinput disabled="disabled" class="brd w500" maxlength="255" name="base_university_generic_name" id="base_university_generic_name"  value="${university_generic_name }" />
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label for="base_university_faculty">Факултет</label></span>
				<input type="hidden" name="base_university_faculty_id" id="base_university_faculty_id" value="${university_faculty_id }" />
				<v:textinput class="brd w600" name="base_university_faculty" id="base_university_faculty"  value="${university_faculty_name}" onkeydown="if (!isSpecialKeyPressed(event)) universityFacultyChanged();" disabled="disabled" />
			</p>
			<div class="clr"><!--  --></div>

			<p>
				<span class="fieldlabel"><label for="base_university_country_id">Държава</label></span>
				<nacid:combobox disabled="disabled" name="base_university_country_id" id="base_university_country_id" attributeName="universityCountry" style="w308 brd" />
			</p>
			<div class="clr"><!--  --></div>
			<p><span class="fieldlabel"><label for="base_university_city">Град</label></span>
				<v:textinput disabled="disabled" class="brd w500" maxlength="100" name="base_university_city" id="base_university_city"  value="${university_city }" />
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label for="base_university_address">Адрес за кореспонденция</label></span> 
				<textarea disabled="disabled" class="brd w600" rows="3" cols="40" name="base_university_address" id="base_university_address">${university_address }</textarea>
			</p>
			<p id="base_university_url_diploma_register_container" style="display: ${empty university_url_diploma_register_link ? 'none' : 'block'}">
				<span class="fieldlabel flt_lft"><label for="base_university_url_diploma_register">Официален регистър за дипломи</label></span>
				<span id="base_university_url_diploma_register" class="flt_lft">${university_url_diploma_register_link }</span>
			</p>

			<div class="clr"><!--  --></div>
		</fieldset>
		<div class="clr20"><!--  --></div>
		<script type="text/javascript">
	        new Autocomplete('base_university_name_bg', { 
	              serviceUrl:'${pathPrefix}/control/universitysuggest?nametype=1',
	              width:600,
	              onSelect:updateUniversity
	        });
	        new Autocomplete('base_university_original_name', { 
	            serviceUrl:'${pathPrefix}/control/universitysuggest?nametype=2',
	            width:600,
	            onSelect:updateUniversity
	      });
            new Autocomplete('base_university_faculty', {
                serviceUrl:'${pathPrefix}/control/universityfacultysuggest',
                width:600,
                onSelect:updateUniversityFaculty,
                dynamicParameters: function (){
                    return {university_id : $('base_university_id').value};
                }
            });
 		</script>
		</nacid:trainingCourseUniversity>
  		<fieldset><legend>Данни за дипломата</legend>
            <p>
                <span class="fieldlabel"><label for="diploma_series">Серия:</label></span>
                <v:textinput class="brd w308" maxlength="15" name="diploma_series" id="diploma_series"  value="${diploma_series }" />
            </p>
            <div class="clr"><!--  --></div>
            <p>
      			<span class="fieldlabel"><label for="diploma_number">Номер:</label></span>
      			<v:textinput class="brd w308" maxlength="15" name="diploma_number" id="diploma_number"  value="${diploma_number }" />
    		</p>
            <div class="clr"><!--  --></div>
            <p>
                <span class="fieldlabel"><label for="diploma_registration_number">Регистрационен номер:</label></span>
                <v:textinput class="brd w308" maxlength="15" name="diploma_registration_number" id="diploma_registration_number"  value="${diploma_registration_number }" />
            </p>
            <div class="clr"><!--  --></div>
            <p>
      			<span class="fieldlabel"><label for="diploma_date">Дата:</label><br /></span>
      			<v:dateinput name="diploma_date" id="diploma_date" value="${diploma_date }" style="brd w308" />
    		</p>

    		<v:diplomatype id="diploma_type" name="diploma_type" eduLevelCombo="eduLevelsCombo" countryIdInput="$('base_university_country_id')" universityIdInput="$('base_university_id')" universityFacultyIdInput="$('base_university_faculty_id')"
    			countryComboAttribute="universityCountry" comboAttribute="diplomaTypeCombo" onchange="updateEduLevelName(this);updateJointUniversities(this)" originalEduLevel="originalEduLevelCombo" isDoctorate="${isDoctorateApplicationType}" universityLabel="${universityLabel}"/>
    		
  		</fieldset>
  		<div class="clr10"><!--  --></div>
  		<div id="joint_universities">
  			<nacid:trainingCourseUniversity type="joint">
				<div id="joint_university${id }" style="display: block;">
				<fieldset><legend>Данни за Съвместно чуждестранно висше училище</legend>
					<div class="clr">&nbsp;</div>
                    <p>
                        <span class="fieldlabel"><label for="joint_university_original_name${id }">Оригинално наименование</label></span>
                        <v:textinput disabled="disabled" class="brd w600" name="joint_university_original_name${id }" id="joint_university_original_name${id }"  value="${university_original_name }" />
                    </p>
                    <div class="clr"><!--  --></div>
                    <p>
						<span class="fieldlabel"><label for="joint_university_name_bg${id }">Наименование на български</label></span> 
						<input type="hidden" name="joint_university_id${id }" id="joint_university_id${id }" value="${university_id }" />
						<v:textinput disabled="disabled" class="brd w600" name="joint_university_name_bg${id }" id="joint_university_name_bg${id }"  value="${university_name_bg }" />
					</p>
					<div class="clr"><!--  --></div>
					<p>
						<span class="fieldlabel"><label for="joint_university_generic_name${id}">Генерично наименование</label></span>
						<input type="hidden" name="joint_university_generic_name_id${id}" id="joint_university_generic_name_id${id}"  value="${university_generic_name_id }"/>
						<v:textinput disabled="disabled" class="brd w500" maxlength="255" name="joint_university_generic_name${id}" id="joint_university_generic_name${id}"  value="${university_generic_name }" />
					</p>
					<div class="clr"><!--  --></div>
					<p>
						<span class="fieldlabel"><label for="joint_university_faculty${id }">Факултет</label></span>
						<input type="hidden" name="joint_university_faculty_id${id }" id="joint_university_faculty_id${id }" value="${university_faculty_id }" />
						<v:textinput disabled="disabled" class="brd w600" name="joint_faculty${id }" id="joint_university_faculty${id }"  value="${university_faculty_name }" />
					</p>
					<div class="clr"><!--  --></div>
					<p>
						<span class="fieldlabel"><label for="joint_university_country_id${id }">Държава</label></span>
						<nacid:combobox disabled="disabled" name="joint_university_country_id${id }" id="joint_university_country_id${id }" attributeName="universityCountry" style="w308 brd" />
					</p>
					<div class="clr"><!--  --></div>
					<p>
						<span class="fieldlabel"><label for="joint_university_city${id }">Населено място</label></span>
						<v:textinput disabled="disabled" class="brd w500" name="joint_university_city${id }" id="joint_university_city${id }"  value="${university_city }" />
					</p>
					<div class="clr"><!--  --></div>
					<p>
						<span class="fieldlabel"><label for="joint_university_address${id }">Адрес за кореспонденция</label></span>
						<textarea disabled="disabled" class="brd w600" rows="3" cols="40" name="joint_university_address${id }" id="joint_university_address${id }">${university_address }</textarea>
					</p>
					<div class="clr"><!--  --></div>
					<p id="joint_university_url_diploma_register_container${id}" style="display: ${empty university_url_diploma_register_link ? 'none' : 'block'}">
						<span class="fieldlabel flt_lft"><label for="joint_university_url_diploma_register${id}">Официален регистър за дипломи</label></span>
						<span id="joint_university_url_diploma_register${id}" class="flt_lft">${university_url_diploma_register_link }</span>
					</p>
				</fieldset>
				</div>
			</nacid:trainingCourseUniversity>
		</div>
		<fieldset id="training"><legend>Обучение</legend>
        <div class="clr"><!--  --></div>
		<nacid:trainingCourseTrainingLocationEdit type="other">
		<div id="training_location${row_id }">
			<fieldset><legend>Място на провеждане</legend>
				<input type="hidden" name="training_location_id${row_id }" value="${training_location_id}"/>
				<c:if test="${row_id != 0}">
				    <p class="flt_rgt"><a href="javascript:$('training_location${row_id }').remove();void(0);" style="color: red">Премахни</a></p>    
				</c:if>
				<p>
				  <span class="fieldlabel"><label for="training_country${row_id }">Държава</label></span>
				  <nacid:combobox name="training_country${row_id }" id="training_country${row_id }" attributeName="trainingCountry" style="w308 brd" />
				</p>
				<div class="clr"><!--  --></div>
				<p>
				  <span class="fieldlabel"><label for="training_city${row_id }">Град</label></span>
				  <v:textinput class="brd w500" name="training_city${row_id }"  value="${training_location_city }" maxlength="30" />
				</p>
			</fieldset>
		</div>
		</nacid:trainingCourseTrainingLocationEdit>
		<div class="clr10"><!--  --></div>
		<p class="flt_rgt"><a href="javascript:addTrainingLocation();">Добави място на провеждане</a></p>
		<div class="clr"><!--  --></div>
		<nacid:trainingCourseTrainingLocationEdit type="empty">
		<div id="training_location" style="display:none;">
            <fieldset><legend>Място на провеждане</legend>
                <p class="flt_rgt"><a href="javascript:void(0);" style="color: red">Премахни</a></p>
                <p>
                  <span class="fieldlabel"><label for="training_country">Държава</label></span>
                  <nacid:combobox name="training_country" id="training_country" attributeName="trainingCountry" style="w308 brd" />
                </p>
                <div class="clr"><!--  --></div>
                <p>
                  <span class="fieldlabel"><label for="training_city">Град</label></span>
                  <v:textinput class="brd w500" name="training_city"  value=""  maxlength="30" />
                </p>
            </fieldset>
        </div>
        </nacid:trainingCourseTrainingLocationEdit>
		<div class="clr"><!--  --></div>
		<div id="specialities_div" style="${hideOnlyForDoctorateStyle}">
            <!-- RayaWritten -->
            <input type="hidden" name="trainingSpecialityId" id="trainingSpecialityId" value="${trainingSingleSpecialityId }" />
            <input type="hidden" name="original_trainingSpecialityId" id="original_trainingSpecialityId" value="${originalTrainingSingleSpecialityId }" />
            <v:specialityinput id="trainingSpeciality" value="${trainingSingleSpeciality }" originalSpecialityValue="${originalTrainingSingleSpeciality }"  comboAttribute="professionGroupCombo" class="brd w450"
                chooseMultiple="true" specialityIdInput="$('trainingSpecialityId')" originalSpecialityIdInput="$('original_trainingSpecialityId')" specialityList="${trainingSpecialities}"
                originalSpecialityList="${trainingOriginalSpecialities}"
                specialityOnkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('trainingSpeciality');"
                originalSpecialityOnkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('original_trainingSpeciality');"
                addOriginalSpeciality="true" />

            <!-- End of RayaWritten -->
            <script type="text/javascript">
                    new Autocomplete('trainingSpeciality', {
                          serviceUrl:'${pathPrefix}/control/specialitysuggest?only_active=1',
                          width:600,
                          onSelect:updateNomenclature
                    });
                    new Autocomplete('original_trainingSpeciality', {
                        serviceUrl:'${pathPrefix}/control/originalspecialitysuggest?only_active=1',
                        width:600,
                        onSelect:updateNomenclature
                    });
            </script>
        </div>
        <div class="clr10"><!--  --></div>
		<p><span class="fieldlabel"><label for="training_start">Начало на обучението</label><br />
		</span> <v:dateinput name="training_start" id="training_start" value="${training_start }" style="brd w100" emptyString="гггг" /></p>
		<div class="clr"><!--  --></div>

		<p><span class="fieldlabel"><label for="training_end">Край на обучението</label><br />
		</span> <v:dateinput name="training_end" id="training_end" value="${training_end }" style="brd w100" emptyString="гггг" /></p>
		<div class="clr"><!--  --></div>

		<p><span class="fieldlabel">
			<label for="training_duration">Продължителност</label></span>
			<v:textinput class="brd w50" maxlength="10" name="training_duration" value="${training_duration }" id="training_duration"/>
			<label for="training_duration_unit_id">Тип</label>
			<nacid:combobox name="training_duration_unit_id" id="training_duration_unit_id" attributeName="trainingDurationUnit" style="w100 brd" />
		</p>

		<div class="clr"><!--  --></div>

		<div class="flt_lft w_prcnt50">
		<div class="flt_lft fieldlabel"><b>Форма на обучение</b></div>
		<div class="flt_lft"><nacid:trainingCourseTrainingFormEdit>
			<input name="training_form" id="tf_${id }" value="${value_id }" onclick="${onclick}" type="radio" ${checked }/>${label } <br />
		</nacid:trainingCourseTrainingFormEdit> <nacid:trainingCourseTrainingFormEdit input="true">
			<v:textinput name="training_form_other" id="training_form_other" style="${style }" class="brd w200" maxlength="255"  value="${value }" />
		</nacid:trainingCourseTrainingFormEdit></div>
		<div class="clr"><!--  --></div>
		</div>

		<div class="flt_lft w_prcnt49">
		<div class="flt_lft fieldlabel"><b>Начин на дипломиране /придобиване на степента/</b></div>
		<div class="clr"><!--  --></div>
		<div class="flt_lft">
            <nacid:trainingCourseGraduationWayEdit>
			    <input name="graduation_way" value="${value_id }" onclick="${onclick}" type="checkbox" ${checked } id="gw_${id }" />${label }<br />
		    </nacid:trainingCourseGraduationWayEdit>
            <nacid:trainingCourseGraduationWayEdit input="true">
			    <v:textinput name="graduation_way_other" id="graduation_way_other" style="${style }" class="brd w210" maxlength="255"  value="${value }" />
		    </nacid:trainingCourseGraduationWayEdit>
        </div>
		<div class="clr"><!--  --></div>
		</div>
		<div class="clr10"><!--  --></div>

		<p>
            <span class="fieldlabel"><label for="credits">Придобити национални кредити</label></span>
            <v:textinput class="brd w50" name="credits" id="credits"  value="${credits}" maxlength="10" />
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel"><label for="ectsCredits">Придобити ECTS кредити</label></span>
            <v:textinput class="brd w50" name="ectsCredits" id="ectsCredits"  value="${ectsCredits}" maxlength="10" />
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel"><label for="creditHours">Хорариум</label></span>
            <v:textinput class="brd w50" name="creditHours" id="creditHours"  value="${creditHours}" maxlength="10" />
        </p>
			<c:if test="${!isDoctorateApplicationType}">
				<div class="clr"><!--  --></div>
				<p>
      				<span class="fieldlabel"><label>Придобита степен - оригинал</label></span><span id="original_edu_level_name">${original_edu_level_name}</span>
				</p>
				<div class="clr"><!--  --></div>
				<p>
	  				<span class="fieldlabel"><label>Придобита степен - превод</label></span><span id="original_edu_level_name_translated">${original_edu_level_name_translated }</span>
				</p>
	  			<div class="clr"><!--  --></div>
				<p>
	  				<span class="fieldlabel"><label>Образователна степен</label></span><span id="edu_level_name">${edu_level_name }</span>
				</p>
				<div class="clr"><!--  --></div>
				<p>
					<span class="fieldlabel"><label for="prof_group">Професионално направление по заявление</label></span>
					<nacid:combobox name="prof_group" id="prof_group" attributeName="profGroupCombo" style="w508 brd" onchange="updateProfGroupEduArea(this)"/>
				</p>
				<div class="clr"><!--  --></div>
				<p>
					<span class="fieldlabel"><label for="prof_group_edu_area_id">Област на образованието</label></span>
					<nacid:combobox name="prof_group_edu_area_id" id="prof_group_edu_area_id" attributeName="profGroupEduAreaCombo" style="w508 brd" disabled="disabled"/>
				</p>
				<div class="clr"><!--  --></div>
				<v:flatNomenclatureSelect labelclass="fieldlabel" nomenclatureType="<%= Integer.toString(NomenclaturesDataProvider.FLAT_NOMENCLATURE_GRADUATION_DOCUMENT_TYPE) %>"
										  fieldName="Тип документ"
										  id="graduationDocumentTypeId" value="${graduationDocumentTypeId}" windowLevel="1"
										  nomenclatureValues="graduationDocumentTypeCombo" inputClass="brd w600" />
			</c:if>


        <div style="${hideOnlyForDoctorateStyle}">
			<v:hidden id="trainingQualificationId" name="trainingQualificationId" value="${trainingQualificationId}"/>
			<v:flatNomenclatureInput nomenclatureType="<%= Integer.toString(NomenclaturesDataProvider.FLAT_NOMENCLATURE_QUALIFICATION) %>"
									 fieldName="Придобита квалификация" onkeydown="if(!isSpecialKeyPressed(event)) resetField('trainingQualificationId');"
									 value="${trainingQualification}" onsuccess="updateTrainingQualificationId();"
									 id="trainingQualification" class="brd w600 flt_lft" maxLength="400" windowLevel="2"/>

			<v:hidden id="trainingOriginalQualificationId" name="trainingOriginalQualificationId" value="${trainingOriginalQualificationId}"/>
			<v:flatNomenclatureInput nomenclatureType="<%= Integer.toString(NomenclaturesDataProvider.FLAT_NOMENCLATURE_ORIGINAL_QUALIFICATION) %>"
									 fieldName="Придобита квалификация - оригинално наименование" onkeydown="if(!isSpecialKeyPressed(event)) resetField('trainingOriginalQualificationId');"
									 value="${trainingOriginalQualification}" onsuccess="updateTrainingOriginalQualificationId()"
									 id="trainingOriginalQualification" class="brd w600 flt_lft" maxLength="400" windowLevel="2"/>
        </div>
        <div class="clr"><!--  --></div>

		</fieldset>
		<div id="thesis_div" style="${showOnlyForDoctorateStyle}">
		<fieldset>
			<legend>Дисертация</legend>
			<p>
				<span class="fieldlabel"><label for="thesis_topic">Тема на дисертацията</label></span>
				<v:textinput id="thesis_topic" name="thesis_topic" value="${thesisTopic}" class="brd w500" />
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label for="thesis_topic_en">Тема на дисертацията на английски</label></span>
				<v:textinput id="thesis_topic_en" name="thesis_topic_en" value="${thesisTopicEn}" class="brd w500" />
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label for="thesis_defence_date">Дата на защитата</label></span>
				<v:dateinput id="thesis_defence_date" name="thesis_defence_date" value="${thesisDefenceDate}" class="brd w500" />
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label for="thesis_language">Език на основния текст</label></span>
				<nacid:combobox name="thesis_language" id="thesis_language" attributeName="thesisLanguageCombo" style="w508 brd"/>
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label for="thesis_bibliography">Библиография</label></span>
				<v:textinput id="thesis_bibliography" name="thesis_bibliography" value="${thesisBibliography}" class="brd w500" />
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label for="thesis_volume">Обем на дисертационния труд</label></span>
				<v:textinput id="thesis_volume" name="thesis_volume" value="${thesisVolume}" class="brd w500" />
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label for="thesis_annotation">Анотация на български език</label></span>
				<v:textarea rows="3" cols="40" id="thesis_annotation" name="thesis_annotation" value="${thesisAnnotation}" class="brd w500" />
			</p>
			<div class="clr"><!--  --></div>
			<p>
				<span class="fieldlabel"><label for="thesis_annotation_en">Анотация на английски език</label></span>
				<v:textarea rows="3" cols="40" id="thesis_annotation_en" name="thesis_annotation_en" value="${thesisAnnotationEn}" class="brd w500" />
			</p>
			<div class="clr"><!--  --></div>
			<c:if test="${isDoctorateApplicationType}">
				<div class="clr"><!--  --></div>
				<p>
					<span class="fieldlabel"><label>Придобита степен - оригинал</label></span><span id="original_edu_level_name">${original_edu_level_name}</span>
				</p>
				<div class="clr"><!--  --></div>
				<p>
					<span class="fieldlabel"><label>Придобита степен - превод</label></span><span id="original_edu_level_name_translated">${original_edu_level_name_translated }</span>
				</p>
				<div class="clr"><!--  --></div>
				<p>
					<span class="fieldlabel"><label>Образователна степен</label></span><span id="edu_level_name">${edu_level_name }</span>
				</p>
				<div class="clr"><!--  --></div>
				<p>
					<span class="fieldlabel"><label for="prof_group">Професионално направление по заявление</label></span>
					<nacid:combobox name="prof_group" id="prof_group" attributeName="profGroupCombo" style="w508 brd" onchange="updateProfGroupEduArea(this)"/>
				</p>
				<div class="clr"><!--  --></div>
				<p>
					<span class="fieldlabel"><label for="prof_group_edu_area_id">Област на образованието</label></span>
					<nacid:combobox name="prof_group_edu_area_id" id="prof_group_edu_area_id" attributeName="profGroupEduAreaCombo" style="w508 brd" disabled="disabled"/>
				</p>
				<div class="clr"><!--  --></div>
				<v:flatNomenclatureSelect labelclass="fieldlabel" nomenclatureType="<%= Integer.toString(NomenclaturesDataProvider.FLAT_NOMENCLATURE_GRADUATION_DOCUMENT_TYPE) %>"
										  fieldName="Тип документ"
										  id="graduationDocumentTypeId" value="${graduationDocumentTypeId}" windowLevel="1"
										  nomenclatureValues="graduationDocumentTypeCombo" inputClass="brd w600" />
			</c:if>
		</fieldset>
		</div>

		<div id="school_diploma_div" style="${hideOnlyForDoctorateStyle}">
			<fieldset>
				<legend>Диплома за средно образование</legend>
				<p><span class="fieldlabel"><label for="schoolCountryId">Държава</label><br /></span>
					<nacid:combobox name="schoolCountryId" id="schoolCountryId" attributeName="schoolCountryCombo" style="w308 brd" />
				</p>
				<div class="clr"><!--  --></div>

				<p><span class="fieldlabel"><label for="schoolCity">Град</label><br /></span>
					<v:textinput class="brd w500" name="schoolCity"  value="${schoolCity }" maxlength="100"/>
				</p>
				<div class="clr"><!--  --></div>

				<p><span class="fieldlabel"><label for="schoolName">Училище</label><br /></span>
					<v:textinput class="brd w600" name="schoolName"  value="${schoolName }" maxlength="255"/>
				</p>
				<div class="clr"><!--  --></div>

				<p><span class="fieldlabel"><label for="schoolGraduationDate">Година на дипломиране</label><br /></span>
					 <v:dateinput name="schoolGraduationDate" id="schoolGraduationDate" value="${schoolGraduationDate }" style="brd w100" emptyString="гггг" />
				</p>
				<div class="clr"><!--  --></div>

				<p><span class="fieldlabel"><label for="schoolNotes">Бележки</label><br /></span>
					<textarea class="brd w600" rows="3" cols="40" name="schoolNotes" id="schoolNotes">${schoolNotes }</textarea>
				</p>
				<div class="clr"><!--  --></div>
			</fieldset>
		</div>

		<div id="prev_diploma_div">
			<fieldset>
				<legend>Диплома за предходна образователна степен за висше образование</legend>

				<nacid:trainingCourseUniversity type="prevDipl">

					<p><span class="fieldlabel"><label for="prevDiplName">Висше училище</label><br /></span>
						<v:textinput id="prevDiplName" class="brd w600" name="prevDiplName"
								value="${university_name_bg }" onkeydown="if(!isSpecialKeyPressed(event)) prevDiplUniversityChanged();" />
						<input type="hidden" value="${university_id }" id="prevDiplUniversityId" name="prevDiplUniversityId"/>
					</p>
					<div class="clr"><!--  --></div>

					<p><span class="fieldlabel"><label for="prevDiplCountryId">Държава</label><br /></span>
						<nacid:combobox disabled="disabled" name="prevDiplCountryId" id="prevDiplCountryId" attributeName="universityCountry" style="w308 brd" />
					</p>
					<div class="clr"><!--  --></div>

					<p><span class="fieldlabel"><label for="prevDiplCity">Град</label><br /></span>
						<v:textinput disabled="disabled" id="prevDiplCity" class="brd w500" name="prevDiplCity"  value="${university_city }" />
					</p>
					<div class="clr"><!--  --></div>
				</nacid:trainingCourseUniversity>
				<script type="text/javascript">
					new Autocomplete('prevDiplName', {
							serviceUrl:'/nacid/control/universitysuggest?nametype=1',
							width:600,
							onSelect:updatePrevDiplUniversity
					});

					function updatePrevDiplUniversity(value, data, el) {

						var id = el.id;
						var d = data.evalJSON(true);
						$('prevDiplUniversityId').value = d.id;
						$('prevDiplCountryId').value = d.countryId;
						$('prevDiplCity').value = d.city;
						$('prevDiplName').value = d.bgName;
					}

					function prevDiplUniversityChanged() {
						$('prevDiplCountryId').value = '-';
						$('prevDiplCity').value = '';
						$('prevDiplUniversityId').value = '';
					}
				</script>

				<p><span class="fieldlabel"><label for="prevDiplEduLevelId">Придобита степен</label><br /></span>
					<nacid:combobox name="prevDiplEduLevelId" id="prevDiplEduLevelId" attributeName="prevDiplEduLevelCombo" style="w308 brd" />
				</p>
				<div class="clr"><!--  --></div>

				<!-- RayaWritten -->
				<input type="hidden" name="prevDiplomaSpecialityId" id="prevDiplomaSpecialityId" value="${prevDiplomaSpecialityId }" />
				<v:specialityinput id="prevDiplomaSpeciality" value="${prevDiplomaSpeciality}" comboAttribute="professionGroupCombo" chooseMultiple="false" class="brd w450"
					specialityIdInput="$('prevDiplomaSpecialityId')" specialityOnkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('prevDiplomaSpeciality');"/>
				<!-- End of RayaWritten -->
				<script type="text/javascript">
						new Autocomplete('prevDiplomaSpeciality', {
							  serviceUrl:'${pathPrefix}/control/specialitysuggest?only_active=1',
							  width:600,
							  onSelect:updateNomenclature
						});
				</script>
				<div class="clr"><!--  --></div>

				<p><span class="fieldlabel"><label for="prevDiplGraduationDate">Година на дипломиране</label><br /></span>
					 <v:dateinput name="prevDiplGraduationDate" id="prevDiplGraduationDate" value="${prevDiplGraduationDate }" style="brd w100"
							emptyString="гггг" />
				</p>
				<div class="clr"><!--  --></div>

				<p><span class="fieldlabel"><label for="prevDiplNotes">Бележки</label><br /></span>
					<textarea class="brd w600" rows="3" cols="40" name="prevDiplNotes" id="prevDiplNotes">${prevDiplNotes }</textarea>
				</p>
				<div class="clr"><!--  --></div>
			</fieldset>
		</div>

		<div id="purpose_div" style="${hideOnlyForDoctorateStyle}">
		<fieldset><legend>Цел на признаването</legend>
		<div class="flt_lft">
		<div class="flt_lft w210">&nbsp;<!--  --></div>
		<div class="flt_lft"><nacid:applicationRecognitionPurposeEdit>
			<input ${checked } name="recognition_purpose" value="${value_id }" onclick="${onclick}" type="checkbox" id="rp_${id }" />${label } <br />
		</nacid:applicationRecognitionPurposeEdit> <nacid:applicationRecognitionPurposeEdit input="true">
			<v:textinput class="brd w600" name="recognition_purpose_other" style="${style }" id="recognition_purpose_other" maxlength="255" 
				value="${value }" />
		</nacid:applicationRecognitionPurposeEdit></div>
		<div class="clr"><!--  --></div>
		</div>
		<div class="clr10"><!--  --></div>

		</fieldset>
		</div>
		
		<fieldset><legend>Бележки</legend>
              <p><span class="fieldlabel2"><label for="notes">Бележки към зявлението</label></span>
              <textarea class="brd w600" rows="3" cols="40" name="notes" id="notes" disabled="disabled">${notes }</textarea></p>
        </fieldset>
		<input id="training_locations_count" name="training_locations_count" value="${training_locations_size }" type="hidden" />
		<input id="trainingSpeciality_specialities_count" name="trainingSpeciality_specialities_count" value="${specialities_count }" type="hidden" />
		<input name="activeForm" value="2" type="hidden" />
	</v:form>
    <%@ include file="similar_diplomas.jsp"%>

</nacid:trainingCourseEdit>


<script type="text/javascript">
	updateJointUniversities($("diploma_type"));
	function showHideEditUniversityButton() {
        if ($('base_university_id').value == '') {
            $('university_edit_lnk').hide();
        } else {
            $('university_edit_lnk').show();
        }
    }
    showHideEditUniversityButton();

    function baseUniversitySaved(json) {
        if (parseInt(json.id) > 0) {
            $('university_new_correct_message_div').show();
            $('university_new_error_message_div').hide();

			updateBaseUniversityFields(json);

            updateDiplomaType(json.id);
        } else {
            $('university_new_correct_message_div').hide();
            $('university_new_error_message_div').show();
        }
    }
    toggleEducationPeriodInformation(hasEducationPeriodInformation);
	function updateTrainingOriginalQualificationId() {
		updateFlatNomenclatureId('trainingOriginalQualification');
	}
	function updateTrainingQualificationId() {
		updateFlatNomenclatureId('trainingQualification');
	}
	function updateFlatNomenclatureId(id) {
		$(id + 'Id').value = $('record_id_' + id).innerHTML;
	}
</script>