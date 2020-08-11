<%@ tag pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags/inquires"  prefix="inquires"%>
<script type="text/javascript" src="${pathPrefix }/js/inquires.jsp"></script>

<h3 class="title"><span>Справка по заявител</span></h3>
    <v:form action="${pathPrefix }/control/applicant_inquire/print"
        method="post" name="InquireForm"
        backurl="${pathPrefix }/control/home" skipsubmitbuttons="true">
	<v:dateValidator input="dateFrom" format="d.m.yyyy" required="false" beforeToday="true" />
    <v:dateValidator input="dateTo" format="d.m.yyyy" required="false" beforeToday="true"/>
    <v:dateIntervalValidator dateFrom="dateFrom" dateTo="dateTo" format="d.m.yyyy" />
	

	<inquires:inquire_submit_buttons formName="applicant_inquire" />

    <div class="clr10"><!--  --></div>
    <inquires:application_type_fieldset />
    <div class="clr10"><!--  --></div>
	<fieldset><legend>Заявител</legend>
	   <fieldset><legend>Физическо лице</legend>
            <div class="flt_rgt">
                <div class="filterElement">
                    <label for="applicant_fname">Име</label><br />
                    <v:textinput  class="brd" name="applicant_fname" value="" />
                </div>
                <div class="filterElement">
                    <label for="applicant_sname">Презиме</label><br />
                    <v:textinput  class="brd" name="applicant_sname" value="" />
                </div>
                <div class="filterElement">
                    <label for="applicant_lname">Фамилия</label><br />
                    <v:textinput  class="brd" name="applicant_lname" value="" />
                </div>
                <div class="filterElement">
                    <label for="applicant_personalId">Персонален идентификатор</label><br />
                    <v:textinput  class="brd" name="applicant_personalId" value="" />
                </div>
            </div>
        </fieldset>
        <fieldset><legend>Юридическо лице</legend>
            <div class="flt_rgt">
                <div class="filterElement">
                    <label for="applicant_company_name">Име</label><br />
                    <v:textinput  class="brd" name="applicant_company_name" value="" />
                </div>
                <div class="filterElement">
                    <label for="applicant_company_eik">ЕИК</label><br />
                    <v:textinput  class="brd" name="applicant_company_eik" value="" />
                </div>
            </div>
        </fieldset>
	</fieldset>
	<div class="clr10"><!--  --></div>
    <fieldset><legend>Собственик на дипломата</legend>
        <div class="flt_rgt">
            <div class="filterElement">
                <label for="owner_fname">Име</label><br />
                <v:textinput  class="brd" name="owner_fname" value="" />
            </div>
            <div class="filterElement">
                <label for="owner_sname">Презиме</label><br />
                <v:textinput  class="brd" name="owner_sname" value="" />
            </div>
            <div class="filterElement">
                <label for="owner_lname">Фамилия</label><br />
                <v:textinput  class="brd" name="owner_lname" value="" />
            </div>
            <div class="filterElement">
                <label for="owner_personalId">Персонален идентификатор</label><br />
                <v:textinput  class="brd" name="owner_personalId" value="" />
            </div>
        </div>
    </fieldset>
        <div class="clr10"><!--  --></div>
	<fieldset><legend><input id="dipl_names_checkbox" name="dipl_names_checkbox" value="1" type="checkbox" onclick="javascript:toggleDivElements($('diplNamesDiv'), this.checked);" />Имена по диплома</legend>
       <div class="flt_rgt" id="diplNamesDiv" style="display:none;">
        <div class="filterElement">
            <label for="dipl_fname">Име</label><br />
            <v:textinput  class="brd" name="dipl_fname" value="" />
        </div>
        <div class="filterElement">
            <label for="dipl_sname">Презиме</label><br />
            <v:textinput  class="brd" name="dipl_sname" value="" />
        </div>
        <div class="filterElement">
            <label for="dipl_lname">Фамилия</label><br />
            <v:textinput  class="brd" name="dipl_lname" value="" />
        </div>
       </div>
    </fieldset>
	<div class="clr10"><!--  --></div>
    <fieldset><legend><input id="repr_checkbox" name="repr_checkbox" value="1" type="checkbox" onclick="javascript:toggleDivElements($('reprDiv'), this.checked);" />Подател</legend>
       <div class="flt_rgt" id="reprDiv" style="display:none;">
        <div class="filterElement">
            <label for="repr_fname">Име</label><br />
            <v:textinput  class="brd" name="repr_fname" value="" />
        </div>
        <div class="filterElement">
            <label for="repr_sname">Презиме</label><br />
            <v:textinput  class="brd" name="repr_sname" value="" />
        </div>
        <div class="filterElement">
            <label for="repr_lname">Фамилия</label><br />
            <v:textinput  class="brd" name="repr_lname" value="" />
        </div>
        <div class="filterElement">
            <label for="repr_personalId">Персонален идентификатор</label><br />
            <v:textinput  class="brd" name="repr_personalId" value="" />
        </div>
        <br />
        <div class="filterElement">
            <label for="repr_company">Фирма</label><br />
            <nacid:combobox name="repr_company" id="repr_company" attributeName="companiesComboBox" style="w308 brd" />
        </div>
       </div>
    </fieldset>
    
	<fieldset><legend>Данни от заявление</legend>
        <p>
           <span class="fieldlabel2"><label>Деловоден номер</label></span> 
           <v:textinput  value="" id="application_number" name="application_number" class="brd w200" />
        </p>
        <p>
           <span class="fieldlabel2"><label>от дата</label></span> 
           <v:dateinput value="дд.мм.гггг" id="dateFrom" name="dateFrom" style="brd w200 flt_lft" />
           <span class="fieldlabel2"><label>до дата</label></span> 
           <v:dateinput value="дд.мм.гггг" id="dateTo" name="dateTo" style="brd w200 flt_lft" />
        </p>
    </fieldset>
    <input type="hidden" name="screen" value="" />
    <input type="hidden" id="print_type" name="print_type" value="" />
    <input type="hidden" id="screen_options" name="screen_options" value="" />
    <input type="hidden" name="joint_degree" value="1" /><%-- pri applicant_inquire kolonite universityName, universityCountry trqbva vinagi da sa obedineni, i poradi tazi pri4ina se slaga joint_degree da e vingi true --%>
	<inquires:inquire_submit_buttons formName="applicant_inquire" />
</v:form>
<div class="clr20"><!--  --></div>
<div id="table_result">
</div>
<inquires:screen_options />
<div id="inquires_screens">
</div>
<script type="text/javascript">
    applicationTypeChanged();
</script>