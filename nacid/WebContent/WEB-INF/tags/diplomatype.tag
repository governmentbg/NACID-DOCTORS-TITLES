<%@ tag import="com.nacid.bl.nomenclatures.NomenclaturesDataProvider" %>
<%@ tag import="static com.nacid.bl.nomenclatures.EducationLevel.EDUCATION_LEVEL_DOCTOR_OF_SCIENCE" %>
<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" dynamic-attributes="dynamicAttrs"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="path" required="false" rtexprvalue="true"%>
<%@ attribute name="name" required="true" rtexprvalue="true"%>
<%@ attribute name="comboAttribute" required="true" rtexprvalue="true"%>
<%@ attribute name="countryComboAttribute" required="true" rtexprvalue="true"%>
<%@ attribute name="countryIdInput" required="true" rtexprvalue="true"%>
<%@ attribute name="universityIdInput" required="true" rtexprvalue="true"%>
<%@ attribute name="universityFacultyIdInput" required="true" rtexprvalue="true"%>
<%@ attribute name="eduLevelCombo" required="true" rtexprvalue="true"%>
<%@ attribute name="originalEduLevel" required="true" rtexprvalue="true"%>
<%@ attribute name="id" required="false" rtexprvalue="true"%>
<%@ attribute name="isDoctorate" required="true" rtexprvalue="true"%>
<%@ attribute name="onchange" required="false" rtexprvalue="true"%>
<%@ attribute name="universityLabel" required="true" rtexprvalue="true"%>

<div style="display:none;" id="dipl_type_correct_message_div" class="correct">Данните бяха успешно въведени!</div>
<div style="display:none;" id="dipl_type_error_message_div" class="error">Получи се грешка при опит за запис!</div>
    <span id="dts" style="display: ${isDoctorate ? 'none;' : 'block;'}">
        <span class="fieldlabel"><label for="${id}">Вид диплома</label></span>
        <span id="diploma_type_span" class="flt_lft">
            <nacid:combobox id="${id}" name="${name}" attributeName="${comboAttribute}" onchange="${onchange};showHideEditDiplomaTypeButton();" style="brd w450"/>
            <c:if test="${isDoctorate}">
                <input type="hidden" value="${requestScope[comboAttribute].selectedKey}" name="${name}"/>
            </c:if>
        </span>
    </span>

    <span class="flt_rgt">
        <c:choose>
            <c:when test="${isDoctorate}">
                <a id="editDiplomaType_${name}" href="javascript: void(0);" onclick="showHideDiplomaTypeEditionDiv(true);">Детайли</a>&nbsp;
            </c:when>
            <c:otherwise>
                <a id="editDiplomaType_${name}" href="javascript: void(0);" onclick="showHideDiplomaTypeEditionDiv(true);">Редакция</a>&nbsp;
                <a id="newDiplomaType_${name}" href="javascript: void(0);" onclick="showHideDiplomaTypeEditionDiv(false);">Нов</a>
            </c:otherwise>
        </c:choose>

    </span>
	<div style="clear: both;"><!--  --></div>

<div id="diplomaType_new_div" style="display:none;" class="subwindow" style="width: 900px; height: auto;">
	<fieldset class="subwindow_f"><legend class="subwindow_l">Основни данни</legend>
		<input type="hidden" id="diplomaTypeId" name="diplomaTypeId"/>
        <input type="hidden" id="diplomaTypeType" name="diplomaTypeType" value="1" />
        <p <c:if test="${isDoctorate}">style="display: none;"</c:if> >
            <span class="fieldlabel2"><label for="diplomaTypeTitle">Вид диплома</label></span>
            <v:textinput class="brd w600" id="diplomaTypeTitle" name="diplomaTypeTitle"/>
        </p>
		<div class="clr"><!--  --></div>
		<p id="diplomaTypeJointDegree" class="checkbox">
  			<input id="joint_degree" name="joint_degree" value="1" type="checkbox" onclick="showHideJointDegree(this.checked);"/>
  			<label for="joint_degree">Съвместна степен</label>
		</p>
		<div id="diplomaTypeUniversityDiv0" class="university_class">
			<fieldset class="subwindow_f"><legend class="subwindow_l">${universityLabel}</legend>
					<%-- <span id="removeButtonSpace0" style="display:none;" class="flt_rgt">
						<a href="javascript:void(0);" style="color: red">Премахни</a>      				
					</span>--%>
				<div class="flt_lft"><!--  --></div>
				<span class="flt_rgt">
                    <a id="editButton0" href="javascript:void(0);" style="display:none;">Редакция</a>&nbsp;
                    <a id="newButton0" href="javascript:void(0);" onclick="" style="display:none;">Нов</a>&nbsp;
                    <a id="removeButton0" href="javascript:void(0);" style="display:none; color: red">Премахни</a>
                    </span>
				<div style="clear: both;"><!--  --></div>
				<div class="clr10"><!--  --></div>
	  			<p><span class="fieldlabel2"><label for="diplomaTypeCountry0">Държава</label></span> 
	      			<nacid:combobox id="diplomaTypeCountry0" name="diplomaTypeCountry0" attributeName="${countryComboAttribute}" style="w600 brd" onchange="getUniversities(this, '-');{if (this.getAttribute('id') == 'diplomaTypeCountry0') updateOriginalEduLevelsCombo();};updateNationalQualificationsFrameworkCombo('nationalQualificationsFrameworkId', null);updateNationalQualificationsFrameworkCombo('nationalQualificationsFrameworkAccessId', null);" />
	  			</p>
	  			<div class="clr"><!--  --></div>
	  			<p><span class="fieldlabel2"><label for="diplomaTypeUniversity0">${universityLabel}</label></span>
	      			<span id="diplomaTypeUniversity_span0"> 
	         			<nacid:combobox id="university_id0" name="university_id0" attributeName="universityCombo" style="brd w600" />
	      			</span>
	  			</p>
	  			<div class="clr10"><!--  --></div>
                <p>
                    <span class="fieldlabel2"><label for="university_faculty_id0">Факултет</label></span>
                    <span id="diplomaTypeUniversityFaculty_span0">
	         			<nacid:combobox id="university_faculty_id0" name="university_faculty_id0" attributeName="universityFacultyCombo" style="brd w450" />
	      			</span>
                    <span class="flt_rgt">
                        <a href="javascript:void(0);"  id="editUniversityFacultyButton0" style="display: none;">Редакция</a>&nbsp;
                        <a href="javascript:void(0);"  id="newUniversityFacultyButton0" style="display: none;">Нов</a>
                    </span>
                </p>
                <div class="clr10"><!--  --></div>
			</fieldset>
		</div>
		<div  id="add_universities" style="display: none;">
       		<div class="clr10"><!--  --></div>
       		<div class="flt_lft"><!--  --></div>
       		<a class="flt_rgt" href="javascript:addUniversity();" id="addUniBtn">Добави ${fn:toLowerCase(universityLabel)}</a>
       		<div style="clear: both;"><!--  --></div>
       		<div class="clr10"><!--  --></div>
       	</div>
      
		<div id="eduLvl" class="clr20"><!--  --></div>
        <p>
            <span class="fieldlabel2"><label for="originalEduLevel">Придобита степен - оригинал</label></span>
            <span id="orgEduLvl"><nacid:combobox id="originalEduLevel" name="originalEduLevel" attributeName="${originalEduLevel }" style="w500 brd" onchange="originalEduLevelChanged();"/></span>
            <span class="flt_rgt">
                <a href="javascript:showHideOriginalEduLevelEditBox(true);"  id="${id}_edit_edu_level">Редакция</a>&nbsp;
                <a href="javascript:showHideOriginalEduLevelEditBox(false);" id="${id}_new_edu_level">Нов</a>
            </span>
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel2"><label for="orgEduLvlTranslated">Придобита степен - Превод</label></span>
            <v:textinput class="brd w500" name="orgEduLvlTranslated" id="orgEduLvlTranslated" value="${originalEduLevelTranslated}" disabled="disabled" />
        </p>


        <div id="new_edu_level_div_original" style="display:none; padding: 20px; width: 800px; height: auto; margin-top:-30px" class="subwindow2">
            <div class="clr"><!--  --></div>
            <v:hidden id="eduLevelClass_original"/>
            <input type="hidden" name="originalEduLevelId" id="originalEduLevelId" />
            <p>
                <span class="fieldlabel2"><label for="originalEduLevelName">Придобита степен - оригинал</label></span>
                <input type="text" class="brd w500" name="originalEduLevelName" id="originalEduLevelName" value="">
            </p>
            <div class="clr"><!--  --></div>
            <p>
                <span class="fieldlabel2"><label for="originalEduLevelNameTranslated">Придобита степен - превод</label></span>
                <input type="text" class="brd w500" name="originalEduLevelNameTranslated" id="originalEduLevelNameTranslated" value="">
            </p>
            <div class="clr"><!--  --></div>
            <p>
                <span class="fieldlabel2"><label for="originalEduLevelCountry">Държава</label></span>
                <nacid:combobox id="originalEduLevelCountry" name="originalEduLevelCountry" attributeName="${countryComboAttribute}" style="w500 brd" />
            </p>
            <div class="clr"><!--  --></div>
            <p>
                <span class="fieldlabel2"><label for="originalEduLevelEduLevel">Образователна степен</label></span>
                <nacid:combobox id="originalEduLevelEduLevel" name="originalEduLevelEduLevel" attributeName="${eduLevelCombo }" style="w500 brd"/>
            </p>

            <p >
                <span class="fieldlabel2"><label for="originalEduLevelDateFrom">От дата</label></span>
                <v:dateinput name="originalEduLevelDateFrom" id="originalEduLevelDateFrom" style="brd w200" />
            </p>
            <div class="clr"><!--  --></div>
            <p>
                <span class="fieldlabel2"><label for="originalEduLevelDateTo">До дата</label></span>
                <v:dateinput name="originalEduLevelDateTo" id="originalEduLevelDateTo" style="brd w200" />
            </p>
            <div class="clr"><!--  --></div>

            <a class="flt_rgt" href="javascript:void(0);" onclick="showHideOriginalEduLevelEditBox(false);">Отказ</a>
            <a class="flt_rgt" href="javascript:void(0);" style="margin-right: 10px;" onclick="saveOriginalEduLevel();">Запис</a>

        </div>

        <span id="afterOriginalEduLevelDiv"></span>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel2"><label for="diplomaTypeEduLevel">Образователна степен</label></span>
            <nacid:combobox id="diplomaTypeEduLevel" name="diplomaTypeEduLevel" attributeName="${eduLevelCombo }" style="w600 brd" onchange="updateOriginalEduLevelsCombo();"/>
        </p>
        <div class="clr"><!--  --></div>
        <div style="width: 430px; float:left;">
            <p>
                <v:flatNomenclatureSelect labelclass="fieldlabel2" nomenclatureType="<%= Integer.toString(NomenclaturesDataProvider.FLAT_NOMENCLATURE_BOLOGNA_CYCLE) %>"
                                         fieldName="${messages.bolognaCycle }"
                                         id="bolognaCycleId" class="brd w150 flt_lft" value="${bolognaCycleId}" windowLevel="2"
                                        nomenclatureValues="bolognaCycleCombo" inputClass="brd w200" onsuccess="updateBolognaCyclesOnSuccess()"/>
            </p>
            <div class="clr"><!--  --></div>
            <p>
                <span class="fieldlabel2"><label for="nationalQualificationsFrameworkId">${messages.nationalQualificationsFramework }</label></span>
                <span id="nationalQualificationsFrameworkId_combo"><nacid:combobox id="nationalQualificationsFrameworkId" name="nationalQualificationsFrameworkId" attributeName="nqfCombo" style="w150 brd"/></span>
                <span id="nationalQualificationsFramework_new" class="flt_rgt">
		            <a id="new_nationalQualificationsFrameworkId" onclick="showSubwindowNqf('nationalQualificationsFrameworkId');" href="javascript:void(0);">Нов</a>
	            </span>
            </p>
            <div class="clr"><!--  --></div>


            <span id="new_nqf" style="display:none; margin-top:-20px; padding: 20px; width: 825px; height: auto;">
                <p>
                    <span class="fieldlabel2"><label for="input_nqf">${messages.nationalQualificationsFramework }</label></span>
                    <input name="input_nqf" id="input_nqf" value="" class="brd w400"/>
                </p>
                <div class="clr"><!--  --></div>
                <p>
                    <span class="fieldlabel2"><label for="input_nqf_country">${messages.Country }</label></span>
                    <nacid:combobox id="input_nqf_country" name="input_nqf_country" attributeName="${countryComboAttribute}" style="w600 brd" />
                </p>
                <a href="javascript:void(0);" style="margin-right: 10px;" id="save_nqf">Запис</a>
                <a href="javascript:void(0);" id="cancel_nqf">Отказ</a>
            </span>


            <div class="clr"><!--  --></div>
            <p>
                <v:flatNomenclatureSelect labelclass="fieldlabel2" nomenclatureType="<%= Integer.toString(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EUROPEAN_QUALIFICATIONS_FRAMEWORK) %>"
                                          fieldName="${messages.europeanQualificationsFramework }"
                                          id="europeanQualificationsFrameworkId" class="brd w450 flt_lft" value="${europeanQualificationsFrameworkId}" windowLevel="2"
                                          nomenclatureValues="eqfCombo" inputClass="brd w200" onsuccess="updateEuropeanQualificationsFrameworksOnSuccess()"/>
            </p>
        </div>
        <div style="width:430px; float:left">
        <div class="clr"><!--  --></div>
        <p>
            <v:flatNomenclatureSelect labelclass="fieldlabel2" nomenclatureType="<%= Integer.toString(NomenclaturesDataProvider.FLAT_NOMENCLATURE_BOLOGNA_CYCLE) %>"
                                      fieldName="${messages.bolognaCycleAccess }"
                                      id="bolognaCycleAccessId" class="brd w450 flt_lft" value="${bolognaCycleAccessId}" windowLevel="2"
                                      nomenclatureValues="bolognaCycleAccessCombo" inputClass="brd w200" onsuccess="updateBolognaCyclesOnSuccess()"/>
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <span class="fieldlabel2"><label for="nationalQualificationsFrameworkAccessId">${messages.nationalQualificationsFrameworkAccess }</label></span>
            <span id="nationalQualificationsFrameworkAccessId_combo"><nacid:combobox id="nationalQualificationsFrameworkAccessId" name="nationalQualificationsFrameworkAccessId" attributeName="nqfAccessCombo" style="w600 brd"/></span>
            <span id="nationalQualificationsFrameworkAccessId_new" class="flt_rgt">
		        <a id="new_nationalQualificationsFrameworkAccessId" onclick="showSubwindowNqf('nationalQualificationsFrameworkAccessId');" href="javascript:void(0);">Нов</a>
	        </span>
        </p>
        <div class="clr"><!--  --></div>
        <p>
            <v:flatNomenclatureSelect labelclass="fieldlabel2" nomenclatureType="<%= Integer.toString(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EUROPEAN_QUALIFICATIONS_FRAMEWORK) %>"
                                      fieldName="${messages.europeanQualificationsFrameworkAccess }"
                                      id="europeanQualificationsFrameworkAccessId" class="brd w450 flt_lft" value="${europeanQualificationsFrameworkAccessId}" windowLevel="2"
                                      nomenclatureValues="europeanQualificationsFrameworkAccessCombo" inputClass="brd w200" onsuccess="updateEuropeanQualificationsFrameworksOnSuccess()"/>
        </p>
        </div>
        <div class="clr"><!--  --></div>
		<%--<div class="clr"><!--  --></div>
        <p><span class="fieldlabel2"><label for="diplomaTypeVisualElementsDescr">Описание на визуалните елементи</label><br />
		</span> <textarea id="diplomaTypeVisualElementsDescr" class="brd w600" rows="3" cols="40" name="diplomaTypeVisualElementsDescr"></textarea></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="diplomaTypeProtectionElementsDescr">Описание на защитните елементи</label><br />
		</span> <textarea id="diplomaTypeProtectionElementsDescr" class="brd w600" rows="3" cols="40" name="diplomaTypeProtectionElementsDescr"></textarea></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="diplomaTypeNumberFormatDescr">Описание на формата на номера</label><br />
		</span> <textarea id="diplomaTypeNumberFormatDescr" class="brd w600" rows="3" cols="40" name="diplomaTypeNumberFormatDescr"></textarea></p>--%>

		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="diplomaTypeNotes">Бележки</label></span>
            <textarea id="diplomaTypeNotes" class="brd w600" rows="3" cols="40" name="diplomaTypeNotes"></textarea>
        </p>
		<div class="clr"><!--  --></div>
        <div id="validity_div" <c:if test="${isDoctorate}">style="display: none;"</c:if>>
            <p>
                <span class="fieldlabel2"><label for="diplomaTypeDateFrom">От дата</label></span>
                <v:dateinput name="diplomaTypeDateFrom" id="diplomaTypeDateFrom" style="brd w200" />
            </p>
		    <div class="clr"><!--  --></div>
		    <p>
                <span class="fieldlabel2"><label for="diplomaTypeDateTo">До дата</label></span>
                <v:dateinput name="diplomaTypeDateTo" id="diplomaTypeDateTo" style="brd w200" />
            </p>
		    <div class="clr"><!--  --></div>
        </div>
		<div style=""></div>

		<a href="#diploma_type_span" onclick="saveDiplomaType();" style="margin-left: 45%;">Запис</a>
		<a href="#diploma_type_span" onclick="showHideDiplomaTypeEditionDiv(false);return false;" style="margin-left: 20px;">Отказ</a>
	</fieldset>
</div>

<script type="text/javascript">
//RayaWritten--------------------------------------------------------------
if(window.location.pathname.lastIndexOf('view') != -1){
		var name = "${name}";
		$('newDiplomaType_'+name).hide();
        $('editDiplomaType_'+name).hide();
}
function getUniversities(el, uniValue) {
    var universityId = el.id.replace("diplomaTypeCountry","");
    var countryId = el.options[el.selectedIndex].value;
	new Ajax.Request('${pathPrefix}/control/diploma_type_university?university_number=' + universityId + '&countryId=' + countryId, {
        asynchronous: false,
        onComplete: function(oXHR) {
            $("diplomaTypeUniversity_span" + universityId).update(oXHR.responseText);
            $$("#diplomaTypeUniversity_span" +universityId+" select")[0].value = uniValue;
            $$("#diplomaTypeUniversity_span" +universityId+" select")[0].setAttribute("onChange", "showHideDiplomaTypeEditUniButton(this);getUniversityFaculties(this, '-')");

        },
        method: 'GET'
  });
}
function getUniversityFaculties(el, facValue) {
    var universityNumber = el.id.replace("university_id","");
    var universityId = el.options[el.selectedIndex].value;

    new Ajax.Request('${pathPrefix}/control/diploma_type_university_faculty?university_number=' + universityNumber + '&university_id=' + universityId, {
        asynchronous: false,
        onComplete: function(oXHR) {
            $("diplomaTypeUniversityFaculty_span" + universityNumber).update(oXHR.responseText);
            $$("#diplomaTypeUniversityFaculty_span" + universityNumber + " select")[0].value =  facValue == null || facValue == "" ? "-" : facValue;
            showHideDiplomaTypeEditUniFacultyButton($('university_faculty_id' + universityNumber));
            $$("#diplomaTypeUniversityFaculty_span" +universityNumber+" select")[0].setAttribute("onChange", "showHideDiplomaTypeEditUniFacultyButton(this);");
        },
        method: 'GET'
    });
}

function fillDiplomaTypeInputs(isEdit){
	var universityIdInput = ${universityIdInput};
	var universityFacultyIdInput = ${universityFacultyIdInput};
	var countryIdInput = ${countryIdInput};
    var idInput = $('${id}');
    $('diplomaTypeCountry0').value = countryIdInput.value;
	getUniversities($('diplomaTypeCountry0'), universityIdInput.value == '' ? '-' : universityIdInput.value);
    getUniversityFaculties($('university_id0'), universityFacultyIdInput.value == '' ? '-' : universityFacultyIdInput.value);
    showHideDiplomaTypeEditUniFacultyButton($('university_faculty_id0'));
    if (idInput.value != '-' && isEdit) {
        new Ajax.Request('/nacid/control/diploma_type_ajax/view', {
            method: 'post',
            asynchronous: false,
            parameters: {
                id: idInput.value
            },
            onSuccess: function(transport) {
                var response = transport.responseText || "no response text";
                var json = response.evalJSON(true);
                $('diplomaTypeTitle').value = json.title;
                if (json.isJointDegree) {
                    $('joint_degree').click();
                    showHideJointDegree(true);
                    for (var i = 1; i < json.universities.length; i++) {
                        addUniversity();
                        $('diplomaTypeCountry'+i).value = json.universities[i].countryId;
                        getUniversities($('diplomaTypeCountry' + i), json.universities[i].id);
                        $('university_id'+i).value = json.universities[i].id;
                        getUniversityFaculties($('university_id' + i), json.universities[i].facultyId);
                        showHideDiplomaTypeEditUniButton($('university_id'+i));
                        showHideDiplomaTypeEditUniFacultyButton($('university_faculty_id'+i));

                    }
                }
                $('diplomaTypeId').value = json.id;

                $('diplomaTypeEduLevel').value = json.educationLevel;


                updateOriginalEduLevelsCombo();//update-va komboto. Tova stava sled kato e update-nato poleto diplomaTypeEduLevel, zashtoto originalEduLevelsCombo zavisi ot nego!
                updateFlatNomenclatureCombo('bolognaCycleId', <%=NomenclaturesDataProvider.FLAT_NOMENCLATURE_BOLOGNA_CYCLE%>, json.bolognaCycleId);
                updateFlatNomenclatureCombo('europeanQualificationsFrameworkId', <%=NomenclaturesDataProvider.FLAT_NOMENCLATURE_EUROPEAN_QUALIFICATIONS_FRAMEWORK%>, json.europeanQualificationsFrameworkId);
                updateNationalQualificationsFrameworkCombo('nationalQualificationsFrameworkId', json.nationalQualificationsFrameworkId);
                updateFlatNomenclatureCombo('bolognaCycleAccessId', <%=NomenclaturesDataProvider.FLAT_NOMENCLATURE_BOLOGNA_CYCLE%>, json.bolognaCycleAccessId);
                updateFlatNomenclatureCombo('europeanQualificationsFrameworkAccessId', <%=NomenclaturesDataProvider.FLAT_NOMENCLATURE_EUROPEAN_QUALIFICATIONS_FRAMEWORK%>, json.europeanQualificationsFrameworkAccessId);
                updateNationalQualificationsFrameworkCombo('nationalQualificationsFrameworkAccessId', json.nationalQualificationsFrameworkAccessId);

                if (typeof json.originalEducationLevel === "undefined") {
                } else {
                    $('originalEduLevel').value = json.originalEducationLevel;
                }

                originalEduLevelChanged();//sled tova izvikva eduLevel.changed - da updatene obrazovatelnata stepen i da q napravi neaktivna pri neobhodimist

                //$('diplomaTypeVisualElementsDescr').value = json.visualElementsDescr;
                //$('diplomaTypeProtectionElementsDescr').value = json.protectionElementsDescr;
                //$('diplomaTypeNumberFormatDescr').value = json.numberFormatDescr;
                $('diplomaTypeNotes').value = json.notes;
                $('diplomaTypeDateFrom').value = json.dateFrom;
                $('diplomaTypeDateTo').value = json.dateTo;
                $('diplomaTypeType').value = json.type;



            },
            onFailure: function() {
                alert('Error');
            }
        });
    } else {

        updateFlatNomenclatureCombo('bolognaCycleId', <%=NomenclaturesDataProvider.FLAT_NOMENCLATURE_BOLOGNA_CYCLE%>, null);
        updateFlatNomenclatureCombo('europeanQualificationsFrameworkId', <%=NomenclaturesDataProvider.FLAT_NOMENCLATURE_EUROPEAN_QUALIFICATIONS_FRAMEWORK%>, null);
        updateNationalQualificationsFrameworkCombo('nationalQualificationsFrameworkId', null);
        updateFlatNomenclatureCombo('bolognaCycleAccessId', <%=NomenclaturesDataProvider.FLAT_NOMENCLATURE_BOLOGNA_CYCLE%>, null);
        updateFlatNomenclatureCombo('europeanQualificationsFrameworkAccessId', <%=NomenclaturesDataProvider.FLAT_NOMENCLATURE_EUROPEAN_QUALIFICATIONS_FRAMEWORK%>, null);
        updateNationalQualificationsFrameworkCombo('nationalQualificationsFrameworkAccessId', null);

        updateOriginalEduLevelsCombo();
        originalEduLevelChanged();


    }


}

function updateBolognaCyclesOnSuccess() {
    updateFlatNomenclatureCombo('bolognaCycleId', <%=NomenclaturesDataProvider.FLAT_NOMENCLATURE_BOLOGNA_CYCLE%>, $('bolognaCycleId').value);
    updateFlatNomenclatureCombo('bolognaCycleAccessId', <%=NomenclaturesDataProvider.FLAT_NOMENCLATURE_BOLOGNA_CYCLE%>, $('bolognaCycleAccessId').value);
}

function updateEuropeanQualificationsFrameworksOnSuccess() {
    updateFlatNomenclatureCombo('europeanQualificationsFrameworkId', <%=NomenclaturesDataProvider.FLAT_NOMENCLATURE_EUROPEAN_QUALIFICATIONS_FRAMEWORK%>, $('europeanQualificationsFrameworkId').value);
    updateFlatNomenclatureCombo('europeanQualificationsFrameworkAccessId', <%=NomenclaturesDataProvider.FLAT_NOMENCLATURE_EUROPEAN_QUALIFICATIONS_FRAMEWORK%>, $('europeanQualificationsFrameworkAccessId').value);
}

function updateNationalQualificationsFrameworksOnSuccess() {
    updateNationalQualificationsFrameworkCombo('nationalQualificationsFrameworkId', $('nationalQualificationsFrameworkId').value);
    updateNationalQualificationsFrameworkCombo('nationalQualificationsFrameworkAccessId', $('nationalQualificationsFrameworkAccessId').value);
}

function showHideDiplomaTypeEditionDiv(isEdit) {
    clearAllErrors(null);
    var editDiv = $('diplomaType_new_div');
    if (editDiv.visible() == false) {
		if ($$('.subwindow_open').length >= 1) {
			return;
		}
        clearDiplomaTypeInputs();
        editDiv.addClassName('subwindow_open');
		editDiv.show();
		fillDiplomaTypeInputs(isEdit);
	} else {
        editDiv.hide();
		editDiv.removeClassName('subwindow_open');
	}


}

var universities = 1;
function addUniversity() {
    var el = $('diplomaTypeUniversityDiv0').clone(true);
    el.id = el.id.replace("0", universities);
    $('eduLvl').insert({before:el});
    el.show();
    /* Preobarzuva ID-tata i name=ovete na inputite/selectovete/textbox-a, kato im slaga po edno jointUniversityId v kraq*/
    $$('#diplomaTypeUniversityDiv' + universities + ' select', '#diplomaTypeUniversityDiv' + universities + ' a', '#diplomaTypeUniversityDiv' + universities + ' span').each(function(element) {
        if (element.name != null && element.name != "") {
        	element.name = element.name.replace("0", universities);
        }
        if (element.id != null && element.id != "") {
            element.id = element.id.replace("0", universities);
        }
    });
    $('diplomaTypeCountry'+universities).value = "-";
    getUniversities($('diplomaTypeCountry'+universities), "-");
    getUniversityFaculties($('university_id'+universities), "-");
    //$('university_id'+universities).value = "-";
    <%--$('removeButtonSpace'+universities).show();
    $$('#removeButtonSpace'+universities+' a')[0].setAttribute('href', "javascript: void(0);");
    $$('#removeButtonSpace'+universities+' a')[0].onclick = function() { $('diplomaTypeUniversityDiv' + universities).remove(); enableDisableJointDegreeCheckBox(); };
     --%>
    var div = $('diplomaTypeUniversityDiv' + universities);
    $('removeButton' + universities).onclick = function() { div.remove(); enableDisableJointDegreeCheckBox(); };
    $('removeButton' + universities).show();
    //$('editButton' + universities).show();
    $('newButton' + universities).show();
    $('newButton' + universities).setAttribute("onClick", "showHideNewUniversityDiv(false, this.up().next(), 2, 0, 'diplomaTypeUniversitySaved(json, " + universities + ")' );");

    universities++;
    enableDisableJointDegreeCheckBox();
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
    if ($$('.university_class').length == 1)  {
        $('joint_degree').enable();
    } else {
    	$('joint_degree').disable();
    }
}
function clearDiplomaTypeInputs() {

    var unis = [];
	unis = $$('.university_class');
	for(var i=1; i<unis.length; i++){
		unis[i].remove();
	}
    $('diplomaTypeId').value="";
	$('diplomaTypeTitle').value="";
	var countryIdInput = ${countryIdInput};
	$('diplomaTypeCountry0').value= countryIdInput.value;
	if ($$('#university_id0').length > 0) {
        Element.remove($('university_id0'));
    }

	$('diplomaTypeEduLevel').value="-";
	//$('diplomaTypeVisualElementsDescr').value = "";
	//$('diplomaTypeProtectionElementsDescr').value = "";
	//$('diplomaTypeNumberFormatDescr').value = "";
	$('diplomaTypeNotes').value = "";
	$('diplomaTypeDateFrom').value = "дд.мм.гггг";
	$('diplomaTypeDateTo').value = "дд.мм.гггг";
	$('joint_degree').checked = false;
	$('joint_degree').enable();
	$('add_universities').style.display = "none";
    clearInputError($('diplomaTypeTitle'));
	clearInputError($('diplomaTypeCountry0'));
	universities = 1;	
}
//---------------------------------------------------------------------------
function saveDiplomaType() {
    clearAllErrors(null);
    var ret = true;
    ret = validateText($('diplomaTypeTitle'), true, -1, -1, null, '') && ret;
    ret = validateComboBox($('diplomaTypeCountry0'), true, '') && ret;
    ret = validateComboBox($('university_id0'), true, '') && ret;
	var universityIds = new Array();
    var facultyIds = new Array();
	universityIds[0] = $('university_id0').value;
    facultyIds[0] = $('university_faculty_id0').value;
	var universitiesCount = 1;
	if ($('joint_degree').checked == true && universities > 1) {
		for (var i = 1; i < universities; i ++) {
			if ($('diplomaTypeUniversityDiv' + i) != null) {
				ret = validateComboBox($('diplomaTypeCountry' + i), true, '') && ret;
				ret = validateComboBox($('university_id' + i), true, '') && ret;
				ret = validateComboBox($('university_faculty_id' + i), false, '') && ret;
				universityIds[universitiesCount] = $('university_id' + i).value;
                facultyIds[universitiesCount] = $('university_faculty_id' + i).value;
				universitiesCount++;
			}
		}
	}
	ret = validateComboBox($('diplomaTypeEduLevel'), true, '') && ret;
	//ret = validateText($('diplomaTypeVisualElementsDescr'), false, -1, -1, null, '') && ret;
	//ret = validateText($('diplomaTypeProtectionElementsDescr'), false, -1, -1, null, '') && ret;
	//ret = validateText($('diplomaTypeNumberFormatDescr'), false, -1, -1, null, '') && ret;
	ret = validateText($('diplomaTypeNotes'), false, -1, -1, null, '') && ret;
	ret = validateDate($('diplomaTypeDateFrom'), 'd.m.yyyy', null, false, true) && ret;
	ret = validateDate($('diplomaTypeDateTo'), 'd.m.yyyy', null, false, true) && ret;
	ret = validateDateInterval($('diplomaTypeDateFrom'), $('diplomaTypeDateTo'), 'yyyy', 'd.m.yyyy', '', true) && ret;
	if (ret == true) {
        var idInput = $('${id}');
	    new Ajax.Request('/nacid/control/diploma_type_ajax/save', {
            asynchronous: false,
            method: 'post',
			parameters: {
                         id : $('diplomaTypeId').value,
                         title : $('diplomaTypeTitle').value, jointDegree : $('joint_degree').checked, universitiesIds : universityIds, facultiesIds : facultyIds, eduLevel : $('diplomaTypeEduLevel').value,
                         originalEduLevel : $('originalEduLevel').value,
                         notes : $('diplomaTypeNotes').value,
                        dateFrom : $('diplomaTypeDateFrom').value,
                        dateTo : $('diplomaTypeDateTo').value,
                        bolognaCycleId : $('bolognaCycleId').value,
                        nationalQualificationsFrameworkId : $('nationalQualificationsFrameworkId').value,
                        europeanQualificationsFrameworkId : $('europeanQualificationsFrameworkId').value,
                        bolognaCycleAccessId : $('bolognaCycleAccessId').value,
                        nationalQualificationsFrameworkAccessId : $('nationalQualificationsFrameworkAccessId').value,
                        europeanQualificationsFrameworkAccessId : $('europeanQualificationsFrameworkAccessId').value,
                        type : $('diplomaTypeType').value



            },
			onSuccess: function(transport) {
				$$('#diplomaType_new_div').each(function(element) {
                    $(element).removeClassName('errorinput');
                    $(element).removeAttribute('title');
				})
				var response = transport.responseText || "no response text";
				var json = response.evalJSON(true);
                if (json.error) {
                    alert(json.errorMessage);
                } else {
                    if (parseInt(json.id) > 0) {
                        $('dipl_type_correct_message_div').show();
                        $('dipl_type_error_message_div').hide();
                    } else {
                        $('dipl_type_correct_message_div').hide();
                        $('dipl_type_error_message_div').show();
                    }

                    <%--ako ne e doctorate, ne moje da se promenq pyrviq universitet v spisyka, no ako e doctorate, moje da se promeni v diplomaType, sled tova trqbva da se update-ne i v application-a.Tova se pravi samo pri doctorate, zashtoto v normalen application, universitetite v trainingCourse-a moje da imat durga potdredba ot tezi v diplomaType-a i ne trqbva da se razmestvat!--%>
                    if (${isDoctorate} == true) {
                        updateBaseUniversityDiplomaType(json);

                    }
                    updateBaseUniversityFacultyDiplomaType(json);

                    showHideDiplomaTypeEditionDiv();
                    var universityIdInput = ${universityIdInput};
                    updateDiplomaType(universityIdInput.value);

                    $('diploma_type').value = json.id;
                    $('diploma_type').onchange();
                    toggleEducationPeriodInformation(json.educationLevel != <%=EDUCATION_LEVEL_DOCTOR_OF_SCIENCE%>);

                }
			},
			onFailure: function() {
				showHideDiplomaTypeEditionDiv();
				$('dipl_type_correct_message_div').hide();
				$('dipl_type_error_message_div').show();
			}			
		});
	}
}

function closeSubwindow(element) {
	element.removeClassName('subwindow_open');
	for (var i = 1; i < universities; i ++) {
		if ($('diplomaTypeUniversityDiv' + i) != null) {
			$('diplomaTypeUniversityDiv' + i).remove();
		}
	}
	universities = 1;
	element.hide();
}



//start of original edu level functions

function updateOriginalEduLevelsCombo() {
    var el = $("diplomaTypeCountry0");
    var applicationType = $("application_type").value;
    var countryId = el.options[el.selectedIndex].value;
    var eduLevelId = $('diplomaTypeEduLevel').value;
    new Ajax.Request('${pathPrefix}/control/original_education_level/view?type=combo&countryId=' + countryId + "&eduLevelId=" + eduLevelId + "&applicationType=" + applicationType, {
        asynchronous: false,
        onComplete: function(oXHR) {
            $("orgEduLvl").update(oXHR.responseText);
        },
        method: 'GET'
    });
};


function updateFlatNomenclatureCombo(comboName, comboType, defaultValue) {
    new Ajax.Request('${pathPrefix}/control/flat_nomenclature_ajax/view?type=' + comboType + '&addEmpty=true&name=' + comboName + '&defaultValue=' + defaultValue + '&style=brd w200', {
        asynchronous: false,
        onComplete: function(oXHR) {
            $("flat_nomenclature_id_" + comboName + "_combo").update(oXHR.responseText);
        },
        method: 'GET'
    });
};
function updateNationalQualificationsFrameworkCombo(comboName, defaultValue) {
    var countryId = $("diplomaTypeCountry0").value;
    new Ajax.Request('${pathPrefix}/control/national_qualifications_framework_ajax/view?countryId=' + countryId + '&name=' + comboName + '&defaultValue=' + defaultValue + '&style=brd w200', {
        asynchronous: false,
        onComplete: function(oXHR) {
            $(comboName + "_combo").update(oXHR.responseText);
        },
        method: 'GET'
    });
};

function originalEduLevelChanged() {
    var eduLevelId = $('originalEduLevel').value;
    if (eduLevelId == '-') {
        $('orgEduLvlTranslated').value = "";
        $('diplomaTypeEduLevel').enable();
        $(${id}_edit_edu_level).hide();
    } else {
        $('diplomaTypeEduLevel').disable();
        new Ajax.Request('${pathPrefix}/control/original_education_level/view', {
            parameters: {id : eduLevelId},
            asynchronous: false,
            onComplete: function(transport) {
                var response = transport.responseText || "no response text";
                var json = response.evalJSON(true);
                $('diplomaTypeEduLevel').value = json.educationLevel;
                $('orgEduLvlTranslated').value = json.nameTranslated;
            },
            method: 'POST'
        });
        $(${id}_edit_edu_level).show();
    }

}
function showHideOriginalEduLevelEditBox(isEdit){
    var newOriginalEduLevelDiv = "new_edu_level_div_original";
    if($(newOriginalEduLevelDiv).visible() == false){
        if ($$('.subwindow2_open').length >= 1) {
            return;
        }

        $(newOriginalEduLevelDiv).insert({before:$('afterOriginalEduLevelDiv')});
        $(newOriginalEduLevelDiv).show();
        var eduLevelName = $('originalEduLevel').options[$('originalEduLevel').selectedIndex].innerHTML;
        if (eduLevelName == '-') {
            eduLevelName = '';
        }
        var eduLevelId = $('originalEduLevel').value;
        if (eduLevelId != '-' && isEdit) {
            new Ajax.Request('${pathPrefix}/control/original_education_level/view', {
                parameters: {id : eduLevelId},
                asynchronous: false,
                onComplete: function(transport) {
                    var response = transport.responseText || "no response text";
                    var json = response.evalJSON(true);
                    $('originalEduLevelId').value = json.id;
                    $('originalEduLevelName').value = json.name;
                    $('originalEduLevelNameTranslated').value = json.nameTranslated;
                    $('originalEduLevelEduLevel').value = json.educationLevel;
                    $('originalEduLevelCountry').value = json.country;
                    $('originalEduLevelDateFrom').value = json.dateFrom;
                    $('originalEduLevelDateTo').value = json.dateTo;
                },
                method: 'POST'
            });
        } else if (!isEdit) {
            $('originalEduLevelId').value = "";
            $('originalEduLevelName').value = "";
            $('originalEduLevelNameTranslated').value = "";
            $('originalEduLevelEduLevel').value = $('diplomaTypeEduLevel').value;
            $('originalEduLevelCountry').value = $('diplomaTypeCountry0').value;
            $('originalEduLevelDateFrom').value = 'дд.мм.гггг';
            $('originalEduLevelDateTo').value = 'дд.мм.гггг';
        }
        if ($('originalEduLevelEduLevel').value != "-") {
            $('originalEduLevelEduLevel').setAttribute("disabled", "disabled");
        } else {
            $('originalEduLevelEduLevel').removeAttribute("disabled");
        }
        if ($('originalEduLevelCountry').value != "-") {
            $('originalEduLevelCountry').setAttribute("disabled", "disabled");
        } else {
            $('originalEduLevelCountry').removeAttribute("disabled");
        }
    } else {
        clearAllErrors(null);
        $(newOriginalEduLevelDiv).hide();
    }
}

function saveOriginalEduLevel() {
    clearAllErrors(null);
    ret = true;
    ret = validateComboBox($('originalEduLevelEduLevel'), true, 'Въведете степен на образование') && ret;
    ret = validateComboBox($('originalEduLevelCountry'), true, 'Въведете държава') && ret;
    //ret = validateText($('originalEduLevelName'), true, 0, 255, ${validations.allButQuote}, '${messages.err_allButQuote}') && ret;
    ret = validateDate($('originalEduLevelDateFrom'), 'd.m.yyyy', null, false, true) && ret;
    ret = validateDate($('originalEduLevelDateTo'), 'd.m.yyyy', null, false, true) && ret;
    ret = validateDateInterval($('originalEduLevelDateFrom'), $('originalEduLevelDateTo'), 'd.m.yyyy', null, '', false) && ret;
    if (ret) {
        new Ajax.Request('${pathPrefix}/control/original_education_level/save', {
            parameters: {
                id : $('originalEduLevelId').value,
                name : $('originalEduLevelName').value,
                nameTranslated : $('originalEduLevelNameTranslated').value,
                eduLevel : $('originalEduLevelEduLevel').value,
                country : $('originalEduLevelCountry').value,
                dateFrom : $('originalEduLevelDateFrom').value,
                dateTo : $('originalEduLevelDateTo').value
            },
            asynchronous: false,
            onComplete: function(transport) {
                var response = transport.responseText || "no response text";
                var json = response.evalJSON(true);
                updateOriginalEduLevelsCombo();
                $('originalEduLevel').value = json.id;
                showHideOriginalEduLevelEditBox();
                originalEduLevelChanged();

            },
            method: 'POST'
        });
    }
}
//end of original edu level functions

function showHideEditDiplomaTypeButton() {
    var name = "${name}";
    var val = $(name).value;
    if (val == '-') {
        $('editDiplomaType_'+name).hide();
    } else {
        $('editDiplomaType_'+name).show();
    }
}
showHideEditDiplomaTypeButton();
function showHideDiplomaTypeEditUniButton(el) {
    var universityNum = el.id.replace("university_id","");
    var universityId = el.options[el.selectedIndex].value;
    if (universityId == '-') {
        $('editButton' + universityNum).hide();


    } else {
        $('editButton' + universityNum).setAttribute("onClick", "showHideNewUniversityDiv(true, this.up().next(), 2, " + universityId + ", 'diplomaTypeUniversitySaved(json, " + universityNum + ")' );");
        $('editButton' + universityNum).show();
    }

}
function showHideDiplomaTypeEditUniFacultyButton(el) {
    var universityNum = el.id.replace("university_faculty_id","");
    var universityFacultyId = el.options[el.selectedIndex].value;
    var universityIdEl = $('university_id' + universityNum);
    var universityId = universityIdEl.options[universityIdEl.selectedIndex].value;
    if (universityId != '-' && universityId != '') {
        $('newUniversityFacultyButton' + universityNum).show();
        $('newUniversityFacultyButton' + universityNum).setAttribute("onClick", "showHideUniversityFacultyEditBox(false, this.up().up().next(), 2, null, " + universityId + ", 'diplomaTypeUniversityFacultySaved(json, " + universityNum + ")' );");

        if (universityFacultyId == '-' || universityFacultyId == '') {
            $('editUniversityFacultyButton' + universityNum).hide();
        } else {
            $('editUniversityFacultyButton' + universityNum).setAttribute("onClick", "showHideUniversityFacultyEditBox(true, this.up().up().next(), 2, " + universityFacultyId + ", " + universityId + ", 'diplomaTypeUniversityFacultySaved(json, " + universityNum + ")' );");
            $('editUniversityFacultyButton' + universityNum).show();
        }
    }

}


function diplomaTypeUniversitySaved(json, universityId) {
    if (parseInt(json.id) > 0) {
        showHideNewUniversityDiv(false, null, 2, 0);
        $('diplomaTypeCountry' + universityId).value = json.countryId;
        getUniversities($('diplomaTypeCountry' + universityId), '-');
        $('university_id' + universityId).value = json.id;
        $('university_id' + universityId).onchange();

    } else {
        $('university_new_correct_message_div').hide();
        $('university_new_error_message_div').show();
    }
}
function diplomaTypeUniversityFacultySaved(json, universityNum) {
    $('university_id' + universityNum).onchange();
    $('university_faculty_id' + universityNum).onchange();
}

function showSubwindowNqf(id) {
    var sw = "subwindow2";
    if ($$('.' + sw + '_open').length >= 1) {
        return;
    }
    clearAllErrors(null);
    $('new_nqf').addClassName(sw );
    $('new_nqf').addClassName(sw + '_open');
    $('new_' + id ).hide();
    $('new_nqf').show();
    $('cancel_nqf').setAttribute("onclick", "hideSubwindowNqf('" + id + "')");
    $('save_nqf').setAttribute("onclick", "saveNqf('" + id + "')");


    $('input_nqf_country').value = $('diplomaTypeCountry0').value;
    $('input_nqf').value = '';




}
function hideSubwindowNqf(id) {
    var sw = "subwindow2";
    $('new_nqf').removeClassName(sw);
    $('new_nqf').removeClassName(sw + '_open');
    $('new_' + id ).show();
    $('new_nqf').hide();
    //$('error_message_' + id).hide();//TODO:What's this???
}
function saveNqf(id) {
    clearAllErrors(null);
    var ret = true;
    ret = validateText($("input_nqf"), true, -1, -1, null, '') && ret;
    ret = validateComboBox($("input_nqf_country"), true, '') && ret;
    if (ret) {
        var n = $("input_nqf").value;
        var c = $("input_nqf_country").value;

        new Ajax.Request('${pathPrefix}/control/national_qualifications_framework_ajax/save', {
            parameters: {
                name : n,
                countryId : c
            },
            asynchronous: false,
            onComplete: function(transport) {
                var response = transport.responseText || "no response text";
                var json = response.evalJSON(true);
                if (json.error) {
                    alert(json.errorMessage);
                } else {
                    updateNationalQualificationsFrameworkCombo(id, json.id);
                    updateNationalQualificationsFrameworksOnSuccess();
                }
                hideSubwindowNqf(id);
            },
            method: 'POST'
        });
    }







}
</script>
