<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ include file="commission_calendar_header.jsp"%>

<h3 class="title"><span>Обработка</span></h3>
<h3 class="names">${commission_calendar_header }</h3>
<div class="clr20"><!--  --></div>
<p class="cc">
	<input class="back" type="button"
		onclick="document.location.href='${pathPrefix }/control/commission_calendar/list?reload=1';"
		value="Назад" />
</p>
<nacid:commissionCalendarProtocolEdit>
<p class="flt_rgt"><a target="_blank" href="${pathPrefix }/control/comission_calendar_print_report/comission_calendar_report.xls?calendar_id=${calendarId}">Генерирай xls report</a></p>
<div class="clr"><!--  --></div>

<form action="${pathPrefix }/control/calendar_session_protocol/save"
		method="post" name="form_protocol"
		onsubmit="startProgressUpdater();" enctype="multipart/form-data">
	

		
		<c:if test="${(operationView && hasDownload) || !operationView}">
			<fieldset><legend>Протокол от заседанието</legend>
				<nacid:systemmessage name="protocolMsg" />
		
				<input id="generate" type="hidden" name="generate" value="0" />
				<input id="calendarId" type="hidden" name="calendarId" value="${calendarId }" />
				<input id="operation" type="hidden" name="operation" value="${operation }" />
				
				<c:if test="${!operationView }" >
					<p><span class="fieldlabel"><label for="doc_content">Файл</label><br /></span>
						<input class="brd w240" name="doc_content" id="doc_content" type="file" value="" />&nbsp;&nbsp;${fileName }
					</p>
				</c:if>
				<div class="clr"><!--  --></div>
		
				<p class="cc">
					<c:if test="${hasDownload }">
						<a class="download" id="downloadLink" 
							href="${pathPrefix }/control/calendar_session_protocol/${fileName }?calendarId=${calendarId }"
							target="_blank" >
							Свали
						</a>
					</c:if>
					<c:if test="${!operationView }" >
						<c:if test="${!hasDownload }" >
							<input class="generate" type="submit" value="Генерирай"
								onclick="$('generate').value = 1;" />
						</c:if>
						<input class="save" type="submit" value="Запис на файла" />
					</c:if>
					
				</p>
			</fieldset>
		</c:if>
	<nacid:loadingBar />
</form>
</nacid:commissionCalendarProtocolEdit>
<div class="formClass">
	<fieldset ><legend class="noForm">Заявления</legend>
	
		<nacid:list />
		<script type="text/javascript">
			document.tableForm1.action = '${pathPrefix }/control/comission_calendar_process/${urlEnd }';
			<%--document.tableForm2.action = '${pathPrefix }/control/comission_calendar_process/${urlEnd }';--%>
		</script>
		
		
		<%-- //Маха се масовата промяна на статуси, но се оставя в кода, защото след време може да се наложи да се върне!
		<c:if test="${!operationView }" >
		<form action="${pathPrefix }/control/comission_calendar_process/save"
			method="post" name="form_prcess">
			<fieldset><legend>Промяна на статусите на маркираните заявления</legend>
				<nacid:systemmessage name="statusChangeMsg" />
				<input type="hidden" name="calendarId" value="${calendarId }" />
				<input type="hidden" id="selectedApplications" name="selectedApplications" value="" />
				<p>
					<span class="fieldlabel2"><label for="newAppStatus">Нов статус</label></span> 
					<nacid:combobox id="newAppStatus" name="newAppStatus" 
						attributeName="applicationStatusChangeCombo" />
				</p>
				<div class="clr"><!--  --></div>
			
				<p class="cc">
					<input class="save" type="submit" value="Промени статус" onclick="if (prefillselectedIds('selectedApplications')) return true;"/>
				</p>
			</fieldset>
		</form>
		</c:if>
		 --%>
		<c:if test="${!operationView }" >
			<form action="${pathPrefix }/control/comission_calendar_print_certs/certificates_date.doc"
				method="post" name="form_process_print_certs" id="form_process_print_certs"
				onsubmit="this.action=this.action.replace('date', currentDateString()); prefillselectedIds('selectedApplicationsPrint');" >
			
			<fieldset><legend>Печат на удостоверения за маркираните заявления</legend>
				<nacid:systemmessage name="printMsgcerts" />
				<input type="hidden" name="calendarId" value="${calendarId }" />
				<input type="hidden" name="type" value="certs" />
				<input type="hidden" id="selectedApplicationsPrint" name="selectedApplicationsPrint" value="" />
				<p class="cc">
					<input class="generate w240" type="submit" value="Печат на удостоверения" />
				</p>
			</fieldset>
			</form>
		</c:if>
		<%-- 
		<c:if test="${!operationView }" >
            <form action="${pathPrefix }/control/comission_calendar_print_certs/refuses_date.doc"
                method="post" name="form_process_print_refuses" id="form_process_print_refuses"
                onsubmit="this.action=this.action.replace('date', currentDateString()); prefillselectedIds('refusedApplications');" >
            
            <fieldset><legend>Генериране на решения за отказ</legend>
                <nacid:systemmessage name="printMsgrefused" />
                <input type="hidden" name="calendarId" value="${calendarId }" />
                <input type="hidden" name="type" value="refused" />
                <input type="hidden" id="refusedApplications" name="selectedApplicationsPrint" value="" />
                <p class="cc">
                    <input class="generate w270" type="submit" value="Печат на решения за отказ" />
                </p>
            </fieldset>
            </form>
        </c:if>
        --%>
        <c:if test="${!operationView }" >
            <form action="${pathPrefix }/control/comission_calendar_print_certs/postpones_date.doc"
                method="post" name="form_process_print_postpones" id="form_process_print_postpones"
                onsubmit="this.action=this.action.replace('date', currentDateString()); prefillselectedIds('postponedApplications');" >
            
            <fieldset><legend>Печат на писма за информация за маркираните записи</legend>
                <nacid:systemmessage name="printMsgpostponed" />
                <input type="hidden" name="calendarId" value="${calendarId }" />
                <input type="hidden" name="type" value="postponed" />
                <input type="hidden" id="postponedApplications" name="selectedApplicationsPrint" value="" />
                <p class="cc">
                    <input class="generate w310" type="submit" value="Печат на писма за информация" />
                </p>
            </fieldset>
            </form>
        </c:if>
		
		<script type="text/javascript">
			function prefillselectedIds(targetId) {
				
				$$("#main_table input[type=checkbox]").each(function (element) {
					  var elvalue = parseInt(element.up(1).down(1).innerHTML);
					  if (element.checked && !isNaN(elvalue)) {
					  $(targetId).value = addElementIdToInput($(targetId).value, elvalue); 
				  } 
				});
				return true;
				
			}

			function currentDateString() {
				var d = new Date();
				var day = d.getDate() + '';
				if(day.length == 1) {
					day = '0' + day;
				}
				var month = (d.getMonth()+1) + '';
				if(month.length == 1) {
					month = '0' + month;
				}
				return day + '.' + month + '.' + d.getFullYear();
			}
		</script>
		
	</fieldset>
</div>
<div class="clr20"><!--  --></div>
<p class="cc">
	<input class="back" type="button"
		onclick="document.location.href='${pathPrefix }/control/commission_calendar/list?reload=1';"
		value="Назад" />
</p>

<%@ include file="commission_calendar_footer.jsp"%>