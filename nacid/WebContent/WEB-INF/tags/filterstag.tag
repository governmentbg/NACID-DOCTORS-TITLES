<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<nacid:tablefilters>
	<c:forEach var="item" items="${hiddenFiltersList}">
		<input type="hidden" value="${item.value }" name="${item.name }" />
	</c:forEach>
	<c:if test="${not empty filtersList}">
		<fieldset class="nav_row ll"><legend>${messages.tablefilter }</legend>
		  <div class="flt_rgt">
			<c:forEach var="item" items="${filtersList}">
				<div class="filterElement" style="padding-botton:15px;">
					<label for="${item.name }">
						<c:out value="${item.label }" escapeXml="false" />
					</label>
					<c:if test="${item.labelOnTop}">
						<br />
					</c:if>
					<c:if test="${item.type == 'checkBox'}">
						<input type="checkbox" value="1" name="${item.name }"
								<c:out value="${item.value }"  escapeXml="false" />
								<c:out value="${item.elementClass }"  escapeXml="false" /> />
					</c:if>
					<c:if test="${item.type == 'comboBox'}">
						<select name="${item.name}" <c:out value="${item.elementClass }" escapeXml="false"/>>
							<c:out value="${item.value}" escapeXml="false" />
						</select>
					</c:if>
					<c:if test="${item.type == 'textField'}">
						<input type="text" value="${item.value }" name="${item.name }"
								<c:out value="${item.elementClass }"  escapeXml="false" /> />
					</c:if>
					<c:if test="${item.type == 'complexCombo' }">
						<input id="${item.name }Ids" type="hidden" name="${item.name }Ids" value="${item.selectedItemIds }" />
						<select name="${item.name}" <c:out value="${item.elementClass }" escapeXml="false"/> >
							<c:out value="${item.value}" escapeXml="false" />
						</select><a href="#" onclick="addComboFilterElement('${item.name}'); return false;">Добави</a>
						<div class="clr10"><!--  --></div>
						<div id="${item.name }selectedDiv">
							<table id="${item.name }selectedTable" >
								<tbody>
								<c:forEach var="selectedItem" items="${item.selectedItems}">
									<tr id="${item.name }Row${selectedItem.key }" class="tdfontsize100">
										<td>${selectedItem.value }</td>
										<td><a href="#" onclick="removeComboFilterItem('${item.name }', '${selectedItem.key }'); return false;" ><img src="${pathPrefix}/img/icon_delete.png" /></a></td>
									</tr>
								</c:forEach>
								</tbody>
							</table>
						</div>
						<script type="text/javascript">
							showHideComboFilterDiv('${item.name }');
						</script>
					</c:if>
				</div>
			</c:forEach>
		  </div>
	    </fieldset>
	</c:if>
</nacid:tablefilters>