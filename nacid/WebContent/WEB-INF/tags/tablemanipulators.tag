<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="formName" required="true" rtexprvalue="true"%>
<%@ attribute name="addFilters" required="false" rtexprvalue="true"%>
<%@ attribute name="tablePrefix" required="false" rtexprvalue="true"%>
<%@ attribute name="attribute" required="false" rtexprvalue="true"%>
<nacid:table attribute="${attribute }">
	<hr class="none" />
    <h1 class="off_jws">Филтри на данните</h1>
	<form name="${formName}" id="${formName}" method="post" action="${pathPrefix }/control/${formGroupName }/list${formAdditionalParams}">
	<input type="hidden" value="1" name="sub" />
	<input type="hidden" name="orderCol" value="${orderColumn }" />
	<input type="hidden" name="orderAscending" value="${orderAscending }" />
	<input type="hidden" name="checkedRows" value="${checkedRows }" />
	<input type="hidden" name="uncheckedRows" value="${uncheckedRows }" />
	<input type="hidden" name="availableRowsCount" value="${availableRowsCount }" />
	<input type="hidden" name="tablePrefix" value="${tablePrefix}" />
	
	<c:if test="${addFilters == true}">
		<nacid:filterstag />
	</c:if>
	<c:if test="${isModifyColumns}">
		<nacid:tablemodifiablecolumnstag />
	</c:if>
	
	<div class="clr20">
	<!--  -->
	</div>
	<fieldset class="nav_row rr"><legend>Записи: ${availableRowsCount } (общо ${allRowsCount })</legend>
	  <c:if test="${hasOperationsColumn}" >
	  <nacid:tableOperationAccess operationName="new">
	  <span>
	    <a href="${pathPrefix }/control/${groupName }/new${newRequestParams }" title="Нов запис"><img src="${pathPrefix }/img/icon_add.png" /></a> Нов запис
	  </span>
	  </nacid:tableOperationAccess>
	  </c:if>
    <span class="pl10">
	    <input name="onlyChecked" type="checkbox" value="1" ${onlyChecked }/>
	    <label for="onlyChecked" onclick="document.${formName}.onlyChecked.click();">Само маркираните</label>
	  </span>
	
	  <span class="pl10">
	    <label for="rowBegin">Начален ред</label>
	    <input type="text" value="${rowBegin }" name="rowBegin" maxlength="9" class="brd w35" />
	  </span>
	
	  <span class="pl10">
	    <label for="rowsCount">Стъпка</label>
	    <input type="text" value="${rowsCount }" name="rowsCount" maxlength="3" class="brd w35" />
	  </span>
	  
	  <span class="pl10">
	  	<a title="Изчисти филтрите" 
	  		onclick="javascript:clearFilters($('${formName}'),$('${tablePrefix}main_table'));" 
	  		href="javascript:void(0);">изчисти филтрите</a>
	  </span>
	  
	
		<span class="pl10">
	    <a title="Презареди" onclick="javascript:doRefresh(document.${formName},$('${tablePrefix}main_table'));" href="javascript:void(0);"><img alt="refresh" src="${pathPrefix }/img/icon_refresh.png" /></a>
	  </span>
	
	  <span class="pl10">
	    <a title="Първа страница" onclick="javascript:goFirst(document.${formName},$('${tablePrefix}main_table'));" href="javascript:void(0);"><img alt="first page" src="${pathPrefix }/img/first-page.png" /></a>
	    <a title="Предишна страница" onclick="javascript:goPrevious(document.${formName},$('${tablePrefix}main_table'));" href="javascript:void(0);"><img alt="previous page" src="${pathPrefix }/img/previous-page.png" /></a>
	    <a title="Следваща страница" onclick="javascript:goNext(document.${formName},$('${tablePrefix}main_table'));" href="javascript:void(0);"><img alt="next page" src="${pathPrefix }/img/next-page.png" /></a>
	    <a title="Последна страница" onclick="javascript:goLast(document.${formName},$('${tablePrefix}main_table'));" href="javascript:void(0);"><img alt="last page" src="${pathPrefix }/img/last-page.png" /></a>
	  </span>
	</fieldset>
	<div class="clr20">
	<!--  -->
	</div>
	</form>
	<hr class="none" />
</nacid:table>