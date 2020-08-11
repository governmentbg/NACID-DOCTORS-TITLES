<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/screens_regprof/header.jsp"%>
<%@ taglib uri="/META-INF/validation.tld" prefix="v"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<v:form
	action="${pathPrefix }/control/nom_article_directive/save" method="post"
	name="articleDirectiveForm"
	backurl="${pathPrefix }/control/nom_article_directive/list?getLastTableState=1" 
	additionalvalidation="validateText($('item'), false, 1, 30, null, '') && additionalValueRepeatsCheck($('item').value)">

	<input type="hidden" name="id" value="${regprofArticleDirective.id }" />
	<v:dateValidator input="dateFrom" format="d.m.yyyy" required="false" beforeToday="true"/>
	<v:dateValidator input="dateTo" format="d.m.yyyy" required="false" beforeToday="true" />
	<v:dateIntervalValidator dateFrom="dateFrom" dateTo="dateTo" format="d.m.yyyy" />
	<v:textValidator input="name" minLength="1" maxLength="50" required="true" 
					 regex="${validations.nomenclature_name_with_fullstop_digits}" errormessage="${messages.err_nomenclature_name_with_fullstop_digits}" />

	<div class="clr10"><!--  --></div>
	<fieldset>
		<div class="clr20"><!--  --></div>
		<nacid:systemmessage />
		<p>
			<span class="fieldlabel"><label for="name">Член</label><br /></span> 
			<v:textinput class="brd w600" maxlength="50" name="name"  value="${regprofArticleDirective.name }" />
		</p>
		<div class="clr"><!--  --></div>
		<div id="itemList" style="display:none;">
			<p><span class="fieldlabel"><label>Точка и буква към този член: </label></span>
				<table id="article_item_table">
				<c:if test="${regprofArticleDirective.articleItems != null }">
					<c:forEach items="${regprofArticleDirective.articleItems}" var="item">
						<tr id="row${item.id}">
							<td>${item.name}
								<c:if test="${not empty item.qualificationLevelLabel }">
									&nbsp;/&nbsp;${item.qualificationLevelLabel }
								</c:if>
							</td><td>
							<c:if test="${item.isActive == false}">
								 - неактивна от дата: ${item.dateToFormatted}
							</c:if>
							</td>
							<td style="display:none;"><v:hidden id="${item.id}" name="itemUnit" value="${item.id}_${item.name}"/>
								<v:hidden id="${item.id}_qll" name="qll" value="${item.id}_${item.qualificationLevelLabel}"/></td>
							<td><a href="javascript:void(0);" onclick="removeItemFromList(this.parentNode.parentNode);">
							<img src="${pathPrefix}/img/icon_delete.png"/></a></td>
						</tr>
					</c:forEach>
				</c:if>
				</table>
			</p>
		</div>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="item">Точка, буква</label><br /></span> 
			<v:textinput class="brd w300" maxlength="50" name="item" id="item"/><br/>
			<span class="fieldlabel"><label for="qualification_level_label">Име на квалификационно ниво</label><br /></span>
			<v:textinput class="brd w300" maxlength="50" name="qualification_level_label" id="qualification_level_label"/>
			<a href="javascript:void(0);" onclick="addItemToList();" style="padding-left: 10px;">Добави точка, буква</a>
		</p>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="dateFrom">от дата</label><br /></span>
			<v:dateinput name="dateFrom" id="dateFrom" value="${regprofArticleDirective.formattedDateFrom }" style="brd w200" />
		</p>
		<div class="clr"><!--  --></div>
		<p>
			<span class="fieldlabel"><label for="dateTo">до дата</label></span>
			<v:dateinput name="dateTo" id="dateTo" value="${regprofArticleDirective.formattedDateTo }" style="brd w200" />
		</p>
		<div class="clr"><!--  --></div>
	</fieldset>
</v:form>
<script type="text/javascript">
if (window.location.pathname.lastIndexOf('view') != -1) {
	$$('#article_item_table a').each(function(el) {
		el.onclick = function(){};
	});
}
showHideItemList();
function addItemToList() {
	var table = $('article_item_table');
	var listSize = table.rows.length;
	if (validateAticleItem() && additionalValueRepeatsCheck($('item').value)) {
		var row = table.insertRow(listSize);
		row.id = "row0"+listSize;
		row.name = "newRow";
    	var cell1 = row.insertCell(0);
    	cell1.innerHTML = $('item').value + ($('qualification_level_label').value == "" ? "" : " / " + $('qualification_level_label').value);
    	var cell2 = row.insertCell(1);
    	cell2.style.display = "none";
    	var input = document.createElement("input");
    	input.name = "itemUnit";
    	input.value = "0_"+$('item').value;
    	cell2.appendChild(input);
    	input = document.createElement("input");
    	input.name = "qll";
    	input.value = "0_" + $('qualification_level_label').value;
    	cell2.appendChild(input);

    	var cell3 = row.insertCell(2);
    	var a = document.createElement("a");
    	a.href = "javascript:void(0);";
    	a.onclick = function() {
    		removeItemFromList(a.parentNode.parentNode);
    	};
    	var img = document.createElement("img");
    	img.src = "${pathPrefix}/img/icon_delete.png";
    	a.appendChild(img);
    	cell3.appendChild(a);
    	$('item').value = "";
        $('qualification_level_label').value = "";
    	clearInputError($('item'));
	} else {
		alert("Моля, коригирайте полетата в червено!");
	}
	showHideItemList();
}
function removeItemFromList(row) {
	var table = $('article_item_table');
	var dt = new Date();
	if(confirm("Сигурни ли сте, че искате да премахнете точката/буквата от списъка?")) {		
		if(row.name == "newRow"){
			Element.remove(row);
		} else {
			if (($$('#'+row.id +' td')[0].innerHTML).lastIndexOf("неактивна") == -1) {
				$$('#'+row.id +' input')[0].name = "itemDeleteUnit";
				$$('#'+row.id +' td')[1].innerHTML = "- неактивна от дата: "+dt.getDate()+"."+
				(dt.getMonth()+1)+"."+dt.getFullYear();
			}
		}
	}
	var rowsLeft = table.rows.length;
	if(rowsLeft == 0){
		$('itemList').hide();
	}
	showHideItemList();
}
function showHideItemList() {
	if ($('article_item_table').rows.length == 0) {
		$('itemList').hide();
	} else {
		$('itemList').show();
	}
}
function valueRepeats(value) {
	var ret = false;
	$$('#article_item_table input[name^=itemUnit]').each(function(element) {
		var tdInactive = element.up().previous();
		var text = tdInactive.innerHTML;
		var el = element.value;
		el = el.replace(/\s*/g, "");
		el = el.substr(el.indexOf('_')+1, el.length);

		var val = value.replace(/\s*/g, "");
		if (el == val && text.indexOf("неактивна") == -1) {
			ret = true;
		}
	});
	return ret;
}
function additionalValueRepeatsCheck(value){
	var ret = valueRepeats(value);
	var retReverse = !ret;
	if (ret) {
		setErrorOnInput($('item'), "Вече е добавена точка/буква с това име");
	} else {
		clearInputError($('item'));
	}
	return retReverse;
	
}
function validateAticleItem(){
	return validateText($('item'), true, 1, 30, null, "");
}
</script>
<%@ include file="/screens_regprof/footer.jsp"%>