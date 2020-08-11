<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<div id="screen_options_div" style="display:none;">
<form name="screen_options_form" id="screen_options_form" action="">
    <fieldset><legend>Настройки</legend>
		<p class="checkbox">
			<input name="screen_options" value="docflowNumber" id="_docflowNumber" type="checkbox" />
			<label for="_docflowNumber">Деловоден номер и дата</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="applicantNames" id="_applicantNames" type="checkbox" />
			<label for="_applicantNames">Име, Презиме и Фамилия на Заявителя</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="electronicallyApplied" id="_electronicallyApplied" type="checkbox" />
			<label for="_electronicallyApplied">Електронно подадено</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="serviceType" id="_serviceType" type="checkbox" />
			<label for="_serviceType">Тип на услугата</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="status" id="_status" type="checkbox" />
			<label for="_status">Статус на заявлението</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="docflow_status" id="_docflow_status" type="checkbox" />
			<label for="_status">Деловоден статус</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="citizenshipName" id="_citizenshipName" type="checkbox" />
			<label for="_citizenshipName">Гражданство на заявителя</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="applicationCountry" id="_applicationCountry" type="checkbox" />
			<label for="_applicationCountry">Кандидатства за държава</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="educationType" id="_educationType" type="checkbox" />
			<label for="_educationType">Вид обучение</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="documentDate" id="_documentDate" type="checkbox" />
			<label for="_documentDate">Година на издаване на дипломата</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="professionalInstitutionName" id="_professionalInstitutionName" type="checkbox" />
			<label for="_professionalInstitutionName">Обучаваща институция - ново име</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="professionalInstitutionFormerName" id="_professionalInstitutionFormerName" type="checkbox" />
			<label for="_professionalInstitutionFormerName">Обучаваща институция - старо име</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="specialities" id="_specialities" type="checkbox" />
			<label for="_specialities">Специалност по документи</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="professionalQualification" id="_professionalQualification" type="checkbox" />
			<label for="_professionalQualification">Професионална квалификация по документ</label>
		</p>
		<div class="clr"><!--  --></div>
        <p class="checkbox">
            <input name="screen_options" value="professionExperienceName" id="_professionExperienceName" type="checkbox" />
            <label for="_professionExperienceName">Стаж - Професия</label>
        </p>
        <div class="clr"><!--  --></div>
        <p class="checkbox">
            <input name="screen_options" value="professionExperienceDocuments" id="_professionExperienceDocuments" type="checkbox" />
            <label for="_professionExperienceDocuments">Стаж - Документи</label>
        </p>
        <div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="professionExperienceDuration" id="_professionExperienceDuration" type="checkbox" />
			<label for="_professionExperienceDuration">Изчислен професионален стаж</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="recognizedEducationLevel" id="_recognizedEducationLevel" type="checkbox" />
			<label for="_recognizedEducationLevel">Удостоверено квалификационно ниво</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="recognizedArticle" id="_recognizedArticle" type="checkbox" />
			<label for="_recognizedArticle">По член от Директивата</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="recognizedProfession" id="_recognizedProfession" type="checkbox" />
			<label for="_recognizedProfession">Удостоверена професия</label>
		</p>
		<div class="clr"><!--  --></div>
		<p class="checkbox">
			<input name="screen_options" value="imiCorrespondence" id="_imiCorrespondence" type="checkbox" />
			<label for="_imiCorrespondence">Свързани преписки по IMI</label>
		</p>
		<div class="clr"><!--  --></div>
		<input id="show_button" type="button" onclick="submitScreenOptions();" value="Покажи" name="sbmt" class="save flt_rgt w110"/>
        <input id="word_button" type="button" value="Word" name="sbmt" class="save flt_rgt w90" onclick="javascript: updateScreenOptionsForms();$('InquireForm').submit();"/>
    </fieldset>
</form>
</div>
<script type="text/javascript">
function submitScreenOptions() {
	updateScreenOptionsForms();
	ajaxGenerate($('InquireForm'), $('screen_options_form').action, 'inquires_screens', $('screen_options_form'));
}
function updateScreenOptionsForms() {
	var h = new Hash();
	$('screen_options_form').getInputs('checkbox', 'screen_options').each(function(element){
	    if (element.checked) {
	        h.set(element.value, element.value);
	    }
	});
	$('screen_options').value = h.values().join(";");
}
</script>