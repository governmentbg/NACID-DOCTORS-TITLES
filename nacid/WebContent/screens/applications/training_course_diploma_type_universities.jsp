
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib prefix="nacid" uri="/META-INF/nacid_taglib.tld"%>
<%@ taglib prefix="nacid_ext" uri="/META-INF/ext_nacid_taglib.tld"%>
<nacid:trainingCourseDiplomaTypeUniversities>
	<div id="joint_university${id }" style="display: block;">
	<fieldset><legend>Данни за Съвместно чуждестранно висше училище</legend>
		<div class="clr">&nbsp;</div>
		<p>
			<span class="fieldlabel"><label for="joint_university_name_bg${id }">Наименование на български</label></span> 
			<input type="hidden" name="joint_university_id${id }" id="joint_university_id${id }" value="${university_id }" />
			<v:textinput disabled="disabled" class="brd w600" name="joint_university_name_bg${id }" id="joint_university_name_bg${id }"  value="${university_name_bg }" />
		</p>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="joint_university_original_name${id }">Оригинално наименование</label></span>
			<v:textinput disabled="disabled" class="brd w600" name="joint_university_original_name${id }" id="joint_university_original_name${id }"  value="${university_original_name }" />
		</p>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="joint_university_generic_name${id}">Генерично наименование</label></span>
			<input type="hidden" name="joint_university_generic_name_id${id}" id="joint_university_generic_name_id${id}"  value="${university_generic_name_id }"/>
			<v:textinput disabled="disabled" class="brd w500" maxlength="100" name="joint_university_generic_name${id}" id="joint_university_generic_name${id}"  value="${university_generic_name }" />
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
			<span class="fieldlabel flt_lft"><label for="joint_university_url_diploma_register${id }">Официален регистър за дипломи</label></span>
			<span id="joint_university_url_diploma_register${id }" class="flt_lft">${university_url_diploma_register_link }</span>
		</p>
		<div class="clr"><!--  --></div>
	</fieldset>
	</div>
</nacid:trainingCourseDiplomaTypeUniversities>
