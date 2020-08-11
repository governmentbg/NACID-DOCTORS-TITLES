<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/inquires"  prefix="inquires"%>
<%@tag import="com.nacid.bl.nomenclatures.ApplicationStatus"%>
<%@tag import="com.nacid.bl.nomenclatures.NomenclaturesDataProvider"%>

<script type="text/javascript" src="${pathPrefix }/js/inquires.jsp"></script>
<script type="text/javascript">
var universities = ${universities_count};
var institutions = ${institutions_count};
//var statusesCount = ${status_ids_count};
//var commissionStatusesCount = ${commission_status_ids_count};
function eSignToggle(el) {
	if (el[el.selectedIndex].value == 1) {
	    $('e_sign_div').show();
	} else {
		$('e_sign_div').hide();
	}
}
function _updateLegalReasons(select, form, type) {
    var additionalName = type=='final' ? "final_" : "";
    var id = select.id.replace(additionalName + 'status_id', '');
    var legalReasonDiv = select.id.replace('status_id', 'legal_reason_div');
    updateLegalReasons(select, form, legalReasonDiv, additionalName + id);
}


function joinTypeToggle(el) {
    var div = el.id.replace("join_type","join_type_div");
    if (el[el.selectedIndex].value == "-") {
        $(div).hide();
    } else {
        $(div).show();
    }
}

function joinTypeToggleJT(el) {
    var div = el.id.replace("status_id","join_type_jt_div");
    if (el[el.selectedIndex].value == "-") {
        $(div).hide();
    } else {
        $(div).show();
    }
}
</script>
<h3 class="title"><span>Справка</span></h3>
    <v:form action="${pathPrefix }/control/common_inquire/print"
        method="post" name="InquireForm"
        backurl="${pathPrefix }/control/home" skipsubmitbuttons="true"
        additionalvalidation="validateInquireDates()">
    <v:dateValidator input="diplomaDateFrom" emptyString="гггг" format="yyyy" required="false" beforeToday="true" />
    <v:dateValidator input="diplomaDateTo" emptyString="гггг" format="yyyy" required="false" beforeToday="true" />
    <v:dateValidator input="appDateFrom" format="d.m.yyyy" required="false" beforeToday="true" />
    <v:dateValidator input="appDateTo" format="d.m.yyyy" required="false" beforeToday="true" />
    <v:dateIntervalValidator dateFrom="appDateFrom" dateTo="appDateTo" format="d.m.yyyy" />
    <v:textValidator input="start_session_id" minLength="1" maxLength="50" required="false" regex="${validations.number}" errormessage="${messages.allowsOnlyDigits}" />
    <v:textValidator input="end_session_id" minLength="1" maxLength="50" required="false" regex="${validations.number}" errormessage="${messages.allowsOnlyDigits}" />
    <v:dateValidator input="commissionDateFrom" format="d.m.yyyy" required="false" beforeToday="true" />
    <v:dateValidator input="commissionDateTo" format="d.m.yyyy" required="false" beforeToday="true" />
    <v:dateIntervalValidator dateFrom="commissionDateFrom" dateTo="commissionDateTo" format="d.m.yyyy" />
    
    
    <inquires:inquire_submit_buttons formName="common_inquire" />
	<div class="clr20"><!--  --></div>
	<div class="formClass" style="width:100%">
    <inquires:application_type_fieldset />
    <div class="clr10"><!--  --></div>
    <fieldset><legend><input id="commission_checkbox" name="commission_checkbox" value="1" type="checkbox" onclick="javascript:$('commissionDiv').toggle();" />Комисия</legend>
	   <div id="commissionDiv" style="display:none">
	    <fieldset><legend>Заседание</legend>
	       <p>
	           <span class="fieldlabel2"><label>от №</label></span> 
	           <v:textinput  onkeyup="disableCommissionCalendar(this, 'number');" value="" id="start_session_id" name="start_session_id" class="brd w200 flt_lft" />
	           <span class="fieldlabel2"><label>до №</label></span>
	           <v:textinput  onkeyup="disableCommissionCalendar(this, 'number');" value="" id="end_session_id" name="end_session_id" class="brd w200 flt_lft" />
	        </p>
	        <p>
	           <span class="fieldlabel2"><label>от дата</label></span> 
	           <v:dateinput id="commissionDateFrom" name="commissionDateFrom" style="brd w200 flt_lft" value="дд.мм.гггг"  onkeyup="disableCommissionCalendar(this, 'period');"/>
	           <span class="fieldlabel2"><label>до дата</label></span>
	           <v:dateinput id="commissionDateTo" name="commissionDateTo" style="brd w200 flt_lft"  value="дд.мм.гггг" onkeyup="disableCommissionCalendar(this, 'period');"/>
	        </p>
	    </fieldset>
	    <fieldset><legend>Статус от комисия</legend>
	       <p>
	           <span class="fieldlabel2"><label>Статус от комисия</label></span> 
	           <nacid:combobox id="commission_status_id0" name="commission_status_id0" attributeName="statusesCombo" style="brd w600" onchange="_updateLegalReasons(this, $('InquireForm'));joinTypeToggleJT(this);"/>
	       </p>
	       <div id="commission_legal_reason_div0">
	       </div>
	       <div id="commission_join_type_jt_div0" style="display:none;">
	       <p>
	          <span class="fieldlabel2"><label>Връзка</label></span>
	          <select class="brd w600" name="commission_join_type0" id="commission_join_type0" onchange="joinTypeToggle(this);">
	                  <option value="-">-</option>
	                  <option value="1">И</option>
	                  <option value="2">НЕ</option>
	          </select>
	       </p>
	       </div>
	       <div id="commission_join_type_div0" style="display:none;">
	          <fieldset><legend>Друг наличен статус</legend>
	            <p>
	                <span class="fieldlabel2"><label>Друг наличен статус</label></span> 
	                <nacid:combobox id="commission_join_type_status_id0" name="commission_join_type_status_id0" attributeName="allStatusesCombo" style="brd w500" onchange="_updateLegalReasons(this, $('InquireForm'), 'join_type');"/>
	            </p>
	            <div id="commission_join_type_legal_reason_div0">
	            </div>
	          </fieldset>
	       </div>
	    </fieldset>
	    
	    <div class="clr10"><!--  --></div>
	    <p class="flt_rgt" id="commission_add_statuses"><a href="javascript:addStatus('commission_');">Добави статус от комисия</a></p>
	    <div class="clr10"><!--  --></div>
	       
	    <div id="commission_status" style="display:none">
	       <fieldset><legend>Статус от комисия</legend>
	       <p class="flt_rgt"><a href="javascript:void(0);" style="color: red">Премахни</a></p>
	       <div class="clr"><!--  --></div>
	       <p>
	           <span class="fieldlabel2"><label>Статус от комисия</label></span> 
	           <nacid:combobox id="commission_status_id" name="commission_status_id" attributeName="statusesCombo" style="brd w600" onchange="_updateLegalReasons(this, $('InquireForm'));joinTypeToggleJT(this);"/>
	       </p>
	       <div id="commission_legal_reason_div">
	       </div>
	       <div id="commission_join_type_jt_div" style="display:none;">
	       <p>
	          <span class="fieldlabel2"><label>Връзка</label></span>
	          <select class="brd w600" name="commission_join_type" id="commission_join_type" onchange="joinTypeToggle(this);">
	                  <option value="-">-</option>
	                  <option value="1">И</option>
	                  <option value="2">НЕ</option>
	          </select>
	       </p>
	       </div>
	       <div id="commission_join_type_div" style="display:none;">
	          <fieldset><legend>Друг наличен статус</legend>
	            <p>
	                <span class="fieldlabel2"><label>Друг наличен статус</label></span> 
	                <nacid:combobox id="commission_join_type_status_id" name="commission_join_type_status_id" attributeName="allStatusesCombo" style="brd w500" onchange="_updateLegalReasons(this, $('InquireForm'), 'join_type');"/>
	            </p>
	            <div id="commission_join_type_legal_reason_div">
	            </div>
	          </fieldset>
	       </div>
	       </fieldset>
	    </div>
	    <div id="commission_afterStatusDiv" style="display:none">&nbsp;</div>
	    </div>
	</fieldset>
	</div>
	
	<div class="clr10"><!--  --></div>
	<fieldset><legend>Начин на подаване</legend>
        <p>
           <span class="fieldlabel2"><label>Начин на подаване</label></span>
           <select class="brd w600" name="eSubmited" id="eSubmited" onchange="eSignToggle(this)">
                    <option value="0">Всички</option>
                    <option value="1">Само електронно подадени</option>
            </select>
        </p>
        <div class="clr10"><!--  --></div>
        <p id="e_sign_div">
           <span class="fieldlabel2"><label>Електронен подпис</label></span>
           <select class="brd w600" name="eSigned">
                    <option value="-">-</option>
                    <option value="0">без електронен подпис</option>
                    <option value="1">с електронен подпис</option>
            </select>
        </p>
    </fieldset>
    
	<inquires:status_fieldset addLegalReasons="true" />
	<div class="clr10"><!--  --></div>
	<fieldset><legend>Дата на заявление</legend>
       <p>
           <span class="fieldlabel2"><label>от дата</label></span> 
           <v:dateinput id="appDateFrom" name="appDateFrom" style="brd w200 flt_lft" value="дд.мм.гггг" />
           <span class="fieldlabel2"><label>до дата</label></span>
           <v:dateinput id="appDateTo" name="appDateTo" style="brd w200 flt_lft"  value="дд.мм.гггг" />
       </p>
    </fieldset>
    <div class="clr10"><!--  --></div>
    
	<inquires:commission_inquire_subelement chosenTitle="Избрани Държави на заявител" labelTitle="Държава" type="applicantCountry" comboAttribute="countriesCombo" elementIds="applicantCountryIds" title="Държава на заявител" selectedElements="selectedCountries" />

    <inquires:university_fieldset />
    <inquires:company_fieldset />

        <inquires:institution_fieldset />

        <div class="clr"><!--  --></div>
    <fieldset><legend>Година на дипломата</legend>
         <p>
            <span class="fieldlabel2"><label>начална година</label></span> 
            <v:dateinput id="diplomaDateFrom" name="diplomaDateFrom" style="brd w200 flt_lft" value="гггг" emptyString="гггг"  />
            <span class="fieldlabel2"><label>крайна година</label></span>
            <v:dateinput id="diplomaDateTo" name="diplomaDateTo" style="brd w200 flt_lft"  value="гггг" emptyString="гггг" />
         </p>
    </fieldset>
        
    <fieldset><legend>Специалност/квалификации/ОКС по диплома</legend>
	    <inquires:speciality_fieldset type="diplomaSpeciality" />
	    <div class="clr10"><!--  --></div>
        <inquires:inquire_suggestion_subelement type="diplomaOriginalSpeciality" title="Специалности - оригинално наименование" chosenTitle="Избрани специалности - оригинално наименование" labelTitle="Специалност - оригинално наименование" serviceUrl="'${pathPrefix}/control/flatnomenclaturesuggest?type=' + originalSpecialityNomenclatureId" inputClass="brd w500"/>
        <div class="clr10"><!--  --></div>
        <inquires:inquire_suggestion_subelement type="diplomaQualification" title="Квалификации" chosenTitle="Избрани квалификации" labelTitle="Квалификация" serviceUrl="'${pathPrefix}/control/flatnomenclaturesuggest?type=' + qualificationNomenclatureId" inputClass="brd w500"/>
        <div class="clr10"><!--  --></div>
        <inquires:inquire_suggestion_subelement type="diplomaOriginalQualification" title="Квалификации - оригинално наименование" chosenTitle="Избрани квалификации - оригинално наименование" labelTitle="Квалификация - оригинално наименование" serviceUrl="'${pathPrefix}/control/flatnomenclaturesuggest?type=' + originalQualificationNomenclatureId" inputClass="brd w500"/>
        <div class="clr10"><!--  --></div>
        <inquires:commission_inquire_subelement chosenTitle="Избрани ОКС" labelTitle="ОКС" type="diplomaEducationLevel" comboAttribute="diplomaEducationLevelsCombo" elementIds="diplomaEducationLevelIds" title="Образователно-квалифиакционни степени" selectedElements="diplomaSelectedEducationLevels" />
        <div class="clr10"><!--  --></div>
        <inquires:inquire_suggestion_subelement type="diplomaOriginalEduLevel" title="Образователно-квалифиакционни степени - оригинал" chosenTitle="Избрани ОКС - оригиналн" labelTitle="ОКС - Оригинал" serviceUrl="'${pathPrefix}/control/flatnomenclaturesuggest?type=' + originalEduLevelNomenclatureId" inputClass="brd w500"/>
    </fieldset>
    
    <fieldset><legend>Признати специалност/квалификации/ОКС</legend>
        
        <inquires:speciality_fieldset type="recognizedSpeciality"></inquires:speciality_fieldset>
        <div class="clr10"><!--  --></div>
        
        <fieldset><legend>Квалификации</legend>
            <input id="recognizedQualificationIds" type="hidden" name="recognizedQualificationIds" value="" />
            <input id="recognizedQualificationNamesIds" type="hidden" name="recognizedQualificationNamesIds" value="" />
            <div id="recognizedQualificationDiv">
                <span class="fieldlabel2"><label>Избрани Квалификации</label></span> 
                <table id="selected_recognizedQualification" >
                    <tbody>
                    </tbody>
                </table>
                <div class="clr10"><!--  --></div>
            </div>
            <div class="clr"><!--  --></div>
            <div id="recognizedQualificationNamesDiv">
                <span class="fieldlabel2"><label>Избрани маски на квалификации</label></span> 
                <table id="selected_recognizedQualificationNames" >
                    <tbody>
                    </tbody>
                </table>
                <div class="clr10"><!--  --></div>
            </div>
            <p>
               <span class="fieldlabel2"><label>Квалификация</label></span> 
               <span id="recognizedQualification_span"> 
                
                    <input type="hidden" name="recognizedQualificationId" id="recognizedQualificationId" value="" />
                    <v:textinput class="brd w500" name="recognizedQualification" id="recognizedQualification"  value="" onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('recognizedQualification');" />
               </span>
                  <script type="text/javascript">
                new Autocomplete('recognizedQualification', { 
                      serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?type=' + qualificationNomenclatureId,
                      width:500,
                      onSelect:updateNomenclature
                });
                
            </script> 
               <a href="#" onclick= "addNomenclature('recognizedQualification'); return false;">Добави</a>
            </p>
        </fieldset>
        <div class="clr10"><!--  --></div>



        <inquires:commission_inquire_subelement chosenTitle="Избрани ОКС" labelTitle="ОКС" type="recognizedEducationLevel" comboAttribute="recognizedEducationLevelsCombo" elementIds="recognizedEducationLevelIds" title="Образователно-квалифиакционни степени" selectedElements="recognizedSelectedEducationLevels" />


    </fieldset>

    <inquires:commission_inquire_subelement chosenTitle="Избрани типове документи" labelTitle="Типове документи" type="attachmentDocumentType" comboAttribute="attachmentDocumentTypesCombo" elementIds="attachmentDocumentTypeIds" title="Типове документи" selectedElements="selectedAttachmentDocumentTypes" />
    <div class="clr10"><!--  --></div>
    <inquires:commission_inquire_subelement chosenTitle="Избрани отговорници" labelTitle="Отговорник" type="responsibleUser" comboAttribute="responsibleUsersCombo" elementIds="responsibleUserIds" title="Отговорници" selectedElements="selectedResponsibleUsers" />
    <div class="clr10"><!--  --></div>
    <inquires:commission_inquire_subelement chosenTitle="Избрани потребители" labelTitle="Потребител" type="userCreated" comboAttribute="usersCombo" elementIds="userCreatedIds" title="Потребител, създал заявлението" selectedElements="selectedUsersCreated" />

    <div class="clr10"><!--  --></div>
    <inquires:commission_inquire_subelement chosenTitle="Избрани начини на получаване на документи" labelTitle="Начин на получаване на документи" type="documentReceiveMethod" comboAttribute="documentReceiveMethodCombo" elementIds="documentRecceiveMethodIds" title="Начин на получаване на документи" selectedElements="selectedDocumentReceiveMethods" />

    <input type="hidden" name="screen" value="" />
    <input type="hidden" id="print_type" name="print_type" value="" />
    <input type="hidden" id="screen_options" name="screen_options" value="" />
    <input type="hidden" name="universities_count" id="universities_count" value="${universities_count }" />
    <input type="hidden" name="institutions_count" id="institutions_count" value="${institutions_count }" />
    <input type="hidden" name="status_ids_count" id="status_ids_count" value="${status_ids_count }" />
    <input type="hidden" name="commission_status_ids_count" id="commission_status_ids_count" value="${commission_status_ids_count }" />
    <inquires:inquire_submit_buttons formName="common_inquire" />
</v:form>
<div class="clr20"><!--  --></div>
<script type="text/javascript">
	showHideDiv('applicantCountry');
	showHideDiv('legalReason0');
	showHideDiv('diplomaSpeciality');
	showHideDiv('diplomaSpecialityNames');
    showHideDiv('diplomaOriginalSpeciality');
    showHideDiv('diplomaOriginalSpecialityNames');
    showHideDiv('diplomaOriginalQualification');
    showHideDiv('diplomaOriginalQualificationNames');
	showHideDiv('diplomaQualification');
	showHideDiv('diplomaQualificationNames');
	showHideDiv('diplomaEducationLevel');
	showHideDiv('recognizedSpeciality');
	showHideDiv('recognizedSpecialityNames');
    showHideDiv('recognizedQualification');
    showHideDiv('recognizedQualificationNames');
    showHideDiv('recognizedEducationLevel');
    showHideDiv('diplomaEducationLevel');
    showHideDiv('university0');
    showHideDiv('university0Names');
    showHideDiv('org_university0Names');
    showHideDiv('institution0');
    showHideDiv('institution0Names');
    showHideDiv('company');
    showHideDiv('companyNames');
    eSignToggle($('eSubmited'));
    applicationTypeChanged();
</script>
<div id="table_result">
</div>
<inquires:screen_options />
<div id="inquires_screens">
</div>
