<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="attribute" required="false" rtexprvalue="false"%>
<%@ attribute name="tablePrefix" required="false" rtexprvalue="true"%>
<%@ attribute name="skipFilters" required="false" rtexprvalue="false"%>
<%@ attribute name="elementClass" required="false" rtexprvalue="false"%>
<div id="${tablePrefix}table_id">
<nacid:table attribute="${attribute }">
	<c:if test="${tableName != null}">
		<h3 class="title"><span>${tableName }</span></h3>
	</c:if>
	<nacid:systemmessage />
	<c:if test="${skipFilters != true}">
		<nacid:tablemanipulators formName="${tablePrefix}tableForm1" addFilters="true" tablePrefix="${tablePrefix }" attribute="${attribute }" />
	</c:if>
	<table width="100%" cellspacing="0" cellpadding="1" border="1" id="${tablePrefix}main_table" class="sort-table ${elementClass }" >
		<thead>
			<tr>
				<td class="dark" width="20">No</td>
				<nacid:tableHeader>
					<td class="dark" ${col_style }>
					<c:choose>
						<c:when test="${skipFilters != true}">
							<a href="#" onclick="javascript:changeOrder(document.${tablePrefix}tableForm1,this,$('${tablePrefix}main_table'));return false;" title="Сортиране по ${columnName}">${columnName}</a>${orderColumn }
						</c:when>
						<c:otherwise>
							${columnName}
						</c:otherwise>
					</c:choose>
					</td>
				</nacid:tableHeader>
				<c:if test="${skipFilters != true}">
					<td class="dark" width="40" align="center"><input type="checkbox"
						onclick="toggleChecked($('${tablePrefix}main_table'), this);" /></td>
				</c:if>
				<c:if test="${hasOperationsColumn}">
					<td class="dark" width="60">&nbsp;</td>
				</c:if>
			</tr>
		</thead>
		<nacid:tableEmptyRow row="begin" additionalCols="${hasOperationsColumn ? 3 : 2}">${content }</nacid:tableEmptyRow>
		<nacid:tableRow>
			<tr id="${tablePrefix}row_${rowId }" onmouseover="$(this).addClassName('gray_bg')"
				onmouseout="$(this).removeClassName('gray_bg')" class="${row_class }">
				<td>${currentRowNumber }</td>
				<nacid:tableCol>
					<td ${col_style} <c:if test="${not empty colTitle}">title="<c:out value="${colTitle}" escapeXml="true" />"</c:if>>${colValue }</td>
				</nacid:tableCol>
				<c:if test="${skipFilters != true}">
					<td align="center"><input type="checkbox" name="rowid_${rowId }" ${rowChecked}/></td>
				</c:if>
				<c:if test="${hasOperationsColumn}">
					<td>
                        <nacid:tableOperationAccess operationName="view">
                            <a href="${pathPrefix }/control/${groupName }/view?id=${uniqueRowValue }${viewRequestParams }" title="Преглед"${viewLinkTarget }>
                                <img src="${pathPrefix }/img/icon_view.png" />
                            </a>
					    </nacid:tableOperationAccess>
                        <nacid:tableOperationAccess operationName="edit">
						    <a href="${pathPrefix }/control/${groupName }/edit?id=${uniqueRowValue }${editRequestParams }" title="Редактиране">
                                <img src="${pathPrefix }/img/icon_edit.png" />
                            </a>
					    </nacid:tableOperationAccess>
                        <nacid:tableOperationAccess operationName="delete">
						    <a href="${pathPrefix }/control/${groupName }/delete?id=${uniqueRowValue }&amp;getLastTableState=1${deleteRequestParams }" title="Изтриване" onclick="if (!confirm('Сигурни ли сте да че желаете да изтриете записа?')) return false;">
                                <img src="${pathPrefix }/img/icon_delete.png" />
						    </a>
					    </nacid:tableOperationAccess>
                    </td>
				</c:if>
			</tr>
		</nacid:tableRow>
		<nacid:tableEmptyRow row="end" additionalCols="${hasOperationsColumn ? 3 : 2}">${content }</nacid:tableEmptyRow>
	</table>
	<%-- <c:if test="${skipFilters != true}">
		<nacid:tablemanipulators formName="${tablePrefix}tableForm2" tablePrefix="${tablePrefix }" attribute="${attribute }" />
	</c:if> --%>
</nacid:table></div>