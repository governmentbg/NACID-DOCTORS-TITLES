<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="com.nacid.bl.nomenclatures.ApplicationStatus, com.nacid.web.taglib.applications.TrainingCourseEditTag"%>
<%@ page import="com.nacid.bl.nomenclatures.ApplicationDocflowStatus" %>
<script type="text/javascript">
var archiveAppDocflowStatus = <%= ApplicationDocflowStatus.APPLICATION_ARCHIVED_DOCFLOW_STATUS_CODE %>;
var postponedSubmittedDocsStatusCode = <%=ApplicationStatus.APPLICATION_POSTPONED_SUBMITTED_DOCS %>;
var emptyArchiveNumber = "<%= TrainingCourseEditTag.ARCHIVE_NUMBER_PREFIX %>";
</script>
<nacid:trainingCourseEdit>
<script type="text/javascript">

var unis = [];
var uniCnt = 0;
<c:forEach items="${applicationWebModel.trainingCourseWebModel.allUniversityWithFaculties}" var="item">
    unis[uniCnt] = [];
    unis[uniCnt]['id'] = ${item.university.id};
    unis[uniCnt]['name'] = "${fn:escapeXml(item.university.bgName)}" + ", " + "${fn:escapeXml(item.university.country)}";
    uniCnt++;
</c:forEach>

var oldArchiveNumber = "${archiveNumber}";
var universitiesCount = ${universitiesCount};
var trainingLocationsCount = ${trainingLocationsCount};

/*var oldSubmittedDocs = '<c:out value="${submittedDocs}" escapeXml="true"/>';*/
function toggleArchiveNumber(select) {
		/* 
			1)Ako e izbran status arhivirano, togava se pokazva container-a za vyvejdane na archive number i input-a se enable-va
			2)ako ne
				a)togava ako predishniq archive_number e bil prazen, togava skriva container-a s arhiven nomer, 
				b)inache pokazva containera, disable-va input-a i slaga stojnostta na stariq archiveNumber 
			
		*/ 
  		if (select[select.selectedIndex].value == archiveAppDocflowStatus) {
			$("archive_number_container").show();
		  	$("archive_number").enable();
		} else {
			if (oldArchiveNumber == emptyArchiveNumber) {
				$("archive_number_container").hide();	
			} else {
				$("archive_number_container").show();
				$("archive_number").disable();
				$("archive_number").value = oldArchiveNumber;
			}
			
		}
}
function duplicate(duplicateType) {
    $('duplicate_type').value = duplicateType;
    $('duplicateApplicationCertificateForm ').submit();
}
function toggleSubmittedDocs(select) {
    //console.debug("selectId:" + select[select.selectedIndex].value);
    if (select[select.selectedIndex].value == postponedSubmittedDocsStatusCode) {
    	//console.debug("inside show submitted_docs_container...");
        $("submitted_docs_container").show();
        $("submitted_docs").enable();
    } else {
        if (oldSubmittedDocs == "") {
        	//console.debug("oldSubmittedDocs is empty...");
            $("submitted_docs_container").hide();
        } else {
        	//console.debug("oldSubmittedDocs is not empty disabling...");
            $("submitted_docs_container").show();
            $("submitted_docs").disable();
        }
        $("submitted_docs").value = oldSubmittedDocs;
    }
}
function showHideCompetentInstitution() {
	var option = $('diploma_examination_competent_institution').options[$('diploma_examination_competent_institution').selectedIndex].value;
	if (option != "-") {
	    $('diploma_examination_competent_institution_span').show();
		$('diploma_examination_competent_institution_href').href="${pathPrefix }/control/competent_institution/view?id=" + option;
	} else {
		$('diploma_examination_competent_institution_span').hide();
	}
}
function validateArchiveNumber() {
	  if (($('application_status')[$('application_status').selectedIndex].value == archiveAppDocflowStatus) && $('archive_number').value == emptyArchiveNumber) {
		   setErrorOnInput($('archive_number'), "Трябва да въведете номенклатурен номер!");
		   return false;
		}
		return true;
}
function validateLegalReason() {
	return $('legalReason') ? validateComboBox($('legalReason'),true,'Трябва да изберете правно основание') : true;
}


function updateLegalReasons(select) {
	var appStatusId = select.options[select.selectedIndex].value;
    if (appStatusId == '-') {
        $("legal_reason").update("");
    } else {
        new Ajax.Request('${pathPrefix}/control/legal_reason_ajax?applicationType=${application_type}&onlyActive=1&appStatusId=' + appStatusId, {
            onCreate : function(oXHR) {
               setLoading($("div_form4"));
            },
            onSuccess: function(oXHR) {
                $("legal_reason").update(oXHR.responseText);
                removeLoading($("div_form4"));
            },
            onFailure : function(oXHR) {
                alert("Възникна грешка при опит за прочитане на правните основания, свързани с този статус на заявление. Моля опитайте по-късно..." + oXHR.statusText);
                $("legal_reason").update("");
                removeLoading($("div_form4"));
            },
            method: 'GET'
          });
    }
}
function resetNewTrainingInstitutionForm() {
    clearAllErrors(null);
    $('training_institution_id').value = "";
    $('training_institution_name').value = "";
    $('training_institution_country').value = "-";
    $('training_institution_city').value = "";
    $('training_institution_post_code').value = "";
    $('training_institution_address').value = "";
    $('training_institution_phone').value = "";
    $('training_institution_date_from').value = "";
    $('training_institution_date_to').value = "";
    $('selected_training_institution_universities').firstDescendant().childElements().each(function(element) {$(element).remove();});//removes all the previously selected universities
}

//start of training institutions functions
function showHideNewTrainingInstitutionDiv(insitutionNumber, isEdit) {
    var editDiv = $('training_institution_new_div');
    var openClass = "subwindow_open";
    var windowClass = "subwindow";

    if (editDiv.visible() == false) {
        if ($$('.' + openClass).length >= 1) {
            return;
        }
        resetNewTrainingInstitutionForm();
        var elementAfter = $('uniInstitutionP' + insitutionNumber).parentNode;
        elementAfter.insert({after : editDiv});
        editDiv.addClassName(openClass);
        editDiv.addClassName(windowClass);
        editDiv.show();
        if (isEdit) {
            new Ajax.Request('${pathPrefix}/control/training_institution_ajax/view?id=' + $("uniInstitutionSelect" + insitutionNumber).value, {
                onSuccess: function(transport) {
                    var response = transport.responseText || "no response text";
                    var json = response.evalJSON(true);
                    if (parseInt(json.id) > 0) {
                        $('training_institution_id').value = json.id;
                        $('training_institution_name').value = json.name;
                        $('training_institution_country').value = json.countryId;
                        $('training_institution_city').value = json.city;
                        $('training_institution_post_code').value = json.postCode;
                        $('training_institution_address').value = json.address;
                        $('training_institution_phone').value = json.phone;
//                        $('training_institution_university').value = json.uniIds;
                        $('training_institution_date_from').value = json.dateFrom;
                        $('training_institution_date_to').value = json.dateTo;

                        var unisCount = json.uniIds.length;
                        for (var i = 0; i < unisCount; i++) {
                            addTiUniversityToTable(json.uniIds[i], json.uniNames[i]);
                        }

                    } else {
                    }

                },
                onFailure : function(transport) {
                    alert("Възникна грешка. Моля опитайте по-късно..." + oXHR.statusText);
                },
                method: 'GET'
            });


        } else {
            for (var i = 0 ; i < uniCnt; i++) {
                addTiUniversityToTable(unis[i]['id'], unis[i]['name']);
            }


        }
        $('saveTrainingInstitutionLnk').setAttribute("onclick","saveTrainingInstitution(" + insitutionNumber + ");");
    } else {
        editDiv.hide();
        editDiv.removeClassName(openClass);
        editDiv.removeClassName(windowClass);


    }
}


function removeTiUniversity(universityId) {
    $('trInstUniRow' + universityId).remove();
    $('training_institution_university_ids').value = removeElementIdFromInput($('training_institution_university_ids').value, universityId);
    showHideTiSelectedUnisDiv();
}

function addTiUniversityToTable(universityId, universityName) {
    if(universityId == '-') {
        return;
    }

    if($('trInstUniRow' + universityId) != null) {
        return;
    }

    $('training_institution_university_ids').value = addElementIdToInput($('training_institution_university_ids').value, universityId);
    showHideTiSelectedUnisDiv();

    var tr = new Element('tr', {'id': 'trInstUniRow' + universityId});

    var td1 = new Element('td');
    /*var a1 = new Element('a', {'href': '${pathPrefix }/control/university/view?id='+universityId,
     'target': '_blank'});
     a1.insert(universityName);*/

    td1.insert(universityName);
    var td2 = new Element('td');
    var a2 = new Element('a', {'href': 'javascript:removeTiUniversity('+universityId+');'});
    var img = new Element('img', {'src': '${pathPrefix}/img/icon_delete.png'});
    a2.insert(img);
    td2.insert(a2);

    tr.insert(td1);
    tr.insert(td2);

    $('selected_training_institution_universities').down().insert({bottom: tr});

}


function showHideTiSelectedUnisDiv() {
    if($('training_institution_university_ids').value == '') {
        $('selectedTiDiv').hide();
    } else {
        $('selectedTiDiv').show();
    }
}
function selectTiUniversity(value, data, el) {
    var id = el.id;
    var d = data.evalJSON(true);
    $('training_institution_university').value = value;
    $('training_institution_university_id').value = d.id;
}
function addTiUniversityButtonPressed() {
    if ($('training_institution_university_id').value == '' || $('training_institution_university').value == '') {
        return;
    }
    addTiUniversityToTable($('training_institution_university_id').value, $('training_institution_university').value);
    $('training_institution_university').value = "";
    $('training_institution_university_id').value = "";

}

function saveTrainingInstitution(institutionNumber) {
    var validated = validateTrainingInstitution();
    if (!validated) {
        return;
    }
    var tiId = $('training_institution_id').value;
    new Ajax.Request('${pathPrefix}/control/training_institution_ajax/save', {
        method: 'post',
        parameters: {
            id : $('training_institution_id').value,
            name : $('training_institution_name').value,
            country : $('training_institution_country').value,
            city : $('training_institution_city').value,
            postCode : $('training_institution_post_code').value,
            address : $('training_institution_address').value,
            phone : $('training_institution_phone').value,
            dateFrom : $('training_institution_date_from').value,
            dateTo : $('training_institution_date_to').value,
            universityIds : $('training_institution_university_ids').value

        },
        onSuccess: function(transport) {
            var response = transport.responseText || "no response text";
            var json = response.evalJSON(true);

            new Ajax.Request('${pathPrefix}/control/training_institution_ajax/list?trainingCourseId=${training_course_id}', {
                onSuccess: function(transport) {
                    var response = transport.responseText || "no response text";

                    //update na vsichki trainingInstitution select boxes
                    $$( "select[id^='uniInstitutionSelect']" ).each(function(oldElement) {
                        var element = (new Element('span')).update(response);
                        element = element.down(0);
                        var id = oldElement.id.replace('uniInstitutionSelect', '');
                        element.setAttribute("id", oldElement.getAttribute("id"));
                        element.setAttribute("name", oldElement.getAttribute("name"));
                        element.setAttribute("onchange", oldElement.getAttribute("onchange"));

                        if (id != institutionNumber) {
                            element.value = oldElement.value;
                        } else {
                            var newId = tiId != "" ? oldElement.value : json.id;
                            element.value = newId;
                        }

                        $("uniInstitutionSpan" + id).update(element);

                    });


                },
                onFailure : function(transport) {
                    alert("Възникна грешка. Моля опитайте по-късно..." + oXHR.statusText);
                },
                method: 'GET'
            });


            showHideNewTrainingInstitutionDiv();

        },
        onFailure : function(transport) {
            alert("Възникна грешка. Моля опитайте по-късно..." + oXHR.statusText);
        }
    });
}

function validateTrainingInstitution() {
    var ret = true;
    ret = validateText($('training_institution_name'), true, -1, 255, ${validations.allButQuote}, '${messages.err_allButQuote}') && ret;
    ret = validateComboBox($('training_institution_country'), true, '') && ret;
    ret = validateText($('training_institution_city'), false, -1, 30, null, '') && ret;
    ret = validateText($('training_institution_post_code'), false, -1, 255, null, '') && ret;
    ret = validateText($('training_institution_phone'), false, -1, 255, null, '') && ret;
    if ($('selected_training_institution_universities').firstDescendant().childElements().length == 0) {
        setErrorOnInput($('training_institution_university'), 'Трябва да бъде добавен поне един университет!');
        ret = false;
    }
    if (!ret) {
        alert("Моля коригирайте полетата в червено!");
    }
    return ret;
}
function toggleEditTrainingInstitutionButton(id) {
    if ($('uniInstitutionSelect' + id).value == '-') {
        $('editTrainingInstitutionLnk' + id).hide();
    } else {
        $('editTrainingInstitutionLnk' + id).show();
    }
}

function toggleRecognizedDetails() {
    var appStatusElement = $('application_status');
    var appStatus = appStatusElement[appStatusElement.selectedIndex].value;
    if (appStatus == ${priznatoStatusCode} && (isRudiApplicationType || isDoctorateApplicationType)) {
        $("recognizedDetails").show();
    } else {
        $("recognizedDetails").hide();
        $("recognizedEduLevel").value = "-";
        $("recognizedProfGroup").value = "-";
        $("recognizedProfGroup_edu_area_id").value = "-";
    }
}
//end of training institutions functions
</script>

<h3 class="title"><span>Статус</span></h3>
<h3 class="names">${application_header }</h3>
<div class="clr15"><!--  --></div>
  <p class="cc">
		<input class="back" type="button" onclick="document.location.href='${pathPrefix }/control/${back_screen}/list?application_type=${application_type}&getLastTableState=1';" value="Назад" />
  </p>
  <form action="${pathPrefix }/control/duplicate_certificate/save" method="POST" name="duplicateApplicationCertificateForm " id="duplicateApplicationCertificateForm ">
      <input type="hidden" name="id" value="${application_id }"/>
      <input type="hidden" name="duplicate_type" id="duplicate_type" value=""/>
      <input type="hidden" name="application_type" id="application_type" value="${application_type}"/>
  </form>
  <v:form skipsubmitbuttons="true" method="post" name="application_status_form" action="${pathPrefix }/control/applications_status/save?type=application_status" additionalvalidation="(validateArchiveNumber() && validateLegalReason())">
      <v:hidden name="recognizedSpeciality_specialities_count" id="recognizedSpeciality_specialities_count" value="${fn:length(recognSpecs)}" />
  	<v:textValidator input="archive_number" maxLength="50" required="($('application_status')[$('application_status').selectedIndex].value == archiveAppStatus)" errormessage="Трябва да въведете номенклатурен номер!" />
    <input type="hidden" name="application_id" value="${application_id }"/>
    <fieldset><legend>Статус на заявлението</legend>
    <nacid:systemmessage name="applicationStatusMsg" />
     <c:if test="${not empty lastCommissionCalendar}">
        <p>
            <span class="flt_lft">Последно заседание на комисия, на което е разглеждано заявлението: ${lastCommissionCalendar.sessionNumber } - ${lastCommissionCalendar.dateTime }</span>
        </p>
        <div class="clr10"><!--  --></div>
     </c:if>
     <c:if test="${not empty applicationWebModel.summary}">
        <p>
            <span class="flt_lft">Мотиви: ${applicationWebModel.summary }</span>
        </p>
        <div class="clr10"><!--  --></div>
     </c:if>
     <p><span class="fieldlabel"><label for="application_status">Статус :</label><br /></span> 
      <nacid:combobox onchange="updateLegalReasons(this); toggleSubmittedDocs(this);toggleRecognizedDetails();" name="application_status" id="application_status" attributeName="applicationStatus" style="brd w400" /></p>
     <div class="clr"><!--  --></div>
	  <p id="submitted_docs_container">
            <span class="fieldlabel"><label for="submitted_docs">Представени документи :</label><br /></span>
            <textarea class="brd w600" rows="3" cols="40" name="submitted_docs" id="submitted_docs">${submittedDocs }</textarea>
      </p>
      <div class="clr"><!--  --></div>
      <p id="legal_reason">
      		<c:if test="${not empty legalReason }">
      		<span class="fieldlabel"><label for="legalReason">Правно основание</label></span>
            <nacid:combobox name="legalReason" id="legalReason" attributeName="legalReason" style="brd w400" />
            </c:if>			
	  </p>

        <div id="recognizedDetails" style="display : ${applicationWebModel.showRecognizedDetails ? 'block' : 'none'}">
            <p>
                <span class="fieldlabel"><label for="recognizedEduLevel">Призната образователна степен</label></span>
                <nacid:combobox id="recognizedEduLevel" name="recognizedEduLevel" attributeName="recognizedEduLevel" style="brd w400" />
            </p>
            <div class="clr"><!--  --></div>

            <div style="${hideOnlyForDoctorateStyle}">
                <fieldset><legend>Признати специалности</legend>
                    <input type="hidden" name="recognizedSpecialityId" id="recognizedSpecialityId" value="" />
                    <v:specialityinput id="recognizedSpeciality" class="brd w450" comboAttribute="recognizedProfGroupCombo" chooseMultiple="true" specialityIdInput="$('recognizedSpecialityId')" specialityList="${recognSpecs}" onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('recognizedSpeciality');"/>
                </fieldset>
                <div class="clr20"><!--  --></div>
                <script type="text/javascript">
                    new Autocomplete('recognizedSpeciality', {
                        serviceUrl:'${pathPrefix}/control/specialitysuggest?only_active=1',
                        width:600,
                        onSelect:updateNomenclature
                    });

                </script>
                <p>
                    <span class="fieldlabel"><label for="recognizedQualificationId">Призната квалификация</label></span>
                    <input type="hidden" name="recognizedQualificationId" id="recognizedQualificationId" value="${recognizedQualificationId }" />
                    <v:textinput class="brd w600" name="recognizedQualification" id="recognizedQualification"  value="${recognizedQualification }" onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('recognizedQualification');" />
                </p>
                <script type="text/javascript">
                    new Autocomplete('recognizedQualification', {
                        serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?only_active=1&type=' + qualificationNomenclatureId,
                        width:600,
                        onSelect:updateNomenclature
                    });

                </script>

                <div class="clr"><!--  --></div>
            </div>
            <p>
                <span class="fieldlabel"><label for="recognizedProfGroup">Признато професионално направление</label></span>
                <nacid:combobox id="recognizedProfGroup" name="recognizedProfGroup" attributeName="recognizedProfGroupCombo" style="brd w400" onchange="updateProfGroupEduArea(this)"/>
            </p>
            <div class="clr"><!--  --></div>
            <p>
                <span class="fieldlabel"><label for="recognizedProfGroup_edu_area_id">Област на образованието</label></span>
                <nacid:combobox name="recognizedProfGroup_edu_area_id" id="recognizedProfGroup_edu_area_id" attributeName="recognizedProfGroupEduAreaCombo" style="w400 brd" disabled="disabled"/>
            </p>
            <div class="clr"><!--  --></div>

        </div>

        <p><span class="fieldlabel"><label for="application_docflow_status">Деловоден статус :</label><br /></span>
            <nacid:combobox onchange="toggleArchiveNumber(this);" name="application_docflow_status" id="application_docflow_status" attributeName="applicationDocflowStatus" style="brd w400" /></p>
        <div class="clr"><!--  --></div>
        <p id="archive_number_container" ${archiveNumberContainerStyle }>
            <span class="fieldlabel"><label for="archive_number">Номенклатурен номер :</label><br /></span>
            <v:textinput class="brd w200" maxlength="50" name="archive_number" id="archive_number"  value="${archiveNumber }" additionaltext="${archiveNumberDisabled}" />
        </p>
	  <div class="clr"><!--  --></div>
		
      
      
      <p>
      	<span class="fieldlabel">
      		<a href="#" onclick="$('statusesDiv').toggle(); return false;">История на статусите</a><br />
      	</span> 
      	<div class="clr10"><!--  --></div>
        <div id="statusesDiv" style="display: none">
            <table id="statusHistory" cellpadding="3">
                <tr>
                    <th colspan="3" align="left">Статус</th>
                </tr>
                <c:forEach items="${appStatusHistoryWebmodelList }" var="item">
                    <tr <c:if test="${item.legalStatus}">class="bld"</c:if> >
                        <td>${item.statusName }</td>
                        <td>${item.dateAssigned }</td>
                        <td>
                            <c:if test="${fn:length(item.legalReason)!=0 }" >
                                на основание ${item.legalReason }
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <div class="clr20">&nbsp;</div>
            <table id="docflowStatusHistory" cellpadding="3">
                <tr>
                    <th colspan="2" align="left">Деловоден статус</th>
                </tr>
                <c:forEach items="${appDoclfowStatusHistoryWebmodelList }" var="item">
                    <tr>
                        <td>${item.statusName }</td>
                        <td>${item.dateAssigned }</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        </p>
      <div class="clr20"><!--  --></div>

        <v:submit type="submit" value="Промени статус" />
        <c:if test="${(!isDoctorateApplicationType && applicationWebModel.intAppStatusId == priznatoStatusCode) || (showDuplicateButtons)}">
            <input type="button" value="Дубликат" name="sbmt" class="docs" style="width:130px" onclick="if (confirm('Сигурни ли сте, че желаете да създадете дубликат на заявлението?')) duplicate('duplicate');">
            <input type="button" value="Очевидна фактическа грешка" name="sbmt" class="docs" style="width:290px" onclick="if (confirm('Сигурни ли сте, че желаете да промените данните на заявлението поради очевидна фактическа грешка?')) duplicate('obvious_factual_error');">
        </c:if>
    </fieldset>
  </v:form>
 
  <div class="clr20"><!--  --></div>
  <nacid:systemmessage name="universityValidityMsg" />
  <nacid:applicationUniversityValidity>
  <form method="post" id="university_validity_form${row_id }" name="university_validity_form${row_id }" action="${pathPrefix }/control/applications_status/save?type=university">
	  <input type="hidden" name="application_id" value="${application_id }"/>
	  <fieldset><legend>Проверка на ${fn:toLowerCase(universityLabel)} по седалище</legend>
	  	<nacid:systemmessage name="universityValidityMsg${university_id }" />
	    
	    <p><span class="fieldlabel2"><label>Наименование на български: </label></span>${university_name_bg }</p>
	    <div class="clr"><!--  --></div>
	    <p><span class="fieldlabel2"><label>Оригинално наименование: </label></span>${university_original_name }</p>
	    <div class="clr"><!--  --></div>
	    <p><span class="fieldlabel2"><label>Държава: </label></span>${university_country }</p>
        <div class="clr"><!--  --></div>
        <p><span class="fieldlabel2"><label>Град: </label></span>${university_city }</p>
        <div class="clr"><!--  --></div>
	    <a href="${pathPrefix }/control/university_validity/new?universityId=${university_id }&amp;applID=${application_id }&amp;applOper=edit&amp;applGroup=${back_screen}">Добави нова проверка</a>
	    <nacid:list tablePrefix="univ_validity${row_id }" skipFilters="true" attribute="universityValidityWebModel"/>
	    <div class="clr20"><!--  --></div>
	    <p>
	      <span class="fieldlabel2"><label for="university_examination_notes">Бележки:</label></span>
	      <textarea class="brd w600" rows="3" cols="40" name="university_examination_notes">${notes }</textarea>
	    </p>
	    <div class="clr20"><!--  --></div>
	    <v:submit type="submit" value="Запис на данните" />
	    <div class="clr"><!--  --></div>
	  </fieldset>
  </form>
  <div class="clr20"><!--  --></div>
  </nacid:applicationUniversityValidity>
  
  <form method="post" id="university_validity_place_form" name="university_validity_place_form" 
  		action="${pathPrefix }/control/applications_status/save?type=universityPlace">
  	<fieldset><legend>Проверка на ${fn:toLowerCase(universityLabel)} по място на провеждане на обучение</legend>
  		<c:choose> 
  			<c:when test="${universityExaminationByPlace.examinationAvailable }">
  				<nacid:systemmessage name="universityValidityByPlaceMsg"/>
  				<input type="hidden" name="trainingCourseStatusId"  value="${training_course_id }"/>
  				<input type="hidden" name="application_id"  value="${application_id }"/>
  		        <c:forEach items="${universityExaminationByPlace.universities}" var="university">
  				<fieldset><legend>${universityLabel}</legend>
  				  <p>
  				      <span class="fieldlabel2"><label>Име:</label></span><c:out value="${university.university.bgName }" />
  				  </p>
  				  <p>
  				      <span class="fieldlabel2"><label>Седалище на ${fn:toLowerCase(theUniversityLabel)}:</label></span>
	      			  <c:out value="${university.university.country }" />, <c:out value="${university.university.city }" />
	    		 </p>
	    		 <div class="clr"><!--  --></div>
	    		 <p>${theUniversityLabel} провежда обучение
                    <c:out value="${university.trainingLocation }" />
                    <c:if test="${university.hasJointDegrees }" >
                        и има съвместна образователна програма
                    </c:if>
                </p>
                </fieldset>
	    		<div class="clr"><!--  --></div>
	    		</c:forEach>
	    		
	    		<c:forEach items="${universityExaminationByPlace.trainingLocations}" var="model" varStatus="status">
                    <fieldset><legend>Проведено обучение</legend>  
                        <span class="fieldlabel2"><label>Място:</label></span><c:out value="${model.trainingLocation.trainingLocationTrainingCountry }" />, <c:out value="${model.trainingLocation.trainingLocationTrainingCity }" />
                        <div class="clr"><!--  --></div>        
                        <p class="checkbox">
                            <input ${model.trainingLocation.hasTrainingInstitution } id="hasUniInstitution${status.index }"  name="hasUniInstitution${model.trainingLocation.id }" value="1" type="checkbox"  onclick="showHideUniInstitution(this.checked, ${status.index });"/>
                            <label for="hasUniInstitution${status.index }"><b>Институцията, провела обучението, НЕ съвпада с ${fn:toLowerCase(theUniversityLabel)}, издало дипломата</b></label>
                        </p>
                        <div class="clr"><!--  --></div>
                
                        <p id="uniInstitutionP${status.index }"><span class="fieldlabel"><label for="uniInstitution${status.index }">Институция, провела обучението</label><br /></span>
                            <c:set target="${requestScope.uniInstitution}"  property="selectedKey" value="${model.trainingInstitutionId}"/>
                            <span id="uniInstitutionSpan${status.index}">
                                <nacid:combobox name="uniInstitution${model.trainingLocation.id }" id="uniInstitutionSelect${status.index}" attributeName="uniInstitution" style="brd w400" onchange="toggleEditTrainingInstitutionButton(${status.index});"/>
                            </span>
                            <span class="flt_rgt">
                                <a onclick="showHideNewTrainingInstitutionDiv(${status.index }, true);" href="javascript: void(0);" id="editTrainingInstitutionLnk${status.index }">Редакция</a>&nbsp;
                                <a onclick="showHideNewTrainingInstitutionDiv(${status.index }, false);" href="javascript: void(0);" id="newTrainingInstitutionLnk${status.index }">Нов</a>
                            </span>
                        </p>
                        <div class="clr"><!--  --></div>




                    </fieldset>
                <div class="clr"><!--  --></div>
                </c:forEach>








                <div id="training_institution_new_div" style="display:none;" class="" style="width: 900px; height: auto;">
                    <fieldset class="subwindow_f"><legend class="subwindow_l">Данни за обучаващата институция</legend>
                        <v:hidden value="" id="training_institution_id" name="training_institution_id"/>
                        <p>
                            <span class="fieldlabel"><label for="training_institution_name">Наименование</label></span>
                            <v:textinput class="brd w500" name="training_institution_name" id="training_institution_name"  />
                        </p>
                        <p>
                            <span class="fieldlabel"><label for="training_institution_country">Държава</label></span>
                            <nacid:combobox name="training_institution_country" id="training_institution_country" attributeName="newTrainingInstitutionCountry" style="w308 brd" />
                        </p>

                        <div class="clr"><!--  --></div>
                        <p>
                            <span class="fieldlabel"><label for="training_institution_city">Град</label></span>
                            <v:textinput class="brd w500" name="training_institution_city" id="training_institution_city"  value="" />
                        </p>

                        <div class="clr"><!--  --></div>
                        <p>
                            <span class="fieldlabel"><label for="training_institution_post_code">Пощенски код</label></span>
                            <v:textinput class="brd w500" name="training_institution_post_code" id="training_institution_post_code"  value="" />
                        </p>

                        <div class="clr"><!--  --></div>
                        <p>
                            <span class="fieldlabel"><label for="training_institution_address">Адрес</label></span>
                            <v:textarea class="brd w500" name="training_institution_address" id="training_institution_address"  value="" />
                        </p>

                        <div class="clr"><!--  --></div>
                        <p>
                            <span class="fieldlabel"><label for="training_institution_phone">Телефон</label></span>
                            <v:textinput class="brd w500" name="training_institution_phone" id="training_institution_phone"  value="" />
                        </p>

                        <div class="clr"><!--  --></div>
                        <input type="hidden" name="training_institution_university_ids" id="training_institution_university_ids" />
                        <div id="selectedTiDiv">
                            <p><span class="fieldlabel2"><label for="selected_training_institution_universities">Избрани висши училища</label><br /></span>
                            <table id="selected_training_institution_universities">
                                <tbody>

                                </tbody>
                            </table>
                        </div>
                        <div class="clr"><!--  --></div>
                        <p>
                            <input type="hidden" name="training_institution_university_id" id="training_institution_university_id">
                            <span class="fieldlabel"><label for="training_institution_university">Университет</label></span>
                            <span class="flt_rgt">
                                <a id="addTiUniversity"  href="javascript:addTiUniversityButtonPressed();">Добави</a>
                            </span>
                            <v:textinput class="brd w400" maxlength="100" name="training_institution_university" id="training_institution_university"  value="" onkeydown="if (!isSpecialKeyPressed(event)) $('training_institution_university_id').value = ''"/>
                            <script type="text/javascript">
                                new Autocomplete('training_institution_university', {
                                    serviceUrl:'${pathPrefix}/control/universitysuggest',
                                    width:400,
                                    onSelect:selectTiUniversity
                                });
                            </script>
                        </p>

                        <v:hidden id="training_institution_date_from" name="training_institution_date_from" value=""/>
                        <v:hidden id="training_institution_date_to" name="training_institution_date_to" value=""/>

                        <a href="javascript:void(0);" onclick="" style="margin-left: 45%;" id="saveTrainingInstitutionLnk">Запис</a>
                        <a href="javascript:void(0);" onclick="showHideNewTrainingInstitutionDiv();" style="margin-left: 20px;" id="cancelTrainingInstitutionLnk">Отказ</a>
                    </fieldset>
                </div>














	    		<div class="clr10"><!--  --></div>
	    		<p class="checkbox">
                    <input ${universityExaminationByPlace.recognized } id="university_validity_place_recognized" name="university_validity_place_recognized" value="1" type="checkbox" />
                    <label for="university_validity_place_recognized"><b>легитимно по място на обучение</b></label>
                </p>
                <div class="clr"><!--  --></div>
      			<script type="text/javascript">
      				function showHideUniInstitution(show, index) {
						if(show){ $('uniInstitutionP' + index).show();}
						else{ $('uniInstitutionP' + index).hide();}
          			}
          			for (var i = 0; i < trainingLocationsCount; i++) {
          				showHideUniInstitution($('hasUniInstitution' + i).checked, i);
              		}
      				
      			</script>
	            
	    		
	    		<div class="clr20"><!--  --></div>
    			<v:submit type="submit" value="Запис на данните" />
    			<div class="clr"><!--  --></div>
    		</c:when>
    		<c:otherwise>
    			За да можете да продължите, е необходимо да маркирате направена проверка на висше училище на предходния етап (по-горе), както и при тази проверка ${fn:toLowerCase(theUniversityLabel)} да е признато по седалище.
    		</c:otherwise>
    	</c:choose>

  	</fieldset>
  </form>
 
  <nacid:diplomaExaminationEdit>
  <div class="formClass">
  <fieldset > <legend class="noForm">Провека на дипломата</legend>
  
  <v:form method="post" action="${pathPrefix }/control/applications_status/save?type=diploma_examination" name="diploma_examination" skipsubmitbuttons="true">
    
    	<nacid:systemmessage name="diplomaExamMsg" />
	    <p><span class="fieldlabel2"><label>Вид диплома: </label></span>${diploma_type_title }</p>
      <div class="clr"><!--  --></div>
      <input type="hidden" name="application_id" value="${application_id }"/>

      <v:dateValidator input="diploma_examination_date" format="${validations.date }" required="false"  beforeToday="true" />
	
	
	    <p><span class="fieldlabel2"><label for="diploma_examination_date">Дата на проверката</label></span> 
	      <v:dateinput name="diploma_examination_date" id="diploma_examination_date" value="${diploma_examination_date }" style="brd w100" />
	    </p>
	    <div class="clr20"><!--  --></div>
      <p><span>${messages.information_sources }</span></p>
      <div class="clr"><!--  --></div>
      <p>
        <span class="fieldlabel2 flt_lft"><label for="diploma_examination_competent_institution">${messages.national_competent_institution } :</label></span> 
        <nacid:combobox name="diploma_examination_competent_institution" id="diploma_examination_competent_institution" attributeName="institutionsCombo" style="brd w500 flt_lft"  onchange="showHideCompetentInstitution();"/>
        <span class="flt_lft pl10" style="${diploma_examination_competent_institution_style }" id="diploma_examination_competent_institution_span">
          <a id="diploma_examination_competent_institution_href" href="${pathPrefix }/control/competent_institution/view?id=${diploma_examination_competent_institution_id }" title="Преглед" target="_blank">
            <img src="${pathPrefix }/img/icon_view.png" />
          </a>
        </span>
      </p>

      <div class="clr"><!--  --></div>
      <c:forEach items="${diploma_examination_universities }" var="uniName">
        <p><span class="fieldlabel2"><label>Регистър на ${fn:toLowerCase(theUniversityLabel)}: </label></span>${uniName }</p>
        <div class="clr"><!--  --></div>
      </c:forEach>
	    <p class="checkbox">
	      <input ${diploma_examination_institutionCommunicated } id="diploma_examination_institutionCommunicated" name="diploma_examination_institutionCommunicated" value="1" type="checkbox" />
	      <label for="diploma_examination_institutionCommunicated"><b>${messages.dipl_exam_inst_comm }</b></label>
	    </p>
	    <div class="clr"><!--  --></div>
      <p class="checkbox">
	      <input ${diploma_examination_universityCommunicated } id="diploma_examination_universityCommunicated" name="diploma_examination_universityCommunicated" value="1" type="checkbox" />
	      <label for="diploma_examination_universityCommunicated"><b>Осъществена е комуникация с ${fn:toLowerCase(theUniversityLabel)}</b></label>
      </p>
      <div class="clr"><!--  --></div>
      <p class="checkbox">
          <input ${diploma_examination_foundInRegister } id="diploma_examination_foundInRegister" name="diploma_examination_foundInRegister" value="1" type="checkbox" />
          <label for="diploma_examination_foundInRegister"><b>${messages.dipl_exam_found_in_register }</b></label>
      </p>
      <div class="clr"><!--  --></div>
	    <p class="checkbox">
	      <input ${diploma_examination_recognized } id="diploma_examination_recognized" name="diploma_examination_recognized" value="1" type="checkbox" />
	      <label for="diploma_examination_recognized"><b>${messages.dipl_exam_recognized }</b></label>
	    </p>
      <div class="clr"><!--  --></div>
	    <p>
	      <span class="fieldlabel2"><label for="diploma_examination_notes">Бележки:</label></span>
	      <textarea class="brd w600" rows="3" cols="40" name="diploma_examination_notes" id="diploma_examination_notes">${diploma_examination_notes }</textarea>
	    </p>
	    <div class="clr20"><!--  --></div>
	    <v:submit type="submit" value="Запис на данните" />
	    <div class="clr"><!--  --></div>
	</v:form>    
	
		<div class="clr20"><!--  --></div>
  
<c:if test="${diplExamTableWebModel != null}">
	    <nacid:list attribute="diplExamTableWebModel" tablePrefix="diplExam" />

	<script type="text/javascript">


		/*
 		* маха линковете за сортиране на таблицата
 		*/
 		$$('#diplExammain_table td[class="dark"] > a').each(function(anchor) {
  			anchor.up(0).innerHTML = anchor.innerHTML; 
 		});
		$$('#diplExammain_table a[title="Преглед"]').each(function(link) {

			var cell = link.ancestors()[0];
			var row = cell.ancestors()[0];
			var fileName = (row.childElements())[4].innerHTML;

			
  			var old = link.href;
  			var ids = old.indexOf('id=') + 3;
  			var id = old.substring(ids, old.length);
  			link.href = '${pathPrefix }/control/dipl_exam_attachment/'+fileName+'?id='+id;

			var src = '${pathPrefix }/control/dipl_exam_attachment/view?width=${messages.imgPreviewWidth }&id='+id;
			(row.childElements())[6].innerHTML = '<img src="'+src+'" alt=""/>';
	
		});

		document.diplExamtableForm1.action = '${backUrlDiplExam }';
		<%--document.diplExamtableForm2.action = '${backUrlDiplExam }';--%>

	</script>
</c:if>
	    
    </fieldset>
  </div>
  </nacid:diplomaExaminationEdit>
  
	<div class="formClass">
		<fieldset><legend class="noForm">Напомняния</legend>
			<nacid:list attribute="eventsTableWebModel" tablePrefix="events" />
			
			<script type="text/javascript">
				/*
 				* маха линковете за сортиране на таблицата
 				*/
 				$$('#eventsmain_table td[class="dark"] > a').each(function(anchor) {
  					anchor.up(0).innerHTML = anchor.innerHTML; 
 				});

				document.eventstableForm1.action = '${backUrlEvents }';
				<%--document.eventstableForm2.action = '${backUrlEvents }';--%>

			</script>
			
		</fieldset>
	</div>

</nacid:trainingCourseEdit>
<div class="clr10"><!--  --></div>
<p class="cc">
		<input class="back" type="button"
			onclick="document.location.href='${pathPrefix }/control/${back_screen}/list?getLastTableState=1';"
			value="Назад" />
</p>

<script type="text/javascript">
/*
 * enable-va checkboxovete za колоната "маркиран" и им слага name = university_validity
 */
 for (var i = 0; i < universitiesCount; i++) {
	 $$('#univ_validity' + i + 'main_table tbody > tr > td:nth-child(2)').each(function(element) {
		 if (element != null) {
		     var select = element.childElements()[0];
		     var id = element.up(0).down(0).next(1).innerHTML;
		     select.enable();
		     select.name = "university_validity";
		     select.value = id;
	 	}
	});
}; 

//Pokazva/skriva input-a sys submittedDocs
var oldSubmittedDocs = $('submitted_docs').innerHTML;
toggleSubmittedDocs($('application_status'));

//alert($$( "select[id^='uniInstitutionSelect']").size());
$$( "select[id^='uniInstitutionSelect']" ).each(function(element) {
    var id = element.id.replace('uniInstitutionSelect', '');
    toggleEditTrainingInstitutionButton(id);
});

</script>
