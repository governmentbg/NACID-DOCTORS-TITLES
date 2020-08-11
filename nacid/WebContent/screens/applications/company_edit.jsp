<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<h3 class="title"><span>${operationStringForScreens } фирма</span></h3>
<nacid:companyEdit>
	<v:form action="${pathPrefix }/control/company/save" method="post" name="form_company"
		backurl="${pathPrefix }/control/company/list?getLastTableState=1">

		<nacid:systemmessage />

		
		<input id="id" type="hidden" name="id" value="${id }" />

		<fieldset><legend>Основни данни</legend>
		
			<v:dateValidator input="dateFrom" format="d.m.yyyy" required="false" beforeToday="true" />
			<v:dateValidator input="dateTo" format="d.m.yyyy" required="false" beforeToday="true" />
			<v:dateIntervalValidator dateFrom="dateFrom" dateTo="dateTo" format="d.m.yyyy" />
			<v:textValidator input="companyName" required="true" maxLength="100" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}"/>
			<v:textValidator input="companyEik" required="true" maxLength="100" regex="${validations.number}" errormessage="${messages.err_number}"/>
			<v:textValidator input="city" maxLength="30" />
			<v:textValidator input="pcode" maxLength="5" />
			<v:textValidator input="phone" maxLength="80" required="true" />

            <p><span class="fieldlabel2"><label for="companyEik">Булстат/ЕИК</label><br /></span>
                <v:textinput class="brd w600" name="companyEik"  value="${companyEik }" />
            </p>
            <div class="clr"><!--  --></div>
			<p><span class="fieldlabel2"><label for="companyName">Име</label><br /></span> 
				<v:textinput class="brd w600" name="companyName"  value="${companyName }" />
			</p>
			<div class="clr"><!--  --></div>
		
			<p><span class="fieldlabel2"><label for="countryId">Държава</label><br /></span> 
				<nacid:combobox name="countryId" id="countryId" attributeName="companyCountry" style="brd w308" />
			</p>
			<div class="clr"><!--  --></div>
			
			<p><span class="fieldlabel2"><label for="city">Град</label><br /></span> 
				<v:textinput class="brd w300" name="city"  value="${city }" />
			</p>
			<div class="clr"><!--  --></div>
		
			<p><span class="fieldlabel2"><label for="pcode">Пощенски код</label><br /></span> 
				<v:textinput class="brd w100" name="pcode"  value="${pcode }" />
			</p>
			<div class="clr"><!--  --></div>
		
			<p><span class="fieldlabel2"><label for="addressDetails">Адрес</label><br /></span> 
				<textarea class="brd w600" rows="3" cols="40"
					name="addressDetails" id="addressDetails">${addressDetails }</textarea>
			</p>
			<div class="clr"><!--  --></div>
		
			<p><span class="fieldlabel2"><label for="phone">Телефон</label><br /></span> 
				<v:textinput class="brd w600" name="phone"  value="${phone }" />
			</p>
			<div class="clr"><!--  --></div>
		
			<p><span class="fieldlabel2"><label for="dateFrom">от дата</label><br /></span> 
				<v:dateinput name="dateFrom" id="dateFrom" value="${dateFrom }" style="brd w200" />
			</p>
			<div class="clr"><!--  --></div>
			
			<p><span class="fieldlabel2"><label for="dateTo">до дата</label></span>
		  		<v:dateinput name="dateTo" id="dateTo" value="${dateTo }" style="brd w200" />
		  	</p>
			<div class="clr"><!--  --></div>
		
		
		</fieldset>


	</v:form>
</nacid:companyEdit>

<%@ include file="/screens/footer.jsp"%>
