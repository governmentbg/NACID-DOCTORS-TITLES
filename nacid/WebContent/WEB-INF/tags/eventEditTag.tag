<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

		<fieldset><legend>Напомняне</legend>
			
			<c:if test="${docflownum != null }" >
				<p><span class="fieldlabel"><label>Деловоден номер на заявлението:</label><br /></span> 
					${docflownum }
				</p>
				<div class="clr"><!--  --></div>
			</c:if>
			<c:if test="${doctype != null }" >
				<p><span class="fieldlabel"><label>Тип документ:</label><br /></span> 
					${doctype }
				</p>
				<div class="clr"><!--  --></div>
			</c:if>
		
			<c:if test="${hasEventDates}" >
				<p><span class="fieldlabel"><label>Дата за напомняне:</label><br /></span> 
					${eventReminderDate }
				</p>
				<div class="clr"><!--  --></div>
				
				<p><span class="fieldlabel"><label>Крайна дата:</label><br /></span> 
					${eventDueDate }
				</p>
				<div class="clr"><!--  --></div>
			</c:if>
			
			<p><span class="fieldlabel"><label for="eventType">Тип</label><br /></span> 
				<nacid:combobox id="eventType" name="eventType"
						attributeName="eventType" onchange="initEventStatus();"/>
			</p>
			<div class="clr"><!--  --></div>
			
			<p id="eventStatusP">
				<c:if test="${eventStatus != null }" >
					<span class="fieldlabel"><label for="eventStatus">Статус</label><br /></span> 
					<nacid:combobox id="eventStatus" name="eventStatus"
							attributeName="eventStatus" />
				</c:if>
			</p>
			<div class="clr"><!--  --></div>
			<script type="text/javascript">
				function initEventStatus() {
					if($('eventType').selectedIndex == 0) {
						$('eventStatusP').hide();
					}
					else {
						$('eventStatusP').show();
					}
				}
				initEventStatus();
			</script>
		</fieldset>

