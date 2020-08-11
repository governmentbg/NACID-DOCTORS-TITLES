<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"  %>
<%@ include file="/screens_regprof/header.jsp" %>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<script type="text/javascript">
var namesCount = ${listSize};

function addName() {
	if (validateText($('name'), true, 2, 255, null, '') && validateName()) {
		var table = $('institution_names_table');
		var rowCount = table.rows.length;
		var row = table.insertRow(rowCount);
    	var name = $('name').value;
    	
    	var cell1 = row.insertCell(0);
    	var hiddenField = new Element('input', { 'type': 'hidden'});
    	hiddenField.name = 'former_name' + rowCount;
    	hiddenField.value = name;
    	cell1.style.display = 'none';
    	cell1.appendChild(hiddenField);
    	
    	var cell2 = row.insertCell(1);
    	cell2.innerHTML = name;
    	
    	var cell3 = row.insertCell(2);
		var deleteButton = new Element('a', {'href': 'javascript:void(0);'});
		deleteButton.onclick = function() {deleteRow(this.parentNode);};
		var img = new Element('img', {'src': '${pathPrefix}/img/icon_delete.png'});
		deleteButton.insert(img);
		cell3.appendChild(deleteButton);
		
		namesCount++;
		$('name').value = '';
		$('list_size').value = namesCount;
	}
}

function deleteRow(tableData) {
	if (namesCount > 0) {
		var table = $('institution_names_table');
		var rowNumber = parseInt(tableData.parentNode.rowIndex);
		table.deleteRow(rowNumber);
		namesCount--;
		$('list_size').value = namesCount;
		iterateTable(table);
	}
}

function iterateTable(table) {
	var rowCount = table.rows.length;
	for (var i = 0; i < rowCount; i++) {
		var row = table.rows[i];
		row.cells[0].children[0].name = 'former_name' + i;
	}
}

function validateName() {
	if ($('name').value == '') {
		setErrorOnInput('name', 'Трябва да въведете име!')
		return false;
	}
	var table = $('institution_names_table');
    var rowCount = table.rows.length;
	for (var i = 0; i < rowCount; i++) {
		var row = table.rows[i];
		if (row.cells[0].children[0].value.toLowerCase() == $('name').value.strip().toLowerCase() || 
				row.cells[1].innerHTML.toLowerCase() == $('name').value.strip().toLowerCase()) {
			setErrorOnInput('name', 'Името вече е въведено в списъка със стари наименования!')
			return false;
		}
	}
	clearInputError('name');
	return true;
}
</script>

<h3 class="title"><span>${operationStringForScreens } обучителна институция</span></h3>

<v:form action="${pathPrefix }/control/professionalinstitution/save" method="post" 
			name="form_professionalinstitution" backurl="${pathPrefix }/control/professionalinstitution/list?getLastTableState=1" modelAttribute="com.nacid.bl.impl.regprof.ProfessionalInstitutionImpl">

		<nacid:systemmessage />
		<v:comboBoxValidator input="countryId" required="true" />
		<v:comboBoxValidator input="professionalInstitutionTypeId" required="true" />
		<v:textValidator input="bgName" maxLength="255" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" required="true" />
		<v:textValidator input="city" maxLength="255" regex="${validations.city}" errormessage="${messages.err_allButQuote}" />
		<v:textValidator input="addrDetails" maxLength="255" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
		<v:textValidator input="phone" maxLength="255" regex="${validations.number}" />
		<v:textValidator input="fax" maxLength="255" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
		<v:textValidator input="email" maxLength="255" regex="${validations.email}" />
		<v:textValidator input="webSite" maxLength="255" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
		<v:textValidator input="former_name" minLength="2" maxLength="255" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
		<v:dateIntervalValidator format="d.m.yyyy" dateTo="dateTo" dateFrom="dateFrom" />
		<v:dateValidator format="d.m.yyyy" input="dateTo" beforeToday="true" />
		<v:dateValidator format="d.m.yyyy" input="dateFrom" beforeToday="true" />
		<v:hidden id="id" name="id" path="id" />
		<fieldset><legend>Основни данни</legend>
		
			<div class="clr"><!--  --></div>
           	<p><span class="fieldlabel2"><label for="countryId">Държава</label><br/></span>
                <nacid:combobox name="countryId" attributeName="countryId" path="countryId" id="countryId" style="brd w500"/>
            </p>
            <div class="clr"><!--  --></div>  
			<p><span class="fieldlabel2"><label for="bgName">Име</label><br/></span>
         		<v:textinput id="bgName" class="brd w600" name="bgName" path="bgName" />
      		</p>
      		<%-- <div class="clr"><!--  --></div>   
      		<p><span class="fieldlabel2"><label for="orgName">Оригинално наименование</label><br/></span>
         		<v:textinput id="orgName" class="brd w500" name="orgName" path="orgName" />
      		</p>--%>
      		<div class="clr"><!--  --></div>
      		<p>
				<span class="fieldlabel2"><label>Стари наименования</label></span>
			</p>
      		<table id="institution_names_table" border="0">
				<c:forEach var="item" items="${formerNames}" varStatus="varStatus">
					<tr>
						<td style="display:none;"><v:hidden name="former_name${varStatus.index}" value="${item.formerName}"/></td>
						<td>${item.formerName}<c:if test="${item.active == 0}">(неактивно)</c:if></td>
						<td><a href="javascript:void(0);" onclick="deleteRow(this.parentNode);"><img src="${pathPrefix}/img/icon_delete.png"/></a></td>
					</tr>
				</c:forEach>
			</table>
      		<v:hidden id="list_size" name="list_size" value="${listSize}" />
      		
      		<div class="clr5"><!--  --></div>

			<p><span class="fieldlabel2"><label for="name">Старо име</label><br /></span> 
				<v:textinput id="name" name="former_name" class="brd w500"/>
				<span id="add_name" style="padding-left: 15px;"><a href="javascript:addName();">Добави старо име</a></span>
			</p>
			<div class="clr"><!--  --></div>
      		
      		<div class="clr"><!--  --></div>     
      		<p><span class="fieldlabel2"><label for="city">Град</label><br/></span>
                <v:textinput id="city" class="brd w500" name="city" path="city" />
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="addrDetails">Адрес</label><br/></span>
          		<v:textarea id="addrDetails" class="brd w500" name="addrDetails" path="addrDetails" />
          	</p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="phone">Телефон</label><br/></span>
                <v:textinput id="phone" class="brd w500" name="phone" path="phone" />
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="fax">Факс</label><br/></span>
                <v:textinput id="fax" class="brd w500" name="fax" path="fax" />
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="email">Електронна поща</label><br/></span>
                <v:textinput id="email" class="brd w500" name="email" path="email" />
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="webSite">Интернет страница</label><br/></span>
                <v:textinput id="webSite" class="brd w500" name="webSite" path="webSite" />
            </p>
            <div class="clr"><!--  --></div>
            <p><span class="fieldlabel2"><label for="urlDiplomaRegister">Официален регистър за дипломи</label><br/></span>
                <v:textarea id="urlDiplomaRegister" rows="3" cols="40" class="brd w500" name="urlDiplomaRegister" path="urlDiplomaRegister" />
            </p>
            <div class="clr"><!--  --></div>
           	<p><span class="fieldlabel2"><label for="professionalInstitutionTypeId">Тип обучителна институция</label><br/></span>
                <nacid:combobox name="professionalInstitutionTypeId" attributeName="professionalInstitutionTypeId" 
                				path="professionalInstitutionTypeId" id="professionalInstitutionTypeId" style="brd w500"/>
            </p>
            <div class="clr"><!--  --></div>
      		<p><span class="fieldlabel2"><label for="dateFrom">От дата</label><br/></span>
	        	 <v:dateinput style="brd w200" name="dateFrom" id="dateFrom" path="dateFrom"/>
	      	</p>
      		<div class="clr"><!--  --></div>
      		<p><span class="fieldlabel2"><label for="dateTo">До дата</label><br/></span>
        		<v:dateinput style="brd w200" name="dateTo" id="dateTo" path="dateTo" />
    		</p>
    		<div class="clr"><!--  --></div>
                       
		</fieldset>
</v:form>
<%@ include file="/screens_regprof/footer.jsp" %>