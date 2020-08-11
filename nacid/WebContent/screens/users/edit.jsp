<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="/screens/header.jsp" %>
<%@ taglib uri="/META-INF/nacid_taglib.tld" prefix="nacid" %>
<%@ taglib uri="/META-INF/validation.tld" prefix="v" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<h3 class="title"><span>${operationStringForScreens } потребител</span></h3>
<nacid:systemmessage/>
<v:form name="usersForm" action="${pathPrefix }/control/users/save"
        method="post"
        backurl="${pathPrefix }/control/users/list?getLastTableState=1">
    <nacid:userEdit>
        <c:choose>
            <c:when test="${id == 0}">
                <v:comboBoxValidator input="id" required="true" />
                <nacid:combobox id="id" name="id" attributeName="externalUsers" style="brd w500"/>
            </c:when>
            <c:otherwise>
                <input type="hidden" name="id" id="id" value="${id }"/>
                Потребител: ${username}<br />
                Име: ${user.fullName}<br />
                email: ${user.email}<br />
            </c:otherwise>
        </c:choose>
        <nacid:userGroupMembership>
            <fieldset>
                <legend>Права - ${application_name }</legend>
                <table width="100%" cellspacing="0" cellpadding="1" border="1"
                       id="main_table" class="sort-table">
                    <thead>
                    <tr>
                        <td class="dark">Група\Операция</td>
                        <nacid:userGroupMembershipColHeaders>
                            <td class="dark">${columnname }</td>
                        </nacid:userGroupMembershipColHeaders>
                    </tr>
                    </thead>
                    <nacid:userGroupMembershipRow>
                        <tr>
                            <td>${name }</td>
                            <nacid:userGroupMembershipCol>
                                <td><input name="app${application_id }operation_${group_id }"
                                           value="${operation_id }" type="checkbox" ${checked } /></td>
                            </nacid:userGroupMembershipCol>
                        </tr>
                    </nacid:userGroupMembershipRow>
                </table>
            </fieldset>
            <div class="clr10"><!--  --></div>
        </nacid:userGroupMembership>
    </nacid:userEdit>
</v:form>
<%@ include file="/screens/footer.jsp" %>