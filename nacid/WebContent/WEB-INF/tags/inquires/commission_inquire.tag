<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/inquires"  prefix="inquires"%>
<%@ tag import="com.nacid.bl.nomenclatures.NomenclaturesDataProvider"%>
<script type="text/javascript" src="${pathPrefix }/js/inquires.jsp"></script>
<script type="text/javascript">
var universities = ${universities_count};
var statusesCount = ${statuses_count};
function joinTypeToggle(el) {
    var div = el.id.replace("join_type","join_type_div");
	if (el[el.selectedIndex].value == "-") {
		$(div).hide();
	} else {
		$(div).show();
	}
}
function _updateLegalReasons(select, form, type) {
    var additionalName = type=='join_type' ? "join_type_" : "";
    var id = select.id.replace(additionalName + 'status_id', '');
    var legalReasonDiv = additionalName + 'legal_reason_div' + id;
    updateLegalReasons(select, form, legalReasonDiv, additionalName + id);
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
    <v:form action="${pathPrefix }/control/commission_inquire/print"
        method="post" name="InquireForm"
        skipsubmitbuttons="true">
	<v:textValidator input="start_session_id" minLength="1" maxLength="50" required="false" regex="${validations.number}" errormessage="${messages.allowsOnlyDigits}" />
	<v:textValidator input="end_session_id" minLength="1" maxLength="50" required="false" regex="${validations.number}" errormessage="${messages.allowsOnlyDigits}" />
	<v:dateValidator input="dateFrom" format="d.m.yyyy" required="false" beforeToday="true" />
    <v:dateValidator input="dateTo" format="d.m.yyyy" required="false" beforeToday="true" />
    <v:dateValidator input="appDateFrom" format="d.m.yyyy" required="false" beforeToday="true" />
    <v:dateValidator input="appDateTo" format="d.m.yyyy" required="false" beforeToday="true" />
    <v:dateIntervalValidator dateFrom="dateFrom" dateTo="dateTo" format="d.m.yyyy" />
    <v:dateIntervalValidator dateFrom="appDateFrom" dateTo="appDateTo" format="d.m.yyyy" />
    <v:dateValidator input="diplomaDateFrom" format="yyyy" required="false" beforeToday="true" emptyString="гггг"/>
    <v:dateValidator input="diplomaDateTo" format="yyyy" required="false" beforeToday="true" emptyString="гггг" />
    <v:dateIntervalValidator dateFrom="diplomaDateFrom" dateTo="diplomaDateTo" format="yyyy" />
    <inquires:inquire_submit_buttons formName="commission_inquire" />
	<div class="clr10"><!--  --></div>
    <fieldset><legend>Тип заявление</legend>
        <span class="fieldlabel2"><label>Тип заявление</label></span>
        <select id="applicationType" name="applicationType" class="brd w600" disabled="disabled">
            <option value="1" selected="selected">по чл. 06(06)</option>
            <option value="2">по чл. 17(21)</option>
        </select>
        <input type="hidden" name="applicationType" value="1">
    </fieldset>
	<fieldset><legend>Заседание</legend>
       <p>
           <span class="fieldlabel2"><label>от №</label></span> 
           <v:textinput  onkeyup="disableCommissionCalendar(this, 'number');" value="" id="start_session_id" name="start_session_id" class="brd w200 flt_lft" />
           <span class="fieldlabel2"><label>до №</label></span>
           <v:textinput  onkeyup="disableCommissionCalendar(this, 'number');" value="" id="end_session_id" name="end_session_id" class="brd w200 flt_lft" />
        </p>
        <p>
           <span class="fieldlabel2"><label>от дата</label></span> 
           <v:dateinput id="dateFrom" name="dateFrom" style="brd w200 flt_lft" value="дд.мм.гггг"  onkeyup="disableCommissionCalendar(this, 'period');"/>
           <span class="fieldlabel2"><label>до дата</label></span>
           <v:dateinput id="dateTo" name="dateTo" style="brd w200 flt_lft"  value="дд.мм.гггг" onkeyup="disableCommissionCalendar(this, 'period');"/>
        </p>
    </fieldset>
    <div class="clr"><!--  --></div>
    <fieldset><legend>Година на дипломата</legend>
        <p>
           <span class="fieldlabel2"><label>начална година</label></span> 
           <v:dateinput id="diplomaDateFrom" name="diplomaDateFrom" style="brd w200 flt_lft" value="гггг" emptyString="гггг"  />
           <span class="fieldlabel2"><label>крайна година</label></span>
           <v:dateinput id="diplomaDateTo" name="diplomaDateTo" style="brd w200 flt_lft"  value="гггг" emptyString="гггг" />
        </p>
    </fieldset>
    
	<fieldset><legend>Статус от комисия</legend>
	   <p>
           <span class="fieldlabel2"><label>Статус от комисия</label></span> 
           <nacid:combobox id="status_id0" name="status_id0" attributeName="statusesCombo" style="brd w600" onchange="_updateLegalReasons(this, $('InquireForm'));joinTypeToggleJT(this);"/>
       </p>
       <div id="legal_reason_div0">
       </div>
       <div id="join_type_jt_div0" style="display:none;">
       <p>
          <span class="fieldlabel2"><label>Връзка</label></span>
          <select class="brd w600" name="join_type0" id="join_type0" onchange="joinTypeToggle(this);">
                  <option value="-">-</option>
                  <option value="1">И</option>
                  <option value="2">НЕ</option>
          </select>
       </p>
       </div>
       <div id="join_type_div0" style="display:none;">
          <fieldset><legend>Друг наличен статус</legend>
            <p>
                <span class="fieldlabel2"><label>Друг наличен статус</label></span> 
                <nacid:combobox id="join_type_status_id0" name="join_type_status_id0" attributeName="allStatusesCombo" style="brd w600" onchange="_updateLegalReasons(this, $('InquireForm'), 'join_type');"/>
            </p>
            <div id="join_type_legal_reason_div0">
            </div>
          </fieldset>
       </div>
	</fieldset>
	
	<div class="clr10"><!--  --></div>
    <p class="flt_rgt" id="add_statuses"><a href="javascript:addStatus();">Добави статус от комисия</a></p>
    <div class="clr10"><!--  --></div>
       
	<div id="status" style="display:none">
	   <fieldset><legend>Статус от комисия</legend>
       <p class="flt_rgt"><a href="javascript:void(0);" style="color: red">Премахни</a></p>
       <div class="clr"><!--  --></div>
       <p>
           <span class="fieldlabel2"><label>Статус от комисия</label></span> 
           <nacid:combobox id="status_id" name="status_id" attributeName="statusesCombo" style="brd w600" onchange="_updateLegalReasons(this, $('InquireForm'));joinTypeToggleJT(this);"/>
       </p>
       <div id="legal_reason_div">
       </div>
       <div id="join_type_jt_div" style="display:none;">
       <p>
          <span class="fieldlabel2"><label>Връзка</label></span>
          <select class="brd w600" name="join_type" id="join_type" onchange="joinTypeToggle(this);">
                  <option value="-">-</option>
                  <option value="1">И</option>
                  <option value="2">НЕ</option>
          </select>
       </p>
       </div>
       <div id="join_type_div" style="display:none;">
          <fieldset><legend>Друг наличен статус</legend>
            <p>
                <span class="fieldlabel2"><label>Друг наличен статус</label></span> 
                <nacid:combobox id="join_type_status_id" name="join_type_status_id" attributeName="allStatusesCombo" style="brd w600" onchange="_updateLegalReasons(this, $('InquireForm'), 'join_type');"/>
            </p>
            <div id="join_type_legal_reason_div">
            </div>
          </fieldset>
       </div>
       </fieldset>
	</div>
	<div id="afterStatusDiv" style="display:none">&nbsp;</div>
	
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
    
    
    <fieldset><legend>Специалност/квалификации/ОКС по диплома</legend>
	    <inquires:speciality_fieldset type="diplomaSpeciality" />
	    <div class="clr10"><!--  --></div>
        
        <fieldset><legend>Квалификации</legend>
            <input id="diplomaQualificationIds" type="hidden" name="diplomaQualificationIds" value="" />
            <input id="diplomaQualificationNamesIds" type="hidden" name="diplomaQualificationNamesIds" value="" />
            <div id="diplomaQualificationDiv">
                <span class="fieldlabel2"><label>Избрани Квалификации</label></span> 
                <table id="selected_diplomaQualification" >
                    <tbody>
                    </tbody>
                </table>
                <div class="clr10"><!--  --></div>
            </div>
            <div class="clr"><!--  --></div>
            <div id="diplomaQualificationNamesDiv">
                <span class="fieldlabel2"><label>Избрани маски на квалификации</label></span> 
                <table id="selected_diplomaQualificationNames" >
                    <tbody>
                    </tbody>
                </table>
                <div class="clr10"><!--  --></div>
            </div>
            <p>
               <span class="fieldlabel2"><label>Квалификация</label></span> 
               <span id="diplomaQualification_span"> 
                
                    <input type="hidden" name="diplomaQualificationId" id="diplomaQualificationId" value="" />
                    <v:textinput class="brd w500" name="diplomaQualification" id="diplomaQualification"  value="" onkeydown="if(!isSpecialKeyPressed(event)) nomenclatureChanged('diplomaQualification');" />
               </span>
                  <script type="text/javascript">
                new Autocomplete('diplomaQualification', { 
                      serviceUrl:'${pathPrefix}/control/flatnomenclaturesuggest?type=' + qualificationNomenclatureId,
                      width:500,
                      onSelect:updateNomenclature
                });
                
            </script> 
               <a href="#" onclick= "addNomenclature('diplomaQualification'); return false;">Добави</a>
            </p>
        </fieldset>
        <div class="clr10"><!--  --></div>
	    <inquires:commission_inquire_subelement chosenTitle="Избрани ОКС" labelTitle="ОКС" type="diplomaEducationLevel" comboAttribute="diplomaEducationLevelsCombo" elementIds="diplomaEducationLevelIds" title="Образователно-квалифиакционни степени" selectedElements="diplomaSelectedEducationLevels" />
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
  
    <input type="hidden" name="universities_count" id="universities_count" value="${universities_count }" />
    <input type="hidden" name="statuses_count" id="status_ids_count" value="${statuses_count }" />
    <input type="hidden" name="screen" value="" />
    <input type="hidden" id="print_type" name="print_type" value="" />
    <input type="hidden" id="screen_options" name="screen_options" value="" />
    <inquires:inquire_submit_buttons formName="commission_inquire" />
    
    
</v:form>
<div class="clr20"><!--  --></div>
<script type="text/javascript">
	showHideDiv('applicantCountry');
	showHideDiv('legalReason');
	showHideDiv('diplomaSpeciality');
	showHideDiv('diplomaSpecialityNames');
	showHideDiv('diplomaQualification');
	showHideDiv('diplomaQualificationNames');
	showHideDiv('diplomaEducationLevel');
	showHideDiv('recognizedSpeciality');
	showHideDiv('recognizedSpecialityNames');
	showHideDiv('recognizedQualification');
	showHideDiv('recognizedQualificationNames');
    showHideDiv('recognizedEducationLevel');
    showHideDiv('university0');
    showHideDiv('university0Names');
    showHideDiv('org_university0Names');
</script>
<div id="table_result">
</div>
<inquires:screen_options />
<div id="inquires_screens">
</div>
