<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<script type="text/javascript">
	var isMemberdataAutoFilled = false;
	function updateMemberData(value, data) {
		var d = data.evalJSON(true);
		$('fname').value = d.fname;
		$('sname').value = d.sname;
		$('lname').value = d.lname;
		$('degree').value = d.degree;
		$('institution').value = d.institution;
		$('division').value = d.division;
		$('title').value = d.title;
		$('homeCity').value = d.homeCity;
		$('homePcode').value = d.homePcode;
		$('homeAddress').value = d.homeAddress;
		$('phone').value = d.phone;
		$('email').value = d.email;
		$('gsm').value = d.gsm;
		$('iban').value = d.iban;
		$('bic').value = d.bic;

		selectOption('user', d.userId);
		selectOption('position', d.comissionPosId);
		selectOption('profGroup', d.profGroupId);

		isMemberdataAutoFilled = true;
	}

	function clearMemberData() {
		if(isMemberdataAutoFilled) {
			$('fname').value = '';
			$('sname').value = '';
			$('lname').value = '';
			$('degree').value = '';
			$('institution').value = '';
			$('division').value = '';
			$('title').value = '';
			$('homeCity').value = '';
			$('homePcode').value = '';
			$('homeAddress').value = '';
			$('phone').value = '';
			$('email').value = '';
			$('gsm').value = '';
			$('iban').value = '';
			$('bic').value = '';

			$('user').selectedIndex = 0;
			$('position').selectedIndex = 0;
			$('profGroup').selectedIndex = 0;
			
			isMemberdataAutoFilled = false;
		}
	}

	function resetAutofilledState() {
		isMemberdataAutoFilled = false;
	}

</script>


<h3 class="title"><span>${operationStringForScreens } член/консултант на
Комисията за академично признаване</span></h3>
<nacid:comissionMemberEdit>
	<v:form action="${pathPrefix }/control/app_comission/save"
		method="post" name="form_comission_member"
		onsubmit="resetAutofilledState();"
		backurl="${pathPrefix }/control/app_comission/list?getLastTableState=1">

		<v:textValidator input="first_name" maxLength="15" required="true" regex="${validations.name}" errormessage="${messages.err_name}" />
		<v:textValidator input="second_name" maxLength="15" regex="${validations.name}" errormessage="${messages.err_name}" />
		<v:textValidator input="last_name" maxLength="30" required="true" regex="${validations.name}" errormessage="${messages.err_name}" />
		<v:textValidator input="degree" maxLength="30" />
		<v:textValidator input="institution" maxLength="255" />
		<v:textValidator input="division" maxLength="255" />
		<v:textValidator input="title" maxLength="255" />

		<v:textValidator input="phone" maxLength="255" />
		<v:textValidator input="gsm" maxLength="15" />
		<v:textValidator input="location" maxLength="30" />
		<v:textValidator input="location_post_code" maxLength="5" />
		
		<v:textValidator input="userName" maxLength="30"/>

		<v:textValidator input="iban" maxLength="30" />
		<v:textValidator input="bic" maxLength="10" />
		<v:textValidator input="email" maxLength="255"  regex="${validations.email }" />
		<v:dateIntervalValidator format="d.m.yyyy" dateTo="date_to"
			dateFrom="date_from" />
		<v:dateValidator format="d.m.yyyy" input="date_to" />
		<v:dateValidator format="d.m.yyyy" input="date_from" />
        <v:comboBoxValidator input="member_consultant" required="true" />

		<input id="id" type="hidden" name="id" value="${id }" />
		<fieldset><legend>Основни данни</legend> <nacid:systemmessage />

		<p><span class="fieldlabel2"><label for="personal_id">ЕГН</label><br />
		</span> <v:textinput id='egn' class="brd w200" maxlength="100" name="personal_id"
			value="${egn }" onkeydown="if(!isSpecialKeyPressed(event)) clearMemberData();" /></p>
		<v:egnLnchValidator input="personal_id" /> <script type="text/javascript">
				if('${id }' == '0' || '${id }' == '') {
    				new Autocomplete('egn', { 
    	    			serviceUrl:'${pathPrefix }/ajax/comission_member_prefill',
    	    			// callback function:
    	    			onSelect: updateMemberData
    	  			});
				}
	 		</script>

		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="first_name">Име</label><br />
		</span> <v:textinput id="fname" class="brd w500" maxlength="100" name="first_name"
			value="${fname }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="second_name">Презиме</label><br />
		</span> <v:textinput id="sname" class="brd w500" maxlength="100" name="second_name"
			value="${sname }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="last_name">Фамилия</label><br />
		</span> <v:textinput id="lname" class="brd w500" maxlength="100" name="last_name"
			value="${lname }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="degree">Звание/степен</label><br />
		</span> <v:textinput id="degree" class="brd w500" maxlength="100" name="degree"
			 value="${degree }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="institution">Висше
		училище/институция</label><br />
		</span> <v:textinput id="institution" class="brd w500" maxlength="100"
			name="institution" value="${institution }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="division">Факултет/дирекция</label><br />
		</span> <v:textinput id="division" class="brd w500" maxlength="100" name="division"
			value="${division }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="title">Длъжност</label><br />
		</span> <v:textinput id="title" class="brd w500" maxlength="100" name="title"
			value="${title }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="profGroup">Професионално
		направление</label><br />
		</span> <nacid:combobox id="profGroup" name="profGroup"
			attributeName="profGroupCombo" style="w500 brd" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="member_consultant">Член/консултант</label><br />
		</span> <nacid:combobox id="position" name="member_consultant"
			attributeName="positionCombo" style="brd" /></p>
		<div class="clr"><!--  --></div>

		</fieldset>
		<fieldset><legend>Контакти</legend>
		<p><span class="fieldlabel2"><label for="phone">Телефони</label><br />
		</span> <textarea id="phone" class="brd w500" rows="3" cols="40" name="phone">${phone }</textarea>
		</p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="gsm">GSM</label><br />
		</span> <v:textinput id="gsm" class="brd w200" maxlength="100" name="gsm"
			value="${gsm }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="email">Електронна
		поща</label><br />
		</span> <v:textinput id="email" class="brd w500" maxlength="100" name="email"
			value="${email }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="location">Населено
		място</label><br />
		</span> <v:textinput id="homeCity" class="brd w500" maxlength="100" name="location"
			value="${homeCity }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label
			for="location_post_code">Пощенски код</label><br />
		</span> <v:textinput id="homePcode" class="brd w300" maxlength="100"
			name="location_post_code" value="${homePcode }" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="address">Адрес</label><br />
		</span> <textarea id="homeAddress" class="brd w600" rows="3" cols="40"
			name="address">${homeAddress }</textarea></p>
		<div class="clr"><!--  --></div>

		<div class="clr"><!--  --></div>
		</fieldset>
		<fieldset><legend>Банкова информация</legend>
		<p><span class="fieldlabel2"><label for="iban">IBAN</label><br />
		</span> <v:textinput id="iban"  value="${iban }" class="brd w500"
			maxlength="100" name="iban" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="bic">BIC</label><br />
		</span> <v:textinput id="bic" class="brd w200" maxlength="100" name="bic"
			value="${bic }" /></p>
		<div class="clr"><!--  --></div>

		</fieldset>
		<fieldset><legend>Системна информация</legend>
		
		<p><span class="fieldlabel2"><label for="userName">Потребителско име</label><br /></span> 
			<input id="userName" name="userName" class="brd w300" value="${userName}" />
		</p>
		<div class="clr"><!--  --></div>
		
		<p><span class="fieldlabel2"><label for="userPass">Парола</label><br /></span> 
			<input id="userPass" name="userPass" class="brd w300" value="" />  (празно - без промяна)
		</p>
		<div class="clr"><!--  --></div>
		
		<p><span class="fieldlabel2"><label for="date_from">От
		дата</label><br />
		</span> <v:dateinput name="date_from" id="date_from" value="${dateFrom }"
			style="brd w200" /></p>
		<div class="clr"><!--  --></div>
		<p><span class="fieldlabel2"><label for="date_to">До
		дата</label><br />
		</span> <v:dateinput name="date_to" id="date_to" value="${dateTo }"
			style="brd w200" /></p>
		<div class="clr"><!--  --></div>
		</fieldset>

	</v:form>
</nacid:comissionMemberEdit>

<%@ include file="/screens/footer.jsp"%>
