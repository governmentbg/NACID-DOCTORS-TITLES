<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ attribute name="tablePrefix" required="false" rtexprvalue="true"%>
<fieldset class="nav_row ll"><legend onclick="$('${tablePrefix}table_columns').toggle()">${messages.tableFilterColumns }</legend>
	<div class="flt_lft" id="${tablePrefix}table_columns">
	<nacid:tablemodifiablecolumns>
		<p>
		<label for="${tablePrefix}column_name_row_id_${rowId}">${columnName}</label>
		<input id="${tablePrefix}column_name_row_id_${rowId}" type="checkbox" value="1" ${selected ? "checked" : ""} onclick="if (this.checked) {$(this).next().setAttribute('disabled', 'disabled');} else {$(this).next().removeAttribute('disabled'); }"/>
			<input type="hidden" id="${tablePrefix}column_name_row_id_${rowId}_hidden" name="${tablePrefix}HIDE_COLUMN_NAME" value="${columnName}" ${selected ? "disabled" : ""} />
		</p>
	</nacid:tablemodifiablecolumns>
	</div>
</fieldset>