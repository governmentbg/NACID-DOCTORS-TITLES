<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens_regprof/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/inquires"  prefix="inquires"%>


<%@page import="com.nacid.bl.nomenclatures.EducationLevel"%>
<%@page import="com.nacid.bl.nomenclatures.regprof.EducationType"%>
<script type="text/javascript" src="${pathPrefix }/js/inquires.jsp"></script>
<script type="text/javascript">
//var institutions = 2;
function eSignToggle(el) {
	if (el[el.selectedIndex].value == 1) {
	    $('e_sign_div').show();
	} else {
		$('e_sign_div').hide();
	}
}

function educationTypeChanged(el) {
	var val = el[el.selectedIndex].value;
	if (isSecondaryEducationType(val)) {
		$('eduTypeSecondaryDiv').show();
		$('recognizedSecondaryQualificationDegree').show();
		$('eduTypeHigherDiv').hide();
		$('recognizedHigherEduLevel').hide();
		
		
		
	} else if (isHigherEducationType(val)) {
		$('eduTypeSecondaryDiv').hide();
		$('recognizedSecondaryQualificationDegree').hide();
		$('eduTypeHigherDiv').show();
		$('recognizedHigherEduLevel').show();
	} else if (val == '-') {
		$('eduTypeSecondaryDiv').hide();
		$('recognizedSecondaryQualificationDegree').hide();
		$('eduTypeHigherDiv').hide();
		$('recognizedHigherEduLevel').hide();
	} else {
		alert("Непознат тип обучение");
	}
}


function isSecondaryEducationType(id) {
	return id == <%= EducationType.EDU_TYPE_SECONDARY %> || id == <%= EducationType.EDU_TYPE_SECONDARY_PROFESSIONAL %>;
}
function isHigherEducationType(id) {
	return id == <%= EducationType.EDU_TYPE_SDK %> || id == <%= EducationType.EDU_TYPE_HIGH %>;
}

function addInstitution() {
	var institutions = parseInt($('institutions_count').value) + 1;
    var el = $('institution_div').clone(true);
    el.id = el.id.replace("institution", "institution" + institutions);
    $('afterInstitutionsDiv').insert({before:el});
    el.show();
    /* Preobarzuva ID-tata i name=ovete na inputite/selectovete/textbox-a, kato im slaga po edno jointUniversityId v kraq*/
    $$('#institution' + institutions + '_div select', '#institution' + institutions + '_div span', '#institution' + institutions + '_div input', '#institution' + institutions + '_div div', '#institution' + institutions + '_div table').each(function(element) {
        //console.log(element.id);
        if (element.name != null && element.name != "") {
            element.name = element.name.replace("institution", "institution" + institutions);
        }
        if (element.id != null && element.id != "") {
            element.id = element.id.replace("institution", "institution" + institutions) ;
        }
    });
    /*Preobrazuva href-a na linka "premahni"*/
    $$('#institution' + institutions + '_div a[href="javascript:void(0);"]')[0].setAttribute('href', "javascript:$('institution" + institutions + "_div').remove();void(0);");
    
    $('institution' + institutions + 'Name').setAttribute('onkeydown', "if(!isSpecialKeyPressed(event)) nomenclatureChanged('institution" + institutions + "Name');");
    $('institution' + institutions + 'OldName').setAttribute('onkeydown', "if(!isSpecialKeyPressed(event)) nomenclatureChanged('institution" + institutions + "OldName');");
    //Linka-a na dobavi
    $$('#institution' + institutions + '_div a[href="#"]')[0].setAttribute('href', "javascript:if ($('institution" + institutions + "OldNameId').value != '') addNomenclature('institution" + institutions + "OldName'); $('institution" + institutions + "Name').disable(); void(0);");
    

    
    new Autocomplete('institution' + institutions + 'Name', { 
        serviceUrl:'${pathPrefix}/control/professionalinstitutionsuggest',
        width:500,
        onSelect:updateNomenclature,
        useBadQueriesCache:false
    });

    var val = 'institution' + institutions + 'NameId';
    new Autocomplete('institution' + institutions + 'OldName', { 
        serviceUrl:'${pathPrefix}/control/professionalinstitutionsuggest?formername=1',
        width:500,
        onSelect:updateNomenclature,
        useBadQueriesCache:false,
        dynamicParameters: function (){
            return {institution : $(val).value};
        }
    });

    institutions++;
    $('institutions_count').value = institutions;
}
function _updateDirectiveArticleItems(select, form) {
	var id = select.id.replace('directive_article', ''); 
	updateDirectiveArticleItems(select, form, 'item_id_directive_article' + id, id);
}
function addDirectiveArticle() {
	
	var articlesCnt = parseInt($('directive_articles_count').value) + 1;
    var el = $('directive_article_div').clone(true);
    el.id = el.id.replace("directive_article_div", "directive_article_div" + articlesCnt);
    el.show();
    $('after_articles_div').insert({before:el});

    $$('#directive_article_div' + articlesCnt + ' select', '#directive_article_div' + articlesCnt + ' div').each(function(element) {
        if (element.name != null && element.name != "") {
            element.name = element.name.replace("directive_article", "directive_article" + articlesCnt);
        }
        if (element.id != null && element.id != "") {
            element.id = element.id.replace("directive_article", "directive_article" + articlesCnt) ;
        }
    });

    articlesCnt++;
    $('directive_articles_count').value = articlesCnt;
    
}
function addRegProfStatus() {
	var statusesCnt = parseInt($('status_ids_count').value);
	var el = $('status').clone(true);
	el.id = el.id + statusesCnt;
	el.show();
	$('afterstatusdiv').insert({before:el});
	$$('#status' + statusesCnt + ' select', '#status' + statusesCnt + ' div').each(function(element) {
        if (element.name != null && element.name != "") {
            element.name = element.name + statusesCnt;
        }
        if (element.id != null && element.id != "") {
            element.id = element.id + statusesCnt;
        }
    });
	$('status_ids_count').value = statusesCnt + 1;
}
</script>
<h3 class="title"><span>Справка</span></h3>
<v:form action="${pathPrefix }/control/common_inquire/print"
        method="post" name="InquireForm"
        backurl="${pathPrefix }/control/home" skipsubmitbuttons="true">
        <v:dateValidator input="appDateFrom" format="d.m.yyyy" required="false" beforeToday="true" />
    	<v:dateValidator input="appDateTo" format="d.m.yyyy" required="false" beforeToday="true" />
    	<v:dateValidator input="service_type_date" format="d.m.yyyy" required="false" beforeToday="false" />
    	<%--<v:dateValidator input="diplomaDateFrom" emptyString="гггг" format="yyyy" required="false" beforeToday="true" />
    	<v:dateValidator input="diplomaDateTo" emptyString="гггг" format="yyyy" required="false" beforeToday="true" />--%>
    	
    	<inquires:inquire_submit_buttons formName="common_inquire" />
    	<div class="clr20"><!--  --></div>
    	
    	
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
	    <div class="clr20"><!--  --></div>
	    <fieldset><legend>Тип услуга</legend>
	        <p>
	           <span class="fieldlabel2"><label>Тип услуга</label></span>
	           <nacid:combobox id="service_type" name="service_type" attributeName="serviceTypeCombo" style="brd w600" />
	        </p>
	    </fieldset>
        <fieldset><legend>Заявление за апостил</legend>
            <p>
                <span class="fieldlabel2"><label for="apostille_application">Заявление за апостил</label></span>
                <input type="checkbox" id="apostille_application" name="apostille_application" value="1" />
            </p>
        </fieldset>
	    <fieldset><legend>Срок на изпълнение</legend>
			<p>
	    		<span class="fieldlabel2"><label>до дата</label></span>
	    		<v:dateinput id="service_type_date" name="service_type_date" style="brd w200 flt_lft" value="дд.мм.гггг"  />
	    	</p>
	    </fieldset>


		<inquires:status_fieldset addLegalReasons="false"/>


	    <div class="clr10"><!--  --></div>
		<fieldset><legend>Дата на заявление</legend>
	       <p>
	           <span class="fieldlabel2"><label>от дата</label></span> 
	           <v:dateinput id="appDateFrom" name="appDateFrom" style="brd w200 flt_lft" value="дд.мм.гггг" />
	           <span class="fieldlabel2"><label>до дата</label></span>
	           <v:dateinput id="appDateTo" name="appDateTo" style="brd w200 flt_lft"  value="дд.мм.гггг" />
	       </p>
	    </fieldset>
	    
	    
	    <inquires:commission_inquire_subelement chosenTitle="Избрани Държави" labelTitle="Кандидатства за държава" type="applicantCountry" comboAttribute="countriesCombo" elementIds="applicantCountryIds" title="Държава" selectedElements="selectedCountries" />
		<div class="clr10"><!--  --></div>
		<fieldset><legend><input id="education_checkbox" name="education_checkbox" value="1" type="checkbox" onclick="javascript:$('educationDiv').toggle();" />Обучение</legend>
	   		<div id="educationDiv" style="display:none">
	   			<inquires:inquire_regprof_professional_institution_fieldset />
	   			
	   			<%--<div class="clr"><!--  --></div>
			    <fieldset><legend>Година на дипломата</legend>
			         <p>
			            <span class="fieldlabel2"><label>начална година</label></span> 
			            <v:dateinput id="diplomaDateFrom" name="diplomaDateFrom" style="brd w200 flt_lft" value="гггг" emptyString="гггг"  />
			            <span class="fieldlabel2"><label>крайна година</label></span>
			            <v:dateinput id="diplomaDateTo" name="diplomaDateTo" style="brd w200 flt_lft"  value="гггг" emptyString="гггг" />
			         </p>
			    </fieldset>--%>

                <inquires:commission_inquire_subelement chosenTitle="Избрани видове документи" labelTitle="Видове документи" type="educationDocumentType" comboAttribute="educationDocumentTypesCombo" elementIds="educationDocumentTypeIds" title="Видове документи за образование" selectedElements="selectedЕducationDocumentTypes" />


                <div class="clr10"><!--  --></div>
			    <p>
	           		<span class="fieldlabel2"><label>Вид обучение</label></span> 
	           		<nacid:combobox id="education_type" name="education_type" attributeName="educationTypesCombo" style="brd w600" onchange="educationTypeChanged(this);"/>
	       		</p>
	       		<div id="eduTypeSecondaryDiv" style="display:none;">
	       			<inquires:inquire_suggestion_subelement type="secondaryQualification" title="Професионална квалификация средно" chosenTitle="Избрани квалификации" labelTitle="Квалификация" serviceUrl="'${pathPrefix}/control/flatnomenclaturesuggest?type=' + secondaryProfessionalQualificationNomenclatureId" inputClass="brd w500"/>
	       			<div class="clr10"><!--  --></div>
	       			<inquires:inquire_suggestion_subelement type="secondarySpeciality" title="Специалност средно" chosenTitle="Избрани специалности" labelTitle="Специалност" serviceUrl="'${pathPrefix}/control/flatnomenclaturesuggest?type=' + secondarySpecialityNomenclatureId" inputClass="brd w500"/>
	       		</div>
	       		<div class="clr10"><!--  --></div>
	       		<div id="eduTypeHigherDiv" style="display:none;">
	       			<inquires:inquire_suggestion_subelement type="higherQualification" title="Професионална квалификация висше" chosenTitle="Избрани квалификации" labelTitle="Квалификация" serviceUrl="'${pathPrefix}/control/flatnomenclaturesuggest?type=' + higherProfessionalQualificationNomenclatureId" inputClass="brd w500"/>
	       			<div class="clr10"><!--  --></div>
	       			<inquires:inquire_suggestion_subelement type="higherSpeciality" title="Специалност висше" chosenTitle="Избрани специалности" labelTitle="Специалност" serviceUrl="'${pathPrefix}/control/flatnomenclaturesuggest?type=' + higherSpecialityNomenclatureId" inputClass="brd w500"/>
	       		</div>
	   		</div>
	   	</fieldset>	    
	    <div class="clr10"><!--  --></div>
	    <fieldset><legend><input id="experience_checkbox" name="experience_checkbox" value="1" type="checkbox" onclick="javascript:$('experienceDiv').toggle();" />Стаж</legend>
	   		<div id="experienceDiv" style="display:none">
	   			<inquires:inquire_suggestion_subelement type="experienceProfession" title="Професия стаж" chosenTitle="Избрани професии" labelTitle="Професия стаж" serviceUrl="'${pathPrefix}/control/flatnomenclaturesuggest?type=' + professionExperienceId" inputClass="brd w500"/>

                <inquires:commission_inquire_subelement chosenTitle="Избрани видове документи за стаж" labelTitle="Видове документи за стаж" type="experienceDocumentType" comboAttribute="experienceDocumentTypesCombo" elementIds="experienceDocumentTypeIds" title="Видове документи за стаж" selectedElements="selectedExperienceDocumentTypes" />
	   		</div>

	   	</fieldset>
		<div style="display:none;" id="recognizedHigherEduLevel" >
	   	<inquires:commission_inquire_subelement chosenTitle="Избрани квалификационни нива" labelTitle="Удостоверено квалификационно ниво" type="higherRecognizedEduLevel" comboAttribute="eduLevels" elementIds="higherRecognizedEduLevelIds" title="Удостоверено квалификационно ниво" selectedElements="higherRecognizedEduLevels" />
	   	</div>
	   	<div class="clr10"><!--  --></div>
	   	<div style="display:none;" id="recognizedSecondaryQualificationDegree" >
	   	<inquires:commission_inquire_subelement chosenTitle="Избрани квалификационни нива" labelTitle="Удостоверено квалификационно ниво" type="secondaryRecognizedQualificationDegree" comboAttribute="qualificationDegrees" elementIds="secondaryRecognizedQualificationDegreeIds" title="Удостоверено квалификационно ниво" selectedElements="secondaryRecognizedQualificationDegrees" />
	   	</div>
	   	<div class="clr10"><!--  --></div>
	   	<fieldset><legend>Член от директивата</legend>
		   <p>
	           <span class="fieldlabel2"><label>Член от директивата</label></span> 
	           <nacid:combobox id="directive_article0" name="directive_article0" attributeName="directivesCombo" style="brd w600" onchange="_updateDirectiveArticleItems(this, $('InquireForm'));"/>
	       </p>
	       <div id="item_id_directive_article0">
	       </div>
        </fieldset>
	   	<div class="clr10"><!--  --></div>
    		<p class="flt_rgt" id="add_status"><a href="javascript:addDirectiveArticle();">Добави член</a></p>
    	<div class="clr10"><!--  --></div>
    	<div id="directive_article_div" style="display:none;">
    	   <fieldset><legend>Член от директивата</legend>
    		 <p class="flt_rgt"><a href="#" onclick="this.parentNode.parentNode.parentNode.remove();return false;" style="color: red">Премахни</a></p>
       		 <div class="clr"><!--  --></div>
			 <p>
		         <span class="fieldlabel2"><label>Член от директивата</label></span> 
		         <nacid:combobox id="directive_article" name="directive_article" attributeName="directivesCombo" style="brd w600" onchange="_updateDirectiveArticleItems(this, $('InquireForm'));"/>
		     </p>
	         <div id="item_id_directive_article">
	         </div>
           </fieldset>
    	</div>
    	<div id="after_articles_div"></div>
	   	<inquires:inquire_suggestion_subelement type="recognizedProfession" title="Удостоверени професии" chosenTitle="Избрани професии" labelTitle="Удостоверени професии" serviceUrl="'${pathPrefix}/control/flatnomenclaturesuggest?type=' + professionId" inputClass="brd w500"/>
	   	
	   	<div class="clr10"><!--  --></div>
	   	<inquires:inquire_suggestion_subelement type="imiCorrespondence" title="Свързана преписка по IMI" chosenTitle="Избрани свързани преписки по IMI" labelTitle="Свързана преписка по IMI" serviceUrl="" inputClass="brd w500"/>
	   	
	   	<div class="clr10"><!--  --></div>
	    <inquires:commission_inquire_subelement chosenTitle="Избрани Фирми" labelTitle="Подател фирма" type="representativeCompany" comboAttribute="companiesCombo" elementIds="representativeCompanyIds" title="Подател фирма" selectedElements="selectedRepresentativeCompanies" />

        <div class="clr10"><!--  --></div>
        <inquires:commission_inquire_subelement chosenTitle="Избрани типове документи" labelTitle="Типове документи" type="attachmentDocumentType" comboAttribute="attachmentDocumentTypesCombo" elementIds="attachmentDocumentTypeIds" title="Типове документи" selectedElements="selectedAttachmentDocumentTypes" />

	   	<input type="hidden" name="institutions_count" id="institutions_count" value="1" />
	   	<input type="hidden" name="directive_articles_count" id="directive_articles_count" value="1" />
	   	<input type="hidden" name="status_ids_count" id="status_ids_count" value="1" />
        <input type="hidden" name="screen" value="" />
        <input type="hidden" id="print_type" name="print_type" value="" />
        <input type="hidden" id="screen_options" name="screen_options" value="" />
	   	<inquires:inquire_submit_buttons formName="common_inquire" />
</v:form>



<script type="text/javascript">
	eSignToggle($('eSubmited'));
</script>

<div id="table_result">
</div>
<div class="clr20">&nbsp;</div>
<inquires:regprof_screen_options />
<div class="clr20">&nbsp;</div>
<div id="inquires_screens">
</div>

<%@ include file="/screens_regprof/footer.jsp"%>