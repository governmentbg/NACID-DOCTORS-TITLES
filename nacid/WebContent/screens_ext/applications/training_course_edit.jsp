<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<nacid_ext:extTrainingCourseEdit>
<script type="text/javascript">
var trainingLocations = ${training_locations_size};
var universitiesCount = ${universities_count};
var specialitiesCount = ${specialities_count};
var hasEducationPeriodInformation = ${has_education_period_information};
var doctorOfScienceEduLevel = ${doctorOfScienceId};


function addTrainingLocation() {
    var el = $('training_location').clone(true);
      el.id = el.id + trainingLocations;
      $('speciality').insert({before:el});
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
function addJointUniversity() {
    var el = $('university').clone(true);
    el.id = el.id + universitiesCount;
    $('diploma_data').insert({before:el});
    el.show();
    /* Preobarzuva ID-tata i name=ovete na inputite/selectovete/textbox-a, kato im slaga po edno jointUniversityId v kraq*/
    $$('#university' + universitiesCount+ ' input', '#faculty' + universitiesCount+ ' input').each(function(element) {
        element.name = element.name + universitiesCount;
        if (element.id != null) {
          element.id = element.id + universitiesCount;
        }
    });
    /*Preobrazuva href-a na linka "premahni"*/
    $$('#university' + universitiesCount+ ' a[href="javascript:void(0);"]')[0].setAttribute('href', "javascript:$('university" + universitiesCount+ "').remove();void(0);");

    new Autocomplete('universityName' + universitiesCount, { 
        serviceUrl:'${pathPrefix}/control/universitysuggest',
        width:600,
        onSelect:updateUniversity
      });
    var uc = universitiesCount;
	new Autocomplete('facultyName' + universitiesCount, {
		serviceUrl:'${pathPrefix}/control/universityfacultysuggest',
		width:600,
		onSelect:updateUniversityFaculty,
		dynamicParameters: function (){
			return {university_id : $('universityId' + uc).value};
		}
	});
    universitiesCount++;
    $('universities_count').value = universitiesCount;
}
function updateUniversity(value, data, el) {
	
    var id = el.id;
    var d = data.evalJSON(true);

    if(id == 'prevDiplName') {
        $('prevDiplUniversityId').value = d.id;
    }

    if(id.indexOf('universityName') > -1) {
        var uniId = id.replace('universityName','');
        $('universityId' + uniId).value = d.id;
        $('facultyId' + uniId).value = '';
        $('facultyName' + uniId).value = '';

    }
	        
}
function updateUniversityFaculty(value, data, el) {
	var id = el.id;
	var d = data.evalJSON(true);
	var uniId = id.replace('facultyName','');

	$('facultyName' + uniId).value = d.name;
	$('facultyId' + uniId).value = d.id;
}

function additionalInputsValidation() {
	if ($('trainingSpecialityId') != null && $('trainingSpecialityId').value == 'other') {
		return validateSpeciality();
	}
	else {
		return true;
	}
}
function toggleEducationPeriodInformation(hasEducationPeriodInformation) {
	if (!hasEducationPeriodInformation) {
		$$('label[for=trainingStart]')[0].removeClassName("required");
		$$('label[for=trainingEnd]')[0].removeClassName("required");
		$("trainingDuration").disable();
		$("trainingDurationUnitId").disable();
		var tf = document.getElementsByName("training_form");
		for (var i = 0; i < tf.length; i++) {
			$(tf[i]).disable();
			$(tf[i]).checked = false;
		}
		$("credits").disable();
		$("training_form_other").hide();
		$("training_form_other").disable();

		$("trainingDuration").value = '';
		$("trainingDurationUnitId").value = ''
		$("credits").value = '';
		$("training_form_other").value = '';

	} else {
		$$('label[for=trainingStart]')[0].addClassName("required");
		$$('label[for=trainingEnd]')[0].addClassName("required");
		$("trainingDuration").enable();
		$("trainingDurationUnitId").enable();
		var tf = document.getElementsByName("training_form");
		for (var i = 0; i < tf.length; i++) {
			$(tf[i]).enable();
		}
		$("credits").enable();
		$("training_form_other").enable();
	}
}
function toggleDissertationDiv(isShow) {
	if (isShow) {
		$("dissertation_div").show();
	} else {
		$("dissertation_div").hide();
		$("thesis_topic").value = "";
		$("thesis_topic_en").value = "";
		$("thesis_defence_date").value = "дд.мм.гггг";
		$("thesis_language").value = "-";
		$("thesis_bibliography").value = "";
		$("thesis_volume").value = "";
		$("thesis_annotation").value = "";
		$("thesis_annotation_en").value = "";
		// $("prof_group").value = "-";
		// $("prof_group_edu_area_id").value = "-";

	}
}
</script>

	<h3 class="title"><span>Информация за курса на обучение</span></h3>

	<nacid:systemmessage name="trainingCourseStatusMessage"/>
	<v:form name="appform2" action="${pathPrefix }/control/application/save" method="post"
	  	backurl="${pathPrefix }/control/application/list/${application_type}?getLastTableState=1" additionalvalidation="additionalInputsValidation()">
		
		<v:dateValidator input="diplomaDate" format="d.m.yyyy" required="false" beforeToday="true" />
		<v:textValidator input="diplomaSeries" maxLength="15" required="false"/>
		<v:textValidator input="diplomaNumber" maxLength="15" required="false"/>
		<v:textValidator input="diplomaRegistrationNumber" maxLength="15" required="false"/>
		<v:dateValidator input="trainingStart" format="yyyy" emptyString="гггг" required="false" beforeToday="true"  />
		<v:dateValidator input="trainingEnd" format="yyyy" emptyString="гггг" required="false" beforeToday="true"  />
		<c:if test="${isDoctorateApplicationType}">
			<v:dateValidator input="thesis_defence_date" format="d.m.yyyy" emptyString="дд.мм.гггг" required="false" beforeToday="true"  />
			<v:textValidator input="thesis_bibliography" regex="/^\d+$/" required="false" />
			<v:textValidator input="thesis_volume" regex="/^\d+$/" required="false" />
		</c:if>
		<v:dateValidator input="prevDiplGraduationDate" format="yyyy" emptyString="гггг" required="false" beforeToday="true"  />

		
		<v:dateIntervalValidator format="yyyy" dateTo="trainingEnd" dateFrom="trainingStart" />
		<v:dateIntervalValidator format="yyyy" dateTo="diplomaDate" dateFrom="trainingEnd" endFormat="d.m.yyyy" errormessage="${messages.trainig_date_end_before_diploma_date}" dontAllowEquals="true"/>
		<v:dateIntervalValidator format="yyyy" endFormat="d.m.yyyy" dateTo="diplomaDate" dateFrom="trainingStart" errormessage="${messages.trainig_date_start_before_diploma_date}" />
		
		<v:dateIntervalValidator format="yyyy" dateTo="diplomaDate" dateFrom="prevDiplGraduationDate" endFormat="d.m.yyyy"  errormessage="${messages.prevDiplDateBeforeDiplDate}" dontAllowEquals="false"/>
		<c:if test="${!isDoctorateApplicationType}">
			<v:dateValidator input="schoolGraduationDate" format="yyyy" emptyString="гггг" required="false" beforeToday="true"  />
			<v:dateIntervalValidator format="yyyy" dateTo="diplomaDate" dateFrom="schoolGraduationDate" endFormat="d.m.yyyy"  errormessage="${messages.schoolDateBeforeDiplDate}" dontAllowEquals="false"/>
			<v:textValidator input="schoolCity" maxLength="30"/>
			<v:textValidator input="schoolName" maxLength="255"/>
			<v:textValidator input="purposeOfRecognitionOther" maxLength="255" required="$('rp_other').checked" />
		</c:if>
		
		<v:textValidator input="trainingDuration" regex="/^(\d+\.?\d*|\.\d+)$/" maxLength="10" required="false" />
		<v:textValidator input="credits" regex="/^(\d+|\d+\.\d{0,2}|\.\d{1,2})$/" maxLength="10" required="false" />
		<v:textValidator input="graduation_way_other" maxLength="255" required="$('gw_other').checked" />
		<v:textValidator input="training_form_other" maxLength="255" required="$('tf_other').checked" />

    	<v:textValidator input="training_city0" maxLength="30" required="false"/>
		

		
		<input name="activeForm" value="2" type="hidden" />
		<input name="applicationId" value="${applicationId }" type="hidden" />
		
		<div class="clr"><!--  --></div>
		
		<c:forEach items="${universities}" var="university" varStatus="status">
			<div id="university${status.index}">
				 <c:choose>
				     <c:when test="${status.index == 0}">
				       <fieldset><legend>Данни за ${fn:toLowerCase(theUniversityLabel)}</legend>
				         <p>
					         <span class="fieldlabel"><label for="universityName${status.index }" title="${messages.ext_app_requeired_data_applying }" class="required">Наименование*</label></span>
					         <input type="hidden" name="universityId${status.index}" id="universityId${status.index}" value="${university.universityId }" /> 
		                     <v:textinput class="brd w600" name="universityName${status.index}" id="universityName${status.index}" 
		                        onkeydown="if(!isSpecialKeyPressed(event)) universityChanged(this);" 
		                        value="${university.universityName }" 
		                        title="${messages.university_hint}"/>
		                     <span class="p220">(Наименование, град, държава)</span><br />
							 <input type="hidden" name="facultyId${status.index}" id="facultyId${status.index}" value="${university.facultyId}" />
							 <span class="fieldlabel"><label for="facultyName${status.index }">Факултет</label></span>
							 <v:textinput class="brd w600" name="facultyName${status.index}" id="facultyName${status.index}"
										  onkeydown="if(!isSpecialKeyPressed(event)) facultyChanged(this);"
										  value="${university.facultyName }"
										  title="${messages.faculty_hint}"/>
				         </p>
				       </fieldset>
				       <div class="clr"><!--  --></div>         
				       <p class="flt_rgt"><a href="javascript:addJointUniversity();">Добави съвместна образователна степен</a></p>
				     </c:when>
				     <c:otherwise>
				       <fieldset><legend>Данни за съвместното ${fn:toLowerCase(universityLabel)}</legend>
				         <p class="flt_rgt"><a href="javascript:void(0);" style="color: red" onclick="javascript:$('university${status.index}').remove();">Премахни</a></p>
				         <div class="clr"><!--  --></div>
				         <p>
				             <span class="fieldlabel"><label for="universityName${status.index }" title="${messages.ext_app_requeired_data_applying }" >Наименование</label></span>
				             <input type="hidden" name="universityId${status.index}" id="universityId${status.index}" value="${university.universityId }" /> 
	                         <v:textinput class="brd w600" name="universityName${status.index}" id="universityName${status.index}" 
	                            onkeydown="if(!isSpecialKeyPressed(event)) universityChanged(this);" 
	                            value="${university.universityName }" 
	                            title="${messages.university_hint}"/>
	                         <span class="p220">(Наименование, град, държава)</span><br >
							 <input type="hidden" name="facultyId${status.index}" id="facultyId${status.index}" value="${university.facultyId}" />
							 <span class="fieldlabel"><label for="facultyName${status.index }">Факултет</label></span>
							 <v:textinput class="brd w600" name="facultyName${status.index}" id="facultyName${status.index}"
										  onkeydown="if(!isSpecialKeyPressed(event)) facultyChanged(this);"
										  value="${university.facultyName }"
										  title="${messages.faculty_hint}"/>
	                     </p>
	                     
				        </fieldset>
				     </c:otherwise> 
				  </c:choose>
				  <div class="clr"><!--  --></div>
			       <script type="text/javascript">
	                       new Autocomplete('universityName${status.index}', { 
	                         serviceUrl:'${pathPrefix}/control/universitysuggest',
	                         width:600,
	                         onSelect:updateUniversity
	                       });
						   new Autocomplete('facultyName${status.index}', {
							   serviceUrl:'${pathPrefix}/control/universityfacultysuggest',
							   width:600,
							   onSelect:updateUniversityFaculty,
							   dynamicParameters: function (){
								   return {university_id : $('universityId${status.index}').value};
							   }
						   });
	               </script>
			</div>
		</c:forEach>
		<div class="clr20"><!--  --></div>
		<div id="university" style="display:none">
		  <fieldset><legend>Данни за съвместно чуждестранното ${fn:toLowerCase(universityLabel)}</legend>
            <p class="flt_rgt"><a href="javascript:void(0);" style="color: red" onclick="javascript:void(0);">Премахни</a></p>
            <div class="clr"><!--  --></div>
            <p>
                <span class="fieldlabel"><label title="${messages.ext_app_requeired_data_applying }" >Наименование</label></span>
                <input type="hidden" name="universityId" id="universityId" value="" /> 
                <v:textinput class="brd w600" name="universityName" id="universityName" 
                   onkeydown="if(!isSpecialKeyPressed(event)) universityChanged(this);" 
                   value="${university.universityName }" 
                   title="${messages.university_hint}"/>
                <span class="p220">(Наименование, град, държава)</span><br />
				<input type="hidden" name="facultyId" id="facultyId" value="" />
				<span class="fieldlabel"><label for="facultyName">Факултет</label></span>
				<v:textinput class="brd w600" name="facultyName" id="facultyName"
							 onkeydown="if(!isSpecialKeyPressed(event)) facultyChanged(this);"
							 value=""
							 title="${messages.faculty_hint}"/>
             </p>
            </fieldset>
		</div>
		<fieldset id="diploma_data"><legend>Данни за дипломата</legend>
			<p><span class="fieldlabel"><label for="diplomaSeries" >Серия</label></span>
				<v:textinput class="brd w308" maxlength="15" name="diplomaSeries" id="diplomaSeries"  value="${diplomaSeries }" />
			</p>
			<div class="clr"><!--  --></div>
    		<p><span class="fieldlabel"><label for="diplomaNumber" >Номер</label></span>
      			<v:textinput class="brd w308" maxlength="15" name="diplomaNumber" id="diplomaNumber"  value="${diplomaNumber }" />
    		</p>
   			<div class="clr"><!--  --></div>
			<p><span class="fieldlabel"><label for="diplomaRegistrationNumber" >Регистрационен Номер</label></span>
				<v:textinput class="brd w308" maxlength="15" name="diplomaRegistrationNumber" id="diplomaRegistrationNumber"  value="${diplomaRegistrationNumber }" />
			</p>
			<div class="clr"><!--  --></div>
    		<p><span class="fieldlabel"><label for="diplomaDate">Дата</label><br /></span>
      			<v:dateinput name="diplomaDate" id="diplomaDate" value="${diplomaDate }" style="brd w308" />
    		</p>
    		<div class="clr"><!--  --></div>
  		</fieldset>
		
		<fieldset id="training"><legend>Обучение</legend>
            <nacid_ext:extTrainingCourseTrainingLocationEdit type="other">
            <div id="training_location${row_id }">
               <fieldset><legend>Място на провеждане</legend>
                   <c:choose>
                       <c:when test="${row_id == 0}">
                           <p>
	                        <span class="fieldlabel"><label for="training_country_id" class="required" >Държава*</label></span>
	                        <nacid:combobox name="training_country${row_id }" id="training_country${row_id }" attributeName="trainingCountry" style="w308 brd" />
	                    </p>
	                    <div class="clr"><!--  --></div>
	                    <p>
	                        <span class="fieldlabel"><label title="${messages.ext_app_requeired_data_applying }" for="training_city${row_id }" class="required">Град*</label></span>
	                        <v:textinput class="brd w500" name="training_city${row_id }"  value="${training_location_city }" maxlength="30" />
	                    </p>
                       </c:when>
                       
                       <c:otherwise>
                           <p class="flt_rgt"><a href="javascript:$('training_location${row_id }').remove();void(0);" style="color: red">Премахни</a></p>
                           <p>
	                        <span class="fieldlabel"><label for="training_country_id">Държава</label></span>
	                        <nacid:combobox name="training_country${row_id }" id="training_country${row_id }" attributeName="trainingCountry" style="w308 brd" />
	                    </p>
	                    <div class="clr"><!--  --></div>
                           <p>
                               <span class="fieldlabel"><label for="training_city${row_id }" >Град</label></span>
                               <v:textinput class="brd w500" name="training_city${row_id }"  value="${training_location_city }" maxlength="30" />
                           </p>
                       </c:otherwise>
                   </c:choose>
                   
               </fieldset>
            </div>
            </nacid_ext:extTrainingCourseTrainingLocationEdit>
            
            <div class="clr10"><!--  --></div>
	        <p class="flt_rgt"><a href="javascript:addTrainingLocation();">Добави място на провеждане</a></p>
	        <div class="clr"><!--  --></div>
	        <nacid_ext:extTrainingCourseTrainingLocationEdit type="empty">
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
	        </nacid_ext:extTrainingCourseTrainingLocationEdit>

			<c:if test="${!isDoctorateApplicationType}">
			<div id="specialities_id">
				<div id="speciality" class="clr20"><!--  --></div>
				<div id="selected_specialities" style="display: none">
					<p>
						<span class="fieldlabel"><label>Избрани специалности</label></span>
					</p>

					<table id="specialities_table" border="0">
						<c:forEach var="item" items="${specialitiesList}" varStatus="varStatus">
							<tr>
								<td style="display:none;"><input type="hidden" name="speciality_id${varStatus.index}" value="${item.specialityId}" /></td>
								<td style="display:none;"><input type="hidden" name="speciality_text${varStatus.index}" value="${item.specialityTxt}" /></td>
								<td>${item.specialityName}</td>
								<td><a href="javascript:void(0);" onclick="deleteRow(this.parentNode);showHideSpecialities();"><img src="${pathPrefix}/img/icon_delete.png"/></a></td>
							</tr>
						</c:forEach>
					</table>
					<input type="hidden" name="list_size" value="${listSize}" />
				</div>
				<div class="clr15"><!--  --></div>
				<p>

					<span class="fieldlabel"><label for="trainingSpeciality" class="required">Специалност *</label></span>
					<span class="flt_rgt"><a href="javascript:addSpeciality();showHideSpecialities();">Добави специалност</a></span>
					<input type="hidden" id="trainingSpecialityId" name="trainingSpecialityId" value="${trainingSpecialityId}">
					<v:textinput onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('trainingSpeciality');" id="trainingSpeciality" name="trainingSpeciality" class="brd w450 flt_lft" value="${trainingSpecialityTxt}" />

					<script type="text/javascript">
						new Autocomplete('trainingSpeciality', {
							serviceUrl:'${pathPrefix}/control/specialitysuggest',
							onSelect: updateNomenclature,
							width:500
						});
					</script>
				</p>
			</div>
			</c:if>

			<div class="clr15"><!--  --></div>

			<p><span class="fieldlabel"><label for="trainingStart" title="${messages.ext_app_requeired_data_applying }">Начало на обучението </label><br /></span>
				<v:dateinput name="trainingStart" id="trainingStart" value="${trainingStart }" style="brd w100" emptyString="гггг" />
			</p>
			<div class="clr"><!--  --></div>

			<p><span class="fieldlabel"><label for="trainingEnd" title="${messages.ext_app_requeired_data_applying }" >Край на обучението </label><br /></span>
				<v:dateinput name="trainingEnd" id="trainingEnd" value="${trainingEnd }" style="brd w100" emptyString="гггг" />
			</p>
			<div class="clr"><!--  --></div>

			<p><span class="fieldlabel"><label for="trainingDuration">Продължителност</label></span> 
				<v:textinput class="brd w50" name="trainingDuration" id="trainingDuration" value="${trainingDuration }" />
				<label for="trainingDurationUnitId">Тип</label> 
				<nacid:combobox name="trainingDurationUnitId" id="trainingDurationUnitId" attributeName="trainingDurationUnitId" style="w100 brd" />
			</p>
			<div class="clr"><!--  --></div>

			<div class="flt_lft w_prcnt50">
				<div class="flt_lft fieldlabel"><b>Форма на обучение</b></div>
				<div class="flt_lft">
					<nacid_ext:extTrainingCourseTrainingFormEdit>
						<input name="training_form" id="tf_${id }" value="${value_id }" onclick="${onclick}" type="radio" ${checked }/>${label } <br />
					</nacid_ext:extTrainingCourseTrainingFormEdit> 
					<nacid_ext:extTrainingCourseTrainingFormEdit input="true">
						<v:textinput name="training_form_other" id="training_form_other" style="${style }" class="brd w200" maxlength="255"  value="${value }" />
					</nacid_ext:extTrainingCourseTrainingFormEdit>
				</div>
				<div class="clr"><!--  --></div>
			</div>

			<div class="flt_lft w_prcnt49">
				<div class="flt_lft fieldlabel2" title="${messages.ext_app_requeired_data_applying }">
					<b>
					<c:choose>
						<c:when test="${isDoctorateApplicationType}">Начин на придобиване на степента</c:when>
						<c:otherwise>Начин на дипломиране</c:otherwise>
					</c:choose>
					</b>
				</div>
				<div class="flt_lft">
					<nacid_ext:extTrainingCourseGraduationWayEdit>
						<input name="graduation_way" value="${value_id }" onclick="${onclick}" type="checkbox" ${checked } id="gw_${id }" /><label for="gw_${id}">${label }</label><br />
					</nacid_ext:extTrainingCourseGraduationWayEdit> 
					<nacid_ext:extTrainingCourseGraduationWayEdit input="true">
						<v:textinput name="graduation_way_other" id="graduation_way_other" style="${style }" class="brd w210" maxlength="255"  value="${value }" />
					</nacid_ext:extTrainingCourseGraduationWayEdit>
				</div>
				<div class="clr"><!--  --></div>
			</div>
			<div class="clr10"><!--  --></div>

			<p><span class="fieldlabel"><label for="credits">Придобити кредити</label><br /></span> 
				<v:textinput class="brd w50" name="credits" id="credits"  value="${credits}" />
			</p>
			<div class="clr"><!--  --></div>

			<p><span class="fieldlabel"><label for="eduLevel" class="required" >Получена степен*</label></span>

				<nacid:combobox name="eduLevel" id="eduLevel" attributeName="eduLevel" style="w308 brd" onchange="toggleEducationPeriodInformation(this.options[this.selectedIndex].value != doctorOfScienceEduLevel)"/>
    		</p>
			<div class="clr"><!--  --></div>
			<c:if test="${!isDoctorateApplicationType}">
				<p>

					<span class="fieldlabel"><label for="qualification">Придобита квалификация</label></span>
					<input type="hidden" id="qualificationId" name="qualificationId" value="${qualificationId}">
					<v:textinput onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('qualification');" id="qualification" name="qualification" class="brd w450 flt_lft" value="${qualification}" />

					<script type="text/javascript">
						new Autocomplete('qualification', {
							serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?type=7',
							onSelect: updateNomenclature,
							width:500
						});
					</script>
				</p>

			</c:if>
		</fieldset>

		<c:if test="${isDoctorateApplicationType}">
		<div>
			<fieldset>
				<legend>Данни за придобитата степен</legend>
				<div id="dissertation_div" style="display: ${hasDissertationGraduationWay ? 'block' : 'none'}">
					<p>
						<span class="fieldlabel"><label for="thesis_topic" class="required">Тема на дисертацията*</label></span>
						<v:textinput id="thesis_topic" name="thesis_topic" value="${thesisTopic}" class="brd w500" />
					</p>
					<div class="clr"><!--  --></div>
					<p>
						<span class="fieldlabel"><label for="thesis_topic_en" class="required">Тема на дисертацията на английски*</label></span>
						<v:textinput id="thesis_topic_en" name="thesis_topic_en" value="${thesisTopicEn}" class="brd w500" />
					</p>
					<div class="clr"><!--  --></div>
					<p>
						<span class="fieldlabel"><label for="thesis_defence_date" class="required">Дата на защитата*</label></span>
						<v:dateinput id="thesis_defence_date" name="thesis_defence_date" value="${thesisDefenceDate}" class="brd w500" />
					</p>
					<div class="clr"><!--  --></div>
					<p>
						<span class="fieldlabel"><label for="thesis_language" class="required">Език на основния текст*</label></span>
						<nacid:combobox name="thesis_language" id="thesis_language" attributeName="thesisLanguageCombo" style="w508 brd"/>
					</p>
					<div class="clr"><!--  --></div>
					<p>
						<span class="fieldlabel"><label for="thesis_bibliography" class="required">Библиография(бр. заглавия)*</label></span>
						<v:textinput id="thesis_bibliography" name="thesis_bibliography" value="${thesisBibliography}" class="brd w500" />
					</p>
					<div class="clr"><!--  --></div>
					<p>
						<span class="fieldlabel"><label for="thesis_volume" class="required">Обем на дисертационния труд(бр. страници)*</label></span>
						<v:textinput id="thesis_volume" name="thesis_volume" value="${thesisVolume}" class="brd w500" />
					</p>
					<div class="clr"><!--  --></div>
					<p>
						<span class="fieldlabel"><label for="thesis_annotation" class="required">Анотация на български език*</label></span>
						<v:textarea rows="3" cols="40" id="thesis_annotation" name="thesis_annotation" value="${thesisAnnotation}" class="brd w500" />
					</p>
					<div class="clr"><!--  --></div>
					<p>
						<span class="fieldlabel"><label for="thesis_annotation_en" class="required">Анотация на английски език*</label></span>
						<v:textarea rows="3" cols="40" id="thesis_annotation_en" name="thesis_annotation_en" value="${thesisAnnotationEn}" class="brd w500" />
					</p>
					<div class="clr"><!--  --></div>
				</div>
				<p>
					<span class="fieldlabel"><label for="prof_group" class="required">Професионално направление по заявление*</label></span>
					<nacid:combobox name="prof_group" id="prof_group" attributeName="profGroupCombo" style="w508 brd" onchange="updateProfGroupEduArea(this)"/>
				</p>
				<div class="clr"><!--  --></div>
				<p>
					<span class="fieldlabel"><label for="prof_group_edu_area_id">Област на образованието</label></span>
					<nacid:combobox name="prof_group_edu_area_id" id="prof_group_edu_area_id" attributeName="profGroupEduAreaCombo" style="w508 brd" disabled="disabled"/>
				</p>
			</fieldset>
		</div>
		</c:if>
		<c:if test="${!isDoctorateApplicationType}">
		<div id="school_diploma_div">
			<fieldset>
				<legend>Диплома за средно образование</legend>
				<p><span class="fieldlabel"><label class="required" for="schoolCountryId">Държава*</label></span>
					<nacid:combobox name="schoolCountryId" id="schoolCountryId" attributeName="schoolCountryCombo" style="w308 brd" />
				</p>
				<div class="clr"><!--  --></div>

				<p><span class="fieldlabel"><label for="schoolCity" title="${messages.ext_app_requeired_data_applying }">Град </label><br /></span>
					<v:textinput class="brd w500" name="schoolCity"  value="${schoolCity }" />
				</p>
				<div class="clr"><!--  --></div>

				<p><span class="fieldlabel"><label for="schoolName" title="${messages.ext_app_requeired_data_applying }">Училище </label><br /></span>
					<v:textinput class="brd w600" name="schoolName"  value="${schoolName }" />
				</p>
				<div class="clr"><!--  --></div>

				<p><span class="fieldlabel"><label for="schoolGraduationDate">Година на дипломиране</label><br /></span>
					 <v:dateinput name="schoolGraduationDate" id="schoolGraduationDate" value="${schoolGraduationDate }" style="brd w100"
							emptyString="гггг" />
				</p>
				<div class="clr"><!--  --></div>

				<p><span class="fieldlabel"><label for="schoolNotes">Бележки</label><br /></span>
					<textarea class="brd w600" rows="3" cols="40" name="schoolNotes" id="schoolNotes">${schoolNotes }</textarea>
				</p>
				<div class="clr"><!--  --></div>
			</fieldset>
		</div>
		</c:if>
		<div id="prev_diploma_div">
			<fieldset>
				<legend>Диплома за предходна образователна степен за висше образование</legend>

				<p><span class="fieldlabel"><label for="prevDiplName">Висше училище</label><br /></span>
					<v:textinput id="prevDiplName" class="brd w600" name="prevDiplName"
							value="${prevDiplName }" onkeydown="if(!isSpecialKeyPressed(event)) prevUnivChanged();"
							title="${messages.university_hint}" />
					<input type="hidden" value="${prevDiplUniversityId }"
						id="prevDiplUniversityId" name="prevDiplUniversityId"/>
				</p>
				<div class="clr"><!--  --></div>

				<p><span class="fieldlabel"><label for="prevDiplEduLevelId">Придобита степен</label><br /></span>
					<nacid:combobox name="prevDiplEduLevelId" id="prevDiplEduLevelId" attributeName="prevDiplEduLevelCombo" style="w308 brd" />
				</p>
				<div class="clr"><!--  --></div>

				<p>

					<span class="fieldlabel"><label for="prevDiplomaSpeciality">Специалност</label></span>
					<input type="hidden" id="prevDiplomaSpecialityId" name="prevDiplomaSpecialityId" value="${prevDiplomaSpecialityId}">
					<v:textinput onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('prevDiplomaSpeciality');" id="prevDiplomaSpeciality" name="prevDiplomaSpeciality" class="brd w450 flt_lft" value="${prevDiplomaSpeciality}" />

					<script type="text/javascript">
						new Autocomplete('prevDiplomaSpeciality', {
							serviceUrl:'${pathPrefix}/control/specialitysuggest',
							onSelect: updateNomenclature,
							width:500
						});
					</script>
				</p>

				<div class="clr10"><!--  --></div>

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
		<c:if test="${!isDoctorateApplicationType}">
		<div id="purpose_div">
			<fieldset><legend>Цел на признаването</legend>
				<div class="flt_lft">
					<div class="flt_lft w210">&nbsp;<!--  --></div>
					<div class="flt_lft">
						<nacid_ext:extApplicationRecognitionPurposeEdit>
							<input ${checked } name="purposeOfRecognition" value="${value_id }" onclick="${onclick}" type="checkbox" id="rp_${id }" />${label } <br />
						</nacid_ext:extApplicationRecognitionPurposeEdit>
						<nacid_ext:extApplicationRecognitionPurposeEdit input="true">
							<v:textinput class="brd w600" name="purposeOfRecognitionOther" style="${style }" id="recognition_purpose_other"  value="${value }" />
						</nacid_ext:extApplicationRecognitionPurposeEdit>
					</div>
					<div class="clr"><!--  --></div>
				</div>
				<div class="clr10"><!--  --></div>
			</fieldset>
		</div>
		</c:if>
		<input id="training_locations_count" name="training_locations_count" value="${training_locations_size }" type="hidden" />
		<input id="universities_count" name="universities_count" value="${universities_count }" type="hidden" />
		<input id="specialities_count" name="specialities_count" value="${specialities_count }" type="hidden" />
	</v:form>
</nacid_ext:extTrainingCourseEdit>

<script type="text/javascript">
    new Autocomplete('prevDiplName', { 
        serviceUrl:'${pathPrefix}/control/universitysuggest',
        width:600,
        onSelect:updateUniversity
  	});

    function prevUnivChanged() {
    	$('prevDiplUniversityId').value = '';
    }
    function universityChanged(el) {
    	$(el.id.replace('universityName','universityId')).value = '';
    	$(el.id.replace('universityName','facultyId')).value = '';
    	$(el.id.replace('universityName','facultyName')).value = '';
    }

	function facultyChanged(el) {
		$(el.id.replace('facultyName','facultyId')).value = '';
	}
    function addSpeciality() {
    	if (validateText($('trainingSpeciality'), true, 2, 150, null, '') && validateSpeciality()) {
    		$('trainingSpecialityId').className = 'brd w450 flt_lft';
	    	var table = $('specialities_table');
	    	var rowCount = table.rows.length;
	    	var row = table.insertRow(rowCount);
	    	var specialityName = '';
	    	
	    	var cell1 = row.insertCell(0);
	    	var specialityIdField = new Element('input', { 'type': 'hidden'});
	    	specialityIdField.name = 'speciality_id' + rowCount;
	    	cell1.style.display = 'none';
		
			var cell2 = row.insertCell(1);
			var specialityTextField = new Element('input', { 'type': 'hidden'});
			specialityTextField.name = 'speciality_text' + rowCount;
			cell2.style.display = 'none';
			
			var cell3 = row.insertCell(2);
			
			if ($('trainingSpecialityId').value != '') {
				specialityIdField.value = $('trainingSpecialityId').value;
			} else {
				specialityTextField.value = $('trainingSpeciality').value.strip();
			}
			specialityName = $('trainingSpeciality').value;
			
			cell1.appendChild(specialityIdField);
			cell2.appendChild(specialityTextField);
			cell3.innerHTML = specialityName;
			
			var cell4 = row.insertCell(3);
			var deleteButton = new Element('a', {'href': 'javascript:void(0);'/*, 'onclick': function(){deleteRow(this.parentNode);}/*'deleteRow(this.parentNode);'*/});
			deleteButton.onclick = function() {deleteRow(this.parentNode);};
			var img = new Element('img', {'src': '${pathPrefix}/img/icon_delete.png'});
			deleteButton.insert(img);
			cell4.appendChild(deleteButton);
			
			specialitiesCount++;
			$('specialities_count').value = specialitiesCount;
			$('trainingSpeciality').value = '';
			$('trainingSpecialityId').value = '';
    	}
    }
	function deleteRow(tableData) {
		if (specialitiesCount > 0) {
			var table = $('specialities_table');
			var rowNumber = parseInt(tableData.parentNode.rowIndex);
			table.deleteRow(rowNumber);
			specialitiesCount--;
			$('specialities_count').value = specialitiesCount;
			iterateTable(table);
		}
	}
    function showHideSpecialities() {
		var selectedSpecialities = $('selected_specialities');
		if (selectedSpecialities == null) {
			return;
		}
    	var elements = $$("input[name^=speciality_id]").size();
        if (elements > 0) {
            selectedSpecialities.show();
        } else {
			selectedSpecialities.hide();
        }
    }
	function iterateTable(table) {
		var rowCount = table.rows.length;
		for (var i = 0; i < rowCount; i++) {
			var row = table.rows[i];
			row.cells[0].children[0].name = 'speciality_id' + i;
			row.cells[1].children[0].name = 'speciality_text' + i;
		}
	}
	
	/*String.prototype.strip = function() {
	    return this.replace(/^\s+|\s+$/g, "");
	};*/
	
    function validateSpeciality() {
    	if ($('trainingSpecialityId').value == '') {
    		var table = $('specialities_table');
        	var rowCount = table.rows.length;
    		for (var i = 0; i < rowCount; i++) {
    			var row = table.rows[i];
    			if (row.cells[1].children[0].value.toLowerCase() == $('trainingSpeciality').value.strip().toLowerCase() ||
    					row.cells[2].innerHTML.toLowerCase() == $('trainingSpeciality').value.strip().toLowerCase()) {
    				$('trainingSpeciality').addClassName('errorinput');
    				return false;
    			}
    		}
    		$('trainingSpeciality').removeClassName('errorinput');
    		return true;
    	} else {
        	var table = $('specialities_table');
        	var rowCount = table.rows.length;
    		for (var i = 0; i < rowCount; i++) {
    			var row = table.rows[i];
    			if (row.cells[0].children[0].value == $('trainingSpecialityId').value ||
    					row.cells[1].children[0].value.toLowerCase() == $('trainingSpeciality').value.toLowerCase()) {
    				$('trainingSpeciality').addClassName('errorinput');
    				return false;
    			}
    		}
    		$('trainingSpeciality').removeClassName('errorinput');
    		return true;
    	}
    }
	
    showHideSpecialities();
    toggleEducationPeriodInformation(hasEducationPeriodInformation);
</script>
