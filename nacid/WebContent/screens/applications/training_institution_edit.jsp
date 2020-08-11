<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens/header.jsp"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<h3 class="title"><span>${operationStringForScreens } институция, провеждаща обучението</span></h3>
<nacid:trainingInstitutionEdit>
	<v:form action="${pathPrefix }/control/training_institution/save"
		method="post" name="training_institution"
		backurl="${pathPrefix }/control/training_institution/list?getLastTableState=1">

		<nacid:systemmessage />

		<v:textValidator input="name" maxLength="100" required="true" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
		<v:textValidator input="city" maxLength="30" />
		<v:textValidator input="phone" maxLength="30" />
		<v:textValidator input="pcode" maxLength="5" />
		<v:dateIntervalValidator format="d.m.yyyy" dateTo="dateTo" dateFrom="dateFrom" />
		<v:dateValidator format="d.m.yyyy" input="dateTo" beforeToday="true"/>
		<v:dateValidator format="d.m.yyyy" input="dateFrom" beforeToday="true" />
		<v:comboBoxValidator input="country" required="true" />

		<input id="id" type="hidden" name="id" value="${id }" />
		<fieldset><legend>Основни данни</legend>
			
			<p><span class="fieldlabel2"><label for="name">Наименование</label><br /></span> 
				<v:textinput id="name" class="brd w500" name="name" 
					value="${name }" />
			</p>
			<div class="clr"><!--  --></div>

			<p><span class="fieldlabel2"><label for="country">Държава</label><br /></span> 
				<nacid:combobox id="country" name="country" attributeName="country" style="brd"/>
			</p>
			<div class="clr"><!--  --></div>
			
			<p><span class="fieldlabel2"><label for="city">Град</label><br /></span> 
				<v:textinput id="city" class="brd w500" name="city" 
					value="${city }" />
			</p>
			<div class="clr"><!--  --></div>
			
			<p><span class="fieldlabel2"><label for="pcode">Пощенски код</label><br /></span> 
				<v:textinput id="pcode" class="brd w500" name="pcode" 
					value="${pcode }" />
			</p>
			<div class="clr"><!--  --></div>
			
			<p><span class="fieldlabel2"><label for="addrDetails">Адрес</label><br /></span> 
				<textarea id="addrDetails" class="brd w600" rows="3" cols="40"
						name="addrDetails">${address_details }</textarea>
			</p>
			<div class="clr"><!--  --></div>
			
			<p><span class="fieldlabel2"><label for="phone">Телефон</label><br /></span> 
				<v:textinput id="phone" class="brd w500" name="phone" 
					value="${phone }" />
			</p>
			<div class="clr"><!--  --></div>
		
			<p><span class="fieldlabel2"><label for="dateFrom">От дата</label><br /></span> 
				<v:dateinput name="dateFrom" style="brd w200" value="${dateFrom }" />
			</p>
			<div class="clr"><!--  --></div>
			
			<p><span class="fieldlabel2"><label for="dateTo">До дата</label><br /></span> 
				<v:dateinput name="dateTo" style="brd w200" value="${dateTo }" />
			</p>
			<div class="clr"><!--  --></div>
		
		</fieldset>
		
		<fieldset><legend>Висши училища, от името на които се провежда обучението</legend>
			<input id="universityIds" type="hidden" name="universityIds" value="${universityIds }" />
			
			<div id="selectedDiv">
				<p><span class="fieldlabel2"><label for="institutions">Избрани висши училища</label><br /></span> 
					<table id="selectedInstitutions" >
						<tbody>
						<c:forEach var="trInst" items="${universities}">
							<tr id="trInstRow${trInst.id }">
								<td><a target="_blank" href="${pathPrefix }/control/university/view?id=${trInst.id }">${trInst.bgName }${trInst.inactive }, ${trInst.country }</a></td>
								<td><a href="#" onclick="removeItem('${trInst.id }'); return false;" ><img src="${pathPrefix}/img/icon_delete.png" /></a></td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</p>
				<div class="clr10"><!--  --></div>
			</div>
			
			<p><span class="fieldlabel2"><label for="univCountryAjax">Държава</label><br /></span> 
				<nacid:combobox id="univCountry" name="univCountry" attributeName="univCountry" 
						onchange="getUniversities(this);"/>
			</p>
			<div class="clr"><!--  --></div>
			
			<p><span class="fieldlabel2"><label for="universitiesAjax">Висше училище</label><br /></span> 
				<span id="university_span"> 
					<%@ include file="diploma_type_university.jsp"%> 
				</span>
				<a href="#" onclick="addElement(); return false;">Добави</a>
			</p>
			<div class="clr"><!--  --></div>
			
			
			<script type="text/javascript">
				function addElement() {
					var univ = $('university_id');
					var option = univ.options[univ.selectedIndex];
					var id = option.value;
					var txt = option.innerHTML; 

					
					
					if(id == '-') {
						return;
					}
					var univCountry = $('univCountry');
					txt += (", " + univCountry.options[univCountry.selectedIndex].innerHTML);

					if($('trInstRow' + id) != null) {
						return;
					}
					
					$('universityIds').value = addElementIdToInput($('universityIds').value, id);
					showHideDiv();
					
					var tr = new Element('tr', {'id': 'trInstRow' + id});

					var td1 = new Element('td');
					var a1 = new Element('a', {'href': '${pathPrefix }/control/university/view?id='+id,
												'target': '_blank'});
					a1.insert(txt);
					td1.insert(a1);
					
					var td2 = new Element('td');
					var a2 = new Element('a', {'href': '#',
												'onclick': 'removeItem('+id+'); return false;'});
					var img = new Element('img', {'src': '${pathPrefix}/img/icon_delete.png'});
					a2.insert(img);
					td2.insert(a2);
					
					tr.insert(td1);
					tr.insert(td2);
					
					$('selectedInstitutions').down().insert({bottom: tr});
				}
				function removeItem(id) {
					$('universityIds').value = removeElementIdFromInput($('universityIds').value, id);
					$('trInstRow' + id).remove();
					showHideDiv();
				}
				function showHideDiv() {
					if($('universityIds').value == '') {
						$('selectedDiv').hide();
					}
					else {
						$('selectedDiv').show();
					}
				}
				function getUniversities(el) {
					var countryId = el.options[el.selectedIndex].value;
					new Ajax.Request('${pathPrefix}/control/diploma_type_university?countryId=' + countryId, {
       				 	onComplete: function(oXHR) {
           			 		$("university_span").update(oXHR.responseText);
        				},
        				method: 'GET'
  					});
	
				}
				showHideDiv();
			</script>
			
		</fieldset>

	</v:form>
</nacid:trainingInstitutionEdit>

<%@ include file="/screens/footer.jsp"%>
