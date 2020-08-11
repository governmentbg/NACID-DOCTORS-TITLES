<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"  %>
<%@ include file="/screens/header.jsp" %>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<script type="text/javascript">
    function showHideNewFacultyDiv(id) {

        var editDiv = $('faculty_new_div');
        var openClass = "subwindow_open";
        var windowClass = "subwindow";

        if (editDiv.visible() == false) {
            if ($$('.' + openClass).length >= 1) {
                return;
            }
            resetNewFacultyForm();
            editDiv.addClassName(openClass);
            editDiv.addClassName(windowClass);
            editDiv.show();
            if (id > 0) {
                new AjaxJsonFieldsUpdater({
                    elements: [
                        'faculty_id',
                        'faculty_name',
                        'faculty_original_name',
                        'faculty_date_from',
                        'faculty_date_to'
                    ]

                }).update('${pathPrefix}/control/university_faculty_ajax/view?id=' + id);
            } else {
            }
        } else {
            editDiv.hide();
            editDiv.removeClassName(openClass);
            editDiv.removeClassName(windowClass);
        }
    }

    function resetNewFacultyForm() {
        clearAllErrors(null);
        $('faculty_id').value = "";
        $('faculty_name').value = "";
        $('faculty_original_name').value = "";
        $('faculty_date_from').value = "";
        $('faculty_date_to').value = "";
    }

    function validateFaculty() {
        var ret = true;
        ret = validateText($('faculty_name'), true, -1, 255, ${validations.allButQuote}, '${messages.err_allButQuote}') && ret;
        ret = validateText($('faculty_original_name'), true, -1, 255, ${validations.allButQuote}, '${messages.err_allButQuote}') && ret;
        ret = validateDate($('faculty_date_from'),'d.m.yyyy', null, false, true) && ret;
        ret = validateDate($('faculty_date_to'),'d.m.yyyy', null, false, true) && ret;
		if (!ret) {
            alert("Моля коригирайте полетата в червено!");
        }
        return ret;
    }
    function saveFaculty() {
        var validated = validateFaculty();
        if (!validated) {
            return;
        }
        new Ajax.Request('${pathPrefix}/control/university_faculty_ajax/save', {
            method: 'post',
            parameters: {
                id : $('faculty_id').value,
				universityId : $('id').value,
                name : $('faculty_name').value,
                originalName : $('faculty_original_name').value,
                dateFrom : $('faculty_date_from').value,
                dateTo : $('faculty_date_to').value
            },
			onSuccess: function(transport) {
                var response = transport.responseText || "no response text";
                var json = response.evalJSON(true);

                new Ajax.Request('${pathPrefix}/control/university_faculty_ajax/list', {
                    method: 'post',
                    parameters: {
                        universityId : $('id').value
                    },
                    onSuccess: function(transp) {
                        var resp = transp.responseText || "no response text";
                        $("university_faculties_table").replace(resp);
                    }
                });


            	showHideNewFacultyDiv();
            },
            onFailure : function(transport) {
                alert("Възникна грешка. Моля опитайте по-късно..." + oXHR.statusText);
            }
        });
    }
</script>
<h3 class="title"><span>${operationStringForScreens } висше училище</span></h3>
<nacid:universityEdit>
	<v:form action="${pathPrefix }/control/university/save" method="post" 
			name="form_university" backurl="${pathPrefix }/control/university/list?getLastTableState=1">

		<nacid:systemmessage />
		
		<c:if test="${not empty changedDiplomaTypes}">
		<div class="clr10"><!--  --></div>
		<div class="correct">
		  <c:forEach items="${changedDiplomaTypes}" var="diplomaType">
		  <p>${diplomaType }</p>
		  </c:forEach>
		</div>
		<div class="clr10"><!--  --></div>
		</c:if>
		
		<v:textValidator input="bgName" maxLength="255" required="true" />
		<v:textValidator input="orgName" maxLength="255" regex="${validations.allButQuote}" errormessage="${messages.err_allButQuote}" />
		<v:textValidator input="city" maxLength="30" />
		<v:textValidator input="phone" maxLength="255" />
		<v:textValidator input="fax" maxLength="255" />
		<v:textValidator input="email" maxLength="255" regex="${validations.email }"/>
		<v:textValidator input="webSite" maxLength="255" />
		<v:textValidator input="urlDiplomaRegister" maxLength="255"  />
		<v:dateIntervalValidator format="d.m.yyyy" dateTo="dateTo" dateFrom="dateFrom" />
		<v:dateValidator format="d.m.yyyy" input="dateTo" beforeToday="true"/>
		<v:dateValidator format="d.m.yyyy" input="dateFrom" beforeToday="true" />

		<input id="id" type="hidden" name="id" value="${id }" />
		<fieldset><legend>Основни данни</legend>
			
			
			<p><span class="fieldlabel2"><label for="country">Държава</label><br/></span>
	        	 <nacid:combobox id="country" name="country" attributeName="countryCombo" style="brd"/>
	      	</p>
			<div class="clr"><!--  --></div>
      		<p><span class="fieldlabel2"><label for="bgName">Наименование</label><br/></span>
         		<v:textinput id="bgName" class="brd w500" name="bgName"  value="${bgName }" />
      		</p>
      		<div class="clr"><!--  --></div>      
      		<p><span class="fieldlabel2"><label for="orgName">Оригинално наименование</label><br/></span>
         		<v:textinput id="orgName" class="brd w500" name="orgName"  value="${orgName }" />
      		</p>
      		<div class="clr"><!--  --></div>
      		<p><span class="fieldlabel2"><label for="city">Град</label><br/></span>
         		<v:textinput id="city" class="brd w500" name="city"  value="${city }" />
      		</p>
			<div class="clr"><!--  --></div>
			<p><span class="fieldlabel2"><label for="addrDetails">Адрес</label><br/></span>
          		<textarea id="addrDetails" class="brd w600" rows="3" cols="40" name="addrDetails">${addrDetails }</textarea>
			</p>
			<div class="clr"><!--  --></div>
      		<p><span class="fieldlabel2"><label for="phone">Телефон</label><br/></span>
         		<v:textinput id="phone" class="brd w500" name="phone"  value="${phone }" />
      		</p>
			<div class="clr"><!--  --></div>
      		<p><span class="fieldlabel2"><label for="fax">Факс</label><br/></span>
         		<v:textinput id="fax" class="brd w500" name="fax"  value="${fax }" />
      		</p>
			<div class="clr"><!--  --></div>
      		<p><span class="fieldlabel2"><label for="email">Електронна поща</label><br/></span>
         		<v:textinput id="email" class="brd w500" name="email"  value="${email }" />
      		</p>
			<div class="clr"><!--  --></div>
      		<p><span class="fieldlabel2"><label for="webSite">Интернет страница</label><br/></span>
         		<v:textinput id="webSite" class="brd w500" name="webSite"  value="${webSite }" />
         		${webSiteLink }
      		</p>
      		<div class="clr"><!--  --></div>
      		<p><span class="fieldlabel2"><label for="urlDiplomaRegister">Официален регистър за дипломи</label><br/></span>
         		<v:textarea id="urlDiplomaRegister" class="brd w500" name="urlDiplomaRegister"  rows="3" cols="40" value="${urlDiplomaRegister }" />
      		</p>
			<c:if test="${not empty urlDiplomaRegisterLink}">
				<p>
					<span class="fieldlabel2 flt_lft">&nbsp;</span>
					<span class="flt_lft">${urlDiplomaRegisterLink }</span>
				</p>
			</c:if>
      		<div class="clr"><!--  --></div>
      		<p><span class="fieldlabel2"><label for="dateFrom">От дата</label><br/></span>
	        	 <v:dateinput style="brd w200" name="dateFrom" value="${dateFrom }" />
	      	</p>
      		<div class="clr"><!--  --></div>
      		<p><span class="fieldlabel2"><label for="dateTo">До дата</label><br/></span>
        		<v:dateinput style="brd w200" name="dateTo" value="${dateTo }" />
    		</p>
			<p><span class="fieldlabel2"><label for="genericNameId">Генерично наименование</label><br/></span>
				<nacid:combobox id="genericNameId" name="genericNameId" attributeName="genericNameCombo" style="brd"/>
			</p>
    		<div class="clr"><!--  --></div>
		</fieldset>
		<div id="faculty_new_div" style="display:none;" class="" style="width: 900px; height: auto;">
			<fieldset class="subwindow_f"><legend class="subwindow_l">Данни за нов факултет</legend>
				<v:hidden value="" id="faculty_id" name="faculty_id"/>
				<p>
					<span class="fieldlabel"><label for="faculty_name">Наименование</label></span>
					<v:textinput class="brd w500" name="faculty_name" id="faculty_name"  />
				</p>
				<div class="clr"><!--  --></div>
				<p>
					<span class="fieldlabel"><label for="faculty_original_name">Оригинално наименование</label></span>
					<v:textinput class="brd w500" name="faculty_original_name" id="faculty_original_name"  />
				</p>
				<div class="clr"><!--  --></div>
				<p>
					<span class="fieldlabel"><label for="faculty_date_from">От дата</label></span>
					<v:dateinput name="faculty_date_from" id="faculty_date_from" style="brd w200" />
				</p>
				<div class="clr"><!--  --></div>
				<p>
					<span class="fieldlabel"><label for="faculty_date_to">До дата</label></span>
					<v:dateinput name="faculty_date_to" id="faculty_date_to" style="brd w200" />
				</p>
				<div class="clr"><!--  --></div>

				<a href="javascript:void(0);" onclick="saveFaculty();" style="margin-left: 45%;" id="saveFacultyLnk">Запис</a>
				<a href="javascript:void(0);" onclick="showHideNewFacultyDiv();" style="margin-left: 20px;" id="cancelFacultyLnk">Отказ</a>
			</fieldset>
		</div>
		<div class="clr"><!-- --></div>
		<fieldset>
			<legend>Факултети</legend>

			<%@include file="university_faculties_table.jsp"%>

			<p>
				<span class="flt_rgt">
					<c:if test="${not empty id}">
                    	<a onclick="showHideNewFacultyDiv(false);" href="javascript: void(0);" id="newFacultyLnk">Нов</a>
					</c:if>
                </span>
			</p>
		</fieldset>



	</v:form>
</nacid:universityEdit>


<%@ include file="/screens/footer.jsp" %>
